 
package com.xbongbong.dingxbb.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xbongbong.dingxbb.entity.PaymentEntity;

 
 
public interface PaymentDao {

	 
	public Integer insert(PaymentEntity payment);
	public Integer update(PaymentEntity payment);
	
	 
	
	public Integer deleteByKey(@Param("key")Integer key,@Param("corpid")String corpid);
	public Integer deleteEntitys(@Param("param")Map<String ,Object>  param);
	public PaymentEntity getByKey (@Param("key") Integer key, @Param("corpid")String corpid);
	
	
	public List<PaymentEntity>  findEntitys(@Param("param")Map<String ,Object>  param);
	public Integer getEntitysCount(@Param("param")Map<String ,Object>  param);
	public Integer deleteByContractId(@Param("contractId")Integer contractId,@Param("corpid")String corpid);
		

	public Double getSumAmount(@Param("param")Map<String ,Object>  param);
	//获取部分回款--与sheet连表--返回金额
	public Double getSomeAmount(@Param("param")Map<String ,Object>  param);
	//获取部分回款--与sheet连表--返回列表--统计用
	public List<PaymentEntity>  findSomeAmounts(@Param("param")Map<String ,Object>  param);
	
	/** 统计时间段内客户的回款金额排名  by chy */
	public List<PaymentEntity> customerAmountRank(@Param("param")Map<String ,Object>  param);
	/** 统计时间段内员工的回款金额排名  by chy */
	public List<PaymentEntity> saleAmountRank(@Param("param")Map<String ,Object>  param);
	/**  @以员工汇总 */
	public List<PaymentEntity> statisticByStaff (@Param("param")Map<String ,Object>  param);
	/**  @以部门汇总 */
	public List<PaymentEntity> statisticByDep (@Param("param")Map<String ,Object>  param);
	
	/** @1统计我一周的回款总额 */
	public Integer totalSumByWeek(@Param("param")Map<String ,Object>  param);	
	/** @2 当前月份或季度的回款总额 */
	public Map<String,Object>  totalSumOfThisMonthOrSeason(@Param("param")Map<String ,Object>  param);
	/**  @3 回款总额的排名 */
	public List<PaymentEntity> rankOfThisMonth (@Param("param")Map<String ,Object>  param);
	/** @3 当前月份或季度的合同分组总计*/
	public Integer rankOfThisMonthOrSeason(@Param("param")Map<String ,Object>  param);	
 
	/**
	 * 回款单列表
	 * @param param
	 * @return
	 */
	public List<PaymentEntity>  findEntitysJoin(@Param("param")Map<String ,Object>  param);
	/**
	 * 回款单数量
	 * @param param
	 * @return
	 */
	public Integer getEntitysCountJoin(@Param("param")Map<String ,Object>  param);
	
	
	/**
	 * 合同回款列表 
	 * @param param
	 * @return
	 */
	public List<PaymentEntity> findEntitysJoinSheet(@Param("param")Map<String, Object> param);
	
	public List<PaymentEntity> findEntitysJoinContractUser(@Param("param")Map<String, Object> param);
	public Integer getEntitysCountJoinContractUser(@Param("param")Map<String ,Object>  param);
	
}