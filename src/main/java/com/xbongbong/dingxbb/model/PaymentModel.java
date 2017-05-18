 
package com.xbongbong.dingxbb.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xbongbong.dingxbb.dao.PaymentDao;
import com.xbongbong.dingxbb.entity.PaymentEntity;
import com.xbongbong.util.CommentUtil;
import com.xbongbong.util.PageHelper;
 

/**
 * @author ah
 *
 */
@Component
public class PaymentModel  implements IModel{

	@Autowired
	private PaymentDao dao;
//	@Autowired
//	private ImportHelper importHelper;
	
	public Integer insert(Object entity){
		((PaymentEntity)entity).setId(null);

		return dao.insert((PaymentEntity)entity);
	}

	public Integer update(Object entity){
		return dao.update((PaymentEntity)entity);
	}
	
	public Integer save(PaymentEntity entity){
		if(entity.getId() == null || entity.getId().equals(0)) {
			((PaymentEntity)entity).setId(null);

			return dao.insert((PaymentEntity)entity);
		}
		return dao.update((PaymentEntity)entity);
	}
	 
	public Integer deleteByKey(Integer key, String corpid) {
		return dao.deleteByKey(key, corpid);
	}
	
	public PaymentEntity getByKey( Integer key, String corpid){
		return dao.getByKey(key, corpid);
	}
	public Integer deleteEntitys(Map<String ,Object>  param){
		return dao.deleteEntitys(param);
	}
	public List<PaymentEntity> findEntitys(Map<String ,Object>  param){
		return dao.findEntitys(param);
	}
	public Integer getEntitysCount(Map<String ,Object>  param){
		return dao.getEntitysCount(param);
	}

	public Integer deleteByContractId(Integer contractId,String corpid) {
		return dao.deleteByContractId(contractId, corpid);
	}
	
	public Double getSumAmount(Map<String ,Object>  param) {
		return dao.getSumAmount(param);
	}
	
	/**
	 * 获取收款额
	 * @param corpid
	 * @param signUserIdIn
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public Double getFinishPaymentAmount(String corpid, List<String> userIdIn, Integer startTime, Integer endTime){
		Map<String, Object> param = new HashMap<String, Object>();
		
		param.put("corpid", corpid);
		param.put("del", 0);
		param.put("userIdIn", userIdIn);
		param.put("startTime", startTime);
		param.put("endTime", endTime);
		//param.put("contractIdIn", contractIdIn);
		param.put("status", 2);//已回款
		
		Double finishContractAmount = getSumAmount(param);
		finishContractAmount = finishContractAmount == null?0:finishContractAmount;
		return finishContractAmount;
	}
	
	/**
	 * 获取部分回款--与sheet连表
	 * @param param
	 * @return
	 */
	public Double getSomeAmount(Map<String ,Object>  param) {
		return dao.getSomeAmount(param);
	}
	/**
	 * 获取部分回款--与sheet连表--返回列表--统计用
	 * @param param
	 * @return
	 * @author chuanpeng.zhang
	 */
	public List<PaymentEntity> findSomeAmounts(Map<String ,Object>  param){
		return dao.findSomeAmounts(param);
	}
	
	public List<PaymentEntity> getEntityList(Map<String, Object> param,PageHelper pageHelper) {
		Integer pageNum = pageHelper.getPageSize();
		Integer start = (pageHelper.getCurrentPageNum() - 1) * pageNum;

		param.put("pageNum", pageNum);
		param.put("start", start);
		return dao.findEntitys(param);
	}
	
	/**
	 * 生成pageHelper
	 * 
	 * @return PageHelper
	 */
	public PageHelper getPageHelperJoin(Map<String, Object> param, Integer pageSize) {
		Integer rowCounts = dao.getEntitysCountJoin(param);
		Integer page = (Integer) param.get("page");
		if (page == null)
			page = 1;

		param.remove("page");
		String pageUriStr = CommentUtil.mapToUrl(param);
		param.put("page", page);
		
		//记录总数放入param
		param.put("total", rowCounts);

		PageHelper pageHelper = new PageHelper(page, pageSize);
		pageHelper.setRowsCount(rowCounts);
		pageHelper.setPageUriStr(pageUriStr);

		return pageHelper;
	}
	
	public List<PaymentEntity> getEntityListJoin(Map<String, Object> param,PageHelper pageHelper) {
		Integer pageNum = pageHelper.getPageSize();
		Integer start = (pageHelper.getCurrentPageNum() - 1) * pageNum;

		param.put("pageNum", pageNum);
		param.put("start", start);
		return dao.findEntitysJoin(param);
	}
	
	
	/** 统计时间段内客户的回款金额排名  by chy */
	public List<PaymentEntity> customerAmountRank(Map<String ,Object>  param){
		return dao.customerAmountRank(param);
	}
	/** 统计时间段内员工的回款金额排名  by chy */
	public List<PaymentEntity> saleAmountRank(Map<String ,Object>  param){
		return dao.saleAmountRank(param);
	}
	
	/**  @以员工汇总 */
	public List<PaymentEntity> statisticByStaff(Map<String ,Object>  param){
		return dao.statisticByStaff(param);
	}
	
	/**  @以部门汇总 */
	public List<PaymentEntity> statisticByDep(Map<String ,Object>  param){
		return dao.statisticByDep(param);
	}

	/**@1 统计我一周的回款总额 */
	public Integer totalSumByWeek(Map<String ,Object>  param){
		return dao.totalSumByWeek(param);
	}
	/** @2 当前月份或季度的回款总额 */
	public Map<String,Object> totalSumOfThisMonthOrSeason(Map<String ,Object>  param){
		return dao.totalSumOfThisMonthOrSeason(param);
	}
	/**  @3 回款总额的排名 */
	public List<PaymentEntity> rankOfThisMonth(Map<String ,Object>  param){
		return dao.rankOfThisMonth(param);
	}
	/** @3 当前月份或季度的合同分组总计*/
	public Integer rankOfThisMonthOrSeason(Map<String ,Object>  param){
		return dao.rankOfThisMonthOrSeason(param);
	}
	
	/**
	 * 回款单列表
	 * @param param
	 * @return
	 */
	public List<PaymentEntity>  findEntitysJoin(Map<String ,Object>  param) {
		return dao.findEntitysJoin(param);
	}
	/**
	 * 回款单数量
	 * @param param
	 * @return
	 */
	public Integer getEntitysCountJoin(Map<String ,Object>  param) {
		return dao.getEntitysCountJoin(param);
	}
	
	
	/**
	 * 合同回款列表 
	 * @param param
	 * @return
	 */
	public List<PaymentEntity> findEntitysJoinSheet(Map<String, Object> param) {
		return dao.findEntitysJoinSheet(param);
	}
	
	/**
	 * 与contract_user连表--- 以合同为主：先统计负责的合同，以统计到的合同ids为条件，再筛选与这些合同关联的回款。
	 * @param param
	 * @return
	 * @author chuanpeng.zhang
	 */
	public List<PaymentEntity> findEntitysJoinContractUser(Map<String, Object> param) {
		return dao.findEntitysJoinContractUser(param);
	}
	public Integer getEntitysCountJoinContractUser(Map<String, Object> param) {
		return dao.getEntitysCountJoinContractUser(param);
	}
	
	/**
	 * excel中的回款保存
	 * @param errorExplainList 	错误解释列表中最后三个是对回款的解释
	 * @param excelMap			excel行数据
	 * @param userEntity		操作人
	 * @param contractEntity	关联合同
	 * @param errorExcel		存放错误信息的excel
	 * @return
	 */
//	public boolean savePayment(List<RedundantFieldExplainEntity> errorExplainList, Map<String,String> excelMap, UserEntity userEntity, 
//			ContractEntity contractEntity, List<List<String>> errorList){
//		
//		RedundantFieldExplainEntity amountExplainEntity = errorExplainList.get(errorExplainList.size() - 4);
//		RedundantFieldExplainEntity estimateTimeExplainEntity = errorExplainList.get(errorExplainList.size() - 3);
//		RedundantFieldExplainEntity payTimeExplainEntity = errorExplainList.get(errorExplainList.size() - 2);
//		RedundantFieldExplainEntity statusExplainEntity = errorExplainList.get(errorExplainList.size() - 1);
//		String payAmountStr = excelMap.get(amountExplainEntity.getAttrName());
//		String estimateTimeStr = excelMap.get(estimateTimeExplainEntity.getAttrName());
//		String payTimeStr = excelMap.get(payTimeExplainEntity.getAttrName());
//		String paymentStatusStr = excelMap.get(statusExplainEntity.getAttrName());
//		if(StringUtil.isEmpty(payAmountStr) && StringUtil.isEmpty(estimateTimeStr) && StringUtil.isEmpty(paymentStatusStr)){
//			return true;
//		}else{
//			if(StringUtil.isEmpty(payAmountStr) || StringUtil.isEmpty(estimateTimeStr) || StringUtil.isEmpty(paymentStatusStr)){
//				String[] val = importHelper.getExcelRowArray(errorExplainList, excelMap);
//				errorList = importHelper.addError(errorList, "缺少回款必填信息", val);
////				session.setAttribute("errorExcel", errorExcel);
//				return false;
//			}
//		}
//		//判断回款总额是否超过合同金额
//		Map<String,Object> param = new HashMap<String,Object>();
//		param.put("corpid", contractEntity.getCorpid());
//		param.put("contractId", contractEntity.getId());
//		param.put("del", 0);
//		List<PaymentEntity> paymentList = findEntitys(param);
//		double paymentMoney = 0.0;
//		for(PaymentEntity payment : paymentList){
//			paymentMoney += payment.getAmount();
//		}
//		paymentMoney += StringUtil.toDouble(payAmountStr, 0.0);
//		
//		if(paymentMoney > contractEntity.getAmount()+ConfigConstant.amountAccuracy){
//			String[] val = importHelper.getExcelRowArray(errorExplainList, excelMap);
//			errorList = importHelper.addError(errorList, "回款总额不能大于合同额", val);
////			session.setAttribute("errorExcel", errorExcel);
//			return false;
//		}
//		
//		PaymentEntity paymentEntity = new PaymentEntity();
//		Integer now = DateUtil.getInt();
//		paymentEntity.setId(null);
//		paymentEntity.setUpdateTime(now);
//		paymentEntity.setAddTime(now);
//		paymentEntity.setDel(0);
//		
//		paymentEntity.setAccountPeriod(0);
//		paymentEntity.setCorpid(contractEntity.getCorpid());
//		paymentEntity.setCustomerId(contractEntity.getCustomerId());
//		paymentEntity.setContractId(contractEntity.getId());
//		paymentEntity.setContractName(contractEntity.getName());
//		paymentEntity.setCustomerName(contractEntity.getCustomerName());
//		paymentEntity.setPayTime(0);
//		
//		String userId = "0";
//		String userName = "";
//		if(userEntity != null){
//			userId = userEntity.getUserId();
//			userName = userEntity.getName();
//		}
//		paymentEntity.setUserId(userId);
//		paymentEntity.setUserName(userName);
//		
//		//转一次，防止废数值
//		Double amount = StringUtil.toDouble(payAmountStr, 0.0);
//		paymentEntity.setAmount(amount);
//		
//		Integer estimateTime = UtilHelper.getExcelTime(estimateTimeStr);
//		paymentEntity.setEstimateTime(estimateTime);
//		Integer payTime = UtilHelper.getExcelTime(payTimeStr);
//		paymentEntity.setPayTime(payTime);
//		Integer status = PaymentEntity.getPaymentStatusInt(paymentStatusStr);
//		paymentEntity.setStatus(status);
////		if(status == 2){
////			paymentEntity.setPayTime(estimateTime);
////		}
//		try {
//			insert(paymentEntity);
//			paymentEntity.setPaymentNo(contractEntity.getContractNo()+"."+paymentEntity.getId());
//			update(paymentEntity);
//		} catch (Exception e) {
//			e.printStackTrace();
//			String[] val = importHelper.getExcelRowArray(errorExplainList, excelMap);
//			errorList = importHelper.addError(errorList, "回款保存失败", val);
////			session.setAttribute("errorExcel", errorExcel);
//			return false;
//		}
//		return true;
//		
//	}
	
	
}

 