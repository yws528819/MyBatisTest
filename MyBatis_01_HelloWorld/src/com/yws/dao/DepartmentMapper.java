package com.yws.dao;

import com.yws.bean.Department;

public interface DepartmentMapper {
	public Department getDepById(Integer id);
	
	public Department getDepByIdPlus(Integer id);
}
