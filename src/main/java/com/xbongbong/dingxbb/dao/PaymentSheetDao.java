package com.xbongbong.dingxbb.dao;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xbongbong.dingxbb.entity.PaymentSheetEntity;

public interface PaymentSheetDao {
	public Integer insert(PaymentSheetEntity paymentSheet);
	public Integer update(PaymentSheetEntity paymentSheet);
	
	public Integer deleteByKey(@Param("key")Integer key,@Param("corpid")String corpid);
	public PaymentSheetEntity getByKey (@Param("key") Integer key, @Param("corpid")String corpid);
	
	public List<PaymentSheetEntity>  findEntitys(@Param("param")Map<String ,Object>  param);
	public Integer getEntitysCount(@Param("param")Map<String ,Object>  param);
	
	
	public Integer deleteByContractId(@Param("contractId")Integer contractId,@Param("corpid")String corpid);
	public Integer deleteByPaymentId(@Param("paymentId")Integer paymentId,@Param("corpid")String corpid);
	
	
	/**
	 * 获取某条回款记录【状态为部分回款】的回款记录的已回款总额
	 * @param corpid
	 * @param paymentId
	 * @return
	 */
	public Double getParterPaymentSum(@Param("corpid")String corpid, @Param("paymentId")Integer paymentId);
	
	/**
	 * 批量更新回款单中某公司某用户的姓名
	 * @param corpid
	 * @param userId
	 * @param userName
	 * @return
	 * @author hongxiao.liu
	 * @time 2016-10-13 下午6:56:55
	 */
	public Integer dataConsistencyUpdateUser(@Param("corpid")String corpid, @Param("belongId")String userId, @Param("belongName")String userName);
}
