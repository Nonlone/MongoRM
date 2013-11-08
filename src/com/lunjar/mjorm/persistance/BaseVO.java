package com.lunjar.mjorm.persistance;

import org.bson.types.ObjectId;

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

	public DBObject toQuery(){
		QueryBuilder qb = QueryBuilder.start("_id").is(get_id());
		return qb.get();
	}
}
