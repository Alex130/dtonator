package com.bizo.dtonator.properties;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.bizo.dtonator.config.RootConfig;
import com.bizo.dtonator.domain.AbstractParent;

import com.bizo.dtonator.domain.Child;
import com.bizo.dtonator.domain.ChildGeneric;
import com.bizo.dtonator.domain.ParentGeneric;

import com.bizo.dtonator.domain.Sibling;
import com.bizo.dtonator.domain.Sister;

import ru.vyarus.java.generics.resolver.GenericsResolver;
import ru.vyarus.java.generics.resolver.context.GenericsContext;
import ru.vyarus.java.generics.resolver.util.NoGenericException;

public class ReflectionTypeOracleTest {

  private ReflectionTypeOracle oracle = new ReflectionTypeOracle();

  private final Map<String, Object> root = new HashMap<String, Object>();
  private final RootConfig rootConfig = new RootConfig(oracle, root);
  private final Map<String, Object> config = new HashMap<String, Object>();

  @Before
  public void setup() {

    config.put("domainPackage", "com.bizo.dtonator.properties");
    config.put("dtoPackage", "com.dto");
    config.put("sourceDirectory", "src/test/java");
    root.put("config", config);
  }

  @Test
  public void testParentGenericResolution() throws NoSuchFieldException, SecurityException, NoGenericException, NoSuchMethodException {

    GenericsContext context = GenericsResolver.resolve(ChildGeneric.class).type(ParentGeneric.class);
    Field field = ParentGeneric.class.getField("siblings");
    assertEquals(List.class, context.resolveClass(field.getGenericType()));
    assertEquals(Sister.class, context.resolveGenericOf(field.getGenericType()));

    field = ParentGeneric.class.getField("stepSiblings");
    assertEquals(Set.class, context.resolveClass(field.getGenericType()));
    assertEquals(Sister.class, context.resolveGenericOf(field.getGenericType()));

    Method method = ParentGeneric.class.getMethod("getSiblings");
    assertEquals(List.class, context.resolveClass(method.getGenericReturnType()));
    assertEquals(Sister.class, context.resolveGenericOf(method.getGenericReturnType()));

    field = ChildGeneric.class.getField("siblings");
    assertEquals(List.class, context.resolveClass(field.getGenericType()));
    assertEquals(Sister.class, context.resolveGenericOf(field.getGenericType()));

    field = ChildGeneric.class.getField("stepSiblings");
    assertEquals(Set.class, context.resolveClass(field.getGenericType()));
    assertEquals(Sister.class, context.resolveGenericOf(field.getGenericType()));

    method = ChildGeneric.class.getMethod("getSiblings");
    assertEquals(List.class, context.resolveClass(method.getGenericReturnType()));
    assertEquals(Sister.class, context.resolveGenericOf(method.getGenericReturnType()));
  }

  @Test
  public void testGetPropertiesForChild() {

    List<Prop> properties = oracle.getProperties(Child.class.getName(), true);

    assertNotNull(properties);
    Prop siblingsProp = null;
    Prop eldestProp = null;
    for (Prop p : properties) {
      if ("siblings".equals(p.name)) {
        siblingsProp = p;

      }

      if ("eldestSibling".equals(p.name)) {
        eldestProp = p;

      }
    }
    assertNotNull(siblingsProp);
    assertThat(siblingsProp.type, is("java.util.List<" + Sibling.class.getName() + ">"));
    assertNotNull(eldestProp);
    assertThat(eldestProp.type, is("com.bizo.dtonator.domain.Sibling"));
    assertThat(properties.size(), is(5));

  }

  @Test
  public void testGetPropertiesForChildGeneric() {

    List<Prop> properties = oracle.getProperties(ChildGeneric.class.getName(), true);

    assertNotNull(properties);
    Prop siblingsProp = null;
    Prop stepSiblingsProp = null;
    Prop eldestProp = null;
    for (Prop p : properties) {
      if ("siblings".equals(p.name)) {
        siblingsProp = p;

      }
      if ("stepSiblings".equals(p.name)) {
        stepSiblingsProp = p;

      }
      if ("eldestSibling".equals(p.name)) {
        eldestProp = p;

      }
    }
    assertNotNull(siblingsProp);
    assertThat(siblingsProp.type, is("java.util.List<T>"));
    assertNotNull(stepSiblingsProp);
    assertThat(stepSiblingsProp.type, is("java.util.List<" + Sister.class.getName() + ">"));
    assertNotNull(eldestProp);
    assertThat(eldestProp.type, is("com.bizo.dtonator.domain.Sister"));
    assertThat(properties.size(), is(7));

  }

  @Test
  public void testGetPropertiesForParentGeneric() {

    List<Prop> properties = oracle.getProperties(ParentGeneric.class.getName(), true);

    assertNotNull(properties);
    Prop siblingsProp = null;
    Prop stepSiblingsProp = null;
    Prop eldestProp = null;
    for (Prop p : properties) {
      if ("siblings".equals(p.name)) {
        siblingsProp = p;

      }
      if ("stepSiblings".equals(p.name)) {
        stepSiblingsProp = p;

      }
      if ("eldestSibling".equals(p.name)) {
        eldestProp = p;

      }
    }
    assertNotNull(siblingsProp);
    assertThat(siblingsProp.type, is("java.util.List<T>"));
    assertNotNull(stepSiblingsProp);
    assertThat(stepSiblingsProp.type, is("java.util.List<T>"));
    assertNotNull(eldestProp);
    assertThat(eldestProp.type, is("T"));
    assertThat(properties.size(), is(6));

  }

  @Test
  public void testGetPropertiesAbstractParent() {

    List<Prop> properties = oracle.getProperties(AbstractParent.class.getName(), true);

    assertNotNull(properties);
    Prop nameProp = null;
    Prop idProp = null;
    for (Prop p : properties) {
      if ("name".equals(p.name)) {
        nameProp = p;

      }

      if ("id".equals(p.name)) {
        idProp = p;

      }
    }
    assertNotNull(nameProp);
    assertThat(nameProp.type, is("java.lang.String"));
    assertFalse(nameProp.isAbstract);
    //make sure that abstract properties are correctly identified
    assertNotNull(idProp);
    assertTrue(idProp.isAbstract);
    assertThat(properties.size(), is(2));

  }
}
