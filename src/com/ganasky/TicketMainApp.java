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
 * 查询界面
 * @author 
 *
 */
public class TicketMainApp extends TabActivity {
	
	// 声明TabHost对象
	TabHost mTabHost;
	// 点击出发城市那个图片
	private ImageView imgSetCity;
	// 点击到达城市那个图片
	private ImageView imgoffCity;
	// 点击选择日期
	private ImageView imgsetTime;
	// 出发城市
	private TextView setCity;
	// 到达城市
	private TextView offCity;
	// 出发时间
	private TextView setTime;
	// 仓位等级
	private TextView setClass;
	// 航空公司
	private TextView setTravelCompany;
	// 查询
	private Button btnSearchTicket;
	// 取消查询
	private Button btnCancelSearchTicket;
	// 仓位下拉列表
	private Spinner spinnerSetClass;
	// 航空公司下拉列表
	private Spinner spinnerSetCompany;
	// 飞机舱适配器
	private ArrayAdapter<String> adapterPlanClass;
	// 仓位类型
	private String[] planeClass = { "所有仓位", "头等舱", "经济舱", "公务舱" };
	// 加载航空公司类型
	private String[] planCompany = { "MU-东方航空","CA-国际航空", "CZ-南方航空", 
			"8L-祥鹏航空","CN-大新华", "EU-鹰联航空", "FM-上海航空", "G5-华厦航空", 
			"9C-春秋航空", "BK-奥凯航空","GS-天津航空", "HO-吉祥航空", "HU-海南航空", 
			"JD-金鹿航空", "JR-幸福航空", "KN-联合航空","KY-昆明航空", "MF-厦门航空",
			"NS-东北航空", "OQ-重庆航空", "PN-西部航空","SC-山东航空", "VD-鲲鹏航空", 
			"ZH-深圳航空","3U-四川航空","所有航空公司"};
	// 定义日历类
	Calendar c;

	// 选择的值(要设置成静态变量才能读到)
	private String selectedStartCity = null;
	private String selectedOffCity = null;
	private String selectedTime = null;
	// 选择的仓位类型
	private String selectedPlaneClass = "All";
	private String selectedPlaneCompany = "All";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ticketmain);

		// 初始化日历
		c = Calendar.getInstance();

		// 得到查询机票界面上的控件
		getTicketmainControls();

		// 读取保存的参数
		readParas();

		// 取得TabHost对象
		mTabHost = getTabHost();

		/* 为TabHost添加标签 */
		// 新建一个newTabSpec(newTabSpec)
		// 设置其标签和图标(setIndicator)
		// 设置内容(setContent)
		mTabHost.addTab(mTabHost.newTabSpec("tab_singleTravel")
				.setIndicator("单程").setContent(R.id.table1));
		mTabHost.addTab(mTabHost.newTabSpec("tab_twoTravel")
		// .setIndicator("往返", getResources().getDrawable(R.drawable.img2))
				.setIndicator("往返").setContent(R.id.table2));

		// 设置TabHost的背景颜色
		// mTabHost.setBackgroundColor(Color.argb(150, 22, 70, 150));

		// 设置TabHost的背景图片资源
		mTabHost.setBackgroundResource(R.drawable.buttonbgon);

		// 设置当前显示哪一个标签
		mTabHost.setCurrentTab(0);

		// 点击出发城市触发事件
		imgSetCity.setOnTouchListener(selectSetCity);
		// 点击到达城市触发事件
		imgoffCity.setOnTouchListener(selectSetCity2);
		// 点击设置出发时间
		imgsetTime.setOnTouchListener(selectDateTime);

		// 初始化仓位
		selectPlaneClass();
		spinnerSetClass.setOnItemSelectedListener(SelectPlanClass);

		// 初始化航空公司
		selectPlaneCompany();
		spinnerSetCompany.setOnItemSelectedListener(SelectPlanCompany);

		// 标签切换事件处理，setOnTabChangedListener
		mTabHost.setOnTabChangedListener(new OnTabChangeListener() {
			// tabId为tab_test1这个值
			@Override
			public void onTabChanged(String tabId) {
				if (tabId == "tab_twoTravel") {
					Dialog dialog = new AlertDialog.Builder(TicketMainApp.this)
							.setTitle("提示")
							.setMessage("功能还未实现，尽请期待！")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int whichButton) {
											dialog.cancel();
										}
									}).create();// 创建按钮
					dialog.show();
				}
			}
		});

		// 得到页面传过来的值并赋值给控件
		setTicketValue();
		// 保存参数
		SaveParas();
		btnSearchTicket.setOnTouchListener(searchTicket);
		btnCancelSearchTicket.setOnTouchListener(cancelSearch);
		// 接收失败信息
		receiveErroMessage();
	}
   //接收失败信息 
	private void receiveErroMessage() {
		Intent intent = getIntent();
		String erroMessage = "";
		erroMessage = intent.getStringExtra("erroMessage");
		if (erroMessage != null && erroMessage.length() > 0) {
			DisplayToast(erroMessage);
		}
	}

	/**
	 * 初始化订票界面
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
	 * 设置页面传过来的值并赋值给控件
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
	 * 设置出发城市
	 */
	ImageView.OnTouchListener selectSetCity 
	                               = new ImageView.OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// 设置哪些触屏事件可用
			int iAction = event.getAction();
			if (iAction == MotionEvent.ACTION_DOWN) {
				/* 新建一个Intent对象 */
				Intent intent = new Intent();
				// 设置传递的参数
				intent.putExtra("startCitypara", "startCitypara");
				/* 指定intent要启动的类 */
				intent.setClass(TicketMainApp.this, CityListView.class);
				/* 启动一个新的Activity */
				TicketMainApp.this.startActivity(intent);
				return true;
			} else {
				return false;
			}
		}
	};

	/**
	 * 设置抵达城市
	 */
	ImageView.OnTouchListener selectSetCity2 
	                                               = new ImageView.OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// 设置哪些触屏事件可用
			int iAction = event.getAction();
			if (iAction == MotionEvent.ACTION_DOWN) {
				/* 新建一个Intent对象 */
				Intent intent = new Intent();
				// 设置传递的参数
				intent.putExtra("offCitypara", "offCitypara");
				/* 指定intent要启动的类 */
				intent.setClass(TicketMainApp.this, CityListView.class);
				/* 启动一个新的Activity */
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
								// 设置日历
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
    * 选择舱位
    */
	private void selectPlaneClass() {

		adapterPlanClass = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, planeClass);

		// 设置下拉列表的风格
		adapterPlanClass
		.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// 将adapter添加到m_Spinner中
		spinnerSetClass.setAdapter(adapterPlanClass);
	}
	 /**
	    * 选择航空公司
	    */
	private void selectPlaneCompany() {

		adapterPlanClass = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, planCompany);
		// 设置下拉列表的风格
		adapterPlanClass
		.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// 将adapter添加到m_Spinner中
		spinnerSetCompany.setAdapter(adapterPlanClass);
	}
	
   /**
   * 实现选择舱位Spinner控件的监听事件
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
				if (selectedPlaneClass == "经济舱") {
					selectedPlaneClass = "Y";

				} else if (selectedPlaneClass == "头等舱") {
					selectedPlaneClass = "F";

				} else if (selectedPlaneClass == "公务舱") {
					selectedPlaneClass = "C";
				} else if (selectedPlaneClass == "所有仓位") {
					selectedPlaneClass = "All";
				}
			} else {
				setClass.setText("所有仓位");
				selectedPlaneClass = "All";
			}
		}
		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
		}
	};
	
	/**
	   * 实现选择航空公司Spinner控件的监听事件
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

				if (selectedPlaneCompany == "所有航空公司") {
					selectedPlaneCompany = "All";
				} else {
					selectedPlaneCompany = selectedPlaneCompany.substring(0, 2);
				}
			} else {
				setTravelCompany.setText("所有航空公司");
				selectedPlaneCompany = "All";
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
		}
	};
   /**
    * 查询实现
    */
	Button.OnTouchListener searchTicket = new Button.OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// 背景色及透明度改变
			// btnSearchTicket.setBackgroundColor(Color.argb(155, 0, 255, 0));
			int iAction = event.getAction();
			if (iAction == MotionEvent.ACTION_CANCEL
					|| iAction == MotionEvent.ACTION_DOWN
					|| iAction == MotionEvent.ACTION_MOVE) {
				return false;
			}
			if (CheckParameter()) {
				//显示等待对话框
				showDialog();
			}
			return true;
		}
	};

	/*
	 * 检测输入的参数
	 */
	private boolean CheckParameter() {
		if (setCity.getText() != null && setCity.getText().length() > 0) {
			selectedStartCity = setCity.getText().toString();
		} else {
			DisplayToast("出发城市不能为空!");
			return false;
		}
		if (offCity.getText() != null && offCity.getText().length() > 0) {
			selectedOffCity = offCity.getText().toString();
		} else {
			DisplayToast("抵达城市不能为空!");
			return false;
		}

		if (selectedTime != null && selectedTime.length() > 0) {

		} else {
			DisplayToast("日期不能为空!");
			return false;
		}
		return true;
	}
    //进度框
	ProgressDialog m_Dialog;
	private void showDialog() {
		// 输入完成后，点击“确定”开始登陆
		m_Dialog = ProgressDialog.show(TicketMainApp.this, "请等待...",
				"正在为您查询机票信息...", true);
		//开启线程
		new Thread() {
			@Override
			public void run() {
				try {
					SaveParas();
					// sleep(100);
					// /* 新建一个Intent对象 */
					Intent intent = new Intent();
					// 设置传递的参数
					intent.putExtra("selectedStartCity", selectedStartCity);
					intent.putExtra("selectedOffCity", selectedOffCity);
					intent.putExtra("selectedTime", selectedTime);
					intent.putExtra("selectedPlaneClass", selectedPlaneClass);
					intent.putExtra("selectedPlaneCompany",
							selectedPlaneCompany);
					/* 指定intent要启动的类 */
					intent.setClass(TicketMainApp.this, FlightInfoMainApp.class);
					/* 启动一个新的Activity */
					TicketMainApp.this.startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					// 登录结束，取消m_Dialog对话框
					m_Dialog.dismiss();
				}
			}
		}.start();
	}
	
  /**
   * 读保存的查询信息
   */
	private void readParas() {
		// 取得活动的preferences对象.
		SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
		// 取得值.
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
	 * 是否查票参数信息
	 */
	private void SaveParas() {
		// 保存
		SharedPreferences uiState = getPreferences(0);
		// 取得编辑对象
		SharedPreferences.Editor editor = uiState.edit();
		if (selectedStartCity != null && selectedStartCity.length() > 0) {
			// 添加值
			editor.putString("selectedStartCity", selectedStartCity);
		}
		if (selectedOffCity != null && selectedOffCity.length() > 0) {
			// 添加值
			editor.putString("selectedOffCity", selectedOffCity);
		}
		if (selectedTime != null && selectedTime.length() > 0) {
			editor.putString("selectedTime", selectedTime);
		}
		// 提交保存
		editor.commit();
	}

	/**
	 * 触屏取消登录的方法
	 */
	Button.OnTouchListener cancelSearch = new Button.OnTouchListener() {

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
	 * 返回主页
	 */
	private void returnMainPage() {
		/* 新建一个Intent对象 */
		Intent intent = new Intent();
		/* 指定intent要启动的类 */
		intent.setClass(TicketMainApp.this, GanaSkyMain.class);
		/* 启动一个新的Activity */
		startActivity(intent);
		/* 关闭当前的Activity */
		TicketMainApp.this.finish();
	}

	/* 显示Toast */
	public void DisplayToast(String str) {
		Toast toast = Toast.makeText(this, str, Toast.LENGTH_SHORT);
		//设置toast显示的位置
		toast.setGravity(Gravity.TOP, 0, 220);
		//显示该Toast
		toast.show();
	}
}
