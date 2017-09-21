package com.bizo.dtonator.domain;

public abstract class AbstractManager<T extends Account> {

  private Long id;

  private String name;

  private T account;

  private int employees;

  public AbstractManager() {
  }

  public AbstractManager(final Long id, String name) {
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

}
