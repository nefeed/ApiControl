 
package com.xbongbong.dingxbb.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xbongbong.dingxbb.entity.ContractUserEntity;

 
 
public interface ContractUserDao {

	 
	public Integer insert(ContractUserEntity contractStaff);
	public Integer update(ContractUserEntity contractStaff);

	public Integer deleteByKey(@Param("key")Integer key, @Param("corpid")String corpid);
	public ContractUserEntity getByKey (@Param("key") Integer key, @Param("corpid")String corpid);

	public ContractUserEntity getByUserIdContractId (@Param("corpid")String corpid, @Param("userId") String userId, @Param("contractId") Integer contractId);
	
	public Integer deleteEntitys(@Param("param")Map<String ,Object>  param);
	
	
	public List<ContractUserEntity>  findEntitys(@Param("param")Map<String ,Object>  param);
	public Integer getEntitysCount(@Param("param")Map<String ,Object>  param);
	
	
	public List<ContractUserEntity>  findEntitysJoinContract(@Param("param")Map<String ,Object>  param);
 
}