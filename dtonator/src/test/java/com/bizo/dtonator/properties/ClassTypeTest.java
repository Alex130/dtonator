package com.bizo.dtonator.properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.MultiValuedMap;
import org.junit.Test;

public class ClassTypeTest<U extends Number, T extends U, V extends List<String> & Collection<String>, W extends Map<U, Set<String>>, X extends Map<List<Number>, Set<String>>> {

  @Test
  public void testTypeDtoMapToString() {
    for (TypeVariable<Class<ClassTypeTest>> type : ClassTypeTest.class.getTypeParameters()) {

      String typeToString = type.getName() + " - " + GenericParser.typeToString(type);
      System.out.println(typeToString);
      String mapTypeToString = type.getName() + " - " + GenericParser.typeToMapStringDto(type);
      System.out.println(mapTypeToString);
      System.out.println("");

      assertEquals(typeToString, mapTypeToString);
    }
  }

  @Test
  public void testTypeMapToString() {
    for (TypeVariable<Class<ClassTypeTest>> type : ClassTypeTest.class.getTypeParameters()) {

      String typeToString = type.getName() + " - " + GenericParser.typeToString(type);
      System.out.println(typeToString);
      String mapTypeToString = type.getName() + " - " + GenericParser.typeToMapString(type);
      System.out.println(mapTypeToString);
      System.out.println("");

      assertEquals(typeToString, mapTypeToString);
    }
  }

  public static void main(String[] args) {
    for (TypeVariable<Class<ClassTypeTest>> type : ClassTypeTest.class.getTypeParameters()) {

      System.out.println(type.getName() + " - " + GenericParser.typeToString(type));

      System.out.println(type.getName() + " - " + GenericParser.typeToMapString(type));
      String mapTypeToString = type.getName() + " - " + GenericParser.typeToMapStringDto(type);
      System.out.println(mapTypeToString);
      System.out.println("");

    }
  }
}
