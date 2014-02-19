package com.chb.mis.locate;

/**
 * Created by hempel on 14-2-12.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TestLocation extends Activity {

	Location gps_loc = null;
	Location agps_loc = null;
	Location pass_loc = null;
	int gps_failed = 0;
	int agps_failed = 0;
	int pass_failed = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.main);

		LinearLayout layout = new LinearLayout(this);
		layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
		layout.setOrientation(LinearLayout.VERTICAL);
		final TextView gps_tv = new TextView(this);
		final TextView agps_tv = new TextView(this);
		final TextView pass_tv = new TextView(this);
		Button gps_refresh = new Button(this);
		Button agps_refresh = new Button(this);
		Button pass_refresh = new Button(this);
		gps_tv.setTextSize(20);
		agps_tv.setTextSize(20);
		pass_tv.setTextSize(20);
		gps_tv.setText("GPS定位 数据将在这儿显示");
		agps_tv.setText("网络定位 数据将在这儿显示");
		pass_tv.setText("被动定位 数据将在这儿显示");
		gps_refresh.setTextSize(20);
		agps_refresh.setTextSize(20);
		pass_refresh.setTextSize(20);

		layout.addView(gps_tv);
		layout.addView(gps_refresh);

		layout.addView(agps_tv);
		layout.addView(agps_refresh);

		layout.addView(pass_tv);
		layout.addView(pass_refresh);

		final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		gps_refresh.setText("gps-刷新");
		gps_refresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				gps_loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

				if (gps_loc != null) {
					gps_tv.setText("GPS定位结果:" + gps_loc.getLatitude() + "," + gps_loc.getLongitude());
				} else {
					gps_failed++;
					gps_tv.setText("定位失败:失败 " + gps_failed + " 次");
				}
				gps_tv.forceLayout();
			}
		});

		agps_refresh.setText("agps-刷新");
		agps_refresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				agps_loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				if (agps_loc != null) {
					agps_tv.setText("网络定位结果:" + agps_loc.getLatitude() + "," + agps_loc.getLongitude());
				} else {
					agps_failed++;
					agps_tv.setText("定位失败:失败 " + agps_failed + " 次");
				}
				agps_tv.forceLayout();
			}
		});

		pass_refresh.setText("pass-刷新");
		pass_refresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				pass_loc = lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

				if (pass_loc != null) {
					pass_tv.setText("被动定位结果:" + pass_loc.getLatitude() + "," + pass_loc.getLongitude());
				} else {
					pass_failed++;
					pass_tv.setText("定位失败:失败 " + pass_failed + " 次");
				}
				pass_tv.forceLayout();
			}
		});

		final TextView gps_tv_r = new TextView(this);
		final TextView agps_tv_r = new TextView(this);
		final TextView pass_tv_r = new TextView(this);

		gps_tv_r.setTextSize(18);
		agps_tv_r.setTextSize(18);
		pass_tv_r.setTextSize(18);

		gps_tv_r.setTextColor(Color.YELLOW);
		agps_tv_r.setTextColor(Color.YELLOW);
		pass_tv_r.setTextColor(Color.YELLOW);

		gps_tv_r.setText("GPS定位更新 数据将在这儿显示");
		agps_tv_r.setText("网络定位更新 数据将在这儿显示");
		pass_tv_r.setText("被动定位更新 数据将在这儿显示");

		layout.addView(gps_tv_r);
		layout.addView(agps_tv_r);
		layout.addView(pass_tv_r);

		LocationListener listen = new LocationListener() {

			@Override
			public void onLocationChanged(Location loc) {
				if (loc != null) {
					String provider = loc.getProvider();
					if (LocationManager.GPS_PROVIDER.equals(provider)) {
						gps_tv_r.setText("GPS定位更新:" + loc.getLatitude() + "," + loc.getLongitude());
						gps_tv_r.forceLayout();
						gps_tv_r.setTextColor(Color.YELLOW);
					} else if (LocationManager.NETWORK_PROVIDER.equals(provider)) {
						agps_tv_r.setText("网络定位更新:" + loc.getLatitude() + "," + loc.getLongitude());
						agps_tv_r.forceLayout();
						agps_tv_r.setTextColor(Color.YELLOW);
					} else if (LocationManager.PASSIVE_PROVIDER.equals(provider)) {
						pass_tv_r.setText("被动定位更新:" + loc.getLatitude() + "," + loc.getLongitude());
						pass_tv_r.forceLayout();
						pass_tv_r.setTextColor(Color.YELLOW);
					} else {
						pass_tv_r.setText("其它定位更新:" + loc.getLatitude() + "," + loc.getLongitude());
						pass_tv_r.forceLayout();
						pass_tv_r.setTextColor(Color.YELLOW);
					}
				}
			}

			@Override
			public void onProviderDisabled(String arg0) {
				Log.i("LBS", "onProviderDisabled");
			}

			@Override
			public void onProviderEnabled(String arg0) {
				Log.i("LBS", "onProviderEnabled");
			}

			@Override
			public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
				Log.i("LBS", "onStatusChanged");
			}
		};

		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listen);
		lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listen);
		lm.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, listen);

		setContentView(layout);
	}
}