package com.bizo.dtonator.domain;

public class YellowManager extends AbstractManager<YellowAccount> {

  private String location;

  public YellowManager() {
    super();
  }

  public YellowManager(Long id, String name, String location) {
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
