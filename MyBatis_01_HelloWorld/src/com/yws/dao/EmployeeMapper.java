package com.yws.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import com.yws.bean.Employee;
import com.yws.bean.Page;

public interface EmployeeMapper {
	public Employee getEmpById(Integer id);
	
	//增删改
	public void addEmp(Employee employee);
	
	public boolean updateEmp(Employee employee);
	
	public void deleteEmpById(Integer id);
	
	//参数处理
	public Employee getEmpByIdAndLastName(@Param("id") Integer id, @Param("lastName") String lastName);
	
	public List<Employee> getEmpsByLastNameLike(String lastName);
	
	//返回一条记录的map；key就是列名，值就是对应的值
	public Map<String, Object> getEmpBIdReturnMap(Integer id);
	
	//多条记录封装一个map：Map<Integer,Employee>：键是这条记录的主键，值是记录封装后的javaBean
	//告诉mybatis封装这个map的时候使用哪个属性作为map的key
	@MapKey("id")
	public Map<Integer, Employee> getEmpByLastNameLikeReturnMap(String lastName);
	
	
	public List<Employee> getEmps();
	
	public void getPageByProcedure(Page page);
}
