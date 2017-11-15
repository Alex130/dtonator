package com.bizo.dtonator.domain;

public class LocationGeneric<T> {

  Long id;

  String Street;

  T poBox;

  public LocationGeneric(Long id, String street, T poBox) {
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

  public T getPoBox() {
    return poBox;
  }

  public void setPoBox(T poBox) {
    this.poBox = poBox;
  }
}
