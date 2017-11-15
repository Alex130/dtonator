package com.bizo.dtonator.domain;

import java.util.List;

import com.bizo.foo.domain.OrangeAccount;

public class OrangeDistrictManager extends DistrictManager<OrangeAccount> {

  protected List<String> colors;

  public OrangeDistrictManager() {
  }

  public OrangeDistrictManager(final Long id, String name) {
    super(id, name);
  }

  public List<String> getColors() {
    return colors;
  }

  public void setColors(List<String> colors) {
    this.colors = colors;
  }

}
