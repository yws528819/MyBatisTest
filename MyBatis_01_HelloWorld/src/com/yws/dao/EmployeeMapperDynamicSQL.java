package com.yws.dao;

import java.util.List;

import com.yws.bean.Employee;

public interface EmployeeMapperDynamicSQL {
	public List<Employee> getEmpsByConditionIf(Employee employee);
}
