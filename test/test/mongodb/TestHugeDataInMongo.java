package test.mongodb;

import java.lang.reflect.InvocationTargetException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import misc.DBPool;
import test.mongodb.model.CatagoryPropValue;

import com.lunjar.mjorm.utils.MongoConverter;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.seahold.dao.Dao;
import com.seahold.dao.sql.SqlMaker;
import com.seahold.dao.sql.impl.Pager;
import com.seahold.dao.sql.impl.SqlC;

public class TestHugeDataInMongo {
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, UnknownHostException, IllegalArgumentException, InvocationTargetException{
		
		Mongo mongo = new Mongo();
		DB db = mongo.getDB("test");
		DBCollection testSpeed = db.getCollection("testSpeed");
		
		String dbName = "test";
		Dao cpvDao = Dao.getInstance(CatagoryPropValue.class, DBPool.getConnection(dbName));
		List<CatagoryPropValue> cpvList = new ArrayList<CatagoryPropValue>();
		int startPage=53;
		int endPage=100;
		int pageSize = 100000;
		SqlC sql = SqlC.select(CatagoryPropValue.class);
		for(int i=startPage;i<endPage;i++){
			Pager pager = Pager.getPager(i, pageSize);
			cpvList = cpvDao.query(sql.limit(pager));
			for(CatagoryPropValue cpv:cpvList){
				DBObject dbo = MongoConverter.bean2DBObject(cpv);
				testSpeed.insert(dbo);
				System.out.println(dbo);
			}
		}
	}
}
