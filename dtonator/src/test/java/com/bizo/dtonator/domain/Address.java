package com.bizo.dtonator.domain;

public class Address extends Location {

  Integer geoCode;

  public Address(Long id, String street, String poBox) {
    super(id, street, poBox);

  }

  public Integer getGeoCode() {
    return geoCode;
  }

  public void setGeoCode(Integer geoCode) {
    this.geoCode = geoCode;
  }

  @Override
  public String getDisplayString() {
    return super.getDisplayString() + " " + geoCode;
  }
}
