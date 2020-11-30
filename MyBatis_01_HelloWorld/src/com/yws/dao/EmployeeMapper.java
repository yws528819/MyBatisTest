package com.yws.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yws.bean.Employee;

public interface EmployeeMapper {
	public Employee getEmpById(Integer id);
	
	//��ɾ��
	public void addEmp(Employee employee);
	
	public boolean updateEmp(Employee employee);
	
	public void deleteEmpById(Integer id);
	
	//��������
	public Employee getEmpByIdAndLastName(@Param("id") Integer id, @Param("lastName") String lastName);
	
	public List<Employee> getEmpsByLastNameLike(String lastName);
}
