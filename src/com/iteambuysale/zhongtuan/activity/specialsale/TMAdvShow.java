package com.iteambuysale.zhongtuan.activity.specialsale;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.define.D;

public class TMAdvShow extends Activity {
	private WebView webView;
	private ImageView iv_anim;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tm_adv_show);
		initview();
		initdata();
	}

	private void initview() {
		TextView tv_title=(TextView) findViewById(R.id.tv_header_tittle);
		tv_title.setText("中团新动态");
		webView = (WebView) findViewById(R.id.wv_showadv);
		iv_anim=(ImageView) findViewById(R.id.iv_zhuanjuhua_webview);
		
	}

	private void initdata() {
		String url =getIntent().getStringExtra("url");
		WebSettings webSettings1=webView.getSettings();
		webSettings1.setSupportZoom(true);
		webSettings1.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webSettings1.setJavaScriptEnabled(true);
		WebViewClient webviewclint=new WebViewClient();
		//webviewclint.shouldOverrideUrlLoading(web, url)
		webView.setWebViewClient(webviewclint);
		if(url!=null){
			webView.loadUrl(url);
		}
		
	}
	class MyWebViewClient extends WebViewClient {  
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			super.onPageStarted(view, url, favicon);
			startanim();
		}
		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);
			iv_anim.setVisibility(View.GONE);
		}
	    @Override  
	  
	    public boolean shouldOverrideUrlLoading(WebView view, String url){  
	  
	    // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边  
	  
	       view.loadUrl(url);  
	  
	       return true;  
	  
	       }  
	  
	}  
	public void startanim() {
		iv_anim.setBackgroundResource(R.anim.special_sale_refresh);
		AnimationDrawable animationdrawable = (AnimationDrawable) iv_anim
				.getBackground();
		animationdrawable.start();
	}
}
