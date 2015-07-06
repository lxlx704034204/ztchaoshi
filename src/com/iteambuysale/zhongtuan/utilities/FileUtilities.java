package com.iteambuysale.zhongtuan.utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Environment;

import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.define.D;

public class FileUtilities {

	/**
	 * 通过资源id获取图片
	 * @param rId
	 * @param width
	 * @param height
	 * Anddward.Liao <Anddward@gmail.com>
	 * 20142014-12-15下午2:31:57
	 */
	public static Bitmap readImageWithResourceId(int rId,int width,int height) {
		Resources res = ZhongTuanApp.getInstance().getResources();
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inDither=false;
		opts.inTempStorage=new byte[12*1024];
		//opts.inPreferredConfig = Bitmap.Config.RGB_565;
		opts.inPurgeable = true;
		opts.inJustDecodeBounds = true;
		//BitmapFactory.decodeResource(res, rId, opts);
		opts.inSampleSize = calculateSampleSize(opts, width, height);
		//System.out.println("opts.inSampleSize:"+opts.inSampleSize);
		opts.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, rId, opts);
	}

	/**
	 * 通过文件名获取图片，默认文件目录在Teambuy/image_cache
	 * @param fileName
	 * @param width
	 * @param height
	 * @throws IOException 
	 */
	public static Bitmap readImageWithFileName(String fileName, int width, int height){
		String filePath = FileUtilities.getImageFilePath(fileName);
		//System.out.println("fileName:"+fileName);
		return readImageWithFilePath(filePath, width, height);
	}
	
	public static Bitmap readImageWithFilePath(String path, int width, int height){
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inDither=false;
		opts.inTempStorage=new byte[12*1024];
		opts.inPreferredConfig = Bitmap.Config.RGB_565;
		opts.inPurgeable = true;
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, opts);
		opts.inSampleSize = calculateSampleSize(opts, width, height);
		//System.out.println("path:"+path+"opts.inSampleSize:"+opts.inSampleSize);
		opts.inJustDecodeBounds = false;
		Bitmap b=null;
		try {
			
			b = BitmapFactory.decodeFile(path, opts);
		} catch (Exception e) {
			//System.out.println("内存溢出");
		}
		return b;
	}

	/**
	 * 检查图片是否存在 /Teambuy/image_cache
	 * 
	 * @param context
	 * @param imageName
	 * @author Anddward.Liao <Anddward@gmail.com>
	 */
	public static boolean isImageExists(String imageName) {
		File file = new File(getImagePath() + imageName);
		if (file.exists()) {
			return true;
		}
		return false;
	}

	/**
	 * 获取图片存储地址
	 * @param context
	 * @author Anddward.Liao <Anddward@gmail.com>
	 */
	public static String getImagePath() {
		return getFilePath(ZhongTuanApp.getInstance(), D.IMAGE_CACHE);
	}
	
	/**
	 * 获取图片路径,路径在 /Teambuy/image_cache 下
	 * @param name
	 * @return
	 * TODO
	 * Anddward.Liao <Anddward@gmail.com>
	 * 20142014-12-13下午4:55:44
	 */
	public static String getImageFilePath(String name){
		return getImagePath()+name;
	}
	
	/**
	 * 获取图片file
	 * @param name
	 * @return
	 * TODO
	 * Anddward.Liao <Anddward@gmail.com>
	 * 20142014-12-13下午4:55:31
	 */
	public static File getImageFile(String name){
		return new File(getImageFilePath(name));
	}

	/**
	 * 获取文件绝对路径，如果文件夹不存在，则会尝试创建。
	 * @param context
	 * @param subPath 子文件名
	 * @author Anddward.Liao <Anddward@gmail.com>
	 */
	public static String getFilePath(Context context, String subPath) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File fileDir = new File(Environment.getExternalStorageDirectory()
					.getAbsoluteFile() + subPath);
			fileDir.mkdirs();
			return Environment.getExternalStorageDirectory().getAbsolutePath()
					+ subPath;
		} else {
			File fileDir = new File(context.getFilesDir().getAbsoluteFile()
					+ subPath);
			fileDir.mkdirs();
			return context.getFilesDir().getAbsolutePath() + subPath;
		}
	}
 
	
	/**
	 * 保存图片到flash
	 * @param bitmap
	 * @param name 路径名称
	 * @author Anddward.Liao <Anddward@gmail.com>
	 */
	public static void saveImage(InputStream in, String name) {
		File dir = new File(getImagePath());
		try {
			File target = new File(dir, name);
			FileOutputStream ot = new FileOutputStream(target);
			byte[] buffer = new byte[5 * 1024];
			int position = 0;
			while (-1 != (position = in.read(buffer))) {
				ot.write(buffer, 0, position);
			}
			ot.flush();
			ot.close();
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 保存图片
	 * @param bitmap	图片	
	 * @param name		图片名称
	 * TODO
	 * Anddward.Liao <Anddward@gmail.com>
	 * 20142014-12-13下午3:43:21
	 */
	public static void saveImage(Bitmap bitmap,String name){
		if(!FileUtilities.isImageExists(name))return;
		File target = FileUtilities.getImageFile(name);
		FileOutputStream ot;
		try {
			ot = new FileOutputStream(target);
			bitmap.compress(CompressFormat.PNG, 100, ot);
			ot.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 保存图片
	 * @param data
	 * @param name
	 * TODO
	 * Anddward.Liao <Anddward@gmail.com>
	 * 20142014-12-13下午3:38:16
	 */
	public static void saveImage(byte[] data,String name){
		File dir = new File(getImagePath());
			File target = new File(dir,name);
			FileOutputStream ot;
			try {
				ot = new FileOutputStream(target);
				ot.write(data);
				ot.flush();
				ot.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
/*
	public static Bitmap compressBitmap(Resources res ,int id,int reqWidth, int reqHeight){
		Bitmap bm = null;
				
			int size=1;
			BitmapFactory.Options opts=new Options();
			opts.inJustDecodeBounds=true;
			BitmapFactory.decodeResource(res, id, opts);
			opts.inJustDecodeBounds=false;
			int width= opts.outWidth;
			int hight=opts.outHeight;
			if(width>reqWidth&&hight>reqHeight){
				size=(width/reqWidth>hight/reqHeight?width/reqWidth:hight/reqHeight);
			}
			opts.inSampleSize=size;
			bm=BitmapFactory.decodeResource(res, id, opts)	;
		
		return bm;
	}*/
	/**
	 * 计算图片缩放比例
	 * @param opts
	 * @param reqWidth
	 * @param reqHeight
	 * @return 缩放比
	 */
	public static int calculateSampleSize(BitmapFactory.Options opts,
			int reqWidth, int reqHeight) {
		if(reqWidth <= 0 || reqHeight <= 0){
			LogUtilities.Log(D.DEBUG, "image view measure failed! return 2",D.DEBUG_DEBUG);
			//System.out.println("from defult");
			return 2;
			
		}
		int inSampleSize = 1;
		if (opts.outWidth > reqWidth || opts.outHeight > reqHeight) {
			if (opts.outWidth > opts.outHeight) {
				inSampleSize = Math.round((float) opts.outHeight/ (float) reqHeight);
			//	System.out.println("");
			} else {
				inSampleSize = Math.round((float) opts.outWidth/ (float) reqWidth);
			}
		}
		LogUtilities.Log(D.DEBUG, "sampleSize:"+inSampleSize,D.DEBUG_DEBUG);
		/*LogUtilities.Log(D.DEBUG, "opts.outHeight:"+opts.outHeight,D.DEBUG_DEBUG);
		LogUtilities.Log(D.DEBUG, "reqHeight:"+reqHeight,D.DEBUG_DEBUG);
		LogUtilities.Log(D.DEBUG, "opts.outWidth:"+opts.outWidth,D.DEBUG_DEBUG);
		LogUtilities.Log(D.DEBUG, " reqWidth:"+ reqWidth,D.DEBUG_DEBUG);*/
		//System.out.println("inSampleSize:"+inSampleSize);
		return inSampleSize;
	}

	/**
	 * 删除图片文件夹
	 * @param context
	 */
	public static void deleteImageCachePath(Context context) {
		File f = new File(getFilePath(context, D.IMAGE_CACHE));
		deleteFile(f);
	}
	
	/**
	 * 删除指定的本地图片
	 * @param name
	 * Anddward.Liao <Anddward@gmail.com>
	 * 20142014-12-16下午3:05:54
	 */
	public static void deleteImageFile(String name){
		File f = getImageFile(name);
		deleteFile(f);
	}

	/**
	 * 删除文件/文件夹
	 * @param file
	 */
	public static void deleteFile(File file) {
		if (file.isDirectory()) {
			if (file.list().length == 0) {
				// 目录已经空，删除
				file.delete();
			} else {
				// 递归删除
				String[] fileNames = file.list();
				for (String subFileName : fileNames) {
					File subFile = new File(file, subFileName);
					deleteFile(subFile);
				}
				// 递归完成，再次检查自身是否已经空，删除自己
				if (file.list().length == 0)
					file.delete();
			}
		} else {
			// 文件，直接删除
			file.delete();
		}
	}
}
