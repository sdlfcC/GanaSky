package com.ganasky;

//package com.ganasky;

import com.ganasky.Help.ConnUrlHelper;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

/**
 * 这是主页面
 * @author 
 * 
 */
public class GanaSkyMain extends Activity {
	private int item = 0;
	private GanaSkyImageAdapter imageApter;

	/** 这是ganaSky的主页面 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 加载主页面
		setContentView(R.layout.main);

		// 取得GridView对象
		GridView gridview = (GridView) findViewById(R.id.gridview);

		// 初始化图片容器对象
		imageApter = new GanaSkyImageAdapter(this);

		// 添加元素给gridview控件
		gridview.setAdapter(imageApter);

		// 设置订票系统的背景
		gridview.setBackgroundResource(R.drawable.bg0);

		// 事件监听
		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				item = position + 1;
				switch (item) {
				 /*跳转到登录界面*/
				case 1:
					/* 新建一个Intent对象 */
					Intent intent = new Intent();
				 /* 指定intent要启动的类，可以先建LoginApp.java文件，
				 * 在后面的章节中再实现 */
					intent.setClass(GanaSkyMain.this, LoginApp.class);
					/* 启动一个新的Activity */
					startActivity(intent);
					/* 关闭当前的Activity */
					GanaSkyMain.this.finish();
					break;
					
				 /*跳转到查票主界面*/
				case 2:
					/*判断是否有网络接入*/
					if (ConnUrlHelper.hasInternet(GanaSkyMain.this)) {
						/* 新建一个Intent对象 */
						Intent intent2 = new Intent();
						/* 指定intent要启动的类 */
						intent2.setClass(GanaSkyMain.this, TicketMainApp.class);
						/* 启动一个新的Activity */
						startActivity(intent2);
						/* 关闭当前的Activity */
						GanaSkyMain.this.finish();
					} else {
						DisplayToast("网络连接失败,不能订票！");
					}
					break;
				case 3:
					/*退出系统*/					
					exitApp2();
					break;
				}
			}
		});
	}

	/**
	 * 退出系统
	 */
	private void exitApp2() {
		// 先溢出用户信息
		Intent startMain = new Intent(Intent.ACTION_MAIN);
		startMain.addCategory(Intent.CATEGORY_HOME);
		startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(startMain);
		//退出系统
		System.exit(0);
	}

	/**
	 * Toast提示框
	 */
	public void DisplayToast(String str) {
		Toast toast = Toast.makeText(this, str, Toast.LENGTH_SHORT);
		// 设置toast显示的位置
		toast.setGravity(Gravity.TOP, 0, 220);
		// 显示该Toast
		toast.show();
	}
	
}

