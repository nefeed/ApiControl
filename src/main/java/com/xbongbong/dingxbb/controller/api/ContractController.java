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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xbongbong.dingxbb.controller.BasicController;
import com.xbongbong.dingxbb.entity.ContractEntity;
import com.xbongbong.dingxbb.entity.ContractProductEntity;
import com.xbongbong.dingxbb.entity.ContractUserEntity;
import com.xbongbong.dingxbb.entity.CustomerEntity;
import com.xbongbong.dingxbb.entity.CustomerUserEntity;
import com.xbongbong.dingxbb.entity.DataDictionaryEntity;
import com.xbongbong.dingxbb.entity.PaymentEntity;
import com.xbongbong.dingxbb.entity.PaymentSheetEntity;
import com.xbongbong.dingxbb.entity.ProductEntity;
import com.xbongbong.dingxbb.entity.RedundantFieldEntity;
import com.xbongbong.dingxbb.entity.RedundantFieldExplainEntity;
import com.xbongbong.dingxbb.entity.RedundantFieldTemplateEntity;
import com.xbongbong.dingxbb.entity.UserEntity;
import com.xbongbong.dingxbb.enums.DictionaryTypeEnum;
import com.xbongbong.dingxbb.enums.ErrcodeEnum;
import com.xbongbong.dingxbb.enums.RedundantTemplateTypeEnum;
import com.xbongbong.dingxbb.helper.ApiHelper;
import com.xbongbong.dingxbb.helper.FastJsonHelper;
import com.xbongbong.dingxbb.helper.FormateRedundantValueHelper;
import com.xbongbong.dingxbb.model.ContractModel;
import com.xbongbong.dingxbb.model.ContractProductModel;
import com.xbongbong.dingxbb.model.ContractUserModel;
import com.xbongbong.dingxbb.model.CustomerModel;
import com.xbongbong.dingxbb.model.CustomerUserModel;
import com.xbongbong.dingxbb.model.DataDictionaryModel;
import com.xbongbong.dingxbb.model.PaymentModel;
import com.xbongbong.dingxbb.model.PaymentSheetModel;
import com.xbongbong.dingxbb.model.ProductModel;
import com.xbongbong.dingxbb.model.RedundantFieldModel;
import com.xbongbong.dingxbb.model.RedundantFieldTemplateModel;
import com.xbongbong.dingxbb.model.UserModel;
import com.xbongbong.util.CommentUtil;
import com.xbongbong.util.DateUtil;
import com.xbongbong.util.PageHelper;
import com.xbongbong.util.StringUtil;

@Controller
@RequestMapping("/api/v1/contract")
public class ContractController extends BasicController {
	
	private static final Logger LOG = LogManager.getLogger(ContractController.class);
	
	@Autowired
	private CustomerModel customerModel;
	@Autowired
	private CustomerUserModel customerUserModel;
	@Autowired
	private ContractModel contractModel;
	@Autowired
	private ContractUserModel contractUserModel;
	@Autowired
	private ContractProductModel contractProductModel;
	@Autowired
	private ProductModel productModel;
	@Autowired
	private PaymentModel paymentModel;
	@Autowired
	private PaymentSheetModel paymentSheetModel;
	@Autowired
	private RedundantFieldTemplateModel redundantFieldTemplateModel;
	@Autowired
	private RedundantFieldModel redundantFieldModel;
	@Autowired
	private DataDictionaryModel dataDictionaryModel;
	@Autowired
	private UserModel userModel;
	@Autowired
	private ApiHelper apiHelper;
	
	/**
	 * 合同列表[全部数据]接口地址:http://example.com/api/v1/contract/list.do 输入参数：data={}
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
	public String contractList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> modelMap)
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
		param.put("type", RedundantTemplateTypeEnum.CONTRACT.getCode());
		param.put("enable", 1);
		
		List<RedundantFieldTemplateEntity> templates = redundantFieldTemplateModel.findEntitys(param);
		
		RedundantFieldTemplateEntity template = templates.size() > 0 ? templates.get(0) : null;
		
		Integer templateId = template == null ? 0 : template.getId();
				
		param.clear();
		param.put("corpid", corpid);
		param.put("templateId", templateId);
		param.put("del", 0);
		param.put("page", page);
		
		PageHelper pageHelper = getPageHelper(param, contractModel, pageSize);
		 
		@SuppressWarnings("unchecked")
		List<ContractEntity> list = (List<ContractEntity>) getEntityList(param, pageHelper, contractModel);
		
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
		
		ret.put("page", page);
		ret.put("pageSize", pageSize);
		ret.put("totalCount", pageHelper.getRowsCount());
		ret.put("errorCode", 0);
		ret.put("msg", "获取成功");
		
		return ret.toJSONString();

	}
	
	/**
	 * 合同列表[简要数据]接口地址:http://example.com/api/v1/contract/simpleList.do 输入参数：data={}
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
	public String contractSimpleList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> modelMap)
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
		
		//签订人姓名模糊查询
		String signPerson = dataJson.getString("signPerson");
		//签订人ID
		String signUserId = dataJson.getString("signUserId");
		
		//返回所有数据，而不是当前模板的数据
		Map<String, Object> param = new HashMap<String, Object>();
//		param.put("corpid", corpid);
//		param.put("type", RedundantTemplateTypeEnum.CONTRACT.getCode());
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
		
		//签订人ID
		CommentUtil.addToMap(param, "signUserId", signUserId);
		//签订人姓名模糊查询
		CommentUtil.addToMap(param, "signPersonrLike", signPerson);
		
		PageHelper pageHelper = getPageHelper(param, contractModel, pageSize);
		 
		@SuppressWarnings("unchecked")
		List<ContractEntity> list = (List<ContractEntity>) getEntityList(param, pageHelper, contractModel);
		
		//数据字典
		Map<Integer,Map<Integer, DataDictionaryEntity>> dataDictionaryMap = dataDictionaryModel.getDataDictionaryMap(corpid);
		Map<Integer,DataDictionaryEntity> statusMap = dataDictionaryMap.get(DictionaryTypeEnum.CONTRACT_STATUS.getCode());
		//数据字典渲染
		for(ContractEntity entity : list) {
			String statusStr ="";
			if(statusMap.get(entity.getStatus()) != null){
				statusStr = statusMap.get(entity.getStatus()).getName();
			}
			entity.setStatusStr(statusStr);
		}
		
		String[] contractFilterStr = {"id","name", "signUserId", "signPerson", "statusStr", "status", "customerName", "amount", "signTime", "addTime"}; //保留字段
		ret.put("contractList", FastJsonHelper.parseIncludeObject(ContractEntity.class, contractFilterStr, list));
		
		ret.put("page", page);
		ret.put("pageSize", pageSize);
		ret.put("totalCount", pageHelper.getRowsCount());
		ret.put("errorCode", 0);
		ret.put("msg", "获取成功");
		
		return ret.toJSONString();

	}

	/**
	 * TODO 去掉客户名称字段，通过ID指定
	 * 新建合同接口地址:http://example.com/api/v1/contract/add.do
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
	public String contractAdd(HttpServletRequest request, HttpServletResponse response,
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
		
		String contractJsonStr = dataJson.getString("contract");
		if(StringUtil.isEmpty(contractJsonStr)) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_203001.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_203001.getMsg());
			return ret.toJSONString();
		}
		
		JSONObject contractJson = null;
		try {
			contractJson = JSON.parseObject(contractJsonStr);
		} catch(Exception e) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_203002.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_203002.getMsg());
			return ret.toJSONString();
		}
		
		if(contractJson == null) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_203001.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_203001.getMsg());
			return ret.toJSONString();
		}
		
		boolean flag = apiHelper.getInfo(corpid, modelMap, RedundantTemplateTypeEnum.CONTRACT.getCode(), contractJson, true);
		
		if(!flag) {
			ret.put("errorCode", modelMap.get("errorCode"));
			ret.put("msg", modelMap.get("msg"));
			return ret.toJSONString();
		}
		
		Integer templateId = (Integer) modelMap.get("templateId");
		@SuppressWarnings("unchecked")
		Map<String,RedundantFieldExplainEntity> explainMap = (Map<String,RedundantFieldExplainEntity>) modelMap.get("explainMap");
		
		String contractNoAttr = "合同编号";
		String signPersonAttr = "签订人";
		int setAttrSemaphore = 2;//有两个attr要查找，找到两个就break
		for(RedundantFieldExplainEntity explain : explainMap.values()) {
			if(explain.getAttr().equals("contractNo")) {
				contractNoAttr = explain.getAttrName();
				setAttrSemaphore--;
				if(setAttrSemaphore <= 0) {
					break;
				}
			}
			if(explain.getAttr().equals("signPerson")) {
				signPersonAttr = explain.getAttrName();
				setAttrSemaphore--;
				if(setAttrSemaphore <= 0) {
					break;
				}
			}
		}
		
		String contractNo = contractJson.getString(contractNoAttr);
		
		if(StringUtil.isEmpty(contractNo)) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_203008.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_203008.getMsg());
			return ret.toJSONString();
		}
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("corpid", corpid);
		param.put("delNeg", 1);//不等于1，目前是0或2，即合同列表中或审批中
		param.put("contractNo", contractNo);
		
		Integer contractCount = contractModel.getEntitysCount(param);
		if(contractCount > 0) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_203003.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_203003.getMsg());
			return ret.toJSONString();
		}
		
		//创建者逻辑 TODO 创建者不存在是否让创建记录
		UserEntity creatorUser = null;
		String creatorName = contractJson.getString("创建人");
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
			creatorUser = creatorUsers.size() > 0 ? creatorUsers.get(0) : null;
			
			if(creatorUser != null) {
				creatorUserId = creatorUser.getUserId();
				creatorUserName = creatorUser.getName();
			}
		}
		
		//签订人
		String signName= contractJson.getString(signPersonAttr);
		String signUserId = "0";
		String signUserName = "";
		
		if(!StringUtil.isEmpty(signName)) {
			param.clear();
			param.put("corpid", corpid);
			param.put("name", signName);
			param.put("negDel", 1);
			param.put("start", 0);
			param.put("pageNum", 1);
			
			List<UserEntity> signUsers = userModel.findEntitys(param);
			UserEntity signUser = signUsers.size() > 0 ? signUsers.get(0) : null;
			
			if(signUser != null) {
				signUserId = signUser.getUserId();
				signUserName = signUser.getName();
			}
		}
		
		Map<Integer,Map<Integer, DataDictionaryEntity>> allDataDictionarys = dataDictionaryModel.getDataDictionaryMap(corpid);
		
		Map<Integer,DataDictionaryEntity> statusMap = allDataDictionarys.get(DictionaryTypeEnum.CONTRACT_STATUS.getCode());
		Map<Integer,DataDictionaryEntity> typeMap = allDataDictionarys.get(DictionaryTypeEnum.CONTRACT_TYPE.getCode());
		
		Integer now = DateUtil.getInt();
		//合同
		ContractEntity contractEntity = new ContractEntity();
		contractEntity.setCorpid(corpid);
		contractEntity.setTemplateId(templateId);
		contractEntity.setUserId(creatorUserId);
		contractEntity.setUserName(creatorUserName);
		contractEntity.setAddTime(now);
		contractEntity.setDel(0);
		contractEntity.setArchived(0);
		contractEntity.setSignUserId(signUserId);
		contractEntity.setSignPerson(signUserName);
		contractEntity.setCustomerId(customerId);
		contractEntity.setCustomerName(customer.getName());
		
		//客户冗余信息
		RedundantFieldEntity fieldEntity = new RedundantFieldEntity();
		fieldEntity.setTemplateId(templateId);
		fieldEntity.setCorpid(corpid);
		fieldEntity.setRefType(RedundantTemplateTypeEnum.CONTRACT.getCode());
		fieldEntity.setRefId(0);
		
		contractEntity.setTypeMap(typeMap);
		contractEntity.setStatusMap(statusMap);
		boolean setValueFlag = apiHelper.setValue(contractJson, explainMap, contractEntity, fieldEntity, modelMap);
		if(!setValueFlag){
			ret.put("errorCode", modelMap.get("errorCode"));
			ret.put("msg", modelMap.get("msg"));
			return ret.toJSONString();
		}
		
		contractEntity.setUpdateTime(now);

		try {
			contractModel.save(contractEntity);
			if(templateId != 0){
				fieldEntity.setRefId(contractEntity.getId());
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
			ContractUserEntity contractUserEntity = new ContractUserEntity();
			contractUserEntity.setCorpid(corpid);
			contractUserEntity.setContractId(contractEntity.getId());
			contractUserEntity.setIsMain(customerUser.getIsMain());	//新建时直接设置设置
			contractUserEntity.setUserId(customerUser.getUserId());
			contractUserEntity.setUserName(customerUser.getUserName());
			contractUserEntity.setAddTime(now);
			contractUserEntity.setUserAvatar("");//头像设成空
			contractUserEntity.setDel(0);
			contractUserModel.insert(contractUserEntity);
		}
		
		ret.put("contractId", contractEntity.getId());
		ret.put("errorCode", 0);
		ret.put("msg", "操作成功");
		return ret.toJSONString();
	}
	
	/**
	 * TODO 修改签订人   客户ID错误
	 * 合同修改接口地址:http://example.com/api/v1/contract/update.do
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
	public String contractUpdate(HttpServletRequest request, HttpServletResponse response,
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
		
		String contractJsonStr = dataJson.getString("contract");
		if(StringUtil.isEmpty(contractJsonStr)) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_203001.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_203001.getMsg());
			return ret.toJSONString();
		}
		
		JSONObject contractJson = null;
		try {
			contractJson = JSON.parseObject(contractJsonStr);
		} catch(Exception e) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_203002.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_203002.getMsg());
			return ret.toJSONString();
		}
		
		if(contractJson == null) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_203001.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_203001.getMsg());
			return ret.toJSONString();
		}
		
		Integer contractId = contractJson.getInteger("contractId");
		if(contractId == null || contractId <= 0) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_203004.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_203004.getMsg());
			return ret.toJSONString();
		}
		
		//合同
		ContractEntity contractEntity = contractModel.getByKey(contractId, corpid);
		if(contractEntity == null) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_203005.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_203005.getMsg());
			return ret.toJSONString();
		}
		
		boolean flag = apiHelper.getInfo(corpid, modelMap, RedundantTemplateTypeEnum.CONTRACT.getCode(), contractJson, false);
		
		if(!flag) {
			ret.put("errorCode", modelMap.get("errorCode"));
			ret.put("msg", modelMap.get("msg"));
			return ret.toJSONString();
		}
		
		Integer templateId = (Integer) modelMap.get("templateId");
		@SuppressWarnings("unchecked")
		Map<String,RedundantFieldExplainEntity> explainMap = (Map<String,RedundantFieldExplainEntity>) modelMap.get("explainMap");
		
		String contractNoAttr = "合同编号";
		String signPersonAttr = "签订人";
		int setAttrSemaphore = 2;//有两个attr要查找，找到两个就break
		for(RedundantFieldExplainEntity explain : explainMap.values()) {
			if(explain.getAttr().equals("contractNo")) {
				contractNoAttr = explain.getAttrName();
				setAttrSemaphore--;
				if(setAttrSemaphore <= 0) {
					break;
				}
			}
			if(explain.getAttr().equals("signPerson")) {
				signPersonAttr = explain.getAttrName();
				setAttrSemaphore--;
				if(setAttrSemaphore <= 0) {
					break;
				}
			}
		}
		
		String contractNo = contractJson.getString(contractNoAttr);
		
		if(!StringUtil.isEmpty(contractNo)) {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("corpid", corpid);
			param.put("delNeg", 1);//不等于1，目前是0或2，即合同列表中或审批中
			param.put("contractNo", contractNo);
			
			//非本条记录
			param.put("idNeg", contractId);
			
			Integer contractCount = contractModel.getEntitysCount(param);
			if(contractCount > 0) {
				ret.put("errorCode", ErrcodeEnum.API_ERROR_203003.getCode());
				ret.put("msg", ErrcodeEnum.API_ERROR_203003.getMsg());
				return ret.toJSONString();
			}
		}
		
		//签订人
		String signName= contractJson.getString(signPersonAttr);
		String signUserId = "0";
		String signUserName = "";
		
		if(!StringUtil.isEmpty(signName)) {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("corpid", corpid);
			param.put("name", signName);
			param.put("negDel", 1);
			param.put("start", 0);
			param.put("pageNum", 1);
			
			List<UserEntity> signUsers = userModel.findEntitys(param);
			UserEntity signUser = signUsers.size() > 0 ? signUsers.get(0) : null;
			
			if(signUser != null) {
				signUserId = signUser.getUserId();
				signUserName = signUser.getName();
			}
		}
		
		RedundantFieldEntity fieldEntity = redundantFieldModel.getByRefTypeAndRefId(corpid, RedundantTemplateTypeEnum.CONTRACT.getCode(), contractId);
		if(fieldEntity == null) {
			fieldEntity = new RedundantFieldEntity();
			fieldEntity.setTemplateId(templateId);
			fieldEntity.setCorpid(corpid);
			fieldEntity.setRefType(RedundantTemplateTypeEnum.CONTRACT.getCode());
			fieldEntity.setRefId(0);
		}
		
		Map<Integer,Map<Integer, DataDictionaryEntity>> allDataDictionarys = dataDictionaryModel.getDataDictionaryMap(corpid);
		
		Map<Integer,DataDictionaryEntity> statusMap = allDataDictionarys.get(DictionaryTypeEnum.CONTRACT_STATUS.getCode());
		Map<Integer,DataDictionaryEntity> typeMap = allDataDictionarys.get(DictionaryTypeEnum.CONTRACT_TYPE.getCode());
		
		contractEntity.setTypeMap(typeMap);
		contractEntity.setStatusMap(statusMap);
		boolean setValueFlag = apiHelper.setValue(contractJson, explainMap, contractEntity, fieldEntity, modelMap);
		if(!setValueFlag){
			ret.put("errorCode", modelMap.get("errorCode"));
			ret.put("msg", modelMap.get("msg"));
			return ret.toJSONString();
		}
		
		if(!signUserId.equals("0") && !signUserName.equals("")) {
			contractEntity.setSignPerson(signUserName);
			contractEntity.setSignUserId(signUserId);
		}
		
		Integer now = DateUtil.getInt();
		contractEntity.setUpdateTime(now);
		
		try {
			contractModel.save(contractEntity);
			if(templateId != 0){
				fieldEntity.setRefId(contractEntity.getId());
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
	 * 获取合同详情接口地址:http://example.com/api/v1/contract/get.do
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
	public String contractGet(HttpServletRequest request, HttpServletResponse response, Map<String, Object> modelMap)
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
		
		Integer contractId = StringUtil.StringToInteger(dataJson.getString("contractId"), 0);
		
		if(contractId == null || contractId <= 0) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_203004.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_203004.getMsg());
			return ret.toJSONString();
		}
		
		ContractEntity contract = contractModel.getByKey(contractId, corpid);
		
		if(contract == null) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_203005.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_203005.getMsg());
			return ret.toJSONString();
		}
		
		//模板ID
		Integer templateId = contract.getTemplateId();
		
		RedundantFieldEntity fieldEntity = redundantFieldModel.getByRefTypeAndRefId(corpid, RedundantTemplateTypeEnum.CONTRACT.getCode(), contractId);
		
		List<RedundantFieldExplainEntity> explains = redundantFieldModel.getRedundantFieldExplains(RedundantTemplateTypeEnum.CONTRACT.getCode(), corpid, templateId);
		
		//数据字典
		Map<Integer,Map<Integer, DataDictionaryEntity>> dataDictionaryMap = dataDictionaryModel.getDataDictionaryMap(corpid);
		Map<Integer,DataDictionaryEntity> statusMap = dataDictionaryMap.get(DictionaryTypeEnum.CONTRACT_STATUS.getCode());
		Map<Integer,DataDictionaryEntity> typeMap = dataDictionaryMap.get(DictionaryTypeEnum.CONTRACT_TYPE.getCode());
		//数据字典渲染
		String statusStr ="";
		if(statusMap.get(contract.getStatus()) != null){
			statusStr = statusMap.get(contract.getStatus()).getName();
		}
		contract.setStatusStr(statusStr);
		
		String typeStr ="";
		if(typeMap.get(contract.getType()) != null){
			typeStr = typeMap.get(contract.getType()).getName();
		}
		contract.setTypeStr(typeStr);
		
		JSONObject retJSON = FormateRedundantValueHelper.formateRedundantValue(RedundantTemplateTypeEnum.CONTRACT.getCode(), explains, fieldEntity, contract);
		
		//获取回款&产品信息
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("corpid", corpid);
		param.put("contractId", contractId);
		param.put("del", 0);
		
		List<PaymentEntity> paymentList = paymentModel.findEntitys(param);
		
		String[] paymentFilterStr = {"id","userId", "userName", "contractId", "customerId", "paymentNo", "amount", "status", "addTime", "updateTime", "unAmount", "estimateTime", "payTime"}; //保留字段
		ret.put("paymentList", FastJsonHelper.parseIncludeObject(PaymentEntity.class, paymentFilterStr, paymentList));
		
		List<ContractProductEntity> contractProductList = contractProductModel.findEntitys(param);
		
		String[] productFilterStr = {"productId","contractId", "productNum", "productName", "price", "specification"}; //保留字段
		ret.put("contractProductList", FastJsonHelper.parseIncludeObject(ContractProductEntity.class, productFilterStr, contractProductList));
		
		//合同销售团队列表
		List<ContractUserEntity> contractUserList = contractUserModel.findEntitys(param);
		
		String[] contractUserStr = {"userId","userName", "userAvatar", "contractId", "isMain"}; //保留字段
		ret.put("contractUserList", FastJsonHelper.parseIncludeObject(ContractUserEntity.class, contractUserStr, contractUserList));
		
		ret.put("contract", retJSON);
		
		ret.put("errorCode", 0);
		ret.put("msg", "获取成功");
		return ret.toJSONString();
	}

	/**
	 * 合同删除接口地址:http://example.com/api/v1/contract/delete.do
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
	public String contractDelete(HttpServletRequest request, HttpServletResponse response, Map<String, Object> modelMap)
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
		
		Integer contractId = StringUtil.StringToInteger(dataJson.getString("contractId"), 0);
		
		if(contractId == null || contractId <= 0) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_203004.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_203004.getMsg());
			return ret.toJSONString();
		}
		
		ContractEntity contract = contractModel.getByKey(contractId, corpid);
		
		if(contract == null) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_203005.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_203005.getMsg());
			return ret.toJSONString();
		}
		
		if(contract.getDel().equals(1)) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_203009.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_203009.getMsg());
			return ret.toJSONString();
		}
		
		try {
			contractModel.deleteByKey(contractId, corpid);
		} catch(Exception e) {
			LOG.error("数据操作失败", e);
			ret.put("errorCode", ErrcodeEnum.API_ERROR_100004.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_100004.getMsg());
			return ret.toJSONString();
		}
		
		/*
		 * 删除回款计划，回款单，合同关联产品
		 */
		//删除合同关联产品
		contractProductModel.deleteByContractId(contractId, corpid);
		//删除合同关联回款单
		paymentSheetModel.deleteByContractId(contractId, corpid);
		//删除合同关联回款计划
		paymentModel.deleteByContractId(contractId, corpid);
		
		ret.put("contractId", contractId);
		ret.put("errorCode", 0);
		ret.put("msg", "删除成功");
		return ret.toJSONString();
	}
	
	/**
	 * 获取合同关联产品列表接口地址:http://example.com/api/v1/contract/product/list.do 输入参数：data={}
	 * 成功输出：data={"errorcode":0,"msg":"获取成功","ret":""}
	 * 失败输出：data={"errorcode":*,"msg":"获取失败","ret":""}
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/product/list")
	public String contractProductList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> modelMap)
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
		
		Integer contractId = StringUtil.StringToInteger(dataJson.getString("contractId"), 0);
		
		if(contractId == null || contractId <= 0) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_203004.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_203004.getMsg());
			return ret.toJSONString();
		}
		
		ContractEntity contract = contractModel.getByKey(contractId, corpid);
		
		if(contract == null) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_203005.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_203005.getMsg());
			return ret.toJSONString();
		}
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("corpid", corpid);
		param.put("contractId", contractId);
		
		List<ContractProductEntity> list = contractProductModel.findEntitys(param);
		
		String[] contractProductFilterStr = {"productId", "productName", "productNum",  "price", "specification"}; //保留字段
		ret.put("contracProductList", FastJsonHelper.parseIncludeObject(ContractProductEntity.class, contractProductFilterStr, list));
		
		ret.put("totalCount", list.size());
		ret.put("contractId", contractId);
		ret.put("errorCode", 0);
		ret.put("msg", "获取成功");
		return ret.toJSONString();
	}
	
	/**
	 * TODO 添加相同产品时问题
	 * 合同关联产品添加接口地址:http://example.com/api/v1/contract/product/add.do
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
	@RequestMapping("/product/add")
	public String contractProductAdd(HttpServletRequest request, HttpServletResponse response,
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
		
		Integer contractId = StringUtil.StringToInteger(dataJson.getString("contractId"), 0);
		
		if(contractId == null || contractId <= 0) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_203004.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_203004.getMsg());
			return ret.toJSONString();
		}
		
		ContractEntity contract = contractModel.getByKey(contractId, corpid);
		
		if(contract == null) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_203005.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_203005.getMsg());
			return ret.toJSONString();
		}
		
		Integer productId = StringUtil.StringToInteger(dataJson.getString("productId"));
		
		if(productId == null || productId <= 0) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_204001.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_204001.getMsg());
			return ret.toJSONString();
		}
		
		Double productNum = StringUtil.toDouble(dataJson.getString("productNum"));

		if(productNum == null || productNum < 0.0000001) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_203006.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_203006.getMsg());
			return ret.toJSONString();
		}
		
		ProductEntity product = productModel.getByKey(productId, corpid);
		
		if(product == null) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_204002.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_204002.getMsg());
			return ret.toJSONString();
		}
		
		Double price = product.getPrice();
		String priceStr = dataJson.getString("price");
		if(!StringUtil.isEmpty(priceStr)) {
			try {
				price = Double.valueOf(priceStr);
			} catch(Exception e) {
				price = product.getPrice();
			}
		}
		
		ContractProductEntity contractProduct = new ContractProductEntity();
		contractProduct.setContractId(contractId);
		contractProduct.setCorpid(corpid);
		contractProduct.setPrice(price);
		contractProduct.setProductId(productId);
		contractProduct.setProductName(product.getName());
		contractProduct.setProductNum(productNum);
		contractProduct.setSpecification(product.getSpecification());
		
		try {
			contractProductModel.insert(contractProduct);
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
	 * 合同关联产品修改接口地址:http://example.com/api/v1/contract/product/update.do
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
	@RequestMapping("/product/update")
	public String contractProductUpdate(HttpServletRequest request, HttpServletResponse response,
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
		
		Integer contractId = StringUtil.StringToInteger(dataJson.getString("contractId"), 0);
		
		if(contractId == null || contractId <= 0) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_203004.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_203004.getMsg());
			return ret.toJSONString();
		}
		
		Integer productId = StringUtil.StringToInteger(dataJson.getString("productId"));
		
		if(productId == null || productId <= 0) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_204001.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_204001.getMsg());
			return ret.toJSONString();
		}
		
		Double productNum = StringUtil.toDouble(dataJson.getString("productNum"));
		Double price = StringUtil.toDouble(dataJson.getString("price"));
		
		
		ContractProductEntity contractProduct = contractProductModel.getByKey(corpid, contractId, productId);
		if(contractProduct == null) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_203007.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_203007.getMsg());
			return ret.toJSONString();
		}
			
		boolean needUpdate = false;
		if(price != null && price > 0.0000001) {
			contractProduct.setPrice(price);
			needUpdate = true;
		}
		if(productNum != null && productNum > 0.0000001) {
			contractProduct.setProductNum(productNum);
			needUpdate = true;
		}
		
		if(needUpdate) {
			try {
				contractProductModel.update(contractProduct);
			} catch(Exception e) {
				LOG.error("数据操作失败", e);
				ret.put("errorCode", ErrcodeEnum.API_ERROR_100004.getCode());
				ret.put("msg", ErrcodeEnum.API_ERROR_100004.getMsg());
				return ret.toJSONString();
			}
		}
		
		ret.put("errorCode", 0);
		ret.put("msg", "操作成功");
		return ret.toJSONString();
	}
	
	/**
	 * 删除合同关联产品接口地址:http://example.com/api/v1/contract/product/delete.do
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
	@RequestMapping("/product/delete")
	public String contractProductDelete(HttpServletRequest request, HttpServletResponse response,
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
		
		Integer contractId = StringUtil.StringToInteger(dataJson.getString("contractId"), 0);
		
		if(contractId == null || contractId <= 0) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_203004.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_203004.getMsg());
			return ret.toJSONString();
		}
		
		Integer productId = StringUtil.StringToInteger(dataJson.getString("productId"));
		
		if(productId == null || productId <= 0) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_204001.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_204001.getMsg());
			return ret.toJSONString();
		}
		
		ContractProductEntity contractProduct = contractProductModel.getByKey(corpid, contractId, productId);
		if(contractProduct == null) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_203007.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_203007.getMsg());
			return ret.toJSONString();
		}
		
		try {
			contractProductModel.deleteContractProduct(corpid, contractId, productId);
		} catch(Exception e) {
			LOG.error("数据操作失败", e);
			ret.put("errorCode", ErrcodeEnum.API_ERROR_100004.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_100004.getMsg());
			return ret.toJSONString();
		}
		
		ret.put("errorCode", 0);
		ret.put("msg", "删除成功");
		return ret.toJSONString();
	}
	
	/**
	 * 获取合同关联回款列表接口地址:http://example.com/api/v1/contract/payment/list.do 输入参数：data={}
	 * 成功输出：data={"errorcode":0,"msg":"获取成功","ret":""}
	 * 失败输出：data={"errorcode":*,"msg":"获取失败","ret":""}
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/payment/list")
	public String contractPaymentList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> modelMap)
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
		
		Integer contractId = StringUtil.StringToInteger(dataJson.getString("contractId"), 0);
		
		if(contractId == null || contractId <= 0) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_203004.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_203004.getMsg());
			return ret.toJSONString();
		}
		
		ContractEntity contract = contractModel.getByKey(contractId, corpid);
		
		if(contract == null) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_203005.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_203005.getMsg());
			return ret.toJSONString();
		}
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("corpid", corpid);
		param.put("contractId", contractId);
		param.put("del", 0);
		
		List<PaymentEntity> list = paymentModel.findEntitys(param);
		
		String[] paymentFilterStr = {"id", "userId", "userName", "customerId", "paymentNo", "amount", "contractId", 
				"contractName", "estimateTime", "accountPeriod", "payTime", "status", "addTime", "updateTime"}; //保留字段
		ret.put("paymentList", FastJsonHelper.parseIncludeObject(PaymentEntity.class, paymentFilterStr, list));
		
		ret.put("totalCount", list.size());
		ret.put("contractId", contractId);
		ret.put("errorCode", 0);
		ret.put("msg", "获取成功");
		return ret.toJSONString();
	}
	
	
	/**
	 * 回款添加接口地址:http://example.com/api/v1/contract/payment/add.do 输入参数：data={}
	 * 成功输出：data={"errorcode":0,"msg":"获取成功","ret":""}
	 * 失败输出：data={"errorcode":*,"msg":"获取失败","ret":""}
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 * @throws Exception
	 * kaka
	 * 2016年11月29日 下午4:37:27
	 */
	@ResponseBody
	@RequestMapping("/payment/add")
	public String contractPaymentAdd(HttpServletRequest request, HttpServletResponse response, Map<String, Object> modelMap)
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
		
		Integer contractId = StringUtil.StringToInteger(dataJson.getString("contractId"), 0);
		
		if(contractId == null || contractId <= 0) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_203004.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_203004.getMsg());
			return ret.toJSONString();
		}
		
		ContractEntity contract = contractModel.getByKey(contractId, corpid);
		
		if(contract == null) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_203005.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_203005.getMsg());
			return ret.toJSONString();
		}
		
		String paymentJsonStr = dataJson.getString("payment");
		if(StringUtil.isEmpty(paymentJsonStr)) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_203010.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_203010.getMsg());
			return ret.toJSONString();
		}
		
		JSONObject paymentJson = null;
		try {
			paymentJson = JSON.parseObject(paymentJsonStr);
		} catch(Exception e) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_203011.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_203011.getMsg());
			return ret.toJSONString();
		}
		
		if(paymentJson == null) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_203010.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_203010.getMsg());
			return ret.toJSONString();
		}
		
		String paymentNo = paymentJson.getString("paymentNo"); 									//回款编号
		Double amount = paymentJson.getDouble("amount");										//回款金额
		Integer status = paymentJson.getInteger("status");										//回款状态，1，未收款，2已收款
		Integer estimateTime = paymentJson.getInteger("estimateTime");							//预计回款时间
		Integer payTime = paymentJson.getInteger("payTime");									//实际回款时间
//		String memo = paymentJson.getString("memo") != null ? paymentJson.getString("memo"): "";//回款备注
		String userName = paymentJson.getString("userName");									//回款创建人
		
		/*
		 * 1.必填项：回款金额，回款状态，回款创建人，预计回款时间
		 * 2.实际回款时间，若为未收款则默认是0，若为已收款没有填则默认是当天
		 * 3.回款编号若填则查看数据库中是否存在该回款编号，若不填则自动生成
		 */
		if(amount == null || amount < 0D) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_100005.getCode());
			ret.put("msg", "回款计划金额字段[amount]不能为空");
			return ret.toJSONString();
		}
		
		if(status == null || status < 0 || status > 2) { //接口添加回款计划，状态只能是未收款或已收款
			ret.put("errorCode", ErrcodeEnum.API_ERROR_100005.getCode());
			ret.put("msg", "回款计划状态字段[status]为空或不合法, 请检查参数");
			return ret.toJSONString();
		}
		
		if(estimateTime == null || estimateTime < 0) { 
			ret.put("errorCode", ErrcodeEnum.API_ERROR_100005.getCode());
			ret.put("msg", "回款计划预计回款时间字段[estimateTime]不能为空");
			return ret.toJSONString();
		}
		
		if(StringUtil.isEmpty(userName)) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_100005.getCode());
			ret.put("msg", "回款计划创建人字段[userName]不能为空");
			return ret.toJSONString();
		}
		
		int now = DateUtil.getInt();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("corpid", corpid);
		param.put("name", userName);
		param.put("del", 0);
		
		List<UserEntity> users = userModel.findEntitys(param);
		UserEntity user = users.size() > 0 ? users.get(0): null;
		if(user == null) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_100005.getCode());
			ret.put("msg", "无法在公司员工列表中找到回款计划创建人[userName]对应的员工，请检查参数");
			return ret.toJSONString();
		}

		if(!StringUtil.isEmpty(paymentNo)) {//填了回款编号
			param.clear();
			param.put("corpid", corpid);
			param.put("paymentNo", paymentNo);
			param.put("del", 0);
			
			Integer paymentCount = paymentModel.getEntitysCount(param);
			if(paymentCount > 0) {
				ret.put("errorCode", ErrcodeEnum.API_ERROR_203012.getCode());
				ret.put("msg", ErrcodeEnum.API_ERROR_203012.getMsg());
				return ret.toJSONString();
			}
		} else {//没填编号
			paymentNo = contract.getContractNo();
			param.clear();
			param.put("corpid", corpid);
			param.put("contractId", contractId);
			param.put("del", 0);
			param.put("OrderByStr", "add_time DESC");
			param.put("start", 0);
			param.put("pageNum", 1);
			
			List<PaymentEntity> payments = paymentModel.findEntitys(param);
			PaymentEntity payment = payments.size() > 0 ? payments.get(0) : null;
			if(payment == null) {
				paymentNo += ".1";
			} else {
				String oldPaymentNo = payment.getPaymentNo();
				Integer paymentCount = 1;
				if(!StringUtil.isEmpty(oldPaymentNo)) {
					try {
						paymentCount = Integer.parseInt(oldPaymentNo.substring(oldPaymentNo.lastIndexOf(".") + 1, oldPaymentNo.length()));
					} catch(Exception e) {
						paymentCount = 1;
					}
				}
				paymentCount++;
				paymentNo += "." + paymentCount;
			}
			
		}
		
		if(payTime == null) {
			if(status == 2) {
				payTime = now; //已收款则实际回款时间设为当前
			} else {
				payTime = 0;
			}
		}
		
		PaymentEntity payment = new PaymentEntity();
		payment.setAccountPeriod(0);
		payment.setAddTime(now);
		payment.setAmount(amount);
		payment.setContractId(contractId);
		payment.setContractName(contract.getName());
		payment.setCorpid(corpid);
		payment.setCustomerId(contract.getCustomerId());
		payment.setCustomerName(contract.getCustomerName());
		payment.setDel(0);
		payment.setEstimateTime(estimateTime);
		payment.setPaymentNo(paymentNo);
		payment.setPayTime(payTime);
		payment.setStatus(status);
		payment.setUpdateTime(now);
		payment.setUserId(user.getUserId());
		payment.setUserName(userName);
		if(status == 2) {
			payment.setUnAmount(0D);
		} else {
			payment.setUnAmount(amount);
		}

		try {
			paymentModel.insert(payment);
			if(status == 2) { //插入一条回款单
				PaymentSheetEntity paymentSheet = new PaymentSheetEntity();
				paymentSheet.setAddTime(now);
				paymentSheet.setAmount(amount);
				paymentSheet.setBelongId(contract.getSignUserId());
				paymentSheet.setBelongName(contract.getSignPerson());
				paymentSheet.setContractId(contractId);
				paymentSheet.setCorpid(corpid);
				paymentSheet.setCustomerId(contract.getCustomerId());
				paymentSheet.setDel(0);
				paymentSheet.setMemo("");
				paymentSheet.setPaymentId(payment.getId());
				paymentSheet.setPaymentSheetNo(payment.getPaymentNo() + ".1");
				paymentSheet.setUpdateTime(now);
				paymentSheet.setPayMethod(1);
				paymentSheet.setUserId(user.getUserId());
				paymentSheet.setPaymentTime(payTime);
				
				paymentSheetModel.insert(paymentSheet);
			}
		} catch(Exception e) {
			LOG.error("数据操作失败", e);
			ret.put("errorCode", ErrcodeEnum.API_ERROR_100004.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_100004.getMsg());
			return ret.toJSONString();
		}
		
		ret.put("paymentId", payment.getId());
		ret.put("errorCode", 0);
		ret.put("msg", "操作成功");
		return ret.toJSONString();
	}
	
	/**
	 * 回款计划编辑接口地址:http://example.com/api/v1/contract/payment/update.do 输入参数：data={}
	 * 成功输出：data={"errorcode":0,"msg":"获取成功","ret":""}
	 * 失败输出：data={"errorcode":*,"msg":"获取失败","ret":""}
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 * @throws Exception
	 * kaka
	 * 2016年11月29日 下午4:37:27
	 */
	@Transactional
	@ResponseBody
	@RequestMapping("/payment/update")
	public String contractPaymentUpdate(HttpServletRequest request, HttpServletResponse response, Map<String, Object> modelMap)
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
		
		String paymentJsonStr = dataJson.getString("payment");
		if(StringUtil.isEmpty(paymentJsonStr)) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_203010.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_203010.getMsg());
			return ret.toJSONString();
		}
		
		JSONObject paymentJson = null;
		try {
			paymentJson = JSON.parseObject(paymentJsonStr);
		} catch(Exception e) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_203011.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_203011.getMsg());
			return ret.toJSONString();
		}
		
		if(paymentJson == null) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_203010.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_203010.getMsg());
			return ret.toJSONString();
		}
		
		Integer paymentId = paymentJson.getInteger("paymentId");
		if(paymentId == null || paymentId <= 0) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_203013.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_203013.getMsg());
			return ret.toJSONString();
		}
		
		PaymentEntity payment = paymentModel.getByKey(paymentId, corpid);
		if(payment == null) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_203014.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_203014.getMsg());
			return ret.toJSONString();
		}
		Integer oldStatus = payment.getStatus();
		if(oldStatus == 2) { //已收款记录不能进行修改
			ret.put("errorCode", ErrcodeEnum.API_ERROR_100005.getCode());
			ret.put("msg", "已回款的回款计划不能进行修改");
			return ret.toJSONString();
		}
		
		Double amount = paymentJson.getDouble("amount");										//回款金额
		Integer status = paymentJson.getInteger("status");										//回款状态，1，未收款，2已收款
		Integer estimateTime = paymentJson.getInteger("estimateTime");							//预计回款时间
		Integer payTime = paymentJson.getInteger("payTime");									//实际回款时间
		
		/*
		 * 修改时都可以不填，不填则不修改
		 */
		if(amount != null && amount < 0D) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_100005.getCode());
			ret.put("msg", "回款计划金额字段[amount]参数不能为负值, 请检查参数");
			return ret.toJSONString();
		}
		
		if(estimateTime != null && estimateTime < 0) { 
			ret.put("errorCode", ErrcodeEnum.API_ERROR_100005.getCode());
			ret.put("msg", "回款计划预计回款时间字段[estimateTime]参数不能为负值");
			return ret.toJSONString();
		}
		int now = DateUtil.getInt();
		
		payment.setUpdateTime(now);
		if(amount != null) {
			payment.setAmount(amount);
		}
		if(estimateTime != null) {
			payment.setEstimateTime(estimateTime);
		}
		if(payTime != null && payTime >= 0) {
			payment.setPayTime(payTime);
		}
		if(payment.getStatus() != 2) {
			payment.setStatus(status);
			if(status == 2) {
				payment.setUnAmount(0D);
			}
		} 

		try {
			paymentModel.update(payment);
			if(oldStatus == 1 && payment.getStatus() == 2) {
				ContractEntity contract = contractModel.getByKey(payment.getContractId(), corpid);
				PaymentSheetEntity paymentSheet = new PaymentSheetEntity();
				paymentSheet.setAddTime(now);
				paymentSheet.setAmount(payment.getAmount());
				paymentSheet.setBelongId(contract.getSignUserId());
				paymentSheet.setBelongName(contract.getSignPerson());
				paymentSheet.setContractId(contract.getId());
				paymentSheet.setCorpid(corpid);
				paymentSheet.setCustomerId(contract.getCustomerId());
				paymentSheet.setDel(0);
				paymentSheet.setMemo("");
				paymentSheet.setPaymentId(payment.getId());
				paymentSheet.setPaymentSheetNo(payment.getPaymentNo() + ".1");
				paymentSheet.setUpdateTime(now);
				paymentSheet.setPayMethod(1);
				paymentSheet.setUserId(payment.getUserId());
				paymentSheet.setPaymentTime(payTime);
				
				paymentSheetModel.insert(paymentSheet);
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
	 * 合同回款计划删除接口地址:http://example.com/api/v1/contract/payment/delete.do
	 * 输入参数：data={"corpid":"","paymentId":""}
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
	@RequestMapping("/payment/delete")
	public String contractPaymentDelete(HttpServletRequest request, HttpServletResponse response, Map<String, Object> modelMap)
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
		
		Integer paymentId = StringUtil.StringToInteger(dataJson.getString("paymentId"), 0);
		
		if(paymentId == null || paymentId <= 0) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_203013.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_203013.getMsg());
			return ret.toJSONString();
		}
		
		PaymentEntity payment = paymentModel.getByKey(paymentId, corpid);
		
		if(payment == null) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_203014.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_203014.getMsg());
			return ret.toJSONString();
		}
		
		if(payment.getDel().equals(1)) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_203015.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_203015.getMsg());
			return ret.toJSONString();
		}
		
		try {
			paymentModel.deleteByKey(paymentId, corpid);
		} catch(Exception e) {
			LOG.error("数据操作失败", e);
			ret.put("errorCode", ErrcodeEnum.API_ERROR_100004.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_100004.getMsg());
			return ret.toJSONString();
		}
		
		/*
		 * 删除回款单
		 */
		paymentSheetModel.deleteByPaymentId(paymentId, corpid);
		
		ret.put("paymentId", paymentId);
		ret.put("errorCode", 0);
		ret.put("msg", "删除成功");
		return ret.toJSONString();
	}
}
