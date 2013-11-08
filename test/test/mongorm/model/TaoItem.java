package test.mongorm.model;

import java.util.Date;

import org.json.JSONObject;


import com.lunjar.mjorm.annotation.DBCollectionName;
import com.lunjar.mjorm.persistance.BaseVO;
import com.seahold.dao.annotation.DBField;
import com.seahold.dao.annotation.Table;

/**
 * 用于记录宝贝的诊断信息
 * 
 * @author ROBOT
 * 
 */
@DBCollectionName(name="testSpeed")
@Table(tableName = "k_taoitem")
public class TaoItem extends BaseVO  {

	/**
	 * 主键
	 */
	@DBField(fieldName = "id", isAutoGenerate = true, isKey = true)
	private Long id;

	/**
	 * 昵称
	 */
	@DBField(fieldName = "nick", isKey = true)
	private String nick;

	/**
	 * 宝贝id
	 */
	@DBField(fieldName = "numIid", isKey = true)
	private Long numIid;

	/**
	 * 标题
	 */
	@DBField(fieldName = "title")
	private String title;

	/**
	 * 宝贝主图
	 */
	@DBField(fieldName = "picUrl")
	private String picUrl;

	/**
	 * 商家编号
	 */
	@DBField(fieldName = "outerId")
	private String outerId;

	/**
	 * 淘宝类目
	 */
	@DBField(fieldName = "cid")
	private Long cid;

	/**
	 * 店铺自定义类目
	 */
	@DBField(fieldName = "sellerCids")
	private String sellerCids;

	/**
	 * 商品修改时间（格式：yyyy-MM-dd HH:mm:ss）
	 */
	@DBField(fieldName = "modified")
	private Date modified;

	/**
	 * 商品上传后的状态。onsale出售中，instock库中
	 */
	@DBField(fieldName = "approveStatus")
	private String approveStatus;

	/**
	 * 优化时间
	 */
	@DBField(fieldName = "optimizationTime")
	private Date optimizationTime;

	/**
	 * 诊断得分
	 */
	@DBField(fieldName = "score")
	private int score;
	
	private String[] testList;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public Long getNumIid() {
		return numIid;
	}

	public void setNumIid(Long numIid) {
		this.numIid = numIid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getOuterId() {
		return outerId;
	}

	public void setOuterId(String outerId) {
		this.outerId = outerId;
	}

	public Long getCid() {
		return cid;
	}

	public void setCid(Long cid) {
		this.cid = cid;
	}

	public String getSellerCids() {
		return sellerCids;
	}

	public void setSellerCids(String sellerCids) {
		this.sellerCids = sellerCids;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	public String getApproveStatus() {
		return approveStatus;
	}

	public void setApproveStatus(String approveStatus) {
		this.approveStatus = approveStatus;
	}

	public Date getOptimizationTime() {
		return optimizationTime;
	}

	public void setOptimizationTime(Date optimizationTime) {
		this.optimizationTime = optimizationTime;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String[] getTestList() {
		return testList;
	}

	public void setTestList(String[] testList) {
		this.testList = testList;
	}
	
	public JSONObject toJSOnObject(){
		return new JSONObject(this);
	}
}
