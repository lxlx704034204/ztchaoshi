package com.iteambuysale.zhongtuan.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.model.Model;
import com.iteambuysale.zhongtuan.model.ProductCategory;
/**
 * 更多分类页面的可展开列表适配器
 * @author lforxeverc
 * 2015-1-13
 */
public class ExAdapter extends BaseExpandableListAdapter {
	private HashMap<String,String> groupData;
	Context mContext;
	//大类ID和大类名称MAP组成的list
	List<HashMap<String,String>> tagList=new ArrayList<HashMap<String, String>>();
	Map<String,List<HashMap<String,String>>> dataMap=new HashMap<String, List<HashMap<String,String>>>();
	public ExAdapter(Context context) {
		super();
		
		mContext=context;
		Cursor tempCr=ZhongTuanApp.getInstance().getRDB().query(Model.getTableName(ProductCategory.class), null, null, null, null,null,null);
		tempCr.moveToFirst();
		//将大类小类以及对应ID和名称分门别类
		for(int i=0;i<tempCr.getCount();i++){
			 String dl=tempCr.getString(tempCr.getColumnIndex("_cup"));
			 String xl=tempCr.getString(tempCr.getColumnIndex("_id"));
			 String lbname=tempCr.getString(tempCr.getColumnIndex("_lbname"));
			if(!dl.equals("0")){
				if(!dataMap.containsKey(dl)){
					List<HashMap<String,String>> list=new ArrayList<HashMap<String,String>>();
					HashMap<String,String> item=new HashMap<String,String>();
					item.put("xl",xl);
					item.put("lbname",lbname);
					list.add(item);
					dataMap.put(dl, list);
				}
				else{
					List<HashMap<String,String>> list=dataMap.get(dl);
					HashMap<String,String> item=new HashMap<String,String>();
					item.put("xl",xl);
					item.put("lbname",lbname);
					list.add(item);
				}
			
			}
			else{
				HashMap<String,String> item=new HashMap<String, String>();
				item.put("dl",xl);
				item.put("lbname",lbname);
				tagList.add(item);
			}
			tempCr.moveToNext();
		}
		
		//关闭游标
		tempCr.close();
		//移除大类中没有子项的大类
		for(int i=0;i<tagList.size();i++){
			if(!dataMap.containsKey(tagList.get(i).get("dl"))){
				tagList.remove(i);
			}
		}
		
	}

	@Override
	public Object getChild(int p, int c) {
		List<HashMap<String, String>> childMap=dataMap.get(tagList.get(p).get("dl"));
		return childMap;
	}

	@Override
	public long getChildId(int p, int c) {
		
		return c;
	}

	@Override
	public View getChildView(final int p, int c, boolean arg2, View view,
			ViewGroup arg4) {
		ViewHolderC viewHolderC=null;
		if(view==null){
			viewHolderC=new ViewHolderC();
			LayoutInflater inflater=LayoutInflater.from(mContext);
			view=inflater.inflate(R.layout.gv_cate, arg4,false);
			viewHolderC.gridview=(GridView) view.findViewById(R.id.gv_item);
			view.setTag(viewHolderC);
		}
		else{
			viewHolderC=(ViewHolderC) view.getTag();
		}
		ItemAdapter adapter=new ItemAdapter(mContext, (List<HashMap<String,String >>) getChild(p, c),R.layout.copy_of_more_category_button, new String[]{"lbname"}, new int[]{R.id.btn1});
		viewHolderC.gridview.setAdapter(adapter);
		viewHolderC.gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
			/*	Intent intent = new Intent(mContext,.class);
				intent.putExtra("pTag",tagList.get(p).get("dl"));
				intent.putExtra("cTag", dataMap.get(tagList.get(p).get("dl")).get(position).get("xl"));
				mContext.startActivity(intent);
				((Activity)mContext).finish();*/
			}
		});
		gridViewAdjustment(viewHolderC.gridview);
		return view;
	}
	/**
	 * 每个子项只有一个gridview，所以此处默认返回1
	 */
	@Override
	public int getChildrenCount(int p) {
		
		return 1;
	}

	@Override
	public Object getGroup(int arg0) {
		
		return tagList.get(arg0);
	}

	@Override
	public int getGroupCount() {
		return dataMap.size();
	}

	@Override
	public long getGroupId(int arg0) {
		
		return arg0;
	}

	@Override
	public View getGroupView(int p, boolean arg1, View view, ViewGroup arg3) {
		ViewHolderG viewHolder=null;
		if(view==null){
			viewHolder=new ViewHolderG();
			LayoutInflater inflater=LayoutInflater.from(mContext);
			view=inflater.inflate(R.layout.copy_of_more_category_item, arg3,false);
			viewHolder.tv=(TextView) view.findViewById(R.id.category_tag);
			viewHolder.iv=(ImageView) view.findViewById(R.id.iv_tag);
			view.setTag(viewHolder);
		}
		else{
			viewHolder=(ViewHolderG) view.getTag();
		}
		groupData= (HashMap<String, String>) getGroup(p);
		viewHolder.tv.setText(groupData.get("lbname"));
		return view;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}
	
	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		return false;
	}
	/**
	 * 调整GridView的高度
	 * @param v
	 */
	private void gridViewAdjustment(GridView v){
		ListAdapter listAdapter=v.getAdapter();
		int totalHeight = 0;   
        int listCount = listAdapter.getCount();
        int itemHight=0;
        
        for (int i = 0; i < listCount;i++) {
            View listItem = listAdapter.getView(i, null, v);   
            listItem.measure(0, 0); 
            itemHight=listItem.getMeasuredHeight();
            totalHeight += itemHight;
        }
       
        if(listCount%3!=0){
        	listCount=listCount/3;
        	totalHeight = totalHeight/3+30*(listCount-1)+itemHight;
        }
        else if(listCount!=0){       	
        	listCount=listCount/3;
        	totalHeight = totalHeight/3+30*(listCount-1);
        }
        
		ViewGroup.LayoutParams params=v.getLayoutParams();
		params.height=(int) (totalHeight);
		v.setLayoutParams(params);
	}
	/**
	 * 每个group中的TextView重用
	 * @author lforxeverc
	 * 2015-1-13
	 */
	public static class ViewHolderG{
		TextView tv;
		ImageView iv;
	}
	/**
	 * 每个child中gridview的重用
	 * @author lforxeverc
	 * 2015-1-13
	 */
	public static class ViewHolderC{
		GridView gridview;
	}
	
	public class ItemAdapter extends SimpleAdapter{
		List<HashMap<String,String >> data;
		public ItemAdapter(Context context,
				List<? extends Map<String, ?>> data, int resource,
				String[] from, int[] to) {
			super(context, data, resource, from, to);
			this.data=(List<HashMap<String, String>>) data;
			
			
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder=null;
			if(convertView==null){
				viewHolder=new ViewHolder();
				convertView=LayoutInflater.from(mContext).inflate(R.layout.copy_of_more_category_button, parent,false);
				viewHolder.tv=(TextView) convertView.findViewById(R.id.btn1);
				convertView.setTag(viewHolder);
			}
			else{
				viewHolder=(ViewHolder) convertView.getTag();
			}
			viewHolder.tv.setText(data.get(position).get("lbname"));
			return convertView;
		}
		/**
		 * gridview的textview
		 * @author lforxeverc
		 * 2015-1-13
		 */
		public class ViewHolder{
			TextView tv;
		}
	}
	

}
