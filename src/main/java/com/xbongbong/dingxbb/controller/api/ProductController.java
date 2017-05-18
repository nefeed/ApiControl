package com.xbongbong.dingxbb.controller.api;

import java.util.ArrayList;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xbongbong.dingxbb.controller.BasicController;
import com.xbongbong.dingxbb.entity.DataDictionaryEntity;
import com.xbongbong.dingxbb.entity.ProductCategoryEntity;
import com.xbongbong.dingxbb.entity.ProductEntity;
import com.xbongbong.dingxbb.entity.RedundantFieldEntity;
import com.xbongbong.dingxbb.entity.RedundantFieldExplainEntity;
import com.xbongbong.dingxbb.entity.RedundantFieldTemplateEntity;
import com.xbongbong.dingxbb.enums.DictionaryTypeEnum;
import com.xbongbong.dingxbb.enums.ErrcodeEnum;
import com.xbongbong.dingxbb.enums.RedundantTemplateTypeEnum;
import com.xbongbong.dingxbb.helper.ApiHelper;
import com.xbongbong.dingxbb.helper.FastJsonHelper;
import com.xbongbong.dingxbb.helper.FormateRedundantValueHelper;
import com.xbongbong.dingxbb.model.DataDictionaryModel;
import com.xbongbong.dingxbb.model.ProductCategoryModel;
import com.xbongbong.dingxbb.model.ProductModel;
import com.xbongbong.dingxbb.model.RedundantFieldModel;
import com.xbongbong.dingxbb.model.RedundantFieldTemplateModel;
import com.xbongbong.util.DateUtil;
import com.xbongbong.util.PageHelper;
import com.xbongbong.util.StringUtil;

@Controller
@RequestMapping("/api/v1/product")
public class ProductController extends BasicController {

	private static final Logger LOG = LogManager.getLogger(ProductController.class);
	
	@Autowired
	private ProductModel productModel;
	@Autowired
	private ProductCategoryModel productCategoryModel;
	@Autowired
	private RedundantFieldTemplateModel redundantFieldTemplateModel;
	@Autowired
	private RedundantFieldModel redundantFieldModel;
	@Autowired
	private DataDictionaryModel dataDictionaryModel;
	@Autowired
	private ApiHelper apiHelper;
	
	/**
	 * 产品列表[全部数据]接口地址:http://example.com/api/v1/product/list.do 输入参数：data={}
	 * 成功输出：data={"errorcode":0,"msg":"获取成功","ret":""}
	 * 失败输出：data={"errorcode":*,"msg":"获取失败","ret":""}
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/list")
	public String productList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> modelMap)
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
		
		Integer page = StringUtil.StringToInteger(dataJson.getString("page"), 1);
		Integer pageSize = StringUtil.StringToInteger(dataJson.getString("pageSize"), 15);
		
		//一页数据最默认15条数据,最多50条记录,负数置为默认值,大于50条置为50条
		if(pageSize < 0) {
			pageSize = 15;
		} else if(pageSize > 50) {
			pageSize = 50;
		}

		//实际逻辑
		/*
		 * 1.通过corpId取客户模板 [corpId, type=1, enable=1]
		 * 2.若没有模板表示是默认模板；若已启用模板则表示有启用模板。取出模板解释列表List<Explain>
		 * 3.取出该模板对应的客户列表 + field
		 * 4.将3中的数据根据2中取出的模板解释进行渲染
		 * 5.将渲染后的列表返回
		 */
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("corpid", corpid);
		param.put("type", RedundantTemplateTypeEnum.PRODUCT.getCode());
		param.put("enable", 1);
		
		List<RedundantFieldTemplateEntity> templates = redundantFieldTemplateModel.findEntitys(param);
		
		RedundantFieldTemplateEntity template = templates.size() > 0 ? templates.get(0) : null;
		
		Integer templateId = template == null ? 0 : template.getId();
				
		param.clear();
		param.put("corpid", corpid);
		param.put("templateId", templateId);
		param.put("del", 0);
		param.put("page", page);
		
		PageHelper pageHelper = getPageHelper(param, productModel, pageSize);
		 
		@SuppressWarnings("unchecked")
		List<ProductEntity> list = (List<ProductEntity>) getEntityList(
				param, pageHelper, productModel);
		
		List<RedundantFieldExplainEntity> explains = redundantFieldModel.getRedundantFieldExplains(RedundantTemplateTypeEnum.PRODUCT.getCode(), corpid, templateId);
		
		List<RedundantFieldEntity> fieldEntityList = null;
		//数据字典
		Map<Integer,Map<Integer, DataDictionaryEntity>> dataDictionaryMap = dataDictionaryModel.getDataDictionaryMap(corpid);
		Map<Integer,DataDictionaryEntity> unitMap = dataDictionaryMap.get(DictionaryTypeEnum.PRODUCT_UNIT.getCode());
		//数据字典渲染
		for(ProductEntity entity : list) {
			String unitStr ="";
			if(unitMap.get(entity.getUnit()) != null){
				unitStr = unitMap.get(entity.getUnit()).getName();
			}
			entity.setUnit(unitStr);
		}
		
		if(templateId > 0) {
			List<Integer> productIds = new ArrayList<Integer>();
			for(ProductEntity product : list) {
				productIds.add(product.getId());
			}
			
			if(productIds.size() < 1) {
				productIds.add(-1);
			}
			
			param.clear();
			param.put("corpid", corpid);
			param.put("templateId", templateId);
			param.put("del", 0);
			param.put("refType", RedundantTemplateTypeEnum.PRODUCT.getCode());
			param.put("refIdIn", productIds);
			
			fieldEntityList = redundantFieldModel.findEntitys(param);
		}
		
		JSONArray retJSONArray = FormateRedundantValueHelper.formateRedundantValues(RedundantTemplateTypeEnum.PRODUCT.getCode(), explains, fieldEntityList, list);
		
		ret.put("productList", retJSONArray);
		
		ret.put("page", page);
		ret.put("pageSize", pageSize);
		ret.put("totalCount", pageHelper.getRowsCount());
		ret.put("errorCode", 0);
		ret.put("msg", "获取成功");
		
		return ret.toJSONString();

	}
	

	/**
	 * TODO 规格有点问题
	 * 新建产品接口地址:http://example.com/api/v1/product/add.do
	 * 输入参数：data={"staffId":"","name":"","country":"","province":"","city":"",
	 * "industry":"","district":"","address":"","longitude":"","latitude":"",
	 * "zipCode":"","website":"",
	 * "type":"","scale":"","isIndividual":"","importantDegree":"","phone":
	 * "[{'name':'座机号码','telNum':'0571-12345678'},{'name':'电话号码','telNum':'1832154786'}]"
	 * ,"fax":"","instruction":"","nameShort":"","id":"",
	 * "templateId":"","attrsObject":'{"attr1":"","attr2":"","attr3":"","attr4":
	 * "","attr5":"","attr6":"","attr7":"","attr8":"","attr9":"","attr10":"",
	 * "attr11":"","attr12":"","attr13":"","attr14":"","attr15":"","attr16":"",
	 * "attr17":"" ,"attr18":"","attr19":"","attr20":""}'}
	 * 成功输出：data={"errorcode":0,"msg":"操作成功","ret":""}
	 * 失败输出：data={"errorcode":*,"msg":"操作失败","ret":""}
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/add")
	public String productAdd(HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> modelMap) throws Exception {
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
		
		String productJsonStr = dataJson.getString("product");
		if(StringUtil.isEmpty(productJsonStr)) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_204003.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_204003.getMsg());
			return ret.toJSONString();
		}
		
		JSONObject productJson = null;
		try {
			productJson = JSON.parseObject(productJsonStr);
		} catch(Exception e) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_204004.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_204004.getMsg());
			return ret.toJSONString();
		}
		
		if(productJson == null) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_204003.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_204003.getMsg());
			return ret.toJSONString();
		}
		
		boolean flag = apiHelper.getInfo(corpid, modelMap, RedundantTemplateTypeEnum.PRODUCT.getCode(), productJson, true);
		
		if(!flag) {
			ret.put("errorCode", modelMap.get("errorCode"));
			ret.put("msg", modelMap.get("msg"));
			return ret.toJSONString();
		}
		
		Integer templateId = (Integer) modelMap.get("templateId");
		@SuppressWarnings("unchecked")
		Map<String,RedundantFieldExplainEntity> explainMap = (Map<String,RedundantFieldExplainEntity>) modelMap.get("explainMap");
		
		String productNoAttr = "产品编号";
		for(RedundantFieldExplainEntity explain : explainMap.values()) {
			if(explain.getAttr().equals("productNo")) {
				productNoAttr = explain.getAttrName();
				break;
			}
		}
		
		String productNo = productJson.getString(productNoAttr);
		if(StringUtil.isEmpty(productNo)){
			ret.put("errorCode", ErrcodeEnum.API_ERROR_204005.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_204005.getMsg());
			return ret.toJSONString();
		}
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("corpid", corpid);
		param.put("productNo", productNo);
		param.put("del", 0);
		
		Integer productCount = productModel.getEntitysCount(param);
		if(productCount > 0) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_204006.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_204006.getMsg());
			return ret.toJSONString();
		}
		
		Map<Integer,Map<Integer, DataDictionaryEntity>> allDataDictionarys = dataDictionaryModel.getDataDictionaryMap(corpid);
		
		Map<Integer, DataDictionaryEntity> unitMap = allDataDictionarys.get(DictionaryTypeEnum.PRODUCT_UNIT.getCode());
		
		Integer now = DateUtil.getInt();
		//客户
		ProductEntity productEntity = new ProductEntity();
		productEntity.setCorpid(corpid);
		productEntity.setTemplateId(templateId);
		productEntity.setAddTime(now);
		productEntity.setDel(0);
		
		//客户冗余信息
		RedundantFieldEntity fieldEntity = new RedundantFieldEntity();
		fieldEntity.setTemplateId(templateId);
		fieldEntity.setCorpid(corpid);
		fieldEntity.setRefType(RedundantTemplateTypeEnum.PRODUCT.getCode());
		fieldEntity.setRefId(0);
		
		productEntity.setUnitMap(unitMap);
		boolean setValueFlag = apiHelper.setValue(productJson, explainMap, productEntity, fieldEntity, modelMap);
		if(!setValueFlag){
			ret.put("errorCode", modelMap.get("errorCode"));
			ret.put("msg", modelMap.get("msg"));
			return ret.toJSONString();
		}
		
		productEntity.setUpdateTime(now);

		try {
			productModel.save(productEntity);
			if(templateId != 0){
				fieldEntity.setRefId(productEntity.getId());
				redundantFieldModel.save(fieldEntity);
			}
		} catch(Exception e) {
			LOG.error("数据库操作失败", e);
			ret.put("errorCode", ErrcodeEnum.API_ERROR_100004.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_100004.getMsg());
			return ret.toJSONString();
		}
		
		ret.put("productId", productEntity.getId());
		ret.put("errorCode", 0);
		ret.put("msg", "操作成功");
		return ret.toJSONString();
	}
	
	/**
	 * 产品修改接口地址:http://example.com/api/v1/product/update.do
	 * 输入参数：data={"staffId":"","name":"","country":"","province":"","city":"",
	 * "industry":"","district":"","address":"","longitude":"","latitude":"",
	 * "zipCode":"","website":"",
	 * "type":"","scale":"","isIndividual":"","importantDegree":"","phone":
	 * "[{'name':'座机号码','telNum':'0571-12345678'},{'name':'电话号码','telNum':'1832154786'}]"
	 * ,"fax":"","instruction":"","nameShort":"","id":"",
	 * "templateId":"","attrsObject":'{"attr1":"","attr2":"","attr3":"","attr4":
	 * "","attr5":"","attr6":"","attr7":"","attr8":"","attr9":"","attr10":"",
	 * "attr11":"","attr12":"","attr13":"","attr14":"","attr15":"","attr16":"",
	 * "attr17":"" ,"attr18":"","attr19":"","attr20":""}'}
	 * 成功输出：data={"errorcode":0,"msg":"操作成功","ret":""}
	 * 失败输出：data={"errorcode":*,"msg":"操作失败","ret":""}
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/update")
	public String productUpdate(HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> modelMap) throws Exception {
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
		
		String productJsonStr = dataJson.getString("product");
		if(StringUtil.isEmpty(productJsonStr)) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_204003.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_204003.getMsg());
			return ret.toJSONString();
		}
		
		JSONObject productJson = null;
		try {
			productJson = JSON.parseObject(productJsonStr);
		} catch(Exception e) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_204004.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_204004.getMsg());
			return ret.toJSONString();
		}
		
		if(productJson == null) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_204003.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_204003.getMsg());
			return ret.toJSONString();
		}
		
		Integer productId = productJson.getInteger("productId");
		if(productId == null || productId <= 0) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_204001.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_204001.getMsg());
			return ret.toJSONString();
		}
		
		//产品
		ProductEntity productEntity = productModel.getByKey(productId, corpid);
		if(productEntity == null) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_204002.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_204002.getMsg());
			return ret.toJSONString();
		}
		
		boolean flag = apiHelper.getInfo(corpid, modelMap, RedundantTemplateTypeEnum.PRODUCT.getCode(), productJson, false);
		
		if(!flag) {
			ret.put("errorCode", modelMap.get("errorCode"));
			ret.put("msg", modelMap.get("msg"));
			return ret.toJSONString();
		}
		
		Integer templateId = (Integer) modelMap.get("templateId");
		@SuppressWarnings("unchecked")
		Map<String,RedundantFieldExplainEntity> explainMap = (Map<String,RedundantFieldExplainEntity>) modelMap.get("explainMap");
		
		String productNoAttr = "产品编号";
		for(RedundantFieldExplainEntity explain : explainMap.values()) {
			if(explain.getAttr().equals("productNo")) {
				productNoAttr = explain.getAttrName();
				break;
			}
		}
		
		String productNo = productJson.getString(productNoAttr);
		if(!StringUtil.isEmpty(productNo)){
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("corpid", corpid);
			param.put("productNo", productNo);
			param.put("del", 0);
			
			//非本产品
			param.put("idNeg", productId);
			
			Integer productCount = productModel.getEntitysCount(param);
			if(productCount > 0) {
				ret.put("errorCode", ErrcodeEnum.API_ERROR_204006.getCode());
				ret.put("msg", ErrcodeEnum.API_ERROR_204006.getMsg());
				return ret.toJSONString();
			}
		}
		
		RedundantFieldEntity fieldEntity = redundantFieldModel.getByRefTypeAndRefId(corpid, RedundantTemplateTypeEnum.PRODUCT.getCode(), productId);
		if(fieldEntity == null) {
			fieldEntity = new RedundantFieldEntity();
			fieldEntity.setTemplateId(templateId);
			fieldEntity.setCorpid(corpid);
			fieldEntity.setRefType(RedundantTemplateTypeEnum.PRODUCT.getCode());
			fieldEntity.setRefId(0);
		}
		
		Map<Integer,Map<Integer, DataDictionaryEntity>> allDataDictionarys = dataDictionaryModel.getDataDictionaryMap(corpid);
		
		Map<Integer, DataDictionaryEntity> unitMap = allDataDictionarys.get(DictionaryTypeEnum.PRODUCT_UNIT.getCode());
		
		productEntity.setUnitMap(unitMap);
		boolean setValueFlag = apiHelper.setValue(productJson, explainMap, productEntity, fieldEntity, modelMap);
		if(!setValueFlag){
			ret.put("errorCode", modelMap.get("errorCode"));
			ret.put("msg", modelMap.get("msg"));
			return ret.toJSONString();
		}
		
		Integer now = DateUtil.getInt();
		productEntity.setUpdateTime(now);
		
		try {
			productModel.save(productEntity);
			if(templateId != 0){
				fieldEntity.setRefId(productEntity.getId());
				redundantFieldModel.save(fieldEntity);
			}
		} catch(Exception e) {
			LOG.error("数据库操作失败", e);
			ret.put("errorCode", ErrcodeEnum.API_ERROR_100004.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_100004.getMsg());
			return ret.toJSONString();
		}
		
		ret.put("errorCode", 0);
		ret.put("msg", "操作成功");
		return ret.toJSONString();
	}
	
	/**
	 * 获取产品详情接口地址:http://example.com/api/v1/product/get.do
	 * 输入参数：data={"staffId":"","customerId":""}
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
	public String productGet(HttpServletRequest request, HttpServletResponse response, Map<String, Object> modelMap)
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
		
		Integer productId = StringUtil.StringToInteger(dataJson.getString("productId"), 0);
		
		if(productId == null || productId <= 0) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_204001.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_204001.getMsg());
			return ret.toJSONString();
		}
		
		ProductEntity product = productModel.getByKey(productId, corpid);
		
		if(product == null) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_204002.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_204002.getMsg());
			return ret.toJSONString();
		}
		
		//模板ID
		Integer templateId = product.getTemplateId();
		
		RedundantFieldEntity fieldEntity = redundantFieldModel.getByRefTypeAndRefId(corpid, RedundantTemplateTypeEnum.PRODUCT.getCode(), productId);
		
		List<RedundantFieldExplainEntity> explains = redundantFieldModel.getRedundantFieldExplains(RedundantTemplateTypeEnum.PRODUCT.getCode(), corpid, templateId);
		
		//数据字典
		Map<Integer,Map<Integer, DataDictionaryEntity>> dataDictionaryMap = dataDictionaryModel.getDataDictionaryMap(corpid);
		Map<Integer, DataDictionaryEntity> unitMap = dataDictionaryMap.get(DictionaryTypeEnum.PRODUCT_UNIT.getCode());
		//数据字典渲染
		String unitStr ="";
		if(unitMap.get(product.getUnit()) != null){
			unitStr = unitMap.get(product.getUnit()).getName();
		}
		product.setUnit(unitStr);
		
		JSONObject retJSON = FormateRedundantValueHelper.formateRedundantValue(RedundantTemplateTypeEnum.PRODUCT.getCode(), explains, fieldEntity, product);
		
		ret.put("contact", retJSON);
		
		ret.put("errorCode", 0);
		ret.put("msg", "获取成功");
		return ret.toJSONString();
	}
	
	/**
	 * 产品删除接口地址:http://example.com/api/v1/product/delete.do
	 * 输入参数：data={"staffId":"","customerId":""}
	 * 成功输出：data={"errorcode":0,"msg":"获取成功","ret":""}
	 * 失败输出：data={"errorcode":*,"msg":"获取失败","ret":""}
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/delete")
	public String productDelete(HttpServletRequest request, HttpServletResponse response, Map<String, Object> modelMap)
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
		
		Integer productId = StringUtil.StringToInteger(dataJson.getString("productId"), 0);
		
		if(productId == null || productId <= 0) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_204001.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_204001.getMsg());
			return ret.toJSONString();
		}
		
		ProductEntity product = productModel.getByKey(productId, corpid);
		
		if(product == null) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_204002.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_204002.getMsg());
			return ret.toJSONString();
		}
		
		if(product.getDel().equals(1)) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_204013.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_204013.getMsg());
			return ret.toJSONString();
		}
		
		try {
			productModel.deleteByKey(productId, corpid);
		} catch(Exception e) {
			LOG.error("数据库操作失败", e);
			ret.put("errorCode", ErrcodeEnum.API_ERROR_100004.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_100004.getMsg());
			return ret.toJSONString();
		}
		
		ret.put("productId", productId);
		ret.put("errorCode", 0);
		ret.put("msg", "删除成功");
		return ret.toJSONString();
	}
	
	/**
	 * 产品分类列表[全部数据]接口地址:http://example.com/api/v1/product/category/list.do 输入参数：data={}
	 * 成功输出：data={"errorcode":0,"msg":"获取成功","ret":""}
	 * 失败输出：data={"errorcode":*,"msg":"获取失败","ret":""}
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/category/list")
	public String productCategoryList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> modelMap)
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
		
		Integer page = StringUtil.StringToInteger(dataJson.getString("page"), 1);
		Integer pageSize = StringUtil.StringToInteger(dataJson.getString("pageSize"), 50);//产品分类默认分页设为50,实体比较简单
		
		//一页数据最默认50条数据,最多50条记录,负数置为默认值,大于50条置为50条
		if(pageSize < 0) {
			pageSize = 50;
		} else if(pageSize > 50) {
			pageSize = 50;
		}
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("corpid", corpid);
		param.put("del", 0);
		param.put("page", page);
		
		PageHelper pageHelper = getPageHelper(param, productCategoryModel, pageSize);
		 
		@SuppressWarnings("unchecked")
		List<ProductCategoryEntity> list = (List<ProductCategoryEntity>) getEntityList(param, pageHelper, productCategoryModel);
		
		String[] productFilterStr = {"id", "parentId", "name", "addTime"}; //保留字段
		ret.put("productCategoryList", FastJsonHelper.parseIncludeObject(ProductCategoryEntity.class, productFilterStr, list));
		
		ret.put("page", page);
		ret.put("pageSize", pageSize);
		ret.put("totalCount", pageHelper.getRowsCount());
		ret.put("errorCode", 0);
		ret.put("msg", "获取成功");
		
		return ret.toJSONString();
	}
	
	/**
	 * 产品分类添加接口地址:http://example.com/api/v1/product/category/add.do
	 * 输入参数：data={"staffId":"","name":"","country":"","province":"","city":"",
	 * "industry":"","district":"","address":"","longitude":"","latitude":"",
	 * "zipCode":"","website":"",
	 * "type":"","scale":"","isIndividual":"","importantDegree":"","phone":
	 * "[{'name':'座机号码','telNum':'0571-12345678'},{'name':'电话号码','telNum':'1832154786'}]"
	 * ,"fax":"","instruction":"","nameShort":"","id":"",
	 * "templateId":"","attrsObject":'{"attr1":"","attr2":"","attr3":"","attr4":
	 * "","attr5":"","attr6":"","attr7":"","attr8":"","attr9":"","attr10":"",
	 * "attr11":"","attr12":"","attr13":"","attr14":"","attr15":"","attr16":"",
	 * "attr17":"" ,"attr18":"","attr19":"","attr20":""}'}
	 * 成功输出：data={"errorcode":0,"msg":"操作成功","ret":""}
	 * 失败输出：data={"errorcode":*,"msg":"操作失败","ret":""}
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/category/add")
	public String productCategoryAdd(HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> modelMap) throws Exception {
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
		
		String name = dataJson.getString("name");
		if(StringUtil.isEmpty(name)) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_204007.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_204007.getMsg());
			return ret.toJSONString();
		}
		
		Integer parentId = StringUtil.StringToInteger(dataJson.getString("parentId"), 0);
		
		if(parentId > 0) {
			ProductCategoryEntity parentCategory = productCategoryModel.getByKey(parentId, corpid);
			if(parentCategory == null) {
				ret.put("errorCode", ErrcodeEnum.API_ERROR_204008.getCode());
				ret.put("msg", ErrcodeEnum.API_ERROR_204008.getMsg());
				return ret.toJSONString();
			}
			
			if(parentCategory.getParentId() > 0) {
				ret.put("errorCode", ErrcodeEnum.API_ERROR_204009.getCode());
				ret.put("msg", ErrcodeEnum.API_ERROR_204009.getMsg());
				return ret.toJSONString();
			}
		}
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("corpid", corpid);
		param.put("name", name);
		param.put("parentId", parentId);
		param.put("del", 0);
		
		Integer categoryCount = productCategoryModel.getEntitysCount(param);
		if(categoryCount > 0) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_204010.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_204010.getMsg());
			return ret.toJSONString();
		}
		
		int now = DateUtil.getInt();
		
		ProductCategoryEntity newCategory = new ProductCategoryEntity();
		newCategory.setCorpid(corpid);
		newCategory.setDel(0);
		newCategory.setName(name);
		newCategory.setParentId(parentId);
		newCategory.setUpdateTime(now);
		newCategory.setAddTime(now);
		
		try {
			productCategoryModel.insert(newCategory);
		} catch(Exception e) {
			LOG.error("数据库操作失败", e);
			ret.put("errorCode", ErrcodeEnum.API_ERROR_100004.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_100004.getMsg());
			return ret.toJSONString();
		}
		
		ret.put("categoryId", newCategory.getId());
		ret.put("errorCode", 0);
		ret.put("msg", "操作成功");
		return ret.toJSONString();
	}
	
	/**
	 * 产品分类更新接口地址:http://example.com/api/v1/product/category/update.do
	 * 输入参数：data={"staffId":"","name":"","country":"","province":"","city":"",
	 * "industry":"","district":"","address":"","longitude":"","latitude":"",
	 * "zipCode":"","website":"",
	 * "type":"","scale":"","isIndividual":"","importantDegree":"","phone":
	 * "[{'name':'座机号码','telNum':'0571-12345678'},{'name':'电话号码','telNum':'1832154786'}]"
	 * ,"fax":"","instruction":"","nameShort":"","id":"",
	 * "templateId":"","attrsObject":'{"attr1":"","attr2":"","attr3":"","attr4":
	 * "","attr5":"","attr6":"","attr7":"","attr8":"","attr9":"","attr10":"",
	 * "attr11":"","attr12":"","attr13":"","attr14":"","attr15":"","attr16":"",
	 * "attr17":"" ,"attr18":"","attr19":"","attr20":""}'}
	 * 成功输出：data={"errorcode":0,"msg":"操作成功","ret":""}
	 * 失败输出：data={"errorcode":*,"msg":"操作失败","ret":""}
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/category/update")
	public String productCategoryUpdate(HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> modelMap) throws Exception {
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
		
		Integer categoryId = StringUtil.StringToInteger(dataJson.getString("categoryId"));
		if(categoryId == null || categoryId <= 0) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_204011.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_204011.getMsg());
			return ret.toJSONString();
		}
		
		String name = dataJson.getString("name");
		if(StringUtil.isEmpty(name)) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_204007.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_204007.getMsg());
			return ret.toJSONString();
		}
		
		//产品
		ProductCategoryEntity productCategoryEntity = productCategoryModel.getByKey(categoryId, corpid);
		if(productCategoryEntity == null) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_204012.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_204012.getMsg());
			return ret.toJSONString();
		}
		
		productCategoryEntity.setName(name);
		
		try {
			productCategoryModel.update(productCategoryEntity);
		} catch(Exception e) {
			LOG.error("数据库操作失败", e);
			ret.put("errorCode", ErrcodeEnum.API_ERROR_100004.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_100004.getMsg());
			return ret.toJSONString();
		}
		
		ret.put("errorCode", 0);
		ret.put("msg", "操作成功");
		return ret.toJSONString();
	}
	
	/**
	 * 产品分类删除接口地址:http://example.com/api/v1/product/category/delete.do
	 * 输入参数：data={"staffId":"","customerId":""}
	 * 成功输出：data={"errorcode":0,"msg":"获取成功","ret":""}
	 * 失败输出：data={"errorcode":*,"msg":"获取失败","ret":""}
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/category/delete")
	public String productCategoryDelete(HttpServletRequest request, HttpServletResponse response, Map<String, Object> modelMap)
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
		
		Integer categoryId = StringUtil.StringToInteger(dataJson.getString("categoryId"), 0);
		
		if(categoryId == null || categoryId <= 0) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_204011.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_204011.getMsg());
			return ret.toJSONString();
		}
		
		ProductCategoryEntity productCategory = productCategoryModel.getByKey(categoryId, corpid);
		
		if(productCategory == null) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_204012.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_204012.getMsg());
			return ret.toJSONString();
		}
		
		if(productCategory.getDel().equals(1)) {
			ret.put("categoryId", categoryId);
			ret.put("errorCode", 0);
			ret.put("msg", "删除成功");
			return ret.toJSONString();
		}
		
		//该分类下有子分类不让删除
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("corpid", corpid);
		param.put("parentId", categoryId);
		param.put("del", 0);
		
		Integer childrenCount = productCategoryModel.getEntitysCount(param);
		
		if(childrenCount > 0) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_204014.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_204014.getMsg());
			return ret.toJSONString();
		}
		
		param.clear();
		param.put("corpid", corpid);
		param.put("categoryId", categoryId);
		param.put("del", 0);
		
		Integer productCount = productModel.getEntitysCount(param);
		
		if(productCount > 0) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_204015.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_204015.getMsg());
			return ret.toJSONString();
		}
		
		try {
			productCategoryModel.deleteByKey(categoryId, corpid);
		} catch(Exception e) {
			LOG.error("数据库操作失败", e);
			ret.put("errorCode", ErrcodeEnum.API_ERROR_100004.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_100004.getMsg());
			return ret.toJSONString();
		}
		
		ret.put("categoryId", categoryId);
		ret.put("errorCode", 0);
		ret.put("msg", "删除成功");
		return ret.toJSONString();
	}
}
