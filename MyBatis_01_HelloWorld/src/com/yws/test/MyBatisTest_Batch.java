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
import com.yws.bean.Employee;
import com.yws.dao.EmployeeMapperDynamicSQL;

public class MyBatisTest_Batch {
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
	
	 @Test
	 public void testBatchSave() throws Exception{
		 SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		 //可以执行批量操作的sqlSession
		 SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH);
		 long start = System.currentTimeMillis();
		 try {
			EmployeeMapperDynamicSQL mapper = session.getMapper(EmployeeMapperDynamicSQL.class);
			List<Employee> emps = new ArrayList<Employee>();
			for (int i=0; i<10000; i++) {
				emps.add(new Employee(null, UUID.randomUUID().toString().substring(0, 5), "a", "0", new Department(1)));
			}
			mapper.addEmps(emps);
			long end = System.currentTimeMillis();
			//批量：（预编译sql1次->设置参数10000次->执行1次）
			//Parameters: d61d9(String), a(String), 0(String)
			//执行时长：5428
			//非批量：（预编译sql->设置参数->执行 ）=> 10000次  耗时10000多毫秒
			System.out.println("执行时长：" + (end - start));
			session.commit();
		} finally {
			session.close();
		}
	 }
}
