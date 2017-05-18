package com.xbongbong.dingxbb.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xbongbong.dingxbb.dao.PaymentSheetDao;
import com.xbongbong.dingxbb.entity.PaymentEntity;
import com.xbongbong.dingxbb.entity.PaymentSheetEntity;
import com.xbongbong.util.DateUtil;
import com.xbongbong.util.StringUtil;

@Component
public class PaymentSheetModel implements IModel{
	@Autowired
	private PaymentSheetDao dao;
	
	public Integer insert(Object entity){
		((PaymentSheetEntity)entity).setId(null);

		return dao.insert((PaymentSheetEntity)entity);
	}

	public Integer update(Object entity){
		return dao.update((PaymentSheetEntity)entity);
	}
	
	public Integer save(PaymentSheetEntity entity){
		if(entity.getId() == null || entity.getId().equals(0)) {
			((PaymentSheetEntity)entity).setId(null);

			return dao.insert((PaymentSheetEntity)entity);
		}
		return dao.update((PaymentSheetEntity)entity);
	}

	 
	public Integer deleteByKey(Integer key, String corpid) {
		return dao.deleteByKey(key, corpid);
	}
	
	public PaymentSheetEntity getByKey( Integer key, String corpid){
		return dao.getByKey(key, corpid);
	}
	
	public List<PaymentSheetEntity> findEntitys(Map<String ,Object>  param){
		return dao.findEntitys(param);
	}
	public Integer getEntitysCount(Map<String ,Object>  param){
		return dao.getEntitysCount(param);
	}
	
	public Integer deleteByContractId(Integer contractId,String corpid) {
		return dao.deleteByContractId(contractId, corpid);
	}
	
	public Integer deleteByPaymentId(Integer paymentId,String corpid) {
		return dao.deleteByPaymentId(paymentId, corpid);
	}
	
	/**
	 * 获取某条回款记录【状态为部分回款】的回款记录的已回款总额
	 * @param corpid
	 * @param paymentId
	 * @return
	 */
	public Double getParterPaymentSum(String corpid, Integer paymentId) {
		return dao.getParterPaymentSum(corpid, paymentId);
	}
	
	
	/**
	 * 批量更新回款单中某公司某用户的姓名
	 * @param corpid
	 * @param userId
	 * @param userName
	 * @return
	 * @author hongxiao.liu
	 * @time 2016-10-13 下午6:56:55
	 */
	public Integer dataConsistencyUpdateUser(String corpid, String userId, String userName) {
		return dao.dataConsistencyUpdateUser(corpid, userId, userName);
	}
	
	/**
	 * 获取最新的回款编号
	 * @param corpid
	 * @param paymentId
	 * @return
	 * @author hongxiao.liu
	 * @time 2016-10-14 下午4:32:50
	 */
	public String getLastNewPaymentSheetNo(String corpid, Integer paymentId) {
		String lastNewNo = "";
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("corpid", corpid);
		param.put("paymentId", paymentId);
		param.put("del", 0);
		param.put("orderByStr", "updateTime desc");
		List<PaymentSheetEntity> sheetList = findEntitys(param);
		if (sheetList != null && sheetList.size() > 0) {
			PaymentSheetEntity entity = sheetList.get(0);
			lastNewNo = entity.getPaymentSheetNo();
		}
		return lastNewNo;
	}
	
	/**
	 * 通过回款计划创建回款单
	 * @param corpid
	 * @param userId
	 * @param signPersonId
	 * @param signPersonName
	 * @param payment
	 * @param payMethod
	 * @author hongxiao.liu
	 * @time 2016-10-14 下午2:24:09
	 */
	public void savePaymentSheet(String corpid, String userId, String signPersonId, String signPersonName, PaymentEntity payment, Integer payMethod){
		//查询最新的回款单编号
		String lastNewNo = getLastNewPaymentSheetNo(corpid, payment.getId());
		
		PaymentSheetEntity entity = new PaymentSheetEntity();
		Integer now = DateUtil.getInt();
		
		entity.setId(null);
		entity.setCorpid(corpid);
		entity.setUserId(userId);
		entity.setCustomerId(payment.getCustomerId());
		entity.setContractId(payment.getContractId());
		entity.setPaymentId(payment.getId());
		
		entity.setAmount(payment.getAmount());
		entity.setPaymentTime(payment.getPayTime());
		
		if(payMethod == null || payMethod == 0 || payMethod > 5) {
			payMethod = 1;
		}
		entity.setPayMethod(payMethod);
		entity.setMemo("");
		entity.setAddTime(now);
		entity.setUpdateTime(now);
		entity.setDel(0);
		
		//回款单编号
		Integer sheetNo = 1;
		if(!StringUtil.isEmpty(lastNewNo)) {
			int index = lastNewNo.lastIndexOf(".");
			if(index > -1) {
				sheetNo = StringUtil.toInt(lastNewNo.substring(index+1), 1) + 1;
			}
		}
		entity.setPaymentSheetNo(payment.getPaymentNo() + "." + sheetNo);
		entity.setBelongId(signPersonId);
		entity.setBelongName(signPersonName);
		
		entity.setPayMethodStr(getPayMethodStr(payMethod));
		
		dao.insert(entity);
	}
	
	private String getPayMethodStr(Integer payMethod) {
		String resultStr = "";
		switch(payMethod){
		case 1:
			resultStr = "网银转账";
			break;
		case 2:
			resultStr = "现金";
			break;
		case 3:
			resultStr = "支票";	
			break;
		case 4:
			resultStr = "电汇";	
			break;
		case 5:
			resultStr = "承兑汇票";
			break;
		default : ;
		}
		return resultStr;
	}
	
}
