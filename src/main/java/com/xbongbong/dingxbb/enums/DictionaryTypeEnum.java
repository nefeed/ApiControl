package com.xbongbong.dingxbb.enums;

import java.util.ArrayList;
import java.util.List;

public enum DictionaryTypeEnum {
	
	CONTRACT_STATUS(1, "合同状态", "contractStatus"),
	
	CONTRACT_TYPE(2, "合同类型", "contractType"),
	
	CUSTOMER_TYPE(3, "客户状态", "customerType"),
	
	CUSTOMER_IS_INDIVIDUAL(4, "客户性质", "customerIsIndividual"),
	
	CONTACT_LEVEL(5, "联系人级别", "contactLevel"),
	
	CUSTOMER_INDUSTRY(6, "客户行业", "customerIndustry"),
	
	APPROVAL_TYPE(7, "审批项目", "approvalType"),
	
	SALES_STAGE(8, "销售阶段", "salesStage"),

	COMMUNICATE_TYPE(9, "跟进方式", "communicateType"),
	
	RELATIONSHIP(10, "决策关系", "relationship"),
	
	CUSTOMER_COUNTRY(11, "国家", "customerCountry"),
	
	CUSTOMER_SOURCE(12, "客户来源", "customerSource"),
	
	CUSTOMER_GENRE(13, "客户类型", "customerGenre"),
	
	PRODUCT_UNIT(14, "产品单位", "productUnit");
	
	/** 枚举值 -- */
	private final int		code;
	
	/** 枚举信息--中文名称 */
	private final String    name;
	
	/** 枚举描述 --英文名称*/
	private final String	alias;
	
	/**
	 * 构造一个<code>SubModuleEnum</code>枚举对象
	 *
	 * @param code
	 * @param name
	 * @param alias
	 */
	private DictionaryTypeEnum(int code, String name,String alias) {
		this.code = code;
		this.name = name;
		this.alias = alias;
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
	 * 通过枚举<code>alias</code>获得枚举
	 *
	 * @param alias
	 * @return WxErrorCodeEnum
	 */
	public static DictionaryTypeEnum getByAlias(String alias) {
		for (DictionaryTypeEnum cacheCode : values()) {
			if (cacheCode.getAlias().equals(alias)) {
				return cacheCode;
			}
		}
		return null;
	}
	
	/**
	 * 通过枚举<code>code</code>获得枚举
	 *
	 * @param alias
	 * @return WxErrorCodeEnum
	 */
	public static DictionaryTypeEnum getByCode(Integer code) {
		for (DictionaryTypeEnum cacheCode : values()) {
			if (code.equals(cacheCode.getCode())) {
				return cacheCode;
			}
		}
		return null;
	}
	
	/**
	 * 获取全部枚举
	 * 
	 * @return List<WxErrorCodeEnum>
	 */
	public List<DictionaryTypeEnum> getAllEnum() {
		List<DictionaryTypeEnum> list = new ArrayList<DictionaryTypeEnum>();
		for (DictionaryTypeEnum cache : values()) {
			list.add(cache);
		}
		return list;
	}
	
	/**
	 * 获取全部枚举值
	 * 
	 * @return List<String>
	 */
	public List<String> getAllEnumAlis() {
		List<String> list = new ArrayList<String>();
		for (DictionaryTypeEnum cache : values()) {
			list.add(cache.getAlias());
		}
		return list;
	}
	
}
