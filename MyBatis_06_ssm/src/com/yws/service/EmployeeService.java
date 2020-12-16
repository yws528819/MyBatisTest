package com.yws.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yws.bean.Employee;
import com.yws.dao.EmployeeMapper;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeMapper employeeMapper;
	
	@Autowired
	private SqlSession sqlSession;
	
	public List<Employee> getEmps() {
		EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
		return mapper.getEmps();
		
		
		//return employeeMapper.getEmps();
	}
}
