 
package com.xbongbong.dingxbb.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xbongbong.dingxbb.dao.ProductDao;
import com.xbongbong.dingxbb.entity.ProductEntity;
import com.xbongbong.util.StringUtil;
 

@Component
public class ProductModel  implements IModel{

	@Autowired
	private ProductDao dao;
	
	public Integer insert(Object entity){
		((ProductEntity)entity).setId(null);

		return dao.insert((ProductEntity)entity);
	}

	public Integer update(Object entity){
		return dao.update((ProductEntity)entity);
	}
	
	public Integer save(ProductEntity entity){
		if(entity.getId() == null || entity.getId().equals(0)) {
			((ProductEntity)entity).setId(null);

			return dao.insert((ProductEntity)entity);
		}
		return dao.update((ProductEntity)entity);
	}

	public Integer deleteByKey(Integer key, String corpid) {
		return dao.deleteByKey(key, corpid);
	}
	
	public ProductEntity getByKey( Integer key, String corpid){
		return dao.getByKey(key, corpid);
	}
	
	public List<ProductEntity> findEntitys(Map<String ,Object>  param){
		return dao.findEntitys(param);
	}
	public List<ProductEntity> getByName(String name, String corpid){
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("name", name);
		param.put("corpid", corpid);
		return dao.findEntitys(param);
	}
	
	public List<ProductEntity> getByProductNo(String productNo, String corpid){
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("productNo", productNo);
		param.put("corpid", corpid);
		return dao.findEntitys(param);
	}
	
	public Integer getEntitysCount(Map<String ,Object>  param){
		return dao.getEntitysCount(param);
	}
	 
	public List<ProductEntity> findEntitysByStaffId(Map<String ,Object>  param){
		return dao.findEntitys(param);
	}
	public Integer getEntitysCountByStaffId(Map<String ,Object>  param){
		return dao.getEntitysCount(param);
	}
	
	/**
	 * 判断是否有产品
	 * @param request
	 * @param param
	 * @param corpid
	 * @return
	 */
	public boolean hasProduct(HttpServletRequest request, Map<String, Object> param, String corpid) {
		
		param.put("corpid", corpid);
		param.put("del", 0);
		param.put("orderByStr", "add_time desc");
		
		//外部传入
//		if (!isSummit(userEntity)) {//老板查看到所有的，其他人只能看分配给自己的
//			CommentUtil.addToMap(param, "userId", userId);
//			setDepIdIn(userEntity, param);
//		}
		
		Integer count = getEntitysCount(param);
		
		boolean hasProduct = true;
		if (count <= 0) {
			hasProduct = false;
		}
		
		return hasProduct;
	}
	
	public boolean isUniqueByProductNo(Integer id, String productNo, String corpid){
		
		//现在产品编号必填，若空则返回false
		if(StringUtil.isEmpty(productNo)){
			return false;
		}
		
		boolean isUnque = true;
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("productNo", productNo);
		param.put("corpid", corpid);
		param.put("del", 0);
		List<ProductEntity> productList = findEntitys(param);
		for(ProductEntity productEntity : productList){
			if(!productEntity.getId().equals(id)){
				isUnque = false;
				break;
			}
		}
		return isUnque;
	}
}

 