package misc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

public class DBPool {
	private final static String defaultBoneCPConfigName = "keywordv4";
	private final static Map<String,BoneCP> boneCPMap = new HashMap<String,BoneCP>();
	private static BoneCP defaultBoneCP = null;

	private static BoneCP defaultConfigBoneCP() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		BoneCPConfig config = null;
		try {
			config = new BoneCPConfig(DBPool.class.getResourceAsStream("/bonecp-config.xml"), defaultBoneCPConfigName);
			return new BoneCP(config);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private static BoneCP configBoneCP(String configName) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		BoneCPConfig config = null;
		try {
			config = new BoneCPConfig(DBPool.class.getResourceAsStream("/bonecp-config.xml"), configName);
			return new BoneCP(config);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static Connection getConnection() throws ClassNotFoundException, SQLException {
		if (null == defaultBoneCP) {
			synchronized (DBPool.class) {
				if (null == defaultBoneCP) {
					defaultBoneCP = defaultConfigBoneCP();
				}
			}
		}
		return defaultBoneCP.getConnection();
	}
	
	public static Connection getConnection(String dbName) throws ClassNotFoundException, SQLException {
		BoneCP boneCP;
		if (!boneCPMap.containsKey(dbName)) {
			synchronized (boneCPMap) {
				if (!boneCPMap.containsKey(dbName)) {
					boneCP = configBoneCP(dbName);
					boneCPMap.put(dbName, boneCP);
				}else{
					boneCP = boneCPMap.get(dbName);
				}
			}
		}else{
			boneCP = boneCPMap.get(dbName);
		}
		return boneCP.getConnection();
	}

	public static void close(Connection conn) throws SQLException {
		conn.close();
	}

	public static void close(PreparedStatement pstmt) throws SQLException {
		pstmt.close();
	}

	public static void close(ResultSet rs) throws SQLException {
		rs.close();
	}

	public static void close() {
		defaultBoneCP.close();
	}
	
	public static void close(String dbName){
		if(boneCPMap.containsKey(dbName)){
			synchronized (boneCPMap) {
				if(boneCPMap.containsKey(dbName)){
					BoneCP boneCP = boneCPMap.get(dbName);
					boneCP.close();
					boneCPMap.remove(dbName);
				}
			}
		}
	}
}
