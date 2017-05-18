 
package com.xbongbong.dingxbb.entity;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.alibaba.fastjson.JSONArray;
import com.xbongbong.util.StringUtil;

 
public class OpportunityProductEntity implements Serializable{

	/** Comment for <code>serialVersionUID</code> */
	private static final long serialVersionUID = -1L;

	//========== properties ==========

	//公司ID
	private String corpid;
	//销售机会ID
	private Integer opportunityId;
	//产品ID
	private Integer productId;
	//产品名称
	private String productName;
	//产品数量
	private Double productNum;
	//价格:默认值为-1，不显示价格
	private Double price;
	//规格
	private String specification;

	//连表查询所需字段
	private String thumbnail;
	private String spec;
	private Integer categoryId;
	private String unit;

	private String priceStr = "";
	private String productNumStr = "";
	
	//型号
	private JSONArray specificationArray = new JSONArray();
	

	public OpportunityProductEntity(){
		this.productName = "";
		this.productNum = 0d;
		this.price = -1D;
		this.specification = "";
		this.thumbnail = "/images/defaultpro.png";
		this.spec = "";
		this.categoryId = 0;
		this.unit = "";
	}

    //========== getters and setters ==========
    
	public String getCorpid() {
		return corpid;
	}
	
	public void setCorpid(String corpid) {
		this.corpid = corpid;
	}
	public Integer getOpportunityId() {
		return opportunityId;
	}
	
	public void setOpportunityId(Integer opportunityId) {
		this.opportunityId = opportunityId;
	}
	public Integer getProductId() {
		return productId;
	}
	
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public Double getProductNum() {
		return productNum;
	}
	
	public void setProductNum(Double productNum) {
		this.productNum = productNum;
	}
	public Double getPrice() {
		return price;
	}
	
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getSpecification() {
		return specification;
	}
	
	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getThumbnail() {
		if(StringUtil.isEmpty(thumbnail)){
			this.thumbnail = "/images/defaultpro.png";
		}
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getPriceStr() {
		return priceStr;
	}

	public void setPriceStr(String priceStr) {
		this.priceStr = priceStr;
	}

	public String getProductNumStr() {
		return productNumStr;
	}

	public void setProductNumStr(String productNumStr) {
		this.productNumStr = productNumStr;
	}

	public JSONArray getSpecificationArray() {
		return specificationArray;
	}

	public void setSpecificationArray(JSONArray specificationArray) {
		this.specificationArray = specificationArray;
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

