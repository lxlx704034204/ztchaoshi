package com.iteambuysale.zhongtuan.background;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.AsyncTask;

import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.utilities.LogUtilities;

public abstract class FileDownloadAsync extends AsyncTask<Void, Integer, Void>{
	private String mUrl;
	private String mPath;
	public FileDownloadAsync(String url,String path){
		mUrl = url;
		mPath = path;
	}

	@Override
	protected Void doInBackground(Void... params) {
		try {
			File file = new File(mPath);
			URL resourceUrl = new URL(mUrl);
			HttpURLConnection conn = (HttpURLConnection)resourceUrl.openConnection();
			int fileLength = conn.getContentLength();
			
			InputStream in = conn.getInputStream();
			if (-1 == fileLength){
				fileLength = in.available();
			}
			OutputStream out = new FileOutputStream(file);
			byte[] buffer = new byte[11*1024];
			int position = 0;
			int current = 0;
			while ((position = in.read(buffer)) != -1){
				if (fileLength == 0){
					publishProgress(100);
				}else{
					publishProgress((current+=position)/fileLength*100);
				}
				out.write(buffer,0,position);
			}
			out.flush();
			out.close();
			in.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		onDownloadSuccess(mPath);
		LogUtilities.Log(D.DEBUG,"download file complete",D.DEBUG_DEBUG);
	}
		
	public abstract void onDownloadSuccess(String f);
	

}
