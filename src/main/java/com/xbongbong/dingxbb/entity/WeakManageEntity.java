 
package com.xbongbong.dingxbb.entity;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

 
public class WeakManageEntity implements Serializable{

	/** Comment for <code>serialVersionUID</code> */
	private static final long serialVersionUID = -1L;

	//========== properties ==========
	
	//自增ID
	private Integer id;
	//公司编号
	private String corpid;
	//管理者/助手ID
	private String supStaffId;
	//被管理者ID
	private String subStaffId;
	//被管理部门ID
	private Long subDepId;
	//标示是部门还是员工，1员工，2部门
	private Integer type;
	//添加时间
	private Integer addTime;
	//删除标识位
	private Integer del;

	//扩展字段
	//下属员工或部门名字
	private String name;
	
	
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

	public String getSupStaffId() {
		return supStaffId;
	}

	public void setSupStaffId(String supStaffId) {
		this.supStaffId = supStaffId;
	}

	public String getSubStaffId() {
		return subStaffId;
	}

	public void setSubStaffId(String subStaffId) {
		this.subStaffId = subStaffId;
	}

	public Long getSubDepId() {
		return subDepId;
	}

	public void setSubDepId(Long subDepId) {
		this.subDepId = subDepId;
	}

	public Integer getType() {
		return type;
	}
	
	public void setType(Integer type) {
		this.type = type;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

