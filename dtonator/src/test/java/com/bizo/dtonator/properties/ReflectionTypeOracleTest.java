package com.bizo.dtonator.properties;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.bizo.dtonator.config.RootConfig;
import com.bizo.dtonator.domain.Child;
import com.bizo.dtonator.domain.ChildGeneric;
import com.bizo.dtonator.domain.ParentGeneric;
import com.bizo.dtonator.domain.Sibling;
import com.bizo.dtonator.domain.Sister;

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
  public void testParseProperties() {
    List<Prop> properties = oracle.parseProperties(rootConfig, Child.class.getName());
    assertNotNull(properties);
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
}
