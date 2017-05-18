package com.xbongbong.dingxbb.controller.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.xbongbong.dingxbb.controller.BasicController;
import com.xbongbong.dingxbb.entity.RedundantFieldExplainEntity;
import com.xbongbong.dingxbb.entity.RedundantFieldTemplateEntity;
import com.xbongbong.dingxbb.enums.ErrcodeEnum;
import com.xbongbong.dingxbb.helper.ApiHelper;
import com.xbongbong.dingxbb.helper.FastJsonHelper;
import com.xbongbong.dingxbb.model.RedundantFieldModel;
import com.xbongbong.dingxbb.model.RedundantFieldTemplateModel;
import com.xbongbong.util.StringUtil;

@Controller
@RequestMapping("/api/v1/template")
public class TemplateController extends BasicController {
	
	private static final Logger LOG = LogManager.getLogger(TemplateController.class);
	
	@Autowired
	private RedundantFieldModel redundantFieldModel;
	@Autowired
	private RedundantFieldTemplateModel redundantFieldTemplateModel;
	@Autowired
	private ApiHelper apiHelper;
	
	/**
	 * 获取当前启用的模板详情接口地址:http://example.com/api/v1/template/get.do 输入参数：data={}
	 * 成功输出：data={"errorcode":0,"msg":"获取成功","ret":""}
	 * 失败输出：data={"errorcode":*,"msg":"获取失败","ret":""}
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/get")
	public String templateGet(HttpServletRequest request, HttpServletResponse response, Map<String, Object> modelMap)
			throws Exception {
		//用于返回的JSON
		JSONObject ret = new JSONObject();
		//检查每个sessionID访问频次
		if(!apiHelper.checkFrequency(request, response, ret)) {
			return ret.toJSONString();
		}
		
		if (request.getMethod().equals("GET")) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_100001.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_100001.getMsg());
			return ret.toJSONString();
		}
		
		//signature 放在header
		String signature = request.getHeader("sign");
		String data = request.getParameter("data");
		JSONObject dataJson = null;
		
		if (StringUtil.isEmpty(data)) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_100006.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_100006.getMsg());
			return ret.toJSONString();
		}
		
		try{
			dataJson =  JSONObject.parseObject(data);
		} catch (Exception e) {
			LOG.warn("data json数据解析失败", e);
			ret.put("errorCode", ErrcodeEnum.API_ERROR_100007.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_100007.getMsg());
			return ret.toJSONString();
		}
		
		String corpid = dataJson.getString("corpid");
		
		if(StringUtil.isEmpty(corpid)) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_100002.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_100002.getMsg());
			return ret.toJSONString();
		}
		//验证signature, 这个方法如果验证不通过会在ret中置入错误码和错误消息
		if(!apiHelper.checkToken(data, corpid, signature, ret)){
			return ret.toJSONString();
		}
		
		Integer refType = dataJson.getInteger("refType");
		if(refType == null || refType < 1 || refType > 5) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_205001.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_205001.getMsg());
			return ret.toJSONString();
		}
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("corpid", corpid);
		param.put("type", refType);
		param.put("enable", 1);
		
		List<RedundantFieldTemplateEntity> templates = redundantFieldTemplateModel.findEntitys(param);
		
		RedundantFieldTemplateEntity template = templates.size() > 0 ? templates.get(0) : null;
		
		Integer templateId = template == null ? 0 : template.getId();
		
		List<RedundantFieldExplainEntity> explains = redundantFieldModel.getRedundantFieldExplains(refType, corpid, templateId);

		String[] explainsFilterStr = {"required","sort", "attr", "attrName", "fieldType", "initValue", "enable", "isRedundant"}; //保留字段
		ret.put("explains", FastJsonHelper.parseIncludeObject(RedundantFieldExplainEntity.class, explainsFilterStr, explains));
		
		ret.put("errorCode", 0);
		ret.put("msg", "获取成功");
		
		return ret.toJSONString();
		
	}
}
