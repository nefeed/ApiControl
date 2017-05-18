 
package com.xbongbong.dingxbb.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xbongbong.dingxbb.entity.WeakManageEntity;

 
 
public interface WeakManageDao {

	 
	public Integer insert(WeakManageEntity weakManage);
	public Integer update(WeakManageEntity weakManage);
	
	public Integer deleteByKey(@Param("key")Integer key,@Param("corpid")String corpid);
	public WeakManageEntity getByKey (@Param("key") Integer key, @Param("corpid")String corpid);
	
	
	public List<WeakManageEntity>  findEntitys(@Param("param")Map<String ,Object>  param);
	public Integer getEntitysCount(@Param("param")Map<String ,Object>  param);
	
	public List<Integer> findOnlyIds(@Param("param")Map<String ,Object>  param);
		
		
 
}