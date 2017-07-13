package com.xbongbong.dingxbb.model

import com.alibaba.fastjson.JSON
import com.xbongbong.dingxbb.dao.ApiDocDao
import com.xbongbong.dingxbb.entity.ApiDocEntity
import com.xbongbong.dingxbb.entity.ApiVersionEntity
import com.xbongbong.dingxbb.entity.SysModuleEntity
import com.xbongbong.dingxbb.pojo.ApiDocListPojo
import com.xbongbong.dingxbb.pojo.ApiDocParamsPojo
import com.xbongbong.dingxbb.pojo.ApiDocResponsePojo
import com.xbongbong.dingxbb.pojo.ApiDocWrongCodePojo
import com.xbongbong.dingxbb.tool.JsonFormatTool
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
 * Time: 09:33
 */
@Component
open class ApiDocModel : BaseModel(), IModel {

    @Autowired
    private val apiDocDao: ApiDocDao? = null
    @Autowired
    private val apiVersionModel: ApiVersionModel? = null
    @Autowired
    private val sysModuleModel: SysModuleModel? = null
    @Autowired
    private val apiCaseModel: ApiCaseModel? = null
    @Autowired
    private val jsonFormatTool: JsonFormatTool ?= null

    override fun insert(entity: Any): Int? {
        val now = DateUtil.getInt()
        (entity as ApiDocEntity).addTime = now
        entity.updateTime = now

        return apiDocDao!!.insert(entity)
    }

    override fun update(entity: Any): Int? {
        (entity as ApiDocEntity).updateTime = DateUtil.getInt()

        return apiDocDao!!.update(entity)
    }

    fun save(entity: ApiDocEntity): Int? {
        val code: Int?
        if (entity.id == null || entity.id == 0) {
            code = insert(entity)
        } else {
            code = update(entity)
        }
        apiCaseModel!!.formatApiDocToCase(entity)
        return code
    }


    fun deleteByKey(key: Int?): Int? {
        val code = apiDocDao!!.deleteByKey(key)
        apiCaseModel!!.deleteByApiId(key)
        return code
    }

    fun getByKey(key: Int?): ApiDocEntity {
        return apiDocDao!!.getByKey(key)
    }

    override fun findEntitys(param: Map<String, Any>): List<ApiDocEntity> {
        return apiDocDao!!.findEntitys(param)
    }

    override fun getEntitysCount(param: Map<String, Any>): Int? {
        return apiDocDao!!.getEntitysCount(param)
    }

    /**
     * 获取所有记录在案的Api版本号

     * @return
     */
    fun findApiVersionList(): List<String> {
        val params = HashMap<String, Any>()
        params.put("orderByStr", "id DESC") // 按是否已读正序排列，推送时间倒叙排列
        params.put("del", 0)
        val apiVersionList = apiVersionModel!!.findEntitys(params)
        Collections.sort(apiVersionList, VERSION_COMPARATOR)
        val versionList = apiVersionList.map { it.version }
        return versionList
    }

    /**
     * 新增版本

     * @return
     */
    fun itemVersion(version: String): Int? {
        val params = HashMap<String, Any>()
        params.put("version", version)
        params.put("del", 0)
        val versionList = apiVersionModel!!.findEntitys(params)
        if (versionList == null || versionList.isEmpty()) {
            return apiVersionModel.save(ApiVersionEntity(version))
        } else {
            return -1
        }
    }

    /**
     * 获取所有记录在案的Api模块

     * @return
     */
    fun findApiModuleList(): List<String> {
        val params = HashMap<String, Any>()
        params.put("orderByStr", "id DESC") // 按是否已读正序排列，推送时间倒叙排列
        params.put("del", 0)
        val apiModuleList = sysModuleModel!!.findEntitys(params)
        Collections.sort(apiModuleList, MODULE_COMPARATOR)
        val moduleList = apiModuleList.map { it.module }
        return moduleList
    }

    /**
     * 新增模块

     * @return
     */
    fun itemModule(moduleName: String): Int? {
        val params = HashMap<String, Any>()
        params.put("moduleLike", "%$moduleName%")
        params.put("del", 0)
        val moduleList = sysModuleModel!!.findEntitys(params)
        if (moduleList == null || moduleList.isEmpty()) {
            return sysModuleModel.save(SysModuleEntity(moduleName))
        } else {
            return -1
        }
    }

    fun formatMarkdownContent(entity: ApiDocEntity): String {
        val content = StringBuffer()
        content.append("# 【" + entity.module + "】" + entity.name + " - v" + entity.version + "<br />")
        content.append("> " + entity.memo + "<br />")
        content.append("<br />")
        var index = 0
        content.append("### " + ++index + ". url：" + entity.url + "<br />")
        content.append("<br />")
        content.append("### " + ++index + ". 请求方式：" + entity.method + "<br />")
        content.append("<br />")
        content.append("### " + ++index + ". 请求参数" + "<br />")
        val params = JSON.parseArray(entity.params, ApiDocParamsPojo::class.java)
        if (params != null && params.size > 0) {
            content.append("|参数 Key|参数名称|参数类型|长度上限|是否必填|说明|" + "<br />")
            content.append("|:-----------|:-----------|:---------|:---------|:---------|:-----------|" + "<br />")
            for (item in params) {
                content.append("|" + item.key + "|" + item.name + "|" + item.type + "|" + item.limit + "|" + item.required!!.toString() + "|" + item.memo + "|" + "<br />")
            }
        } else {
            content.append("无请求参数<br />")
        }
        content.append("<br />")
        if (!StringUtil.isEmpty(entity.paramsDemo)) {
            content.append("### " + ++index + ". 请求实例" + "<br />")
            content.append("```JSON" + "<br />")
            content.append(jsonFormatTool!!.formatJson2Html(entity.paramsDemo) + "<br />")
            content.append("```" + "<br />")
            content.append("<br />")
        }
        val responses = JSON.parseArray(entity.response, ApiDocResponsePojo::class.java)
        content.append("### " + ++index + ". 主要返回内容" + "<br />")
        if (responses != null && responses.size > 0) {
            content.append("|参数 Key|参数名称|参数类型|说明|" + "<br />")
            content.append("|:-----------|:-----------|:---------|:---------|:---------|:-----------|" + "<br />")
            for (item in responses) {
                content.append("|" + item.key + "|" + item.name + "|" + item.type + "|" + item.memo + "|" + "<br />")
            }
        } else {
            content.append("无主要返回内容<br />")
        }
        content.append("<br />")
        val wrongCodes = JSON.parseArray(entity.wrongCode, ApiDocWrongCodePojo::class.java)
        if (wrongCodes != null && wrongCodes.size > 0) {
            content.append("### " + ++index + ". 错误Code" + "<br />")
            content.append("|Code|内容|" + "<br />")
            content.append("|:-----------|:-----------|" + "<br />")
            for (item in wrongCodes) {
                content.append("|" + item.code + "|" + item.msg + "|" + "<br />")
            }
        }
        content.append("<br />")
        if (!StringUtil.isEmpty(entity.responseDemo)) {
            content.append("### " + ++index + ". 返回实例" + "<br />")
            content.append("```JSON" + "<br />")
            content.append(jsonFormatTool!!.formatJson2Html(entity.responseDemo) + "<br />")
            content.append("```" + "<br />")
            content.append("<br />")
        }
        return content.toString()
    }

    /**
     * 获取 Api 文档列表

     * @param fuzzySearchPojo
     * *
     * @return
     */
    fun findApiDocList(fuzzySearchPojo: ApiDocListPojo): List<ApiDocEntity> {
        var params: MutableMap<String, Any> = HashMap()
        params.put("page", fuzzySearchPojo.page)
        params.put("pageNum", fuzzySearchPojo.pageSize)
        params.put("orderByStr", "module ASC, version DESC, update_time DESC") // 按是否已读正序排列，推送时间倒叙排列
        if (StringUtil.isEmpty(fuzzySearchPojo.moduleSort) &&
                StringUtil.isEmpty(fuzzySearchPojo.versionSort) &&
                StringUtil.isEmpty(fuzzySearchPojo.authorSort) &&
                StringUtil.isEmpty(fuzzySearchPojo.updateTimeSort)) {
            params.put("orderByStr", "module ASC, version DESC, update_time DESC") // 按是否已读正序排列，推送时间倒叙排列
        }
        val sort = StringBuilder()
        if (!StringUtil.isEmpty(fuzzySearchPojo.moduleSort)) {
            sort.append("module").append(" ").append(fuzzySearchPojo.moduleSort).append(",")
        }
        if (!StringUtil.isEmpty(fuzzySearchPojo.versionSort)) {
            sort.append("version").append(" ").append(fuzzySearchPojo.versionSort).append(",")
        }
        if (!StringUtil.isEmpty(fuzzySearchPojo.authorSort)) {
            sort.append("username").append(" ").append(fuzzySearchPojo.authorSort).append(",")
        }
        if (!StringUtil.isEmpty(fuzzySearchPojo.updateTimeSort)) {
            sort.append("update_time").append(" ").append(fuzzySearchPojo.updateTimeSort).append(",")
        }
        if (sort.isNotEmpty()) {
            //            params.put("orderByStr", sort.append("id DESC").toString());
            params.put("orderByStr", sort.substring(0, sort.length - 1))
        }
        params = initFuzzySearchMap(params, fuzzySearchPojo)
        params.put("columns", "id,module,version,name,url,username,update_time")
        val pageHelper = BaseModel.getPageHelper(params, fuzzySearchPojo.pageSize, this)
        return getEntityList(
                params, pageHelper, this) as List<ApiDocEntity>
    }

    /**
     * 根据 Api 文档 id 列表获取 Api 文档列表

     * @param apiIdList
     * *
     * @return
     */
    fun findApiDocList(apiIdList: List<Int>): List<ApiDocEntity> {
        val params = HashMap<String, Any>()
        params.put("idIn", apiIdList)
        params.put("del", 0)
        params.put("limitNum", apiIdList.size)
        params.put("columns", "id,module,version,name")
        return findEntitys(params)
    }

    /**
     * 模糊搜索 Api 文档列表总数

     * @param fuzzySearchPojo
     * *
     * @return
     */
    fun getApiDocCount(fuzzySearchPojo: ApiDocListPojo): Int? {
        var params: MutableMap<String, Any> = HashMap()
        params = initFuzzySearchMap(params, fuzzySearchPojo)
        return apiDocDao!!.getEntitysCount(params)
    }

    private fun initFuzzySearchMap(params: MutableMap<String, Any>, fuzzySearchPojo: ApiDocListPojo): MutableMap<String, Any> {
        params.put("del", 0)
        if (!StringUtil.isEmpty(fuzzySearchPojo.module)) {
            params.put("module", fuzzySearchPojo.module)
        }
        if (!StringUtil.isEmpty(fuzzySearchPojo.version)) {
            params.put("version", fuzzySearchPojo.version)
        }
        if (!StringUtil.isEmpty(fuzzySearchPojo.urlLike)) {
            params.put("urlLike", fuzzySearchPojo.urlLike)
        }
        if (!StringUtil.isEmpty(fuzzySearchPojo.apiNameLike)) {
            params.put("apiNameLike", fuzzySearchPojo.apiNameLike)
        }
        if (!StringUtil.isEmpty(fuzzySearchPojo.authorNameLike)) {
            params.put("authorNameLike", fuzzySearchPojo.authorNameLike)
        }
        if (fuzzySearchPojo.updateTimeStart > 0 && fuzzySearchPojo.updateTimeEnd > 0 &&
                fuzzySearchPojo.updateTimeEnd > fuzzySearchPojo.updateTimeStart) {
            params.put("updateTimeStart", fuzzySearchPojo.updateTimeStart)
            params.put("updateTimeEnd", fuzzySearchPojo.updateTimeEnd)
        }
        return params
    }

    companion object {

        // 按版本号的 id 倒叙排列
        private val VERSION_COMPARATOR = Comparator<ApiVersionEntity> { o1, o2 -> o2.id!!.compareTo(o1.id!!) }

        // 按模块的 id 倒叙排列
        private val MODULE_COMPARATOR = Comparator<SysModuleEntity> { o1, o2 -> o2.id!!.compareTo(o1.id!!) }
    }
}

