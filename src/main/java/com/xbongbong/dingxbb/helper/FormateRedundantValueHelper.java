package com.xbongbong.dingxbb.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xbongbong.dingxbb.entity.ContactEntity;
import com.xbongbong.dingxbb.entity.ContractEntity;
import com.xbongbong.dingxbb.entity.CustomerEntity;
import com.xbongbong.dingxbb.entity.ProductEntity;
import com.xbongbong.dingxbb.entity.RedundantFieldEntity;
import com.xbongbong.dingxbb.entity.RedundantFieldExplainEntity;
import com.xbongbong.dingxbb.enums.RedundantTemplateTypeEnum;
import com.xbongbong.util.DateUtil;
import com.xbongbong.util.ReflectHelper;

/**
 * 用于格式化各种自定义模板键值
 * @author kaka -xbongbong.com
 * @time 2016年7月22日 下午5:13:24
 */
public class FormateRedundantValueHelper {

	/**
	 * 构造自定义客户相关数据
	 * @param explains
	 * @param customer
	 * @param fieldEntity
	 * @return
	 * @author kaka
	 * @time 2016年7月22日 下午5:20:46
	 */
	public static List<RedundantFieldExplainEntity> formateCustomerValue(List<RedundantFieldExplainEntity> explains,
			CustomerEntity customer, RedundantFieldEntity fieldEntity) {
		
		List<RedundantFieldExplainEntity> retExplains = new ArrayList<RedundantFieldExplainEntity>();

		//是否需要放入explain中,默认true
		boolean shouldputToRetExplains = true;
		for(RedundantFieldExplainEntity explain : explains) {
			if(!explain.getEnable().equals(1)) {
				continue;
			}
			shouldputToRetExplains = true;
			
			if(explain.getIsRedundant().equals(0)) {
				
				if(explain.getAttr().equals("name")) {
					/*--------------------------------------------------客户名称-----------------------------------------------------*/
					explain.setVaule4Show(customer.getName());
				} else if(explain.getAttr().equals("nameShort")) {
					/*-----------------------------------------------------客户简称--------------------------------------------------*/
					explain.setVaule4Show(customer.getNameShort());
				} else if(explain.getAttr().equals("phone")) {
					/*--------------------------------------------------客户电话-----------------------------------------------------*/
					//[{'name':'座机号码','telNum':'0571-12345678'},{'name':'电话号码','telNum':'1832154786'}]
					String phoneStr = customer.getPhone();
					
					explain.setVaule4Show(phoneStr);
					//不用详细解析，留给客户处理，不然可能会误导客户
//					JSONArray phoneArray = null;
//					try {
//						phoneArray = JSON.parseArray(phoneStr);
//					} catch(Exception e) {
//						phoneArray = new JSONArray();
//					}
//					
//					if(phoneArray == null) {
//						phoneArray = new JSONArray();
//					}
//					
//					if(phoneArray.size() == 0) {
//						explain.setVaule4Show("");
//					} else {
//						//设为不加explain,人去加，有多少个号码就加多少条
//						shouldputToRetExplains = false;
//						for(int i = 0; i < phoneArray.size(); i++) {
//							JSONObject phoneTmp = phoneArray.getJSONObject(i);
//							String name = phoneTmp.getString("name");
//							String telNum = phoneTmp.getString("telNum");
//							
//							RedundantFieldExplainEntity explainTmp = new RedundantFieldExplainEntity();
//							explainTmp.setAttrName(name);
//							explainTmp.setVaule4Show(telNum);
//							explainTmp.setEnable(1);
//							explainTmp.setFieldType(0);
//							explainTmp.setIsRedundant(0);
//							
//							retExplains.add(explainTmp);
//						}
//					}
					
				} else if(explain.getAttr().equals("type")) {
					/*--------------------------------------------------客户状态-----------------------------------------------------*/
					
					explain.setVaule4Show(customer.getTypeStr());
				} else if(explain.getAttr().equals("isIndividual")) {
					/*--------------------------------------------------客户性质-----------------------------------------------------*/
					
					explain.setVaule4Show(customer.getIsIndividualStr());
				} else if(explain.getAttr().equals("scale")) {
					/*--------------------------------------------------客户分级-----------------------------------------------------*/
					explain.setVaule4Show(customer.getScaleStr());
				} else if(explain.getAttr().equals("industry")) {
					/*--------------------------------------------------客户行业-----------------------------------------------------*/
					
					explain.setVaule4Show(customer.getIndustryStr());
				} else if(explain.getAttr().equals("importantDegree")) {
					/*--------------------------------------------------重要程度-----------------------------------------------------*/
					customer.setImportantDegreeStr(customer.getImportantDegree());
					explain.setVaule4Show(customer.getImportantDegreeStr());
				} else if(explain.getAttr().equals("address")) {
					/*--------------------------------------------------客户地址-----------------------------------------------------*/
					shouldputToRetExplains = false;
					
					RedundantFieldExplainEntity explainProvince = new RedundantFieldExplainEntity();
					explainProvince.setAttrName("省");
					explainProvince.setVaule4Show(customer.getProvince());
					explainProvince.setEnable(1);
					explainProvince.setFieldType(0);
					explainProvince.setIsRedundant(0);
					
					retExplains.add(explainProvince);
					
					RedundantFieldExplainEntity explainCity = new RedundantFieldExplainEntity();
					explainCity.setAttrName("市");
					explainCity.setVaule4Show(customer.getCity());
					explainCity.setEnable(1);
					explainCity.setFieldType(0);
					explainCity.setIsRedundant(0);
					
					retExplains.add(explainCity);
					
					RedundantFieldExplainEntity explainDitrict = new RedundantFieldExplainEntity();
					explainDitrict.setAttrName("区/县");
					explainDitrict.setVaule4Show(customer.getDistrict());
					explainDitrict.setEnable(1);
					explainDitrict.setFieldType(0);
					explainDitrict.setIsRedundant(0);
					
					retExplains.add(explainDitrict);
					
					RedundantFieldExplainEntity explainAddress = new RedundantFieldExplainEntity();
					explainAddress.setAttrName("地址");
					explainAddress.setVaule4Show(customer.getAddress());
					explainAddress.setEnable(1);
					explainAddress.setFieldType(0);
					explainAddress.setIsRedundant(0);
					
					retExplains.add(explainAddress);
					
				} else if(explain.getAttr().equals("website")) {
					/*--------------------------------------------------客户官网-----------------------------------------------------*/
					
					explain.setVaule4Show(customer.getWebsite());
				} else if(explain.getAttr().equals("instruction")) {
					/*--------------------------------------------------客户简介-----------------------------------------------------*/
					
					explain.setVaule4Show(customer.getInstruction());
				} else if(explain.getAttr().equals("genre")) {
					/*--------------------------------------------------客户简介-----------------------------------------------------*/
					
					explain.setVaule4Show(customer.getGenreStr());
				} else if(explain.getAttr().equals("source")) {
					/*--------------------------------------------------客户简介-----------------------------------------------------*/
					
					explain.setVaule4Show(customer.getSource());
				} else {
					//其他没设值，目前先丢弃
					shouldputToRetExplains = false;
				}
				
			} else {
				explain.setVaule4Show((String)ReflectHelper.valueGet(fieldEntity, explain.getAttr()));
			}
			
			//将explian置入retExplains中
			if(shouldputToRetExplains) {
				retExplains.add(explain);
			}
		}
		
		return retExplains;
	}
	
	/**
	 * 构造自定义合同相关数据
	 * @param explains
	 * @param contract
	 * @param fieldEntity
	 * @return
	 * @author kaka
	 * @time 2016年7月22日 下午5:20:46
	 */
	public static List<RedundantFieldExplainEntity> formateContractValue(List<RedundantFieldExplainEntity> explains,
			ContractEntity contract, RedundantFieldEntity fieldEntity) {
		
		List<RedundantFieldExplainEntity> retExplains = new ArrayList<RedundantFieldExplainEntity>();

		//是否需要放入explain中,默认true
		boolean shouldputToRetExplains = true;
		for(RedundantFieldExplainEntity explain : explains) {
			if(!explain.getEnable().equals(1)) {
				continue;
			}
			shouldputToRetExplains = true;
			
			if(explain.getIsRedundant().equals(0)) {
				
				if(explain.getAttr().equals("name")) {
					/*--------------------------------------------------合同名称-----------------------------------------------------*/
					explain.setVaule4Show(contract.getName());
				} else if(explain.getAttr().equals("contractNo")) {
					/*-----------------------------------------------------合同编号--------------------------------------------------*/
					explain.setVaule4Show(contract.getContractNo());
				} else if(explain.getAttr().equals("opportunityId")) {
					/*--------------------------------------------------关联机会-----------------------------------------------------*/
					//目前先丢弃
					shouldputToRetExplains = false;
					
				} else if(explain.getAttr().equals("customerName")) {
					/*--------------------------------------------------关联客户-----------------------------------------------------*/
					
					explain.setVaule4Show(contract.getCustomerName());
				} else if(explain.getAttr().equals("partnerName")) {
					/*--------------------------------------------------客户性质-----------------------------------------------------*/
					
					explain.setVaule4Show(contract.getPartnerName());
				} else if(explain.getAttr().equals("product")) {
					/*--------------------------------------------------关联产品-----------------------------------------------------*/
					//目前先丢弃，其他地方会处理
					shouldputToRetExplains = false;
				} else if(explain.getAttr().equals("amount")) {
					/*--------------------------------------------------合同金额-----------------------------------------------------*/
					
					explain.setVaule4Show(contract.getAmount().toString());
				} else if(explain.getAttr().equals("signTime")) {
					/*--------------------------------------------------签订时间-----------------------------------------------------*/
					
					explain.setVaule4Show(DateUtil.getString(contract.getSignTime()));
				} else if(explain.getAttr().equals("status")) {
					/*--------------------------------------------------合同状态-----------------------------------------------------*/
					explain.setVaule4Show(contract.getStatusStr());
					
				} else if(explain.getAttr().equals("type")) {
					/*--------------------------------------------------合同类型-----------------------------------------------------*/
					
					explain.setVaule4Show(contract.getTypeStr());
				} else if(explain.getAttr().equals("signPerson")) {
					/*--------------------------------------------------签订人-----------------------------------------------------*/
					
					explain.setVaule4Show(contract.getSignPerson());
					//签订人userId
					RedundantFieldExplainEntity explainSignUserId = new RedundantFieldExplainEntity();
					explainSignUserId.setAttrName("签订人ID");
					explainSignUserId.setVaule4Show(contract.getSignUserId());
					explainSignUserId.setEnable(1);
					explainSignUserId.setFieldType(0);
					explainSignUserId.setIsRedundant(0);
					
					retExplains.add(explainSignUserId);
				} else if(explain.getAttr().equals("payMethod")) {
					/*--------------------------------------------------客户简介-----------------------------------------------------*/
					
					explain.setVaule4Show(contract.getPayMethodStr(contract.getPayMethod()));
				} else if(explain.getAttr().equals("deadline")) {
					/*--------------------------------------------------客户简介-----------------------------------------------------*/
					if(contract.getDeadline() != null) {
						explain.setVaule4Show(DateUtil.getDateString(contract.getDeadline()));
					} else {
						explain.setVaule4Show("");
					}
					
				} else {
					//其他没设值，目前先丢弃
					shouldputToRetExplains = false;
				}
				
			} else {
				explain.setVaule4Show((String)ReflectHelper.valueGet(fieldEntity, explain.getAttr()));
			}
			
			//将explian置入retExplains中
			if(shouldputToRetExplains) {
				retExplains.add(explain);
			}
		}
		
		return retExplains;
	}
	
	
	/**
	 * 构造自定义联系人相关数据
	 * @param explains
	 * @param customer
	 * @param fieldEntity
	 * @return
	 * @author kaka
	 * @time 2016年7月22日 下午5:20:46
	 */
	public static List<RedundantFieldExplainEntity> formateContactValue(List<RedundantFieldExplainEntity> explains,
			ContactEntity contact, RedundantFieldEntity fieldEntity) {
		
		List<RedundantFieldExplainEntity> retExplains = new ArrayList<RedundantFieldExplainEntity>();

		//是否需要放入explain中,默认true
		boolean shouldputToRetExplains = true;
		for(RedundantFieldExplainEntity explain : explains) {
			if(!explain.getEnable().equals(1)) {
				continue;
			}
			shouldputToRetExplains = true;
			
			if(explain.getIsRedundant().equals(0)) {
				
				if(explain.getAttr().equals("name")) {
					/*--------------------------------------------------联系人名称-----------------------------------------------------*/
					explain.setVaule4Show(contact.getName());
				} else if(explain.getAttr().equals("customerName")) { //TODO 是否需要显示关联客户名称
					/*--------------------------------------------------联系人部门-----------------------------------------------------*/
					
					explain.setVaule4Show(contact.getCustomerName());
				} else if(explain.getAttr().equals("phone")) {
					/*--------------------------------------------------联系人电话-----------------------------------------------------*/
					//[{'name':'座机号码','telNum':'0571-12345678'},{'name':'电话号码','telNum':'1832154786'}]
					String phoneStr = contact.getPhone();
					explain.setVaule4Show(phoneStr);
					//不用详细解析，留给接口调用者处理，不然可能会误导接口调用者
//					JSONArray phoneArray = null;
//					try {
//						phoneArray = JSON.parseArray(phoneStr);
//					} catch(Exception e) {
//						phoneArray = new JSONArray();
//					}
//					
//					if(phoneArray == null) {
//						phoneArray = new JSONArray();
//					}
//					
//					if(phoneArray.size() == 0) {
//						explain.setVaule4Show("");
//					} else {
//						//设为不加explain,人去加，有多少个号码就加多少条
//						shouldputToRetExplains = false;
//						for(int i = 0; i < phoneArray.size(); i++) {
//							JSONObject phoneTmp = phoneArray.getJSONObject(i);
//							String name = phoneTmp.getString("name");
//							String telNum = phoneTmp.getString("telNum");
//							
//							RedundantFieldExplainEntity explainTmp = new RedundantFieldExplainEntity();
//							explainTmp.setAttrName(name);
//							explainTmp.setVaule4Show(telNum);
//							explainTmp.setEnable(1);
//							explainTmp.setFieldType(0);
//							explainTmp.setIsRedundant(0);
//							
//							retExplains.add(explainTmp);
//						}
//					}
					
				} else if(explain.getAttr().equals("department")) {
					/*--------------------------------------------------联系人部门-----------------------------------------------------*/
					
					explain.setVaule4Show(contact.getDepartment());
				} else if(explain.getAttr().equals("position")) {
					/*--------------------------------------------------联系人职位-----------------------------------------------------*/
					
					explain.setVaule4Show(contact.getPosition());
				} else if(explain.getAttr().equals("level")) {
					/*--------------------------------------------------联系人级别-----------------------------------------------------*/
					explain.setVaule4Show(contact.getLevelStr());
				} else if(explain.getAttr().equals("importantDegree")) {
					/*--------------------------------------------------重要程度-----------------------------------------------------*/
					contact.setImportantDegreeStr(contact.getImportantDegree());
					explain.setVaule4Show(contact.getImportantDegreeStr());
				} else if(explain.getAttr().equals("address")) {
					/*--------------------------------------------------联系人地址-----------------------------------------------------*/
					shouldputToRetExplains = false;
					
					RedundantFieldExplainEntity explainProvince = new RedundantFieldExplainEntity();
					explainProvince.setAttrName("省");
					explainProvince.setVaule4Show(contact.getProvince());
					explainProvince.setEnable(1);
					explainProvince.setFieldType(0);
					explainProvince.setIsRedundant(0);
					
					retExplains.add(explainProvince);
					
					RedundantFieldExplainEntity explainCity = new RedundantFieldExplainEntity();
					explainCity.setAttrName("市");
					explainCity.setVaule4Show(contact.getCity());
					explainCity.setEnable(1);
					explainCity.setFieldType(0);
					explainCity.setIsRedundant(0);
					
					retExplains.add(explainCity);
					
					RedundantFieldExplainEntity explainDitrict = new RedundantFieldExplainEntity();
					explainDitrict.setAttrName("区/县");
					explainDitrict.setVaule4Show(contact.getDistrict());
					explainDitrict.setEnable(1);
					explainDitrict.setFieldType(0);
					explainDitrict.setIsRedundant(0);
					
					retExplains.add(explainDitrict);
					
					RedundantFieldExplainEntity explainAddress = new RedundantFieldExplainEntity();
					explainAddress.setAttrName("地址");
					explainAddress.setVaule4Show(contact.getAddress());
					explainAddress.setEnable(1);
					explainAddress.setFieldType(0);
					explainAddress.setIsRedundant(0);
					
					retExplains.add(explainAddress);
					
				} else if(explain.getAttr().equals("email")) {
					/*--------------------------------------------------联系人email-----------------------------------------------------*/
					
					explain.setVaule4Show(contact.getEmail());
				} else if(explain.getAttr().equals("qq")) {
					/*--------------------------------------------------联系人QQ-----------------------------------------------------*/
					
					explain.setVaule4Show(contact.getQq());
				} else if(explain.getAttr().equals("zipCode")) {
					/*--------------------------------------------------联系人邮编-----------------------------------------------------*/
					
					explain.setVaule4Show(contact.getZipCode());
				} else if(explain.getAttr().equals("sex")) {
					/*--------------------------------------------------联系人性别-----------------------------------------------------*/
					contact.setSexStr(contact.getSex());
					explain.setVaule4Show(contact.getSexStr());
				} else if(explain.getAttr().equals("birthday")) {
					/*--------------------------------------------------联系人生日-----------------------------------------------------*/
					explain.setVaule4Show(DateUtil.getDateString(contact.getBirthday()));
				} else if(explain.getAttr().equals("hobbies")) {
					/*--------------------------------------------------联系人爱好-----------------------------------------------------*/
					
					explain.setVaule4Show(contact.getHobbies());
				} else if(explain.getAttr().equals("relationship")) {
					/*--------------------------------------------------决策关系-----------------------------------------------------*/
					
					explain.setVaule4Show(contact.getRelationship());
				} else if(explain.getAttr().equals("intimateDegree")) {
					/*--------------------------------------------------新密度-----------------------------------------------------*/
					contact.setIntimateDegreeStr(contact.getIntimateDegree());
					explain.setVaule4Show(contact.getImportantDegreeStr());
				} else if(explain.getAttr().equals("memo")) {
					/*--------------------------------------------------备注-----------------------------------------------------*/
					
					explain.setVaule4Show(contact.getMemo());
				} else {
					//其他没设值，目前先丢弃
					shouldputToRetExplains = false;
				}
				
			} else {
				explain.setVaule4Show((String)ReflectHelper.valueGet(fieldEntity, explain.getAttr()));
			}
			
			//将explian置入retExplains中
			if(shouldputToRetExplains) {
				retExplains.add(explain);
			}
		}
		
		return retExplains;
	}
	
	/**
	 * 构造自定义产品相关数据
	 * @param explains
	 * @param customer
	 * @param fieldEntity
	 * @return
	 * @author kaka
	 * @time 2016年7月22日 下午5:20:46
	 */
	public static List<RedundantFieldExplainEntity> formateProductValue(List<RedundantFieldExplainEntity> explains,
			ProductEntity product, RedundantFieldEntity fieldEntity) {
		
		List<RedundantFieldExplainEntity> retExplains = new ArrayList<RedundantFieldExplainEntity>();

		//是否需要放入explain中,默认true
		boolean shouldputToRetExplains = true;
		for(RedundantFieldExplainEntity explain : explains) {
			if(!explain.getEnable().equals(1)) {
				continue;
			}
			shouldputToRetExplains = true;
			
			if(explain.getIsRedundant().equals(0)) {
				
				if(explain.getAttr().equals("name")) {
					/*--------------------------------------------------产品名称-----------------------------------------------------*/
					explain.setVaule4Show(product.getName());
				} else if(explain.getAttr().equals("productNo")) { 
					/*--------------------------------------------------产品编号-----------------------------------------------------*/
					
					explain.setVaule4Show(product.getProductNo());
				} else if(explain.getAttr().equals("productImgs")) {
					/*--------------------------------------------------产品图片-----------------------------------------------------*/
					
					explain.setVaule4Show(product.getProductImgs());
				} else if(explain.getAttr().equals("specification")) {
					/*--------------------------------------------------产品型号-----------------------------------------------------*/
					
					explain.setVaule4Show(product.getSpecification());
				} else if(explain.getAttr().equals("stock")) {
					/*--------------------------------------------------产品库存-----------------------------------------------------*/
					
					explain.setVaule4Show(product.getStock().toString());
				} else if(explain.getAttr().equals("price")) {
					/*--------------------------------------------------产品价格-----------------------------------------------------*/
					explain.setVaule4Show(product.getPrice().toString());
				} else if(explain.getAttr().equals("cost")) {
					/*--------------------------------------------------产品成本-----------------------------------------------------*/
					explain.setVaule4Show(product.getCost().toString());
				} else if(explain.getAttr().equals("instruction")) {
					/*--------------------------------------------------产品简介-----------------------------------------------------*/
					explain.setVaule4Show(product.getInstruction());
				} else if(explain.getAttr().equals("unit")) {
					/*--------------------------------------------------产品单位-----------------------------------------------------*/
					
					explain.setVaule4Show(product.getUnit());
				} else {
					//其他没设值，目前先丢弃
					shouldputToRetExplains = false;
				}
				
			} else {
				explain.setVaule4Show((String)ReflectHelper.valueGet(fieldEntity, explain.getAttr()));
			}
			
			//将explian置入retExplains中
			if(shouldputToRetExplains) {
				retExplains.add(explain);
			}
		}
		
		return retExplains;
	}
	
	/**
	 * 
	 * @param refType
	 * @param explains
	 * @param fieldEntity
	 * @param object
	 * @return
	 * @author kaka
	 * @time 2016年9月27日 下午7:14:17
	 */
	public static JSONObject formateRedundantValue(Integer refType, List<RedundantFieldExplainEntity> explains, RedundantFieldEntity field, Object object) {
		List<RedundantFieldExplainEntity> keyValueExplains;
		RedundantFieldEntity fieldEntity = null;
		JSONObject entity = null;
		if(refType == RedundantTemplateTypeEnum.CUSTOMER.getCode()) {
			//客户
			CustomerEntity customer = (CustomerEntity) object;
			if(field != null) {
				fieldEntity = field;
			}
			
			keyValueExplains = formateCustomerValue(explains, customer, fieldEntity);
			entity = getJSONObjectKeyValue(keyValueExplains);
			//id信息
			entity.put("id", customer.getId());
			//创建人和创建人ID
			entity.put("创建人", customer.getUserName());
			entity.put("创建人ID", customer.getUserId());
			if((customer.getLatitude() < -0.01 || customer.getLatitude() > 0.01) || (customer.getLongitude() < -0.01 || customer.getLongitude() > 0.01)) {
				entity.put("经度", customer.getLongitude());
				entity.put("纬度", customer.getLatitude());
			} else {
				entity.put("经度", "");
				entity.put("纬度", "");
			}
			
		} if(refType == RedundantTemplateTypeEnum.SALES_OPPORTUNITY.getCode()) {
			//TODO 销售机会
		} if(refType == RedundantTemplateTypeEnum.CONTRACT.getCode()) {
			//合同
			ContractEntity contract = (ContractEntity) object;
			if(field != null) {
				fieldEntity = field;
			}
			
			keyValueExplains = formateContractValue(explains, contract, fieldEntity);
			entity = getJSONObjectKeyValue(keyValueExplains);
			//id信息
			entity.put("id", contract.getId());
		} if(refType == RedundantTemplateTypeEnum.CONTACT.getCode()) {
			//联系人
			ContactEntity contact = (ContactEntity) object;
			if(field != null) {
				fieldEntity = field;
			}
			
			keyValueExplains = formateContactValue(explains, contact, fieldEntity);
			entity = getJSONObjectKeyValue(keyValueExplains);
			//id信息
			entity.put("id", contact.getId());
		} if(refType == RedundantTemplateTypeEnum.PRODUCT.getCode()) {
			//产品
			ProductEntity product = (ProductEntity) object;
			if(field != null) {
				fieldEntity = field;
			}
			
			keyValueExplains = formateProductValue(explains, product, fieldEntity);
			entity = getJSONObjectKeyValue(keyValueExplains);
			//id信息
			entity.put("id", product.getId());
		}
		return entity;

	}
	
	/**
	 * 获取设好值的JSONArray
	 * @param explains
	 * @param fieldEntityList
	 * @param objectLists
	 * @return
	 * @author kaka
	 * @time 2016年9月25日 下午2:10:37
	 */
	public static JSONArray formateRedundantValues(Integer refType, List<RedundantFieldExplainEntity> explains, List<RedundantFieldEntity> fieldEntityList, List<?> objectList) {
		JSONArray retJSONArray = new JSONArray();
		
		//fieldEntityList --> fieldEntityMap
		Map<Integer, RedundantFieldEntity> fieldEntityMap = new HashMap<Integer, RedundantFieldEntity>();
		if(fieldEntityList != null && fieldEntityList.size() > 0){
			for(RedundantFieldEntity field : fieldEntityList) {
				fieldEntityMap.put(field.getRefId(), field);
			}
		}
		
		for(int i = 0; i < objectList.size(); i++) {
			List<RedundantFieldExplainEntity> keyValueExplains;
			RedundantFieldEntity fieldEntity = null;
			if(refType == RedundantTemplateTypeEnum.CUSTOMER.getCode()) {
				//客户
				CustomerEntity customer = (CustomerEntity) objectList.get(i);
				fieldEntity = fieldEntityMap.get(customer.getId());
				
				keyValueExplains = formateCustomerValue(explains, customer, fieldEntity);
				JSONObject entity = getJSONObjectKeyValue(keyValueExplains);
				//id信息
				entity.put("id", customer.getId());
				//创建人和创建人ID
				entity.put("创建人", customer.getUserName());
				entity.put("创建人ID", customer.getUserId());
				if((customer.getLatitude() < -0.01 || customer.getLatitude() > 0.01) || (customer.getLongitude() < -0.01 || customer.getLongitude() > 0.01)) {
					entity.put("经度", customer.getLongitude());
					entity.put("纬度", customer.getLatitude());
				} else {
					entity.put("经度", "");
					entity.put("纬度", "");
				}
				entity.put("更新时间", customer.getUpdateTime());
				
				retJSONArray.add(entity);
			} if(refType == RedundantTemplateTypeEnum.SALES_OPPORTUNITY.getCode()) {
				//销售机会
				
			} if(refType == RedundantTemplateTypeEnum.CONTRACT.getCode()) {
				//合同
				ContractEntity contract = (ContractEntity) objectList.get(i);
				fieldEntity = fieldEntityMap.get(contract.getId());
				
				keyValueExplains = formateContractValue(explains, contract, fieldEntity);
				JSONObject entity = getJSONObjectKeyValue(keyValueExplains);
				//id信息
				entity.put("id", contract.getId());
				//创建人和创建人ID
				entity.put("创建人", contract.getUserName());
				entity.put("创建人ID", contract.getUserId());
				
				retJSONArray.add(entity);
			} if(refType == RedundantTemplateTypeEnum.CONTACT.getCode()) {
				//联系人
				ContactEntity contact = (ContactEntity) objectList.get(i);
				fieldEntity = fieldEntityMap.get(contact.getId());
				
				keyValueExplains = formateContactValue(explains, contact, fieldEntity);
				JSONObject entity = getJSONObjectKeyValue(keyValueExplains);
				//id信息
				entity.put("id", contact.getId());
				
				retJSONArray.add(entity);
			} if(refType == RedundantTemplateTypeEnum.PRODUCT.getCode()) {
				//产品
				ProductEntity product = (ProductEntity) objectList.get(i);
				fieldEntity = fieldEntityMap.get(product.getId());
				
				keyValueExplains = formateProductValue(explains, product, fieldEntity);
				JSONObject entity = getJSONObjectKeyValue(keyValueExplains);
				//id信息
				entity.put("id", product.getId());
				
				retJSONArray.add(entity);
			}
		}
		
		return retJSONArray;
	}
	
	public static JSONObject getJSONObjectKeyValue(List<RedundantFieldExplainEntity> explains) {
		JSONObject retJson = new JSONObject();
		
		for(RedundantFieldExplainEntity explain : explains) {
			String key = explain.getAttrName();
			String value = explain.getVaule4Show();
			
			retJson.put(key, value);
		}
		return retJson;
	}
}
