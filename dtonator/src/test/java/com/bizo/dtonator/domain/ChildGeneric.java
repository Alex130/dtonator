package com.bizo.dtonator.domain;

import java.util.Set;

public class ChildGeneric extends ParentGeneric<Sister> {

  String birthday;

  public String getBirthday() {
    return birthday;
  }

  public void setBirthday(String birthday) {
    this.birthday = birthday;
  }

  @Override
  public Set<Sister> getStepSiblings() {

    return super.getStepSiblings();
  }

  @Override
  public void setStepSiblings(Set<Sister> stepSiblings) {

    super.setStepSiblings(stepSiblings);
  }

  @Override
  public Sister getEldestStep() {

    return super.getEldestStep();
  }

  @Override
  public void setEldestStep(Sister eldestStep) {

    super.setEldestStep(eldestStep);
  }

}
