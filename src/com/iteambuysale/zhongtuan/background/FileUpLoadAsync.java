package com.iteambuysale.zhongtuan.background;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.global.NetAsyncListener;
import com.iteambuysale.zhongtuan.model.NetError;
import com.iteambuysale.zhongtuan.utilities.FileUtilities;
import com.iteambuysale.zhongtuan.utilities.JsonUtilities;
import com.iteambuysale.zhongtuan.utilities.LogUtilities;

public abstract class FileUpLoadAsync extends AsyncTask<Void,Void,String> {
	private String mRequestUri;
	private String[] mPicNames;
	private ArrayList<NameValuePair> mParams;
	private NetAsyncListener mListener;
	private String mErrMsg;
	private Object mTarget;
	private static final String LINE_END = "\r\n";
	private static final String TWO_HYPHENS = "--";
	private static final String BOUNDARY = "*Z*T*Z*T*Z*T*Z*Z*";
	
	public FileUpLoadAsync(String url, String[] picNames,NetAsyncListener listener){
		mRequestUri = url;
		mPicNames = picNames;
		mListener = listener;
	}

	@Override
	protected String doInBackground(Void... params) {
		if (!ZhongTuanApp.getInstance().getNetWorkState()) {
			return processNetFailed("亲，我们不能通过网络连接到你，请检查是否已经打开网络");
		}
		initDefaultParams();
		if(null!=mListener)beforeRequestInBackground(mParams);
		if(null == mParams)return processNetFailed("至少选择一张图片");
//		for (String fileName : mPicNames){
//			if(null == fileName || !FileUtilities.isImageExists(fileName))continue;
//			processBitmapBeforeUpload(fileName);
//		}
		return doPost(mRequestUri, mParams);
	}
	
	@Override
	protected void onPostExecute(String status) {
		switch (status) {
		case D.FLAG_SUCCESS:
			if(null!=mListener)mListener.onResultSuccess(mRequestUri,mTarget);
			break;
		case D.FLAG_FAILED:
			if(null!=mListener)mListener.onResultError(mRequestUri,mErrMsg);
			break;
		case D.FLAG_TOKEN_TIMEOUT:
			if(null!=mListener)mListener.onTokenTimeout();
			break;
		}
	}
	
	//////////////////////////////////// Helpers ///
	/**
	 * 初始化默认的参数
	 * TODO
	 * Anddward.Liao <Anddward@gmail.com>
	 * 20142014-12-13下午4:15:15
	 */
	private void initDefaultParams() {
		if(null == mParams)mParams = new ArrayList<NameValuePair>();
		String ackToken = ZhongTuanApp.getInstance().getAppSettings().ackToken;
		mParams.add(new BasicNameValuePair(D.ARG_LOGIN_BY_TOKEN, ackToken));
		
	}
	
	/**
	 * post 请求与解释
	 * @param strUrl
	 * @param params
	 * @param listener
	 * @return
	 */
	private String doPost(String strUrl,List<NameValuePair> params){
		try {
			URL _url = new URL(strUrl);
			HttpURLConnection conn = (HttpURLConnection)_url.openConnection();
			initPostConnection(conn);	
			LogUtilities.Log(D.NETWORK_DEBUG, "[Begin Request]:"+strUrl,D.DEBUG_DEBUG);
			LogUtilities.Log(D.NETWORK_DEBUG, "[request Args]:"+params.toString(),D.DEBUG_DEBUG);
			LogUtilities.Log(D.NETWORK_DEBUG, "[request Header]:"+conn.getRequestProperties().toString(),D.DEBUG_DEBUG);
			conn.connect();	
			postData(conn);	
			switch (conn.getResponseCode()) {
			case HttpURLConnection.HTTP_OK:
				String cookies = conn.getHeaderField("Set-Cookie");
				if (null != cookies && cookies.length()>0){
					ZhongTuanApp.getInstance().saveSession(cookies);
				}
				LogUtilities.Log(D.NETWORK_DEBUG,"[response header]:"+conn.getHeaderFields().toString(),D.DEBUG_DEBUG);
				String res = getStringFromNet(conn.getInputStream());
				LogUtilities.superLog(res);
				conn.disconnect();
				if (null != res && !"".equals(res)){
					return parseJson(res);
				}else{
					return processNetFailed("访问结果成功，但是服务器没有返回任何内容");
				}
			default:
				return processNetFailed("Error:"+conn.getResponseCode()+"\t errorContent:"+conn.getResponseMessage());
			}
		} catch (MalformedURLException e) {
			LogUtilities.Log(D.NETWORK_DEBUG, "[MalformedURLException]:"+e.toString(),D.DEBUG_DEBUG);
			return processNetFailed("[MalformedURLException]:"+e.toString());
		} catch (IOException e) {
			LogUtilities.Log(D.NETWORK_DEBUG, "[IOException]:"+e.toString(),D.DEBUG_DEBUG);
			return processNetFailed("[IOException]"+e.toString());
		}
	}
	
	/**
	 * 初始化post参数
	 * @param conn
	 * @throws ProtocolException
	 */
	private void initPostConnection(HttpURLConnection conn){
		try {
			conn.setConnectTimeout(1*60*1000);
			conn.setReadTimeout(1*60*1000);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");
			conn.setUseCaches(false);
			conn.setRequestProperty("Connection", "	keep-alive");
			conn.setRequestProperty("Cookie",ZhongTuanApp.getInstance().getSession());		
	        conn.setRequestProperty("ENCTYPE", "multipart/form-data");
	        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + BOUNDARY);
		} catch (ProtocolException e) {
			processNetFailed("网络协议异常");
		}
	}
	
	/**
	 * post 数据
	 * @param conn
	 * @param params
	 * @throws IOException
	 */
	private void postData(HttpURLConnection conn){
		LogUtilities.Log(D.NETWORK_DEBUG, "[Query Contents]:"+mParams.toString(),D.DEBUG_DEBUG);
		try {
			DataOutputStream ot = new DataOutputStream(conn.getOutputStream());
			postParams(ot);
			if(mPicNames!=null){
				
				postFiles(ot);
			}
			ot.flush();
			ot.close();
		} catch (IOException e) {
			processNetFailed(e.toString());
		}

	}
	
	private void postFiles(DataOutputStream ot) {
		File imageFile;
		try {
			for(String fileName : mPicNames){
				if(FileUtilities.isImageExists(fileName)){
					imageFile=	FileUtilities.getImageFile(fileName);
				}
				else{				
				imageFile=new File(fileName);
				}
				ot.writeBytes(LINE_END);
				ot.writeBytes(TWO_HYPHENS+BOUNDARY+LINE_END);
				ot.writeBytes("Content-Disposition:form-data; name=upload"+fileName+";filename="+fileName+LINE_END);
		        ot.writeBytes(LINE_END);		       
		        InputStream imageIn = processBitmapBeforeUpload(imageFile);
		        LogUtilities.Log("FileUpLoadAsync", "----name---->"+imageFile.getName()+imageFile.length());
		        byte[] fileCache = new byte[4096];
		        int position = 0;
		        while ( (position = imageIn.read(fileCache)) != -1){
		        	ot.write(fileCache,0, position);
		        }
		        imageIn.close();
		        ot.writeBytes(LINE_END);
			}
	        ot.writeBytes(TWO_HYPHENS+BOUNDARY+TWO_HYPHENS+LINE_END);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void postParams(OutputStream ot) throws UnsupportedEncodingException, IOException{
		if(null == mParams)return;
		StringBuilder postData = new StringBuilder();
		for (NameValuePair nvp : mParams){
			postData.append(TWO_HYPHENS+BOUNDARY+LINE_END);
			postData.append("Content-Disposition:form-data; name=\""+nvp.getName()+"\""+LINE_END);
			postData.append(LINE_END);
			postData.append(nvp.getValue()+LINE_END);
		}
		ot.write(postData.toString().getBytes());
		ot.flush();
	}
	
	/**
	 * 网络请求失败处理
	 * @param errmsg
	 * @param listener
	 * @return
	 */
	private String processNetFailed(String errmsg){
		this.mErrMsg = errmsg;
		return D.FLAG_FAILED;
	}
	
	/**
	 * 从inputStream读取数据到String
	 * @param in
	 * @return
	 * @throws IOException
	 */
	private String getStringFromNet(InputStream in) throws IOException{
		StringBuilder strBd = new StringBuilder();
		int position = 0;
		byte[] buffer  = new byte[1024];
		while ((position = in.read(buffer)) != -1){
			strBd.append(new String(buffer, 0,position));
		}
		in.close();
		return strBd.toString();
	}
	
	/**
	 * json 数据解释
	 * @param str
	 * @param listener
	 * @return
	 */
	private String parseJson(String str){
		Type type = new TypeToken<Map<String, JsonElement>>() {
		}.getType();
		Map<String,JsonElement> mResult = JsonUtilities.parseModelByType(str, type);
		if (!mResult.containsKey(D.KEY_RET_RET)){
			return processNetFailed("ret not found");
		}
		String status = mResult.get(D.KEY_RET_RET).getAsString();
		switch (status) {
		case D.FLAG_SUCCESS:
			JsonElement elData = mResult.get(D.KEY_RET_DATA);
			return processNetSuccess(elData);
		case D.FLAG_FAILED:
			NetError error = JsonUtilities.parseModelByType(mResult.get(D.KEY_RET_DATA), NetError.class);
			return processNetFailed(error.getErrmsg());
		case D.FLAG_TOKEN_TIMEOUT:
			return D.FLAG_TOKEN_TIMEOUT;
		default:
			return processNetFailed("not support ret value");
		}
	}
	
	/**
	 * 网络请求成功处理
	 * @param data
	 * @return
	 */
	private String processNetSuccess(JsonElement data){
		if(null!=mListener)mTarget = processDataInBackground(data);
		return D.FLAG_SUCCESS;
	}

	///////////////////////////////////// Abstract Methods //
	/**
	 * 根据图片文件来压缩图片，必须把压缩完的图片转换成输入流
	 * @param name
	 * @return
	 * lforxeverc
	 * 2014-12-24下午3:02:50
	 * @throws FileNotFoundException 
	 */
	public abstract InputStream processBitmapBeforeUpload(File name) throws FileNotFoundException;
	
	/**
	 * 在这里添加参数
	 * @param mParams
	 * TODO
	 * Anddward.Liao <Anddward@gmail.com>
	 * 20142014-12-13下午4:18:33
	 */
	public abstract void beforeRequestInBackground(ArrayList<NameValuePair> mParams);
	
	//对数据进行解释和db操作
	public abstract Object processDataInBackground(JsonElement elData);
}
