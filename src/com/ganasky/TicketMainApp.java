package com.ganasky;

import java.util.Calendar;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
/**
 * ��ѯ����
 * @author 
 *
 */
public class TicketMainApp extends TabActivity {
	
	// ����TabHost����
	TabHost mTabHost;
	// ������������Ǹ�ͼƬ
	private ImageView imgSetCity;
	// �����������Ǹ�ͼƬ
	private ImageView imgoffCity;
	// ���ѡ������
	private ImageView imgsetTime;
	// ��������
	private TextView setCity;
	// �������
	private TextView offCity;
	// ����ʱ��
	private TextView setTime;
	// ��λ�ȼ�
	private TextView setClass;
	// ���չ�˾
	private TextView setTravelCompany;
	// ��ѯ
	private Button btnSearchTicket;
	// ȡ����ѯ
	private Button btnCancelSearchTicket;
	// ��λ�����б�
	private Spinner spinnerSetClass;
	// ���չ�˾�����б�
	private Spinner spinnerSetCompany;
	// �ɻ���������
	private ArrayAdapter<String> adapterPlanClass;
	// ��λ����
	private String[] planeClass = { "���в�λ", "ͷ�Ȳ�", "���ò�", "�����" };
	// ���غ��չ�˾����
	private String[] planCompany = { "MU-��������","CA-���ʺ���", "CZ-�Ϸ�����", 
			"8L-��������","CN-���»�", "EU-ӥ������", "FM-�Ϻ�����", "G5-���ú���", 
			"9C-���ﺽ��", "BK-�¿�����","GS-��򺽿�", "HO-���麽��", "HU-���Ϻ���", 
			"JD-��¹����", "JR-�Ҹ�����", "KN-���Ϻ���","KY-��������", "MF-���ź���",
			"NS-��������", "OQ-���캽��", "PN-��������","SC-ɽ������", "VD-��������", 
			"ZH-���ں���","3U-�Ĵ�����","���к��չ�˾"};
	// ����������
	Calendar c;

	// ѡ���ֵ(Ҫ���óɾ�̬�������ܶ���)
	private String selectedStartCity = null;
	private String selectedOffCity = null;
	private String selectedTime = null;
	// ѡ��Ĳ�λ����
	private String selectedPlaneClass = "All";
	private String selectedPlaneCompany = "All";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ticketmain);

		// ��ʼ������
		c = Calendar.getInstance();

		// �õ���ѯ��Ʊ�����ϵĿؼ�
		getTicketmainControls();

		// ��ȡ����Ĳ���
		readParas();

		// ȡ��TabHost����
		mTabHost = getTabHost();

		/* ΪTabHost��ӱ�ǩ */
		// �½�һ��newTabSpec(newTabSpec)
		// �������ǩ��ͼ��(setIndicator)
		// ��������(setContent)
		mTabHost.addTab(mTabHost.newTabSpec("tab_singleTravel")
				.setIndicator("����").setContent(R.id.table1));
		mTabHost.addTab(mTabHost.newTabSpec("tab_twoTravel")
		// .setIndicator("����", getResources().getDrawable(R.drawable.img2))
				.setIndicator("����").setContent(R.id.table2));

		// ����TabHost�ı�����ɫ
		// mTabHost.setBackgroundColor(Color.argb(150, 22, 70, 150));

		// ����TabHost�ı���ͼƬ��Դ
		mTabHost.setBackgroundResource(R.drawable.buttonbgon);

		// ���õ�ǰ��ʾ��һ����ǩ
		mTabHost.setCurrentTab(0);

		// ����������д����¼�
		imgSetCity.setOnTouchListener(selectSetCity);
		// ���������д����¼�
		imgoffCity.setOnTouchListener(selectSetCity2);
		// ������ó���ʱ��
		imgsetTime.setOnTouchListener(selectDateTime);

		// ��ʼ����λ
		selectPlaneClass();
		spinnerSetClass.setOnItemSelectedListener(SelectPlanClass);

		// ��ʼ�����չ�˾
		selectPlaneCompany();
		spinnerSetCompany.setOnItemSelectedListener(SelectPlanCompany);

		// ��ǩ�л��¼�����setOnTabChangedListener
		mTabHost.setOnTabChangedListener(new OnTabChangeListener() {
			// tabIdΪtab_test1���ֵ
			@Override
			public void onTabChanged(String tabId) {
				if (tabId == "tab_twoTravel") {
					Dialog dialog = new AlertDialog.Builder(TicketMainApp.this)
							.setTitle("��ʾ")
							.setMessage("���ܻ�δʵ�֣������ڴ���")
							.setPositiveButton("ȷ��",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int whichButton) {
											dialog.cancel();
										}
									}).create();// ������ť
					dialog.show();
				}
			}
		});

		// �õ�ҳ�洫������ֵ����ֵ���ؼ�
		setTicketValue();
		// �������
		SaveParas();
		btnSearchTicket.setOnTouchListener(searchTicket);
		btnCancelSearchTicket.setOnTouchListener(cancelSearch);
		// ����ʧ����Ϣ
		receiveErroMessage();
	}
   //����ʧ����Ϣ 
	private void receiveErroMessage() {
		Intent intent = getIntent();
		String erroMessage = "";
		erroMessage = intent.getStringExtra("erroMessage");
		if (erroMessage != null && erroMessage.length() > 0) {
			DisplayToast(erroMessage);
		}
	}

	/**
	 * ��ʼ����Ʊ����
	 */
	private void getTicketmainControls() {
		setCity = (TextView) findViewById(R.id.ticketmain_setCity);
		imgSetCity = (ImageView) findViewById(R.id.ticketmain_imgSetCity);
		offCity = (TextView) findViewById(R.id.ticketmain_offCity);
		imgoffCity = (ImageView) findViewById(R.id.ticketmain_imgoffCity);
		setTime = (TextView) findViewById(R.id.ticketmain_setTime);
		setClass = (TextView) findViewById(R.id.ticketmain_setClass);
		setTravelCompany = (TextView) findViewById(R.id.ticketmain_setTravelCompany);
		btnSearchTicket = (Button) findViewById(R.id.ticketmain_btnSearchTicket);
		btnCancelSearchTicket = (Button) findViewById(R.id.ticketmain_btnCancelSearchTicket);
		spinnerSetClass = (Spinner) findViewById(R.id.ticketmain_spinnerSetClass);
		spinnerSetCompany = (Spinner) findViewById(R.id.ticketmain_spinnerSetCompany);
		imgsetTime = (ImageView) findViewById(R.id.ticketmain_imgsetTime);

	}

	/**
	 * ����ҳ�洫������ֵ����ֵ���ؼ�
	 */
	private void setTicketValue() {
		Intent intent = getIntent();
		selectedStartCity = intent.getStringExtra("startCity");
		selectedOffCity = intent.getStringExtra("offCity");
		if (selectedStartCity != null) {
			selectedStartCity = selectedStartCity.substring(selectedStartCity
					.indexOf(",") + 1);
			setCity.setText(selectedStartCity);
		}
		if (selectedOffCity != null) {
			selectedOffCity = selectedOffCity.substring(selectedOffCity
					.indexOf(",") + 1);
			offCity.setText(selectedOffCity);
		}
	}

	/**
	 * ���ó�������
	 */
	ImageView.OnTouchListener selectSetCity 
	                               = new ImageView.OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// ������Щ�����¼�����
			int iAction = event.getAction();
			if (iAction == MotionEvent.ACTION_DOWN) {
				/* �½�һ��Intent���� */
				Intent intent = new Intent();
				// ���ô��ݵĲ���
				intent.putExtra("startCitypara", "startCitypara");
				/* ָ��intentҪ�������� */
				intent.setClass(TicketMainApp.this, CityListView.class);
				/* ����һ���µ�Activity */
				TicketMainApp.this.startActivity(intent);
				return true;
			} else {
				return false;
			}
		}
	};

	/**
	 * ���õִ����
	 */
	ImageView.OnTouchListener selectSetCity2 
	                                               = new ImageView.OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// ������Щ�����¼�����
			int iAction = event.getAction();
			if (iAction == MotionEvent.ACTION_DOWN) {
				/* �½�һ��Intent���� */
				Intent intent = new Intent();
				// ���ô��ݵĲ���
				intent.putExtra("offCitypara", "offCitypara");
				/* ָ��intentҪ�������� */
				intent.setClass(TicketMainApp.this, CityListView.class);
				/* ����һ���µ�Activity */
				TicketMainApp.this.startActivity(intent);
				return true;
			} else {
				return false;
			}
		}
	};

	ImageView.OnTouchListener selectDateTime 
	                                     = new ImageView.OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			int iAction = event.getAction();
			if (iAction == MotionEvent.ACTION_DOWN) {
				// TODO Auto-generated method stub
				new DatePickerDialog(TicketMainApp.this,
						new DatePickerDialog.OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker view, int year,
									int monthOfYear, int dayOfMonth) {
								// ��������
								setTime.setText(Integer.toString(year) + "-"
										+ Integer.toString(monthOfYear + 1)
										+ "-" + Integer.toString(dayOfMonth));
								selectedTime = setTime.getText().toString();
							}
						}, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
						c.get(Calendar.DAY_OF_MONTH)).show();
				return true;
			} else {
				return false;
			}
		}

	};
   /**
    * ѡ���λ
    */
	private void selectPlaneClass() {

		adapterPlanClass = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, planeClass);

		// ���������б�ķ��
		adapterPlanClass
		.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// ��adapter��ӵ�m_Spinner��
		spinnerSetClass.setAdapter(adapterPlanClass);
	}
	 /**
	    * ѡ�񺽿չ�˾
	    */
	private void selectPlaneCompany() {

		adapterPlanClass = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, planCompany);
		// ���������б�ķ��
		adapterPlanClass
		.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// ��adapter��ӵ�m_Spinner��
		spinnerSetCompany.setAdapter(adapterPlanClass);
	}
	
   /**
   * ʵ��ѡ���λSpinner�ؼ��ļ����¼�
   */
	Spinner.OnItemSelectedListener SelectPlanClass 
	                                       = new Spinner.OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			selectedPlaneClass = parent.getItemAtPosition(position).toString();
			if (selectedPlaneClass != null) {

				setClass.setText(selectedPlaneClass);
				if (selectedPlaneClass == "���ò�") {
					selectedPlaneClass = "Y";

				} else if (selectedPlaneClass == "ͷ�Ȳ�") {
					selectedPlaneClass = "F";

				} else if (selectedPlaneClass == "�����") {
					selectedPlaneClass = "C";
				} else if (selectedPlaneClass == "���в�λ") {
					selectedPlaneClass = "All";
				}
			} else {
				setClass.setText("���в�λ");
				selectedPlaneClass = "All";
			}
		}
		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
		}
	};
	
	/**
	   * ʵ��ѡ�񺽿չ�˾Spinner�ؼ��ļ����¼�
	   */
	Spinner.OnItemSelectedListener SelectPlanCompany 
	        = new Spinner.OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			selectedPlaneCompany = parent.getItemAtPosition(position)
					.toString();
			if (selectedPlaneCompany != null) {
				setTravelCompany.setText(selectedPlaneCompany);

				if (selectedPlaneCompany == "���к��չ�˾") {
					selectedPlaneCompany = "All";
				} else {
					selectedPlaneCompany = selectedPlaneCompany.substring(0, 2);
				}
			} else {
				setTravelCompany.setText("���к��չ�˾");
				selectedPlaneCompany = "All";
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
		}
	};
   /**
    * ��ѯʵ��
    */
	Button.OnTouchListener searchTicket = new Button.OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// ����ɫ��͸���ȸı�
			// btnSearchTicket.setBackgroundColor(Color.argb(155, 0, 255, 0));
			int iAction = event.getAction();
			if (iAction == MotionEvent.ACTION_CANCEL
					|| iAction == MotionEvent.ACTION_DOWN
					|| iAction == MotionEvent.ACTION_MOVE) {
				return false;
			}
			if (CheckParameter()) {
				//��ʾ�ȴ��Ի���
				showDialog();
			}
			return true;
		}
	};

	/*
	 * �������Ĳ���
	 */
	private boolean CheckParameter() {
		if (setCity.getText() != null && setCity.getText().length() > 0) {
			selectedStartCity = setCity.getText().toString();
		} else {
			DisplayToast("�������в���Ϊ��!");
			return false;
		}
		if (offCity.getText() != null && offCity.getText().length() > 0) {
			selectedOffCity = offCity.getText().toString();
		} else {
			DisplayToast("�ִ���в���Ϊ��!");
			return false;
		}

		if (selectedTime != null && selectedTime.length() > 0) {

		} else {
			DisplayToast("���ڲ���Ϊ��!");
			return false;
		}
		return true;
	}
    //���ȿ�
	ProgressDialog m_Dialog;
	private void showDialog() {
		// ������ɺ󣬵����ȷ������ʼ��½
		m_Dialog = ProgressDialog.show(TicketMainApp.this, "��ȴ�...",
				"����Ϊ����ѯ��Ʊ��Ϣ...", true);
		//�����߳�
		new Thread() {
			@Override
			public void run() {
				try {
					SaveParas();
					// sleep(100);
					// /* �½�һ��Intent���� */
					Intent intent = new Intent();
					// ���ô��ݵĲ���
					intent.putExtra("selectedStartCity", selectedStartCity);
					intent.putExtra("selectedOffCity", selectedOffCity);
					intent.putExtra("selectedTime", selectedTime);
					intent.putExtra("selectedPlaneClass", selectedPlaneClass);
					intent.putExtra("selectedPlaneCompany",
							selectedPlaneCompany);
					/* ָ��intentҪ�������� */
					intent.setClass(TicketMainApp.this, FlightInfoMainApp.class);
					/* ����һ���µ�Activity */
					TicketMainApp.this.startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					// ��¼������ȡ��m_Dialog�Ի���
					m_Dialog.dismiss();
				}
			}
		}.start();
	}
	
  /**
   * ������Ĳ�ѯ��Ϣ
   */
	private void readParas() {
		// ȡ�û��preferences����.
		SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
		// ȡ��ֵ.
		selectedStartCity = settings.getString("selectedStartCity",
				selectedStartCity);
		selectedOffCity = settings
				.getString("selectedOffCity", selectedOffCity);
		selectedTime = settings.getString("selectedTime", selectedTime);
		if (selectedStartCity != null && selectedStartCity.length() > 0) {
			setCity.setText(selectedStartCity);
		}
		if (selectedOffCity != null && selectedOffCity.length() > 0) {
			offCity.setText(selectedOffCity);
		}
		if (selectedTime != null && selectedTime.length() > 0) {
			setTime.setText(selectedTime);
		}
	}

	/**
	 * �Ƿ��Ʊ������Ϣ
	 */
	private void SaveParas() {
		// ����
		SharedPreferences uiState = getPreferences(0);
		// ȡ�ñ༭����
		SharedPreferences.Editor editor = uiState.edit();
		if (selectedStartCity != null && selectedStartCity.length() > 0) {
			// ���ֵ
			editor.putString("selectedStartCity", selectedStartCity);
		}
		if (selectedOffCity != null && selectedOffCity.length() > 0) {
			// ���ֵ
			editor.putString("selectedOffCity", selectedOffCity);
		}
		if (selectedTime != null && selectedTime.length() > 0) {
			editor.putString("selectedTime", selectedTime);
		}
		// �ύ����
		editor.commit();
	}

	/**
	 * ����ȡ����¼�ķ���
	 */
	Button.OnTouchListener cancelSearch = new Button.OnTouchListener() {

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
	 * ������ҳ
	 */
	private void returnMainPage() {
		/* �½�һ��Intent���� */
		Intent intent = new Intent();
		/* ָ��intentҪ�������� */
		intent.setClass(TicketMainApp.this, GanaSkyMain.class);
		/* ����һ���µ�Activity */
		startActivity(intent);
		/* �رյ�ǰ��Activity */
		TicketMainApp.this.finish();
	}

	/* ��ʾToast */
	public void DisplayToast(String str) {
		Toast toast = Toast.makeText(this, str, Toast.LENGTH_SHORT);
		//����toast��ʾ��λ��
		toast.setGravity(Gravity.TOP, 0, 220);
		//��ʾ��Toast
		toast.show();
	}
}
