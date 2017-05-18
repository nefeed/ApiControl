package com.xbongbong.dingxbb.controller;

import com.alibaba.fastjson.JSON;
import com.xbongbong.dingxbb.entity.ApiDocEntity;
import com.xbongbong.dingxbb.model.ApiDocModel;
import com.xbongbong.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

    @RequestMapping(value = "/version", produces = "application/json")
    public void version(HttpServletRequest request,
                     HttpServletResponse response, Map<String, Object> modelMap)
            throws Exception {
        List<String> versionList = apiDocModel.findApiVersionList();
        returnSuccessJsonData(request, response, versionList);
    }

    @RequestMapping(value = "/module", produces = "application/json")
    public void module(HttpServletRequest request,
                        HttpServletResponse response, Map<String, Object> modelMap)
            throws Exception {
        List<String> moduleList = apiDocModel.findApiModuleList();
        returnSuccessJsonData(request, response, moduleList);
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
        modelMap.put("del", 0);
        Integer count = apiDocModel.getEntitysCount(modelMap);
        modelMap.clear();
        modelMap.put("count", count);
        returnSuccessJsonData(request, response, modelMap);
    }

    @RequestMapping(value = "/list", produces = "application/json")
    public void list(HttpServletRequest request,
                     HttpServletResponse response, Map<String, Object> modelMap)
            throws Exception {

        Integer page = Integer.parseInt(request.getParameter("page"));
        Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));
        if (page == 0) {
            page = 1;
        }
        if (pageSize == 0) {
            pageSize = 20;
        }
        List<ApiDocEntity> apiDocList = apiDocModel.findApiDocList(page, pageSize);
        returnSuccessJsonData(request, response, apiDocList);
    }

    @RequestMapping(value = "/detail", produces = "application/json")
    public void detail(HttpServletRequest request,
                       HttpServletResponse response, Map<String, Object> modelMap)
            throws Exception {

        Integer id = Integer.parseInt(request.getParameter("id"));
        if (id == 0) {
            returnJsonData(request, response, 100005, "缺少 Api 文档主键", modelMap);
            return;
        }
        returnSuccessJsonData(request, response, apiDocModel.getByKey(id));
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
                returnJsonData(request, response, 100004, "服务器删除旧 Markdown 文件失败！", modelMap);
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
        modelMap.put("markdown", markdownContent);
        try {
            FileWriter fileWriter = new FileWriter(apiDocFile);
            markdownContent = markdownContent.replaceAll("<br />", "\n");
            fileWriter.write(markdownContent);
            fileWriter.close(); // 关闭数据流
        } catch (IOException e) {
            e.printStackTrace();
        }
        returnSuccessJsonData(request, response, modelMap);
    }

    private ApiDocEntity analysisRequest(HttpServletRequest request, HttpServletResponse response, Map<String, Object> modelMap) {
        String params = request.getParameter("params");
        if (StringUtil.isEmpty(params)) {
            returnJsonData(request, response, 100005, "缺少必填参数", modelMap);
            return null;
        }
        ApiDocEntity apiDoc = JSON.parseObject(params, ApiDocEntity.class);
        if (StringUtil.isEmpty(apiDoc.getModule())) {
            returnJsonData(request, response, 100005, "缺少所属模块名称", modelMap);
            return null;
        }
        if (StringUtil.isEmpty(apiDoc.getName())) {
            returnJsonData(request, response, 100005, "缺少 Api 名称", modelMap);
            return null;
        }
        if (StringUtil.isEmpty(apiDoc.getUrl())) {
            returnJsonData(request, response, 100005, "缺少 Api 请求地址", modelMap);
            return null;
        }
        if (StringUtil.isEmpty(apiDoc.getUsername())) {
            returnJsonData(request, response, 100005, "缺少 Api 作者大名", modelMap);
            return null;
        }
        return apiDoc;
    }
}
