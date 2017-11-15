package com.bizo.foo.domain;

import com.bizo.dtonator.domain.Account;

public class OrangeAccount extends Account {

  private boolean foo;

  public OrangeAccount() {
  }

  public OrangeAccount(Long id, String name, boolean foo) {

    super(id, name);
    this.foo = foo;
  }

  public boolean isFoo() {
    return foo;
  }

  public void setFoo(boolean foo) {
    this.foo = foo;
  }

}
