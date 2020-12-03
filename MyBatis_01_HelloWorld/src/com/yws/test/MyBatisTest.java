package com.yws.test;


import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;

import com.yws.bean.Department;
import com.yws.bean.Employee;
import com.yws.dao.DepartmentMapper;
import com.yws.dao.EmployeeMapper;
import com.yws.dao.EmployeeMapperAnnotation;
import com.yws.dao.EmployeeMapperDynamicSQL;
import com.yws.dao.EmployeeMapperPlus;

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
	
	/**
	 * 测试增删改
	 * 1.mybatis允许增删改直接定义以下类型返回值
	 * 		Integer,Long,Boolean
	 * 2.我们需要手动提交事务
	 * 		sqlSessionFactory.openSession(); -->手动提交
	 * 		sqlSessionFactory.openSession(true); -->自动提交
	 * @throws Exception
	 */
	@Test
	void test03() throws Exception {
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		//1.获取到的sqlSession不会自动提交事务
		SqlSession session = sqlSessionFactory.openSession();
		
		try {
			EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
			//增加
			Employee employee = new Employee(null, "jerry3", "123@qq.com", "1");
			mapper.addEmp(employee);
			System.out.println(employee.getId());
			
			//修改
//			Employee employee = new Employee(1, "jerry", "jerry@qq.com", "0");
//			mapper.updateEmp(employee);
			
			//删除
//			mapper.deleteEmpById(3);
			
			//2.手动提交事务
			session.commit();
		} finally {
			session.close();
		}
	}
	
	@Test
	void test04() throws Exception {
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession session = sqlSessionFactory.openSession();
		
		try {
			EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
			//getEmpByIdAndLastName
//			Employee emp = mapper.getEmpByIdAndLastName(1, "jerry");
//			System.out.println(emp);
			
			/*
			 * List<Employee> emps = mapper.getEmpsByLastNameLike("%e%");
			 * emps.stream().forEach(e -> System.out.println(e));
			 */
			
			/*
			 * Map<String, Object> map = mapper.getEmpBIdReturnMap(1);
			 * System.out.println(map);
			 */
			
			Map<Integer, Employee> empMaps = mapper.getEmpByLastNameLikeReturnMap("%e%");
			System.out.println(empMaps);
			
			
		}finally {
			session.close();
		}
	}
	
	@Test
	void test05() throws Exception {
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession session = sqlSessionFactory.openSession();
		try {
			EmployeeMapperPlus mapper = session.getMapper(EmployeeMapperPlus.class);
			/*
			 * Employee emp = mapper.getEmpById(1); 
			 * System.out.println(emp);
			 */
			
			/*
			 * Employee emp = mapper.getEmpAndDept(1); 
			 * System.out.println(emp);
			 * System.out.println(emp.getDeparment());
			 */
			
			Employee emp = mapper.getEmpByIdStep(2);
			 System.out.println(emp);
			 System.out.println(emp.getDeparment());
		} finally {
			session.close();
		}
	}

	@Test
	void test06() throws Exception {
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession session = sqlSessionFactory.openSession();
		
		try {
			DepartmentMapper mapper = session.getMapper(DepartmentMapper.class);
			/* Department department = mapper.getDepByIdPlus(1); */
			
			Department department = mapper.getDeptByIdStep(1);
			
			System.out.println(department.getDepartmentName());
			System.out.println(department.getEmps());
		}finally {
			session.close();
		}
	}
	
	@Test
	public void testDynamicSql() throws Exception {
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession session = sqlSessionFactory.openSession();
		
		 try {
				EmployeeMapperDynamicSQL mapper = session.getMapper(EmployeeMapperDynamicSQL.class);
				Employee emp = new Employee(null, "%e%", "", "1");
				List<Employee> emps = mapper.getEmpsByConditionIf(emp);
				emps.stream().forEach(e -> System.out.println(e));
				
				//查询的时候如果某些条件没带可能sql拼装会有问题
				//1.给where后面加上1=1，以后的条件都"and xxx"
				//2.mybatis使用where标签来将所有的查询条件包括在内。mybatis会将where标签中拼装出来的sql，多出的and或者or删掉
					//where标签只会去掉第一个多出来的and或者or
				
				//测试trim
				/*
				 * emps = mapper.getEmpsByConditionTrim(emp); emps.stream().forEach(e ->
				 * System.out.println(e));
				 */
				
				//测试choose
				/*
				 * emps = mapper.getEmpsByConditionChoose(emp); 
				 * emps.stream().forEach(e->System.out.println(e));
				 */
				/*
				 * emp = new Employee(1, "Tom", null, "1");
				 *  mapper.updateEmp(emp);
				 */
				emps = mapper.getEmpsByConditionForeach(Arrays.asList("2","1"));
				emps.stream().forEach(e->System.out.println(e));
		} finally {
			session.close();
		}
		 
		 
	}
}
