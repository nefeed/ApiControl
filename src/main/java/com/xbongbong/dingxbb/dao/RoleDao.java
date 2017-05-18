 
package com.xbongbong.dingxbb.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xbongbong.dingxbb.entity.RoleEntity;

 
 
public interface RoleDao {

	 
	public Integer insert(RoleEntity role);
	public Integer update(RoleEntity role);
	
	 
	
	public Integer deleteByKey(@Param("key")Integer key,@Param("corpid")String corpid);
	public RoleEntity getByKey (@Param("key") Integer key,@Param("corpid")String corpid);
	
	public List<RoleEntity>  findEntitys(@Param("param")Map<String ,Object>  param);
	public Integer getEntitysCount(@Param("param")Map<String ,Object>  param);
		
		
 
}