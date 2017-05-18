package com.xbongbong.dingxbb.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.xbongbong.dingxbb.entity.ApiTokenEntity;
import com.xbongbong.dingxbb.entity.ImportEntity;
import com.xbongbong.dingxbb.entity.RedundantFieldEntity;
import com.xbongbong.dingxbb.entity.RedundantFieldExplainEntity;
import com.xbongbong.dingxbb.entity.RedundantFieldTemplateEntity;
import com.xbongbong.dingxbb.enums.ErrcodeEnum;
import com.xbongbong.dingxbb.model.ApiTokenModel;
import com.xbongbong.dingxbb.model.CompanyModel;
import com.xbongbong.dingxbb.model.RedundantFieldExplainModel;
import com.xbongbong.dingxbb.model.RedundantFieldTemplateModel;
import com.xbongbong.util.DigestUtil;
import com.xbongbong.util.ReflectHelper;
import com.xbongbong.util.StringUtil;
import com.xbongbong.util.URLEncodeUtils;

@Component
public class ApiHelper {
	
	public static final String API_ACCESS_PER_MINUTE = "apiAccessPerMinute";
	
	public static final String API_ACCESS_IS_VIP = "apiAccessIsVip";
	
	public static final Long MAX_ACCESS_TIMES_PER_SESSION_ID_PER_MINUTE = 1000L;
	
	private static final Integer ONE_DAY = 24 * 60 * 60;
	
	private static final Logger LOG = LogManager.getLogger(ApiHelper.class);
	
	@Autowired
	private RedundantFieldExplainModel redundantFieldExplainModel;
	@Autowired
	private RedundantFieldTemplateModel redundantFieldTemplateModel;
	@Autowired
	private ApiTokenModel apiTokenModel;
	@Autowired
	private CompanyModel companyModel;
	
	/**
	 * 检查每个sessionId每分钟的访问次数
	 * @param request
	 * @param response
	 * @param ret
	 * @return
	 * @author kaka
	 */
	public boolean checkFrequency(HttpServletRequest request, HttpServletResponse response, JSONObject ret) {
		Long accessTimes = JedisUtils.getIncrNum(request, response, API_ACCESS_PER_MINUTE);
		if(accessTimes != null && accessTimes > MAX_ACCESS_TIMES_PER_SESSION_ID_PER_MINUTE) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_100009.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_100009.getMsg());
			return false;
		}
		
		return true;
	}
	
	/**
	 * 检查签名是否正确
	 * 如果验证失败，该方法会影响到传入的ret参数，用于错误返回
	 * @param data
	 * @param corpId
	 * @param sign
	 * @param ret
	 * @return
	 * 
	 * @author kaka
	 */
	public boolean checkToken(String data, String corpId, String sign, JSONObject ret) {
		LOG.debug("data : " + data);
		LOG.debug("sign : " + sign);
		
		boolean flag = false;
		ApiTokenEntity realToken = apiTokenModel.getByCorpId(corpId);
		if(realToken == null) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_100008.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_100008.getMsg());
			return false;
		}
		
		String realTokenStr = realToken.getToken();
		if(StringUtil.isEmpty(realTokenStr)) {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_100008.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_100008.getMsg());
			return false;
		}
		
		String culculateSign = DigestUtil.Encrypt(data + realTokenStr, "SHA-256");
		data = URLEncodeUtils.encodeURL(data); 
		String culculateSignUrlEncoded = DigestUtil.Encrypt(data + realTokenStr, "SHA-256");
		if(sign != null && (sign.equalsIgnoreCase(culculateSign) || sign.equalsIgnoreCase(culculateSignUrlEncoded))) {
			Map<String, Object> tmpMap = new HashMap<String, Object>();
			int isVip = 0;//是否是VIP
			boolean fromDB = false;//是否从数据库查询出的vip数据，默认先走缓存
			try {
				isVip = (int) JedisUtils.getJedisValue(API_ACCESS_IS_VIP, corpId);
			} catch (Exception e) {
				isVip = 0;
			}
			if(isVip == 0) {
				isVip = companyModel.isVip(corpId, tmpMap);
				fromDB = true;
			}
			if(isVip == 0) {
				ret.put("errorCode", ErrcodeEnum.API_ERROR_100010.getCode());
				ret.put("msg", ErrcodeEnum.API_ERROR_100010.getMsg());
				flag = false;
			} else {
				if(fromDB) {
//					System.out.println("from 数据库");
					JedisUtils.setJedisValue(API_ACCESS_IS_VIP, corpId, 1, ONE_DAY);
				} else {
//					System.out.println("from 缓存");
				}
				flag = true;
			}
		} else {
			ret.put("errorCode", ErrcodeEnum.API_ERROR_100003.getCode());
			ret.put("msg", ErrcodeEnum.API_ERROR_100003.getMsg());
			flag = false;
		}
		return flag;
	}
	
	/**
     * 
     * @param explainMap	Map<String,解释的实体> 每个公司一份
     * @param entity		需要转化的实体，可以是客户、联系人、销售机会、合同
     * @param fieldEntity	扩展实体，可以是客户、联系人、销售机会、合同的扩展实体
     * @param modelMap  
     * @return
     */
	public boolean setValue(JSONObject entityJson, Map<String,RedundantFieldExplainEntity> explainMap, ImportEntity entity, 
    		RedundantFieldEntity fieldEntity, Map<String,Object> modelMap){
    	//赋值
    	boolean success = true;
		for(Entry<String, Object> entry : entityJson.entrySet()){
			String name = entry.getKey();
			String value = null;
			if(entry.getValue() instanceof String) {
				value = (String) entry.getValue();
			} else {
				value = String.valueOf(entry.getValue());
			}
			value = StringUtil.isEmpty(value) ? "" : StringUtil.trim(value);
			RedundantFieldExplainEntity explainEntity = explainMap.get(name);
			if(explainEntity == null){//不再模板中的字段
				continue ;
			}
			String attr = explainEntity.getAttr();
			
			//在原表中的字段处理
			if(ReflectHelper.exist(entity, attr)){//通过反射，判断attr是否是实体中的属性
				//设置不能直接赋值的字段
				Integer code = entity.setValue(value, attr);
				//值不存在，提示错误
				if(code.equals(2)){
					modelMap.put("errCode", ErrcodeEnum.API_ERROR_100005.getCode());
					modelMap.put("msg", name + "不存在或格式错误");
					success = false;
					return success;
				}
			}
			if(ReflectHelper.exist(fieldEntity, attr)){//在冗余表中的字段 
				//自定义图片上传，文件上传，多选类型特殊处理
				value = fieldEntity.importSpecExtend(value, explainMap, name);
				if(value != null) {
					ReflectHelper.valueSet(fieldEntity, attr, value);
				} else {
					modelMap.put("errCode", ErrcodeEnum.API_ERROR_100005.getCode());
					modelMap.put("msg", "“" + name + "”参数格式错误，请检查数据格式");
					success = false;
					return success;
				}
			}
		}
		return success;
    }
	
	/**
	 * 
	 * @param corpid
	 * @param modelMap
	 * @param type
	 * @param entityJson
	 * @return
	 * @author kaka
	 * @time 2016年9月26日 下午4:16:25
	 */
    public boolean getInfo(String corpid, Map<String, Object> modelMap, Integer type, JSONObject entityJson, boolean insert) {
		
		Integer templateId = 0;
		RedundantFieldTemplateEntity templateEntity = redundantFieldTemplateModel.getDefaultTemplate(corpid, type);
		if(templateEntity != null){
			templateId = templateEntity.getId();
		}
		
		String typeStr = "";
		switch (type) {
		case 1: //RedundantTemplateTypeEnum.CUSTOMER.getCode()
			typeStr = "customer";
			break;
		case 2: //RedundantTemplateTypeEnum.SALES_OPPORTUNITY.getCode()
			typeStr = "opportunity";
			break;
		case 3: //RedundantTemplateTypeEnum.CONTRACT.getCode()
			typeStr = "contract";
			break;
		case 4: //RedundantTemplateTypeEnum.CONTACT.getCode()
			typeStr = "contact";
			break;
		case 5: //RedundantTemplateTypeEnum.PRODUCT.getCode()
			typeStr = "product";
			break;
		default:
			break;
		}
		//获取排序好的解释列表,若templateId=0，通过type对应的枚举生成
		List<RedundantFieldExplainEntity> explainList = redundantFieldExplainModel.getExplainList(typeStr, templateId, corpid);	
		List<RedundantFieldExplainEntity> errorExplainList = getErrorExplainList(type,explainList);
		//获取模板中name（title）对应的解释
		Map<String,RedundantFieldExplainEntity> explainMap = redundantFieldExplainModel.getExplainMap(errorExplainList, false);
		
		//数据正确性检验(解释中的attrName能在excel中完全找到)
		if(insert) {
			for(Entry<String, RedundantFieldExplainEntity> entry : explainMap.entrySet()){
				String title = entry.getKey();
				RedundantFieldExplainEntity explain = entry.getValue();
				if(explain.getFieldType().equals(6)) {
					//图片不验证必填
					continue;
				}
				if(explain.getRequired().equals(1) && (!entityJson.containsKey(title) || StringUtil.isEmpty(entityJson.getString(title)))){
					modelMap.put("code", ErrcodeEnum.API_ERROR_100005.getCode());
					modelMap.put("msg", title + "参数缺失，请检查接口参数！");
					return false;
				}
			}
		}
		
		modelMap.put("errorCode", 1);
		modelMap.put("msg", "获取成功");
		modelMap.put("templateId", templateId);
		modelMap.put("explainMap", explainMap);
		return true;
    }
    
	public static List<RedundantFieldExplainEntity> getErrorExplainList(
			Integer type, List<RedundantFieldExplainEntity> explainList) {
		List<RedundantFieldExplainEntity> errorExplainList = new ArrayList<RedundantFieldExplainEntity>();
		for(RedundantFieldExplainEntity entity : explainList){
			Integer fieldType = entity.getFieldType();
			if(fieldType == null || fieldType.equals(5) /*|| fieldType.equals(6)*/ || fieldType.equals(8)){ //图片文件不在导入范围
				continue ;
			}
			
			errorExplainList.add(entity);
		}
		switch (type) {
		case 1: //RedundantTemplateTypeEnum.CUSTOMER.getCode()
			RedundantFieldExplainEntity userExplainEntity = new RedundantFieldExplainEntity();
			userExplainEntity.setAttr("userName");
			userExplainEntity.setAttrName("负责人");
			userExplainEntity.setEnable(1);
			userExplainEntity.setRequired(0);
			userExplainEntity.setIsRedundant(0);
			errorExplainList.add(userExplainEntity);
			RedundantFieldExplainEntity minorUserExplainEntity = new RedundantFieldExplainEntity();
			minorUserExplainEntity.setAttr("minorUserName");
			minorUserExplainEntity.setAttrName("协同人");
			minorUserExplainEntity.setEnable(1);
			minorUserExplainEntity.setRequired(0);
			minorUserExplainEntity.setIsRedundant(0);
			errorExplainList.add(minorUserExplainEntity);
			break;
		case 2: //RedundantTemplateTypeEnum.SALES_OPPORTUNITY.getCode()
			
			break;
		case 3: //RedundantTemplateTypeEnum.CONTRACT.getCode()
			RedundantFieldExplainEntity amountExplainEntity = new RedundantFieldExplainEntity();
			amountExplainEntity.setAttr("paymentAmount");
			amountExplainEntity.setAttrName("回款金额");
			amountExplainEntity.setEnable(1);
			amountExplainEntity.setRequired(0);
			amountExplainEntity.setIsRedundant(0);
			RedundantFieldExplainEntity estimateTimeExplainEntity = new RedundantFieldExplainEntity();
			estimateTimeExplainEntity.setAttr("paymentEstimateTime");
			estimateTimeExplainEntity.setAttrName("预计回款时间");
			estimateTimeExplainEntity.setEnable(1);
			estimateTimeExplainEntity.setRequired(0);
			estimateTimeExplainEntity.setIsRedundant(0);
			//TODO  添加实际回款时间
			RedundantFieldExplainEntity payTimeExplainEntity = new RedundantFieldExplainEntity();
			payTimeExplainEntity.setAttr("payTime");
			payTimeExplainEntity.setAttrName("实际回款时间");
			payTimeExplainEntity.setEnable(1);
			payTimeExplainEntity.setRequired(0);
			payTimeExplainEntity.setIsRedundant(0);
			RedundantFieldExplainEntity statusExplainEntity = new RedundantFieldExplainEntity();
			statusExplainEntity.setAttr("paymentStatus");
			statusExplainEntity.setAttrName("回款状态");
			statusExplainEntity.setEnable(1);
			statusExplainEntity.setRequired(0);
			statusExplainEntity.setIsRedundant(0);
			errorExplainList.add(amountExplainEntity);
			errorExplainList.add(estimateTimeExplainEntity);
			errorExplainList.add(payTimeExplainEntity);
			errorExplainList.add(statusExplainEntity);
			break;
		case 4: //RedundantTemplateTypeEnum.CONTACT.getCode()
			
			break;
		case 5: //RedundantTemplateTypeEnum.PRODUCT.getCode()
			RedundantFieldExplainEntity depNameExplainEntity = new RedundantFieldExplainEntity();
			depNameExplainEntity.setAttr("depName");
			depNameExplainEntity.setAttrName("部门");
			depNameExplainEntity.setEnable(1);
			depNameExplainEntity.setRequired(0);
			depNameExplainEntity.setIsRedundant(0);
			errorExplainList.add(depNameExplainEntity);
			break;
		default:
			break;
		}
		return errorExplainList;
	}
}
