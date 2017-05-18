 
package com.xbongbong.dingxbb.dao;

import com.xbongbong.dingxbb.entity.SysModuleEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


public interface SysModuleDao {

	 
	public Integer insert(SysModuleEntity sysModule);
	public Integer update(SysModuleEntity sysModule);
	
	 
	
	public Integer deleteByKey(@Param("key") Integer key);
	public SysModuleEntity getByKey(@Param("key") Integer key);


	public List<SysModuleEntity>  findEntitys(@Param("param") Map<String, Object> param);
	public Integer getEntitysCount(@Param("param") Map<String, Object> param);
		
		
 
}