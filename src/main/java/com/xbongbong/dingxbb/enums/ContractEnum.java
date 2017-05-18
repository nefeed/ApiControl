package com.xbongbong.dingxbb.enums;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

public enum ContractEnum {
	
	NAME("name", "合同名称", 0, 9999, 1, 1),
	CONTRACT_NO("contractNo", "合同编号", 0, 9998, 1, 1),
	CUSTOMER_NAME("customerName", "客户名称", 0, 9997, 1, 1),
	CONTACT_NAME("contactName", "客户联系人", 2, 9996, 0, 0),
	OPPORTUNITY_ID("opportunityId", "关联机会", 1, 9995, 0, 0), //添加关联机会
	PARTNER_NAME("partnerName", "合作客户", 1, 9994, 0, 0),
	PRODUCT("product", "关联产品", 0, 9993, 0, 1),
	AMOUNT("amount", "合同金额", 0, 9992, 1, 1),
	SIGN_TIME("signTime", "签订日期", 0, 9991, 1, 1),
	STATUS("status", "合同状态", 0, 9990, 1, 1),
	TYPE("type", "合同类型", 1, 9989, 0, 1),
	SIGN_PERSON("signPerson", "签订人", 0, 9988, 1, 1),
	PAY_METHOD("payMethod", "付款方式", 2, 9987, 0, 1),
	DEADLINE("deadline", "到期时间", 2, 9986, 0, 1);
	
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
	 * 构造一个<code>ContractEnum</code>枚举对象
	 * @param alias
	 * @param name
	 * @param code
	 * @param sort
	 * @param required
	 */
	private ContractEnum(String alias, String name, int code, int sort, int required, int enable) {
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
	public static List<ContractEnum> getAllEnum() {
		List<ContractEnum> list = new ArrayList<ContractEnum>();
		for (ContractEnum cache : values()) {
			list.add(cache);
		}
		return list;
	}	
	
	/**
	 * 获取已经按照sort从大到小排序的所有枚举
	 * @return List<ContractEnum>
	 */
	public static List<ContractEnum> getAllEnumSorted() {		
		List<ContractEnum> list = getAllEnum();
		for (int i = 0; i < list.size(); i++) {
			for (int j = i+1; j < list.size(); j++) {
				ContractEnum enumEntity = null;
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
	public static List<ContractEnum> getModifyEnum() {
		List<ContractEnum> list = new ArrayList<ContractEnum>();
		for (ContractEnum cache : values()) {
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
	public static ContractEnum getByAlias(String alias) {
		for (ContractEnum cache : values()) {
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
		for (ContractEnum cache : values()) {
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
		for (ContractEnum cache : values()) {
			list.add(cache.getCode());
		}
		return list;
	}
	
	/**
	 * 获取已经按照sort从大到小排序的所有枚举的名字和必填对应jsonList
	 * @return List<JSONObject>
	 */
	public static List<JSONObject> getAllEnumJsonSorted() {		
		List<ContractEnum> list = getAllEnum();
		for (int i = 0; i < list.size(); i++) {
			for (int j = i+1; j < list.size(); j++) {
				ContractEnum enumEntity = null;
				if (list.get(j).getSort() > list.get(i).getSort()) {
					enumEntity = list.get(i);
					list.set(i, list.get(j));
					list.set(j, enumEntity);
				}
			}
			
		}
		
		List<JSONObject> basicJson = new ArrayList<JSONObject>();
		for (ContractEnum enumEntity : list) {
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
