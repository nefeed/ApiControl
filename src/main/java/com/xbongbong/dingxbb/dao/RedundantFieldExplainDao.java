 
package com.xbongbong.dingxbb.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xbongbong.dingxbb.entity.RedundantFieldExplainEntity;

 
 
public interface RedundantFieldExplainDao {

	 
	public Integer insert(RedundantFieldExplainEntity redundantFieldExplain);
	public Integer update(RedundantFieldExplainEntity redundantFieldExplain);
	
	public Integer deleteByKey(@Param("key")String key, @Param("corpid")String corpid);
	public RedundantFieldExplainEntity getByKey (@Param("key") String key, @Param("corpid")String corpid);
	
	
	public List<RedundantFieldExplainEntity>  findEntitys(@Param("param")Map<String ,Object>  param);
	public Integer getEntitysCount(@Param("param")Map<String ,Object>  param);
	
	public Integer deleteByTemplateId(@Param("corpid")String corpid, @Param("templateId")Integer templateId);
}