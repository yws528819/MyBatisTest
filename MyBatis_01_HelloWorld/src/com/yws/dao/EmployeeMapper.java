package com.yws.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yws.bean.Employee;

public interface EmployeeMapper {
	public Employee getEmpById(Integer id);
	
	//增删改
	public void addEmp(Employee employee);
	
	public boolean updateEmp(Employee employee);
	
	public void deleteEmpById(Integer id);
	
	//参数处理
	public Employee getEmpByIdAndLastName(@Param("id") Integer id, @Param("lastName") String lastName);
	
	public List<Employee> getEmpsByLastNameLike(String lastName);
}
