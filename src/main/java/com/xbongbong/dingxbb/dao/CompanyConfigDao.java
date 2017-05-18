 
package com.xbongbong.dingxbb.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xbongbong.dingxbb.entity.CompanyConfigEntity;

 
 
public interface CompanyConfigDao {

	 
	public Integer insert(CompanyConfigEntity companyConfig);
	public Integer update(CompanyConfigEntity companyConfig);
	
	 
	
	public Integer deleteByKey(@Param("key") Integer key, @Param("corpid") String corpid);
	public CompanyConfigEntity getByKey (@Param("key") Integer key, @Param("corpid") String corpid);
	
	
	public List<CompanyConfigEntity>  findEntitys(@Param("param")Map<String ,Object>  param);
	public Integer getEntitysCount(@Param("param")Map<String ,Object>  param);
		
		
 
}