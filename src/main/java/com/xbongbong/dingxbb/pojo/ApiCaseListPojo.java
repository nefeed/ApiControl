package com.xbongbong.dingxbb.pojo;

import java.io.Serializable;

/**
 * User: Gavin
 * E-mail: GavinChangCN@163.com
 * Desc:
 * Date: 2017-06-12
 * Time: 15:46
 */
public class ApiCaseListPojo implements Serializable {

    private static final long serialVersionUID = -1L;

    public ApiCaseListPojo() {
    }

    private int page = 1;
    private int pageSize = 20;
    private String expectedStateCodeSort;
    private String testResultSort;
    private String durationTimeSort;
    private String updateTimeSort;

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

    public String getExpectedStateCodeSort() {
        return expectedStateCodeSort;
    }

    public void setExpectedStateCodeSort(String expectedStateCodeSort) {
        this.expectedStateCodeSort = expectedStateCodeSort;
    }

    public String getTestResultSort() {
        return testResultSort;
    }

    public void setTestResultSort(String testResultSort) {
        this.testResultSort = testResultSort;
    }

    public String getDurationTimeSort() {
        return durationTimeSort;
    }

    public void setDurationTimeSort(String durationTimeSort) {
        this.durationTimeSort = durationTimeSort;
    }

    public String getUpdateTimeSort() {
        return updateTimeSort;
    }

    public void setUpdateTimeSort(String updateTimeSort) {
        this.updateTimeSort = updateTimeSort;
    }
}