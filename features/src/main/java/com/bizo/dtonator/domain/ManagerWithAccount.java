package com.bizo.dtonator.domain;

public class ManagerWithAccount<T extends Account> {

  private Long id;
  private String name;
  private Dollars balance;
  private T account;

  public ManagerWithAccount() {
  }

  public ManagerWithAccount(final Long id, final String name) {
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

  public void setName(final String name) {
    this.name = name;
  }

  public Dollars getBalance() {
    return balance;
  }

  public void setBalance(final Dollars balance) {
    this.balance = balance;
  }

  public T getAccount() {
    return account;
  }

  public void setAccount(T account) {
    this.account = account;
  }

}
