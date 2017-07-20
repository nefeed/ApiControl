package com.xbongbong.dingxbb.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xbongbong.dingxbb.entity.ApiDocEntity;
import com.xbongbong.dingxbb.enums.ErrcodeEnum;
import com.xbongbong.dingxbb.model.ApiDocModel;
import com.xbongbong.dingxbb.model.SysModuleModel;
import com.xbongbong.dingxbb.pojo.ApiDocListPojo;
import com.xbongbong.dingxbb.util.ExcelUtil;
import com.xbongbong.dingxbb.util.JsonFormatUtil;
import com.xbongbong.util.DateUtil;
import com.xbongbong.util.StringUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Gavin
 * E-mail: GavinChangCN@163.com
 * Desc:
 * Date: 2017-05-16
 * Time: 16:44
 */
@Controller
@RequestMapping("/api/doc")
public class ApiDocController extends BasicController {
    protected static final String TAG = "ApiDocController";

    @Autowired
    private ApiDocModel apiDocModel;
    @Autowired
    private SysModuleModel sysModuleModel;
    @Autowired
    private JsonFormatUtil jsonFormatUtil;
    @Autowired
    private ExcelUtil excelUtil;

    @RequestMapping(value = "/version", produces = "application/json")
    public void version(HttpServletRequest request,
                        HttpServletResponse response, Map<String, Object> modelMap)
            throws Exception {
        List<String> versionList = apiDocModel.findApiVersionList();
        returnSuccessJsonData(request, response, versionList);
    }

    @RequestMapping(value = "/version/item", produces = "application/json")
    public void versionItem(@RequestParam(required = true) String version, HttpServletRequest request,
                            HttpServletResponse response, Map<String, Object> modelMap)
            throws Exception {
        if (apiDocModel.itemVersion(version) != -1) {
            returnSuccessJsonData(request, response, version);
        } else {
            jsonOut(request, response, ErrcodeEnum.API_ERROR_100015.getCode(), "该版本号已经存在，请校验列表", null);
        }
    }

    @RequestMapping(value = "/module", produces = "application/json")
    public void module(HttpServletRequest request,
                       HttpServletResponse response, Map<String, Object> modelMap)
            throws Exception {
        List<String> moduleList = apiDocModel.findApiModuleList();
        returnSuccessJsonData(request, response, moduleList);
    }

    @RequestMapping(value = "/module/item", produces = "application/json")
    public void moduleItem(@RequestParam(required = true) String module, HttpServletRequest request,
                           HttpServletResponse response, Map<String, Object> modelMap)
            throws Exception {
        if (apiDocModel.itemModule(module) != -1) {
            returnSuccessJsonData(request, response, module);
        } else {
            jsonOut(request, response, ErrcodeEnum.API_ERROR_100015.getCode(), "已经存在类似模块，请校验列表", null);
        }
    }

    @RequestMapping(value = "/item", produces = "application/json")
    public void item(HttpServletRequest request,
                     HttpServletResponse response, Map<String, Object> modelMap)
            throws Exception {
        ApiDocEntity apiDoc = analysisRequest(request, response, modelMap);
        if (apiDoc == null) {
            return;
        }
        modelMap.put("code", apiDocModel.save(apiDoc));
        modelMap.put("id", apiDoc.getId());
        returnSuccessJsonData(request, response, modelMap);
    }

    @RequestMapping(value = "/count", produces = "application/json")
    public void count(HttpServletRequest request,
                      HttpServletResponse response, Map<String, Object> modelMap)
            throws Exception {
        Integer count = apiDocModel.getApiDocCount(new ApiDocListPojo());
        modelMap.clear();
        modelMap.put("count", count);
        returnSuccessJsonData(request, response, modelMap);
    }

    @RequestMapping(value = "/list", produces = "application/json")
    public void list(ApiDocListPojo fuzzySearchPojo,
                     HttpServletRequest request,
                     HttpServletResponse response, Map<String, Object> modelMap)
            throws Exception {
        if (fuzzySearchPojo.getPage() == 0) {
            fuzzySearchPojo.setPage(1);
        }
        if (fuzzySearchPojo.getPageSize() == 0) {
            fuzzySearchPojo.setPageSize(20);
        }
//        if (StringUtil.isEmpty(fuzzySearchPojo.getVersion()) && StringUtil.isEmpty(fuzzySearchPojo.getModule())
//                && StringUtil.isEmpty(fuzzySearchPojo.getApiNameLike()) && StringUtil.isEmpty(fuzzySearchPojo.getUrlLike())
//                && StringUtil.isEmpty(fuzzySearchPojo.getAuthorNameLike()) && fuzzySearchPojo.getUpdateTimeStart() == 0
//                && fuzzySearchPojo.getUpdateTimeEnd() == 0) {
//            jsonOut(request, response, ErrcodeEnum.API_ERROR_100005.getCode(), "请至少选择一个搜索项", modelMap);
//            return;
//        }
        if (fuzzySearchPojo.getUpdateTimeStart() > 0 && fuzzySearchPojo.getUpdateTimeEnd() > 0 &&
                fuzzySearchPojo.getUpdateTimeEnd() <= fuzzySearchPojo.getUpdateTimeStart()) {
            jsonOut(request, response, ErrcodeEnum.API_ERROR_100005.getCode(), "更新时间区间格式错误", modelMap);
            return;
        }
        List<ApiDocEntity> apiDocList = apiDocModel.findApiDocList(fuzzySearchPojo);
        returnSuccessJsonData(request, response, apiDocList);
    }

    @RequestMapping(value = "/detail", produces = "application/json")
    public void detail(@RequestParam(required = true) int id,
                       HttpServletRequest request,
                       HttpServletResponse response, Map<String, Object> modelMap)
            throws Exception {
        if (id == 0) {
            jsonOut(request, response, ErrcodeEnum.API_ERROR_100005.getCode(), "缺少 Api 文档主键", modelMap);
            return;
        }
        ApiDocEntity apiDoc = apiDocModel.getByKey(id);
        apiDoc.checkArray();
        returnSuccessJsonData(request, response, apiDoc);
    }

    @RequestMapping(value = "/outputMarkdown", produces = "application/json")
    public void outputMarkdown(HttpServletRequest request,
                               HttpServletResponse response, Map<String, Object> modelMap)
            throws Exception {
        ApiDocEntity apiDoc = analysisRequest(request, response, modelMap);
        if (apiDoc == null) {
            return;
        }

        modelMap.put("code", apiDocModel.save(apiDoc));
        modelMap.put("id", apiDoc.getId());

        String rootPath = System.getProperty("xbb-dingtalk-api");

        File apiDocFilePackage = new File(rootPath + "api_doc/");
        if (!apiDocFilePackage.exists() && !apiDocFilePackage.mkdirs()) {
            throw new IOException("无法创建文件夹 " + apiDocFilePackage);
        }
        String path = rootPath + String.format("api_doc/[%s]%s-v%s.md", apiDoc.getModule(), apiDoc.getName(), apiDoc.getVersion());
        File apiDocFile = new File(path);
        if (apiDocFile.exists()) {
            boolean result = apiDocFile.delete();
            if (!result) {
                jsonOut(request, response, 100004, "服务器删除旧 Markdown 文件失败！", modelMap);
            }
        }
        try {
            // 创建文件
            apiDocFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 下面是向文件file2里面写数据
        String markdownContent = apiDocModel.formatMarkdownContent(apiDoc);
        try {
            FileWriter fileWriter = new FileWriter(apiDocFile);
            fileWriter.write(markdownContent.replaceAll("<br />", "\n"));
            fileWriter.close(); // 关闭数据流
        } catch (IOException e) {
            e.printStackTrace();
        }

        modelMap.put("markdown", markdownContent);
        returnSuccessJsonData(request, response, modelMap);
    }

    private ApiDocEntity analysisRequest(HttpServletRequest request, HttpServletResponse response, Map<String, Object> modelMap) {
        String params = request.getParameter("params");
        if (StringUtil.isEmpty(params)) {
            jsonOut(request, response, ErrcodeEnum.API_ERROR_100005.getCode(), "缺少必填参数", modelMap);
            return null;
        }
        ApiDocEntity apiDoc = JSON.parseObject(params, ApiDocEntity.class);
        if (StringUtil.isEmpty(apiDoc.getModule())) {
            jsonOut(request, response, ErrcodeEnum.API_ERROR_100005.getCode(), "缺少所属模块名称", modelMap);
            return null;
        }
        if (StringUtil.isEmpty(apiDoc.getName())) {
            jsonOut(request, response, ErrcodeEnum.API_ERROR_100005.getCode(), "缺少 Api 名称", modelMap);
            return null;
        }
        if (StringUtil.isEmpty(apiDoc.getUrl())) {
            jsonOut(request, response, ErrcodeEnum.API_ERROR_100005.getCode(), "缺少 Api 请求地址", modelMap);
            return null;
        }
        if (StringUtil.isEmpty(apiDoc.getMemo())) {
            jsonOut(request, response, ErrcodeEnum.API_ERROR_100005.getCode(), "请至少在简要说明中填写Api的使用场景！", modelMap);
            return null;
        } else {
            String memo = apiDoc.getMemo();
            memo = StringUtil.replaceBlank(memo.replaceAll("使用场景：", ""));
            if (StringUtil.isEmpty(memo.trim())) {
                jsonOut(request, response, ErrcodeEnum.API_ERROR_100005.getCode(), "请至少在简要说明中填写Api的使用场景！", modelMap);
                return null;
            }
        }
        if (StringUtil.isEmpty(apiDoc.getUsername())) {
            jsonOut(request, response, ErrcodeEnum.API_ERROR_100005.getCode(), "缺少 Api 作者大名", modelMap);
            return null;
        }
        if (StringUtil.isEmpty(apiDoc.getParamsDemo())) {
            jsonOut(request, response, ErrcodeEnum.API_ERROR_100005.getCode(), "缺少请求 Demo，请务必填写，生成用例必须！\n(如若真无请求参数，请填\"{}\")", modelMap);
            return null;
        }
        if (StringUtil.isEmpty(apiDoc.getParamsDemo())) {
            apiDoc.setParamsDemo("{}");
        }
        try {
            JSON.parseObject(apiDoc.getParamsDemo(), JSONObject.class);
        } catch (Exception e) {
            System.out.println("json格式有误");
            jsonOut(request, response, ErrcodeEnum.API_ERROR_100005.getCode(), "请求 Demo 必须为 JSON 格式字符串，请校验！", modelMap);
            return null;
        }
        apiDoc.setParamsDemo(apiDoc.getParamsDemo().replaceAll("\r|\n", ""));
        apiDoc.setParamsDemo(jsonFormatUtil.formatJson2Str(apiDoc.getParamsDemo().trim()));

//        if (!(apiDoc.getParamsDemo().startsWith("{") && apiDoc.getParamsDemo().endsWith("}"))) {
//            jsonOut(request, response, ErrcodeEnum.API_ERROR_100005.getCode(), "请求 Demo 必须是 Json 格式字符串！", modelMap);
//            return null;
//        }
//        JSONObject json = JSON.parseObject(apiDoc.getParamsDemo());
//        if (!json.containsKey("corpid")) {
//            json.put("corpid", "1");
//        }
//        if (!json.containsKey("nowUserId")) {
//            json.put("nowUserId", "1");
//        }
//        apiDoc.setParamsDemo(JSON.toJSONString(json));
        if (StringUtil.isEmpty(apiDoc.getResponseDemo())) {
            jsonOut(request, response, ErrcodeEnum.API_ERROR_100005.getCode(), "缺少返回 Demo，请务必填写，生成用例必须！", modelMap);
            return null;
        } else {
            try {
                JSON.parseObject(apiDoc.getResponseDemo(), JSONObject.class);
            } catch (Exception e) {
                System.out.println("json格式有误");
                jsonOut(request, response, ErrcodeEnum.API_ERROR_100005.getCode(), "返回 Demo 必须为 JSON 格式字符串，请校验！", modelMap);
                return null;
            }
            apiDoc.setResponseDemo(apiDoc.getResponseDemo().replaceAll("\r|\n", ""));
            apiDoc.setResponseDemo(jsonFormatUtil.formatJson2Str(apiDoc.getResponseDemo().trim()));
        }
        // TODO 2017-05-25 因为返回 Demo 并非正确的 json 格式，无法解析
//        ResponseDemoPojo responseDemoPojo = JSON.parseObject(apiDoc.getResponseDemo(), ResponseDemoPojo.class);
//        if (responseDemoPojo == null || responseDemoPojo.getCode() == null) {
//            jsonOut(request, response, ErrcodeEnum.API_ERROR_100005.getCode(), "返回 Demo 缺少结果code", modelMap);
//            return null;
//        }
        apiDoc.setUrl(StringUtil.trim(apiDoc.getUrl()))
                .setModule(StringUtil.trim(apiDoc.getModule()));
        return apiDoc;
    }

    @RequestMapping(value = "/search/count", produces = "application/json")
    public void fuzzySearchCount(ApiDocListPojo fuzzySearchPojo,
                                 HttpServletRequest request,
                                 HttpServletResponse response, Map<String, Object> modelMap)
            throws Exception {
        Integer count = apiDocModel.getApiDocCount(fuzzySearchPojo);
        modelMap.clear();
        modelMap.put("count", count);
        returnSuccessJsonData(request, response, modelMap);
    }

    @RequestMapping(value = "/delete", produces = "application/json")
    public void delete(HttpServletRequest request,
                       HttpServletResponse response, Map<String, Object> modelMap)
            throws Exception {

        Integer id = Integer.parseInt(request.getParameter("id"));
        if (id == 0) {
            jsonOut(request, response, ErrcodeEnum.API_ERROR_100005.getCode(), "缺少 Api 文档主键", modelMap);
            return;
        }
        returnSuccessJsonData(request, response, apiDocModel.deleteByKey(id));
    }

    @RequestMapping(value = "/export4Testing")
    @ResponseBody
    public void exportFeedBack(HttpServletRequest request,
                               HttpServletResponse response) {

        String fileName = "Api列表校验-" + DateUtil.getString(DateUtil.getInt(), DateUtil.SDFMonthDay) + ".xls"; //文件名
        String sheetName = "Api列表校验";//sheet名

        String[] title = new String[]{"主键Id", "所属模块", "接口名称", "接口地址", "接口维护负责人", "更新时间", "接口备注", "校验"}; // 标题

        // TODO 获取需要导出到Excel的对象队列
        Map<String, Object> param = new HashMap<>();
        param.put("del", 0);
        param.put("orderByStr", "id DESC");
        param.put("groupByStr", "module");
        List<ApiDocEntity> list = apiDocModel.findEntitys(param);

        String[][] values = new String[list.size()][];
        for (int i = 0; i < list.size(); i++) {
            values[i] = new String[title.length];
            //将对象内容转换成string
            ApiDocEntity apiDoc = list.get(i);
            values[i][0] = "" + apiDoc.getId();
            values[i][1] = apiDoc.getModule();
            values[i][2] = apiDoc.getName();
            values[i][3] = apiDoc.getUrl();
            values[i][4] = apiDoc.getUsername();
            values[i][5] = DateUtil.getString(apiDoc.getUpdateTime());
            values[i][6] = apiDoc.getMemo();
            values[i][7] = "";

        }

        HSSFWorkbook wb = excelUtil.getHSSFWorkbook(sheetName, title, values, null);

        //将文件存到指定位置
        try {
            this.setResponseHeader(response, fileName);
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        returnSuccessJsonData(request, response, "");
    }

    /**
     * 输出excel文件
     *
     * @param response 请求返回
     * @param fileName 文件名
     */
    private void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(), "ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
