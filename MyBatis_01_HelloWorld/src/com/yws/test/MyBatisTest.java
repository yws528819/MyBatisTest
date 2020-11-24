package com.yws.test;


import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;

import com.yws.bean.Employee;

class MyBatisTest {

	/**
	 * 1.根据xml配置文件（全局配置文件）创建一个sqlSessionFactory对象
	 * 		有数据源的一些运行环境信息
	 * 2.sql映射文件：配置了每一个sql，以及sql的封装规则等。
	 * 3.将sql映射文件注册在全局配置文件中
	 * 4.写代码：
	 * 		1）根据全局配置文件得到SqlSessionFactory；
	 * 		2）使用SqlSessionFactory工厂，获取到sqlSession对象使用它来执行增删改查
	 * 		       一个sqlSession就是代表和数据库的一次会话，用完关闭
	 * 		3）使用sql的唯一标识来告诉MyBatis执行哪个sql，sql都是保存在sql映射文件中的
	 * @throws Exception
	 */
	@Test
	void test() throws Exception {
		String resource = "mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		
		//2.获取sqlSession实例，能直接执行已经映射的sql语句
		SqlSession session = sqlSessionFactory.openSession();
		try {
			//sql的唯一标识：statement Unique identifier matching the statement to use.
			//执行sql要用的参数：parameter A parameter object to pass to the statement.
			Employee e = session.selectOne("com.yws.EmployeeMapper.selectEmp", 1);
			System.out.println(e);		
		}finally {
			session.close();
		}

	}

}
