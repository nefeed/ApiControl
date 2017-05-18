 
package com.xbongbong.dingxbb.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xbongbong.dingxbb.entity.ProductEntity;

 
 
public interface ProductDao {

	 
	public Integer insert(ProductEntity product);
	public Integer update(ProductEntity product);
	
	 
	
	public Integer deleteByKey(@Param("key")Integer key,@Param("corpid")String corpid);
	public ProductEntity getByKey (@Param("key") Integer key, @Param("corpid")String corpid);
	
	
	
	public List<ProductEntity>  findEntitys(@Param("param")Map<String ,Object>  param);
	public Integer getEntitysCount(@Param("param")Map<String ,Object>  param);
		
	public List<ProductEntity>  findEntitysByStaffId(@Param("param")Map<String ,Object>  param);
	public Integer getEntitysCountByStaffId(@Param("param")Map<String ,Object>  param);
 
}