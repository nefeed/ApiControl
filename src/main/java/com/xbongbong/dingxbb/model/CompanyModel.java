 
package com.xbongbong.dingxbb.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xbongbong.dingxbb.dao.CompanyDao;
import com.xbongbong.dingxbb.entity.CompanyEntity;
import com.xbongbong.util.DateUtil;

 

@Component
public class CompanyModel  implements IModel{

	@Autowired
	private CompanyDao dao;
	@Autowired
	private UserModel userModel;
	@Autowired
	private DepartmentModel departmentModel;
	
	private static final Logger LOG = LogManager.getLogger(CompanyModel.class);
	public Integer insert(Object entity){
		//插入套餐
		dao.insertFeeCompany((CompanyEntity)entity);
		return dao.insert((CompanyEntity)entity);
	}

	public Integer update(Object entity){
		return dao.update((CompanyEntity)entity);
	}
	
	public Integer deleteByKey( String key){
		return dao.deleteByKey(key);
	}
	
	public CompanyEntity getByKey( String key){
		return dao.getByKey(key);
	}
	/**
	 * 获取正常状态的公司
	 * @param key
	 * @return
	 */
	public CompanyEntity getNormalCompanyByKey( String key){
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("corpid", key);
		param.put("status", 1);
		param.put("del", 0);
		List<CompanyEntity> companyList = findEntitys(param);
		return companyList.size() > 0 ? companyList.get(0) : null;
	}
	
	/**
	 * 查出某个公司ID对应的公司数据，不管del字段是1还是0
	 * @param key
	 * @return
	 */
	public CompanyEntity getByKeyIgnoreDel (String key) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("corpid", key);
		
		List<CompanyEntity> list =  findEntitys(param);
		return list.size() > 0 ? list.get(0) : null;
	}
	
	public List<CompanyEntity> findEntitys(Map<String ,Object>  param){
		return dao.findEntitys(param);
	}
	public Integer getEntitysCount(Map<String ,Object>  param){
		return dao.getEntitysCount(param);
	}
	
	public Integer getEndTime(Map<String ,Object>  param){
		return dao.getEndTime(param);
	}

	public Object getByKey(String key, String corpid) {
		// TODO Auto-generated method stub
		return null;
	}

	public Integer deleteByKey(String key, String corpid) {
		// TODO Auto-generated method stub
		return null;
	}
	 
	
	public List<Map<String,Object>> findFeeCompanyEntitys(Map<String ,Object>  param){
		return dao.findFeeCompanyEntitys(param);
	}
	
	/**
	 * 获取公司使用人数
	 * @param corpid
	 * @return
	 * @author cp.zhang
	 * @time 2016-10-22 上午2:12:06
	 */
	public int getCorpRealUserNum(String corpid) {

		Map<String,Object> param=new HashMap<String,Object>();
		param.put("corpid", corpid);
		param.put("del", 0);
		Integer realUserNum=userModel.getEntitysCount(param);//公司人数
		
//		TODO 待优化
		Integer depNum = departmentModel.getEntitysCount(param);
		
		if (depNum > 100) {
			realUserNum = 1001;
		}
		return realUserNum;
	}
	
	
	/**
	 *   判断公司是否vip
	 * @param corpId
	 * @param corpUserNum
	 * @param modelMap
	 * @author zheng.li
	 * @return
	 */
	public int isVip(String corpid ,Map<String,Object> modelMap){
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("corpid", corpid);
		param.put("negDel", 1);
		Integer corpUserNum=userModel.getEntitysCount(param);//公司人数
		
		param.clear();
		param.put("corpid", corpid);
		param.put("del", 0);
		Integer realUserNum=userModel.getEntitysCount(param);//使用人数
		return isVip(  corpid,  corpUserNum,  realUserNum, modelMap);
	}
	
	/**
	 *   判断公司是否vip
	 * @param corpId
	 * @param corpUserNum
	 * @param modelMap
	 * @author zheng.li
	 * @return
	 */
	public int isVip(String corpId,Integer corpUserNum,Integer realUserNum,Map<String,Object> modelMap){
		Integer isVip=0;//默认非VIP
		Integer isActive=1;//是否有效，1有效，0禁用
		Integer isCookie=0;//是否存cookie
		Integer WRANSTART=7;//提前七天提醒即将过期
		//Integer WARNEND = 14;// 超期后提醒14天
		String showMsg="free";
		String corpName="";//公司名称
		final Integer OCTOBERFIRST=1475251200;//2016年 10月1日  秒数
		
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("corpid", corpId);
		param.put("del", 0);
		param.put("orderByStr", "id  ");
		list=dao.findFeeCompanyEntitys(param);
		CompanyEntity companyEntity=dao.getByKey(corpId);
		if(companyEntity==null){
			isVip=0;
			modelMap.put("isActive", 0);
			showMsg="系统错误，公司不存在！";
			modelMap.put("showMsg", showMsg);
			return isVip;
		}
		Integer companyAddTime=companyEntity.getAddTime();
		if(list.isEmpty()){
			modelMap.put("corpUserNum", corpUserNum);
			modelMap.put("isVip",0);
			if(companyAddTime<=OCTOBERFIRST){   //10月1日之前注册的公司,总人数19人及以下或者5人及以下免费使用
				if(corpUserNum >19 && realUserNum>5){
					modelMap.put("isActive", 0);
					showMsg= "感谢贵公司试用销帮帮CRM。贵公司("+corpName+")目前总人数为"+ corpUserNum
							+ "人，实际使用人数为" + realUserNum + "人，若需要继续使用我们的软件服务，请务必付费购买VIP版本。给您带来不便，敬请谅解！";
					modelMap.put("showMsg", showMsg);
				}else {
					modelMap.put("isActive", 1);
					modelMap.put("showMsg", "free");
				}
			}else{   //10月1日之后注册的公司,实际人数5人以下免费使用
				if(realUserNum >5){
					modelMap.put("isActive", 0);
					showMsg= "感谢贵公司试用销帮帮CRM。贵公司("+corpName+")目前总人数为"+ corpUserNum
							+ "人，实际使用人数为" + realUserNum + "人，若需要继续使用我们的软件服务，请务必付费购买VIP版本。给您带来不便，敬请谅解！";
					modelMap.put("showMsg", showMsg);
				}else {
					modelMap.put("isActive", 1);
					modelMap.put("showMsg", "free");
				}
			}
			
			//
			modelMap.put("corpName", corpName);
			modelMap.put("unpayCount",0);
			modelMap.put("payCount",0);
			return isVip;
		}
		LOG.debug("nowTime3:"+System.currentTimeMillis()+"  corpid:"+corpId);
		Map<String, Object> resultMap=list.get(0);
		corpName=(String) resultMap.get("corp_name");
		Integer feeType=0;//套餐类型
		String feeName=null;
		Integer startTime=0;//=(Integer)resultMap.get("start_time");//套餐开始时间
		Integer endTime=0;//=(Integer)resultMap.get("end_time");//套餐结束时间
		int today=DateUtil.getTodayInt()+86400;
		int userNumTotal=0;//套餐许可使用人数
		int payCount=0;//已支付套餐 数
		int unpayCount=0;//未支付套餐 数
		for(Map<String,Object> myMap:list){
			Integer startTimeM=(Integer)myMap.get("start_time");
			Integer endTimeM=(Integer)myMap.get("end_time");
			Integer isActiveM=(Integer) myMap.get("is_active");
			Integer feeTypeM=(Integer) myMap.get("fee_type");
			if(isActiveM ==1 && today>startTimeM && today<(endTimeM)){
			//	if(isActiveM ==1 && today>startTimeM && today<(endTimeM+WARNEND*86400)){
				Integer userNum = (Integer) myMap.get("user_num");
				Integer buyNum=(Integer)myMap.get("buy_num");
				userNumTotal += userNum*buyNum;
				
				feeType = feeTypeM;
				startTime = startTimeM;
				endTime = endTimeM;
				feeName = (String)myMap.get("fee_name");
			}
			
			if( (Integer)myMap.get("is_pay") == 1){
				payCount++;
			}else if(isActiveM == 0 && (Integer)myMap.get("is_pay") == 0){
				unpayCount++;
			}
		}
		LOG.debug("nowTime4:"+System.currentTimeMillis()+"  corpid:"+corpId);
		String endTimeStr=DateUtil.getString(endTime, DateUtil.SDFDate);
		 
		
		int overDay=(today-endTime)/86400;//超期天数，正为超期，负为未超期
		System.out.println("overDay:"+overDay);
		
	
		if(companyAddTime>OCTOBERFIRST){   //====== 10月1日之后注册的公司,实际人数5人及以下免费使用
			if(realUserNum<=5 && today>startTime && today<endTime){//实际使用人数小于等于5人
				isVip=1;
				showMsg="yes";
				if(today+WRANSTART*86400>endTime){//即将过期
					System.out.println("即将过期");
					showMsg="尊敬的用户您好！感谢贵公司("+corpName+")试用销帮帮CRM，您的试用期的截止时间是"+ endTimeStr+ "，还有"
							+ (-overDay)+ "天即将结束。贵公司实际使用人数为" + realUserNum + "人，您可以继续选择使用我们提供的免费版（部分功能受限），也可以选择付费升级为VIP版本享受更全更优质的服务。给您带来不便，敬请谅解！";
					isCookie=1;
				}
			}else if(realUserNum<=5 && today>endTime){//实际使用人数5人及以下且已过期，免费用户
				isVip=0;
				showMsg="free";
			}else if(realUserNum>5 && today>startTime && today<(endTime )){//实际使用人数大于5人
				isVip=1;
				showMsg="yes";
				if(today<endTime && today + WRANSTART*86400>endTime){//即将过期
					showMsg= "尊敬的用户您好！感谢贵公司("+corpName+")试用销帮帮CRM，您的试用期的截止时间是" + endTimeStr + "，还有"
							+ Math.abs(overDay) + "天即将结束。贵公司实际使用人数为" + realUserNum + "人，若需要继续使用我们的软件服务，请在试用期结束之前付费升级为VIP版本。给您带来不便，敬请谅解！";
					isCookie=1;
				} 
			}else if(realUserNum>5 && endTime <today){//已超期 ,停止使用
				isVip=0;
				isActive=0;
				showMsg= "感谢贵公司("+corpName+")试用销帮帮CRM，您的试用期现已超期。贵公司实际使用人数为" + realUserNum + "人，若需要继续使用我们的软件服务，请务必付费购买VIP版本。给您带来不便，敬请谅解！";
				
			}
		}else{  //====== 10月1日之前注册的公司,公司人数19人及以下（或者实际使用人数5人及以下）免费使用
				if( (corpUserNum<20 || realUserNum <=5 ) && today>startTime && today<endTime){//公司人数小于20人或者实际使用人数小于等于5人
					isVip=1;
					showMsg="yes";
					if(today+WRANSTART*86400>endTime){//即将过期
						showMsg="尊敬的用户您好！感谢贵公司("+corpName+")试用销帮帮CRM，您的试用期的截止时间是"+ endTimeStr+ "，还有"
								+ (-overDay)+ "天即将结束。贵公司目前总人数为"+ corpUserNum + 
								"人，实际使用人数为" + realUserNum + "人，您可以继续选择使用我们提供的免费版（部分功能受限），也可以选择付费升级为VIP版本享受更全更优质的服务。给您带来不便，敬请谅解！";
						isCookie=1;
					}
				}else if((corpUserNum<20 || realUserNum <=5 ) && today>endTime){//公司人数小于20人或者实际使用人数小于等于5人且已过期，免费用户
					isVip=0;
					showMsg="free";
				}else if(corpUserNum>=20 && realUserNum>5 && today>startTime && today<(endTime )){//公司人数大于20人并且实际使用人人数大于5人
					LOG.debug("nowTime5:"+System.currentTimeMillis()+"  corpid:"+corpId);
					isVip=1;
					showMsg="yes";
					if(today<endTime && today + WRANSTART*86400>endTime){//即将过期
						showMsg= "尊敬的用户您好！感谢贵公司("+corpName+")试用销帮帮CRM，您的试用期的截止时间是" + endTimeStr + "，还有"
								+ Math.abs(overDay) + "天即将结束。贵公司目前总人数为" + corpUserNum
								+ "人，实际使用人数为" + realUserNum + "人，若需要继续使用我们的软件服务，请在试用期结束之前付费升级为VIP版本。给您带来不便，敬请谅解！";
						isCookie=1;
					} 
				}else if(corpUserNum>=20  && realUserNum>5 && endTime <today){//已超期,停止使用
					isVip=0;
					isActive=0;
					showMsg= "感谢贵公司("+corpName+")试用销帮帮CRM，您的试用期现已超期。贵公司目前总人数为"+ corpUserNum
							+ "人，实际使用人数为" + realUserNum + "人，若需要继续使用我们的软件服务，请务必付费购买VIP版本。给您带来不便，敬请谅解！";
					
				}
		}
		
		
		if(0==feeType){//公司总人数套餐
			if(isVip==1 && corpUserNum>userNumTotal){//公司总人数>许可人数，停止使用
				isActive=0;
				showMsg="贵公司("+corpName+")所购买套餐许可总人数"+userNumTotal+"人，现公司人数为"+corpUserNum+
						"人，已超过套餐许可人数，无法继续使用。请尽快升级套餐，给您带来不便，敬请谅解！如有疑问，请咨询客服电话 ：4000-464-288";
			}
		}else if(1==feeType){//实际使用人数套餐
			if(isVip==1 && realUserNum>userNumTotal){//实际使用人数>许可人数，停止使用
				isActive=0;
				showMsg="贵公司("+corpName+")所购买套餐许可实际人数为"+userNumTotal+"人，实际使用人数为"
						+ realUserNum + "人，已超过套餐许可人数，无法继续使用。请尽快升级套餐，给您带来不便，敬请谅解！如有疑问，请咨询客服电话 ：4000-464-288";
			}
		}
		LOG.debug("nowTime6:"+System.currentTimeMillis()+"  corpid:"+corpId);	
		modelMap.put("corpName", corpName);
		modelMap.put("feeName",feeName);
		modelMap.put("corpUserNum", corpUserNum);//公司人数 ，
		modelMap.put("realUserNum", realUserNum);//使用人数 ，
		modelMap.put("userNum", userNumTotal);//套餐许可人数
		modelMap.put("feeType", feeType);//套餐类型
		modelMap.put("isVip",isVip);
		modelMap.put("showMsg", showMsg);
		modelMap.put("isActive",isActive);
		modelMap.put("isCookie", isCookie);
		modelMap.put("startTime",DateUtil.getString(startTime,DateUtil.SDFDate));
		modelMap.put("endTime",DateUtil.getString(endTime,DateUtil.SDFDate));
			
		modelMap.put("unpayCount",unpayCount);
		modelMap.put("payCount",payCount);
		//System.out.println(modelMap);
		LOG.debug("nowTime7:"+System.currentTimeMillis()+"  corpid:"+corpId);
		return isVip;
			
		
	}
}

 