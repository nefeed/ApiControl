package com.xbongbong.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectHelper {
	/**
	 * 对应有参数的方法  比如entity的set方法setAttr1(value)
	 * @param object
	 * @param key 传方法名 如“setAttr1” “setAttr2”
	 * @param value 方法key的参数
	 */
	
	public static boolean valueSet(Object object, String key, String value) {
		try {
			Method method = object.getClass().getDeclaredMethod(JavaBeansUtil.getSetterMethodName(key),
					String.class);
			method.setAccessible(true); // 因为写成private 所以这里必须设置
			method.invoke(object, value);
			return true;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 对应get方法
	 * @param obj
	 * @param key 传属性名 如“attr1” “attr2”
	 * @return
	 */
	public static Object valueGet(Object obj, String key){
        if (obj == null) return null;
        
		try {  
			Field[] fields = obj.getClass().getDeclaredFields();
			Field field = null;
			for (int i = 0; i < fields.length; i++) {
//				System.out.println("fields[i].getName():" + fields[i].getName());
				field = fields[i];
				field.setAccessible(true);// 修改访问权限
				if (key.equals(field.getName())) {
//					System.out.println(field.getName() + ":" + field.get(obj));// 读取属性值
					return field.get(obj);
				}
			}
			
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 判断是否存在名为key的成员变量
	 * @param obj
	 * @param key 传属性名 如“attr1” “attr2”
	 * @return true or false
	 */
	public static boolean exist(Object obj, String key){
        if (obj == null) return false;
        
		try {  
			Field[] fields = obj.getClass().getDeclaredFields();
			Field field = null;
			for (int i = 0; i < fields.length; i++) {
//				System.out.println("fields[i].getName():" + fields[i].getName());
				field = fields[i];
				field.setAccessible(true);// 修改访问权限
				if (key.equals(field.getName())) {
//					System.out.println(field.getName() + ":" + field.get(obj));// 读取属性值
					return true;
				}
			}
			
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		
		return false;
	}
}
