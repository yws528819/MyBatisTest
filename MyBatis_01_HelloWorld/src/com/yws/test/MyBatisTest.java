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
 * 1.�ӿ�ʽ���
 * ԭ����                 Dao ===> DaoImpl
 * mybatis  Dao ===> xxMapper.xml
 * 
 * 2.sqlSession��������ݿ�һ�ζԻ����������ر�
 * 3.SqlSession��Connectionһ�����Ƿ��̰߳�ȫ��ÿ��ʹ��ȥ��ȡ�µĶ���
 * 4.mapper�ӿ�û��ʵ���࣬����mybatis��Ϊ����ӿ�����һ���������
 * 		(���ӿں�xml��)
 * 		EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
 * 5.������Ҫ�������ļ���
 * 		mybatis��ȫ�������ļ����������ݿ����ӳ���Ϣ�������������...ϵͳ���л�����Ϣ
 * 		sqlӳ���ļ�������ÿһ��sql����ӳ����Ϣ
 * @author mayn
 *
 */
class MyBatisTest {

	/**
	 * 1.����xml�����ļ���ȫ�������ļ�������һ��sqlSessionFactory����
	 * 		������Դ��һЩ���л�����Ϣ
	 * 2.sqlӳ���ļ���������ÿһ��sql���Լ�sql�ķ�װ����ȡ�
	 * 3.��sqlӳ���ļ�ע����ȫ�������ļ���
	 * 4.д���룺
	 * 		1������ȫ�������ļ��õ�SqlSessionFactory��
	 * 		2��ʹ��SqlSessionFactory��������ȡ��sqlSession����ʹ������ִ����ɾ�Ĳ�
	 * 		       һ��sqlSession���Ǵ�������ݿ��һ�λỰ������ر�
	 * 		3��ʹ��sql��Ψһ��ʶ������MyBatisִ���ĸ�sql��sql���Ǳ�����sqlӳ���ļ��е�
	 * @throws Exception
	 */
	@Test
	void test() throws Exception {
		//1.��ȡsqlSessionFactory����
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		
		//2.��ȡsqlSessionʵ������ֱ��ִ���Ѿ�ӳ���sql���
		SqlSession session = sqlSessionFactory.openSession();
		try {
			//sql��Ψһ��ʶ��statement Unique identifier matching the statement to use.
			//ִ��sqlҪ�õĲ�����parameter A parameter object to pass to the statement.
			Employee e = session.selectOne("com.yws.dao.EmployeeMapper.getEmpById", 1);
			System.out.println(e);		
		}finally {
			session.close();
		}

	}

	/**
	 * ��ȡsqlSessionFactory����
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
		//1.��ȡsqlSessionFactory����
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		
		//2.��ȡsqlSessionʵ������ֱ��ִ���Ѿ�ӳ���sql���
		SqlSession session = sqlSessionFactory.openSession();
		
		try {
			//3.��ȡ�ӿڵ�ʵ�������
			//��Ϊ�ӿ��Զ�����һ��������󣬴������ȥִ����ɾ�Ĳ鷽��
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
		//1.��ȡsqlSessionFactory����
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		
		//2.��ȡsqlSessionʵ������ֱ��ִ���Ѿ�ӳ���sql���
		SqlSession session = sqlSessionFactory.openSession();
		
		try {
			//3.��ȡ�ӿڵ�ʵ�������
			//��Ϊ�ӿ��Զ�����һ��������󣬴������ȥִ����ɾ�Ĳ鷽��
			EmployeeMapperAnnotation mapper = session.getMapper(EmployeeMapperAnnotation.class);			
			Employee emp = mapper.getEmployee(1);
			System.out.println(emp);
		}finally {
			session.close();
		}
	}
	
	/**
	 * ������ɾ��
	 * 1.mybatis������ɾ��ֱ�Ӷ����������ͷ���ֵ
	 * 		Integer,Long,Boolean
	 * 2.������Ҫ�ֶ��ύ����
	 * 		sqlSessionFactory.openSession(); -->�ֶ��ύ
	 * 		sqlSessionFactory.openSession(true); -->�Զ��ύ
	 * @throws Exception
	 */
	@Test
	void test03() throws Exception {
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		//1.��ȡ����sqlSession�����Զ��ύ����
		SqlSession session = sqlSessionFactory.openSession();
		
		try {
			EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
			//����
			Employee employee = new Employee(null, "jerry3", "123@qq.com", "1");
			mapper.addEmp(employee);
			System.out.println(employee.getId());
			
			//�޸�
//			Employee employee = new Employee(1, "jerry", "jerry@qq.com", "0");
//			mapper.updateEmp(employee);
			
			//ɾ��
//			mapper.deleteEmpById(3);
			
			//2.�ֶ��ύ����
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
				
				//��ѯ��ʱ�����ĳЩ����û������sqlƴװ��������
				//1.��where�������1=1���Ժ��������"and xxx"
				//2.mybatisʹ��where��ǩ�������еĲ�ѯ�����������ڡ�mybatis�Ὣwhere��ǩ��ƴװ������sql�������and����orɾ��
					//where��ǩֻ��ȥ����һ���������and����or
				
				//����trim
				/*
				 * emps = mapper.getEmpsByConditionTrim(emp); emps.stream().forEach(e ->
				 * System.out.println(e));
				 */
				
				//����choose
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
