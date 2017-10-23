package com.bizo.dtonator.domain;

import java.util.List;

public class Parent {

  Long id;
  String name;
  Sibling eldestSibling;
  List<Sibling> siblings;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Sibling> getSiblings() {
    return siblings;
  }

  public void setSiblings(List<Sibling> siblings) {
    this.siblings = siblings;
  }

  public Long getId() {
    return id;
  }

  public Sibling getEldestSibling() {
    return eldestSibling;
  }

  public void setEldestSibling(Sibling eldestSibling) {
    this.eldestSibling = eldestSibling;
  }

}
