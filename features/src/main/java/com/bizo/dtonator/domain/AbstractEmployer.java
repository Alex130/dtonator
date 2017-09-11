package com.bizo.dtonator.domain;

public abstract class AbstractEmployer {

  private Long id;

  public AbstractEmployer() {
  }

  public AbstractEmployer(final Long id) {
    setId(id);

  }

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
  }

}
