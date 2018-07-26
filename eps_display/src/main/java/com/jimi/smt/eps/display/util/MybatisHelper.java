package com.jimi.smt.eps.display.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 * Mybatis辅助类
 * <br>
 * <b>2018年3月28日</b>
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
public class MybatisHelper{
	
	/**
	 * 默认配置文件路径
	 */
	private static final String DEFAULT_PATH = "mybatis-config.xml";
	
	private static SqlSessionFactoryBuilder builder;
	
	/**
	 * 多数据源
	 */
	private static Map<String, SqlSessionFactory> factories;
	
	/**
	 * Session包装类
	 * @param <M>
	 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
	 */
	public class MybatisSession<M>{
		private SqlSession session;
		private M mapper;
		
		public MybatisSession(SqlSession session, M mapper) {
			this.session = session;
			this.mapper = mapper;
		}

		public SqlSession getSession() {
			return session;
		}
		
		public void commit() {
			session.commit();
		}
		
		public void rollback() {
			session.rollback();
		}

		public M getMapper() {
			return mapper;
		}
		
		public void colseSession(){
			session.close();
		}
	}
	
	
	static {
		//创建工厂建造者
		factories = new HashMap<String, SqlSessionFactory>();
		builder = new SqlSessionFactoryBuilder();
	}
	
	
	/**
	 * 获取MybatisSession对象
	 * @param confPath 相对classpath的文件名;
	 * @param mapperType mapper的类型
	 * @param datasourceName 数据源名称
	 * @return 返回一个MybatisSession对象
	 * @throws IOException 文件不存在
	 */
	public synchronized static <T> MybatisSession<T> getMS(String confPath ,Class<T> mapperType, String datasourceName) throws IOException {
		SqlSession session = null;
		if(datasourceName == null) {
			//如果不存在默认工厂，则创建默认工厂
			if(factories.get("default") == null) {
				SqlSessionFactory defaultFactory = builder.build(Resources.getResourceAsStream(confPath));
				factories.put("default", defaultFactory);
			}
			session = factories.get("default").openSession();
		}else {
			//如果不存在该工厂，则创建一个
			if(factories.get(datasourceName) == null) {
				SqlSessionFactory factory = builder.build(Resources.getResourceAsStream(confPath), datasourceName);
				factories.put(datasourceName, factory);
			}
			session = factories.get(datasourceName).openSession();
		}
		T mapper = session.getMapper(mapperType);
		return new MybatisHelper().new MybatisSession<T>(session,mapper);
	}
	
	
	/**
	 * 获取MybatisSession对象
	 * @param confPath 相对classpath的文件名;
	 * @param mapperType mapper的类型
	 * @return 返回一个MybatisSession对象
	 * @throws IOException 文件不存在
	 */
	public static <T> MybatisSession<T> getMS(String confPath ,Class<T> mapperType) throws IOException {
		return getMS(confPath, mapperType, null);
	}
	
	
	/**
	 * 获取MybatisSession对象,文件名固定为classpath下的mybatis-config.xml
	 * @param mapperType mapper的类型
	 * @return 返回一个MybatisSession对象
	 * @throws IOException 文件不存在
	 */
	public static <T> MybatisSession<T> getMS(Class<T> mapperType) throws IOException {
		return getMS(DEFAULT_PATH, mapperType, null);
	}
	
}


