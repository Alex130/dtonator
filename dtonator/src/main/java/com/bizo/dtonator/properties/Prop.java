package com.bizo.dtonator.properties;

import java.util.Map;

import org.apache.commons.collections4.MultiValuedMap;

/** A simple DTO to abstract discovering properties via reflection. */
public class Prop {

  public final String name;
  public final String type;
  public final boolean readOnly;
  private final String getterMethodName;
  private final String setterNameMethod;
  public final boolean inherited;
  private final MultiValuedMap<String, GenericParts> genericTypes;

  public Prop(
    String name,
    String type,
    boolean readOnly,
    String getterMethodName,
    String setterNameMethod,
    boolean inherited,
    MultiValuedMap<String, GenericParts> genericTypes) {
    super();
    this.name = name;
    this.type = type;
    this.readOnly = readOnly;
    this.getterMethodName = getterMethodName;
    this.setterNameMethod = setterNameMethod;
    this.inherited = inherited;
    this.genericTypes = genericTypes;

  }

  public String getGetterMethodName() {
    return getterMethodName;
  }

  public String getSetterNameMethod() {
    return setterNameMethod;
  }

  public MultiValuedMap<String, GenericParts> getGenericTypes() {
    return genericTypes;
  }

  @Override
  public String toString() {
    return name + " " + type;
  }
}
