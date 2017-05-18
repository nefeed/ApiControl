package com.xbongbong.dingxbb.entity;

import java.io.Serializable;

public class PaymentSheetEntity implements Serializable{
	/** Comment for <code>serialVersionUID</code> */
	private static final long serialVersionUID = -1L;
	
	//========== properties ==========
	
	//主键
	private Integer id;
	//公司id
	private String corpid;
	//用户ID
	private String userId;
	//客户ID
	private Integer customerId;
	//合同ID
	private Integer contractId;
	//回款ID
	private Integer paymentId;
	
	//回款金额
	private Double amount;
	//实际收款时间
	private Integer paymentTime;
	//支付方式
	private Integer payMethod;
	//备注，200字以内
	private String memo;
	//创建时间
	private Integer addTime;
	//更新时间
	private Integer updateTime;
	//删除标记
	private Integer del;
	
	//回款单编号
	private String paymentSheetNo;
	//归属人ID(默认为签订人)
	private String belongId;
	//归属人名字
	private String belongName;
	
	//扩展字段
	//支付方式名称
	private String payMethodStr;
	
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
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	public Integer getContractId() {
		return contractId;
	}
	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}
	public Integer getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(Integer paymentId) {
		this.paymentId = paymentId;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Integer getPaymentTime() {
		return paymentTime;
	}
	public void setPaymentTime(Integer paymentTime) {
		this.paymentTime = paymentTime;
	}
	public Integer getPayMethod() {
		return payMethod;
	}
	public void setPayMethod(Integer payMethod) {
		this.payMethod = payMethod;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
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
	public String getPayMethodStr() {
		return payMethodStr;
	}
	public void setPayMethodStr(String payMethodStr) {
		this.payMethodStr = payMethodStr;
	}
	public String getPaymentSheetNo() {
		return paymentSheetNo;
	}
	public void setPaymentSheetNo(String paymentSheetNo) {
		this.paymentSheetNo = paymentSheetNo;
	}
	public String getBelongId() {
		return belongId;
	}
	public void setBelongId(String belongId) {
		this.belongId = belongId;
	}
	public String getBelongName() {
		return belongName;
	}
	public void setBelongName(String belongName) {
		this.belongName = belongName;
	}
	
}
