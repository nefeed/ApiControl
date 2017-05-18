 
package com.xbongbong.dingxbb.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xbongbong.dingxbb.entity.RedundantFieldTemplateEntity;

 
 
public interface RedundantFieldTemplateDao {

	 
	public Integer insert(RedundantFieldTemplateEntity redundantFieldTemplate);
	public Integer update(RedundantFieldTemplateEntity redundantFieldTemplate);
	
	public Integer disableTemplateByKey(@Param("key")Integer key, @Param("corpid")String corpid);
	
	public Integer deleteByKey(@Param("key")Integer key, @Param("corpid")String corpid);
	public RedundantFieldTemplateEntity getByKey (@Param("key") Integer key, @Param("corpid")String corpid, @Param("del")Integer del);
	
	public RedundantFieldTemplateEntity getByCorpIdAndDDFormUuid (@Param("corpid")String corpid, @Param("ddFormUuid") String ddFormUuid, @Param("type")Integer type, @Param("del")Integer del);

	public RedundantFieldTemplateEntity getByKeyPublic(@Param("key") Integer key);
	
	public RedundantFieldTemplateEntity getMyNoCopyTemplate(@Param("corpid") String corpid, @Param("type") Integer type);
	public RedundantFieldTemplateEntity getByKeyPublicOrMine(@Param("key") Integer key, @Param("corpid")String corpid);
	
	public List<RedundantFieldTemplateEntity>  findEntitys(@Param("param")Map<String ,Object>  param);
	public Integer getEntitysCount(@Param("param")Map<String ,Object>  param);
		
		
 
}