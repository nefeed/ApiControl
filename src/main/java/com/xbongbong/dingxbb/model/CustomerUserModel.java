 
package com.xbongbong.dingxbb.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xbongbong.dingxbb.dao.CustomerUserDao;
import com.xbongbong.dingxbb.entity.CustomerEntity;
import com.xbongbong.dingxbb.entity.CustomerUserEntity;
import com.xbongbong.dingxbb.entity.UserEntity;
import com.xbongbong.util.StringUtil;
 

@Component
public class CustomerUserModel  implements IModel{

	@Autowired
	private CustomerUserDao dao;
	
	public Integer insert(Object entity){
		((CustomerUserEntity)entity).setId(null);

		return dao.insert((CustomerUserEntity)entity);
	}

	public Integer update(Object entity){
		return dao.update((CustomerUserEntity)entity);
	}
	
	public Integer save(Object entity){
		if(((CustomerUserEntity)entity).getId() == null || ((CustomerUserEntity)entity).getId().equals(0)) {
			((CustomerUserEntity)entity).setId(null);

			return dao.insert((CustomerUserEntity)entity);
		}
		return dao.update((CustomerUserEntity)entity);
	}

	public Integer deleteByKey(Integer key, String corpid) {
		return dao.deleteByKey(key, corpid);
	}
	
	public CustomerUserEntity getByKey( Integer key, String corpid){
		return dao.getByKey(key, corpid);
	}
	
	public CustomerUserEntity getByUserIdCustomerId(String corpid, String userId, Integer customerId) {
		return dao.getByUserIdCustomerId(corpid, userId, customerId);
	}
	 
	public List<CustomerUserEntity> findEntitys(Map<String ,Object>  param){
		return dao.findEntitys(param);
	}
	public Integer getEntitysCount(Map<String ,Object>  param){
		return dao.getEntitysCount(param);
	}
	 
	public List<UserEntity> findEntitysJoinUser(Map<String ,Object>  param){
		return dao.findEntitysJoinUser(param);
	}
	public Integer getEntitysCountJoinUser(Map<String ,Object>  param){
		return dao.getEntitysCountJoinUser(param);
	}
	/**
	 * customerUser插入数据方法
	 * @param user
	 * @param customerId
	 */
	public void customerUserInsert(UserEntity user,Integer customerId, Integer isMain){

		String userId = user.getUserId();
		String corpid = user.getCorpid();
		String name = user.getName();
		
		CustomerUserEntity customerUserEntity = new CustomerUserEntity();
		
		customerUserEntity.setUserId(userId);
		customerUserEntity.setCorpid(corpid);
		customerUserEntity.setUserName(name);
		customerUserEntity.setCustomerId(customerId);
		customerUserEntity.setIsMain(isMain);
		
		insert(customerUserEntity);
	}
	
	/**
	 * 获取客户负责人
	 * @param customerId
	 * @return
	 */
	public List<UserEntity> getCustomerUserList(Integer customerIdJoin, String corpidJoin){
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("customerIdJoin", customerIdJoin);
		param.put("corpidJoin", corpidJoin);
		param.put("del", 0);
		param.put("orderByStr", "is_main desc");
		return findEntitysJoinUser(param);
	}
	/**
	 * 判断是否负责人     -1无关系   0非主负责人   1主负责人   2老板
	 * @param customerId
	 * @param userEntity
	 * @return
	 */
	public Integer isMain(Integer customerId, UserEntity userEntity){
		
		if(userEntity.isBoss()){
			return 2;
		}
		
		Integer flag = -1;
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("customerId", customerId);
		param.put("userId", userEntity.getUserId());
		param.put("corpid", userEntity.getCorpid());
		param.put("del", 0);
		List<CustomerUserEntity> customerUserList = findEntitys(param);
		for(CustomerUserEntity customerUserEntity : customerUserList){
			flag = 0;
			if(customerUserEntity.getIsMain().equals(1)){
				flag = 1;
				break;
			}
		}
		return flag;
	}
	
	/**
	 * 删除某客户的用户关联表数据
	 * @param corpid
	 * @param customerId
	 * @return
	 */
	public Integer deleteByCustomerId(String corpid, Integer customerId) {
		return dao.deleteByCustomerId(corpid, customerId);
	}
	
	public List<CustomerEntity> setMainPerson(List<CustomerEntity> customerList, String corpid){
		
		List<Integer> customerIdIn = new ArrayList<Integer>();
		customerIdIn.add(-1);
		for(CustomerEntity customerEntity : customerList){
			customerIdIn.add(customerEntity.getId());
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("customerIdIn", customerIdIn);
		param.put("isMain", 1);
		param.put("corpid", corpid);
		
		List<CustomerUserEntity> customerUserList = dao.getMainPerson(param);
		
		Map<Integer,String> customerUserMap = new HashMap<Integer,String>();
		for(CustomerUserEntity customerUserEntity : customerUserList){
			customerUserMap.put(customerUserEntity.getCustomerId(), customerUserEntity.getUserName());
		}
		
		for(CustomerEntity customerEntity : customerList){
			String customerUser  = customerUserMap.get(customerEntity.getId());
			if(!StringUtil.isEmpty(customerUser) ){
				customerEntity.setUserName(customerUser );
			}
		}
		
		return customerList;
	}
}

 