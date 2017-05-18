 
package com.xbongbong.dingxbb.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xbongbong.dingxbb.entity.ContractProductEntity;

 
 
public interface ContractProductDao {

	 
	public Integer insert(ContractProductEntity contractProduct);
	public Integer update(ContractProductEntity contractProduct);
	

	public Integer deleteByKey(@Param("corpid") String corpid, @Param("contractId") Integer contractId, @Param("productId") Integer productId);
	
	public ContractProductEntity getByKey (@Param("corpid") String corpid, @Param("contractId") Integer contractId, @Param("productId") Integer productId);
	
	
	public List<ContractProductEntity>  findEntitys(@Param("param")Map<String ,Object>  param);
	public Integer getEntitysCount(@Param("param")Map<String ,Object>  param);
	public Integer deleteByContractId(@Param("contractId")Integer contractId, @Param("corpid")String corpid);

	/**通过corpid,contractId删除合同的所有产品*/
	public Integer deleteContractAllProduct(@Param("corpid")String corpid,@Param("contractId")Integer contractId);

	/**通过contractId,productId删除合同的单个产品*/
	public Integer deleteContractProduct(@Param("corpid")String corpid,@Param("contractId")Integer contractId, @Param("productId")Integer productId);

	public List<ContractProductEntity> findEntitysJoinProduct(@Param("param")Map<String ,Object>  param);
	
	public List<ContractProductEntity> findEntitysJoinContract(@Param("param")Map<String ,Object>  param);
	public Integer getEntitysJoinContractCount(@Param("param")Map<String ,Object>  param);
	
}