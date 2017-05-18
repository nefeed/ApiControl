 
package com.xbongbong.dingxbb.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xbongbong.dingxbb.dao.CompanyConfigDao;
import com.xbongbong.dingxbb.entity.CompanyConfigEntity;
import com.xbongbong.dingxbb.enums.CompanyConfigEnum;
import com.xbongbong.util.DateUtil;

 

@Component
public class CompanyConfigModel  implements IModel{

	@Autowired
	private CompanyConfigDao dao;
	
	public Integer insert(Object entity){
		Integer now =DateUtil.getInt();
    	((CompanyConfigEntity)entity).setAddTime(now);
    	((CompanyConfigEntity)entity).setUpdateTime(now);
		
		return dao.insert((CompanyConfigEntity)entity);
	}

	public Integer update(Object entity){
		((CompanyConfigEntity)entity).setUpdateTime(DateUtil.getInt());
		
		return dao.update((CompanyConfigEntity)entity);
	}
	
	public Integer save(Object entity){
		
		if(((CompanyConfigEntity)entity).getId() == null || ((CompanyConfigEntity)entity).getId().equals(0)) {
			return dao.insert((CompanyConfigEntity)entity);
		}
		return dao.update((CompanyConfigEntity)entity);
	}

	 
	public Integer deleteByKey( Integer key, String corpid){
		return dao.deleteByKey(key, corpid);
	}
	
	public CompanyConfigEntity getByKey( Integer key, String corpid){
		return dao.getByKey(key, corpid);
	}
	 
	public List<CompanyConfigEntity> findEntitys(Map<String ,Object>  param){
		return dao.findEntitys(param);
	}
	public Integer getEntitysCount(Map<String ,Object>  param){
		return dao.getEntitysCount(param);
	}
	
	/**
	 * 获取某个配置的参数值
	 * @param corpid
	 * @param configAlias
	 * @return
	 * kaka
	 * 2017年2月24日 下午3:15:33
	 */
	public String getStringConfig(String corpid, String configAlias) {
		String value = null;
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("corpid", corpid);
		param.put("del", 0);
		param.put("configAlias", configAlias);
		param.put("start", 0);
		param.put("pageNum", 1);
		
		List<CompanyConfigEntity> configList = findEntitys(param);
		if (configList != null && configList.size() > 0) {
			CompanyConfigEntity config = configList.get(0);
			if(config != null){
				value = config.getConfigValue();
			}
		}
		
		return value;
	}
	
	/**
	 * 获取某项1,0类型数据
	 * @param corpid
	 * @param configAlias
	 * @return
	 * @author kaka
	 * @time 2016-10-19 14:04:15
	 */
	public Boolean getBooleanConfig(String corpid, String configAlias) {
		
		String value = null;
		boolean flag = true;
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("corpid", corpid);
		param.put("del", 0);
		param.put("configAlias", configAlias);
		param.put("start", 0);
		param.put("pageNum", 1);
		
		List<CompanyConfigEntity> configList = findEntitys(param);
		if (configList != null && configList.size() > 0) {
			CompanyConfigEntity config = configList.get(0);
			if(config != null){
				value = config.getConfigValue();
			}
		}
		
		if(value != null && value.equals("0")){
			flag = false;
		}else{
			flag = true;
		}
		
		return flag;
	}

	/**
	 * 获取当前公司的客户名称判重规则设置
	 * @param corpid
	 * @return
	 * @author zheng.li
	 * @time 2016-9-10 上午9:23:15
	 */
	public Boolean getCustomerNameRule(String corpid) {
		
		//该公司客户名称判重的值
		String nameCheckValue=null;
		boolean isNameCheck=true;
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("corpid", corpid);
		param.put("del", 0);
		param.put("configAlias",CompanyConfigEnum.CUSTOMER_NAME_CHECK_SET.getAlias());
		
		List<CompanyConfigEntity> nameCheckList = findEntitys(param);
		if (nameCheckList != null && nameCheckList.size() > 0) {
			CompanyConfigEntity nameCheck = nameCheckList.get(0);
			if(nameCheck!=null){
				nameCheckValue=nameCheck.getConfigValue();
			}
		}
		
		if(nameCheckValue!=null && nameCheckValue.equals("0")){
			isNameCheck=false;
		}else{
			isNameCheck=true;
		}
		
		return isNameCheck;
		
	}
	
	/**
	 * 获取当前公司的客户电话判重规则设置
	 * @param corpid
	 * @return
	 * @author zheng.li
	 * @time 2016-9-10 上午9:23:15
	 */
	public Boolean getCustomerPhoneRule(String corpid){
		
		//该公司客户电话判重的值
		String phoneCheckValue=null;
		boolean isPhoneCheck=true;
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("corpid", corpid);
		param.put("del", 0);
		param.put("configAlias", CompanyConfigEnum.CUSTOMER_PHONE_CHECK_SET.getAlias());
		
		List<CompanyConfigEntity> phoneCheckList = findEntitys(param);
		if (phoneCheckList != null && phoneCheckList.size() > 0) {
			CompanyConfigEntity phoneCheck = phoneCheckList.get(0);
			if(phoneCheck!=null){
				phoneCheckValue=phoneCheck.getConfigValue();
			}
		}
		
		if(phoneCheckValue!=null && phoneCheckValue.equals("0")){
			isPhoneCheck=false;
		}else{
			isPhoneCheck=true;
		}
		
		return isPhoneCheck;

	}
	
	/**
	 * 获取配置，数据库没有(说明公司还未设置)，则初始化(插入一条默认配置)
	 * @param corpid
	 * @param configAlias 通过枚举获取 比如 CompanyConfigEnum.PERFORMANCE_SET.getAlias()，CompanyConfigEnum.PERFORMANCE_TYPES.getAlias()
	 * @param name
	 * @param configValue 配置默认值，比如“移动端设置个人业绩目标权限”默认启用、“目标配置”默认启用全部指标
	 * @return
	 * @author chuanpeng.zhang
	 */
	public CompanyConfigEntity initConfigEntity(String corpid, String configAlias, String name, String configValue) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("corpid", corpid);
		param.put("del", 0);
		param.put("configAlias", configAlias);
		
		List<CompanyConfigEntity> list = findEntitys(param);
		if (list != null && list.size() > 0) {
			//数据库有该条配置，则获取
			return list.get(0);
		}else {//如果数据库还没有，说明该公司还未设置，则插入一条默认配置
			CompanyConfigEntity configEntity = new CompanyConfigEntity();
			
			Integer now = DateUtil.getInt();
			
			configEntity.setCorpid(corpid);
			configEntity.setConfigName(name);
			configEntity.setConfigAlias(configAlias);
			configEntity.setConfigValue(configValue);
			configEntity.setAddTime(now);
			configEntity.setUpdateTime(now);
			configEntity.setDel(0);
			
			insert(configEntity);
			
			return configEntity;
		}
	}
	
	/**
	 * 某个规则是否已经存在
	 * 
	 * @param corpid
	 * @param configAlias
	 * @param name
	 * @param configValue
	 * @return
	 * @author hugj
	 * @time 2016年9月10日 下午6:27:21
	 */
	public boolean hasBeenSet(String corpid, String configAlias, String name, String configValue) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("corpid", corpid);
		param.put("del", 0);
		param.put("configAlias", configAlias);
		
		List<CompanyConfigEntity> list = findEntitys(param);
		return list != null && list.size() > 0;
	}

}

 