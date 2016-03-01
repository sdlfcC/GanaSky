package com.ganasky.Help;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
/**
 * ����һ��ͨ�Ű����࣬������������������һЩ��ͨ���йصķ���
 * @author 
 *
 */
public abstract class ConnUrlHelper {
	private static String DEBUG_TAG = "ConnUrlHelper";

	public static boolean hasInternet(Activity activity) {

		ConnectivityManager manager = (ConnectivityManager) activity

		.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo info = manager.getActiveNetworkInfo();

		if (info == null || !info.isConnected()) {

			return false;
		}
		if (info.isRoaming()) {
			
			return true;
		}
		return true;
	}

}
