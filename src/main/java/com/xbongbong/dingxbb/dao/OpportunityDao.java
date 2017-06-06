 
package com.xbongbong.dingxbb.dao;

import com.xbongbong.dingxbb.entity.OpportunityEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

 
 
public interface OpportunityDao {

	 
	public Integer insert(OpportunityEntity salesOpportunity);
	public Integer update(OpportunityEntity salesOpportunity);
	
	public Integer deleteByCustomerId(@Param("corpid")String corpid, @Param("customerId")Integer customerId);
	
	public Integer deleteByKey(@Param("key")Integer key,@Param("corpid")String corpid);
	public OpportunityEntity getByKey (@Param("key") Integer key, @Param("corpid")String corpid);
	
	
	public List<OpportunityEntity>  findEntitys(@Param("param")Map<String ,Object>  param);
	public Integer getEntitysCount(@Param("param")Map<String ,Object>  param);
	public List<OpportunityEntity> getByStaffId(@Param("param")Map<String, Object> param);
		
	public List<OpportunityEntity>  countReportByStage(@Param("param")Map<String ,Object>  param);
	public List<OpportunityEntity>  amountReportByStage(@Param("param")Map<String ,Object>  param);

	public List<Map<String,Object>> opportunityStatic(@Param("param")Map<String ,Object> param);

	public List<OpportunityEntity> opportunityStaticList(@Param("param")Map<String ,Object> param);
		
	public List<OpportunityEntity>  findEntitysJoinOpportunityUser(@Param("param")Map<String ,Object>  param);
	public List<OpportunityEntity>  findEntitysJoinOpportunityUserIsMain(@Param("param")Map<String ,Object>  param);
	public Integer getEntitysJoinOpportunityUserCount(@Param("param")Map<String ,Object>  param);
	/**
	 * 批量更新销售机会列表中某公司某客户的客户名称
	 * @param corpid
	 * @return
	 */
	public Integer dataConsistencyUpdate(@Param("corpid")String corpid, @Param("customerId")Integer customerId, @Param("customerName")String customerName);
	
	/**
	 * 批量更新销售机会中某公司某用户的姓名和头像
	 * @param corpid
	 * @param userId
	 * @param userName
	 * @param userAvatar
	 * @return
	 */
	public Integer dataConsistencyUpdateUser(@Param("corpid")String corpid, @Param("userId")String userId, @Param("userName")String userName, @Param("userAvatar")String userAvatar);
}