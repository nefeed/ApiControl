 
package com.xbongbong.dingxbb.entity;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.xbongbong.util.DateUtil;

 
public class ApiTokenEntity implements Serializable{

	/** Comment for <code>serialVersionUID</code> */
	private static final long serialVersionUID = -1L;
	public ApiTokenEntity (){
		updateTime = DateUtil.getInt();
		del = 0;
	}
	//========== properties ==========
	
	//主键
	private Integer id;
	//公司ID
	private String corpid;
	//api访问时的token
	private String token;
	//添加时间
	private Integer addTime;
	//更新时间
	private Integer updateTime;
	//删除标记
	private Integer del;

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
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
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
	public Integer getDel() {
		return del;
	}
	
	public void setDel(Integer del) {
		this.del = del;
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

