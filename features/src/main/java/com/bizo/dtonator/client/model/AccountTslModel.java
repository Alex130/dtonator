package com.bizo.dtonator.client.model;

import com.bizo.dtonator.dtos.AccountTslDto;

public class AccountTslModel extends AccountTslModelCodegen {

  public AccountTslModel(AccountTslDto dto) {
    super(dto);
    addRules();
    merge(dto);
  }

  private void addRules() {
  }

}
