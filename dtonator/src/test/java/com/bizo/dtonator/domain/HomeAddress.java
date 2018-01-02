package com.bizo.dtonator.domain;

public class HomeAddress extends Address {

  private String phone;

  public HomeAddress(Long id, String street, String poBox, String phone) {
    super(id, street, poBox);
    this.phone = phone;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }
}
