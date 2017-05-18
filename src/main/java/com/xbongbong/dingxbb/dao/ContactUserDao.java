 
package com.xbongbong.dingxbb.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xbongbong.dingxbb.entity.ContactUserEntity;

 
 
public interface ContactUserDao {

	 
	public Integer insert(ContactUserEntity staffContact);
	public Integer update(ContactUserEntity staffContact);
	
	 
	
	public Integer deleteByKey(@Param("key")Integer key,@Param("corpid")String corpid);
	public ContactUserEntity getByKey (@Param("key") Integer key, @Param("companyId")Integer companyId);
	
	
	public List<ContactUserEntity>  findEntitys(@Param("param")Map<String ,Object>  param);
	public Integer getEntitysCount(@Param("param")Map<String ,Object>  param);
	public Integer deleteByUserCustomer(@Param("userId") String userId, @Param("customerId")Integer customerId,@Param("corpid") String corpid);
		
	/**
	 * 根据corpid和customerId删除联系人关联信息
	 * @param corpid
	 * @param customerId
	 * @return
	 */
	public Integer deleteByCorpidAndCustomerId(@Param("corpid")String corpid, @Param("customerId")Integer customerId);
	
	/**
	 * 根据corpid和contactId删除联系人关联信息
	 * @param corpid
	 * @param contactId
	 * @return
	 */
	public Integer deleteByCorpidAndContactId(@Param("corpid")String corpid, @Param("contactId")Integer contactId);
}