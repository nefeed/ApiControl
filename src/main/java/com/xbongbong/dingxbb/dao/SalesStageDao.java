 
package com.xbongbong.dingxbb.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xbongbong.dingxbb.entity.SalesStageEntity;

 
 
public interface SalesStageDao {

	 
	public Integer insert(SalesStageEntity salesStage);
	public Integer update(SalesStageEntity salesStage);
	
	public Integer deleteByKey(@Param("key")String key, @Param("corpid")String corpid);
	public SalesStageEntity getByKey (@Param("key") String key, @Param("corpid")String corpid);
	public SalesStageEntity getByKey (@Param("key") Integer key, @Param("corpid")String corpid);
	
	
	public List<SalesStageEntity>  findEntitys(@Param("param")Map<String ,Object>  param);
	public Integer getEntitysCount(@Param("param")Map<String ,Object>  param);
	
	public Integer sort(@Param("param")Map<String ,Object>  param, @Param("sortMap")Map<String ,Integer>  sortMap);
		
 
}