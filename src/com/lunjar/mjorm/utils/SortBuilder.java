package com.lunjar.mjorm.utils;

import java.util.HashMap;
import java.util.Map;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 排序构造器
 * 
 * @author Ezir
 * 
 */
public class SortBuilder {
	/**
	 * 排序Map
	 */
	private Map<String, Integer> sortMap = new HashMap<String, Integer>();

	/**
	 * 屏蔽构造器
	 */
	private SortBuilder() {
	}

	/**
	 * 静态入口 升序
	 * 
	 * @param key
	 * @return
	 */
	public static SortBuilder startAsc(String key) {
		SortBuilder sb = new SortBuilder();
		sb.sortMap.put(key, 1);
		return sb;
	}

	/**
	 * 静态入口 降序
	 * 
	 * @param key
	 * @return
	 */
	public static SortBuilder startDesc(String key) {
		SortBuilder sb = new SortBuilder();
		sb.sortMap.put(key, -1);
		return sb;
	}

	/**
	 * 实例方法 升序
	 * 
	 * @param key
	 * @return
	 */
	public SortBuilder asc(String key) {
		this.sortMap.put(key, 1);
		return this;
	}

	/**
	 * 实例方法 降序
	 * 
	 * @param key
	 * @return
	 */
	public SortBuilder desc(String key) {
		this.sortMap.put(key, -1);
		return this;
	}

	/**
	 * 获得DBObject对象
	 * 
	 * @return
	 */
	public DBObject get() {
		if (!sortMap.isEmpty()) {
			DBObject dbObject = new BasicDBObject(sortMap);
			return dbObject;
		} else {
			return null;
		}

	}
}
