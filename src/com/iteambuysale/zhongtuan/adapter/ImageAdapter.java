package com.iteambuysale.zhongtuan.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.AsyncTask;
import android.provider.MediaStore.Images.Media;
import android.support.v4.util.LruCache;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;

import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.activity.me.AlbumActivity;

public class ImageAdapter extends SimpleCursorAdapter implements OnScrollListener{
	Context mContext;
	ViewHolder viewHolder;
	Boolean state=false;
	Boolean isFirstEnter=true;
	LruCache<String,Bitmap> memoryCache;
	List<String> pathList; 
	List<CompressAndSaveTask> taskList;
	Cursor c;
	GridView gridview;
	int mFirst,mCount;

	public ImageAdapter(Context context, int layout, Cursor c, String[] from,
			int[] to, int flags,GridView gridview) {
		super(context, layout, c, from, to, flags);
		mContext=context;
		this.c=c;
		this.gridview=gridview;
		pathList=new ArrayList<>();
		taskList=new ArrayList<ImageAdapter.CompressAndSaveTask>();
		
		int max=(int) Runtime.getRuntime().maxMemory();

		memoryCache=new LruCache<String,Bitmap>(max/8){
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {			
				return bitmap.getRowBytes() * bitmap.getHeight();
			}};
		
		this.c.moveToFirst();
		for(int i=0;i<this.c.getCount();i++){
			pathList.add(this.c.getString(c.getColumnIndex(Media.DATA)));
			this.c.moveToNext();
		}
		
		this.gridview.setOnScrollListener(this);		
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		c.moveToPosition(position);
		String id=c.getString(c.getColumnIndex("_id"));

		if(convertView==null){
			LayoutInflater inflater=LayoutInflater.from(mContext);
			convertView=inflater.inflate(R.layout.image, parent,false);
			viewHolder=new ViewHolder();
			viewHolder.img=(ImageView) convertView.findViewById(R.id.img);
			viewHolder.img.setDrawingCacheEnabled(true);
			viewHolder.cb=(CheckBox) convertView.findViewById(R.id.checkbox);
			viewHolder.cb.setVisibility(state?View.VISIBLE:View.GONE);			
			viewHolder.cb.setChecked(AlbumActivity.list.contains(id));
			convertView.setTag(viewHolder);
		}
		else{
			viewHolder=(ViewHolder) convertView.getTag();
			viewHolder.cb.setChecked(AlbumActivity.list.contains(id));
		}
		//构造函数处初始化了图片路径列表
		String path=pathList.get(position);
		viewHolder.img.setTag(path);
		setViewImage(viewHolder.img, path);
		return convertView;
	}
	@Override
	public void setViewImage(ImageView iv, String path) {
		Bitmap bm=memoryCache.get(path);
		if(bm!=null){
			iv.setImageBitmap(bm);
		}
		else{
			iv.setImageResource(R.drawable.place_holder);
		}
	}
	public class ViewHolder{
		public ImageView img;
		public CheckBox cb;
		public CheckBox getCb(){
			return cb;
		}
	}
	/**
	 * 设置checkbox的是否可见(评价页面重用此Adapter)
	 * @param state true 可见 false 不可见
	 */
	public void setCheboxVisibility(Boolean state){
		this.state=state;
	}
	/**
	 * 异步压缩图片并加载图片
	 * @author lforxeverc
	 *
	 */
	public class CompressAndSaveTask extends AsyncTask<String, Void, Bitmap>{
		
		String tag;
		@Override
		protected void onPostExecute(Bitmap result) {
			//前面getView()设置了每个Imageview的Tag
			ImageView iv=(ImageView) gridview.findViewWithTag(tag);
			if(iv!=null){
			iv.setImageBitmap(result);
			taskList.remove(this);
			}
			super.onPostExecute(result);
		}
		@Override
		protected Bitmap doInBackground(String... path) {		
			tag=path[0];
			Bitmap bitmap=compressBitmap(path[0]);
			if(bitmap!=null&&path[0]!=null){
				memoryCache.put(path[0], bitmap);
			}			
			return bitmap;
		}
	}
	/**
	 * 根据给出的图片路径压缩图片
	 * @param path
	 * @return
	 */
	public Bitmap compressBitmap(String path){
		Bitmap bm = null;
		File file=new File(path);
		if(!file.exists()){
			return null;
		}
		else{		
			int size=1;
			BitmapFactory.Options opts=new Options();
			opts.inJustDecodeBounds=true;
			BitmapFactory.decodeFile(path, opts);
			opts.inJustDecodeBounds=false;
			int width= opts.outWidth;
			int hight=opts.outHeight;
			if(width>150&&hight>150){
				size=(width/150>hight/150?width/150:hight/150);
			}
			opts.inSampleSize=size;
			bm=BitmapFactory.decodeFile(path, opts);	
		}
		return bm;
	}
	/**
	 * 加载图片，先从缓存里面找，如果有则直接取出，没有则新起异步任务加载
	 * @param first 第一个可见的位置
	 * @param count 页面总共可见的数目
	 */
	public void loadBitmaps(int first,int count){
		for(int i=first;i<first+count;i++){
			String path=pathList.get(i);
			Bitmap bm=memoryCache.get(path);
			if(bm==null){
				CompressAndSaveTask task=new CompressAndSaveTask();
			    taskList.add(task);
				task.execute(path);
			}
			else{
				ImageView iv=(ImageView) gridview.findViewWithTag(path);
				if(iv!=null&&bm!=null){
					iv.setImageBitmap(bm);
				}				
			}
		}
	}
	/**
	 * 取消所有加载任务
	 */
	public void cancelAllTask(){
		if(taskList !=null){
			for(CompressAndSaveTask task:taskList){
				task.cancel(false);
			}
		}
		
	}

	@Override
	public void onScroll(AbsListView view, int first, int count, int all) {
		mFirst=first;
		mCount=count;
		//第一次进入加载图片
		if(isFirstEnter&&count>0){
			loadBitmaps(first, count);
			isFirstEnter=false;
		}
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int state) {
		//当列表不滚动时加载图片
		if(state==SCROLL_STATE_IDLE){
			loadBitmaps(mFirst, mCount);
		}
		//列表在其它状态时不加载图片
		else{
			cancelAllTask();
		}
		
	}
	



}
