package com.bizo.dtonator.domain;

import java.text.DateFormat;
import java.text.ParseException;
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
    try {
      return DateFormat.getDateInstance().parse(birthday);
    } catch (ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return null;
    }
  }
}
