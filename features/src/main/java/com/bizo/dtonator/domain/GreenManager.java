package com.bizo.dtonator.domain;

import java.util.List;

public class GreenManager extends AbstractManager<GreenAccount, GreenAccount> {

  private String location;

  public GreenManager() {
    super();
  }

  public GreenManager(Long id, String name, String location) {
    super(id, name);
    this.location = location;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public List<GreenAccount> getManagedAccounts() {
    return managedAccounts;
  }

  public void setManagedAccounts(List<GreenAccount> managedAccounts) {
    this.managedAccounts = managedAccounts;
  }

}
