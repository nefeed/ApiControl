 
package com.xbongbong.dingxbb.entity;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

 
public class RedundantFieldExplainEntity implements Serializable{

	/** Comment for <code>serialVersionUID</code> */
	private static final long serialVersionUID = -1L;

	//========== properties ==========
	
	//
	private Integer id;
	//公司ID
	private String corpid;
	//模板ID
	private Integer templateId;
	//是否是必填项0：非必填；1：必填
	private Integer required;
	//排序字段，越大的排在越前面
	private Integer sort;
	//冗余字段的名称
	private String attr;
	//冗余字段显示名称
	private String attrName;
	//字段显示类型：1.单行文本，2.数字输入，3.下拉选择，4，日期型，5。图片单图，6.图片多图， 7.多行文本
	private Integer fieldType;
	//初始值，下拉框的选项用|隔开
	private String initValue;
	//是否启用，1：启用，0：不启用
	private Integer enable;
	//删除标记
	private Integer del;
	//是否是冗余字段 0否(默认字段)，1是(扩展字段)
	private Integer isRedundant;


	private String[] valueArr;//fieldType为多选项时，各选项的值
	
	//扩展字段，从钉钉获取同步数据时保存钉钉那边的fieldId,不用入库，因为不可能做到钉钉那边fieldId直接存到我们的attr
	private String ddAttr;
	//用于保存对应字段对于某个实体的值,用于结构化后前端显示，by kaka
	private String vaule4Show;
	
	public RedundantFieldExplainEntity() {
		this.del = 0;
		this.initValue = "";
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
	public Integer getTemplateId() {
		return templateId;
	}
	
	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}
	public Integer getRequired() {
		return required;
	}
	
	public void setRequired(Integer required) {
		this.required = required;
	}
	public Integer getSort() {
		return sort;
	}
	
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public String getAttr() {
		return attr;
	}
	
	public void setAttr(String attr) {
		this.attr = attr;
	}
	public String getAttrName() {
		return attrName;
	}
	
	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}
	public Integer getFieldType() {
		return fieldType;
	}
	
	public void setFieldType(Integer fieldType) {
		this.fieldType = fieldType;
	}
	public String getInitValue() {
		return initValue;
	}
	
	public void setInitValue(String initValue) {
		this.initValue = initValue;
	}
	public Integer getEnable() {
		return enable;
	}
	
	public void setEnable(Integer enable) {
		this.enable = enable;
	}
	public Integer getDel() {
		return del;
	}
	
	public void setDel(Integer del) {
		this.del = del;
	}
	public Integer getIsRedundant() {
		return isRedundant;
	}
	
	public void setIsRedundant(Integer isRedundant) {
		this.isRedundant = isRedundant;
	}

	public String[] getValueArr() {
		return valueArr;
	}

	public void setValueArr(String[] valueArr) {
		this.valueArr = valueArr;
	}

	public String getDdAttr() {
		return ddAttr;
	}

	public void setDdAttr(String ddAttr) {
		this.ddAttr = ddAttr;
	}

	/**
	 * @return the vaule4Show
	 */
	public String getVaule4Show() {
		return vaule4Show;
	}

	/**
	 * @param vaule4Show the vaule4Show to set
	 */
	public void setVaule4Show(String vaule4Show) {
		this.vaule4Show = vaule4Show;
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

