package com.xbongbong.dingxbb.pojo;

import java.io.Serializable;

/**
 * User: Gavin
 * E-mail: GavinChangCN@163.com
 * Desc:
 * Date: 2017-06-12
 * Time: 15:46
 */
public class ApiFuzzySearchPojo implements Serializable {

    private static final long serialVersionUID = -1L;

    public ApiFuzzySearchPojo() {
    }

    private int page = 1;
    private int pageSize = 20;
    private String version;
    private String module;
    private String apiNameLike;
    private String urlLike;
    private String authorNameLike;
    private int updateTimeStart = 0;
    private int updateTimeEnd = 0;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
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

    public String getApiNameLike() {
        return apiNameLike;
    }

    public void setApiNameLike(String apiNameLike) {
        this.apiNameLike = apiNameLike;
    }

    public String getUrlLike() {
        return urlLike;
    }

    public void setUrlLike(String urlLike) {
        this.urlLike = urlLike;
    }

    public String getAuthorNameLike() {
        return authorNameLike;
    }

    public void setAuthorNameLike(String authorNameLike) {
        this.authorNameLike = authorNameLike;
    }

    public int getUpdateTimeStart() {
        return updateTimeStart;
    }

    public void setUpdateTimeStart(int updateTimeStart) {
        this.updateTimeStart = updateTimeStart;
    }

    public int getUpdateTimeEnd() {
        return updateTimeEnd;
    }

    public void setUpdateTimeEnd(int updateTimeEnd) {
        this.updateTimeEnd = updateTimeEnd;
    }
}