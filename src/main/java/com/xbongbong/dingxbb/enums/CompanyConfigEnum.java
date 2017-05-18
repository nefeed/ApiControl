package com.xbongbong.dingxbb.enums;

import java.util.ArrayList;
import java.util.List;

public enum CompanyConfigEnum {
	
	//是否允许员工自己修改业绩指标  configValue 0否 1是
	PERFORMANCE_SET("performanceSet", "是否允许移动端设置业绩目标"), 
	//所有业绩目标类型，比如 1合同金额，2回款金额，3新增合同数，4产品销量，5新增客户数，6新增联系人数，7跟进次数，8收益利润--configValue用|隔开 比如|1|2|代表只有1和2
	PERFORMANCE_TYPES("performanceTypes", "目标配置"),
	//是否启用多币种  configValue 0否 1是
	MULTI_CURRENCY("multiCurrency", "多币种"),
	// 设置客户查重规则：按照客户名称查重
	CUSTOMER_NAME_CHECK_SET("customerNameCheck", "客户名称判重是否开启"),
	// 设置客户查重规则：按照客户电话查重
	CUSTOMER_PHONE_CHECK_SET("customerPhoneCheck", "客户电话判重是否开启"),
	// 用户自定义电话号码的判断规则：是否必须是数字或可以有特殊字符
	CUS_PHONE_RULE_SET("cusPhoneRule", "用户自定义电话号码的判断规则"),
	// 合同业绩分配：
	CONTRACT_PERFORMANCE_RULE_SET("contractPerformanceRule", "合同业绩分配是否开启"),
	;
	
	/** 枚举描述 --英文名称*/
	private final String	alias;
	
	/** 枚举信息--中文名称 */
	private final String    name;
	
	/**
	 * 构造一个<code>CompanyConfigEnum</code>枚举对象
	 *
	 * @param code
	 * @param name
	 * @param alias
	 */
	private CompanyConfigEnum(String alias, String name) {
		this.alias = alias;
		this.name = name;
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
	 * 根据alias获取枚举
	 * @return
	 */
	public static CompanyConfigEnum getByAlias(String alias) {
		for (CompanyConfigEnum cache : values()) {
			if (alias.equals(cache.getAlias())) {
				return cache;
			}
		}
		return null;
	}

	/**
	 * 获取全部枚举
	 * 
	 * @return List<CompanyConfigEnum>
	 */
	public static List<CompanyConfigEnum> getAllEnum() {
		List<CompanyConfigEnum> list = new ArrayList<CompanyConfigEnum>();
		for (CompanyConfigEnum cache : values()) {
			list.add(cache);
		}
		return list;
	}
	
	/**
	 * 获取全部枚举值
	 * 
	 * @return List<String>
	 */
	public static List<String> getAllEnumAlis() {
		List<String> list = new ArrayList<String>();
		for (CompanyConfigEnum cache : values()) {
			list.add(cache.getAlias());
		}
		return list;
	}
	
	/**
	 * 获取全部枚举name
	 * @return List<String>
	 */
	public static List<String> getAllEnumName() {
		List<String> list = new ArrayList<String>();
		for (CompanyConfigEnum cache : values()) {
			list.add(cache.getName());
		}
		return list;
	}
}
