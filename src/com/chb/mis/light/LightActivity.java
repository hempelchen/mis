package com.chb.mis.light;

/**
 * Created by hempel on 14-3-19.
 */

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import com.chb.mis.R;

public class LightActivity extends Activity {

	private static FlashlightSurface mSurface;
	private ImageView mImageView;
	private Button mBtn;
	private boolean isFlashlightOn = false;
	private Handler handler= new Handler();

	private static LightActivity instance = null;
	public static LightActivity getInstance() {
		synchronized (LightActivity.class) {
			if(instance == null){
				synchronized (LightActivity.class) {
					if(instance == null){
						instance = new LightActivity();
					}
				}
			}
		}
		return instance;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		instance = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.light);
		this.setTitle(R.string.light_title);
		this.setTitleColor(Color.BLUE);

		mSurface = (FlashlightSurface) findViewById(R.id.light_surfaceview);
//		mImageView = (ImageView) findViewById(R.id.light_image);
//		mImageView.setVisibility(View.GONE);
		mBtn = (Button)findViewById(R.id.light_btn);
		mBtn.setTextColor(Color.YELLOW);
		mBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				changeLightStatus();
			}
		});

		handler.post(new Runnable() {
			@Override
			public void run() {
				Log.i("CHB", "post.run");
//				changeLightStatus();    //必须要等到 FlashlightSurface.surfaceChanged() 调用之后，再打开手电筒才有效
			}
		});

	}

	public void changeLightStatus(){
		if (isFlashlightOn) {
			mSurface.setFlashlightSwitch(false);
			isFlashlightOn = false;
			mBtn.setText(getString(R.string.light_status) + " 关");
		} else {
			mSurface.setFlashlightSwitch(true);
			isFlashlightOn = true;
			mBtn.setText(getString(R.string.light_status) + " 开");
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
//		if (MotionEvent.ACTION_UP == event.getAction()) {
//			if (isFlashlightOn) {
//				mSurface.setFlashlightSwitch(false);
//				isFlashlightOn = false;
////				mImageView.setImageResource(R.drawable.flashlight_off);
//			} else {
//				mSurface.setFlashlightSwitch(true);
//				isFlashlightOn = true;
////				mImageView.setImageResource(R.drawable.flashlight_on);
//			}
//		}
		return super.onTouchEvent(event);
	}

	@Override
	protected void onDestroy(){
		System.out.println(this.getClass().getSimpleName() + ":" + Thread.currentThread().getStackTrace()[2].getMethodName().toString());

		isFlashlightOn = false;

		super.onDestroy();
	}

}

