package com.ljm.util;

import java.io.IOException;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class MyUtil {
	//1.读取配置文件
	private static SqlSessionFactory factory;
	static{
		try {
			factory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader("mybatis.xml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//创建session
	public static SqlSession getsession(){
		return factory.openSession();
	}
	//关闭session
	public static void closeSession(SqlSession session){
		if(session != null) session.close();
	}
}
