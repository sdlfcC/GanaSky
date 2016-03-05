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
 * ��¼����Activityʵ��
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

	public static final String PREFS_NAME = "GanaSky�û���Ϣ";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		userInfo = new UserInfo();
		// ��ȡҳ���ϵ�ֵ
		et_userName = (EditText) findViewById(R.id.et_UserName);
		et_userPsd = (EditText) findViewById(R.id.et_UserPsd);
		// ��¼�ɹ�
		btnOk = (Button) findViewById(R.id.btn_Ok);
		btnCancel = (Button) findViewById(R.id.btn_Cancel);
		m_RadioGroup = (RadioGroup) findViewById(R.id.RadioGroup01);
		rb_save = (RadioButton) findViewById(R.id.RadioButtonOfSave);
		rd_notSave = (RadioButton) findViewById(R.id.RadioButtonOfNotSave);
		btnRegister = (Button) findViewById(R.id.btn_Register);
		m_RadioGroup.setOnCheckedChangeListener(m_RadioGroupSaveOrNot);
		clickKeyBoardEvent();
		onTouchClickEvent();
		// ��ȡStrings�е�ֵ
		urlLogin = this.getString(R.string.LoginaspURL);
		// ���û���Ϣ
		readUserName();
	}

	private void clickKeyBoardEvent() {
		// ���巽��(��������ķ���)
		// btnOk.setOnClickListener(btnOKClick);
		// btnCancel.setOnClickListener(btnCancelClick);
	}

	private void onTouchClickEvent() {
		btnOk.setOnTouchListener(btnTouchOKClick);
		btnCancel.setOnTouchListener(btnTouchCancelClick);
		// ע��
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
			// �����½���
			/* �½�һ��Intent���� */
			Intent intent = new Intent();
			/* ָ��intentҪ�������� */
			intent.setClass(LoginApp.this, RegisterApp.class);
			/* ����һ���µ�Activity */
			startActivity(intent);
			/* �رյ�ǰ��Activity */
			LoginApp.this.finish();
			return true;
		}
	};

	// �Ƿ񱣴��û�����
	RadioGroup.OnCheckedChangeListener m_RadioGroupSaveOrNot 
	            = new RadioGroup.OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub
			if (checkedId == rb_save.getId()) {
				isSave = true;
			} else if (checkedId == rd_notSave.getId()) {
				// ������
				isSave = false;
				// et_userName.setText(null);
			}
		}
	};

	/*
	 * ��¼�ķ���(���������)
	 */
	Button.OnClickListener btnOKClick = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {

			// ��������
			setLoginParams();
			// ��¼
			login();
		}
	};
	
	/**
	 * ������¼�ķ���
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
				// ���ص�¼����
				if (setLoginParams()) {
					// ��¼
					return login();
				}
			} else {
				DisplayToast("��������ʧ�ܣ�ȷ������������");
				return false;
			}
			return true;
		}
	};

	/**
	 * ���ò���
	 */
	private boolean setLoginParams() {
		if (et_userName.getText() != null && et_userName.getText().length() > 0) {
			userInfo.setUserName(et_userName.getText().toString());
		} else {
			DisplayToast("�û�������Ϊ�գ�");
			return false;
		}
		if (et_userPsd.getText() != null && et_userPsd.getText().length() > 0) {
			userInfo.setUserPwd(et_userPsd.getText().toString());
		} else {
			DisplayToast("���벻��Ϊ�գ�");
			return false;
		}
		return true;
	}

	/**
	 * ��¼�ķ���
	 */
	private boolean login() {
		try {
			String params = null;
			try {
				// ���ݵĲ����趨�����ʽ���Է�ֹ����
				params = URLEncoder.encode("strName", "UTF-8") + "="
						+ URLEncoder.encode(userInfo.getUserName(), "UTF-8");
				params += "&" + URLEncoder.encode("strPwd", "UTF-8") + "="
						+ URLEncoder.encode(userInfo.getUserPwd(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				Log.e(DEBUG_TAG, "��¼�����в������ݴ���");
			}
			// �õ��ַ�������
			resultData = ConnUrlHelper.getPostHttpURLConnByUrl(urlLogin, params);
			userInfo = getUserInfo(resultData);
			// �û���Ϣ����
			if (userInfo != null && userInfo.getUserId().length() > 0) {
				// �����û�
				SaveUserInfo();
				DisplayToast("�û���¼�ɹ�����ת����Ʊҳ�档");
				// ��������Ʊҳ��
				returnFligtListPage();
				return true;
			} else {
				DisplayToast("��û��ע���û�����¼ʧ�ܣ�");
				return false;
			}
			// ������try
		} catch (Exception e) {
			Log.e(DEBUG_TAG, "�����¼�����д���");
			DisplayToast("����δע�ᡢ����������������ԭ���¼ʧ�ܣ�");
			return false;
		}
	}

	/**
	 * ���ز�Ʊҳ��
	 */
	private void returnFligtListPage() {
		/* �½�һ��Intent���� */
		Intent intent = new Intent();
		/* ָ��intentҪ�������� */
		intent.setClass(LoginApp.this, TicketMainApp.class);
		/* ����һ���µ�Activity */
		startActivity(intent);
		/* �رյ�ǰ��Activity */
		LoginApp.this.finish();
	}

	/**
	 * ���û�
	 * 
	 * @param resultDatas
	 * @return
	 */
	private UserInfo getUserInfo(String resultDatas) {
		// ת��Ϊxml�ĵ�
		Document doc = DoXmlHelper.StringToxml(resultDatas);
		// ��ȡ�ĵ��е�Ԫ��
		Element root = doc.getDocumentElement();
		// �õ���Ҫ�Ľڵ㼯��
		NodeList userInfoNodeList = root.getElementsByTagName("Users");
		userInfo = new UserInfo();
		// ����Account�е�����Ԫ��
		for (int i = 0; i < userInfoNodeList.getLength(); i++) {
			// �õ�Account�е�����Ԫ��(������n��account)
			Element userInfoElement = (Element) userInfoNodeList.item(i);
			// ��������account�е�Ԫ�صĵ�һ���ڵ�
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
	 * ����ȡ����¼�ķ���
	 */
	Button.OnTouchListener btnTouchCancelClick = new Button.OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// ������Щ�����¼�����
			int iAction = event.getAction();
			if (iAction == MotionEvent.ACTION_CANCEL
					|| iAction == MotionEvent.ACTION_DOWN
					|| iAction == MotionEvent.ACTION_MOVE) {
				return false;
			}
			// �Ƿ񱣴��û���
			SaveUserInfo();
			// ������ҳ��
			returnMainPage();
			return true;
		}
	};

	/**
	 * ���ȡ���ķ���
	 */
	Button.OnClickListener btnCancelClick = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			// ��¼����
			returnMainPage();
		}
	};

	/**
	 * ������ҳ
	 */
	private void returnMainPage() {
		/* �½�һ��Intent���� */
		Intent intent = new Intent();
		/* ָ��intentҪ�������� */
		intent.setClass(LoginApp.this, GanaSkyMain.class);
		/* ����һ���µ�Activity */
		startActivity(intent);
		/* �رյ�ǰ��Activity */
		LoginApp.this.finish();
	}

	/**
	 * �Ƿ񱣴��û�
	 */
	private void SaveUserInfo() {
		// ����
		SharedPreferences uiState = getSharedPreferences(PREFS_NAME,
				Context.MODE_WORLD_READABLE);
		// ȡ�ñ༭����
		SharedPreferences.Editor editor = uiState.edit();

		// �����û���Ϣ
		if (userInfo != null) {
			editor.putBoolean("isSave", isSave);
			editor.putString("userName", userInfo.getUserName());
			editor.putString("userId", userInfo.getUserId());
			editor.putString("userMobile", userInfo.getUserMobilePhone());
			editor.putString("userEmail", userInfo.getUserEmail());
		}
		// �ύ����
		editor.commit();
	}

	/**
	 * �Ƿ���û�
	 */
	private void readUserName() {
		// ȡ�û��preferences����.
		SharedPreferences settings = getSharedPreferences(PREFS_NAME,
				Context.MODE_WORLD_READABLE);
		// ȡ��ֵ
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

	/* ��ʾToast */
	public void DisplayToast(String str) {
		Toast toast = Toast.makeText(this, str, Toast.LENGTH_SHORT);
		// ����toast��ʾ��λ��
		toast.setGravity(Gravity.TOP, 0, 220);
		// ��ʾ��Toast
		toast.show();
	}
}
