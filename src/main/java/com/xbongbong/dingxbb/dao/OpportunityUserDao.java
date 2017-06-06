 
package com.xbongbong.dingxbb.dao;

import com.xbongbong.dingxbb.entity.OpportunityUserEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OpportunityUserDao {

	 
	public Integer insert(OpportunityUserEntity opportunityUser);
	public Integer update(OpportunityUserEntity opportunityUser);
	
	public Integer deleteByKey(@Param("key")Integer key, @Param("corpid")String corpid);
	
	public Integer deleteByOpportunityId(@Param("corpid")String corpid, @Param("opportunityId")Integer opportunityId);
	
	public OpportunityUserEntity getByKey (@Param("key") Integer key, @Param("corpid")String corpid);
	
	public OpportunityUserEntity getByUserIdOpportunityId (@Param("corpid")String corpid, @Param("userId") String userId, @Param("opportunityId") Integer opportunityId);
	
	public Integer deleteEntitys(@Param("param")Map<String ,Object>  param);
	
	public List<OpportunityUserEntity>  findEntitys(@Param("param")Map<String ,Object>  param);
	public Integer getEntitysCount(@Param("param")Map<String ,Object>  param);
		
	/**
	 * 批量更新销售机会销售团队中某公司某用户的姓名和头像
	 * @param corpid
	 * @param userId
	 * @param userName
	 * @param userAvatar
	 * @return
	 */
	public Integer dataConsistencyUpdateUser(@Param("corpid")String corpid, @Param("userId")String userId, @Param("userName")String userName, @Param("userAvatar")String userAvatar);
 
	
	/**
	 *通过customerIdIn获取实体
	 * @param param
	 * @return
	 * @author hongxiao.liu
	 * @time 2016-7-14
	 */
	public List<OpportunityUserEntity>  findEntitysJoinOpportunity(@Param("param")Map<String ,Object>  param);

}