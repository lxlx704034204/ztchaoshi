package com.iteambuysale.zhongtuan.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.activity.me.unpay.EvaluateActivity;
import com.iteambuysale.zhongtuan.activity.me.unpay.UnpayActivity;
import com.iteambuysale.zhongtuan.activity.near.pay.PayActivity;
import com.iteambuysale.zhongtuan.activity.specialsale.Paymoney;
import com.iteambuysale.zhongtuan.utilities.ImageUtilities;

public class UnpayListAdapter extends SimpleCursorAdapter {
	Context mContext;
	int[] to;
	String[] from;
	//布局
	private int layout;
	//0为团购状态,1为特卖状态
	private int tabState ;
	//编辑状态（未支付订单页面有效）
	static Boolean editState;
	//已支付为1，待支付为0
	String activityTag;
	private static final String PAYED="1";
	private static final String UNPAY="0";
	Cursor cursor;

	public UnpayListAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to, int flags) {
		super(context, layout, c, from, to, flags);

	}

	public UnpayListAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to, int flags, Boolean editState,int activityTag,int tabState) {
		super(context, layout, c, from, to, flags);

		mContext = context;
		this.to = to;
		this.from=from;
		this.editState=editState;
		this.activityTag=activityTag+""; 
		this.layout=layout;
		this.tabState=tabState;
		cursor=c;

	}
	

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		ViewHolder holder;
		if(view==null){
			view=LayoutInflater.from(mContext).inflate(layout,parent,false);	
			holder=new ViewHolder();
			//待支付页面编辑页面
			if(editState&&activityTag.equals(UNPAY)){
				holder.checkbox=(CheckBox) view.findViewById(R.id.checkbox);	
				holder.checkbox.setVisibility(View.VISIBLE);
				holder.pay=(TextView) view.findViewById(R.id.tv_pay);
				holder.pay.setBackgroundResource(R.drawable.pay_unclick);
				holder.pay.setTag(position);	
				initCell(view, cursor,position);
				}
			//待支付非编辑页面
			else if(!editState&&activityTag.equals(UNPAY)){
				holder.checkbox=(CheckBox) view.findViewById(R.id.checkbox);	
				holder.checkbox.setVisibility(View.GONE);
				//在待支付页面该按钮为支付按钮
				holder.pay=(TextView) view.findViewById(R.id.tv_pay);
				holder.pay.setBackgroundResource(R.drawable.unpay_bg1);
				holder.pay.setTag(position);				
				holder.pay.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						cursor.moveToPosition((int)view.getTag());
						Intent intent;
						if (tabState==0) {
							intent = new Intent(mContext,PayActivity.class);
						}else{
							intent = new Intent(mContext,Paymoney.class);
						}
						intent.putExtra("productname",cursor.getString(cursor.getColumnIndex("_fcpmc")) );
						intent.putExtra("cpje",cursor.getString(cursor.getColumnIndex("_ordje")) );
						intent.putExtra("ordno",cursor.getString(cursor.getColumnIndex("_ordno")) );
						intent.putExtra("sum",Integer.parseInt(cursor.getString(cursor.getColumnIndex("_ordsl"))));
						intent.putExtra("id",cursor.getString(cursor.getColumnIndex("_fcpmid")));
						//intent.putExtra("currentstate", tabState);
						mContext.startActivity(intent);						
					}
				});
				initCell(view, cursor,position);
			}
			//已支付页面
			else if(activityTag.equals(PAYED)){
					holder.time=(TextView) view.findViewById(R.id.tv_time);
					//在已支付页面该按钮为订单状态按钮（当订单状态为已消费或者已收货时可点击）
					holder.evaluateBtn=(Button) view.findViewById(R.id.tv_pay);
					holder.evaluateBtn.setTag(position);
					holder.evaluateBtn.setOnClickListener(new OnClickListener() {					
						@Override
						public void onClick(View view) {
							cursor.moveToPosition((int)view.getTag());
							Intent intent=new Intent(mContext,EvaluateActivity.class);
							 //ordnoid
							intent.putExtra("id",cursor.getString(cursor.getColumnIndex("_id")));
							intent.putExtra("name",cursor.getString(cursor.getColumnIndex("_fcpmc")));
							intent.putExtra("picurl", cursor.getString(cursor.getColumnIndex("_fcppic")));
							intent.putExtra("cost", cursor.getString(cursor.getColumnIndex("_ordje")));
							intent.putExtra("sum", cursor.getString(cursor.getColumnIndex("_ordsl")));
							intent.putExtra("time", cursor.getString(cursor.getColumnIndex("_dateandtime")));
							intent.putExtra("currentstate", tabState);
							mContext.startActivity(intent);
							
						}
					});
					
				}
				holder.pic=(ImageView) view.findViewById(R.id.pic);
				holder.tittle=(TextView) view.findViewById(R.id.tv_tittle);
				holder.price=(TextView) view.findViewById(R.id.tv_price);
				holder.sum=(TextView) view.findViewById(R.id.tv_sum);
				view.setTag(holder);
				initCell(view, cursor,position);
		}
		else{
			holder=(ViewHolder) view.getTag();
			cursor.moveToPosition(position);
			//待支付页面编辑页面
			if(editState&&activityTag.equals(UNPAY)){
				holder.pay.setTag(position);
				holder.checkbox.setVisibility(View.VISIBLE);
				holder.checkbox.setChecked(UnpayActivity.ordList.contains(cursor.getString(cursor.getColumnIndex("_id"))));
				holder.pay.setBackgroundResource(R.drawable.pay_unclick);
				holder.pay.setClickable(false);	
				initCell(view, cursor,position);
			}
			//待支付页面非编辑页面
			else if(!editState&&activityTag.equals(UNPAY)){
				holder.checkbox.setVisibility(View.GONE);
				holder.pay.setTag(position);
				holder.pay.setBackgroundResource(R.drawable.unpay_bg1);
				holder.pay.setClickable(true);
				initCell(view, cursor,position);
			}
			//已支付页面
			else if(activityTag.equals(PAYED)){
				holder.evaluateBtn.setTag(position);
				initCell(view, cursor,position);	
				
			}			
		}				
		return view;
	}
	
	@Override
	public void setViewImage(final ImageView iv, String url) {
		ImageUtilities.loadBitMap(url, iv);
	}

	@Override
	public void setViewText(TextView v, String text) {
		super.setViewText(v, text);
		switch (v.getId()) {
		case R.id.tv_price:
			v.setText(text + "元");
			break;
		case R.id.tv_time:
			v.setText(text.substring(0,11));
			break;
		case R.id.tv_pay:
			if(activityTag.equals(PAYED)){
				if(text.equals("1")){
					v.setText("已支付");
					v.setClickable(false);
					v.setBackgroundResource(R.color.transparent);
					v.setTextColor(mContext.getResources().getColor(R.color.green_pay));
					
				}
				if(text.equals("2")){
					if (tabState==1) {
						v.setText("已收货");
					}else{
						v.setText("已消费");
					}
					v.setClickable(true);
					v.setBackgroundResource(R.drawable.evaluation_bg);
					v.setTextColor(mContext.getResources().getColor(R.color.white));
				

				}

				if(text.equals("3")){
					v.setText("已评价");
					v.setClickable(false);
					v.setBackgroundResource(R.color.transparent);
					v.setTextColor(mContext.getResources().getColor(R.color.green_pay));
				}
				if(text.equals("4")){
					if (tabState==1) {
						v.setText("已发货");
					}else{
						return;
					}
					v.setClickable(true);
					v.setBackgroundResource(R.color.transparent);
					v.setTextColor(mContext.getResources().getColor(R.color.green_pay));
				}
                
				if(text.equals("5")){
					if (tabState==0) {
						v.setText("申请退款中");
					}else{
						v.setText("退款处理中");
					}
					
					v.setClickable(false);
					v.setBackgroundResource(R.color.transparent);
					v.setTextColor(mContext.getResources().getColor(R.color.green_pay));
				}
                if(text.equals("7")){
                	v.setText("拒绝退款");
					v.setClickable(false);
					v.setBackgroundResource(R.color.transparent);
					v.setTextColor(mContext.getResources().getColor(R.color.green_pay));
                }
				if(text.equals("8")){
					v.setText("退款完成");
					v.setClickable(false);
					v.setBackgroundResource(R.color.transparent);
					v.setTextColor(mContext.getResources().getColor(R.color.green_pay));
				}

				if(text.equals("9")){
					v.setText("退款中");
					v.setClickable(false);
					v.setBackgroundResource(R.color.transparent);
					v.setTextColor(mContext.getResources().getColor(R.color.green_pay));
				}			
				
			}
			if(activityTag.equals(UNPAY)){
				v.setText("");
			}
			
			break;

		default:
			break;
		}
	}

	public static class ViewHolder {
		public CheckBox getCheckbox() {
			return checkbox;
		}
		public void setCheckbox(CheckBox checkbox,ArrayList<String> checkMap) {
			this.checkbox = checkbox;
			
		}
		CheckBox checkbox;
		ImageView pic;
		TextView tittle, price, sum, pay,time;
		Button evaluateBtn;
	}

	/**
	 * 初始化item
	 * @param parent
	 * @param cr
	 * @param position
	 * 2015-1-15
	 * lforxeverc
	 */
	private void initCell(View parent,Cursor cr,int position){		
		cr.moveToPosition(position);	
		for (int i=0;i<mTo.length;i++){
			View v = parent.findViewById(mTo[i]);
			String value = cr.getString(cr.getColumnIndex(from[i]));
			if (TextView.class.isInstance(v)){
				setViewText((TextView)v,value);
			}
			if (ImageView.class.isInstance(v)){
				setViewImage((ImageView)v,value);
			}
		}
	}
	/**
	 * 设置编辑按钮的状态
	 * @param state
	 * 2015-1-15
	 * lforxeverc
	 */
	public void setEditable(Boolean editState){
		this.editState=editState;
	}
	
	

}
