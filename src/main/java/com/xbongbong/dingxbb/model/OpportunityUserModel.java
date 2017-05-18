package com.xbongbong.dingxbb.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xbongbong.dingxbb.dao.OpportunityUserDao;
import com.xbongbong.dingxbb.entity.CustomerUserEntity;
import com.xbongbong.dingxbb.entity.OpportunityUserEntity;
import com.xbongbong.dingxbb.entity.UserEntity;

 

@Component
public class OpportunityUserModel  implements IModel{

	@Autowired
	private OpportunityUserDao dao;
	
	public Integer insert(Object entity){
		return dao.insert((OpportunityUserEntity)entity);
	}

	public Integer update(Object entity){
		return dao.update((OpportunityUserEntity)entity);
	}
	
	public Integer save(OpportunityUserEntity entity){
		if(entity.getId() == null || entity.getId().equals(0)) {
			return dao.insert((OpportunityUserEntity)entity);
		}
		return dao.update((OpportunityUserEntity)entity);
	}
	 
	public List<OpportunityUserEntity> findEntitys(Map<String ,Object>  param){
		return dao.findEntitys(param);
	}
	public Integer getEntitysCount(Map<String ,Object>  param){
		return dao.getEntitysCount(param);
	}
	
	public OpportunityUserEntity getByUserIdOpportunityId(String corpid, String userId, Integer opportunityId) {
		return dao.getByUserIdOpportunityId(corpid, userId, opportunityId);
	}

	public OpportunityUserEntity getByKey(Integer key, String corpid) {
		return dao.getByKey(key, corpid);
	}

	public Integer deleteByKey(Integer key, String corpid) {
		return dao.deleteByKey(key, corpid);
	}
	
	public Integer deleteByOpportunityId(String corpid, Integer opportunityId) {
		return dao.deleteByOpportunityId(corpid, opportunityId);
	}
	
	public Integer deleteEntitys(Map<String ,Object>  param){
		return dao.deleteEntitys(param);
	}
	
	 
	/**
	 * 销售团队
	 * @param corpid
	 * @param opportunityId
	 * @return
	 */
	public List<OpportunityUserEntity> getMyTeam(String corpid, Integer opportunityId) {
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("del", 0);
		param.put("orderByStr", "is_main desc, add_time asc");//主要负责人在上
		param.put("corpid", corpid);
		param.put("opportunityId", opportunityId);
		
		List<OpportunityUserEntity> opportunityUserList = findEntitys(param);
		
		return opportunityUserList;
	}

	/**
	 * opportunityUserInsert插入数据方法
	 * @param userEntity
	 * @param opportunityId
	 * @param isMain 第一个创建人为主要负责人 isMain为1，其他情况不是主要负责人 isMain 0
	 */
	public void opportunityUserInsert(UserEntity userEntity,Integer opportunityId, int isMain){
	    if (userEntity == null) {
			return;
		}
		
		String userId = userEntity.getUserId();
		String corpid = userEntity.getCorpid();
		String userName = userEntity.getName();
		String userAvatar = userEntity.getAvatar();
		
		OpportunityUserEntity opportunityUser = new OpportunityUserEntity();
		opportunityUser.setCorpid(corpid);
		opportunityUser.setUserId(userId);
		opportunityUser.setUserName(userName);
		opportunityUser.setUserAvatar(userAvatar);
		opportunityUser.setOpportunityId(opportunityId);
		opportunityUser.setIsMain(isMain);
		
		insert(opportunityUser);
	}
	
	/**
	 * 批量更新销售机会销售团队中某公司某用户的姓名和头像
	 * @param corpid
	 * @param userId
	 * @param userName
	 * @param userAvatar
	 * @return
	 */
	public Integer dataConsistencyUpdateUser(String corpid, String userId, String userName, String userAvatar) {
		return dao.dataConsistencyUpdateUser(corpid, userId, userName, userAvatar);
	}
	
	/**
	 * 通过customerIdIn获取实体
	 * @param param
	 * @return
	 * @author hongxiao.liu
	 * @time 2016-7-14 上午10:46:56
	 */
	public List<OpportunityUserEntity> findEntitysJoinOpportunity(Map<String ,Object>  param){
		return dao.findEntitysJoinOpportunity(param);
	}

	/**
	 * opportunityUserInsert插入数据方法
	 * @param userEntity
	 * @param opportunityId
	 * @param isMain 第一个创建人为主要负责人 isMain为1，其他情况不是主要负责人 isMain 0
	 */
	public void opportunityUserInsert(CustomerUserEntity customerUserEntity,Integer opportunityId, int isMain){
	    if (customerUserEntity == null) {
			return;
		}
		
		String userId = customerUserEntity.getUserId();
		String corpid = customerUserEntity.getCorpid();
		String userName = customerUserEntity.getUserName();
		String userAvatar = "";
		
		OpportunityUserEntity opportunityUser = new OpportunityUserEntity();
		opportunityUser.setCorpid(corpid);
		opportunityUser.setUserId(userId);
		opportunityUser.setUserName(userName);
		opportunityUser.setUserAvatar(userAvatar);
		opportunityUser.setOpportunityId(opportunityId);
		opportunityUser.setIsMain(isMain);
		
		insert(opportunityUser);
	}
	
}

 