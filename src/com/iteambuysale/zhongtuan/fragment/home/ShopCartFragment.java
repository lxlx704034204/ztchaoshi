package com.iteambuysale.zhongtuan.fragment.home;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.activity.HomeActivity;
import com.iteambuysale.zhongtuan.activity.shopcart.ShopCartPostOrder;
import com.iteambuysale.zhongtuan.activity.specialsale.ProductActivity;
import com.iteambuysale.zhongtuan.actor.HomeActor;
import com.iteambuysale.zhongtuan.adapter.ShopCartListAdapter;
import com.iteambuysale.zhongtuan.background.NetAsync;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.global.NetAsyncListener;
import com.iteambuysale.zhongtuan.listener.shopcart.UpdataCartMsgListener;
import com.iteambuysale.zhongtuan.model.ShopCart;
import com.iteambuysale.zhongtuan.utilities.JsonUtilities;
import com.iteambuysale.zhongtuan.views.CustomProgressDialog;

public class ShopCartFragment extends Fragment implements OnClickListener, NetAsyncListener, UpdataCartMsgListener, OnItemLongClickListener {
	private HomeActor mActor;
	private int TAG_SALE=0;
	CustomProgressDialog dialog ;
	private ListView lv_production;
	private LinearLayout ll_without_profuct;
	private ShopCartListAdapter adapter;
	private View view;
	private RadioButton rb_select_all;
	private RadioButton rb_jiesuan;
	private TextView tv_price;
	ArrayList<ShopCart> sizeList;
	private View button_view;
	private boolean selectallchecked;
	private boolean jiesuanchecked;
	ArrayList<ShopCart> buycart;//结算是传递的购物订单

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		dialog=CustomProgressDialog.createDialog(getActivity());
		buycart =new ArrayList<ShopCart>();
	
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.shopcartfragment, null);
		Button bt_continue_shoping=(Button) view.findViewById(R.id.bt_continue_shoping);
		TextView tv_header_tittle=(TextView) view.findViewById(R.id.tv_header_tittle);
		lv_production = (ListView) view.findViewById(R.id.lv_product);
		ll_without_profuct = (LinearLayout) view.findViewById(R.id.ll_without_profuct);
		tv_header_tittle.setText("购物车");
		bt_continue_shoping.setOnClickListener(this);
		rb_select_all = (RadioButton) view.findViewById(R.id.rb_select_all);
		rb_jiesuan = (RadioButton) view.findViewById(R.id.rb_jiesuan);
		tv_price=(TextView)view.findViewById(R.id.tv_price);
		button_view=view.findViewById(R.id.butom_view);
		button_view.setVisibility(View.GONE);
		
		return view ;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		button_view.setVisibility(View.GONE);
		
		initdata();
		//lv_production.setOnItemClickListener(this);
		lv_production.setOnItemLongClickListener(this);
		/*rb_select_all.setOnCheckedChangeListener(this);
		rb_jiesuan.setOnCheckedChangeListener(this);*/
		rb_select_all.setOnClickListener(this);
		rb_jiesuan.setOnClickListener(this);
	}
	
  private void initdata(){
	  //lv_production.setAdapter(new ShopCartListAdapter(getActivity(), list));
	  if(adapter!=null){
		  sizeList.clear();
		  adapter.notifyDataSetChanged();
	  }
	   String ackToken = ZhongTuanApp.getInstance().getAppSettings().ackToken;
	   if (!TextUtils.isEmpty(ackToken)) {
		
		   loadcartmsg();
		   dialog.show();
	}
  }
	private void loadcartmsg() {
	
		NetAsync paramtersAsync = new NetAsync(D.API_SPECIAL_GET_CART,
				this) {
			@Override
			public Object processDataInBackground(JsonElement elData) {
				Type type = new TypeToken<ArrayList<ShopCart>>() {
				}.getType();
				ArrayList<ShopCart> sizeList = JsonUtilities
						.parseModelByType(elData, type);
				return sizeList;
			}

			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
			
			}
		};
		// asynclist.add(paramtersAsync);
		paramtersAsync.execute();
}
	private void editcartmsg(final String ctid,final String sl) {
		
		NetAsync paramtersAsync = new NetAsync(D.API_SPECIAL_EDIT_CART,
				this) {
			
			@Override
			public Object processDataInBackground(JsonElement elData) {
				/*Type type = new TypeToken<ArrayList<ShopCart>>() {
				}.getType();
				ArrayList<ShopCart> sizeList = JsonUtilities
						.parseModelByType(elData, type);
				return sizeList;*/
				return null;
			}
			
			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				params.add(new BasicNameValuePair("ctid", ctid));
				params.add(new BasicNameValuePair("tmsl", sl));
			}
		};
		// asynclist.add(paramtersAsync);
		paramtersAsync.execute();
	}
	/*private boolean selectallchecked;
	private boolean jiesuanchecked;*/
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_continue_shoping:
			mActor = new HomeActor(getActivity());
			mActor.changeTab(TAG_SALE);
			break;
		case R.id.rb_jiesuan:
			/*if(jiesuanchecked){
				rb_jiesuan.setChecked(false);
				jiesuanchecked=false;
			}else if(!jiesuanchecked){
				rb_jiesuan.setChecked(true);
				jiesuanchecked=true;
			}*/
			Intent intent =new Intent(getActivity(),ShopCartPostOrder.class );
			intent.putExtra("cartlist", buycart);
			intent.putExtra("sum", sum+"");
			startActivity(intent);
			break;
		case R.id.rb_select_all:
			if(selectallchecked){
				rb_select_all.setChecked(false);
				
				adapter=new ShopCartListAdapter(getActivity(), sizeList, lv_production);
				adapter.setonUpdataCartMsgListener(this);
				adapter.unselectall();
				lv_production.setAdapter(adapter);
				selectallchecked=false;
			}else if(!selectallchecked){
				rb_select_all.setChecked(true);
				adapter=new ShopCartListAdapter(getActivity(), sizeList, lv_production);
				adapter.setonUpdataCartMsgListener(this);
				adapter.selectall();
				lv_production.setAdapter(adapter);
				selectallchecked=true;
			}
			break;

		default:
			break;
		}
		
		
	}

	@Override
	public void onResultError(String reqUrl, String errMsg) {
		if (dialog!=null&&dialog.isShowing()) {
			dialog.dismiss();
		}
		//Toast.makeText(getActivity(), errMsg, 1).show();
		
	}

	@Override
	public void onResultSuccess(String reqUrl, Object data) {
		if (dialog!=null&&dialog.isShowing()) {
			dialog.dismiss();
		}
		switch (reqUrl) {
		case D.API_SPECIAL_GET_CART:
		    sizeList=(ArrayList<ShopCart>) data;
			handlecart(sizeList);
			break;
		case D.API_SPECIAL_EDIT_CART:
			initdata();
			break;
		case D.API_SPECIAL_CART_DELETE_ORDER:
			initdata();
		
		default:
			break;
		}
		
	}

	private void handlecart(ArrayList<ShopCart> sizeList) {
		ll_without_profuct.setVisibility(View.GONE);
		adapter = new ShopCartListAdapter(getActivity(), sizeList,lv_production);
		adapter.setonUpdataCartMsgListener(this);
		lv_production.setAdapter(adapter);
		
	}

	@Override
	public void onTokenTimeout() {
		if (dialog!=null&&dialog.isShowing()) {
			dialog.dismiss();
		}
	}

	@Override
	public void updataCartMsg(String ctid, String sl) {
		editcartmsg(ctid, sl);
		dialog.show();
	}

	/*@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.rb_jiesuan:
			Toast.makeText(getActivity(), isChecked+"", 0).show();
			break;
		case R.id.rb_select_all:
			Toast.makeText(getActivity(), isChecked+"", 0).show();
			break;

		default:
			break;
		}
		
	}*/
   float sum;
	@Override
	public void changeprice(ArrayList<Integer> selectnum) {
		sum=0.00f;
	//	Toast.makeText(getActivity(), selectnum.size()+"", 0).show();
		if(selectnum.size()==0){
			button_view.setVisibility(View.GONE);
			lv_production.setPadding(0, 0, 0, 0);
		}else{
			button_view.setVisibility(View.VISIBLE);
			int button=((HomeActivity)getActivity()).getviewgroup().getHeight();
			//Toast.makeText(getActivity(), button+"", 0).show();
			lv_production.setPadding(0, 0, 0, button+2);
			//lv_production.setPadding(lv_production.getLeft(), lv_production.getTop(), lv_production.getRight(),button);
		}
		  buycart.clear();
          for(int i=0;i<selectnum.size();i++){
        	 // ShopCart shopCart = sizeList.get(selectnum.get(i));
        	buycart.add(sizeList.get(selectnum.get(i)));
        	sum+=Float.parseFloat(sizeList.get(selectnum.get(i)).getJe());
          }	
          tv_price.setText("￥"+sum);
          rb_jiesuan.setText("结算（"+selectnum.size()+")");
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		
		dialog(sizeList.get(position).getCtid());
		//deleteshopCart(sizeList.get(position).getCtid());
		return true;
	}

	/*@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(getActivity(), ProductActivity.class);
		intent.putExtra("productId", sizeList.get(position).getMid());
		startActivity(intent);
		
	}*/
	public void deleteshopCart(final String ctid){
		NetAsync cartdeleteAsync=new NetAsync(D.API_SPECIAL_CART_DELETE_ORDER,this) {
			
			@Override
			public Object processDataInBackground(JsonElement elData) {
				// TODO Auto-generated method stub
				return elData;
			}
			
			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				params.add(new BasicNameValuePair("ctid", ctid));
				
			}
		};
		dialog.show(); 
		cartdeleteAsync.execute();
		
	}
	protected void dialog(final String ctid) {
		  AlertDialog.Builder builder = new Builder(getActivity());
		  builder.setMessage("确认要删除选中产品？");

		  builder.setTitle("提示");

		  builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

		   @Override
		   public void onClick(DialogInterface dialog, int which) {
		    dialog.dismiss();
		    //ShopCartFragment.dialog.show();
		    deleteshopCart(ctid);
		   }
		  });

		  builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

		   @Override
		   public void onClick(DialogInterface dialog, int which) {
		    dialog.dismiss();
		   }
		  });

		  builder.create().show();
		 }


}
