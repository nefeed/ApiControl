 
package com.xbongbong.dingxbb.entity;

import com.xbongbong.util.DateUtil;
import com.xbongbong.util.StringUtil;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;


public class ApiDocEntity implements Serializable{

	/** Comment for <code>serialVersionUID</code> */
	private static final long serialVersionUID = -1L;
	public ApiDocEntity(){
		updateTime = DateUtil.getInt();
		del = 0;
	}
	//========== properties ==========
	
	//Api Doc 主键
	private Integer id;
	//Api Doc 版本号
	private String version;
	//Api Doc 模块名
	private String module;
	//Api Doc 接口名
	private String name;
	//Api Doc 请求地址，不包含域名部分
	private String url;
	//Api Doc 创建者名称
	private String username;
	//Api Doc 请求方式：POST或GET
	private String method;
	//Api Doc 说明
	private String memo;
	//Api Doc 请求参数 {"key": "参数 key", "name": "参数中文", "type": "参数类型（String、int、float、double、boolean）", "limie": "参数长度上限（0：无上限）", "required": "是否必填（boolean）", "memo": "说明"}
	private String params;
	//Api Doc 请求参数 Demo
	private String paramsDemo;
	//Api Doc 返回 Response 的 body 主要内容 {"key": "参数 key", "name": "参数中文", "type": "参数类型（String、int、float、double、boolean）", "memo": "说明"}
	private String response;
	//Api Doc 返回 Response 的 body 的 demo
	private String responseDemo;
	//错误代码： {"code": "错误代码 Code", "msg": "错误说明"}
	private String wrongCode;
	//创建时间（Unix_timestamp）
	private Integer addTime;
	//更新时间（Unix_timestamp）
	private Integer updateTime;
	//是否删除
	private Integer del;

    //========== getters and setters ==========
    
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getModule() {
		return module;
	}
	
	public void setModule(String module) {
		this.module = module;
	}
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	public String getMethod() {
		return method;
	}
	
	public void setMethod(String method) {
		this.method = method;
	}
	public String getMemo() {
		return memo;
	}
	
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getParams() {
		return params;
	}
	
	public void setParams(String params) {
		this.params = params;
	}
	public String getParamsDemo() {
		return paramsDemo;
	}
	
	public void setParamsDemo(String paramsDemo) {
		this.paramsDemo = paramsDemo;
	}
	public String getResponse() {
		return response;
	}
	
	public void setResponse(String response) {
		this.response = response;
	}
	public String getResponseDemo() {
		return responseDemo;
	}
	
	public void setResponseDemo(String responseDemo) {
		this.responseDemo = responseDemo;
	}
	public String getWrongCode() {
		return wrongCode;
	}
	
	public void setWrongCode(String wrongCode) {
		this.wrongCode = wrongCode;
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

	/**
	* 重载toString方法
	* @return
	*
	* @see Object#toString()
	*/
	@Override
    public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public void checkArray() {
		if (StringUtil.isEmpty(this.params)) {
			this.params = "[]";
		}
		if (StringUtil.isEmpty(this.response)) {
			this.response = "[]";
		}
		if (StringUtil.isEmpty(this.wrongCode)) {
			this.wrongCode = "[]";
		}
	}
}

