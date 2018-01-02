package com.bizo.dtonator.domain;

import com.bizo.foo.domain.CommonEmployer;

public class NewEmployer extends CommonEmployer {

  private Integer employeeCount;

  public NewEmployer() {
  }

  public NewEmployer(final Long id, final String name) {
    super(id, name);
    setEmployeeCount(employeeCount);
  }

  public Integer getEmployeeCount() {
    return employeeCount;
  }

  public void setEmployeeCount(Integer employeeCount) {
    this.employeeCount = employeeCount;
  }

}
