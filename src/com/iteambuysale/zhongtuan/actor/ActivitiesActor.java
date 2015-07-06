package com.iteambuysale.zhongtuan.actor;

import java.lang.reflect.Type;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ListView;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.adapter.ActivitiesListAdapter;
import com.iteambuysale.zhongtuan.background.NetAsync;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.ActivitiesListener;
import com.iteambuysale.zhongtuan.model.Activities;
import com.iteambuysale.zhongtuan.model.Model;
import com.iteambuysale.zhongtuan.model.Tuanbohui;
import com.iteambuysale.zhongtuan.utilities.DBUtilities;
import com.iteambuysale.zhongtuan.utilities.JsonUtilities;

public class ActivitiesActor extends SuperActor {
	Context mContext;
	ActivitiesListener mListener;
	ActivitiesListAdapter adapter;
	ListView listView;
	
	public ActivitiesActor(Context context,ActivitiesListener listener){
		super(context);
		mListener = listener;
		mContext = context;
	}
	
	public void initTGHView(ListView list,View headerView){
		String[] from = new String[] { D.DB_EVENT_PICURL, D.DB_EVENT_TITLE, D.DB_EVENT_TTIME,D.DB_EVENT_BMTEL,D.DB_EVENT_BMQQ,D.DB_EVENT_ADDRESS };
		int[] to = new int[] { R.id.iv_event_pic,R.id.tv_event_title,R.id.tv_event_time,R.id.tv_event_phone,R.id.tv_event_qq,R.id.tv_event_address};
		Cursor cr = DBUtilities.getEventList();
		adapter = new ActivitiesListAdapter(mContext, cr, from, to, mListener);
		if(headerView!=null){
			list.addHeaderView(headerView);
			
		}
		list.setAdapter(adapter);
		list.setOnItemClickListener(mListener);
//		$lv("list").setAdapter(adapter);
//		$lv("list").setOnItemClickListener(mListener);
	}

	public void loadEvent(final int page) {
		NetAsync task_loadEvent = new NetAsync(D.API_CPMX_GETHUODONG,mListener) {
			
			@Override
			public Object processDataInBackground(JsonElement elData) {
				Type type = new TypeToken<Activities[]>(){}.getType();
				Activities[] activitiesList = JsonUtilities.parseModelByType(elData, type);
				Model.delete(Activities.class);
				Model.save(activitiesList);
				return activitiesList;
			}
			
			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				params.add(new BasicNameValuePair(D.ARG_AC_AC, ZhongTuanApp.getInstance().getCityCode()));
				params.add(new BasicNameValuePair(D.ARG_TM_PAGE, page+""));				
			}
		};
		task_loadEvent.execute();
	}
	
	// 刷新列表
	public void updateList(boolean isReloadData) {
		if (isReloadData) {
			if(adapter==null) return;
			Cursor oldCr = adapter.getCursor();
			Cursor newCr = DBUtilities.getEventList();
			adapter.changeCursor(newCr);
			oldCr.close();
			
		}
		adapter.notifyDataSetChanged();
	}
	
	public void updateTBHList(boolean isReloadData) {
		if (isReloadData) {
			Cursor oldCr = adapter.getCursor();
			Cursor newCr = DBUtilities.getTBHList();
			adapter.changeCursor(newCr);
			oldCr.close();
		}
		adapter.notifyDataSetChanged();
	}
	
	public void loadTuanbohui() {
		NetAsync task_loadEvent = new NetAsync(D.API_CPMX_GETTBH,mListener) {
			
			@Override
			public Object processDataInBackground(JsonElement elData) {
				Type type = new TypeToken<Tuanbohui[]>(){}.getType();
				Tuanbohui[] tuanbohuiList = JsonUtilities.parseModelByType(elData, type);
				Model.delete(Tuanbohui.class);
				Model.save(tuanbohuiList);
				return tuanbohuiList;
			}
			
			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				params.add(new BasicNameValuePair(D.ARG_AC_AC, ZhongTuanApp.getInstance().getCityCode()));
				params.add(new BasicNameValuePair(D.ARG_TM_PAGE, "0"));			
			}
		};
		task_loadEvent.execute();
	}
	public void initTBHView(ListView list){
		String[] from = new String[] { D.DB_EVENT_PICLARGE, D.DB_EVENT_EXNAME, D.DB_EVENT_TTIME,D.DB_EVENT_BMTEL,D.DB_EVENT_BMQQ,D.DB_EVENT_ADDRESS};
		int[] to = new int[] { R.id.iv_event_pic,R.id.tv_event_title,R.id.tv_event_time,R.id.tv_event_phone,R.id.tv_event_qq,R.id.tv_event_address};
		Cursor cr = DBUtilities.getTBHList();
		adapter = new ActivitiesListAdapter(mContext, cr, from, to, mListener);
		list.setAdapter(adapter);
		list.setOnItemClickListener(mListener);
//		$lv("list").setAdapter(adapter);
//		$lv("list").setOnItemClickListener(mListener);
	}
}
