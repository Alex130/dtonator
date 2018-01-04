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

  @Override
  public Integer getGeoCode() {
    return super.getGeoCode();
  }

  @Override
  public void setGeoCode(Integer geoCode) {

    super.setGeoCode(geoCode);
  }
}
