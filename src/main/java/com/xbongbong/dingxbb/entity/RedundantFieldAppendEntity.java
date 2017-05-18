 
package com.xbongbong.dingxbb.entity;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

 
public class RedundantFieldAppendEntity implements Serializable{

	/** Comment for <code>serialVersionUID</code> */
	private static final long serialVersionUID = -1L;
	public RedundantFieldAppendEntity (){
	}
	//========== properties ==========
	
	//主建
	private Integer id;
	//redundantFieldId
	private Integer redundantFieldId;
	//公司ID
	private String corpid;
	//关联类型，1；客户，2：销售机会，3：合同，4：联系人 5：产品 6：跟进记录 7：销售阶段
	private Integer refType;
	//关联对象id
	private Integer refId;
	//模板ID
	private Integer templateId;
	//扩展参数1
	private String attr21;
	//扩展参数2
	private String attr22;
	//扩展参数3
	private String attr23;
	//扩展参数4
	private String attr24;
	//扩展参数5
	private String attr25;
	//扩展参数6
	private String attr26;
	//扩展参数7
	private String attr27;
	//扩展参数8
	private String attr28;
	//扩展参数9
	private String attr29;
	//扩展参数10
	private String attr30;
	//扩展参数11
	private String attr31;
	//扩展参数12
	private String attr32;
	//扩展参数13
	private String attr33;
	//扩展参数14
	private String attr34;
	//扩展参数15
	private String attr35;
	//扩展参数16
	private String attr36;
	//扩展参数17
	private String attr37;
	//扩展参数18
	private String attr38;
	//扩展参数19
	private String attr39;
	//扩展参数20
	private String attr40;

    //========== getters and setters ==========
    
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getRedundantFieldId() {
		return redundantFieldId;
	}

	public void setRedundantFieldId(Integer redundantFieldId) {
		this.redundantFieldId = redundantFieldId;
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
	public String getAttr21() {
		return attr21;
	}
	
	public void setAttr21(String attr21) {
		this.attr21 = attr21;
	}
	public String getAttr22() {
		return attr22;
	}
	
	public void setAttr22(String attr22) {
		this.attr22 = attr22;
	}
	public String getAttr23() {
		return attr23;
	}
	
	public void setAttr23(String attr23) {
		this.attr23 = attr23;
	}
	public String getAttr24() {
		return attr24;
	}
	
	public void setAttr24(String attr24) {
		this.attr24 = attr24;
	}
	public String getAttr25() {
		return attr25;
	}
	
	public void setAttr25(String attr25) {
		this.attr25 = attr25;
	}
	public String getAttr26() {
		return attr26;
	}
	
	public void setAttr26(String attr26) {
		this.attr26 = attr26;
	}
	public String getAttr27() {
		return attr27;
	}
	
	public void setAttr27(String attr27) {
		this.attr27 = attr27;
	}
	public String getAttr28() {
		return attr28;
	}
	
	public void setAttr28(String attr28) {
		this.attr28 = attr28;
	}
	public String getAttr29() {
		return attr29;
	}
	
	public void setAttr29(String attr29) {
		this.attr29 = attr29;
	}
	public String getAttr30() {
		return attr30;
	}
	
	public void setAttr30(String attr30) {
		this.attr30 = attr30;
	}
	public String getAttr31() {
		return attr31;
	}
	
	public void setAttr31(String attr31) {
		this.attr31 = attr31;
	}
	public String getAttr32() {
		return attr32;
	}
	
	public void setAttr32(String attr32) {
		this.attr32 = attr32;
	}
	public String getAttr33() {
		return attr33;
	}
	
	public void setAttr33(String attr33) {
		this.attr33 = attr33;
	}
	public String getAttr34() {
		return attr34;
	}
	
	public void setAttr34(String attr34) {
		this.attr34 = attr34;
	}
	public String getAttr35() {
		return attr35;
	}
	
	public void setAttr35(String attr35) {
		this.attr35 = attr35;
	}
	public String getAttr36() {
		return attr36;
	}
	
	public void setAttr36(String attr36) {
		this.attr36 = attr36;
	}
	public String getAttr37() {
		return attr37;
	}
	
	public void setAttr37(String attr37) {
		this.attr37 = attr37;
	}
	public String getAttr38() {
		return attr38;
	}
	
	public void setAttr38(String attr38) {
		this.attr38 = attr38;
	}
	public String getAttr39() {
		return attr39;
	}
	
	public void setAttr39(String attr39) {
		this.attr39 = attr39;
	}
	public String getAttr40() {
		return attr40;
	}
	
	public void setAttr40(String attr40) {
		this.attr40 = attr40;
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
}

