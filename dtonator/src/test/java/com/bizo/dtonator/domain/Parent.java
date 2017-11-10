package com.bizo.dtonator.domain;

import java.util.List;

public class Parent extends AbstractParent {

  Long id;

  Sibling eldestSibling;
  List<Sibling> siblings;

  public List<Sibling> getSiblings() {
    return siblings;
  }

  public void setSiblings(List<Sibling> siblings) {
    this.siblings = siblings;
  }

  @Override
  public Long getId() {
    return id;
  }

  public Sibling getEldestSibling() {
    return eldestSibling;
  }

  public void setEldestSibling(Sibling eldestSibling) {
    this.eldestSibling = eldestSibling;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

}
