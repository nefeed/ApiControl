
package com.xbongbong.dingxbb.model;

import com.alibaba.fastjson.JSON;
import com.xbongbong.dingxbb.dao.ApiDocDao;
import com.xbongbong.dingxbb.entity.ApiDocEntity;
import com.xbongbong.dingxbb.pojo.ApiDocParamsPojo;
import com.xbongbong.dingxbb.pojo.ApiDocResponsePojo;
import com.xbongbong.dingxbb.pojo.ApiDocWrongCodePojo;
import com.xbongbong.util.DateUtil;
import com.xbongbong.util.PageHelper;
import com.xbongbong.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class ApiDocModel extends BaseModel implements IModel {

    @Autowired
    private ApiDocDao dao;

    public Integer insert(Object entity) {
        Integer now = DateUtil.getInt();
        ((ApiDocEntity) entity).setAddTime(now);
        ((ApiDocEntity) entity).setUpdateTime(now);

        return dao.insert((ApiDocEntity) entity);
    }

    public Integer update(Object entity) {
        ((ApiDocEntity) entity).setUpdateTime(DateUtil.getInt());

        return dao.update((ApiDocEntity) entity);
    }

    public Integer save(ApiDocEntity entity) {
        if (entity.getId() == null || entity.getId().equals(0)) {
            return insert(entity);
        }
        return update(entity);
    }


    public Integer deleteByKey(Integer key) {
        return dao.deleteByKey(key);
    }

    public ApiDocEntity getByKey(Integer key) {
        return dao.getByKey(key);
    }

    public List<ApiDocEntity> findEntitys(Map<String, Object> param) {
        return dao.findEntitys(param);
    }

    public Integer getEntitysCount(Map<String, Object> param) {
        return dao.getEntitysCount(param);
    }

    public List<ApiDocEntity> findApiDocList(Integer page, Integer pageNum) {
        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        params.put("pageNum", pageNum);
        params.put("orderByStr", "module ASC, version DESC, update_time DESC"); // 按是否已读正序排列，推送时间倒叙排列
        params.put("del", 0);
        PageHelper pageHelper = getPageHelper(params, pageNum, this);
        return (List<ApiDocEntity>) getEntityList(
                params, pageHelper, this);
    }

    /**
     * 获取所有记录在案的Api版本号
     *
     * @return
     */
    public List<String> findApiVersionList() {
        Map<String, Object> params = new HashMap<>();
        params.put("orderByStr", "id DESC"); // 按是否已读正序排列，推送时间倒叙排列
        params.put("del", 0);
        List<ApiVersionEntity> apiVersionList = apiVersionModel.getEntityList(params);
        List<String> versionList = new ArrayList<>();
        for (ApiVersionEntity apiVersion : apiVersionList) {
            versionList.add(apiVersion.getVersion());
        }
        return versionList;
    }

    /**
     * 获取所有记录在案的Api模块
     *
     * @return
     */
    public List<String> findApiModuleList() {
        Map<String, Object> params = new HashMap<>();
        params.put("orderByStr", "id DESC"); // 按是否已读正序排列，推送时间倒叙排列
        params.put("del", 0);
        List<ApiModuleEntity> apiModuleList = apiModuleModel.getEntityList(params);
        List<String> moduleList = new ArrayList<>();
        for (ApiModuleEntity apiModule : apiModuleList) {
            moduleList.add(apiModule.getModule());
        }
        return moduleList;
    }

    public String formatMarkdownContent(ApiDocEntity entity) {
        StringBuffer content = new StringBuffer();
        content.append("## 【" + entity.getModule() + "】" + entity.getName() + " - v" + entity.getVersion() + "<br />");
        content.append("> " + entity.getMemo() + "<br />");
        content.append("<br />");
        int index = 0;
        content.append("#### " + (++index) + ". url：" + entity.getUrl() + "<br />");
        content.append("<br />");
        content.append("#### " + (++index) + ". 请求方式：" + entity.getMethod() + "<br />");
        content.append("<br />");
        content.append("#### " + (++index) + ". 请求参数" + "<br />");
        List<ApiDocParamsPojo> params = JSON.parseArray(entity.getParams(), ApiDocParamsPojo.class);
        if (params != null && params.size() > 0) {
            content.append("|参数 Key|参数名称|参数类型|长度上限|是否必填|说明|" + "<br />");
            content.append("|:-----------|:-----------|:---------|:---------|:---------|:-----------|" + "<br />");
            for (ApiDocParamsPojo item : params) {
                content.append("|" + item.getKey() + "|" + item.getName() + "|" + item.getType() + "|" + item.getLimit() + "|" + item.getRequired().toString() + "|" + item.getMemo() + "|" + "<br />");
            }
        } else {
            content.append("无请求参数<br />");
        }
        content.append("<br />");
        List<ApiDocResponsePojo> responses = JSON.parseArray(entity.getResponse(), ApiDocResponsePojo.class);
        content.append("#### " + (++index) + ". 主要返回内容" + "<br />");
        if (responses != null && responses.size() > 0) {
            content.append("|参数 Key|参数名称|参数类型|说明|" + "<br />");
            content.append("|:-----------|:-----------|:---------|:---------|:---------|:-----------|" + "<br />");
            for (ApiDocResponsePojo item : responses) {
                content.append("|" + item.getKey() + "|" + item.getName() + "|" + item.getType() + "|" + item.getMemo() + "|" + "<br />");
            }
        } else {
            content.append("无主要返回内容<br />");
        }
        content.append("<br />");
        List<ApiDocWrongCodePojo> wrongCodes = JSON.parseArray(entity.getWrongCode(), ApiDocWrongCodePojo.class);
        if (wrongCodes != null && wrongCodes.size() > 0) {
            content.append("#### " + (++index) + ". 错误Code" + "<br />");
            content.append("|Code|内容|" + "<br />");
            content.append("|:-----------|:-----------|" + "<br />");
            for (ApiDocWrongCodePojo item : wrongCodes) {
                content.append("|" + item.getCode() + "|" + item.getMsg() + "|" + "<br />");
            }
        }
        content.append("<br />");
        if (!StringUtil.isEmpty(entity.getParamsDemo())) {
            content.append("#### " + (++index) + ". 请求实例" + "<br />");
            content.append("```JSON" + "<br />");
            content.append(entity.getParamsDemo() + "<br />");
            content.append("```" + "<br />");
            content.append("<br />");
        }
        if (!StringUtil.isEmpty(entity.getResponseDemo())) {
            content.append("#### " + (++index) + ". 返回实例" + "<br />");
            content.append("```JSON" + "<br />");
            content.append(entity.getResponseDemo() + "<br />");
            content.append("```" + "<br />");
            content.append("<br />");
        }
        return content.toString();
    }
}

 