 
package com.xbongbong.dingxbb.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xbongbong.dingxbb.entity.CustomerUserEntity;
import com.xbongbong.dingxbb.entity.UserEntity;

 
 
public interface CustomerUserDao {

	 
	public Integer insert(CustomerUserEntity customerUser);
	public Integer update(CustomerUserEntity customerUser);
	
	 
	
	public Integer deleteByKey(@Param("key")Integer key,@Param("corpid")String corpid);
	public CustomerUserEntity getByKey (@Param("key") Integer key, @Param("corpid")String corpid);

	public CustomerUserEntity getByUserIdCustomerId (@Param("corpid")String corpid, @Param("userId") String userId, @Param("customerId") Integer customerId);
	
	public List<CustomerUserEntity>  findEntitys(@Param("param")Map<String ,Object>  param);
	public Integer getEntitysCount(@Param("param")Map<String ,Object>  param);
		
	public List<UserEntity> findEntitysJoinUser(@Param("param")Map<String ,Object>  param);
	public Integer getEntitysCountJoinUser(@Param("param")Map<String ,Object>  param);
 
	/**
	 * 删除某客户的用户关联表数据
	 * @param corpid
	 * @param customerId
	 * @return
	 */
	public Integer deleteByCustomerId(@Param("corpid")String corpid, @Param("customerId")Integer customerId);
	
	public List<CustomerUserEntity>  getMainPerson(@Param("param")Map<String ,Object>  param);
}