 
package com.xbongbong.dingxbb.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.alibaba.fastjson.JSONArray;
import com.xbongbong.dingxbb.model.ContactModel;
import com.xbongbong.dingxbb.model.CustomerModel;
import com.xbongbong.dingxbb.model.UserModel;
import com.xbongbong.util.DateUtil;
import com.xbongbong.util.ReflectHelper;
import com.xbongbong.util.StringUtil;

 
public class ContractEntity implements Serializable,ImportEntity{

	/** Comment for <code>serialVersionUID</code> */
	private static final long serialVersionUID = -1L;

	//========== properties ==========
	
	//主键
	private Integer id;
	//模板ID
	private Integer templateId;
	//公司ID
	private String corpid;
	//创建合同的员工ID【合同所属员工】
	private String userId;
	//员工名称
	private String userName;
	//客户ID
	private Integer customerId;
	//客户名称
	private String customerName;
	//联系人ID
	private Integer contactId;
	//联系人名称
	private String contactName;
	//合同名称
	private String name;
	//合同金额
	private Double amount;
	//收取的其他费用
	private Double otherCharge;
	//该合同所享受的折扣
	private Double discount;
	//合同编号
	private String contractNo;
	//省
	private String province;
	//市
	private String city;
	//
	private String district;
	//国家
	private String country;
	//客户地址
	private String address;
	//已收回款金额
	private Double receivedPayment;
	//合同类型，1直销合同，2代理合同，3服务合同，4快销合同
	private Integer type;
	//合同签订时间，默认是当天，可以修改
	private Integer signTime;
	//签订合同的员工id
	private String signUserId;
	//签订合同的员工,默认是合同创建人
	private String signPerson;
	//合作客户ID
	private Integer partnerId;
	//合作客户名称
	private String partnerName;
	//是否用于合同统计：0：不用于合同统计（status = 1,5）；1:用于合同统计（status = 2,3,4）
	private Integer ifStatistic;
	//合同状态，1预签约，2签约，3执行中，4完毕,5终止
	private Integer status;
	//付款方式，1网银转账，2现金，3支票，4电汇，5承兑汇票
	private Integer payMethod;
	//销售机会ID
	private Integer opportunityId;
	//创建时间
	private Integer addTime;
	//更新时间
	private Integer updateTime;
	//删除标记
	private Integer del;
	//到期时间
	private Integer deadline;

	//币种
	private Integer currency;
	//单位
	private String unit;
	//汇率
	private String exchangeRate;
	
	//是否归档，0未归档，1归档
	private Integer archived;


	//责任人/销售团队 逗号间隔
	private String contractUserName;
	//主负责人userId/销售团队userId列表，逗号间隔
	private String isMainUserId;

		//客户联系方式
		private JSONArray cuPhoneJsonArray = new JSONArray();
		//扩展字段，付款方式
		private String payMethodStr;
		//关联产品
		private String products;
		//错误提示(用于excel导入)
		private String errorMsg;
		
		//排名
		private Integer rank;
		//部门
		private String department;
		//已收款额
		private Double receiveAmount;
		//应收未收款额
		private Double noReceiveAmount;
		//合同金额的显示
		private String amountStr;
		
		private Double paymentAmount;
		//合同类型名称
		private String typeStr;
		//合同状态名称
		private String statusStr;
		//冗余字段
		private RedundantFieldEntity fieldEntity = new RedundantFieldEntity();
		//合同商品列表
		private List<ContractProductEntity> contractProductList;
		//合同跟进人列表
		private List<ContractUserEntity> contractUserEntityList;
		//合同回款列表
		private List<PaymentEntity> paymentEntityList;
		
		//关联机会名称
		private String opportunityName = "";
				
		/************************导入特殊值处理用到******************************/
		private String product;
		private Map<Integer, DataDictionaryEntity> statusMap;
		private Map<Integer, DataDictionaryEntity> typeMap;
		private transient CustomerModel customerModel;
		private transient ContactModel contactModel;
		private transient UserModel userModel;
		private UserEntity userEntity;
		private UserEntity signPersonEntity;
		private List<RedundantFieldExplainEntity> errorExplainList;
		private Map<String,String> excelMap;
		/************************导入特殊值处理用到******************************/

	public ContractEntity(){
		Integer nowTime = DateUtil.getInt();
		this.templateId = 0;
		this.corpid = "";
		this.userId = "";
		this.userName = "";
		this.customerId = 0;
		this.customerName = "";
		this.contactId = 0;
		this.contactName = "";
		this.name = "";
		this.amount = 0D;
		this.otherCharge = 0D;
		this.discount = 100D;
		this.contractNo = "";
		this.province = "";
		this.city = "";
		this.district = "";
		this.country = "";
		this.address = "";
		this.receivedPayment = 0D;
		this.type = 0;
		this.signUserId = "";
		this.signPerson = "";
		this.signTime = nowTime;
		this.ifStatistic = 0;
		this.status = 1;
		this.payMethod = 1;
		this.opportunityId = 0;
		this.addTime = nowTime;
		this.updateTime = nowTime;
		this.del = 0;
		this.currency = 0;
		this.unit = "元";
		this.exchangeRate = "1";
		this.archived = 0;
	}
	
    //========== getters and setters ==========
		
	public Integer getId() {
		return id;
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

	public void setId(Integer id) {
		this.id = id;
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

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public Double getAmount() {
		return amount;
	}
	
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getContractNo() {
		return contractNo;
	}
	
	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}
	public String getProvince() {
		return province;
	}
	
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	public String getDistrict() {
		return district;
	}
	
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getCountry() {
		return country;
	}
	
	public void setCountry(String country) {
		this.country = country;
	}
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	public Double getReceivedPayment() {
		return receivedPayment;
	}
	
	public void setReceivedPayment(Double receivedPayment) {
		this.receivedPayment = receivedPayment;
	}
	public Integer getType() {
		return type;
	}
	
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getSignTime() {
		return signTime;
	}
	
	public void setSignTime(Integer signTime) {
		this.signTime = signTime;
	}
	
	public String getSignUserId() {
		return signUserId;
	}

	public void setSignUserId(String signUserId) {
		this.signUserId = signUserId;
	}

	public String getSignPerson() {
		return signPerson;
	}
	
	public void setSignPerson(String signPerson) {
		this.signPerson = signPerson;
	}
	
	public Integer getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Integer partnerId) {
		this.partnerId = partnerId;
	}

	public String getPartnerName() {
		return partnerName;
	}

	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}

	public Integer getStatus() {
		return status;
	}
	
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getPayMethod() {
		return payMethod;
	}
	
	public void setPayMethod(Integer payMethod) {
		this.payMethod = payMethod;
	}
	public Integer getOpportunityId() {
		return opportunityId;
	}

	public void setOpportunityId(Integer opportunityId) {
		this.opportunityId = opportunityId;
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

	public Integer getDeadline() {
		return deadline;
	}

	public void setDeadline(Integer deadline) {
		this.deadline = deadline;
	}

	public String getContractUserName() {
		return contractUserName;
	}

	public void setContractUserName(String contractUserName) {
		this.contractUserName = contractUserName;
	}
	
	

	public Integer getArchived() {
		return archived;
	}

	public void setArchived(Integer archived) {
		this.archived = archived;
	}

	public String getIsMainUserId() {
		return isMainUserId;
	}

	public void setIsMainUserId(String isMainUserId) {
		this.isMainUserId = isMainUserId;
	}

	public Integer getIfStatistic() {
		return ifStatistic;
	}

	public void setIfStatistic(Integer ifStatistic) {
		this.ifStatistic = ifStatistic;
	}
	
	public Integer getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}
	

	public String getPayMethodStr() {
		return payMethodStr;
	}

	public void setPayMethodStr(String payMethodStr) {
		this.payMethodStr = payMethodStr;
	}

	public JSONArray getCuPhoneJsonArray() {
		return cuPhoneJsonArray;
	}

	public void setCuPhoneJsonArray(JSONArray cuPhoneJsonArray) {
		this.cuPhoneJsonArray = cuPhoneJsonArray;
	}

	public String getProducts() {
		return products;
	}

	public void setProducts(String products) {
		this.products = products;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Double getOtherCharge() {
		return otherCharge;
	}

	public void setOtherCharge(Double otherCharge) {
		this.otherCharge = otherCharge;
	}
	
	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}
	

	public Double getReceiveAmount() {
		return receiveAmount;
	}

	public void setReceiveAmount(Double receiveAmount) {
		this.receiveAmount = receiveAmount;
	}

	public Double getNoReceiveAmount() {
		return noReceiveAmount;
	}

	public void setNoReceiveAmount(Double noReceiveAmount) {
		this.noReceiveAmount = noReceiveAmount;
	}
	
	

	public Double getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(Double paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	
	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
	public String getTypeStr() {
		return typeStr;
	}

	public void setTypeStr(String typeStr) {
		this.typeStr = typeStr;
	}
	
	public String getStatusStr() {
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
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

	/**
	 * @param statusMap the statusMap to set
	 */
	public void setStatusMap(Map<Integer, DataDictionaryEntity> statusMap) {
		this.statusMap = statusMap;
	}

	/**
	 * @param typeMap the typeMap to set
	 */
	public void setTypeMap(Map<Integer, DataDictionaryEntity> typeMap) {
		this.typeMap = typeMap;
	}

	public void setCustomerModel(CustomerModel customerModel) {
		this.customerModel = customerModel;
	}

	public void setUserModel(UserModel userModel) {
		this.userModel = userModel;
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

	public Integer getPayMethodInt(String payMethodStr){
		Integer payMethodInt = 0;
		switch (payMethodStr) {
		case "网银转账":
			payMethodInt = 1;
			break;
		case "现金":
			payMethodInt = 2;
			break;
		case "支票":
			payMethodInt = 3;
			break;
		case "电汇":
			payMethodInt = 4;
			break;
		case "承兑汇票":
			payMethodInt = 5;
			break;
		default:
			break;
		}
		return payMethodInt;
	}
	
	public String getPayMethodStr(Integer payMethodInt){
		String payMethodStr = "";
		switch (payMethodInt) {
		case 1:
			payMethodStr = "网银转账";
			break;
		case 2:
			payMethodStr = "现金";
			break;
		case 3:
			payMethodStr = "支票";
			break;
		case 4:
			payMethodStr = "电汇";
			break;
		case 5:
			payMethodStr = "承兑汇票";
			break;
		default:
			break;
		}
		return payMethodStr;
	}
	
	public String getOpportunityName() {
		return opportunityName;
	}

	public void setOpportunityName(String opportunityName) {
		this.opportunityName = opportunityName;
	}

	public UserEntity getSignPersonEntity() {
		return signPersonEntity;
	}

	public void setSignPersonEntity(UserEntity signPersonEntity) {
		this.signPersonEntity = signPersonEntity;
	}
	
	public List<RedundantFieldExplainEntity> getErrorExplainList() {
		return errorExplainList;
	}

	public void setErrorExplainList(
			List<RedundantFieldExplainEntity> errorExplainList) {
		this.errorExplainList = errorExplainList;
	}

	public Map<String, String> getExcelMap() {
		return excelMap;
	}

	public void setExcelMap(Map<String, String> excelMap) {
		this.excelMap = excelMap;
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

	public List<ContractProductEntity> getContractProductList() {
		return contractProductList;
	}

	public void setContractProductList(List<ContractProductEntity> contractProductList) {
		this.contractProductList = contractProductList;
	}

	public List<ContractUserEntity> getContractUserEntityList() {
		return contractUserEntityList;
	}

	public void setContractUserEntityList(List<ContractUserEntity> contractUserEntityList) {
		this.contractUserEntityList = contractUserEntityList;
	}

	public List<PaymentEntity> getPaymentEntityList() {
		return paymentEntityList;
	}

	public void setPaymentEntityList(List<PaymentEntity> paymentEntityList) {
		this.paymentEntityList = paymentEntityList;
	}

	public String getAmountStr() {
		return amountStr;
	}

	public void setAmountStr(String amountStr) {
		this.amountStr = amountStr;
	}

	/**
	 * 实体赋值处理，用于excel导入
	 * @param value
	 * @param attr
	 * @return  0：不是特殊字段，1：操作成功，2：值无法匹配到
	 */
	public Integer setValue(String value, String attr) {
		Integer code = 0;
		
		switch (attr) {
		case "customerName":
		/*	Integer customerId = 0;
			List<CustomerEntity> customerList = this.customerModel.getByCustomerName(value, this.corpid);
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
		/*	Integer customerId = 0;
			List<CustomerEntity> customerList = this.customerModel.getByCustomerName(value, this.corpid);
			if(customerList.size() == 0){
				code = 2;
				break;
			}
			customerId = customerList.get(0).getId();
			this.customerId = customerId;
			this.customerName = value;*/
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
		case "amount":
			String amountStr = value;
			
			Double amount = StringUtil.toDouble(amountStr, 0.0);
			this.amount = amount;
			code = 1;
			break;
		case "payMethod":
			Integer payMethod = getPayMethodInt(value);
			this.payMethod = payMethod;
			code = 1;
			break;
		case "signTime":
			Integer signTime = 0;
			try {
				signTime = Integer.valueOf(value);
			} catch (Exception e) {
				signTime = 0;
			}
			this.signTime = signTime;
			code = 1;
			break;
		case "status":
			for(Map.Entry<Integer, DataDictionaryEntity> entry : this.statusMap.entrySet()){
				DataDictionaryEntity dataDictionary = entry.getValue();
				if(value.equals(dataDictionary.getName())){ //匹配到时，赋值、code置1
					this.status = dataDictionary.getCode();
					code = 1;
					break ;
				}
			}
			if(!code.equals(1))
				code = 2;
			break;
		case "type":
			if(!StringUtil.isEmpty(value))
				for(Map.Entry<Integer, DataDictionaryEntity> entry : this.typeMap.entrySet()){
					DataDictionaryEntity dataDictionary = entry.getValue();
					if(value.equals(dataDictionary.getName())){ //匹配到时，赋值、code置1
						this.type = dataDictionary.getCode();
						code = 1;
						break ;
					}
				}
			else{
				this.type = 0;
				code = 1;
			}
			if(!code.equals(1))
				code = 2;
			break;
		case "signPerson":
//			String signPerson = value;
//			if(!StringUtil.isEmpty(value)){
//				UserEntity user = this.userModel.getByName(signPerson, corpid);
//				if(user == null){//签订人不存在
//					this.signUserId = "0";
//					this.signPerson = signPerson;//记录签订人姓名
//				}else{
//					this.signUserId = user.getUserId(); //签订人id
//					this.signPerson = signPerson;
//					this.signPersonEntity = user;
//				}
//			}else{
//				this.signUserId = "0"; //签订人id
//				this.signPerson = "";
//			}
			code = 1;
			break;
		case "partnerName":
//			if(!StringUtil.isEmpty(value)){
//				Integer partnerId = 0;
//				List<CustomerEntity> partnerList = this.customerModel.getByCustomerName(value, this.corpid);
//				if(partnerList.size() == 0){
//					code = 2;
//					break;
//				}
//				partnerId = partnerList.get(0).getId();
//				this.partnerId = partnerId;
//				this.partnerName = value;
//				code = 1;
//			}else{
//				code = 1;
//			}
			//TODO 合作伙伴暂时不处理
			code = 1;
			break;
		case "paymentAmount":			//回款部分不处理
			
			code = 1;
			break;
		case "paymentEstimateTime":		//回款部分不处理
			
			code = 1;
			break;
		case "paymentStatus":			//回款部分不处理
	
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
		case "deadline"://到期时间
			Integer deadline = 0;
			try {
				deadline = Integer.valueOf(value);
			} catch (Exception e) {
				deadline = 0;
			}
			this.deadline = deadline;
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

