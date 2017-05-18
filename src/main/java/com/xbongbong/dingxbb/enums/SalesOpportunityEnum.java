package com.xbongbong.dingxbb.enums;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

public enum SalesOpportunityEnum {
	
	NAME("name", "销售机会名称", 0, 9999, 1, 1),
	CUSTOMER_NAME("customerName", "客户名称", 0, 9998, 1, 1),
	CONTACT_NAME("contactName", "客户联系人", 2, 9997, 0, 0),
	PRODUCT("product", "关联产品", 0, 9996, 0, 1),  //客户表没有对应字段
	ESTIMATE_AMOUNT("estimateAmount", "预计金额", 0, 9995, 1, 1),
	SALES_STAGE_ID("salesStageId", "销售阶段", 0, 9994, 1, 1),
	ESTIMATE_END_TIME("estimateEndTime", "预计结束时间", 0, 9993, 1, 1),
	IMPORTANT_DEGREE("importantDegree", "重要程度", 1, 9992, 0, 1),
	DECISION_MAKER("decisionMaker", "决策人", 1, 9991, 0, 1),
	//ESTIMATE_WIN_RATE("estimateWinRate", "赢率", 0, 9991, 0), 赢率与销售阶段关联
	//PARTNER("partner", "合作伙伴", 2, 9990, 0),
	COMPETITOR("competitor", "竞争对手", 2, 9990, 0, 1);
	
	//可更改的字段
	
	
	/** 枚举描述 --英文名称*/
	private final String	alias;
	
	/** 枚举信息--中文名称 */
	private final String    name;
	
	/** 枚举值 -- */
	private final int code;//此Enum里的code用来标记该字段是否可以更改. 0只能排序；1名字、是否启用、排序 ；2是否启用、排序
	
	/** 枚举值 -- */
	private final int sort;//此Enum里的sort来标记该字段的排序值
	
	/** 枚举值 -- */
	private final int required;//此Enum里的sort来标记该字段的排序值
	
	/** 枚举值 -- */
	private final int enable;//此Enum里的enable来标记该字段默认是否启用. 0不不启用；1启用
	/**
	 * 构造一个<code>SalesOpportunityEnum</code>枚举对象
	 * @param alias
	 * @param name
	 * @param code
	 * @param sort
	 * @param required
	 */
	private SalesOpportunityEnum(String alias, String name, int code, int sort, int required, int enable) {
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
	 * @return List<SalesOpportunityEnum>
	 */
	public static List<SalesOpportunityEnum> getAllEnum() {
		List<SalesOpportunityEnum> list = new ArrayList<SalesOpportunityEnum>();
		for (SalesOpportunityEnum cache : values()) {
			list.add(cache);
		}
		return list;
	}
	
	/**
	 * 获取已经按照sort从大到小排序的所有枚举
	 * @return List<ContractEnum>
	 */
	public static List<SalesOpportunityEnum> getAllEnumSorted() {		
		List<SalesOpportunityEnum> list = getAllEnum();
		for (int i = 0; i < list.size(); i++) {
			for (int j = i+1; j < list.size(); j++) {
				SalesOpportunityEnum enumEntity = null;
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
	public static List<SalesOpportunityEnum> getModifyEnum() {
		List<SalesOpportunityEnum> list = new ArrayList<SalesOpportunityEnum>();
		for (SalesOpportunityEnum cache : values()) {
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
	public static SalesOpportunityEnum getByAlias(String alias) {
		for (SalesOpportunityEnum cache : values()) {
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
		for (SalesOpportunityEnum cache : values()) {
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
		for (SalesOpportunityEnum cache : values()) {
			list.add(cache.getCode());
		}
		return list;
	}

	/**
	 * 获取已经按照sort从大到小排序的所有枚举的名字和必填对应jsonList
	 * @return List<JSONObject>
	 */
	public static List<JSONObject> getAllEnumJsonSorted() {		
		List<SalesOpportunityEnum> list = getAllEnum();
		for (int i = 0; i < list.size(); i++) {
			for (int j = i+1; j < list.size(); j++) {
				SalesOpportunityEnum enumEntity = null;
				if (list.get(j).getSort() > list.get(i).getSort()) {
					enumEntity = list.get(i);
					list.set(i, list.get(j));
					list.set(j, enumEntity);
				}
			}
			
		}
		
		List<JSONObject> basicJson = new ArrayList<JSONObject>();
		for (SalesOpportunityEnum enumEntity : list) {
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
