package com.iteambuysale.zhongtuan.activity.me;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.activity.BaseActivity;
import com.iteambuysale.zhongtuan.activity.me.unpay.EvaluateActivity;
import com.iteambuysale.zhongtuan.adapter.ImageAdapter;
import com.iteambuysale.zhongtuan.adapter.ImageAdapter.ViewHolder;

public class AlbumActivity extends BaseActivity implements OnItemClickListener,
		OnClickListener{

	GridView gridView;
	TextView tv;
	Button pickBtn;
	Context mContext;
	ImageAdapter adapter;
	Intent intent;
	Boolean state = false;
//	LruCache<String, Bitmap> cache;
	int picCount=0;
	public static ArrayList<String> list;
	Cursor pictureCr;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.album);			
		initViews();

	}

	private void initViews() {
		intent = getIntent();
		state = intent.getBooleanExtra("state", false);
		mContext = this;
		list = new ArrayList<String>();
		tv=(TextView) findViewById(R.id.tv_header_tittle);
		tv.setText("相册");
		pickBtn=(Button) findViewById(R.id.setting);
		gridView = (GridView) findViewById(R.id.gridview);
		pictureCr = getContentResolver().query(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null,
				Media.DATE_ADDED+" desc");
		adapter = new ImageAdapter(mContext, R.layout.image, pictureCr,
				new String[] { Media.DATA }, new int[] { R.id.img }, 0 ,gridView);
		adapter.setCheboxVisibility(state);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(this);
		pickBtn.setText("选择0/6");
		pickBtn.setVisibility(state?View.VISIBLE:View.GONE);
		pickBtn.setOnClickListener(this);

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
		ViewHolder viewHolder = (ViewHolder) view.getTag();
		CheckBox cb=viewHolder.getCb();
		if (state) {
			if (!cb.isChecked()) {				
				if(picCount<6){
				list.add(id + "");
				viewHolder.getCb().setChecked(true);
				picCount++;
				pickBtn.setText("选择"+picCount+"/6");
				}
				else{
					Toast.makeText(mContext, "最多选择6张图片", Toast.LENGTH_SHORT).show();
				}
				
			}
			else{
				list.remove(id+"");
				viewHolder.getCb().setChecked(false);
				picCount--;
				pickBtn.setText("选择"+picCount+"/6");
				
			}
		} else {
			Bitmap bitmap = viewHolder.img.getDrawingCache();
			intent.putExtra("bitmap", bitmap);
			setResult(RESULT_OK, intent);
			finish();
		}

	}



	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.setting:
			if(!list.isEmpty()){
				Intent intent=new Intent(AlbumActivity.this,EvaluateActivity.class);
				String[] key=new String[list.size()];
				key=(String[])list.toArray(key);
				intent.putExtra("bitmaps", key);
				setResult(RESULT_OK,intent);
				finish();
				}
				else{
					Intent intent=new Intent(AlbumActivity.this,EvaluateActivity.class);
					setResult(RESULT_CANCELED,intent);
					finish();
				}
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
	@Override
	protected void onDestroy() {
		super.onDestroy();
		pictureCr.close();
	}


}
