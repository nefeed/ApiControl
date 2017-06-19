package com.xbongbong.dingxbb.model

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.xbongbong.dingxbb.dao.ApiCaseDao
import com.xbongbong.dingxbb.entity.ApiCaseEntity
import com.xbongbong.dingxbb.entity.ApiDocEntity
import com.xbongbong.dingxbb.pojo.ApiCaseListItemPojo
import com.xbongbong.dingxbb.pojo.ApiCaseListPojo
import com.xbongbong.util.DateUtil
import com.xbongbong.util.StringUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

/**
 * User: Gavin
 * E-mail: GavinChangCN@163.com
 * Desc:
 * Date: 2017-06-19
 * Time: 09:42
 */
@Component
open class ApiCaseModel : BaseModel(), IModel {

    @Autowired
    private val apiDocModel: ApiDocModel? = null
    @Autowired
    private val apiCaseDao: ApiCaseDao? = null

    override fun insert(entity: Any): Int? {
        val now = DateUtil.getInt()
        (entity as ApiCaseEntity).addTime = now
        entity.updateTime = now

        return apiCaseDao!!.insert(entity)
    }

    override fun update(entity: Any): Int? {
        (entity as ApiCaseEntity).updateTime = DateUtil.getInt()

        return apiCaseDao!!.update(entity)
    }

    fun save(entity: ApiCaseEntity): Int? {

        if (entity.id == null || entity.id == 0) {
            return insert(entity)
        }
        return update(entity)
    }


    fun deleteByKey(key: Int?): Int? {
        return apiCaseDao!!.deleteByKey(key)
    }

    fun deleteByApiId(apiId: Int?): Int? {
        return apiCaseDao!!.deleteByApiId(apiId)
    }

    fun getByKey(key: Int?): ApiCaseEntity {
        return apiCaseDao!!.getByKey(key)
    }

    override fun findEntitys(param: Map<String, Any>): List<ApiCaseEntity>? {
        return apiCaseDao!!.findEntitys(param)
    }

    override fun getEntitysCount(param: Map<String, Any>): Int? {
        return apiCaseDao!!.getEntitysCount(param)
    }

    fun findApiCaseList(apiCaseListPojo: ApiCaseListPojo): List<ApiCaseEntity> {
        val params = HashMap<String, Any>()
        params.put("page", apiCaseListPojo.page)
        params.put("pageNum", apiCaseListPojo.pageSize)
        if (StringUtil.isEmpty(apiCaseListPojo.expectedStateCodeSort) &&
                StringUtil.isEmpty(apiCaseListPojo.testResultSort) &&
                StringUtil.isEmpty(apiCaseListPojo.durationTimeSort) &&
                StringUtil.isEmpty(apiCaseListPojo.updateTimeSort)) {
            params.put("orderByStr", "api_id DESC, update_time DESC") // 按是否已读正序排列，推送时间倒叙排列
        }
        val sort = StringBuilder()
        if (!StringUtil.isEmpty(apiCaseListPojo.expectedStateCodeSort)) {
            sort.append("expected_state_code").append(" ").append(apiCaseListPojo.expectedStateCodeSort).append(",")
        }
        if (!StringUtil.isEmpty(apiCaseListPojo.testResultSort)) {
            sort.append("test_result").append(" ").append(apiCaseListPojo.testResultSort).append(",")
        }
        if (!StringUtil.isEmpty(apiCaseListPojo.durationTimeSort)) {
            sort.append("duration_time").append(" ").append(apiCaseListPojo.durationTimeSort).append(",")
        }
        if (!StringUtil.isEmpty(apiCaseListPojo.updateTimeSort)) {
            sort.append("update_time").append(" ").append(apiCaseListPojo.updateTimeSort).append(",")
        }
        if (sort.isNotEmpty()) {
            params.put("orderByStr", sort.append("api_id DESC").toString())
        }
        params.put("del", 0)
        params.put("columns", "id,case_name,api_id,expected_state_code,test_result,duration_time,update_time")
        val pageHelper = BaseModel.getPageHelper(params, apiCaseListPojo.pageSize, this)
        return getEntityList(
                params, pageHelper, this) as List<ApiCaseEntity>
    }


    fun formatApiDocToCase(apiDoc: ApiDocEntity) {
        // todo  以下是需要完善功能的代码，暂时注释
        //        List<ApiDocParamsPojo> apiDocParamsList = JSON.parseArray(apiDoc.getParams(), ApiDocParamsPojo.class);
        //        JSONObject params = new JSONObject();
        //        if (apiDocParamsList != null && apiDocParamsList.size() > 0) {
        //            for (ApiDocParamsPojo apiDocParamsPojo : apiDocParamsList) {
        //                insertJsonParam(params, apiDocParamsPojo.getType(), apiDocParamsPojo.getKey());
        //            }
        //        }
        //        List<ApiDocResponsePojo> apiResponseParamsList = JSON.parseArray(apiDoc.getResponse(), ApiDocResponsePojo.class);
        //        JSONObject response = new JSONObject();
        //        if (apiResponseParamsList != null && apiResponseParamsList.size() > 0) {
        //            for (ApiDocResponsePojo apiDocResponsePojo : apiResponseParamsList) {
        //                insertJsonParam(response, apiDocResponsePojo.getType(), apiDocResponsePojo.getKey());
        //            }
        //        }
        //        JSONObject expectedContentJSON = new JSONObject();
        //        expectedContentJSON.put("code", 0);
        //        expectedContentJSON.put("msg", "操作成功");
        //        expectedContentJSON.put("result", JSON.toJSONString(response));
        //        String requestParameters = JSON.toJSONString(params);
        //        String expectedContent = JSON.toJSONString(expectedContentJSON);
        // todo 2017-05-24 初步使用 Demo 填充
        val requestParameters = apiDoc.paramsDemo
        val expectedContent = apiDoc.responseDemo

        // 查询是否已经生成相关测试用例
        val findParams = HashMap<String, Any>()
        findParams.put("apiId", apiDoc.id)
        findParams.put("del", 0)
        val apiCaseList = findEntitys(findParams)
        if (apiCaseList != null && apiCaseList.isNotEmpty()) {
            for (apiCase in apiCaseList) {
                apiCase.requestParameters = requestParameters
                apiCase.expectedContent = expectedContent
                update(apiCase)
            }
        } else {
            save(ApiCaseEntity(apiDoc.name + "__1", apiDoc.id, requestParameters, expectedContent))
        }
    }


    /**
     * 根据数据类型，向参数json插入初始化参数

     * @param json
     * *
     * @param type
     * *
     * @param key
     */
    private fun insertJsonParam(json: JSONObject, type: String, key: String) {
        when (type) {
            "String" -> json.put(key, "")
            "Integer" -> json.put(key, 0)
            "Array" -> json.put(key, "[]")
            "Boolean" -> json.put(key, "true")
            "JsonObject" -> json.put(key, "{}")
            "Float" -> json.put(key, 0.0f)
            "Double" -> json.put(key, 0.0)
        }
    }

    /**
     * 格式化用例列表返回列表数据

     * @param apiCaseList
     * *
     * @return
     */
    fun formatCasePojoList(apiCaseList: List<ApiCaseEntity>): List<ApiCaseListItemPojo> {
        val pojoList = ArrayList<ApiCaseListItemPojo>()
        var apiIdList: MutableList<Int> = ArrayList()
        for (item in apiCaseList) {
            apiIdList.add(item.apiId)
        }
        apiIdList = duplicateChecking(apiIdList)
        val apiDocList = apiDocModel!!.findApiDocList(apiIdList)
        for (item in apiCaseList) {
            for (doc in apiDocList) {
                if (doc.id == item.apiId) {
                    pojoList.add(formatCase(item, doc))
                    break
                }
            }
        }
        return pojoList
    }


    private fun formatCase(apiCase: ApiCaseEntity, apiDoc: ApiDocEntity): ApiCaseListItemPojo {
        val item = JSON.parseObject(JSON.toJSONString(apiCase), ApiCaseListItemPojo::class.java)
        item.setApiModule(apiDoc.module)
                .setApiName(apiDoc.name).apiVersion = apiDoc.version
        return item
    }

    /**
     * 查重队列中的重复部分，返回干净的队列
     * @param list
     * *
     * @return
     */
    private fun duplicateChecking(list: MutableList<Int>?): MutableList<Int> {
        if (list == null || list.size == 0) {
            return ArrayList()
        }
        val tempSet = HashSet<Int>()
        tempSet.addAll(list)
        list.clear()
        list.addAll(tempSet)
        return list
    }
}

