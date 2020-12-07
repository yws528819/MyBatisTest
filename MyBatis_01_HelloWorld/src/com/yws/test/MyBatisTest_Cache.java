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

class MyBatisTest_Cache {

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
	 * 两级缓存
	 * 	一级缓存：（本地缓存）sqlSession级别的缓存。一级缓存是一直开启的；SQLSession级别的一个Map
	 * 		与数据库的同一次会话期间查询到的数据会放在本地缓存中；
	 * 		以后如果需要获取到相同的数据，直接从缓存中拿，就没必要去数据库查
	 * 
	 * 		一级缓存失效情况（没有使用到当前一级缓存的情况，效果就是，还需要向数据库再次发出查询）
	 *      1.Sq1Sess1on不同。
     *      2.Sq1Sess1on相同，査询条件不同（当前一级緩存中还没有这个数据）
     *      3.sqlSession相同，两次查询间执行了增删改操作（这次增删改可能对当前数据有影响）
     *      	3.1扩展：不同的sqlSession做了增删改（一级缓存未失效）
     *      4.sqlSession相同，手动清除了一级缓存（缓存清空）
	 * 
	 * 
	 *  二级缓存：（全局缓存）；基于namespace级别的缓存；一个namespace对应一个二级缓存
	 *  	工作机制：
	 *  	1.一个会话，查询一条数据，这个数据就被放在当前会话的一级缓存中
	 *  	2.如果会话关闭，一级缓存中的数据就会被保存到二级缓存中；新的会话查询信息，就可以参照二级缓存中的内容
	 *  	3.sqlSession -> EmployeeMapper -> Employee
	 *  				 -> DepartmentMapper -> Department
	 *  	  不同namespace查出的数据会放在自己对应的缓存中(map)
	 *  	效果：数据会从二级缓存中获取
	 *  		查出的数据都会默认先放在一级缓存中。
	 *  		只有会话提交或者关闭以后，一级缓存中的数据才会转移到二级缓存中去
	 *  
	 *  使用：
	 *   	1)开启全局二级缓存配置<setting name="cacheEnabled" value="true"/>
	 *   	2)去mapper.xml中配置使用二级缓存:
	 *   		<cache></cache>
	 *   	3)我们的POJO需要实现序列化接口
	 *   
	 *   和缓存有关的设置/属性：
	 *   	1）cacheEnabled=true：false：关闭缓存（二级缓存关闭）（一级缓存一直可用的）
	 *   	2）每个select标签都有useCache="true"：
	 *   		false：不使用缓存（一级缓存依然使用，二级缓存不使用）
	 *   	3）每个增删改标签的：flushCache="true"；（一级二级都会清除）
	 *   		增删改执行完成后就会清除缓存；
	 *   		测试：flushCache="true"：一级缓存就清空了；二级缓存也会被清除
	 *   	             查询标签：flushCache="false"；
	 *   			如果flushCache="true"；每次查询之后都会清空缓存；缓存是没有被使用的
	 *   	4）sqlSession.clearCache();只是清除当前session的一级缓存；
	 *   	5）localCacheScope：本地缓存作用域（一级缓存SESSION）；当前会话的所有数据保存在会话缓存中； 
	 *   			STATEMENT：可以禁用一级缓存；
	 *   
	 *   第三方缓存整合：
	 *   	1）导入第三方缓存包即可
	 *   	2）导入与第三方整合缓存的适配包；官方下载
	 *   	3）mapper.xml中使用自定义缓存
	 *   	<cache type="org.mybatis.caches.ehcache.EhcacheCache"></cache>
	 *   
	 * @throws Exception 
	 *   		
	 * @throws Exception 
	 */
	@Test
	public void testSecondLevelCache() throws Exception {
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession session = sqlSessionFactory.openSession();
		SqlSession session2 = sqlSessionFactory.openSession();
		
		try {
			EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
			Employee emp = mapper.getEmpById(1);
			session.close();
			
			EmployeeMapper mapper2 = session2.getMapper(EmployeeMapper.class);
			//mapper2.addEmp(new Employee(null, "Lily", "123@qq.com", "1"));
			
			session2.clearCache();
			//第二次查询是从二级缓存中拿数据，并没有发送新的sql
			Employee emp2 = mapper2.getEmpById(1);
			session2.close();
			
			System.out.println(emp == emp2);
		} finally {
			session.close();
			session2.close();
		}
	}
	
	
	
	
	
	
	
	@Test
	public void testFirstLevelCache() throws Exception {
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession session = sqlSessionFactory.openSession();
		SqlSession session2 = sqlSessionFactory.openSession();
		
		try {
			EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
			Employee emp = mapper.getEmpById(1);
			System.out.println(emp);
			
			Employee emp2 = mapper.getEmpById(1);
			System.out.println(emp2);
			
			//1.sqlSession不同
//			EmployeeMapper mapper2 = session2.getMapper(EmployeeMapper.class);
//			Employee emp2 = mapper2.getEmpById(1);
			
			
			//2.sqlSession相同，查询条件不同（当前一级缓存中还没有这个数据）
			//Employee emp2 = mapper.getEmpById(3);	
			
			//3.sqlSession相同，两次查询间执行了增删改操作（这次增删改可能对当前数据有影响）
			//mapper.addEmp(new Employee(null, "lucy", "123@123.com", "0"));
			//3.1扩展：不同的sqlSession做了增删改（一级缓存未失效）
//			EmployeeMapper mapper2 = session2.getMapper(EmployeeMapper.class);
//			mapper2.addEmp(new Employee(null, "lucy2", "123@123.com", "0"));
			
			//4.sqlSession相同，手动清除了一级缓存（缓存清空）
//			session.clearCache();
//			Employee emp2 = mapper.getEmpById(1);
			System.out.println(emp == emp2);
		} finally {
			session.close();
			session2.close();
		}
		
	}
	
}
