package com.xbongbong.dingxbb.pojo;

import java.io.Serializable;

/**
 * User: Gavin
 * E-mail: GavinChangCN@163.com
 * Desc:
 * Date: 2017-05-17
 * Time: 16:54
 */
public class ApiDocWrongCodePojo implements Serializable {

    private static final long serialVersionUID = -1L;
    public ApiDocWrongCodePojo(){
    }

    private String code;
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
