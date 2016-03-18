package com.ganasky;

import java.io.BufferedInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import com.ganasky.Help.ConnUrlHelper;
import com.ganasky.Help.DoXmlHelper;
import com.ganasky.model.ClassInfo;
import com.ganasky.model.FlightInfo;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
/**
 * ������Ϣ�б�
 * @author 
 *
 */
public class FlightInfoMainApp extends Activity {
   //���峣��
	private final String DEBUG_TAG = "FlightInfoMainApp";
	// ѡ���ֵ(Ҫ���óɾ�̬�������ܶ���)
	private String selectedStartCity = null;
	private String selectedOffCity = null;
	private String selectedTime = null;
	// ѡ��Ĳ�λ����
	private String selectedPlaneClass = null;
	private String selectedPlaneCompany = null;
	// ��ѯ��ַ
	private String ticketSearchUrl;

	// ������Ϣ�б�
	private List<FlightInfo> flightInfoList = null;
	// ������Ϣ
	private FlightInfo flightInfo = null;
	// �����λ�б�
	private List<ClassInfo> classInfoList = null;
	// �����λ
	private ClassInfo classInfo = null;
	// listView�ؼ�
	private ListView flightListView;
	// ʼ���ؿؼ�
	private TextView txtSetCity;
	// Ŀ�ĵؿؼ�
	private TextView txtOffCity;
	// ʼ��ʱ��
	private TextView txtSetTime;
	// ��ͷ
	private TextView txtArrow;
	// ʱ������ť
	private Button flightlistmain_orderTime;
	// �۸�����ť
	private Button flightlistmain_orderPrice;
	// �ж��Ƿ������ݣ�����һ����־
	private int flag = 0;

	// ���ɶ�̬���飬��������(������ó���)
	ArrayList<HashMap<String, Object>> listItem = null;
	//��ʼ��ListAdapter������
	ListAdapter listItemAdapter = null;
    //���صĺ�����Ϣ������ʾ���ı�����
	private TextView txtflightCount;
	  //���صĺ�����Ϣ����
	private int flightCount = 0;
	//���ص�������Ϣ�ַ���
	private String LongFlightInfo = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.flightlistmain);

		// ��ʼ���ؼ�
		initControl();

		// ����ҳ�洫�����Ĳ���
		setTicketValue();

		// ��ȡString�е���Ϣ
		ticketSearchUrl = this.getString(R.string.TicketSearchUrl);
		// ��ѯ(Ĭ�ϸ��Ӽ۸�����)
		flightInfoList = Search(ticketSearchUrl);

		// ��������ԴlistItem��map������
		listItem = initCityData(flightInfoList);
		// �󶨵�ҳ��ؼ���
		bingList(listItem);
		if (flightCount != 0) {
			txtflightCount.setText("���鵽" + flightCount + "��¼");
		}
		//�����¼�����
		flightlistmain_orderPrice.setOnTouchListener(orderByPrice);
		flightlistmain_orderTime.setOnTouchListener(oderByTime);
		flightListView.setOnItemClickListener(selectFlightInfo);
	}

	  /**
	   * ��ʼ���ؼ�
	   */
	private void initControl() {
		// ��Layout�����ListView
		flightListView = (ListView) findViewById(R.id.flightlistmain_listFlight);
		txtSetCity = (TextView) findViewById(R.id.flightlistmain_txtSetCity);
		txtOffCity = (TextView) findViewById(R.id.flightlistmain_txtOffCity);
		txtSetTime = (TextView) findViewById(R.id.flightlistmain_txtTime);
		txtArrow = (TextView) findViewById(R.id.flightlistmain_arrow);
		flightlistmain_orderTime = (Button) findViewById(R.id.flightlistmain_orderTime);
		flightlistmain_orderPrice = (Button) findViewById(R.id.flightlistmain_orderPrice);
		txtflightCount = (TextView) findViewById(R.id.flightlistmain_txtflightCount);
	}

	/**
	 * ����ҳ�洫������ֵ����ֵ���ؼ�
	 */
	private void setTicketValue() {
		Intent intent = getIntent();
		selectedStartCity = intent.getStringExtra("selectedStartCity");
		selectedOffCity = intent.getStringExtra("selectedOffCity");
		selectedTime = intent.getStringExtra("selectedTime");
		selectedPlaneClass = intent.getStringExtra("selectedPlaneClass");
		selectedPlaneCompany = intent.getStringExtra("selectedPlaneCompany");

		// �������������ݸ�ֵ
		if (selectedTime != null && selectedTime.length() > 0) {
			txtSetTime.setText(selectedTime);
		}

		if (selectedStartCity != null && selectedStartCity.length() > 0) {
			txtSetCity.setText(selectedStartCity);
		}
		txtArrow.setText("->");

		if (selectedOffCity != null && selectedOffCity.length() > 0) {
			txtOffCity.setText(selectedOffCity);
		}
	}
  /**
   * ��ArrayList����Դ�󶨵�ListView������
   * @param listItem
   */
	private void bingList(ArrayList<HashMap<String, Object>> listItem) {
		// ���
		listItemAdapter = null;
		if (flag == 1) {
			// ������������Item�Ͷ�̬�����Ӧ��Ԫ��
			// CompanyCode��Ȼ��ͼƬ��ʾ����������ҲҪ���
			listItemAdapter = new ImageAdapterOfFlightList(this, listItem, // ����Դ
					R.layout.flightlist_items, // layout�е�citylist_items�Ӳ�
					new String[] { "CompanyCodeImage", "FlightNumber",
							"ClassName", "PublicPrice", "CompanyName",
							"FromAirportName", "TakeOffTemp", "AvailableSeat",
							"LandingAirportName", "LandingTemp" }, // ��̬���ݵ�����
					new int[] { R.id.flightList_CompanyCode,
							R.id.flightList_FlightNumber,
							R.id.flightList_ClassName,
							R.id.flightList_PublicPrice,
							R.id.flightList_CompanyName,
							R.id.flightList_FromAirportName,
							R.id.flightList_TakeOff,
							R.id.flightList_AvailableSeat2,
							R.id.flightList_LandingAirportName,
							R.id.flightList_Landing } // ��list_items�еĿؼ�
			);

			flag = 0;
			// ��û������
		} else if (flag == 0) {
			listItemAdapter = new SimpleAdapter(this, listItem, // ����Դ
					R.layout.flightlist_items,// layout�е�citylist_items�Ӳ�
					new String[] { "FlightNumber" }, // ��̬���ݵ�����
					new int[] { R.id.flightList_FlightNumber } // ��list_items�еĿؼ�
			);
		}
		// ��ӵ�listView��
		flightListView.setAdapter(listItemAdapter);
	}
  /**
   * ���ݲ������Ӻ�̨��ѯ������Ϣ
   * @param ticketSearchUrl
   * @return
   */
	private List<FlightInfo> Search(String ticketSearchUrl) {
		// �õ��ַ�������
		String resultData = null;
		try {

			String params = null;
			try {
				// ���ݵĲ����趨�����ʽ���Է�ֹ����
				params = URLEncoder.encode("from", "UTF-8") + "="
						+ URLEncoder.encode(selectedStartCity, "UTF-8");
				params += "&" + URLEncoder.encode("to", "UTF-8") + "="
						+ URLEncoder.encode(selectedOffCity, "UTF-8");
				params += "&" + URLEncoder.encode("time", "UTF-8") + "="
						+ URLEncoder.encode(selectedTime, "UTF-8");
				params += "&" + URLEncoder.encode("class", "UTF-8") + "="
						+ URLEncoder.encode(selectedPlaneClass, "UTF-8");
				params += "&" + URLEncoder.encode("company", "UTF-8") + "="
						+ URLEncoder.encode(selectedPlaneCompany, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				Log.e(DEBUG_TAG, "��Ʊ��ѯ�����в������ݴ���");
			}
			resultData = ConnUrlHelper.getLongPostHttpURLConnByUrl(
					ticketSearchUrl, params);
			if (resultData != null && resultData.length() > 0) {
				if (resultData.contains("<Result>")) {
					resultData = resultData.substring(
							resultData.indexOf("<Result>") + 8,
							resultData.lastIndexOf("</Result>"));
				} else if (resultData.contains("<FlightInfo>")
						&& resultData.contains("<ClassInfoInfo>")) {
					// ����Ʊ����Ϣ(��������쳣)
					flightInfoList = GetTicketInfo(resultData);
					// ��������
					LongFlightInfo = resultData;
				}
			}
			// ������try
		} catch (Exception e) {
			DisplayToast("Flight�쳣��");
		}
		return flightInfoList;
	}

	/**
	 * ������Ʊ��Ϣ
	 * 
	 * @param resultData
	 *            return ���ز�ѯ���ĺ�����Ϣlist
	 */
	private List<FlightInfo> GetTicketInfo(String resultData) {
		try {
			// ת��Ϊxml�ĵ�
			Document docFlightInfo = DoXmlHelper.StringToxml(resultData);
			// ��ȡ�ĵ��е�Ԫ��
			Element root = docFlightInfo.getDocumentElement();
			// �õ���Ҫ�Ľڵ㼯��
			NodeList flightInfoNodeList = root
					.getElementsByTagName("FlightInfo");

			// ��ʼ��������ϢList����
			flightInfoList = new ArrayList<FlightInfo>();
			for (int i = 0; i < flightInfoNodeList.getLength(); i++) {
				// �õ�FlightInfo�е�����Ԫ��
				Element flightElement = (Element) flightInfoNodeList.item(i);
				Element companyCode = (Element) flightElement
						.getElementsByTagName("CompanyCode").item(0);
				Element companyName = (Element) flightElement
						.getElementsByTagName("CompanyName").item(0);
				Element flightNumber = (Element) flightElement
						.getElementsByTagName("FlightNumber").item(0);
				Element planeModel = (Element) flightElement
						.getElementsByTagName("PlaneModel").item(0);
				Element takeOff = (Element) flightElement.getElementsByTagName(
						"TakeOff").item(0);
				Element landing = (Element) flightElement.getElementsByTagName(
						"Landing").item(0);
				Element fromAirportCode = (Element) flightElement
						.getElementsByTagName("FromAirportCode").item(0);
				Element fromAirportName = (Element) flightElement
						.getElementsByTagName("FromAirportName").item(0);
				Element formatedFromAiport = (Element) flightElement
						.getElementsByTagName("FormatedFromAiport").item(0);
				Element fuelSurcharge = (Element) flightElement
						.getElementsByTagName("FuelSurcharge").item(0);
				Element landingAirportCode = (Element) flightElement
						.getElementsByTagName("LandingAirportCode").item(0);
				Element landingAirportName = (Element) flightElement
						.getElementsByTagName("LandingAirportName").item(0);
				Element formatedLandingAirport = (Element) flightElement
						.getElementsByTagName("FormatedLandingAirport").item(0);
				Element isPassBy = (Element) flightElement
						.getElementsByTagName("IsPassBy").item(0);
				Element isElectronicTicket = (Element) flightElement
						.getElementsByTagName("IsElectronicTicket").item(0);
				Element airportConstructionFee = (Element) flightElement
						.getElementsByTagName("AirportConstructionFee").item(0);
				Element distance = (Element) flightElement
						.getElementsByTagName("Distance").item(0);
				Element availableClassCount = (Element) flightElement
						.getElementsByTagName("AvailableClassCount").item(0);
				Element availableClassPolicyCount = (Element) flightElement
						.getElementsByTagName("AvailableClassPolicyCount")
						.item(0);
				Element xmlClasses = (Element) flightElement
						.getElementsByTagName("xmlClasses").item(0);

				// ������Ԫ�ش���FlightInfo
				flightInfo = new FlightInfo();
				flightInfo.setAirportConstructionFee(airportConstructionFee
						.getFirstChild().getNodeValue());
				flightInfo.setAvailableClassCount(availableClassCount
						.getFirstChild().getNodeValue());
				flightInfo
						.setAvailableClassPolicyCount(availableClassPolicyCount
								.getFirstChild().getNodeValue());
				flightInfo.setCompanyCode(companyCode.getFirstChild()
						.getNodeValue());
				flightInfo.setCompanyName(companyName.getFirstChild()
						.getNodeValue());
				flightInfo.setDistance(distance.getFirstChild().getNodeValue());
				flightInfo.setFlightNumber(flightNumber.getFirstChild()
						.getNodeValue());
				flightInfo.setFormatedFromAiport(formatedFromAiport
						.getFirstChild().getNodeValue());
				flightInfo.setFormatedLandingAirport(formatedLandingAirport
						.getFirstChild().getNodeValue());
				flightInfo.setFromAirportCode(fromAirportCode.getFirstChild()
						.getNodeValue());
				flightInfo.setFromAirportName(fromAirportName.getFirstChild()
						.getNodeValue());
				flightInfo.setFuelSurcharge(fuelSurcharge.getFirstChild()
						.getNodeValue());
				flightInfo.setIsElectronicTicket(isElectronicTicket
						.getFirstChild().getNodeValue());
				flightInfo.setIsPassBy(isPassBy.getFirstChild().getNodeValue());
				flightInfo.setLanding(landing.getFirstChild().getNodeValue());
				flightInfo.setLandingAirportCode(landingAirportCode
						.getFirstChild().getNodeValue());
				flightInfo.setLandingAirportName(landingAirportName
						.getFirstChild().getNodeValue());
				flightInfo.setPlaneModel(planeModel.getFirstChild()
						.getNodeValue());
				flightInfo.setTakeOff(takeOff.getFirstChild().getNodeValue());

				Element ClassInfoInfo = (Element) xmlClasses
						.getElementsByTagName("ClassInfoInfo").item(0);
				// �õ���Ҫ�Ľڵ㼯��
				NodeList classInfoNodeList = ClassInfoInfo
						.getElementsByTagName("ClassInfo");
				// ��ʼ��������ϢList����
				classInfoList = new ArrayList<ClassInfo>();

				for (int j = 0; j < classInfoNodeList.getLength(); j++) {
					Element classInfoElement = (Element) classInfoNodeList
							.item(j);
					Element classCode = (Element) classInfoElement
							.getElementsByTagName("ClassCode").item(0);
					Element className = (Element) classInfoElement
							.getElementsByTagName("ClassName").item(0);
					Element availableSeat = (Element) classInfoElement
							.getElementsByTagName("AvailableSeat").item(0);
					Element publicPrice = (Element) classInfoElement
							.getElementsByTagName("PublicPrice").item(0);
					Element refundPercent = (Element) classInfoElement
							.getElementsByTagName("RefundPercent").item(0);
					Element settlementPrice = (Element) classInfoElement
							.getElementsByTagName("SettlementPrice").item(0);
					Element isPolicyAvailable = (Element) classInfoElement
							.getElementsByTagName("IsPolicyAvailable").item(0);

					classInfo = new ClassInfo();
					classInfo.setAvailableSeat(availableSeat.getFirstChild()
							.getNodeValue());
					classInfo.setClassCode(classCode.getFirstChild()
							.getNodeValue());
					classInfo.setClassName(className.getFirstChild()
							.getNodeValue());
					classInfo.setIsPolicyAvailable(isPolicyAvailable
							.getFirstChild().getNodeValue());
					classInfo.setPublicPrice(publicPrice.getFirstChild()
							.getNodeValue());
					classInfo.setRefundPercent(refundPercent.getFirstChild()
							.getNodeValue());
					classInfo.setSettlementPrice(settlementPrice
							.getFirstChild().getNodeValue());
					classInfoList.add(classInfo);
				}
				flightInfo.setClassInfo(classInfoList);
				// ��Ӻ�����Ϣ��������Ϣ�б���
				flightInfoList.add(flightInfo);
			}
		} catch (Exception e) {
			DisplayToast(e.getMessage());
			e.printStackTrace();	
		}
		return flightInfoList;
	}

	/**
	 * ��ʼ������Դ
	 */
	private ArrayList<HashMap<String, Object>> initCityData(
			List<FlightInfo> flightInfoList) {
		// ��ʼ��
		listItem = new ArrayList<HashMap<String, Object>>();
		// ������Ϣ
		if (flightInfoList != null) {
			flag = 1;
			for (int i = 0; i < flightInfoList.size(); i++) {
				// ��㺽����Ϣ
				flightInfo = (FlightInfo) flightInfoList.get(i);
				classInfoList = flightInfo.getClassInfo();
				// �����ڶ��㣨��λ������Ϣ
				for (int j = 0; j < classInfoList.size(); j++) {
					classInfo = (ClassInfo) classInfoList.get(j);
					HashMap<String, Object> map = new HashMap<String, Object>();
					// �����
					map.put("FlightNumber", flightInfo.getFlightNumber());
					// ��λ
					map.put("ClassName", classInfo.getClassName());
					// �۸�
					map.put("PublicPrice", classInfo.getPublicPrice());
					// ���չ�˾���ź�����
					map.put("CompanyCode", flightInfo.getCompanyCode());
					map.put("CompanyName", flightInfo.getCompanyName());
					// ʼ����
					map.put("FromAirportName", flightInfo.getFromAirportName());
					map.put("LandingAirportName",
							flightInfo.getLandingAirportName());

					// ��ɺ͵���ʱ��
					map.put("TakeOffTemp", getDateTime(flightInfo.getTakeOff()
							.replaceAll("/", "-")));
					map.put("LandingTemp", getDateTime(flightInfo.getLanding()
							.replaceAll("/", "-")));

					map.put("TakeOff",
							flightInfo.getTakeOff().replaceAll("/", "-"));
					map.put("Landing",
							flightInfo.getLanding().replaceAll("/", "-"));

					// �õ�����
					map.put("dayofweek", getWeekDay(flightInfo.getTakeOff()
							.replaceAll("/", "-")));

					// Ʊ��
					map.put("AvailableSeat", classInfo.getAvailableSeat());

					// ���չ�˾����ͼƬ
					String companyCodeIamgeName = flightInfo.getCompanyCode()
							.trim() + ".png";
					Drawable drawable = null;
					try {
						BufferedInputStream bis = new BufferedInputStream(
								getAssets().open(companyCodeIamgeName));
						Bitmap bm = BitmapFactory.decodeStream(bis);
						drawable = new BitmapDrawable(bm);
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("==========file not found======");
					}
					map.put("CompanyCodeImage", drawable);

					// �Ƿ�������ӿ�Ʊ
					String isElicetroicTicket = "";
					if (flightInfo.getIsElectronicTicket().equals("True")) {
						isElicetroicTicket = "֧��";
					} else {
						isElicetroicTicket = "��֧��";
					}
					map.put("IsElectronicTicket", isElicetroicTicket);

					// ��ͣ
					map.put("IsPassBy", flightInfo.getIsPassBy());

					// ����ȼ�ͷ�
					map.put("AirportConstructionFee",
							flightInfo.getAirportConstructionFee());
					map.put("FuelSurcharge", flightInfo.getFuelSurcharge());
					// ����
					map.put("PlaneModel", flightInfo.getPlaneModel());
					// ����������
					map.put("FromAirportCode", flightInfo.getFromAirportCode());
					map.put("LandingAirportCode",
							flightInfo.getLandingAirportCode());
					// ����
					map.put("Distance", flightInfo.getDistance());
					// ��λ����
					map.put("ClassCode", classInfo.getClassCode());
					// ����
					map.put("RefundPercent", classInfo.getRefundPercent());
					map.put("IsPolicyAvailable",
							classInfo.getIsPolicyAvailable());
					listItem.add(map);
				}
			}
			flightCount = flightInfoList.size() * classInfoList.size();
		} else {
			flag = 0;
			HashMap<String, Object> map1 = new HashMap<String, Object>();
			map1.put("FlightNumber", "���಻���ڣ�δ�ܲ�ѯ����Ʊ��Ϣ");
			listItem.add(map1);
		}
		return listItem;
	}

	/**
	 * �õ�ʱ�䣨Сʱ�ͷ��ӣ�
	 */
	private String getDateTime(String strDate) {
		String returndate = "";
		if (strDate != null) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd hh:mm:ss");
			Date date = new Date();
			try {
				date = simpleDateFormat.parse(strDate);
				returndate = String.valueOf(date.getHours() + ":"
						+ date.getMinutes());
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			}
		}
		return returndate;
	}

	/**
	 * �õ����ڣ�Сʱ�ͷ��ӣ�
	 */
	private String getWeekDay(String strDate) {
		String returndate = "����";
		int weekDay = 0;
		if (strDate != null) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd hh:mm:ss");
			Date date = new Date();
			try {
				date = simpleDateFormat.parse(strDate);
				weekDay = date.getDay();
				switch (weekDay) {
				case 0:
					returndate += "��";
					break;
				case 1:
					returndate += "һ";
					break;
				case 2:
					returndate += "��";
					break;
				case 3:
					returndate += "��";
					break;
				case 4:
					returndate += "��";
					break;
				case 5:
					returndate += "��";
					break;
				case 6:
					returndate += "��";
					break;
				default:
					break;
				}
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			}
		}
		return returndate;
	}
  /**
   * ���۸�����
   */
	Button.OnTouchListener orderByPrice = new Button.OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			int iAction = event.getAction();
			// �ı䱳��ɫ
			if (iAction == MotionEvent.ACTION_DOWN) {
				flightlistmain_orderPrice
						.setBackgroundResource(R.drawable.buttonshape_down);

			} else if (iAction == MotionEvent.ACTION_UP) {
				flightlistmain_orderPrice
						.setBackgroundResource(R.drawable.buttonshape);
			}

			if (iAction == MotionEvent.ACTION_CANCEL
					|| iAction == MotionEvent.ACTION_DOWN
					|| iAction == MotionEvent.ACTION_MOVE) {
				return false;
			}
	Comparator<FlightInfo> comparatorByTime = new Comparator<FlightInfo>() {
				@Override
				public int compare(FlightInfo object1, FlightInfo object2) {
					// TODO Auto-generated method stub
					return object1.getTakeOff().compareTo(object2.getTakeOff());
				}
			};
			List<FlightInfo> flightInfoList1 = new ArrayList<FlightInfo>();
			flightInfoList1 = flightInfoList;
			Collections.sort(flightInfoList1, comparatorByTime);
			// ��������ԴlistItem��map������
			listItem = initCityData(flightInfoList1);
			// �󶨵�ҳ��ؼ���
			bingList(listItem);
			// ��ԭ����
			return true;
		}
	};
	  /**
	   * ��ʱ������
	   */
	Button.OnTouchListener oderByTime = new Button.OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			int iAction = event.getAction();
			if (iAction == MotionEvent.ACTION_DOWN) {
				flightlistmain_orderTime
						.setBackgroundResource(R.drawable.buttonshape_down);

			} else if (iAction == MotionEvent.ACTION_UP) {
				flightlistmain_orderTime
						.setBackgroundResource(R.drawable.buttonshape);
			}
			if (iAction == MotionEvent.ACTION_CANCEL
					|| iAction == MotionEvent.ACTION_DOWN
					|| iAction == MotionEvent.ACTION_MOVE) {
				return false;
			}
			// ��ʱ������
	 Comparator<FlightInfo> comparatorByTime = new Comparator<FlightInfo>() {
				@Override
				public int compare(FlightInfo object1, FlightInfo object2) {
					// TODO Auto-generated method stub
					return object2.getTakeOff().compareTo(object1.getTakeOff());
				}
			};
			List<FlightInfo> flightInfoList1 = new ArrayList<FlightInfo>();
			flightInfoList1 = flightInfoList;
			Collections.sort(flightInfoList1, comparatorByTime);
			// ��������ԴlistItem��map������
			listItem = initCityData(flightInfoList1);
			// �󶨵�ҳ��ؼ���
			bingList(listItem);
			return true;
		}
	};

	/**
	 * ����ѡ��
	 */
ListView.OnItemClickListener selectFlightInfo 
                                               = new ListView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			flightInfo = new FlightInfo();
			// ���ѡ�����HashMap����
			HashMap<String, String> map = (HashMap<String, String>) flightListView
					.getItemAtPosition(position);
			// ����ʱ��
			flightInfo.setTakeOff(map.get("TakeOff"));
			flightInfo.setLanding(map.get("Landing"));
			// ����
			flightInfo.setDateTime(selectedTime);
			// ����
			flightInfo.setWeekDay(map.get("dayofweek"));
			// �ɻ��ͺ�
			flightInfo.setPlaneModel(map.get("PlaneModel"));
			// �����ص�͵���ص�
			flightInfo.setFromAirportName(map.get("FromAirportName"));
			flightInfo.setLandingAirportName(map.get("LandingAirportName"));
			// �Ƿ�Ϊ���ӿ�Ʊ
			flightInfo.setIsElectronicTicket(map.get("IsElectronicTicket"));
			// �Ƿ�ͣ
			flightInfo.setIsPassBy(map.get("IsPassBy"));
           //��ʼ����λ��Ϣ��
			classInfo = new ClassInfo();
			// �۸�
			classInfo.setPublicPrice(map.get("PublicPrice"));
			flightInfo.setAirportConstructionFee(map
					.get("AirportConstructionFee"));
			flightInfo.setFuelSurcharge(map.get("FuelSurcharge"));
			flightInfo.setFlightNumber(map.get("FlightNumber"));
			flightInfo.setCompanyCode(map.get("CompanyCode"));
			flightInfo.setFromAirportCode(map.get("FromAirportCode"));
			flightInfo.setLandingAirportCode(map.get("LandingAirportCode"));
			flightInfo.setDistance(map.get("Distance"));
			classInfo.setClassCode(map.get("ClassCode"));
			classInfo.setRefundPercent(map.get("RefundPercent"));
			classInfo.setIsPolicyAvailable(map.get("IsPolicyAvailable"));

			if (flightInfo != null && classInfo != null) {
				// �����û�
				SaveflightInfo(flightInfo, classInfo);
				// ��ת����һ��ҳ��
				flightListDetail(flightInfo, classInfo);
			}

		}
	};
  /**
   * ��ListView��ѡ��ĺ�����Ϣ���뵽��һ��ҳ�棨FlightListDetail��
   * @param flightInfo
   * @param classInfo
   */
	private void flightListDetail(FlightInfo flightInfo, ClassInfo classInfo) {
		/* �½�һ��Intent���� */
		Intent intent = new Intent();
		// ���ô��ݵĲ���
		intent.putExtra("dateTime", flightInfo.getDateTime());
		intent.putExtra("dayofweek", flightInfo.getWeekDay());
		intent.putExtra("TakeOff", flightInfo.getTakeOff());
		intent.putExtra("Landing", flightInfo.getLanding());
		intent.putExtra("PlaneModel", flightInfo.getPlaneModel());
		intent.putExtra("FromAirportName", flightInfo.getFromAirportName());
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
		intent.setClass(FlightInfoMainApp.this, FlightListDetail.class);
		// ����intent��Activity
		FlightInfoMainApp.this.startActivity(intent);
	}

	/**
	 * �����Ʊ��Ϣ
	 */
	private void SaveflightInfo(FlightInfo flightInfo, ClassInfo classInfo) {
		// ����
		SharedPreferences uiState = getSharedPreferences(LoginApp.PREFS_NAME,
				Activity.MODE_WORLD_READABLE);
		// ȡ�ñ༭����
		SharedPreferences.Editor editor = uiState.edit();

		editor.putString("FlightNumber", flightInfo.getFlightNumber());
		editor.putString("ClassName", classInfo.getClassName());
		editor.putString("PublicPrice", classInfo.getPublicPrice());
		editor.putString("CompanyCode", flightInfo.getCompanyCode());
		editor.putString("CompanyName", flightInfo.getCompanyName());
		editor.putString("FromAirportName", flightInfo.getFromAirportName());
		editor.putString("LandingAirportName",
				flightInfo.getLandingAirportName());
		editor.putString("TakeOff", flightInfo.getTakeOff());
		editor.putString("Landing", flightInfo.getLanding());
		editor.putString("IsElectronicTicket",
				flightInfo.getIsElectronicTicket());
		editor.putString("IsPassBy", flightInfo.getIsPassBy());
		editor.putString("AirportConstructionFee",
				flightInfo.getAirportConstructionFee());
		editor.putString("FuelSurcharge", flightInfo.getFuelSurcharge());
		editor.putString("PlaneModel", flightInfo.getPlaneModel());
		editor.putString("FromAirportCode", flightInfo.getFromAirportCode());
		editor.putString("LandingAirportCode",
				flightInfo.getLandingAirportCode());
		editor.putString("Distance", flightInfo.getDistance());
		editor.putString("ClassCode", classInfo.getClassCode());
		editor.putString("RefundPercent", classInfo.getRefundPercent());
		editor.putString("IsPolicyAvailable", classInfo.getIsPolicyAvailable());

		// �ύ����
		editor.commit();
	}

	/* ��ʾToast */
	public void DisplayToast(String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}

}
