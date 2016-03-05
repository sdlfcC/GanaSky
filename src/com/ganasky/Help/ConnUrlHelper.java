package com.ganasky.Help;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
/**
 * 这是与后台ASP通信有关的类
 * @author 
 *
 */
public abstract class ConnUrlHelper {
	private static String DEBUG_TAG = "ConnUrlHelper";
     //这一部分代码在第三章中实现过，这里不重复展开
	public static boolean hasInternet(Activity activity) {

		ConnectivityManager manager = (ConnectivityManager) activity

		.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo info = manager.getActiveNetworkInfo();

		if (info == null || !info.isConnected()) {

			return false;
		}
		if (info.isRoaming()) {
			// here is the roaming option you can change it if you want to

			// disable internet while roaming, just return false

			return true;
		}
		return true;
	}

	/**
	 * 用于将流转换为字符串
	 */
	public static String convertStreamToString1(InputStreamReader is) {
		BufferedReader reader = new BufferedReader(is);
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			Log.e(DEBUG_TAG, "the convertStreamToString1 methods IOException");
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				Log.e(DEBUG_TAG,
						"the convertStreamToString1 methods IOException");
			}
		}
		return sb.toString();
	}

	/**
	 * 通过HttpURLConnection get方式进行通信
	 * 
	 * @param url1
	 * @return
	 */
	public static String getHttpURLConnByUrl(String url1) {
		URL url = null;
		InputStreamReader in = null;
		String resultData = "";
		HttpURLConnection urlConn = null;
		try {
			// 构造一个URL对象
			url = new URL(url1);
		} catch (MalformedURLException e) {
			Log.e(DEBUG_TAG,
					"getHttpURLConnByUrl method MalformedURLException erro");
		}
		if (url != null) {
			try {
				// 使用HttpURLConnection打开连接
				urlConn = (HttpURLConnection) url.openConnection();
				// 设置连接超时时间
				urlConn.setConnectTimeout(6 * 1000);
				if (urlConn.getResponseCode() != 200)
					throw new RuntimeException("请求url连接失败！");
				// 得到读取的内容(流)
				in = new InputStreamReader(urlConn.getInputStream());
				resultData = convertStreamToString1(in);
				// 关闭InputStreamReader
				in.close();
				// 关闭http连接
				urlConn.disconnect();
			} catch (IOException e) {
				Log.e(DEBUG_TAG, "getHttpURLConnByUrl method IOException erro");
			} finally {
				urlConn.disconnect();
			}
		} else {
			Log.e(DEBUG_TAG, "getHttpURLConnByUrl method Url NULL erro");
		}
		return resultData;
	}

	/**
	 * 通过HttpURLConnection post方式进行通信
	 * 
	 * @param url1
	 * @return
	 */
	public static String getPostHttpURLConnByUrl(String httpUrl, String params) {
		// 获得的数据
		String resultData = "";
		URL url = null;
		try {
			// 构造一个URL对象
			url = new URL(httpUrl);
		} catch (MalformedURLException e) {
			Log.e(DEBUG_TAG,
					" MalformedURLException erro");
		}
		if (url != null) {
			try {
				// 使用HttpURLConnection打开连接
				HttpURLConnection urlConn = (HttpURLConnection) url
						.openConnection();
				//urlConn.setConnectTimeout(6 * 10000000);
				//urlConn.setReadTimeout(6 * 100000); 
				// 因为这个是post请求,设立需要设置为true
				urlConn.setDoOutput(true);
				// 设置以POST方式
				urlConn.setRequestMethod("POST");
				// Post 请求不能使用缓存
				urlConn.setUseCaches(false);
				// URLConnection.setInstanceFollowRedirects是成员函数，仅作用于当前函数
				urlConn.setInstanceFollowRedirects(true);
				// 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的
				/*urlencoded编码过的form参数
				* 下面我们可以看到我们对正文内容使用URLEncoder.encode进行编码*/
				urlConn.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded");
				// 连接，从postUrl.openConnection()至此的配置必须要在connect之前完成，
				// 要注意的是connection.getOutputStream会隐含的进行connect。
				urlConn.connect();
				// DataOutputStream流
				DataOutputStream out = new DataOutputStream(
						urlConn.getOutputStream());
				// 将要上传的内容写入流中
				out.writeBytes(params);
				// 刷新、关闭
				out.flush();
				out.close();
				// 获取数据
				InputStreamReader in = null;
				in = new InputStreamReader(urlConn.getInputStream());
				resultData = convertStreamToString1(in);
				// 关闭InputStreamReader
				in.close();
				// 关闭http连接
				urlConn.disconnect();
			} catch (IOException e) {
				// 查看输出异常
				Log.e(DEBUG_TAG,
						"the getPostHttpURLConnByUrl methods  IOException erro");
			}
		} else {
			Log.e(DEBUG_TAG,
					"the getPostHttpURLConnByUrl methods  Url NULL erro");
		}
		return resultData;
	}

	/**
	 * 通过HttpURLConnection post方式进行通信（设置相对较长的超时连接时间）
	 *
	 * @param url1
	 * @return
	 */
	public static String getLongPostHttpURLConnByUrl(String httpUrl,
			String params) {
		// 获得的数据
		StringBuilder resultData = new StringBuilder();
		URL url = null;
		try {
			// 构造一个URL对象
			url = new URL(httpUrl);
		} catch (MalformedURLException e) {
			Log.e(DEBUG_TAG,
					"  MalformedURLException erro");
		}
		if (url != null) {
			try {
				// 使用HttpURLConnection打开连接
				HttpURLConnection urlConn = (HttpURLConnection) url
						.openConnection();
				// 因为这个是post请求,设立需要设置为true
				urlConn.setDoOutput(true);
				// 设置以POST方式
				urlConn.setRequestMethod("POST");
				// Post 请求不能使用缓存
				urlConn.setUseCaches(false);
				// URLConnection.setInstanceFollowRedirects是成员函数，仅作用于当前函数
				urlConn.setInstanceFollowRedirects(true);
				// 配置本次连接的Content-type，配置为application/x-www-form-urlencoded
				/*urlencoded编码过的form参数
				* 下面我们可以看到我们对正文内容使用URLEncoder.encode进行编码*/
				urlConn.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded");
				// 连接，从postUrl.openConnection()至此的配置必须要在connect之前完成，
				// 要注意的是connection.getOutputStream会隐含的进行connect。
				urlConn.setConnectTimeout(6 * 1000); // 加了这么两个超时设置
				urlConn.setReadTimeout(6 * 1000000000);
				urlConn.connect();
				// DataOutputStream流
				DataOutputStream out = new DataOutputStream(
						urlConn.getOutputStream());
				// 将要上传的内容写入流中
				out.writeBytes(params);
				// 刷新、关闭
				out.flush();
				out.close();
				// 获取数据
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(urlConn.getInputStream()));
				String inputLine = null;
				// 使用循环来读取获得的数据
				while (((inputLine = reader.readLine()) != null)) {
					// 我们在每一行后面加上一个"\n"来换行
					resultData.append(inputLine).append("\r\n");
				}
				reader.close();
				// 关闭http连接
				urlConn.disconnect();
			} catch (IOException e) {
				// 查看输出异常
				Log.e(DEBUG_TAG,
						"the  methods  IOException erro");
			}
		} else {
			Log.e(DEBUG_TAG,
					"the getPostHttpURLConnByUrl methods  Url NULL erro");
		}
		return resultData.toString();
	}

	/**
	 * HttpClient Get通信方式方式
	 * 
	 * @param url1
	 * @return
	 */
	public static String getHttpClientConnByUrl(String url1) {
		// HttpGet连接对象
		HttpGet httpRequest = new HttpGet(url1);
		String resultData = "";
		try {
			// 取得HttpClient对象
			HttpClient httpclient = new DefaultHttpClient();
			// 请求HttpClient，取得HttpResponse
			HttpResponse httpResponse = httpclient.execute(httpRequest);
			// 请求成功
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// 取得返回的字符串
				resultData = EntityUtils.toString(httpResponse.getEntity());
			} else {
				resultData = "请求错误!";
			}
		} catch (ClientProtocolException e) {
			Log.e(DEBUG_TAG,
					"the  methods  ClientProtocolException erro");
		} catch (IOException e) {
			Log.e(DEBUG_TAG,
					"the getHttpClientConnByUrl methods  IOException erro");
		} catch (Exception e) {
			Log.e(DEBUG_TAG,
					"the getHttpClientConnByUrl methods  Exception erro");
		}
		return resultData;
	}

	/**
	 * 通过HttpClient post方式通信
	 * 
	 * @param httpUrl请求的http不带参数
	 * @param postData
	 *            （传入的poatData的参数）
	 * @return
	 */
	public static String getPostHttpClientConnByUrl(String httpUrl,
			List<BasicNameValuePair> postData) {

		// HttpPost连接对象
		HttpPost httpRequest = new HttpPost(httpUrl);
		String resultData = "";
		try {
			// 设置字符集utf-8
			HttpEntity httpentity = new UrlEncodedFormEntity(postData, "utf-8");
			// 请求httpRequest
			httpRequest.setEntity(httpentity);
			// 取得默认的HttpClient
			HttpClient httpclient = new DefaultHttpClient();
			// 取得HttpResponse
			HttpResponse httpResponse = httpclient.execute(httpRequest);
			// HttpStatus.SC_OK表示连接成功
			// 请求成功
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// 取得返回的字符串
				resultData = EntityUtils.toString(httpResponse.getEntity());
			} else {
				resultData = "请求错误!";
			}
		} catch (ClientProtocolException e) {
			Log.e(DEBUG_TAG,
					"the  methods  ClientProtocolException erro");
		} catch (IOException e) {
			Log.e(DEBUG_TAG,
					"the  methods  IOException erro");
		} catch (Exception e) {
			Log.e(DEBUG_TAG,
					"the  methods  Exception erro");
		}
		return resultData;
	}
}
