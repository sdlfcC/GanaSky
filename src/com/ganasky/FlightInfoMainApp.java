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
 * 航班信息列表
 * @author 
 *
 */
public class FlightInfoMainApp extends Activity {
   //定义常量
	private final String DEBUG_TAG = "FlightInfoMainApp";
	// 选择的值(要设置成静态变量才能读到)
	private String selectedStartCity = null;
	private String selectedOffCity = null;
	private String selectedTime = null;
	// 选择的仓位类型
	private String selectedPlaneClass = null;
	private String selectedPlaneCompany = null;
	// 查询地址
	private String ticketSearchUrl;

	// 航班信息列表
	private List<FlightInfo> flightInfoList = null;
	// 航班信息
	private FlightInfo flightInfo = null;
	// 航班仓位列表
	private List<ClassInfo> classInfoList = null;
	// 航班仓位
	private ClassInfo classInfo = null;
	// listView控件
	private ListView flightListView;
	// 始发地控件
	private TextView txtSetCity;
	// 目的地控件
	private TextView txtOffCity;
	// 始发时间
	private TextView txtSetTime;
	// 箭头
	private TextView txtArrow;
	// 时间排序按钮
	private Button flightlistmain_orderTime;
	// 价格排序按钮
	private Button flightlistmain_orderPrice;
	// 判断是否有数据，定义一个标志
	private int flag = 0;

	// 生成动态数组，加入数据(在这里该城市)
	ArrayList<HashMap<String, Object>> listItem = null;
	//初始化ListAdapter适配器
	ListAdapter listItemAdapter = null;
    //返回的航班信息条数显示在文本框中
	private TextView txtflightCount;
	  //返回的航班信息条数
	private int flightCount = 0;
	//返回的所有信息字符串
	private String LongFlightInfo = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.flightlistmain);

		// 初始化控件
		initControl();

		// 接收页面传过来的参数
		setTicketValue();

		// 读取String中的信息
		ticketSearchUrl = this.getString(R.string.TicketSearchUrl);
		// 查询(默认更加价格升序)
		flightInfoList = Search(ticketSearchUrl);

		// 加载数据源listItem是map型数据
		listItem = initCityData(flightInfoList);
		// 绑定到页面控件上
		bingList(listItem);
		if (flightCount != 0) {
			txtflightCount.setText("共查到" + flightCount + "记录");
		}
		//设置事件监听
		flightlistmain_orderPrice.setOnTouchListener(orderByPrice);
		flightlistmain_orderTime.setOnTouchListener(oderByTime);
		flightListView.setOnItemClickListener(selectFlightInfo);
	}

	  /**
	   * 初始化控件
	   */
	private void initControl() {
		// 绑定Layout里面的ListView
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
	 * 设置页面传过来的值并赋值给控件
	 */
	private void setTicketValue() {
		Intent intent = getIntent();
		selectedStartCity = intent.getStringExtra("selectedStartCity");
		selectedOffCity = intent.getStringExtra("selectedOffCity");
		selectedTime = intent.getStringExtra("selectedTime");
		selectedPlaneClass = intent.getStringExtra("selectedPlaneClass");
		selectedPlaneCompany = intent.getStringExtra("selectedPlaneCompany");

		// 将传过来的内容赋值
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
   * 将ArrayList数据源绑定到ListView子项里
   * @param listItem
   */
	private void bingList(ArrayList<HashMap<String, Object>> listItem) {
		// 清空
		listItemAdapter = null;
		if (flag == 1) {
			// 生成适配器的Item和动态数组对应的元素
			// CompanyCode虽然是图片显示，但是这里也要添加
			listItemAdapter = new ImageAdapterOfFlightList(this, listItem, // 数据源
					R.layout.flightlist_items, // layout中的citylist_items子层
					new String[] { "CompanyCodeImage", "FlightNumber",
							"ClassName", "PublicPrice", "CompanyName",
							"FromAirportName", "TakeOffTemp", "AvailableSeat",
							"LandingAirportName", "LandingTemp" }, // 动态数据的子项
					new int[] { R.id.flightList_CompanyCode,
							R.id.flightList_FlightNumber,
							R.id.flightList_ClassName,
							R.id.flightList_PublicPrice,
							R.id.flightList_CompanyName,
							R.id.flightList_FromAirportName,
							R.id.flightList_TakeOff,
							R.id.flightList_AvailableSeat2,
							R.id.flightList_LandingAirportName,
							R.id.flightList_Landing } // 绑定list_items中的控件
			);

			flag = 0;
			// 若没有数据
		} else if (flag == 0) {
			listItemAdapter = new SimpleAdapter(this, listItem, // 数据源
					R.layout.flightlist_items,// layout中的citylist_items子层
					new String[] { "FlightNumber" }, // 动态数据的子项
					new int[] { R.id.flightList_FlightNumber } // 绑定list_items中的控件
			);
		}
		// 添加到listView中
		flightListView.setAdapter(listItemAdapter);
	}
  /**
   * 根据参数连接后台查询航班信息
   * @param ticketSearchUrl
   * @return
   */
	private List<FlightInfo> Search(String ticketSearchUrl) {
		// 得到字符串集合
		String resultData = null;
		try {

			String params = null;
			try {
				// 传递的参数设定编码格式，以防止乱码
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
				Log.e(DEBUG_TAG, "机票查询方法中参数传递错误！");
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
					// 解析票务信息(这里出现异常)
					flightInfoList = GetTicketInfo(resultData);
					// 保存数据
					LongFlightInfo = resultData;
				}
			}
			// 最外层的try
		} catch (Exception e) {
			DisplayToast("Flight异常！");
		}
		return flightInfoList;
	}

	/**
	 * 解析机票信息
	 * 
	 * @param resultData
	 *            return 返回查询到的航班信息list
	 */
	private List<FlightInfo> GetTicketInfo(String resultData) {
		try {
			// 转换为xml文档
			Document docFlightInfo = DoXmlHelper.StringToxml(resultData);
			// 读取文档中的元素
			Element root = docFlightInfo.getDocumentElement();
			// 得到想要的节点集合
			NodeList flightInfoNodeList = root
					.getElementsByTagName("FlightInfo");

			// 初始化航班信息List对象
			flightInfoList = new ArrayList<FlightInfo>();
			for (int i = 0; i < flightInfoNodeList.getLength(); i++) {
				// 得到FlightInfo中的所有元素
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

				// 将各个元素存在FlightInfo
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
				// 得到想要的节点集合
				NodeList classInfoNodeList = ClassInfoInfo
						.getElementsByTagName("ClassInfo");
				// 初始化航班信息List对象
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
				// 添加航班信息到航班信息列表中
				flightInfoList.add(flightInfo);
			}
		} catch (Exception e) {
			DisplayToast(e.getMessage());
			e.printStackTrace();	
		}
		return flightInfoList;
	}

	/**
	 * 初始化数据源
	 */
	private ArrayList<HashMap<String, Object>> initCityData(
			List<FlightInfo> flightInfoList) {
		// 初始化
		listItem = new ArrayList<HashMap<String, Object>>();
		// 解析信息
		if (flightInfoList != null) {
			flag = 1;
			for (int i = 0; i < flightInfoList.size(); i++) {
				// 外层航班信息
				flightInfo = (FlightInfo) flightInfoList.get(i);
				classInfoList = flightInfo.getClassInfo();
				// 解析第二层（仓位）的信息
				for (int j = 0; j < classInfoList.size(); j++) {
					classInfo = (ClassInfo) classInfoList.get(j);
					HashMap<String, Object> map = new HashMap<String, Object>();
					// 航班号
					map.put("FlightNumber", flightInfo.getFlightNumber());
					// 仓位
					map.put("ClassName", classInfo.getClassName());
					// 价格
					map.put("PublicPrice", classInfo.getPublicPrice());
					// 航空公司代号和名称
					map.put("CompanyCode", flightInfo.getCompanyCode());
					map.put("CompanyName", flightInfo.getCompanyName());
					// 始发地
					map.put("FromAirportName", flightInfo.getFromAirportName());
					map.put("LandingAirportName",
							flightInfo.getLandingAirportName());

					// 起飞和到达时间
					map.put("TakeOffTemp", getDateTime(flightInfo.getTakeOff()
							.replaceAll("/", "-")));
					map.put("LandingTemp", getDateTime(flightInfo.getLanding()
							.replaceAll("/", "-")));

					map.put("TakeOff",
							flightInfo.getTakeOff().replaceAll("/", "-"));
					map.put("Landing",
							flightInfo.getLanding().replaceAll("/", "-"));

					// 得到星期
					map.put("dayofweek", getWeekDay(flightInfo.getTakeOff()
							.replaceAll("/", "-")));

					// 票数
					map.put("AvailableSeat", classInfo.getAvailableSeat());

					// 航空公司代号图片
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

					// 是否允许电子客票
					String isElicetroicTicket = "";
					if (flightInfo.getIsElectronicTicket().equals("True")) {
						isElicetroicTicket = "支持";
					} else {
						isElicetroicTicket = "不支持";
					}
					map.put("IsElectronicTicket", isElicetroicTicket);

					// 经停
					map.put("IsPassBy", flightInfo.getIsPassBy());

					// 基建燃油费
					map.put("AirportConstructionFee",
							flightInfo.getAirportConstructionFee());
					map.put("FuelSurcharge", flightInfo.getFuelSurcharge());
					// 机型
					map.put("PlaneModel", flightInfo.getPlaneModel());
					// 城市三字码
					map.put("FromAirportCode", flightInfo.getFromAirportCode());
					map.put("LandingAirportCode",
							flightInfo.getLandingAirportCode());
					// 距离
					map.put("Distance", flightInfo.getDistance());
					// 仓位类型
					map.put("ClassCode", classInfo.getClassCode());
					// 返点
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
			map1.put("FlightNumber", "航班不存在，未能查询到机票信息");
			listItem.add(map1);
		}
		return listItem;
	}

	/**
	 * 得到时间（小时和分钟）
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
	 * 得到星期（小时和分钟）
	 */
	private String getWeekDay(String strDate) {
		String returndate = "星期";
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
					returndate += "日";
					break;
				case 1:
					returndate += "一";
					break;
				case 2:
					returndate += "二";
					break;
				case 3:
					returndate += "三";
					break;
				case 4:
					returndate += "四";
					break;
				case 5:
					returndate += "五";
					break;
				case 6:
					returndate += "六";
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
   * 按价格排序
   */
	Button.OnTouchListener orderByPrice = new Button.OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			int iAction = event.getAction();
			// 改变背景色
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
			// 加载数据源listItem是map型数据
			listItem = initCityData(flightInfoList1);
			// 绑定到页面控件上
			bingList(listItem);
			// 还原背景
			return true;
		}
	};
	  /**
	   * 按时间排序
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
			// 按时间升序
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
			// 加载数据源listItem是map型数据
			listItem = initCityData(flightInfoList1);
			// 绑定到页面控件上
			bingList(listItem);
			return true;
		}
	};

	/**
	 * 子项选择
	 */
ListView.OnItemClickListener selectFlightInfo 
                                               = new ListView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			flightInfo = new FlightInfo();
			// 获得选中项的HashMap对象
			HashMap<String, String> map = (HashMap<String, String>) flightListView
					.getItemAtPosition(position);
			// 出发时间
			flightInfo.setTakeOff(map.get("TakeOff"));
			flightInfo.setLanding(map.get("Landing"));
			// 日期
			flightInfo.setDateTime(selectedTime);
			// 星期
			flightInfo.setWeekDay(map.get("dayofweek"));
			// 飞机型号
			flightInfo.setPlaneModel(map.get("PlaneModel"));
			// 出发地点和到达地点
			flightInfo.setFromAirportName(map.get("FromAirportName"));
			flightInfo.setLandingAirportName(map.get("LandingAirportName"));
			// 是否为电子客票
			flightInfo.setIsElectronicTicket(map.get("IsElectronicTicket"));
			// 是否经停
			flightInfo.setIsPassBy(map.get("IsPassBy"));
           //初始化舱位信息类
			classInfo = new ClassInfo();
			// 价格
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
				// 保存用户
				SaveflightInfo(flightInfo, classInfo);
				// 跳转到下一个页面
				flightListDetail(flightInfo, classInfo);
			}

		}
	};
  /**
   * 将ListView中选择的航班信息传入到下一个页面（FlightListDetail）
   * @param flightInfo
   * @param classInfo
   */
	private void flightListDetail(FlightInfo flightInfo, ClassInfo classInfo) {
		/* 新建一个Intent对象 */
		Intent intent = new Intent();
		// 设置传递的参数
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

		/* 指定intent要启动的类 */
		intent.setClass(FlightInfoMainApp.this, FlightListDetail.class);
		// 启动intent的Activity
		FlightInfoMainApp.this.startActivity(intent);
	}

	/**
	 * 保存机票信息
	 */
	private void SaveflightInfo(FlightInfo flightInfo, ClassInfo classInfo) {
		// 保存
		SharedPreferences uiState = getSharedPreferences(LoginApp.PREFS_NAME,
				Activity.MODE_WORLD_READABLE);
		// 取得编辑对象
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

		// 提交保存
		editor.commit();
	}

	/* 显示Toast */
	public void DisplayToast(String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}

}
