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
 * ����ע��ҳ�� ʵ��ע�Ṧ��
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

	/** ֻ��ganaSky��ע����� */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		// ��������
		userInfo = new UserInfo();
		ed_userName = (EditText) findViewById(R.id.ed_registeruserName);
		ed_userPwd = (EditText) findViewById(R.id.ed_registerPwd);
		ed_userRePwd = (EditText) findViewById(R.id.ed_registerRePwd);
		ed_userMobilePhone = (EditText) findViewById(R.id.ed_registerUserMoblePhone);
		ed_userEmail = (EditText) findViewById(R.id.ed_registerUserEmail);
		btn_Register = (Button) findViewById(R.id.btn_Register);
		btn_RegisterCancel=(Button) findViewById(R.id.btn_RegisterCancel);

		urlRigester = this.getString(R.string.RegisterURL); // ��ȡע������

		// ���ü���
		btn_Register.setOnTouchListener(btnRegisterTouch);
		btn_RegisterCancel.setOnTouchListener(btnRegisterCancelTouch);

	}

	Button.OnTouchListener btnRegisterTouch = new Button.OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// ������Щ�����¼�����
			int iAction = event.getAction();
			if (iAction == MotionEvent.ACTION_CANCEL
					|| iAction == MotionEvent.ACTION_DOWN
					|| iAction == MotionEvent.ACTION_MOVE) {
				return false;
			}
			// �ͻ����ж�
			if (checkUserInfo()) {
				// ��ֵ
				setUserInfo();
				// ע��
				Register(JoinRegisterUserInfo());
			}
			return true;
		}
	};

	/**
	 * ��ֵ�û���Ϣ
	 */
	private void setUserInfo() {
		userInfo.setUserName(ed_userName.getText().toString());
		userInfo.setUserPwd(ed_userPwd.getText().toString());
		userInfo.setUserMobilePhone(ed_userMobilePhone.getText().toString());
		userInfo.setUserEmail(ed_userEmail.getText().toString());
	}

	/**
	 * ע��
	 * @param userInfo
	 *            ���û���д���û���
	 */
	private boolean Register(String userInfo) {
		String params = null;
		String userInfo1 = userInfo;
		try {
			// ���ݵĲ����趨�����ʽ���Է�ֹ����
			params = URLEncoder.encode("userInfo", "UTF-8") + "="
					+ URLEncoder.encode(userInfo1, "gb2312");
		} catch (UnsupportedEncodingException e) {
			Log.e(DEBUG_TAG, "ע�᷽���в������ݴ���");
		}
		// �õ��ַ�������
		String resultData = null;
		resultData = ConnUrlHelper.getPostHttpURLConnByUrl(urlRigester, params);
		//�������
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
					//ע��ɹ�Ĭ�ϵ�¼
					String id=resultData.substring(0,resultData.indexOf("/"));
					//�����û���Ϣ
					SaveUserInfo(id);
					DisplayToast("�û�ע��ɹ�����ת����Ʊҳ�棡");
					returnFligtListPage();
					return true;
				}
			} 
		}			
		return false;

	}
	/**
	 * ���ز�Ʊҳ��
	 */
	private void returnFligtListPage() {
		/* �½�һ��Intent���� */
		Intent intent = new Intent();
		/* ָ��intentҪ�������� */
		intent.setClass(RegisterApp.this, TicketMainApp.class);
		/* ����һ���µ�Activity */
		startActivity(intent);
		/* �رյ�ǰ��Activity */
		RegisterApp.this.finish();
	}
	
	/**
	 * �Ƿ񱣴��û�
	 */
	private void SaveUserInfo(String id) {
		// ����
		SharedPreferences uiState = getSharedPreferences(LoginApp.PREFS_NAME,
				Context.MODE_WORLD_READABLE);
		// ȡ�ñ༭����
		SharedPreferences.Editor editor = uiState.edit();		 		
		// �����û���Ϣ
		if (userInfo != null) {
			editor.putString("userName", userInfo.getUserName());
			editor.putString("userId",id);
			editor.putString("userMobile", userInfo.getUserMobilePhone());
			editor.putString("userEmail", userInfo.getUserEmail());
		}
		// �ύ����
		editor.commit();
	}

	/**
	 * ����ȡ����¼�ķ���
	 */
	Button.OnTouchListener btnRegisterCancelTouch 
	                                                 = new Button.OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// ������Щ�����¼�����
			int iAction = event.getAction();
			if (iAction == MotionEvent.ACTION_CANCEL
					|| iAction == MotionEvent.ACTION_DOWN
					|| iAction == MotionEvent.ACTION_MOVE) {
				return false;
			}
			// ���ص���¼�����û���
			getToLoginPage();			
			return true;
		}
	};

	/**
	 * ��ת����¼ҳ��
	 */
	private void getToLoginPage() {
		/* �½�һ��Intent���� */
		Intent intent = new Intent();
		/* ָ��intentҪ�������� */
		intent.setClass(RegisterApp.this, LoginApp.class);
		/* ����һ���µ�Activity */
		startActivity(intent);
		/* �رյ�ǰ��Activity */
		RegisterApp.this.finish();
	}

	/**
	 * ��֤�û���Ϣ
	 */
	private boolean checkUserInfo() {
		if (ed_userName.getText() != null && ed_userName.getText().length() > 0) {
		} else {
			DisplayToast("�û�������Ϊ�գ�");
			return false;
		}
		if (ed_userName.getText().length() < 6) {
			DisplayToast("�û���̫�̣�");
			return false;
		}

		if (ed_userPwd.getText() != null && ed_userPwd.getText().length() > 0) {

		} else {
			DisplayToast("���벻��Ϊ�գ�");
			return false;
		}
		if (ed_userPwd.getText().length() < 6) {
			DisplayToast("����̫�̣�");
			return false;
		}

		if (ed_userRePwd.getText() != null
				&& ed_userRePwd.getText().length() > 0
				&& ed_userPwd.getText().toString()
						.equals(ed_userRePwd.getText().toString())) {

		} else {
			DisplayToast("�������벻һ�£�");
			return false;
		}

		if (ed_userMobilePhone.getText() != null
				&& ed_userMobilePhone.getText().length() > 0) {

		} else {
			DisplayToast("����д�绰���룡");
			return false;
		}
		if (isNumeric(ed_userMobilePhone.getText().toString())) {

		} else {
			DisplayToast("�ֻ�ֻ��Ϊ���֣�");
			return false;

		}
		if ( ed_userMobilePhone.getText().length()==11) {

		} else {
			DisplayToast("�ֻ����������ʮһλ��");
			return false;
		}

		if (ed_userEmail.getText() != null
				&& ed_userEmail.getText().length() > 0) {
		} else {
			DisplayToast("����д���䣡");
			return false;
		}

		if (getEmail(ed_userEmail.getText().toString())) {

		} else {
			DisplayToast("�����ʽ����ȷ��");
			return false;
		}
		return true;

	}

	/**
	 * ������֤
	 * 
	 * @param str
	 * @return
	 */
	private boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	/**
	 * ������֤
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
	 * ƴ���û���Ϣ
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

	/* ��ʾToast */
	public void DisplayToast(String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}

}
