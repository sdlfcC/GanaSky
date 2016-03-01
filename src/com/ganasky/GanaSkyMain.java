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
 * ������ҳ��
 * @author 
 * 
 */
public class GanaSkyMain extends Activity {
	private int item = 0;
	private GanaSkyImageAdapter imageApter;

	/** ����ganaSky����ҳ�� */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ������ҳ��
		setContentView(R.layout.main);

		// ȡ��GridView����
		GridView gridview = (GridView) findViewById(R.id.gridview);

		// ��ʼ��ͼƬ��������
		imageApter = new GanaSkyImageAdapter(this);

		// ���Ԫ�ظ�gridview�ؼ�
		gridview.setAdapter(imageApter);

		// ���ö�Ʊϵͳ�ı���
		gridview.setBackgroundResource(R.drawable.bg0);

		// �¼�����
		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				item = position + 1;
				switch (item) {
				 /*��ת����¼����*/
				case 1:
					/* �½�һ��Intent���� */
					Intent intent = new Intent();
				 /* ָ��intentҪ�������࣬�����Ƚ�LoginApp.java�ļ���
				 * �ں�����½�����ʵ�� */
					intent.setClass(GanaSkyMain.this, LoginApp.class);
					/* ����һ���µ�Activity */
					startActivity(intent);
					/* �رյ�ǰ��Activity */
					GanaSkyMain.this.finish();
					break;
					
				 /*��ת����Ʊ������*/
				case 2:
					/*�ж��Ƿ����������*/
					if (ConnUrlHelper.hasInternet(GanaSkyMain.this)) {
						/* �½�һ��Intent���� */
						Intent intent2 = new Intent();
						/* ָ��intentҪ�������� */
						intent2.setClass(GanaSkyMain.this, TicketMainApp.class);
						/* ����һ���µ�Activity */
						startActivity(intent2);
						/* �رյ�ǰ��Activity */
						GanaSkyMain.this.finish();
					} else {
						DisplayToast("��������ʧ��,���ܶ�Ʊ��");
					}
					break;
				case 3:
					/*�˳�ϵͳ*/					
					exitApp2();
					break;
				}
			}
		});
	}

	/**
	 * �˳�ϵͳ
	 */
	private void exitApp2() {
		// ������û���Ϣ
		Intent startMain = new Intent(Intent.ACTION_MAIN);
		startMain.addCategory(Intent.CATEGORY_HOME);
		startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(startMain);
		//�˳�ϵͳ
		System.exit(0);
	}

	/**
	 * Toast��ʾ��
	 */
	public void DisplayToast(String str) {
		Toast toast = Toast.makeText(this, str, Toast.LENGTH_SHORT);
		// ����toast��ʾ��λ��
		toast.setGravity(Gravity.TOP, 0, 220);
		// ��ʾ��Toast
		toast.show();
	}
	
}

