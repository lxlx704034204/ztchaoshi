package com.iteambuysale.zhongtuan.activity.specialsale;

import java.lang.reflect.Type;
import java.util.List;

import org.apache.http.NameValuePair;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.activity.BaseActivity;
import com.iteambuysale.zhongtuan.background.NetAsync;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.global.NetAsyncListener;
import com.iteambuysale.zhongtuan.model.Model;
import com.iteambuysale.zhongtuan.model.SpecialSaleCatagory;
import com.iteambuysale.zhongtuan.utilities.DBUtilities;
import com.iteambuysale.zhongtuan.utilities.ImageUtilities;
import com.iteambuysale.zhongtuan.utilities.JsonUtilities;

public class MoreCategoryLoad extends BaseActivity implements OnClickListener,
		NetAsyncListener, OnItemClickListener {

	private Cursor cursor;
	private Myadapter adapter;

	@Override
	protected void onDestroy() {
		cursor.close();
		super.onDestroy();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_catagory_load);
		Button back = (Button) findViewById(R.id.back);
		back.setOnClickListener(this);
		back.setBackgroundResource(R.drawable.special_sale_lady_return_background);
		TextView tv_header_tittle = (TextView) findViewById(R.id.tv_header_tittle);
		GridView gv_list_catagory = (GridView) findViewById(R.id.gv_list_catagory);
		cursor = DBUtilities.gettmbiglb(false);
		adapter = new Myadapter(getApplicationContext(),
				R.layout.project_guide, cursor, new String[] { "_lbname",
						"_icon" }, new int[] { R.id.tv_more_catgory,
						R.id.iv_prj_catory_load });
		gv_list_catagory.setAdapter(adapter);
		gv_list_catagory.setSelector(new ColorDrawable(Color.TRANSPARENT));
		gv_list_catagory.setOnItemClickListener(this);
		tv_header_tittle.setText("类目导航");
		loadCatgoryproduct();
	}

	@Override
	public void onClick(View v) {
		finish();
	}

	private class Myadapter extends SimpleCursorAdapter {

		public Myadapter(Context context, int layout, Cursor c, String[] from,
				int[] to) {
			super(context, layout, c, from, to);
		}

		@Override
		public void setViewImage(final ImageView iv, String url) {
			ImageUtilities.loadBitMap(url, iv);
		}

	}

	private void loadCatgoryproduct() {
		NetAsync catory = new NetAsync(D.API_SPECIAL_GETTMLB, this) {

			@Override
			public Object processDataInBackground(JsonElement elData) {
				Type type = new TypeToken<SpecialSaleCatagory[]>() {
				}.getType();
				SpecialSaleCatagory[] specialSaleCatagory = JsonUtilities
						.parseModelByType(elData, type);
				Model.delete(SpecialSaleCatagory.class);
				Model.save(specialSaleCatagory);
				return specialSaleCatagory;
			}

			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {

			}
		};
		catory.execute();
	}

	@Override
	public void onResultError(String reqUrl, String errMsg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResultSuccess(String reqUrl, Object data) {
		cursor = DBUtilities.gettmbiglb(false);
		adapter.changeCursor(cursor);
		adapter.notifyDataSetChanged();

	}

	@Override
	public void onTokenTimeout() {
		ZhongTuanApp.getInstance().logout(this,false);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		/*Intent intent = new Intent(getApplicationContext(),
				SpecialSaleLady.class);
		intent.putExtra("lbid", id + "");
		intent.putExtra("lbname", DBUtilities.gettmlbname(id + ""));*/
		Intent intent = new Intent(this, LadyCatgory.class);
		intent.putExtra("cup", id + "");
		//intent.putExtra("lbname", DBUtilities.gettmlbname(id + ""));
		startActivity(intent);
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
