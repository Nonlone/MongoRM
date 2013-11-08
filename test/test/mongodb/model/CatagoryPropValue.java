package test.mongodb.model;

import org.json.JSONObject;

import com.seahold.dao.annotation.DBField;
import com.seahold.dao.annotation.Table;

@Table(tableName="testcategorypropvalue")
public class CatagoryPropValue {
	
	@DBField(isKey=true)
	private Long cid;
	
	@DBField(isKey=true)
	private Long pid;
	
	@DBField(isKey=true)
	private Long vid;
	
	@DBField
	private String propName;
	
	@DBField
	private String name;
	
	@DBField
	private Long childPid;

	public Long getCid() {
		return cid;
	}

	public void setCid(Long cid) {
		this.cid = cid;
	}

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public Long getVid() {
		return vid;
	}

	public void setVid(Long vid) {
		this.vid = vid;
	}

	public String getPropName() {
		return propName;
	}

	public void setPropName(String propName) {
		this.propName = propName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getChildPid() {
		return childPid;
	}

	public void setChildPid(Long childPid) {
		this.childPid = childPid;
	}

	public JSONObject toJSONObject(){
		return new JSONObject(this);
	}
}
