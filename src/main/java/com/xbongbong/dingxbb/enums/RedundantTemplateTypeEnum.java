package com.xbongbong.dingxbb.enums;

import java.util.ArrayList;
import java.util.List;

public enum RedundantTemplateTypeEnum {

	CUSTOMER(1 , "客户"),
	SALES_OPPORTUNITY(2 , "销售机会"),
	CONTRACT(3 , "合同订单"),
	CONTACT(4 , "联系人"),
	PRODUCT(5 , "产品");

	/** 枚举值 -- */
	private final int		code;
	
	/** 枚举信息--中文名称 */
	private final String    name;

	/**
	 * 构造一个<code>RedundantTemplateTypeEnum</code>枚举对象
	 *
	 * @param code
	 * @param name
	 * @param alias
	 */
	private RedundantTemplateTypeEnum(int code, String name) {
		this.code = code;
		this.name = name;
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
	 * 通过枚举<code>code</code>获得枚举
	 *
	 * @param alias
	 * @return RedundantTemplateTypeEnum
	 */
	public static RedundantTemplateTypeEnum getByCode(Integer code) {
		for (RedundantTemplateTypeEnum cacheCode : values()) {
			if (code.equals(cacheCode.getCode())) {
				return cacheCode;
			}
		}
		return null;
	}
	
	/**
	 * 获取全部枚举
	 * 
	 * @return List<RedundantTemplateTypeEnum>
	 */
	public List<RedundantTemplateTypeEnum> getAllEnum() {
		List<RedundantTemplateTypeEnum> list = new ArrayList<RedundantTemplateTypeEnum>();
		for (RedundantTemplateTypeEnum cache : values()) {
			list.add(cache);
		}
		return list;
	}
	
	/**
	 * 获取全部枚举code
	 * @return
	 */
	public static List<Integer> getAllEnumCode() {
		List<Integer> list = new ArrayList<Integer>();
		for (RedundantTemplateTypeEnum cache : values()) {
			list.add(cache.getCode());
		}
		return list;
	}
	
}
