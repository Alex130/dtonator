package com.bizo.dtonator.domain;

public abstract class Location {

  Long id;

  String Street;

  String poBox;

  public Location(Long id, String street, String poBox) {
    super();
    this.id = id;
    Street = street;
    this.poBox = poBox;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getStreet() {
    return Street;
  }

  public void setStreet(String street) {
    Street = street;
  }

  public String getPoBox() {
    return poBox;
  }

  public void setPoBox(String poBox) {
    this.poBox = poBox;
  }

  public String getDisplayString() {
    return this.toString();
  }
}
