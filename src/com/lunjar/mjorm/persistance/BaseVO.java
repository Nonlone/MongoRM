package com.lunjar.mjorm.persistance;

import org.bson.types.ObjectId;


import com.lunjar.mjorm.exception.ObjectIdIsNullException;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;

public abstract class BaseVO {

	private ObjectId _id = new ObjectId();

	public ObjectId get_id() {
		return _id;
	}

	public void set_id(ObjectId _id) {
		this._id = _id;
	}

	public DBObject toQuery() throws ObjectIdIsNullException{
		if(get_id()!=null){
			QueryBuilder qb = QueryBuilder.start("_id").is(get_id());
			return qb.get();
		}else{
			throw new ObjectIdIsNullException(); 
		}
	}
}