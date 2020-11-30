package com.yws.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
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
	
	//����һ����¼��map��key����������ֵ���Ƕ�Ӧ��ֵ
	public Map<String, Object> getEmpBIdReturnMap(Integer id);
	
	//������¼��װһ��map��Map<Integer,Employee>������������¼��������ֵ�Ǽ�¼��װ���javaBean
	//����mybatis��װ���map��ʱ��ʹ���ĸ�������Ϊmap��key
	@MapKey("id")
	public Map<Integer, Employee> getEmpByLastNameLikeReturnMap(String lastName);
}
