package com.xbongbong.dingxbb.enums;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

public enum ProductEnum {
	
	NAME("name", "产品名称", 0, 9999, 1, 1),
	PRODUCT_NO("productNo", "产品编号", 0, 9997, 1, 1),
	PRODUCT_IMGS("productImgs", "产品图片", 0, 9996, 0, 1),
	CATEGORY_ID("categoryId", "分类", 0, 9995, 0, 1),
		
	SPECIFICATION("specification", "型号", 1, 9994, 1, 1),
	STOCK("stock", "库存", 1, 9993, 0, 1),
	PRICE("price", "价格", 1, 9992, 0, 1),
	COST("cost", "成本", 1, 9991, 0, 1),
	INSTRUCTION("instruction", "产品简介", 1, 9990, 0, 1),
	UNIT("unit", "产品单位", 1, 9989, 0, 1);
	
	
	/** 枚举描述 --英文名称*/
	private final String	alias;
	
	/** 枚举信息--中文名称 */
	private final String    name;
	
	/** 枚举值 -- */
	private final int code;//此Enum里的code用来标记该字段是否可以更改. 0只能排序；1是否启用、排序
	
	/** 枚举值 -- */
	private final int sort;//此Enum里的sort来标记该字段的排序值
	
	/** 枚举值 -- */
	private final int required;//此Enum里的sort来标记该字段的排序值. 0非必填，1必填
	
	/** 枚举值 -- */
	private final int enable;//此Enum里的enable来标记该字段默认是否启用. 0不不启用；1启用
	
	/**
	 * 构造一个<code>ContractEnum</code>枚举对象
	 * @param alias
	 * @param name
	 * @param code
	 * @param sort
	 * @param required
	 */
	private ProductEnum(String alias, String name, int code, int sort, int required, int enable) {
		this.alias = alias;
		this.name = name;
		this.code = code;
		this.sort = sort;
		this.required = required;
		this.enable = enable;
	}
	
	/**
	 * @return Returns the enable.
	 */
	public int getEnable() {
		return enable;
	}
	
	/**
	 * @return Returns the required.
	 */
	public int getRequired() {
		return required;
	}
	
	/**
	 * @return Returns the sort.
	 */
	public int getSort() {
		return sort;
	}

	/**
	 * @return Returns the code.
	 */
	public int getCode() {
		return code;
	}
	
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return Returns the alias.
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * 获取全部枚举
	 * 
	 * @return List<ContractEnum>
	 */
	public static List<ProductEnum> getAllEnum() {
		List<ProductEnum> list = new ArrayList<ProductEnum>();
		for (ProductEnum cache : values()) {
			list.add(cache);
		}
		return list;
	}	
	
	/**
	 * 获取已经按照sort从大到小排序的所有枚举
	 * @return List<ContractEnum>
	 */
	public static List<ProductEnum> getAllEnumSorted() {		
		List<ProductEnum> list = getAllEnum();
		for (int i = 0; i < list.size(); i++) {
			for (int j = i+1; j < list.size(); j++) {
				ProductEnum enumEntity = null;
				if (list.get(j).getSort() > list.get(i).getSort()) {
					enumEntity = list.get(i);
					list.set(i, list.get(j));
					list.set(j, enumEntity);
				}
			}
			
		}
		return list;
	}
	
	/**
	 * 获取可以更改的字段
	 * @return
	 */
	public static List<ProductEnum> getModifyEnum() {
		List<ProductEnum> list = new ArrayList<ProductEnum>();
		for (ProductEnum cache : values()) {
			if (1 == cache.getCode()) {
				list.add(cache);
			}
		}
		return list;
	}
	
	/**
	 * 根据alias获取枚举
	 * @return
	 */
	public static ProductEnum getByAlias(String alias) {
		for (ProductEnum cache : values()) {
			if (alias.equals(cache.getAlias())) {
				return cache;
			}
		}
		return null;
	}
	
	/**
	 * 获取全部枚举值
	 * 
	 * @return List<String>
	 */
	public static List<String> getAllEnumAlis() {
		List<String> list = new ArrayList<String>();
		for (ProductEnum cache : values()) {
			list.add(cache.getAlias());
		}
		return list;
	}
	/**
	 * 获取全部枚举值
	 * 
	 * @return List<String>
	 */
	public static List<String> getAllAlisSorted() {
		List<ProductEnum> enumList = getAllEnum();
		for (int i = 0; i < enumList.size(); i++) {
			for (int j = i+1; j < enumList.size(); j++) {
				ProductEnum enumEntity = null;
				if (enumList.get(j).getSort() > enumList.get(i).getSort()) {
					enumEntity = enumList.get(i);
					enumList.set(i, enumList.get(j));
					enumList.set(j, enumEntity);
				}
			}
			
		}
		
		List<String> list = new ArrayList<String>();
		for (ProductEnum con : enumList) {
			list.add(con.getAlias());
		}
		return list;
	}
	
	/**
	 * 获取全部枚举code
	 * @return List<Integer>
	 */
	public static List<Integer> getAllEnumCode() {
		List<Integer> list = new ArrayList<Integer>();
		for (ProductEnum cache : values()) {
			list.add(cache.getCode());
		}
		return list;
	}

	/**
	 * 获取已经按照sort从大到小排序的所有枚举的名字和必填对应jsonList
	 * @return List<JSONObject>
	 */
	public static List<JSONObject> getAllEnumJsonSorted() {		
		List<ProductEnum> list = getAllEnum();
		for (int i = 0; i < list.size(); i++) {
			for (int j = i+1; j < list.size(); j++) {
				ProductEnum enumEntity = null;
				if (list.get(j).getSort() > list.get(i).getSort()) {
					enumEntity = list.get(i);
					list.set(i, list.get(j));
					list.set(j, enumEntity);
				}
			}
			
		}
		
		List<JSONObject> basicJson = new ArrayList<JSONObject>();
		for (ProductEnum enumEntity : list) {
			JSONObject object = new JSONObject();
			object.put("alias", enumEntity.getAlias());
			object.put("name", enumEntity.getName());
			object.put("required", enumEntity.getRequired());
			object.put("enable", enumEntity.getEnable());
			basicJson.add(object);
		}
		
		return basicJson;
	}
	
}
