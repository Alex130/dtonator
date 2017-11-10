package com.bizo.dtonator.domain;

public abstract class AbstractParent {

  String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public abstract Long getId();

  public abstract void setId(Long id);
}
