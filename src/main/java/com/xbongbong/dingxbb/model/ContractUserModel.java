 
package com.xbongbong.dingxbb.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xbongbong.dingxbb.dao.ContractUserDao;
import com.xbongbong.dingxbb.entity.ContractUserEntity;
import com.xbongbong.dingxbb.entity.UserEntity;
import com.xbongbong.util.CommentUtil;

 

@Component
public class ContractUserModel  implements IModel{

	@Autowired
	private ContractUserDao dao;
	
	public Integer insert(Object entity){
		((ContractUserEntity)entity).setId(null);

		return dao.insert((ContractUserEntity)entity);
	}

	public Integer update(Object entity){
		return dao.update((ContractUserEntity)entity);
	}
	
	public Integer save(ContractUserEntity entity){
		if(entity.getId() == null || entity.getId().equals(0)) {
			((ContractUserEntity)entity).setId(null);
			return dao.insert((ContractUserEntity)entity);
		}
		return dao.update((ContractUserEntity)entity);
	}

	public ContractUserEntity getByKey(Integer key, String corpid) {
		return dao.getByKey(key, corpid);
	}
	
	public ContractUserEntity getByUserIdContractId(String corpid, String userId, Integer contractId) {
		return dao.getByUserIdContractId(corpid, userId, contractId);
	}

	public Integer deleteByKey(Integer key, String corpid) {
		return dao.deleteByKey(key, corpid);
	}
	
	public Integer deleteEntitys(Map<String ,Object>  param){
		return dao.deleteEntitys(param);
	}
	 
	public List<ContractUserEntity> findEntitys(Map<String ,Object>  param){
		return dao.findEntitys(param);
	}
	public Integer getEntitysCount(Map<String ,Object>  param){
		return dao.getEntitysCount(param);
	}
	
	public List<ContractUserEntity> findEntitysJoinContract(Map<String ,Object>  param){
		return dao.findEntitysJoinContract(param);
	}
	
//	/**
//	 * contractStaff插入数据方法
//	 * @param staff
//	 * @param contractId
//	 */
//	public void contractStaffInsert(StaffEntity staff,Integer contractId, Integer isMain){
//	    
//		Integer staffId = staff.getId();
//		Integer companyId = staff.getCompanyId();
//		String realname = staff.getRealname();
//		String avatar = staff.getAvatar();
//		
//		ContractUserEntity contractStaffEntity = new ContractUserEntity();
//		contractStaffEntity.setCompanyId(companyId);
//		contractStaffEntity.setIsMain(isMain);
//		contractStaffEntity.setContractId(contractId);
//		contractStaffEntity.setStaffName(realname);
//		contractStaffEntity.setStaffAvatar(avatar);
//		contractStaffEntity.setStaffId(staffId);
//		
//		insert(contractStaffEntity);
//	}
	
	/**
	 * contractUser插入数据方法
	 * @param userEntity
	 * @param contractId
	 * @param isMain
	 */
	public void contractUserInsert(UserEntity userEntity,Integer contractId, Integer isMain){
	    
		String userId = userEntity.getUserId();
		String corpid = userEntity.getCorpid();
		String userName = userEntity.getName() == null ? "" : userEntity.getName();
		String avatar = userEntity.getAvatar() == null ? "" : userEntity.getAvatar();
		
		Map<String,Object> param = new HashMap<String,Object>();
		CommentUtil.addToMap(param, "corpid", corpid);
		CommentUtil.addToMap(param, "contractId", contractId);
		CommentUtil.addToMap(param, "userId", userId);
		CommentUtil.addToMap(param, "pageNum", 1);
		CommentUtil.addToMap(param, "start", 0);
		
		List<ContractUserEntity> contractUserList = this.findEntitys(param);
		if(contractUserList == null || contractUserList.size() < 1){
			ContractUserEntity contractUserEntity = new ContractUserEntity();
			contractUserEntity.setCorpid(corpid);
			contractUserEntity.setUserId(userId);
			contractUserEntity.setUserName(userName);
			contractUserEntity.setUserAvatar(avatar);
			contractUserEntity.setContractId(contractId);
			contractUserEntity.setIsMain(isMain);
			insert(contractUserEntity);
		}else{
			ContractUserEntity contractUserEntity = contractUserList.get(0);
			contractUserEntity.setCorpid(corpid);
			contractUserEntity.setUserId(userId);
			contractUserEntity.setUserName(userName);
			contractUserEntity.setUserAvatar(avatar);
			contractUserEntity.setContractId(contractId);
			contractUserEntity.setIsMain(isMain);
			contractUserEntity.setDel(0);
			update(contractUserEntity);
		}
	}

}

 