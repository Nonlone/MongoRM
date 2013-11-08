package test.mongorm.dao;

import test.mongorm.model.TaoItem;

import com.lunjar.mjorm.persistance.BaseDao;

public class TaoItemDao extends BaseDao<TaoItem>{

	public TaoItemDao(String alias) throws Exception {
		super(alias);
	}

	public TaoItemDao() throws Exception {
		super();
	}
}
