 
package com.xbongbong.dingxbb.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xbongbong.dingxbb.entity.CompanyEntity;

 
 
public interface CompanyDao {

	 
	public Integer insert(CompanyEntity company);
	public Integer update(CompanyEntity company);
	
	 
	
	public Integer deleteByKey(@Param("key") String key);
	public CompanyEntity getByKey (@Param("key") String key);
	
	
	public List<CompanyEntity>  findEntitys(@Param("param")Map<String ,Object>  param);
	public Integer getEntitysCount(@Param("param")Map<String ,Object>  param);
		
	public List<Map<String,Object>>  findFeeCompanyEntitys(@Param("param")Map<String ,Object>  param);	
 public Integer getEndTime(@Param("param")Map<String ,Object>  param);
public Integer insertFeeCompany(CompanyEntity company);	}