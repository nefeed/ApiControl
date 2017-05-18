package com.xbongbong.dingxbb.helper;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.xbongbong.util.CookieUtil;
import com.xbongbong.util.SerializeUtil;
import com.xbongbong.util.StringUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 实现与本目录FileUtils对应的方法,替换FileUtils
 * 将钉钉用到的accesstoken, tickect,jsticket,permanent_code存到redis缓存服务器
 * 原来的FileUtils是存本服务器，无法满足多机部署和负载均衡的需求
 * @author kaka
 *
 */
public class JedisUtils {
	
	private static final Logger LOG = LogManager.getLogger(JedisUtils.class);
	
	//存在redis中的session信息的有效时间，单位秒
	private static final Integer SESSION_EFFECTIVE_TIME = 30*60;
	//一分钟
	private static final Integer ONE_MINUTE = 60;
	//并发锁时间
	private static final Integer CONCURRENT_LOCK_TIME = 3;
	
	//redis key项目前缀
	private static final String cachePrefix = ConfigConstant.getProjectName() + ":"; 
	//jedis对象池
	private static JedisPool pool = JedisPoolHelper.getInstance().getJedisPool();

	/**
	 * redis处理自增值，用于一段时间内限制频次的场景
	 * @param request
	 * @param response
	 * @param key
	 * @param delaySeconds
	 * @return
	 */
	public synchronized static Long getIncrNum(HttpServletRequest request, HttpServletResponse response, String key, int delaySeconds) {
		String sessionId = "";
		Cookie sessionIdCookie = CookieUtil.getCookie(request, "JSESSIONID");
		if(sessionIdCookie != null){
			sessionId = sessionIdCookie.getValue();
		}
		if(StringUtil.isEmpty(sessionId)){
			sessionId = StringUtil.getRandomPassword(4, 32);
			sessionIdCookie = new Cookie("JSESSIONID", sessionId);
			CookieUtil.addCookie(response, sessionIdCookie);
		}
		Jedis jedis = pool.getResource();
		String subUserHashKey = cachePrefix + sessionId + ":" + key;
		Long value = null;
		try {
			value = jedis.incr(subUserHashKey.getBytes());
			if(value == null || value <= 0) {
				return 0L;
			}
			if(value == 1) {//第一次设值处理设置对应的key的缓存时间
				jedis.expire(subUserHashKey.getBytes(), delaySeconds);
			}
		} catch (Exception e) {
			String errMsg = "Jedis取出" + subUserHashKey + "时出错";
			LOG.error(errMsg, e);
			throw e; //TODO 重新抛出， 后面再考虑怎么处理好
		} finally {
			if (null != jedis) { 
			   jedis.close();
			}
		}
		return value;
	}
	
	public synchronized static Long getIncrNum(HttpServletRequest request, HttpServletResponse response, String key) {
		return getIncrNum(request, response, key, ONE_MINUTE);
	}
	
	/**
	 * json写入文件
	 * @param filePrefix redis保存前缀,替代FileUtils中的FileName,
	 * 					 分别是accesstoken,tickect,jsticket,permanentcode
	 * @param key		redis key末尾标示key
	 * @param json		要存储的value
	 */
	public synchronized static void write2Redis(String filePrefix, String key, Object json) {
		Jedis jedis = pool.getResource();
		String subUserHashKey = cachePrefix + filePrefix + ":" + key;

		try {
			String jsonStr = null;
			if(json instanceof String) {
				jsonStr = (String)json;
			} else {
				jsonStr = JSON.toJSONString(json);
			}
			jedis.set(subUserHashKey.getBytes(), jsonStr.getBytes());
			
		} catch (Exception e) {
			String errMsg = "Jedis写入" + subUserHashKey + "时出错";
			LOG.error(errMsg, e);
		} finally {
			if (null != jedis) { 
			   jedis.close();
			}
		}
	}

	/**
	 * 通过key值获取文件中的value
	 * @param filePrefix redis保存前缀,替代FileUtils中的FileName,
	 * 					 分别是accesstoken,tickect,jsticket,permanentcode
	 * @param key		redis key末尾标示key
	 * @return
	 */
	public static String getValue(String filePrefix, String key) {
		Jedis jedis = pool.getResource();
		String subUserHashKey = cachePrefix + filePrefix + ":" + key;
		
		String value = null;
		try {
			byte[] valueBytes = jedis.get(subUserHashKey.getBytes());
			if(valueBytes == null || valueBytes.length <= 0) {
				return null;
			}
			value = new String(valueBytes);
		} catch (Exception e) {
			String errMsg = "Jedis取出" + subUserHashKey + "时出错";
			LOG.error(errMsg, e);
			throw e; //TODO 重新抛出， 后面再考虑怎么处理好
		} finally {
			if (null != jedis) { 
			   jedis.close();
			}
		}
		return value;
	}
	
	/**
	 * 获取session值
	 * @param request
	 * @param name1		session key
	 * @return
	 */
	public static Object getSessionValue(HttpServletRequest request,String name1, String name2){
		//
		String sessionId = "";
		Cookie sessionIdCookie = CookieUtil.getCookie(request, "JSESSIONID");
		if(sessionIdCookie != null){
			sessionId = sessionIdCookie.getValue();
		}
		if(StringUtil.isEmpty(sessionId)){
			return null;
		}
		Jedis jedis = pool.getResource();
		String subUserHashKey = cachePrefix + sessionId + ":" + name1;
		Object value = null;
		try {
			byte[] valueBytes = null;
			if(StringUtil.isEmpty(name2)){
				valueBytes = jedis.get(subUserHashKey.getBytes());
			}else{
				valueBytes = jedis.hget(subUserHashKey.getBytes(), name2.getBytes());
			}
			if(valueBytes == null || valueBytes.length <= 0) {
				return null;
			}
			
			value = SerializeUtil.unserialize(valueBytes);
			
			
		} catch (Exception e) {
			String errMsg = "Jedis取出" + subUserHashKey+ "--" + name2 + "时出错";
			LOG.error(errMsg, e);
			throw e; //TODO 重新抛出， 后面再考虑怎么处理好
		} finally {
			if (null != jedis) { 
			   jedis.close();
			}
		}
		return value;
	}
	
	/**
	 * 获取session值
	 * @param request
	 * @param name		session key
	 * @return
	 */
	public static Object getSessionValue(HttpServletRequest request,String name){
		return getSessionValue(request, name, null);
	}
	
	/**
	 * 设置session值
	 * @param request
	 * @param name		session key
	 * @param value		值 
	 * @return 是否存成功  true false
	 */
	public static boolean setSessionValue(HttpServletRequest request, HttpServletResponse response,String name, Object value){
		//
		String sessionId = "";
		Cookie sessionIdCookie = CookieUtil.getCookie(request, "JSESSIONID");
		if(sessionIdCookie != null){
			sessionId = sessionIdCookie.getValue();
		}
		if(StringUtil.isEmpty(sessionId)){
			sessionId = StringUtil.getRandomPassword(4, 32);
		 
			sessionIdCookie = new Cookie("JSESSIONID", sessionId);
			CookieUtil.addCookie(response, sessionIdCookie);
			//return false;
		}
		boolean retFlag = false;
		Jedis jedis = pool.getResource();
		String subUserHashKey = cachePrefix + sessionId + ":" + name;
		try {
			byte[] bytes = SerializeUtil.serialize(value);
//			jedis.set(subUserHashKey.getBytes(), bytes);
			jedis.setex(subUserHashKey.getBytes(), SESSION_EFFECTIVE_TIME, bytes);
			LOG.debug("---setSessionValue "+subUserHashKey);
			retFlag = true;
		} catch (Exception e) {
			String errMsg = "Jedis写入" + subUserHashKey + "时出错";
			LOG.error(errMsg, e);
		} finally {
			if (null != jedis) { 
			   jedis.close();
			}
		}
		return retFlag;
	}
	
	/**
	 * 查看jedisSession是否存在key
	 * @param request
	 * @param response
	 * @param key
	 * @return
	 */
	public static boolean sessionContainsKey(HttpServletRequest request, HttpServletResponse response,String key){
		return sessionContainsKey(request, response, key, null);
	}
	
	/**
	 * 查看jedisSession是否存在key1 key2
	 * @param request
	 * @param response
	 * @param key
	 * @return
	 */
	public static boolean sessionContainsKey(HttpServletRequest request, HttpServletResponse response,String key1, String key2){
		String sessionId = "";
		Cookie sessionIdCookie = CookieUtil.getCookie(request, "JSESSIONID");
		if(sessionIdCookie != null){
			sessionId = sessionIdCookie.getValue();
		}
		if(StringUtil.isEmpty(sessionId)){
			sessionId = StringUtil.getRandomPassword(4, 32);
		 
			sessionIdCookie = new Cookie("JSESSIONID", sessionId);
			CookieUtil.addCookie(response, sessionIdCookie);
			//return false;
		}
		String subUserHashKey = cachePrefix + sessionId + ":" + key1;
		if(containsKey(subUserHashKey,key2)){
			return true;
		}else{
			return false;
		}
	}
	
	
	
	
	/**
	 * 删除redissession值
	 * @param request
	 * @param name		session key
	 * @return
	 */
	public static Object delSessionValue(HttpServletRequest request,String name){
		//
		String sessionId = "";
		Cookie sessionIdCookie = CookieUtil.getCookie(request, "JSESSIONID");
		if(sessionIdCookie != null){
			sessionId = sessionIdCookie.getValue();
		}
		if(StringUtil.isEmpty(sessionId)){
			return null;
		}
		
		Jedis jedis = pool.getResource();
		String subUserHashKey = cachePrefix + sessionId + ":" + name;
		Object value = null;
		try {
			jedis.del(subUserHashKey.getBytes());
			value="";
		} catch (Exception e) {
			String errMsg = "Jedis删除" + subUserHashKey + "时出错";
			LOG.error(errMsg, e);
			//throw e; //TODO 重新抛出， 后面再考虑怎么处理好
		} finally {
			if (null != jedis) { 
			   jedis.close();
			}
		}
		return value;
	}
	
	/**
	 * 通过key remove相应的数据
	 * @param filePrefix
	 * @param key
	 * @return
	 */
	public static String removeValue(String filePrefix, String key) {
		Jedis jedis = pool.getResource();
		String subUserHashKey = cachePrefix + filePrefix + ":" + key;
		String value = null;
		try {
			byte[] valueBytes = jedis.get(subUserHashKey.getBytes());
			if(valueBytes == null || valueBytes.length <= 0) {
				return null;
			}
			value = new String(valueBytes);
			if(!StringUtil.isEmpty(value)) {
				jedis.del(subUserHashKey.getBytes());
			}
		} catch (Exception e) {
			String errMsg = "Jedis删除" + subUserHashKey + "时出错";
			LOG.error(errMsg, e);
			throw e; //TODO 重新抛出， 后面再考虑怎么处理好
		} finally {
			if (null != jedis) { 
			   jedis.close();
			}
		}
		return value;
	}
	public static boolean containsKey(String key1, String key2){
		Jedis jedis = pool.getResource();
		boolean flag = false;
		try {
			if(StringUtil.isEmpty(key2)){
				flag = jedis.hexists(key1, key2);
			}else{
				flag = jedis.exists(key1);
			}
		} catch (Exception e) {
			String errMsg = "Jedis执行 exists/hexists时出错";
			LOG.error(errMsg, e);
			throw e; //TODO 重新抛出， 后面再考虑怎么处理好
		} finally {
			if (null != jedis) { 
			   jedis.close();
			}
		}
		return flag;
	}
	
	/**
	 * 获取并发锁，若没有被占用则返回true并构建锁，若已被占用则返回false
	 * 目前的并发锁时间为3秒
	 * @param filePrefix
	 * @param key
	 * @return true 表示被正常，false表示被锁定
	 * kaka
	 * 2017年2月21日 下午4:02:47
	 */
	public static boolean checkConcurrentLock(String filePrefix, String key) {
		Jedis jedis = pool.getResource();
		String subUserHashKey = cachePrefix + filePrefix + ":" + key;
		
		String value = null;
		try {
			byte[] valueBytes = jedis.get(subUserHashKey.getBytes());
			if(valueBytes == null || valueBytes.length <= 0) {
				jedis.setex(subUserHashKey.getBytes(), CONCURRENT_LOCK_TIME, "lock".getBytes());
				return true;
			}
			value = new String(valueBytes);
		} catch (Exception e) {
			String errMsg = "Jedis取出" + subUserHashKey + "时出错";
			LOG.error(errMsg, e);
			throw e; //TODO 重新抛出， 后面再考虑怎么处理好
		} finally {
			if (null != jedis) { 
			   jedis.close();
			}
		}
		if(value.equals("lock")) {
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param sessionName	redis保存前缀,替代FileUtils中的FileName
	 * @param key			redis key末尾标示key
	 * @return
	 * @author chuanpeng.zhang
	 * @time 2016-10-8 下午4:20:38
	 */
	public static Object getJedisValue(String sessionName, String key){
		
		Jedis jedis = pool.getResource();
		String subUserHashKey = cachePrefix + sessionName + ":" + key;
		
		Object value = null;
		try {
			byte[] valueBytes = jedis.get(subUserHashKey.getBytes());
			if(valueBytes == null || valueBytes.length <= 0) {
				return null;
			}
			value = SerializeUtil.unserialize(valueBytes);
		} catch (Exception e) {
			String errMsg = "Jedis取出" + subUserHashKey + "时出错";
			LOG.error(errMsg, e);
			throw e; //TODO 重新抛出， 后面再考虑怎么处理好
		} finally {
			if (null != jedis) { 
				jedis.close();
			}
		}
		return value;
	}
	
	/**
	 * 
	 * @param sessionName	redis保存前缀,替代FileUtils中的FileName
	 * @param key			redis key末尾标示key
	 * @param value			值
	 * @param time
	 * @return
	 * @author chuanpeng.zhang
	 * @time 2016-10-8 下午4:00:34
	 */
	@SuppressWarnings("unused")
	public static boolean setJedisValue(String sessionName, String key, Object value, Integer time){
		
		boolean retFlag = false;
		Jedis jedis = pool.getResource();
		String subUserHashKey = cachePrefix + sessionName + ":" + key;
		
		try {
			byte[] bytes = SerializeUtil.serialize(value);
			
			if(time!=null && time > 0){//如果有时间传入，则传入时间
				jedis.setex(subUserHashKey.getBytes(), time, bytes);
			}else{
				jedis.setex(subUserHashKey.getBytes(), SESSION_EFFECTIVE_TIME, bytes);
			}
			
			LOG.debug("---setSessionValue "+subUserHashKey);
			retFlag = true;
		} catch (Exception e) {
			String errMsg = "Jedis写入" + subUserHashKey + "时出错";
			LOG.error(errMsg, e);
		} finally {
			if (null != jedis) { 
			   jedis.close();
			}
		}
		return retFlag;
	}
}
