 
package com.xbongbong.dingxbb.model;

import com.xbongbong.dingxbb.dao.SysModuleDao;
import com.xbongbong.dingxbb.entity.SysModuleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Component
public class SysModuleModel implements IModel{

	@Autowired
	private SysModuleDao sysModuleDao;
	
	public Integer insert(Object entity){
		return sysModuleDao.insert((SysModuleEntity)entity);
	}

	public Integer update(Object entity){
		return sysModuleDao.update((SysModuleEntity)entity);
	}
	
	public Integer save(SysModuleEntity entity){
		if(entity.getId() == null || entity.getId().equals(0)) {
			return insert(entity);
		}
		return update(entity);
	}

	 
	public Integer deleteByKey( Integer key){
		return sysModuleDao.deleteByKey(key);
	}
	
	public SysModuleEntity getByKey(Integer key){
		return sysModuleDao.getByKey(key);
	}
	 
	public List<SysModuleEntity> findEntitys(Map<String ,Object>  param){
		return sysModuleDao.findEntitys(param);
	}
	public Integer getEntitysCount(Map<String ,Object>  param){
		return sysModuleDao.getEntitysCount(param);
	}
	 

}

 