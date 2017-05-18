 
package com.xbongbong.dingxbb.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xbongbong.util.DateUtil;
import com.xbongbong.util.ReflectHelper;
import com.xbongbong.util.StringUtil;

 
public class CustomerEntity implements Serializable,ImportEntity{

	/** Comment for <code>serialVersionUID</code> */
	private static final long serialVersionUID = -1L;

	//========== properties ==========
	
	//主键
	private Integer id;
	//公司ID
	private String corpid;
	//上级客户ID
	private Integer parentId;
	//客户创建人
	private String userId;
	//客户名称
	private String name;
	//客户简称
	private String nameShort;
	//国家
	private String country;
	//省
	private String province;
	//市
	private String city;
	//区或县
	private String district;
	//客户详细地址
	private String address;
	//经度
	private Double longitude;
	//纬度
	private Double latitude;
	//邮编
	private String zipCode;
	//公司网址
	private String website;
	//客户来源
	private String source;
	//行业
	private Integer industry;
	//客户状态：该字段为数据字典字段
	private Integer type;
	//客户类型：数据字典
	private Integer genre;
	//客户规模【客户分级】：1 大型，2 中型，3 小型
	private Integer scale;
	//客户性质【命名是否需要改】：1 企业客户， 2 个人客户
	private Integer isIndividual;
	//重要程度，1到5，数字越大表示重要程度越高
	private Integer importantDegree;
	//客户总机，可以是手机或座机
	private String phone;
	//传真
	private String fax;
	//是否是公海客户池
	private Integer isPublic;
	//客户简介
	private String instruction;
	//健康度
	private Integer healthDegree;
	//创建时间
	private Integer addTime;
	//更新时间
	private Integer updateTime;
	//最近一次接触时间
	private Integer lastConnectTime;
	//是否归档
	private Integer isArchived;
	//删除标记
	private Integer del;
	//模板id
	private Integer templateId = 0;

	//名片
	private String businessCard;
	
	//钉钉客户ID
	private String dingtalkCustomerId;
	
	//扩展字段
	
	//分配时间
	private Integer distributionTime;
	//上级客户名称
	private String parentName;
	//回显联系方式
	private JSONArray phoneJsonArray = new JSONArray();
	//客户状态名称
	private String scaleStr;
	//客户性质名称
	private String isIndividualStr;
	//客户行业名称
	private String industryStr;
	//回显客户状态
	private String typeStr;
	//回显客户类型
	private String genreStr;
	//回显客户重要程度
	private String importantDegreeStr;
	//添加人
	private String staffName;
	//责任人/销售团队 逗号间隔
	private String userName;
	//协同人 逗号间隔
	private String minorUserName;
	//主负责人userId/销售团队userId列表，逗号间隔
	private String isMainUserId;	
	/*
	 * 是否主负责人判断序列,连表查询 GROUP_CONCAT出来的is_main序列，形如1,0,0,0 用逗号间隔，
	 * 代表userName,isMainUserId序列中对应用户是否是主负责
	 */
	private String isMainCheck;
	
	//客户数量（统计时使用）
	private Integer count;
	//开始时间（统计时使用）
	private Integer startTime;
	//添加客户的员工所在的部门ID（统计时使用）
	private Integer departmentId;
	//添加客户的员工所在的部门名（统计时使用）
	private String departmentName;
	//用于记录百分比
	private String percent;
	//标题（统计使用）
	private String title;
	//排名（统计使用）
	private Integer rank;
	//冗余字段
	private RedundantFieldEntity fieldEntity = new RedundantFieldEntity();
	
	//超过多少天未跟进
	private String lastConnectTimeStr = "";
	
	//是否关注客户
	private Integer isFocus;
	
	/************************导入特殊值处理用到******************************/
	private Map<Integer, DataDictionaryEntity> typeMap;
	private Map<Integer, DataDictionaryEntity> isIndividualMap;
	private Map<Integer, DataDictionaryEntity> industryMap;
	private Map<Integer, DataDictionaryEntity> genreMap;
	private Map<Integer, DataDictionaryEntity> countryMap;
	private Map<Integer, DataDictionaryEntity> sourceMap;
	private JSONObject redundantValueObject = new JSONObject();
	/************************导入特殊值处理用到******************************/
	
    //========== getters and setters ==========

		public CustomerEntity(){
			this.address = "";
			this.addTime = DateUtil.getInt();
			this.city = "";
			this.corpid = "";
			this.country = "";
			this.del = 0;
			this.district = "";
			this.fax = "";
			this.healthDegree = 0;
			this.importantDegree = 0;
			this.industry = 0;
			this.instruction = "";
			this.isIndividual = 0;
			this.isPublic = 0;
			this.lastConnectTime = 0;
			this.latitude = 0.0;
			this.longitude = 0.0;
			this.name = "";
			this.nameShort = "";
			this.phone = "";
			this.province = "";
			this.scale = 0;
			this.templateId = 0;
			this.type = 0;
			this.updateTime = 0;
			this.userId = "";
			this.userName = "";
			this.minorUserName = "";
			this.website = "";
			this.zipCode = "";
			this.isArchived = 0;
		}


	public Integer getId() {
		return id;
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

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getNameShort() {
		return nameShort;
	}
	
	public void setNameShort(String nameShort) {
		this.nameShort = nameShort;
	}
	public String getCountry() {
		return country;
	}
	
	public void setCountry(String country) {
		this.country = country;
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
	
	public Integer getIndustry() {
		return industry;
	}
	
	public void setIndustry(Integer industry) {
		this.industry = industry;
	}
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	public Double getLongitude() {
		return longitude;
	}
	
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Double getLatitude() {
		return latitude;
	}
	
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public String getZipCode() {
		return zipCode;
	}
	
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getWebsite() {
		return website;
	}
	
	public void setWebsite(String website) {
		this.website = website;
	}
	public Integer getType() {
		return type;
	}
	
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getScale() {
		return scale;
	}
	
	public void setScale(Integer scale) {
		this.scale = scale;
	}
	public Integer getIsIndividual() {
		return isIndividual;
	}
	
	public void setIsIndividual(Integer isIndividual) {
		this.isIndividual = isIndividual;
	}
	public Integer getImportantDegree() {
		return importantDegree;
	}
	
	public void setImportantDegree(Integer importantDegree) {
		this.importantDegree = importantDegree;
	}
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getFax() {
		return fax;
	}
	
	public void setFax(String fax) {
		this.fax = fax;
	}
	
	public Integer getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(Integer isPublic) {
		this.isPublic = isPublic;
	}

	public String getInstruction() {
		return instruction;
	}
	
	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}
	public Integer getHealthDegree() {
		return healthDegree;
	}
	
	public void setHealthDegree(Integer healthDegree) {
		this.healthDegree = healthDegree;
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
	public Integer getLastConnectTime() {
		return lastConnectTime;
	}
	
	public void setLastConnectTime(Integer lastConnectTime) {
		this.lastConnectTime = lastConnectTime;
	}

	public Integer getIsArchived() {
		return isArchived;
	}


	public void setIsArchived(Integer isArchived) {
		this.isArchived = isArchived;
	}


	public Integer getDel() {
		return del;
	}
	
	public void setDel(Integer del) {
		this.del = del;
	}
	
	public Integer getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}
	

	public JSONArray getPhoneJsonArray() {
		return phoneJsonArray;
	}

	public void setPhoneJsonArray(JSONArray phoneJsonArray) {
		this.phoneJsonArray = phoneJsonArray;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getMinorUserName() {
		return minorUserName;
	}


	public void setMinorUserName(String minorUserName) {
		this.minorUserName = minorUserName;
	}


	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getPercent() {
		return percent;
	}

	public void setPercent(String percent) {
		this.percent = percent;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getStartTime() {
		return startTime;
	}

	public void setStartTime(Integer startTime) {
		this.startTime = startTime;
	}

	public String getSource() {
		return source;
	}


	public void setSource(String source) {
		this.source = source;
	}


	public Integer getGenre() {
		return genre;
	}


	public void setGenre(Integer genre) {
		this.genre = genre;
	}


	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	/**
	 * @return the isMainCheck
	 */
	public String getIsMainCheck() {
		return isMainCheck;
	}


	/**
	 * @param isMainCheck the isMainCheck to set
	 */
	public void setIsMainCheck(String isMainCheck) {
		this.isMainCheck = isMainCheck;
	}


	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}
	

	public RedundantFieldEntity getFieldEntity() {
		return fieldEntity;
	}

	public void setFieldEntity(RedundantFieldEntity fieldEntity) {
		this.fieldEntity = fieldEntity;
	}

	public String getLastConnectTimeStr() {
		return lastConnectTimeStr;
	}

	public void setLastConnectTimeStr(String lastConnectTimeStr) {
		this.lastConnectTimeStr = lastConnectTimeStr;
	}

	
	
	public Integer getIsFocus() {
		return isFocus;
	}

	public void setIsFocus(Integer isFocus) {
		this.isFocus = isFocus;
	}
	
	public String getTypeStr() {
		return typeStr;
	}

	public void setTypeStr(String typeStr) {
		this.typeStr = typeStr;
	}
	
	public String getGenreStr() {
		return genreStr;
	}


	public void setGenreStr(String genreStr) {
		this.genreStr = genreStr;
	}


	public Integer getDistributionTime() {
		return distributionTime;
	}


	public void setDistributionTime(Integer distributionTime) {
		this.distributionTime = distributionTime;
	}


	public void setTypeStr(Integer type,List<DataDictionaryEntity> dataDictionaryList) {
		String value = "未设置";
		
		if(dataDictionaryList != null && type != null){
			for(DataDictionaryEntity dataDictionaryEntity : dataDictionaryList){
				if(type.equals(dataDictionaryEntity.getCode())){
					value = dataDictionaryEntity.getName();
					break;
				}
			}
		}
		
		setTypeStr(value);
	}
	
	public void setGenreStr(Integer genre,List<DataDictionaryEntity> dataDictionaryList) {
		String value = "未设置";
		
		if(dataDictionaryList != null && genre != null){
			for(DataDictionaryEntity dataDictionaryEntity : dataDictionaryList){
				if(genre.equals(dataDictionaryEntity.getCode())){
					value = dataDictionaryEntity.getName();
					break;
				}
			}
		}
		
		setGenreStr(value);
	}

	public String getIsIndividualStr() {
		return isIndividualStr;
	}

	public void setIsIndividualStr(String isIndividualStr) {
		this.isIndividualStr = isIndividualStr;
	}
	//1 企业客户， 2 个人客户
	public void setIsIndividualStr(Integer isIndividual) {
		String value = null;
		switch (isIndividual) {
		case 1:
			value = "企业客户";
			break;
		case 2:
			value = "个人客户";
			break;
		}
		setIsIndividualStr(value);
	}
	public void setIsIndividualStr(Integer isIndividual, List<DataDictionaryEntity> dataDictionaryList) {
		String value = "未设置";
		
		if(dataDictionaryList != null && isIndividual != null){
			for(DataDictionaryEntity dataDictionaryEntity : dataDictionaryList){
				if(isIndividual.equals(dataDictionaryEntity.getCode())){
					value = dataDictionaryEntity.getName();
					break;
				}
			}
		}
		setIsIndividualStr(value);
	}

	public String getScaleStr() {
		return scaleStr;
	}

	public void setScaleStr(String scaleStr) {
		this.scaleStr = scaleStr;
	}
	//客户规模【客户分级】：1 大型，2 中型，3 小型
	public void setScaleStr(Integer scale) {
		String value = getScaleStr(scale);
		setScaleStr(value);
	}

	public Integer getScaleInt(String scaleStr){
		Integer scaleInt = 0;
		switch (scaleStr) {
		case "大型":
			scaleInt = 1;
			break;
		case "中型":
			scaleInt = 2;
			break;
		case "小型":
			scaleInt = 3;
			break;
		default:
			break;
		}
		return scaleInt;
	}
	
	public String getScaleStr(Integer scaleInt){
		String scaleStr = "";
		switch (scaleInt) {
		case 1:
			scaleStr = "大型";
			break;
		case 2:
			scaleStr = "中型";
			break;
		case 3:
			scaleStr = "小型";
			break;
		default:
			break;
		}
		return scaleStr;
	}
	
	public String getIndustryStr() {
		return industryStr;
	}

	public void setIndustryStr(String industryStr) {
		this.industryStr = industryStr;
	}
	
	public void setIndustryStr(Integer industry) {
		String value = null;
		switch (industry) {
		case 1:
			value = "金融";
			break;
		case 2:
			value = "电信";
			break;
		case 3:
			value = "教育";
			break;
		case 4:
			value = "高科技";
			break;
		case 5:
			value = "政府";
			break;
		case 6:
			value = "制造业";
			break;
		case 7:
			value = "服务业";
			break;
		case 8:
			value = "能源";
			break;
		case 9:
			value = "零售";
			break;
		case 10:
			value = "媒体";
			break;
		case 11:
			value = "娱乐";
			break;
		case 12:
			value = "咨询";
			break;
		case 13:
			value = "非营利事业";
			break;
		case 14:
			value = "公用事业";
			break;
		case 15:
			value = "其他";
			break;
		};
		setIndustryStr(value);
	}
	public void setIndustryStr(Integer industry, List<DataDictionaryEntity> dataDictionaryList) {
		String value = "未设置";
		
		if(dataDictionaryList != null && industry != null){
			for(DataDictionaryEntity dataDictionaryEntity : dataDictionaryList){
				if(industry.equals(dataDictionaryEntity.getCode())){
					value = dataDictionaryEntity.getName();
					break;
				}
			}
		}
		setIndustryStr(value);
	}

	public String getImportantDegreeStr() {
		return importantDegreeStr;
	}

	public void setImportantDegreeStr(String importantDegreeStr) {
		this.importantDegreeStr = importantDegreeStr;
	}
	
	public void setImportantDegreeStr(Integer importantDegree) {
		String value = getImportantDegreeStr(importantDegree);
		setImportantDegreeStr(value);
	}

	public Integer getImportantDegreeInt(String importantDegreeStr){
		Integer importantDegreeInt = 0;
		//先尝试直接转化
		try {
			importantDegreeInt = Integer.valueOf(importantDegreeStr);
		} catch (Exception e) {
			importantDegreeInt = 0;
		}
		if(importantDegreeInt > 0) {
			if(importantDegreeInt > 5) {
				importantDegreeInt = 5;
			}
			return importantDegreeInt;
		}
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
	
	public String getIsMainUserId() {
		return isMainUserId;
	}


	public void setIsMainUserId(String isMainUserId) {
		this.isMainUserId = isMainUserId;
	}

	public String getDingtalkCustomerId() {
		return dingtalkCustomerId;
	}


	public void setDingtalkCustomerId(String dingtalkCustomerId) {
		this.dingtalkCustomerId = dingtalkCustomerId;
	}

	public JSONObject getRedundantValueObject() {
		return redundantValueObject;
	}


	public void setRedundantValueObject(JSONObject redundantValueObject) {
		this.redundantValueObject = redundantValueObject;
	}

	/**
	 * @param typeMap the typeMap to set
	 */
	public void setTypeMap(Map<Integer, DataDictionaryEntity> typeMap) {
		this.typeMap = typeMap;
	}


	/**
	 * @param isIndividualMap the isIndividualMap to set
	 */
	public void setIsIndividualMap(Map<Integer, DataDictionaryEntity> isIndividualMap) {
		this.isIndividualMap = isIndividualMap;
	}


	/**
	 * @param industryMap the industryMap to set
	 */
	public void setIndustryMap(Map<Integer, DataDictionaryEntity> industryMap) {
		this.industryMap = industryMap;
	}


	/**
	 * @param genreMap the genreMap to set
	 */
	public void setGenreMap(Map<Integer, DataDictionaryEntity> genreMap) {
		this.genreMap = genreMap;
	}


	/**
	 * @param countryMap the countryMap to set
	 */
	public void setCountryMap(Map<Integer, DataDictionaryEntity> countryMap) {
		this.countryMap = countryMap;
	}


	/**
	 * @param sourceMap the sourceMap to set
	 */
	public void setSourceMap(Map<Integer, DataDictionaryEntity> sourceMap) {
		this.sourceMap = sourceMap;
	}


	public String getParentName() {
		return parentName;
	}


	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	/**
	 * @return the businessCard
	 */
	public String getBusinessCard() {
		return businessCard;
	}


	/**
	 * @param businessCard the businessCard to set
	 */
	public void setBusinessCard(String businessCard) {
		this.businessCard = businessCard;
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

	/**
	 * 特殊字段处理，用于excel导入
	 * @param value
	 * @param attr
	 * @return  0：不是特殊字段，1：操作成功，2：值无法匹配到
	 */
	public Integer setValue(String value, String attr) {
		Integer code = 0;
		
		switch (attr) {
		case "phone":
			JSONArray jsonArray = new JSONArray();
			JSONObject json = new JSONObject();
			String tel = value;
			if(!StringUtil.isEmpty(tel)){
				json.put("name", "手机");
				json.put("telNum", tel);
				jsonArray.add(json);
			}
			this.phone = jsonArray.toJSONString();
			this.phoneJsonArray = jsonArray;
			this.redundantValueObject.put(attr, value);
			code = 1;
			break;
		case "genre":
			if(StringUtil.isEmpty(value)){
				this.genre = 0;
				code = 1;
				break ;
			}
			for(Map.Entry<Integer, DataDictionaryEntity> entry : this.genreMap.entrySet()){
				DataDictionaryEntity dataDictionary = entry.getValue();
				if(value.equals(dataDictionary.getName())){ //匹配到时，赋值、code置1
					this.genre = dataDictionary.getCode();
					this.redundantValueObject.put(attr, value);
					code = 1;
					break ;
				}
			}
			if(!code.equals(1))
				code = 2;
			break;
		case "type":
			if(StringUtil.isEmpty(value)){
				this.type = 0;
				code = 1;
				break ;
			}
			for(Map.Entry<Integer, DataDictionaryEntity> entry : this.typeMap.entrySet()){
				DataDictionaryEntity dataDictionary = entry.getValue();
				if(value.equals(dataDictionary.getName())){ //匹配到时，赋值、code置1
					this.type = dataDictionary.getCode();
					this.redundantValueObject.put(attr, value);
					code = 1;
					break ;
				}
			}
			if(!code.equals(1))
				code = 2;
			break;
		case "isIndividual":
			if(StringUtil.isEmpty(value)){
				this.isIndividual = 0;
				code = 1;
				break ;
			}
			for(Map.Entry<Integer, DataDictionaryEntity> entry : this.isIndividualMap.entrySet()){
				DataDictionaryEntity dataDictionary = entry.getValue();
				if(value.equals(dataDictionary.getName())){ //匹配到时，赋值、code置1
					this.isIndividual = dataDictionary.getCode();
					code = 1;
					this.redundantValueObject.put(attr, value);
					break ;
				}
			}
			if(!code.equals(1))
				code = 2;
			break;
		case "scale":
			Integer scale = getScaleInt(value);
			this.scale = scale;
			this.redundantValueObject.put(attr, value);
			code = 1;
			break;
		case "industry":
			if(StringUtil.isEmpty(value)){
				this.industry = 0;
				code = 1;
				break ;
			}
			for(Map.Entry<Integer, DataDictionaryEntity> entry : this.industryMap.entrySet()){
				DataDictionaryEntity dataDictionary = entry.getValue();
				if(value.equals(dataDictionary.getName())){ //匹配到时，赋值、code置1
					this.industry = dataDictionary.getCode();
					code = 1;
					this.redundantValueObject.put(attr, value);
					break ;
				}
			}
			if(!code.equals(1))
				code = 2;
			break;
		case "importantDegree":
			Integer importantDegree = getImportantDegreeInt(value);
			this.importantDegree = importantDegree;
			code = 1;
			this.redundantValueObject.put(attr, value);
			break;
		case "country":
			if(StringUtil.isEmpty(value)){
				this.country = "";
				code = 1;
				break ;
			}
			for(Map.Entry<Integer, DataDictionaryEntity> entry : this.countryMap.entrySet()){
				DataDictionaryEntity dataDictionary = entry.getValue();
				if(value.equals(dataDictionary.getName())){ //匹配到时，赋值、code置1
					this.country = dataDictionary.getName();
					this.redundantValueObject.put(attr, value);
					code = 1;
					break ;
				}
			}
			if(!code.equals(1))
				code = 2;
			break;
		case "source":
			if(StringUtil.isEmpty(value)){
				this.source = "";
				code = 1;
				break ;
			}
			for(Map.Entry<Integer, DataDictionaryEntity> entry : this.sourceMap.entrySet()){
				DataDictionaryEntity dataDictionary = entry.getValue();
				if(value.equals(dataDictionary.getName())){ //匹配到时，赋值、code置1
					this.source = dataDictionary.getName();
					this.redundantValueObject.put(attr, value);
					code = 1;
					break ;
				}
			}
			if(!code.equals(1))
				code = 2;
			break;
		case "userId":
			code = 1;//不处理
			break;
		default:
			//可以直接赋值的字段,其中产品比较特别，赋值后，核心分配逻辑在保存完销售机会之后
			ReflectHelper.valueSet(this, attr, value);
			this.redundantValueObject.put(attr, value);
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
		this.corpid = "";
		this.nameShort = "";
		this.country = "";
		this.province = "";
		this.city = "";
		this.district = "";
		this.address = "";
		this.zipCode = "";
		this.website = "";
		this.industry = 0;
		this.type = 0;
		this.scale = 0;
		this.isIndividual = 0;
		this.importantDegree = 0;
		this.phone = "[]";
	}
	
	public boolean isEmpty() {
		if(!StringUtil.isEmpty(this.corpid)){
			return false;
		}
		if(!StringUtil.isEmpty(this.nameShort)){
			return false;
		}
		if(!StringUtil.isEmpty(this.country)){
			return false;
		}
		if(!StringUtil.isEmpty(this.province)){
			return false;
		}
		if(!StringUtil.isEmpty(this.city)){
			return false;
		}
		if(!StringUtil.isEmpty(this.district)){
			return false;
		}
		if(!StringUtil.isEmpty(this.address)){
			return false;
		}
		
		if(!StringUtil.isEmpty(this.zipCode)){
			return false;
		}
		if(!StringUtil.isEmpty(this.website)){
			return false;
		}
		if(this.industry != 0){
			return false;
		}
		if(this.type != 0){
			return false;
		}
		if(this.scale != 0){
			return false;
		}
		if(this.isIndividual != 0){
			return false;
		}
		if(this.importantDegree != 0){
			return false;
		}
//		if(!this.phone.equals("[]")){
//			return false;
//		}
		return true;
	}
}

