package com.xbongbong.dingxbb.controller;

import com.alibaba.fastjson.JSON;
import com.xbongbong.dingxbb.entity.ApiCaseEntity;
import com.xbongbong.dingxbb.model.ApiCaseModel;
import com.xbongbong.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
@RequestMapping("/api/case")
public class ApiCaseController extends BasicController {
    protected static final String TAG = "ApiCaseController";

    @Autowired
    private ApiCaseModel apiCaseModel;

    @RequestMapping(value = "/clone", produces = "application/json")
    public void version(HttpServletRequest request,
                     HttpServletResponse response, Map<String, Object> modelMap)
            throws Exception {
        ApiCaseEntity apiCase = analysisRequest(request, response, modelMap);
        Integer copyNum = Integer.parseInt(request.getParameter("copyNum"));
        if (apiCase == null) {
            return;
        }
        modelMap.put("code", apiCaseModel.save(apiCase.copyOne(copyNum)));
        modelMap.put("msg", "复制成功，请返回列表查看");
        returnSuccessJsonData(request, response, modelMap);
    }

    @RequestMapping(value = "/item", produces = "application/json")
    public void item(HttpServletRequest request,
                     HttpServletResponse response, Map<String, Object> modelMap)
            throws Exception {
        ApiCaseEntity apiCase = analysisRequest(request, response, modelMap);
        if (apiCase == null) {
            return;
        }
        modelMap.put("code", apiCaseModel.save(apiCase));
        modelMap.put("id", apiCase.getId());
        returnSuccessJsonData(request, response, modelMap);
    }

    @RequestMapping(value = "/count", produces = "application/json")
    public void count(HttpServletRequest request,
                      HttpServletResponse response, Map<String, Object> modelMap)
            throws Exception {
        modelMap.put("del", 0);
        Integer count = apiCaseModel.getEntitysCount(modelMap);
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
        List<ApiCaseEntity> apiCaseList = apiCaseModel.findApiCaseList(page, pageSize);
        returnSuccessJsonData(request, response, apiCaseList);
    }

    @RequestMapping(value = "/detail", produces = "application/json")
    public void detail(HttpServletRequest request,
                       HttpServletResponse response, Map<String, Object> modelMap)
            throws Exception {

        Integer id = Integer.parseInt(request.getParameter("id"));
        if (id == 0) {
            returnJsonData(request, response, 100005, "缺少 Api 测试用例主键", modelMap);
            return;
        }
        returnSuccessJsonData(request, response, apiCaseModel.getByKey(id));
    }

    @RequestMapping(value = "/delete", produces = "application/json")
    public void delete(HttpServletRequest request,
                       HttpServletResponse response, Map<String, Object> modelMap)
            throws Exception {

        Integer id = Integer.parseInt(request.getParameter("id"));
        if (id == 0) {
            returnJsonData(request, response, 100005, "缺少 Api 测试用例主键", modelMap);
            return;
        }
        returnSuccessJsonData(request, response, apiCaseModel.deleteByKey(id));
    }

    private ApiCaseEntity analysisRequest(HttpServletRequest request, HttpServletResponse response, Map<String, Object> modelMap) {
        String params = request.getParameter("params");
        if (StringUtil.isEmpty(params)) {
            returnJsonData(request, response, 100005, "缺少必填参数", modelMap);
            return null;
        }
        ApiCaseEntity apiCase = JSON.parseObject(params, ApiCaseEntity.class);
        if (StringUtil.isEmpty(apiCase.getCaseName())) {
            returnJsonData(request, response, 100005, "缺少用例名称名称", modelMap);
            return null;
        }
        if (StringUtil.isEmpty(apiCase.getExpectedContent())) {
            returnJsonData(request, response, 100005, "缺少预期返回参数", modelMap);
            return null;
        }
        return apiCase;
    }
}
