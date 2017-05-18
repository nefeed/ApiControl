 
package com.xbongbong.dingxbb.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xbongbong.dingxbb.dao.ProductCategoryDao;
import com.xbongbong.dingxbb.entity.ProductCategoryEntity;
import com.xbongbong.util.CommentUtil;
import com.xbongbong.util.DateUtil;
import com.xbongbong.util.StringUtil;
 

@Component
public class ProductCategoryModel  implements IModel{

	@Autowired
	private ProductCategoryDao dao;
	
	public Integer insert(Object entity){
		((ProductCategoryEntity)entity).setId(null);

		return dao.insert((ProductCategoryEntity)entity);
	}

	public Integer update(Object entity){
		return dao.update((ProductCategoryEntity)entity);
	}
	
	public Integer save(ProductCategoryEntity entity){
		if(entity.getId() == null || entity.getId().equals(0)) {
			((ProductCategoryEntity)entity).setId(null);

			return dao.insert((ProductCategoryEntity)entity);
		}
		return dao.update((ProductCategoryEntity)entity);
	}

	public Integer deleteByKey(Integer key, String corpid) {
		return dao.deleteByKey(key, corpid);
	}
	
	public ProductCategoryEntity getByKey( Integer key, String corpid){
		return dao.getByKey(key, corpid);
	}
	 
	public List<ProductCategoryEntity> findEntitys(Map<String ,Object>  param){
		return dao.findEntitys(param);
	}
	public Integer getEntitysCount(Map<String ,Object>  param){
		return dao.getEntitysCount(param);
	}
	public Integer getCategoryIdByNames(String names, String corpid){
		if(StringUtil.isEmpty(names)){
			return 0;
		}
		String[] nameArray = names.split("-");
		ProductCategoryEntity retEntity = null;//返回的entity
		Map<String,Object> param = new HashMap<String,Object>();
		ProductCategoryEntity supCategoryEntity = null;//当前处理的父entity
		ProductCategoryEntity entity = null;//当前处理的entity
		for(int i = 0 ; i<nameArray.length ; i++){ //遍历分类数组
			
			entity = new ProductCategoryEntity();
			entity.setCorpid(corpid);
			entity.setName(nameArray[i]);
			entity.setAddTime(DateUtil.getInt());
			entity.setUpdateTime(DateUtil.getInt());
			entity.setDel(0);
			
			param.put("name", nameArray[i]);
			param.put("corpid", corpid);
			param.put("del", 0);
			List<ProductCategoryEntity> categoryList = this.findEntitys(param);
			Integer num = categoryList.size();
			
			if(i == 0){//父分类
				if(num == 0){	//分类不存在，创建
					entity.setParentId(0);
					insert(entity);
				}else{
					entity = categoryList.get(0);
				}
				supCategoryEntity = entity;
			}
			if(i == 1){//子分类
				if(num == 0){	//分类不存在，创建
					entity.setParentId(supCategoryEntity.getId());
					insert(entity);
				}else{
					entity = categoryList.get(0);
				}
				supCategoryEntity = entity;
			}
			
			if(i == nameArray.length-1){//最后一个
				retEntity = entity;
			}
			
		}
		return retEntity == null ? 0 : retEntity.getId();
	}

	/**
	 * 所有父类，子类在ChildrenList
	 * @param companyId
	 * @return
	 */
	public List<ProductCategoryEntity> getProductCategoryList(String corpid){
		Map<String,Object> param = new HashMap<String,Object>();
		CommentUtil.addToMap(param,"corpid", corpid);
		CommentUtil.addToMap(param,"parentId",0);
		CommentUtil.addToMap(param,"del",0);
		
		List<ProductCategoryEntity> productCategoryList = findEntitys(param);
		List<ProductCategoryEntity> childList = null;
		for(ProductCategoryEntity productCategory : productCategoryList){
			Integer categoryId = productCategory.getId();
			CommentUtil.addToMap(param, "parentId", categoryId);
			CommentUtil.addToMap(param, "del", 0);
			childList = findEntitys(param);
			productCategory.setChildrenList(childList);
		}
		return productCategoryList;
	}
}

 