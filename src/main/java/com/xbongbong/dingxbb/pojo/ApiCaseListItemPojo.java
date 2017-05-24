
package com.xbongbong.dingxbb.pojo;

import java.io.Serializable;


public class ApiCaseListItemPojo implements Serializable, Cloneable {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -1L;

    //api用例id
    private Integer id;
    //api用例名称
    private String caseName;
    //api关联表 id
    private Integer apiId;
    // api 模块
    private String apiModule;
    // api 名称
    private String apiName;
    // api  版本
    private String apiVersion;
    //api请求参数
    private String requestParameters;
    //api预期返回 code
    private Integer expectedStateCode;
    //api实际返回 code
    private Integer actualStateCode;
    //api预期返回文本
    private String expectedContent;
    //api实际返回文本
    private String actualContent;
    //api比较方式：equal或contains
    private String contentJudgeLogic;
    //请求耗时ms
    private Integer durationTime;
    //api验证结果：Pass或Fail
    private String testResult;
    //更新时间
    private Integer updateTime;
    //创建时间
    private Integer addTime;
    //是否删除
    private Integer del;

    //========== getters and setters ==========

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public Integer getApiId() {
        return apiId;
    }

    public void setApiId(Integer apiId) {
        this.apiId = apiId;
    }

    public String getApiModule() {
        return apiModule;
    }

    public ApiCaseListItemPojo setApiModule(String apiModule) {
        this.apiModule = apiModule;
        return this;
    }

    public String getApiName() {
        return apiName;
    }

    public ApiCaseListItemPojo setApiName(String apiName) {
        this.apiName = apiName;
        return this;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public ApiCaseListItemPojo setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
        return this;
    }

    public String getRequestParameters() {
        return requestParameters;
    }

    public void setRequestParameters(String requestParameters) {
        this.requestParameters = requestParameters;
    }

    public Integer getExpectedStateCode() {
        return expectedStateCode;
    }

    public void setExpectedStateCode(Integer expectedStateCode) {
        this.expectedStateCode = expectedStateCode;
    }

    public Integer getActualStateCode() {
        return actualStateCode;
    }

    public void setActualStateCode(Integer actualStateCode) {
        this.actualStateCode = actualStateCode;
    }

    public String getExpectedContent() {
        return expectedContent;
    }

    public void setExpectedContent(String expectedContent) {
        this.expectedContent = expectedContent;
    }

    public String getActualContent() {
        return actualContent;
    }

    public void setActualContent(String actualContent) {
        this.actualContent = actualContent;
    }

    public String getContentJudgeLogic() {
        return contentJudgeLogic;
    }

    public void setContentJudgeLogic(String contentJudgeLogic) {
        this.contentJudgeLogic = contentJudgeLogic;
    }

    public Integer getDurationTime() {
        return durationTime;
    }

    public void setDurationTime(Integer durationTime) {
        this.durationTime = durationTime;
    }

    public String getTestResult() {
        return testResult;
    }

    public void setTestResult(String testResult) {
        this.testResult = testResult;
    }

    public Integer getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getAddTime() {
        return addTime;
    }

    public void setAddTime(Integer addTime) {
        this.addTime = addTime;
    }

    public Integer getDel() {
        return del;
    }

    public void setDel(Integer del) {
        this.del = del;
    }

}

