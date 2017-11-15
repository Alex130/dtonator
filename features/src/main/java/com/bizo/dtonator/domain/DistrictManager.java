package com.bizo.dtonator.domain;

import java.util.List;

import com.bizo.foo.domain.OrangeAccount;

public class DistrictManager<T extends Account> {

  private Long id;

  private String name;

  private T account;

  private int employees;

  protected List<T> managedAccounts;

  public DistrictManager() {
  }

  public DistrictManager(final Long id, String name) {
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

  public T getAccount() {
    return account;
  }

  public void setAccount(T account) {
    this.account = account;
  }

  public int getEmployees() {
    return employees;
  }

  public void setEmployees(int employees) {
    this.employees = employees;
  }

  public List<T> getManagedAccounts() {
    return managedAccounts;
  }

  public void setManagedAccounts(List<T> managedAccounts) {
    this.managedAccounts = managedAccounts;
  }

}
