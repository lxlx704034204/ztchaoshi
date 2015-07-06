package com.iteambuysale.zhongtuan.activity.me.setting;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.activity.BaseActivity;

public class CopyrightActivity extends BaseActivity {
	TextView title;
	Button back;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.copyright_activity);
			title=(TextView) findViewById(R.id.tv_header_tittle);
			back=(Button) findViewById(R.id.back);
			title.setText("版权声明");
			back.setBackgroundResource(R.drawable.header_back);
			back.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View arg0) {
					finish();					
				}
			});
		}

		@Override
		protected void onPause() {
			super.onPause();
		}
		@Override
		protected void onResume() {
			super.onResume();
			
			
		}

}
