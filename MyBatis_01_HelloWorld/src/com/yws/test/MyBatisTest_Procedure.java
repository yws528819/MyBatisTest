package com.yws.test;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;

import com.yws.bean.Page;
import com.yws.dao.EmployeeMapperProcedure;

public class MyBatisTest_Procedure {
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
	 public void testProcedure() throws Exception{
		 SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		 //可以执行批量操作的sqlSession
		 SqlSession session = sqlSessionFactory.openSession();
		 long start = System.currentTimeMillis();
		 try {
			 EmployeeMapperProcedure mapper = session.getMapper(EmployeeMapperProcedure.class);
			 Page page = new Page();
			 page.setStart(2);
			 page.setEnd(5);
			 mapper.getPageByProcedure(page);
			 
			 System.out.println("总记录数：" + page.getCount());
			 System.out.println("查出的数据记录数：" + page.getEmps().size());
			 System.out.println("查出的数据：" + page.getEmps());
			session.commit();
		} finally {
			session.close();
		}
	 }
}
