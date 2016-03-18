package com.ganasky;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import com.ganasky.Help.ConnUrlHelper;
import com.ganasky.Help.VerifyIdCard;
import com.ganasky.model.ClassInfo;
import com.ganasky.model.FlightInfo;
import com.ganasky.model.PassengerInfo;
import com.ganasky.model.UserInfo;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 用户信息选择与填写（机票预订）实现
 * @author 
 *
 */
public class OrderTicket extends Activity {

	private final String DEBUG_TAG = "OrderTicketApp";
	// 始发地控件
	private TextView txtSetCity;
	// 目的地控件
	private TextView txtOffCity;
	// 始发时间
	private TextView txtSetTime;
   //星期
	private TextView txtWeek;
	// 选择人数
	private Spinner spinnerchoicePersonNumber;
	// 证件类型
	private Spinner spinnerSelectCardType;

	private FlightInfo flightInfo;
	private ClassInfo classInfo;
	// 定义证件
	private String[] cardType = { "身份证", "护照", "军官证" };
	// 定义人数
	private String[] personsNumber = {"1", "2", "3", "4", "5", "6", "7", "8",
			"9", "10" };
	// 适配器，用于选择卡和选择人数
	private ArrayAdapter<String> personsNumberAdapter;

	// 选择的人数
	private String selectedPersonsNumber;
	// 选择卡的类型
	private int selectedcardType = 1;

	// 外面总的layout
	LinearLayout linearLayoutTotal;
	// 信息确定按钮
	private Button btnOrder;
	private Button btnReturn;
    //乘客信息
	private PassengerInfo passengerInfo;
	private List<PassengerInfo> passengerInfoList;
    //是否送票复选框
	private CheckBox IsSentTicket;
	//送票地址
	private EditText edAddress;
	 //是否需要送票
	private boolean isCheckSentTicket = false;
    //用户信息类
	private UserInfo userInfo;
	
	private String address;
	private String urlTicketsInfo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.orderticket);

		// 初始化控件
		initControl();

		// 接收页面传过来的参数
		setTicketValue();

		// 初始化选择卡类型和身份证的类型
		selectedPersonsNumber = personsNumber[0];

		// 选择证件类型
		selectCardType();
		// 选择人数
		selectPassageNumber();
		// 选择证件类型
		spinnerSelectCardType.setOnItemSelectedListener(selectedCardType);
		// 选择人数
		spinnerchoicePersonNumber
				.setOnItemSelectedListener(selectedPassageNumber);

		btnReturn.setOnTouchListener(returnFlightListDetailView);
		// 是否送票
		IsSentTicket.setOnCheckedChangeListener(SentTicket);
		// 如果选择了送票
		if (IsSentTicket.isChecked()) {
			isCheckSentTicket = true;
		}
		// 订票按钮
		btnOrder.setOnTouchListener(OrderTicket);

	}

	/**
	 * 初始化该页面上的控件
	 */
	private void initControl() {
		// 绑定Layout里面的ListView
		txtSetCity = (TextView) findViewById(R.id.orderticket_txtSetCity);
		txtOffCity = (TextView) findViewById(R.id.orderticket_txtOffCity);
		txtSetTime = (TextView) findViewById(R.id.orderticket_txtTime);
		txtWeek = (TextView) findViewById(R.id.orderticket_txtWeek);
	  spinnerchoicePersonNumber = (Spinner) findViewById(R.id.orderticket_choicePersonNumber);
		spinnerSelectCardType = (Spinner) findViewById(R.id.orderticket_selectCardType);
		btnOrder = (Button) findViewById(R.id.orderticket_btnOK);
		btnReturn = (Button) findViewById(R.id.orderticket_btnReturn);
		linearLayoutTotal = (LinearLayout) findViewById(R.id.orderticket_lineLayOut);
		// 是否送票
		IsSentTicket = (CheckBox) findViewById(R.id.orderticket_IsSentTicket);
		edAddress = (EditText) findViewById(R.id.orderticket_edAddress);
	}

	/**
	 * 接收传过来的参数
	 */
	private void setTicketValue() {
		Intent intent = getIntent();
		flightInfo = new FlightInfo();
		classInfo = new ClassInfo();
		flightInfo.setDateTime(intent.getStringExtra("dateTime"));
		flightInfo.setWeekDay(intent.getStringExtra("dayofweek"));
		flightInfo.setFlightNumber(intent.getStringExtra("FlightNumber"));
		flightInfo.setFromAirportName(intent.getStringExtra("FromAirportName"));
		flightInfo.setLandingAirportName(intent
				.getStringExtra("LandingAirportName"));
		flightInfo.setPlaneModel(intent.getStringExtra("PlaneModel"));
		flightInfo.setTakeOff(intent.getStringExtra("TakeOff"));
		flightInfo.setLanding(intent.getStringExtra("Landing"));
		flightInfo.setIsElectronicTicket(intent
				.getStringExtra("IsElectronicTicket"));
		flightInfo.setAirportConstructionFee(intent
				.getStringExtra("AirportConstructionFee"));
		flightInfo.setFuelSurcharge(intent.getStringExtra("FuelSurcharge"));
		classInfo.setPublicPrice(intent.getStringExtra("PublicPrice"));

		if (flightInfo != null && classInfo != null) {
			// 赋值
			txtSetCity.setText(flightInfo.getFromAirportName());
			txtOffCity.setText(flightInfo.getLandingAirportName());
			txtSetTime.setText(flightInfo.getDateTime());
			txtWeek.setText(flightInfo.getWeekDay());
		}

	}

	/**
	 * 证件类型
	 */
	private void selectCardType() {

		personsNumberAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, cardType);

		// 设置下拉列表的风格
		personsNumberAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter添加到m_Spinner中
		spinnerSelectCardType.setAdapter(personsNumberAdapter);
	}

	/**
	 * 给乘客人数这个下拉列表赋值
	 */
	private void selectPassageNumber() {

		personsNumberAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, personsNumber);

		// 设置下拉列表的风格
		personsNumberAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter添加到m_Spinner中
		spinnerchoicePersonNumber.setAdapter(personsNumberAdapter);
	}

	// 选择证件下拉列表框的选择事件
	Spinner.OnItemSelectedListener selectedCardType 
	                                         = new Spinner.OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			String type = "";
			type = parent.getItemAtPosition(position).toString();
			if (type.trim().equals("身份证")) {
				selectedcardType = 1;
			} else if (type.trim().equals("护照")) {
				selectedcardType = 2;
			} else if (type.trim().equals("军官证")) {
				selectedcardType = 3;
			}

		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
		}
	};

	// 选择人数下拉列表框的选择事件
	Spinner.OnItemSelectedListener selectedPassageNumber 
	                                         = new Spinner.OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			selectedPersonsNumber = parent.getItemAtPosition(position)
					.toString();
			if (selectedPersonsNumber != null) {
				// 动态添加子项
				AddItem(Integer.parseInt(selectedPersonsNumber));
			} else {
				DisplayToast("请选择证件类型或者人数");
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
		}
	};

	/**
	 * 添加子项
	 * 
	 * @param num
	 */
	private void AddItem(int num) {
		if (num != 0) {
			final int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;
			final int FILL_PARENT = ViewGroup.LayoutParams.FILL_PARENT;
			linearLayoutTotal.removeAllViews();
			for (int i = 0; i < num; i++) {
				LinearLayout linearLayout1 = new LinearLayout(this);
				linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
				linearLayoutTotal
						.addView(linearLayout1, new LinearLayout.LayoutParams(
								FILL_PARENT, WRAP_CONTENT));

				EditText editTextName = new EditText(this);
				editTextName.setHint("请输入证件上对应的姓名");
				editTextName.setTextSize(11);
				linearLayout1.addView(editTextName,
						new LinearLayout.LayoutParams(WRAP_CONTENT,
								WRAP_CONTENT));

				EditText editTextCardId = new EditText(this);
				editTextCardId.setTextSize(11);
				editTextCardId.setHint("请输入正确的证件号码     ");
				linearLayout1.addView(editTextCardId,
						new LinearLayout.LayoutParams(WRAP_CONTENT,
								WRAP_CONTENT));
			}
		}
	}

	/**
	 * 触屏取消登录的方法
	 */
	Button.OnTouchListener OrderTicket = new Button.OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// 设置哪些触屏事件可用
			int iAction = event.getAction();
			if (iAction == MotionEvent.ACTION_CANCEL
					|| iAction == MotionEvent.ACTION_DOWN
					|| iAction == MotionEvent.ACTION_MOVE) {
				return false;
			}
			// 地址要填写
			if (checkIsAddress() && getUserNameAndUserCardId()) {
				// 用户信息进行遍历循环
				userInfo = new UserInfo();
				// 读取用户信息
				userInfo = readUserInfo();
				if (userInfo != null
						&& userInfo.getUserMobilePhone().length() > 0) {
					// 读取机票信息
					readflightInfo();
					String result = null;
					result = OrderTickets(JoinFlightInfoAndUserInfo(
							passengerInfoList, userInfo, flightInfo, classInfo));
					if (result != null) {
						// 跳转到填写姓名和身份证页面
						Intent intent = new Intent();
						// 设置传递的参数
						intent.putExtra("result", result);
						/* 指定intent要启动的类 */
						intent.setClass(OrderTicket.this, OrderSuccess.class);
						// 启动intent的Activity
						OrderTicket.this.startActivity(intent);
						return true;

					} else {
						DisplayToast("信息有误，请检查所输入的信息。");
						return false;
					}
				} else {
					DisplayToast("请先登录才能订票");
					return false;
				}
			}

			return true;
		}

	};
  /**
   * 是否选择了送票地址
   * @return
   */
	private Boolean checkIsAddress() {
		// 判断是否选择了地址
		if (isCheckSentTicket) {
			address = edAddress.getText().toString().trim();
			if (address != null && address.length() > 0) {
			} else {
				DisplayToast("请选择地址！");
				return false;
			}
			// address = edAddress.getText().toString().trim();
		}
		return true;

	}
   /**
    * 获取动态添加的编辑文本框的信息
    * @return
    */
	private boolean getUserNameAndUserCardId() {
		passengerInfoList = new ArrayList<PassengerInfo>();
		int num = 0;
		num = linearLayoutTotal.getChildCount();
		String passengerName = "";
		String passengerCardId = "";
		VerifyIdCard verifyIdCard = new VerifyIdCard();
		for (int i = 0; i < num; i++) {
			LinearLayout linearLayout;
			linearLayout = (LinearLayout) linearLayoutTotal.getChildAt(i);
			EditText userName = (EditText) linearLayout.getChildAt(0);
			EditText userCardId = (EditText) linearLayout.getChildAt(1);
			if (userName != null && userName.getText().length() > 0
					&& userCardId != null) {
				passengerInfo = new PassengerInfo();
				passengerName = userName.getText().toString();
				passengerCardId = userCardId.getText().toString();
				if (selectedcardType == 1) {
					if (verifyIdCard.verify(passengerCardId.toUpperCase())) {
						passengerInfo.setPassengerName(passengerName);
						passengerInfo.setPassengerCardID(passengerCardId);
					} else {
						DisplayToast("身份证号码输入有误");
						return false;
					}
				} else {
					passengerInfo.setPassengerName(passengerName);
					passengerInfo.setPassengerCardID(passengerCardId);
				}
			} else {
				DisplayToast("用户姓名或者证件号不能为空");
				return false;
			}
			passengerInfoList.add(passengerInfo);
		}
		return true;
	}

	/**
	 * 触屏取消登录的方法
	 */
	Button.OnTouchListener returnFlightListDetailView 
	                                          = new Button.OnTouchListener() {
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
			returnFligtListDeatailPage();
			return true;
		}
	};

	/**
	 * 返回查票页面
	 */
	private void returnFligtListDeatailPage() {
		/* 新建一个Intent对象 */
		Intent intent = new Intent();
		/* 指定intent要启动的类 */
		intent.setClass(OrderTicket.this, TicketMainApp.class);
		/* 启动一个新的Activity */
		startActivity(intent);
		/* 关闭当前的Activity */
		OrderTicket.this.finish();
	}

	/**
	 * 是否选择了送票地址
	 */
	CheckBox.OnCheckedChangeListener SentTicket 
	                                  = new CheckBox.OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (IsSentTicket.isChecked()) {
				isCheckSentTicket = true;
			}
		}
	};

	/**
	 * 拼接用户信息
	 * 
	 * @return
	 */
	private String JoinFlightInfoAndUserInfo(
			List<PassengerInfo> passengerInfoList, UserInfo userInfo,
			FlightInfo flightInfo, ClassInfo classInfo) {

		String infos = "";
		infos += "<Infos>";
		infos += "<userInfos>";
		infos += "<userId>" + userInfo.getUserName() + "</userId>";
		infos += "<userMobilePhone>" + userInfo.getUserMobilePhone()
				+ "</userMobilePhone>";
		infos += "<PassengerIdType>" + selectedcardType + "</PassengerIdType>";
		infos += "<sentAddress>" + address + "</sentAddress>";
		// 要循环
		// 解析用户数据
		infos += "<userInfo>";
		if (passengerInfoList != null) {
			for (int i = 0; i < passengerInfoList.size(); i++) {
				passengerInfo = new PassengerInfo();
				passengerInfo = passengerInfoList.get(i);

				infos += "<PassengerName" + (i + 1) + ">"
						+ passengerInfo.getPassengerName() + "</PassengerName"
						+ (i + 1) + ">";
				infos += "<PassengerIdNumber" + (i + 1) + ">"
						+ passengerInfo.getPassengerCardID()
						+ "</PassengerIdNumber" + (i + 1) + ">";
			}
		}
		infos += "</userInfo>";
		infos += "</userInfos>";
		// 拼接机票信息

		infos += "<flightInfo>";
		infos += "<Carrier>" + flightInfo.getCompanyCode() + "</Carrier>";
		infos += "<Flight>" + flightInfo.getFlightNumber() + "</Flight>";
		infos += "<Origin>" + flightInfo.getFromAirportCode() + "</Origin>";
		infos += "<Destination>" + flightInfo.getLandingAirportCode()
				+ "</Destination>";
		infos += "<OriginTime>" + flightInfo.getTakeOff() + "</OriginTime>";
		infos += "<DestinationTime>" + flightInfo.getLanding()
				+ "</DestinationTime>";
		infos += "<TClass>" + classInfo.getClassCode() + "</TClass>";
		infos += "<Fare>" + classInfo.getPublicPrice() + "</Fare>";
		infos += "<AirportTax>" + flightInfo.getAirportConstructionFee()
				+ "</AirportTax>";
		infos += "<FuelSurcharge>" + flightInfo.getFuelSurcharge()
				+ "</FuelSurcharge>";
		infos += "<PlaneModel>" + flightInfo.getPlaneModel() + "</PlaneModel>";
		infos += "<Distance>" + flightInfo.getDistance() + "</Distance>";
		infos += "</flightInfo>";
		infos += "</Infos>";
		return infos;

	}
	
  /**
   * 读取保存的用户信息
   * @return
   */
	private UserInfo readUserInfo() {
		userInfo = new UserInfo();
		// 取得活动的preferences对象.
		SharedPreferences settings = getSharedPreferences(LoginApp.PREFS_NAME,
				Activity.MODE_WORLD_READABLE);
		userInfo.setUserName(settings.getString("userName",
				userInfo.getUserName()));
		userInfo.setUserMobilePhone(settings.getString("userMobile",
				userInfo.getUserMobilePhone()));
		userInfo.setUserEmail(settings.getString("userEmail",
				userInfo.getUserEmail()));
		userInfo.setUserId(settings.getString("userId", userInfo.getUserId()));
		return userInfo;
	}

	/* 显示Toast */
	public void DisplayToast(String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}

	/*
	 * 读取机票信息
	 */
	public void readflightInfo() {
		// 取得活动的preferences对象.
		SharedPreferences settings = getSharedPreferences(LoginApp.PREFS_NAME,
				Activity.MODE_WORLD_READABLE);
		// 取得值.
		flightInfo.setFlightNumber(settings.getString("FlightNumber",
				flightInfo.getFlightNumber()));
		classInfo.setClassName(settings.getString("ClassName",
				classInfo.getClassName()));
		classInfo.setPublicPrice(settings.getString("PublicPrice",
				classInfo.getPublicPrice()));
		flightInfo.setCompanyCode(settings.getString("CompanyCode",
				flightInfo.getCompanyCode()));
		flightInfo.setCompanyName(settings.getString("CompanyName",
				flightInfo.getCompanyName()));
		flightInfo.setFromAirportName(settings.getString("FromAirportName",
				flightInfo.getFromAirportName()));
		flightInfo.setLandingAirportName(settings.getString(
				"LandingAirportName", flightInfo.getLandingAirportName()));
		flightInfo.setTakeOff(settings.getString("TakeOff",
				flightInfo.getTakeOff()));
		flightInfo.setLanding(settings.getString("Landing",
				flightInfo.getLanding()));
		flightInfo.setIsElectronicTicket(settings.getString(
				"IsElectronicTicket", flightInfo.getIsElectronicTicket()));
		flightInfo.setIsPassBy(settings.getString("IsPassBy",
				flightInfo.getIsPassBy()));
		flightInfo.setAirportConstructionFee(settings.getString(
				"AirportConstructionFee",
				flightInfo.getAirportConstructionFee()));
		flightInfo.setFuelSurcharge(settings.getString("FuelSurcharge",
				flightInfo.getFuelSurcharge()));
		flightInfo.setPlaneModel(settings.getString("PlaneModel",
				flightInfo.getPlaneModel()));
		flightInfo.setFromAirportCode(settings.getString("FromAirportCode",
				flightInfo.getFromAirportCode()));
		flightInfo.setLandingAirportCode(settings.getString(
				"LandingAirportCode", flightInfo.getLandingAirportCode()));
		flightInfo.setDistance(settings.getString("Distance",
				flightInfo.getDistance()));
		classInfo.setClassCode(settings.getString("ClassCode",
				classInfo.getClassCode()));
		classInfo.setRefundPercent(settings.getString("RefundPercent",
				classInfo.getRefundPercent()));
		classInfo.setIsPolicyAvailable(settings.getString("IsPolicyAvailable",
				classInfo.getIsPolicyAvailable()));

	}
  /**
   * 预订机票
   * @param info
   * @return
   */
	private String OrderTickets(String info) {
		// 参数
		String params = null;
		String resultData = null;

		try {
			// 传递的参数设定编码格式，以防止乱码
			params = URLEncoder.encode("ticketsInfo", "UTF-8") + "="
					+ URLEncoder.encode(info, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			Log.e(DEBUG_TAG, "注册方法中参数传递错误！");
		}
		urlTicketsInfo = this.getString(R.string.OrderTicketURL);
		// 得到字符串集合
		resultData = ConnUrlHelper.getPostHttpURLConnByUrl(urlTicketsInfo,
				params);
		if (resultData != null && resultData.length() > 0) {
			if (resultData.contains("<Result>")) {
				resultData = resultData.substring(
						resultData.indexOf("<Result>") + 8,
						resultData.lastIndexOf("</Result>"));
			} else {
				resultData = "订票失败 ";
				DisplayToast(resultData);
			}
		}
		return resultData;
	}
}
