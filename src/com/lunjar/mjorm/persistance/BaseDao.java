package com.lunjar.mjorm.persistance;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.lunjar.mjorm.annotation.DBCollectionName;
import com.lunjar.mjorm.connection.ConnectionFactory;
import com.lunjar.mjorm.persistance.BaseVO;
import com.lunjar.mjorm.utils.MongoConverter;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class BaseDao<T extends BaseVO> {
	/**
	 * 继承擦除类
	 */
	private Class<? extends BaseVO> clazz;
	private DB db;

	public BaseDao() throws Exception {
		clazz = (Class<? extends BaseVO>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		db = ConnectionFactory.getDefaultDB();
	}

	public BaseDao(String alias) throws Exception {
		clazz = (Class<? extends BaseVO>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		db = ConnectionFactory.getDB(alias);
	}

	public DB getDb() {
		return db;
	}

	public void setDb(DB db) {
		this.db = db;
	}

	//插入
	/**
	 * 插入单个实体
	 * 
	 * @param <T>
	 * @param dbc
	 * @param entity
	 */
	public <T> void insert(BaseVO entity) {
		getDBCollection().insert(MongoConverter.bean2DBObject(entity));
	}

	/**
	 * 插入集合实体
	 * 
	 * @param dbc
	 * @param entities
	 */
	public void insert(Collection<? extends BaseVO> entities) {
		List<DBObject> dboList = new ArrayList<DBObject>();
		for (BaseVO entity : entities) {
			dboList.add(MongoConverter.bean2DBObject(entity));
		}
		getDBCollection().insert(dboList);
	}

	//删除
	/**
	 * 删除单个实体
	 * 
	 * @param dbc
	 * @param entity
	 */
	public void delete(BaseVO entity) {
		getDBCollection().remove(entity.toQuery());
	}

	/**
	 * 删除集合实体
	 * 
	 * @param dbc
	 * @param entities
	 */
	public void delete(Collection<? extends BaseVO> entities) {
		for (BaseVO entity : entities) {
			getDBCollection().remove(entity.toQuery());
		}
	}

	//更新
	/**
	 * 更新单个实体
	 * 
	 * @param dbc
	 * @param entity
	 */
	public void update(BaseVO entity) {
		getDBCollection().update(entity.toQuery(), MongoConverter.bean2DBObject(entity));
	}

	/**
	 * 更新集合实体
	 * 
	 * @param dbc
	 * @param entities
	 */
	public void update(Collection<? extends BaseVO> entities) {
		for (BaseVO entity : entities) {
			getDBCollection().update(entity.toQuery(), MongoConverter.bean2DBObject(entity));
		}
	}

	//查找List
	/**
	 * 查找query，返回集合
	 * 
	 * @param classOfT 实体类型
	 * @param dbc 数据集
	 * @param query 搜索串
	 * @return 按照类型封装List返回
	 */
	public <T> List<T> find(DBObject query) {
		DBCursor dbCursor = getDBCollection().find(query);
		return dbCursorWrapper(clazz, dbCursor);
	}

	/**
	 * 查找query，返回集合
	 * 
	 * @param classOfT 实体类型
	 * @param dbc 数据集
	 * @param query 搜索串
	 * @param sort 排序串
	 * @return 按照类型封装List返回
	 */
	public <T> List<T> find(DBObject query, DBObject sort) {
		DBCursor dbCursor = getDBCollection().find(query).sort(sort);
		return dbCursorWrapper(clazz, dbCursor);
	}

	/**
	 * 查找query，返回集合
	 * 
	 * @param classOfT 实体类型
	 * @param dbc 数据集
	 * @param query 搜索串
	 * @param sort 排序串
	 * @param skip 跳过文档数
	 * @param limit 返回文档数
	 * @return 按照类型封装List返回
	 */
	public <T> List<T> find(DBObject query, DBObject sort, Integer skip, Integer limit) {
		DBCursor dbCursor;
		if (skip != null && limit != null) {
			if (query != null) {
				dbCursor = getDBCollection().find(query).sort(sort).skip(skip).limit(limit);
			} else {
				dbCursor = getDBCollection().find().sort(sort).skip(skip).limit(limit);
			}
		} else {
			if (query != null) {
				dbCursor = getDBCollection().find(query).sort(sort);
			} else {
				dbCursor = getDBCollection().find().sort(sort);
			}
		}
		return dbCursorWrapper(clazz, dbCursor);
	}

	//查找单个
	/**
	 * 查找query，返回单个实体
	 * 
	 * @param classOfT 实体类型
	 * @param dbc 数据集
	 * @param query 搜索串
	 * @return 按照类型封装实体返回
	 */
	public <T> T findOne(DBObject query) {
		DBObject dbObject = getDBCollection().findOne(query);
		return MongoConverter.dbobject2Bean(clazz, dbObject);
	}

	private <T> List<T> dbCursorWrapper(Class<? extends BaseVO> classOfT, DBCursor dbCursor) {
		List<T> result = new ArrayList<T>();
		while (dbCursor.hasNext()) {
			T entity = MongoConverter.dbobject2Bean(classOfT, dbCursor.next());
			result.add(entity);
		}
		return result;
	}

	private DBCollection getDBCollection() {
		DBCollectionName dbcn = clazz.getAnnotation(DBCollectionName.class);
		if (dbcn != null && dbcn.name().length() > 0) {
			return db.getCollection(dbcn.name());
		} else {
			return db.getCollection(clazz.getName());
		}
	}
}