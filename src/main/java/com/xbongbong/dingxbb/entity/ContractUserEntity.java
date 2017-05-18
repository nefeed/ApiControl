 
package com.xbongbong.dingxbb.entity;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.xbongbong.util.DateUtil;
import com.xbongbong.util.StringUtil;

 
public class ContractUserEntity implements Serializable{

	/** Comment for <code>serialVersionUID</code> */
	private static final long serialVersionUID = -1L;

	//========== properties ==========

	//主键
	private Integer id;
	//公司ID
	private String corpid;
	//员工ID
	private String userId;
	//主要负责人姓名
	private String userName;
	//主要负责人头像
	private String userAvatar;
	//合同ID
	private Integer contractId;
	//是否是主要负责人
	private Integer isMain;
	//创建时间
	private Integer addTime;
	//删除标记
	private Integer del;
	
	//扩展字段
	private String position ="";

	public ContractUserEntity(){
		this.userName = "";
		this.userAvatar = "/images/default.jpg";
		this.contractId = 0;
		this.isMain = 0;
		this.addTime = DateUtil.getInt();
		this.del = 0;
	}
    //========== getters and setters ==========

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCorpid() {
		return corpid;
	}

	public void setCorpid(String corpid) {
		this.corpid = corpid;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserAvatar() {
		if(StringUtil.isEmpty(userAvatar)){
			this.userAvatar = "/images/default.jpg";
		}
		return userAvatar;
	}

	public void setUserAvatar(String userAvatar) {
		this.userAvatar = userAvatar;
	}

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public Integer getIsMain() {
		return isMain;
	}

	public void setIsMain(Integer isMain) {
		this.isMain = isMain;
	}

	public Integer getAddTime() {
		return addTime;
	}

	public void setAddTime(Integer addTime) {
		this.addTime = addTime;
	}

	public Integer getDel() {
		return del;
	}

	public void setDel(Integer del) {
		this.del = del;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}
    
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
	
}

