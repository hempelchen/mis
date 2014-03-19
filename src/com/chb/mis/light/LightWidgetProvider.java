package com.chb.mis.light;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import com.chb.mis.R;
import com.chb.mis.utils.NetworkManager;

public class LightWidgetProvider extends AppWidgetProvider {
	private static int index = 0;

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		System.out.println(this.getClass().getSimpleName() + ":" + Thread.currentThread().getStackTrace()[2].getMethodName().toString());
		super.onDeleted(context, appWidgetIds);
	}

	@Override
	public void onDisabled(Context context) {
		System.out.println(this.getClass().getSimpleName() + ":" + Thread.currentThread().getStackTrace()[2].getMethodName().toString());
		super.onDisabled(context);
	}

	@Override
	public void onEnabled(Context context) {
		System.out.println(this.getClass().getSimpleName() + ":" + Thread.currentThread().getStackTrace()[2].getMethodName().toString());
		super.onEnabled(context);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.mobile_light);
		System.out.println(this.getClass().getSimpleName() + ":" + Thread.currentThread().getStackTrace()[2].getMethodName().toString());
		System.out.println(intent.getAction());
		//views的更新，需要放到onUpdate里面，否则安装/第一次添加widgegt时，不能正确显示
//		try {
//			if(LightActivity.getInstance().getLightStatus()) {
//				views.setTextViewText(R.id.tx_mobile_light_status, "开");
//			} else {
//				views.setTextViewText(R.id.tx_mobile_light_status, "关");
//			}
//			LightActivity.getInstance().changeLightStatus();
//		} catch (SecurityException e) {
//			e.printStackTrace();
//		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		super.onReceive(context, intent);

	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
	                     int[] appWidgetIds) {
		System.out.println(this.getClass().getSimpleName() + ":" + Thread.currentThread().getStackTrace()[2].getMethodName().toString());

		for (int i = 0; i < appWidgetIds.length; i++) {

			Intent intent = new Intent("CHANGE_MAIN_BUTTON_CONTENT");
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.mobile_light);
			try {
				if(LightActivity.getInstance().getLightStatus()) {
					remoteViews.setTextViewText(R.id.tx_mobile_light_status, "开");
				} else {
					remoteViews.setTextViewText(R.id.tx_mobile_light_status, "关");
				}
				LightActivity.getInstance().changeLightStatus();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			PendingIntent pending = PendingIntent.getBroadcast(context, 0, intent, 0);
			remoteViews.setOnClickPendingIntent(R.id.btn_mobile_light_switcher, pending);
			appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
		}
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	public static RemoteViews getView(Context context) {
		System.out.println("LightWidgetProvider:" + Thread.currentThread().getStackTrace()[2].getMethodName().toString());

		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.mobile_light);
		try {
			//因为系统设置是异步的，比较慢，而用户点击后会立即同步调用这里，因此，这里的开关状态与运行是的网络开关状态相反，
			// 也就是说，这里的开关状态，是设置成大概1秒后的系统设置状态。
			if(LightActivity.getInstance().getLightStatus()) {
				views.setTextViewText(R.id.tx_mobile_light_status, "开");
			} else {
				views.setTextViewText(R.id.tx_mobile_light_status, "关");
			}
			LightActivity.getInstance().changeLightStatus();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return views;
	}

}
