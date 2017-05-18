 
package com.xbongbong.dingxbb.entity;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.xbongbong.util.DateUtil;

 
public class CompanyConfigEntity implements Serializable{

	/** Comment for <code>serialVersionUID</code> */
	private static final long serialVersionUID = -1L;
	public CompanyConfigEntity (){
		updateTime = DateUtil.getInt();
		del = 0;
	}
	//========== properties ==========
	
	//主键
	private Integer id;
	//公司id
	private String corpid;
	//配置名，用于显示
	private String configName;
	//配置别名 key，英文
	private String configAlias;
	//配置值
	private String configValue;
	//创建时间
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
	public String getConfigName() {
		return configName;
	}
	
	public void setConfigName(String configName) {
		this.configName = configName;
	}
	public String getConfigAlias() {
		return configAlias;
	}
	
	public void setConfigAlias(String configAlias) {
		this.configAlias = configAlias;
	}
	public String getConfigValue() {
		return configValue;
	}
	
	public void setConfigValue(String configValue) {
		this.configValue = configValue;
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

