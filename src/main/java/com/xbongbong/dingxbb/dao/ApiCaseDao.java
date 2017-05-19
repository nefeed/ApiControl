 
package com.xbongbong.dingxbb.dao;

import com.xbongbong.dingxbb.entity.ApiCaseEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


public interface ApiCaseDao {

	 
	public Integer insert(ApiCaseEntity apiCase);
	public Integer update(ApiCaseEntity apiCase);
	
	 
	
	public Integer deleteByKey(@Param("key") Integer key);
	public Integer deleteByApiId(@Param("apiId") Integer apiId);
	public ApiCaseEntity getByKey(@Param("key") Integer key);


	public List<ApiCaseEntity>  findEntitys(@Param("param") Map<String, Object> param);
	public Integer getEntitysCount(@Param("param") Map<String, Object> param);
		
		
 
}