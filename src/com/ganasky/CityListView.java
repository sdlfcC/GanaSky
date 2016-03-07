package com.ganasky;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import com.ganasky.Help.ConnUrlHelper;
import com.ganasky.Help.DoXmlHelper;
import com.ganasky.model.City;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/**
 * 城市选择功能
 * @author 
 *
 */
public class CityListView extends Activity {
	// 生成动态数组，加入数据(在这里该城市)
	ArrayList<HashMap<String, Object>> listItem=null;
	ListAdapter listItemAdapter = null;
	private String urlCity = "";
	private List<City> cityList = null;
	private City city;
	private static String[] autoStringCity = null;
	AutoCompleteTextView m_AutoCompleteTextView;
	ListView citylist;
	// 选择的值
	private String selectedStartCity = null;
	private String selectedOffCity = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// 得到TicketMainApp传递过来的数据
		setTicketValue();

		// 读取Strings中的值
		urlCity = this.getString(R.string.CitySelectURL);

		if (urlCity.length() > 0) {
			// 连接后台得到数据
			if(getCity(urlCity)){
				//加载画面
				setContentView(R.layout.citylistmain);
				// 加载数据源
				initCityData();
				// 绑定Layout里面的ListView
				citylist = (ListView) findViewById(R.id.ListView01);			
				// 生成适配器的Item和动态数组对应的元素
				listItemAdapter = new SimpleAdapter(this, listItem, // 数据源
						R.layout.citylist_items,// layout中的citylist_items子层
						new String[] { "city" }, // 动态数据的子项
						new int[] { R.id.ItemText } // 绑定list_items中的控件
				);
				// 添加到listView中
				citylist.setAdapter(listItemAdapter);

				// 匹配城市 关联关键字
				autoCityByPinyin();	
				citylist.setOnItemClickListener(cityListSelectCity);
			}else{
				returnTicketMain();							
			}
		}		
	}
	/**
	 * 返回主界面
	 */
	private void returnTicketMain(){
		// /* 新建一个Intent对象 */
		Intent intent = new Intent();
		// 设置传递的参数
		intent.putExtra("erroMessage", "网络连接失败");	
		/* 指定intent要启动的类 */
		intent.setClass(CityListView.this, TicketMainApp.class);
		/* 启动一个新的Activity */
		CityListView.this.startActivity(intent);	
	}

	/**
	 * 设置页面传过来的值并赋值给控件
	 */
	private void setTicketValue() {
		Intent intent = getIntent();
		selectedStartCity = intent.getStringExtra("startCitypara");
		selectedOffCity = intent.getStringExtra("offCitypara");
	}

	/**
	 * 子项选择
	 */
	ListView.OnItemClickListener cityListSelectCity 
	                                        = new ListView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			String startCity = parent.getItemAtPosition(position).toString();
			String newStartCity = startCity.substring(startCity
					.lastIndexOf("=") + 1);
			newStartCity = newStartCity.substring(0, newStartCity.length() - 1);
			if (newStartCity != null) {
				returnTicketMain(newStartCity);
			}
		}
	};

	/**
	 * 得到城市列表
	 * 
	 * @param urlCity
	 */
	private boolean getCity(String urlCity) {
		String resultData = "";
		boolean isNet = false;
		// 得到字符串集合
		resultData = ConnUrlHelper.getHttpURLConnByUrl(urlCity);
		if (resultData != null && resultData.length() > 0) {
			// 转换为xml文档
			Document doc = DoXmlHelper.StringToxml(resultData);
			// 读取文档中的元素
			Element root = doc.getDocumentElement();
			// 得到想要的节点集合
			NodeList citysNodeList = root.getElementsByTagName("City");
			// 初始化城市List对象
			cityList = new ArrayList<City>();
			// 初始化城市自动匹配数组
			autoStringCity = new String[citysNodeList.getLength()];
			// 遍历Account中的所有元素
			for (int i = 0; i < citysNodeList.getLength(); i++) {
				// 得到Account中的所有元素(可以有n个account)
				Element cityElement = (Element) citysNodeList.item(i);
				// 读到具体account中的元素的第一个节点
				Element code = (Element) cityElement.getElementsByTagName(
						"Code").item(0);
				Element name = (Element) cityElement.getElementsByTagName(
						"Name").item(0);
				Element pinYin = (Element) cityElement.getElementsByTagName(
						"PinYin").item(0);
				Element cityShort = (Element) cityElement.getElementsByTagName(
						"Short").item(0);

				city = new City();
				city.setCode(code.getFirstChild().getNodeValue());
				city.setName(name.getFirstChild().getNodeValue());
				city.setPinYin(pinYin.getFirstChild().getNodeValue());
				city.setShort(cityShort.getFirstChild().getNodeValue());

				// 添加城市到城市列表中
				cityList.add(city);
				// 构造城市匹配字符串数据集
				autoStringCity[i] = city.getPinYin() + "," + city.getName();
				isNet = true;
			}
		} else {			
			isNet = false;
		}
		return isNet;
	}

	/**
	 * 用拼音匹配城市
	 */
	private void autoCityByPinyin() {

		if (autoStringCity != null) {
			ArrayAdapter<String> adapterCity = new ArrayAdapter<String>(this,
					android.R.layout.simple_dropdown_item_1line, autoStringCity);

			m_AutoCompleteTextView 
			= (AutoCompleteTextView) findViewById(R.id.AutoCompleteTextView_City);
			// 设置从第几个字母开始匹配
			m_AutoCompleteTextView.setThreshold(1);

			// 将adapter添加到AutoCompleteTextView中
			m_AutoCompleteTextView.setAdapter(adapterCity);

			m_AutoCompleteTextView.setOnTouchListener(setCityReturnTicketMain);
		}
	}

	/**
	 * 选择城市后返回查票页面
	 */
	AutoCompleteTextView.OnTouchListener setCityReturnTicketMain 
	                           = new AutoCompleteTextView.OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			int iAction = event.getAction();
			if (iAction == MotionEvent.ACTION_DOWN) {
				if (m_AutoCompleteTextView.getText() != null
						&& m_AutoCompleteTextView.getText().length() > 0) {
					returnTicketMain(m_AutoCompleteTextView.getText()
							.toString());
					return true;
				}
			}
			return false;
		}
	};

	/**
	 * 返回到查票页面
	 */
	private void returnTicketMain(String startCity) {
		/* 新建一个Intent对象 */
		Intent intent = new Intent();
		if (selectedStartCity != null) {
			// 设置传递的参数
			intent.putExtra("startCity", startCity);
		}
		if (selectedOffCity != null) {
			intent.putExtra("offCity", startCity);
		}
		/* 指定intent要启动的类 */
		intent.setClass(CityListView.this, TicketMainApp.class);
		// 启动intent的Activity
		CityListView.this.startActivity(intent);
	}

	/**
	 * 初始化数据源（分热门城市与一般城市）
	 */
	private void initCityData() {
		listItem = new ArrayList<HashMap<String, Object>>();
		
		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map1.put("city", "北京");
		map1.put("code", "PEK");
		listItem.add(map1);

		HashMap<String, Object> map2 = new HashMap<String, Object>();
		map2.put("city", "昆明");
		map2.put("code", "KMG");
		listItem.add(map2);

		HashMap<String, Object> map3 = new HashMap<String, Object>();
		map3.put("city", "广州");
		map3.put("code", "CAN");
		listItem.add(map3);

		HashMap<String, Object> map4 = new HashMap<String, Object>();
		map4.put("city", "杭州");
		map4.put("code", "HHA");

		HashMap<String, Object> map5 = new HashMap<String, Object>();
		map5.put("city", "深圳");
		map5.put("code", "SZX");
		listItem.add(map5);

		HashMap<String, Object> map6 = new HashMap<String, Object>();
		map6.put("city", "成都");
		map6.put("code", "CD");
		listItem.add(map6);

		// 得到所有城市信息
		for (int i = 0; i < cityList.size(); i++) {
			city = cityList.get(i);
			HashMap<String, Object> map7 = new HashMap<String, Object>();
			map7.put("city", city.getName());
			map7.put("code", city.getCode());
			map7.put("pinyin", city.getPinYin());
			listItem.add(map7);
		}
	}
}
