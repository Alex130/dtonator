package com.bizo.dtonator.properties;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

public class GenericParser {

  public static String typeToString(Type type) {
    StringBuilder sb = new StringBuilder();
    typeToString(sb, type, new HashSet<Type>());
    return sb.toString();
  }

  private static void typeToString(StringBuilder sb, Type type, Set<Type> visited) {
    if (type instanceof ParameterizedType) {
      ParameterizedType parameterizedType = (ParameterizedType) type;
      final Class<?> rawType = (Class<?>) parameterizedType.getRawType();
      sb.append(rawType.getName());
      boolean first = true;
      for (Type typeArg : parameterizedType.getActualTypeArguments()) {
        if (first) {
          first = false;
        } else {
          sb.append(", ");
        }
        sb.append('<');
        typeToString(sb, typeArg, visited);
        sb.append('>');
      }
    } else if (type instanceof WildcardType) {
      WildcardType wildcardType = (WildcardType) type;
      sb.append("?");

      /*
       *  According to JLS(http://java.sun.com/docs/books/jls/third_edition/html/typesValues.html#4.5.1):
       *  - Lower and upper can't coexist: (for instance, this is not allowed: <? extends List<String> & super MyInterface>)
       *  - Multiple bounds are not supported (for instance, this is not allowed: <? extends List<String> & MyInterface>)
       */
      final Type bound;
      if (wildcardType.getLowerBounds().length != 0) {
        sb.append(" super ");
        bound = wildcardType.getLowerBounds()[0];
      } else {
        sb.append(" extends ");
        bound = wildcardType.getUpperBounds()[0];
      }
      typeToString(sb, bound, visited);
    } else if (type instanceof TypeVariable<?>) {
      TypeVariable<?> typeVariable = (TypeVariable<?>) type;
      sb.append(typeVariable.getName());
      /*
       * Prevent cycles in case: <T extends List<T>>
       */
      if (!visited.contains(type)) {
        visited.add(type);
        sb.append(" extends ");
        boolean first = true;
        for (Type bound : typeVariable.getBounds()) {
          if (first) {
            first = false;
          } else {
            sb.append(" & ");
          }
          typeToString(sb, bound, visited);
        }
        visited.remove(type);
      }
    } else if (type instanceof GenericArrayType) {
      GenericArrayType genericArrayType = (GenericArrayType) type;
      typeToString(genericArrayType.getGenericComponentType());
      sb.append(genericArrayType.getGenericComponentType());
      sb.append("[]");
    } else if (type instanceof Class) {
      Class<?> typeClass = (Class<?>) type;
      sb.append(typeClass.getName());
    } else {
      throw new IllegalArgumentException("Unsupported type: " + type);
    }
  }

  public static String typeToMapString(Type type) {
    List<GenericParts> parts = new ArrayList<>();

    typeToMap(parts, 0, type, new HashSet<Type>());

    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < parts.size(); i++) {
      GenericParts gp = parts.get(i);
      if (i > 0) {
        sb.append(", ");
      }
      typeToMapString(gp, sb);
    }

    return sb.toString();
  }

  private static void typeToMapString(GenericParts gp, StringBuilder sb) {
    sb.append(gp.getParamTypeString());
    if (!gp.getParamTypeString().isEmpty()) {
      sb.append("<");
    }

    if (gp.wildType != null) {
      sb.append("?");
    } else if (gp.typeVar != null) {
      sb.append(gp.getTypeVarString());
    } else if (gp.arrayType != null) {
      sb.append(gp.getArrayTypeString()).append("[]");
    }

    if (gp.operator != null) {
      sb.append(" " + gp.operator + " ");
    }

    if (gp.linkedTypes != null) {
      for (int j = 0; j < gp.linkedTypes.size(); j++) {
        if (j > 0) {
          sb.append(" & ");
        }
        if (gp.linkedTypes.get(j) instanceof GenericParts) {
          typeToMapString((GenericParts) gp.linkedTypes.get(j), sb);
        } else {
          sb.append(gp.linkedTypes.get(j));
        }
      }
    }

    if (!gp.getParamTypeString().isEmpty()) {
      sb.append(">");
    }

  }

  public static List<GenericParts> typeToMap(Type type) {
    List<GenericParts> parts = new ArrayList<>();

    typeToMap(parts, 0, type, new HashSet<Type>());
    return parts;
  }

  private static void typeToMap(List<GenericParts> parts, int index, Type type, Set<Type> visited) {

    if (parts == null) {
      parts = new ArrayList<GenericParts>();
      index = 0;
    }

    if (type instanceof ParameterizedType) {
      ParameterizedType parameterizedType = (ParameterizedType) type;

      for (int i = 0; i < parameterizedType.getActualTypeArguments().length; i++) {
        Type typeArg = parameterizedType.getActualTypeArguments()[i];

        GenericParts gp = new GenericParts();
        gp.paramType = parameterizedType;
        parts.add(gp);
        typeToMap(parts, i, typeArg, visited);

      }
    } else if (type instanceof WildcardType) {

      GenericParts gp = getGpFromParts(parts, index);

      WildcardType wildcardType = (WildcardType) type;
      gp.wildType = wildcardType;

      /*
       *  According to JLS(http://java.sun.com/docs/books/jls/third_edition/html/typesValues.html#4.5.1):
       *  - Lower and upper can't coexist: (for instance, this is not allowed: <? extends List<String> & super MyInterface>)
       *  - Multiple bounds are not supported (for instance, this is not allowed: <? extends List<String> & MyInterface>)
       */
      final Type bound;
      if (wildcardType.getLowerBounds().length != 0) {
        gp.operator = "super";
        bound = wildcardType.getLowerBounds()[0];
      } else {
        gp.operator = "extends";
        bound = wildcardType.getUpperBounds()[0];
      }
      typeToMap(parts, index, bound, visited);
    } else if (type instanceof TypeVariable<?>) {

      GenericParts gp = getGpFromParts(parts, index);

      TypeVariable<?> typeVariable = (TypeVariable<?>) type;
      gp.typeVar = typeVariable;
      /*
       * Prevent cycles in case: <T extends List<T>>
       */
      if (!visited.contains(type)) {
        visited.add(type);
        gp.operator = "extends";
        List<GenericParts> typeParts = new ArrayList<GenericParts>();
        for (int i = 0; i < typeVariable.getBounds().length; i++) {
          Type bound = typeVariable.getBounds()[i];
          typeToMap(typeParts, i, bound, visited);
        }
        gp.linkedTypes = typeParts;
        visited.remove(type);
      }
    } else if (type instanceof GenericArrayType) {
      GenericParts gp = getGpFromParts(parts, index);

      GenericArrayType genericArrayType = (GenericArrayType) type;
      typeToMap(genericArrayType.getGenericComponentType());
      gp.arrayType = genericArrayType;

    } else if (type instanceof Class) {
      GenericParts gp = getGpFromParts(parts, index);

      Class<?> typeClass = (Class<?>) type;
      if (gp.linkedTypes == null) {
        gp.linkedTypes = new ArrayList<>();
      }
      gp.linkedTypes.add(typeClass.getName());
    } else {
      throw new IllegalArgumentException("Unsupported type: " + type);
    }
  }

  private static GenericParts getGpFromParts(List<GenericParts> parts, int index) {
    GenericParts gp = null;
    if (index < parts.size()) {
      gp = parts.get(index);
    }
    if (gp == null) {
      gp = new GenericParts();
      parts.add(gp);
    }

    return gp;
  }

}
