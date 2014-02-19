package com.chb.mis.locate;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.chb.mis.R;

public class LocateActivity extends Activity {

	private double latitude = 0.0;
	private double longitude = 0.0;
	private TextView tx1;


	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.locate);
		tx1 = (TextView) findViewById(R.id.tx1);
		tx1.setTextSize(18);
		tx1.setText("我是定位进程，我的进程 PID = " + Process.myPid());
		tx1.setTextColor(Color.YELLOW);

		Button btn1 = (Button) findViewById(R.id.btn1);
		btn1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				tx1.setText("GPS定位中...");
				startLbsWithGps();
			}
		});
		Button btn2 = (Button) findViewById(R.id.btn2);
		btn2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				tx1.setText("网络定位中...");
				startLbsWithNetwork();
			}
		});
		Button btn3 = (Button) findViewById(R.id.btn3);
		btn3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				tx1.setText("被动定位中...");
				startLbsWithPassiveMode();
			}
		});
	}

	private void startLbsWithNetwork() {
		Log.i("LBS", "startLocate");
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, new LocationListener() {
			public void onLocationChanged(Location location) { //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
				// log it when the location changes
				if (location != null) {
					Log.i("LBS", "Location changed : Lat: "
							             + location.getLatitude() + " Lon: "
							             + location.getLongitude());
					tx1.setText("网络定位成功\n经度： " + location.getLongitude() + "\n纬度： " + location.getLatitude());
				} else {
//				startLbsWithNetwork(locationManager);
					tx1.setText("网络定位失败，试试别的定位方法？");
				}
			}

			public void onProviderDisabled(String provider) {
				// Provider被disable时触发此函数，比如GPS被关闭
				Log.i("LBS", "onProviderDisabled");
			}

			public void onProviderEnabled(String provider) {
				//  Provider被enable时触发此函数，比如GPS被打开
				Log.i("LBS", "onProviderEnabled");
			}

			public void onStatusChanged(String provider, int status, Bundle extras) {
				// Provider的转态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
				Log.i("LBS", "onStatusChanged");
			}
		});
	}

	private void startLbsWithGps() {
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Log.i("LBS", "LocationManager.GPS_PROVIDER is enable");
			Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (location != null) {
				latitude = location.getLatitude();
				longitude = location.getLongitude();
				Log.i("LBS", "LocationManager.GPS_PROVIDER Location: Lat: "
						             + location.getLatitude() + " Lon: "
						             + location.getLongitude());
				tx1.setText("GPS定位中成功\n经度： " + location.getLongitude() + "\n纬度： " + location.getLatitude());
			} else {
//				startLbsWithNetwork(locationManager);
				tx1.setText("GPS定位失败，试试别的定位方法？");
			}
		} else {
			Log.i("LBS", "LocationManager.GPS_PROVIDER is disable");
			tx1.setText("GPS定位设置已关闭，请在手机设置中打开GPS定位后再试");
//			startLbsWithNetwork(locationManager);
		}
	}

	private void startLbsWithPassiveMode() {
		Log.i("LBS", "startLocate");
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Location location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
		if (location != null) {
			Log.i("LBS", "Location changed : Lat: "
							             + location.getLatitude() + " Lon: "
							             + location.getLongitude());
			tx1.setText("被动定位成功\n经度： " + location.getLongitude() + "\n纬度： " + location.getLatitude());
		} else {
			tx1.setText("被动定位失败，试试别的定位方法？");
		}
//		locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 1000, 0, new LocationListener() {
//			public void onLocationChanged(Location location) { //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
//				// log it when the location changes
//				if (location != null) {
//					Log.i("LBS", "Location changed : Lat: "
//							             + location.getLatitude() + " Lon: "
//							             + location.getLongitude());
//					tx1.setText("定位成功\n经度： " + location.getLongitude() + "\n纬度： " + location.getLatitude());
//				} else {
////				startLbsWithNetwork(locationManager);
//					tx1.setText("被动定位失败，试试别的定位方法？");
//				}
//			}
//
//			public void onProviderDisabled(String provider) {
//				// Provider被disable时触发此函数，比如GPS被关闭
//				Log.i("LBS", "onProviderDisabled");
//			}
//
//			public void onProviderEnabled(String provider) {
//				//  Provider被enable时触发此函数，比如GPS被打开
//				Log.i("LBS", "onProviderEnabled");
//			}
//
//			public void onStatusChanged(String provider, int status, Bundle extras) {
//				// Provider的转态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
//				Log.i("LBS", "onStatusChanged");
//			}
//		});
	}

}
