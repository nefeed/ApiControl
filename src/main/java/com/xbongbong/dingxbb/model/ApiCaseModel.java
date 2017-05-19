
package com.xbongbong.dingxbb.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xbongbong.dingxbb.dao.ApiCaseDao;
import com.xbongbong.dingxbb.entity.ApiCaseEntity;
import com.xbongbong.dingxbb.entity.ApiDocEntity;
import com.xbongbong.dingxbb.pojo.ApiDocParamsPojo;
import com.xbongbong.dingxbb.pojo.ApiDocResponsePojo;
import com.xbongbong.util.DateUtil;
import com.xbongbong.util.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class ApiCaseModel extends BaseModel implements IModel {

    @Autowired
    private ApiCaseDao apiCaseDao;

    public Integer insert(Object entity) {
        Integer now = DateUtil.getInt();
        ((ApiCaseEntity) entity).setAddTime(now);
        ((ApiCaseEntity) entity).setUpdateTime(now);

        return apiCaseDao.insert((ApiCaseEntity) entity);
    }

    public Integer update(Object entity) {
        ((ApiCaseEntity) entity).setUpdateTime(DateUtil.getInt());

        return apiCaseDao.update((ApiCaseEntity) entity);
    }

    public Integer save(ApiCaseEntity entity) {

        if (entity.getId() == null || entity.getId().equals(0)) {
            return insert(entity);
        }
        return update(entity);
    }


    public Integer deleteByKey(Integer key) {
        return apiCaseDao.deleteByKey(key);
    }

    public Integer deleteByApiId(Integer apiId) {
        return apiCaseDao.deleteByApiId(apiId);
    }

    public ApiCaseEntity getByKey(Integer key) {
        return apiCaseDao.getByKey(key);
    }

    public List<ApiCaseEntity> findEntitys(Map<String, Object> param) {
        return apiCaseDao.findEntitys(param);
    }

    public Integer getEntitysCount(Map<String, Object> param) {
        return apiCaseDao.getEntitysCount(param);
    }

    public List<ApiCaseEntity> findApiCaseList(Integer page, Integer pageNum) {
        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        params.put("pageNum", pageNum);
        params.put("orderByStr", "api_id DESC, update_time DESC"); // 按是否已读正序排列，推送时间倒叙排列
        params.put("del", 0);
        PageHelper pageHelper = getPageHelper(params, pageNum, this);
        return (List<ApiCaseEntity>) getEntityList(
                params, pageHelper, this);
    }


    public void formatApiDocToCase(final ApiDocEntity apiDoc) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<ApiDocParamsPojo> apiDocParamsList = JSON.parseArray(apiDoc.getParams(), ApiDocParamsPojo.class);
                JSONObject params = new JSONObject();
                if (apiDocParamsList != null && apiDocParamsList.size() > 0) {
                    for (ApiDocParamsPojo apiDocParamsPojo : apiDocParamsList) {
                        insertJsonParam(params, apiDocParamsPojo.getType(), apiDocParamsPojo.getKey());
                    }
                }
                List<ApiDocResponsePojo> apiResponseParamsList = JSON.parseArray(apiDoc.getResponse(), ApiDocResponsePojo.class);
                JSONObject response = new JSONObject();
                if (apiResponseParamsList != null && apiResponseParamsList.size() > 0) {
                    for (ApiDocResponsePojo apiDocResponsePojo : apiResponseParamsList) {
                        insertJsonParam(response, apiDocResponsePojo.getType(), apiDocResponsePojo.getKey());
                    }
                }
                JSONObject expectedContentJSON = new JSONObject();
                expectedContentJSON.put("code", 0);
                expectedContentJSON.put("msg", "操作成功");
                expectedContentJSON.put("result", JSON.toJSONString(response));
                String requestParameters = JSON.toJSONString(params);
                String expectedContent = JSON.toJSONString(expectedContentJSON);

                // 查询是否已经生成相关测试用例
                Map<String, Object> findParams = new HashMap<>();
                findParams.put("apiId", apiDoc.getId());
                findParams.put("del", 0);
                List<ApiCaseEntity> apiCaseList = findEntitys(findParams);
                if (apiCaseList != null && apiCaseList.size() > 0) {
                    for (ApiCaseEntity apiCase : apiCaseList) {
                        apiCase.setRequestParameters(requestParameters);
                        apiCase.setExpectedContent(expectedContent);
                        update(apiCase);
                    }
                } else {
                    save(new ApiCaseEntity(apiDoc.getName() + "__1", apiDoc.getId(), requestParameters, expectedContent));
                }
            }
        }).start();
    }

    /**
     * 根据数据类型，向参数json插入初始化参数
     *
     * @param json
     * @param type
     * @param key
     */
    private void insertJsonParam(JSONObject json, String type, String key) {
        switch (type) {
            case "String":
                json.put(key, "");
                break;
            case "Integer":
                json.put(key, 0);
                break;
            case "Array":
                json.put(key, "[]");
                break;
            case "Boolean":
                json.put(key, "true");
                break;
            case "Float":
                json.put(key, 0.0f);
                break;
            case "Double":
                json.put(key, 0.0d);
                break;
        }
    }
}

 