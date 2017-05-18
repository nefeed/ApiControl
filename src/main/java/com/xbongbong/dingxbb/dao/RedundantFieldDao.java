 
package com.xbongbong.dingxbb.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xbongbong.dingxbb.entity.RedundantFieldEntity;

 
 
public interface RedundantFieldDao {

	 
	public Integer insert(RedundantFieldEntity redundantField);
	public Integer update(RedundantFieldEntity redundantField);
	
	public Integer deleteByKey(@Param("key")String key, @Param("corpid")String corpid);
	public RedundantFieldEntity getByKey (@Param("key") Integer key, @Param("corpid")String corpid);
	
	
	public List<RedundantFieldEntity>  findEntitys(@Param("param")Map<String ,Object>  param);
	public Integer getEntitysCount(@Param("param")Map<String ,Object>  param);
		
	public RedundantFieldEntity getByRefTypeAndRefId(@Param("corpid")String corpid, @Param("refType") Integer refType, @Param("refId") Integer refId);
 
}