package com.bizo.dtonator.domain;

public class EmployeeRedAccount extends EmployeeTypedAccount<RedAccount> {

  private String eFoo;

  public EmployeeRedAccount() {
  }

  public EmployeeRedAccount(final Long id, final String name, String eFoo) {
    setId(id);
    setName(name);
    this.eFoo = eFoo;
  }

  public String geteFoo() {
    return eFoo;
  }

  public void seteFoo(String eFoo) {
    this.eFoo = eFoo;
  }

}
