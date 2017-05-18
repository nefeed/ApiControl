 
package com.xbongbong.dingxbb.entity;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.alibaba.fastjson.JSONArray;
import com.xbongbong.dingxbb.model.ProductCategoryModel;
import com.xbongbong.util.ReflectHelper;
import com.xbongbong.util.StringUtil;

 
public class ProductEntity implements Serializable,ImportEntity{

	/** Comment for <code>serialVersionUID</code> */
	private static final long serialVersionUID = -1L;

	//========== properties ==========

	//主键
	private Integer id;
	//公司ID
	private String corpid;
	//分类ID
	private Integer categoryId;
	//产品名称
	private String name;
	//产品编号，50字内
	private String productNo;
	//型号，json格式
	private String specification;
	//产品封面图
	private String thumbnail;
	//产品详情图，json格式保存
	private String productImgs;
	//产品简介
	private String instruction;
	//产品价格
	private Double price;
	//是否显示价格，1 显示 0 不显示
	private Integer showPrice;
	//产品成本
	private Double cost;
	//是否显示成本，1 显示 0 不显示
	private Integer showCost;
	//产品库存
	private Double stock;
	//产品单位
	private String unit;
	//创建时间
	private Integer addTime;
	//模板ID
	private Integer templateId;
	//更新时间
	private Integer updateTime;
	//删除标记
	private Integer del;
	
	
	//扩展字段
	//分类名称
	private String categoryName = "";
	
	//图片
	private JSONArray imgArray = new JSONArray();
	
	//产品显示
	private String priceStr = "";
	
	//库存显示
	private String stockShow = "";
	
	//成本显示
	private String costShow = "";
	
	//规格
	private JSONArray specificationArray = new JSONArray();
	
	/******************************产品导入重复数据更新********************************/
	//将excel中的分配信息存在此
	private String depName;
	
	private Map<Integer, DataDictionaryEntity> unitMap;
	
	private RedundantFieldEntity fieldEntity = new RedundantFieldEntity();
	
	private transient ProductCategoryModel productCategoryModel;
	/******************************产品导入重复数据更新********************************/

    //========== getters and setters ==========
		
		
	public ProductEntity(int i){
		categoryId = 0;
		name = "";
		productNo = "";
		specification = "[]";
		thumbnail = "";
		productImgs = "";
		instruction = "";
		price = 0.0;
		cost = 0.0;
		showPrice = 0;
		stock = 0d;
		templateId = 0;
		del = 0;
	}
		
    public ProductEntity(){
		categoryId = 0;
		name = "";
		productNo = "";
		specification = "[]";
		thumbnail = "";
		productImgs = "";
		instruction = "";
		price = 0.0;
		cost = 0.0;
		showPrice = 0;
		stock = 0d;
		templateId = 0;
		del = 0;
    }

    //========== getters and setters ==========
    
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
	public Integer getCategoryId() {
		return categoryId;
	}
	
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getProductNo() {
		return productNo;
	}
	
	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}
	public String getSpecification() {
		return specification;
	}
	
	public void setSpecification(String specification) {
		this.specification = specification;
	}
	public String getThumbnail() {
		if(StringUtil.isEmpty(thumbnail)){
			this.thumbnail = "/images/defaultpro.png";
		}
		return thumbnail;
	}
	
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	public String getProductImgs() {
		return productImgs;
	}
	
	public void setProductImgs(String productImgs) {
		this.productImgs = productImgs;
	}
	public String getInstruction() {
		return instruction;
	}
	
	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}
	public Double getPrice() {
		return price;
	}
	
	public void setPrice(Double price) {
		this.price = price;
	}
	
	public Integer getShowPrice() {
		return showPrice;
	}
	
	public void setShowPrice(Integer showPrice) {
		this.showPrice = showPrice;
	}
	
	public Integer getShowPriceInt(String showPriceStr){
		if("否".equals(showPriceStr)){
			return 0;
		}else{
			return 1;
		}
	}
	public String getShowPriceStr(Integer showPriceInt){
		if(showPriceInt != null && showPriceInt.equals(0)){
			return "否";
		}else{
			return "是";
		}
	}
	
	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public Integer getShowCost() {
		return showCost;
	}

	public void setShowCost(Integer showCost) {
		this.showCost = showCost;
	}
	
	public Integer getShowCostInt(String showCostStr){
		if("否".equals(showCostStr)){
			return 0;
		}else{
			return 1;
		}
	}
	public String getShowCostStr(Integer showCostInt){
		if(showCostInt != null && showCostInt.equals(0)){
			return "否";
		}else{
			return "是";
		}
	}
	
	public Double getStock() {
		return stock;
	}
	
	public void setStock(Double stock) {
		this.stock = stock;
	}
	
	public Integer getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
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
	
	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	

	public JSONArray getImgArray() {
		return imgArray;
	}

	public void setImgArray(JSONArray imgArray) {
		this.imgArray = imgArray;
	}

	public String getPriceStr() {
		return priceStr;
	}

	public void setPriceStr(String priceStr) {
		this.priceStr = priceStr;
	}

	public JSONArray getSpecificationArray() {
		return specificationArray;
	}

	public void setSpecificationArray(JSONArray specificationArray) {
		this.specificationArray = specificationArray;
	}

	public String getDepName() {
		return depName;
	}

	public void setDepName(String depName) {
		this.depName = depName;
	}

	public String getStockShow() {
		return stockShow;
	}

	public void setStockShow(String stockShow) {
		this.stockShow = stockShow;
	}

	public String getCostShow() {
		return costShow;
	}

	public void setCostShow(String costShow) {
		this.costShow = costShow;
	}

	/**
	 * @param unitMap the unitMap to set
	 */
	public void setUnitMap(Map<Integer, DataDictionaryEntity> unitMap) {
		this.unitMap = unitMap;
	}

	public RedundantFieldEntity getFieldEntity() {
		return fieldEntity;
	}

	public void setFieldEntity(RedundantFieldEntity fieldEntity) {
		this.fieldEntity = fieldEntity;
	}

	public void setProductCategoryModel(ProductCategoryModel productCategoryModel) {
		this.productCategoryModel = productCategoryModel;
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
	
	@Override
	//0：不是特殊字段，1：操作成功，2：值无法匹配到
	public Integer setValue(String value, String attr) {
		int code = 0;
		switch (attr) {
		case "specification":
			
			JSONArray specArray = new JSONArray();
			if (!StringUtil.isEmpty(value)) {
				String[] specificationArray = value.split("\\|");
				for (String spec : specificationArray) {
					if (!StringUtil.isEmpty(spec)) {
						specArray.add(spec);
					}
				}
			}
			this.specification = specArray.toString();
			break;
		case "unit":
			
			if(this.unitMap != null && !StringUtil.isEmpty(value)){
				for(Map.Entry<Integer, DataDictionaryEntity> entry : this.unitMap.entrySet()){
					DataDictionaryEntity dataDictionaryEntity = entry.getValue();
					if(value.equals(dataDictionaryEntity.getName())){
						this.unit = dataDictionaryEntity.getName();
						code = 1;
						break;
					}
				}
			}else{
				this.unit = "";
				code = 1;
			}
			if(code != 1)
				code = 2;
			break;
		case "categoryId":
			Integer categoryId = productCategoryModel.getCategoryIdByNames(value, corpid);
			this.categoryId = categoryId;
			code = 1;
			break;
		case "stock":
			try {
				Double stock = StringUtil.toDouble(value,0.0);
				this.stock = stock;
			} catch (Exception e) {
				code = 2;
				break ;
			}
			code = 1;
			break;
		case "price":
			try {
				Double price = StringUtil.toDouble(value,0.0);
				this.price = price;
			} catch (Exception e) {
				code = 2;
				break ;
			}
			code = 1;
			break;
		case "showPrice":
			int showPrice = 0;//默认不显示
			if(!StringUtil.isEmpty(value)){
				if("是".equals(value)){
					showPrice = 1;
				}
			}
			this.showPrice = showPrice;
			code = 1;
			break;
		case "cost":
			try {
				Double cost = StringUtil.toDouble(value,0.0);
				this.cost = cost;
			} catch (Exception e) {
				code = 2;
				break ;
			}
			code = 1;
			break;
		case "showCost":
			int showCost = 0;//默认不显示
			if(!StringUtil.isEmpty(value)){
				if("是".equals(value)){
					showCost = 1;
				}
			}
			this.showCost = showCost;
			code = 1;
			break;
		default:
			//可以直接赋值的字段,其中部门比较特别，赋值后，核心分配逻辑在保存完产品之后
			ReflectHelper.valueSet(this, attr, value);
			break;
		}
		return code;
	}

	@Override
	public void setValue() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setEmpty() {
		// TODO Auto-generated method stub
		
	}
}

