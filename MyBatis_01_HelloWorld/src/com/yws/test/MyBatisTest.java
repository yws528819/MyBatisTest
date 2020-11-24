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
		String resource = "mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		
		//2.��ȡsqlSessionʵ������ֱ��ִ���Ѿ�ӳ���sql���
		SqlSession session = sqlSessionFactory.openSession();
		try {
			//sql��Ψһ��ʶ��statement Unique identifier matching the statement to use.
			//ִ��sqlҪ�õĲ�����parameter A parameter object to pass to the statement.
			Employee e = session.selectOne("com.yws.EmployeeMapper.selectEmp", 1);
			System.out.println(e);		
		}finally {
			session.close();
		}

	}

}
