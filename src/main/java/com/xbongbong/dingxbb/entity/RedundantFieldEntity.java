 
package com.xbongbong.dingxbb.entity;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xbongbong.util.StringUtil;

 
public class RedundantFieldEntity implements Serializable{

	/** Comment for <code>serialVersionUID</code> */
	private static final long serialVersionUID = -1L;

	//========== properties ==========
	
	//主建
	private Integer id;
	//公司ID
	private String corpid;
	//关联类型，1；客户，2：销售机会，3：合同 ，4：联系人
	private Integer refType;
	//关联对象id
	private Integer refId;
	//模板ID
	private Integer templateId;
	//扩展参数1
	private String attr1;
	//扩展参数2
	private String attr2;
	//扩展参数3
	private String attr3;
	//扩展参数4
	private String attr4;
	//扩展参数5
	private String attr5;
	//扩展参数6
	private String attr6;
	//扩展参数7
	private String attr7;
	//扩展参数8
	private String attr8;
	//扩展参数9
	private String attr9;
	//扩展参数10
	private String attr10;
	//扩展参数11
	private String attr11;
	//扩展参数12
	private String attr12;
	//扩展参数13
	private String attr13;
	//扩展参数14
	private String attr14;
	//扩展参数15
	private String attr15;
	//扩展参数16
	private String attr16;
	//扩展参数17
	private String attr17;
	//扩展参数18
	private String attr18;
	//扩展参数19
	private String attr19;
	//扩展参数20
	private String attr20;
	//销售阶段code
	private Integer salesStageCode;
	//扩展参数21
	private String attr21;
	//扩展参数22
	private String attr22;
	//扩展参数23
	private String attr23;
	//扩展参数24
	private String attr24;
	//扩展参数25
	private String attr25;
	//扩展参数26
	private String attr26;
	//扩展参数27
	private String attr27;
	//扩展参数28
	private String attr28;
	//扩展参数29
	private String attr29;
	//扩展参数30
	private String attr30;
	//扩展参数31
	private String attr31;
	//扩展参数32
	private String attr32;
	//扩展参数33
	private String attr33;
	//扩展参数34
	private String attr34;
	//扩展参数35
	private String attr35;
	//扩展参数36
	private String attr36;
	//扩展参数37
	private String attr37;
	//扩展参数38
	private String attr38;
	//扩展参数39
	private String attr39;
	//扩展参数40
	private String attr40;
	
	public RedundantFieldEntity(){
		this.corpid = "";
		this.refId = 0;
		this.refType = 0;
		this.templateId = 0;
		this.attr1 = "";
		this.attr2 = "";
		this.attr3 = "";
		this.attr4 = "";
		this.attr5 = "";
		this.attr6 = "";
		this.attr7 = "";
		this.attr8 = "";
		this.attr9 = "";
		this.attr10 = "";
		this.attr11 = "";
		this.attr12 = "";
		this.attr13 = "";
		this.attr14 = "";
		this.attr15 = "";
		this.attr16 = "";
		this.attr17 = "";
		this.attr18 = "";
		this.attr19 = "";
		this.attr20 = "";
		this.attr21 = "";
		this.attr22 = "";
		this.attr23 = "";
		this.attr24 = "";
		this.attr25 = "";
		this.attr26 = "";
		this.attr27 = "";
		this.attr28 = "";
		this.attr29 = "";
		this.attr30 = "";
		this.attr31 = "";
		this.attr32 = "";
		this.attr33 = "";
		this.attr34 = "";
		this.attr35 = "";
		this.attr36 = "";
		this.attr37 = "";
		this.attr38 = "";
		this.attr39 = "";
		this.attr40 = "";
		
		this.salesStageCode = 0;
	}

    //========== getters and setters ==========
    
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCorpid() {
		return corpid;
	}
	
	public void setCorpid(String corpid) {
		this.corpid = corpid;
	}
	public Integer getRefType() {
		return refType;
	}
	
	public void setRefType(Integer refType) {
		this.refType = refType;
	}
	public Integer getRefId() {
		return refId;
	}
	
	public void setRefId(Integer refId) {
		this.refId = refId;
	}
	public Integer getTemplateId() {
		return templateId;
	}
	
	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}
	public String getAttr1() {
		return  attr1.replace("\'","");
	}
	
	public void setAttr1(String attr1) {
		this.attr1 = attr1;
	}
	public String getAttr2() {
		return  attr2.replace("\'","");
	}
	
	public void setAttr2(String attr2) {
		this.attr2 = attr2;
	}
	public String getAttr3() {
		return  attr3.replace("\'","");
	}
	
	public void setAttr3(String attr3) {
		this.attr3 = attr3;
	}
	public String getAttr4() {
		return  attr4.replace("\'","");
	}
	
	public void setAttr4(String attr4) {
		this.attr4 = attr4;
	}
	public String getAttr5() {
		return  attr5.replace("\'","");
	}
	
	public void setAttr5(String attr5) {
		this.attr5 = attr5;
	}
	public String getAttr6() {
		return  attr6.replace("\'","");
	}
	
	public void setAttr6(String attr6) {
		this.attr6 = attr6;
	}
	public String getAttr7() {
		return  attr7.replace("\'","");
	}
	
	public void setAttr7(String attr7) {
		this.attr7 = attr7;
	}
	public String getAttr8() {
		return  attr8.replace("\'","");
	}
	
	public void setAttr8(String attr8) {
		this.attr8 = attr8;
	}
	public String getAttr9() {
		return  attr9.replace("\'","");
	}
	
	public void setAttr9(String attr9) {
		this.attr9 = attr9;
	}
	public String getAttr10() {
		return  attr10.replace("\'","");
	}
	
	public void setAttr10(String attr10) {
		this.attr10 = attr10;
	}
	public String getAttr11() {
		return  attr11.replace("\'","");
	}
	
	public void setAttr11(String attr11) {
		this.attr11 = attr11;
	}
	public String getAttr12() {
		return  attr12.replace("\'","");
	}
	
	public void setAttr12(String attr12) {
		this.attr12 = attr12;
	}
	public String getAttr13() {
		return  attr13.replace("\'","");
	}
	
	public void setAttr13(String attr13) {
		this.attr13 = attr13;
	}
	public String getAttr14() {
		return  attr14.replace("\'","");
	}
	
	public void setAttr14(String attr14) {
		this.attr14 = attr14;
	}
	public String getAttr15() {
		return  attr15.replace("\'","");
	}
	
	public void setAttr15(String attr15) {
		this.attr15 = attr15;
	}
	public String getAttr16() {
		return  attr16.replace("\'","");
	}
	
	public void setAttr16(String attr16) {
		this.attr16 = attr16;
	}
	public String getAttr17() {
		return  attr17.replace("\'","");
	}
	
	public void setAttr17(String attr17) {
		this.attr17 = attr17;
	}
	public String getAttr18() {
		return  attr18.replace("\'","");
	}
	
	public void setAttr18(String attr18) {
		this.attr18 = attr18;
	}
	public String getAttr19() {
		return   attr19.replace("\'","");
	}
	
	public void setAttr19(String attr19) {
		this.attr19 = attr19;
	}
	public String getAttr20() {
		return  attr20.replace("\'","");
	}
	
	public void setAttr20(String attr20) {
		this.attr20 = attr20;
	}
	
	public String getAttr21() {
		return attr21.replace("\'","");
	}

	public void setAttr21(String attr21) {
		this.attr21 = attr21;
	}

	public String getAttr22() {
		return attr22.replace("\'","");
	}

	public void setAttr22(String attr22) {
		this.attr22 = attr22;
	}

	public String getAttr23() {
		return attr23.replace("\'","");
	}

	public void setAttr23(String attr23) {
		this.attr23 = attr23;
	}

	public String getAttr24() {
		return attr24.replace("\'","");
	}

	public void setAttr24(String attr24) {
		this.attr24 = attr24;
	}

	public String getAttr25() {
		return attr25.replace("\'","");
	}

	public void setAttr25(String attr25) {
		this.attr25 = attr25;
	}

	public String getAttr26() {
		return attr26.replace("\'","");
	}

	public void setAttr26(String attr26) {
		this.attr26 = attr26;
	}

	public String getAttr27() {
		return attr27.replace("\'","");
	}

	public void setAttr27(String attr27) {
		this.attr27 = attr27;
	}

	public String getAttr28() {
		return attr28.replace("\'","");
	}

	public void setAttr28(String attr28) {
		this.attr28 = attr28;
	}

	public String getAttr29() {
		return attr29.replace("\'","");
	}

	public void setAttr29(String attr29) {
		this.attr29 = attr29;
	}

	public String getAttr30() {
		return attr30.replace("\'","");
	}

	public void setAttr30(String attr30) {
		this.attr30 = attr30;
	}

	public String getAttr31() {
		return attr31.replace("\'","");
	}

	public void setAttr31(String attr31) {
		this.attr31 = attr31;
	}

	public String getAttr32() {
		return attr32.replace("\'","");
	}

	public void setAttr32(String attr32) {
		this.attr32 = attr32;
	}

	public String getAttr33() {
		return attr33.replace("\'","");
	}

	public void setAttr33(String attr33) {
		this.attr33 = attr33;
	}

	public String getAttr34() {
		return attr34.replace("\'","");
	}

	public void setAttr34(String attr34) {
		this.attr34 = attr34;
	}

	public String getAttr35() {
		return attr35.replace("\'","");
	}

	public void setAttr35(String attr35) {
		this.attr35 = attr35;
	}

	public String getAttr36() {
		return attr36.replace("\'","");
	}

	public void setAttr36(String attr36) {
		this.attr36 = attr36;
	}

	public String getAttr37() {
		return attr37.replace("\'","");
	}

	public void setAttr37(String attr37) {
		this.attr37 = attr37;
	}

	public String getAttr38() {
		return attr38.replace("\'","");
	}

	public void setAttr38(String attr38) {
		this.attr38 = attr38;
	}

	public String getAttr39() {
		return attr39.replace("\'","");
	}

	public void setAttr39(String attr39) {
		this.attr39 = attr39;
	}

	public String getAttr40() {
		return attr40.replace("\'","");
	}

	public void setAttr40(String attr40) {
		this.attr40 = attr40;
	}
	
	public Integer getSalesStageCode() {
		return salesStageCode;
	}

	public void setSalesStageCode(Integer salesStageCode) {
		this.salesStageCode = salesStageCode;
	}

	/**
	* 重载toString方法
	* @return
	*
	* @see java.lang.Object#toString()
	*/
	@Override
    public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
	
	/**
	 * 自定义图片上传，文件上传，多选类型特殊处理
	 * 5,6图片限制1个  8文件  9多选 
	 * @param value
	 * @param explainMap
	 * @param attrName
	 * @return
	 */
	public String importSpecExtend(String value, Map<String,RedundantFieldExplainEntity> explainMap, String attrName) {
		if (StringUtil.isEmpty(value)) {
			return "";
		}
		
		//5,6图片限制1个  8文件  9多选 
		//自定义图片类型特殊处理
		if(explainMap.containsKey(attrName) && explainMap.get(attrName).getFieldType().equals(6)){
			JSONArray imgArray = null;
			try {
				imgArray = JSON.parseArray(value);
			} catch(Exception e) {
				//错误格式就无视
				return null;
			}
			if(imgArray != null) {
				value = imgArray.toString();
			}
		}
		//文件
		if(explainMap.containsKey(attrName) && explainMap.get(attrName).getFieldType().equals(8)){
			JSONArray fileArray = new JSONArray();
			JSONObject object = new JSONObject();
			object.put("attachIndex", value);
			object.put("filename", value);
			object.put("ext", "");
			object.put("size", "");
			fileArray.add(object);
			value = fileArray.toString();
		}
		//多选项
		if(explainMap.containsKey(attrName) && explainMap.get(attrName).getFieldType().equals(9)){
			String muchArray[] = value.split(",|，");//用户导入时输入的
			String muchValue = "";
			for (String itemStr : muchArray) {
				if (!StringUtil.isEmpty(muchValue)) {
					muchValue += "|";
				}
				muchValue += itemStr;
			}
			
			value = muchValue;
		}
		
		return value;
	}
	
	public RedundantFieldAppendEntity getRedundantFieldAppendEntity(){
		
		if(StringUtil.isEmpty(this.attr21) && StringUtil.isEmpty(this.attr22) && StringUtil.isEmpty(this.attr23) && StringUtil.isEmpty(this.attr24)
				 && StringUtil.isEmpty(this.attr25) && StringUtil.isEmpty(this.attr26) && StringUtil.isEmpty(this.attr27) && StringUtil.isEmpty(this.attr28) 
				 && StringUtil.isEmpty(this.attr29) && StringUtil.isEmpty(this.attr30) && StringUtil.isEmpty(this.attr31) && StringUtil.isEmpty(this.attr32) 
				 && StringUtil.isEmpty(this.attr33) && StringUtil.isEmpty(this.attr34) && StringUtil.isEmpty(this.attr35) && StringUtil.isEmpty(this.attr36) 
				 && StringUtil.isEmpty(this.attr37) && StringUtil.isEmpty(this.attr38) && StringUtil.isEmpty(this.attr39) && StringUtil.isEmpty(this.attr40)){
			return null;
		}
		
		RedundantFieldAppendEntity redundantFieldAppendEntity = new RedundantFieldAppendEntity();
		redundantFieldAppendEntity.setAttr21(this.attr21);
		redundantFieldAppendEntity.setAttr22(this.attr22);
		redundantFieldAppendEntity.setAttr23(this.attr23);
		redundantFieldAppendEntity.setAttr24(this.attr24);
		redundantFieldAppendEntity.setAttr25(this.attr25);
		redundantFieldAppendEntity.setAttr26(this.attr26);
		redundantFieldAppendEntity.setAttr27(this.attr27);
		redundantFieldAppendEntity.setAttr28(this.attr28);
		redundantFieldAppendEntity.setAttr29(this.attr29);
		redundantFieldAppendEntity.setAttr30(this.attr30);
		redundantFieldAppendEntity.setAttr31(this.attr31);
		redundantFieldAppendEntity.setAttr32(this.attr32);
		redundantFieldAppendEntity.setAttr33(this.attr33);
		redundantFieldAppendEntity.setAttr34(this.attr34);
		redundantFieldAppendEntity.setAttr35(this.attr35);
		redundantFieldAppendEntity.setAttr36(this.attr36);
		redundantFieldAppendEntity.setAttr37(this.attr37);
		redundantFieldAppendEntity.setAttr38(this.attr38);
		redundantFieldAppendEntity.setAttr39(this.attr39);
		redundantFieldAppendEntity.setAttr40(this.attr40);
		redundantFieldAppendEntity.setCorpid(this.corpid);
		redundantFieldAppendEntity.setRedundantFieldId(this.id);
		redundantFieldAppendEntity.setRefId(this.refId);
		redundantFieldAppendEntity.setRefType(this.refType);
		redundantFieldAppendEntity.setTemplateId(this.templateId);
		return redundantFieldAppendEntity;
	}
	
	public void setRedundantFieldAppendEntity(RedundantFieldAppendEntity redundantFieldAppendEntity){
		if(redundantFieldAppendEntity == null)
			return ;
		this.attr21 = redundantFieldAppendEntity.getAttr21();
		this.attr22 = redundantFieldAppendEntity.getAttr22();
		this.attr23 = redundantFieldAppendEntity.getAttr23();
		this.attr24 = redundantFieldAppendEntity.getAttr24();
		this.attr25 = redundantFieldAppendEntity.getAttr25();
		this.attr26 = redundantFieldAppendEntity.getAttr26();
		this.attr27 = redundantFieldAppendEntity.getAttr27();
		this.attr28 = redundantFieldAppendEntity.getAttr28();
		this.attr29 = redundantFieldAppendEntity.getAttr29();
		this.attr30 = redundantFieldAppendEntity.getAttr30();
		this.attr31 = redundantFieldAppendEntity.getAttr31();
		this.attr32 = redundantFieldAppendEntity.getAttr32();
		this.attr33 = redundantFieldAppendEntity.getAttr33();
		this.attr34 = redundantFieldAppendEntity.getAttr34();
		this.attr35 = redundantFieldAppendEntity.getAttr35();
		this.attr36 = redundantFieldAppendEntity.getAttr36();
		this.attr37 = redundantFieldAppendEntity.getAttr37();
		this.attr38 = redundantFieldAppendEntity.getAttr38();
		this.attr39 = redundantFieldAppendEntity.getAttr39();
		this.attr40 = redundantFieldAppendEntity.getAttr40();
//		this.corpid = redundantFieldAppendEntity.getCorpid();
//		this.id = redundantFieldAppendEntity.getRedundantFieldId();
//		this.refId = redundantFieldAppendEntity.getRefId();
//		this.refType = redundantFieldAppendEntity.getRefType();
//		this.templateId = redundantFieldAppendEntity.getTemplateId();
	}

	public RedundantFieldAppendEntity getRedundantFieldAppendEntity(
			RedundantFieldAppendEntity redundantFieldAppend) {
		
		//没有attr20以上的字段，且没有attr20以上的数据
		if(redundantFieldAppend == null && StringUtil.isEmpty(this.attr21) && StringUtil.isEmpty(this.attr22) && StringUtil.isEmpty(this.attr23) && StringUtil.isEmpty(this.attr24)
				 && StringUtil.isEmpty(this.attr25) && StringUtil.isEmpty(this.attr26) && StringUtil.isEmpty(this.attr27) && StringUtil.isEmpty(this.attr28) 
				 && StringUtil.isEmpty(this.attr29) && StringUtil.isEmpty(this.attr30) && StringUtil.isEmpty(this.attr31) && StringUtil.isEmpty(this.attr32) 
				 && StringUtil.isEmpty(this.attr33) && StringUtil.isEmpty(this.attr34) && StringUtil.isEmpty(this.attr35) && StringUtil.isEmpty(this.attr36) 
				 && StringUtil.isEmpty(this.attr37) && StringUtil.isEmpty(this.attr38) && StringUtil.isEmpty(this.attr39) && StringUtil.isEmpty(this.attr40)){
			return null;
		}
		
		if(redundantFieldAppend == null){
			redundantFieldAppend = new RedundantFieldAppendEntity();
		}
		
		redundantFieldAppend.setAttr21(this.attr21);
		redundantFieldAppend.setAttr22(this.attr22);
		redundantFieldAppend.setAttr23(this.attr23);
		redundantFieldAppend.setAttr24(this.attr24);
		redundantFieldAppend.setAttr25(this.attr25);
		redundantFieldAppend.setAttr26(this.attr26);
		redundantFieldAppend.setAttr27(this.attr27);
		redundantFieldAppend.setAttr28(this.attr28);
		redundantFieldAppend.setAttr29(this.attr29);
		redundantFieldAppend.setAttr30(this.attr30);
		redundantFieldAppend.setAttr31(this.attr31);
		redundantFieldAppend.setAttr32(this.attr32);
		redundantFieldAppend.setAttr33(this.attr33);
		redundantFieldAppend.setAttr34(this.attr34);
		redundantFieldAppend.setAttr35(this.attr35);
		redundantFieldAppend.setAttr36(this.attr36);
		redundantFieldAppend.setAttr37(this.attr37);
		redundantFieldAppend.setAttr38(this.attr38);
		redundantFieldAppend.setAttr39(this.attr39);
		redundantFieldAppend.setAttr40(this.attr40);
		
		if(StringUtil.isEmpty(redundantFieldAppend.getCorpid())){
			redundantFieldAppend.setCorpid(this.corpid);
		}
		
		if(redundantFieldAppend.getRedundantFieldId() == null || redundantFieldAppend.getRedundantFieldId().equals(0)){
			redundantFieldAppend.setRedundantFieldId(this.id);
		}
		
		if(redundantFieldAppend.getRefId() == null || redundantFieldAppend.getRefId().equals(0)){
			redundantFieldAppend.setRefId(this.refId);
		}
		
		if(redundantFieldAppend.getRefType() == null || redundantFieldAppend.getRefType().equals(0)){
			redundantFieldAppend.setRefType(this.refType);
		}
		
		if(redundantFieldAppend.getTemplateId() == null || redundantFieldAppend.getTemplateId().equals(0)){
			redundantFieldAppend.setTemplateId(this.templateId);
		}
		
		return redundantFieldAppend;
	}
}

