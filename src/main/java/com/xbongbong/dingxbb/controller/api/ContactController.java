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
import com.xbongbong.dingxbb.entity.ContactUserEntity;
import com.xbongbong.dingxbb.entity.CustomerEntity;
import com.xbongbong.dingxbb.entity.CustomerUserEntity;
import com.xbongbong.dingxbb.entity.DataDictionaryEntity;
import com.xbongbong.dingxbb.entity.RedundantFieldEntity;
import com.xbongbong.dingxbb.entity.RedundantFieldExplainEntity;
import com.xbongbong.dingxbb.entity.RedundantFieldTemplateEntity;
import com.xbongbong.dingxbb.entity.UserEntity;
import com.xbongbong.dingxbb.enums.CompanyConfigEnum;
import com.xbongbong.dingxbb.enums.DictionaryTypeEnum;
import com.xbongbong.dingxbb.enums.ErrcodeEnum;
import com.xbongbong.dingxbb.enums.RedundantTemplateTypeEnum;
import com.xbongbong.dingxbb.helper.ApiHelper;
import com.xbongbong.dingxbb.helper.FastJsonHelper;
import com.xbongbong.dingxbb.helper.FormateRedundantValueHelper;
import com.xbongbong.dingxbb.model.CompanyConfigModel;
import com.xbongbong.dingxbb.model.ContactModel;
import com.xbongbong.dingxbb.model.ContactUserModel;
import com.xbongbong.dingxbb.model.CustomerModel;
import com.xbongbong.dingxbb.model.CustomerUserModel;
import com.xbongbong.dingxbb.model.DataDictionaryModel;
import com.xbongbong.dingxbb.model.RedundantFieldModel;
import com.xbongbong.dingxbb.model.RedundantFieldTemplateModel;
import com.xbongbong.dingxbb.model.UserModel;
import com.xbongbong.util.DateUtil;
import com.xbongbong.util.PageHelper;
import com.xbongbong.util.StringUtil;

@Controller
@RequestMapping("/api/v1/contact")
public class ContactController extends BasicController {
	
	private static final Logger LOG = LogManager.getLogger(ContactController.class);
	
	@Autowired
	private CustomerModel customerModel;
	@Autowired
	private CustomerUserModel customerUserModel;
	@Autowired
	private ContactModel contactModel;
	@Autowired
	private ContactUserModel contactUserModel;
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
	 * 联系人列表[全部数据]接口地址:http://example.com/api/v1/contact/list.do 输入参数：data={}
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
	public String contactList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> modelMap)
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
		param.put("type", RedundantTemplateTypeEnum.CONTACT.getCode());
		param.put("enable", 1);
		
		List<RedundantFieldTemplateEntity> templates = redundantFieldTemplateModel.findEntitys(param);
		
		RedundantFieldTemplateEntity template = templates.size() > 0 ? templates.get(0) : null;
		
		Integer templateId = template == null ? 0 : template.getId();
				
		param.clear();
		param.put("corpid", corpid);
		param.put("templateId", templateId);
		param.put("del", 0);
		param.put("page", page);
		
		PageHelper pageHelper = getPageHelper(param, contactModel, pageSize);
		 
		@SuppressWarnings("unchecked")
		List<ContactEntity> list = (List<ContactEntity>) getEntityList(
				param, pageHelper, contactModel);
		
		List<RedundantFieldExplainEntity> explains = redundantFieldModel.getRedundantFieldExplains(RedundantTemplateTypeEnum.CUSTOMER.getCode(), corpid, templateId);
		
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
		
		ret.put("page", page);
		ret.put("pageSize", pageSize);
		ret.put("totalCount", pageHelper.getRowsCount());
		ret.put("errorCode", 0);
		ret.put("msg", "获取成功");
		
		return ret.toJSONString();

	}
	
	/**
	 * 联系人列表[简要数据]接口地址:http://example.com/api/v1/contact/simpleList.do 输入参数：data={}
	 * 成功输出：data={"errorcode":0,"msg":"获取成功","ret":""}
	 * 失败输出：data={"errorcode":*,"msg":"获取失败","ret":""}
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/simpleList")
	public String contactSimpleList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> modelMap)
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
		
		//获取所有数据，而不是某模板的数据
		Map<String, Object> param = new HashMap<String, Object>();
//		param.put("corpid", corpid);
//		param.put("type", RedundantTemplateTypeEnum.CONTACT.getCode());
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
		
		PageHelper pageHelper = getPageHelper(param, contactModel, pageSize);
		 
		@SuppressWarnings("unchecked")
		List<ContactEntity> list = (List<ContactEntity>) getEntityList(param, pageHelper, contactModel);
		
		String[] contactFilterStr = {"id","name", "phone", "addTime"}; //保留字段
		ret.put("contactList", FastJsonHelper.parseIncludeObject(ContactEntity.class, contactFilterStr, list));
		
		ret.put("page", page);
		ret.put("pageSize", pageSize);
		ret.put("totalCount", pageHelper.getRowsCount());
		ret.put("errorCode", 0);
		ret.put("msg", "获取成功");
		
		return ret.toJSONString();

	}

	/**
	 * TODO 关联客户在外部关联，方法内的关联客户操作去除，需处理
	 * 新建联系人接口地址:http://example.com/api/v1/contact/add.do
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
	public String contactAdd(HttpServletRequest request, HttpServletResponse response,
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
		
		String contactJsonStr = dataJson.getString("contact");
		if(StringUtil.isEmpty(contactJsonStr)) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_202001.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_202001.getMsg());
			return ret.toJSONString();
		}
		
		JSONObject contactJson = null;
		try {
			contactJson = JSON.parseObject(contactJsonStr);
		} catch(Exception e) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_202002.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_202002.getMsg());
			return ret.toJSONString();
		}
		
		if(contactJson == null) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_202001.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_202001.getMsg());
			return ret.toJSONString();
		}
		
		boolean flag = apiHelper.getInfo(corpid, modelMap, RedundantTemplateTypeEnum.CONTACT.getCode(), contactJson, true);
		
		if(!flag) {
			ret.put("errorCode", modelMap.get("errorCode"));
			ret.put("msg", modelMap.get("msg"));
			return ret.toJSONString();
		}
		
		Integer templateId = (Integer) modelMap.get("templateId");
		@SuppressWarnings("unchecked")
		Map<String,RedundantFieldExplainEntity> explainMap = (Map<String,RedundantFieldExplainEntity>) modelMap.get("explainMap");
		
		String phoneAttr = "联系电话";
		for(RedundantFieldExplainEntity explain : explainMap.values()) {
			if(explain.getAttr().equals("phone")) {
				phoneAttr = explain.getAttrName();
				break;
			}
		}
		
		//创建者逻辑 TODO 创建者不存在是否让创建记录
		String creatorName = contactJson.getString("创建人");
		String creatorUserId = "0";
		
		Map<String, Object> param = new HashMap<String, Object>();
		
		if(!StringUtil.isEmpty(creatorName)) {
			param.put("corpid", corpid);
			param.put("name", creatorName);
			param.put("negDel", 1);
			param.put("start", 0);
			param.put("pageNum", 1);
			
			List<UserEntity> creatorUsers = userModel.findEntitys(param);
			UserEntity creatorUser = creatorUsers.size() > 0 ? creatorUsers.get(0) : null;
			
			if(creatorUser != null) {
				creatorUserId = creatorUser.getUserId();
			}
		}
		
		String contactPhone = contactJson.getString(phoneAttr);
		
		//电话格式校验
		if(!StringUtil.isEmpty(contactPhone)) {
			contactPhone = contactPhone.trim();
			
			boolean phoneAllowOtherLetter = companyConfigModel.getBooleanConfig(corpid, CompanyConfigEnum.CUS_PHONE_RULE_SET.getAlias());
			Pattern pattern = Pattern.compile("^[0-9][0-9\\ \\-\\+]+");
			if(!phoneAllowOtherLetter) {
				pattern = Pattern.compile("^[0-9][0-9\\ ]+");
			}
			
			Matcher matcher = pattern.matcher(contactPhone);
			boolean isMatch = matcher.matches();
			
			if(!isMatch) {
				ret.put("errorCode", ErrcodeEnum.API_ERROR_202008.getCode());
				ret.put("msg", ErrcodeEnum.API_ERROR_202008.getMsg());
				return ret.toJSONString();
			}
		}
		
		param.clear();
		param.put("corpid", corpid);
		param.put("del", 0);

		if(StringUtil.isEmpty(contactPhone)){
			ret.put("errorCode", ErrcodeEnum.API_ERROR_202003.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_202003.getMsg());
			return ret.toJSONString();
		} else {
			param.put("phoneLike", contactPhone);
		}
		
		Integer contactCount = contactModel.getEntitysCount(param);
		if(contactCount > 0) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_202004.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_202004.getMsg());
			return ret.toJSONString();
		}
		
		Map<Integer,Map<Integer, DataDictionaryEntity>> allDataDictionarys = dataDictionaryModel.getDataDictionaryMap(corpid);
		
		Map<Integer, DataDictionaryEntity> levelMap = allDataDictionarys.get(DictionaryTypeEnum.CONTACT_LEVEL.getCode());
		
		Integer now = DateUtil.getInt();
		//客户
		ContactEntity contactEntity = new ContactEntity();
		contactEntity.setCorpid(corpid);
		contactEntity.setTemplateId(templateId);
		contactEntity.setUserId(creatorUserId);
		contactEntity.setCustomerId(customerId);
		contactEntity.setCustomerName(customer.getName());
		contactEntity.setAddTime(now);
		contactEntity.setLatitude(0.0);
		contactEntity.setLongitude(0.0);
		contactEntity.setHonorificTitle("");
		contactEntity.setDel(0);
		
		//客户冗余信息
		RedundantFieldEntity fieldEntity = new RedundantFieldEntity();
		fieldEntity.setTemplateId(templateId);
		fieldEntity.setCorpid(corpid);
		fieldEntity.setRefType(RedundantTemplateTypeEnum.CONTACT.getCode());
		fieldEntity.setRefId(0);
		
		contactEntity.setLevelMap(levelMap);
		boolean setValueFlag = apiHelper.setValue(contactJson, explainMap, contactEntity, fieldEntity, modelMap);
		if(!setValueFlag){
			ret.put("errorCode", modelMap.get("errorCode"));
			ret.put("msg", modelMap.get("msg"));
			return ret.toJSONString();
		}
		
		contactEntity.setUpdateTime(now);

		try {
			contactModel.save(contactEntity);
			if(templateId != 0){
				fieldEntity.setRefId(contactEntity.getId());
				redundantFieldModel.save(fieldEntity);
			}
		} catch(Exception e) {
			LOG.error("数据操作失败", e);
			ret.put("errorCode", ErrcodeEnum.API_ERROR_100004.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_100004.getMsg());
			return ret.toJSONString();
		}
		
		//插入contractUser记录
		param.clear();
		param.put("corpid", corpid);
		param.put("customerId", customerId);
		param.put("del", 0);
		
		List<CustomerUserEntity> customerUsers = customerUserModel.findEntitys(param);
		
		for(CustomerUserEntity customerUser : customerUsers) {
			ContactUserEntity contactUserEntity = new ContactUserEntity();
			contactUserEntity.setCorpid(corpid);
			contactUserEntity.setCustomerId(customerId);
			contactUserEntity.setContactId(contactEntity.getId());
			contactUserEntity.setIsMain(customerUser.getIsMain());	//新建时直接设置设置
			contactUserEntity.setUserId(customerUser.getUserId());
			contactUserEntity.setUserName(customerUser.getUserName());
			contactUserModel.insert(contactUserEntity);
		}
		
		ret.put("contactId", contactEntity.getId());
		ret.put("errorCode", 0);
		ret.put("msg", "操作成功");
		return ret.toJSONString();
	}
	
	/**
	 * 联系人修改接口地址:http://example.com/api/v1/contact/update.do
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
	public String contactUpdate(HttpServletRequest request, HttpServletResponse response,
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
		
		String contactJsonStr = dataJson.getString("contact");
		if(StringUtil.isEmpty(contactJsonStr)) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_202001.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_202001.getMsg());
			return ret.toJSONString();
		}
		
		JSONObject contactJson = null;
		try {
			contactJson = JSON.parseObject(contactJsonStr);
		} catch(Exception e) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_202002.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_202002.getMsg());
			return ret.toJSONString();
		}
		
		if(contactJson == null) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_202001.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_202001.getMsg());
			return ret.toJSONString();
		}
		
		Integer contactId = contactJson.getInteger("contactId");
		if(contactId == null || contactId <= 0) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_202005.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_202005.getMsg());
			return ret.toJSONString();
		}
		
		//联系人
		ContactEntity contactEntity = contactModel.getByKey(contactId, corpid);
		if(contactEntity == null) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_202006.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_202006.getMsg());
			return ret.toJSONString();
		}
		
		boolean flag = apiHelper.getInfo(corpid, modelMap, RedundantTemplateTypeEnum.CONTACT.getCode(), contactJson, false);
		
		if(!flag) {
			ret.put("errorCode", modelMap.get("errorCode"));
			ret.put("msg", modelMap.get("msg"));
			return ret.toJSONString();
		}
		
		Integer templateId = (Integer) modelMap.get("templateId");
		@SuppressWarnings("unchecked")
		Map<String,RedundantFieldExplainEntity> explainMap = (Map<String,RedundantFieldExplainEntity>) modelMap.get("explainMap");
		
		String phoneAttr = "联系电话";
		for(RedundantFieldExplainEntity explain : explainMap.values()) {
			if(explain.getAttr().equals("phone")) {
				phoneAttr = explain.getAttrName();
				break;
			}
		}
		
		String contactPhone = contactJson.getString(phoneAttr);
		
		if(!StringUtil.isEmpty(contactPhone)){
			//电话格式校验
			contactPhone = contactPhone.trim();
			boolean phoneAllowOtherLetter = companyConfigModel.getBooleanConfig(corpid, CompanyConfigEnum.CUS_PHONE_RULE_SET.getAlias());
			Pattern pattern = Pattern.compile("^[0-9][0-9\\ \\-\\+]+");
			if(!phoneAllowOtherLetter) {
				pattern = Pattern.compile("^[0-9][0-9\\ ]+");
			}
			
			Matcher matcher = pattern.matcher(contactPhone);
			boolean isMatch = matcher.matches();
			
			if(!isMatch) {
				ret.put("errorCode", ErrcodeEnum.API_ERROR_202008.getCode());
				ret.put("msg", ErrcodeEnum.API_ERROR_202008.getMsg());
				return ret.toJSONString();
			}
			
			//联系电话查重
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("corpid", corpid);
			param.put("del", 0);
			param.put("phoneLike", contactPhone);
			
			//除本条记录以外的其他记录
			param.put("idNeg", contactId);
			
			Integer contactCount = contactModel.getEntitysCount(param);
			if(contactCount > 0) {
				ret.put("errorCode", ErrcodeEnum.API_ERROR_202004.getCode());
				ret.put("msg", ErrcodeEnum.API_ERROR_202004.getMsg());
				return ret.toJSONString();
			}
		}
		
		RedundantFieldEntity fieldEntity = redundantFieldModel.getByRefTypeAndRefId(corpid, RedundantTemplateTypeEnum.CONTACT.getCode(), contactId);
		if(fieldEntity == null) {
			fieldEntity = new RedundantFieldEntity();
			fieldEntity.setTemplateId(templateId);
			fieldEntity.setCorpid(corpid);
			fieldEntity.setRefType(RedundantTemplateTypeEnum.CONTACT.getCode());
			fieldEntity.setRefId(0);
		}
		
		Map<Integer,Map<Integer, DataDictionaryEntity>> allDataDictionarys = dataDictionaryModel.getDataDictionaryMap(corpid);
		
		Map<Integer, DataDictionaryEntity> levelMap = allDataDictionarys.get(DictionaryTypeEnum.CONTACT_LEVEL.getCode());
		
		contactEntity.setLevelMap(levelMap);
		boolean setValueFlag = apiHelper.setValue(contactJson, explainMap, contactEntity, fieldEntity, modelMap);
		if(!setValueFlag){
			ret.put("errorCode", modelMap.get("errorCode"));
			ret.put("msg", modelMap.get("msg"));
			return ret.toJSONString();
		}
		
		Integer now = DateUtil.getInt();
		contactEntity.setUpdateTime(now);
		
		try {
			contactModel.save(contactEntity);
			if(templateId != 0){
				fieldEntity.setRefId(contactEntity.getId());
				redundantFieldModel.save(fieldEntity);
			}
		} catch(Exception e) {
			LOG.error("数据操作失败", e);
			ret.put("errorCode", ErrcodeEnum.API_ERROR_100004.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_100004.getMsg());
			return ret.toJSONString();
		}
		
		ret.put("errorCode", 0);
		ret.put("msg", "操作成功");
		return ret.toJSONString();
	}
	
	/**
	 * 获取联系人详情接口地址:http://example.com/api/v1/contact/get.do
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
	public String contactGet(HttpServletRequest request, HttpServletResponse response, Map<String, Object> modelMap)
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
		
		Integer contactId = StringUtil.StringToInteger(dataJson.getString("contactId"), 0);
		
		if(contactId == null || contactId <= 0) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_202005.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_202005.getMsg());
			return ret.toJSONString();
		}
		
		ContactEntity contact = contactModel.getByKey(contactId, corpid);
		
		if(contact == null) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_202006.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_202006.getMsg());
			return ret.toJSONString();
		}
		
		//模板ID
		Integer templateId = contact.getTemplateId();
		
		RedundantFieldEntity fieldEntity = redundantFieldModel.getByRefTypeAndRefId(corpid, RedundantTemplateTypeEnum.CONTACT.getCode(), contactId);
		
		List<RedundantFieldExplainEntity> explains = redundantFieldModel.getRedundantFieldExplains(RedundantTemplateTypeEnum.CONTACT.getCode(), corpid, templateId);
		
		//数据字典
		Map<Integer,Map<Integer, DataDictionaryEntity>> dataDictionaryMap = dataDictionaryModel.getDataDictionaryMap(corpid);
		Map<Integer, DataDictionaryEntity> levelMap = dataDictionaryMap.get(DictionaryTypeEnum.CONTACT_LEVEL.getCode());
		//数据字典渲染
		String levelStr ="";
		if(levelMap.get(contact.getLevel()) != null){
			levelStr = levelMap.get(contact.getLevel()).getName();
		}
		contact.setLevelStr(levelStr);
		
		JSONObject retJSON = FormateRedundantValueHelper.formateRedundantValue(RedundantTemplateTypeEnum.CONTACT.getCode(), explains, fieldEntity, contact);
		
		ret.put("contact", retJSON);
		
		ret.put("errorCode", 0);
		ret.put("msg", "获取成功");
		return ret.toJSONString();
	}
	
	/**
	 * 联系人删除接口地址:http://example.com/api/v1/contact/delete.do
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
	public String contactDelete(HttpServletRequest request, HttpServletResponse response, Map<String, Object> modelMap)
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
		
		Integer contactId = StringUtil.StringToInteger(dataJson.getString("contactId"), 0);
		
		if(contactId == null || contactId <= 0) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_202005.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_202005.getMsg());
			return ret.toJSONString();
		}
		
		ContactEntity contact = contactModel.getByKey(contactId, corpid);
		
		if(contact == null) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_202006.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_202006.getMsg());
			return ret.toJSONString();
		}
		
		if(contact.getDel().equals(1)){
			ret.put("errorCode", ErrcodeEnum.API_ERROR_202007.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_202007.getMsg());
			return ret.toJSONString();
		}
		
		try {
			contactModel.deleteByKey(contactId, corpid);
		} catch(Exception e) {
			LOG.error("数据操作失败", e);
			ret.put("errorCode", ErrcodeEnum.API_ERROR_100004.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_100004.getMsg());
			return ret.toJSONString();
		}
		
		//删除联系人关联
		contactUserModel.deleteByCorpidAndContactId(corpid, contactId);
		
		ret.put("errorCode", 0);
		ret.put("msg", "删除成功");
		return ret.toJSONString();
	}
}
