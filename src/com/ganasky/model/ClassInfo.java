package com.ganasky.model;
/**
 * �����λ��
 * @author 
 *
 */
public class ClassInfo {
	private String ClassCode;
	private String ClassName;
	private String AvailableSeat;
	private String PublicPrice;
	private String RefundPercent;
	private String SettlementPrice;
	private String IsPolicyAvailable;
	
	/*�����Խ��з�װ��Get/Set��*/
	public String getClassCode() {
		return ClassCode;
	}
	public void setClassCode(String classCode) {
		ClassCode = classCode;
	}
	public String getClassName() {
		return ClassName;
	}
	public void setClassName(String className) {
		ClassName = className;
	}
	public String getAvailableSeat() {
		return AvailableSeat;
	}
	public void setAvailableSeat(String availableSeat) {
		AvailableSeat = availableSeat;
	}
	public String getPublicPrice() {
		return PublicPrice;
	}
	public void setPublicPrice(String publicPrice) {
		PublicPrice = publicPrice;
	}
	public String getRefundPercent() {
		return RefundPercent;
	}
	public void setRefundPercent(String refundPercent) {
		RefundPercent = refundPercent;
	}
	public String getSettlementPrice() {
		return SettlementPrice;
	}
	public void setSettlementPrice(String settlementPrice) {
		SettlementPrice = settlementPrice;
	}
	public String getIsPolicyAvailable() {
		return IsPolicyAvailable;
	}
	public void setIsPolicyAvailable(String isPolicyAvailable) {
		IsPolicyAvailable = isPolicyAvailable;
	}
	
	

}
