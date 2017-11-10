package com.bizo.dtonator.dtos;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.MultiValuedMap;
import org.junit.Test;

import com.bizo.dtonator.domain.AbstractManager;
import com.bizo.dtonator.properties.GenericParser;

public class MethodReturnTypeTest {

  @Test
  public void testTypeDtoMapToString() {
    for (Method method : AbstractManager.class.getDeclaredMethods()) {
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
    for (Method method : AbstractManager.class.getDeclaredMethods()) {
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
    for (Method method : AbstractManager.class.getDeclaredMethods()) {
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
