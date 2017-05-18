package com.xbongbong.dingxbb.dao;


import com.xbongbong.dingxbb.entity.ApiDocEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ApiDocDao {

	 
	public Integer insert(ApiDocEntity apiDoc);
	public Integer update(ApiDocEntity apiDoc);
	
	 
	
	public Integer deleteByKey(@Param("key") Integer key);
	public ApiDocEntity getByKey(@Param("key") Integer key);


	public List<ApiDocEntity> findEntitys(@Param("param") Map<String, Object> param);
	public Integer getEntitysCount(@Param("param") Map<String, Object> param);
		
		
 
}