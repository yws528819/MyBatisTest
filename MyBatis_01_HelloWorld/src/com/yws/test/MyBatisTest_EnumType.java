package com.yws.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;

import com.yws.bean.Department;
import com.yws.bean.EmpStatus;
import com.yws.bean.Employee;
import com.yws.dao.EmployeeMapper;
import com.yws.dao.EmployeeMapperDynamicSQL;

public class MyBatisTest_EnumType {
	/**
	 * 获取sqlSessionFactory对象
	 * @return
	 * @throws IOException
	 */
	private SqlSessionFactory getSqlSessionFactory() throws IOException {
		String resource = "mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		return sqlSessionFactory;
	}
	
	/**
	 * 默认MyBatis在处理枚举对象的时候保存的是枚举的名字：默认使用EnumTypeHandler
	 * 改变使用：EnumOrdinalTypeHandler
	 */
	 @Test
	 public void testEnum() throws Exception{
		 SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		 //可以执行批量操作的sqlSession
		 SqlSession session = sqlSessionFactory.openSession();
		 try {
			EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
			Employee employee = new Employee(null, "test_enum2", "223@enum.com", "0");
			mapper.addEmp(employee);
			session.commit();
		} finally {
			session.close();
		}
	 }
	 
	 @Test
	 public void testEnumUse() {
		 EmpStatus login = EmpStatus.LOGIN;
		 System.out.println("枚举的索引：" + login.ordinal());
		 System.out.println("枚举的名字：" + login.name());
	 }
}
