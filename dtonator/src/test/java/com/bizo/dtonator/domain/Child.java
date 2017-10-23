package com.bizo.dtonator.domain;

import java.util.Date;

public class Child extends Parent {

  String birthday;

  public String getBirthday() {
    return birthday;
  }

  public void setBirthday(String birthday) {
    this.birthday = birthday;
  }

  public Date convertBirthday() {
    return new Date(birthday);
  }
}
