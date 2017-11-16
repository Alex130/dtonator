package com.bizo.dtonator.domain;

public abstract class Account {

  protected Long id;
  private String name;
  private Number actCode;

  public Account() {
  }

  public Account(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Number getActCode() {
    return actCode;
  }

  public void setActCode(Number actCode) {
    this.actCode = actCode;
  }

}
