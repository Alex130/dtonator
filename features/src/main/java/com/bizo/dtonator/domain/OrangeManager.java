package com.bizo.dtonator.domain;

import java.util.List;

import com.bizo.foo.domain.OrangeAccount;

public class OrangeManager {

  private Long id;

  private String name;

  private OrangeAccount account;

  private int employees;

  protected List<OrangeAccount> managedAccounts;

  public OrangeManager() {
  }

  public OrangeManager(final Long id, String name) {
    setId(id);
    setName(name);
  }

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public OrangeAccount getAccount() {
    return account;
  }

  public void setAccount(OrangeAccount account) {
    this.account = account;
  }

  public int getEmployees() {
    return employees;
  }

  public void setEmployees(int employees) {
    this.employees = employees;
  }

  public List<OrangeAccount> getManagedAccounts() {
    return managedAccounts;
  }

  public void setManagedAccounts(List<OrangeAccount> managedAccounts) {
    this.managedAccounts = managedAccounts;
  }

}
