package com.base;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ListView;

import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.adapter.MeEvalutonList;
import com.iteambuysale.zhongtuan.utilities.DBUtilities;

public class EvaluteListBasePager extends Basepager{
	//private Context context;
	//private View rootview;
	//private boolean istemai;
	private Cursor cursor;
	private String phoneNumber;
	private MeEvalutonList adapter;

	public EvaluteListBasePager(Context context) {
		super(context);
	}
	/*public EvaluteListBasePager(Context context) {
		this.context = context;
		rootview = initview();
	}*/
    @Override
	public View initview() {
		View view = View.inflate(context, R.layout.my_evalute_vp_viewl, null);
		ListView listview = (ListView) view.findViewById(R.id.my_evalute_List);
		String[] from = new String[] { "_cpmc", "_dfen", "_recmemo", "_recpic",
				"_dateandtime" };
		int[] to = new int[] { R.id.tv_pro_name, R.id.rb_ratingbar,
				R.id.tv_evalution_detial, R.id.gv_e_gridview,
				R.id.tv_evlute_time };
		phoneNumber = ZhongTuanApp.getInstance().getAppSettings().phoneNumber;
		cursor = DBUtilities.getrecmByuid(phoneNumber,getismai());
		
		adapter = new MeEvalutonList(context, R.layout.listitem_evalution, cursor,
				from, to);
		listview.setAdapter(adapter);
		return view;
	}

	
	@Override
	public void notedata(){
		cursor=DBUtilities.getrecmByuid(phoneNumber, getismai());
		adapter.changeCursor(cursor);
		adapter.notifyDataSetChanged();
	}
}
