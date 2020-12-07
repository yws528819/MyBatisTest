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
	
	/**
	 * ��������
	 * 	һ�����棺�����ػ��棩sqlSession����Ļ��档һ��������һֱ�����ģ�SQLSession�����һ��Map
	 * 		�����ݿ��ͬһ�λỰ�ڼ��ѯ�������ݻ���ڱ��ػ����У�
	 * 		�Ժ������Ҫ��ȡ����ͬ�����ݣ�ֱ�Ӵӻ������ã���û��Ҫȥ���ݿ��
	 * 
	 * 		һ������ʧЧ�����û��ʹ�õ���ǰһ������������Ч�����ǣ�����Ҫ�����ݿ��ٴη�����ѯ��
	 *      1.Sq1Sess1on��ͬ��
     *      2.Sq1Sess1on��ͬ����ѯ������ͬ����ǰһ�������л�û��������ݣ�
     *      3.sqlSession��ͬ�����β�ѯ��ִ������ɾ�Ĳ����������ɾ�Ŀ��ܶԵ�ǰ������Ӱ�죩
     *      	3.1��չ����ͬ��sqlSession������ɾ�ģ�һ������δʧЧ��
     *      4.sqlSession��ͬ���ֶ������һ�����棨������գ�
	 * 
	 * 
	 *  �������棺��ȫ�ֻ��棩������namespace����Ļ��棻һ��namespace��Ӧһ����������
	 *  	�������ƣ�
	 *  	1.һ���Ự����ѯһ�����ݣ�������ݾͱ����ڵ�ǰ�Ự��һ��������
	 *  	2.����Ự�رգ�һ�������е����ݾͻᱻ���浽���������У��µĻỰ��ѯ��Ϣ���Ϳ��Բ��ն��������е�����
	 *  	3.sqlSession -> EmployeeMapper -> Employee
	 *  				 -> DepartmentMapper -> Department
	 *  	  ��ͬnamespace��������ݻ�����Լ���Ӧ�Ļ�����(map)
	 *  	Ч�������ݻ�Ӷ��������л�ȡ
	 *  		��������ݶ���Ĭ���ȷ���һ�������С�
	 *  		ֻ�лỰ�ύ���߹ر��Ժ�һ�������е����ݲŻ�ת�Ƶ�����������ȥ
	 *  
	 *  ʹ�ã�
	 *   	1)����ȫ�ֶ�����������<setting name="cacheEnabled" value="true"/>
	 *   	2)ȥmapper.xml������ʹ�ö�������:
	 *   		<cache></cache>
	 *   	3)���ǵ�POJO��Ҫʵ�����л��ӿ�
	 *   
	 *   �ͻ����йص�����/���ԣ�
	 *   	1��cacheEnabled=true��false���رջ��棨��������رգ���һ������һֱ���õģ�
	 *   	2��ÿ��select��ǩ����useCache="true"��
	 *   		false����ʹ�û��棨һ��������Ȼʹ�ã��������治ʹ�ã�
	 *   	3��ÿ����ɾ�ı�ǩ�ģ�flushCache="true"����һ���������������
	 *   		��ɾ��ִ����ɺ�ͻ�������棻
	 *   		���ԣ�flushCache="true"��һ�����������ˣ���������Ҳ�ᱻ���
	 *   	             ��ѯ��ǩ��flushCache="false"��
	 *   			���flushCache="true"��ÿ�β�ѯ֮�󶼻���ջ��棻������û�б�ʹ�õ�
	 *   	4��sqlSession.clearCache();ֻ�������ǰsession��һ�����棻
	 *   	5��localCacheScope�����ػ���������һ������SESSION������ǰ�Ự���������ݱ����ڻỰ�����У� 
	 *   			STATEMENT�����Խ���һ�����棻
	 *   
	 *   �������������ϣ�
	 *   	1��������������������
	 *   	2����������������ϻ������������ٷ�����
	 *   	3��mapper.xml��ʹ���Զ��建��
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
			//�ڶ��β�ѯ�ǴӶ��������������ݣ���û�з����µ�sql
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
			
			//1.sqlSession��ͬ
//			EmployeeMapper mapper2 = session2.getMapper(EmployeeMapper.class);
//			Employee emp2 = mapper2.getEmpById(1);
			
			
			//2.sqlSession��ͬ����ѯ������ͬ����ǰһ�������л�û��������ݣ�
			//Employee emp2 = mapper.getEmpById(3);	
			
			//3.sqlSession��ͬ�����β�ѯ��ִ������ɾ�Ĳ����������ɾ�Ŀ��ܶԵ�ǰ������Ӱ�죩
			//mapper.addEmp(new Employee(null, "lucy", "123@123.com", "0"));
			//3.1��չ����ͬ��sqlSession������ɾ�ģ�һ������δʧЧ��
//			EmployeeMapper mapper2 = session2.getMapper(EmployeeMapper.class);
//			mapper2.addEmp(new Employee(null, "lucy2", "123@123.com", "0"));
			
			//4.sqlSession��ͬ���ֶ������һ�����棨������գ�
//			session.clearCache();
//			Employee emp2 = mapper.getEmpById(1);
			System.out.println(emp == emp2);
		} finally {
			session.close();
			session2.close();
		}
		
	}
	
}
