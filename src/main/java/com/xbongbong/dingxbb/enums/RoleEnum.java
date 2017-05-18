package com.xbongbong.dingxbb.enums;

import java.util.ArrayList;
import java.util.List;

public enum RoleEnum {
	
	ADMIN(1, "超级管理员", "|1|"),
	BOSS(2, "老板", "|2|"),
	MANAGER(3, "主管", "|3|"),
	SALE(4, "销售", "|4|"),
	ASSISTANT(5, "助理", "|5|");
	
	/** 枚举值 -- 角色id*/
	private final int		code;
	
	/** 枚举信息--角色中文名称 */
	private final String    name;
	
	/** 枚举描述 --角色使用时状态*/
	private final String	alias;
	
	/**
	 * 构造一个<code>SubModuleEnum</code>枚举对象
	 *
	 * @param code
	 * @param name
	 * @param alias
	 */
	private RoleEnum(int code, String name,String alias) {
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
	public static RoleEnum getByAlias(String alias) {
		for (RoleEnum cacheCode : values()) {
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
	public static RoleEnum getByCode(Integer code) {
		for (RoleEnum cacheCode : values()) {
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
	public List<RoleEnum> getAllEnum() {
		List<RoleEnum> list = new ArrayList<RoleEnum>();
		for (RoleEnum cache : values()) {
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
		for (RoleEnum cache : values()) {
			list.add(cache.getAlias());
		}
		return list;
	}
	
}
