 
package com.xbongbong.dingxbb.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xbongbong.dingxbb.dao.DepartmentDao;
import com.xbongbong.dingxbb.entity.DepartmentEntity;
import com.xbongbong.dingxbb.entity.UserEntity;
import com.xbongbong.util.StringUtil;

 

@Component
public class DepartmentModel  implements IModel{

	@Autowired
	private DepartmentDao dao;
	
	public Integer insert(Object entity){
		return dao.insert((DepartmentEntity)entity);
	}

	public Integer update(Object entity){
		return dao.update((DepartmentEntity)entity);
	}
	
	public Integer save(DepartmentEntity entity){
		if(entity.getId() == null || entity.getId().equals(0)) {
			return dao.insert((DepartmentEntity)entity);
		}
		return dao.update((DepartmentEntity)entity);
	}
	 
	public List<DepartmentEntity> findEntitys(Map<String ,Object>  param){
		return dao.findEntitys(param);
	}
	public Integer getEntitysCount(Map<String ,Object>  param){
		return dao.getEntitysCount(param);
	}

	public DepartmentEntity getByKey(String key, String corpid) {
		return dao.getByKey(key, corpid);
	}
	
	/**
	 * 通过公司ID和部门ID获取部门实体，不管del为1还是0
	 * @param key
	 * @param corpid
	 * @return
	 */
	public DepartmentEntity getByKeyIgnoreDel(String key, String corpid) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("corpid", corpid);
		param.put("id", key);
		
		/*
		 * 此处getByKey方法返回的是del=0的记录，这里del=1的记录也可以，最后会将其del置为0
		 */
		List<DepartmentEntity> depList = dao.findEntitys(param);
		
		return depList.size() > 0 ? depList.get(0) : null;
	}

	public Integer deleteByKey(String key, String corpid) {
		return dao.deleteByKey(key, corpid);
	}
	 
	
	/**
	 * 删除某公司的部门数据
	 * @param corpid
	 * @return
	 */
	public Integer deleteByCorpId(String corpid) {
		return dao.deleteByCorpId(corpid);
	}
	
	/**
	 * 批量删除部门，根据部门ID列表和公司ID
	 * @param keys
	 * @param corpid
	 * @return
	 */
	public Integer deleteByKeys(List<Long> keys, String corpid) {
		return dao.deleteByKeys(keys, corpid);
	}
	
	public List<DepartmentEntity> getAllDepList(String corpid){
		if(StringUtil.isEmpty(corpid)){
			return null;
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("corpid", corpid);
		param.put("del", 0);
		return findEntitys(param);
	}
	
	/**
	 * 获得部门路由List+0（全公司）
	 * @param dep
	 * @return
	 */
	public List<Long> getRouterIdList(DepartmentEntity dep){
		List<Long> depIdList = new ArrayList<Long>();
//		depIdList.add(0l);
		if(dep != null){
			String depIdRouter = dep.getDepIdRouter();
			String[] depIdArray = depIdRouter.split("\\|");
			for(String depId : depIdArray){
				if(!StringUtil.isEmpty(depId)){
					depIdList.add(StringUtil.StringToLong(depId));
				}
			}
		}
		return depIdList;
	}


	/**
	 * 该员工所在部门idlist
	 * @param userEntity
	 * @param modelMap
	 */
	public void setProductDepIdIn(UserEntity userEntity, Map<String, Object> modelMap, UserModel userModel) {
		String corpid = userEntity.getCorpid();
		
		List<Long> belongdeptList = userModel.getUserBelongDeptIdList(userEntity);//所属部门id列表
//		List<Long> depIdIn = new ArrayList<Long>();
		Set<Long> depIdIn = new HashSet<Long>();
		if (belongdeptList != null && belongdeptList.size() > 0) {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("corpid", corpid);
			param.put("del", 0);
			param.put("idIn", belongdeptList);
			List<DepartmentEntity> departList = findEntitys(param);
			for (DepartmentEntity department : departList) {
				depIdIn.addAll(getRouterIdList(department));
			}
		}
		if (depIdIn.size() <= 0) {
			depIdIn.add(-1l);
		}
//		DepartmentEntity department = departmentModel.getByKey(staffEntity.getDepId().toString(), companyId.toString());//TODO pid改成longs
		modelMap.put("depIdIn", depIdIn);
	}
}

 