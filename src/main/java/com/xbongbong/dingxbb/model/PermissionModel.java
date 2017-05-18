 
package com.xbongbong.dingxbb.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xbongbong.dingxbb.dao.PermissionDao;
import com.xbongbong.dingxbb.entity.PermissionEntity;
import com.xbongbong.dingxbb.entity.RoleEntity;
import com.xbongbong.dingxbb.entity.UserEntity;
import com.xbongbong.dingxbb.helper.JedisUtils;
import com.xbongbong.util.CommentUtil;
import com.xbongbong.util.StringUtil;

 

@Component
public class PermissionModel  implements IModel{

	@Autowired
	private PermissionDao dao;
	
	@Autowired
	private RoleModel roleModel;
	
	public Integer insert(Object entity){
		((PermissionEntity)entity).setId(null);

		return dao.insert((PermissionEntity)entity);
	}

	public Integer update(Object entity){
		return dao.update((PermissionEntity)entity);
	}
	
	public Integer save(PermissionEntity entity){
		if(entity.getId() == null || entity.getId().equals(0)) {
			entity.setId(null);

			return dao.insert(entity);
		}
		return dao.update(entity);
	}

	public Integer deleteByKey(Integer key, Integer companyId) {
		return dao.deleteByKey(key, companyId);
	}
	
	public PermissionEntity getByKey( String key){
		return dao.getByKey(key);
	}
	 
	public List<PermissionEntity> findEntitys(Map<String ,Object>  param){
		return dao.findEntitys(param);
	}
	public Integer getEntitysCount(Map<String ,Object>  param){
		return dao.getEntitysCount(param);
	}
	
	
	
	
	
	
	private List<PermissionEntity> permissionEntityListTreeOrdered; // 按照菜单级别排过序的权限列表
	
	private List<PermissionEntity> permissionEntityListTree;
	
	public List<PermissionEntity> findPermissionsByPid(Integer pid){
		Map<String,Object>  param = new HashMap<String,Object> ();
		CommentUtil.addToMap(param, "parentId",pid);
		List<PermissionEntity> tmp = dao.findEntitys(param);
		return tmp;
		
	}
	
	/**
	 * 判断一个权限是否有子权限
	 * 
	 * @param menuEntity
	 * @return
	 */
	public boolean hasChildPermission(PermissionEntity permissionEntity) {
		List<PermissionEntity> tmp = findPermissionsByPid(permissionEntity.getId());
		if (tmp.size() > 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 递归遍历所有的子菜单
	 * 
	 * @param menuEntity
	 * @return
	 */
	public PermissionEntity getChildSysPermissionEntity(PermissionEntity permissionEntity) {
		permissionEntityListTreeOrdered.add(permissionEntity);// 将菜单按照树的顺序加入list
		if (hasChildPermission(permissionEntity)) {// 判断是否有子菜单
			permissionEntity.setHasChildPermission(true);
			List<PermissionEntity> childPermissionList = findPermissionsByPid(permissionEntity
					.getId());
			for (int i = 0; i < childPermissionList.size(); i++) {
				permissionEntity
						.setChildPermissionEntityList(getChildSysPermissionEntity(childPermissionList
								.get(i)));
			}
		}
		return permissionEntity;
	}
	
	/**
	 * 取得所有菜单
	 * 
	 * @return
	 */
	public List<PermissionEntity> findAllPermissions() {
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("isUse", 1);
		List<PermissionEntity> allPermissionEntityList = findEntitys(param);// 获取所有的根菜单
		return allPermissionEntityList;
	}
	
	/**
	 * 取得所有菜单，并且组织成tree
	 * 
	 * @return
	 */
	public List<PermissionEntity> findAllPermissionsTree() {
		permissionEntityListTreeOrdered = new ArrayList<PermissionEntity>();
		List<PermissionEntity> rootSysPermissionEntityList = findPermissionsByPid(0);// 获取所有的根菜单
		for (int i = 0; i < rootSysPermissionEntityList.size(); i++) { // 循环根菜单
			// menuEntityListTreeOrdered.add(rootMenuEntityList.get(i));
			rootSysPermissionEntityList.set(i, getChildSysPermissionEntity(rootSysPermissionEntityList.get(i)));
		}
		return rootSysPermissionEntityList;
	}
	
	public List<PermissionEntity> getPermissionEntityList() {
		permissionEntityListTree = findAllPermissionsTree();
		return permissionEntityListTreeOrdered;
	}
	
	public void setChildList(PermissionEntity permissionEntity){		
		
		if(permissionEntity == null ){
			return ;
		}
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("orderByStr", "sort desc");
		param.put("isUse", 1);
//		param.put("isMenu", 1);
		param.put("parentId", permissionEntity.getId());

		List<PermissionEntity> childList = findEntitys(param);
		for (PermissionEntity entity : childList) {
			setChildList(entity);
		}
		
		permissionEntity.setChildPermissionEntityList(childList);
		
	}

	/**
	 * 获取菜单权限，用于手机端菜单显隐
	 * @param userEntity
	 * @param httpSession
	 * @return
	 */
	public boolean setMenu(HttpServletRequest request, HttpServletResponse response,
			UserEntity userEntity, Map<String, Object> modelMap) {
		//所有菜单
		List<PermissionEntity> allPermissionEntityList = findAllPermissions();
		if(userEntity == null){
			JedisUtils.delSessionValue(request, "permissionsMap");
			return false;
		}
		//从数据库中获取权限列表
		List<PermissionEntity> permissionEntityList = getPermissionFromDB(userEntity);
		if(permissionEntityList == null){
			return false;
		}
		//将当前permissionEntityList存与session和jedisSession（当session中取不到时在jedis中取）
		request.getSession().setAttribute("currentPermissionEntityList", permissionEntityList);
		JedisUtils.setSessionValue(request, response, "currentPermissionEntityList", permissionEntityList);
		request.getSession().setAttribute("allPermissionEntityList", allPermissionEntityList);
		JedisUtils.setSessionValue(request, response, "allPermissionEntityList", allPermissionEntityList);
		//用于菜单显隐、拦截器判断权限
		Map<String,Boolean> permissionMap = new HashMap<String,Boolean>();
		for(PermissionEntity permissionEntity : permissionEntityList){
			permissionMap.put(permissionEntity.getAlias(), true);
		}
		modelMap.put("permissionMap", permissionMap);//首页菜单权限
		request.getSession().setAttribute("permissionMap", permissionMap);
		return JedisUtils.setSessionValue(request, response, "permissionMap", permissionMap);
	} 
	
	/**
	 * 从数据库中获取权限列表
	 * @param user
	 * @return
	 */
	protected List<PermissionEntity> getPermissionFromDB(UserEntity user) {
		//获取权限列表
	    String roleIdsStr = user.getRoleIds();
	    if(StringUtil.isEmpty(roleIdsStr)) {
	    	return null;
	    }
	    String[] roleIds = roleIdsStr.substring(1, roleIdsStr.length() - 1).split("\\|");
	    List<PermissionEntity> permissionList = getPermissionList(roleIds, user.getCorpid());
	    if(permissionList == null || permissionList.size() < 1) {
	    	return null;
	    }
	    return permissionList;
	}
	
	/**
	 * 根据角色获取权限列表
	 * @param roleIds
	 * @return
	 */
	protected List<PermissionEntity> getPermissionList(String[] roleIds, String corpId) {
		List<Integer> roleIntList = new ArrayList<Integer>();
		for(String roleId : roleIds) {
			roleIntList.add(Integer.valueOf(roleId));
		}
		
		List<PermissionEntity> permissionList = null;
		List<RoleEntity> roles = new ArrayList<RoleEntity>();
		//该用户具有的默认角色ID列表
		List<Integer> defaultRoleInt = new ArrayList<Integer>();
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("isUse", 1);
		params.put("corpid", "0");//corpid为String型
		
		//先拿出所有companyId=0的默认角色
		List<RoleEntity> defaultRoles = roleModel.findEntitys(params);
		
		//处理默认角色
		for(RoleEntity dr: defaultRoles) {
			Integer drId = dr.getId();
			if(roleIntList.contains(drId)) {
				roles.add(dr);
				defaultRoleInt.add(drId);
				roleIntList.remove(drId);
				
				//一点优化
				if(roleIntList.size() <= 0) {
					break;
				}
			}
		}

		//如果都是默认角色下面的逻辑则不执行
		if(roleIntList.size() > 0) {
			params.put("corpid", corpId);
			params.put("idIn", roleIntList);
			
			//公司自定义的角色
			List<RoleEntity> companyDefinedRoles = roleModel.findEntitys(params);
			//合并默认角色和公司自定义的角色---本处role的处理是为了适应mycat companyId路由
			roles.addAll(companyDefinedRoles);
		}
		
		//获取不重复的permission ID列表
		Set<Integer> permissionIdsSet = new HashSet<Integer>();
		for(RoleEntity role : roles) {
			String permissionStr = role.getPermissions();
			if(!StringUtil.isEmpty(permissionStr)) {
				String[] permissionIdsStr = permissionStr.substring(1, permissionStr.length() - 1).split("\\|");
				for(String permissionIdStr : permissionIdsStr) {
					if(!StringUtil.isEmpty(permissionIdStr)) {//手贱输成||这种空权限可以丢弃而不是出错
						permissionIdsSet.add(Integer.valueOf(permissionIdStr));
					}
				}
			}
		}
		
		List<Integer> permissionIds = new ArrayList<Integer>(permissionIdsSet);
		
		if(permissionIds == null || permissionIds.size() < 1) {
			return null;
		}
		
		//获取permission列表
		params.clear();
//		params.put("columns", "id,name,alias,parent_id,is_menu,sort,permission,manage_sub,filter_field");
		params.put("idIn", permissionIds);
		params.put("orderByStr", "sort desc");
		params.put("isUse", 1);
		
		 
		params.put("corpid",corpId);
		permissionList = findEntitys(params);//TODO vip

		return permissionList;
	}
}

 