 
package com.xbongbong.dingxbb.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.xbongbong.dingxbb.dao.UserDao;
import com.xbongbong.dingxbb.entity.DepartmentEntity;
import com.xbongbong.dingxbb.entity.RoleEntity;
import com.xbongbong.dingxbb.entity.UserEntity;
import com.xbongbong.dingxbb.entity.WeakManageEntity;
import com.xbongbong.dingxbb.helper.JedisUtils;
import com.xbongbong.util.CommentUtil;
import com.xbongbong.util.CookieUtil;
import com.xbongbong.util.StringUtil;
import com.xbongbong.util.URLEncodeUtils;

 

@Component
public class UserModel  implements IModel{

	
	@Autowired
	private UserDao dao;
	@Autowired
	protected DepartmentModel departmentModel;
	@Autowired
	protected RoleModel roleModel;
	@Autowired
	protected WeakManageModel weakManageModel;
	
	public Integer insert(Object entity){
		return dao.insert((UserEntity)entity);
	}

	public Integer update(Object entity){
		return dao.update((UserEntity)entity);
	}
	
//	public Integer save(Object entity){
//		if(entity.getId() == null || entity.getId().equals(0)) {
//			return dao.insert((UserEntity)entity);
//		}
//		return dao.update((UserEntity)entity);
//	}
	
	public Integer deleteByKey( String key, String corpid){
		return dao.deleteByKey(key, corpid);
	}
	
	/**
	 * 删除某公司的用户
	 * @param corpid
	 * @return
	 */
	public Integer deleteByCorpId(String corpid) {
		return dao.deleteByCorpId(corpid);
	}
	
	public List<UserEntity> findEntitys(Map<String ,Object>  param){
		return dao.findEntitys(param);
	}
	
	public List<String> getUserIds(Map<String ,Object>  param){
		return dao.getUserIds(param);
	}
	
	public Integer getEntitysCount(Map<String ,Object>  param){
		return dao.getEntitysCount(param);
	}
	
	public List<UserEntity> findEntitysJoin(Map<String ,Object>  param){
		return dao.findEntitysJoin(param);
	}
	
	public List<String> getUserIdsJoin(Map<String ,Object>  param){
		return dao.getUserIdsJoin(param);
	}

	public UserEntity getByKeyIngoreDel(String key, String corpid) {
		return dao.getByKey(key, corpid);
	}
	
	public UserEntity getByKey(String key, String corpid) {
		if(StringUtil.isEmpty(key) || StringUtil.isEmpty(corpid)){
			return null;
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userId", key);
		param.put("corpid", corpid);
		param.put("del", 0);
		List<UserEntity> userList = findEntitys(param);
		return userList.size()==0?null:userList.get(0);
	}
	
	
	/**
	 * 判断是否是主管或老板(xbongbong逻辑)
	 * @return
	 */
	public boolean isLeaderOrBoss(UserEntity userEntity) {
		boolean isLeaderOrBoss = true;
		
		if(getDatePermission(userEntity).equals(1)){
			isLeaderOrBoss = false;
		}
		
		return isLeaderOrBoss;
	}
	
	/**
	 * 获取当前的用户[员工]信息，先取session,若为空则从cookie中取出当前用户的ID从数据库中查出
	 * 若最后返回的是null，即最后没有获得当前用户信息，请报错
	 * @param request
	 * @return
	 */
	public UserEntity getLoginUser(HttpServletRequest request ,HttpServletResponse response) {
		//HttpSession session = request.getSession();
		UserEntity curUser = (UserEntity) JedisUtils.getSessionValue(request,"curUserEntity");
		if(curUser == null) {
			Cookie curUserId = CookieUtil.getCookie(request, "curUserId");
			Cookie curCorpId = CookieUtil.getCookie(request, "curCorpId");
			if(curUserId == null || curCorpId == null) {
				return null;
			}
//			String userId = curUserId.getValue();
			String userId = URLEncodeUtils.decodeURL(curUserId.getValue());
			String corpid = curCorpId.getValue();
			curUser = getByKey(userId, corpid);
			JedisUtils.setSessionValue(request,response,"curUserEntity", curUser);
		}
		return curUser;
	}
	
	/**
	 * 获取下属ID（不包含自己）
	 * @param userEntity
	 * @return
	 */
	public List<String> getSubIdList(UserEntity userEntity){
		List<String> subIdList = new ArrayList<String>();
		
		if(userEntity.isBoss()){
			//老板，获取所有员工list
			if(userEntity.getCorpid() == null){
				return subIdList;
			}
			Map<String,Object> param = new HashMap<String,Object>();
			param.put("corpid", userEntity.getCorpid());
			param.put("del", 0);
			subIdList = getUserIds(param);
			subIdList.remove(userEntity.getUserId());
		}else{
			subIdList = getSubIdsByDataPermission(userEntity);
		}
		
		return subIdList;
	}
	
	/**
	 * 通过数据权限，获取管理的员工ID
	 * @param dataPermission 5：全公司 4：自定义 3：本部门和下属部门，2本部门 1：本人
	 * @param userEntity
	 * @return
	 */
	public List<String> getSubIdsByDataPermission(UserEntity userEntity){
		Integer dataPermission = getDatePermission(userEntity);
		String corpid = userEntity.getCorpid();
		List<String> userIds = new ArrayList<String>();
		if(dataPermission == null || dataPermission < 1){
			return userIds;
		}
		Map<String,Object> param = new HashMap<String,Object>();
		switch (dataPermission) {
		case 1:
			
			break;
		case 2:
//			List<Long> depIdIn = getUserBelongDeptIdList(userEntity);
			//拿到所有的根节点
			List<DepartmentEntity> depRootList = getRootDeps(userEntity);
			List<Long> depIdIn = new ArrayList<Long>();
			for(DepartmentEntity dep : depRootList){
				depIdIn.add(dep.getId());
			}
			userIds = getDepUserIdList(corpid, new HashSet(depIdIn));
			break;
		case 3:
			Set<Long> depIdSet = new HashSet<Long>();
			List<DepartmentEntity> departmentList = getManagementDepList(userEntity);
			for(DepartmentEntity departmentEntity : departmentList){
				depIdSet.add(departmentEntity.getId());
			}
			userIds = getDepUserIdList(corpid, depIdSet);
			break;
		case 4:
			Set<String> userIdSet = new HashSet<String>();
			String supUserId = userEntity.getUserId();
			param.put("supStaffId", supUserId);
			param.put("corpid", corpid);
			param.put("del", 0);
			List<WeakManageEntity> weakManageList = weakManageModel.findEntitys(param);
			for(WeakManageEntity weakManage : weakManageList){
				Integer type = weakManage.getType();
				if(type.equals(1)){
					userIdSet.add(weakManage.getSubStaffId());
				}else if(type.equals(2)){
					Long subDepId = weakManage.getSubDepId();
					List<String> tempUserIds = getDepUserIdList(corpid, subDepId);
					userIdSet.addAll(tempUserIds);
				}
			}
			userIds = new ArrayList<String>(userIdSet);
			break;
		case 5:
			
			param.put("corpid", corpid);
			param.put("del", 0);
			userIds = getUserIds(param);
			break;
		default:
			break;
		}
		userIds.remove(userEntity.getUserId());
		List<String> retList = new ArrayList<String>(new HashSet<String>(userIds));
		return retList;
	}
	
	/**
	 * 获取datePermission
	 * @param userEntity
	 * @return 5：全公司 4：自定义 3：本部门和下属部门，2本部门 1：本人
	 */
	public Integer getDatePermission(UserEntity userEntity){
		String rolesStr = userEntity.getRoleIds();
		String[] roleArray = rolesStr.split("\\|");
		List<Integer> roleIdIn = new ArrayList<Integer>();
		for(String roleStr : roleArray){
			if(!StringUtil.isEmpty(roleStr)){
				roleIdIn.add(StringUtil.toInt(roleStr, 0));
			}
		}
		if(roleIdIn.size() == 0){
			roleIdIn.add(-1);
		}
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("idIn", roleIdIn);
		param.put("isUse", 1);
		List<RoleEntity> roleList = roleModel.findEntitys(param);
		Integer datePermission = 1;
		for(RoleEntity role : roleList){
			if(role.getDataPermission() > datePermission){//取最大datePermission
				datePermission = role.getDataPermission();
			}
		}
		return datePermission;
	}
	
	/**
	 * 查询部门内的员工id列表
	 * @param corpid
	 * @param depId
	 * @return
	 */
	public List<String> getDepUserIdList(String corpid, Set<Long> depIdIn){
		List<String> depUserIdList = new ArrayList<String>();
		if (depIdIn.size() <= 0) {
			return depUserIdList;
		}
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("delJoin", 0);
		param.put("corpidJoin", corpid);
		param.put("depIdIn", depIdIn);
		//连表查询
		depUserIdList = getUserIdsJoin(param);
		
		return depUserIdList;
	}
	
	/**
	 * 获取userEntity管理的所有部门（不包含隐藏部门）
	 * @param userEntity
	 * @return
	 */
	List<DepartmentEntity> getManagementDepList(UserEntity userEntity){
		//管理的所有部门
		List<DepartmentEntity> managementDepList = new ArrayList<DepartmentEntity>();
		//拿到所有的根节点
		List<DepartmentEntity> depRootList = getRootDeps(userEntity);
		
		//公司所有的部门
		List<DepartmentEntity> allDepList = departmentModel.getAllDepList(userEntity.getCorpid());
		
		Map<Long,List<DepartmentEntity>> supSubListDepMap = getSupSubListMap(allDepList);
		
		//已访问的节点
		Set<DepartmentEntity> depSet = new HashSet<DepartmentEntity>();
		for(DepartmentEntity depRoot : depRootList) {
			//若节点已访问，则返回
			if(depSet.contains(depRoot)) {
				continue;
			}
			//用于递归的队列
			Queue<DepartmentEntity> accessDep = new LinkedList<DepartmentEntity>();
			accessDep.add(depRoot);
			while(accessDep.size() > 0) {
				DepartmentEntity dep = accessDep.poll();
				//若节点已访问，则返回
				if(depSet.contains(dep)) {
					continue ;
				}
			  
				depSet.add(dep);
				//部门隐藏为false 本部门的员工仅可见员工自己false
				if(dep.getDeptHiding().equals(1) && !ableViewHidingDept(userEntity, dep)) { 
					continue ;
				}
				
				managementDepList.add(dep);
			  
				List<DepartmentEntity> subList = supSubListDepMap.get(dep.getId());
			  
				if(subList != null && subList.size() > 0) {
					accessDep.addAll(subList);
				}
			  
			}
		}
		return managementDepList;
	}
//	private boolean ableViewDep(UserEntity userEntity, DepartmentEntity dep) {
//		if(!dep.getDeptHiding().equals(1)){
//			return true;
//		}
//		return false;
//	}

	/**
	 * 获得一个上级下级关系的Map
	 * @param allDepList
	 * @return
	 */
	private Map<Long, List<DepartmentEntity>> getSupSubListMap(
			List<DepartmentEntity> allDepList) {
		Map<Long, DepartmentEntity> allDepMap = new HashMap<Long, DepartmentEntity>();
		for(DepartmentEntity dep : allDepList){
			allDepMap.put(dep.getId(), dep);
		}
		
		Map<Long, List<DepartmentEntity>> supSubListDepMap = new HashMap<Long, List<DepartmentEntity>>();
		for(DepartmentEntity dep : allDepList){
			DepartmentEntity keyEntity = allDepMap.get(dep.getParentId());//父级
			Long key = null;
			List<DepartmentEntity> value = null;
			if(keyEntity == null){//跟节点
				continue;
			}
			
			key = keyEntity.getId();
			if(supSubListDepMap.containsKey(key)){
				value = supSubListDepMap.get(key);
			}
			if(value == null){
				value = new ArrayList<DepartmentEntity>();
			}
			value.add(dep);
			supSubListDepMap.put(key, value);
		}
		return supSubListDepMap;
	}

	List<DepartmentEntity> getRootDeps(UserEntity userEntity) {
		if (StringUtil.isEmpty(userEntity.getIsLeaderInDepts())) {
			return new ArrayList<DepartmentEntity>();
		}
		List<Long> depIdList = new ArrayList<Long>();
		//遍历isLeaderInDepts  在对应的部门中是否为主管, Map结构的json字符串, key是部门的Id, value是人员在这个部门中是否为主管, true表示是, false表示不是
		Map<Long,Boolean> isLeaderInDeptsObject = JSON.parseObject(userEntity.getIsLeaderInDepts(),  new TypeReference<Map<Long,Boolean>>(){});
		
		for(Entry<Long,Boolean> entry: isLeaderInDeptsObject.entrySet()){
			Long id = entry.getKey();
			boolean isLeader = (Boolean) entry.getValue();
			if(isLeader){
				depIdList.add(id);			
			}
		}
		if(depIdList.size() == 0){
			depIdList.add(-1L);
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("corpid", userEntity.getCorpid());
		param.put("idIn", depIdList);
		param.put("del", 0);
		List<DepartmentEntity> rootDeps = departmentModel.findEntitys(param);
		return rootDeps;
	}
	
	/**
	 * @param corpid 企业id
	 * @param depList 
	 * @return
	 */
	List<UserEntity> getDepUserList(String corpid, List<DepartmentEntity> depList){
		
		if (depList == null || depList.size() <= 0) {
			return null;
		}
		List<Long> depIdIn = new ArrayList<Long>();
		for(DepartmentEntity entity : depList){
			depIdIn.add(entity.getId());
		}
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("delJoin", 0);
		param.put("corpidJoin", corpid);
		param.put("depIdIn", depIdIn);
		//连表查询
		List<UserEntity> depUserList = findEntitysJoin(param);
		
		return depUserList;
	}
	
	/**获取部门DepList中的用户ID列表
	 * @param corpid 企业id
	 * @param depList 
	 * @return
	 */
	public List<String> getDepUserIdList(String corpid, List<DepartmentEntity> depList){
		
		if (depList == null || depList.size() <= 0) {
			return new ArrayList<String>();
		}
		List<Long> depIdIn = new ArrayList<Long>();
		for(DepartmentEntity entity : depList){
			depIdIn.add(entity.getId());
		}
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("delJoin", 0);
		param.put("corpidJoin", corpid);
		param.put("depIdIn", depIdIn);
		//连表查询
		List<String> depUserIdList = getUserIdsJoin(param);
		
		return depUserIdList;
	}
	
	/**
	 * 查询某一部门内的员工id列表
	 * @param corpid
	 * @param depId
	 * @return
	 */
	public List<String> getDepUserIdList(String corpid, Long depId){
		if (depId <= 0) {
			return null;
		}
		List<Long> depIdIn = new ArrayList<Long>();
		depIdIn.add(depId);
		
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("delJoin", 0);
		param.put("corpidJoin", corpid);
		param.put("depIdIn", depIdIn);
		//连表查询
		List<String> depUserIdList = getUserIdsJoin(param);
		
		return depUserIdList;
	}
	
	/**
	 * 判断员工是否有查看该隐藏部门的权限
	 * @param userEntity
	 * @param departmentEntity
	 * @return
	 */
	boolean ableViewHidingDept(UserEntity userEntity, DepartmentEntity hideDept){
		if (userEntity == null || hideDept == null) {
			return false;
		}
		String userId = userEntity.getUserId();
		String belongdeptStr = userEntity.getDepartment();//所属部门列表
		
		
		String userPerimits = hideDept.getUserPerimits();//可以查看指定隐藏部门的其他人员列表，如果部门隐藏，则此值生效，取值为其他的人员userid组成的的字符串，使用’ | '符号进行分割
		
		if (!StringUtil.isEmpty(userPerimits)) {				
			List<String> userPerimitsList = Arrays.asList(userPerimits.split("\\|"));
			
			if (userPerimitsList.contains(userId)) {
				return true;
			}
		}
		
		String deptPerimits = hideDept.getDeptPerimits();//可以查看指定隐藏部门的其他部门列表，如果部门隐藏，则此值生效，取值为其他的部门id组成的的字符串，使用’ | ‘符号进行分割
		if (!StringUtil.isEmpty(belongdeptStr) && !StringUtil.isEmpty(deptPerimits)) {
			//员工所属部门
			List<Long> belongdeptList = JSONArray.parseArray(belongdeptStr, Long.class);
			
			//隐藏部门里可以查看的部门列表
			String[] deptPerimitArr = deptPerimits.split("\\|");
			List<Long> deptPerimitList = new ArrayList<Long>();
			for (String deptIdStr : deptPerimitArr) {
				deptPerimitList.add(Long.valueOf(deptIdStr));
			}
			
			//看是否有交集
			deptPerimitList.retainAll(belongdeptList);
			
			if (deptPerimitList.size() > 0) {//有交集则说明可查看的部门里包含该员工所在部门
				return true;
			}
			
		}
		
		return false;
	}

	/**
	 * 获取对userEntity开放的隐藏部门的所有成员
	 * @param userEntity
	 * @return
	 */
	List<UserEntity> getOuterUserList(UserEntity userEntity){
		if (userEntity == null) {
			return null;
		}
		String corpid = userEntity.getCorpid();
		String userId = userEntity.getUserId();
		String belongdeptStr = userEntity.getDepartment();//所属部门列表
		
//		List<DepartmentEntity> viewHidingDepList = getViewHidingDepList(userEntity);//该用户可以查看的隐藏部门
		
		
		//获取所有隐藏的部门  deptHiding 为true
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("del", 0);
		param.put("deptHiding", 1);//true表示隐藏, false表示显示
		
		List<DepartmentEntity> hideDepList = departmentModel.findEntitys(param);
		
		List<DepartmentEntity> viewHidingDepList = new ArrayList<DepartmentEntity>();//该用户可以查看的隐藏部门
		
		for (DepartmentEntity departmentEntity : hideDepList) {
			String userPerimits = departmentEntity.getUserPerimits();//可以查看指定隐藏部门的其他人员列表，如果部门隐藏，则此值生效，取值为其他的人员userid组成的的字符串，使用’ | '符号进行分割
			String deptPerimits = departmentEntity.getDeptPerimits();//可以查看指定隐藏部门的其他部门列表，如果部门隐藏，则此值生效，取值为其他的部门id组成的的字符串，使用’ | ‘符号进行分割
			
			if (!StringUtil.isEmpty(userPerimits)) {				
				List<String> userPerimitsList = Arrays.asList(userPerimits.split("\\|"));
				
				if (userPerimitsList.contains(userId)) {
					viewHidingDepList.add(departmentEntity);
					
					continue;//该用户可以看到该部门 跳出即可
				}
			}
			
			if (!StringUtil.isEmpty(belongdeptStr) && !StringUtil.isEmpty(deptPerimits)) {
				String[] deptPerimitArr = deptPerimits.split("\\|");//可以查看的部门
				List<Long> deptPerimitList = new ArrayList<Long>();
				for (String deptIdStr : deptPerimitArr) {
					deptPerimitList.add(Long.valueOf(deptIdStr));
				}
				
				JSONArray belongdeptArray = JSONArray.parseArray(belongdeptStr);
				@SuppressWarnings("unchecked")
				List<Long> belongdeptList = JSONArray.toJavaObject(belongdeptArray, List.class);
				
				deptPerimitList.contains(belongdeptList);
				
				if (deptPerimitList.size() > 0) {
					viewHidingDepList.add(departmentEntity);
				}
				
			}
			
		}
		
		if (viewHidingDepList.size() > 0) {
			return getDepUserList(corpid, viewHidingDepList);
		}
		
		return null;
	}

	//部门员工业绩方法
	public Map<String,Object> getMapByUser(HttpServletRequest request,HttpServletResponse response) throws Exception{
//		// 登录验证
//		if (!checkToken(request,response)) return null;
//		// 获取json对象
//		JSONObject dataJson =  validateData(request, response);
//		if(dataJson.size() == 0) return null;

		//mycat路由字段
		UserEntity userEntity = getLoginUser(request,response);
		
		String userId = (userEntity == null)? "1" : userEntity.getUserId();//用户ID
		String corpid = (userEntity == null)? "1" : userEntity.getCorpid();//公司ID
		
		userEntity = (userEntity == null)? getByKey(userId, corpid) : userEntity;
		Integer isBoss = userEntity.getIsBoss();
		
		String data = request.getParameter("data");
		JSONObject dataJson = JSONObject.parseObject(data);
		
//		Integer appParam = dataJson.getInteger("appParam") == null ? 1 : dataJson.getInteger("appParam");  //来源 1：来自app
		Integer firstLoadPage = dataJson.getInteger("firstLoadPage");   //是否是首次加载页面
		Integer num = (dataJson.getInteger("num") == null || dataJson.getInteger("num") < 1) ? 5 : dataJson.getInteger("num");
//		String userId = dataJson.getString("userId");
		Long departmentId = dataJson.getLong("departmentId") == null ? 0 : dataJson.getLong("departmentId");
		
		String depNameStr = dataJson.getString("depName");		
		String depName = "";
		if(!StringUtil.isEmpty(depNameStr)){
			depName = new String(depNameStr.getBytes("ISO8859-1"),"UTF-8");
		}
		
		//获取companyId
//		UserEntity userEntity = userModel.getByKey(userId, companyId);
//		if(userEntity == null){
//		   return null;
//		}
		
		boolean bossToken = false;
		boolean depToken = false;
		if(departmentId != null && departmentId > 0){   
			depToken = true;
		}
		
		Map<String,Object> param = new HashMap<String,Object>();
		CommentUtil.addToMap(param, "corpid", corpid);
		CommentUtil.addToMap(param, "del", 0);

		List<String> userIdList = null;
		
		if(depToken){    //token == true说明是筛选
			
			userIdList = getDepUserIdList(corpid, departmentId);
			if(userIdList == null || userIdList.size() < 1) {
				userIdList.add("-1");    //-1是为了不让数据库报错
			}
			param.put("userIdIn", userIdList);
		}else{//没有筛选部门
			if (isBoss.equals(1)) {
				bossToken = true;
			}
			
			//下属条件
//			List<String> userIdIn = userModel.getSubIdList(userEntity);
//			//把自己加进去
//			userIdIn.add(userId);
//			param.put("userIdIn", userIdIn);
			
		}
//		if(!depToken){
//			CommentUtil.addToMap(param, "isDismission", 0);
//			userList = staffModel.findEntityIds(param);
//		}else{
//			staffList = getStaffIDListByDepId(departmentId, companyId);
//		}
		
		CommentUtil.addToMap(param, "num",num);
		CommentUtil.addToMap(param, "bossToken",bossToken);
		CommentUtil.addToMap(param, "userIdList", userIdList);
		CommentUtil.addToMap(param, "departmentId", departmentId); 
		param.put("depName", depName);
//		param.put("appParam", appParam);  
		param.put("firstLoadPage", firstLoadPage);  
		param.put("corpid", corpid);
		
		return param;
	}
	
	/**
	 * 更新微应用可见状态,单个公司所有用户
	 * @param corpid	公司ID
	 * @param isVisible 1可见[授权]，0不可见[不授权]
	 * @return 
	 * @author kaka
	 * @time 2016年7月18日 下午3:52:16
	 */
	public Integer updateAllVisibleByCorpId(String corpid, Integer isVisible) {
		return dao.updateAllVisibleByCorpId(corpid, isVisible);
	}
	
	/**
	 * 更新微应用可见状态 ,单个公司单个或多个用户
	 * @param corpid 公司ID
	 * @param isVisible 1可见[授权]，0不可见[不授权]
	 * @param userIdIn 需要修改的用户列表
	 * @return
	 * @author kaka
	 * @time 2016年7月18日 下午3:55:23
	 */
	public Integer updateVisibleByCorpIdAndUserIdIn(String corpid, Integer isVisible, List<String> userIdIn) {
		return dao.updateVisibleByCorpIdAndUserIdIn(corpid, isVisible, userIdIn);
	}
	
	
	/**
	 * 更新微应用不可见状态 ,单个公司单个或多个用户
	 * @param corpid 公司ID
	 * @param isVisible 1可见[授权]，0不可见[不授权]
	 * @param userIdNotIn 不可见的用户列表
	 * @return
	 * @author kaka
	 * @time 2016年7月18日 下午3:55:23
	 */
	public Integer updateVisibleByCorpIdAndUserIdNotIn(String corpid, Integer isVisible, List<String> userIdNotIn) {
		return dao.updateVisibleByCorpIdAndUserIdNotIn(corpid, isVisible, userIdNotIn);
	}
	
	/**
	 * 获得用户所属部门id列表
	 * @param userEntity
	 * @return
	 */
	public List<Long> getUserBelongDeptIdList(UserEntity userEntity) {
		
		String belongdeptStr = userEntity.getDepartment();//所属部门列表
		List<Long> belongdeptList = null;
		if (!StringUtil.isEmpty(belongdeptStr)) {
			//员工所属部门
			belongdeptList = JSONArray.parseArray(belongdeptStr, Long.class);
		}

//		if (belongdeptList == null || belongdeptList.size() <= 0) {
//			belongdeptList.add(-1l);
//		}
		return belongdeptList;
	}
	public static void main(String[] args) {
		String a="1746066|2429218|3820076|1746064|3813273|1746065|2471406|3818130|4379239|2912140|2830480|3499078|1930983|2914169|3402081|3814200|1746029|2913159|3812266|2907212";
		// a="1746066|2429218";
		String[] deptPerimitArr =  a.split("|");
		 
		//List<Long> deptPerimitList = new ArrayList<Long>();
		for (String deptIdStr : deptPerimitArr) {
			//deptPerimitList.add(Long.valueOf(deptIdStr));
		}
	}
}

 