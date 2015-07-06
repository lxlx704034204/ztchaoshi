package com.iteambuysale.zhongtuan.utilities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.util.LruCache;
import android.widget.Button;
import android.widget.ImageView;

import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.define.D;

public class ImageUtilities {
	private static int max=(int) Runtime.getRuntime().maxMemory();
	private static LruCache<String, Bitmap> bitmapCache;
	private static List<String> removePath=new ArrayList<>();
	private static ArrayList<AsyncTask<Void, Void, Bitmap>> tasklist=new ArrayList<AsyncTask<Void, Void, Bitmap>>();
		
	public static void loadBitMap(String url, ImageView iv){
		/*int[] imageSize = getImageSize(iv);
		//System.out.println("iv.getId():"+iv.getId()+"imageSize[0]:"+imageSize[0]+",imageSize[1]:"+imageSize[1]);
		loadBitMap(url,iv,null,imageSize[0],imageSize[1]);*/
		loadBitMap(url,iv,null,iv.getWidth(),iv.getHeight(),null);
	}
	
	public static void loadBitMap(String url, ImageView iv ,int width, int height){
		loadBitMap(url, iv, null,width,height,null);
	}
	
	public static void loadBitMap(String url, ImageView iv,Object placeHolder){
		loadBitMap(url,iv,placeHolder,iv.getWidth(),iv.getHeight(),null);
	}
	public static void loadBitMap(
			LruCache<String, Bitmap> memorycahe, String path,
			ImageView image) {
          	loadBitMap(path,image,null,image.getWidth(),image.getHeight(),memorycahe);	
	}
	public static void loadBitMap(String url, ImageView iv,Object placeHolder,int width,int height,LruCache<String, Bitmap> memorycahe){
//		iv.setBackgroundColor(0xffD9D979);
		iv.setImageBitmap(null);
		String tag = getTagForItem(url, width, height);
		LogUtilities.Log(D.DEBUG, "tag:"+tag,D.DEBUG_DEBUG);
		iv.setTag(tag);
		Bitmap bmp = getBitmapFromCache(tag);
		if (null != bmp){
			LogUtilities.Log(D.DEBUG, "load from cache",D.DEBUG_DEBUG);
			iv.setImageBitmap(bmp);
			if (memorycahe!=null) {
				memorycahe.put(url, bmp);
			}
		}else{
			LogUtilities.Log(D.DEBUG, "load async:",D.DEBUG_DEBUG);
			setPlaceHolder(placeHolder,iv);			
			asyncLoad(url,tag,iv,width,height,memorycahe);
		}
	}
	
	private static void setBitmap(String tag,ImageView iv,Bitmap bmp){
		if(null != iv && tag.equals( iv.getTag())){
			iv.setImageBitmap(bmp);
		}else{
			LogUtilities.Log(D.DEBUG, "tag issue");
		}
	}
	private static void setbuttonBitmap(String tag,Button btn,Bitmap bmp,int density){
		if(null != btn && tag.equals( btn.getTag())){
			BitmapDrawable drawable = new BitmapDrawable(bmp);
			drawable.setBounds(1, 1, 40*density, 40*density);
			btn.setCompoundDrawables(null, drawable, null, null);
		}else{
			LogUtilities.Log(D.DEBUG, "tag issue");
		}
	}
	///////////////////////////// 异步 //////////////////////////////////

	public static void asyncLoad(final String url,final String tag,final ImageView iv,final int width, final int height,final LruCache<String, Bitmap> memorycahe) {
		if(url==null||url.length()==0) {iv.setImageResource(R.drawable.place_holder); return;}
		final String fileName = StringUtilities.getImageFileName(url);
		AsyncTask< Void, Void, Bitmap> load_task = new AsyncTask<Void, Void, Bitmap>(){
			@Override
			protected Bitmap doInBackground(Void... params) {
				Bitmap b = null;
				if(!StringUtilities.ishttpUrl(url)){
					b = FileUtilities.readImageWithFilePath(url, width, height);
					
					LogUtilities.Log(D.DEBUG, "load absulutly path："+url,D.DEBUG_DEBUG);
				}else{
					b = FileUtilities.readImageWithFileName(fileName, width, height);
					if(b == null){
						b = readImageFromNet(url,width,height);
						//System.out.println("asyncLoad(): "+url);
						LogUtilities.Log(D.DEBUG, "load from net"+url,D.DEBUG_DEBUG);
					}else{
						LogUtilities.Log(D.DEBUG, "load from flash"+url,D.DEBUG_DEBUG);
					}
				}
				if (memorycahe!=null&&url!=null&&b!=null) {
					memorycahe.put(url, b);
				}
				return b;
			}
			
			private Bitmap readImageFromNet(String url,int width,int height) {
				try {
					URL netUrl = new URL(url);
					FileUtilities.saveImage(netUrl.openConnection().getInputStream(), fileName);
					Bitmap bmp = FileUtilities.readImageWithFileName(fileName, width, height);
					LogUtilities.Log(D.DEBUG, "load from net success:"+url,D.DEBUG_DEBUG);
					return bmp;
				} catch (MalformedURLException e) {
					iv.setImageResource(R.drawable.place_holder);
					LogUtilities.Log(D.DEBUG, "load from net failed:"+url,D.DEBUG_DEBUG);
					return null;
				} catch (IOException e) {
					LogUtilities.Log(D.DEBUG, "load from net failed:"+url,D.DEBUG_DEBUG);
					return null;
				}
			}

			@Override
			protected void onPostExecute(Bitmap result) {
				saveBitmapWithTag(tag, result);
				setBitmap(tag, iv, result);
				
				removePath.add(tag);
			}
			
		};
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
			load_task.executeOnExecutor(Executors.newFixedThreadPool(21));
		}else{
			load_task.execute();
		}
		if (memorycahe!=null) {
			tasklist.add(load_task);
		}
	}
	 public static ArrayList<AsyncTask<Void, Void, Bitmap>> getTaskList(){
		 return tasklist;
	 }
	public static void asyncLoad(final String url,final Button btn,final int density) {
		final String fileName = StringUtilities.getImageFileName(url);
		btn.measure(0, 0);
		final int width =(int) (btn.getMeasuredWidth()*0.5);
		final int height=(int) (btn.getMeasuredHeight()*0.5);
		final String tag = getTagForItem(url, width, height);
		btn.setTag(tag);
		AsyncTask< Void, Void, Bitmap> load_task = new AsyncTask<Void, Void, Bitmap>(){
			
		

			@Override
			protected Bitmap doInBackground(Void... params) {
				Bitmap b;
				if(!StringUtilities.ishttpUrl(url)){
					b = FileUtilities.readImageWithFilePath(url, width, height);
					LogUtilities.Log(D.DEBUG, "load absulutly path",D.DEBUG_DEBUG);
				}else{
					b = FileUtilities.readImageWithFileName(fileName, width, height);
					if(b == null){
						b = readImageFromNet(url,width,height);
						LogUtilities.Log(D.DEBUG, "load from net",D.DEBUG_DEBUG);
					}else{
						LogUtilities.Log(D.DEBUG, "load from flash",D.DEBUG_DEBUG);
					}
				}
				return b;
			}
			
			private Bitmap readImageFromNet(String url,int width,int height) {
				try {
					URL netUrl = new URL(url);
					FileUtilities.saveImage(netUrl.openConnection().getInputStream(), fileName);
					Bitmap bmp = FileUtilities.readImageWithFileName(fileName, width, height);
					LogUtilities.Log(D.DEBUG, "load from net success",D.DEBUG_DEBUG);
					return bmp;
				} catch (MalformedURLException e) {
					LogUtilities.Log(D.DEBUG, "load from net failed",D.DEBUG_DEBUG);
					return null;
				} catch (IOException e) {
					LogUtilities.Log(D.DEBUG, "load from net failed",D.DEBUG_DEBUG);
					return null;
				}
			}
			
			@Override
			protected void onPostExecute(Bitmap result) {
				saveBitmapWithTag(tag, result);
				setbuttonBitmap(tag, btn, result,density);
			}
			
			
		};
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
			load_task.executeOnExecutor(Executors.newFixedThreadPool(21));
		}else{
			load_task.execute();
		}
		
	}
	
	
	
	////////////////////////// place holder //////////////////////////////
	
	/**
	 * 设置默认图片
	 * @param iv
	 * @param placeHolder
	 * Anddward.Liao <Anddward@gmail.com>
	 * 20142014-12-17上午10:39:16
	 */
	private static void setPlaceHolder(Object placeHolder,ImageView iv) {
		if(placeHolder instanceof Integer){
			int rId = (int)placeHolder;
			setPlaceHolderWithId(rId,iv);
		}else if(placeHolder instanceof String){
			String name = (String)placeHolder;
			setPlaceHolderWithName(name,iv);
		}else if(placeHolder instanceof Bitmap){
			Bitmap b = (Bitmap)placeHolder;
			String tag = getTagForItem(placeHolder, iv.getWidth(), iv.getHeight());
			saveBitmapWithTag(tag, b);
			if(null != iv)iv.setImageBitmap(b);
		}
	}
	
	/**
	 * 根据图片名称加载默认图片
	 * @param name
	 * @param iv
	 * Anddward.Liao <Anddward@gmail.com>
	 * 20142014-12-17上午11:10:45
	 */
	private static void setPlaceHolderWithName(String name,ImageView iv) {
		String tag = getTagForItem(name, iv.getWidth(), iv.getHeight());
		Bitmap b = getBitmapFromCache(tag);
		if(null == b){
			b = FileUtilities.readImageWithFileName(name, iv.getWidth(), iv.getHeight());
			saveBitmapWithTag(tag, b);
		}
		if(null != iv)iv.setImageBitmap(b);
	}

	/**
	 * 根据resourceId加载默认图片
	 * @param rId
	 * @param iv
	 * Anddward.Liao <Anddward@gmail.com>
	 * 20142014-12-17上午11:01:05
	 */
	private static void setPlaceHolderWithId(int rId,ImageView iv) {
		String tag = getTagForItem(rId, iv.getWidth(), iv.getHeight());
		Bitmap b = getBitmapFromCache(tag);
		if(null == b){
			b = FileUtilities.readImageWithResourceId(rId, iv.getWidth(), iv.getHeight());
			saveBitmapWithTag(tag, b);
		}
		if(null != iv)iv.setImageBitmap(b);
	}
	
	///////////////////////////  缓存管理 /////////////////////////////////

	/**
	 * 从缓存中获取图片
	 * @param tag
	 * @return 如果缓存中存在，则返回图片，否则返回null
	 * Anddward.Liao <Anddward@gmail.com>
	 * 20142014-12-15上午11:00:21
	 */
	private static Bitmap getBitmapFromCache(String tag){
		if(bitmapCache == null)bitmapCache = new LruCache<>(max/8);
		if(bitmapCache.get(tag)!=null){
			return bitmapCache.get(tag);
		}
		return null;
	}
	
	/**
	 * 保存图片到缓存中
	 * @param tag
	 * @param bmp
	 * Anddward.Liao <Anddward@gmail.com>
	 * 20142014-12-15上午11:15:43
	 */
	private static void saveBitmapWithTag(String tag, Bitmap bmp){
		try {
			
			if(null != bmp)
				bitmapCache.put(tag, bmp);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	/**
	 * 获取图片的tag
	 * @param originTag
	 * @param v
	 * Anddward.Liao <Anddward@gmail.com>
	 * 20142014-12-15下午1:42:30
	 */
	private static String getTagForItem(Object originTag,int width ,int height){
		if(null != originTag)return new StringBuffer().append(width).append("|").append(height).append(originTag.toString()).toString();
		return "";
	}
	
	/**
	 * 剪切图片成圆形，取中间部分
	 * @param bitmap
	 * Anddward.Liao <Anddward@gmail.com>
	 * 20142014-12-13上午9:47:03
	 */
	public static Bitmap cropCircleAvatar(Bitmap bitmap){
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Bitmap output = Bitmap.createBitmap(width,height,Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		Paint paint = new Paint();
		Rect rect = new Rect(0, 0, width, height);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		canvas.drawCircle(width/ 2, height / 2,width/2, paint);
		paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}
	
	/**
	 * 剪切方形的图片,取中间部分
	 * @param bitmap
	 * @return
	 * TODO
	 * Anddward.Liao <Anddward@gmail.com>
	 * 20142014-12-13上午10:11:01
	 */
	public static Bitmap cropSquareBitmap(Bitmap bitmap){
		int squreWidth = bitmap.getWidth() >= bitmap.getHeight()? bitmap.getHeight():bitmap.getWidth();
		int cropX = (bitmap.getWidth() - squreWidth) / 2;
		int cropY = (bitmap.getHeight() - squreWidth) / 2;
		return Bitmap.createBitmap(bitmap, cropX, cropY,squreWidth ,squreWidth );
	}
	
	/**
	 * 把图片压缩到指定的大小,并保存（注意！！不能100%保证压缩到指定大小！！！）
	 * @param name	文件名字
	 * @param limit	文件大小限制
	 * @param step	压缩步进
	 * TODO
	 * Anddward.Liao <Anddward@gmail.com>
	 * 20142014-12-13下午3:24:04
	 */
	public static void compressBitmap(Bitmap bitmap,String fileName,int limit,int step){
			int quelity = 100;					// 初始品质为100
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			bitmap.compress(CompressFormat.PNG, quelity, bout);
			while(bout.toByteArray().length/1024 > limit){
				if(quelity - step < 0)break;
				bout.reset();
				quelity -= step;
				bitmap.compress(CompressFormat.PNG, quelity, bout);
			}
			FileUtilities.saveImage(bout.toByteArray(), fileName);
			
	}
	/**
	 * 根据传入的图片file压缩图片
	 * @param file
	 * @param limit
	 * @param step
	 * @return
	 * Administrator
	 * 2014-12-24下午3:06:50
	 * @throws FileNotFoundException 
	 */
	public static InputStream compressBitmap(File file,int limit,int step) throws FileNotFoundException{
		int quelity = 100;					// 初始品质为100
//		FileInputStream inputStream=new FileInputStream(file);
		Bitmap bitmap=FileUtilities.readImageWithFilePath(file.getPath(), 200, 200);
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, quelity, bout);
		while(bout.toByteArray().length/1024 > limit){
			if(quelity - step < 0)break;
			bout.reset();
			quelity -= step;
			bitmap.compress(CompressFormat.PNG, quelity, bout);
		}
		InputStream inputStream2=new ByteArrayInputStream(bout.toByteArray());
		return inputStream2;
	}
	
	public static void clearImageCache(String name,ImageView v){
		
		String tag = getTagForItem(name, v.getWidth(), v.getHeight());
		if(bitmapCache.get(tag)!=null){
			bitmapCache.remove(tag);
			
		}
	}
//	public static void clear(String name,ImageView v){
//		String tag = getTagForItem(name, v.getWidth(), v.getHeight());
//		if(bitmapCache.size()>70){
//			for(String temp:bitmapCache.keySet()){
//				if(bitmapCache.size()<=70){
//					return ;
//				}
//				bitmapCache.get(temp).get().recycle();
//				bitmapCache.remove(temp);
//			}
//			
//		}
//	}

	public static void loadbitmaps(String path, ImageView img, int i, int j) {
		 BitmapFactory.Options opt = new BitmapFactory.Options();      
		   
		             opt.inPreferredConfig = Bitmap.Config.RGB_565;       
		 
		            opt.inPurgeable = true;      
		  
		            opt.inInputShareable = true;      
		   
		           //获取资源图片       
		   
		          //    InputStream is = context.getResources().openRawResource(resId);      
		  
		             // BitmapFactory.decodeStream(is,null,opt);      

		
	}
	/*public static Bitmap myLoadimage(final String url,int width,int height){
		final URL netUrl;
			try {
				netUrl = new URL(url);
				
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							try {
								FileUtilities.saveImage(netUrl.openConnection().getInputStream(), url+"yangchuying");
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}).start();
				
				
			} catch (MalformedURLException e) {
				System.out.println("eeeeeeeeeeee:"+e.toString());
				e.printStackTrace();
			}
		Bitmap bmp = FileUtilities.readImageWithFileName(url+"yangchuying", width, height);
		return bmp;
	}*/
	/**
	 * 移除之前页面不需要的bitmap，减少内存开销
	 * 2015-1-29
	 * lforxeverc
	 */
	public static void removeBitmaps(){
//		if(bitmapCache == null || removePath == null ) return;
//		LogUtilities.Log("clear", "removepath0 size:"+removePath.size()+" bitmapcache0 size:"+bitmapCache.size(), D.DEBUG_DEBUG);
//		if(removePath!=null&&removePath.size()>0){
//			for(String temp:removePath){
//				Bitmap bm=bitmapCache.get(temp);
//				bitmapCache.remove(temp);
////				if(bm!=null) bm.recycle();
//			}
//			removePath.clear();
//		}
//		LogUtilities.Log("clear", "removepath1 size:"+removePath.size()+" bitmapcache1 size:"+bitmapCache.size(), D.DEBUG_DEBUG);
	}

	
}


