package com.bizo.dtonator.domain;

public class GreenManager extends AbstractManager<GreenAccount> {

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

  //  @Override
  //  public RedAccount getAccount() {
  //    return super.getAccount();
  //  }
}
