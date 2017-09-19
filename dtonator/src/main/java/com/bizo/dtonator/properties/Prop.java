package com.bizo.dtonator.properties;

/** A simple DTO to abstract discovering properties via reflection. */
public class Prop {

  public final String name;
  public final String type;
  public final boolean readOnly;
  private final String getterMethodName;
  private final String setterNameMethod;
  public final boolean inherited;

  public Prop(String name, String type, boolean readOnly, String getterMethodName, String setterNameMethod, boolean inherited) {
    super();
    this.name = name;
    this.type = type;
    this.readOnly = readOnly;
    this.getterMethodName = getterMethodName;
    this.setterNameMethod = setterNameMethod;
    this.inherited = inherited;

  }

  public String getGetterMethodName() {
    return getterMethodName;
  }

  public String getSetterNameMethod() {
    return setterNameMethod;
  }

  @Override
  public String toString() {
    return name + " " + type;
  }
}
