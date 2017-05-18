 
package com.xbongbong.dingxbb.model;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xbongbong.dingxbb.dao.WeakManageDao;
import com.xbongbong.dingxbb.entity.WeakManageEntity;

 

@Component
public class WeakManageModel  implements IModel{

	@Autowired
	private WeakManageDao dao;
	
	public Integer insert(Object entity){
		((WeakManageEntity)entity).setId(null);

		return dao.insert((WeakManageEntity)entity);
	}

	public Integer update(Object entity){
		return dao.update((WeakManageEntity)entity);
	}
	
	public Integer save(WeakManageEntity entity){
		if(entity.getId() == null || entity.getId().equals(0)) {
			entity.setId(null);

			return dao.insert(entity);
		}
		return dao.update(entity);
	}

	public Integer deleteByKey(Integer key, String corpid) {
		return dao.deleteByKey(key, corpid);
	}
	
	public WeakManageEntity getByKey( Integer key, String corpid){
		return dao.getByKey(key, corpid);
	}
	 
	public List<WeakManageEntity> findEntitys(Map<String ,Object>  param){
		return dao.findEntitys(param);
	}
	public Integer getEntitysCount(Map<String ,Object>  param){
		return dao.getEntitysCount(param);
	}
	 
	
	public List<Integer> findOnlyIds(Map<String ,Object>  param) {
		return dao.findOnlyIds(param);
	}

}

 