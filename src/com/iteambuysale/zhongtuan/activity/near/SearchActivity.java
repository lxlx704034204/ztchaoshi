package com.iteambuysale.zhongtuan.activity.near;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.activity.BaseActivity;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.views.CustomProgressDialog;

public class SearchActivity extends BaseActivity implements OnClickListener{
	Button cancelBtn,foodBtn,playBtn,movieBtn,travelBtn,ktvBtn,clBtn;
	EditText searchEt;
	CustomProgressDialog mDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		findViews();
		initListener();		
		
	}
	
	
	private void initListener() {
		cancelBtn.setOnClickListener(this);
		foodBtn.setOnClickListener(this);
		playBtn.setOnClickListener(this);
		travelBtn.setOnClickListener(this);
		movieBtn.setOnClickListener(this);
		ktvBtn.setOnClickListener(this);
		clBtn.setOnClickListener(this);
		
	}


	private void findViews() {
		cancelBtn=(Button) findViewById(R.id.btn_cancel);
		searchEt=(EditText) findViewById(R.id.et_search);
		playBtn=(Button) findViewById(R.id.btn1);
		foodBtn=(Button) findViewById(R.id.btn2);		
		movieBtn=(Button)findViewById(R.id.btn3);
		travelBtn=(Button) findViewById(R.id.btn4);
		ktvBtn=(Button) findViewById(R.id.btn5);
		clBtn=(Button) findViewById(R.id.btn6);
		
	}


	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btn_cancel:
			finish();
			break;
		/*case R.id.btn1:
			Intent intent = new Intent(SearchActivity.this,
					NearBusinessActivity.class);
			intent.putExtra("pTag", D.CATE_PTAG_4);
			intent.putExtra("cTag", "");
			startActivity(intent);
			break;
		case R.id.btn2:
			Intent intent1 = new Intent(SearchActivity.this,
					NearBusinessActivity.class);
			intent1.putExtra("pTag", D.CATE_PTAG_2);
			intent1.putExtra("cTag", "");
			startActivity(intent1);
			break;
		case R.id.btn3:
			Intent intent2 = new Intent(SearchActivity.this,
					NearBusinessActivity.class);
			intent2.putExtra("pTag", D.CATE_PTAG_3);
			intent2.putExtra("cTag", "");
			startActivity(intent2);
			break;
		case R.id.btn4:
			Intent intent3 = new Intent(SearchActivity.this,
					NearBusinessActivity.class);
			intent3.putExtra("pTag", D.CATE_PTAG_6);
			intent3.putExtra("cTag", "");
			startActivity(intent3);
			break;
		case R.id.btn5:
			Intent intent4=new Intent(SearchActivity.this,NearBusinessActivity.class);
        	intent4.putExtra("pTag","");
        	intent4.putExtra("cTag","");
        	intent4.putExtra("isSearch", true);
        	intent4.putExtra("key","KTV");
        	startActivity(intent4);
			break;
		case R.id.btn6:
			Intent intent5=new Intent(SearchActivity.this,NearBusinessActivity.class);
        	intent5.putExtra("pTag","");
        	intent5.putExtra("cTag","");
        	intent5.putExtra("isSearch", true);
        	intent5.putExtra("key","长隆");
        	startActivity(intent5);
			break;*/
		default:
			break;
		}
		
	}
	@Override  
    public boolean dispatchKeyEvent(KeyEvent event) { 
       /* if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER&&event.getAction()!=KeyEvent.ACTION_UP){
        	Intent intent=new Intent(SearchActivity.this,NearBusinessActivity.class);
        	intent.putExtra("pTag","");
        	intent.putExtra("cTag","");
        	intent.putExtra("isSearch", true);
        	intent.putExtra("key",searchEt.getText().toString());
        	startActivity(intent);
            return true;  
        } */
        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK&&event.getAction()!=KeyEvent.ACTION_UP){
        	finish();
        	return true;
        }
        return super.dispatchKeyEvent(event);
        
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