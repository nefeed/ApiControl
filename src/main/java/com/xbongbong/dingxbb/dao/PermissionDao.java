 
package com.xbongbong.dingxbb.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xbongbong.dingxbb.entity.PermissionEntity;

 
 
public interface PermissionDao {

	 
	public Integer insert(PermissionEntity permission);
	public Integer update(PermissionEntity permission);
	
	 
	
	public Integer deleteByKey(@Param("key")Integer key,@Param("companyId")Integer companyId);
	public PermissionEntity getByKey (@Param("key") String key);
	
	
	public List<PermissionEntity>  findEntitys(@Param("param")Map<String ,Object>  param);
	public Integer getEntitysCount(@Param("param")Map<String ,Object>  param);
		
		
 
}