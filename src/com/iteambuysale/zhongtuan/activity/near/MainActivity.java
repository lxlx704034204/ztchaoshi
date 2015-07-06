package com.iteambuysale.zhongtuan.activity.near;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class MainActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WebView webview=new WebView(this);	
		WebSettings settings1 = webview.getSettings();
		settings1.setJavaScriptEnabled(true);
		settings1.setSupportZoom(true);
		settings1.setBuiltInZoomControls(true);
		settings1.setUseWideViewPort(true);
		settings1.setLoadWithOverviewMode(true);
		webview.loadUrl("http://www.teambuy.com.cn/drmp/join.php");
		setContentView(webview);
	}
}
