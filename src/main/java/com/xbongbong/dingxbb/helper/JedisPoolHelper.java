package com.xbongbong.dingxbb.helper;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;


public class JedisPoolHelper {
	
	private static JedisPoolHelper instance = new JedisPoolHelper();
	
	//TODO 线上暂时用注释掉的这个pool
//	private static JedisPool pool = new JedisPool(new JedisPoolConfig(), ConfigConstant.getRedisCacheHost());
	private static JedisPool pool = new JedisPool(new JedisPoolConfig(), ConfigConstant.getRedisCacheHost(),
			Protocol.DEFAULT_PORT, Protocol.DEFAULT_TIMEOUT, "mUWPMPpWyv8I069o");
	private JedisPoolHelper() {

	}
	
	public static JedisPoolHelper getInstance() {
		return instance;
	}
	
	public JedisPool getJedisPool() {
		return pool;
	}
}
