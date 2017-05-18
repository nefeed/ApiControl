 
package com.xbongbong.dingxbb.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.xbongbong.dingxbb.enums.RoleEnum;
import com.xbongbong.util.DateUtil;
import com.xbongbong.util.StringUtil;

 
public class UserEntity implements Serializable{

	/** Comment for <code>serialVersionUID</code> */
	private static final long serialVersionUID = -1L;

	//========== properties ==========
	//优化innodb B+树，主键
	private Integer id;
	//公司ID
	private String corpid;
	//员工唯一标识ID（不可修改）
	private String userId;
	//成员名称
	private String name;
	//是否已经激活, 1表示已激活, 0表示未激活
	private Integer active;
	//是否为企业的管理员, 1表示是, 0表示不是
	private Integer isAdmin;
	//是否为企业的老板, 1表示是, 0表示不是
	private Integer isBoss;
	//钉钉Id
	private String dingId;
	//职位信息
	private String position;
	//头像url
	private String avatar;
	//员工工号
	private String jobnumber;
	//成员所属部门id列表
	private String department;
	//在对应的部门中的排序, Map结构的json字符串, key是部门的Id, value是人员在这个部门的排序值."orderInDepts" : "{1:10, 2:20}",
	private String orderInDepts;
	//在对应的部门中是否为主管, Map结构的json字符串, key是部门的Id, value是人员在这个部门中是否为主管, true表示是, false表示不是."{1:true, 2:false}"
	private String isLeaderInDepts;
	//扩展属性，可以设置多种属性(但手机上最多只能显示10个扩展属性，具体显示哪些属性，请到OA管理后台->设置->通讯录信息设置和OA管理后台->设置->手机端显示信息设置)性
	private String extattr;
	//添加时间
	private Integer addTime;
	//更新时间
	private Integer updateTime;
	//删除标记
	private Integer del;
	//新增员工角色字段
	private String roleIds;
	
	//是否接受推送消息,1推送，0不推送
	private Integer isPushMessage;
	
	//是否被授权使用销帮帮
	private Integer isVisible = 1;
	
	//扩展字段
	//是否主负责人
	private Boolean isMain;
	
	public UserEntity() {
		int now = DateUtil.getInt();

		this.addTime = now;
		this.updateTime = now;
		this.del = 0;
	}
	
	/**
	 * 将userObject中的数据设置到User实体中
	 * @param userObject	包含用户信息的JSONObject
	 * @param depId			若depId不为null,则表明是通过部门员工详情接口获取的员工详情,为null则表示是用户详情接口
	 */
	public void formatUserData(JSONObject userObject, Long depId, boolean insertUser) {
		
		this.userId = userObject.getString("userid");
		this.name = userObject.getString("name");
		this.active = "true".equals(userObject.getString("active")) ? 1 : 0;
		this.isAdmin = "true".equals(userObject.getString("isAdmin")) ? 1 : 0;
		this.isBoss = "true".equals(userObject.getString("isBoss")) ? 1 : 0;
		this.dingId = userObject.getString("dingId");
		this.position = userObject.getString("position") != null ? userObject.getString("position") : "";
		this.avatar = userObject.getString("avatar") != null ? userObject.getString("avatar") : "";
		this.jobnumber = userObject.getString("jobnumber") != null ? userObject.getString("jobnumber") : "";
		this.extattr = userObject.getString("extattr") != null ? userObject.getString("extattr") : "";
		
		//更新修改时间
		int now = DateUtil.getInt();
		this.updateTime = now;
		
		if(depId == null) {
			this.orderInDepts = userObject.getString("orderInDepts");
			this.isLeaderInDepts = userObject.getString("isLeaderInDepts");
			this.department = userObject.getString("department");
		} else {
			this.department = userObject.getString("department");
			boolean isLeader = userObject.getBooleanValue("isLeader");
			Long order = userObject.getLong("order");
			
			Map<Long, Long> orderInDeptsJson = new HashMap<Long, Long>();
			orderInDeptsJson.put(depId, order);
			this.orderInDepts  = JSON.toJSONString(orderInDeptsJson);
			Map<Long, Boolean> isLeaderInDeptsJson = new HashMap<Long, Boolean>();
			isLeaderInDeptsJson.put(depId, isLeader);
			this.isLeaderInDepts = JSON.toJSONString(isLeaderInDeptsJson);
		}
		
		if(insertUser) {//插入时设置角色，更新时只有在用户没有isBoss权限或没有isAdmin权限的时候进行增加权限
			//设置角色
			if(this.isBoss.equals(1)) {
				//老板权限
				this.setRoleIds(RoleEnum.BOSS.getAlias());
			} else {
				
				if(isLeader()) {//主管
					this.setRoleIds(RoleEnum.MANAGER.getAlias());
				} else {	  //普通销售
					this.setRoleIds(RoleEnum.SALE.getAlias());
				}
			}
			
			//管理员权限
			if(this.isAdmin.equals(1)) {
				String fulRoleIds = this.getRoleIds() + RoleEnum.ADMIN.getCode() + "|";
				this.setRoleIds(fulRoleIds);
			}
		} else {
			String roleIdsTmp = this.getRoleIds();
//			//老板权限
//			if(this.isBoss.equals(1)) {
//				if(StringUtil.isEmpty(roleIdsTmp)) {
//					roleIdsTmp =  RoleEnum.BOSS.getAlias();
//				} else if(roleIdsTmp.indexOf(RoleEnum.BOSS.getAlias()) == -1) { //没找到就添加相应权限
//					roleIdsTmp = roleIdsTmp + RoleEnum.BOSS.getCode() + "|";
//				}
//			}
//			//管理员权限
//			if(this.isAdmin.equals(1)) {
//				if(StringUtil.isEmpty(roleIdsTmp)) {
//					roleIdsTmp =  RoleEnum.ADMIN.getAlias();
//				} else if(roleIdsTmp.indexOf(RoleEnum.ADMIN.getAlias()) == -1) { //没找到就添加相应权限
//					roleIdsTmp = roleIdsTmp + RoleEnum.ADMIN.getCode() + "|";
//				}
//			}
			this.setRoleIds(roleIdsTmp);
		}
	}
	
	/**
	 * 判断是否是主管(钉钉逻辑)
	 * @return
	 */
	public boolean isLeader() {
		if (StringUtil.isEmpty(this.isLeaderInDepts)) {
			return false;
		}
		Map<Long, Boolean> isLeaderMap = JSON.parseObject(this.isLeaderInDepts,  new TypeReference<Map<Long,Boolean>>(){});
		boolean isLeader = false;
		for(Map.Entry<Long, Boolean> isLeaderEntry : isLeaderMap.entrySet()) {
			//如果是某个部门主管就跳出
			if(isLeaderEntry.getValue()) {
				isLeader = true;
				break;
			}
		}
		return isLeader;
	}
	
	
    //========== getters and setters ==========
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getCorpid() {
		return corpid;
	}
	
	public void setCorpid(String corpid) {
		this.corpid = corpid;
	}
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public Integer getActive() {
		return active;
	}
	
	public void setActive(Integer active) {
		this.active = active;
	}
	public Integer getIsAdmin() {
		String roles = this.roleIds;
		
		if((!StringUtil.isEmpty(roles) && roles.indexOf(RoleEnum.ADMIN.getAlias()) > -1) || this.isAdmin.equals(1)){
			return 1;
		}
		
		return 0;
	}
	
	public void setIsAdmin(Integer isAdmin) {
		this.isAdmin = isAdmin;
	}
	public Integer getIsBoss() {
		String roles = this.roleIds;
		
		if((!StringUtil.isEmpty(roles) && roles.indexOf(RoleEnum.BOSS.getAlias()) > -1) || this.isBoss.equals(1)){
			return 1;
		}
		
		return 0;
	}
	
	public void setIsBoss(Integer isBoss) {
		this.isBoss = isBoss;
	}
	public String getDingId() {
		return dingId;
	}
	
	public void setDingId(String dingId) {
		this.dingId = dingId;
	}
	public String getPosition() {
		return position;
	}
	
	public void setPosition(String position) {
		this.position = position;
	}
	public String getAvatar() {
		if(StringUtil.isEmpty(this.avatar)){
			return "/images/default-avatar.jpg";
		}
		return avatar;
	}
	
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getJobnumber() {
		return jobnumber;
	}
	
	public void setJobnumber(String jobnumber) {
		this.jobnumber = jobnumber;
	}
	public String getDepartment() {
		return department;
	}
	
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getOrderInDepts() {
		return orderInDepts;
	}
	
	public void setOrderInDepts(String orderInDepts) {
		this.orderInDepts = orderInDepts;
	}
	public String getIsLeaderInDepts() {
		return isLeaderInDepts;
	}
	
	public void setIsLeaderInDepts(String isLeaderInDepts) {
		this.isLeaderInDepts = isLeaderInDepts;
	}
	public String getExtattr() {
		return extattr;
	}
	
	public void setExtattr(String extattr) {
		this.extattr = extattr;
	}
	public Integer getAddTime() {
		return addTime;
	}
	
	public void setAddTime(Integer addTime) {
		this.addTime = addTime;
	}
	public Integer getUpdateTime() {
		return updateTime;
	}
	
	public void setUpdateTime(Integer updateTime) {
		this.updateTime = updateTime;
	}
	public Integer getDel() {
		return del;
	}
	
	public void setDel(Integer del) {
		this.del = del;
	}

	public String getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}

	public Boolean getIsMain() {
		return isMain;
	}


	public void setIsMain(Boolean isMain) {
		this.isMain = isMain;
	}
	
	
	public Integer getIsPushMessage() {
		return isPushMessage;
	}

	public void setIsPushMessage(Integer isPushMessage) {
		this.isPushMessage = isPushMessage;
	}
	
	/**
	 * @return the isVisible
	 */
	public Integer getIsVisible() {
		return isVisible;
	}

	/**
	 * @param isVisible the isVisible to set
	 */
	public void setIsVisible(Integer isVisible) {
		this.isVisible = isVisible;
	}

	/**
	 * 判断是否是老板(有boss的role或isBoss==1)
	 * @return
	 */
	public boolean isBoss() {
		String roles = this.roleIds;
		
		if((!StringUtil.isEmpty(roles) && roles.indexOf(RoleEnum.BOSS.getAlias()) > -1) || this.isBoss.equals(1)){
			return true;
		}
		return false;
	}
	
	/**
	* 重载toString方法
	* @return
	*
	* @see java.lang.Object#toString()
	*/
	@Override
    public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}

