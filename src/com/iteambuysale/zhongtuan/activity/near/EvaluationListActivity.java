package com.iteambuysale.zhongtuan.activity.near;

import java.lang.reflect.Type;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.activity.BaseActivity;
import com.iteambuysale.zhongtuan.adapter.EvaluationAdapter;
import com.iteambuysale.zhongtuan.background.NetAsync;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.OnRefreshListener;
import com.iteambuysale.zhongtuan.listener.global.NetAsyncListener;
import com.iteambuysale.zhongtuan.model.Evaluation;
import com.iteambuysale.zhongtuan.model.Model;
import com.iteambuysale.zhongtuan.model.TeMaiEvaluation;
import com.iteambuysale.zhongtuan.utilities.ImageUtilities;
import com.iteambuysale.zhongtuan.utilities.JsonUtilities;
import com.iteambuysale.zhongtuan.views.CustomProgressDialog;
import com.iteambuysale.zhongtuan.views.RefreshListView;

public class EvaluationListActivity extends BaseActivity implements
		NetAsyncListener,OnClickListener,OnRefreshListener {
	RefreshListView listview;
	Button back,loadMoreBtn;
	TextView tittle;
	String shopId, productId;
	int page=1;
	CustomProgressDialog mDialog;
	EvaluationAdapter adapter;
	SQLiteDatabase db;
	Boolean state=true;
	private boolean istemai;
	private Cursor evaluationCursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lv_evaluation);
		Intent intent = getIntent();
		shopId = intent.getStringExtra("shop");
		productId = intent.getStringExtra("cpid");
		mDialog=CustomProgressDialog.createDialog(this);
        istemai = getIntent().getBooleanExtra("istemai", false);
		listview = (RefreshListView) findViewById(R.id.lv_pj);
		listview.setOnRefreshListener(this);
		listview.setEnablePullDownRefresh(true);
		listview.setEnableLoadingMore(true);
		back = (Button) findViewById(R.id.back);
		tittle = (TextView) findViewById(R.id.tv_header_tittle);
		tittle.setText("评价列表");
		back.setBackgroundResource(R.drawable.header_back);
		back.setOnClickListener(this);
		db = ZhongTuanApp.getInstance().getRDB();
		if (istemai) {
			 Model.delete(TeMaiEvaluation.class);
			 loadEvaluation(D.API_SPECIAL_ORDER_GETTMRECM,0);
				evaluationCursor = db.rawQuery("select * from TEMAIEVALUATION_LIST where _shopid=? and _cpid=? order by _dateandtime desc ", new String[]{shopId,productId});
		}else{
			evaluationCursor = db.query("EVALUATION_LIST", null, null, null,
					null, null, "_id desc");
		}
		adapter = new EvaluationAdapter(this,
				R.layout.listitem_pj, evaluationCursor, new String[] {
						"_username", "_dateandtime", "_recmemo","_recpic" }, new int[] {
						R.id.cusname, R.id.time, R.id.pingjia ,R.id.gridview}, 0);
		listview.setAdapter(adapter);
		

	}

	/**
	 * 加载评价
	 */
	private void loadEvaluation(String url,final int page) {
		mDialog.show();
		NetAsync loadEvaluation = new NetAsync(url, this) {

			@Override
			public Object processDataInBackground(JsonElement elData) {
				Type type;
				if (istemai) {
					type = new TypeToken<TeMaiEvaluation[]>(){}.getType();
					TeMaiEvaluation[] evaluation = JsonUtilities.parseModelByType(elData, type);
					Model.save(evaluation);
				}else{
					type=new TypeToken<Evaluation[]>(){}.getType();
					Evaluation [] evaluation =JsonUtilities.parseModelByType(elData, type);
					Model.save(evaluation);
				}
				return null;
			}

			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				params.add(new BasicNameValuePair("shopid", shopId));
				params.add(new BasicNameValuePair("cpid", productId));
				params.add(new BasicNameValuePair("page", page+""));
				

			}
		};
		loadEvaluation.execute();
	}

	@Override
	public void onResultError(String reqUrl, String errMsg) {
		mDialog.dismiss();
		state=true;
		listview.onRefreshFinish();
		Toast.makeText(this, "评论已全部加载！", Toast.LENGTH_SHORT).show();
		

	}

	@Override
	public void onResultSuccess(String reqUrl, Object data) {		
		mDialog.dismiss();	
		Cursor evaluationCursor=null;;
		if (istemai) {
			
			evaluationCursor = db.query("TEMAIEVALUATION_LIST", null, null, null,
					null, null, "_id desc");
		}else{
			evaluationCursor = db.query("EVALUATION_LIST", null, null, null,
					null, null, "_id desc");
		}
		this.evaluationCursor=evaluationCursor;
		adapter.changeCursor(evaluationCursor);
		page++;
		state=true;
		listview.onRefreshFinish();

	}

	@Override
	public void onTokenTimeout() {
		mDialog.dismiss();
		ZhongTuanApp.getInstance().logout(this,false);

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.loadMoreBtn:
//			loadEvaluation();
			break;
		default:
			break;
		}
		
	}

	@Override
	public void OnPullDownRefresh() {
		if (istemai) {
			loadEvaluation(D.API_SPECIAL_ORDER_GETTMRECM,0);
		}else{
			
			loadEvaluation(D.API_GET_EVALUATION,0);
		}
		
	}

	@Override
	public void onLoadingMore() {
		//mDialog.show();
		if (istemai) {
			loadEvaluation(D.API_SPECIAL_ORDER_GETTMRECM,page);
			state=false;
		}else{
			loadEvaluation(D.API_GET_EVALUATION,page);
			state=false;
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
	@Override
	protected void onDestroy() {
		super.onDestroy();
		evaluationCursor.close();
		ImageUtilities.removeBitmaps();
	}
	
}
