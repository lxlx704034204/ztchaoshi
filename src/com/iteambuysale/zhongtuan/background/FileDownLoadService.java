package com.iteambuysale.zhongtuan.background;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;

import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.utilities.FileUtilities;
import com.iteambuysale.zhongtuan.utilities.StringUtilities;

public class FileDownLoadService extends IntentService {

	public FileDownLoadService() {
		super("FileDownLoad Service");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		String url = intent.getStringExtra("url");
		String down_url = getDownLoadUrl(url);
		String filePath = FileUtilities.getFilePath(this, D.FILE_CACHE)+StringUtilities.getImageFileName(down_url);
		showAndDown(down_url,filePath);
	}
	
	private void showAndDown(final String url,final String path){
		final NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
		builder.setContentTitle("下载："+StringUtilities.getImageFileName(path));
		builder.setContentText("");
		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setTicker("正在下载中....");
		downLoadFile(url, path,manager,builder);

	}
	
	private String getDownLoadUrl(String url) {
		try {
			URL resourceURl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection)resourceURl.openConnection();
			conn.setRequestMethod("GET");
			conn.setInstanceFollowRedirects(false);
			conn.addRequestProperty("Accept-Charset", "UTF-8");
			conn.addRequestProperty("User-Agent", "Mozilla/5.0(Windows;U;Windows NT 5.1;zh-CN;rv:1.9.2.8)Firefox/3.6.8");
			conn.connect();
			int status = conn.getResponseCode();
			if (302 == status){
				return conn.getHeaderField("Location");
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return url;
	}

	private void downLoadFile(String url,String path,final NotificationManager manager,final Builder builder){
		FileDownloadAsync file_task = new FileDownloadAsync(url, path){
			@Override
			public void onDownloadSuccess(String fileName) {
				manager.cancel(0);
				Intent intent = new Intent();
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setAction(android.content.Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.fromFile(new File(fileName)), "application/vnd.android.package-archive");
				startActivity(intent);
			}
			
			@Override
			protected void onProgressUpdate(Integer... values) {
				builder.setProgress(100, values[0], false);
				manager.notify(0,builder.build());
				super.onProgressUpdate(values);
			}
		};
		file_task.execute();
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
