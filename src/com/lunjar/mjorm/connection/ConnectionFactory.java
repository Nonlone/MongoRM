package com.lunjar.mjorm.connection;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;

public class ConnectionFactory {
	
	private static Map<String,MongoClient> clientMap;
	private static String dbName;
	private static String defaultAlias;
	
	public static void init(String alias, String configName) throws Exception {
		InputStream in = null;
		Properties prop = new Properties();
		in = ConnectionFactory.class.getResourceAsStream("/" + configName);
		prop.load(in);
		//将第一个设置为默认数据库
		if(null!=alias) defaultAlias=alias;
		//初始化mongoclient配置
		if(null==clientMap||!clientMap.containsKey(alias)){
			
			final MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
			builder.connectionsPerHost(Integer.parseInt(prop.get("ConnectionsPerHost").toString().trim())); // 最大连接数
			builder.threadsAllowedToBlockForConnectionMultiplier(Integer.parseInt(prop.get("ThreadsAllowedToBlockForConnectionMultiplier").toString().trim())); // 一个连接最大等待线程数
			builder.maxWaitTime(Integer.parseInt(prop.get("MaxWaitTime").toString().trim())); // 线程最大等待时长
			builder.connectTimeout(Integer.parseInt(prop.get("ConnectTimeout").toString().trim())); // 连接超时
			builder.socketTimeout(Integer.parseInt(prop.get("SocketTimeout").toString().trim())); // socket超时
			builder.socketKeepAlive(Boolean.parseBoolean(prop.get("SocketKeepAlive").toString().trim())); // socket保持时长
			builder.autoConnectRetry(Boolean.parseBoolean(prop.get("AutoConnectRetry").toString().trim())); // 自动重连
			builder.maxAutoConnectRetryTime(Integer.parseInt(prop.get("MaxAutoConnectRetryTime").toString().trim())); // 最大自动重连次数
			builder.readPreference(ReadPreference.primary()); // 读参数，从主数据库读取
			builder.writeConcern(WriteConcern.ACKNOWLEDGED); // 写参数
			
			MongoClientOptions clientOptions=builder.build();
			
			String IPAddress=prop.getProperty("IPAddress").trim();
			int port = Integer.parseInt(prop.getProperty("Port").trim());
			dbName=prop.getProperty("DBName").trim();
			ServerAddress serverAddress=new ServerAddress(IPAddress, port);
			
			MongoClient mongoClient = new MongoClient(serverAddress, clientOptions);
			build(alias,mongoClient);
			
		}
		if (null != in) {
			in.close();
			in = null;
		}
	}
	
	public static void build(String alias, MongoClient client) {
		if(null == ConnectionFactory.clientMap) {
			ConnectionFactory.clientMap = new ConcurrentHashMap<String, MongoClient>();
		}
		ConnectionFactory.clientMap.put(alias, client);
	}
	
	public static DB getDefaultDB() throws Exception{
		return getDB(defaultAlias);
	}
	
	public static DB getDB(String alias)throws Exception {
		MongoClient client=clientMap.get(alias);
		if(null == client) {
			throw new Exception("MongoClient is not initialized...");
		}
		return client.getDB(dbName);
	}
	
	public static DBCollection getCollection(String alias,String coll) throws Exception {
		MongoClient client=clientMap.get(alias);
		if(null == client) {
			throw new Exception("MongoClient is not initialized...");
		}
		return client.getDB(dbName).getCollection(coll);
	}
	
	public static void close(String alias) {
		try {
			if(null != clientMap && !clientMap.containsKey(alias) && null != clientMap.get(alias)){
				MongoClient client=clientMap.get(alias);
				clientMap.remove(alias);
				client.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
