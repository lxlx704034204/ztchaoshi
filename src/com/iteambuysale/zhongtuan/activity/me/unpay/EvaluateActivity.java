package com.iteambuysale.zhongtuan.activity.me.unpay;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.activity.BaseActivity;
import com.iteambuysale.zhongtuan.activity.me.AlbumActivity;
import com.iteambuysale.zhongtuan.adapter.ImageAdapter;
import com.iteambuysale.zhongtuan.background.FileUpLoadAsync;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.global.NetAsyncListener;
import com.iteambuysale.zhongtuan.utilities.ImageUtilities;
import com.iteambuysale.zhongtuan.views.CustomProgressDialog;

public class EvaluateActivity extends BaseActivity implements OnClickListener,
		NetAsyncListener {
	String ordId, name, picUrl, cost, sum, time, evaluation, rating, shopid,
			ordno, cpid;
	ImageView picImage;
	TextView tittleTv, productNameTv, sumTv, timeTv, costTv;
	RatingBar ratingbar;
	EditText evaluationEt;
	Button uploadBtn, backBtn, commitBtn;
	CustomProgressDialog mDialog;
	GridView gridView;
	String[] filesName;
	//0为团购，1为特卖
	private int currentstate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.evaluate);
		getDataFromIntent();
		initView();
		loadData();
		LoadDataFromDB();
		initListener();

	}

	private void initListener() {
		backBtn.setOnClickListener(this);
		commitBtn.setOnClickListener(this);
		uploadBtn.setOnClickListener(this);

	}

	private void getDataFromIntent() {
		Intent intent = getIntent();
		ordId = intent.getStringExtra("id");
		name = intent.getStringExtra("name");
		picUrl = intent.getStringExtra("picurl");
		cost = intent.getStringExtra("cost");
		sum = intent.getStringExtra("sum");
		time = intent.getStringExtra("time");
		currentstate = intent.getIntExtra("currentstate", -1);
	}

	private void loadData() {
		ImageUtilities.loadBitMap(picUrl, picImage);
		tittleTv.setText("评价");
		commitBtn.setText("提交评价");
		backBtn.setBackgroundResource(R.drawable.header_back);
		productNameTv.setText(name);
		sumTv.setText(sum);
		costTv.setText(cost);
		timeTv.setText(time.substring(0, 11));

	}

	private void initView() {
		picImage = (ImageView) findViewById(R.id.pic);
		tittleTv = (TextView) findViewById(R.id.tv_header_tittle);
		productNameTv = (TextView) findViewById(R.id.tv_tittle);
		sumTv = (TextView) findViewById(R.id.tv_sum);
		costTv = (TextView) findViewById(R.id.tv_price);
		timeTv = (TextView) findViewById(R.id.time);
		ratingbar = (RatingBar) findViewById(R.id.ratingbar);
		evaluationEt = (EditText) findViewById(R.id.et_evaluation);
		uploadBtn = (Button) findViewById(R.id.uplodeBtn);
		backBtn = (Button) findViewById(R.id.back);
		commitBtn = (Button) findViewById(R.id.setting);
		gridView = (GridView) findViewById(R.id.picView);

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.setting:
			commitEvaluation();
			break;
		case R.id.uplodeBtn:
			Intent intent = new Intent(EvaluateActivity.this,
					AlbumActivity.class);
			intent.putExtra("state", true);
			startActivityForResult(intent, 1);
			break;
		}

	}

	private void commitEvaluation() {
		evaluation = evaluationEt.getText().toString();
		rating = ratingbar.getRating() + "";
		if (!evaluation.equals("") && !rating.equals("")) {
			// commit();
			if (currentstate == 1) {
				commitWithPics(D.API_SPECIAL_ORDER_ORDERCCOM);
			} else {
				commitWithPics(D.API_CPORD_ORDRECCOM);
			}
		} else {
			Toast.makeText(this, "你还没有评论或评价！", Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * 带图片评价 lforxeverc 2014-12-23上午10:24:36
	 */
	private void commitWithPics(String url) {
		LoadDataFromDB();
		mDialog = CustomProgressDialog.createDialog(this);
		mDialog.show();
		FileUpLoadAsync upLoad = new FileUpLoadAsync(url, filesName, this) {

			@Override
			public Object processDataInBackground(JsonElement elData) {
				return elData;
			}

			@Override
			public InputStream processBitmapBeforeUpload(File file)
					throws FileNotFoundException {

				return ImageUtilities.compressBitmap(file, 100, 10);
			}

			@Override
			public void beforeRequestInBackground(
					ArrayList<NameValuePair> mParams) {
				mParams.add(new BasicNameValuePair("ordno", ordno));
				mParams.add(new BasicNameValuePair("shopid", shopid));
				mParams.add(new BasicNameValuePair("cpid", cpid));
				mParams.add(new BasicNameValuePair("cpmc", name));
				mParams.add(new BasicNameValuePair("level", "2"));
				mParams.add(new BasicNameValuePair("dfen", rating));
				mParams.add(new BasicNameValuePair("memo", evaluation));
				// for(int i=0;i<filesName.length;i++){
				// mParams.add(new BasicNameValuePair("pic"+i,filesName[i]));
				// }
			}
		};
		upLoad.execute();

	}

	private void LoadDataFromDB() {
		Cursor cursor;
		SQLiteDatabase db = ZhongTuanApp.getInstance().getRDB();
		if(currentstate==0){
		cursor = db.query("ORDER_TG_LIST", null, "_id=?",
				new String[] { ordId }, null, null, null);
		}
		else{
		cursor = db.query("ORDER_TM_LIST", null, "_id=?",
					new String[] { ordId }, null, null, null);
		}
		cursor.moveToFirst();
		ordno = cursor.getString(cursor.getColumnIndex("_ordno"));
		shopid = cursor.getString(cursor.getColumnIndex("_shopid"));
		cpid = cursor.getString(cursor.getColumnIndex("_fcpmid"));
		// cursor.close();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1 && resultCode == RESULT_OK) {
			// 选择图片后隐藏按钮
			uploadBtn.setVisibility(View.GONE);
			String[] bmList = data.getStringArrayExtra("bitmaps");
			String where = "_id in (";
			for (int i = 0; i < bmList.length; i++) {
				if (i != bmList.length - 1) {
					where = where + "?,";
				} else {
					where = where + "? )";
				}
			}
			Cursor c = getContentResolver().query(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, where,
					bmList, null);
			ImageAdapter adapter = new ImageAdapter(this, R.layout.image, c,
					new String[] { Media.DATA }, new int[] { R.id.img }, 0,
					gridView);
			gridView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			getFiles(c);
			// c.close();

		}
		if (resultCode == RESULT_CANCELED) {

		}
	}

	/**
	 * 根据cursor获得图片的文件名
	 * 
	 * @param c
	 * @return lforxeverc 2014-12-23上午10:14:19
	 */
	private String[] getFiles(Cursor c) {
		c.moveToFirst();
		filesName = new String[c.getCount()];
		for (int i = 0; i < c.getCount(); i++) {
			filesName[i] = c.getString(c.getColumnIndex(Media.DATA));
			c.moveToNext();
		}
		return filesName;

	}

	@Override
	public void onResultError(String reqUrl, String errMsg) {
		mDialog.dismiss();
		Toast.makeText(this, errMsg, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onResultSuccess(String reqUrl, Object data) {
		mDialog.dismiss();
		Toast.makeText(this, "成功！", Toast.LENGTH_SHORT).show();
		finish();

	}

	@Override
	public void onTokenTimeout() {
		mDialog.dismiss();
		Toast.makeText(this, "超时！", Toast.LENGTH_SHORT).show();
		ZhongTuanApp.getInstance().logout(this,true);

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
	protected void onDestroy() {
		super.onDestroy();
		ImageUtilities.removeBitmaps();
	}

}
