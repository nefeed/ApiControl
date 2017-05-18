 
package com.xbongbong.dingxbb.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xbongbong.dingxbb.entity.ContractEntity;

 
 
public interface ContractDao {

	 
	public Integer insert(ContractEntity contract);
	public Integer update(ContractEntity contract);
	
	 
	
	public Integer deleteByKey(@Param("key")Integer key, @Param("corpid")String corpid);
	public ContractEntity getByKey (@Param("key") Integer key, @Param("corpid")String corpid);
	
	
	public List<ContractEntity>  findEntitys(@Param("param")Map<String ,Object>  param);
	public Integer getEntitysCount(@Param("param")Map<String ,Object>  param);
	
	public Integer getJoinContractUserCount(@Param("param")Map<String ,Object>  param);
	
	public List<ContractEntity>  findEntitysJoinContractUser(@Param("param")Map<String ,Object>  param);
	
	/**客户排名**/
	public List<ContractEntity>  rankByCustomer(@Param("param")Map<String ,Object>  param);
	/**销售排名**/
	public List<ContractEntity>  saleAmountRank(@Param("param")Map<String ,Object>  param);
	
	public List<ContractEntity>  customerAmountRank(@Param("param")Map<String ,Object>  param);
	/*****获得总金额****/
	public Double getSumAmount(@Param("param")Map<String ,Object>  param);
	/** 合同明细*/
	public List<ContractEntity>  contractDetails(@Param("param")Map<String ,Object>  param);
	
	public List<Map<String,Object>> contractStatistic(@Param("param")Map<String ,Object>  param);
	
	
	/** @1 合同一周统计*/
	public List<ContractEntity>  statisticsByWeek(@Param("param")Map<String ,Object>  param);
	/** @2 当前月份或季度的合同总额 */
	public Map<String,Object>  totalSumOfThisMonthOrSeason(@Param("param")Map<String ,Object>  param);
	/** @3 当前月份或季度的合同分组总计*/
	public Integer rankOfThisMonthOrSeason(@Param("param")Map<String ,Object>  param);
}