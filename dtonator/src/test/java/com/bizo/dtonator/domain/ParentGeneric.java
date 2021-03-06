package com.bizo.dtonator.domain;

import java.util.List;
import java.util.Set;

public class ParentGeneric<T extends SiblingGeneric> {

  Long id;
  String name;
  T eldestSibling;
  T eldestStep;
  public List<T> siblings;
  public Set<T> stepSiblings;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<T> getSiblings() {
    return siblings;
  }

  public void setSiblings(List<T> siblings) {
    this.siblings = siblings;
  }

  public Long getId() {
    return id;
  }

  public T getEldestSibling() {
    return eldestSibling;
  }

  public void setEldestSibling(T eldestSibling) {
    this.eldestSibling = eldestSibling;
  }

  public Set<T> getStepSiblings() {
    return stepSiblings;
  }

  public void setStepSiblings(Set<T> stepSiblings) {
    this.stepSiblings = stepSiblings;
  }

  public T getEldestStep() {
    return eldestStep;
  }

  public void setEldestStep(T eldestStep) {
    this.eldestStep = eldestStep;
  }
}
