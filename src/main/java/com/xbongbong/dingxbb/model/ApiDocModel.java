
package com.xbongbong.dingxbb.model;

import com.alibaba.fastjson.JSON;
import com.xbongbong.dingxbb.dao.ApiDocDao;
import com.xbongbong.dingxbb.entity.ApiDocEntity;
import com.xbongbong.dingxbb.entity.ApiVersionEntity;
import com.xbongbong.dingxbb.entity.SysModuleEntity;
import com.xbongbong.dingxbb.pojo.ApiDocParamsPojo;
import com.xbongbong.dingxbb.pojo.ApiDocResponsePojo;
import com.xbongbong.dingxbb.pojo.ApiDocWrongCodePojo;
import com.xbongbong.dingxbb.pojo.ApiFuzzySearchPojo;
import com.xbongbong.util.DateUtil;
import com.xbongbong.util.PageHelper;
import com.xbongbong.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
public class ApiDocModel extends BaseModel implements IModel {

    @Autowired
    private ApiDocDao apiDocDao;
    @Autowired
    private ApiVersionModel apiVersionModel;
    @Autowired
    private SysModuleModel sysModuleModel;
    @Autowired
    private ApiCaseModel apiCaseModel;

    public Integer insert(Object entity) {
        Integer now = DateUtil.getInt();
        ((ApiDocEntity) entity).setAddTime(now);
        ((ApiDocEntity) entity).setUpdateTime(now);

        return apiDocDao.insert((ApiDocEntity) entity);
    }

    public Integer update(Object entity) {
        ((ApiDocEntity) entity).setUpdateTime(DateUtil.getInt());

        return apiDocDao.update((ApiDocEntity) entity);
    }

    public Integer save(ApiDocEntity entity) {
        Integer code;
        if (entity.getId() == null || entity.getId().equals(0)) {
            code = insert(entity);
        } else {
            code = update(entity);
        }
        apiCaseModel.formatApiDocToCase(entity);
        return code;
    }


    public Integer deleteByKey(final Integer key) {
        Integer code = apiDocDao.deleteByKey(key);
        apiCaseModel.deleteByApiId(key);
        return code;
    }

    public ApiDocEntity getByKey(Integer key) {
        return apiDocDao.getByKey(key);
    }

    public List<ApiDocEntity> findEntitys(Map<String, Object> param) {
        return apiDocDao.findEntitys(param);
    }

    public Integer getEntitysCount(Map<String, Object> param) {
        return apiDocDao.getEntitysCount(param);
    }

    public List<ApiDocEntity> findApiDocList(Integer page, Integer pageNum) {
        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        params.put("pageNum", pageNum);
        params.put("orderByStr", "module ASC, version DESC, update_time DESC"); // 按是否已读正序排列，推送时间倒叙排列
        params.put("del", 0);
        params.put("columns", "id,module,version,name,url,username,update_time");
        PageHelper pageHelper = getPageHelper(params, pageNum, this);
        return (List<ApiDocEntity>) getEntityList(
                params, pageHelper, this);
    }

    public List<ApiDocEntity> findApiDocList(List<Integer> apiIdList) {
        Map<String, Object> params = new HashMap<>();
        params.put("idIn", apiIdList);
        params.put("del", 0);
        params.put("columns", "id,module,version,name");
        return findEntitys(params);
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
        List<ApiVersionEntity> apiVersionList = apiVersionModel.findEntitys(params);
        Collections.sort(apiVersionList, VERSION_COMPARATOR);
        List<String> versionList = new ArrayList<>();
        for (ApiVersionEntity apiVersion : apiVersionList) {
            versionList.add(apiVersion.getVersion());
        }
        return versionList;
    }

    // 按版本号的 id 倒叙排列
    private static final Comparator<ApiVersionEntity> VERSION_COMPARATOR = new Comparator<ApiVersionEntity>() {
        public int compare(ApiVersionEntity o1, ApiVersionEntity o2) {
            return o2.getId().compareTo(o1.getId());
        }
    };

    /**
     * 新增版本
     *
     * @return
     */
    public Integer itemVersion(String version) {
        Map<String, Object> params = new HashMap<>();
        params.put("version", version);
        params.put("del", 0);
        List<ApiVersionEntity> versionList = apiVersionModel.findEntitys(params);
        if (versionList == null || versionList.isEmpty()) {
            return apiVersionModel.save(new ApiVersionEntity(version));
        } else {
            return -1;
        }
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
        List<SysModuleEntity> apiModuleList = sysModuleModel.findEntitys(params);
        Collections.sort(apiModuleList, MODULE_COMPARATOR);
        List<String> moduleList = new ArrayList<>();
        for (SysModuleEntity apiModule : apiModuleList) {
            moduleList.add(apiModule.getModule());
        }
        return moduleList;
    }

    // 按模块的 id 倒叙排列
    private static final Comparator<SysModuleEntity> MODULE_COMPARATOR = new Comparator<SysModuleEntity>() {
        public int compare(SysModuleEntity o1, SysModuleEntity o2) {
            return o2.getId().compareTo(o1.getId());
        }
    };

    /**
     * 新增模块
     *
     * @return
     */
    public Integer itemModule(String moduleName) {
        Map<String, Object> params = new HashMap<>();
        params.put("moduleLike", "%" + moduleName + "%");
        params.put("del", 0);
        List<SysModuleEntity> moduleList = sysModuleModel.findEntitys(params);
        if (moduleList == null || moduleList.isEmpty()) {
            return sysModuleModel.save(new SysModuleEntity(moduleName));
        } else {
            return -1;
        }
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
        if (!StringUtil.isEmpty(entity.getParamsDemo())) {
            content.append("#### " + (++index) + ". 请求实例" + "<br />");
            content.append("```JSON" + "<br />");
            content.append(entity.getParamsDemo() + "<br />");
            content.append("```" + "<br />");
            content.append("<br />");
        }
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
        if (!StringUtil.isEmpty(entity.getResponseDemo())) {
            content.append("#### " + (++index) + ". 返回实例" + "<br />");
            content.append("```JSON" + "<br />");
            content.append(entity.getResponseDemo() + "<br />");
            content.append("```" + "<br />");
            content.append("<br />");
        }
        return content.toString();
    }

    /**
     * 模糊搜索 Api 文档列表
     *
     * @param fuzzySearchPojo
     * @return
     */
    public List<ApiDocEntity> fuzzySearchList(ApiFuzzySearchPojo fuzzySearchPojo) {
        Map<String, Object> params = new HashMap<>();
        params.put("page", fuzzySearchPojo.getPage());
        params.put("pageNum", fuzzySearchPojo.getPageSize());
        params.put("orderByStr", "module ASC, version DESC, update_time DESC");
        params.put("columns", "id,module,version,name,url,username,update_time");
        params = initFuzzySearchMap(params, fuzzySearchPojo);
        PageHelper pageHelper = getPageHelper(params, fuzzySearchPojo.getPageSize(), this);
        return (List<ApiDocEntity>) getEntityList(
                params, pageHelper, this);
    }

    /**
     * 模糊搜索 Api 文档列表总数
     *
     * @param fuzzySearchPojo
     * @return
     */
    public Integer getFuzzySearchCount(ApiFuzzySearchPojo fuzzySearchPojo) {
        Map<String, Object> params = new HashMap<>();
        params = initFuzzySearchMap(params, fuzzySearchPojo);
        return apiDocDao.getEntitysCount(params);
    }

    private Map<String, Object> initFuzzySearchMap(Map<String, Object> params, ApiFuzzySearchPojo fuzzySearchPojo) {
        params.put("del", 0);
        if (!StringUtil.isEmpty(fuzzySearchPojo.getModule())) {
            params.put("module", fuzzySearchPojo.getModule());
        }
        if (!StringUtil.isEmpty(fuzzySearchPojo.getVersion())) {
            params.put("version", fuzzySearchPojo.getVersion());
        }
        if (!StringUtil.isEmpty(fuzzySearchPojo.getUrlLike())) {
            params.put("urlLike", fuzzySearchPojo.getUrlLike());
        }
        if (!StringUtil.isEmpty(fuzzySearchPojo.getApiNameLike())) {
            params.put("apiNameLike", fuzzySearchPojo.getApiNameLike());
        }
        if (!StringUtil.isEmpty(fuzzySearchPojo.getAuthorNameLike())) {
            params.put("authorNameLike", fuzzySearchPojo.getAuthorNameLike());
        }
        if (fuzzySearchPojo.getUpdateTimeStart() > 0 && fuzzySearchPojo.getUpdateTimeEnd() > 0 &&
                fuzzySearchPojo.getUpdateTimeEnd() > fuzzySearchPojo.getUpdateTimeStart()) {
            params.put("updateTimeStart", fuzzySearchPojo.getUpdateTimeStart());
            params.put("updateTimeEnd", fuzzySearchPojo.getUpdateTimeEnd());
        }
        return params;
    }
}

 