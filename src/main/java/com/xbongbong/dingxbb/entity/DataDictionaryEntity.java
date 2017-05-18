 
package com.xbongbong.dingxbb.entity;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

 
public class DataDictionaryEntity implements Serializable{

	/** Comment for <code>serialVersionUID</code> */
	private static final long serialVersionUID = -1L;

	//========== properties ==========
	
	//主键
	private Integer id;
	//公司ID，0表示默认字典
	private String corpid;
	//所属entity，1：合同订单 2：客户 3：联系人 4:审批 5：销售机会 6：跟进记录
	private Integer refEntity;
	//字典字段名称
	private String name;
	//编号，用于字段标示
	private Integer code;
	//字段类型，区分不同类型的字段,1:合同状态，2：合同类型，3：客户分级，4：客户性质，5：联系人级别,6：客户行业字段,7：报销审批报销项目,8：销售阶段（在销售阶段表）,9:跟进方式
	private Integer type;
	//附加字段，对于销售合同，1表示统计字段，0表示非统计字段，销售合同状态数据字典编辑是需编辑该字段
	private Integer addtionnalField;
	//是否启用，1为启用，0为不启用
	private Integer enable;
	//是否是缺省值，1则是，0则不是
	private Integer ifDefault;
	//是否是保留参数，1表示是，0表示不是
	private Integer reserved;
	//添加时间
	private Integer addTime;
	//更新时间
	private Integer updateTime;
	//排序字段，越大的排在越前面
	private Integer sort;

	
	//扩展字段
	private String typeName;
	
	private String entityName;
	//用于渲染销售阶段
	private Double estimateWinRate;
	private String stageGuide;
	private Integer stageType;
	//存放关联实体的每个type的entity
	private List<DataDictionaryEntity> refChildList;
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
	public Integer getRefEntity() {
		return refEntity;
	}
	
	public void setRefEntity(Integer refEntity) {
		this.refEntity = refEntity;
	}
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public Integer getCode() {
		return code;
	}
	
	public void setCode(Integer code) {
		this.code = code;
	}
	public Integer getType() {
		return type;
	}
	
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getAddtionnalField() {
		return addtionnalField;
	}
	
	public void setAddtionnalField(Integer addtionnalField) {
		this.addtionnalField = addtionnalField;
	}
	public Integer getEnable() {
		return enable;
	}
	
	public void setEnable(Integer enable) {
		this.enable = enable;
	}
	public Integer getIfDefault() {
		return ifDefault;
	}
	
	public void setIfDefault(Integer ifDefault) {
		this.ifDefault = ifDefault;
	}
	public Integer getReserved() {
		return reserved;
	}
	
	public void setReserved(Integer reserved) {
		this.reserved = reserved;
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

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public Double getEstimateWinRate() {
		return estimateWinRate;
	}

	public void setEstimateWinRate(Double estimateWinRate) {
		this.estimateWinRate = estimateWinRate;
	}

	public String getStageGuide() {
		return stageGuide;
	}

	public void setStageGuide(String stageGuide) {
		this.stageGuide = stageGuide;
	}

	public Integer getStageType() {
		return stageType;
	}

	public void setStageType(Integer stageType) {
		this.stageType = stageType;
	}

	public List<DataDictionaryEntity> getRefChildList() {
		return refChildList;
	}

	public void setRefChildList(List<DataDictionaryEntity> refChildList) {
		this.refChildList = refChildList;
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

