package com.iteambuysale.zhongtuan.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.activity.HomeActivity;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.utilities.FileUtilities;
import com.iteambuysale.zhongtuan.utilities.StringUtilities;

public class UpdateService extends Service {

	private NotificationManager updateNotificationManager;
	private Notification updateNotification;
	private PendingIntent updatePendingIntent;
	private Handler updateHandler = new  Handler(){ 
	    @Override
	    public void handleMessage(Message msg) { 
	          switch (msg.what) {
			 case DOWNLOAD_COMPLETE:
				  Intent intent = new Intent();
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.setAction(android.content.Intent.ACTION_VIEW);
					intent.setDataAndType(Uri.fromFile(new File(filePath)), "application/vnd.android.package-archive");
					startActivity(intent);
				break;
			 case DOWNLOAD_FAIL:
               Toast.makeText(getApplicationContext(), "下载失败", 1).show();
			default:
				break;
			}
	    } 
	};
	public static final  int DOWNLOAD_COMPLETE=0;
	private File file;
	private String url;
	public static final int DOWNLOAD_FAIL=1;
	private String filePath;
	private boolean flag=false;
	private String str; 

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//获取传值 
	   // titleId = intent.getIntExtra("titleId",0); 
		try {
			
			url =intent.getStringExtra("url");
		} catch (Exception e) {
			return super.onStartCommand(intent, flags, startId);
		}
		filePath = FileUtilities.getFilePath(this, D.FILE_CACHE)+StringUtilities.getImageFileName(url);
	    file = new File(filePath); 
	 
	    this.updateNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE); 
	    this.updateNotification = new Notification(); 
	    updateNotification.flags = Notification.FLAG_AUTO_CANCEL;
	 
	    //设置下载过程中，点击通知栏，回到主界面 
	    Intent  updateIntent = new Intent(this, HomeActivity.class); 
	    updatePendingIntent = PendingIntent.getActivity(this,0,updateIntent,0); 
	    //设置通知栏显示内容 
	    updateNotification.icon = R.drawable.ic_launcher; 
	    updateNotification.tickerText = "开始下载"; 
	    updateNotification.setLatestEventInfo(this,"中团","0%",updatePendingIntent); 
	    //发出通知 
	    updateNotificationManager.notify(0,updateNotification); 
	 
	    //开启一个新的线程下载，如果使用Service同步下载，会导致ANR问题，Service本身也会阻塞 
	    new Thread(new updateRunnable()).start();//这个是下载的重点，是下载的过程 
	      
	    return super.onStartCommand(intent, flags, startId); 

	}
	class updateRunnable implements Runnable { 
		Message message = updateHandler.obtainMessage();
        public void run() { 
            message.what = DOWNLOAD_COMPLETE; 
            try{ 
               
                if(!file.exists()){ 
                    file.createNewFile(); 
                } 
                //下载函数，以QQ为例子 
                //增加权限; 
                long downloadSize = downloadUpdateFile(url,file); 
                if(downloadSize>0){ 
                    //下载成功 
                    updateHandler.sendMessage(message); 
                } 
            }catch(Exception ex){ 
                ex.printStackTrace(); 
                message.what = DOWNLOAD_FAIL; 
                //下载失败 
                updateHandler.sendMessage(message); 
            } 
        } 
    } 
	public long downloadUpdateFile(String downloadUrl, File saveFile) throws Exception { 
        //这样的下载代码很多，我就不做过多的说明 
        int downloadCount = 0; 
        int currentSize = 0; 
        long totalSize = 0; 
        int updateTotalSize = 0; 
          
        HttpURLConnection httpConnection = null; 
        InputStream is = null; 
        FileOutputStream fos = null; 
          
        try { 
            URL url = new URL(downloadUrl); 
            httpConnection = (HttpURLConnection)url.openConnection(); 
            httpConnection.setRequestMethod("GET");
            httpConnection.setRequestProperty("User-Agent", "PacificHttpClient"); 
            if(currentSize > 0) { 
                httpConnection.setRequestProperty("RANGE", "bytes=" + currentSize + "-"); 
            } 
            httpConnection.setConnectTimeout(10000); 
            httpConnection.setReadTimeout(20000); 
          //  updateTotalSize = httpConnection.getContentLength(); 
            if (httpConnection.getResponseCode() == 404) { 
                throw new Exception("fail!"); 
            } 
            is = httpConnection.getInputStream();   
            fos = new FileOutputStream(saveFile, false); 
            byte buffer[] = new byte[1024*4000]; 
            int readsize = 0; 
            while((readsize = is.read(buffer)) > 0){ 
                fos.write(buffer, 0, readsize);
                if(flag){
                	totalSize += 1; 
                	//为了防止频繁的通知导致应用吃紧，百分比增加10才通知一次 
                	if(totalSize%100==0){
                		str = totalSize%1000+"";
                		if(!TextUtils.isEmpty(str)){
                			str=str.substring(0, 1);
                		}
                		updateNotification.setLatestEventInfo(UpdateService.this, "正在下载",(int)totalSize/1000+"."+str+"M",updatePendingIntent); 
                		updateNotificationManager.notify(0, updateNotification); 
                	}                         
                }
                flag=!flag;
            } 
	}catch(Exception e){
		Message message = updateHandler.obtainMessage();
         message.what=DOWNLOAD_FAIL;
         updateHandler.sendMessage(message);
	}
        finally { 
            if(httpConnection != null) { 
                httpConnection.disconnect(); 
            } 
            if(is != null) { 
                is.close(); 
            } 
            if(fos != null) { 
                fos.close(); 
            } 
        } 
        System.out.println("totalSize::"+totalSize);
        return totalSize; 
    } 

}
