package com.iteambuysale.zhongtuan.activity.specialsale;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.activity.BaseActivity;
import com.iteambuysale.zhongtuan.model.Model;
import com.iteambuysale.zhongtuan.model.SpecialSaleCatagory;
import com.iteambuysale.zhongtuan.utilities.DBUtilities;
import com.iteambuysale.zhongtuan.utilities.ImageUtilities;

public class SpecialSaleLady extends BaseActivity implements OnClickListener,
		OnItemClickListener {

	private int getcatgory;
	private GridView gv_special_sale_catory_load;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.special_sale_lady);
		TextView tv_header_tittle = (TextView) findViewById(R.id.tv_header_tittle);
		String lbname = getIntent().getStringExtra("lbname");
		getcatgory = getIntent().getIntExtra("lbid", -1);
		tv_header_tittle.setText(lbname);
		TextView tv_wecanse = (TextView) findViewById(R.id.tv_wecanse);
		tv_wecanse.setText(lbname);
		// loadsellmsg(getcatgory);
		// GridView gv_sp_item=(GridView) findViewById(R.id.gv_sp_item);
		Button back = (Button) findViewById(R.id.back);
		back.setOnClickListener(this);
		back.setBackgroundResource(R.drawable.special_sale_lady_return_background);
		gv_special_sale_catory_load = (GridView) findViewById(R.id.gv_special_sale_catory_load);
		String lbid = (String) getIntent().getCharSequenceExtra("lbid");
		Cursor cursor = DBUtilities.getSpecialSaleCatagory(lbid);
		Myadapter adapter = new Myadapter(getApplicationContext(), cursor);
		gv_special_sale_catory_load.setAdapter(adapter);
		gv_special_sale_catory_load.setSelector(new ColorDrawable(
				Color.TRANSPARENT));
		gv_special_sale_catory_load.setOnItemClickListener(this);
	}

	private class Myadapter extends CursorAdapter {
		String cup;
		String lbid;

		public Myadapter(Context context, Cursor c) {
			super(context, c);

		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			final Viewholder holder = (Viewholder) view.getTag();
			holder.tv_sale_catagory_words.setText(cursor.getString(cursor
					.getColumnIndex("_lbname")));
			ImageView iv = holder.iv_sale_catagory;
			String url = cursor.getString(cursor.getColumnIndex("_icon"));
			ImageUtilities.loadBitMap(url, iv);

			// Bitmap bitMap = ImageUtilities.loadBitMap(url, iv, new
			// ImageUpdateListener() {
			//
			// @Override
			// public void onLoadImageSuccess(Bitmap bitmap) {
			// holder.iv_sale_catagory.setImageBitmap(bitmap);
			// }
			// });
			// holder.iv_sale_catagory.setImageBitmap(bitMap);

		}

		@Override
		public View newView(Context context, final Cursor cursor,
				ViewGroup viewgoroup) {
			Viewholder holder = new Viewholder();
			View view = View.inflate(getBaseContext(),
					R.layout.spl_sell_catagory_load, null);
			holder.iv_sale_catagory = (ImageView) view
					.findViewById(R.id.iv_sale_catagory);
			holder.tv_sale_catagory_words = (TextView) view
					.findViewById(R.id.tv_sale_catagory_words);
			view.setTag(holder);
			/*
			 * view.setOnClickListener(new OnClickListener() {
			 * 
			 * @Override public void onClick(View v) {
			 * cup=cursor.getString(cursor.getColumnIndex("_cup"));
			 * lbid=cursor.getString(cursor.getColumnIndex("_id")); Intent
			 * intent=new Intent(getApplicationContext(), LadyCatgory.class);
			 * intent.putExtra("cup", cup); intent.putExtra("lbid", lbid);
			 * startActivity(intent); } });
			 */
			return view;
		}

	}

	public static class Viewholder {
		ImageView iv_sale_catagory;
		TextView tv_sale_catagory_words;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		SpecialSaleCatagory catagory = Model.load(new SpecialSaleCatagory(),
				String.valueOf(id));
		String lbid = catagory.getLbid();
		String cup = catagory.getCup();
		Intent intent = new Intent(getApplicationContext(), LadyCatgory.class);
		intent.putExtra("cup", cup);
		intent.putExtra("lbid", lbid);
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
