 
package com.xbongbong.dingxbb.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xbongbong.dingxbb.entity.UserEntity;

 
 
public interface UserDao {

	 
	public Integer insert(UserEntity user);
	public Integer update(UserEntity user);
	
	public Integer deleteByKey(@Param("key")String key, @Param("corpid")String corpid);
	public UserEntity getByKey (@Param("key") String key, @Param("corpid")String corpid);
	
	
	public List<UserEntity>  findEntitys(@Param("param")Map<String ,Object>  param);
	public Integer getEntitysCount(@Param("param")Map<String ,Object>  param);
		
	public List<UserEntity>  findEntitysJoin(@Param("param")Map<String ,Object>  param);
	public List<String>  getUserIdsJoin(@Param("param")Map<String ,Object>  param);
	public List<String>  getUserIds(@Param("param")Map<String ,Object>  param);
	
	/**
	 * 删除某公司的用户
	 * @param corpid
	 * @return
	 */
	public Integer deleteByCorpId( @Param("corpid")String corpid);
	
	/**
	 * 更新微应用可见状态,单个公司所有用户
	 * @param corpid	公司ID
	 * @param isVisible 1可见[授权]，0不可见[不授权]
	 * @return 
	 * @author kaka
	 * @time 2016年7月18日 下午3:52:16
	 */
	public Integer updateAllVisibleByCorpId(@Param("corpid")String corpid, @Param("isVisible")Integer isVisible);
	
	/**
	 * 更新微应用可见状态 ,单个公司单个或多个用户
	 * @param corpid 公司ID
	 * @param isVisible 1可见[授权]，0不可见[不授权]
	 * @param userIdIn 需要修改的用户列表
	 * @return
	 * @author kaka
	 * @time 2016年7月18日 下午3:55:23
	 */
	public Integer updateVisibleByCorpIdAndUserIdIn(@Param("corpid")String corpid, @Param("isVisible")Integer isVisible, @Param("userIdIn")List<String> userIdIn);
	
	/**
	 * 更新微应用不可见状态 ,单个公司单个或多个用户
	 * @param corpid 公司ID
	 * @param isVisible 1可见[授权]，0不可见[不授权]
	 * @param userIdNotIn 不可见的用户列表
	 * @return
	 * @author kaka
	 * @time 2016年7月18日 下午3:55:23
	 */
	public Integer updateVisibleByCorpIdAndUserIdNotIn(@Param("corpid")String corpid, @Param("isVisible")Integer isVisible, @Param("userIdNotIn")List<String> userIdNotIn);
	
}