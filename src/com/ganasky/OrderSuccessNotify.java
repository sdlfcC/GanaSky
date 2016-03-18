package com.ganasky;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
/**
 * 点开状态栏的通知，显示的页面
 * @author 
 *
 */
public class OrderSuccessNotify extends Activity {
	String result;
	TextView tv_result;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notify);
		//接收参数
		Intent intent = getIntent();
		result = intent.getStringExtra("result");
		//显示信息
		tv_result=(TextView)this.findViewById(R.id.tv_result);
		tv_result.setText(result);
	}
}

