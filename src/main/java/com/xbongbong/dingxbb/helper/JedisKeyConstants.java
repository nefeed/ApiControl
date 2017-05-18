package com.xbongbong.dingxbb.helper;


public class JedisKeyConstants {
	
	//项目缓存键前缀
	public static final String cachePrefix = ConfigConstant.getProjectName() + ":";
	
	//更任务缓存相关的key
	public static final String TASK_AVATAR_KEY = cachePrefix + "staffAvatar:staffId:";
	public static final String TASK_AVATAR_FIELD = "staffAvatar";
	
	//更新某个下属的所有上级的缓存
	public static final String SUB_STAFF_HASHKEY= cachePrefix + "subStaffsHash:";
	
	//更新user缓存
	public static final String USER_HASHKEY= cachePrefix + ":userHash";
	public static final String ID_FIELD = "id";
	public static final String M_FIELD = "m";
	
	//更任务缓存相关的key
	public static String getHashKey(Integer staffId){
		return TASK_AVATAR_KEY + staffId;
	}
	
	//更新某个下属的所有上级的缓存
	public static String getHashKeyOfSubStaff(Integer staffId){
		return SUB_STAFF_HASHKEY + staffId;
	}
	
	//更新user缓存
	public static String getHashField(Integer userId){
		return ID_FIELD + userId;
	}
	public static String getHashField(String mobile){
		return M_FIELD + mobile;
	}
}
