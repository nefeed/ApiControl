 
package com.xbongbong.dingxbb.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xbongbong.dingxbb.entity.DataDictionaryEntity;

 
 
public interface DataDictionaryDao {

	 
	public Integer insert(DataDictionaryEntity dataDictionary);
	public Integer update(DataDictionaryEntity dataDictionary);
	
	public Integer deleteByKey(@Param("key")String key, @Param("corpid")String corpid);
	public DataDictionaryEntity getByKey (@Param("key") Integer key, @Param("corpid")String corpid);
	
	
	public List<DataDictionaryEntity>  findEntitys(@Param("param")Map<String ,Object>  param);
	public Integer getEntitysCount(@Param("param")Map<String ,Object>  param);
	
	public Integer sort(@Param("param")Map<String ,Object>  param, @Param("sortMap")Map<String ,Integer>  sortMap);
		
 
}