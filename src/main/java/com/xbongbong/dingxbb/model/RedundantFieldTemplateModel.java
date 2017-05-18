 
package com.xbongbong.dingxbb.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xbongbong.dingxbb.dao.RedundantFieldTemplateDao;
import com.xbongbong.dingxbb.entity.RedundantFieldTemplateEntity;

 

@Component
public class RedundantFieldTemplateModel  implements IModel{

	@Autowired
	private RedundantFieldTemplateDao dao;
	
	public Integer insert(Object entity){
		return dao.insert((RedundantFieldTemplateEntity)entity);
	}

	public Integer update(Object entity){
		return dao.update((RedundantFieldTemplateEntity)entity);
	}
	
	public Integer save(RedundantFieldTemplateEntity entity){
		if(entity.getId() == null || entity.getId().equals(0)) {
			return dao.insert((RedundantFieldTemplateEntity)entity);
		}
		return dao.update((RedundantFieldTemplateEntity)entity);
	}
	 
	public List<RedundantFieldTemplateEntity> findEntitys(Map<String ,Object>  param){
		return dao.findEntitys(param);
	}
	public Integer getEntitysCount(Map<String ,Object>  param){
		return dao.getEntitysCount(param);
	}
	
	public RedundantFieldTemplateEntity getByKey(Integer key, String corpid) {
		return dao.getByKey(key, corpid, 0);
	}
	public RedundantFieldTemplateEntity getByKey(Integer key, String corpid, Integer del) {
		return dao.getByKey(key, corpid, del);
	}
	
	public RedundantFieldTemplateEntity getByCorpIdAndDDFormUuid(String corpid, String ddFormUuid, Integer type, Integer del) {
		return dao.getByCorpIdAndDDFormUuid(corpid, ddFormUuid, type, del);
	}

	public RedundantFieldTemplateEntity getByCorpIdAndDDFormUuid(String corpid, String ddFormUuid, Integer type) {
		return dao.getByCorpIdAndDDFormUuid(corpid, ddFormUuid, type, null);
	}
	
	public Integer deleteByKey(Integer key, String corpid) {
		return dao.deleteByKey(key, corpid);
	}
	
	/**
	 * 将制定模板enable设为0
	 * @param key
	 * @param corpid
	 * @return
	 */
	public Integer disableTemplateByKey(Integer key,String corpid) {
		return dao.disableTemplateByKey(key, corpid);
	}

	
	public RedundantFieldTemplateEntity getByKeyPublic(Integer key) {
		return dao.getByKeyPublic(key);
	}

	public RedundantFieldTemplateEntity getMyNoCopyTemplate(String corpid, Integer type) {
		return dao.getMyNoCopyTemplate(corpid, type);
	}
	
	public RedundantFieldTemplateEntity getByKeyPublicOrMine(Integer key, String corpid) {
		return dao.getByKeyPublicOrMine(key, corpid);
	}
	
	//获取当前使用的模板
	/**
	 * 获取当前使用的模板，默认模板返回null
	 * @param corpid
	 * @param type 1：客户，2：销售机会，3：合同 ，4：联系人
	 * @return
	 */
	public RedundantFieldTemplateEntity getDefaultTemplate(String corpid, Integer type){
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("corpid", corpid);
		param.put("type", type);
		param.put("del", 0);
		param.put("enable", 1);
		List<RedundantFieldTemplateEntity> templateList = findEntitys(param);
		return templateList.size() > 0 ? templateList.get(0) : null;
	}
}

 