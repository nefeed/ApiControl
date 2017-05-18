 
package com.xbongbong.dingxbb.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xbongbong.dingxbb.entity.ProductCategoryEntity;

 
 
public interface ProductCategoryDao {

	 
	public Integer insert(ProductCategoryEntity productCategory);
	public Integer update(ProductCategoryEntity productCategory);
	
	 
	
	public Integer deleteByKey(@Param("key")Integer key,@Param("corpid")String corpid);
	public ProductCategoryEntity getByKey (@Param("key") Integer key, @Param("corpid")String corpid);
	
	
	public List<ProductCategoryEntity>  findEntitys(@Param("param")Map<String ,Object>  param);
	public Integer getEntitysCount(@Param("param")Map<String ,Object>  param);
		
		
 
}