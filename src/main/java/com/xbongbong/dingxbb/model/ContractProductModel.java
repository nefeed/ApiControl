 
package com.xbongbong.dingxbb.model;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xbongbong.dingxbb.dao.ContractProductDao;
import com.xbongbong.dingxbb.entity.ContractProductEntity;
 

@Component
public class ContractProductModel  implements IModel{

	@Autowired
	private ContractProductDao dao;
	
	public Integer insert(Object entity){
		return dao.insert((ContractProductEntity)entity);
	}

	public Integer update(Object entity){
		return dao.update((ContractProductEntity)entity);
	}
	
//	public Integer save(ContractProductEntity entity){
//		if(entity.getId() == null || entity.getId().equals(0)) {
//			return dao.insert((ContractProductEntity)entity);
//		}
//		return dao.update((ContractProductEntity)entity);
//	}
	
	public ContractProductEntity getByKey(String corpid,Integer contractId,Integer productId) {
		return dao.getByKey(corpid, contractId, productId);
	}

	public Integer deleteByKey(String corpid, Integer contractId, Integer productId){
		return dao.deleteByKey(corpid, contractId, productId);
	}
	 
	public List<ContractProductEntity> findEntitys(Map<String ,Object>  param){
		return dao.findEntitys(param);
	}
	public Integer getEntitysCount(Map<String ,Object>  param){
		return dao.getEntitysCount(param);
	}

	public Integer deleteByContractId(Integer contractId, String corpid) {
		return dao.deleteByContractId(contractId, corpid);
	}

	/**通过corpid,contractId删除合同的所有产品*/
	public Integer deleteContractAllProduct(String corpid,Integer contractId){
		return dao.deleteContractAllProduct(corpid, contractId);
	}
	/**通过contractId,productId删除合同的单个产品*/
	public Integer deleteContractProduct(String corpid, Integer contractId,Integer productId){
		return dao.deleteContractProduct(corpid, contractId, productId);
	}

	public List<ContractProductEntity> findEntitysJoinProduct(Map<String ,Object>  param){
		return dao.findEntitysJoinProduct(param);
	}
	
	/**
	 * 报表中心统计产品销量时，获取合同签订人信息    只比findEntitys多查询出signUserId
	 * @param param
	 * @return
	 * @author chuanpeng.zhang
	 * @time 2016-8-2 下午4:24:29
	 */
	public List<ContractProductEntity> findEntitysJoinContract(Map<String ,Object>  param){
		return dao.findEntitysJoinContract(param);
	}
	public Integer getEntitysJoinContractCount(Map<String ,Object>  param){
		return dao.getEntitysJoinContractCount(param);
	}
}

 