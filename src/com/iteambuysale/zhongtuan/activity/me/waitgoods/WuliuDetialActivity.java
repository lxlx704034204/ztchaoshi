package com.iteambuysale.zhongtuan.activity.me.waitgoods;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.activity.BaseActivity;

public class WuliuDetialActivity extends BaseActivity {
    private WebView webView;
	private ImageView iv_anim;
	private Handler mHandler;

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
		tv_title.setText("物流详情");
		webView = (WebView) findViewById(R.id.wv_showadv);
		iv_anim=(ImageView) findViewById(R.id.iv_zhuanjuhua_webview);
		iv_anim.setVisibility(View.GONE);
	//	webView.loadData("","text/html","UTF-8");
		
	}

	@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
	private void initdata() {
		mHandler=new Handler();
		String id =getIntent().getStringExtra("wuliuid");
		String url="http://app.teambuy.com.cn/webc/m/tmlog/id/"+id;
		//String url ="http://www.teambuy.com.cn/";
		//String url ="http://www.baidu.com";
		WebSettings webSettings1=webView.getSettings();
		webSettings1.setSupportZoom(true);
		webSettings1.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webSettings1 .setJavaScriptCanOpenWindowsAutomatically(true);
		webSettings1.setJavaScriptEnabled(true);
		MyWebViewClient webviewclint=new MyWebViewClient();
		//webviewclint.shouldOverrideUrlLoading(web, url)
		webView.setWebViewClient(webviewclint);
		webView.setWebChromeClient(new WebChromeClient()
	        {

	            @Override
	            public boolean onJsAlert(WebView view, String url, String message,
	                    JsResult result)
	            {
	                // TODO Auto-generated method stub
	                return super.onJsAlert(view, url, message, result);
	            }

	        });
		//webView.setWebChromeClient(webclient);
		//webView.addJavascriptInterface(new DemoJavaScriptInterface(), url);
		if(url!=null){
			webView.loadUrl(url);
		}
		
	}
	class MyWebViewClient extends WebViewClient {  
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			startanim();
			super.onPageStarted(view, url, favicon);
		}
		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			iv_anim.setVisibility(View.GONE);
			super.onPageFinished(view, url);
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
