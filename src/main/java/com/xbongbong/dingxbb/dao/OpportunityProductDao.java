 
package com.xbongbong.dingxbb.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xbongbong.dingxbb.entity.OpportunityProductEntity;

 
 
public interface OpportunityProductDao {

	 
	public Integer insert(OpportunityProductEntity opportunityProduct);
	public Integer update(OpportunityProductEntity opportunityProduct);
	
	public Integer deleteByOpportunityId(@Param("corpid")String corpid,@Param("opportunityId")Integer opportunityId);
	public Integer deleteByKey(@Param("corpid")String corpid, @Param("opportunityId")Integer opportunityId, @Param("productId")Integer productId);
	public OpportunityProductEntity getByKey (@Param("corpid") String corpid, @Param("opportunityId")Integer opportunityId, @Param("productId")Integer productId);
	
	public List<OpportunityProductEntity>  findEntitys(@Param("param")Map<String ,Object>  param);
	public Integer getEntitysCount(@Param("param")Map<String ,Object>  param);

	public List<OpportunityProductEntity>  findEntitysJoinProduct(@Param("param")Map<String ,Object>  param);
		
	/**
	 * 批量销售机会中更新某公司某产品的产品名称&产品规格
	 * @param key
	 * @param corpid
	 * @return
	 */
	public Integer dataConsistencyUpdate(@Param("corpid")String corpid, @Param("productId")Integer productId, @Param("productName")String productName, @Param("specification")String specification);
 
}