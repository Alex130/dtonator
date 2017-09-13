package com.bizo.dtonator.domain;

public class Employer extends AbstractEmployer {

  private String name;

  public Employer() {
  }

  public Employer(final Long id, final String name) {
    super(id);
    setName(name);
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

}
