package com.yws.test;


import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;

import com.yws.bean.Employee;
import com.yws.dao.EmployeeMapper;
import com.yws.dao.EmployeeMapperAnnotation;

/**
 * 1.接口式编程
 * 原生：                 Dao ===> DaoImpl
 * mybatis  Dao ===> xxMapper.xml
 * 
 * 2.sqlSession代表和数据库一次对话；用完必须关闭
 * 3.SqlSession和Connection一样都是非线程安全。每次使用去获取新的对象。
 * 4.mapper接口没有实现类，但是mybatis会为这个接口生成一个代理对象。
 * 		(将接口和xml绑定)
 * 		EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
 * 5.两个重要的配置文件：
 * 		mybatis的全局配置文件，包含数据库连接池信息，事务管理器等...系统运行环境信息
 * 		sql映射文件：保存每一个sql语句的映射信息
 * @author mayn
 *
 */
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
		//1.获取sqlSessionFactory对象
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		
		//2.获取sqlSession实例，能直接执行已经映射的sql语句
		SqlSession session = sqlSessionFactory.openSession();
		try {
			//sql的唯一标识：statement Unique identifier matching the statement to use.
			//执行sql要用的参数：parameter A parameter object to pass to the statement.
			Employee e = session.selectOne("com.yws.dao.EmployeeMapper.getEmpById", 1);
			System.out.println(e);		
		}finally {
			session.close();
		}

	}

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
	void test01() throws Exception {
		//1.获取sqlSessionFactory对象
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		
		//2.获取sqlSession实例，能直接执行已经映射的sql语句
		SqlSession session = sqlSessionFactory.openSession();
		
		try {
			//3.获取接口的实现类对象
			//会为接口自动创建一个代理对象，代理对象去执行增删改查方法
			EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
			System.out.println(mapper.getClass());
			Employee emp = mapper.getEmpById(1);
			System.out.println(emp);
		} finally {
			session.close();
		}
	}
	
	@Test
	void test02() throws Exception {
		//1.获取sqlSessionFactory对象
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		
		//2.获取sqlSession实例，能直接执行已经映射的sql语句
		SqlSession session = sqlSessionFactory.openSession();
		
		try {
			//3.获取接口的实现类对象
			//会为接口自动创建一个代理对象，代理对象去执行增删改查方法
			EmployeeMapperAnnotation mapper = session.getMapper(EmployeeMapperAnnotation.class);			
			Employee emp = mapper.getEmployee(1);
			System.out.println(emp);
		}finally {
			session.close();
		}
	}

}
