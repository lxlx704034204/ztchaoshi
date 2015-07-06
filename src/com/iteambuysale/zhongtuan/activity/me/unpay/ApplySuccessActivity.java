package com.iteambuysale.zhongtuan.activity.me.unpay;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.activity.BaseActivity;

public class ApplySuccessActivity extends BaseActivity implements OnClickListener {
	TextView tittle, tv_price, tv_taocan, tv_ordno, tv_time, tv_mobile, tv_sum,
			tv_all, tv_qid;
	Button back, detailbtn;
	String id,tag,ordno,qid,taocan,payTime,mobile,allPrice,productId,shopId,edate,ztq;
	int sum;
	String[] list; 
	SQLiteDatabase db=ZhongTuanApp.getInstance().getRDB();
	private int currentstate;
	private Cursor cursor;
	@Override
	protected void onDestroy() {
		if(cursor!=null){
			cursor.close();
		}		
		super.onDestroy();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.refund_success);
		Intent intent=getIntent();
		ordno=intent.getStringExtra("ordno");
		qid=intent.getStringExtra("qid");
		allPrice=intent.getStringExtra("price");
		currentstate=intent.getIntExtra("currentstate", -1);
		try {
			sum=qid.split(",").length;
			list=qid.split(",");
		} catch (Exception e) {
			
		}
		
		tittle = (TextView) findViewById(R.id.tv_header_tittle);
		tv_taocan = (TextView) findViewById(R.id.tv_tc);
		tv_ordno = (TextView) findViewById(R.id.tv_ordno);
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_mobile = (TextView) findViewById(R.id.tv_mobile);
		tv_sum = (TextView) findViewById(R.id.tv_sum);
		tv_all = (TextView) findViewById(R.id.tv_all);
		tv_qid = (TextView) findViewById(R.id.tv_qid);
		back = (Button) findViewById(R.id.back);
		detailbtn = (Button) findViewById(R.id.detailbtn);

		tittle.setText("订单详情");
		back.setBackgroundResource(R.drawable.header_back);
		if (currentstate==0) {//0为团购1为特卖
			loadTuanGouMsg();
		}else{
			tv_qid.setText("订单号："+ordno);
			cursor = db.query("ORDER_TM_LIST", null, "_ordno=?",
					new String[] { ordno }, null, null, null);
			cursor.moveToFirst();
			payTime=cursor.getString(cursor.getColumnIndex("_dateandtime"));
			mobile=cursor.getString(cursor.getColumnIndex("_tel"));
			productId=cursor.getString(cursor.getColumnIndex("_fcpmid"));
			sum=Integer.parseInt(cursor.getString(cursor.getColumnIndex("_ordsl")));
			allPrice=cursor.getString(cursor.getColumnIndex("_ordje"));
			
			/*Cursor productCursor=db.query("PRODUCT_LIST", null, "_id=?", new String[]{productId}, null, null,null);
			productCursor.moveToFirst();
			taocan=productCursor.getString(productCursor.getColumnIndex("_cpmemo"));
			
			tv_taocan.setText(taocan);*/
			tv_ordno.setText(ordno);
			tv_time.setText(payTime);
			tv_mobile.setText(mobile);
			tv_sum.setText(sum+"");
			tv_all.setText(allPrice);
		}
	
		
		
	}

	private void loadTuanGouMsg() {
		String where="_id in (";
		if (currentstate!=0) {
			
		}
		for(int i=0;i<list.length;i++){
			
			if(i!=list.length-1){
				where=where+" ? "+",";
			}
			else{
				where=where+" ? "+")";
			}
			
			
		}
		qid="";
		Cursor ztqCursor=ZhongTuanApp.getInstance().getRDB().query("ZTQ_LIST", null, where,list,null,null,null);
		ztqCursor.moveToFirst();
		for(int i=0;i<ztqCursor.getCount();i++){
				qid=qid+ztqCursor.getString(ztqCursor.getColumnIndex("_qno"))+"\n";	
				ztqCursor.moveToNext();
		}
		tv_qid.setText(qid);
		tv_ordno.setText(ordno);
		back.setOnClickListener(this);
		detailbtn.setOnClickListener(this);
		
		SQLiteDatabase db=ZhongTuanApp.getInstance().getRDB();
		Cursor cursor=db.query("ORDER_TG_LIST", null, "_ordno=?", new String[]{ordno}, null, null,null);
		cursor.moveToFirst();
		payTime=cursor.getString(cursor.getColumnIndex("_dateandtime"));
		mobile=cursor.getString(cursor.getColumnIndex("_tel"));
		productId=cursor.getString(cursor.getColumnIndex("_fcpmid"));
		Cursor productCursor=db.query("PRODUCT_LIST", null, "_id=?", new String[]{productId}, null, null,null);
		productCursor.moveToFirst();
		taocan=productCursor.getString(productCursor.getColumnIndex("_cpmemo"));
		
		tv_qid.setText(qid);
		tv_taocan.setText(taocan);
		tv_ordno.setText(ordno);
		tv_time.setText(payTime);
		tv_mobile.setText(mobile);
		tv_sum.setText(sum+"");
		tv_all.setText(allPrice);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.detailbtn:
			Intent intent=new Intent(ApplySuccessActivity.this,PicAndWordActivity.class);
			intent.putExtra("tag", "0");
			startActivity(intent);
			break;

		default:
			break;
		}

	}
	@Override
	protected void onPause() {
		super.onPause();
	}
	@Override
	protected void onResume() {
		super.onResume();
	}

}
