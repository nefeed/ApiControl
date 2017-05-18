 
package com.xbongbong.dingxbb.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xbongbong.dingxbb.entity.DepartmentEntity;

 
 
public interface DepartmentDao {

	 
	public Integer insert(DepartmentEntity department);
	public Integer update(DepartmentEntity department);
	
	public Integer deleteByKey(@Param("key")String key, @Param("corpid")String corpid);
	public DepartmentEntity getByKey (@Param("key") String key, @Param("corpid")String corpid);
	
	public Integer deleteByKeys(@Param("keys")List<Long> keys, @Param("corpid")String corpid);
	
	public List<DepartmentEntity>  findEntitys(@Param("param")Map<String ,Object>  param);
	public Integer getEntitysCount(@Param("param")Map<String ,Object>  param);
		
	/**
	 * 删除某公司的部门数据
	 * @param corpid
	 * @return
	 */
	public Integer deleteByCorpId(@Param("corpid")String corpid);
 
}