 
package com.xbongbong.dingxbb.dao;

import java.util.List;
import java.util.Map;
import java.lang.Integer;
import org.apache.ibatis.annotations.Param;
import com.xbongbong.dingxbb.entity.ApiTokenEntity;

 
 
public interface ApiTokenDao {

	 
	public Integer insert(ApiTokenEntity apiToken);
	public Integer update(ApiTokenEntity apiToken);
	
	 
	
	public Integer deleteByKey(@Param("key") Integer key);
	public ApiTokenEntity getByKey (@Param("key") Integer key);
	public ApiTokenEntity getByCorpId (@Param("corpid") String corpid);
	
	public List<ApiTokenEntity>  findEntitys(@Param("param")Map<String ,Object>  param);
	public Integer getEntitysCount(@Param("param")Map<String ,Object>  param);
		
		
 
}