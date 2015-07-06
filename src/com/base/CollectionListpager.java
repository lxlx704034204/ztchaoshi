package com.base;

import java.lang.reflect.Type;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.activity.specialsale.ProductActivity;
import com.iteambuysale.zhongtuan.adapter.CollectionAdapter;
import com.iteambuysale.zhongtuan.background.NetAsync;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.global.NetAsyncListener;
import com.iteambuysale.zhongtuan.model.Collection;
import com.iteambuysale.zhongtuan.model.Model;
import com.iteambuysale.zhongtuan.utilities.DBUtilities;
import com.iteambuysale.zhongtuan.utilities.JsonUtilities;

public class CollectionListpager extends Basepager implements
		OnItemClickListener, OnItemLongClickListener, NetAsyncListener {
	private static final String TUANGOU = "cpmx-cp";
	private static final String TEMAI = "cpmx-tm";
	private String[] from = new String[] { "_picurl", "_title", "_memo", "_dj" };;
	private int[] to = new int[] { R.id.img_product, R.id.tv_product_tittle,
			R.id.webview_product_detail1, R.id.tv_product_price };
	private String nowStatus;
	// private static SQLiteDatabase db;
	private CollectionAdapter adapter;
	private ListView listview;
	private SQLiteDatabase db;

	public CollectionListpager(Context context) {
		super(context);
	}

	public CollectionListpager(Context context, boolean istemai) {
		super(context);
		if (istemai) {
			this.nowStatus = TEMAI;
		} else {
			this.nowStatus = TUANGOU;
		}
		setListView();
		loadCollections();
		listview.setOnItemClickListener(this);
		listview.setOnItemLongClickListener(this);
	}

	@Override
	public View initview() {
		View view = View.inflate(context, R.layout.my_evalute_vp_viewl, null);
		listview = (ListView) view.findViewById(R.id.my_evalute_List);
		db = ZhongTuanApp.getInstance().getRDB();
		return view;
	}

	/**
	 * 设置收藏列表ListView的适配器和数据，如果数据为空则隐藏listview
	 * 
	 * 2015-1-14 lforxeverc
	 */
	public void setListView() {

		Cursor c = db.query("COLLECTION_LIST", null, new String("_uflb = ?"),
				new String[] { nowStatus }, null, null, null);
		// cursor = c;

		if (c.getCount() != 0) {
			// listview.setVisibility(View.VISIBLE);
			if (adapter == null) {
				adapter = new CollectionAdapter(context,
						R.layout.listitem_collection, c, from, to, 0);
				listview.setAdapter(adapter);
			} else {
				adapter.changeCursor(c);
				adapter.notifyDataSetChanged();
			}
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

		Cursor c = db.query("COLLECTION_LIST", null, "_id=?",
				new String[] { arg3 + "" }, null, null, null);
		if (c.getCount() != 0) {
			c.moveToFirst();
			String productId = c.getString(c.getColumnIndex("_lbid"));
			Intent intent = null;
			intent = null;
			if (TextUtils.equals(nowStatus, TUANGOU)) {
				//intent = new Intent(context, NearProductActivity.class);
			} else {
				intent = new Intent(context, ProductActivity.class);
			}
			intent.putExtra("productId", productId);
			context.startActivity(intent);
		} else {
			Toast.makeText(context, "该商品不存在！", Toast.LENGTH_SHORT).show();
		}
		c.close();

	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, final long id) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setMessage("是否取消该收藏？");

		builder.setTitle("提示");

		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				String lbid = DBUtilities.getCollectionLbid(id + "");
				DBUtilities.deletecollection(id + "");
				System.out.println("lbid" + lbid);
				// concelCollection(lbid);
				concelCollection(lbid);
				setListView();
				dialog.dismiss();
			}
		});

		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		builder.create().show();
		return true;
	}

	private void concelCollection(final String lbid) {
		NetAsync concelNetAsync = new NetAsync(D.API_CANCEL_COLLECT, this) {

			@Override
			public Object processDataInBackground(JsonElement elData) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				params.add(new BasicNameValuePair("uflb", nowStatus));
				params.add(new BasicNameValuePair("lbid", lbid));
			}
		};
		concelNetAsync.execute();
	}
	/**
	 * 根据当前状态是团购页面还是特卖页面加载对应页数的收藏商品
	 * 
	 * @param page
	 *            2015-1-14 lforxeverc
	 */
	public void loadCollections() {
		//mDialog.show();
		NetAsync loadTask = new NetAsync(D.API_GETALL_COLLECT, this) {
			@Override
			public Object processDataInBackground(JsonElement elData) {
				Type type = new TypeToken<Collection[]>() {
				}.getType();
				Collection[] data = JsonUtilities
						.parseModelByType(elData, type);
				Model.save(data);
				return elData;
			}

			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				// int page = 0;
				// if (nowStatus.equals(TUANGOU))
				// page = tgPage;
				// if (nowStatus.equals(TEMAI))
				// page = tmPage;
				params.add(new BasicNameValuePair("uflbs", nowStatus));
				System.out.println("nowStatus:"+nowStatus);
				//params.add(new BasicNameValuePair("page", page + ""));

			}
		};
		loadTask.execute();
	}
	@Override
	public void onResultError(String reqUrl, String errMsg) {
		// if(tgPage==0||tmPage==0) listview.setVisibility(View.GONE);
		switch (reqUrl) {
		case D.API_GETALL_COLLECT:
			setListView();
			break;

		default:
			break;
		}
	}

	@Override
	public void onResultSuccess(String reqUrl, Object data) {
		switch (reqUrl) {
		case D.API_CANCEL_COLLECT:
			Toast.makeText(context, "您取消了该商品的收藏", 0).show();
			break;
		case D.API_GETALL_COLLECT:
			/*
			 * firstTime = false; if (isLoadMore) { page++; isLoadMore = false;
			 * } // 下拉刷新，隐藏下拉头 else { //listview.onRefreshFinish(); }
			 */
			setListView();
			break;
		default:
			break;

		}

	}

	@Override
	public void onTokenTimeout() {
		// ZhongTuanApp.getInstance().logout();

	}

}
