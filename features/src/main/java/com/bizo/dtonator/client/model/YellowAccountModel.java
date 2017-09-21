package com.bizo.dtonator.client.model;

import com.bizo.dtonator.dtos.YellowAccountDto;

public class YellowAccountModel extends YellowAccountModelCodegen {

  public YellowAccountModel(YellowAccountDto dto) {
    super(dto);
    addRules();
    merge(dto);
  }

  private void addRules() {
  }

}
