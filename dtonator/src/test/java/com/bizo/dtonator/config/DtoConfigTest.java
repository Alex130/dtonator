package com.bizo.dtonator.config;

import static joist.util.Copy.list;
import static joist.util.Copy.map;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.bizo.dtonator.properties.StubTypeOracle;

public class DtoConfigTest {

  private final StubTypeOracle oracle = new StubTypeOracle();
  private final Map<String, Object> root = new HashMap<String, Object>();
  private final RootConfig rootConfig = new RootConfig(oracle, root);
  private final Map<String, Object> config = new HashMap<String, Object>();

  @Before
  public void setupRootConfig() {
    config.put("domainPackage", "com.domain, com.other");
    config.put("dtoPackage", "com.dto");
    root.put("config", config);
  }

  @Test
  public void testAllProperties() {
    // given two properties
    oracle.addProperty("com.domain.Foo", "a", "java.lang.String");
    oracle.addProperty("com.domain.Foo", "b", "java.lang.String");
    // and no overrides
    addDto("FooDto", domain("Foo"), properties("*"));
    // then we have both
    final DtoConfig dc = rootConfig.getDto("FooDto");
    assertThat(dc.getAllProperties().size(), is(2));
  }

  @Test
  public void testAllPropertiesForChild() {
    // given two properties
    oracle.addProperty("com.domain.Foo", "a", "java.lang.String");
    oracle.addProperty("com.domain.Foo", "b", "java.lang.String");
    oracle.addProperty("com.domain.Bar", "c", "java.lang.String");
    // and no overrides
    addDto("FooDto", domain("Foo"), properties("*"));
    addDto("BarDto", domain("Bar"), properties("*"), extendsDto("FooDto"));
    // then we have both
    final DtoConfig dc = rootConfig.getDto("BarDto");

    assertThat(dc.getAllProperties().size(), is(3));
  }

  @Test
  public void testAllMappedPropertiesForChild() {
    // given a parent and child
    oracle.addProperty("com.domain.Foo", "a", "java.lang.String");
    oracle.addProperty("com.domain.Foo", "b", "java.lang.String");
    oracle.addProperty("com.domain.Bar", "a", "java.lang.String", true, false);
    oracle.addProperty("com.domain.Bar", "b", "java.lang.String", true, false);
    oracle.addProperty("com.domain.Bar", "c", "java.lang.String");
    // and parent maps a property 'a'
    addDto("FooDto", domain("Foo"), properties("a"));
    addDto("BarDto", domain("Bar"), properties("*"), extendsDto("FooDto"));
    // then child should only include mapped properties from parent
    final DtoConfig dc = rootConfig.getDto("BarDto");
    for (DtoProperty dp : dc.getAllProperties()) {
      if (dp.getName().equals("b")) {
        fail("'b' was not mapped in the parent and should have been excluded.");
      }
    }
    assertThat(dc.getAllProperties().size(), is(2));
  }

  @Test
  public void testAllOverridePropertiesForChild() {
    // given a parent and child
    oracle.addProperty("com.domain.Foo", "a", "java.lang.Number");
    oracle.addProperty("com.domain.Foo", "b", "java.lang.String");
    oracle.addProperty("com.domain.Foo", "c", "java.lang.String");
    oracle.addProperty("com.domain.Bar", "a", "java.lang.Number", true, false);
    oracle.addProperty("com.domain.Bar", "b", "java.lang.String", true, false);
    oracle.addProperty("com.domain.Bar", "c", "java.lang.String", true, false);
    oracle.addProperty("com.domain.Bar", "d", "java.lang.Long");
    // and parent maps a property 'a'
    addDto("FooDto", domain("Foo"), properties("a"));
    addDto("BarDto", domain("Bar"), properties("a Integer, c, *"), extendsDto("FooDto"));
    // then child should only include mapped properties from parent
    // or properties that are explicitly mapped by the child.
    final DtoConfig dc = rootConfig.getDto("BarDto");
    for (DtoProperty dp : dc.getAllProperties()) {
      if (dp.getName().equals("b")) {
        fail("'b' was not mapped in the parent and should have been excluded.");
      } else if (dp.getName().equals("a")) {
        assertThat(dp.getDtoType(), is("java.lang.Integer"));
      }
    }
    assertThat(dc.getAllProperties().size(), is(3));
  }

  @Test
  public void testAllPropertiesForChildWithList() {
    // given two properties
    oracle.addProperty("com.domain.Foo", "a", "java.lang.String");
    oracle.addProperty("com.domain.Foo", "b", "List");
    oracle.addProperty("com.domain.Bar", "b", "List<java.lang.String>", true, false);
    oracle.addProperty("com.domain.Bar", "c", "java.lang.String");
    // and no overrides
    addDto("FooDto", domain("Foo"), properties("*"));
    addDto("BarDto", domain("Bar"), properties("*"), extendsDto("FooDto"));
    // then we have both
    final DtoConfig dc = rootConfig.getDto("BarDto");

    assertThat(dc.getAllProperties().size(), is(3));
    for (DtoProperty dp : dc.getAllProperties()) {
      if (dp.getName().equals("b")) {
        assertThat(dp.getDomainType(), is("List<java.lang.String>"));
      }
    }
  }

  @Test
  public void testAllPropertiesForChildWithGeneric() {
    // given three properties
    oracle.addClassType("com.domain.Foo", "T", "extends", "Number");
    oracle.addProperty("com.domain.Foo", "a", "java.lang.String");
    oracle.addProperty("com.domain.Foo", "b", "T", map("T", "Number"));
    oracle.addProperty("com.domain.Bar", "b", "Integer", true, false);
    oracle.addProperty("com.domain.Bar", "c", "java.lang.String");
    // and no overrides
    addDto("FooDto", domain("Foo"), properties("*"));
    addDto("BarDto", domain("Bar"), properties("*"), extendsDto("FooDto"));
    // then we have both

    DtoConfig dc = rootConfig.getDto("FooDto");

    for (DtoProperty dp : dc.getAllProperties()) {
      if (dp.getName().equals("b")) {
        assertThat(dp.getDomainType(), is("T"));
        assertThat(dp.getGenericDomainType(), is("Number"));
      }
    }

    dc = rootConfig.getDto("BarDto");

    for (DtoProperty dp : dc.getAllProperties()) {
      if (dp.getName().equals("b")) {
        assertThat(dp.getDomainType(), is("Integer"));
      }
    }

    assertThat(dc.getAllProperties().size(), is(3));
  }

  @Test
  public void testMappedPropertiesForChildWithGeneric() {
    // given three properties
    oracle.addClassType("com.domain.Foo", "T", "extends", "Number");
    oracle.addProperty("com.domain.Foo", "a", "java.lang.String");
    oracle.addProperty("com.domain.Foo", "b", "T", map("T", "Number"));
    oracle.addProperty("com.domain.Bar", "b", "Integer", true, false);
    oracle.addProperty("com.domain.Bar", "c", "java.lang.String");
    // and no overrides
    addDto("FooDto", domain("Foo"), properties("b T"));
    addDto("BarDto", domain("Bar"), properties("*"), extendsDto("FooDto"));
    // then we have both

    DtoConfig dc = rootConfig.getDto("FooDto");

    for (DtoProperty dp : dc.getAllProperties()) {
      if (dp.getName().equals("b")) {
        assertThat(dp.getDomainType(), is("T"));
        assertThat(dp.getGenericDtoType(), is("Number"));
      }
    }
    assertThat(dc.getAllProperties().size(), is(1));

    dc = rootConfig.getDto("BarDto");

    for (DtoProperty dp : dc.getAllProperties()) {
      if (dp.getName().equals("b")) {
        assertThat(dp.getDomainType(), is("Integer"));
      }
    }

    assertThat(dc.getAllProperties().size(), is(2));
  }

  @Test
  public void testAllMappedPropertiesForChildWithListOfEntities() {
    // given two properties
    oracle.addProperty("com.domain.Foo", "a", "java.lang.String");
    oracle.addProperty("com.domain.Foo", "b", "java.util.List<com.domain.Child>");
    oracle.addProperty("com.domain.Bar", "c", "java.lang.String");
    oracle.addProperty("com.domain.Child", "id", "java.lang.Integer");
    // and the child dto has an entry in the yaml file
    addDto("ChildDto");
    // and no overrides
    addDto("FooDto", domain("Foo"), properties("*"));
    addDto("BarDto", domain("Bar"), properties("*"), extendsDto("FooDto"));
    // then we have both
    final DtoConfig dc = rootConfig.getDto("BarDto");

    for (DtoProperty dp : dc.getAllProperties()) {
      assertThat(dp.getName(), not("b"));

    }
    assertThat(dc.getAllProperties().size(), is(2));
  }

  @Test
  public void testAbstractProperties() {
    // given two properties
    oracle.addProperty("com.domain.Foo", "a", "java.lang.String");
    oracle.addProperty("com.domain.Foo", "b", "java.lang.Long", false, true);

    // and no overrides
    addDto("FooDto", domain("Foo"), properties("*"));
    // then b should be marked as abstract and excluded
    final DtoConfig dc = rootConfig.getDto("FooDto");

    for (DtoProperty dp : dc.getAllProperties()) {

      assertThat(dp.getName(), not("b"));
      assertFalse(dp.isAbstract());

    }
    assertThat(dc.getAllProperties().size(), is(1));
  }

  @Test
  public void testAllPropertiesForChildWithAbstractParent() {
    // given two properties
    oracle.addProperty("com.domain.Foo", "a", "java.lang.String");
    oracle.addProperty("com.domain.Foo", "b", "java.lang.Long", false, true);
    oracle.addProperty("com.domain.Bar", "c", "java.lang.String");

    // and no overrides
    addDto("FooDto", domain("Foo"), properties("*"));
    addDto("BarDto", domain("Bar"), properties("*"), extendsDto("FooDto"));
    // then abstract properties should be excluded
    final DtoConfig dc = rootConfig.getDto("BarDto");

    for (DtoProperty dp : dc.getAllProperties()) {
      assertThat(dp.getName(), not("b"));

    }
    assertThat(dc.getAllProperties().size(), is(2));
  }

  @Test
  public void testAllMappedOverridesForChildWithListOfEntities() {
    // given two properties
    oracle.addProperty("com.domain.Foo", "a", "java.lang.String");
    oracle.addProperty("com.domain.Foo", "b", "java.util.List<com.domain.Child>");
    oracle.addProperty("com.domain.Bar", "c", "java.lang.String");
    oracle.addProperty("com.domain.Child", "id", "java.lang.Integer");
    // and the child dto has an entry in the yaml file
    addDto("ChildDto");
    // and no overrides
    addDto("FooDto", domain("Foo"), properties("a, b"));
    addDto("BarDto", domain("Bar"), properties("*"), extendsDto("FooDto"));
    // then we have both
    final DtoConfig dc = rootConfig.getDto("BarDto");

    for (DtoProperty dp : dc.getAllProperties()) {
      if (dp.getName().equals("b")) {
        assertThat(dp.getDomainType(), is("java.util.List<com.domain.Child>"));
        assertThat(dp.getDtoType(), is("java.util.List<com.dto.ChildDto>"));
      }
    }
    assertThat(dc.getAllProperties().size(), is(3));
  }

  @Test
  public void testClassProperties() {
    // given two properties
    oracle.addProperty("com.domain.Foo", "a", "java.lang.String");
    oracle.addProperty("com.domain.Foo", "b", "java.lang.String");
    oracle.addProperty("com.domain.Bar", "c", "java.lang.String");
    // and no overrides
    addDto("FooDto", domain("Foo"), properties("*"));
    addDto("BarDto", domain("Bar"), properties("*"), extendsDto("FooDto"));
    // then we have both
    final DtoConfig dc = rootConfig.getDto("BarDto");

    assertThat(dc.getClassProperties().size(), is(1));
    assertThat(dc.getClassProperties().get(0).getName(), is("c"));
  }

  @Test
  public void testInheritedProperties() {
    // given two properties
    oracle.addProperty("com.domain.Foo", "a", "java.lang.String");
    oracle.addProperty("com.domain.Foo", "b", "java.lang.String");
    oracle.addProperty("com.domain.Bar", "c", "java.lang.String");
    // and no overrides
    addDto("FooDto", domain("Foo"), properties("*"));
    addDto("BarDto", domain("Bar"), properties("*"), extendsDto("FooDto"));
    // then we have both
    final DtoConfig dc = rootConfig.getDto("BarDto");
    for (DtoProperty dp : dc.getInheritedProperties()) {
      assertThat(dp.getName(), not("c"));
    }
    assertThat(dc.getInheritedProperties().size(), is(2));
  }

  @Test
  public void testPropertiesExclusion() {
    // given two properties
    oracle.addProperty("com.domain.Foo", "a", "java.lang.String");
    oracle.addProperty("com.domain.Foo", "b", "java.lang.String");
    // and an override to skip b
    addDto("FooDto", domain("Foo"), properties("-b, *"));
    // then we have only 1
    final DtoConfig dc = rootConfig.getDto("FooDto");
    assertThat(dc.getClassProperties().size(), is(1));
  }

  @Test
  public void testPropertiesInclusion() {
    // given two properties
    oracle.addProperty("com.domain.Foo", "a", "java.lang.String");
    oracle.addProperty("com.domain.Foo", "b", "java.lang.String");
    // and an override to skip b
    final Map<String, Object> map = new HashMap<String, Object>();
    map.put("domain", "Foo");
    map.put("properties", "a");
    // when asked
    final DtoConfig dc = new DtoConfig(oracle, rootConfig, "FooDto", map);
    // then we have only 1
    assertThat(dc.getClassProperties().size(), is(1));
  }

  @Test
  public void testPropertiesOverrideTypeAndIncludesAll() {
    // given a property of List and another string
    oracle.addProperty("com.domain.Foo", "a", "java.util.List");
    oracle.addProperty("com.domain.Foo", "b", "java.lang.String");
    // and an override for a and * to include b
    addDto("FooDto", domain("Foo"), properties("a ArrayList<Integer>, *"));
    // when asked
    final DtoConfig dc = rootConfig.getDto("FooDto");
    assertThat(dc.getClassProperties().size(), is(2));
    assertThat(dc.getClassProperties().get(0).getName(), is("a"));
    assertThat(dc.getClassProperties().get(1).getName(), is("b"));
  }

  // Terminology:
  // "mapped" == exists on the domain object, extension=false
  // "mapped overrides" == from domain object, but still needs converted, extension=true
  // "currently called extension" == not on the domain object, extension=true
  //
  // "extension" can apply to both mapped or "extra" (new term) properties

  @Test
  public void testManualProperties() {
    // given no domain object, but manually specified properties
    addDto("FooDto", properties("id Integer, name String"));
    // then we have both properties
    final DtoConfig dc = rootConfig.getDto("FooDto");
    assertThat(dc.getClassProperties().size(), is(2));
    assertThat(dc.getClassProperties().get(0).getName(), is("id"));
    assertThat(dc.getClassProperties().get(0).getDtoType(), is("java.lang.Integer"));
    assertThat(dc.getClassProperties().get(0).getDomainType(), is("java.lang.Integer"));
    assertThat(dc.getClassProperties().get(0).isExtension(), is(true));
    assertThat(dc.getClassProperties().get(1).getName(), is("name"));
    assertThat(dc.getClassProperties().get(1).getDtoType(), is("java.lang.String"));
    assertThat(dc.getClassProperties().get(1).getDomainType(), is("java.lang.String"));
    assertThat(dc.getClassProperties().get(1).isExtension(), is(true));
  }

  @Test
  public void testExtensionProperties() {
    // given a domain object with Foo.a
    oracle.addProperty("com.domain.Foo", "a", "java.lang.String");
    // but we specify both a and b
    addDto("FooDto", domain("Foo"), properties("a, b String"));
    // then we have both
    final DtoConfig dc = rootConfig.getDto("FooDto");
    assertThat(dc.getClassProperties().size(), is(2));
    assertThat(dc.getClassProperties().get(0).getName(), is("a"));
    assertThat(dc.getClassProperties().get(0).getDtoType(), is("java.lang.String"));
    assertThat(dc.getClassProperties().get(1).getName(), is("b"));
    assertThat(dc.getClassProperties().get(1).getDtoType(), is("java.lang.String"));
  }

  @Test
  public void testExtensionPropertiesFromJavaUtil() {
    // given an extension property of ArrayList
    addDto("FooDto", properties("a ArrayList<String>"));
    final DtoConfig dc = rootConfig.getDto("FooDto");
    // then we get the right type
    assertThat(dc.getClassProperties().get(0).getDtoType(), is("java.util.ArrayList<java.lang.String>"));
    assertThat(dc.getClassProperties().get(0).getDomainType(), is("java.util.ArrayList<java.lang.String>"));
    assertThat(dc.getClassProperties().get(0).isExtension(), is(true));
  }

  @Test
  public void testMappedOverridesFromJavaUtil() {
    // given a domain object with some children
    oracle.addProperty("com.domain.Foo", "children", "java.util.List<Child>");
    // and an override property of ArrayList
    addDto("FooDto", domain("Foo"), properties("children ArrayList<String>"));
    final DtoConfig dc = rootConfig.getDto("FooDto");
    // then we get the right type
    assertThat(dc.getClassProperties().get(0).getDtoType(), is("java.util.ArrayList<java.lang.String>"));
    assertThat(dc.getClassProperties().get(0).getDomainType(), is("java.util.List<Child>"));
    assertThat(dc.getClassProperties().get(0).isExtension(), is(true));
  }

  @Test
  public void testMappedOverridesFromJavaUtilSet() {
    // given a domain object with some children
    oracle.addProperty("com.domain.Foo", "children", "java.util.Set<Child>");
    // and an override property of HashSet
    addDto("FooDto", domain("Foo"), properties("children HashSet<String>"));
    final DtoConfig dc = rootConfig.getDto("FooDto");
    // then we get the right type
    assertThat(dc.getClassProperties().get(0).getDtoType(), is("java.util.HashSet<java.lang.String>"));
    assertThat(dc.getClassProperties().get(0).getDomainType(), is("java.util.Set<Child>"));
    assertThat(dc.getClassProperties().get(0).isExtension(), is(true));
  }

  @Test
  public void testMappedPropertiesThatAreValueTypes() {
    // given a domain object with a value types
    oracle.addProperty("com.domain.Foo", "a", "com.domain.ValueType");
    valueTypes().put("com.domain.ValueType", "com.dto.ValueType");
    addDto("FooDto", domain("Foo"), properties("*"));
    // then we know the right dto type
    final DtoConfig dc = rootConfig.getDto("FooDto");
    assertThat(dc.getClassProperties().get(0).getDtoType(), is("com.dto.ValueType"));
    assertThat(dc.getClassProperties().get(0).getDomainType(), is("com.domain.ValueType"));
    assertThat(dc.getClassProperties().get(0).isValueType(), is(true));
    assertThat(dc.getClassProperties().get(0).isExtension(), is(false));
  }

  @Test
  public void testMappedOverridesThatAreValueTypes() {
    // given a domain object with a value types
    oracle.addProperty("com.domain.Foo", "a", "com.domain.ValueType");
    valueTypes().put("com.domain.ValueType", "com.dto.ValueType");
    addDto("FooDto", domain("Foo"), properties("a"));
    // then we know the right dto type
    final DtoConfig dc = rootConfig.getDto("FooDto");
    assertThat(dc.getClassProperties().get(0).getDtoType(), is("com.dto.ValueType"));
    assertThat(dc.getClassProperties().get(0).getDomainType(), is("com.domain.ValueType"));
    assertThat(dc.getClassProperties().get(0).isValueType(), is(true));
    assertThat(dc.getClassProperties().get(0).isExtension(), is(false));
  }

  @Test
  public void testExtensionPropertiesThatAreValueTypes() {
    // given an extension property that is a ValueType
    addDto("FooDto", properties("value ValueType"));
    valueTypes().put("com.domain.values.ValueType", "com.dto.values.ValueType");
    final DtoConfig dc = rootConfig.getDto("FooDto");
    // map it back, with the fully qualified types
    assertThat(dc.getClassProperties().get(0).getDtoType(), is("com.dto.values.ValueType"));
    assertThat(dc.getClassProperties().get(0).getDomainType(), is("com.domain.values.ValueType"));
    assertThat(dc.getClassProperties().get(0).isValueType(), is(true));
    assertThat(dc.getClassProperties().get(0).isExtension(), is(true));
  }

  @Test
  public void testMappedPropertiesThatAreEnums() {
    oracle.setEnumValues("com.domain.Type", list("ONE", "TWO"));
    oracle.addProperty("com.domain.Foo", "type", "com.domain.Type");
    // when enums are mapped automatically
    addDto("FooDto", domain("Foo"), properties("*"));
    // they have the client/server types
    final DtoConfig dc = rootConfig.getDto("FooDto");
    assertThat(dc.getClassProperties().get(0).getDtoType(), is("com.dto.Type"));
    assertThat(dc.getClassProperties().get(0).getDomainType(), is("com.domain.Type"));
  }

  @Test
  public void testExtensionPropertiesThatAreEnums() {
    oracle.setEnumValues("com.domain.Type", list("ONE", "TWO"));
    // when an extension property references an enum
    addDto("FooDto", properties("type Type"));
    // it's fully qualified
    final DtoConfig dc = rootConfig.getDto("FooDto");
    assertThat(dc.getClassProperties().get(0).getDtoType(), is("com.dto.Type"));
    assertThat(dc.getClassProperties().get(0).getDomainType(), is("com.domain.Type"));
  }

  @Test
  public void testExtensionPropertiesThatAreDtos() {
    // when an extension property references another dto
    addDto("FooDto", properties("bar BarDto"));
    addDto("BarDto");
    // it's fully qualified
    final DtoConfig dc = rootConfig.getDto("FooDto");
    assertThat(dc.getClassProperties().get(0).getDtoType(), is("com.dto.BarDto"));
    assertThat(dc.getClassProperties().get(0).getDomainType(), is("com.dto.BarDto"));
  }

  @Test
  public void testMappedPropertiesThatAreEntities() {
    // when Foo references a parent Bar
    oracle.addProperty("com.domain.Foo", "bar", "com.domain.Bar");
    // and it's not explicitly included in properties
    addDto("FooDto", domain("Foo"));
    addDto("BarDto");
    // then we skip it
    final DtoConfig dc = rootConfig.getDto("FooDto");
    assertThat(dc.getClassProperties().size(), is(0));
  }

  @Test
  public void testMappedOverridesThatAreEntities() {
    // when referencing BarDto
    oracle.addProperty("com.domain.Foo", "bar", "com.domain.Bar");
    addDto("FooDto", domain("Foo"), properties("bar")); // leave off BarDto
    addDto("BarDto");
    // it's fully qualified
    final DtoConfig dc = rootConfig.getDto("FooDto");
    assertThat(dc.getClassProperties().get(0).getDtoType(), is("com.dto.BarDto"));
    assertThat(dc.getClassProperties().get(0).isExtension(), is(false));
  }

  @Test
  public void testMappedOverridesThatAreDtos() {
    // when referencing BarDto
    oracle.addProperty("com.domain.Foo", "bar", "com.domain.Bar");
    addDto("FooDto", domain("Foo"), properties("bar BarDto")); // now include BarDto
    addDto("BarDto");
    // it's fully qualified
    final DtoConfig dc = rootConfig.getDto("FooDto");
    assertThat(dc.getClassProperties().get(0).getDtoType(), is("com.dto.BarDto"));
    assertThat(dc.getClassProperties().get(0).isExtension(), is(false));
  }

  @Test
  public void testMappedPropertiesThatAreReadOnly() {
    // given Foo.a
    oracle.addProperty("com.domain.Foo", "a", "java.lang.String");
    // and mapped as read only
    addDto("FooDto", domain("Foo"), properties("~a"));
    // then we know it's read only
    final DtoConfig dc = rootConfig.getDto("FooDto");
    assertThat(dc.getClassProperties().size(), is(1));
    assertThat(dc.getClassProperties().get(0).isReadOnly(), is(true));
  }

  @Test
  public void testMappedPropertiesThatAreTheSameType() {
    // given Foo.a
    oracle.addProperty("com.domain.Foo", "a", "java.lang.Integer");
    // and also mapped as an integer
    addDto("FooDto", domain("Foo"), properties("a Integer"));
    // then it's not an extension property
    final DtoConfig dc = rootConfig.getDto("FooDto");
    assertThat(dc.getClassProperties().size(), is(1));
    assertThat(dc.getClassProperties().get(0).isExtension(), is(false));
  }

  @Test
  public void testMappedPropertiesThatAreADifferentType() {
    // given Foo.a
    oracle.addProperty("com.domain.Foo", "a", "java.lang.Integer");
    // and mapped as long
    addDto("FooDto", domain("Foo"), properties("a Long"));
    // then we know it's an extension property
    final DtoConfig dc = rootConfig.getDto("FooDto");
    assertThat(dc.getClassProperties().size(), is(1));
    assertThat(dc.getClassProperties().get(0).isExtension(), is(true));
  }

  @Test
  public void testExtensionPropertiesThatAreReadOnly() {
    // given an extension property that is read only
    addDto("FooDto", properties("~a String"));
    final DtoConfig dc = rootConfig.getDto("FooDto");
    // then we know it's read only
    assertThat(dc.getClassProperties().get(0).isReadOnly(), is(true));
  }

  @Test
  public void testMappedPropertiesThatAreListsOfEntities() {
    // given a parent and child
    oracle.addProperty("com.domain.Parent", "name", "java.lang.String");
    oracle.addProperty("com.domain.Parent", "children", "java.util.List<com.domain.Child>");
    oracle.addProperty("com.domain.Child", "id", "java.lang.Integer");
    // and the child dto has an entry in the yaml file
    addDto("ChildDto", properties("*"));
    // and the parent doesn't opt in the children
    addDto("ParentDto", domain("Parent"), properties("*"));
    // then it only has the name property
    final DtoConfig dc = rootConfig.getDto("ParentDto");
    assertThat(dc.getClassProperties().size(), is(1));
    assertThat(dc.getClassProperties().get(0).getName(), is("name"));
  }

  @Test
  public void testMappedPropertiesThatAreSetsOfEntities() {
    // given a parent and child
    oracle.addProperty("com.domain.Parent", "name", "java.lang.String");
    oracle.addProperty("com.domain.Parent", "children", "java.util.Set<com.domain.Child>");
    oracle.addProperty("com.domain.Child", "id", "java.lang.Integer");
    // and the child dto has an entry in the yaml file
    addDto("ChildDto", properties("*"));
    // and the parent doesn't opt in the children
    addDto("ParentDto", domain("Parent"), properties("*"));
    // then it only has the name property
    final DtoConfig dc = rootConfig.getDto("ParentDto");
    assertThat(dc.getClassProperties().size(), is(1));
    assertThat(dc.getClassProperties().get(0).getName(), is("name"));
  }

  @Test
  public void testPropertiesThatAreListsOfEntitiesInDifferentDomain() {
    // given a parent and child
    oracle.addProperty("com.domain.Parent", "name", "java.lang.String");
    oracle.addProperty("com.domain.Parent", "children", "java.util.List<com.other.Child>");
    oracle.addProperty("com.domain.Child", "id", "java.lang.Integer");
    // and the child dto has an entry in the yaml file
    addDto("ChildDto", properties("*"));
    // and the parent doesn't opt in the children
    addDto("ParentDto", domain("Parent"), properties("*"));
    // then it only has the name property
    final DtoConfig dc = rootConfig.getDto("ParentDto");
    assertThat(dc.getClassProperties().size(), is(1));
    assertThat(dc.getClassProperties().get(0).getName(), is("name"));
  }

  @Test
  public void testPropertiesThatAreSetsOfEntitiesInDifferentDomain() {
    // given a parent and child
    oracle.addProperty("com.domain.Parent", "name", "java.lang.String");
    oracle.addProperty("com.domain.Parent", "children", "java.util.Set<com.other.Child>");
    oracle.addProperty("com.domain.Child", "id", "java.lang.Integer");
    // and the child dto has an entry in the yaml file
    addDto("ChildDto", properties("*"));
    // and the parent doesn't opt in the children
    addDto("ParentDto", domain("Parent"), properties("*"));
    // then it only has the name property
    final DtoConfig dc = rootConfig.getDto("ParentDto");
    assertThat(dc.getClassProperties().size(), is(1));
    assertThat(dc.getClassProperties().get(0).getName(), is("name"));
  }

  @Test
  public void testMappedPropertiesThatAreAnIdOfAnEntity() {
    // given a parent and child
    oracle.addProperty("com.domain.Parent", "id", "java.lang.Long");
    oracle.addProperty("com.domain.Child", "parent", "com.domain.Parent");
    addDto("ParentDto", domain("Parent"));
    addDto("ChildDto", domain("Child"), properties("parentId"));
    // then it only has the name property
    final DtoConfig dc = rootConfig.getDto("ChildDto");
    assertThat(dc.getClassProperties().size(), is(1));
    assertThat(dc.getClassProperties().get(0).getName(), is("parentId"));
    assertThat(dc.getClassProperties().get(0).isChainedId(), is(true));
    assertThat(dc.getClassProperties().get(0).getGetterMethodName(), is("getParent"));
    assertThat(dc.getClassProperties().get(0).getSetterMethodName(), is("setParent"));
  }

  @Test
  public void testMappedPropertiesThatAreAnIdOfNotAnEntity() {
    // given a parent and child, but the Parent.id isn't a Long
    oracle.addProperty("com.domain.Parent", "id", "java.lang.Integer");
    oracle.addProperty("com.domain.Child", "parent", "com.domain.Parent");
    addDto("ParentDto", domain("Parent"));
    addDto("ChildDto", domain("Child"), properties("parentId Long"));
    // then it only has the name property
    final DtoConfig dc = rootConfig.getDto("ChildDto");
    assertThat(dc.getClassProperties().size(), is(1));
    assertThat(dc.getClassProperties().get(0).getName(), is("parentId"));
    assertThat(dc.getClassProperties().get(0).isChainedId(), is(false));
    assertThat(dc.getClassProperties().get(0).isExtension(), is(true));
  }

  @Test
  public void testMappedPropertiesThatAreAnIdOfAnEntityWithADifferentType() {
    // given a parent and child
    oracle.addProperty("com.domain.Parent", "id", "java.lang.Long");
    oracle.addProperty("com.domain.Child", "parent", "com.domain.Parent");
    addDto("ParentDto", domain("Parent"));
    addDto("ChildDto", domain("Child"), properties("parentId Integer"));
    // then it only has the name property
    final DtoConfig dc = rootConfig.getDto("ChildDto");
    assertThat(dc.getClassProperties().size(), is(1));
    assertThat(dc.getClassProperties().get(0).getName(), is("parentId"));
    assertThat(dc.getClassProperties().get(0).isChainedId(), is(false));
    assertThat(dc.getClassProperties().get(0).isExtension(), is(true));
  }

  @Test
  public void testMappedPropertiesThatLookLikeAnIdByAreASeparateProperty() {
    // given a parent and child
    oracle.addProperty("com.domain.Parent", "id", "java.lang.Long");
    oracle.addProperty("com.domain.Child", "parent", "com.domain.Parent");
    // but the child has an actual "parentId" property
    oracle.addProperty("com.domain.Child", "parentId", "java.lang.String");
    addDto("ParentDto", domain("Parent"));
    addDto("ChildDto", domain("Child"), properties("parentId"));
    // then parentId gets mapped just once
    final DtoConfig dc = rootConfig.getDto("ChildDto");
    assertThat(dc.getClassProperties().size(), is(1));
    assertThat(dc.getClassProperties().get(0).getName(), is("parentId"));
    assertThat(dc.getClassProperties().get(0).isChainedId(), is(false));
    assertThat(dc.getClassProperties().get(0).isExtension(), is(false));
  }

  @Test
  public void testMappedOverridesThatAreListsOfEntities() {
    // given a parent and child
    oracle.addProperty("com.domain.Parent", "children", "java.util.List<com.domain.Child>");
    oracle.addProperty("com.domain.Child", "id", "java.lang.Integer");
    // and the child dto has an entry in the yaml file
    addDto("ChildDto");
    // and explicitly asking for children
    addDto("ParentDto", domain("Parent"), properties("children")); // leave off List<ChildDto>
    // then we map Child to the ChildDto
    final DtoConfig dc = rootConfig.getDto("ParentDto");
    assertThat(dc.getClassProperties().size(), is(1));
    assertThat(dc.getClassProperties().get(0).getName(), is("children"));
    assertThat(dc.getClassProperties().get(0).getDomainType(), is("java.util.List<com.domain.Child>"));
    assertThat(dc.getClassProperties().get(0).getDtoType(), is("java.util.List<com.dto.ChildDto>"));
  }

  @Test
  public void testMappedOverridesThatAreListsOfDtos() {
    // given a parent and child
    oracle.addProperty("com.domain.Parent", "children", "java.util.List<com.domain.Child>");
    oracle.addProperty("com.domain.Child", "id", "java.lang.Integer");
    // and the child dto has an entry in the yaml file
    addDto("ChildDto");
    // and explicitly asking for children
    addDto("ParentDto", domain("Parent"), properties("children List<ChildDto>")); // include List<ChildDto>
    // then we map Child to the ChildDto
    final DtoConfig dc = rootConfig.getDto("ParentDto");
    assertThat(dc.getClassProperties().size(), is(1));
    assertThat(dc.getClassProperties().get(0).getName(), is("children"));
    assertThat(dc.getClassProperties().get(0).getDomainType(), is("java.util.List<com.domain.Child>"));
    assertThat(dc.getClassProperties().get(0).getDtoType(), is("java.util.List<com.dto.ChildDto>"));
  }

  @Test
  public void testExtensionPropertiesThatAreListsOfDtos() {
    // when referencing BarDtos
    addDto("FooDto", domain("Foo"), properties("bars ArrayList<BarDto>"));
    addDto("BarDto");
    // it's fully qualified
    final DtoConfig dc = rootConfig.getDto("FooDto");
    assertThat(dc.getClassProperties().get(0).getDtoType(), is("java.util.List<com.dto.BarDto>"));
  }

  @Test
  public void testMappedAliasedProperties() {
    // given a domain object with longPropertyName
    oracle.addProperty("com.domain.Foo", "longPropertyName", "java.lang.String");
    // and aliased to name
    addDto("FooDto", domain("Foo"), properties("name(longPropertyName)"));
    final DtoConfig dc = rootConfig.getDto("FooDto");
    assertThat(dc.getClassProperties().get(0).getName(), is("name"));
    assertThat(dc.getClassProperties().get(0).getGetterMethodName(), is("getLongPropertyName"));
  }

  @Test
  public void testSortedAlphabetically() {
    // given three properties
    oracle.addProperty("com.domain.Foo", "b", "java.lang.String");
    oracle.addProperty("com.domain.Foo", "a", "java.lang.String");
    oracle.addProperty("com.domain.Foo", "id", "java.lang.Integer");
    addDto("FooDto", domain("Foo"), properties("*"));
    // then we've sorted them
    final DtoConfig dc = rootConfig.getDto("FooDto");
    assertThat(dc.getClassProperties().size(), is(3));
    assertThat(dc.getClassProperties().get(0).getName(), is("id"));
    assertThat(dc.getClassProperties().get(1).getName(), is("a"));
    assertThat(dc.getClassProperties().get(2).getName(), is("b"));
  }

  @Test
  public void testSortedByConfigFirst() {
    // given four properties, initially out of order
    oracle.addProperty("com.domain.Foo", "d", "java.lang.String");
    oracle.addProperty("com.domain.Foo", "c", "java.lang.String");
    oracle.addProperty("com.domain.Foo", "b", "java.lang.String");
    oracle.addProperty("com.domain.Foo", "a", "java.lang.String");
    // and overrides putting d, c first
    addDto("FooDto", domain("Foo"), properties("d, c String, b, a"));
    // then we've sorted them
    final DtoConfig dc = rootConfig.getDto("FooDto");
    assertThat(dc.getClassProperties().size(), is(4));
    assertThat(dc.getClassProperties().get(0).getName(), is("d"));
    assertThat(dc.getClassProperties().get(1).getName(), is("c"));
    assertThat(dc.getClassProperties().get(2).getName(), is("b"));
    assertThat(dc.getClassProperties().get(3).getName(), is("a"));
  }

  private void addDto(final String simpleName, final Entry... entries) {
    final Map<String, Object> map = new HashMap<String, Object>();
    for (final Entry entry : entries) {
      map.put(entry.name, entry.value);
    }
    root.put(simpleName, map);
  }

  @SuppressWarnings("unchecked")
  private Map<String, String> valueTypes() {
    Object value = config.get("valueTypes");
    if (value == null) {
      value = new HashMap<String, String>();
      config.put("valueTypes", value);
    }
    return (Map<String, String>) value;
  }

  private static Entry domain(final String value) {
    return new Entry("domain", value);
  }

  private static Entry properties(final String value) {
    return new Entry("properties", value);
  }

  private static Entry extendsDto(final String value) {
    return new Entry("extends", value);
  }

  private static class Entry {
    private final String name;
    private final Object value;

    private Entry(final String name, final Object value) {
      this.name = name;
      this.value = value;
    }
  }

}
