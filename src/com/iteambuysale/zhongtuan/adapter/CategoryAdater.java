package com.iteambuysale.zhongtuan.adapter;

import java.util.ArrayList;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.iteambuysale.zhongtuan.R;


public class CategoryAdater extends BaseAdapter implements OnClickListener{
	ArrayList<Map<String,Object>> list;
	int layout;
	String[] tittleText;
	String[][] subText;
	int[] pic;
	LayoutInflater inflater;  
	Context context;
	Button tittle,sub1,sub2,sub3,sub4,sub5,sub6,sub7,sub8,etc;
	ImageView imageView;
	boolean state=true;
	public CategoryAdater(Context context,int layout,String[] tittle,String[][] subtext,int[] pic ) {
		this.tittleText=tittle;
		this.layout=layout;
		this.subText=subtext;
		this.context=context;
		this.pic=pic;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 4;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		if(arg1==null){
		inflater=LayoutInflater.from(context);
		arg1=inflater.inflate(R.layout.category_item,arg2,false);	
		}
		imageView=(ImageView) arg1.findViewById(R.id.pic);
		tittle=(Button) arg1.findViewById(R.id.tittle);
		sub1=(Button) arg1.findViewById(R.id.sub1);
		sub2=(Button) arg1.findViewById(R.id.sub2);
		sub3=(Button) arg1.findViewById(R.id.sub3);
		sub4=(Button) arg1.findViewById(R.id.sub4);
		sub5=(Button) arg1.findViewById(R.id.sub5);
		sub6=(Button) arg1.findViewById(R.id.sub6);
		sub7=(Button) arg1.findViewById(R.id.sub7);
		sub8=(Button) arg1.findViewById(R.id.sub8);
		etc=(Button) arg1.findViewById(R.id.etc);
		tittle.setOnClickListener(this);
		sub1.setOnClickListener(this);
		sub2.setOnClickListener(this);
		sub3.setOnClickListener(this);
		sub4.setOnClickListener(this);
		sub5.setOnClickListener(this);
		sub6.setOnClickListener(this);
		sub7.setOnClickListener(this);
		sub8.setOnClickListener(this);
		
		imageView.setBackgroundColor(Color.WHITE);
		imageView.setBackgroundResource(pic[arg0]);
		tittle.setText(tittleText[arg0]);
		sub1.setText(subText[arg0][0]);
		sub2.setText(subText[arg0][1]);
		sub3.setText(subText[arg0][2]);
		sub4.setText(subText[arg0][3]);
		sub5.setText(subText[arg0][4]);
		sub6.setText(subText[arg0][5]);
		sub7.setText(subText[arg0][6]);
		sub8.setText(subText[arg0][7]);
		return arg1;
	}

	@Override
	public void onClick(View arg0) {		
		if(arg0.getId()==R.id.tittle){			
		View view=(View) arg0.getParent();
		sub5=(Button) view.findViewById(R.id.sub5);
		sub6=(Button) view.findViewById(R.id.sub6);
		sub7=(Button) view.findViewById(R.id.sub7);	
		sub8=(Button) view.findViewById(R.id.sub8);	
		etc=(Button) view.findViewById(R.id.etc);
		if(state){
		sub5.setVisibility(View.VISIBLE);
		sub6.setVisibility(View.VISIBLE);
		sub7.setVisibility(View.VISIBLE);
		sub8.setVisibility(View.VISIBLE);
		etc.setVisibility(View.GONE);		
		state=false;
		}
		else {
			sub5.setVisibility(View.GONE);
			sub6.setVisibility(View.GONE);
			sub7.setVisibility(View.GONE);
			sub8.setVisibility(View.GONE);
			etc.setVisibility(View.VISIBLE);
			state=true;
		}
		}
		else{
			Button click=(Button) arg0.findViewById(arg0.getId());
			String text=click.getText().toString();
			Toast.makeText(context, text+"暂未开通，敬请期待！", Toast.LENGTH_SHORT).show();
		}
	}

}
