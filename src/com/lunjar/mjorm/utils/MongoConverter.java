package com.lunjar.mjorm.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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

	public static <T> T dbobject2Bean(Class<?> classOfT, DBObject dboject) {
		//返回泛型
		T entity;
		try {
			entity = (T) classOfT.newInstance();
		} catch (InstantiationException e) {
			return null;
		} catch (IllegalAccessException e) {
			return null;
		}
		if (dboject == null)
			return null;
		Map<String, Method> methodMap = new HashMap<String, Method>();
		Set<Field> fieldSet = new HashSet<Field>();
		//初始化class
		Class<?> clazz = classOfT;
		while (clazz != Object.class) {
			for (Field field : clazz.getDeclaredFields()) {
				fieldSet.add(field);
			}
			clazz = clazz.getSuperclass();
		}
		for (Method method : classOfT.getMethods()) {
			methodMap.put(method.getName(), method);
		}
		Set<String> keySet = dboject.keySet();
		for (Field field : fieldSet) {
			//注入类
			String fieldName = field.getName();
			String methodName = "set" + upperFirst(fieldName);
			if (keySet.contains(fieldName) && methodMap.containsKey(methodName)) {
				//对应DBObject有key，注入
				Method method = methodMap.get(methodName);
				Object params = dboject.get(fieldName);
				try {
					boolean colflag = false;
					for (Class<?> fieldInterfaceType : field.getType().getInterfaces()) {
						//查看类型的全部接口
						if (fieldInterfaceType.getSimpleName().equals("Collection")) {
							//带有Collection接口
							colflag = true;
							break;
						}
					}
					if (colflag) {
						//集合类型
						BasicDBList bdbl = (BasicDBList) params;
						clazz = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
						List<T> listParams = new ArrayList<T>();
						for (int i = 0; i < bdbl.size(); i++) {
							DBObject dbObject = (DBObject) bdbl.get(i);
							listParams.add((T) MongoConverter.dbobject2Bean(clazz, dbObject));
						}
						method.invoke(entity, listParams);
					} else {
						method.invoke(entity, params);
					}
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
		while (clazz != Object.class) {
			for (Field field : clazz.getDeclaredFields()) {
				fieldSet.add(field);
			}
			clazz = clazz.getSuperclass();
		}
		for (Field field : fieldSet) {
			String methodName;
			if (field.getType() != Boolean.class && field.getType() != boolean.class) {
				methodName = "get" + upperFirst(field.getName());
			} else {
				methodName = "is" + upperFirst(field.getName());
			}
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
						dboject.put(field.getName(), bdbl);
					} else if (obj instanceof Object[]) {
						Object[] value = (Object[]) obj;
						BasicDBList bdbl = new BasicDBList();
						for (Object o : value) {
							//递归转换
							bdbl.add(bean2DBObject(o));
						}
						dboject.put(field.getName(), bdbl);
					} else {
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
