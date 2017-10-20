package com.bizo.dtonator.properties;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ReflectionTypeOracleTest {

  private class Parent {

    Long id;
    String name;
    List<Sibling> siblings;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public List<Sibling> getSiblings() {
      return siblings;
    }

    public void setSiblings(List<Sibling> siblings) {
      this.siblings = siblings;
    }

    public Long getId() {
      return id;
    }
  }

  private class Child extends Parent {

    String birthday;

    public String getBirthday() {
      return birthday;
    }

    public void setBirthday(String birthday) {
      this.birthday = birthday;
    }

  }

  private class Sibling {
    Long id;

    String gender;

    public String getGender() {
      return gender;
    }

    public void setGender(String gender) {
      this.gender = gender;
    }
  }

  /************************
   * Generic Classes
   * @param <T>
   ***********************/
  private class ParentGeneric<T extends SiblingGeneric> {

    Long id;
    String name;
    List<T> siblings;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public List<T> getSiblings() {
      return siblings;
    }

    public void setSiblings(List<T> siblings) {
      this.siblings = siblings;
    }

    public Long getId() {
      return id;
    }
  }

  private class ChildGeneric extends ParentGeneric<Sister> {

    String birthday;

    public String getBirthday() {
      return birthday;
    }

    public void setBirthday(String birthday) {
      this.birthday = birthday;
    }

  }

  private class SiblingGeneric {
    Long id;

    String gender;

    public String getGender() {
      return gender;
    }

    public void setGender(String gender) {
      this.gender = gender;
    }
  }

  private class Sister extends SiblingGeneric {
    Integer age;

    public Integer getAge() {
      return age;
    }

    public void setAge(Integer age) {
      this.age = age;
    }
  }

  private ReflectionTypeOracle oracle;

  @Before
  public void setup() {
    oracle = new ReflectionTypeOracle();
  }

  @Test
  public void testGetPropertiesForChild() {

    List<Prop> properties = oracle.getProperties(Child.class.getName(), true);

    assertNotNull(properties);
    Prop siblingsProp = null;
    for (Prop p : properties) {
      if ("siblings".equals(p.name)) {
        siblingsProp = p;
        break;
      }
    }
    assertNotNull(siblingsProp);
    assertThat(siblingsProp.type, is("java.util.List<" + Sibling.class.getName() + ">"));
    assertThat(properties.size(), is(4));

  }

  @Test
  public void testGetPropertiesForChildGeneric() {

    List<Prop> properties = oracle.getProperties(ChildGeneric.class.getName(), true);

    assertNotNull(properties);
    Prop siblingsProp = null;
    for (Prop p : properties) {
      if ("siblings".equals(p.name)) {
        siblingsProp = p;
        break;
      }
    }
    assertNotNull(siblingsProp);
    assertThat(siblingsProp.type, is("java.util.List<" + Sister.class.getName() + ">"));
    assertThat(properties.size(), is(4));

  }
}
