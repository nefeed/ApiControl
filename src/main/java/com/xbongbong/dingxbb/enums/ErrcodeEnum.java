package com.xbongbong.dingxbb.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 错误码枚举
 * 错误码规则 ：1.第一位为大分类，1标示系统错误，2为业务错误; 
 *         2.第二，三位为模块标示，系统错误对应位为00 01为客户，02为联系人，03为合同，04为产品,05是模板相关
 *         3.第四，五，六位为具体错误标示
 * 如  201001 标示业务错误，客户错误中的001错误
 * 
 * 目前英文描述都空置，后面有需要再补，暂时用不到
 * 
 * @author kaka
 */
public enum ErrcodeEnum {
	
	API_ERROR_100001(100001, "接口请求方式错误，本接口只支持post请求", ""),
	
	API_ERROR_100002(100002, "缺少corpid", ""),
	
	API_ERROR_100003(100003, "签名验证失败", ""),
	
	API_ERROR_100004(100004, "服务繁忙或服务端错误", ""),//可能是数据库插入错误
	
	API_ERROR_100005(100005, "必填参数缺失,提示信息会应不同参数缺失变化", ""),
	
	API_ERROR_100006(100006, "data参数不能为空", ""),
	
	API_ERROR_100007(100007, "data不是合法的json字符串，无法解析为JSONObject", ""),
	
	API_ERROR_100008(100008, "corpid错误或该公司缺少api_token请在后台设置", ""),
	
	API_ERROR_100009(100009, "接口访问太频繁，请稍后再访问", ""),
	
	API_ERROR_100010(100010, "目前接口只能提供给VIP用户及试用期内用户调用", ""),
	
	API_ERROR_100011(100011, "接口访问太频繁，请稍后再访问", ""),//与100009不同，表示新建相同数据同时多次提交
	
	API_ERROR_201001(201001, "客户信息不能为空", ""),
	
	API_ERROR_201002(201002, "客户实体解析失败", ""),
	
	API_ERROR_201003(201003, "客户名称不能为空", ""),
	
	API_ERROR_201004(201004, "客户“客户电话”字段不能为空", ""),
	
	API_ERROR_201005(201005, "根据客户查重规则，已存在该客户或客户电话，无法新建该客户", ""),
	
	API_ERROR_201006(201006, "客户ID不能为空", ""),
	
	API_ERROR_201007(201007, "客户不存在", ""),
	
	API_ERROR_201008(201008, "该客户已经被逻辑删除", ""),
	
	API_ERROR_201009(201009, "该客户有关联合同不能删除", ""),
	
	API_ERROR_201010(201010, "客户“客户电话”格式不正确，含有不符合格式的字符", ""),
	
	API_ERROR_201011(201011, "经度参数错误", ""),
	
	API_ERROR_201012(201012, "纬度参数错误", ""),

	API_ERROR_201013(201013, "协同人姓名不能为空", ""),

	API_ERROR_201014(201014, "协同人不存在", ""),

	API_ERROR_201015(201015, "协同人姓名填写错误", ""),

	API_ERROR_201016(201016, "协同人已存在", ""),
	
	API_ERROR_202001(202001, "联系人信息不能为空", ""),
	
	API_ERROR_202002(202002, "联系人实体解析失败", ""),
	
	API_ERROR_202003(202003, "联系人“联系电话”字段不能为空", ""),
	
	API_ERROR_202004(202004, "已存在该电话对应的客户联系人，无法新建该联系人", ""),
		
	API_ERROR_202005(202005, "联系人ID不能为空", ""),
	
	API_ERROR_202006(202006, "联系人不存在", ""),
	
	API_ERROR_202007(202007, "该联系人已经被删除", ""),
	
	API_ERROR_202008(202008, "联系人“联系电话”格式不正确，含有不符合格式的字符", ""),
	
	API_ERROR_203001(203001, "合同信息不能为空", ""),
	
	API_ERROR_203002(203002, "合同实体解析失败", ""),
	
	API_ERROR_203003(203003, "该合同编号对应的合同订单编号已存在，可能在合同列表中或合同审批中", ""),
		
	API_ERROR_203004(203004, "合同ID不能为空", ""),
	
	API_ERROR_203005(203005, "合同不存在", ""),
	
	API_ERROR_203006(203006, "合同产品数量 不正确", ""),

	API_ERROR_203007(203007, "试图操作的合同关联产品数据不存在", ""),
	
	API_ERROR_203008(203008, "合同编号不能为空", ""),
	
	API_ERROR_203009(203009, "该合同已经被删除", ""),
	
	API_ERROR_203010(203010, "回款计划信息不能为空", ""),
	
	API_ERROR_203011(203011, "回款计划实体解析失败", ""),
	
	API_ERROR_203012(203012, "回款计划编号已存在", ""),
	
	API_ERROR_203013(203013, "回款计划ID不能为空", ""),
	
	API_ERROR_203014(203014, "回款计划不存在", ""),
	
	API_ERROR_203015(203015, "该回款计划已经被删除", ""),
	
	API_ERROR_204001(204001, "产品ID不能为空", ""),
	
	API_ERROR_204002(204002, "产品ID对应的产品不存在", ""),
	
	API_ERROR_204003(204003, "产品信息不能为空", ""),
	
	API_ERROR_204004(204004, "产品实体解析失败", ""),
	
	API_ERROR_204005(204005, "产品编号不能为空", ""),
	
	API_ERROR_204006(204006, "产品编号已存在", ""),
	
	API_ERROR_204007(204007, "产品分类名称不能为空", ""),
	
	API_ERROR_204008(204008, "产品父分类不存在", ""),
	
	API_ERROR_204009(204009, "不能建立三级产品分类", ""),
	
	API_ERROR_204010(204010, "该产品分类名称已存在", ""),
	
	API_ERROR_204011(204011, "产品分类ID不能为空", ""),
	
	API_ERROR_204012(204012, "产品分类不存在", ""),
	
	API_ERROR_204013(204013, "该产品已经被删除", ""),
	
	API_ERROR_204014(204014, "该产品分类下还有子分类，请先删除子分类再删除该分类", ""),
	
	API_ERROR_204015(204015, "该产品分类下还有关联产品，不能删除该分类", ""),
	
	API_ERROR_205001(205001, "refType不能为空或不合法", "");
	
	
	/** 枚举值--错误码-- */
	private final int		code;
	
	/** 错误信息--中文描述 */
	private final String    msg;
	
	/** 错误信息--英文描述*/
	private final String	alias;
	
	/**
	 * 构造一个<code>ErrcodeEnum</code>枚举对象
	 *
	 * @param code
	 * @param msg
	 * @param alias
	 */
	private ErrcodeEnum(int code, String msg,String alias) {
		this.code = code;
		this.msg = msg;
		this.alias = alias;
	}
	
	/**
	 * @return Returns the code.
	 */
	public int getCode() {
		return code;
	}
	
	/**
	 * @return Returns the msg.
	 */
	public String getMsg() {
		return msg;
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
	public static ErrcodeEnum getByAlias(String alias) {
		for (ErrcodeEnum cacheCode : values()) {
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
	public static ErrcodeEnum getByCode(Integer code) {
		for (ErrcodeEnum cacheCode : values()) {
			if (code.equals(cacheCode.getCode())) {
				return cacheCode;
			}
		}
		return null;
	}
	
	/**
	 * 获取全部枚举
	 * 
	 * @return List<ErrcodeEnum>
	 */
	public List<ErrcodeEnum> getAllEnum() {
		List<ErrcodeEnum> list = new ArrayList<ErrcodeEnum>();
		for (ErrcodeEnum cache : values()) {
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
		for (ErrcodeEnum cache : values()) {
			list.add(cache.getAlias());
		}
		return list;
	}
}
