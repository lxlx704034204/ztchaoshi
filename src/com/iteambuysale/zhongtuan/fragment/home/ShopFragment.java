package com.iteambuysale.zhongtuan.fragment.home;

import java.lang.reflect.Type;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.activity.specialsale.SpecialShop;
import com.iteambuysale.zhongtuan.adapter.ShopCollectionAdapter;
import com.iteambuysale.zhongtuan.background.NetAsync;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.TimeToloadDataListener;
import com.iteambuysale.zhongtuan.listener.global.NetAsyncListener;
import com.iteambuysale.zhongtuan.model.Model;
import com.iteambuysale.zhongtuan.model.ShopMsg;
import com.iteambuysale.zhongtuan.utilities.DBUtilities;
import com.iteambuysale.zhongtuan.utilities.JsonUtilities;

public class ShopFragment extends Fragment implements NetAsyncListener,
		TimeToloadDataListener, OnItemClickListener  {

	private TextView title;
	private ListView list;
	private View footerView;
	private Cursor cur;
	private ShopCollectionAdapter adapter;
	private String[] froms;
	private int[] tos;
	private int page = 0;
	private int beforepage = -1;
	private int listFirstvisibleItem;
	private int topy;
	private ImageView iv_anim;
	private int footerheight;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = View.inflate(getActivity(), R.layout.tuangouhui_fregment,
				null);
		title = (TextView) view.findViewById(R.id.tv_header_tittle);
		list = (ListView) view.findViewById(R.id.list);
		footerView = View.inflate(getActivity(), R.layout.product_footer_v2,
				null);
		iv_anim = (ImageView) footerView.findViewById(R.id.zhuanjuhua);
		return view;

	}

	@Override
	public void onStart() {
		super.onStart();
		list.addFooterView(footerView);
		title.setText("推荐店铺");
		initdata();
	}

	public void initdata() {
		cur = DBUtilities.getallShop();
		froms = new String[] { "_adpic", "_shopname", "_tel", "_collects" };
		tos = new int[] { R.id.iv_product_image, R.id.tv_product_introduce,
				R.id.tv_before_discount, R.id.tv_sell_num };
		adapter = new ShopCollectionAdapter(getActivity(),
				R.layout.showproduct_version2, cur, froms, tos, list);
		adapter.setLoadDataListener(this);
		list.setAdapter(adapter);
		list.setOnItemClickListener(this);
		footerheight=measurefootview();
		startanim();
		loadshop(0);
	}
	private int measurefootview() {
		footerView.measure(0, 0);
		return footerView.getMeasuredHeight();
	}

	// 加载特卖商品列表
	public void loadshop(final int page) {
		NetAsync task_loadTemai = new NetAsync(D.API_SPECIAL_GET_ALL_SHOP, this) {

			@Override
			public Object processDataInBackground(JsonElement elData) {
				Type type = new TypeToken<ShopMsg[]>() {
				}.getType();
				ShopMsg[] shopmsg = JsonUtilities
						.parseModelByType(elData, type);
				if(page==0){
					
					Model.delete(ShopMsg.class);
				}
				
				Model.save(shopmsg);
				return shopmsg;
			}

			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				params.add(new BasicNameValuePair("page", page + ""));
			}
		};
		// asynclist.add(task_loadTemai);
		task_loadTemai.execute();
	}

	@Override
	public void onResultError(String reqUrl, String errMsg) {
		switch (reqUrl) {
		case D.API_SPECIAL_GET_ALL_SHOP:
			footerView.setPadding(0, -footerheight, 0, 0);
			Toast.makeText(getActivity(), errMsg, 0).show();

			break;

		default:
			break;
		}

	}

	@Override
	public void onResultSuccess(String reqUrl, Object data) {
		switch (reqUrl) {
		case D.API_SPECIAL_GET_ALL_SHOP:
			cur = DBUtilities.getallShop();
			// System.out.println("c.getcount():"+c.getCount());
			// System.out.println("cpid:"+cpid);
			adapter.changeCursor(cur);
			adapter.putValuetolist(cur);
			adapter.setflag(false);
			adapter.notifyDataSetChanged();
			// list.setSelection(listFirstvisibleItem);
			// int position=list.getFirstVisiblePosition();//可能存在问题啊
			// View view=list.getChildAt(listFirstvisibleItem);
			if (page == 0) {
				list.setSelection(0);
			} else {
				list.setSelectionFromTop(listFirstvisibleItem + 1, topy);
			}
			adapter.setflag(true);
			// }
			page++;
			beforepage = page - 1;

			break;

		default:
			break;
		}

	}

	@Override
	public void onTokenTimeout() {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadNextPageData() {
		if (beforepage < page) {
			loadshop(page);
			beforepage++;
		}

	}

	@Override
	public void getcurrentvisiableItem(int firstVisibaleItem, int y) {
		listFirstvisibleItem = firstVisibaleItem;
		topy = y;
	}

	public void startanim() {
		iv_anim.setBackgroundResource(R.anim.special_sale_loadmore_v2);
		AnimationDrawable animationdrawable = (AnimationDrawable) iv_anim
				.getBackground();
		animationdrawable.start();
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		//System.out.println(id+"");
		Intent intent3 = new Intent(getActivity(), SpecialShop.class);
		intent3.putExtra("shopId", id+"");
		startActivity(intent3);
		
	}
    
}
