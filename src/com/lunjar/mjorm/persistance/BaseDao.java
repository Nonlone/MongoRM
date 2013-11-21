package com.lunjar.mjorm.persistance;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import com.lunjar.mjorm.annotation.DBCollectionName;
import com.lunjar.mjorm.connection.ConnectionFactory;
import com.lunjar.mjorm.exception.ObjectIdIsNullException;
import com.lunjar.mjorm.exception.UndefinedDBCollectionNameException;
import com.lunjar.mjorm.persistance.BaseVO;
import com.lunjar.mjorm.utils.MongoConverter;
import com.lunjar.mjorm.utils.SortBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;

public class BaseDao<T extends BaseVO> {
	/**
	 * 继承擦除类
	 */
	private Class<? extends BaseVO> clazz;
	private DB db;

	@SuppressWarnings("unchecked")
	public BaseDao() throws Exception {
		clazz = (Class<? extends BaseVO>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		db = ConnectionFactory.getDefaultDB();
	}

	@SuppressWarnings("unchecked")
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
	 * @param entity
	 * @throws UndefinedDBCollectionNameException
	 */
	@SuppressWarnings("hiding")
	public <T> void insert(BaseVO entity) throws UndefinedDBCollectionNameException {
		getDBCollection().insert(MongoConverter.bean2DBObject(entity));
	}

	/**
	 * 插入集合实体
	 * @param entities
	 * @throws UndefinedDBCollectionNameException
	 */
	public void insert(Collection<? extends BaseVO> entities) throws UndefinedDBCollectionNameException {
		List<DBObject> dboList = new ArrayList<DBObject>();
		for(BaseVO entity:entities){
			dboList.add(MongoConverter.bean2DBObject(entity));
		}
		getDBCollection().insert(dboList);
	}

	//删除
	/**
	 * 按照QueryBuilder的条件删除
	 * @param qb
	 * @throws UndefinedDBCollectionNameException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public void delete(QueryBuilder qb) throws UndefinedDBCollectionNameException, InstantiationException, IllegalAccessException{
		getDBCollection().remove(qb.get());
	}
	/**
	 * 删除单个实体
	 * @param entity
	 * @throws UndefinedDBCollectionNameException
	 * @throws ObjectIdIsNullException 
	 */
	public void delete(BaseVO entity) throws UndefinedDBCollectionNameException, ObjectIdIsNullException {
		getDBCollection().remove(entity.toQuery());
	}

	/**
	 * 删除集合实体
	 * @param entities
	 * @throws UndefinedDBCollectionNameException
	 * @throws ObjectIdIsNullException 
	 */
	public void delete(Collection<? extends BaseVO> entities) throws UndefinedDBCollectionNameException, ObjectIdIsNullException {
		for(BaseVO entity:entities){
			getDBCollection().remove(entity.toQuery());
		}
	}

	//更新
	/**
	 * 按照QueryBuilder条件修改，非空对象才会修改
	 * @param qb QueryBuilder
	 * @param entity 实体
	 * @throws UndefinedDBCollectionNameException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public void update(QueryBuilder qb,BaseVO entity) throws UndefinedDBCollectionNameException, InstantiationException, IllegalAccessException{
		entity.set_id(null);
		getDBCollection().update(qb.get(),MongoConverter.bean2DBObject(entity));
	}
	/**
	 * 更新单个实体
	 * @param entity
	 * @throws UndefinedDBCollectionNameException
	 * @throws ObjectIdIsNullException 
	 */
	public void update(BaseVO entity) throws UndefinedDBCollectionNameException, ObjectIdIsNullException {
		getDBCollection().update(entity.toQuery(), MongoConverter.bean2DBObject(entity));
	}

	/**
	 * 更新集合实体
	 * @param entities
	 * @throws UndefinedDBCollectionNameException
	 * @throws ObjectIdIsNullException 
	 */
	public void update(Collection<? extends BaseVO> entities) throws UndefinedDBCollectionNameException, ObjectIdIsNullException {
		for(BaseVO entity:entities){
			getDBCollection().update(entity.toQuery(), MongoConverter.bean2DBObject(entity));
		}
	}

	//查找List
	/**
	 * 查找全部记录
	 * @return
	 * @throws UndefinedDBCollectionNameException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings("hiding")
	public <T> List<T> find() throws UndefinedDBCollectionNameException, InstantiationException, IllegalAccessException {
		DBCursor dbCursor = getDBCollection().find();
		return dbCursorWrapper(clazz, dbCursor);
	}
	
	/**
	 * 查找query，返回集合
	 * @param query
	 * @return
	 * @throws UndefinedDBCollectionNameException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings("hiding")
	public <T> List<T> find(QueryBuilder query) throws UndefinedDBCollectionNameException, InstantiationException, IllegalAccessException {
		DBCursor dbCursor = getDBCollection().find(query.get());
		return dbCursorWrapper(clazz, dbCursor);
	}

	/**
	 * 查找query，返回集合
	 * @param query
	 * @param sort
	 * @return
	 * @throws UndefinedDBCollectionNameException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings("hiding")
	public <T> List<T> find(QueryBuilder query, SortBuilder sort) throws UndefinedDBCollectionNameException, InstantiationException, IllegalAccessException {
		DBCursor dbCursor = getDBCollection().find(query.get()).sort(sort.get());
		return dbCursorWrapper(clazz, dbCursor);
	}

	/**
	 * 查找query，返回集合
	 * @param query 搜索条件
	 * @param sort 排序条件
	 * @param skip 不为空会忽略limit
	 * @param limit 不为空会忽略skip
	 * @return
	 * @throws UndefinedDBCollectionNameException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings("hiding")
	public <T> List<T> find(QueryBuilder query, SortBuilder sort, Integer skip, Integer limit) throws UndefinedDBCollectionNameException, InstantiationException, IllegalAccessException {
		DBCursor dbCursor;
		if (skip != null && limit != null) {
			if (query != null) {
				dbCursor = getDBCollection().find(query.get()).sort(sort.get()).skip(skip).limit(limit);
			} else {
				dbCursor = getDBCollection().find().sort(sort.get()).skip(skip).limit(limit);
			}
		} else {
			if (query != null) {
				dbCursor = getDBCollection().find(query.get()).sort(sort.get());
			} else {
				dbCursor = getDBCollection().find().sort(sort.get());
			}
		}
		return dbCursorWrapper(clazz, dbCursor);
	}
	
	public <T> List<T> findByPage(QueryBuilder query, SortBuilder sort, Integer page, Integer pageSize) throws UndefinedDBCollectionNameException, InstantiationException, IllegalAccessException {
		DBCursor dbCursor;
		if (page != null && pageSize != null) {
			if (query != null) {
				dbCursor = getDBCollection().find(query.get()).sort(sort.get()).skip((page-1)*pageSize).limit(pageSize);
			} else {
				dbCursor = getDBCollection().find().sort(sort.get()).skip((page-1)*pageSize).limit(pageSize);
			}
		} else {
			if (query != null) {
				dbCursor = getDBCollection().find(query.get()).sort(sort.get());
			} else {
				dbCursor = getDBCollection().find().sort(sort.get());
			}
		}
		return dbCursorWrapper(clazz, dbCursor);
	}

	//查找单个
	/**
	 * 查找query，返回单个实体
	 * @param query
	 * @return
	 * @throws UndefinedDBCollectionNameException
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@SuppressWarnings("hiding")
	public <T> T findOne(QueryBuilder query) throws UndefinedDBCollectionNameException, InstantiationException, IllegalAccessException {
		DBObject dbObject = getDBCollection().findOne(query.get());
		return MongoConverter.dbobject2Bean(clazz, dbObject);
	}
	
	/**
	 * 查找第一个对象，按照类型封装返回
	 * @return
	 * @throws UndefinedDBCollectionNameException
	 */
	public <T> T findOne() throws UndefinedDBCollectionNameException{
		DBObject dbObject = getDBCollection().findOne();
		return MongoConverter.dbobject2Bean(clazz, dbObject);
	}
	
	//count
	/**
	 * 获取全部记录数
	 * @return
	 * @throws UndefinedDBCollectionNameException 
	 */
	public Long count() throws UndefinedDBCollectionNameException{
		return getDBCollection().count();
	}
	
	/**
	 * 根据条件获取记录数
	 * @param qb 条件构造器
	 * @return
	 * @throws UndefinedDBCollectionNameException
	 */
	public Long count(QueryBuilder qb) throws UndefinedDBCollectionNameException{
		return getDBCollection().count(qb.get());
	}
	
	/**
	 * DBCursor包装器，按照类型包装
	 * @param classOfT
	 * @param dbCursor
	 * @return
	 */
	@SuppressWarnings("hiding")
	private <T> List<T> dbCursorWrapper(Class<? extends BaseVO> classOfT, DBCursor dbCursor) {
		List<T> result = new ArrayList<T>();
		while (dbCursor.hasNext()) {
			T entity = MongoConverter.dbobject2Bean(classOfT, dbCursor.next());
			result.add(entity);
		}
		return result;
	}

	/**
	 * DBCollection获得器
	 * @param baseVO
	 * @return
	 * @throws UndefinedDBCollectionNameException
	 */
	private DBCollection getDBCollection() throws UndefinedDBCollectionNameException {
		String collectionName ;
		DBCollectionName dbcn = clazz.getAnnotation(DBCollectionName.class);
		if (dbcn != null ) {
			if(dbcn.name().length() > 0){
				collectionName = dbcn.name();
			}else{
				collectionName = clazz.getSimpleName();
			}
		} else {
			collectionName = clazz.getSimpleName();
		}
		return db.getCollection(collectionName);
	}
}