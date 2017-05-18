 
package com.xbongbong.dingxbb.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xbongbong.dingxbb.dao.ContactUserDao;
import com.xbongbong.dingxbb.entity.ContactEntity;
import com.xbongbong.dingxbb.entity.ContactUserEntity;
import com.xbongbong.dingxbb.entity.CustomerUserEntity;
import com.xbongbong.dingxbb.entity.UserEntity;
 

@Component
public class ContactUserModel  implements IModel{

	@Autowired
	private ContactUserDao dao;
	@Autowired
	private ContactModel contactModel;
	
	public Integer insert(Object entity){
		((ContactUserEntity)entity).setId(null);

		return dao.insert((ContactUserEntity)entity);
	}

	public Integer update(Object entity){
		return dao.update((ContactUserEntity)entity);
	}
	
	public Integer save(ContactUserEntity entity){
		if(entity.getId() == null || entity.getId().equals(0)) {
			((ContactUserEntity)entity).setId(null);

			return dao.insert((ContactUserEntity)entity);
		}
		return dao.update((ContactUserEntity)entity);
	}

	public Integer deleteByKey(Integer key, String corpid) {
		return dao.deleteByKey(key, corpid);
	}
	
	public ContactUserEntity getByKey( Integer key, Integer companyId){
		return dao.getByKey(key, companyId);
	}
	 
	public List<ContactUserEntity> findEntitys(Map<String ,Object>  param){
		return dao.findEntitys(param);
	}
	public Integer getEntitysCount(Map<String ,Object>  param){
		return dao.getEntitysCount(param);
	}
	//TODO 加公司ID
	public Integer deleteByUserCustomer(String userId, Integer customerId,String corpid) {
		return dao.deleteByUserCustomer(userId, customerId,corpid);
	}
	
	
	/**
	 * contactUser插入数据方法
	 * @param user
	 * @param customerId
	 */
	public void contactUserInsert(UserEntity user,Integer contactId, Integer isMain){

		String userId = user.getUserId();
		String corpid = user.getCorpid();
		String name = user.getName();
		ContactEntity contact=contactModel.getByKey(contactId, corpid);
		
		ContactUserEntity contactUserEntity = new ContactUserEntity();
		
		contactUserEntity.getCustomerId();
		contactUserEntity.setUserId(userId);
		contactUserEntity.setCorpid(corpid);
		contactUserEntity.setUserName(name);
		contactUserEntity.setContactId(contactId);
		contactUserEntity.setIsMain(isMain);
		if(contact!=null){
			contactUserEntity.setCustomerId(contact.getCustomerId());
		}
		insert(contactUserEntity);
	}
	
	
	/**
	 * user关联到客户对应的所有联系人上（用于客户新建联系人以及删除联系人）
	 * @param user
	 * @param customer
	 * @param operate 联系人操作（add delete）
	 */
	public void userRelateCustomerContact(ContactEntity contact, String operate, CustomerUserModel customerUserModel){
		if(contact == null){
			return ;
		}
		Integer customerId = contact.getCustomerId();
		
		Map<String,Object> param = new HashMap<String,Object>();
		if(operate.equals("add")){
			param.put("customerId", customerId);
			param.put("corpid", contact.getCorpid());
			param.put("del", 0);
			List<CustomerUserEntity> customerUserList = customerUserModel.findEntitys(param);//团队
			
			for(CustomerUserEntity customerUser : customerUserList){
				ContactUserEntity contactUser = new ContactUserEntity();
				contactUser.setCorpid(contact.getCorpid());
				contactUser.setUserId(customerUser.getUserId());
				contactUser.setContactId(contact.getId());
				contactUser.setCustomerId(customerId);
				insert(contactUser);
			}
		}else if(operate.equals("delete")){
//			contactUserModel.deleteByContact(contact.getId());
		}
		
	}
	
	/**
	 * 根据corpid或者customerId删除联系人关联信息
	 * @param corpid
	 * @param customerId
	 * @return
	 */
	public Integer deleteByCorpidAndCustomerId(String corpid, Integer customerId) {
		return dao.deleteByCorpidAndCustomerId(corpid, customerId);
	}
	
	/**
	 * 根据corpid和contactId删除联系人关联信息
	 * @param corpid
	 * @param contactId
	 * @return
	 */
	public Integer deleteByCorpidAndContactId(String corpid, Integer contactId) {
		return dao.deleteByCorpidAndContactId(corpid, contactId);
	}
}

 