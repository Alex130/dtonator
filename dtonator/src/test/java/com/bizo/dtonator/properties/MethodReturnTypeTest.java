package com.bizo.dtonator.properties;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.MultiValuedMap;
import org.junit.Test;

public class MethodReturnTypeTest {
  public List mRaw() {
    return null;
  }

  public List<String> mTypeString() {
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

  @Test
  public void testTypeMapToString() {
    for (Method method : MethodReturnTypeTest.class.getDeclaredMethods()) {
      Type type = method.getGenericReturnType();
      String typeToString = method.getName() + " - " + GenericParser.typeToString(type);
      //      System.out.println(typeToString);
      String mapTypeToString = method.getName() + " - " + GenericParser.typeToMapString(type);
      //      System.out.println(mapTypeToString);
      //      System.out.println("");

      assertEquals(typeToString, mapTypeToString);
    }
  }

  public static void main(String[] args) {
    for (Method method : MethodReturnTypeTest.class.getDeclaredMethods()) {
      if (Modifier.isPublic(method.getModifiers())) {
        Type type = method.getGenericReturnType();
        System.out.println(method.getName() + " - " + GenericParser.typeToString(type));

        System.out.println(method.getName() + " - " + GenericParser.typeToMapString(type));
        MultiValuedMap<String, GenericParts> typeMap = GenericParser.typeToMap(type);

        System.out.println("");
      }
    }
  }
}
