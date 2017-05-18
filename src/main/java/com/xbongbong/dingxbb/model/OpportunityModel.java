 
package com.xbongbong.dingxbb.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xbongbong.dingxbb.dao.OpportunityDao;
import com.xbongbong.dingxbb.entity.CustomerEntity;
import com.xbongbong.dingxbb.entity.OpportunityEntity;
import com.xbongbong.dingxbb.entity.OpportunityUserEntity;
import com.xbongbong.dingxbb.entity.UserEntity;
import com.xbongbong.util.DateUtil;
import com.xbongbong.util.PageHelper;
 

@Component
public class OpportunityModel  implements IModel{

	@Autowired
	private OpportunityDao dao;
	
	public Integer insert(Object entity){
		((OpportunityEntity)entity).setId(null);

		return dao.insert((OpportunityEntity)entity);
	}

	public Integer update(Object entity){
		return dao.update((OpportunityEntity)entity);
	}
	
	public Integer save(OpportunityEntity entity){
		if(entity.getId() == null || entity.getId().equals(0)) {
			((OpportunityEntity)entity).setId(null);

			return dao.insert((OpportunityEntity)entity);
		}
		return dao.update((OpportunityEntity)entity);
	}

	public Integer deleteByKey(Integer key, String corpid) {
		return dao.deleteByKey(key, corpid);
	}

	public Integer deleteByCustomerId(String corpid, Integer customerId) {
		return dao.deleteByCustomerId(corpid, customerId);
	}
	
	public OpportunityEntity getByKey( Integer key, String corpid){
		return dao.getByKey(key, corpid);
	}
	 
	public List<OpportunityEntity> findEntitys(Map<String ,Object>  param){
		return dao.findEntitys(param);
	}

//	筛选需要companyId
	public List<OpportunityEntity> getListByCutomer(Integer customerId, Integer companyId){
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("companyId", companyId);
		param.put("customerId", customerId);
		param.put("del", 0);
		List<OpportunityEntity> list = findEntitys(param);
		return list;
	}
	
	public Integer getEntitysCount(Map<String ,Object>  param){
		return dao.getEntitysCount(param);
	}

	public List<OpportunityEntity> getByStaffId(Map<String, Object> param) {
		return dao.getByStaffId(param);
	}
	 
	public List<OpportunityEntity> countReportByStage(Map<String ,Object>  param){
		return dao.countReportByStage(param);
	}
	public List<OpportunityEntity> amountReportByStage(Map<String ,Object>  param){
		return dao.amountReportByStage(param);
	}
	public void addRelateOpportunityUser(UserEntity cUser,
			CustomerEntity customer, Integer isMain, OpportunityUserModel opportunityUserModel) {
		// TODO Auto-generated method stub
		if(cUser == null || customer == null){
			return ;
		}
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("customerId", customer.getId());
		param.put("del", 0);
		List<OpportunityEntity> opportunityList = findEntitys(param);
		for(OpportunityEntity opportunityEntity : opportunityList){
			OpportunityUserEntity opportunityUser = null;
			param.clear();
			param.put("userId", cUser.getUserId());
			param.put("opportunityId", opportunityEntity.getId());
			List<OpportunityUserEntity> opportunityUserList = opportunityUserModel.findEntitys(param);
			if(opportunityUserList.size() > 0){
				opportunityUser = opportunityUserList.get(0);
			}
			if(opportunityUser == null){
				opportunityUser = new OpportunityUserEntity();
				opportunityUser.setAddTime(DateUtil.getInt());
				opportunityUser.setCorpid(cUser.getCorpid());
			}
			opportunityUser.setDel(0);
			opportunityUser.setIsMain(isMain);
			opportunityUser.setOpportunityId(opportunityEntity.getId());
			opportunityUser.setUserAvatar(cUser.getAvatar());
			opportunityUser.setUserId(cUser.getUserId());
			opportunityUser.setUserName(cUser.getName());
			opportunityUserModel.save(opportunityUser);
		}
	}
	
	public void deleteRelateOpportunityUser(UserEntity cUser,
			CustomerEntity customer, OpportunityUserModel opportunityUserModel) {
		// TODO Auto-generated method stub
		if(cUser == null || customer == null){
			return ;
		}


		Map<String,Object> param = new HashMap<String,Object>();
		param.put("customerId", customer.getId());
		param.put("del", 0);
		List<OpportunityEntity> opportunityList = findEntitys(param);
		for(OpportunityEntity opportunityEntity : opportunityList){
			param.clear();
			param.put("userId", cUser.getUserId());
			param.put("opportunityId", opportunityEntity.getId());
			param.put("del", 0);
			List<OpportunityUserEntity> opportunityUserList = opportunityUserModel.findEntitys(param);
			if(opportunityUserList.size()>0){
				opportunityUserModel.deleteByKey(opportunityUserList.get(0).getId(), cUser.getCorpid());
			}
		}
	}

	
	public List<OpportunityEntity> findEntitysJoinOpportunityUser(Map<String ,Object>  param){
		return dao.findEntitysJoinOpportunityUser(param);
	}
	public Integer getEntitysJoinOpportunityUserCount(Map<String ,Object>  param){
		return dao.getEntitysJoinOpportunityUserCount(param);
	}


	public List<Map<String,Object>> opportunityStatic(Map<String ,Object>  param){
		return dao.opportunityStatic(param);
	}
	
	public List<OpportunityEntity> opportunityStaticList(Map<String ,Object>  param){
		return dao.opportunityStaticList(param);
	}
	
	//tb_opportunity 和 tb_opportunity_user连表查询
	public PageHelper getPageHelper(Map<String, Object> param,Integer pageSize) {
		Integer rowCounts = dao.getEntitysJoinOpportunityUserCount(param);
		Integer page = (Integer) param.get("page");
		PageHelper pageHelper = new PageHelper(page, pageSize);
		pageHelper.setRowsCount(rowCounts);
		
		//将总数放入modelMap
		param.put("total", rowCounts);
		
		return pageHelper;
	}

	public List<OpportunityEntity> getEntityList(Map<String, Object> param,PageHelper pageHelper) {
		Integer pageNum = pageHelper.getPageSize();
		Integer start = (pageHelper.getCurrentPageNum() - 1) * pageNum;

		param.put("pageNum", pageNum);
		param.put("start", start);
		return dao.findEntitysJoinOpportunityUser(param);
	}
	
	public List<OpportunityEntity> getEntityListIsMain(Map<String, Object> param,PageHelper pageHelper) {
		Integer pageNum = pageHelper.getPageSize();
		Integer start = (pageHelper.getCurrentPageNum() - 1) * pageNum;
		
		param.put("pageNum", pageNum);
		param.put("start", start);
		return dao.findEntitysJoinOpportunityUserIsMain(param);
	}
	
	/**
	 * 获取某个客户的销售机会数
	 * @param customerId
	 * @return
	 */
	public Integer getOpportunityCount(Integer customerId){
		if(customerId == null){
			return 0;
		}
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("customerId", customerId);
		param.put("del", 0);
		return getEntitysCount(param);
	}
	
	/**
	 * 表链接获取销售机会（带负责人）
	 * @param customerId
	 * @return
	 */
	public List<OpportunityEntity> getOpportunityUser(Integer customerId, String corpid){
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("customerIdJoin", customerId);
		param.put("corpidJoin", corpid);
		param.put("delJoin", 0);
		return findEntitysJoinOpportunityUser(param);
	}
	
	public List<OpportunityEntity> getOpportunityList(UserEntity userEntity, UserModel userModel, Integer customerId, Integer page, Integer pageSize) {
		String corpid = userEntity.getCorpid();
		
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//条件
		if (customerId != null && customerId > 0) {
			modelMap.put("customerId", customerId);
		}
		modelMap.put("page", page);
		modelMap.put("delJoin", 0);
		modelMap.put("corpidJoin", corpid);
		modelMap.put("orderByStr", "add_time desc");
		//下属条件
		List<String> userIdIn = userModel.getSubIdList(userEntity);
		//自己
		userIdIn.add(""+userEntity.getUserId());
		modelMap.put("userIdInJoin", userIdIn);

		PageHelper pageHelper = getPageHelper(modelMap, pageSize);
		List<OpportunityEntity> opportunityList = getEntityList(modelMap, pageHelper);
		
		return opportunityList;
	}
	
	/**
	 * 批量更新销售机会列表中某公司某客户的客户名称
	 * @param corpid
	 * @param contractId
	 * @param contractName
	 * @return
	 */
	public Integer dataConsistencyUpdate(String corpid, Integer customerId, String customerName) {
		return dao.dataConsistencyUpdate(corpid, customerId, customerName);
	}
	
	/**
	 * 批量更新销售机会中某公司某用户的姓名和头像
	 * @param corpid
	 * @param userId
	 * @param userName
	 * @param userAvatar
	 * @return
	 */
	public Integer dataConsistencyUpdateUser(String corpid, String userId, String userName, String userAvatar) {
		return dao.dataConsistencyUpdateUser(corpid, userId, userName, userAvatar);
	}
}

 