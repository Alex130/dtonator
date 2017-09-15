package com.bizo.dtonator.domain;

public class RedManager extends AbstractManager<RedAccount> {

  private String location;

  public RedManager(Long id, String name, String location) {
    super(id, name);
    this.location = location;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

}
