package com.ganasky;

import com.ganasky.model.ClassInfo;
import com.ganasky.model.FlightInfo;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
/**
 * List信息详情功能
 * @author 
 *
 */
public class FlightListDetail extends Activity {
	// 控件
	private TextView txtTime;
	private TextView txtWeek;
	private TextView txtSetCity;
	private TextView txtOffCity;
	private TextView txtPlaneModel;
	private TextView txtTakeOffAndFromAirportName;
	private TextView txtLandingAndToAirportName;
	private TextView txtElectronicTicket;
	private TextView txtPrice;
	private TextView txtAirportConstructionFeeAndFuelSurcharge;
	private TextView txtFlightNumber;
	private Button btnOrder;
	private Button btnReturn;
	private FlightInfo flightInfo;
	private ClassInfo classInfo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.flightlist_detail);
		// 初始化控件
		initControl();
		// 接收页面传过来的参数
		setTicketValue();
       //监听事件
		btnOrder.setOnTouchListener(OrderTicket);
		btnReturn.setOnTouchListener(returnFlightListView);
	}

   /**
    * 获取界面控件
    */
	private void initControl() {
		// 绑定Layout里面的ListView
	 txtTime = (TextView) findViewById(R.id.flightlistDetail_txtTime);
	txtWeek = (TextView) findViewById(R.id.flightlistDetail_txtWeek);
	txtFlightNumber = (TextView) findViewById(R.id.flightlistDetail_txtFlightNumber);
	txtSetCity = (TextView) findViewById(R.id.flightlistDetail_txtSetCity);
	txtOffCity = (TextView) findViewById(R.id.flightlistDetail_txtOffCity);
	txtPlaneModel = (TextView) findViewById(R.id.flightlistDetail_txtPlaneModel);
	txtTakeOffAndFromAirportName = 
		(TextView) findViewById(R.id.flightlistDetail_txtTakeOffAndFromAirportName);
	txtLandingAndToAirportName =
		(TextView) findViewById(R.id.flightlistDetail_txtLandingAndToAirportName);
	txtElectronicTicket = (TextView) findViewById(R.id.flightlistDetail_txtElectronicTicket);
	txtPrice = (TextView) findViewById(R.id.flightlistDetail_txtPrice);
	txtAirportConstructionFeeAndFuelSurcharge =
	 (TextView) findViewById(R.id.flightlistDetail_txtAirportConstructionFeeAndFuelSurcharge);
	btnOrder = (Button) findViewById(R.id.flightlistDetail_btnOrder);
	btnReturn = (Button) findViewById(R.id.flightlistDetail_btnReturn);
	}
   /**
    * 得到上一个界面传递过来的机票信息
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
		txtTime.setText(flightInfo.getDateTime());
		txtWeek.setText(flightInfo.getWeekDay());
		txtFlightNumber.setText(flightInfo.getFlightNumber());
		txtSetCity.setText(flightInfo.getFromAirportName());
		txtOffCity.setText(flightInfo.getLandingAirportName());
		txtPlaneModel.setText(flightInfo.getPlaneModel());
		txtTakeOffAndFromAirportName.setText(flightInfo.getTakeOff()
				+ flightInfo.getFromAirportName());
		txtLandingAndToAirportName.setText(flightInfo.getLanding()
				+ flightInfo.getLandingAirportName());
		txtElectronicTicket.setText(flightInfo.getIsElectronicTicket());
		txtPrice.setText(classInfo.getPublicPrice() + "元");
		txtAirportConstructionFeeAndFuelSurcharge.setText(flightInfo
				.getAirportConstructionFee()
				+ "/"
				+ flightInfo.getFuelSurcharge());
	}

	/**
	 * 触屏取消登录的方法
	 */
	Button.OnTouchListener returnFlightListView = new Button.OnTouchListener() {

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
			returnFligtListPage();
			return true;
		}

	};

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
			// 看是否登录过
			// 取得活动的preferences对象.
			// 判断是否有电话号码
			String userMoblie = "";
			String userName="";
			SharedPreferences settings = getSharedPreferences(
					LoginApp.PREFS_NAME, Context.MODE_WORLD_READABLE);
			userMoblie = settings.getString("userMobile", userMoblie);
			userName=settings.getString("userName", userName);
			if (userMoblie != null && userMoblie.length() > 0 && userName.length()>0) {
				// 跳转到填写姓名和身份证页面
				Intent intent = new Intent();
				// 设置传递的参数
				intent.putExtra("dateTime", flightInfo.getDateTime());
				intent.putExtra("dayofweek", flightInfo.getWeekDay());
				intent.putExtra("TakeOff", flightInfo.getTakeOff());
				intent.putExtra("Landing", flightInfo.getLanding());
				intent.putExtra("PlaneModel", flightInfo.getPlaneModel());
				intent.putExtra("FromAirportName",
						flightInfo.getFromAirportName());
				intent.putExtra("LandingAirportName",
						flightInfo.getLandingAirportName());
				intent.putExtra("PublicPrice", classInfo.getPublicPrice());
				intent.putExtra("AirportConstructionFee",
						flightInfo.getAirportConstructionFee());
				intent.putExtra("FuelSurcharge", flightInfo.getFuelSurcharge());
				intent.putExtra("IsElectronicTicket",
						flightInfo.getIsElectronicTicket());
				intent.putExtra("IsPassBy", flightInfo.getIsPassBy());
				intent.putExtra("FlightNumber", flightInfo.getFlightNumber());
				/* 指定intent要启动的类 */
				intent.setClass(FlightListDetail.this, OrderTicket.class);
				// 启动intent的Activity
				FlightListDetail.this.startActivity(intent);
				return true;
			} else {
				DisplayToast("您还未登录，不能订票，请先登录...");
				returnLoginPage();
				return false;
			}			
		}
	};

	/**
	 * 返回查票页面
	 */
	private void returnFligtListPage() {
		/* 新建一个Intent对象 */
		Intent intent = new Intent();
		/* 指定intent要启动的类 */
		intent.setClass(FlightListDetail.this, TicketMainApp.class);
		/* 启动一个新的Activity */
		startActivity(intent);
		/* 关闭当前的Activity */
		FlightListDetail.this.finish();
	}

	/**
	 * 返回注册页面
	 */
	private void returnLoginPage() {
		/* 新建一个Intent对象 */
		Intent intent = new Intent();
		/* 指定intent要启动的类 */
		intent.setClass(FlightListDetail.this, LoginApp.class);
		/* 启动一个新的Activity */
		startActivity(intent);
		/* 关闭当前的Activity */
		FlightListDetail.this.finish();
	}

	/* 显示Toast */
	public void DisplayToast(String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}
}
