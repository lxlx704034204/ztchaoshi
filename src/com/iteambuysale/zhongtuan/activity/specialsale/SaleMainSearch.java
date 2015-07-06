package com.iteambuysale.zhongtuan.activity.specialsale;

import java.lang.reflect.Type;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.activity.BaseActivity;
import com.iteambuysale.zhongtuan.background.NetAsync;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.global.NetAsyncListener;
import com.iteambuysale.zhongtuan.model.TemaiVerson2;
import com.iteambuysale.zhongtuan.utilities.JsonUtilities;
import com.iteambuysale.zhongtuan.views.CustomProgressDialog;

public class SaleMainSearch extends BaseActivity implements OnClickListener,
		TextWatcher, NetAsyncListener {
	private AutoCompleteTextView at_search;
	private ImageView iv_search_delte;
	private Button bt_search;
	CustomProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sale_search_home);
		 
		initView();
		initdata();
	}

	private void initView() {
		at_search = (AutoCompleteTextView) findViewById(R.id.at_search);
		iv_search_delte = (ImageView) findViewById(R.id.iv_search_delete);
		bt_search = (Button) findViewById(R.id.bt_search);
		
	}

	private void initdata() {
		dialog=CustomProgressDialog.createDialog(this);
		iv_search_delte.setOnClickListener(this);
		at_search.addTextChangedListener(this);
		bt_search.setOnClickListener(this);
	
		// at_search.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_search_delete:
			at_search.setText("");
			break;
		case R.id.bt_search:
			String search=at_search.getText().toString();
			if(!TextUtils.isEmpty(search)){
				dialog.show();
				loadSearchResult(search);
			}else{
				Toast.makeText(this, "搜索不能为空", 0).show();
			}
		default:
			break;
		}

	}

	private void loadSearchResult(final String key) {
	    NetAsync searchAsync=new NetAsync(D.API_SPECIAL_SELL,this) {
			
			@Override
			public Object processDataInBackground(JsonElement elData) {
				Type type = new TypeToken<TemaiVerson2[]>() {
				}.getType();
				TemaiVerson2[] temaiList = JsonUtilities.parseModelByType(
						elData, type);
				return temaiList;
			}
			
			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				params.add(new BasicNameValuePair("key",key));
				
				
			}
		};
			searchAsync.execute();
		}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResultError(String reqUrl, String errMsg) {
	        if(dialog!=null&&dialog.isShowing()){
	        	dialog.dismiss();
	        }
		
	}

	@Override
	public void onResultSuccess(String reqUrl, Object data) {
		 if(dialog!=null&&dialog.isShowing()){
	        	dialog.dismiss();
	        }
		  TemaiVerson2[] tmlist =(TemaiVerson2[]) data;
		  Intent intent=new Intent(this, SearchResultShow.class);
		  intent.putExtra("tmlist", tmlist);
		  startActivity(intent);
		 
		
		
	}

	@Override
	public void onTokenTimeout() {
		 if(dialog!=null&&dialog.isShowing()){
	        	dialog.dismiss();
	        }
		
		
	}

}
