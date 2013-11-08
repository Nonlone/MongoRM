package com.lunjar.mjorm;

import org.apache.log4j.Logger;

import com.lunjar.mjorm.connection.ConnectionFactory;
import com.mongodb.DB;

/**
 * Mongodb与JAVA的ODM，实现文档对象映射及存储管理
 * @author Mango
 * 2013-11-6
 */
public class Mjodm {
	private static Logger logger = Logger.getLogger(Mjodm.class);
	
	public Mjodm(){
		
	}

	public DB getDefaultDB(){
		try {
			return ConnectionFactory.getDefaultDB();
		} catch (Exception e) {
			logger.error("--!未能获取默认数据库，请检查第一配置数据库！",e);
			throw new RuntimeException("--!counldn't not reach the default mongo!");
		}
	}
}
