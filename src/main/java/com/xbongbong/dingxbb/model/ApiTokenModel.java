 
package com.xbongbong.dingxbb.model;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xbongbong.dingxbb.dao.ApiTokenDao;
import com.xbongbong.dingxbb.entity.ApiTokenEntity;
import com.xbongbong.util.DateUtil;

 

@Component
public class ApiTokenModel  implements IModel{

	@Autowired
	private ApiTokenDao dao;
	
	public Integer insert(Object entity){
		Integer now =DateUtil.getInt();
    	((ApiTokenEntity)entity).setAddTime(now);
    	((ApiTokenEntity)entity).setUpdateTime(now);
		
		return dao.insert((ApiTokenEntity)entity);
	}

	public Integer update(Object entity){
		((ApiTokenEntity)entity).setUpdateTime(DateUtil.getInt());
		
		return dao.update((ApiTokenEntity)entity);
	}
	
	public Integer save(ApiTokenEntity entity){
		
		if(entity.getId() == null || entity.getId().equals(0)) {
			return dao.insert((ApiTokenEntity)entity);
		}
		return dao.update((ApiTokenEntity)entity);
	}

	 
	public Integer deleteByKey( Integer key){
		return dao.deleteByKey(key);
	}
	
	public ApiTokenEntity getByKey( Integer key){
		return dao.getByKey(key);
	}
	
	public ApiTokenEntity getByCorpId(String corpid){
		return dao.getByCorpId(corpid);
	}
	
	 
	public List<ApiTokenEntity> findEntitys(Map<String ,Object>  param){
		return dao.findEntitys(param);
	}
	public Integer getEntitysCount(Map<String ,Object>  param){
		return dao.getEntitysCount(param);
	}
	 

}

 