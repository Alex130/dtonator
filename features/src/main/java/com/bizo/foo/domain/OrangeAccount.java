package com.bizo.foo.domain;

public class OrangeAccount {

  private Long id;
  private String name;
  private boolean foo;

  public OrangeAccount() {
  }

  public OrangeAccount(Long id, String name, boolean foo) {
    this.id = id;
    this.name = name;
    this.foo = foo;
  }

  public boolean isFoo() {
    return foo;
  }

  public void setFoo(boolean foo) {
    this.foo = foo;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getId() {
    return id;
  }

}
