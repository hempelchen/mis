package com.chb.mis;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;

/**
 * Created by hempel on 14-2-18.
 */
public class Methods {
	public static String getLocalIpAddress() {
//		try {
//			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
//				NetworkInterface intf = en.nextElement();
//				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
//					InetAddress inetAddress = enumIpAddr.nextElement();
//					if (!inetAddress.isLoopbackAddress()) {
//						return inetAddress.getHostAddress().toString();
//					}
//				}
//			}
//		} catch (SocketException ex) {
//			Log.e("testAndroid1", ex.toString());
//		}
//		return null;
		WifiManager wifiManager = (WifiManager) MisMain.getInstance().getSystemService(MisMain.getInstance().getApplicationContext().WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ipAddress = wifiInfo.getIpAddress();
		return Formatter.formatIpAddress(ipAddress);
	}

}
