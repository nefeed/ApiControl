 
package com.xbongbong.dingxbb.entity;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

 
public class RoleEntity implements Serializable{

	/** Comment for <code>serialVersionUID</code> */
	private static final long serialVersionUID = -1L;

	//========== properties ==========
	
	//主键
	private Integer id;
	//角色名，英文
	private String roleName;
	//角色别名，可以中文，用于显示
	private String roleAlias;
	//权限集合
	private String permissions;
	
	private Integer dataPermission = 1;
	//公司id
	private String corpid;
	//创建时间
	private Integer addTime;
	//更新时间
	private Integer updateTime;
	//是否启用，1启用，0不启用
	private Integer isUse;
	private Integer del;
	
	
	//扩展字段
	private boolean used; // 根据manger的role找到对应的roleEntity，标记为true
	
	private List<PermissionEntity> permissionsList;

	
	public RoleEntity() {
		del = 0;
	}
    //========== getters and setters ==========
    
	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}

	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	public String getRoleName() {
		return roleName;
	}
	
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getRoleAlias() {
		return roleAlias;
	}
	
	public void setRoleAlias(String roleAlias) {
		this.roleAlias = roleAlias;
	}
	public String getPermissions() {
		return permissions;
	}
	
	public void setPermissions(String permissions) {
		this.permissions = permissions;
	}

	public Integer getDataPermission() {
		return dataPermission;
	}

	public void setDataPermission(Integer dataPermission) {
		this.dataPermission = dataPermission;
	}

	public String getCorpid() {
		return corpid;
	}

	public void setCorpid(String corpid) {
		this.corpid = corpid;
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
	public Integer getIsUse() {
		return isUse;
	}
	
	public void setIsUse(Integer isUse) {
		this.isUse = isUse;
	}

	public List<PermissionEntity> getPermissionsList() {
		return permissionsList;
	}

	public void setPermissionsList(List<PermissionEntity> permissionsList) {
		this.permissionsList = permissionsList;
	}

	public Integer getDel() {
		return del;
	}

	public void setDel(Integer del) {
		this.del = del;
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

