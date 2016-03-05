package com.ganasky.Help;

//package com.ganasky.Help;

import java.io.IOException;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.util.Log;

public abstract class DoXmlHelper {
	private static String DEBUG_TAG = "DoXmlHelper";

	/**
	 * 将String字符串转换为xml文档
	 * 
	 * @param resultData
	 * @return
	 */
	public static Document StringToxml(String resultData) {

		StringReader sr = new StringReader(resultData);
		InputSource is = new InputSource(sr);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document doc = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {

			Log.e(DEBUG_TAG,
					"the StringToxml method ParserConfigurationException erro ");
		}
		try {
			doc = builder.parse(is);

		} catch (SAXException e) {

			Log.e(DEBUG_TAG, "the StringToxml method SAXException erro ");

		} catch (IOException e) {
			Log.e(DEBUG_TAG, "the StringToxml method IOException erro ");
		}
		return doc;
	}

}
