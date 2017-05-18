 
package com.xbongbong.dingxbb.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.xbongbong.dingxbb.dao.ContactDao;
import com.xbongbong.dingxbb.entity.ContactEntity;
import com.xbongbong.dingxbb.entity.ContactUserEntity;
import com.xbongbong.dingxbb.entity.CustomerEntity;
import com.xbongbong.dingxbb.entity.UserEntity;
import com.xbongbong.util.CommentUtil;
import com.xbongbong.util.PageHelper;
import com.xbongbong.util.StringUtil;
 

@Component
public class ContactModel  implements IModel{

	@Autowired
	private ContactDao dao;
	
	public Integer insert(Object entity){
		((ContactEntity)entity).setId(null);
		return dao.insert((ContactEntity)entity);
	}

	public Integer update(Object entity){
		return dao.update((ContactEntity)entity);
	}
	
	public Integer save(Object entity){
		if(((ContactEntity)entity).getId() == null || ((ContactEntity)entity).getId().equals(0)) {
			((ContactEntity)entity).setId(null);

			return dao.insert((ContactEntity)entity);
		}
		return dao.update((ContactEntity)entity);
	}

	public Integer deleteByKey(Integer key, String corpid) {
		return dao.deleteByKey(key, corpid);
	}
	
	public ContactEntity getByKey( Integer key, String corpid){
		return dao.getByKey(key, corpid);
	}
	 
	public List<ContactEntity> findEntitys(Map<String ,Object>  param){
		return dao.findEntitys(param);
	}
	
	//筛选需要companyId
	public List<ContactEntity> getListByCustomer(Integer customerId, String corpid){
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("corpid", corpid);
		param.put("customerId", customerId);
		param.put("del", 0);
		List<ContactEntity> list = findEntitys(param);
		for(ContactEntity contactEntity : list){
			contactEntity.setPhoneJsonArray(JSONArray.parseArray(contactEntity.getPhone()));
		}
		return list;
	}
	
	public Integer getEntitysCount(Map<String ,Object>  param){
		return dao.getEntitysCount(param);
	}
	 
	public Integer getEntitysJoinStaffRelationCount(Map<String ,Object>  param){
		return dao.getEntitysJoinUserRelationCount(param);
	}
	
	public List<ContactEntity> findEntitysJoinUserRelation (Map<String ,Object>  param){
		return dao.findEntitysJoinUserRelation(param);
	}
	
	public PageHelper getPageHelper(Map<String, Object> param,Integer pageSize) {
		Integer rowCounts = dao.getEntitysJoinUserRelationCount(param);
		Integer page = (Integer) param.get("page");
		if (page == null)
			page = 1;
		param.remove("page");
		String pageUriStr = CommentUtil.mapToUrl(param);
		param.put("page", page);

		PageHelper pageHelper = new PageHelper(page, pageSize);
		pageHelper.setRowsCount(rowCounts);
		pageHelper.setPageUriStr(pageUriStr);
		return pageHelper;
	}

	public List<ContactEntity> getEntityList(Map<String, Object> param,PageHelper pageHelper) {
		Integer pageNum = pageHelper.getPageSize();
		Integer start = (pageHelper.getCurrentPageNum() - 1) * pageNum;

		param.put("pageNum", pageNum);
		param.put("start", start);
		return dao.findEntitysJoinUserRelation(param);
	}
	/**
	 * user关联到客户对应的所有联系人上（用于客户新建、添加客户负责人以及删除客户负责人）
	 * @param user
	 * @param customer
	 * @param operate 客户操作（add delete）
	 */
	public void userRelateCustomerContact(UserEntity user, CustomerEntity customer, String operate, ContactUserModel contactUserModel,Integer isMain){
		if(user == null || customer == null){
			return ;
		}
		Map<String,Object> param = new HashMap<String,Object>();
		if(operate.equals("add")){
			param.put("customerId", customer.getId());
			param.put("corpid", user.getCorpid());
			param.put("del", 0);
			List<ContactEntity> contactList = findEntitys(param);
			for(ContactEntity contactEntity : contactList){
				ContactUserEntity contactUser = new ContactUserEntity();
				contactUser.setCorpid(user.getCorpid());
				contactUser.setUserId(user.getUserId());
				contactUser.setUserName(user.getName());
				contactUser.setContactId(contactEntity.getId());
				contactUser.setCustomerId(customer.getId());
				contactUser.setIsMain(isMain);
				contactUserModel.insert(contactUser);
			}
		}else if(operate.equals("delete")){
			contactUserModel.deleteByUserCustomer(user.getUserId(), customer.getId(),customer.getCorpid());
		}
		
	}
	
	/**
	 * 判断联系人手机号是是否重复
	 * @param tel
	 * @param id
	 * @param corpid
	 * @param customerId
	 * @return
	 */
	public boolean contactRepeat(String tel, Integer id, String corpid, Integer customerId){
		boolean flag = false;
		if(!StringUtil.isEmpty(tel)){
			Map<String,Object> param = new HashMap<String,Object>();
			param.put("customerId", customerId);
			param.put("phoneLike", tel);
			param.put("corpid", corpid);
			param.put("del", 0);
			List<ContactEntity> contactList = findEntitys(param);
			for(ContactEntity contactEntity : contactList){
				if(!contactEntity.getId().equals(id)){
					flag = true;
					break;
				}
			}
		}
		return flag;
	}
	
	/**
	 * 根据corpid或者customerId删除联系人
	 * @param corpid
	 * @param customerId
	 * @return
	 */
	public Integer deleteByCorpidAndCustomerId(String corpid, Integer customerId) {
		return dao.deleteByCorpidAndCustomerId(corpid, customerId);
	}

	public List<ContactEntity> getByContactName(String name, String corpid ,Integer customerId) {
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("name", name);
		param.put("corpid", corpid);
		param.put("customerId", customerId);
		param.put("del", 0);
		return findEntitys(param);
	}
}

 