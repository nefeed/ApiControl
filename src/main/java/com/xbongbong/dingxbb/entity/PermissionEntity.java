 
package com.xbongbong.dingxbb.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

 
public class PermissionEntity implements Serializable{

	/** Comment for <code>serialVersionUID</code> */
	private static final long serialVersionUID = -1L;

	//========== properties ==========
	
	//主键
	private Integer id;
	//权限名称
	private String name;
	//权限别名。用于显示
	private String alias;
	//父权限ID
	private Integer parentId;
	//是否是菜单
	private Integer isMenu;
	//菜单排序
	private Integer sort;
	//权限，用于shiro
	private String permission;
	//权限对应Url
	private String aimUrl;
	//权限类型，1 API, 2管理后台
	private Integer type;
	//权限路由[菜单路由]
	private String router;
	//图标
	private String icon;
	//创建时间
	private Integer addTime;
	//更新时间
	private Integer updateTime;
	//是否启用，1启用，2不启用
	private Integer isUse;
    //是否管理下属(如修改下属合同)；0：不管理；1：管理
	private Integer manageSub=0;
	private Integer companyId;

	//vip 0:免费功能;1:vip功能
	private Integer vip;
	
	//扩展字段
	
	/**
	 * 是否被使用
	 */
	private boolean isUsed = false;
	

	/**
	 * 子菜单
	 */
	private List<PermissionEntity> childMenuEntityList;
	
	/**
	 * 是否有子权限
	 */
	private boolean hasChildPermission = false;
	
	/**
	 * 子权限
	 */
	private List<PermissionEntity> childPermissionEntityList;
	
    //========== getters and setters ==========
	
	public PermissionEntity() {
		childMenuEntityList = new ArrayList<PermissionEntity>();
//		childPermissionEntityList = new ArrayList<PermissionEntity>();
		
	}
    
	public List<PermissionEntity> getChildMenuEntityList() {
		return childMenuEntityList;
	}

	public void setChildMenuEntityList(PermissionEntity permissionEntity) {
		if(permissionEntity.getIsMenu() == 1){
			this.childMenuEntityList.add(permissionEntity);
		}
	}

	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getAlias() {
		return alias;
	}
	
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public Integer getParentId() {
		return parentId;
	}
	
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public Integer getIsMenu() {
		return isMenu;
	}
	
	public void setIsMenu(Integer isMenu) {
		this.isMenu = isMenu;
	}
	public Integer getSort() {
		return sort;
	}
	
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public String getPermission() {
		return permission;
	}
	
	public void setPermission(String permission) {
		this.permission = permission;
	}
	public String getAimUrl() {
		return aimUrl;
	}
	
	public void setAimUrl(String aimUrl) {
		this.aimUrl = aimUrl;
	}

	public Integer getType() {
		return type;
	}
	
	public void setType(Integer type) {
		this.type = type;
	}
	public String getRouter() {
		return router;
	}
	
	public void setRouter(String router) {
		this.router = router;
	}
	public String getIcon() {
		return icon;
	}
	
	public void setIcon(String icon) {
		this.icon = icon;
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
	

	public boolean getIsUsed() {
		return isUsed;
	}

	public void setIsUsed(boolean isUsed) {
		this.isUsed = isUsed;
	}

	public boolean isHasChildPermission() {
		return hasChildPermission;
	}

	public void setHasChildPermission(boolean hasChildPermission) {
		this.hasChildPermission = hasChildPermission;
	}
	
	public List<PermissionEntity> getChildPermissionEntityList() {
		return childPermissionEntityList;
	}
	public void setChildPermissionEntityList(List<PermissionEntity> childPermissionEntityList) {
		this.childPermissionEntityList = childPermissionEntityList;
	}
	public void setChildPermissionEntityList(PermissionEntity permissionEntity) {
		this.childPermissionEntityList.add(permissionEntity);
	}

	public Integer getManageSub() {
		return manageSub;
	}

	public void setManageSub(Integer manageSub) {
		this.manageSub = manageSub;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	
	

	public Integer getVip() {
		return vip;
	}

	public void setVip(Integer vip) {
		this.vip = vip;
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

