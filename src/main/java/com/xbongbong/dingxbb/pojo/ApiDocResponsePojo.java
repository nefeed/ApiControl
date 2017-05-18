package com.xbongbong.dingxbb.pojo;

import java.io.Serializable;

/**
 * User: Gavin
 * E-mail: GavinChangCN@163.com
 * Desc:
 * Date: 2017-05-17
 * Time: 16:54
 */
public class ApiDocResponsePojo implements Serializable {

    private static final long serialVersionUID = -1L;
    public ApiDocResponsePojo(){
    }

    private String key;
    private String name;
    private String type;
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

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
