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
 * ����ѡ����
 * @author 
 *
 */
public class CityListView extends Activity {
	// ���ɶ�̬���飬��������(������ó���)
	ArrayList<HashMap<String, Object>> listItem=null;
	ListAdapter listItemAdapter = null;
	private String urlCity = "";
	private List<City> cityList = null;
	private City city;
	private static String[] autoStringCity = null;
	AutoCompleteTextView m_AutoCompleteTextView;
	ListView citylist;
	// ѡ���ֵ
	private String selectedStartCity = null;
	private String selectedOffCity = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// �õ�TicketMainApp���ݹ���������
		setTicketValue();

		// ��ȡStrings�е�ֵ
		urlCity = this.getString(R.string.CitySelectURL);

		if (urlCity.length() > 0) {
			// ���Ӻ�̨�õ�����
			if(getCity(urlCity)){
				//���ػ���
				setContentView(R.layout.citylistmain);
				// ��������Դ
				initCityData();
				// ��Layout�����ListView
				citylist = (ListView) findViewById(R.id.ListView01);			
				// ������������Item�Ͷ�̬�����Ӧ��Ԫ��
				listItemAdapter = new SimpleAdapter(this, listItem, // ����Դ
						R.layout.citylist_items,// layout�е�citylist_items�Ӳ�
						new String[] { "city" }, // ��̬���ݵ�����
						new int[] { R.id.ItemText } // ��list_items�еĿؼ�
				);
				// ��ӵ�listView��
				citylist.setAdapter(listItemAdapter);

				// ƥ����� �����ؼ���
				autoCityByPinyin();	
				citylist.setOnItemClickListener(cityListSelectCity);
			}else{
				returnTicketMain();							
			}
		}		
	}
	/**
	 * ����������
	 */
	private void returnTicketMain(){
		// /* �½�һ��Intent���� */
		Intent intent = new Intent();
		// ���ô��ݵĲ���
		intent.putExtra("erroMessage", "��������ʧ��");	
		/* ָ��intentҪ�������� */
		intent.setClass(CityListView.this, TicketMainApp.class);
		/* ����һ���µ�Activity */
		CityListView.this.startActivity(intent);	
	}

	/**
	 * ����ҳ�洫������ֵ����ֵ���ؼ�
	 */
	private void setTicketValue() {
		Intent intent = getIntent();
		selectedStartCity = intent.getStringExtra("startCitypara");
		selectedOffCity = intent.getStringExtra("offCitypara");
	}

	/**
	 * ����ѡ��
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
	 * �õ������б�
	 * 
	 * @param urlCity
	 */
	private boolean getCity(String urlCity) {
		String resultData = "";
		boolean isNet = false;
		// �õ��ַ�������
		resultData = ConnUrlHelper.getHttpURLConnByUrl(urlCity);
		if (resultData != null && resultData.length() > 0) {
			// ת��Ϊxml�ĵ�
			Document doc = DoXmlHelper.StringToxml(resultData);
			// ��ȡ�ĵ��е�Ԫ��
			Element root = doc.getDocumentElement();
			// �õ���Ҫ�Ľڵ㼯��
			NodeList citysNodeList = root.getElementsByTagName("City");
			// ��ʼ������List����
			cityList = new ArrayList<City>();
			// ��ʼ�������Զ�ƥ������
			autoStringCity = new String[citysNodeList.getLength()];
			// ����Account�е�����Ԫ��
			for (int i = 0; i < citysNodeList.getLength(); i++) {
				// �õ�Account�е�����Ԫ��(������n��account)
				Element cityElement = (Element) citysNodeList.item(i);
				// ��������account�е�Ԫ�صĵ�һ���ڵ�
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

				// ��ӳ��е������б���
				cityList.add(city);
				// �������ƥ���ַ������ݼ�
				autoStringCity[i] = city.getPinYin() + "," + city.getName();
				isNet = true;
			}
		} else {			
			isNet = false;
		}
		return isNet;
	}

	/**
	 * ��ƴ��ƥ�����
	 */
	private void autoCityByPinyin() {

		if (autoStringCity != null) {
			ArrayAdapter<String> adapterCity = new ArrayAdapter<String>(this,
					android.R.layout.simple_dropdown_item_1line, autoStringCity);

			m_AutoCompleteTextView 
			= (AutoCompleteTextView) findViewById(R.id.AutoCompleteTextView_City);
			// ���ôӵڼ�����ĸ��ʼƥ��
			m_AutoCompleteTextView.setThreshold(1);

			// ��adapter��ӵ�AutoCompleteTextView��
			m_AutoCompleteTextView.setAdapter(adapterCity);

			m_AutoCompleteTextView.setOnTouchListener(setCityReturnTicketMain);
		}
	}

	/**
	 * ѡ����к󷵻ز�Ʊҳ��
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
	 * ���ص���Ʊҳ��
	 */
	private void returnTicketMain(String startCity) {
		/* �½�һ��Intent���� */
		Intent intent = new Intent();
		if (selectedStartCity != null) {
			// ���ô��ݵĲ���
			intent.putExtra("startCity", startCity);
		}
		if (selectedOffCity != null) {
			intent.putExtra("offCity", startCity);
		}
		/* ָ��intentҪ�������� */
		intent.setClass(CityListView.this, TicketMainApp.class);
		// ����intent��Activity
		CityListView.this.startActivity(intent);
	}

	/**
	 * ��ʼ������Դ�������ų�����һ����У�
	 */
	private void initCityData() {
		listItem = new ArrayList<HashMap<String, Object>>();
		
		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map1.put("city", "����");
		map1.put("code", "PEK");
		listItem.add(map1);

		HashMap<String, Object> map2 = new HashMap<String, Object>();
		map2.put("city", "����");
		map2.put("code", "KMG");
		listItem.add(map2);

		HashMap<String, Object> map3 = new HashMap<String, Object>();
		map3.put("city", "����");
		map3.put("code", "CAN");
		listItem.add(map3);

		HashMap<String, Object> map4 = new HashMap<String, Object>();
		map4.put("city", "����");
		map4.put("code", "HHA");

		HashMap<String, Object> map5 = new HashMap<String, Object>();
		map5.put("city", "����");
		map5.put("code", "SZX");
		listItem.add(map5);

		HashMap<String, Object> map6 = new HashMap<String, Object>();
		map6.put("city", "�ɶ�");
		map6.put("code", "CD");
		listItem.add(map6);

		// �õ����г�����Ϣ
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
