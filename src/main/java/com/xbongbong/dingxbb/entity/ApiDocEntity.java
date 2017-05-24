
package com.xbongbong.dingxbb.entity;

import com.xbongbong.util.DateUtil;
import com.xbongbong.util.StringUtil;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;


public class ApiDocEntity implements Serializable {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -1L;

    public ApiDocEntity() {
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

    public ApiDocEntity setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public ApiDocEntity setVersion(String version) {
        this.version = version;
        return this;
    }

    public String getModule() {
        return module;
    }

    public ApiDocEntity setModule(String module) {
        this.module = module;
        return this;
    }

    public String getName() {
        return name;
    }

    public ApiDocEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public ApiDocEntity setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public ApiDocEntity setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public ApiDocEntity setMethod(String method) {
        this.method = method;
        return this;
    }

    public String getMemo() {
        return memo;
    }

    public ApiDocEntity setMemo(String memo) {
        this.memo = memo;
        return this;
    }

    public String getParams() {
        return params;
    }

    public ApiDocEntity setParams(String params) {
        this.params = params;
        return this;
    }

    public String getParamsDemo() {
        return paramsDemo;
    }

    public ApiDocEntity setParamsDemo(String paramsDemo) {
        this.paramsDemo = paramsDemo;
        return this;
    }

    public String getResponse() {
        return response;
    }

    public ApiDocEntity setResponse(String response) {
        this.response = response;
        return this;
    }

    public String getResponseDemo() {
        return responseDemo;
    }

    public ApiDocEntity setResponseDemo(String responseDemo) {
        this.responseDemo = responseDemo;
        return this;
    }

    public String getWrongCode() {
        return wrongCode;
    }

    public ApiDocEntity setWrongCode(String wrongCode) {
        this.wrongCode = wrongCode;
        return this;
    }

    public Integer getAddTime() {
        return addTime;
    }

    public ApiDocEntity setAddTime(Integer addTime) {
        this.addTime = addTime;
        return this;
    }

    public Integer getUpdateTime() {
        return updateTime;
    }

    public ApiDocEntity setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public Integer getDel() {
        return del;
    }

    public ApiDocEntity setDel(Integer del) {
        this.del = del;
        return this;
    }

    /**
     * 重载toString方法
     *
     * @return
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

