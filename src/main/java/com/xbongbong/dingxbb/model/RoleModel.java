 
package com.xbongbong.dingxbb.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xbongbong.dingxbb.dao.RoleDao;
import com.xbongbong.dingxbb.entity.PermissionEntity;
import com.xbongbong.dingxbb.entity.RoleEntity;

 

@Component
public class RoleModel  implements IModel{

	@Autowired
	private RoleDao dao;
	
	public Integer insert(Object entity){
		((RoleEntity)entity).setId(null);

		return dao.insert((RoleEntity)entity);
	}

	public Integer update(Object entity){
		return dao.update((RoleEntity)entity);
	}
	
	public Integer save(RoleEntity entity){
		if(entity.getId() == null || entity.getId().equals(0)) {
			entity.setId(null);

			return dao.insert(entity);
		}
		return dao.update(entity);
	}

	public Integer deleteByKey(Integer key, String corpid) {
		return dao.deleteByKey(key, corpid);
	}
	
	public RoleEntity getByKey( Integer key, String corpId){
		return dao.getByKey(key, corpId);
	}
	 
	public List<RoleEntity> findEntitys(Map<String ,Object>  param){
		return dao.findEntitys(param);
	}
	public Integer getEntitysCount(Map<String ,Object>  param){
		return dao.getEntitysCount(param);
	}
	 
	public RoleEntity getByRoleName( String roleName,String corpid){
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("roleName", roleName);
		param.put("corpid", corpid);
		List<RoleEntity> roleEntityList = this.findEntitys(param);
		if(roleEntityList.size()==0){//公司未设置这样的role，在默认中查找
			param.put("corpid", "0");
			roleEntityList = this.findEntitys(param);
		}
		Integer num = roleEntityList.size();
		return num == 0 ? null : roleEntityList.get(0);
	}

	
	/**
	 * 判断用户是否有权限，是否显示按钮  比如：新员工审核，角色管理
 	 * @param roleIdsStr 用户所有角色 格式：|2|4|
	 * @param permission 格式：|2|
	 * @return
	 */
	public boolean havePermission(List<PermissionEntity> permissionList, String permission) {
		if (permissionList == null || permissionList.size() <= 0) {
			return false;
		}
		
		for(PermissionEntity pEntity : permissionList) {
			String permissionIdString = "|" + pEntity.getId() + "|";
			if(permissionIdString.indexOf(permission) > -1) {
				return true;
			}
		}
		
		return false;
	}
}

 