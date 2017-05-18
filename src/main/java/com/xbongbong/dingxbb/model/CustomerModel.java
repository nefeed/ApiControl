 
package com.xbongbong.dingxbb.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xbongbong.dingxbb.dao.CustomerDao;
import com.xbongbong.dingxbb.entity.CustomerEntity;
import com.xbongbong.dingxbb.entity.DataDictionaryEntity;
import com.xbongbong.dingxbb.entity.UserEntity;
import com.xbongbong.util.CommentUtil;
import com.xbongbong.util.PageHelper;

@Component
public class CustomerModel  implements IModel{

	@Autowired
	private CustomerDao dao;
	
	@Autowired
	private UserModel userModel;
	
	@Autowired
	private DataDictionaryModel dataDictionaryModel;
	
	public Integer insert(Object entity){
		return dao.insert((CustomerEntity)entity);
	}

	public Integer update(Object entity){
		return dao.update((CustomerEntity)entity);
	}
	
	public Integer save(CustomerEntity entity){
		if(entity.getId() == null || entity.getId().equals(0)) {
			return dao.insert((CustomerEntity)entity);
		}
		return dao.update((CustomerEntity)entity);
	}

	public List<CustomerEntity> findEntitys(Map<String ,Object>  param){
		return dao.findEntitys(param);
	}
	public Integer getEntitysCount(Map<String ,Object>  param){
		return dao.getEntitysCount(param);
	}
	public Integer getJoinUserCustomerCount(Map<String ,Object>  param){
		return dao.getJoinUserCustomerCount(param);
	}

	public CustomerEntity getByKey(Integer key, String corpid) {
		return dao.getByKey(key, corpid);
	}
	
	public CustomerEntity getByKey(String key, String corpid) {
		return dao.getByKey(key, corpid);
	}

	public Integer deleteByKey(Integer key, String corpid) {
		return dao.deleteByKey(key, corpid);
	}
	
	public Integer getJoinCustomerUserCount(Map<String, Object> param) {
		return dao.getJoinCustomerUserCount(param);
	}
	
	public List<CustomerEntity> findEntitysJoinCustomerUser(Map<String, Object> param) {
		return dao.findEntitysJoinCustomerUser(param);
	}
	
	public List<CustomerEntity> findEntitysJoinCustomerUserIsMain(Map<String, Object> param) {
		return dao.findEntitysJoinCustomerUserIsMain(param);
	}
	
	public List<Integer> getCustomerIdsJoinCustomerUser(Map<String, Object> param) {
		return dao.getCustomerIdsJoinCustomerUser(param);
	}
	 

	public PageHelper getPageHelper(Map<String, Object> param, Integer pageSize) {
		Integer rowCounts = getJoinCustomerUserCount(param);
		Integer page = (Integer) param.get("page");

		PageHelper pageHelper = new PageHelper(page, pageSize);
		pageHelper.setRowsCount(rowCounts);
		return pageHelper;
	}

	public List<CustomerEntity> getEntityList(Map<String, Object> param,PageHelper pageHelper) {
		Integer pageNum = pageHelper.getPageSize();
		Integer start = (pageHelper.getCurrentPageNum() - 1) * pageNum;

		param.put("pageNum", pageNum);
		param.put("start", start);
		return findEntitysJoinCustomerUser(param);
	}
	
	public List<CustomerEntity> getEntityListIsMain(Map<String, Object> param,PageHelper pageHelper) {
		Integer pageNum = pageHelper.getPageSize();
		Integer start = (pageHelper.getCurrentPageNum() - 1) * pageNum;
		
		param.put("pageNum", pageNum);
		param.put("start", start);
		return findEntitysJoinCustomerUserIsMain(param);
	}
	
	public List<Integer> getCustomerAddTimeJoinCustomerUser(Map<String, Object> param){
		return dao.getCustomerAddTimeJoinCustomerUser(param);
	}
	
	/**
	 * 获取公海池客户
	 * @param userId
	 * @param corpid
	 * @return
	 */
	public List<CustomerEntity> getPublicCustomerlist(String corpid) {
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("corpid", corpid);
		param.put("isPublic", 1);
		param.put("del", 0);
		return findEntitys(param);
	}
	
	/**
	 * 获取客户总数量方法
	 * 
	 */
	
	public Integer getCustomerCustomerTotalSizex(UserEntity userEntity,Integer page, Integer pageSize,String nameLike){
		if (userEntity == null) {
			return 0;
		}
		String corpid = userEntity.getCorpid();
		
		Map<String,Object> param = new HashMap<String,Object>();
		//条件
		param.put("isMainJoin", 1);
		CommentUtil.addToMap(param, "page", page);
		CommentUtil.addToMap(param, "delJoin", 0);
		CommentUtil.addToMap(param, "nameLike", nameLike);
		//下属条件
		List<String> userIdIn = userModel.getSubIdList(userEntity);
		//自己
		userIdIn.add(""+userEntity.getUserId());
		CommentUtil.addToMap(param, "userIdInJoin", userIdIn);
		CommentUtil.addToMap(param, "corpidJoin", corpid);
		PageHelper pageHelper = this.getPageHelper(param, pageSize);
		return pageHelper.getPageTotal();
	}
	
	public List<DataDictionaryEntity> customerStatusDataList(String corpid){
		Map<String,Object> param = new HashMap<String,Object>();
		CommentUtil.addToMap(param, "corpid", corpid);
		CommentUtil.addToMap(param, "enable", 1);
		CommentUtil.addToMap(param, "type", 3);
		
		List<DataDictionaryEntity> dataDicList = dataDictionaryModel.findEntitys(param);
		if(dataDicList == null || dataDicList.size() < 1){
			CommentUtil.addToMap(param, "corpid", "0");
			dataDicList = dataDictionaryModel.findEntitys(param);
		}
		
		return dataDicList;
	}
}

 