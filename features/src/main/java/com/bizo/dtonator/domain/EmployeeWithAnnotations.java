package com.bizo.dtonator.domain;

import java.util.List;

import com.bizo.dtonator.annotation.Transient;

public class EmployeeWithAnnotations {

  private Long id;
  private String name;
  private boolean working;
  private EmployeeType type;
  private Dollars salary;
  private List<EmployeeAccount> accounts;
  private Employer employer;
  @Transient
  private String status;
  private String internalName;
  @Transient
  private String workEmail;
  private String homeEmail;

  public EmployeeWithAnnotations() {
  }

  public EmployeeWithAnnotations(final Long id, final String name) {
    setId(id);
    setName(name);
  }

  public EmployeeWithAnnotations(final String name) {
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

  public boolean isWorking() {
    return working;
  }

  public void setWorking(final boolean working) {
    this.working = working;
  }

  public EmployeeType getType() {
    return type;
  }

  public void setType(final EmployeeType type) {
    this.type = type;
  }

  public Dollars getSalary() {
    return salary;
  }

  public void setSalary(final Dollars salary) {
    this.salary = salary;
  }

  public Employer getEmployer() {
    return employer;
  }

  public void setEmployer(final Employer employer) {
    this.employer = employer;
  }

  public List<EmployeeAccount> getAccounts() {
    return accounts;
  }

  public void setAccounts(final List<EmployeeAccount> accounts) {
    this.accounts = accounts;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  @Transient
  public String getInternalName() {
    return internalName;
  }

  public void setInternalName(String internalName) {
    this.internalName = internalName;
  }

  public String getWorkEmail() {
    return workEmail;
  }

  public void setWorkEmail(String workEmail) {
    this.workEmail = workEmail;
  }

  @Transient
  public String getHomeEmail() {
    return homeEmail;
  }

  public void setHomeEmail(String homeEmail) {
    this.homeEmail = homeEmail;
  }

}
