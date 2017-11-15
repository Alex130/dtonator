package com.bizo.dtonator.domain;

import java.util.List;

public class Role {

  private Long id;
  private String name;

  private List<Role> childRoles;
  private List<Role> parentRoles;

  public Role() {
  }

  public Role(final Long id, final String name) {
    setId(id);
    setName(name);
  }

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public List<Role> getChildRoles() {
    return childRoles;
  }

  public void setChildRoles(List<Role> childRoles) {
    this.childRoles = childRoles;
  }

  public List<Role> getParentRoles() {
    return parentRoles;
  }

  public void setParentRoles(List<Role> parentRoles) {
    this.parentRoles = parentRoles;
  }

}
