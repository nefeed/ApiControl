 
package com.xbongbong.dingxbb.model;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xbongbong.dingxbb.dao.RedundantFieldAppendDao;
import com.xbongbong.dingxbb.entity.RedundantFieldAppendEntity;

 

@Component
public class RedundantFieldAppendModel  implements IModel{

	@Autowired
	private RedundantFieldAppendDao dao;
	
	public Integer insert(Object entity){
		
		return dao.insert((RedundantFieldAppendEntity)entity);
	}

	public Integer update(Object entity){
		return dao.update((RedundantFieldAppendEntity)entity);
	}
	
	public Integer save(RedundantFieldAppendEntity entity){
		if(entity == null){
			return 1;
		}
		if(entity.getId() == null || entity.getId().equals(0)) {
			return dao.insert((RedundantFieldAppendEntity)entity);
		}
		return dao.update((RedundantFieldAppendEntity)entity);
	}

	 
	public Integer deleteByKey( Integer key){
		return dao.deleteByKey(key);
	}
	
	public RedundantFieldAppendEntity getByKey( Integer redundantFieldId, String corpid){
		return dao.getByKey(redundantFieldId, corpid);
	}
	 
	public List<RedundantFieldAppendEntity> findEntitys(Map<String ,Object>  param){
		return dao.findEntitys(param);
	}
	
	public List<Integer> findRefIds(Map<String ,Object>  param){
		return dao.findRefIds(param);
	}
	
	public Integer getEntitysCount(Map<String ,Object>  param){
		return dao.getEntitysCount(param);
	}
	 
	/**
	 * 根据corpid+refType+refId获取RedundantFieldEntity实体
	 * @param corpid
	 * @param refType
	 * @param refId
	 * @return
	 */
	public RedundantFieldAppendEntity getByRefTypeAndRefId(String corpid, Integer refType, Integer refId) {
		return dao.getByRefTypeAndRefId(corpid, refType, refId);
	}
}

 