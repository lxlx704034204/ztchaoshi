package com.iteambuysale.zhongtuan.activity.me.waitgoods;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.activity.BaseActivity;
import com.iteambuysale.zhongtuan.activity.me.unpay.EvaluateActivity;
import com.iteambuysale.zhongtuan.background.NetAsync;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.OnRefreshListener;
import com.iteambuysale.zhongtuan.listener.global.NetAsyncListener;
import com.iteambuysale.zhongtuan.model.Model;
import com.iteambuysale.zhongtuan.model.OrderDetailsTM;
import com.iteambuysale.zhongtuan.model.OrderTM;
import com.iteambuysale.zhongtuan.utilities.ImageUtilities;
import com.iteambuysale.zhongtuan.utilities.JsonUtilities;
import com.iteambuysale.zhongtuan.views.CustomProgressDialog;
import com.iteambuysale.zhongtuan.views.RefreshListView;

public class WaitForGoods extends BaseActivity implements OnClickListener,
		NetAsyncListener, OnRefreshListener {
	CustomProgressDialog mProgressDialog;
	private Cursor cursor;
	private Myadapter adapter;
	private SQLiteDatabase db;
	private RefreshListView lv_goodmsg;
	private boolean iswaitgood;
	Context mContext;

	@Override
	protected void onDestroy() {
		super.onDestroy();
		cursor.close();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wait_goods);
		mContext = this;
		iswaitgood = getIntent().getBooleanExtra("iswaitgood", true);
		mProgressDialog = CustomProgressDialog.createDialog(this);
		Button back = (Button) findViewById(R.id.back);
		back.setOnClickListener(this);
		back.setBackgroundResource(R.drawable.special_sale_lady_return_background);
		TextView tv_header_tittle = (TextView) findViewById(R.id.tv_header_tittle);

		if (iswaitgood) {
			tv_header_tittle.setText("待收货");
		} else {
			tv_header_tittle.setText("已收货");
		}
		db = ZhongTuanApp.getInstance().getRDB();
		//loadsendgoodmsg(D.API_MY_GETTMORDER);
		lv_goodmsg = (RefreshListView) findViewById(R.id.lv_goodmsg);
		if (iswaitgood) {
			cursor = db.query("ORDER_TM_LIST", null, "_ordzt=? or _ordzt=? ",
					new String[] { "1", "4" }, null, null, "_id desc");
		} else {
			cursor = db.query("ORDER_TM_LIST", null, "_ordzt=? or _ordzt=? ",
					new String[] { "2", "3" }, null, null,
					"_ordzt asc,_id desc ");// _ordzt asc
		}
		if (cursor != null) {
			adapter = new Myadapter(getApplicationContext(), cursor);
			lv_goodmsg.setAdapter(adapter);
		}
		initRefresh();

		// getWuLiu("9201504033");

	}

	@Override
	protected void onRestart() {
		super.onRestart();
		loadsendgoodmsg(D.API_MY_GETTMORDER);
		/*
		 * if (!iswaitgood) { cursor = db.query("ORDER_TM_LIST", null,
		 * "_ordzt=? or _ordzt=? ", new String[] { "2","3" }, null, null,
		 * "_id desc");
		 * 
		 * }else{ cursor = db.query("ORDER_TM_LIST", null,
		 * "_ordzt=? or _ordzt=? ", new String[] { "1", "4" }, null, null,
		 * "_id desc"); } adapter.changeCursor(cursor);
		 * adapter.notifyDataSetChanged();
		 */
	}

	/**
	 * 初始化下拉刷新的功能
	 */
	private void initRefresh() {
		lv_goodmsg.setEnableLoadingMore(false);
		lv_goodmsg.setEnablePullDownRefresh(true);
		lv_goodmsg.setOnRefreshListener(this);

	}

	private void loadsendgoodmsg(String url) {

		mProgressDialog.show();
		NetAsync task_loadorder = new NetAsync(url, this) {
			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
			}

			/* 处理返回数据 */
			@Override
			public Object processDataInBackground(JsonElement elData) {
				OrderTM.delete(OrderTM.class);
				OrderDetailsTM.delete(OrderDetailsTM.class);
				Type type = new TypeToken<Map<String, OrderTM>>() {
				}.getType();
				Map<String, OrderTM> orderMap = JsonUtilities.parseModelByType(
						elData, type);
				SQLiteDatabase db = ZhongTuanApp.getInstance().getWDB();
				db.beginTransaction();
				for (OrderTM o : orderMap.values()) {
					o.save(db);
					OrderDetailsTM[] odList = o.getCpmx();
					Model.save(odList, db);
				}
				db.setTransactionSuccessful();
				db.endTransaction();
				return null;
			}
		};
		task_loadorder.execute();
	}

	/*
	 * private void getWuLiu(final String orderno){ NetAsync NetWuliu=new
	 * NetAsync(D.API_SPECIAL_GETWULIU,this) {
	 * 
	 * @Override public Object processDataInBackground(JsonElement elData) { //
	 * TODO Auto-generated method stub return null; }
	 * 
	 * @Override public void beforeRequestInBackground(List<NameValuePair>
	 * params) { params.add(new BasicNameValuePair("ordno", orderno));
	 * 
	 * } }; NetWuliu.execute(); }
	 */
	private class Myadapter extends CursorAdapter implements NetAsyncListener {

		private LinearLayout ll_wuliu;

		public Myadapter(Context context, Cursor c) {
			super(context, c);
		}

		@Override
		public void bindView(View view, final Context context,
				final Cursor cursor) {
			final Viewholder holder = (Viewholder) view.getTag();
			final String productName = cursor.getString(cursor
					.getColumnIndex("_fcpmc"));
			final String id = cursor.getString(cursor.getColumnIndex("_id"));
			holder.tv_prodct_title.setText(productName);
			final float allprice = Float.parseFloat(cursor.getString(cursor
					.getColumnIndex("_payje")));
			final int shuliang = Integer.parseInt(cursor.getString(cursor
					.getColumnIndex("_ordsl")));
			final String orderno = cursor.getString(cursor
					.getColumnIndex("_ordno"));
			String orderstatus = cursor.getString(cursor
					.getColumnIndex("_ordzt"));
			final String enddate = cursor.getString(cursor
					.getColumnIndex("_dateandtime"));
			String wuliuName = cursor
					.getString(cursor.getColumnIndex("_logco"));
			final String wuliuid = cursor.getString(cursor
					.getColumnIndex("_logid"));
			String wuliuno = cursor.getString(cursor.getColumnIndex("_logno"));
			if (!TextUtils.isEmpty(wuliuName)) {
				holder.posts_name.setText(wuliuName);
			} else {
				holder.posts_name.setText("物流公司");
			}
			if (!TextUtils.isEmpty(wuliuno)) {
				holder.tv_wuliunum.setText(wuliuno);
			} else {
				holder.tv_wuliunum.setText("");
			}
			if (!TextUtils.isEmpty(wuliuid)) {
				holder.bt_check_wuliu.setEnabled(true);
			} else {
				holder.bt_check_wuliu.setEnabled(false);
				// holder.bt_waitgoods.setEnabled(false);
			}
			if (TextUtils.equals(orderstatus, "1")) {
				holder.tv_item_status.setText("等待卖家发货中");
				
				// holder.bt_waitgoods.setVisibility(View.VISIBLE);
				//ll_wuliu.setVisibility(View.VISIBLE);
			} else if (TextUtils.equals(orderstatus, "2")) {
				holder.tv_item_status.setText("您已确认收货");
				
				// holder.bt_waitgoods.setVisibility(View.VISIBLE);
				//ll_wuliu.setVisibility(View.VISIBLE);
			} else if (TextUtils.equals(orderstatus, "4")) {
				holder.tv_item_status.setText("卖家已发货");
				holder.bt_waitgoods.setEnabled(true);
				holder.bt_waitgoods.setClickable(true);
				// holder.bt_waitgoods.setVisibility(View.VISIBLE);
				//ll_wuliu.setVisibility(View.VISIBLE);
				
			} else if (TextUtils.equals(orderstatus, "3")) {
				holder.tv_item_status.setText("您已评价了该商品");
				// holder.bt_waitgoods.setVisibility(View.GONE);
				holder.bt_waitgoods.setText("已评价");
				//holder.bt_waitgoods.setEnabled(false);
				//holder.bt_waitgoods.setClickable(false);
				//ll_wuliu.setVisibility(view.VISIBLE);

			}
			holder.tv_price_pro.setText(allprice / shuliang + "");
			holder.tv_nownum.setText("数量：" + shuliang);
			holder.tv_xiaoji.setText(allprice + "");
			holder.tv_item_odernun.setText(orderno);
			if (iswaitgood) {
				holder.bt_waitgoods.setText("确认收货");
				holder.bt_waitgoods.setEnabled(true);
				holder.bt_waitgoods.setClickable(true);
			} else {
				if(TextUtils.equals(orderstatus, "3")){
					
					holder.bt_waitgoods.setText("已评价");
					holder.bt_waitgoods.setEnabled(false);
					//holder.bt_waitgoods.setClickable(false);
				}else{
					holder.bt_waitgoods.setText("立即评价");
					holder.bt_waitgoods.setEnabled(true);
					//holder.bt_waitgoods.setClickable(true);
				}
				
			}
			final String url = cursor.getString(cursor
					.getColumnIndex("_fcppic"));
			ImageUtilities.loadBitMap(url, holder.iv_product_show);
			holder.bt_waitgoods.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (iswaitgood) {
						AlertDialog.Builder dialog = new AlertDialog.Builder(
								WaitForGoods.this.mContext);
						dialog.setTitle("提示");
						dialog.setMessage("是否确认收货？");
						dialog.setPositiveButton("确认",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										ensureGetGoods(orderno);

									}
								});
						dialog.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										arg0.dismiss();
									}
								});
						dialog.create().show();
						//
					} else {
						intoEvalution();
						// TODO 进入评价页面
					}
				}

				/**
				 * 进入评论页面
				 */
				private void intoEvalution() {
					Intent intent = new Intent(getApplicationContext(),
							EvaluateActivity.class);
					// ordnoid
					intent.putExtra("id", id + "");
					intent.putExtra("name", productName);
					intent.putExtra("picurl", url);
					intent.putExtra("cost", allprice + "");
					intent.putExtra("sum", shuliang + "");
					intent.putExtra("time", enddate);
					intent.putExtra("currentstate", 1);
					startActivity(intent);
				}
			});

			holder.bt_check_wuliu.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// Toast.makeText(getApplicationContext(), "查看物流",
					// 0).show();
					if (!TextUtils.isEmpty(wuliuid)
							&& holder.bt_check_wuliu.isEnabled()) {
						
						 /* Intent intent = new Intent(context,
						  WuliuDetialActivity.class);
						  intent.putExtra("wuliuid", 40+"");
						  startActivity(intent);*/
						 // 暂时不用

						String url = "http://app.teambuy.com.cn/webc/m/tmlog/id/"
								+ wuliuid;
						Uri uri = Uri.parse(url);
						Intent intent = new Intent();
						intent.setAction("android.intent.action.VIEW");
						intent.setData(uri);
						startActivity(intent);
						// loadwuliumsg();
					}
				}

				private void loadwuliumsg() {
					new Thread() {
						public void run() {
							try {

								HttpClient client = new DefaultHttpClient();
								// http://api.kuaidi100.com/api?id=XXXX&com=tiantian&nu=11111&show=2&muti=1&order=desc
								// HttpGet request = new
								// HttpGet("http://api.kuaidi100.com/api?id=3afb629e942af468&com=yuantong&nu=7622454784&show=0&muti=1&order=desc");
								HttpGet request = new HttpGet(
										"http://app.teambuy.com.cn/webc/m/tmlog/id/40");
								HttpResponse response = client.execute(request);
								if (response.getStatusLine().getStatusCode() == 200) {
									InputStream content = response.getEntity()
											.getContent();
									ByteArrayOutputStream out = new ByteArrayOutputStream();
									byte[] buffer = new byte[1024];
									int len = 0;
									while ((len = content.read(buffer)) != -1) {
										out.write(buffer, 0, len);
									}
									System.out.println("返回码"
											+ response.getStatusLine()
													.getStatusCode());
									System.out.println(out.toString());

								}
							} catch (Exception e) {
								e.printStackTrace();
								e.printStackTrace(java.lang.System.out);
								// System.out.println("exception"+);
							}
						};
					}.start();

				}
			});
			//holder.bt_waitgoods.setEnabled(true);
			holder.bt_waitgoods.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (iswaitgood) {
						AlertDialog.Builder dialog = new AlertDialog.Builder(
								WaitForGoods.this.mContext);
						dialog.setTitle("提示");
						dialog.setMessage("是否确认收货？");
						dialog.setPositiveButton("确认",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										ensureGetGoods(orderno);

									}
								});
						dialog.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										arg0.dismiss();
									}
								});
						dialog.create().show();
						//
					} else {
						intoEvalution();
						// TODO 进入评价页面
					}
				}

				/**
				 * 进入评论页面
				 */
				private void intoEvalution() {
					Intent intent = new Intent(getApplicationContext(),
							EvaluateActivity.class);
					// ordnoid
					intent.putExtra("id", id + "");
					intent.putExtra("name", productName);
					intent.putExtra("picurl", url);
					intent.putExtra("cost", allprice + "");
					intent.putExtra("sum", shuliang + "");
					intent.putExtra("time", enddate);
					intent.putExtra("currentstate", 1);
					startActivity(intent);
				}
			});
		}

		private void ensureGetGoods(final String orderno) {
			NetAsync ensureorder = new NetAsync(
					D.API_SPECIAL_ORDER_ENSURE_GOODS, this) {

				@Override
				public Object processDataInBackground(JsonElement elData) {
					return null;
				}

				@Override
				public void beforeRequestInBackground(List<NameValuePair> params) {
					params.add(new BasicNameValuePair("ordno", orderno));
				}
			};
			ensureorder.execute();
		}

		@Override
		public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
			View view = View.inflate(getApplicationContext(),
					R.layout.watit_good_adapter, null);
			Viewholder holder = new Viewholder();
			holder.tv_prodct_title = (TextView) view
					.findViewById(R.id.tv_prodct_introduce);
			holder.tv_price_pro = (TextView) view
					.findViewById(R.id.tv_price_pro);
			holder.tv_nownum = (TextView) view.findViewById(R.id.tv_nownum);
			holder.tv_xiaoji = (TextView) view.findViewById(R.id.tv_xiaoji1);
			holder.tv_item_odernun = (TextView) view
					.findViewById(R.id.tv_item_odernun);
			holder.iv_product_show = (ImageView) view
					.findViewById(R.id.iv_product_show);
			holder.tv_item_status = (TextView) view
					.findViewById(R.id.tv_item_status);
			holder.bt_waitgoods = (Button) view
					.findViewById(R.id.bt_ensure_getgoods);
			holder.bt_check_wuliu = (Button) view
					.findViewById(R.id.bt_check_wuliu);
			holder.tv_wuliunum = (TextView) view.findViewById(R.id.tv_wuliunum);
			holder.posts_name = (TextView) view.findViewById(R.id.posts_name);

			ll_wuliu = (LinearLayout) view.findViewById(R.id.ll_wuliu);
			/*
			 * if(iswaitgood){ ll_wuliu.setVisibility(View.VISIBLE); }else{
			 * ll_wuliu.setVisibility(View.GONE); }
			 */
			/*
			 * String orderstatus = cursor.getString(cursor
			 * .getColumnIndex("_ordzt")); String orderno =
			 * cursor.getString(cursor .getColumnIndex("_ordno")); if
			 * (TextUtils.equals(orderstatus, "1")) {
			 * holder.tv_item_status.setText("等待卖家发货中");
			 * holder.bt_waitgoods.setVisibility(View.VISIBLE); } else if
			 * (TextUtils.equals(orderstatus, "2")) {
			 * holder.tv_item_status.setText("您已确认收货");
			 * holder.bt_waitgoods.setVisibility(View.VISIBLE); } else
			 * if(TextUtils.equals(orderstatus, "4")){
			 * holder.tv_item_status.setText("卖家已发货");
			 * holder.bt_waitgoods.setVisibility(View.VISIBLE); }else
			 * if(TextUtils.equals(orderstatus, "3")){
			 * holder.tv_item_status.setText("您已评价了该商品");
			 * holder.bt_waitgoods.setVisibility(View.GONE); }
			 */
			view.findViewById(R.id.rl_header).getBackground().setAlpha(10);
			view.setTag(holder);

			return view;
		}

		@Override
		public void onResultError(String reqUrl, String errMsg) {
			Toast.makeText(getApplicationContext(), errMsg, Toast.LENGTH_SHORT)
					.show();
		}

		@Override
		public void onResultSuccess(String reqUrl, Object data) {
			/*
			 * cursor = db.query("ORDER_LIST", null, "_ordzt =? or _ordzt =?",
			 * new String[] { "1","4" }, null, null, "_id desc");
			 * adapter.changeCursor(cursor); adapter.notifyDataSetChanged();
			 */
			// loadsendgoodmsg(D.API_MY_GETTMORDER);
			Intent intent_have = new Intent(getApplicationContext(),
					WaitForGoods.class);
			intent_have.putExtra("iswaitgood", false);
			//intent_have.putExtra("", value)
			startActivity(intent_have);

		}

		@Override
		public void onTokenTimeout() {
			mProgressDialog.dismiss();
			ZhongTuanApp.getInstance().logout(WaitForGoods.this,true);
			finish();
		}
	}

	private static class Viewholder {
		TextView tv_prodct_title;
		TextView tv_price_pro;
		TextView tv_nownum;
		TextView tv_xiaoji;
		TextView tv_item_odernun;
		TextView tv_item_status;
		TextView posts_name;
		TextView tv_wuliunum;
		Button bt_waitgoods, bt_check_wuliu;
		ImageView iv_product_show;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;

		default:
			break;
		}

	}

	@Override
	public void onResultError(String reqUrl, String errMsg) {
		mProgressDialog.dismiss();
		Toast.makeText(getApplicationContext(), errMsg, Toast.LENGTH_SHORT)
				.show();

	}

	@Override
	public void onResultSuccess(String reqUrl, Object data) {
		switch (reqUrl) {
		case D.API_MY_GETTMORDER:
			mProgressDialog.dismiss();
			lv_goodmsg.onRefreshFinish();
			if (iswaitgood) {
				cursor = db.query("ORDER_TM_LIST", null,
						"_ordzt =? or _ordzt =?", new String[] { "1", "4" },
						null, null, "_id desc");
			} else {
				cursor = db.query("ORDER_TM_LIST", null,
						"_ordzt =? or _ordzt =?", new String[] { "2", "3" },
						null, null, "_ordzt asc,_id desc");
			}
			adapter.changeCursor(cursor);
			adapter.notifyDataSetChanged();
			break;
		case D.API_SPECIAL_GETWULIU:

		default:
			break;
		}

	}

	@Override
	public void onTokenTimeout() {
		mProgressDialog.dismiss();
		ZhongTuanApp.getInstance().logout(WaitForGoods.this,true);
		finish();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void OnPullDownRefresh() {
		//Model.delete(OrderTM.class);
		loadsendgoodmsg(D.API_MY_GETTMORDER);
	}

	@Override
	public void onLoadingMore() {

	}
}
