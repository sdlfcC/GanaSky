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
 * List��Ϣ���鹦��
 * @author 
 *
 */
public class FlightListDetail extends Activity {
	// �ؼ�
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
		// ��ʼ���ؼ�
		initControl();
		// ����ҳ�洫�����Ĳ���
		setTicketValue();
       //�����¼�
		btnOrder.setOnTouchListener(OrderTicket);
		btnReturn.setOnTouchListener(returnFlightListView);
	}

   /**
    * ��ȡ����ؼ�
    */
	private void initControl() {
		// ��Layout�����ListView
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
    * �õ���һ�����洫�ݹ����Ļ�Ʊ��Ϣ
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
		txtPrice.setText(classInfo.getPublicPrice() + "Ԫ");
		txtAirportConstructionFeeAndFuelSurcharge.setText(flightInfo
				.getAirportConstructionFee()
				+ "/"
				+ flightInfo.getFuelSurcharge());
	}

	/**
	 * ����ȡ����¼�ķ���
	 */
	Button.OnTouchListener returnFlightListView = new Button.OnTouchListener() {

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
			returnFligtListPage();
			return true;
		}

	};

	/**
	 * ����ȡ����¼�ķ���
	 */
	Button.OnTouchListener OrderTicket = new Button.OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// ������Щ�����¼�����
			int iAction = event.getAction();
			if (iAction == MotionEvent.ACTION_CANCEL
					|| iAction == MotionEvent.ACTION_DOWN
					|| iAction == MotionEvent.ACTION_MOVE) {
				return false;
			}
			// ���Ƿ��¼��
			// ȡ�û��preferences����.
			// �ж��Ƿ��е绰����
			String userMoblie = "";
			String userName="";
			SharedPreferences settings = getSharedPreferences(
					LoginApp.PREFS_NAME, Context.MODE_WORLD_READABLE);
			userMoblie = settings.getString("userMobile", userMoblie);
			userName=settings.getString("userName", userName);
			if (userMoblie != null && userMoblie.length() > 0 && userName.length()>0) {
				// ��ת����д���������֤ҳ��
				Intent intent = new Intent();
				// ���ô��ݵĲ���
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
				/* ָ��intentҪ�������� */
				intent.setClass(FlightListDetail.this, OrderTicket.class);
				// ����intent��Activity
				FlightListDetail.this.startActivity(intent);
				return true;
			} else {
				DisplayToast("����δ��¼�����ܶ�Ʊ�����ȵ�¼...");
				returnLoginPage();
				return false;
			}			
		}
	};

	/**
	 * ���ز�Ʊҳ��
	 */
	private void returnFligtListPage() {
		/* �½�һ��Intent���� */
		Intent intent = new Intent();
		/* ָ��intentҪ�������� */
		intent.setClass(FlightListDetail.this, TicketMainApp.class);
		/* ����һ���µ�Activity */
		startActivity(intent);
		/* �رյ�ǰ��Activity */
		FlightListDetail.this.finish();
	}

	/**
	 * ����ע��ҳ��
	 */
	private void returnLoginPage() {
		/* �½�һ��Intent���� */
		Intent intent = new Intent();
		/* ָ��intentҪ�������� */
		intent.setClass(FlightListDetail.this, LoginApp.class);
		/* ����һ���µ�Activity */
		startActivity(intent);
		/* �رյ�ǰ��Activity */
		FlightListDetail.this.finish();
	}

	/* ��ʾToast */
	public void DisplayToast(String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}
}
