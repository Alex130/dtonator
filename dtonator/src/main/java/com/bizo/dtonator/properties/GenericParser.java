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

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;

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
      sb.append('<');
      for (Type typeArg : parameterizedType.getActualTypeArguments()) {
        if (first) {
          first = false;
        } else {
          sb.append(", ");
        }

        typeToString(sb, typeArg, visited);

      }
      sb.append('>');
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

  public static String typeToMapString(Type type, String key) {
    MultiValuedMap<String, GenericParts> partsMap = new ArrayListValuedHashMap<>();

    typeToMap(partsMap, key, type);

    StringBuilder sb = new StringBuilder();

    typeToMapString(partsMap, sb);

    return sb.toString();
  }

  public static String typeToMapStringDto(Type type, String key) {
    MultiValuedMap<String, GenericParts> partsMap = new ArrayListValuedHashMap<>();

    typeToMap(partsMap, key, type);

    StringBuilder sb = new StringBuilder();

    MultiValuedMap<String, GenericPartsDto> partsDtoMap = convertGenericMap(partsMap);
    typeToMapString(partsDtoMap, sb);

    return sb.toString();
  }

  public static MultiValuedMap<String, GenericPartsDto> convertGenericMap(MultiValuedMap<String, GenericParts> partsMap) {
    MultiValuedMap<String, GenericPartsDto> partsDtoMap = new ArrayListValuedHashMap<>();

    for (String key : partsMap.keySet()) {
      List<GenericParts> parts = (List<GenericParts>) partsMap.get(key);
      if (parts != null) {
        for (GenericParts gp : parts) {

          partsDtoMap.put(key, new GenericPartsDto(gp));

        }
      }
    }

    return partsDtoMap;
  }

  public static <T extends IGenericParts> MultiValuedMap<String, T> flattenGenericMap(MultiValuedMap<String, T> partsMap) {
    MultiValuedMap<String, T> partsList = new ArrayListValuedHashMap<>();
    return flattenGenericMap(partsMap, partsList);
  }

  private static <T extends IGenericParts> MultiValuedMap<String, T> flattenGenericMap(
    MultiValuedMap<String, T> partsMap,
    MultiValuedMap<String, T> partsList) {

    if (partsMap != null) {
      for (String key : partsMap.keySet()) {
        List<T> parts = (List<T>) partsMap.get(key);
        if (parts != null) {
          for (T gp : parts) {

            if (gp.getTypeVarString() != null && !gp.getTypeVarString().isEmpty()) {
              String type = gp.getTypeVarString();
              partsList.put(type, gp);
              if (gp.getLinkedTypes() != null && !gp.getLinkedTypes().isEmpty()) {
                flattenGenericMap(gp.getLinkedTypes(), partsList);

              }

            } else if (gp.getParamTypeArgs() != null && !gp.getParamTypeArgs().isEmpty()) {
              flattenGenericMap(gp.getParamTypeArgs(), partsList);

            }

          }

        }
      }

      if (partsList.isEmpty()) {
        partsList.putAll(partsMap);
      }
    }
    return partsList;
  }

  public static <T extends IGenericParts> String typeToMapString(MultiValuedMap<String, T> partsMap) {

    StringBuilder sb = new StringBuilder();

    typeToMapString(partsMap, sb);

    return sb.toString();
  }

  private static <T extends IGenericParts> void typeToMapString(MultiValuedMap<String, T> partsMap, StringBuilder sb) {

    Iterator<String> itr = partsMap.keySet().iterator();
    while (itr.hasNext()) {
      String key = itr.next();

      List<IGenericParts> parts = (List<IGenericParts>) partsMap.get(key);

      for (int i = 0; i < parts.size(); i++) {

        if (i > 0) {
          sb.append(", ");

        }
        sb.append(parts.get(i).getFormattedString());

      }
      if (itr.hasNext()) {
        sb.append(", ");

      }
    }

  }

  public static MultiValuedMap<String, GenericParts> typeToMap(Type type, String key) {
    MultiValuedMap<String, GenericParts> parts = new ArrayListValuedHashMap<>();

    typeToMap(parts, key, type);
    return parts;
  }

  public static MultiValuedMap<String, GenericParts> typeToMap(Class clazz) {
    MultiValuedMap<String, GenericParts> parts = new ArrayListValuedHashMap<>();

    for (int i = 0; i < clazz.getTypeParameters().length; i++) {
      Type type = clazz.getTypeParameters()[i];
      typeToMap(parts, "default", i, type, new HashSet<Type>());
    }
    return parts;
  }

  private static void typeToMap(MultiValuedMap<String, GenericParts> parts, String key, Type type) {
    typeToMap(parts, key, 0, type, new HashSet<Type>());
  }

  private static void typeToMap(MultiValuedMap<String, GenericParts> parts, String key, int index, Type type, Set<Type> visited) {

    if (parts == null) {

      parts = new ArrayListValuedHashMap<String, GenericParts>();
      index = 0;

    }
    if (key == null) {
      key = "default";
    }

    if (type instanceof ParameterizedType) {
      ParameterizedType parameterizedType = (ParameterizedType) type;
      String typeName = ((Class<?>) parameterizedType.getRawType()).getName();

      MultiValuedMap<String, GenericParts> typeParts = new ArrayListValuedHashMap<String, GenericParts>();

      GenericParts gp = getGpFromParts(parts, key, index);

      gp.paramType = parameterizedType;
      typeName = gp.getParamTypeString();

      for (int i = 0; i < parameterizedType.getActualTypeArguments().length; i++) {
        Type typeArg = parameterizedType.getActualTypeArguments()[i];

        typeToMap(typeParts, typeName, i, typeArg, visited);
        gp.paramTypeArgs = typeParts;
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

      TypeVariable<?> typeVariable = (TypeVariable<?>) type;

      /*
       * Prevent cycles in case: <T extends List<T>>
       */
      if (!visited.contains(type)) {

        GenericParts gp = getGpFromParts(parts, key, index);
        gp.typeVar = typeVariable;
        visited.add(type);
        gp.operator = "extends";

        MultiValuedMap<String, GenericParts> typeParts = new ArrayListValuedHashMap<String, GenericParts>();
        for (int i = 0; i < typeVariable.getBounds().length; i++) {
          Type bound = typeVariable.getBounds()[i];
          typeToMap(typeParts, typeVariable.getName(), i, bound, visited);

          gp.linkedTypes = typeParts;
        }

        visited.remove(type);
      }
    } else if (type instanceof GenericArrayType) {
      GenericParts gp = getGpFromParts(parts, key, index);

      GenericArrayType genericArrayType = (GenericArrayType) type;
      typeToMap(genericArrayType.getGenericComponentType(), genericArrayType.getGenericComponentType().getTypeName());
      gp.arrayType = genericArrayType;

    } else if (type instanceof Class) {
      GenericParts gp = getGpFromParts(parts, key, index);

      Class<?> typeClass = (Class<?>) type;
      gp.boundClass = typeClass;
    } else {
      throw new IllegalArgumentException("Unsupported type: " + type);
    }
  }

  private static GenericParts getGpFromParts(MultiValuedMap<String, GenericParts> partsMap, String key, int index) {
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
