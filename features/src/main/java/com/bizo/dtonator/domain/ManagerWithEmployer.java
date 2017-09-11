package com.bizo.dtonator.domain;

public class ManagerWithEmployer<V extends AbstractEmployer> {

  private Long id;
  private String name;
  private Dollars balance;
  private V employer;

  public ManagerWithEmployer() {
  }

  public ManagerWithEmployer(final Long id, final String name) {
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

  public V getEmployer() {
    return employer;
  }

  public void setEmployer(V employer) {
    this.employer = employer;
  }

}
