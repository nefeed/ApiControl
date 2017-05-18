 
package com.xbongbong.dingxbb.entity;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.alibaba.fastjson.JSONObject;
import com.xbongbong.util.DateUtil;

 
public class DepartmentEntity implements Serializable{

	/** Comment for <code>serialVersionUID</code> */
	private static final long serialVersionUID = -1L;

	//========== properties ==========
	
	//公司ID
	private String corpid;
	//部门id
	private Long id;
	//部门名称
	private String name;
	//父部门id，根部门为1
	private Long parentId;
	//部门层级，根部门为1
	private Integer level;
	//对应钉钉的order,在父部门中的次序值
	private Integer sort;
	//是否隐藏部门, true表示隐藏, false表示显示
	private Integer deptHiding;
	//可以查看指定隐藏部门的其他部门列表，如果部门隐藏，则此值生效，取值为其他的部门id组成的的字符串，使用’ | ‘符号进行分割
	private String deptPerimits;
	//可以查看指定隐藏部门的其他人员列表，如果部门隐藏，则此值生效，取值为其他的人员userid组成的的字符串，使用’ | '符号进行分割
	private String userPerimits;
	//是否本部门的员工仅可见员工自己, 为true时，本部门员工默认只能看到员工自己
	private Integer outerDept;
	//本部门的员工仅可见员工自己为true时，可以配置额外可见部门，值为部门id组成的的字符串，使用’ | '符号进行分割
	private String outerPermitDepts;
	//本部门的员工仅可见员工自己为true时，可以配置额外可见人员，值为userid组成的的字符串，使用’ | '符号进行分割
	private String outerPermitUsers;
	//部门的主管列表,取值为由主管的userid组成的字符串，不同的userid使用’ | '符号进行分割
	private String deptManagerUseridList;
	//企业群群主
	private String orgDeptOwner;
	//是否同步创建一个关联此部门的企业群, 1表示是, 0表示不是
	private Integer createDeptGroup;
	//当群已经创建后，是否有新人加入部门会自动加入该群, 1表示是, 0表示不是
	private Integer autoAddUser;
	//添加时间
	private Integer addTime;
	//更新时间
	private Integer updateTime;
	//删除标记
	private Integer del;
	
	//部门ID路由
	private String depIdRouter;

	public DepartmentEntity() {
		int now = DateUtil.getInt();
		
		this.addTime = now;
		this.updateTime = now;
		this.del = 0;
		this.level = -1;//level 字段暂时不用
	}
	
    //========== getters and setters ==========
    
	public String getCorpid() {
		return corpid;
	}
	
	public void setCorpid(String corpid) {
		this.corpid = corpid;
	}
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public Long getParentId() {
		return parentId;
	}
	
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public Integer getLevel() {
		return level;
	}
	
	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getSort() {
		return sort;
	}
	
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public Integer getDeptHiding() {
		return deptHiding;
	}
	
	public void setDeptHiding(Integer deptHiding) {
		this.deptHiding = deptHiding;
	}
	public String getDeptPerimits() {
		return deptPerimits;
	}
	
	public void setDeptPerimits(String deptPerimits) {
		this.deptPerimits = deptPerimits;
	}
	public String getUserPerimits() {
		return userPerimits;
	}
	
	public void setUserPerimits(String userPerimits) {
		this.userPerimits = userPerimits;
	}
	public Integer getOuterDept() {
		return outerDept;
	}
	
	public void setOuterDept(Integer outerDept) {
		this.outerDept = outerDept;
	}
	public String getOuterPermitDepts() {
		return outerPermitDepts;
	}
	
	public void setOuterPermitDepts(String outerPermitDepts) {
		this.outerPermitDepts = outerPermitDepts;
	}
	public String getOuterPermitUsers() {
		return outerPermitUsers;
	}
	
	public void setOuterPermitUsers(String outerPermitUsers) {
		this.outerPermitUsers = outerPermitUsers;
	}
	public String getDeptManagerUseridList() {
		return deptManagerUseridList;
	}
	
	public void setDeptManagerUseridList(String deptManagerUseridList) {
		this.deptManagerUseridList = deptManagerUseridList;
	}
	public String getOrgDeptOwner() {
		return orgDeptOwner;
	}
	
	public void setOrgDeptOwner(String orgDeptOwner) {
		this.orgDeptOwner = orgDeptOwner;
	}
	public Integer getCreateDeptGroup() {
		return createDeptGroup;
	}
	
	public void setCreateDeptGroup(Integer createDeptGroup) {
		this.createDeptGroup = createDeptGroup;
	}
	public Integer getAutoAddUser() {
		return autoAddUser;
	}
	
	public void setAutoAddUser(Integer autoAddUser) {
		this.autoAddUser = autoAddUser;
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

	public String getDepIdRouter() {
		return depIdRouter;
	}

	public void setDepIdRouter(String depIdRouter) {
		this.depIdRouter = depIdRouter;
	}

	public void formatDepartmentData(JSONObject depJson) {
		this.id = depJson.getLong("id");
		this.name = depJson.getString("name");
		this.sort = depJson.getIntValue("order");
		this.parentId = depJson.getLongValue("parentid");
		this.createDeptGroup = "true".equals(depJson.getString("createDeptGroup")) ? 1 : 0;
		this.autoAddUser = "true".equals(depJson.getString("autoAddUser")) ? 1 : 0;
		this.deptHiding = "true".equals(depJson.getString("deptHiding")) ? 1 : 0;
		this.deptPerimits = depJson.getString("deptPerimits");
		this.userPerimits = depJson.getString("userPerimits");
		this.outerDept = "true".equals(depJson.getString("outerDept")) ? 1 : 0;
		this.outerPermitDepts = depJson.getString("outerPermitDepts");
		this.outerPermitUsers = depJson.getString("outerPermitUsers");
		this.orgDeptOwner = depJson.getString("orgDeptOwner") != null ? depJson.getString("orgDeptOwner") : "";
		this.deptManagerUseridList = depJson.getString("deptManagerUseridList") != null ? depJson.getString("deptManagerUseridList") : "";

		//更新修改时间
		this.updateTime = DateUtil.getInt();
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

