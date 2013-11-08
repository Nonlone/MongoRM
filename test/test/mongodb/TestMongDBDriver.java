package test.mongodb;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.sql.rowset.JdbcRowSet;

import org.bson.types.ObjectId;

import misc.DBPool;

import test.mongodb.model.TaoItem;

import com.lunjar.mjorm.utils.MongoConverter;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.QueryBuilder;
import com.seahold.dao.Dao;
import com.seahold.dao.sql.impl.Conditions;
import com.seahold.dao.sql.impl.Orders;
import com.seahold.dao.sql.impl.Pager;
import com.seahold.dao.sql.impl.SqlC;

public class TestMongDBDriver {

	public static void main(String[] args) throws UnknownHostException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, IllegalArgumentException, InvocationTargetException{
		
		List<TaoItem> tiList = new ArrayList<TaoItem>();
		
		Mongo mongo = new Mongo();
		
		for(String dbName : mongo.getDatabaseNames()){
			System.out.println(dbName);
		}
		
		DB db = mongo.getDB("test");
		DBCollection test = db.getCollection("test");
		
		DBCursor cursor = test.find();
		while(cursor.hasNext()){
			DBObject dbo = cursor.next();
			TaoItem ti = MongoConverter.dbobject2Bean(TaoItem.class, dbo);
			tiList.add(ti);
		}
		System.out.println("-----------tiList start---------");
		for(TaoItem ti:tiList){
			System.out.println(ti.toJSONObject().toString());
		}
		System.out.println("-----------tiList end---------");
		
		
		String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";   
		List<String> testList = new ArrayList<String>();
		Random random = new Random();
		for(int i=0;i<10;i++){
			int length = random.nextInt(10);
			StringBuffer sb = new StringBuffer(length);
			for(int k=0;k<length;k++){
				int ind = random.nextInt(base.length());
				sb.append(base.charAt(ind));
			}
			testList.add(sb.toString());
		}
		if(!tiList.isEmpty()){
			System.out.println();
			
			TaoItem ti = tiList.get(0);
			ti.setTestList(testList.toArray(new String[testList.size()]));
			
			DBObject dbo = MongoConverter.bean2DBObject(ti);
			DBCollection dbc = db.getCollection("testCol");
			dbc.insert(dbo);
			System.out.println("dbo: "+dbo);
			
			TaoItem testTi = new TaoItem();
			System.out.println(MongoConverter.bean2DBObject(testTi).get("_id"));
			
			TaoItem testBean = MongoConverter.dbobject2Bean(TaoItem.class, dbo);
			System.out.println("bean: "+testBean.toJSONObject().toString());
			dbo =dbc.findOne();
			System.out.println("from dbo: "+dbo);
			testBean = MongoConverter.dbobject2Bean(TaoItem.class, dbo);
			System.out.println("from bean: "+testBean.toJSONObject().toString());
		}
		
		QueryBuilder qb = QueryBuilder.start("nick").is("robot627").and("count").lessThan(5).and("count").greaterThanEquals(10);
		
		
		mongo.close();
	}
	
}
