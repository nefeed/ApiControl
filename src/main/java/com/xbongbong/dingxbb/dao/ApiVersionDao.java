 
package com.xbongbong.dingxbb.dao;

import com.xbongbong.dingxbb.entity.ApiVersionEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


public interface ApiVersionDao {

	 
	public Integer insert(ApiVersionEntity apiVersion);
	public Integer update(ApiVersionEntity apiVersion);
	
	 
	
	public Integer deleteByKey(@Param("key") Integer key);
	public ApiVersionEntity getByKey(@Param("key") Integer key);


	public List<ApiVersionEntity>  findEntitys(@Param("param") Map<String, Object> param);
	public Integer getEntitysCount(@Param("param") Map<String, Object> param);
		
		
 
}