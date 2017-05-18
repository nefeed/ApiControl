package com.xbongbong.dingxbb.enums;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

public enum CustomerEnum{
	
	NAME("name", "客户名称", 0, 9999, 1, 1),
	NAMESHORT("nameShort", "客户简称", 1, 9998, 1, 1),
	PARENT("parent", "上级客户", 2, 9997,0, 0),
	PHONE("phone", "客户电话", 1, 9996, 0, 1),
	GENRE("genre", "客户类型",1, 9995, 0, 1),
	TYPE("type", "客户状态", 1, 9994, 1, 1),
	
	//只能修改可用状态
	ISINDIVIDUAL("isIndividual", "客户性质", 1, 9993, 0, 1),
	SCALE("scale", "客户分级", 1, 9992, 0, 1),
	INDUSTRY("industry", "客户行业", 1, 9991, 0, 1),
	IMPORTANTDEGREE("importantDegree", "重要程度", 1, 9990, 0, 1),
	COUNTRY("country", "国家",1, 9989, 0, 1),
	ADDRESS("address", "客户地址", 2, 9988, 0, 1),
//	PROVINCE("province", "省份", 2, 9991, 0),
//	CITY("city", "城市", 2, 9990, 0),
//	DISTRICT("district", "地区", 2, 9989, 0),
	
	//可更改的字段
	SOURCE("source", "客户来源",1, 9987, 0, 1),
	WEBSITE("website", "客户官网", 1, 9986, 0, 1),
	INSTRUCTION("instruction", "客户简介",1, 9985, 0, 1);
	
	
	/** 枚举描述 --英文名称*/
	private final String	alias;
	
	/** 枚举信息--中文名称 */
	private final String    name;
	
	/** 枚举值 -- */
	private final int code;//此Enum里的code用来标记该字段是否可以更改.---现在0是只能排序，其他code都为只可编辑是否启用、必填、排序，都不能改默认字段名字。  0只能排序；1名字、是否启用、排序（是否必填？） ；2是否启用、排序
	
	/** 枚举值 -- */
	private final int sort;//此Enum里的sort来标记该字段的排序值
	
	/** 枚举值 -- */
	private final int required;//此Enum里的sort来标记该字段的排序值. 0不必填；1必填
	
	/** 枚举值 -- */
	private final int enable;//此Enum里的enable来标记该字段默认是否启用. 0不不启用；1启用
	/**
	 * 构造一个<code>CustomerEnum</code>枚举对象
	 * @param alias
	 * @param name
	 * @param code
	 * @param sort
	 * @param required
	 */
	private CustomerEnum(String alias, String name, int code, int sort, int required, int enable) {
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
	 * @return List<CustomerEnum>
	 */
	public static List<CustomerEnum> getAllEnum() {
		List<CustomerEnum> list = new ArrayList<CustomerEnum>();
		for (CustomerEnum cache : values()) {
			list.add(cache);
		}
		return list;
	}
	
	/**
	 * 获取已经按照sort从大到小排序的所有枚举
	 * @return List<ContractEnum>
	 */
	public static List<CustomerEnum> getAllEnumSorted() {		
		List<CustomerEnum> list = getAllEnum();
		for (int i = 0; i < list.size(); i++) {
			for (int j = i+1; j < list.size(); j++) {
				CustomerEnum enumEntity = null;
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
	public static List<CustomerEnum> getModifyEnum() {
		List<CustomerEnum> list = new ArrayList<CustomerEnum>();
		for (CustomerEnum cache : values()) {
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
	public static CustomerEnum getByAlias(String alias) {
		for (CustomerEnum cache : values()) {
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
		for (CustomerEnum cache : values()) {
			list.add(cache.getAlias());
		}
		return list;
	}

	/**
	 * 获取全部枚举code
	 * @return List<Integer>
	 */
	public static List<Integer> getAllEnumCode() {
		List<Integer> list = new ArrayList<Integer>();
		for (CustomerEnum cache : values()) {
			list.add(cache.getCode());
		}
		return list;
	}
	/**
	 * 获取已经按照sort从大到小排序的所有枚举的名字和必填对应jsonList
	 * @return List<JSONObject>
	 */
	public static List<JSONObject> getAllEnumJsonSorted() {
		List<CustomerEnum> list = getAllEnum();
		for (int i = 0; i < list.size(); i++) {
			for (int j = i+1; j < list.size(); j++) {
				CustomerEnum enumEntity = null;
				if (list.get(j).getSort() > list.get(i).getSort()) {
					enumEntity = list.get(i);
					list.set(i, list.get(j));
					list.set(j, enumEntity);
				}
			}
			
		}
		
		List<JSONObject> basicJson = new ArrayList<JSONObject>();
		for (CustomerEnum enumEntity : list) {
			JSONObject object = new JSONObject();
			object.put("name", enumEntity.getName());
			object.put("required", enumEntity.getRequired());
			object.put("alias", enumEntity.getAlias());
			object.put("enable", enumEntity.getEnable());
			basicJson.add(object);
		}
		
		return basicJson;
	}
	
}
