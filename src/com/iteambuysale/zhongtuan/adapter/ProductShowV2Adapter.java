package com.iteambuysale.zhongtuan.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.adapter.ImageAdapter.CompressAndSaveTask;
import com.iteambuysale.zhongtuan.listener.TimeToloadDataListener;
import com.iteambuysale.zhongtuan.model.TemaiVerson2;
import com.iteambuysale.zhongtuan.utilities.ImageUtilities;

public class ProductShowV2Adapter extends SimpleCursorAdapter implements OnScrollListener {

	private Context context;
	private LruCache<String, Bitmap> memorycahe;
	private ArrayList<TemaiVerson2 > pathList=new ArrayList<TemaiVerson2>();
	private boolean isfirst=true;
	private List<CompressAndSaveTask> taskList;
	private int mfirstVisibleItem=0;
	private int mvisiblleItemCount=0;
	private ListView listview;
	private TemaiVerson2 temaiVerson2;
	//private DecimalFormat fnum;
	private TimeToloadDataListener listener;
	private boolean flag=true;
	
	public ProductShowV2Adapter(Context context, int layout, Cursor c, String[] from,
			int[] to,ListView listview) {
		super(context, layout, c, from, to);
		this.context=context;
		this.listview=listview;
		
		int size =(int) Runtime.getRuntime().maxMemory();
		memorycahe =new LruCache<String, Bitmap>(size/8){

			@Override
			protected int sizeOf(String key, Bitmap value) {
				// TODO Auto-generated method stub
				return value.getRowBytes()*value.getHeight();
			}
		};
		putValuetolist(c);
		listview.setOnScrollListener(this);
		 //fnum = new  DecimalFormat("##0.0");
	}

	public void putValuetolist(Cursor c) {
		 c.moveToFirst();
		 pathList.clear();
		for(int i=0;i<c.getCount();i++){
			//pathList.add(c.getString(c.getColumnIndex("_picurl")));
            TemaiVerson2 temaiVerson2=new TemaiVerson2();
            temaiVerson2.setPicurl(c.getString(c.getColumnIndex("_picurl")));
            temaiVerson2.setDj0(c.getString(c.getColumnIndex("_dj0")));
            temaiVerson2.setTmdj(c.getString(c.getColumnIndex("_tmdj")));
            temaiVerson2.setSells(c.getString(c.getColumnIndex("_sells")));
            temaiVerson2.setTitle(c.getString(c.getColumnIndex("_title")));
            temaiVerson2.setTmword(c.getString(c.getColumnIndex("_tmword")));
            temaiVerson2.setTmid(c.getString(c.getColumnIndex("_id")));
            pathList.add(temaiVerson2);
			c.moveToNext();
		}
	}
	
	
	@Override
	public View getView(int position, View convertview, ViewGroup parents) {
        ViewHolder holder;
        if(convertview==null){
        	holder = new ViewHolder();	
			convertview = View.inflate(context, R.layout.showproduct_version2, null);
        	holder.iv_product_image=(ImageView) convertview.findViewById(R.id.iv_product_image);
        	holder.tv_product_introduce=(TextView) convertview.findViewById(R.id.tv_product_introduce);
        	holder.tv_discount=(TextView) convertview.findViewById(R.id.tv_discount);
        	holder.tv_before_discount=(TextView) convertview.findViewById(R.id.tv_before_discount);
        	holder.tv_discount_rate =(TextView) convertview.findViewById(R.id.tv_discount_rate);
        	holder.sell_num=(TextView) convertview.findViewById(R.id.tv_sell_num);
        	holder.tv_before_discount.getPaint().setFlags((Paint. STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG));
        	convertview.setTag(holder);
        }else{
        	holder=(ViewHolder) convertview.getTag();
        	holder.tv_before_discount.getPaint().setFlags((Paint. STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG));
        }
        temaiVerson2 = pathList.get(position);
        setViewImage(holder.iv_product_image, temaiVerson2.getPicurl());
        holder.tv_product_introduce.setText(temaiVerson2.getTitle()+temaiVerson2.getTmword());
        float dj0=Float.parseFloat(temaiVerson2.getDj0());
        float tmdj=Float.parseFloat(temaiVerson2.getTmdj());
        float rate=tmdj/dj0;
        rate=(float)(Math.round(rate*100))/10;
        holder.iv_product_image.setTag(temaiVerson2.getPicurl());
        holder.tv_discount_rate.setText(rate+"折");
        holder.tv_before_discount.setText("￥"+dj0+"");
        holder.tv_discount.setText("￥"+tmdj+"");
        holder.sell_num.setText("销量"+temaiVerson2.getSells()+"");
		return convertview;
	}
	@Override
	public void setViewImage(ImageView iv, String path) {
		Bitmap bm=memorycahe.get(path);
		if(bm!=null){
			iv.setImageBitmap(bm);
		}
		else{
			iv.setImageResource(R.drawable.place_holder);
		}
	}
	private static class ViewHolder{
		ImageView iv_product_image;
		TextView tv_product_introduce;
		TextView tv_discount;
		TextView tv_before_discount;
		TextView tv_discount_rate;
		TextView sell_num;
	}
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		switch (scrollState) {
		case SCROLL_STATE_IDLE:
			loadbitmap(mfirstVisibleItem, mvisiblleItemCount);
			break;
		default:
			concelAllTask();
			break;
		}
		
	}
	private void concelAllTask() {
		 ArrayList<AsyncTask<Void, Void, Bitmap>> taskList2 = ImageUtilities.getTaskList();
		 if (taskList2!=null) {
			for(AsyncTask<Void, Void, Bitmap> task:taskList2){
				task.cancel(false);
			}
		}
	}
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		mfirstVisibleItem=firstVisibleItem;
		mvisiblleItemCount=visibleItemCount;
		if(isfirst&&visibleItemCount>0){
			loadbitmap(firstVisibleItem,visibleItemCount);
			isfirst=false;
		}
		if(listener==null){
			return ;
		}
		if((firstVisibleItem+visibleItemCount)==totalItemCount){
			listener.loadNextPageData();
		}
		if(flag){
			View views =listview.getChildAt(mfirstVisibleItem>1?1:mfirstVisibleItem);
			//System.out.println("firstVisibleItem"+firstVisibleItem);
			if(listener!=null&&views!=null){
				listener.getcurrentvisiableItem(mfirstVisibleItem, views.getTop());
			}
		}
	}
	public void setflag(boolean flag){
		this.flag=flag;
	}
	public  int getlocation(){
		 // View view =(View) getItem(mfirstVisibleItem);
	    View view =getView(mfirstVisibleItem, null, listview);
		  //int [] location=new int[2];
		 /* view.getLocationInWindow(location);
		  System.out.println("location[1]"+location[1]);*/
		  view.getTop();
		  System.out.println("view.getTop()"+view.getTop());
		 return  view.getTop();
	}
	public void setLoadDataListener(TimeToloadDataListener listener){
		this.listener=listener;
	}
	private void loadbitmap(int firstVisibleItem, int visibleItemCount) {
		String path;
		for(int i=firstVisibleItem;i<firstVisibleItem+visibleItemCount;i++){
			if (i>=pathList.size()) {
				return ;
			}
		   path=pathList.get(i).getPicurl();
		   
		   Bitmap bitmap = memorycahe.get(path);
		   ImageView image= (ImageView) listview.findViewWithTag(path);
		   if (image!=null) {
			   if (bitmap==null) {
					   //把图片加入到memorycahe，并且设置imageview的图像
					   ImageUtilities.loadBitMap(memorycahe,path,image);
			}else{
				image.setImageBitmap(bitmap);
			}
		}
		  
		}
	}
	
}
