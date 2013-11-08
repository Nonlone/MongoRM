package com.lunjar.mjorm.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 默认包装器，DBObject包装器接口实现类
 * 
 * @author Ezir
 * 
 */
public class MongoConverter {

	public static <T> T dbobject2Bean(Class<?> classOfT, DBObject dboject)  {
		//返回泛型
		T entity;
		try {
			entity = (T) classOfT.newInstance();
		} catch (InstantiationException e) {
			return null;
		} catch (IllegalAccessException e) {
			return null;
		}
		
		Map<String, Method> methodMap = new HashMap<String, Method>();
		//初始化class
		for (Method method : classOfT.getMethods()) {
			methodMap.put(method.getName(), method);
		}
		Set<String> keySet = dboject.keySet();
		for (String key : keySet) {
			//注入类
			String methodName = "set" + upperFirst(key);
			if (methodMap.containsKey(methodName) && dboject.get(key) != null) {
				//反射注入
				Method method = methodMap.get(methodName);
				Object params = dboject.get(key);
				try {
					method.invoke(entity, params);
				} catch (IllegalArgumentException e) {
					continue;
				} catch (IllegalAccessException e) {
					continue;
				} catch (InvocationTargetException e) {
					continue;
				}
			}
		}
		return entity;
	}

	public static <T> DBObject bean2DBObject(T entity) {
		//空判断
		if (entity == null) {
			return null;
		}
		DBObject dboject = new BasicDBObject();
		Map<String, Method> methodMap = new HashMap<String, Method>();
		for (Method method : entity.getClass().getMethods()) {
			methodMap.put(method.getName(), method);
		}
		Set<Field> fieldSet = new HashSet<Field>();
		Class clazz = entity.getClass();
		while(clazz!=Object.class){
			for (Field field : clazz.getDeclaredFields()) {
				fieldSet.add(field);
			}
			clazz = clazz.getSuperclass();
		}
		for (Field field : fieldSet) {
			String methodName = "get" + upperFirst(field.getName());
			if (methodMap.containsKey(methodName)) {
				Method method = methodMap.get(methodName);
				//用get方法取得bean的结果
				try {
					Object obj = method.invoke(entity, null);
					if (obj == null) {
						continue;
					} else if (obj instanceof Integer) {
						int value = ((Integer) obj).intValue();
						dboject.put(field.getName(), value);
					} else if (obj instanceof String) {
						String value = (String) obj;
						dboject.put(field.getName(), value);
					} else if (obj instanceof Double) {
						double value = ((Double) obj).doubleValue();
						dboject.put(field.getName(), value);
					} else if (obj instanceof Long) {
						long value = ((Long) obj).longValue();
						dboject.put(field.getName(), value);
					} else if (obj instanceof Float) {
						float value = ((Float) obj).floatValue();
						dboject.put(field.getName(), value);
					} else if (obj instanceof Boolean) {
						boolean value = ((Boolean) obj).booleanValue();
						dboject.put(field.getName(), value);
					} else if (obj instanceof Date) {
						Date value = (Date) obj;
						dboject.put(field.getName(), value);
					} else if (obj instanceof Collection<?>) {
						Collection<?> value = (Collection<?>) obj;
						BasicDBList bdbl = new BasicDBList();
						for (Object o : value) {
							//递归转换
							bdbl.add(bean2DBObject(o));
						}
						dboject.put(field.getName(), value);
					} else if(obj instanceof Object[]){
						Object[] value = (Object[]) obj;
						BasicDBList bdbl = new BasicDBList();
						for (Object o : value) {
							//递归转换
							bdbl.add(bean2DBObject(o));
						}
						dboject.put(field.getName(), value);
					}else {
						//其他情况
						dboject.put(field.getName(), obj);
					}
				} catch (Exception e) {
					//TODO: comment it
					e.printStackTrace();
					continue;
				}
			}
		}
		return dboject;
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
}
