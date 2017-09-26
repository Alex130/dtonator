package com.bizo.dtonator.properties;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class MethodReturnTypeTest {
  List mRaw() {
    return null;
  }

  List<String> mTypeString() {
    return null;
  }

  List<?> mWildcard() {
    return null;
  }

  List<? extends Number> mBoundedWildcard() {
    return null;
  }

  <T extends List<String>> List<T> mTypeLiteral() {
    return null;
  }

  <T extends List<String>, V extends Set<Integer>> Map<T, V> mTypeLiteralMultiple() {
    return null;
  }

  <T extends List<String>, V extends Map<Integer, T>> Map<T, V> mMapTypeLiteralMultiple() {
    return null;
  }

  <Q extends List & Set> Q multiType() {
    return null;
  }

  <T extends Set> T[] mGenericArray() {
    return null;
  }

  @Test
  public void testTypeMapToString() {
    for (Method method : MethodReturnTypeTest.class.getDeclaredMethods()) {
      Type type = method.getGenericReturnType();
      String typeToString = method.getName() + " - " + GenericParser.typeToString(type);
      System.out.println(typeToString);
      String mapTypeToString = method.getName() + " - " + GenericParser.typeToMapString(type);
      System.out.println(mapTypeToString);
      System.out.println("");

      assertEquals(typeToString, mapTypeToString);
    }
  }

  public static void main(String[] args) {
    for (Method method : MethodReturnTypeTest.class.getDeclaredMethods()) {
      Type type = method.getGenericReturnType();
      System.out.println(method.getName() + " - " + GenericParser.typeToString(type));
      List<GenericParts> parts = GenericParser.typeToMap(type);
      //      System.out.println(parts);
      System.out.println(method.getName() + " - " + GenericParser.typeToMapString(type));
      System.out.println("");
    }
  }
}
