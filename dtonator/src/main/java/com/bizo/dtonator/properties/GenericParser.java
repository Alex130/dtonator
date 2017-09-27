package com.bizo.dtonator.properties;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.MapIterator;
import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
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
    MultiValuedMap<String, GenericParts> partsMap = new ArrayListValuedHashMap<>();

    typeToMap(partsMap, type);

    StringBuilder sb = new StringBuilder();

    typeToMapString(partsMap, sb);

    return sb.toString();
  }

  public static void typeToMapString(MultiValuedMap<String, ?> partsMap, StringBuilder sb) {

    Iterator<String> itr = partsMap.keySet().iterator();
    while (itr.hasNext()) {
      String key = itr.next();

      List<GenericParts> parts = (List<GenericParts>) partsMap.get(key);

      for (int i = 0; i < parts.size(); i++) {

        if (i > 0) {
          sb.append(", ");
          sb.append(parts.get(i).getFormattedString(""));
        } else {
          sb.append(parts.get(i).getFormattedString());
        }

      }
      if (itr.hasNext()) {
        sb.append(", ");

      }
    }

  }

  public static MultiValuedMap<String, GenericParts> typeToMap(Type type) {
    MultiValuedMap<String, GenericParts> parts = new ArrayListValuedHashMap<>();

    typeToMap(parts, type);
    return parts;
  }

  private static void typeToMap(MultiValuedMap<String, GenericParts> parts, Type type) {
    typeToMap(parts, "default", 0, type, new HashSet<Type>());
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  private static void typeToMap(MultiValuedMap<String, GenericParts> parts, String key, int index, Type type, Set<Type> visited) {

    if (parts == null) {

      parts = new ArrayListValuedHashMap<String, GenericParts>();
      index = 0;
      key = "default";
    }

    if (type instanceof ParameterizedType) {
      ParameterizedType parameterizedType = (ParameterizedType) type;

      for (int i = 0; i < parameterizedType.getActualTypeArguments().length; i++) {
        Type typeArg = parameterizedType.getActualTypeArguments()[i];

        GenericParts gp = new GenericParts();
        gp.paramType = parameterizedType;
        parts.put(gp.getParamTypeString(), gp);
        typeToMap(parts, gp.getParamTypeString(), i, typeArg, visited);

      }
    } else if (type instanceof WildcardType) {

      GenericParts gp = getGpFromParts(parts, key, index);

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
      typeToMap(parts, key, index, bound, visited);
    } else if (type instanceof TypeVariable<?>) {

      GenericParts gp = getGpFromParts(parts, key, index);

      TypeVariable<?> typeVariable = (TypeVariable<?>) type;
      gp.typeVar = typeVariable;
      /*
       * Prevent cycles in case: <T extends List<T>>
       */
      if (!visited.contains(type)) {
        visited.add(type);
        gp.operator = "extends";
        MultiValuedMap<String, GenericParts> typeParts = new ArrayListValuedHashMap<String, GenericParts>();
        for (int i = 0; i < typeVariable.getBounds().length; i++) {
          Type bound = typeVariable.getBounds()[i];
          typeToMap(typeParts, key, i, bound, visited);
        }
        gp.linkedTypes = typeParts;
        visited.remove(type);
      }
    } else if (type instanceof GenericArrayType) {
      GenericParts gp = getGpFromParts(parts, key, index);

      GenericArrayType genericArrayType = (GenericArrayType) type;
      typeToMap(genericArrayType.getGenericComponentType());
      gp.arrayType = genericArrayType;

    } else if (type instanceof Class) {
      GenericParts gp = getGpFromParts(parts, key, index);

      Class<?> typeClass = (Class<?>) type;
      gp.boundClass = typeClass;
    } else {
      throw new IllegalArgumentException("Unsupported type: " + type);
    }
  }

  @SuppressWarnings("rawtypes")
  private static GenericParts<?> getGpFromParts(MultiValuedMap<String, GenericParts> partsMap, String key, int index) {
    List<GenericParts> parts = (List<GenericParts>) partsMap.get(key);
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
