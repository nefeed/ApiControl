 
package com.xbongbong.dingxbb.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xbongbong.util.CommentUtil;
import com.xbongbong.util.DateUtil;
import com.xbongbong.util.ReflectHelper;
import com.xbongbong.util.StringUtil;

 
public class ContactEntity implements Serializable,ImportEntity{

	/** Comment for <code>serialVersionUID</code> */
	private static final long serialVersionUID = -1L;

	//========== properties ==========
	
	//主键
	private Integer id;
	//模板id
	private Integer templateId;
	//客户ID
	private Integer customerId;
	//公司ID，统计和迁移时用
	private String corpid;
	//创建人id
	private String userId;
	//客户名称
	private String customerName;
	//联系人姓名
	private String name;
	//尊称
	private String honorificTitle;
	//部门
	private String department;
	//职位
	private String position;
	//联系人等级，1 高层，2中层，3基层
	private Integer level;
	//联系电话，json保存，支持添加多个联系电话【手机，座机，传真等】
	private String phone;
	//email
	private String email;
	//qq
	private String qq;
	//性别，0表示未填，1表示男，2表示女
	private Integer sex;
	//生日
	private Integer birthday;
	//兴趣爱好
	private String hobbies;
	//联系人地址
	private String address;
	//邮编
	private String zipCode;
	//决策关系
	private String relationship;
	//亲密度，是用1-5或其他范围数值的度量值还是用描述
	private Integer intimateDegree;
	//重要程度，1-5的度量值
	private Integer importantDegree;
	// 备注 
	private String memo;
	// 省 
	private String province;
	// 市 
	private String city;
	// 区
	private String district;
	// 纬度 
	private Double latitude;
	// 经度
	private Double longitude;
	//创建时间
	private Integer addTime;
	//更新时间
	private Integer updateTime;
	
	//删除标记
	private Integer del;
	
	
	//扩展字段
		private List<String> phoneItemList = new ArrayList<String>();

		private JSONArray phoneJsonArray = new JSONArray();
		//客户联系方式
		private JSONArray cuPhoneJsonArray = new JSONArray();
		//性别显示
		private String sexStr;
		//回显生日
		private String birthdayStr;
		//等级显示字段
		private String levelStr;
		private String importantDegreeStr;
		private String intimateDegreeStr;
		//冗余字段
		private RedundantFieldEntity fieldEntity = new RedundantFieldEntity();
		//主负责人userId
		private String isMainUserId;
		
		/****************** 导入特殊值处理用到 *******************/
		private String tel = "";
		private JSONObject redundantValueObject = new JSONObject();
		private Map<Integer, DataDictionaryEntity> levelMap;
		private Map<String,Integer> customerNameIdMap;
		/****************** 导入特殊值处理用到 *******************/
		
	public ContactEntity() {
		this.name = "";
		this.userId = "";
		this.customerId = 0;
		this.customerName = "";
		this.phone = "";
		this.honorificTitle = "";
		this.department = "";
		this.position = "";
		this.level = 0;
		this.importantDegree = 0;
		this.email = "";
		this.qq = "";
		this.address = "";
		this.province = "";
		this.city = "";
		this.district = "";
		this.latitude = 0.0;
		this.longitude = 0.0;
		this.zipCode = "";
		this.sex = 0;
		this.birthday = 0;
		this.hobbies = "";
		this.relationship = "";
		this.intimateDegree = 0;
		this.memo = "";
		this.updateTime = 0;
		this.addTime = 0;
		this.del = 0;
	}
		
    //========== getters and setters ==========
    

	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}

	public Integer getCustomerId() {
		return customerId;
	}
	
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
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

	public String getCustomerName() {
		return customerName;
	}
	
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getHonorificTitle() {
		return honorificTitle;
	}
	
	public void setHonorificTitle(String honorificTitle) {
		this.honorificTitle = honorificTitle;
	}
	public String getDepartment() {
		return department;
	}
	
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getPosition() {
		return position;
	}
	
	public void setPosition(String position) {
		this.position = position;
	}
	public Integer getLevel() {
		return level;
	}
	
	public void setLevel(Integer level) {
		this.level = level;
	}
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	public String getQq() {
		return qq;
	}
	
	public void setQq(String qq) {
		this.qq = qq;
	}
	public Integer getSex() {
		return sex;
	}
	
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public Integer getBirthday() {
		return birthday;
	}
	
	public void setBirthday(Integer birthday) {
		this.birthday = birthday;
	}
	public String getHobbies() {
		return hobbies;
	}
	
	public void setHobbies(String hobbies) {
		this.hobbies = hobbies;
	}
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	public String getZipCode() {
		return zipCode;
	}
	
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getRelationship() {
		return relationship;
	}
	
	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}
	public Integer getIntimateDegree() {
		return intimateDegree;
	}
	
	public void setIntimateDegree(Integer intimateDegree) {
		this.intimateDegree = intimateDegree;
	}
	public Integer getImportantDegree() {
		return importantDegree;
	}
	
	public void setImportantDegree(Integer importantDegree) {
		this.importantDegree = importantDegree;
	}
	public String getMemo() {
		return memo;
	}
	
	public void setMemo(String memo) {
		this.memo = memo;
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

	public List<String> getPhoneItemList() {
		return phoneItemList;
	}

	public void setPhoneItemList(List<String> phoneItemList) {
		this.phoneItemList = phoneItemList;
	}
	
	public JSONArray getPhoneJsonArray() {
		return phoneJsonArray;
	}

	public void setPhoneJsonArray(JSONArray phoneJsonArray) {
		this.phoneJsonArray = phoneJsonArray;
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

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}


	public String getBirthdayStr() {
		return birthdayStr;
	}

	public void setBirthdayStr(String birthdayStr) {
		this.birthdayStr = birthdayStr;
	}

	public String getLevelStr() {
		return levelStr;
	}

	public void setLevelStr(String levelStr) {
		this.levelStr = levelStr;
	}

	public String getSexStr() {
		return sexStr;
	}

	public void setSexStr(String sexStr) {
		this.sexStr = sexStr;
	}
	public void setSexStr(Integer sex) {
		String value = "未设置";
		switch (sex) {
		case 1:
			value = "男";
			break;
		case 2:
			value = "女";
			break;
		}
		setSexStr(value);
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
	
	public String getIntimateDegreeStr() {
		return intimateDegreeStr;
	}

	public void setIntimateDegreeStr(String intimateDegreeStr) {
		this.intimateDegreeStr = intimateDegreeStr;
	}
	public void setIntimateDegreeStr(Integer intimateDegree) {
		String value = getIntimateDegreeStr(intimateDegree);
		setIntimateDegreeStr(value);
	}

	public Integer getIntimateDegreeInt(String intimateDegreeStr){
		Integer intimateDegreeInt = 0;
		//先尝试直接转化
		try {
			intimateDegreeInt = Integer.valueOf(intimateDegreeStr);
		} catch (Exception e) {
			intimateDegreeInt = 0;
		}
		if(intimateDegreeInt > 0) {
			if(intimateDegreeInt > 5) {
				intimateDegreeInt = 5;
			}
			return intimateDegreeInt;
		}
		switch (intimateDegreeStr) {
		case "一星":
			intimateDegreeInt = 1;
			break;
		case "二星":
			intimateDegreeInt = 2;
			break;
		case "三星":
			intimateDegreeInt = 3;
			break;
		case "四星":
			intimateDegreeInt = 4;
			break;
		case "五星":
			intimateDegreeInt = 5;
			break;
		default:
			break;
		}
		return intimateDegreeInt;
	}
	
	public String getIntimateDegreeStr(Integer intimateDegreeInt){
		String intimateDegreeStr = "";
		switch (intimateDegreeInt) {
		case 1:
			intimateDegreeStr = "一星";
			break;
		case 2:
			intimateDegreeStr = "二星";
			break;
		case 3:
			intimateDegreeStr = "三星";
			break;
		case 4:
			intimateDegreeStr = "四星";
			break;
		case 5:
			intimateDegreeStr = "五星";
			break;
		default:
			break;
		}
		return intimateDegreeStr;
	}
	
	public RedundantFieldEntity getFieldEntity() {
		return fieldEntity;
	}

	
	
	public void setFieldEntity(RedundantFieldEntity fieldEntity) {
		this.fieldEntity = fieldEntity;
	}

	public String getIsMainUserId() {
		return isMainUserId;
	}

	public void setIsMainUserId(String isMainUserId) {
		this.isMainUserId = isMainUserId;
	}

	public JSONObject getRedundantValueObject() {
		return redundantValueObject;
	}

	public void setRedundantValueObject(JSONObject redundantValueObject) {
		this.redundantValueObject = redundantValueObject;
	}

	/**
	 * @param levelMap the levelMap to set
	 */
	public void setLevelMap(Map<Integer, DataDictionaryEntity> levelMap) {
		this.levelMap = levelMap;
	}

	public void setCustomerNameIdMap(Map<String, Integer> customerNameIdMap) {
		this.customerNameIdMap = customerNameIdMap;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
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
			if(this.id == null || this.id.equals(0)){
				JSONArray jsonArray = new JSONArray();
				JSONObject json = new JSONObject();
				String tel = value;
				if(!StringUtil.isEmpty(tel)){
					json.put("name", "手机");
					json.put("telNum", tel);
					jsonArray.add(json);
				}
				this.phone = jsonArray.toJSONString();
				this.redundantValueObject.put(attr, value);
			}else{
				this.tel = value;
			}
			code = 1;
			break ;
		case "level":
			if(StringUtil.isEmpty(value)){
				this.level = 0;
				code = 1;
				break ;
			}
			for(Map.Entry<Integer,DataDictionaryEntity> entry : levelMap.entrySet()){
				DataDictionaryEntity entity = entry.getValue();
				if(value.equals(entity.getName())){ //匹配到时，赋值、code置1
					this.level = entity.getCode();
					this.redundantValueObject.put(attr, value);
					code = 1;
					break ;
				}
			}
			if(!code.equals(1))
				code = 2;
			break;
		case "customerName":
			
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
			}*/
			this.redundantValueObject.put(attr, value);	
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
		case "birthday":
			String birthdayStr = value;
			Integer birthday = 0;
			try {
				if(!StringUtil.isEmpty(birthdayStr)){
					birthday = DateUtil.getInt(birthdayStr, DateUtil.SDFDate);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			this.birthday = birthday;
			this.redundantValueObject.put(attr, value);
			code = 1;
			break;
		case "sex":
			String sexStr = value;
			Integer sex = CommentUtil.getSexNum(sexStr);
			this.sex = sex;
			this.redundantValueObject.put(attr, value);
			code = 1;
			break;
		case "importantDegree":
			String importantDegreeStr = value;
			Integer importantDegree = getImportantDegreeInt(importantDegreeStr);
			this.importantDegree = importantDegree;
			this.redundantValueObject.put(attr, value);
			code = 1;
			break;
		case "intimateDegree":
			String intimateDegreeStr = value;
			Integer intimateDegree = getIntimateDegreeInt(intimateDegreeStr);
			this.intimateDegree = intimateDegree;
			this.redundantValueObject.put(attr, value);
			code = 1;
			break;
		default:
			//可以直接赋值的字段,其中产品比较特别，赋值后，核心分配逻辑在保存完销售机会之后
			ReflectHelper.valueSet(this, attr, value);
			this.redundantValueObject.put(attr, value);
			break;
		}
		return code;
	}
	
	
	
	public JSONArray getCuPhoneJsonArray() {
		return cuPhoneJsonArray;
	}

	public void setCuPhoneJsonArray(JSONArray cuPhoneJsonArray) {
		this.cuPhoneJsonArray = cuPhoneJsonArray;
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

