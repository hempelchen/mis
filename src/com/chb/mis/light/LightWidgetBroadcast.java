package com.chb.mis.light;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;
import com.chb.mis.utils.NetworkManager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class LightWidgetBroadcast extends BroadcastReceiver {
	private Button light_widget_btn1;
	private TextView light_widget_tx1;

	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println(this.getClass().getSimpleName() + ":" + Thread.currentThread().getStackTrace()[2].getMethodName().toString());

		AppWidgetManager manager = AppWidgetManager.getInstance(context);
		RemoteViews remoteViews = LightWidgetProvider.getView(context);
		int[] appids = manager.getAppWidgetIds(new ComponentName(context, LightWidgetProvider.class));
		manager.updateAppWidget(appids, remoteViews);
	}

}
