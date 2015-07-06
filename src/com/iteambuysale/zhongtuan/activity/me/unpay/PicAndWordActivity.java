package com.iteambuysale.zhongtuan.activity.me.unpay;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.activity.BaseActivity;
import com.iteambuysale.zhongtuan.activity.near.pay.OrderActivity;
import com.iteambuysale.zhongtuan.activity.near.pay.PayActivity;
import com.iteambuysale.zhongtuan.define.D;

public class PicAndWordActivity extends BaseActivity {
	WebView productWebView,shopWebView;
	TextView price,prime_price,tittle;
	Button buybtn,back;
	String nowPrice,primePrice,shopId,productId,picUrl,proName;
	int tag;
	Intent intent;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.picandword);
			intent=getIntent();
			tag=intent.getIntExtra("tag", 1);
			nowPrice=intent.getStringExtra("cpje");
			primePrice=intent.getStringExtra("pprice");
			shopId=intent.getStringExtra("shop");
			productId=intent.getStringExtra("cpid");
			picUrl=intent.getStringExtra("cppic");
			productWebView=(WebView) findViewById(R.id.webview1);
			shopWebView=(WebView) findViewById(R.id.webview2);
			price=(TextView) findViewById(R.id.price);
			prime_price=(TextView) findViewById(R.id.prime_price);
			tittle=(TextView) findViewById(R.id.tv_header_tittle);
			buybtn=(Button) findViewById(R.id.buybtn);
			back=(Button) findViewById(R.id.back);
			tittle.setText("图文详情");
			back.setBackgroundResource(R.drawable.header_back);
			back.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View arg0) {
					finish();					
				}
			});
			price.setText(nowPrice);
			prime_price.setText(primePrice);
			
			WebSettings webSettings1=productWebView.getSettings();
			webSettings1.setSupportZoom(true);
			webSettings1.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
			webSettings1.setJavaScriptEnabled(true);
			productWebView.loadUrl(D.API_PRODUCT_WEBVIEW+productId);
			WebSettings webSettings2=shopWebView.getSettings();
			webSettings2.setJavaScriptEnabled(true);
			webSettings2.setSupportZoom(true);
			webSettings1.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
			shopWebView.loadUrl(D.API_SHOP_WEBVIEW+shopId);
		}
		public void onBuyNowClick(View v){
			//直接跳到支付，订单号已从OrderDetailActivity传入
			switch (tag) {
			case 0:
				intent.setClass(this, PayActivity.class);
				startActivity(intent);
				break;

			case 1:
				intent.setClass(this, OrderActivity.class);
				startActivity(intent);
				break;
			}
			
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
