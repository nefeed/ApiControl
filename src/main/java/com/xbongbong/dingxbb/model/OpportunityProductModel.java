 
package com.xbongbong.dingxbb.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xbongbong.dingxbb.dao.OpportunityProductDao;
import com.xbongbong.dingxbb.entity.OpportunityProductEntity;
import com.xbongbong.util.CommentUtil;
 

@Component
public class OpportunityProductModel  implements IModel{

	@Autowired
	private OpportunityProductDao dao;
	
	public Integer insert(Object entity){
		return dao.insert((OpportunityProductEntity)entity);
	}

	public Integer update(Object entity){
		return dao.update((OpportunityProductEntity)entity);
	}
	
//	public Integer save(OpportunityProductEntity entity){
//		if(entity.getId() == null || entity.getId().equals(0)) {
//			return dao.insert((OpportunityProductEntity)entity);
//		}
//		return dao.update((OpportunityProductEntity)entity);
//	}
	
	public OpportunityProductEntity getByKey(String corpid, Integer opportunityId, Integer productId) {
		return dao.getByKey(corpid, opportunityId, productId);
	}

	public Integer deleteByKey(String corpid, Integer opportunityId, Integer productId) {
		return dao.deleteByKey(corpid, opportunityId, productId);
	}
	 
	public Integer deleteByOpportunityId(String corpid, Integer opportunityId) {
		return dao.deleteByOpportunityId(corpid, opportunityId);
	}
	
	public List<OpportunityProductEntity> findEntitys(Map<String ,Object>  param){
		return dao.findEntitys(param);
	}
	public Integer getEntitysCount(Map<String ,Object>  param){
		return dao.getEntitysCount(param);
	}

	public List<OpportunityProductEntity> findEntitysJoinProduct(Map<String ,Object>  param){
		return dao.findEntitysJoinProduct(param);
	}

	/**
	 * 获取产品选择的产品列表
	 * @param corpid
	 * @return
	 */
	public List<OpportunityProductEntity> getProdutChooseList(String corpid,Integer opportunityId){
		Map<String,Object> param = new HashMap<String,Object>();
		CommentUtil.addToMap(param, "opportunityId", opportunityId);
		CommentUtil.addToMap(param, "corpid", corpid);
		return dao.findEntitysJoinProduct(param);
	}
	
	/**
	 * 批量销售机会中更新某公司某产品的产品名称&产品规格
	 * *用于数据一致性同步 @see {@DataConsistencyKafkaCallbackController}
	 * @param key
	 * @param corpid
	 * @return
	 */
	public Integer dataConsistencyUpdate(String corpid, Integer productId, String productName, String specification) {
		return dao.dataConsistencyUpdate(corpid, productId, productName, specification);
	}
}

 