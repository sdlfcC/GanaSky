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
 * �û���Ϣѡ������д����ƱԤ����ʵ��
 * @author 
 *
 */
public class OrderTicket extends Activity {

	private final String DEBUG_TAG = "OrderTicketApp";
	// ʼ���ؿؼ�
	private TextView txtSetCity;
	// Ŀ�ĵؿؼ�
	private TextView txtOffCity;
	// ʼ��ʱ��
	private TextView txtSetTime;
   //����
	private TextView txtWeek;
	// ѡ������
	private Spinner spinnerchoicePersonNumber;
	// ֤������
	private Spinner spinnerSelectCardType;

	private FlightInfo flightInfo;
	private ClassInfo classInfo;
	// ����֤��
	private String[] cardType = { "���֤", "����", "����֤" };
	// ��������
	private String[] personsNumber = {"1", "2", "3", "4", "5", "6", "7", "8",
			"9", "10" };
	// ������������ѡ�񿨺�ѡ������
	private ArrayAdapter<String> personsNumberAdapter;

	// ѡ�������
	private String selectedPersonsNumber;
	// ѡ�񿨵�����
	private int selectedcardType = 1;

	// �����ܵ�layout
	LinearLayout linearLayoutTotal;
	// ��Ϣȷ����ť
	private Button btnOrder;
	private Button btnReturn;
    //�˿���Ϣ
	private PassengerInfo passengerInfo;
	private List<PassengerInfo> passengerInfoList;
    //�Ƿ���Ʊ��ѡ��
	private CheckBox IsSentTicket;
	//��Ʊ��ַ
	private EditText edAddress;
	 //�Ƿ���Ҫ��Ʊ
	private boolean isCheckSentTicket = false;
    //�û���Ϣ��
	private UserInfo userInfo;
	
	private String address;
	private String urlTicketsInfo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.orderticket);

		// ��ʼ���ؼ�
		initControl();

		// ����ҳ�洫�����Ĳ���
		setTicketValue();

		// ��ʼ��ѡ�����ͺ����֤������
		selectedPersonsNumber = personsNumber[0];

		// ѡ��֤������
		selectCardType();
		// ѡ������
		selectPassageNumber();
		// ѡ��֤������
		spinnerSelectCardType.setOnItemSelectedListener(selectedCardType);
		// ѡ������
		spinnerchoicePersonNumber
				.setOnItemSelectedListener(selectedPassageNumber);

		btnReturn.setOnTouchListener(returnFlightListDetailView);
		// �Ƿ���Ʊ
		IsSentTicket.setOnCheckedChangeListener(SentTicket);
		// ���ѡ������Ʊ
		if (IsSentTicket.isChecked()) {
			isCheckSentTicket = true;
		}
		// ��Ʊ��ť
		btnOrder.setOnTouchListener(OrderTicket);

	}

	/**
	 * ��ʼ����ҳ���ϵĿؼ�
	 */
	private void initControl() {
		// ��Layout�����ListView
		txtSetCity = (TextView) findViewById(R.id.orderticket_txtSetCity);
		txtOffCity = (TextView) findViewById(R.id.orderticket_txtOffCity);
		txtSetTime = (TextView) findViewById(R.id.orderticket_txtTime);
		txtWeek = (TextView) findViewById(R.id.orderticket_txtWeek);
	  spinnerchoicePersonNumber = (Spinner) findViewById(R.id.orderticket_choicePersonNumber);
		spinnerSelectCardType = (Spinner) findViewById(R.id.orderticket_selectCardType);
		btnOrder = (Button) findViewById(R.id.orderticket_btnOK);
		btnReturn = (Button) findViewById(R.id.orderticket_btnReturn);
		linearLayoutTotal = (LinearLayout) findViewById(R.id.orderticket_lineLayOut);
		// �Ƿ���Ʊ
		IsSentTicket = (CheckBox) findViewById(R.id.orderticket_IsSentTicket);
		edAddress = (EditText) findViewById(R.id.orderticket_edAddress);
	}

	/**
	 * ���մ������Ĳ���
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
			// ��ֵ
			txtSetCity.setText(flightInfo.getFromAirportName());
			txtOffCity.setText(flightInfo.getLandingAirportName());
			txtSetTime.setText(flightInfo.getDateTime());
			txtWeek.setText(flightInfo.getWeekDay());
		}

	}

	/**
	 * ֤������
	 */
	private void selectCardType() {

		personsNumberAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, cardType);

		// ���������б�ķ��
		personsNumberAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// ��adapter��ӵ�m_Spinner��
		spinnerSelectCardType.setAdapter(personsNumberAdapter);
	}

	/**
	 * ���˿�������������б�ֵ
	 */
	private void selectPassageNumber() {

		personsNumberAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, personsNumber);

		// ���������б�ķ��
		personsNumberAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// ��adapter��ӵ�m_Spinner��
		spinnerchoicePersonNumber.setAdapter(personsNumberAdapter);
	}

	// ѡ��֤�������б���ѡ���¼�
	Spinner.OnItemSelectedListener selectedCardType 
	                                         = new Spinner.OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			String type = "";
			type = parent.getItemAtPosition(position).toString();
			if (type.trim().equals("���֤")) {
				selectedcardType = 1;
			} else if (type.trim().equals("����")) {
				selectedcardType = 2;
			} else if (type.trim().equals("����֤")) {
				selectedcardType = 3;
			}

		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
		}
	};

	// ѡ�����������б���ѡ���¼�
	Spinner.OnItemSelectedListener selectedPassageNumber 
	                                         = new Spinner.OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			selectedPersonsNumber = parent.getItemAtPosition(position)
					.toString();
			if (selectedPersonsNumber != null) {
				// ��̬�������
				AddItem(Integer.parseInt(selectedPersonsNumber));
			} else {
				DisplayToast("��ѡ��֤�����ͻ�������");
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
		}
	};

	/**
	 * �������
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
				editTextName.setHint("������֤���϶�Ӧ������");
				editTextName.setTextSize(11);
				linearLayout1.addView(editTextName,
						new LinearLayout.LayoutParams(WRAP_CONTENT,
								WRAP_CONTENT));

				EditText editTextCardId = new EditText(this);
				editTextCardId.setTextSize(11);
				editTextCardId.setHint("��������ȷ��֤������     ");
				linearLayout1.addView(editTextCardId,
						new LinearLayout.LayoutParams(WRAP_CONTENT,
								WRAP_CONTENT));
			}
		}
	}

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
			// ��ַҪ��д
			if (checkIsAddress() && getUserNameAndUserCardId()) {
				// �û���Ϣ���б���ѭ��
				userInfo = new UserInfo();
				// ��ȡ�û���Ϣ
				userInfo = readUserInfo();
				if (userInfo != null
						&& userInfo.getUserMobilePhone().length() > 0) {
					// ��ȡ��Ʊ��Ϣ
					readflightInfo();
					String result = null;
					result = OrderTickets(JoinFlightInfoAndUserInfo(
							passengerInfoList, userInfo, flightInfo, classInfo));
					if (result != null) {
						// ��ת����д���������֤ҳ��
						Intent intent = new Intent();
						// ���ô��ݵĲ���
						intent.putExtra("result", result);
						/* ָ��intentҪ�������� */
						intent.setClass(OrderTicket.this, OrderSuccess.class);
						// ����intent��Activity
						OrderTicket.this.startActivity(intent);
						return true;

					} else {
						DisplayToast("��Ϣ�����������������Ϣ��");
						return false;
					}
				} else {
					DisplayToast("���ȵ�¼���ܶ�Ʊ");
					return false;
				}
			}

			return true;
		}

	};
  /**
   * �Ƿ�ѡ������Ʊ��ַ
   * @return
   */
	private Boolean checkIsAddress() {
		// �ж��Ƿ�ѡ���˵�ַ
		if (isCheckSentTicket) {
			address = edAddress.getText().toString().trim();
			if (address != null && address.length() > 0) {
			} else {
				DisplayToast("��ѡ���ַ��");
				return false;
			}
			// address = edAddress.getText().toString().trim();
		}
		return true;

	}
   /**
    * ��ȡ��̬��ӵı༭�ı������Ϣ
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
						DisplayToast("���֤������������");
						return false;
					}
				} else {
					passengerInfo.setPassengerName(passengerName);
					passengerInfo.setPassengerCardID(passengerCardId);
				}
			} else {
				DisplayToast("�û���������֤���Ų���Ϊ��");
				return false;
			}
			passengerInfoList.add(passengerInfo);
		}
		return true;
	}

	/**
	 * ����ȡ����¼�ķ���
	 */
	Button.OnTouchListener returnFlightListDetailView 
	                                          = new Button.OnTouchListener() {
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
			returnFligtListDeatailPage();
			return true;
		}
	};

	/**
	 * ���ز�Ʊҳ��
	 */
	private void returnFligtListDeatailPage() {
		/* �½�һ��Intent���� */
		Intent intent = new Intent();
		/* ָ��intentҪ�������� */
		intent.setClass(OrderTicket.this, TicketMainApp.class);
		/* ����һ���µ�Activity */
		startActivity(intent);
		/* �رյ�ǰ��Activity */
		OrderTicket.this.finish();
	}

	/**
	 * �Ƿ�ѡ������Ʊ��ַ
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
	 * ƴ���û���Ϣ
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
		// Ҫѭ��
		// �����û�����
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
		// ƴ�ӻ�Ʊ��Ϣ

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
   * ��ȡ������û���Ϣ
   * @return
   */
	private UserInfo readUserInfo() {
		userInfo = new UserInfo();
		// ȡ�û��preferences����.
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

	/* ��ʾToast */
	public void DisplayToast(String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}

	/*
	 * ��ȡ��Ʊ��Ϣ
	 */
	public void readflightInfo() {
		// ȡ�û��preferences����.
		SharedPreferences settings = getSharedPreferences(LoginApp.PREFS_NAME,
				Activity.MODE_WORLD_READABLE);
		// ȡ��ֵ.
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
   * Ԥ����Ʊ
   * @param info
   * @return
   */
	private String OrderTickets(String info) {
		// ����
		String params = null;
		String resultData = null;

		try {
			// ���ݵĲ����趨�����ʽ���Է�ֹ����
			params = URLEncoder.encode("ticketsInfo", "UTF-8") + "="
					+ URLEncoder.encode(info, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			Log.e(DEBUG_TAG, "ע�᷽���в������ݴ���");
		}
		urlTicketsInfo = this.getString(R.string.OrderTicketURL);
		// �õ��ַ�������
		resultData = ConnUrlHelper.getPostHttpURLConnByUrl(urlTicketsInfo,
				params);
		if (resultData != null && resultData.length() > 0) {
			if (resultData.contains("<Result>")) {
				resultData = resultData.substring(
						resultData.indexOf("<Result>") + 8,
						resultData.lastIndexOf("</Result>"));
			} else {
				resultData = "��Ʊʧ�� ";
				DisplayToast(resultData);
			}
		}
		return resultData;
	}
}
