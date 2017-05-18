package com.xbongbong.dingxbb.enums;

import java.util.ArrayList;
import java.util.List;

public enum RedundantEnum {
	
	CUSTOMER(1, "客户"),
	SALES_OPPORTUNITY(2, "销售机会"),
	CONTRACT(3,"合同");
	
	/** 枚举值 -- */
	private final int code;
	/** 枚举信息--中文名称 */
	private final String  name;
	
	/**
	 * 构造一个<code>PushClassificationEnum</code>枚举对象
	 * @param code
	 * @param name
	 */
	private RedundantEnum(int code, String name) {
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
	 * 获取全部枚举
	 * 
	 * @return List<WxErrorCodeEnum>
	 */
	public List<RedundantEnum> getAllEnum() {
		List<RedundantEnum> list = new ArrayList<RedundantEnum>();
		for (RedundantEnum cache : values()) {
			list.add(cache);
		}
		return list;
	}
}
