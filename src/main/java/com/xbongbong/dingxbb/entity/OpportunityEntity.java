 
package com.xbongbong.dingxbb.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.swing.plaf.synth.Region;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xbongbong.dingxbb.controller.api.ContactController;
import com.xbongbong.dingxbb.model.ContactModel;
import com.xbongbong.dingxbb.model.CustomerModel;
import com.xbongbong.util.DateUtil;
import com.xbongbong.util.ReflectHelper;
import com.xbongbong.util.StringUtil;

 
public class OpportunityEntity implements Serializable,ImportEntity{

	private static final Logger LOG = LogManager.getLogger(OpportunityEntity.class);
	
	private static final long serialVersionUID = 5320259035785801175L;

	//========== properties ==========
	
	//主键
	private Integer id;
	//模板ID
	private Integer templateId;
	//公司ID
	private String corpid;
	//员工ID
	private String userId;
	//员工名称
	private String userName;
	//客户ID
	private Integer customerId;
	//联系人ID
	private Integer contactId;
	//联系人名称
	private String contactName;
	//销售机会名称
	private String name;
	//销售阶段ID
	private Integer salesStageId;
	//销售阶段名
	private String salesStage;
	//收取的其他费用
	private Double otherCharge;
	//改销售机会享受的折扣
	private Double discount;
	//客户名称
	private String customerName;
	//预计金额
	private Double estimateAmount;
	//预计结束时间
	private Integer estimateEndTime;
	//预计盈率
	private Double estimateWinRate;
	//重要程度，1--5之间的数值
	private Integer importantDegree;
	//是否归档，1是，2否
	private Integer isArchived;
	//决策人编号
	private Integer decisionMakerId;
	//决策人
	private String decisionMaker;
	//竞争对手
	private String competitor;
	//创建时间
	private Integer addTime;
	//更新时间
	private Integer updateTime;
	//删除标记
	private Integer del;
	
	//币种
	private Integer currency;
	//单位
	private String unit;
	//汇率
	private String exchangeRate;
	
	private String currencyStr;
	//扩展字段
	//客户联系方式
	private JSONArray cuPhoneJsonArray = new JSONArray();
	//产品列表
	private List<OpportunityProductEntity> opportunityProductList;
	private String products;
	//机会数
	private Integer count;
	//冗余字段
	private RedundantFieldEntity fieldEntity = new RedundantFieldEntity();
	//跟进的员工列表
	private List<OpportunityUserEntity> opportunityUserEntityList;
	//预计金额的前台显示处理(1000000-->1,000,000.00)
	private String showEstimateAmount;
	//跟进的员工名字
	private String opportunityUserName;
	
	/****************** 导入特殊值处理用到 *******************/
	private String product;
	private List<SalesStageEntity> salesStageList;
	private transient CustomerModel customerModel;
	private transient ContactModel contactModel;
	private UserEntity userEntity;
	/****************** 导入特殊值处理用到 *******************/

	private Integer dayLeft = 0 ;//剩余几天
	//预计金额
	private String estimateAmountStr;
	//是否关注  1关注 0未关注
	private Integer isFocus = 0;

	public OpportunityEntity(){
		this.contactId = 0;
		this.contactName = "";
		this.otherCharge = 0d;
		this.discount = 100d;
		this.estimateAmount = 0d;
		this.estimateEndTime = 0;
		this.estimateWinRate = 0d;
		this.importantDegree = 0;
		this.decisionMakerId = 0;
		this.decisionMaker = "";
		this.competitor = "";
		this.del = 0;
		
		this.currency = 0;
		this.unit = "元";
		this.exchangeRate = "1";
	}
	
    //========== getters and setters ==========

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCurrency() {
		return currency;
	}

	public void setCurrency(Integer currency) {
		this.currency = currency;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(String exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public Integer getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}

	public String getCorpid() {
		return corpid;
	}

	public void setCorpid(String corpid) {
		this.corpid = corpid;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSalesStageId() {
		return salesStageId;
	}

	public void setSalesStageId(Integer salesStageId) {
		this.salesStageId = salesStageId;
	}

	public String getSalesStage() {
		return salesStage;
	}

	public void setSalesStage(String salesStage) {
		this.salesStage = salesStage;
	}

	public Double getOtherCharge() {
		return otherCharge;
	}

	public void setOtherCharge(Double otherCharge) {
		this.otherCharge = otherCharge;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Integer getContactId() {
		return contactId;
	}

	public void setContactId(Integer contactId) {
		this.contactId = contactId;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public Double getEstimateAmount() {
		return estimateAmount;
	}

	public void setEstimateAmount(Double estimateAmount) {
		this.estimateAmount = estimateAmount;
	}

	public Integer getEstimateEndTime() {
		return estimateEndTime;
	}

	public void setEstimateEndTime(Integer estimateEndTime) {
		this.estimateEndTime = estimateEndTime;
	}

	public Double getEstimateWinRate() {
		return estimateWinRate;
	}

	public void setEstimateWinRate(Double estimateWinRate) {
		this.estimateWinRate = estimateWinRate;
	}

	public Integer getImportantDegree() {
		return importantDegree;
	}

	public void setImportantDegree(Integer importantDegree) {
		this.importantDegree = importantDegree;
	}

	public Integer getIsArchived() {
		return isArchived;
	}

	public void setIsArchived(Integer isArchived) {
		this.isArchived = isArchived;
	}

	public Integer getDecisionMakerId() {
		return decisionMakerId;
	}

	public void setDecisionMakerId(Integer decisionMakerId) {
		this.decisionMakerId = decisionMakerId;
	}

	public String getDecisionMaker() {
		return decisionMaker;
	}

	public void setDecisionMaker(String decisionMaker) {
		this.decisionMaker = decisionMaker;
	}

	public String getCompetitor() {
		return competitor;
	}

	public void setCompetitor(String competitor) {
		this.competitor = competitor;
	}

	public Integer getAddTime() {
		return addTime;
	}

	public void setAddTime(Integer addTime) {
		this.addTime = addTime;
	}

	public Integer getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Integer updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getDel() {
		return del;
	}

	public void setDel(Integer del) {
		this.del = del;
	}

	public List<OpportunityProductEntity> getOpportunityProductList() {
		return opportunityProductList;
	}

	public void setOpportunityProductList(
			List<OpportunityProductEntity> opportunityProductList) {
		this.opportunityProductList = opportunityProductList;
	}

	public String getProducts() {
		return products;
	}

	public void setProducts(String products) {
		this.products = products;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public RedundantFieldEntity getFieldEntity() {
		return fieldEntity;
	}

	public void setFieldEntity(RedundantFieldEntity fieldEntity) {
		this.fieldEntity = fieldEntity;
	}
    
	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public List<SalesStageEntity> getSalesStageList() {
		return salesStageList;
	}

	public void setSalesStageList(List<SalesStageEntity> salesStageList) {
		this.salesStageList = salesStageList;
	}

	public void setCustomerModel(CustomerModel customerModel) {
		this.customerModel = customerModel;
	}

	public void setContactModel(ContactModel contactModel) {
		this.contactModel = contactModel;
	}

	public UserEntity getUserEntity() {
		return userEntity;
	}

	public void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
	}

	public JSONArray getCuPhoneJsonArray() {
		return cuPhoneJsonArray;
	}

	public void setCuPhoneJsonArray(JSONArray cuPhoneJsonArray) {
		this.cuPhoneJsonArray = cuPhoneJsonArray;
	}

	public Integer getImportantDegreeInt(String importantDegreeStr){
		Integer importantDegreeInt = 0;
		switch (importantDegreeStr) {
		case "一星":
			importantDegreeInt = 1;
			break;
		case "二星":
			importantDegreeInt = 2;
			break;
		case "三星":
			importantDegreeInt = 3;
			break;
		case "四星":
			importantDegreeInt = 4;
			break;
		case "五星":
			importantDegreeInt = 5;
			break;
		default:
			break;
		}
		return importantDegreeInt;
	}
	
	public String getImportantDegreeStr(Integer importantDegreeInt){
		String importantDegreeStr = "";
		switch (importantDegreeInt) {
		case 1:
			importantDegreeStr = "一星";
			break;
		case 2:
			importantDegreeStr = "二星";
			break;
		case 3:
			importantDegreeStr = "三星";
			break;
		case 4:
			importantDegreeStr = "四星";
			break;
		case 5:
			importantDegreeStr = "五星";
			break;
		default:
			break;
		}
		return importantDegreeStr;
	}
	
	
	public String getCurrencyStr() {
		return currencyStr;
	}

	public void setCurrencyStr(String currencyStr) {
		this.currencyStr = currencyStr;
	}

	public Integer getDayLeft() {
		return dayLeft;
	}

	public void setDayLeft(Integer dayLeft) {
		this.dayLeft = dayLeft;
	}

	public String getEstimateAmountStr() {
		return estimateAmountStr;
	}

	public void setEstimateAmountStr(String estimateAmountStr) {
		this.estimateAmountStr = estimateAmountStr;
	}

	public Integer getIsFocus() {
		return isFocus;
	}

	public void setIsFocus(Integer isFocus) {
		this.isFocus = isFocus;
	}

	public String[] getImportantDegreeArr() {
		return importantDegreeArr;
	}

	public void setImportantDegreeArr(String[] importantDegreeArr) {
		this.importantDegreeArr = importantDegreeArr;
	}

	public String[] getIsArchiveArr() {
		return isArchiveArr;
	}

	public void setIsArchiveArr(String[] isArchiveArr) {
		this.isArchiveArr = isArchiveArr;
	}

	public CustomerModel getCustomerModel() {
		return customerModel;
	}

	public ContactModel getContactModel() {
		return contactModel;
	}

	/**
	* 重载toString方法
	* @return
	*
	* @see java.lang.Object#toString()
	*/
	@Override
    public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
	

	public String importantDegreeArr[] = {"0星","一星","二星","三星","四星","五星"};
	public String isArchiveArr[] = {"","已归档","未归档"};

	public List<OpportunityUserEntity> getOpportunityUserEntityList() {
		return opportunityUserEntityList;
	}

	public void setOpportunityUserEntityList(List<OpportunityUserEntity> opportunityUserEntityList) {
		this.opportunityUserEntityList = opportunityUserEntityList;
	}

	public String getShowEstimateAmount() {
		return showEstimateAmount;
	}

	public void setShowEstimateAmount(String showEstimateAmount) {
		this.showEstimateAmount = showEstimateAmount;
	}

	
	public String getOpportunityUserName() {
		return opportunityUserName;
	}

	public void setOpportunityUserName(String opportunityUserName) {
		this.opportunityUserName = opportunityUserName;
	}

	/**
	 * 特殊字段处理，用于excel导入
	 * @param value
	 * @param attr
	 * @return  0：不是特殊字段，1：操作成功，2：值无法匹配到
	 */
	public Integer setValue(String value, String attr) {
		Integer code = 0;
		switch (attr) {
		case "salesStageId":
			for(SalesStageEntity salesStage : salesStageList){
				if(value.equals(salesStage.getStageName())){ //匹配到时，赋值、code置1
					this.salesStageId = salesStage.getCode();
					this.salesStage = salesStage.getStageName();
					code = 1;
					break ;
				}
			}
			if(!code.equals(1))
				code = 2;
			break;
		case "customerName":
		/*	Integer customerId = 0;
			List<CustomerEntity> customerList = customerModel.getByCustomerName(value, corpid);
			if(customerList.size() == 0){
				code = 2;
				break;
			}
			customerId = customerList.get(0).getId();
			this.customerId = customerId;
			this.customerName = value;*/
			code = 1;
			break;
		case "customerPhone":
			
			/*	if(customerNameIdMap == null || customerNameIdMap.size() == 0){
					code = 1;
					break;
				}
				
				String customerName = value;
				if(!StringUtil.isEmpty(customerName)){
					if(!customerNameIdMap.containsKey(customerName)){
						code = 2;
						break ;
					}else{
						this.customerId = customerNameIdMap.get(customerName);
						this.customerName = customerName;
					}
				}else{
					this.customerId = 0;
					this.customerName = "";
				}
				this.redundantValueObject.put(attr, value);	*/
				code = 1;
			break;	
		case "contactName":
			if(!StringUtil.isEmpty(value)){
				Integer contactId = 0;
				List<ContactEntity> contactList = this.contactModel.getByContactName(value, this.corpid, this.customerId);
				if(contactList.size() == 0){
					code = 2;
					break;
				}
				contactId = contactList.get(0).getId();
				this.contactId = contactId;
				this.contactName = value;
			}else{
				this.contactId = 0;
				this.contactName = "";
			}
			code = 1;
			break;
		case "estimateEndTime":
			try {
				Integer estimateEndTime = 0;
				try{
					estimateEndTime = Integer.valueOf(value);
				} catch(Exception e){
					LOG.warn("OpportunityEntity setValue estimateEndTime 类型转换错误", e);
					estimateEndTime = 0;
				}
				this.estimateEndTime = estimateEndTime;
			} catch (Exception e) {
				code = 2;
				break ;
			}
			code = 1;
			break;
		case "importantDegree":
			try {
				Integer importantDegree = getImportantDegreeInt(value);
				this.importantDegree = importantDegree;
			} catch (Exception e) {
				code = 2;
				break ;
			}
			code = 1;
			break;
		case "estimateAmount":
			try {
				Double estimateAmount = StringUtil.toDouble(value,0.0);
				this.estimateAmount = estimateAmount;
			} catch (Exception e) {
				code = 2;
				break ;
			}
			code = 1;
			break;
		case "discount":
			try {
				Double discount = StringUtil.toDouble(value,100d);
				this.discount = discount;
			} catch (Exception e) {
				code = 2;
				break ;
			}
			code = 1;
			break;
		case "otherCharge":
			try {
				Double otherCharge = StringUtil.toDouble(value,0d);
				this.otherCharge = otherCharge;
			} catch (Exception e) {
				code = 2;
				break ;
			}
			code = 1;
			break;
		default:
			//可以直接赋值的字段,其中产品比较特别，赋值后，核心分配逻辑在保存完销售机会之后
			ReflectHelper.valueSet(this, attr, value);
			break;
		}
		return code;
	}
	/**
	 * 特殊字段处理，用于excel导入,在实体保存成功后建立关系
	 */
	public void setValue(){
		
	}

	@Override
	public void setEmpty() {
		// TODO Auto-generated method stub
		
	}
	
}

