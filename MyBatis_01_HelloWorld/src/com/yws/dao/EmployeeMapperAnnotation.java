package com.yws.dao;

import org.apache.ibatis.annotations.Select;

import com.yws.bean.Employee;

public interface EmployeeMapperAnnotation {
	@Select("select * from tbl_employee where id=#{id}")
	public Employee getEmployee(Integer id);
}
