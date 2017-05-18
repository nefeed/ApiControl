 
package com.xbongbong.dingxbb.model;

import com.xbongbong.dingxbb.dao.SysModuleDao;
import com.xbongbong.dingxbb.entity.SysModuleEntity;
import com.xbongbong.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Component
public class SysModuleModel implements IModel{

	@Autowired
	private SysModuleDao dao;
	
	public Integer insert(Object entity){
		
		return dao.insert((SysModuleEntity)entity);
	}

	public Integer update(Object entity){
		
		return dao.update((SysModuleEntity)entity);
	}
	
	public Integer save(SysModuleEntity entity){
		
		if(entity.getId() == null || entity.getId().equals(0)) {
			return dao.insert(entity);
		}
		return dao.update(entity);
	}

	 
	public Integer deleteByKey( Integer key){
		return dao.deleteByKey(key);
	}
	
	public SysModuleEntity getByKey(Integer key){
		return dao.getByKey(key);
	}
	 
	public List<SysModuleEntity> findEntitys(Map<String ,Object>  param){
		return dao.findEntitys(param);
	}
	public Integer getEntitysCount(Map<String ,Object>  param){
		return dao.getEntitysCount(param);
	}
	 

}

 