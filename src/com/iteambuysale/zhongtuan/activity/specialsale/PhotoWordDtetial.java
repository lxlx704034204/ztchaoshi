package com.iteambuysale.zhongtuan.activity.specialsale;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.activity.BaseActivity;
import com.iteambuysale.zhongtuan.define.D;

public class PhotoWordDtetial extends BaseActivity implements OnClickListener {
	private String nowPrice;
	private String primePrice;
	private String shopId;
	private String productId;
	private String picUrl;
	private WebView productWebView;
//	private WebView shopWebView;
	private TextView price;
	private TextView prime_price;
	private TextView tittle;
	private Button buybtn;
	private Button back;
	private RelativeLayout rl;
	Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.special_picandword);
		initview();
		initdata();
	}

	private void initdata() {
		intent=getIntent();
		nowPrice=intent.getStringExtra("nowprice");
		primePrice=intent.getStringExtra("beforPrice");
		shopId=intent.getStringExtra("shopId");
		productId=intent.getStringExtra("productId");
		WebSettings settings1 = productWebView.getSettings();
		settings1.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN); 
		price.setText(nowPrice);
		prime_price.setText(primePrice);
		WebSettings webSettings1=productWebView.getSettings();
		webSettings1.setSupportZoom(true);
		webSettings1.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webSettings1.setJavaScriptEnabled(true);
		productWebView.loadUrl(D.API_TEMAI_WEBVIEW+productId);
		back.setOnClickListener(new OnClickListener() {				
			@Override
			public void onClick(View arg0) {
				finish();					
			}
		});
	}
	private void initview(){
		productWebView=(WebView) findViewById(R.id.wb_webview1);
		price=(TextView) findViewById(R.id.tv_tw_price);
		prime_price=(TextView) findViewById(R.id.tv_tw_prime_price);
		tittle=(TextView) findViewById(R.id.tv_header_tittle);
		buybtn=(Button) findViewById(R.id.bt_buybtn);
		back=(Button) findViewById(R.id.back);
		tittle.setText("图文详情");
		back.setBackgroundResource(R.drawable.header_back);
		buybtn.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		intent.setClass(getApplicationContext(), SpecialSaleBuyAtOnce.class);
        intent.putExtra("productId", productId);
        startActivity(intent);
		
	}
}
