 
package com.xbongbong.dingxbb.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xbongbong.dingxbb.dao.RedundantFieldExplainDao;
import com.xbongbong.dingxbb.entity.RedundantFieldExplainEntity;
import com.xbongbong.dingxbb.enums.ContactEnum;
import com.xbongbong.dingxbb.enums.ContractEnum;
import com.xbongbong.dingxbb.enums.CustomerEnum;
import com.xbongbong.dingxbb.enums.ProductEnum;
import com.xbongbong.dingxbb.enums.SalesOpportunityEnum;
import com.xbongbong.util.CommentUtil;

 

@Component
public class RedundantFieldExplainModel  implements IModel{

	@Autowired
	private RedundantFieldExplainDao dao;
	@Autowired
	private CompanyConfigModel companyConfigModel;
	
	public Integer insert(Object entity){
		return dao.insert((RedundantFieldExplainEntity)entity);
	}

	public Integer update(Object entity){
		return dao.update((RedundantFieldExplainEntity)entity);
	}
	
	public Integer save(RedundantFieldExplainEntity entity){
		if(entity.getId() == null || entity.getId().equals(0)) {
			return dao.insert((RedundantFieldExplainEntity)entity);
		}
		return dao.update((RedundantFieldExplainEntity)entity);
	}
	 
	public List<RedundantFieldExplainEntity> findEntitys(Map<String ,Object>  param){
		return dao.findEntitys(param);
	}
	public Integer getEntitysCount(Map<String ,Object>  param){
		return dao.getEntitysCount(param);
	}

	public RedundantFieldExplainEntity getByKey(String key, String corpid) {
		return dao.getByKey(key, corpid);
	}

	public Integer deleteByKey(String key, String corpid) {
		return dao.deleteByKey(key, corpid);
	}
	
	public Integer deleteByTemplateId(String corpid, Integer templateId) {
		return dao.deleteByTemplateId(corpid, templateId);
	}
	
	/**
	 * 获取某个模板已启用的扩展字段list
	 * @param companyId
	 * @param templateId
	 * @return
	 */
	public List<RedundantFieldExplainEntity> findEnableExplainList(String corpid, Integer templateId) {

		//获取启用的扩展字段
		Map<String, Object> exportMap = new HashMap<String, Object>();
		CommentUtil.addToMap(exportMap,"corpid", corpid);
		CommentUtil.addToMap(exportMap,"templateId",templateId);
		CommentUtil.addToMap(exportMap,"enable",1);
		CommentUtil.addToMap(exportMap,"del",0);
		CommentUtil.addToMap(exportMap,"isRedundant",1);
		CommentUtil.addToMap(exportMap,"orderByStr", "sort desc");
		List<RedundantFieldExplainEntity> explainList = findEntitys(exportMap);
		
		return explainList;
	}
	
	/**
	 * 使用explainList获取一个map（中文名：解释）
	 * @param explainList
	 * @param plusStar 是否需要根据required补上*
	 * @return
	 */
	public Map<String,RedundantFieldExplainEntity> getExplainMap(List<RedundantFieldExplainEntity> explainList, boolean plusStar) {
		Map<String,RedundantFieldExplainEntity> retMap = new HashMap<String,RedundantFieldExplainEntity>();
		if(explainList == null || explainList.size() <= 0){
			return retMap;
		}
		for(RedundantFieldExplainEntity entity : explainList){
			//允许图片类型参数提交
			if (entity.getFieldType() != null /*&& !entity.getFieldType().equals(6)*/ && !entity.getFieldType().equals(8)) {//图片，文件不导入
				
				String key = entity.getAttrName();
				if(plusStar && entity.getRequired().equals(1)){
					key += "*";
				}
				
				retMap.put(key, entity);
			}
		}
		return retMap;
	}
	
	/**
	 * 获取客户解释(根据客户判重规则)
	 * @param explainEntity
	 * @return
	 */
	private List<RedundantFieldExplainEntity> getCustomerExplainList(
			RedundantFieldExplainEntity explainEntity) {
		
		List<RedundantFieldExplainEntity> customerExplainList = new ArrayList<RedundantFieldExplainEntity>();
		
//		RedundantFieldExplainEntity customerNameExplainEntity = new RedundantFieldExplainEntity();
		RedundantFieldExplainEntity customerPhoneExplainEntity = new RedundantFieldExplainEntity();
		
		boolean nameCheck=companyConfigModel.getCustomerNameRule(explainEntity.getCorpid());//客户名称判重是否开启
		boolean phoneCheck=companyConfigModel.getCustomerPhoneRule(explainEntity.getCorpid());//客户电话判重是否开启
		
		customerExplainList.add(explainEntity);
		
		if(!nameCheck && phoneCheck){//没有客户名判重有客户号码判重时，要填写客户号码
			customerPhoneExplainEntity.setAttr("customerPhone");
			customerPhoneExplainEntity.setAttrName("客户号码");
			customerPhoneExplainEntity.setRequired(explainEntity.getRequired());
			customerPhoneExplainEntity.setSort(explainEntity.getSort());
			customerExplainList.add(customerPhoneExplainEntity);
		}
		
		return customerExplainList;
	}
	
	/**
	 * 获取解释列表，address转化成省、市、区、详细地址的解释及其他特殊字段处理,用于excel
	 * @param type
	 * @param templateId
	 * @param corpid
	 * @return
	 */
	public List<RedundantFieldExplainEntity> getExplainList(String type, Integer templateId,String corpid){
		List<RedundantFieldExplainEntity> explainList = new ArrayList<RedundantFieldExplainEntity>();
		
		if("salesOpportunity".equals(type) || "opportunity".equals(type)){
			type = "salesOpportunity";
		}
		
		if(templateId > 0){	//有模板ID，从数据库获取解释列表
			Map<String, Object> exportMap = new HashMap<String, Object>();
			CommentUtil.addToMap(exportMap,"corpid", corpid);
			CommentUtil.addToMap(exportMap,"enable", 1);
			CommentUtil.addToMap(exportMap,"templateId",templateId);
			CommentUtil.addToMap(exportMap,"orderByStr","sort desc");
			CommentUtil.addToMap(exportMap,"del",0);
			List<RedundantFieldExplainEntity> templateExplainList = findEntitys(exportMap);
			for(RedundantFieldExplainEntity explainEntity : templateExplainList){
				if(explainEntity.getAttr().equals("address")){
					List<RedundantFieldExplainEntity> addressExplainList = getAddressExplainList(explainEntity);
					explainList.addAll(addressExplainList);
				}else if(explainEntity.getAttr().equals("product") && "contract".equals(type)){
					List<RedundantFieldExplainEntity> productExplainList = getProductExplainList(explainEntity);
					explainList.addAll(productExplainList);
				}else if(explainEntity.getAttr().equals("price") && "product".equals(type)){
					List<RedundantFieldExplainEntity> productPriceExplainList = getProductPriceExplainList(explainEntity);
					explainList.addAll(productPriceExplainList);
				}else if(explainEntity.getAttr().equals("cost") && "product".equals(type)){
					List<RedundantFieldExplainEntity> productCostExplainList = getProductCostExplainList(explainEntity);
					explainList.addAll(productCostExplainList);
				}else if(explainEntity.getAttr().equals("productImgs") && "product".equals(type)){
					//产品图片不导入
				}else if(explainEntity.getAttr().equals("opportunityId") && "contract".equals(type)){
					//合同关联机会不导入
				}else if(explainEntity.getAttr().equals("customerName") 
						&& ("contact".equals(type) || "salesOpportunity".equals(type) || "contract".equals(type))){
					//客户名称通过id关联
//					List<RedundantFieldExplainEntity> customerExplainList = getCustomerExplainList(explainEntity);
//					explainList.addAll(customerExplainList);
				}else{
					explainList.add(explainEntity);
				}
			}
		}else{	//没有模板ID，拿默认解释列表
			explainList = getDefaultExplainList(type, corpid);
		}
		return explainList;
	}

	/**
	 * 通过类型对应的枚举生成一个解释列表
	 * @param type		customer/contact/opportunity/contract
	 * @return
	 */
	private List<RedundantFieldExplainEntity> getDefaultExplainList(String type,String corpid) {
		List<RedundantFieldExplainEntity> defaultExplainList = new ArrayList<RedundantFieldExplainEntity>();
		switch (type) {
		case "customer":
			List<CustomerEnum> customerEnumList = CustomerEnum.getAllEnumSorted();
			for(CustomerEnum customerEnum : customerEnumList){
				RedundantFieldExplainEntity redundantFieldExplainEntity = new RedundantFieldExplainEntity();
				redundantFieldExplainEntity.setAttr(customerEnum.getAlias());
				redundantFieldExplainEntity.setAttrName(customerEnum.getName());
				redundantFieldExplainEntity.setRequired(customerEnum.getRequired());
				redundantFieldExplainEntity.setSort(customerEnum.getSort());
				redundantFieldExplainEntity.setFieldType(0);
				
				if(customerEnum.getAlias().equals("parent")){
					//默认不使用上级客户
				}else if(customerEnum.getAlias().equals("address")){
					//地址字段特殊解释
					List<RedundantFieldExplainEntity> addressExplainList = getAddressExplainList(redundantFieldExplainEntity);
					defaultExplainList.addAll(addressExplainList);
				}else{
					defaultExplainList.add(redundantFieldExplainEntity);
				}
			}
			break;
		case "contact":
			List<ContactEnum> contactEnumList = ContactEnum.getAllEnumSorted();
			for(ContactEnum contactEnum : contactEnumList){
				RedundantFieldExplainEntity redundantFieldExplainEntity = new RedundantFieldExplainEntity();
				redundantFieldExplainEntity.setAttr(contactEnum.getAlias());
				redundantFieldExplainEntity.setAttrName(contactEnum.getName());
				redundantFieldExplainEntity.setRequired(contactEnum.getRequired());
				redundantFieldExplainEntity.setSort(contactEnum.getSort());
				redundantFieldExplainEntity.setCorpid(corpid);
				redundantFieldExplainEntity.setFieldType(0);
				
				//地址字段特殊解释
				if(contactEnum.getAlias().equals("address")){
					List<RedundantFieldExplainEntity> addressExplainList = getAddressExplainList(redundantFieldExplainEntity);
					defaultExplainList.addAll(addressExplainList);
				}else if(contactEnum.getAlias().equals("customerName")){
					//客户名称通过id关联
//					List<RedundantFieldExplainEntity> customerExplainList = getCustomerExplainList(redundantFieldExplainEntity);
//					defaultExplainList.addAll(customerExplainList);
				}else{
					defaultExplainList.add(redundantFieldExplainEntity);
				}
			}
			break;
		case "salesOpportunity":
			List<SalesOpportunityEnum> opportunityEnumList = SalesOpportunityEnum.getAllEnumSorted();
			for(SalesOpportunityEnum salesOpportunityEnum : opportunityEnumList){
				RedundantFieldExplainEntity redundantFieldExplainEntity = new RedundantFieldExplainEntity();
				redundantFieldExplainEntity.setAttr(salesOpportunityEnum.getAlias());
				redundantFieldExplainEntity.setAttrName(salesOpportunityEnum.getName());
				redundantFieldExplainEntity.setRequired(salesOpportunityEnum.getRequired());
				redundantFieldExplainEntity.setSort(salesOpportunityEnum.getSort());
				redundantFieldExplainEntity.setFieldType(0);
				
				if(salesOpportunityEnum.getAlias().equals("contactName")){
					//默认不使用客户联系人
				}else if(salesOpportunityEnum.getAlias().equals("customerName")){
					//客户名称通过id关联
//					List<RedundantFieldExplainEntity> customerExplainList = getCustomerExplainList(redundantFieldExplainEntity);
//					defaultExplainList.addAll(customerExplainList);
				}else{
					defaultExplainList.add(redundantFieldExplainEntity);
				}
				
			}
			break;
		case "contract":
			List<ContractEnum> contractEnumList = ContractEnum.getAllEnumSorted();
			for(ContractEnum contractEnum : contractEnumList){
				RedundantFieldExplainEntity redundantFieldExplainEntity = new RedundantFieldExplainEntity();
				redundantFieldExplainEntity.setAttr(contractEnum.getAlias());
				redundantFieldExplainEntity.setAttrName(contractEnum.getName());
				redundantFieldExplainEntity.setRequired(contractEnum.getRequired());
				redundantFieldExplainEntity.setSort(contractEnum.getSort());
				redundantFieldExplainEntity.setFieldType(0);
				//产品字段特殊解释
				if(contractEnum.getAlias().equals("product")){
					List<RedundantFieldExplainEntity> productExplainList = getProductExplainList(redundantFieldExplainEntity);
					defaultExplainList.addAll(productExplainList);
				}else if(contractEnum.getAlias().equals("opportunityId")){
					//默认不使用关联机会
				}else if(contractEnum.getAlias().equals("contactName")){
					//默认不使用客户联系人
				}else if(contractEnum.getAlias().equals("customerName")){
					//客户名称通过id关联
//					List<RedundantFieldExplainEntity> customerExplainList = getCustomerExplainList(redundantFieldExplainEntity);
//					defaultExplainList.addAll(customerExplainList);
				}else{
					defaultExplainList.add(redundantFieldExplainEntity);
				}
			}
			break;
		case "product":
			List<ProductEnum> productEnumList = ProductEnum.getAllEnumSorted();
			for(ProductEnum productEnum : productEnumList){
				RedundantFieldExplainEntity redundantFieldExplainEntity = new RedundantFieldExplainEntity();
				redundantFieldExplainEntity.setAttr(productEnum.getAlias());
				redundantFieldExplainEntity.setAttrName(productEnum.getName());
				redundantFieldExplainEntity.setRequired(productEnum.getRequired());
				redundantFieldExplainEntity.setSort(productEnum.getSort());
				redundantFieldExplainEntity.setFieldType(0);
				//产品字段特殊解释
				if(productEnum.getAlias().equals("price")){
					List<RedundantFieldExplainEntity> priceExplainList = getProductPriceExplainList(redundantFieldExplainEntity);
					defaultExplainList.addAll(priceExplainList);
				}else if(productEnum.getAlias().equals("cost")){
					List<RedundantFieldExplainEntity> costExplainList = getProductCostExplainList(redundantFieldExplainEntity);
					defaultExplainList.addAll(costExplainList);
				}else if(productEnum.getAlias().equals("productImgs")){
					
				}else{
					defaultExplainList.add(redundantFieldExplainEntity);
				}
			}
			break;
		default:
			break;
		}
		return defaultExplainList;
	}

	/**
	 * 地址解释特殊处理 address转化成省、市、区、详细地址的解释
	 * @param explainEntity
	 * @return
	 */
	private List<RedundantFieldExplainEntity> getAddressExplainList(RedundantFieldExplainEntity explainEntity){
		List<RedundantFieldExplainEntity> addressExplainList = new ArrayList<RedundantFieldExplainEntity>();
		if(explainEntity != null){
			RedundantFieldExplainEntity provinceExplainEntity = new RedundantFieldExplainEntity();
			provinceExplainEntity.setAttr("province");
			provinceExplainEntity.setAttrName("省");
			provinceExplainEntity.setRequired(explainEntity.getRequired());
			provinceExplainEntity.setSort(explainEntity.getSort());
			provinceExplainEntity.setFieldType(0);
			addressExplainList.add(provinceExplainEntity);
			RedundantFieldExplainEntity cityExplainEntity = new RedundantFieldExplainEntity();
			cityExplainEntity.setAttr("city");
			cityExplainEntity.setAttrName("市");
			cityExplainEntity.setRequired(explainEntity.getRequired());
			cityExplainEntity.setSort(explainEntity.getSort());
			cityExplainEntity.setFieldType(0);
			addressExplainList.add(cityExplainEntity);
			RedundantFieldExplainEntity districtExplainEntity = new RedundantFieldExplainEntity();
			districtExplainEntity.setAttr("district");
			districtExplainEntity.setAttrName("区/县");
//			districtExplainEntity.setRequired(explainEntity.getRequired());
			districtExplainEntity.setRequired(0);//区县可以不填 TODO kaka
			districtExplainEntity.setSort(explainEntity.getSort());
			districtExplainEntity.setFieldType(0);
			addressExplainList.add(districtExplainEntity);
			RedundantFieldExplainEntity addressExplainEntity = new RedundantFieldExplainEntity();
			addressExplainEntity.setAttr("address");
			addressExplainEntity.setAttrName("详细地址");
			addressExplainEntity.setRequired(explainEntity.getRequired());
			addressExplainEntity.setSort(explainEntity.getSort());
			addressExplainEntity.setFieldType(0);
			addressExplainList.add(addressExplainEntity);
		}
		return addressExplainList;
	}
	/**
	 * 产品解释特殊处理 product转化成产品编号、产品价格、产品数量、折扣%、其他金额 
	 * @param explainEntity
	 * @return
	 */
	private List<RedundantFieldExplainEntity> getProductExplainList(RedundantFieldExplainEntity explainEntity){
		List<RedundantFieldExplainEntity> addressExplainList = new ArrayList<RedundantFieldExplainEntity>();
		if(explainEntity != null){
			RedundantFieldExplainEntity productExplainEntity = new RedundantFieldExplainEntity();
			productExplainEntity.setAttr("product");
			productExplainEntity.setAttrName("关联产品");
			productExplainEntity.setRequired(explainEntity.getRequired());
			productExplainEntity.setSort(explainEntity.getSort());
			productExplainEntity.setFieldType(0);
			addressExplainList.add(productExplainEntity);
			RedundantFieldExplainEntity discountExplainEntity = new RedundantFieldExplainEntity();
			discountExplainEntity.setAttr("discount");
			discountExplainEntity.setAttrName("折扣%");
			discountExplainEntity.setRequired(explainEntity.getRequired());
			discountExplainEntity.setSort(explainEntity.getSort());
			discountExplainEntity.setFieldType(0);
			addressExplainList.add(discountExplainEntity);
			RedundantFieldExplainEntity otherChargeExplainEntity = new RedundantFieldExplainEntity();
			otherChargeExplainEntity.setAttr("otherCharge");
			otherChargeExplainEntity.setAttrName("其他金额");
			otherChargeExplainEntity.setRequired(explainEntity.getRequired());
			otherChargeExplainEntity.setSort(explainEntity.getSort());
			otherChargeExplainEntity.setFieldType(0);
			addressExplainList.add(otherChargeExplainEntity);
		}
		return addressExplainList;
	}
	/**
	 * 产品价格特殊处理 价格转化成价格、价格是否显示
	 * @param explainEntity
	 * @return
	 * @author chy
	 * @time 2016年7月18日 下午6:43:58
	 */
	private List<RedundantFieldExplainEntity> getProductPriceExplainList(RedundantFieldExplainEntity explainEntity){
		List<RedundantFieldExplainEntity> productPriceExplainList = new ArrayList<RedundantFieldExplainEntity>();
		if(explainEntity != null){
			RedundantFieldExplainEntity priceExplainEntity = new RedundantFieldExplainEntity();
			priceExplainEntity.setAttr("price");
			priceExplainEntity.setAttrName(explainEntity.getAttrName());
			priceExplainEntity.setRequired(explainEntity.getRequired());
			priceExplainEntity.setIsRedundant(0);
			priceExplainEntity.setSort(explainEntity.getSort());
			priceExplainEntity.setFieldType(0);
			productPriceExplainList.add(priceExplainEntity);
			
			RedundantFieldExplainEntity priceShowExplainEntity = new RedundantFieldExplainEntity();
			priceShowExplainEntity.setAttr("showPrice");
			priceShowExplainEntity.setAttrName("是否显示"+explainEntity.getAttrName());
			priceShowExplainEntity.setRequired(0);//此处设置成非必填，模板导入时默认为不显示
			priceShowExplainEntity.setIsRedundant(0);
			priceShowExplainEntity.setSort(explainEntity.getSort());
			priceShowExplainEntity.setFieldType(0);
			productPriceExplainList.add(priceShowExplainEntity);
		}
		return productPriceExplainList;
	}
	/**
	 * 产品成本特殊处理 价格转化成价格、价格是否显示
	 * @param explainEntity
	 * @return
	 * @author chy
	 * @time 2016年7月18日 下午6:51:29
	 */
	private List<RedundantFieldExplainEntity> getProductCostExplainList(RedundantFieldExplainEntity explainEntity){
		List<RedundantFieldExplainEntity> productPriceCostExplainList = new ArrayList<RedundantFieldExplainEntity>();
		if(explainEntity != null){
			RedundantFieldExplainEntity costExplainEntity = new RedundantFieldExplainEntity();
			costExplainEntity.setAttr("cost");
			costExplainEntity.setAttrName(explainEntity.getAttrName());
			costExplainEntity.setRequired(explainEntity.getRequired());
			costExplainEntity.setIsRedundant(0);
			costExplainEntity.setSort(explainEntity.getSort());
			costExplainEntity.setFieldType(0);
			productPriceCostExplainList.add(costExplainEntity);
			
			RedundantFieldExplainEntity costShowExplainEntity = new RedundantFieldExplainEntity();
			costShowExplainEntity.setAttr("showCost");
			costShowExplainEntity.setAttrName("是否显示"+explainEntity.getAttrName());
			costShowExplainEntity.setRequired(0);//此处设置成非必填，模板导入时默认为不显示
			costShowExplainEntity.setIsRedundant(0);
			costShowExplainEntity.setSort(explainEntity.getSort());
			costShowExplainEntity.setFieldType(0);
			productPriceCostExplainList.add(costShowExplainEntity);
		}
		return productPriceCostExplainList;
	}
}

 