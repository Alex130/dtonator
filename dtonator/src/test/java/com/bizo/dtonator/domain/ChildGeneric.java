package com.bizo.dtonator.domain;

import java.util.List;

public class ChildGeneric extends ParentGeneric<Sister> {

  String birthday;

  public String getBirthday() {
    return birthday;
  }

  public void setBirthday(String birthday) {
    this.birthday = birthday;
  }

  @Override
  public List<Sister> getStepSiblings() {

    return super.getStepSiblings();
  }

  @Override
  public void setStepSiblings(List<Sister> stepSiblings) {

    super.setStepSiblings(stepSiblings);
  }

  @Override
  public Sister getEldestStep() {
    // TODO Auto-generated method stub
    return super.getEldestStep();
  }

  @Override
  public void setEldestStep(Sister eldestStep) {
    // TODO Auto-generated method stub
    super.setEldestStep(eldestStep);
  }

}
