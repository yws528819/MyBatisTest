package com.yws.test;


import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yws.bean.Employee;
import com.yws.dao.EmployeeMapper;


class MyBatisTest_PageHelper {


	@Test
	void test() throws Exception {
		//1.获取sqlSessionFactory对象
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		
		//2.获取sqlSession实例，能直接执行已经映射的sql语句
		SqlSession session = sqlSessionFactory.openSession();
		try {
			EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
			Page<Object> page = PageHelper.startPage(1, 1);
			List<Employee> emps = mapper.getEmps();
			
			emps.stream().forEach(e -> System.out.println(e));
			
			//PageInfo<Employee> info = new PageInfo<>(emps);
			PageInfo<Employee> info = new PageInfo<>(emps, 4);
			
			/*
			 * System.out.println("当前页码：" + page.getPageNum()); System.out.println("总记录数：" +
			 * page.getTotal()); System.out.println("每页的记录数：" + page.getPageSize());
			 * System.out.println("总页码：" + page.getPages());
			 */
			System.out.println("当前页码：" + info.getPageNum());
			System.out.println("总记录数：" + info.getTotal());
			System.out.println("每页的记录数：" + info.getPageSize());
			System.out.println("总页码：" + info.getPages());
			System.out.println("是否第一页：" + info.isIsFirstPage());
			System.out.println("是否最后一页：" + info.isIsLastPage());
			
			int[] nums = info.getNavigatepageNums();
			Arrays.stream(nums).forEach(e -> System.out.println(e));
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
	

}
