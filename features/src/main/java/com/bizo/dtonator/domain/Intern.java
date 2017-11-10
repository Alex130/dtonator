package com.bizo.dtonator.domain;

import java.util.Date;

public class Intern extends Employee {

  public Date endDate;

  public Intern() {
    super();
  }

  public Intern(Long id, String name, Date endDate) {
    super(id, name);
    this.endDate = endDate;
  }

  public Intern(String name) {
    super(name);

  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

}
