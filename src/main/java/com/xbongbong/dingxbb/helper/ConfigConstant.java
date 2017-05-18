package com.xbongbong.dingxbb.helper;

import com.xbongbong.util.DefaultConfig;

public class ConfigConstant {
	
  	public static final String projectName = DefaultConfig.getValue("projectName");					//项目根路径
  	
  	public static final String CONCURRENT_LOCK_CUSTOMER_ADD = "conLockCustomerAdd";					//客户添加并发锁前缀
  	
  	/**
  	 * 缓存相关
  	 */
  	public static final String redisCacheHost = DefaultConfig.getValue("redisCacheHost");					//缓存系统所以在主机
  	
  	public static String getProjectName() {
  		return projectName;
  	}
  	
  	public static String getRedisCacheHost() {
  		return redisCacheHost;
  	}
}
