 
package com.xbongbong.dingxbb.entity;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.xbongbong.util.DateUtil;

 
public class RedundantFieldTemplateEntity implements Serializable{

	/** Comment for <code>serialVersionUID</code> */
	private static final long serialVersionUID = -1L;

	//========== properties ==========
	
	//
	private Integer id;
	//公司ID
	private String corpid;
	//是否启用冗余字段，1：启用，0：不启用
	private Integer isRedundant;
	//1：客户冗余字段，2：销售机会冗余字段，3：合同冗余字段 ，4：联系人冗余字段
	private Integer type;
	//模板名称
	private String name;
	//添加时间
	private Integer addTime;
	//修改时间
	private Integer updateTime;
	//删除标识：0：保留；1：删除
	private Integer del;
	
	//拷贝的template_id，用于选择模板时的回显
	private Integer copyTemplateId;
	//是否公开模板，0：否，1：是
	private Integer isPublic;
	//是否是拷贝的模板：0表示是自己新建的模板，1表示是拷贝的模板
	private Integer isCopy;
	//适配类型，1：通用模板，2：行业适配模板
	private Integer adaptation;
	//模板描述。80字以内
	private String introduction;
	//模板图标
	private String icon;
	//是否启用，某公司同一时刻只能有一个启用的模板，现在del只表示删除逻辑
	private Integer enable;

	//钉钉表单ID
	private String ddFormUuid;
	
	public RedundantFieldTemplateEntity() {
		int now = DateUtil.getInt();
		
		this.del = 0;
		this.addTime = now;
		this.updateTime = now;
		this.introduction = "";
		this.icon = "";
		this.ddFormUuid = "";
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
	public Integer getIsRedundant() {
		return isRedundant;
	}
	
	public void setIsRedundant(Integer isRedundant) {
		this.isRedundant = isRedundant;
	}
	public Integer getType() {
		return type;
	}
	
	public void setType(Integer type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
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

	public Integer getCopyTemplateId() {
		return copyTemplateId;
	}

	public void setCopyTemplateId(Integer copyTemplateId) {
		this.copyTemplateId = copyTemplateId;
	}

	public Integer getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(Integer isPublic) {
		this.isPublic = isPublic;
	}

	public Integer getIsCopy() {
		return isCopy;
	}

	public void setIsCopy(Integer isCopy) {
		this.isCopy = isCopy;
	}

	public Integer getAdaptation() {
		return adaptation;
	}

	public void setAdaptation(Integer adaptation) {
		this.adaptation = adaptation;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Integer getEnable() {
		return enable;
	}

	public void setEnable(Integer enable) {
		this.enable = enable;
	}

	public String getDdFormUuid() {
		return ddFormUuid;
	}

	public void setDdFormUuid(String ddFormUuid) {
		this.ddFormUuid = ddFormUuid;
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

