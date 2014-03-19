package com.chb.mis.memory;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import com.chb.mis.R;

/**
 * Created by hempel on 14-2-20.
 */
public class MemoryCleanActivity extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setTitle(R.string.memory_clean_title);
		this.setTitleColor(Color.BLUE);
		setContentView(R.layout.wireless_usb);

		TextView textView = (TextView) findViewById(R.id.wu_textView);
		textView.setTextColor(Color.YELLOW);
		textView.setText("未完成...");
	}
}