package com.ganasky.model;

//package com.ganasky.model;

/**
 * 用户信息类
 * @author 
 *
 */
public  class UserInfo {
	public final static String USERNAME="userName";
	public final static String USERREALNAME="userRealName";
	public final static String CHECKLEVEL="checklevel";
	
	private String userName;
	private String userPwd;
	private String userRealName;
	private String checkLevel;
	private String userMobilePhone;
	private String userId;
	
	private String userEmail;
	
	
	public String getUserRealName() {
		return userRealName;
	}
	public void setUserRealName(String userRealName) {
		this.userRealName = userRealName;
	}
	public String getCheckLevel() {
		return checkLevel;
	}
	public void setCheckLevel(String checkLevel) {
		this.checkLevel = checkLevel;
	}

	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPwd() {
		return userPwd;
	}
	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}
	public String getUserMobilePhone() {
		return userMobilePhone;
	}
	public void setUserMobilePhone(String userMobilePhone) {
		this.userMobilePhone = userMobilePhone;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	

}

