 
package com.xbongbong.dingxbb.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xbongbong.dingxbb.entity.ContactEntity;

 
 
public interface ContactDao {

	 
	public Integer insert(ContactEntity contact);
	public Integer update(ContactEntity contact);
	
	 
	
	public Integer deleteByKey(@Param("key")Integer key,@Param("corpid")String corpid);
	public ContactEntity getByKey (@Param("key") Integer key, @Param("corpid")String corpid);
	
	
	public List<ContactEntity>  findEntitys(@Param("param")Map<String ,Object>  param);
	public Integer getEntitysCount(@Param("param")Map<String ,Object>  param);
	
	public Integer getEntitysJoinUserRelationCount(@Param("param")Map<String ,Object>  param);
	public List<ContactEntity>  findEntitysJoinUserRelation(@Param("param")Map<String ,Object>  param);
		
 
	/**
	 * 根据corpid或者customerId删除联系人
	 * @param corpid
	 * @param customerId
	 * @return
	 */
	public Integer deleteByCorpidAndCustomerId(@Param("corpid")String corpid, @Param("customerId")Integer customerId);
}