package com.xbongbong.dingxbb.entity;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.xbongbong.util.StringUtil;

public class ContractProductEntity implements Serializable {

	/** Comment for <code>serialVersionUID</code> */
	private static final long serialVersionUID = -1L;

	// ========== properties ==========

	// 公司ID
	private String corpid;
	// 合同ID
	private Integer contractId;
	// 产品ID
	private Integer productId;
	// 产品数量
	private Double productNum;
	// 产品名称
	private String productName;
	// 价格： 默认是-1，不显示价格
	private Double price;
	// 产品规格
	private String specification;

	// 连表查询所需字段
	private String thumbnail;
	private String spec;
	private Integer categoryId;
	private String unit;
	private String signUserId;
	
	public ContractProductEntity() {
		this.productNum = 0d;
		this.productName = "";
		this.price = -1D;
		this.specification = "";
		
		this.thumbnail = "";
		this.spec = "";
		this.categoryId = 0;
		this.unit = "";
		this.signUserId = "";
	}

	// ========== getters and setters ==========

	public String getCorpid() {
		return corpid;
	}

	public void setCorpid(String corpid) {
		this.corpid = corpid;
	}

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Double getProductNum() {
		return productNum;
	}

	public void setProductNum(Double productNum) {
		this.productNum = productNum;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
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

	public String getSignUserId() {
		return signUserId;
	}

	public void setSignUserId(String signUserId) {
		this.signUserId = signUserId;
	}

	/**
	 * 重载toString方法
	 * 
	 * @return
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}

	 @Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null) {
			return false;
		}
		if (!(other instanceof ContractProductEntity)) {
			return false;
		}

		final ContractProductEntity cp = (ContractProductEntity) other;

		if (!getCorpid().equals(cp.getCorpid())) {
			return false;
		}
		if (!getContractId().equals(cp.getContractId())) {
			return false;
		}
		if (!getProductId().equals(cp.getProductId())) {
			return false;
		}
		if (!getProductNum().equals(cp.getProductNum())) {
			return false;
		}
		if (!getProductName().equals(cp.getProductName())) {
			return false;
		}
		if (!getPrice().equals(cp.getPrice())) {
			return false;
		}
		if (!getSpecification().equals(cp.getSpecification())) {
			return false;
		}

		return true;
	}
}
