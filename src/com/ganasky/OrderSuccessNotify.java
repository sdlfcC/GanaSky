package com.ganasky;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
/**
 * �㿪״̬����֪ͨ����ʾ��ҳ��
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
		//���ղ���
		Intent intent = getIntent();
		result = intent.getStringExtra("result");
		//��ʾ��Ϣ
		tv_result=(TextView)this.findViewById(R.id.tv_result);
		tv_result.setText(result);
	}
}

