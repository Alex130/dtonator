package com.bizo.dtonator.properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.MultiValuedMap;
import org.junit.Test;

public class MethodReturnTypeTest<U extends Number> {

  @SuppressWarnings("unused")
  private class MethodReturnType {
    public List mRaw() {
      return null;
    }

    public List<String> mTypeString() {
      return null;
    }

    public U mClassTypeNumber() {
      return null;
    }

    public List<U> mListClassTypeNumber() {
      return null;
    }

    public List<?> mWildcard() {
      return null;
    }

    public List<? extends Number> mBoundedWildcard() {
      return null;
    }

    public <T extends Set<String>> List<T> mTypeLiteral() {
      return null;
    }

    public <T extends Set<V>, V extends Number> T mNestedTypeLiteral() {
      return null;
    }

    public <T extends List<String>, V extends Set<Integer>> Map<T, V> mTypeLiteralMultiple() {
      return null;
    }

    //  public <T extends List<String>, V extends Map<Integer, T>> Map<T, V> mMapTypeLiteralMultiple() {
    //    return null;
    //  }

    public <Q extends List & Set> Q multiType() {
      return null;
    }

    public <T extends Set> T[] mGenericArray() {
      return null;
    }
  }

  @Test
  public void testFlattenGenericMap() {
    try {
      Method method = MethodReturnType.class.getMethod("mNestedTypeLiteral");

      MultiValuedMap<String, GenericParts> typeMap = GenericParser.typeToMap(method.getGenericReturnType(), method.getName());
      assertNotNull(typeMap);
      MultiValuedMap<String, GenericParts> flatMap = GenericParser.flattenGenericMap(typeMap);
      assertNotNull(flatMap);

      System.out.println(GenericParser.typeToMapString(flatMap));
      assertEquals(2, flatMap.keySet().size());

    } catch (NoSuchMethodException | SecurityException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      fail(e.getMessage());
    }
  }

  @Test
  public void testTypeDtoMapToString() {
    for (Method method : MethodReturnType.class.getDeclaredMethods()) {
      Type type = method.getGenericReturnType();
      String typeToString = method.getName() + " - " + GenericParser.typeToString(type);
      System.out.println(typeToString);
      String mapTypeToString = method.getName() + " - " + GenericParser.typeToMapStringDto(type, method.getName());
      System.out.println(mapTypeToString);
      System.out.println("");

      assertEquals(typeToString, mapTypeToString);
    }
  }

  @Test
  public void testTypeMapToString() {
    for (Method method : MethodReturnType.class.getDeclaredMethods()) {
      Type type = method.getGenericReturnType();
      String typeToString = method.getName() + " - " + GenericParser.typeToString(type);
      System.out.println(typeToString);
      String mapTypeToString = method.getName() + " - " + GenericParser.typeToMapString(type, method.getName());
      System.out.println(mapTypeToString);
      System.out.println("");

      assertEquals(typeToString, mapTypeToString);
    }
  }

  public static void main(String[] args) {
    for (Method method : MethodReturnTypeTest.MethodReturnType.class.getDeclaredMethods()) {
      if (Modifier.isPublic(method.getModifiers())) {
        Type type = method.getGenericReturnType();
        System.out.println(method.getName() + " - " + GenericParser.typeToString(type));

        System.out.println(method.getName() + " - " + GenericParser.typeToMapString(type, method.getName()));
        String mapTypeToString = method.getName() + " - " + GenericParser.typeToMapStringDto(type, method.getName());
        System.out.println(mapTypeToString);
        System.out.println("");
      }
    }
  }
}
