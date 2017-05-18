package com.xbongbong.dingxbb.entity;


public interface ImportEntity {
	
	/**
	 * 导入时，设置值
	 * @param value
	 * @param attr
	 * @return
	 */
	public Integer setValue(String value, String attr);
	/**
	 * 获取要插入的数据
	 * @param request
	 * @param response
	 * @param modelMap
	 * @param model 比如可以是staffModel
	 * @param departModel
	 * @param staffRelaModel
	 * @return
	
	public List getDataList(HttpServletRequest request,
			HttpServletResponse response, Map<String, Object> modelMap, Object model, Object departModel,Object staffRelaModel, boolean ifPage) ; */
	/**
	 * 特殊字段处理，用于excel导入,在实体保存成功后建立关系 
	 */
	public void setValue() ;
	
	/**
	 * 将非关联字段置空。主要用于联合导入
	 */
	public void setEmpty();
}
