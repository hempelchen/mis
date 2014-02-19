package com.chb.mis.wirelessusb;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;
import com.chb.mis.R;
import com.chb.mis.utils.Methods;
import com.chb.mis.utils.Utils;

import java.io.*;

/**
 * Created by hempel on 14-2-18.
 */
public class WirelessUSBActivity extends Activity {
	private String ftpConfigDir= Environment.getExternalStorageDirectory().getAbsolutePath()+"/ftpConfig/";
	private String wuSuccess = "请在我的电脑或者浏览器中输入以下地址（电脑和手机要在同一个路由器下）：\n\n";
	private String wuFailed = "请先打开WIFI并连接成功";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wireless_usb);
		this.setTitle(R.string.wireless_usb_title);
		this.setTitleColor(Color.BLUE);

		TextView textView = (TextView) findViewById(R.id.wu_textView);
		textView.setTextColor(Color.YELLOW);
		Utils.phoneWifiIpV4 = Methods.getLocalIpAddress();
		if(Utils.phoneWifiIpV4.equals("0.0.0.0")) {
			textView.setText(wuFailed);
		} else {
			textView.setText(wuSuccess + "ftp://" + Utils.phoneWifiIpV4 + ":" + FtpServerImpl.ftpServerPort);

			Thread thread = new Thread(null, startFtpServer, "Background");
			thread.start();
		}

	}

	private Runnable startFtpServer = new Runnable() {
		public void run() {
			File f=new File(ftpConfigDir);
			if(!f.exists())
				f.mkdir();
			File fconf = new File(ftpConfigDir+"users.properties");
			if(fconf.exists())
				fconf.delete();

			copyResourceFile(R.raw.users, ftpConfigDir+"users.properties");
			FtpServerImpl.getInstance().startFtpServer(ftpConfigDir);
		}
	};

	private void copyResourceFile(int rid, String targetFile){
		InputStream fin = ((Context)this).getResources().openRawResource(rid);
		FileOutputStream fos=null;
		int length;
		try {
			fos = new FileOutputStream(targetFile);
			byte[] buffer = new byte[1024];
			while( (length = fin.read(buffer)) != -1){
				fos.write(buffer,0,length);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(fin!=null){
				try {
					fin.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(fos!=null){
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	protected void onDestroy(){
		super.onDestroy();

		// 关于Ftp服务
		FtpServerImpl.getInstance().stopFtpServer();
	}
}