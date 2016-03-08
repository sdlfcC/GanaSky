package com.ganasky.model;

import java.util.List;
/**
 * 航班信息类
 * @author 
 *
 */
public class FlightInfo {
	
	private String CompanyCode;
	private String CompanyName;
	private String FlightNumber;
	private String PlaneModel;
	private String TakeOff;
	private String Landing;
	private String FromAirportCode;
	private String FromAirportName;
	private String FormatedFromAiport;
	private String LandingAirportCode;
	private String LandingAirportName;
	private String FormatedLandingAirport;
	private String IsPassBy;
	private String IsElectronicTicket;
	private String AirportConstructionFee;
	private String FuelSurcharge;
	private String Distance;
	private String AvailableClassCount;
	private String AvailableClassPolicyCount;
	private List<ClassInfo> classInfo;
	private String DateTime; //日期
	private String weekDay; 	//星期
	
	/*对属性进行封装（Get/Set）*/
	public String getCompanyCode() {
		return CompanyCode;
	}
	public void setCompanyCode(String companyCode) {
		CompanyCode = companyCode;
	}
	public String getCompanyName() {
		return CompanyName;
	}
	public void setCompanyName(String companyName) {
		CompanyName = companyName;
	}
	public String getFlightNumber() {
		return FlightNumber;
	}
	public void setFlightNumber(String flightNumber) {
		FlightNumber = flightNumber;
	}
	public String getPlaneModel() {
		return PlaneModel;
	}
	public void setPlaneModel(String planeModel) {
		PlaneModel = planeModel;
	}
	public String getTakeOff() {
		return TakeOff;
	}
	public void setTakeOff(String takeOff) {
		TakeOff = takeOff;
	}
	public String getLanding() {
		return Landing;
	}
	public void setLanding(String landing) {
		Landing = landing;
	}
	public String getFromAirportCode() {
		return FromAirportCode;
	}
	public void setFromAirportCode(String fromAirportCode) {
		FromAirportCode = fromAirportCode;
	}
	public String getFromAirportName() {
		return FromAirportName;
	}
	public void setFromAirportName(String fromAirportName) {
		FromAirportName = fromAirportName;
	}
	public String getFormatedFromAiport() {
		return FormatedFromAiport;
	}
	public void setFormatedFromAiport(String formatedFromAiport) {
		FormatedFromAiport = formatedFromAiport;
	}
	public String getLandingAirportCode() {
		return LandingAirportCode;
	}
	public void setLandingAirportCode(String landingAirportCode) {
		LandingAirportCode = landingAirportCode;
	}
	public String getLandingAirportName() {
		return LandingAirportName;
	}
	public void setLandingAirportName(String landingAirportName) {
		LandingAirportName = landingAirportName;
	}
	public String getFormatedLandingAirport() {
		return FormatedLandingAirport;
	}
	public void setFormatedLandingAirport(String formatedLandingAirport) {
		FormatedLandingAirport = formatedLandingAirport;
	}
	public String getIsPassBy() {
		return IsPassBy;
	}
	public void setIsPassBy(String isPassBy) {
		IsPassBy = isPassBy;
	}
	public String getIsElectronicTicket() {
		return IsElectronicTicket;
	}
	public void setIsElectronicTicket(String isElectronicTicket) {
		IsElectronicTicket = isElectronicTicket;
	}
	public String getAirportConstructionFee() {
		return AirportConstructionFee;
	}
	public void setAirportConstructionFee(String airportConstructionFee) {
		AirportConstructionFee = airportConstructionFee;
	}
	public String getFuelSurcharge() {
		return FuelSurcharge;
	}
	public void setFuelSurcharge(String fuelSurcharge) {
		FuelSurcharge = fuelSurcharge;
	}
	public String getDistance() {
		return Distance;
	}
	public void setDistance(String distance) {
		Distance = distance;
	}
	public String getAvailableClassCount() {
		return AvailableClassCount;
	}
	public void setAvailableClassCount(String availableClassCount) {
		AvailableClassCount = availableClassCount;
	}
	public String getAvailableClassPolicyCount() {
		return AvailableClassPolicyCount;
	}
	public void setAvailableClassPolicyCount(String availableClassPolicyCount) {
		AvailableClassPolicyCount = availableClassPolicyCount;
	}
	public List<ClassInfo> getClassInfo() {
		return classInfo;
	}
	public void setClassInfo(List<ClassInfo> classInfo) {
		this.classInfo = classInfo;
	}
	public String getDateTime() {
		return DateTime;
	}
	public void setDateTime(String dateTime) {
		DateTime = dateTime;
	}
	public String getWeekDay() {
		return weekDay;
	}
	public void setWeekDay(String weekDay) {
		this.weekDay = weekDay;
	}
}
