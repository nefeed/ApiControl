package com.xbongbong.dingxbb.helper;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;

public class FastJsonHelper {
	
	/**
	 * 解析并返回传过来字段对应的json格式数据
	 * @param clazz  类型实体   UserEntity.class
	 * @param str    需要解析并返回的字段   str = {"id", "userId", "corpid","name"}
	 * @param object 查找数据库后返回内容（可以是单个实体或者集合类型）  UserEntity , UserList
	 * @return
	 */
	public static Object parseIncludeObject(Class<?> clazz,String[] str,Object object){
		SimplePropertyPreFilter filter = new SimplePropertyPreFilter(clazz,str);

		if(object instanceof Collection<?>){
			return JSON.parseArray(JSON.toJSONString(object,filter));
		}else{
			return JSON.parseObject(JSON.toJSONString(object,filter));
		}
	}
	
	/**
	 * 解析并返回排除传过来字段的其他字段对应的json格式数据
	 * @param fields   fields = {"id", "userId", "corpid","name"}
	 * @param object   查找数据库后返回内容（可以是单个实体或者集合类型） UserEntity , UserList
	 * @return
	 */
	public static SerializeWriter parseExcludeObject(final String[] fields, Object object){
		
		final List<String> list = Arrays.asList(fields);
		
        PropertyFilter filter = new PropertyFilter() {
	        //过滤不需要的字段
	        public boolean apply(Object source, String name, Object value) {
        	   if(list.contains(name)){
	                return false;
	           }
        	        return true;
	        }
		};
	    
		SerializeWriter sw = new SerializeWriter();
	    JSONSerializer serializer = new JSONSerializer(sw);
	    serializer.getPropertyFilters().add(filter);
	    serializer.write(object);
	    
	    return sw;
	}
	
	/**
	 * 解析并返回排除传过来字段的其他字段对应的json格式数据
	 * @param fields   fields = {"id", "userId", "corpid","name"}
	 * @param object   查找数据库后返回内容（可以是单个实体或者集合类型） UserEntity , UserList
	 * @return
	 */
	public static SerializeWriter parseExcludeObject(final List<String> fields, Object object){
		
        PropertyFilter filter = new PropertyFilter() {
	        //过滤不需要的字段
	        public boolean apply(Object source, String name, Object value) {
        	   if(fields.contains(name)){
	                return false;
	           }
        	        return true;
	        }
		};
	    
		SerializeWriter sw = new SerializeWriter();
	    JSONSerializer serializer = new JSONSerializer(sw);
	    serializer.getPropertyFilters().add(filter);
	    serializer.write(object);
	    
	    return sw;
	}
}
