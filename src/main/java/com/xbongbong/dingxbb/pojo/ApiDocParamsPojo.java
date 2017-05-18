package com.xbongbong.dingxbb.pojo;

import java.io.Serializable;

/**
 * User: Gavin
 * E-mail: GavinChangCN@163.com
 * Desc:
 * Date: 2017-05-17
 * Time: 16:54
 */
public class ApiDocParamsPojo implements Serializable {

    private static final long serialVersionUID = -1L;
    public ApiDocParamsPojo(){
    }

    private String key;
    private String name;
    private String type;
    private Integer limit;
    private Boolean required;
    private String memo;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
