 
package com.xbongbong.dingxbb.entity;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.xbongbong.util.DateUtil;

 
public class CustomerUserEntity implements Serializable{

	/** Comment for <code>serialVersionUID</code> */
	private static final long serialVersionUID = -1L;

	//========== properties ==========
	
	//员工
	private Integer id;
	//员工ID
	private String userId;
	//客户ID
	private Integer customerId;
	//是否是客户的主负责人
	private Integer isMain;
	//公司ID，迁移时使用
	private String corpid;
	//员工名字
	private String userName;
	//分配时间
	private Integer distributionTime;
	//退回公海池时间
	private Integer backTime;
	//删除标记
	private Integer del;
	//添加时间
	private Integer addTime;
	//更新时间
	private Integer updateTime;
	//扩展字段
	//头像
	private String userAvatar="";
	//职位
	private String position ="";
	
	public CustomerUserEntity() {
		this.userId = "0";
		this.customerId = 0;
		this.isMain = 0;
		this.corpid = "0";
		this.userName = "";
		this.backTime = -1;
		this.del = 0;
		this.distributionTime = DateUtil.getInt();
	}
	
    //========== getters and setters ==========
   
	
	
	
	/**
	* 重载toString方法
	* @return
	*
	* @see java.lang.Object#toString()
	*/
	@Override
    public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public Integer getDistributionTime() {
		return distributionTime;
	}

	public void setDistributionTime(Integer distributionTime) {
		this.distributionTime = distributionTime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getIsMain() {
		return isMain;
	}

	public void setIsMain(Integer isMain) {
		this.isMain = isMain;
	}

	public String getCorpid() {
		return corpid;
	}

	public void setCorpid(String corpid) {
		this.corpid = corpid;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserAvatar() {
		return userAvatar;
	}

	public void setUserAvatar(String userAvatar) {
		this.userAvatar = userAvatar;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public Integer getBackTime() {
		return backTime;
	}

	public void setBackTime(Integer backTime) {
		this.backTime = backTime;
	}

	public Integer getDel() {
		return del;
	}

	public void setDel(Integer del) {
		this.del = del;
	}

	public Integer getAddTime() {
		return addTime;
	}

	public void setAddTime(Integer addTime) {
		this.addTime = addTime;
	}

	public Integer getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Integer updateTime) {
		this.updateTime = updateTime;
	}
}

