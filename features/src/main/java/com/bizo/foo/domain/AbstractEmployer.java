package com.bizo.foo.domain;

import com.bizo.dtonator.domain.HasId;

public abstract class AbstractEmployer implements HasId {

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

  public String getDisplayString() {
    return this.toString();
  }

}
