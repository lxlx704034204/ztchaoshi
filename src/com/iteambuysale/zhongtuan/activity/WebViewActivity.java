package com.iteambuysale.zhongtuan.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;

import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.define.D;

@SuppressLint("SetJavaScriptEnabled")
public class WebViewActivity extends SuperActivity {

	ProgressBar pb;
	WebView showWebView;
	Button button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_PROGRESS);
		String url = getIntent().getStringExtra(D.BUNDLE_URL);
		setContentView(R.layout.x_block_webview);
		Button btn_back = (Button) findViewById(R.id.back);
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		pb = (ProgressBar) findViewById(R.id.pb);
		showWebView = (WebView) findViewById(R.id.wv);
		button = (Button) findViewById(R.id.sign_up);

		pb.setMax(100);
		WebSettings webSettings = showWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		webSettings.setAllowFileAccess(true);
		showWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		showWebView.loadUrl(url);
		showWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				pb.setProgress(newProgress);
				int count = 0;
				if (newProgress == 100) {
					count += 1;
				}
				if (count == 2) {
					pb.setVisibility(View.GONE);
				}
			}
		});
		showWebView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return super.shouldOverrideUrlLoading(view, url);
			}

		});

	}

	// 报名按钮事件
	public void onbuttonclick(View v) {
		showDialog();

	}

	public void showDialog() {
		AlertDialog dialog = new AlertDialog.Builder(this).create();
		dialog.show();
		Window window = dialog.getWindow();
		window.setContentView(R.layout.webview_dialog);
		// 找到控件

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
