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
		// 绑定Layout里面的ListView
		orderSeccessresult = (TextView) findViewById(R.id.ordersuccess_result);
		btnExit=(Button) findViewById(R.id.ordersuccess_exit);		
		btnReturn = (Button) findViewById(R.id.ordersuccess_btnReturn);
		
		Intent intent = getIntent();
		result = intent.getStringExtra("result");
		if (result != null && result.length() > 0) {
			//显示预订结果
			orderSeccessresult.setText(result);
			
			//消息提示功能（通过消息提示显示预订结果）
			notifyMessage(result);
		}
				
		btnReturn.setOnTouchListener(ReturnMainPage);
		btnExit.setOnTouchListener(ExitApp);
	}
	
	private void notifyMessage(String result){
		// 获取系统服务（消息管理）
		notificationManager = (NotificationManager) this
				.getSystemService(NOTIFICATION_SERVICE);
		// 点击通知时转移内容
		intent = new Intent(this, OrderSuccessNotify.class);
		// 设置传递的参数
		intent.putExtra("result", result);
		
		// 设置点击通知时显示内容的类
		pendIntent = PendingIntent.getActivity(this, 0, intent, 0);
		notification = new Notification();
		
		notification.icon = R.drawable.icon;// 设置在状态栏显示的图标
		notification.tickerText = "预订成功的通知内容.......";//设置在状态栏显示的内容
		notification.defaults = Notification.DEFAULT_SOUND;// 默认的声音
		// 设置通知显示的参数
		notification.setLatestEventInfo(OrderSuccess.this,
				"OrderSuccess", "预订成功通知", pendIntent);
		// 执行通知
		notificationManager.notify(0, notification);
		
	}

	/**
	 * 返回查票
	 */
	Button.OnTouchListener ReturnMainPage = new Button.OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// 设置哪些触屏事件可用
			int iAction = event.getAction();
			if (iAction == MotionEvent.ACTION_CANCEL
					|| iAction == MotionEvent.ACTION_DOWN
					|| iAction == MotionEvent.ACTION_MOVE) {
				return false;
			}
			// 返回主页面
			returnMainPage();
			return true;
		}
	};
	
	/**
	 * 退出程序
	 */
	Button.OnTouchListener ExitApp = new Button.OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// 设置哪些触屏事件可用
			int iAction = event.getAction();
			if (iAction == MotionEvent.ACTION_CANCEL
					|| iAction == MotionEvent.ACTION_DOWN
					|| iAction == MotionEvent.ACTION_MOVE) {
				return false;
			}
			// 返回主页面2.2
			//exitApp1();
			RemoveUserInfo();
			exitApp2();
			return true;
		}

	};
	
	
	private void RemoveUserInfo() {		
		// 取得活动的preferences对象.
		SharedPreferences settings = getSharedPreferences(LoginApp.PREFS_NAME,
				Context.MODE_WORLD_READABLE);
		SharedPreferences.Editor editor = settings.edit();  
		editor.remove("userMobile");
		editor.remove("userId");
		editor.remove("userEmail");
		 editor.commit();   		
	}
	/**
	 * 2.2退出
	 */
	private void exitApp2(){
		Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
        System.exit(0);	
	}
	
	/**
	 * 2.1退出
	 */
	private void exitApp1(){
		ActivityManager am = (ActivityManager)getSystemService (Context.ACTIVITY_SERVICE); 
	    am.restartPackage(getPackageName()); 		
	}

	/**
	 * 返回主页
	 */
	private void returnMainPage() {
		/* 新建一个Intent对象 */
		Intent intent = new Intent();
		/* 指定intent要启动的类 */
		intent.setClass(OrderSuccess.this, TicketMainApp.class);
		/* 启动一个新的Activity */
		startActivity(intent);
		/* 关闭当前的Activity */
		OrderSuccess.this.finish();
	}

}
