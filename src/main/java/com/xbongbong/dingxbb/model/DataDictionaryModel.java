 
package com.xbongbong.dingxbb.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xbongbong.dingxbb.dao.DataDictionaryDao;
import com.xbongbong.dingxbb.entity.DataDictionaryEntity;
import com.xbongbong.dingxbb.entity.SalesStageEntity;
import com.xbongbong.dingxbb.enums.DictionaryTypeEnum;
import com.xbongbong.util.CommentUtil;
import com.xbongbong.util.DateUtil;

 

@Component
public class DataDictionaryModel  implements IModel{

	@Autowired
	private DataDictionaryDao dao;
	
	@Autowired
	private SalesStageModel salesStageModel;
	
	
	public Integer insert(Object entity){
		((DataDictionaryEntity)entity).setId(null);
		return dao.insert((DataDictionaryEntity)entity);
	}

	public Integer update(Object entity){
		return dao.update((DataDictionaryEntity)entity);
	}
	
	public Integer save(DataDictionaryEntity entity){
		if(entity.getId() == null || entity.getId().equals(0)) {
			((DataDictionaryEntity)entity).setId(null);

			return dao.insert((DataDictionaryEntity)entity);
		}
		return dao.update((DataDictionaryEntity)entity);
	}

	public Integer deleteByKey(String key, String corpid) {
		return dao.deleteByKey(key, corpid);
	}
	
	public DataDictionaryEntity getByKey( Integer key, String corpid){
		return dao.getByKey(key, corpid);
	}
	 
	public List<DataDictionaryEntity> findEntitys(Map<String ,Object>  param){
		return dao.findEntitys(param);
	}
	public Integer getEntitysCount(Map<String ,Object>  param){
		return dao.getEntitysCount(param);
	}
	
	public Integer sort(Map<String ,Object>  param, Map<String ,Integer>  sortMap) {
		return dao.sort(param, sortMap);
	}
	
	/**
	 * 
	 * @param companyId
	 * @param type 1:合同状态，2：合同类型，3：客户状态，4：客户性质，5：联系人级别。6：客户行业
	 * enable 0返回不可用，1返回可用，2返回所有
	 * @return
	 */
	public Map<String,Integer> getDataDictionaryMap(String corpid, Integer type, Integer enable){
		Map<String,Integer> map = new HashMap<String,Integer>();
		
//		List<DataDictionaryEntity> list = getDataDictionaryList(corpid,type);
		List<DataDictionaryEntity> list = getNowDataDictionaryList(corpid, type, enable);
		
		if(list != null && list.size() > 0){
			for(DataDictionaryEntity entity : list){
				if(enable.equals(0)){
					if(entity.getEnable().equals(0)){
						map.put(entity.getName(), entity.getCode());
					}
				}else if(enable.equals(1)){
					if(entity.getEnable().equals(1)){
						map.put(entity.getName(), entity.getCode());
					}
				}else{
					map.put(entity.getName(), entity.getCode());
				}
			}
		}
		return map;
	}
	
	/**
	 * 获取一个公司所有数据字典的配置，用于web端客户列表页、合同列表页、机会列表等地方
	 * @param corpid
	 * @return
	 * @author kaka
	 * @time 2016年9月25日 下午4:14:41
	 */
	public Map<Integer,Map<Integer, DataDictionaryEntity>> getDataDictionaryMap(String corpid){
		
		Map<Integer,Map<Integer, DataDictionaryEntity>> returnMap = new HashMap<Integer,Map<Integer, DataDictionaryEntity>>();

		Map<String,Object> param = new HashMap<String,Object>();
		param.put("corpid", "0");
		param.put("orderByStr", "sort desc, id asc");
		// 取出系统所有的默认字典
		List<DataDictionaryEntity> defaultList = findEntitys(param);
		// 取出公司下所有的数据字典		 
		param.put("corpid", corpid);
		List<DataDictionaryEntity> thisList = findEntitys(param);
		
		/*
		 * 数据字典的类型为枚举类型，所以循环枚举类的所有值
		 */
		for(DictionaryTypeEnum type:DictionaryTypeEnum.values()){
			Map<Integer, DataDictionaryEntity> map = new HashMap<Integer, DataDictionaryEntity>();
			//如果是销售阶段，则专门一段逻辑处理销售阶段
			if(type.getCode() == 8){
				param.clear();
				param.put("corpid", corpid);
				param.put("orderByStr", "sort desc, id asc");
				param.put("del", 0);
				List<SalesStageEntity> stageList = salesStageModel.findEntitys(param);
				List<SalesStageEntity> tempList = new ArrayList<SalesStageEntity>();
				if(stageList == null || stageList.size() == 0){
					param.put("corpid", "0");
					stageList = salesStageModel.findEntitys(param);
					for (SalesStageEntity salesStageEntity : stageList) {
						SalesStageEntity entity = salesStageModel.initSalesStage(corpid, salesStageEntity);
						if (entity != null) {
							tempList.add(entity);
						}
					}
				}else {
					tempList = stageList;
				}
				for(SalesStageEntity entity : tempList){
					DataDictionaryEntity dataEntity = new DataDictionaryEntity();
					dataEntity.setId(entity.getId());
					dataEntity.setType(DictionaryTypeEnum.SALES_STAGE.getCode());//销售机会类型
					dataEntity.setAddtionnalField(entity.getType()); //销售阶段的type相当于数据字典的addtionnalField字段
					dataEntity.setName(entity.getStageName());
					dataEntity.setEnable(entity.getEnable());
					dataEntity.setCorpid(entity.getCorpid());//可能是0或该公司的公司ID 
					dataEntity.setStageType(entity.getType());
					dataEntity.setCode(entity.getCode());
					//赢率和操作指导
					dataEntity.setEstimateWinRate(entity.getEstimateWinRate());
					dataEntity.setStageGuide(entity.getStageGuide());
					
					map.put(dataEntity.getCode(), dataEntity);
				}
				returnMap.put(type.getCode(), map);
				
				continue;
			}
			
			//循环处理thisList，把公司已有的数据字典按照type分类
			for(DataDictionaryEntity dataDictionaryEntity : thisList){
				if(dataDictionaryEntity.getType().equals(type.getCode())){
					map.put(dataDictionaryEntity.getCode(), dataDictionaryEntity);
				}
			}
			if(map.size()>0){
				returnMap.put(type.getCode(), map);
				continue;
			}
			//如果公司没有这些数据字典，则循环处理defaultList，把系统的数据字典按照type分类
			for(DataDictionaryEntity dataDictionaryEntity : defaultList){
				if(dataDictionaryEntity.getType().equals(type.getCode())){
					//把系统默认的同步给公司
					DataDictionaryEntity entity = initDataDictionary(corpid, dataDictionaryEntity);
					map.put(entity.getCode(), entity);
				}
			}
			returnMap.put(type.getCode(), map);
		}

		return returnMap;
	}
	
	public DataDictionaryEntity getEntity(String corpid,Integer type,Integer code){
		DataDictionaryEntity entity = null;
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("corpid", corpid);
		param.put("type", type);
		param.put("code", code);
		List<DataDictionaryEntity> list = findEntitys(param);
		if(list.size() == 0){//本公司不存在数据字典，取系统默认
			param.put("corpid", "0");
			list = findEntitys(param);
		}
		entity = list.size() == 0 ? entity : list.get(0);
		return entity;
	}

	public DataDictionaryEntity getEntity(List<DataDictionaryEntity> dataDictionaryList, Integer code){
		if(code == null){
			return null;
		}
		for(DataDictionaryEntity dataDictionaryEntity : dataDictionaryList){
			if(code.equals(dataDictionaryEntity.getCode())){
				return dataDictionaryEntity;
			}
		}
		return null;
	}
	
	/**
	 * 获取自定义数据字典中要统计的部分.
	 * @param corpid
	 * @param type 字段类型，区分不同类型的字段  与DataDictionaryEntity中的一致
	 * @return
	 */
	public List<Integer> statisticTypeList(String corpid,Integer type) {
		Map<String, Object> param = new HashMap<String, Object>();
				
		param.put("corpid", corpid);
		param.put("type", type);
		param.put("addtionnalField", 1);
		param.put("enable", 1);
		param.put("del", 0);
		
		List<DataDictionaryEntity> dataDictionaryList =  findEntitys(param);
		
		if(dataDictionaryList == null || dataDictionaryList.size() == 0){//公司未设置销售阶段，使用默认
			param.put("corpid", "0");
			dataDictionaryList = findEntitys(param);
		}
		
		List<Integer> statisticTypeList = new ArrayList<Integer>();//不统计输单、取消
		for(DataDictionaryEntity dataDictionary : dataDictionaryList){
			statisticTypeList.add(dataDictionary.getCode());
		}
		Set<Integer> statisticTypeSet = new HashSet<Integer>(statisticTypeList);
		
		return new ArrayList<Integer>(statisticTypeSet);
	}
	
	public List<DataDictionaryEntity> getNowDataDictionaryList(String corpid, Integer type, Integer enable){
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("corpid", corpid);
		param.put("type", type);
//		param.put("enable", enable);
		CommentUtil.addToMap(param, "enable", enable);//enable为null则不插入
		param.put("del", 0);
		param.put("orderByStr", "sort desc, id asc");//sort从大到小排序--如果sort相等，默认按照id
		List<DataDictionaryEntity> dataDictionaryList =  findEntitys(param);
		
		if(dataDictionaryList == null || dataDictionaryList.size() == 0){//公司未设置，使用默认
			param.put("corpid", "0");
			dataDictionaryList = findEntitys(param);
		}
		return dataDictionaryList;
	}
	
	
	public DataDictionaryEntity initDataDictionary(String corpid, DataDictionaryEntity dataDictionaryEntity) {
		if (dataDictionaryEntity == null) {
			return null;
		}
		Integer now = DateUtil.getInt();
		DataDictionaryEntity entity = new DataDictionaryEntity();
		
		entity.setCorpid(corpid);
		entity.setRefEntity(dataDictionaryEntity.getRefEntity());
		entity.setName(dataDictionaryEntity.getName());
		entity.setCode(dataDictionaryEntity.getCode());
		entity.setType(dataDictionaryEntity.getType());
		entity.setAddtionnalField(dataDictionaryEntity.getAddtionnalField());
		entity.setEnable(dataDictionaryEntity.getEnable());
		entity.setIfDefault(dataDictionaryEntity.getIfDefault());
		entity.setReserved(dataDictionaryEntity.getReserved());
		entity.setAddTime(now);
		entity.setUpdateTime(now);
		entity.setSort(dataDictionaryEntity.getSort());
		
		dao.insert(entity);
		return entity;
	}
}

 