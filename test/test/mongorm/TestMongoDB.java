package test.mongorm;

import java.util.List;

import test.mongorm.dao.TaoItemDao;
import test.mongorm.model.TaoItem;

import com.lunjar.mjorm.connection.ConnectionFactory;
import com.mongodb.DB;
import com.mongodb.DBCollection;

public class TestMongoDB {
	public static void main(String[] args) throws Exception {
		System.out.println(TestMongoDB.class.getResource("").getPath());
		
		ConnectionFactory.init("testMongo", "mongodb.properties");
		
		DB db = ConnectionFactory.getDB("testMongo");
		
		DBCollection dbc = db.getCollection("testSpeed");
		
		TaoItemDao tiDao = new TaoItemDao();
		
		List<TaoItem> tiList = tiDao.find(null, null, 0, 5);
		for (TaoItem ti : tiList) {
			System.out.println(ti.get_id());
		}
	}
}
