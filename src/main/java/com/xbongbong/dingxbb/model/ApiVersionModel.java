 
package com.xbongbong.dingxbb.model;

import com.xbongbong.dingxbb.dao.ApiVersionDao;
import com.xbongbong.dingxbb.entity.ApiVersionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Component
public class ApiVersionModel implements IModel{

	@Autowired
	private ApiVersionDao dao;
	
	public Integer insert(Object entity){
		return dao.insert((ApiVersionEntity)entity);
	}

	public Integer update(Object entity){
		return dao.update((ApiVersionEntity)entity);
	}
	
	public Integer save(ApiVersionEntity entity){
		
		if(entity.getId() == null || entity.getId().equals(0)) {
			return insert(entity);
		}
		return update(entity);
	}

	 
	public Integer deleteByKey( Integer key){
		return dao.deleteByKey(key);
	}
	
	public ApiVersionEntity getByKey(Integer key){
		return dao.getByKey(key);
	}
	 
	public List<ApiVersionEntity> findEntitys(Map<String ,Object>  param){
		return dao.findEntitys(param);
	}
	public Integer getEntitysCount(Map<String ,Object>  param){
		return dao.getEntitysCount(param);
	}
	 

}

 