package com.utilpackage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class MybatisUtil {

	public static SqlSessionFactory getSqlSessionFactory() {
		SqlSessionFactory sqlSessionFactory = null;
		try {
			Charset charset = Charset.forName("UTF-8");
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(Resources.getResourceAsStream("sqlconfig.xml"), charset));
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
			reader.close();
		} catch (Exception e) {
			System.out.println("Problem in session factory(In MybatisUtil class)");
		}
		return sqlSessionFactory;
	}

}