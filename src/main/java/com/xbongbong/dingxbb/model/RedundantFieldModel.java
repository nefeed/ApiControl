 
package com.xbongbong.dingxbb.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xbongbong.dingxbb.dao.RedundantFieldDao;
import com.xbongbong.dingxbb.dao.RedundantFieldExplainDao;
import com.xbongbong.dingxbb.dao.RedundantFieldTemplateDao;
import com.xbongbong.dingxbb.entity.ContractEntity;
import com.xbongbong.dingxbb.entity.CustomerEntity;
import com.xbongbong.dingxbb.entity.RedundantFieldAppendEntity;
import com.xbongbong.dingxbb.entity.RedundantFieldEntity;
import com.xbongbong.dingxbb.entity.RedundantFieldExplainEntity;
import com.xbongbong.dingxbb.entity.RedundantFieldTemplateEntity;
import com.xbongbong.dingxbb.enums.ContactEnum;
import com.xbongbong.dingxbb.enums.ContractEnum;
import com.xbongbong.dingxbb.enums.CustomerEnum;
import com.xbongbong.dingxbb.enums.ProductEnum;
import com.xbongbong.dingxbb.enums.SalesOpportunityEnum;
import com.xbongbong.dingxbb.helper.FormateRedundantValueHelper;
import com.xbongbong.util.ReflectHelper;
import com.xbongbong.util.StringUtil;

 

@Component
public class RedundantFieldModel  implements IModel{

	@Autowired
	private RedundantFieldDao dao;
	@Autowired
	private RedundantFieldAppendModel redundantFieldAppendModel;
	@Autowired
	private RedundantFieldTemplateDao redundantFieldTemplateDao;
	@Autowired
	private RedundantFieldExplainDao redundantFieldExplainDao;
	@Autowired
	private RedundantFieldDao redundantFieldDao;
	
	public Integer insert(Object entity){
		Integer id = dao.insert((RedundantFieldEntity)entity);
		RedundantFieldAppendEntity redundantFieldAppendEntity = ((RedundantFieldEntity)entity).getRedundantFieldAppendEntity();
		if(redundantFieldAppendEntity != null){
			redundantFieldAppendModel.insert(redundantFieldAppendEntity);
		}
		return id;
	}

	public Integer update(Object entity){
		Integer flag1 = dao.update((RedundantFieldEntity)entity);
		
		RedundantFieldAppendEntity redundantFieldAppend = redundantFieldAppendModel.getByKey(((RedundantFieldEntity)entity).getId(), ((RedundantFieldEntity)entity).getCorpid());
		redundantFieldAppend = ((RedundantFieldEntity)entity).getRedundantFieldAppendEntity(redundantFieldAppend);
		
		Integer flag2 = redundantFieldAppendModel.save(redundantFieldAppend);
		
		return flag1*flag2;
	}
	
	public Integer save(RedundantFieldEntity entity){
		if(entity.getId() == null || entity.getId().equals(0)) {
			((RedundantFieldEntity)entity).setId(null);

			return insert((RedundantFieldEntity)entity);
		}
		return update((RedundantFieldEntity)entity);
	}
	 
	public List<RedundantFieldEntity> findEntitys(Map<String ,Object>  param){
		List<RedundantFieldEntity> retList = new ArrayList<RedundantFieldEntity>();
		List<RedundantFieldEntity> list = dao.findEntitys(param);
		
		List<RedundantFieldAppendEntity> appendList = redundantFieldAppendModel.findEntitys(param);
		//List to map appendMap:redundantFieldId : RedundantFieldAppendEntity
		Map<Integer,RedundantFieldAppendEntity> appendMap = new HashMap<Integer,RedundantFieldAppendEntity>();
		for(RedundantFieldAppendEntity appendEntity : appendList){
			appendMap.put(appendEntity.getRedundantFieldId(), appendEntity);
		}
		//取交集，设置attr20-attr40的字段
		for(RedundantFieldEntity redundantFieldEntity : list){
			if(appendMap.containsKey(redundantFieldEntity.getId())){
				redundantFieldEntity.setRedundantFieldAppendEntity(appendMap.get(redundantFieldEntity.getId()));
			}
			retList.add(redundantFieldEntity);
		}
		return retList;
	}
	public Integer getEntitysCount(Map<String ,Object>  param){
		return dao.getEntitysCount(param);
	}

	public RedundantFieldEntity getByKey(Integer key, String corpid) {
		RedundantFieldEntity redundantFieldEntity = dao.getByKey(key, corpid);
		if(redundantFieldEntity != null) {
			RedundantFieldAppendEntity redundantFieldAppendEntity = redundantFieldAppendModel.getByKey(key, corpid);
			redundantFieldEntity.setRedundantFieldAppendEntity(redundantFieldAppendEntity);
		}
		return redundantFieldEntity;
	}

	//没有使用过，不改造
	public Integer deleteByKey(String key, String corpid) {
		return dao.deleteByKey(key, corpid);
	}
	 
	/**
	 * 保存自定义字段
	 * @param corpid 	公司ID
	 * @param refType	关联类型，1；客户，2：销售机会，3：合同, 4：联系人, 5:产品
	 * @param refId		关联对象id
	 * @param templateId	模板ID
	 * @param redundantValues 自定义字段的object String 形式
	 */
	public void saveRedundantField(String corpid, Integer refType, Integer refId, Integer templateId, String redundantValues){
		//判断redundantValues是否可用
		JSONObject redundantValueObject = null;
		try {
			redundantValueObject = (JSONObject) JSONObject.parse(redundantValues);
		} catch (Exception e) {
			// TODO: handle exception
		}
		if(redundantValueObject == null || redundantValueObject.isEmpty()){
			return ;
		}
		RedundantFieldEntity redundantField = null;
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("corpid", corpid);
		param.put("refType", refType);
		param.put("refId", refId);
		param.put("templateId", templateId);
		List<RedundantFieldEntity> redundantFieldList = findEntitys(param);
		if(redundantFieldList.size() > 0){
			redundantField = redundantFieldList.get(0);
		}else{
			redundantField = new RedundantFieldEntity();
			redundantField.setCorpid(corpid);
			redundantField.setRefId(refId);
			redundantField.setRefType(refType);
			redundantField.setTemplateId(templateId);
		}
		
		Set<String> keySet = redundantValueObject.keySet();
		for(String key : keySet){
//			String value = (String) redundantValueObject.get(key);
			String value = redundantValueObject.get(key).toString();
			ReflectHelper.valueSet(redundantField, key, value);
		}
		save(redundantField);
	}
	
	/**
	 * 获取某个模板对应的解释列表，包括默认模板
	 * @param refType
	 * @param corpid
	 * @param templateId
	 * @return
	 * @author kaka
	 * @time 2016年9月25日 下午2:00:46
	 */
	public List<RedundantFieldExplainEntity> getRedundantFieldExplains(Integer refType, String corpid, Integer templateId) {
		List<RedundantFieldExplainEntity> explains = new ArrayList<RedundantFieldExplainEntity>();
		
		if(templateId == null || templateId == 0){ //默认模板
			List<JSONObject> basicExplains = new ArrayList<JSONObject>();
			if(refType == 1) {
				basicExplains = CustomerEnum.getAllEnumJsonSorted();
			} else if(refType == 2) {
				basicExplains = SalesOpportunityEnum.getAllEnumJsonSorted();
			} else if(refType == 3) {
				basicExplains = ContractEnum.getAllEnumJsonSorted();
			} else if(refType == 4) {
				basicExplains = ContactEnum.getAllEnumJsonSorted();
			} else if(refType == 5) {
				basicExplains = ProductEnum.getAllEnumJsonSorted();
			}
			
			for(JSONObject basicExplain : basicExplains) {
				int enable = basicExplain.getIntValue("enable");
				
				if(enable == 1) {
					String attr = basicExplain.getString("alias");
					String attrName = basicExplain.getString("name");
					int required = basicExplain.getIntValue("required");
					
					RedundantFieldExplainEntity explain = new RedundantFieldExplainEntity();
					//目前只构造用得到的字段
					explain.setAttr(attr); 
					explain.setAttrName(attrName);
					explain.setFieldType(0);
					explain.setIsRedundant(0);
					explain.setEnable(1);
					explain.setRequired(required);
					
					explains.add(explain);
				}
			}
		} else { //自定义模板
			
			Map<String, Object> param = new HashMap<String, Object>();
			
			param.put("corpid", corpid);
			param.put("templateId", templateId);
			param.put("del", 0);
			param.put("orderByStr", "sort desc");
			
			//拿出的是启用的字段
			param.put("enable", 1);
			
			//模板字段解释实体
			explains = redundantFieldExplainDao.findEntitys(param);
		}
		
		return explains;
	}
	
	/**
	 * 返回排序好的fieldExplains用于前端显示
	 * @param refType
	 * @param refId
	 * @param corpid
	 * @param tempalteId
	 * @param entity
	 * @return
	 * @author kaka
	 * @time 2016年7月22日 下午4:16:35
	 */
	public List<RedundantFieldExplainEntity> getExplainInfo4Show(Integer refType, Integer refId, String corpid, Integer templateId,
			Object entity) {
		List<RedundantFieldExplainEntity> explains = new ArrayList<RedundantFieldExplainEntity>();
		RedundantFieldEntity fieldEntity = new RedundantFieldEntity();
		
		if(templateId == null || templateId == 0){ //默认模板
			List<JSONObject> basicExplains = new ArrayList<JSONObject>();
			if(refType == 1) {
				basicExplains = CustomerEnum.getAllEnumJsonSorted();
			} else if(refType == 2) {
				basicExplains = SalesOpportunityEnum.getAllEnumJsonSorted();
			} else if(refType == 3) {
				basicExplains = ContractEnum.getAllEnumJsonSorted();
			} else if(refType == 4) {
				basicExplains = ContactEnum.getAllEnumJsonSorted();
			} else if(refType == 5) {
				basicExplains = ProductEnum.getAllEnumJsonSorted();
			}
			
			for(JSONObject basicExplain : basicExplains) {
				String attr = basicExplain.getString("alias");
				String attrName = basicExplain.getString("name");
				int required = basicExplain.getIntValue("required");
				
//				String vaule = 
				
				RedundantFieldExplainEntity explain = new RedundantFieldExplainEntity();
				//目前只构造用得到的字段
				explain.setAttr(attr); 
				explain.setAttrName(attrName);
				explain.setFieldType(0);
				explain.setIsRedundant(0);
				explain.setEnable(1);
				explain.setRequired(required);
				
				explains.add(explain);
			}
		} else { //自定义模板
			
			Map<String, Object> param = new HashMap<String, Object>();
			
			param.put("corpid", corpid);
			param.put("templateId", templateId);
			param.put("refType", refType);
			param.put("refId", refId);
			
			//获取模板对应的值数据
			List<RedundantFieldEntity> rfeList = redundantFieldDao.findEntitys(param);
			fieldEntity = rfeList.size() > 0 ? rfeList.get(0) : new RedundantFieldEntity();
			
			param.clear();
			param.put("corpid", corpid);
			param.put("templateId", templateId);
			param.put("del", 0);
			param.put("orderByStr", "sort desc");
			
			//模板字段解释实体
			explains = redundantFieldExplainDao.findEntitys(param);
		}
		
		//设置对应的值
		if(refType == 1) {
			explains = FormateRedundantValueHelper.formateCustomerValue(explains, (CustomerEntity) entity, fieldEntity);
		} else if(refType == 2) {
			//销售机会暂时不处理 TODO
		} else if(refType == 3) {
			explains = FormateRedundantValueHelper.formateContractValue(explains, (ContractEntity) entity, fieldEntity);
		}  else if(refType == 4) {
//			explains = FormateRedundantValueHelper.formateContactValue(explains, (ContactEntity) entity, fieldEntity);
		}  else if(refType == 5) {
//			explains = FormateRedundantValueHelper.formateProductValue(explains, (ProductEntity) entity, fieldEntity);
		}
		
		return explains;
	}
	
	/**
	 * 设置自定义信息(用于vm解释自定义字段)
	 * @param refType 1：客户冗余字段，2：销售机会冗余字段，3：合同冗余字段，4：联系人冗余字段
	 * @param refId 关联对象id
	 * @param corpid 公司ID
	 * @param modelMap
	 * 模版存在则会返回模板及其模板ID  不存在返回null和0
	 */
	public void setRedundantFieldInfo(Integer refType, Integer refId, String corpid, Map<String, Object> modelMap,Integer templateId){
		/*
		 * 获取自定义模板
		 * 编辑时采用最新模板，查看时根据相关实体对应模板进行渲染
		 */
		List<RedundantFieldTemplateEntity> templates = null;
		RedundantFieldTemplateEntity template = null;
		Map<String, Object> param = new HashMap<String, Object>();
		if(templateId != null){
			template = redundantFieldTemplateDao.getByKey(templateId, corpid,null);
		}else{
			param.put("del", 0);
			param.put("corpid", corpid);
			param.put("type", refType);
			param.put("enable", 1);//添加enable
			templates = redundantFieldTemplateDao.findEntitys(param);
			if(templates.size()>0){
				template = templates.get(0);
			}
		}
		
		//该公司选择的模板，默认为空；
		//选择的模板ID，默认为0
		if(template != null) {	//有模板
			templateId = template.getId();
			
			param.clear();
			param.put("corpid", corpid);
			param.put("templateId", templateId);
			param.put("del", 0);
			param.put("orderByStr", "sort desc");
			
			/*
			 * 获取模板相关数据
			 */
			//模板字段解释实体
			List<RedundantFieldExplainEntity> explains = redundantFieldExplainDao.findEntitys(param);
			List<RedundantFieldExplainEntity> redundantExplains = new ArrayList<RedundantFieldExplainEntity>();
			List<RedundantFieldExplainEntity> defaultExplains = new ArrayList<RedundantFieldExplainEntity>();
			for(RedundantFieldExplainEntity explainEntity : explains){
				Integer enable = explainEntity.getEnable();
				Integer isRedundant = explainEntity.getIsRedundant();
				if(enable.equals(1)){
					if(isRedundant.equals(1)){

						if (explainEntity.getFieldType() == 9) {//多选项
							String initValue = explainEntity.getInitValue();
							if (!StringUtil.isEmpty(initValue)) {
								if (initValue.startsWith("|")) {
									initValue = initValue.substring(1);
								}
								if (initValue.endsWith("|")) {
									initValue = initValue.substring(0, initValue.length()-1);
								}
								
								String valueArr[] = initValue.split("\\|");
								explainEntity.setValueArr(valueArr);
							}
						}
						redundantExplains.add(explainEntity);
					}else{
						defaultExplains.add(explainEntity);
					}
				}
			}
			//置入字段解释
			//将defaultExplains 做成map 方便前端使用
			Map<String,RedundantFieldExplainEntity> defaultExplainsMap = new HashMap<String,RedundantFieldExplainEntity>();
			for(RedundantFieldExplainEntity defaultExplain : defaultExplains){
				defaultExplainsMap.put(defaultExplain.getAttr(), defaultExplain);
			}
			
			
			modelMap.put("redundantExplains", JSON.toJSONString(redundantExplains));
			modelMap.put("defaultExplains", JSON.toJSONString(defaultExplains));
			modelMap.put("defaultExplainsMap", defaultExplainsMap);
			
			//模板扩展字段值数据
			if(refId != null) {
				
				param.clear();
				param.put("corpid", corpid);
				param.put("templateId", templateId);
				param.put("refType", refType);
				param.put("refId", refId);
				
				List<RedundantFieldEntity> rfeList = redundantFieldDao.findEntitys(param);
				RedundantFieldEntity fieldValues = rfeList.size() > 0 ? rfeList.get(0) : null;
				
				modelMap.put("fieldValues", JSON.toJSONString(fieldValues));
			}
		}else{
			templateId = 0;
		}
		//置入模版
		modelMap.put("template", template);
		modelMap.put("templateId", templateId);
	}
	
	/**
	  *  用于格式化字段信息，用于后面的调用钉钉接口
	 * @param refType 1：客户冗余字段，2：销售机会冗余字段，3：合同冗余字段，4：联系人冗余字段
	 * @param corpid 公司ID
	 * @return 如果没有模板返回null 有模板则返回对应模板 List<RedundantFieldExplainEntity>
	 */
	public List<RedundantFieldExplainEntity> getRedundantFieldExplains(Integer refType, String corpid){
		
		RedundantFieldTemplateEntity template = null;
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("del", 0);
		param.put("corpid", corpid);
		param.put("type", refType);
		param.put("enable", 1);//添加enable
		List<RedundantFieldTemplateEntity> templates = redundantFieldTemplateDao.findEntitys(param);
		if(templates.size()>0){
			template = templates.get(0);
			
			Integer templateId = template.getId();
			
			param.clear();
			param.put("corpid", corpid);
			param.put("templateId", templateId);
			param.put("del", 0);
//			param.put("orderByStr", "sort desc");
			
			List<RedundantFieldExplainEntity> explains = redundantFieldExplainDao.findEntitys(param);
			return explains;
		} else {
			return null;
		}
	}
	
	/**
	 * 根据corpid+refType+refId获取RedundantFieldEntity实体
	 * @param corpid
	 * @param refType
	 * @param refId
	 * @return
	 */
	public RedundantFieldEntity getByRefTypeAndRefId(String corpid, Integer refType, Integer refId) {
		RedundantFieldEntity redundantFieldEntity = dao.getByRefTypeAndRefId(corpid, refType, refId);
		if(redundantFieldEntity != null) {
			RedundantFieldAppendEntity redundantFieldAppendEntity = redundantFieldAppendModel.getByRefTypeAndRefId(corpid, refType, refId);
			redundantFieldEntity.setRedundantFieldAppendEntity(redundantFieldAppendEntity);
		}
		return redundantFieldEntity;
	}

}

 