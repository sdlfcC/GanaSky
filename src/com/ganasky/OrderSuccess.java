package com.ganasky;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class OrderSuccess extends Activity {
	// private final String DEBUG_TAG = "OrderSuccess";
	private String result;
	private TextView orderSeccessresult;
	private Button btnReturn;
	private Button btnExit;
	
	private Notification notification;  
	private NotificationManager notificationManager;
	private Intent intent;
	private PendingIntent pendIntent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ordersuccess);
		// ��Layout�����ListView
		orderSeccessresult = (TextView) findViewById(R.id.ordersuccess_result);
		btnExit=(Button) findViewById(R.id.ordersuccess_exit);		
		btnReturn = (Button) findViewById(R.id.ordersuccess_btnReturn);
		
		Intent intent = getIntent();
		result = intent.getStringExtra("result");
		if (result != null && result.length() > 0) {
			//��ʾԤ�����
			orderSeccessresult.setText(result);
			
			//��Ϣ��ʾ���ܣ�ͨ����Ϣ��ʾ��ʾԤ�������
			notifyMessage(result);
		}
				
		btnReturn.setOnTouchListener(ReturnMainPage);
		btnExit.setOnTouchListener(ExitApp);
	}
	
	private void notifyMessage(String result){
		// ��ȡϵͳ������Ϣ����
		notificationManager = (NotificationManager) this
				.getSystemService(NOTIFICATION_SERVICE);
		// ���֪ͨʱת������
		intent = new Intent(this, OrderSuccessNotify.class);
		// ���ô��ݵĲ���
		intent.putExtra("result", result);
		
		// ���õ��֪ͨʱ��ʾ���ݵ���
		pendIntent = PendingIntent.getActivity(this, 0, intent, 0);
		notification = new Notification();
		
		notification.icon = R.drawable.icon;// ������״̬����ʾ��ͼ��
		notification.tickerText = "Ԥ���ɹ���֪ͨ����.......";//������״̬����ʾ������
		notification.defaults = Notification.DEFAULT_SOUND;// Ĭ�ϵ�����
		// ����֪ͨ��ʾ�Ĳ���
		notification.setLatestEventInfo(OrderSuccess.this,
				"OrderSuccess", "Ԥ���ɹ�֪ͨ", pendIntent);
		// ִ��֪ͨ
		notificationManager.notify(0, notification);
		
	}

	/**
	 * ���ز�Ʊ
	 */
	Button.OnTouchListener ReturnMainPage = new Button.OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// ������Щ�����¼�����
			int iAction = event.getAction();
			if (iAction == MotionEvent.ACTION_CANCEL
					|| iAction == MotionEvent.ACTION_DOWN
					|| iAction == MotionEvent.ACTION_MOVE) {
				return false;
			}
			// ������ҳ��
			returnMainPage();
			return true;
		}
	};
	
	/**
	 * �˳�����
	 */
	Button.OnTouchListener ExitApp = new Button.OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// ������Щ�����¼�����
			int iAction = event.getAction();
			if (iAction == MotionEvent.ACTION_CANCEL
					|| iAction == MotionEvent.ACTION_DOWN
					|| iAction == MotionEvent.ACTION_MOVE) {
				return false;
			}
			// ������ҳ��2.2
			//exitApp1();
			RemoveUserInfo();
			exitApp2();
			return true;
		}

	};
	
	
	private void RemoveUserInfo() {		
		// ȡ�û��preferences����.
		SharedPreferences settings = getSharedPreferences(LoginApp.PREFS_NAME,
				Context.MODE_WORLD_READABLE);
		SharedPreferences.Editor editor = settings.edit();  
		editor.remove("userMobile");
		editor.remove("userId");
		editor.remove("userEmail");
		 editor.commit();   		
	}
	/**
	 * 2.2�˳�
	 */
	private void exitApp2(){
		Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
        System.exit(0);	
	}
	
	/**
	 * 2.1�˳�
	 */
	private void exitApp1(){
		ActivityManager am = (ActivityManager)getSystemService (Context.ACTIVITY_SERVICE); 
	    am.restartPackage(getPackageName()); 		
	}

	/**
	 * ������ҳ
	 */
	private void returnMainPage() {
		/* �½�һ��Intent���� */
		Intent intent = new Intent();
		/* ָ��intentҪ�������� */
		intent.setClass(OrderSuccess.this, TicketMainApp.class);
		/* ����һ���µ�Activity */
		startActivity(intent);
		/* �رյ�ǰ��Activity */
		OrderSuccess.this.finish();
	}

}
