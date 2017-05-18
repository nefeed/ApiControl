 
package com.xbongbong.dingxbb.entity;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

 
public class PaymentEntity implements Serializable {

	/** Comment for <code>serialVersionUID</code> */
	private static final long serialVersionUID = -1L;

	//========== properties ==========
	
	//主键
	private Integer id;
	//公司ID，用于统计和迁移
	private String corpid;
	//用于统计，员工ID
	private String userId;
	//合同ID
	private Integer contractId;
	//回款金额
	private Double amount;
	//合同名称
	private String contractName;
	//预计时间
	private Integer estimateTime;
	//账期
	private Integer accountPeriod;
	//实际到账时间
	private Integer payTime;
	//状态，1未收款，2已收款，3应收款，4部分收款
	private Integer status;
	//创建时间
	private Integer addTime;
	//更新时间
	private Integer updateTime;
	//删除标记
	private Integer del;
	//客户名称
	private String customerName;
	//员工名称
	private String userName;
	//回款编号
	private String paymentNo;
	//未回款金额
	private Double unAmount;
	//备注
	private String memo;
	

	//扩展字段
	//将收款
	private Integer willOverdue;
	//增加数
	private Integer addNum;
	//客户ID
	private Integer customerId;
	//部门ID
	private Integer departmentId;
	//部门名
	private String departmentName;
	//部门名
	private Integer rank;
	
	//扩展字段
	//实际回款金额
	private Double realPayment;
	//回款单数量
	private Integer paymentSheetNum;
	//实际回款时间---sheet表内paymentTime
	private Integer paymentTime;
	//合同跟进的员工名字
	private String paymentUserNames;
		
		
	private String unit;
		
    //========== getters and setters ==========
    
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getContractId() {
		return contractId;
	}
	
	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}
	public Double getAmount() {
		return amount;
	}
	
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getContractName() {
		return contractName;
	}
	
	public void setContractName(String contractName) {
		this.contractName = contractName;
	}
	public Integer getEstimateTime() {
		return estimateTime;
	}
	
	public void setEstimateTime(Integer estimateTime) {
		this.estimateTime = estimateTime;
	}
	public Integer getAccountPeriod() {
		return accountPeriod;
	}
	
	public void setAccountPeriod(Integer accountPeriod) {
		this.accountPeriod = accountPeriod;
	}
	public Integer getPayTime() {
		return payTime;
	}
	
	public void setPayTime(Integer payTime) {
		this.payTime = payTime;
	}
	public Integer getStatus() {
		return status;
	}
	
	public void setStatus(Integer status) {
		this.status = status;
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
	public String getCustomerName() {
		return customerName;
	}
	
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Integer getWillOverdue() {
		return willOverdue;
	}

	public void setWillOverdue(Integer willOverdue) {
		this.willOverdue = willOverdue;
	}

	public Integer getAddNum() {
		return addNum;
	}

	public void setAddNum(Integer addNum) {
		this.addNum = addNum;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	
	public String getPaymentNo() {
		return paymentNo;
	}

	public void setPaymentNo(String paymentNo) {
		this.paymentNo = paymentNo;
	}

	public Double getUnAmount() {
		return unAmount;
	}

	public void setUnAmount(Double unAmount) {
		this.unAmount = unAmount;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	
	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public Double getRealPayment() {
		return realPayment;
	}

	public void setRealPayment(Double realPayment) {
		this.realPayment = realPayment;
	}

	public Integer getPaymentSheetNum() {
		return paymentSheetNum;
	}

	public void setPaymentSheetNum(Integer paymentSheetNum) {
		this.paymentSheetNum = paymentSheetNum;
	}

	public static Integer getPaymentStatusInt(String paymentStatus){
		if("未收款".equals(paymentStatus)){
			return 1;
		}else if("已收款".equals(paymentStatus)){
			return 2;
		}else if("应收款".equals(paymentStatus)){
			return 3;
		}
		return 0;
	}
	
	public Integer getPaymentTime() {
		return paymentTime;
	}

	public void setPaymentTime(Integer paymentTime) {
		this.paymentTime = paymentTime;
	}

	
	//合同跟进的员工名字
	public String getPaymentUserNames() {
		return paymentUserNames;
	}

	public void setPaymentUserNames(String paymentUserNames) {
		this.paymentUserNames = paymentUserNames;
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

	public String isArchiveArr[] = {"","已归档","未归档"};
	
}

