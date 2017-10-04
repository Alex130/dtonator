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

public class ClassTypeTest {
  private class ClassType<U extends Number, T extends U, V extends List<String> & Collection<String>, W extends Map<U, Set<String>>, X extends Map<List<Number>, Set<String>>> {
  }

  @Test
  public void testTypeDtoMapToString() {
    for (TypeVariable<Class<ClassType>> type : ClassType.class.getTypeParameters()) {

      String typeToString = type.getName() + " - " + GenericParser.typeToString(type);
      System.out.println(typeToString);
      String mapTypeToString = type.getName() + " - " + GenericParser.typeToMapStringDto(type, type.getName());
      System.out.println(mapTypeToString);
      System.out.println("");

      assertEquals(typeToString, mapTypeToString);
    }
  }

  @Test
  public void testTypeMapToString() {
    for (TypeVariable<Class<ClassType>> type : ClassType.class.getTypeParameters()) {

      String typeToString = type.getName() + " - " + GenericParser.typeToString(type);
      System.out.println(typeToString);
      String mapTypeToString = type.getName() + " - " + GenericParser.typeToMapString(type, type.getName());
      System.out.println(mapTypeToString);
      System.out.println("");

      assertEquals(typeToString, mapTypeToString);
    }
  }

  @Test
  public void testTypeToMapFromClass() {
    MultiValuedMap<String, GenericParts> classMap = GenericParser.typeToMap(ClassType.class);
    assertNotNull(classMap);
    assertEquals(5, classMap.values().size());
  }

  public static void main(String[] args) {
    for (TypeVariable<Class<ClassType>> type : ClassType.class.getTypeParameters()) {

      System.out.println(type.getName() + " - " + GenericParser.typeToString(type));

      System.out.println(type.getName() + " - " + GenericParser.typeToMapString(type, type.getName()));
      String mapTypeToString = type.getName() + " - " + GenericParser.typeToMapStringDto(type, type.getName());
      System.out.println(mapTypeToString);
      System.out.println("");

    }
  }
}
