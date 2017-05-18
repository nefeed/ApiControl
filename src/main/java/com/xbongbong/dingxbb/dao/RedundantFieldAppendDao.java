 
package com.xbongbong.dingxbb.dao;

import java.util.List;
import java.util.Map;
import java.lang.Integer;
import org.apache.ibatis.annotations.Param;
import com.xbongbong.dingxbb.entity.RedundantFieldAppendEntity;

 
 
public interface RedundantFieldAppendDao {

	 
	public Integer insert(RedundantFieldAppendEntity redundantFieldAppend);
	public Integer update(RedundantFieldAppendEntity redundantFieldAppend);
	
	 
	
	public Integer deleteByKey(@Param("key") Integer key);
	public RedundantFieldAppendEntity getByKey (@Param("key") Integer key, @Param("corpid")String corpid);
	
	
	public List<RedundantFieldAppendEntity>  findEntitys(@Param("param")Map<String ,Object>  param);
	public List<Integer>  findRefIds(@Param("param")Map<String ,Object>  param);
	public Integer getEntitysCount(@Param("param")Map<String ,Object>  param);
	public RedundantFieldAppendEntity getByRefTypeAndRefId(@Param("corpid")String corpid, @Param("refType") Integer refType, @Param("refId") Integer refId);
		
		
 
}