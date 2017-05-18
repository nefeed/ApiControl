 
package com.xbongbong.dingxbb.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xbongbong.dingxbb.dao.ContractDao;
import com.xbongbong.dingxbb.entity.ContractEntity;
import com.xbongbong.dingxbb.entity.ContractProductEntity;
import com.xbongbong.dingxbb.entity.ContractUserEntity;
import com.xbongbong.dingxbb.entity.CustomerEntity;
import com.xbongbong.dingxbb.entity.UserEntity;
import com.xbongbong.util.DateUtil;
import com.xbongbong.util.StringUtil;
 

@Component
public class ContractModel  implements IModel{

	@Autowired
	private ContractDao dao;
	
	public Integer insert(Object entity){
		((ContractEntity)entity).setId(null);

		return dao.insert((ContractEntity)entity);
	}

	public Integer update(Object entity){
		return dao.update((ContractEntity)entity);
	}
	
	public Integer save(ContractEntity entity){
		if(entity.getId() == null || entity.getId().equals(0)) {
			((ContractEntity)entity).setId(null);

			return dao.insert((ContractEntity)entity);
		}
		return dao.update((ContractEntity)entity);
	}
	
	public Integer deleteByKey( Integer key, String corpid){
		return dao.deleteByKey(key, corpid);
	}
	public ContractEntity getByKey( Integer key, String corpid){
		return dao.getByKey(key, corpid);
	}
	
	public List<ContractEntity> findEntitys(Map<String ,Object>  param){
		return dao.findEntitys(param);
	}
	
//	筛选需要corpid
	public List<ContractEntity> getListByCustomer(Integer customerId, String corpid){
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("corpid", corpid);
		param.put("customerId", customerId);
		param.put("del", 0);
		List<ContractEntity> list = findEntitys(param);
		return list;
	}
	
	public Integer getEntitysCount(Map<String ,Object>  param){
		return dao.getEntitysCount(param);
	}

	public List<ContractEntity> findEntitysJoinContractUser(Map<String ,Object>  param){
		return dao.findEntitysJoinContractUser(param);
	}
	
	public Integer getJoinContractUserCount(Map<String ,Object>  param){
		return dao.getJoinContractUserCount(param);
	}
	
	/**客户排名**/
	public List<ContractEntity> rankByCustomer(Map<String ,Object>  param){
		return dao.rankByCustomer(param);
	}
	/**销售排名**/
	public List<ContractEntity> saleAmountRank(Map<String ,Object>  param){
		return dao.saleAmountRank(param);
	}
	public List<ContractEntity> customerAmountRank(Map<String ,Object>  param){
		return dao.customerAmountRank(param);
	}
	/*****获得总金额****/
	public Double getSumAmount(Map<String ,Object>  param){
		return dao.getSumAmount(param);
	}
	
	
	/**合同一周统计*/
	public List<ContractEntity> statisticsByWeek(Map<String ,Object>  param){
		return dao.statisticsByWeek(param);
	}
	/** @2 当前月份或季度的合同总额 */
	public Map<String,Object> totalSumOfThisMonthOrSeason(Map<String ,Object>  param){
		return dao.totalSumOfThisMonthOrSeason(param);
	}
	/** @3 当前月份或季度的合同分组总计*/
	public Integer rankOfThisMonthOrSeason(Map<String ,Object>  param){
		return dao.rankOfThisMonthOrSeason(param);
	}
	/** 统计合同明细 **/
	public List<ContractEntity> contractDetails(Map<String ,Object>  param){
		return dao.contractDetails(param);
	}
	
	public List<Map<String,Object>> contractStatistic(Map<String ,Object>  param){
		return dao.contractStatistic(param);
	}
	
	public void addRelateContractUser(UserEntity cUser, CustomerEntity customer, Integer isMain, ContractUserModel contractUserModel) {
		if(cUser == null || customer == null){
			return ;
		}
		// 客户相关的所有合同
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("customerId", customer.getId());
		param.put("del", 0);
		List<ContractEntity> contractList = findEntitys(param);
		for(ContractEntity contractEntity : contractList){
			ContractUserEntity contractUser = null;
			param.clear();
			param.put("userId", cUser.getUserId());
			param.put("contractId", contractEntity.getId());
			List<ContractUserEntity> contractUserList = contractUserModel.findEntitys(param);
			if(contractUserList.size() > 0){
				contractUser = contractUserList.get(0);
			}
			
			if(contractUser == null){
				contractUser = new ContractUserEntity();
				contractUser.setAddTime(DateUtil.getInt());
				contractUser.setCorpid(cUser.getCorpid());
			}
			contractUser.setContractId(contractEntity.getId());
			contractUser.setDel(0);
			contractUser.setIsMain(isMain);
			contractUser.setUserAvatar(cUser.getAvatar());
			contractUser.setUserId(cUser.getUserId());
			contractUser.setUserName(cUser.getName());
			contractUserModel.save(contractUser);
		}
	}
	public void deleteRelateContractUser(UserEntity cUser, CustomerEntity customer, ContractUserModel contractUserModel) {
		if(cUser == null || customer == null){
			return ;
		}
		// 客户相关的所有合同
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("customerId", customer.getId());
		param.put("del", 0);
		List<ContractEntity> contractList = findEntitys(param);
		for(ContractEntity contractEntity : contractList){
			param.clear();
			param.put("userId", cUser.getUserId());
			param.put("contractId", contractEntity.getId());
			param.put("del", 0);
			List<ContractUserEntity> contractUserList = contractUserModel.findEntitys(param);
			if(contractUserList.size()>0){
				contractUserModel.deleteByKey(contractUserList.get(0).getId(), cUser.getCorpid());
			}
		}
	}
	
	/**
	 * 将合同产品列表中的合同签订人扩展字段进行填充
	 * @param contractProductList
	 * @param corpid
	 * @return
	 * @author kaka
	 * @time 2016年9月21日 下午3:32:44
	 */
	public List<ContractProductEntity> setContractSignUserId(List<ContractProductEntity> contractProductList, String corpid) {
		Set<Integer> contractIdIn = new HashSet<Integer>();
		contractIdIn.add(-1);
		for(ContractProductEntity contractProductEntity : contractProductList){
			contractIdIn.add(contractProductEntity.getContractId());
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("idIn", contractIdIn);
		param.put("corpid", corpid);
		param.put("columns", "id,sign_user_id,sign_person");
		
		List<ContractEntity> contractList = dao.findEntitys(param);
		
		Map<Integer,String> contractSignUserIdMap = new HashMap<Integer,String>();
		for(ContractEntity contract : contractList){
			contractSignUserIdMap.put(contract.getId(), contract.getSignUserId());
		}
		
		for(ContractProductEntity contractProductEntity : contractProductList){
			String signUserId  = contractSignUserIdMap.get(contractProductEntity.getContractId());
			if(!StringUtil.isEmpty(signUserId) ){
				contractProductEntity.setSignUserId(signUserId);
			}
		}
		
		return contractProductList;
	}
}

 