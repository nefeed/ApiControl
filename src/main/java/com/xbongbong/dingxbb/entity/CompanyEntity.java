 
package com.xbongbong.dingxbb.entity;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.alibaba.fastjson.JSONObject;
import com.xbongbong.util.DateUtil;

 
public class CompanyEntity implements Serializable{

	/** Comment for <code>serialVersionUID</code> */
	private static final long serialVersionUID = -1L;

	//========== properties ==========
	
	//授权方企业id
	private String corpid;
	//授权方企业名称
	private String corpName;
	//企业授权状态 -1：解除授权 0：禁用 1：正常 2：待激活
	private Integer status;
	//表示企业所属行业
	private String industry;
	//企业是否认证
	private Integer isAuthenticated;
	//企业logo
	private String corpLogoUrl;
	//
	private String authUserId;
	//添加时间
	private Integer addTime;
	//更新时间
	private Integer updateTime;
	//删除标记
	private Integer del;
	//表示邀请码，只有填写过且是ISV自己邀请码的数据才会返回,否则值为空字符串
	private String inviteCode;
	//企业邀请链接
	private String inviteUrl;
	//序列号
	private String licenseCode;
	private String permanentCode;
	private	String agentId;
	//是否接受推送消息,1推送，0不推送
	private Integer isPushMessage;
	//渠道码
	private String authChannel;

	public CompanyEntity() {
		int now = DateUtil.getInt();
		this.addTime = now;
		this.updateTime = now;
		this.del = 0;
	}
    //========== getters and setters ==========
    
	public String getCorpid() {
		return corpid;
	}
	
	public void setCorpid(String corpid) {
		this.corpid = corpid;
	}
	public String getCorpName() {
		return corpName;
	}
	
	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}
	public Integer getStatus() {
		return status;
	}
	
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getIndustry() {
		return industry;
	}
	
	public void setIndustry(String industry) {
		this.industry = industry;
	}
	public Integer getIsAuthenticated() {
		return isAuthenticated;
	}
	
	public void setIsAuthenticated(Integer isAuthenticated) {
		this.isAuthenticated = isAuthenticated;
	}
	public String getCorpLogoUrl() {
		return corpLogoUrl;
	}
	
	public void setCorpLogoUrl(String corpLogoUrl) {
		this.corpLogoUrl = corpLogoUrl;
	}
	public String getAuthUserId() {
		return authUserId;
	}
	
	public void setAuthUserId(String authUserId) {
		this.authUserId = authUserId;
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
	public String getInviteCode() {
		return inviteCode;
	}
	
	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}
	public String getInviteUrl() {
		return inviteUrl;
	}
	
	public void setInviteUrl(String inviteUrl) {
		this.inviteUrl = inviteUrl;
	}
	public String getLicenseCode() {
		return licenseCode;
	}
	
	public void setLicenseCode(String licenseCode) {
		this.licenseCode = licenseCode;
	}

	public String getPermanentCode() {
		return permanentCode;
	}

	public void setPermanentCode(String permanentCode) {
		this.permanentCode = permanentCode;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public Integer getIsPushMessage() {
		return isPushMessage;
	}

	public void setIsPushMessage(Integer isPushMessage) {
		this.isPushMessage = isPushMessage;
	}

	public String getAuthChannel() {
		return authChannel;
	}

	public void setAuthChannel(String authChannel) {
		this.authChannel = authChannel;
	}

	
	public void formatCompanyData(JSONObject authInfo) {
		JSONObject authCorpInfo = authInfo.getJSONObject("auth_corp_info");
		this.corpid = authCorpInfo.getString("corpid");
		this.corpName = authCorpInfo.getString("corp_name");
		this.corpLogoUrl = authCorpInfo.getString("corp_logo_url");
		this.industry = authCorpInfo.getString("industry");
		this.inviteCode = authCorpInfo.getString("invite_code");
		this.licenseCode = authCorpInfo.getString("license_code");
		this.isAuthenticated = "true".equals(authCorpInfo.getString("is_authenticated")) ? 1 : 0;
		this.inviteUrl = authCorpInfo.getString("invite_url");
		//渠道码
		this.authChannel = authCorpInfo.getString("auth_channel");
		
		JSONObject authUserInfo = authInfo.getJSONObject("auth_user_info");
		this.authUserId = authUserInfo.getString("userId");
		
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

