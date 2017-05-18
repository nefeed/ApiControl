 
package com.xbongbong.dingxbb.entity;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

 
public class SalesStageEntity implements Serializable{

	/** Comment for <code>serialVersionUID</code> */
	private static final long serialVersionUID = -1L;

	//========== properties ==========
	
	//主键
	private Integer id;
	//公司ID
	private String corpid;
	//编号，用于字段标示;
	private Integer code;
	//销售阶段名
	private String stageName;
	//是否启用
	private Integer enable;
	//销售阶段类型,0其他类型，1赢单，2输单，3取消
	private Integer type;
	//该销售阶段对应的赢率
	private Double estimateWinRate;
	//本销售阶段的工作指导
	private String stageGuide;
	//创建时间
	private Integer addTime;
	//更新时间
	private Integer updateTime;
	//删除标记
	private Integer del;
	//排序字段，越大的排在越前面
	private Integer sort;

    //========== getters and setters ==========
    public SalesStageEntity() {
    	this.stageGuide = "";
    	this.estimateWinRate = 100D;
    	this.del = 0;
    }
	
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
	public Integer getCode() {
		return code;
	}
	
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getStageName() {
		return stageName;
	}
	
	public void setStageName(String stageName) {
		this.stageName = stageName;
	}
	public Integer getEnable() {
		return enable;
	}
	
	public void setEnable(Integer enable) {
		this.enable = enable;
	}
	public Integer getType() {
		return type;
	}
	
	public void setType(Integer type) {
		this.type = type;
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

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
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

