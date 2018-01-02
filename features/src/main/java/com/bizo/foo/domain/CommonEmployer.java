package com.bizo.foo.domain;

public class CommonEmployer extends AbstractEmployer {

  private String name;

  public CommonEmployer() {
  }

  public CommonEmployer(final Long id, final String name) {
    super(id);
    setName(name);
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  @Override
  public String getDisplayString() {
    return getName();
  }

}
