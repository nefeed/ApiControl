 
package com.xbongbong.dingxbb.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xbongbong.dingxbb.entity.CustomerEntity;

 
 
public interface CustomerDao {

	 
	public Integer insert(CustomerEntity customer);
	public Integer update(CustomerEntity customer);
	
	public Integer deleteByKey(@Param("key")Integer key, @Param("corpid")String corpid);
	public CustomerEntity getByKey (@Param("key") String key, @Param("corpid")String corpid);
	public CustomerEntity getByKey (@Param("key") Integer key, @Param("corpid")String corpid);
	
	
	public List<CustomerEntity>  findEntitys(@Param("param")Map<String ,Object>  param);
	public Integer getEntitysCount(@Param("param")Map<String ,Object>  param);
	
	public Integer getJoinUserCustomerCount(@Param("param")Map<String ,Object>  param);
		
	public List<CustomerEntity>  findEntitysJoinCustomerUser(@Param("param")Map<String ,Object>  param);
	public List<CustomerEntity>  findEntitysJoinCustomerUserIsMain(@Param("param")Map<String ,Object>  param);
	
	public List<Integer>  getCustomerIdsJoinCustomerUser(@Param("param")Map<String ,Object>  param);
	public List<Integer>  getCustomerAddTimeJoinCustomerUser(@Param("param")Map<String ,Object>  param);
	public Integer getJoinCustomerUserCount(@Param("param")Map<String ,Object>  param);
 
}