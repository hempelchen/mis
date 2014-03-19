package com.chb.mis;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import com.chb.mis.light.LightActivity;
import com.chb.mis.locate.LocateActivity;
import com.chb.mis.wirelessusb.WirelessUSBActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class MisMain extends Activity {
	private boolean mIsExit = false;

	private static MisMain instance = null;
	public static MisMain getInstance() {
		synchronized (MisMain.class) {
			if(instance == null){
				synchronized (MisMain.class) {
					if(instance == null){
						instance = new MisMain();
					}
				}
			}
		}
		return instance;
	}

	/**
	 * Called when the activity is first created.
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		Thread thread = new Thread(null, mainGetMis, "Background");
		thread.start();

		setContentView(R.layout.main);
		GridView gridview = (GridView) findViewById(R.id.gridview);
		gridview.setBackgroundResource(R.drawable.main_bg);


		//生成动态数组，并且转入数据
		ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();

		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map1.put("ItemText", getString(R.string.wireless_usb_title));
		lstImageItem.add(map1);

		HashMap<String, Object> map2 = new HashMap<String, Object>();
		map2.put("ItemText", getString(R.string.location_title));
		lstImageItem.add(map2);

		HashMap<String, Object> map3 = new HashMap<String, Object>();
		map3.put("ItemText", getString(R.string.light_title));
		lstImageItem.add(map3);

		for (int i = 3; i < 9; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
//			map.put("ItemImage", R.drawable.icon);//添加图像资源的ID
			map.put("ItemText", "NO." + String.valueOf(i));//按序号做ItemText
			lstImageItem.add(map);
		}
		//生成适配器的ImageItem <====> 动态数组的元素，两者一一对应
		SimpleAdapter saImageItems = new SimpleAdapter(this, //没什么解释
				                                              lstImageItem,//数据来源
				                                              R.layout.night_item,//night_item的XML实现

				                                              //动态数组与ImageItem对应的子项
				                                              new String[]{"ItemText"},

				                                              //ImageItem的XML文件里面的一个ImageView,两个TextView ID
				                                              new int[]{R.id.ItemText});
		//添加并且显示
		gridview.setAdapter(saImageItems);
		//添加消息处理
		gridview.setOnItemClickListener(new ItemClickListener());
	}

	private Runnable mainGetMis = new Runnable() {
		public void run() {
			//IP地址必须在使用时实时检查，防止用户开始打开，后来关闭
//			Utils.phoneWifiIpV4 = Methods.getLocalIpAddress();
		}
	};

	//当AdapterView被单击(触摸屏或者键盘)，则返回的Item单击事件
	class ItemClickListener implements AdapterView.OnItemClickListener {
		public void onItemClick(AdapterView<?> arg0,//The AdapterView where the click happened
		                        View arg1,//The view within the AdapterView that was clicked
		                        int arg2,//The position of the view in the adapter
		                        long arg3//The row id of the item that was clicked
		) {
			//在本例中arg2=arg3
			HashMap<String, Object> item = (HashMap<String, Object>) arg0.getItemAtPosition(arg2);
			//显示所选Item的ItemText
//			setTitle((String) item.get("ItemText"));
			Intent intent;

			switch (arg2) {
				case 0: //热点
//					Toast.makeText(getApplicationContext(), "启动无线USB", Toast.LENGTH_SHORT).show();
					intent = new Intent(MisMain.this, WirelessUSBActivity.class);
					startActivity(intent);
					break;

				case 1://定位
					intent = new Intent(MisMain.this, LocateActivity.class);
					startActivity(intent);
					break;

				case 2://手电筒
					intent = new Intent(MisMain.this, LightActivity.class);
					startActivity(intent);
					break;

				default:
					Toast.makeText(getApplicationContext(), (String) item.get("ItemText"), Toast.LENGTH_SHORT).show();
					break;
			}
		}

	}

	@Override
	public void onBackPressed() {
		if (mIsExit) {
			this.finish();
			System.exit(0);
			return;
		}
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				mIsExit = false;
			}
		}, 3000);
		Toast.makeText(this, "连续点击退出", Toast.LENGTH_SHORT).show();
		mIsExit = true;

	}

}
