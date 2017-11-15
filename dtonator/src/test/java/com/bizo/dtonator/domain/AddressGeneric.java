package com.bizo.dtonator.domain;

public class AddressGeneric extends LocationGeneric<String> {

  Integer geoCode;

  public AddressGeneric(Long id, String street, String poBox) {
    super(id, street, poBox);

  }

  public Integer getGeoCode() {
    return geoCode;
  }

  public void setGeoCode(Integer geoCode) {
    this.geoCode = geoCode;
  }

}
