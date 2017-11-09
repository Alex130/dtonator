package com.bizo.dtonator.mapper;

import java.util.ArrayList;
import java.util.List;

import com.bizo.dtonator.domain.Employee;
import com.bizo.dtonator.dtos.EmployeeAccountTslDto;

public class DefaultEmployeeWithAccountsTslDtoMapper implements EmployeeWithAccountsTslDtoMapper {

  @Override
  public ArrayList<EmployeeAccountTslDto> getOtherAccounts(final Mapper m, final Employee employee) {
    return new ArrayList<EmployeeAccountTslDto>();
  }

  @Override
  public void setOtherAccounts(final Mapper m, final Employee employee, final List<EmployeeAccountTslDto> otherAccounts) {
  }

}
