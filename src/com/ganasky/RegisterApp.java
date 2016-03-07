package com.ganasky;

//package com.ganasky;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.ganasky.Help.ConnUrlHelper;
import com.ganasky.model.UserInfo;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 这是注册页面 实现注册功能
 * 
 * @author 
 * 
 */
public class RegisterApp extends Activity {
	private EditText ed_userName;
	private EditText ed_userPwd;
	private EditText ed_userRePwd;
	private EditText ed_userMobilePhone;
	private EditText ed_userEmail;
	private Button btn_Register;
	private Button btn_RegisterCancel;
	private UserInfo userInfo;
	private final String DEBUG_TAG = "RegisterApp";
	private String urlRigester = null;

	/** 只是ganaSky的注册界面 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		// 变量声明
		userInfo = new UserInfo();
		ed_userName = (EditText) findViewById(R.id.ed_registeruserName);
		ed_userPwd = (EditText) findViewById(R.id.ed_registerPwd);
		ed_userRePwd = (EditText) findViewById(R.id.ed_registerRePwd);
		ed_userMobilePhone = (EditText) findViewById(R.id.ed_registerUserMoblePhone);
		ed_userEmail = (EditText) findViewById(R.id.ed_registerUserEmail);
		btn_Register = (Button) findViewById(R.id.btn_Register);
		btn_RegisterCancel=(Button) findViewById(R.id.btn_RegisterCancel);

		urlRigester = this.getString(R.string.RegisterURL); // 获取注册连接

		// 设置监听
		btn_Register.setOnTouchListener(btnRegisterTouch);
		btn_RegisterCancel.setOnTouchListener(btnRegisterCancelTouch);

	}

	Button.OnTouchListener btnRegisterTouch = new Button.OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// 设置哪些触屏事件可用
			int iAction = event.getAction();
			if (iAction == MotionEvent.ACTION_CANCEL
					|| iAction == MotionEvent.ACTION_DOWN
					|| iAction == MotionEvent.ACTION_MOVE) {
				return false;
			}
			// 客户端判断
			if (checkUserInfo()) {
				// 赋值
				setUserInfo();
				// 注册
				Register(JoinRegisterUserInfo());
			}
			return true;
		}
	};

	/**
	 * 赋值用户信息
	 */
	private void setUserInfo() {
		userInfo.setUserName(ed_userName.getText().toString());
		userInfo.setUserPwd(ed_userPwd.getText().toString());
		userInfo.setUserMobilePhone(ed_userMobilePhone.getText().toString());
		userInfo.setUserEmail(ed_userEmail.getText().toString());
	}

	/**
	 * 注册
	 * @param userInfo
	 *            （用户填写的用户）
	 */
	private boolean Register(String userInfo) {
		String params = null;
		String userInfo1 = userInfo;
		try {
			// 传递的参数设定编码格式，以防止乱码
			params = URLEncoder.encode("userInfo", "UTF-8") + "="
					+ URLEncoder.encode(userInfo1, "gb2312");
		} catch (UnsupportedEncodingException e) {
			Log.e(DEBUG_TAG, "注册方法中参数传递错误！");
		}
		// 得到字符串集合
		String resultData = null;
		resultData = ConnUrlHelper.getPostHttpURLConnByUrl(urlRigester, params);
		//解析结果
		if (resultData != null && resultData.length() > 0) {
			if (resultData.contains("<Result>")) {
				resultData = resultData.substring(
						resultData.indexOf("<Result>") + 8,
						resultData.lastIndexOf("</Result>"));
				String flg="0";
				flg=resultData.substring(0,1);
				if(flg=="0"){
					DisplayToast(resultData);
					return false;					
				}else{
					//注册成功默认登录
					String id=resultData.substring(0,resultData.indexOf("/"));
					//保存用户信息
					SaveUserInfo(id);
					DisplayToast("用户注册成功，跳转到查票页面！");
					returnFligtListPage();
					return true;
				}
			} 
		}			
		return false;

	}
	/**
	 * 返回查票页面
	 */
	private void returnFligtListPage() {
		/* 新建一个Intent对象 */
		Intent intent = new Intent();
		/* 指定intent要启动的类 */
		intent.setClass(RegisterApp.this, TicketMainApp.class);
		/* 启动一个新的Activity */
		startActivity(intent);
		/* 关闭当前的Activity */
		RegisterApp.this.finish();
	}
	
	/**
	 * 是否保存用户
	 */
	private void SaveUserInfo(String id) {
		// 保存
		SharedPreferences uiState = getSharedPreferences(LoginApp.PREFS_NAME,
				Context.MODE_WORLD_READABLE);
		// 取得编辑对象
		SharedPreferences.Editor editor = uiState.edit();		 		
		// 保存用户信息
		if (userInfo != null) {
			editor.putString("userName", userInfo.getUserName());
			editor.putString("userId",id);
			editor.putString("userMobile", userInfo.getUserMobilePhone());
			editor.putString("userEmail", userInfo.getUserEmail());
		}
		// 提交保存
		editor.commit();
	}

	/**
	 * 触屏取消登录的方法
	 */
	Button.OnTouchListener btnRegisterCancelTouch 
	                                                 = new Button.OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// 设置哪些触屏事件可用
			int iAction = event.getAction();
			if (iAction == MotionEvent.ACTION_CANCEL
					|| iAction == MotionEvent.ACTION_DOWN
					|| iAction == MotionEvent.ACTION_MOVE) {
				return false;
			}
			// 返回到登录画面用户名
			getToLoginPage();			
			return true;
		}
	};

	/**
	 * 跳转到登录页面
	 */
	private void getToLoginPage() {
		/* 新建一个Intent对象 */
		Intent intent = new Intent();
		/* 指定intent要启动的类 */
		intent.setClass(RegisterApp.this, LoginApp.class);
		/* 启动一个新的Activity */
		startActivity(intent);
		/* 关闭当前的Activity */
		RegisterApp.this.finish();
	}

	/**
	 * 验证用户信息
	 */
	private boolean checkUserInfo() {
		if (ed_userName.getText() != null && ed_userName.getText().length() > 0) {
		} else {
			DisplayToast("用户名不能为空！");
			return false;
		}
		if (ed_userName.getText().length() < 6) {
			DisplayToast("用户名太短！");
			return false;
		}

		if (ed_userPwd.getText() != null && ed_userPwd.getText().length() > 0) {

		} else {
			DisplayToast("密码不能为空！");
			return false;
		}
		if (ed_userPwd.getText().length() < 6) {
			DisplayToast("密码太短！");
			return false;
		}

		if (ed_userRePwd.getText() != null
				&& ed_userRePwd.getText().length() > 0
				&& ed_userPwd.getText().toString()
						.equals(ed_userRePwd.getText().toString())) {

		} else {
			DisplayToast("两次密码不一致！");
			return false;
		}

		if (ed_userMobilePhone.getText() != null
				&& ed_userMobilePhone.getText().length() > 0) {

		} else {
			DisplayToast("请填写电话号码！");
			return false;
		}
		if (isNumeric(ed_userMobilePhone.getText().toString())) {

		} else {
			DisplayToast("手机只能为数字！");
			return false;

		}
		if ( ed_userMobilePhone.getText().length()==11) {

		} else {
			DisplayToast("手机号码必须是十一位！");
			return false;
		}

		if (ed_userEmail.getText() != null
				&& ed_userEmail.getText().length() > 0) {
		} else {
			DisplayToast("请填写邮箱！");
			return false;
		}

		if (getEmail(ed_userEmail.getText().toString())) {

		} else {
			DisplayToast("邮箱格式不正确！");
			return false;
		}
		return true;

	}

	/**
	 * 数字验证
	 * 
	 * @param str
	 * @return
	 */
	private boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	/**
	 * 邮箱验证
	 * 
	 * @param strEmail
	 * @return
	 */
	private boolean getEmail(String strEmail) {
		Pattern p = Pattern
				.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
		Matcher m = p.matcher(strEmail);
		return m.find();
	}

	/**
	 * 拼接用户信息
	 * @return
	 */
	private String JoinRegisterUserInfo() {
		String userInfoStr = "";
		userInfoStr += "<userInfo>";
		userInfoStr += "<userName>" + userInfo.getUserName() + "</userName>";
		userInfoStr += "<userPwd>" + userInfo.getUserPwd() + "</userPwd>";
		userInfoStr += "<userMobilePhone>" + userInfo.getUserMobilePhone()
				+ "</userMobilePhone>";
		userInfoStr += "<userEmail>" + userInfo.getUserEmail() + "</userEmail>";
		userInfoStr += "</userInfo>";
		return userInfoStr;
	}

	/* 显示Toast */
	public void DisplayToast(String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}

}
