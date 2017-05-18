package com.xbongbong.dingxbb.controller.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xbongbong.dingxbb.controller.BasicController;
import com.xbongbong.dingxbb.entity.ContactEntity;
import com.xbongbong.dingxbb.entity.ContractEntity;
import com.xbongbong.dingxbb.entity.CustomerEntity;
import com.xbongbong.dingxbb.entity.CustomerUserEntity;
import com.xbongbong.dingxbb.entity.DataDictionaryEntity;
import com.xbongbong.dingxbb.entity.OpportunityEntity;
import com.xbongbong.dingxbb.entity.RedundantFieldEntity;
import com.xbongbong.dingxbb.entity.RedundantFieldExplainEntity;
import com.xbongbong.dingxbb.entity.RedundantFieldTemplateEntity;
import com.xbongbong.dingxbb.entity.UserEntity;
import com.xbongbong.dingxbb.enums.CompanyConfigEnum;
import com.xbongbong.dingxbb.enums.DictionaryTypeEnum;
import com.xbongbong.dingxbb.enums.ErrcodeEnum;
import com.xbongbong.dingxbb.enums.RedundantTemplateTypeEnum;
import com.xbongbong.dingxbb.helper.ApiHelper;
import com.xbongbong.dingxbb.helper.ConfigConstant;
import com.xbongbong.dingxbb.helper.FastJsonHelper;
import com.xbongbong.dingxbb.helper.FormateRedundantValueHelper;
import com.xbongbong.dingxbb.helper.JedisUtils;
import com.xbongbong.dingxbb.model.CompanyConfigModel;
import com.xbongbong.dingxbb.model.ContactModel;
import com.xbongbong.dingxbb.model.ContactUserModel;
import com.xbongbong.dingxbb.model.ContractModel;
import com.xbongbong.dingxbb.model.CustomerModel;
import com.xbongbong.dingxbb.model.CustomerUserModel;
import com.xbongbong.dingxbb.model.DataDictionaryModel;
import com.xbongbong.dingxbb.model.OpportunityModel;
import com.xbongbong.dingxbb.model.OpportunityProductModel;
import com.xbongbong.dingxbb.model.OpportunityUserModel;
import com.xbongbong.dingxbb.model.RedundantFieldModel;
import com.xbongbong.dingxbb.model.RedundantFieldTemplateModel;
import com.xbongbong.dingxbb.model.UserModel;
import com.xbongbong.util.DateUtil;
import com.xbongbong.util.PageHelper;
import com.xbongbong.util.StringUtil;

@Controller
@RequestMapping("/api/v1/customer")
public class CustomerController extends BasicController {

	private static final Logger LOG = LogManager.getLogger(CustomerController.class);

	@Autowired
	private CustomerModel customerModel;
	@Autowired
	private CustomerUserModel customerUserModel;
	@Autowired
	private ContactModel contactModel;
	@Autowired
	private ContactUserModel contactUserModel;
	@Autowired
	private ContractModel contractModel;
	@Autowired
	private OpportunityModel opportunityModel;
	@Autowired
	private OpportunityUserModel opportunityUserModel;
	@Autowired
	private OpportunityProductModel opportunityProductModel;
	@Autowired
	private RedundantFieldTemplateModel redundantFieldTemplateModel;
	@Autowired
	private RedundantFieldModel redundantFieldModel;
	@Autowired
	private DataDictionaryModel dataDictionaryModel;
	@Autowired
	private CompanyConfigModel companyConfigModel;
	@Autowired
	private UserModel userModel;
	@Autowired
	private ApiHelper apiHelper;

	/**
	 * 客户列表[全部数据]接口地址:http://example.com/api/v1/customer/list.do 输入参数：data= {"corpid":"dingxxxxxx", "page":1, "pageSize":15}
	 * 成功输出：data={"errorcode":0,"msg":"获取成功","ret":{customerList[{}], "errorCode": 0,"msg": "获取成功","page": 1,"pageSize": 15,"totalCount": xxx}}
	 * 失败输出：data={"errorcode":*,"msg":"获取失败","ret":""}
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/list")
	public String customerList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> modelMap)
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
			dataJson = JSONObject.parseObject(data);
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
		
		//筛选字段：客户名称筛选
		String name = dataJson.getString("name");
		//筛选字段:客户电话号码
		String phone = dataJson.getString("phone");
		//筛选字段：客户更新时间，查询大于该更新时间的数据
		Integer updateTimeStart = dataJson.getInteger("updateTimeStart");
		
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
		param.put("type", RedundantTemplateTypeEnum.CUSTOMER.getCode());
		param.put("enable", 1);
		
		List<RedundantFieldTemplateEntity> templates = redundantFieldTemplateModel.findEntitys(param);
		
		RedundantFieldTemplateEntity template = templates.size() > 0 ? templates.get(0) : null;
		
		Integer templateId = template == null ? 0 : template.getId();
				
		param.clear();
		param.put("corpid", corpid);
		param.put("templateId", templateId);
		param.put("del", 0);
		param.put("page", page);
		//更新时间倒序
		param.put("orderByStr", "update_time DESC");
		
		//若客户名称不为空，则添加客户名称筛选
		if(!StringUtil.isEmpty(name)) {
			param.put("nameLike", name);
		}
		
		if(!StringUtil.isEmpty(phone)) {
			param.put("phoneLike", phone);
		}

		//根据更新时间进行筛选
		if(updateTimeStart != null && updateTimeStart > 0) {
			param.put("updateTimeStart", updateTimeStart);
		}
		
		PageHelper pageHelper = getPageHelper(param, customerModel, pageSize);
		 
		@SuppressWarnings("unchecked")
		List<CustomerEntity> list = (List<CustomerEntity>) getEntityList(
				param, pageHelper, customerModel);
		
		List<RedundantFieldExplainEntity> explains = redundantFieldModel.getRedundantFieldExplains(RedundantTemplateTypeEnum.CUSTOMER.getCode(), corpid, templateId);
		
		List<RedundantFieldEntity> fieldEntityList = null;
		//数据字典
		Map<Integer,Map<Integer, DataDictionaryEntity>> dataDictionaryMap = dataDictionaryModel.getDataDictionaryMap(corpid);
		Map<Integer,DataDictionaryEntity> typeMap = dataDictionaryMap.get(DictionaryTypeEnum.CUSTOMER_TYPE.getCode());
		Map<Integer,DataDictionaryEntity> industryMap = dataDictionaryMap.get(DictionaryTypeEnum.CUSTOMER_INDUSTRY.getCode());
		Map<Integer,DataDictionaryEntity> individualMap = dataDictionaryMap.get(DictionaryTypeEnum.CUSTOMER_IS_INDIVIDUAL.getCode());
		Map<Integer,DataDictionaryEntity> genreMap = dataDictionaryMap.get(DictionaryTypeEnum.CUSTOMER_GENRE.getCode());
		//数据字典渲染
		for(CustomerEntity entity : list) {
			String typeStr ="";
			if(typeMap.get(entity.getType()) != null){
				typeStr = typeMap.get(entity.getType()).getName();
			}
			entity.setTypeStr(typeStr);
			
			String industryStr ="";
			if(industryMap.get(entity.getIndustry()) != null){
				industryStr = industryMap.get(entity.getIndustry()).getName();
			}
			entity.setIndustryStr(industryStr);
			
			String isIndividualStr ="";
			if(individualMap.get(entity.getIsIndividual()) != null){
				isIndividualStr = individualMap.get(entity.getIsIndividual()).getName();
			}
			entity.setIsIndividualStr(isIndividualStr);
			
			String genreStr ="";
			if(genreMap.get(entity.getGenre()) != null){
				genreStr = genreMap.get(entity.getGenre()).getName();
			}
			entity.setGenreStr(genreStr);
		}
		
		if(templateId > 0) {
			List<Integer> customerIds = new ArrayList<Integer>();
			for(CustomerEntity customer : list) {
				customerIds.add(customer.getId());
			}
			
			if(customerIds.size() < 1) {
				customerIds.add(-1);
			}
			
			param.clear();
			param.put("corpid", corpid);
			param.put("templateId", templateId);
			param.put("del", 0);
			param.put("refType", RedundantTemplateTypeEnum.CUSTOMER.getCode());
			param.put("refIdIn", customerIds);
			
			fieldEntityList = redundantFieldModel.findEntitys(param);
		}
		
		JSONArray retJSONArray = FormateRedundantValueHelper.formateRedundantValues(RedundantTemplateTypeEnum.CUSTOMER.getCode(), explains, fieldEntityList, list);
		
		ret.put("customerList", retJSONArray);
		
		ret.put("page", page);
		ret.put("pageSize", pageSize);
		ret.put("totalCount", pageHelper.getRowsCount());
		ret.put("errorCode", 0);
		ret.put("msg", "获取成功");
		
		return ret.toJSONString();

	}
	
	/**
	 * 客户列表[简要数据]接口地址:http://example.com/api/v1/customer/simpleList.do 输入参数：data={"corpid":"dingxxxxxx", "page":1, "pageSize":15}
	 * 成功输出：data={"errorcode":0,"msg":"获取成功","ret":{customerList[{}], "errorCode": 0,"msg": "获取成功","page": 1,"pageSize": 15,"totalCount": xxx}}
	 * 失败输出：data={"errorcode":*,"msg":"获取失败","ret":""}
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/simpleList")
	public String customerSimpleList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> modelMap)
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
		
		//筛选字段：客户名称筛选
		String name = dataJson.getString("name");
		//筛选字段:客户电话号码
		String phone = dataJson.getString("phone");
		
		//返回所有数据，而不是当前模板的数据
		Map<String, Object> param = new HashMap<String, Object>();
//		param.put("corpid", corpid);
//		param.put("type", RedundantTemplateTypeEnum.CUSTOMER.getCode());
//		param.put("enable", 1);
//		
//		List<RedundantFieldTemplateEntity> templates = redundantFieldTemplateModel.findEntitys(param);
//		
//		RedundantFieldTemplateEntity template = templates.size() > 0 ? templates.get(0) : null;
//		
//		Integer templateId = template == null ? 0 : template.getId();
		
		param.clear();
		param.put("corpid", corpid);
//		param.put("templateId", templateId);
		param.put("del", 0);
		param.put("page", page);
		//更新时间倒序
		param.put("orderByStr", "update_time DESC");
		
		//若客户名称不为空，则添加客户名称筛选
		if(!StringUtil.isEmpty(name)) {
			param.put("nameLike", name);
		}
		if(!StringUtil.isEmpty(phone)) {
			param.put("phoneLike", phone);
		}
		
		PageHelper pageHelper = getPageHelper(param, customerModel, pageSize);
		 
		@SuppressWarnings("unchecked")
		List<CustomerEntity> list = (List<CustomerEntity>) getEntityList(param, pageHelper, customerModel);
		
		String[] customerFilterStr = {"id","name", "nameShort", "phone", "lastConnectTime", "addTime", "updateTime"}; //保留字段
		ret.put("customerList", FastJsonHelper.parseIncludeObject(CustomerEntity.class, customerFilterStr, list));
		
		ret.put("page", page);
		ret.put("pageSize", pageSize);
		ret.put("totalCount", pageHelper.getRowsCount());
		ret.put("errorCode", 0);
		ret.put("msg", "获取成功");
		
		return ret.toJSONString();

	}

	/**
	 * 新建客户接口地址:http://example.com/api/v1/customer/add.do
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
	public String customerAdd(HttpServletRequest request, HttpServletResponse response,
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
		
		String customerJsonStr = dataJson.getString("customer");
		if(StringUtil.isEmpty(customerJsonStr)) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_201001.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_201001.getMsg());
			return ret.toJSONString();
		}
		
		JSONObject customerJson = null;
		try {
			customerJson = JSON.parseObject(customerJsonStr);
		} catch(Exception e) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_201002.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_201002.getMsg());
			return ret.toJSONString();
		}
		
		if(customerJson == null) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_201001.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_201001.getMsg());
			return ret.toJSONString();
		}
		
		Map<String, Object> param = new HashMap<String, Object>();
		
		boolean nameCheck = companyConfigModel.getCustomerNameRule(corpid);
		boolean phoneCheck = companyConfigModel.getCustomerPhoneRule(corpid);
		
		boolean flag = apiHelper.getInfo(corpid, modelMap, RedundantTemplateTypeEnum.CUSTOMER.getCode(), customerJson, true);
		
		if(!flag) {
			ret.put("errorCode", modelMap.get("errorCode"));
			ret.put("msg", modelMap.get("msg"));
			return ret.toJSONString();
		}
		
		Integer templateId = (Integer) modelMap.get("templateId");
		@SuppressWarnings("unchecked")
		Map<String,RedundantFieldExplainEntity> explainMap = (Map<String,RedundantFieldExplainEntity>) modelMap.get("explainMap");
		
		String nameAttr = "客户名称";
		String phoneAttr = "客户电话";
		int setAttrSemaphore = 2;//有两个attr要查找，找到两个就break
		for(RedundantFieldExplainEntity explain : explainMap.values()) {
			if(explain.getAttr().equals("name")) {
				nameAttr = explain.getAttrName();
				setAttrSemaphore--;
				if(setAttrSemaphore <= 0) {
					break;
				}
			}
			if(explain.getAttr().equals("phone")) {
				phoneAttr = explain.getAttrName();
				setAttrSemaphore--;
				if(setAttrSemaphore <= 0) {
					break;
				}
			}
		}
		
		String customerName = customerJson.getString(nameAttr);
		String customerPhone = customerJson.getString(phoneAttr);
		
		//电话格式校验
		if(!StringUtil.isEmpty(customerPhone)) {
			customerPhone = customerPhone.trim();
			
			String phoneFormat = companyConfigModel.getStringConfig(corpid, CompanyConfigEnum.CUS_PHONE_RULE_SET.getAlias());
			if(StringUtil.isEmpty(phoneFormat)) {
				phoneFormat = "0";
			}
			Pattern pattern;
			switch(phoneFormat) {
			case "0":
				pattern = Pattern.compile("^[0-9][0-9\\ ]+"); 			//数字,允许空格
				break;
			case "1":
//				pattern = Pattern.compile("^[0-9][0-9\\ \\-\\+]+");
				pattern = Pattern.compile("^[0-9a-zA-Z\\ \\-\\+]+");	//数字，字母或三个符号[空格，中划线，加号]
				break;
			case "2":
				pattern = Pattern.compile("^[0-9]{11}$");				//必须11位数字
				break;
			default:
				pattern = Pattern.compile("^[0-9][0-9\\ ]+");
				break;
			}
			
			Matcher matcher = pattern.matcher(customerPhone);
			boolean isMatch = matcher.matches();
			
			if(!isMatch) {
				ret.put("errorCode", ErrcodeEnum.API_ERROR_201010.getCode());
				ret.put("msg", ErrcodeEnum.API_ERROR_201010.getMsg());
				return ret.toJSONString();
			}
		}
		
		param.clear();
		param.put("corpid", corpid);

		//并发锁key：规则若是客户名+电话号码排重则格式为p_电话号码_n_客户名;
		//		       若为客户名排重则格式为n_客户名;
		//        若为电话号码排重则格式为p_电话号码;
		StringBuffer customerAddLockKey = new StringBuffer();
		customerAddLockKey.append(corpid).append("_");
		
		if(nameCheck && phoneCheck) { //客户名称必填
			if(!StringUtil.isEmpty(customerName) && !StringUtil.isEmpty(customerPhone)){
				param.put("nameOr", customerName);
				param.put("phoneLikeOr", customerPhone);
				customerAddLockKey.append("p_").append(customerPhone).append("n_").append(customerName);
			} else if(!StringUtil.isEmpty(customerName)){
				param.put("name", customerName);
				customerAddLockKey.append("n_").append(customerName);
			} else {
				ret.put("errorCode", ErrcodeEnum.API_ERROR_201003.getCode());
				ret.put("msg", ErrcodeEnum.API_ERROR_201003.getMsg());
				return ret.toJSONString();
			}
		} else if(nameCheck) {
			if(StringUtil.isEmpty(customerName)){
				ret.put("errorCode", ErrcodeEnum.API_ERROR_201003.getCode());
				ret.put("msg", ErrcodeEnum.API_ERROR_201003.getMsg());
				return ret.toJSONString();
			} else {
				param.put("name", customerName);
				customerAddLockKey.append("n_").append(customerName);
			}
		} else if(phoneCheck) {
			if(StringUtil.isEmpty(customerPhone)){
				ret.put("errorCode", ErrcodeEnum.API_ERROR_201004.getCode());
				ret.put("msg", ErrcodeEnum.API_ERROR_201004.getMsg());
				return ret.toJSONString();
			} else {
				param.put("phoneLike", customerPhone);
				customerAddLockKey.append("p_").append(customerPhone);
			}
		}
		
		String customerAddLockKeyStr = customerAddLockKey.toString();
		if(!StringUtil.isEmpty(customerAddLockKeyStr) && !customerAddLockKeyStr.equals(corpid + "_")) {
			//判断并发
			boolean getLock = JedisUtils.checkConcurrentLock(ConfigConstant.CONCURRENT_LOCK_CUSTOMER_ADD, customerAddLockKeyStr);
			if(!getLock) {
				ret.put("errorCode", ErrcodeEnum.API_ERROR_100011.getCode());
				ret.put("msg", ErrcodeEnum.API_ERROR_100011.getMsg());
				return ret.toJSONString();
			}
		}
		
		Integer customerCount = customerModel.getEntitysCount(param);
		if(customerCount > 0) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_201005.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_201005.getMsg());
			return ret.toJSONString();
		}
		
		//创建者逻辑 TODO 创建者不存在是否让创建记录
		String creatorName = customerJson.getString("创建人");
		String creatorUserId = "0";
		String creatorUserName = "";
		
		if(!StringUtil.isEmpty(creatorName)) {
			param.clear();
			param.put("corpid", corpid);
			param.put("name", creatorName);
			param.put("negDel", 1);
			param.put("start", 0);
			param.put("pageNum", 1);
			
			List<UserEntity> creatorUsers = userModel.findEntitys(param);
			UserEntity creatorUser = creatorUsers.size() > 0 ? creatorUsers.get(0) : null;
			
			if(creatorUser != null) {
				creatorUserId = creatorUser.getUserId();
				creatorUserName = creatorUser.getName();
			}
		}

		Map<Integer,Map<Integer, DataDictionaryEntity>> allDataDictionarys = dataDictionaryModel.getDataDictionaryMap(corpid);
		
		Map<Integer, DataDictionaryEntity> typeMap = allDataDictionarys.get(DictionaryTypeEnum.CUSTOMER_TYPE.getCode());
		Map<Integer, DataDictionaryEntity> isIndividualMap = allDataDictionarys.get(DictionaryTypeEnum.CUSTOMER_IS_INDIVIDUAL.getCode());
		Map<Integer, DataDictionaryEntity> industryMap = allDataDictionarys.get(DictionaryTypeEnum.CUSTOMER_INDUSTRY.getCode());
		Map<Integer, DataDictionaryEntity> genreMap = allDataDictionarys.get(DictionaryTypeEnum.CUSTOMER_GENRE.getCode());
		Map<Integer, DataDictionaryEntity> countryMap = allDataDictionarys.get(DictionaryTypeEnum.CUSTOMER_COUNTRY.getCode());
		Map<Integer, DataDictionaryEntity> sourceMap = allDataDictionarys.get(DictionaryTypeEnum.CUSTOMER_SOURCE.getCode());
		
		Integer now = DateUtil.getInt();
		//客户
		CustomerEntity customerEntity = new CustomerEntity();
		customerEntity.setCorpid(corpid);
		customerEntity.setTemplateId(templateId);
		customerEntity.setUserId(creatorUserId);
		customerEntity.setUserName(creatorUserName);
		customerEntity.setHealthDegree(0);
		customerEntity.setAddTime(now);
		customerEntity.setLastConnectTime(0);
		customerEntity.setLatitude(0.0);
		customerEntity.setLongitude(0.0);
		customerEntity.setFax("");
		customerEntity.setCountry("中国");
		customerEntity.setDel(0);
		customerEntity.setIsArchived(0);
		
		//客户冗余信息
		RedundantFieldEntity fieldEntity = new RedundantFieldEntity();
		fieldEntity.setTemplateId(templateId);
		fieldEntity.setCorpid(corpid);
		fieldEntity.setRefType(RedundantTemplateTypeEnum.CUSTOMER.getCode());
		fieldEntity.setRefId(0);
		
		customerEntity.setTypeMap(typeMap);
		customerEntity.setIsIndividualMap(isIndividualMap);;
		customerEntity.setIndustryMap(industryMap);;
		customerEntity.setGenreMap(genreMap);;
		customerEntity.setCountryMap(countryMap);;
		customerEntity.setSourceMap(sourceMap);;
		boolean setValueFlag = apiHelper.setValue(customerJson, explainMap, customerEntity, fieldEntity, modelMap);
		if(!setValueFlag){
			ret.put("errorCode", modelMap.get("errorCode"));
			ret.put("msg", modelMap.get("msg"));
			return ret.toJSONString();
		}
		
		customerEntity.setUpdateTime(now);
		
		//经纬度特殊参数,高德地图坐标
		Double longitude = customerJson.getDouble("经度");
		Double latitude = customerJson.getDouble("纬度");
		
		if(longitude != null && latitude != null) {
			if(longitude < -180 && longitude > 180) {
				ret.put("errorCode", ErrcodeEnum.API_ERROR_201011.getCode());
				ret.put("msg", ErrcodeEnum.API_ERROR_201011.getMsg());
				return ret.toJSONString();
			}
			if(latitude < -90 && latitude > 90) {
				ret.put("errorCode", ErrcodeEnum.API_ERROR_201012.getCode());
				ret.put("msg", ErrcodeEnum.API_ERROR_201012.getMsg());
				return ret.toJSONString();
			}
			customerEntity.setLongitude(longitude);
			customerEntity.setLatitude(latitude);
		}

		//负责人
		String userName = customerJson.getString("负责人");
		UserEntity changeUser = null;
		if(!StringUtil.isEmpty(userName)) {
			param.clear();
			param.put("name", userName);
			param.put("corpid", corpid);
			param.put("del", 0);
			param.put("start", 0);
			param.put("pageNum", 1);
			List<UserEntity> userEntityList = userModel.findEntitys(param);
			
			changeUser = userEntityList.size() > 0 ? userEntityList.get(0) : null;
		}

		//协同人相关逻辑
		String collaboratorStr = customerJson.getString("协同人");
		List<UserEntity> collaborators = new ArrayList<>();
		if(!StringUtil.isEmpty(collaboratorStr)) {
			String[] collaboratorNames = collaboratorStr.split(",|，");
			for(String collaboratorName : collaboratorNames){
				if(StringUtil.isEmpty(collaboratorName) || collaboratorName.equals(userName)) {
					continue;
				}
				param.clear();
				param.put("name", collaboratorName);
				param.put("corpid", corpid);
				param.put("del", 0);
				param.put("start", 0);
				param.put("pageNum", 1);
				List<UserEntity> userEntityList = userModel.findEntitys(param);

				UserEntity colUser =  userEntityList.size() > 0 ? userEntityList.get(0) : null;
				if(colUser != null) {
					collaborators.add(colUser);
				}
			}
		}
		
		if(changeUser == null) {
			customerEntity.setIsPublic(1);//没有负责人，放入公海池
		}
		
		try {
			customerModel.save(customerEntity);
			if(templateId != 0){
				fieldEntity.setRefId(customerEntity.getId());
				redundantFieldModel.save(fieldEntity);
			}
			
			//插入cusetomerUser记录
			if(changeUser != null) {
				CustomerUserEntity customerUserEntity = new CustomerUserEntity();
				customerUserEntity.setCorpid(corpid);
				customerUserEntity.setCustomerId(customerEntity.getId());
				customerUserEntity.setIsMain(1);	//新建时直接设置
				customerUserEntity.setUserId(changeUser.getUserId());
				customerUserEntity.setUserName(changeUser.getName());
				customerUserEntity.setAddTime(now);
				customerUserEntity.setUpdateTime(now);
				customerUserModel.insert(customerUserEntity);
			}
			//插入协同人记录
			if(collaborators.size() > 0) {
				for(UserEntity colUser : collaborators) {
					CustomerUserEntity customerUserEntity = new CustomerUserEntity();
					customerUserEntity.setCorpid(corpid);
					customerUserEntity.setCustomerId(customerEntity.getId());
					customerUserEntity.setIsMain(0);	//新建时直接设置
					customerUserEntity.setUserId(colUser.getUserId());
					customerUserEntity.setUserName(colUser.getName());
					customerUserEntity.setAddTime(now);
					customerUserEntity.setUpdateTime(now);
					customerUserModel.insert(customerUserEntity);
				}
			}
		} catch(Exception e) {
			LOG.error("数据操作失败", e);
			ret.put("errorCode", ErrcodeEnum.API_ERROR_100004.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_100004.getMsg());
			return ret.toJSONString();
		}
		
		ret.put("errorCode", 0);
		ret.put("msg", "操作成功");
		ret.put("customerId", customerEntity.getId());
		return ret.toJSONString();
	}
	
	/**
	 * 修改客户接口地址:http://example.com/api/v1/customer/update.do
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
	public String customerUpdate(HttpServletRequest request, HttpServletResponse response,
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
		
		String customerJsonStr = dataJson.getString("customer");
		if(StringUtil.isEmpty(customerJsonStr)) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_201001.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_201001.getMsg());
			return ret.toJSONString();
		}
		
		JSONObject customerJson = null;
		try {
			customerJson = JSON.parseObject(customerJsonStr);
		} catch(Exception e) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_201002.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_201002.getMsg());
			return ret.toJSONString();
		}
		
		if(customerJson == null) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_201001.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_201001.getMsg());
			return ret.toJSONString();
		}
		
		Integer customerId = customerJson.getInteger("customerId");
		if(customerId == null || customerId <= 0) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_201006.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_201006.getMsg());
			return ret.toJSONString();
		}
		
		//客户
		CustomerEntity customerEntity = customerModel.getByKey(customerId, corpid);
		if(customerEntity == null) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_201007.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_201007.getMsg());
			return ret.toJSONString();
		}
		
		boolean nameCheck = companyConfigModel.getCustomerNameRule(corpid);
		boolean phoneCheck = companyConfigModel.getCustomerPhoneRule(corpid);
		
		boolean flag = apiHelper.getInfo(corpid, modelMap, RedundantTemplateTypeEnum.CUSTOMER.getCode(), customerJson, false);
		
		if(!flag) {
			ret.put("errorCode", modelMap.get("errorCode"));
			ret.put("msg", modelMap.get("msg"));
			return ret.toJSONString();
		}
		
		Integer templateId = (Integer) modelMap.get("templateId");
		@SuppressWarnings("unchecked")
		Map<String,RedundantFieldExplainEntity> explainMap = (Map<String,RedundantFieldExplainEntity>) modelMap.get("explainMap");
		
		String nameAttr = "客户名称";
		String phoneAttr = "客户电话";
		int setAttrSemaphore = 2;//有两个attr要查找，找到两个就break
		for(RedundantFieldExplainEntity explain : explainMap.values()) {
			if(explain.getAttr().equals("name")) {
				nameAttr = explain.getAttrName();
				setAttrSemaphore--;
				if(setAttrSemaphore <= 0) {
					break;
				}
			}
			if(explain.getAttr().equals("phone")) {
				phoneAttr = explain.getAttrName();
				setAttrSemaphore--;
				if(setAttrSemaphore <= 0) {
					break;
				}
			}
		}
		
		String customerName = customerJson.getString(nameAttr);
		String customerPhone = customerJson.getString(phoneAttr);
		
		//电话格式校验
		if(!StringUtil.isEmpty(customerPhone)) {
			customerPhone = customerPhone.trim();
			
			boolean phoneAllowOtherLetter = companyConfigModel.getBooleanConfig(corpid, CompanyConfigEnum.CUS_PHONE_RULE_SET.getAlias());
			Pattern pattern = Pattern.compile("^[0-9][0-9\\ \\-\\+]+");
			if(!phoneAllowOtherLetter) {
				pattern = Pattern.compile("^[0-9][0-9\\ ]+");
			}
			
			Matcher matcher = pattern.matcher(customerPhone);
			boolean isMatch = matcher.matches();
			
			if(!isMatch) {
				ret.put("errorCode", ErrcodeEnum.API_ERROR_201010.getCode());
				ret.put("msg", ErrcodeEnum.API_ERROR_201010.getMsg());
				return ret.toJSONString();
			}
		}
		
		//是否执行查重，如果客户名称及电话都没填不用修改的话就不用查重，编辑客户不一定要改电话号码和名称
		boolean needCheck = true;
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("corpid", corpid);

		if(nameCheck && phoneCheck) { //客户名称必填
			if(!StringUtil.isEmpty(customerName) && !StringUtil.isEmpty(customerPhone)){
				param.put("nameOr", customerName);
				param.put("phoneLikeOr", customerPhone);
			} else if(!StringUtil.isEmpty(customerName)){
				param.put("name", customerName);
			} else {
				needCheck = false;
			}
		} else if(nameCheck) {
			if(StringUtil.isEmpty(customerName)){
				needCheck = false;
			} else {
				param.put("name", customerName);
			}
		} else if(phoneCheck) {
			if(StringUtil.isEmpty(customerPhone)){
				needCheck = false;
			} else {
				param.put("phoneLike", customerPhone);
			}
		}
		if(needCheck) {
			//不算本条记录
			param.put("idNeg", customerEntity.getId());
			
			Integer customerCount = customerModel.getEntitysCount(param);
			if(customerCount > 0) {
				ret.put("errorCode", ErrcodeEnum.API_ERROR_201005.getCode());
				ret.put("msg", ErrcodeEnum.API_ERROR_201005.getMsg());
				return ret.toJSONString();
			}
		}
		
		RedundantFieldEntity fieldEntity = redundantFieldModel.getByRefTypeAndRefId(corpid, RedundantTemplateTypeEnum.CUSTOMER.getCode(), customerId);
		if(fieldEntity == null) {
			fieldEntity = new RedundantFieldEntity();
			fieldEntity.setTemplateId(templateId);
			fieldEntity.setCorpid(corpid);
			fieldEntity.setRefType(RedundantTemplateTypeEnum.CUSTOMER.getCode());
			fieldEntity.setRefId(0);
		}
		
		Map<Integer,Map<Integer, DataDictionaryEntity>> allDataDictionarys = dataDictionaryModel.getDataDictionaryMap(corpid);
		
		Map<Integer, DataDictionaryEntity> typeMap = allDataDictionarys.get(DictionaryTypeEnum.CUSTOMER_TYPE.getCode());
		Map<Integer, DataDictionaryEntity> isIndividualMap = allDataDictionarys.get(DictionaryTypeEnum.CUSTOMER_IS_INDIVIDUAL.getCode());
		Map<Integer, DataDictionaryEntity> industryMap = allDataDictionarys.get(DictionaryTypeEnum.CUSTOMER_INDUSTRY.getCode());
		Map<Integer, DataDictionaryEntity> genreMap = allDataDictionarys.get(DictionaryTypeEnum.CUSTOMER_GENRE.getCode());
		Map<Integer, DataDictionaryEntity> countryMap = allDataDictionarys.get(DictionaryTypeEnum.CUSTOMER_COUNTRY.getCode());
		Map<Integer, DataDictionaryEntity> sourceMap = allDataDictionarys.get(DictionaryTypeEnum.CUSTOMER_SOURCE.getCode());
		
		customerEntity.setTypeMap(typeMap);
		customerEntity.setIsIndividualMap(isIndividualMap);
		customerEntity.setIndustryMap(industryMap);
		customerEntity.setGenreMap(genreMap);
		customerEntity.setCountryMap(countryMap);
		customerEntity.setSourceMap(sourceMap);
		boolean setValueFlag = apiHelper.setValue(customerJson, explainMap, customerEntity, fieldEntity, modelMap);
		if(!setValueFlag){
			ret.put("errorCode", modelMap.get("errorCode"));
			ret.put("msg", modelMap.get("msg"));
			return ret.toJSONString();
		}
		
		Integer now = DateUtil.getInt();
		customerEntity.setUpdateTime(now);
		
		//经纬度特殊参数,高德地图坐标
		Double longitude = customerJson.getDouble("经度");
		Double latitude = customerJson.getDouble("纬度");
		
		if(longitude != null && latitude != null) {
			if(longitude < -180 && longitude > 180) {
				ret.put("errorCode", ErrcodeEnum.API_ERROR_201011.getCode());
				ret.put("msg", ErrcodeEnum.API_ERROR_201011.getMsg());
				return ret.toJSONString();
			}
			if(latitude < -90 && latitude > 90) {
				ret.put("errorCode", ErrcodeEnum.API_ERROR_201012.getCode());
				ret.put("msg", ErrcodeEnum.API_ERROR_201012.getMsg());
				return ret.toJSONString();
			}
			customerEntity.setLongitude(longitude);
			customerEntity.setLatitude(latitude);
		}
		
		try {
			customerModel.save(customerEntity);
			if(templateId != 0){
				fieldEntity.setRefId(customerEntity.getId());
				redundantFieldModel.save(fieldEntity);
			}
		} catch(Exception e) {
			LOG.error("数据操作失败", e);
			ret.put("errorCode", 1);
			ret.put("msg", "数据库操作失败");
			return ret.toJSONString();
		}
		
		ret.put("errorCode", 0);
		ret.put("msg", "操作成功");
		return ret.toJSONString();
	}
	
	/**
	 * 获取客户详情接口地址:http://example.com/api/v1/customer/get.do
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
	public String customerGet(HttpServletRequest request, HttpServletResponse response, Map<String, Object> modelMap)
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

		Integer customerId = StringUtil.StringToInteger(dataJson.getString("customerId"), 0);
		
		if(customerId == null || customerId <= 0) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_201006.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_201006.getMsg());
			return ret.toJSONString();
		}
		
		CustomerEntity customer = customerModel.getByKey(customerId, corpid);
		
		if(customer == null) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_201007.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_201007.getMsg());
			return ret.toJSONString();
		}
		
		//模板ID
		Integer templateId = customer.getTemplateId();
		
		RedundantFieldEntity fieldEntity = redundantFieldModel.getByRefTypeAndRefId(corpid, RedundantTemplateTypeEnum.CUSTOMER.getCode(), customerId);
		
		List<RedundantFieldExplainEntity> explains = redundantFieldModel.getRedundantFieldExplains(RedundantTemplateTypeEnum.CUSTOMER.getCode(), corpid, templateId);
		
		//数据字典
		Map<Integer,Map<Integer, DataDictionaryEntity>> dataDictionaryMap = dataDictionaryModel.getDataDictionaryMap(corpid);
		Map<Integer,DataDictionaryEntity> typeMap = dataDictionaryMap.get(DictionaryTypeEnum.CUSTOMER_TYPE.getCode());
		Map<Integer,DataDictionaryEntity> industryMap = dataDictionaryMap.get(DictionaryTypeEnum.CUSTOMER_INDUSTRY.getCode());
		Map<Integer,DataDictionaryEntity> individualMap = dataDictionaryMap.get(DictionaryTypeEnum.CUSTOMER_IS_INDIVIDUAL.getCode());
		Map<Integer,DataDictionaryEntity> genreMap = dataDictionaryMap.get(DictionaryTypeEnum.CUSTOMER_GENRE.getCode());
		//数据字典渲染
		String typeStr ="";
		if(typeMap.get(customer.getType()) != null){
			typeStr = typeMap.get(customer.getType()).getName();
		}
		customer.setTypeStr(typeStr);
		
		String industryStr ="";
		if(industryMap.get(customer.getIndustry()) != null){
			industryStr = industryMap.get(customer.getIndustry()).getName();
		}
		customer.setIndustryStr(industryStr);
		
		String isIndividualStr ="";
		if(individualMap.get(customer.getIsIndividual()) != null){
			isIndividualStr = individualMap.get(customer.getIsIndividual()).getName();
		}
		customer.setIsIndividualStr(isIndividualStr);
		
		String genreStr ="";
		if(genreMap.get(customer.getGenre()) != null){
			genreStr = genreMap.get(customer.getGenre()).getName();
		}
		customer.setGenreStr(genreStr);
		
		//获取创建人
		UserEntity user = userModel.getByKeyIngoreDel(customer.getUserId(), corpid);
		if(user != null) {
			customer.setUserName(user.getName());
		}
		
		JSONObject retJSON = FormateRedundantValueHelper.formateRedundantValue(RedundantTemplateTypeEnum.CUSTOMER.getCode(), explains, fieldEntity, customer);
		
		ret.put("customer", retJSON);
		
		//客户销售团队
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("corpid", corpid);
		param.put("customerId", customerId);
		param.put("del", 0);
		
		List<CustomerUserEntity> customerUserList = customerUserModel.findEntitys(param);
		
		String[] customerUserStr = {"userId", "userName", "customerId", "distributionTime", "isMain"}; //保留字段
		ret.put("customerUserList", FastJsonHelper.parseIncludeObject(CustomerUserEntity.class, customerUserStr, customerUserList));
		
		ret.put("errorCode", 0);
		ret.put("msg", "获取成功");
		return ret.toJSONString();
	}
	
	
	/**
	 * TODO 补充删除关联customerUser，联系人及联系人关联数据
	 * 客户删除接口地址:http://example.com/api/v1/customer/delete.do
	 * 输入参数：data={"staffId":"","customerId":""}
	 * 成功输出：data={"errorcode":0,"msg":"获取成功","ret":""}
	 * 失败输出：data={"errorcode":*,"msg":"获取失败","ret":""}
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @throws Exception
	 */
	@Transactional
	@ResponseBody
	@RequestMapping("/delete")
	public String customerDelete(HttpServletRequest request, HttpServletResponse response, Map<String, Object> modelMap)
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
		
		Integer customerId = StringUtil.StringToInteger(dataJson.getString("customerId"), 0);
		
		if(customerId == null || customerId <= 0) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_201006.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_201006.getMsg());
			return ret.toJSONString();
		}
		
		CustomerEntity customer = customerModel.getByKey(customerId, corpid);
		
		if(customer == null) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_201007.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_201007.getMsg());
			return ret.toJSONString();
		}
		
		if(customer.getDel().equals(1)) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_201008.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_201008.getMsg());
			return ret.toJSONString();
		}
		
		//有合同的客户不让删除
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("corpid", corpid);
		param.put("customerId", customerId);
		param.put("del", 0);
		
		Integer contractCount = contractModel.getEntitysCount(param);
		if(contractCount > 0) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_201009.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_201009.getMsg());
			return ret.toJSONString();
		}
		
		try {
			customerModel.deleteByKey(customerId, corpid);
		} catch(Exception e) {
			LOG.error("数据操作失败", e);
			ret.put("errorCode", ErrcodeEnum.API_ERROR_100004.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_100004.getMsg());
			return ret.toJSONString();
		}
		
		//删除customerUser关系
		customerUserModel.deleteByCustomerId(corpid, customerId);
		//删除客户关联联系人
		contactModel.deleteByCorpidAndCustomerId(corpid, customerId);
		contactUserModel.deleteByCorpidAndCustomerId(corpid, customerId);
		//删除客户关联销售机会
		param.clear();
		param.put("corpid", corpid);
		param.put("customerId", customerId);
		param.put("del", 0);
		param.put("columns", "id");
		
		List<OpportunityEntity> opportunityList = opportunityModel.findEntitys(param);
		
		if(opportunityList != null && opportunityList.size() > 0) {
			for(OpportunityEntity opportunity : opportunityList) {
				if(opportunity.getId() != null && opportunity.getId() > 0) {
					opportunityUserModel.deleteByOpportunityId(corpid, opportunity.getId());
					opportunityProductModel.deleteByOpportunityId(corpid, opportunity.getId());
				}
			}
			
			opportunityModel.deleteByCustomerId(corpid, customerId);
		}
		
		
		ret.put("errorCode", 0);
		ret.put("msg", "删除成功");
		ret.put("customerId", customerId);
		return ret.toJSONString();
	}
	
	/**
	 * 客户关联联系人接口地址:http://example.com/api/v1/customer/contact.do
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
	@RequestMapping("/contact")
	public String customerContact(HttpServletRequest request, HttpServletResponse response, Map<String, Object> modelMap)
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

		Integer customerId = StringUtil.StringToInteger(dataJson.getString("customerId"), 0);
		
		if(customerId == null || customerId <= 0) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_201006.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_201006.getMsg());
			return ret.toJSONString();
		}
		
		CustomerEntity customer = customerModel.getByKey(customerId, corpid);
		
		if(customer == null) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_201007.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_201007.getMsg());
			return ret.toJSONString();
		}
		
		//获取客户对应的联系人
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("corpid", corpid);
		param.put("type", RedundantTemplateTypeEnum.CONTACT.getCode());
		param.put("enable", 1);
		
		List<RedundantFieldTemplateEntity> templates = redundantFieldTemplateModel.findEntitys(param);
		
		RedundantFieldTemplateEntity template = templates.size() > 0 ? templates.get(0) : null;
		
		Integer templateId = template == null ? 0 : template.getId();
				
		param.clear();
		param.put("corpid", corpid);
//		param.put("templateId", templateId); //获取客户关联联系人列表时不用指定必须当前模板的数据,不然可能数据没有取出
		param.put("customerId", customerId);
		param.put("del", 0);
		
		List<ContactEntity> list = contactModel.findEntitys(param);
		
		List<RedundantFieldExplainEntity> explains = redundantFieldModel.getRedundantFieldExplains(RedundantTemplateTypeEnum.CONTACT.getCode(), corpid, templateId);
		
		List<RedundantFieldEntity> fieldEntityList = null;
		//数据字典
		Map<Integer,Map<Integer, DataDictionaryEntity>> dataDictionaryMap = dataDictionaryModel.getDataDictionaryMap(corpid);
		Map<Integer,DataDictionaryEntity> levelMap = dataDictionaryMap.get(DictionaryTypeEnum.CONTACT_LEVEL.getCode());
		//数据字典渲染
		for(ContactEntity entity : list) {
			String levelStr ="";
			if(levelMap.get(entity.getLevel()) != null){
				levelStr = levelMap.get(entity.getLevel()).getName();
			}
			entity.setLevelStr(levelStr);
		}
		
		if(templateId > 0) {
			List<Integer> contactIds = new ArrayList<Integer>();
			for(ContactEntity contact : list) {
				contactIds.add(contact.getId());
			}
			
			if(contactIds.size() < 1) {
				contactIds.add(-1);
			}
			
			param.clear();
			param.put("corpid", corpid);
			param.put("templateId", templateId);
			param.put("del", 0);
			param.put("refType", RedundantTemplateTypeEnum.CONTACT.getCode());
			param.put("refIdIn", contactIds);
			
			fieldEntityList = redundantFieldModel.findEntitys(param);
		}
		
		JSONArray retJSONArray = FormateRedundantValueHelper.formateRedundantValues(RedundantTemplateTypeEnum.CONTACT.getCode(), explains, fieldEntityList, list);
		
		ret.put("contactList", retJSONArray);
		
		ret.put("totalCount", list.size());
		ret.put("customerId", customerId);
		ret.put("errorCode", 0);
		ret.put("msg", "获取成功");
		return ret.toJSONString();
	}
	
	/**
	 * 客户关联合同接口地址:http://example.com/api/v1/customer/contract.do
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
	@RequestMapping("/contract")
	public String customerContract(HttpServletRequest request, HttpServletResponse response, Map<String, Object> modelMap)
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
		
		Integer customerId = StringUtil.StringToInteger(dataJson.getString("customerId"), 0);
		
		if(customerId == null || customerId <= 0) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_201006.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_201006.getMsg());
			return ret.toJSONString();
		}
		
		CustomerEntity customer = customerModel.getByKey(customerId, corpid);
		
		if(customer == null) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_201007.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_201007.getMsg());
			return ret.toJSONString();
		}
		
		//获取客户对应的合同列表
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("corpid", corpid);
		param.put("type", RedundantTemplateTypeEnum.CONTRACT.getCode());
		param.put("enable", 1);
		
		List<RedundantFieldTemplateEntity> templates = redundantFieldTemplateModel.findEntitys(param);
		
		RedundantFieldTemplateEntity template = templates.size() > 0 ? templates.get(0) : null;
		
		Integer templateId = template == null ? 0 : template.getId();
				
		param.clear();
		param.put("corpid", corpid);
//		param.put("templateId", templateId);//获取客户关联合同列表时不用指定必须当前模板的数据,不然可能数据没有取出
		param.put("customerId", customerId);
		param.put("del", 0);
		
		List<ContractEntity> list = contractModel.findEntitys(param);
		
		List<RedundantFieldExplainEntity> explains = redundantFieldModel.getRedundantFieldExplains(RedundantTemplateTypeEnum.CONTRACT.getCode(), corpid, templateId);
		
		List<RedundantFieldEntity> fieldEntityList = null;
		//数据字典
		Map<Integer,Map<Integer, DataDictionaryEntity>> dataDictionaryMap = dataDictionaryModel.getDataDictionaryMap(corpid);
		Map<Integer,DataDictionaryEntity> statusMap = dataDictionaryMap.get(DictionaryTypeEnum.CONTRACT_STATUS.getCode());
		Map<Integer,DataDictionaryEntity> typeMap = dataDictionaryMap.get(DictionaryTypeEnum.CONTRACT_TYPE.getCode());
		//数据字典渲染
		for(ContractEntity entity : list) {
			String statusStr ="";
			if(statusMap.get(entity.getStatus()) != null){
				statusStr = statusMap.get(entity.getStatus()).getName();
			}
			entity.setStatusStr(statusStr);
			
			String typeStr ="";
			if(typeMap.get(entity.getType()) != null){
				typeStr = typeMap.get(entity.getType()).getName();
			}
			entity.setTypeStr(typeStr);
		}
		
		if(templateId > 0) {
			List<Integer> contractIds = new ArrayList<Integer>();
			for(ContractEntity contract : list) {
				contractIds.add(contract.getId());
			}
			
			if(contractIds.size() < 1) {
				contractIds.add(-1);
			}
			
			param.clear();
			param.put("corpid", corpid);
			param.put("templateId", templateId);
			param.put("del", 0);
			param.put("refType", RedundantTemplateTypeEnum.CONTRACT.getCode());
			param.put("refIdIn", contractIds);
			
			fieldEntityList = redundantFieldModel.findEntitys(param);
		}
		
		JSONArray retJSONArray = FormateRedundantValueHelper.formateRedundantValues(RedundantTemplateTypeEnum.CONTRACT.getCode(), explains, fieldEntityList, list);
		
		ret.put("contractList", retJSONArray);
		
		ret.put("totalCount", list.size());
		ret.put("customerId", customerId);
		
		ret.put("errorCode", 0);
		ret.put("msg", "获取成功");
		return ret.toJSONString();
	}

	/**
	 * 客户添加协同人地址:http://example.com/api/v1/customer/team/add.do
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
	@RequestMapping("/team/add")
	public String customerTeamAdd(HttpServletRequest request, HttpServletResponse response, Map<String, Object> modelMap)
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

		Integer customerId = StringUtil.StringToInteger(dataJson.getString("customerId"), 0);

		if(customerId == null || customerId <= 0) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_201006.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_201006.getMsg());
			return ret.toJSONString();
		}

		CustomerEntity customer = customerModel.getByKey(customerId, corpid);

		if(customer == null) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_201007.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_201007.getMsg());
			return ret.toJSONString();
		}

		String name = dataJson.getString("name");
		if(StringUtil.isEmpty(name)) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_201013.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_201013.getMsg());
			return ret.toJSONString();
		}

		Map<String, Object> param = new HashMap<>();
		param.put("corpid", corpid);
		param.put("name", name);
		param.put("negDel", 1);
		param.put("start", 0);
		param.put("pageNum", 1);

		List<UserEntity> users = userModel.findEntitys(param);
		UserEntity colUser = users.size() > 0 ? users.get(0) : null;

		if(colUser == null) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_201014.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_201014.getMsg());
			return ret.toJSONString();
		}

		param.clear();
		param.put("corpid", corpid);
		param.put("customerId", customerId);
		param.put("userId", colUser.getUserId());
		param.put("del", 0);

		Integer userCount = customerUserModel.getEntitysCount(param);

		if(userCount > 0) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_201016.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_201016.getMsg());
			return ret.toJSONString();
		}

		//插入协同人
		try {
			int now = DateUtil.getInt();
			CustomerUserEntity customerUserEntity = new CustomerUserEntity();
			customerUserEntity.setCorpid(corpid);
			customerUserEntity.setCustomerId(customerId);
			customerUserEntity.setIsMain(0);	//新建时直接设置
			customerUserEntity.setUserId(colUser.getUserId());
			customerUserEntity.setUserName(colUser.getName());
			customerUserEntity.setAddTime(now);
			customerUserEntity.setUpdateTime(now);
			customerUserModel.insert(customerUserEntity);
		} catch (Exception e) {
			LOG.error("数据操作失败", e);
			ret.put("errorCode", ErrcodeEnum.API_ERROR_100004.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_100004.getMsg());
			return ret.toJSONString();
		}

		ret.put("errorCode", 0);
		ret.put("msg", "添加成功");
		return ret.toJSONString();
	}

	/**
	 * 客户删除协同人地址:http://example.com/api/v1/customer/team/delete.do
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
	@RequestMapping("/team/delete")
	public String customerTeamDelete(HttpServletRequest request, HttpServletResponse response, Map<String, Object> modelMap)
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

		Integer customerId = StringUtil.StringToInteger(dataJson.getString("customerId"), 0);

		if(customerId == null || customerId <= 0) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_201006.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_201006.getMsg());
			return ret.toJSONString();
		}

		CustomerEntity customer = customerModel.getByKey(customerId, corpid);

		if(customer == null) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_201007.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_201007.getMsg());
			return ret.toJSONString();
		}

		String name = dataJson.getString("name");
		if(StringUtil.isEmpty(name)) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_201013.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_201013.getMsg());
			return ret.toJSONString();
		}

		Map<String, Object> param = new HashMap<>();
		param.put("corpid", corpid);
		param.put("name", name);
		param.put("negDel", 1);
		param.put("start", 0);
		param.put("pageNum", 1);

		List<UserEntity> users = userModel.findEntitys(param);
		UserEntity colUser = users.size() > 0 ? users.get(0) : null;

		if(colUser == null) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_201014.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_201014.getMsg());
			return ret.toJSONString();
		}

		param.clear();
		param.put("corpid", corpid);
		param.put("customerId", customerId);
		param.put("userId", colUser.getUserId());
		param.put("del", 0);

		List<CustomerUserEntity> customerUsers = customerUserModel.findEntitys(param);
		CustomerUserEntity customerUser = customerUsers.size() > 0 ? customerUsers.get(0) : null;

		if(customerUser == null) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_201014.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_201014.getMsg());
			return ret.toJSONString();
		}

		//删除协同人
		try {
			customerUserModel.deleteByKey(customerUser.getId(), customerUser.getCorpid());
		} catch (Exception e) {
			LOG.error("数据操作失败", e);
			ret.put("errorCode", ErrcodeEnum.API_ERROR_100004.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_100004.getMsg());
			return ret.toJSONString();
		}

		ret.put("errorCode", 0);
		ret.put("msg", "删除成功");
		return ret.toJSONString();
	}
}