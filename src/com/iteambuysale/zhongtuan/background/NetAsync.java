package com.iteambuysale.zhongtuan.background;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import com.iteambuysale.zhongtuan.utilities.JsonUtilities;
import com.iteambuysale.zhongtuan.utilities.LogUtilities;
import com.iteambuysale.zhongtuan.utilities.StringUtilities;


public abstract class NetAsync extends AsyncTask<Void,Integer, String>{
	private String mRequestUri;
	private NetAsyncListener mListener;
	private ArrayList<NameValuePair> mParams;
	private String mErrMsg;
	private Object mTarget;

	// 初始化函数
	public NetAsync( String requestUri, NetAsyncListener listener) {
		mRequestUri = requestUri;
		mListener = listener;
	}

	// 参数中添加sessionId，进行网络请求
	@Override
	protected String doInBackground(Void... none) {
		mParams = new ArrayList<NameValuePair>();
		String ackToken = ZhongTuanApp.getInstance().getAppSettings().ackToken;
		mParams.add(new BasicNameValuePair(D.ARG_LOGIN_BY_TOKEN, ackToken));
		if(null!=mListener)beforeRequestInBackground(mParams);
		if (!ZhongTuanApp.getInstance().getNetWorkState()) {
			return processNetFailed("亲，我们不能通过网络连接到你，请检查是否已经打开网络");
		}
		return doPost(mRequestUri, mParams);		
	}

	// 处理网络请求结果
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
	
	/*======================================== helpers ===============================================*/
	
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
			postData(conn, params);	
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
			return processNetFailed("网络故障，请稍后再试！");
		}
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
		Map<String,JsonElement> mResult;
		try {
			
			mResult = JsonUtilities.parseModelByType(str, type);
		} catch (Exception e) {
			return null;
		}
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
	
	/**
	 * 网络请求失败处理
	 * @param errmsg
	 * @param listener
	 * @return
	 */
	public String processNetFailed(String errmsg){
		this.mErrMsg = errmsg;
		return D.FLAG_FAILED;
	}
	
	/**
	 * post 数据
	 * @param conn
	 * @param params
	 * @throws IOException
	 */
	private void postData(HttpURLConnection conn,List<NameValuePair> params) throws IOException{
		LogUtilities.Log(D.NETWORK_DEBUG, "[Query Contents]:"+params.toString(),D.DEBUG_DEBUG);
		OutputStream out = conn.getOutputStream();
		out.write(StringUtilities.getPostQuery(params));
		out.flush();
		out.close();
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
	 * 初始化post参数
	 * @param conn
	 * @throws ProtocolException
	 */
	private void initPostConnection(HttpURLConnection conn) throws ProtocolException  {
		conn.setConnectTimeout(1*60*1000);
		conn.setReadTimeout(1*60*1000);
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setUseCaches(false);
		conn.setRequestProperty("Connection", "	keep-alive");
		conn.setRequestProperty("Cookie",ZhongTuanApp.getInstance().getSession());
		conn.setRequestProperty("Accept", "");
	}
	
	//处理NetAsync在doInBackground方法中进行网络请求前的业务
	public abstract void beforeRequestInBackground(List<NameValuePair> params);
	
	//对数据进行解释和db操作
	public abstract Object processDataInBackground(JsonElement elData);

}
