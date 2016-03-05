package com.ganasky;

import android.app.Activity;


//package com.ganasky;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import com.ganasky.Help.ConnUrlHelper;
import com.ganasky.Help.DoXmlHelper;
import com.ganasky.model.UserInfo;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * 登录界面Activity实现
 * @author
 *
 */
public class LoginApp extends Activity {
	ProgressDialog m_Dialog;
	UserInfo userInfo;
	private EditText et_userName;
	private EditText et_userPsd;
	private Button btnOk;
	private Button btnCancel;
	private String urlLogin;
	private RadioButton rb_save, rd_notSave;
	private RadioGroup m_RadioGroup;
	private Button btnRegister;
	String resultData = "";

	private String saveOfUserName = null;
	private boolean isSave = false;
	private final String DEBUG_TAG = "LoginApp";

	public static final String PREFS_NAME = "GanaSky用户信息";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		userInfo = new UserInfo();
		// 获取页面上的值
		et_userName = (EditText) findViewById(R.id.et_UserName);
		et_userPsd = (EditText) findViewById(R.id.et_UserPsd);
		// 登录成功
		btnOk = (Button) findViewById(R.id.btn_Ok);
		btnCancel = (Button) findViewById(R.id.btn_Cancel);
		m_RadioGroup = (RadioGroup) findViewById(R.id.RadioGroup01);
		rb_save = (RadioButton) findViewById(R.id.RadioButtonOfSave);
		rd_notSave = (RadioButton) findViewById(R.id.RadioButtonOfNotSave);
		btnRegister = (Button) findViewById(R.id.btn_Register);
		m_RadioGroup.setOnCheckedChangeListener(m_RadioGroupSaveOrNot);
		clickKeyBoardEvent();
		onTouchClickEvent();
		// 读取Strings中的值
		urlLogin = this.getString(R.string.LoginaspURL);
		// 读用户信息
		readUserName();
	}

	private void clickKeyBoardEvent() {
		// 定义方法(按键点击的方法)
		// btnOk.setOnClickListener(btnOKClick);
		// btnCancel.setOnClickListener(btnCancelClick);
	}

	private void onTouchClickEvent() {
		btnOk.setOnTouchListener(btnTouchOKClick);
		btnCancel.setOnTouchListener(btnTouchCancelClick);
		// 注册
		btnRegister.setOnTouchListener(btnRegisterTouchClick);
	}

	Button.OnTouchListener btnRegisterTouchClick = new Button.OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			int iAction = event.getAction();
			if (iAction == MotionEvent.ACTION_CANCEL
					|| iAction == MotionEvent.ACTION_DOWN
					|| iAction == MotionEvent.ACTION_MOVE) {
				return false;
			}
			// 创建新界面
			/* 新建一个Intent对象 */
			Intent intent = new Intent();
			/* 指定intent要启动的类 */
			intent.setClass(LoginApp.this, RegisterApp.class);
			/* 启动一个新的Activity */
			startActivity(intent);
			/* 关闭当前的Activity */
			LoginApp.this.finish();
			return true;
		}
	};

	// 是否保存用户姓名
	RadioGroup.OnCheckedChangeListener m_RadioGroupSaveOrNot 
	            = new RadioGroup.OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub
			if (checkedId == rb_save.getId()) {
				isSave = true;
			} else if (checkedId == rd_notSave.getId()) {
				// 不保存
				isSave = false;
				// et_userName.setText(null);
			}
		}
	};

	/*
	 * 登录的方法(点击按键版)
	 */
	Button.OnClickListener btnOKClick = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {

			// 出来参数
			setLoginParams();
			// 登录
			login();
		}
	};
	
	/**
	 * 触屏登录的方法
	 */
	Button.OnTouchListener btnTouchOKClick = new Button.OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			int iAction = event.getAction();
			if (iAction == MotionEvent.ACTION_CANCEL
					|| iAction == MotionEvent.ACTION_DOWN
					|| iAction == MotionEvent.ACTION_MOVE) {
				return false;
			}
			if (ConnUrlHelper.hasInternet(LoginApp.this)) {
				// 加载登录参数
				if (setLoginParams()) {
					// 登录
					return login();
				}
			} else {
				DisplayToast("网络连接失败，确保网络正常。");
				return false;
			}
			return true;
		}
	};

	/**
	 * 设置参数
	 */
	private boolean setLoginParams() {
		if (et_userName.getText() != null && et_userName.getText().length() > 0) {
			userInfo.setUserName(et_userName.getText().toString());
		} else {
			DisplayToast("用户名不能为空！");
			return false;
		}
		if (et_userPsd.getText() != null && et_userPsd.getText().length() > 0) {
			userInfo.setUserPwd(et_userPsd.getText().toString());
		} else {
			DisplayToast("密码不能为空！");
			return false;
		}
		return true;
	}

	/**
	 * 登录的方法
	 */
	private boolean login() {
		try {
			String params = null;
			try {
				// 传递的参数设定编码格式，以防止乱码
				params = URLEncoder.encode("strName", "UTF-8") + "="
						+ URLEncoder.encode(userInfo.getUserName(), "UTF-8");
				params += "&" + URLEncoder.encode("strPwd", "UTF-8") + "="
						+ URLEncoder.encode(userInfo.getUserPwd(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				Log.e(DEBUG_TAG, "登录方法中参数传递错误！");
			}
			// 得到字符串集合
			resultData = ConnUrlHelper.getPostHttpURLConnByUrl(urlLogin, params);
			userInfo = getUserInfo(resultData);
			// 用户信息保存
			if (userInfo != null && userInfo.getUserId().length() > 0) {
				// 保存用户
				SaveUserInfo();
				DisplayToast("用户登录成功！跳转到订票页面。");
				// 调整到查票页面
				returnFligtListPage();
				return true;
			} else {
				DisplayToast("您没有注册用户，登录失败！");
				return false;
			}
			// 最外层的try
		} catch (Exception e) {
			Log.e(DEBUG_TAG, "点击登录方法有错误！");
			DisplayToast("由于未注册、密码错误、网络问题等原因登录失败！");
			return false;
		}
	}

	/**
	 * 返回查票页面
	 */
	private void returnFligtListPage() {
		/* 新建一个Intent对象 */
		Intent intent = new Intent();
		/* 指定intent要启动的类 */
		intent.setClass(LoginApp.this, TicketMainApp.class);
		/* 启动一个新的Activity */
		startActivity(intent);
		/* 关闭当前的Activity */
		LoginApp.this.finish();
	}

	/**
	 * 读用户
	 * 
	 * @param resultDatas
	 * @return
	 */
	private UserInfo getUserInfo(String resultDatas) {
		// 转换为xml文档
		Document doc = DoXmlHelper.StringToxml(resultDatas);
		// 读取文档中的元素
		Element root = doc.getDocumentElement();
		// 得到想要的节点集合
		NodeList userInfoNodeList = root.getElementsByTagName("Users");
		userInfo = new UserInfo();
		// 遍历Account中的所有元素
		for (int i = 0; i < userInfoNodeList.getLength(); i++) {
			// 得到Account中的所有元素(可以有n个account)
			Element userInfoElement = (Element) userInfoNodeList.item(i);
			// 读到具体account中的元素的第一个节点
			Element userId = (Element) userInfoElement.getElementsByTagName(
					"Id").item(0);
			Element loginName = (Element) userInfoElement.getElementsByTagName(
					"LoginName").item(0);
			Element mobileNo = (Element) userInfoElement.getElementsByTagName(
					"MobileNo").item(0);
			Element email = (Element) userInfoElement.getElementsByTagName(
					"E_Mail").item(0);

			userInfo.setUserId(userId.getFirstChild().getNodeValue());
			userInfo.setUserName(loginName.getFirstChild().getNodeValue());
			userInfo.setUserMobilePhone(mobileNo.getFirstChild().getNodeValue());
			userInfo.setUserEmail(email.getFirstChild().getNodeValue());
		}

		return userInfo;
	}

	/**
	 * 触屏取消登录的方法
	 */
	Button.OnTouchListener btnTouchCancelClick = new Button.OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// 设置哪些触屏事件可用
			int iAction = event.getAction();
			if (iAction == MotionEvent.ACTION_CANCEL
					|| iAction == MotionEvent.ACTION_DOWN
					|| iAction == MotionEvent.ACTION_MOVE) {
				return false;
			}
			// 是否保存用户名
			SaveUserInfo();
			// 返回主页面
			returnMainPage();
			return true;
		}
	};

	/**
	 * 点击取消的方法
	 */
	Button.OnClickListener btnCancelClick = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			// 登录窗口
			returnMainPage();
		}
	};

	/**
	 * 返回主页
	 */
	private void returnMainPage() {
		/* 新建一个Intent对象 */
		Intent intent = new Intent();
		/* 指定intent要启动的类 */
		intent.setClass(LoginApp.this, GanaSkyMain.class);
		/* 启动一个新的Activity */
		startActivity(intent);
		/* 关闭当前的Activity */
		LoginApp.this.finish();
	}

	/**
	 * 是否保存用户
	 */
	private void SaveUserInfo() {
		// 保存
		SharedPreferences uiState = getSharedPreferences(PREFS_NAME,
				Context.MODE_WORLD_READABLE);
		// 取得编辑对象
		SharedPreferences.Editor editor = uiState.edit();

		// 保存用户信息
		if (userInfo != null) {
			editor.putBoolean("isSave", isSave);
			editor.putString("userName", userInfo.getUserName());
			editor.putString("userId", userInfo.getUserId());
			editor.putString("userMobile", userInfo.getUserMobilePhone());
			editor.putString("userEmail", userInfo.getUserEmail());
		}
		// 提交保存
		editor.commit();
	}

	/**
	 * 是否读用户
	 */
	private void readUserName() {
		// 取得活动的preferences对象.
		SharedPreferences settings = getSharedPreferences(PREFS_NAME,
				Context.MODE_WORLD_READABLE);
		// 取得值
		saveOfUserName = settings.getString("userName", userInfo.getUserName());
		isSave=settings.getBoolean("isSave", isSave);
		if (isSave && saveOfUserName != null) {
			et_userName.setText(saveOfUserName);
			rb_save.setChecked(true);
		} else {
			et_userName.setText("");
			rd_notSave.setChecked(true);
		}
	}

	/* 显示Toast */
	public void DisplayToast(String str) {
		Toast toast = Toast.makeText(this, str, Toast.LENGTH_SHORT);
		// 设置toast显示的位置
		toast.setGravity(Gravity.TOP, 0, 220);
		// 显示该Toast
		toast.show();
	}
}
