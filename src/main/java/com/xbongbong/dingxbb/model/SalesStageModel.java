 
package com.xbongbong.dingxbb.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xbongbong.dingxbb.dao.SalesStageDao;
import com.xbongbong.dingxbb.entity.SalesStageEntity;
import com.xbongbong.util.DateUtil;
 

@Component
public class SalesStageModel  implements IModel{

	@Autowired
	private SalesStageDao dao;
	
	public Integer insert(Object entity){
		((SalesStageEntity)entity).setId(null);

		return dao.insert((SalesStageEntity)entity);
	}

	public Integer update(Object entity){
		return dao.update((SalesStageEntity)entity);
	}
	
//	public Integer save(IEntity entity){
//		if(entity.getId() == null || entity.getId().equals(0)) {
//			((SalesStageEntity)entity).setId(null);
//
//			return dao.insert((SalesStageEntity)entity);
//		}
//		return dao.update((SalesStageEntity)entity);
//	}

	public Integer deleteByKey(String key, String corpId) {
		return dao.deleteByKey(key, corpId);
	}
	
	public SalesStageEntity getByKey( String key, String corpId){
		return dao.getByKey(key, corpId);
	}
	public SalesStageEntity getByKey( Integer key, String corpId){
		return dao.getByKey(key, corpId);
	}
	 
	public List<SalesStageEntity> findEntitys(Map<String ,Object>  param){
		return dao.findEntitys(param);
	}
	public Integer getEntitysCount(Map<String ,Object>  param){
		return dao.getEntitysCount(param);
	}
	public Integer sort(Map<String ,Object>  param, Map<String ,Integer>  sortMap) {
		return dao.sort(param, sortMap);
	}
	 
	public List<SalesStageEntity> getByStageName(String stageName, String corpid){
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("corpid", corpid);
		List<SalesStageEntity> list = findEntitys(param);
		List<SalesStageEntity> retList = null;
		if(list.size() == 0){//公司未设置销售阶段
			param.put("corpid", "0");
			param.put("stageName", stageName);
			retList = findEntitys(param);
		}else{
			param.put("corpid", corpid);
			param.put("stageName", stageName);
			retList = findEntitys(param);
		}
		return retList;
	}

	public SalesStageEntity getEntity(Integer companyId,Integer code){
		SalesStageEntity retEntity = null;
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("companyId", companyId);
		param.put("code", code);
		List<SalesStageEntity> list = findEntitys(param);
		//公司设置不存在，使用系统默认
		if(list.size() == 0){
			param.put("companyId", 0);
			list = findEntitys(param);
		}
		if(list.size()>0){
			retEntity = list.get(0);
		}
		return retEntity;
	}
	
	/**
	 * 获得公司的销售阶段列表
	 * @param companyId
	 * @return
	 */
	public List<SalesStageEntity> getSalesStageList(Integer companyId){
		List<SalesStageEntity> list = null;
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("companyId", companyId);
		param.put("orderByStr", "type");
		list = findEntitys(param);
		if(list.size() == 0){//公司未设置销售阶段，使用默认
			param.put("companyId", 0);
			param.put("orderByStr", "type");
			list = findEntitys(param);
		}
		return list;
	}
	
	public SalesStageEntity getEntity(String corpid,Integer code){
		SalesStageEntity retEntity = null;
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("corpid", corpid);
		param.put("code", code);
		List<SalesStageEntity> list = findEntitys(param);
		//公司设置不存在，使用系统默认
		if(list.size() == 0){
			param.put("corpid", "0");
			list = findEntitys(param);
		}
		if(list.size()>0){
			retEntity = list.get(0);
		}
		return retEntity;
	}
	
	/**
	 * 获得公司的销售阶段列表
	 * @param corpid
	 * @return
	 */
	public List<SalesStageEntity> getSalesStageList(String corpid){
		List<SalesStageEntity> list = null;
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("corpid", corpid);
		param.put("orderByStr", "sort desc, id asc");//sort从大到小排序--如果sort相等，默认按照id
		param.put("del", 0);
		param.put("enable", 1);
		
		list = findEntitys(param);
		if(list == null || list.size() == 0){//公司未设置销售阶段，使用默认
			param.put("corpid", "0");
			list = findEntitys(param);
		}
		
		return list;
	}
	
	/**
	 * 获取某一阶段--比如统计获取赢单
	 * @param corpid
	 * @return
	 */
	public SalesStageEntity getSalesStage(String corpid, Integer type){
		List<SalesStageEntity> list = null;
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("type", type);
		param.put("corpid", corpid);
		param.put("orderByStr", "sort desc, id asc");//sort从大到小排序--如果sort相等，默认按照id
		param.put("del", 0);
		param.put("enable", 1);
		list = findEntitys(param);
		if(list == null || list.size() == 0){//公司未设置销售阶段，使用默认
			param.put("corpid", "0");
			list = findEntitys(param);
		}
		
		SalesStageEntity salesStageEntity = null;
		if (list != null && list.size() > 0) {
			salesStageEntity = list.get(0);
		}
		
		return salesStageEntity;
	}
	 
	/**
	 * 统计特定阶段
	 * 比如，获取赢单和普通阶段 的salesStageIdIn或salesStageList，排除输单和取消的阶段.
	 * 比如，历史赢率分析，统计的是：赢单、输单、取消
	 * 注：根据returnType的不同，返回值也不同!
	 * @param corpid
	 * @param returnType 1 则返回salesStageIdIn即阶段idList，2 则返回salesStageList即阶段entityList
	 * 					即一个为id list，一个为对象list
	 * @param typeIn 0普通，1赢单，2输单，3取消----对应tb_sales_stage表的type字段
	 * 
	 * @return 两种返回值--根据returnType的变化
	 */
	public Object statisticStageList(String corpid, Integer returnType, List<Integer> typeIn) {
		Map<String, Object> param = new HashMap<String, Object>();
				
		//阶段
//		List<Integer> typeIn = new ArrayList<Integer>();//不统计输单、取消  只获取赢单和普通的阶段
//		typeIn.add(0);
//		typeIn.add(1);
		
		param.put("typeIn", typeIn);		
		param.put("corpid", corpid);
		param.put("orderByStr", "sort desc, id asc");//sort从大到小排序--如果sort相等，默认按照id
		param.put("del", 0);
		param.put("enable", 1);
		
		List<SalesStageEntity> salesStageList =  findEntitys(param);
		
		if(salesStageList == null || salesStageList.size() == 0){//公司未设置销售阶段，使用默认
			param.put("corpid", "0");
			salesStageList = findEntitys(param);
		}
		
		if (returnType == 1) {//返回salesStageIdIn
			List<Integer> salesStageIdIn = new ArrayList<Integer>();//不统计输单、取消
			for (SalesStageEntity salesStageEntity : salesStageList) {
				salesStageIdIn.add(salesStageEntity.getCode());
			}
			if (salesStageIdIn == null || salesStageIdIn.size() <= 0) {
				salesStageIdIn.add(-1);
			}
			
			return salesStageIdIn;
		}else {//返回salesStageList
			return salesStageList;			
		}
		
	}
	
	/**
	 * 给公司初始化销售阶段
	 * @param corpid
	 * @param salesStageEntity
	 * @return
	 * @author wzq
	 * @time 2016-8-9 下午3:55:36
	 */
	public SalesStageEntity initSalesStage(String corpid, SalesStageEntity salesStageEntity) {
		if (salesStageEntity == null) {
			return null;
		}
		Integer now = DateUtil.getInt();
		SalesStageEntity entity = new SalesStageEntity();
		
		entity.setCorpid(corpid);
		entity.setCode(salesStageEntity.getCode());
		entity.setStageName(salesStageEntity.getStageName());
		entity.setEnable(salesStageEntity.getEnable());
		entity.setType(salesStageEntity.getType());
		entity.setEstimateWinRate(salesStageEntity.getEstimateWinRate());
		entity.setStageGuide(salesStageEntity.getStageGuide());
		entity.setAddTime(now);
		entity.setUpdateTime(now);
		entity.setDel(0);
		entity.setSort(salesStageEntity.getSort());
		
		dao.insert(entity);
		return entity;
	}

}

 