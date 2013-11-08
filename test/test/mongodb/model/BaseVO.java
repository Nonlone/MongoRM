package test.mongodb.model;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.bson.types.ObjectId;
import org.json.JSONObject;

import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;

public class BaseVO {

	private ObjectId _id = new ObjectId();

	
	public ObjectId get_id() {
		return _id;
	}

	public void set_id(ObjectId _id) {
		this._id = _id;
	}

	public DBObject toQuery() {
		QueryBuilder qb = QueryBuilder.start("_id").is(get_id());
		return qb.get();
	}

	public JSONObject toJSONObject() {
		JSONObject json = new JSONObject(this);
		json.put("_id", get_id());
		Map<String, Method> methodMap = new HashMap<String, Method>();
		for (Method method : this.getClass().getMethods()) {
			methodMap.put(method.getName(), method);
		}
		for (Object key : json.keySet()) {
			String methodName = "get" + upperFirst((String) key);
			if (methodMap.containsKey(methodName) && methodMap.get(methodName) != null) {
				Method method = methodMap.get(methodName);
				if (method != null && method.getReturnType() == Date.class) {
					//时间类型转换成毫秒记录
					try {
						Object obj = method.invoke(this, null);
						if (obj != null && obj instanceof Date) {
							json.put((String) key, ((Date) obj).getTime());
						}
					} catch (Exception e) {
						continue;
					}
				}
			}
		}
		return json;
	}

	/**
	 * 首字母大写
	 * 
	 * @param str
	 * @return
	 */
	private static String upperFirst(String str) {
		if (null == str || str.length() < 1) {
			return null;
		}
		StringBuffer sb = new StringBuffer(str.substring(0, 1).toUpperCase());
		sb.append(str.substring(1));
		return sb.toString();
	}

	/**
	 * 首字母小写
	 * 
	 * @param str
	 * @return
	 */
	private static String lowerFirst(String str) {
		if (null == str || str.length() < 1) {
			return null;
		}
		StringBuffer sb = new StringBuffer(str.substring(0, 1).toLowerCase());
		sb.append(str.substring(1));
		return sb.toString();
	}
}
