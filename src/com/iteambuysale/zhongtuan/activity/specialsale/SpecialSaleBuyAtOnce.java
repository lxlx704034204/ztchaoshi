package com.iteambuysale.zhongtuan.activity.specialsale;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.Text;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.activity.BaseActivity;
import com.iteambuysale.zhongtuan.activity.me.address.SelectedAddressActivity;
import com.iteambuysale.zhongtuan.background.NetAsync;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.global.NetAsyncListener;
import com.iteambuysale.zhongtuan.model.Model;
import com.iteambuysale.zhongtuan.model.Tuanbi;
import com.iteambuysale.zhongtuan.model.UserAddress;
import com.iteambuysale.zhongtuan.utilities.DBUtilities;
import com.iteambuysale.zhongtuan.utilities.ImageUtilities;
import com.iteambuysale.zhongtuan.utilities.JsonUtilities;
import com.iteambuysale.zhongtuan.utilities.StringUtilities;
import com.iteambuysale.zhongtuan.views.CustomProgressDialog;
import com.iteambuysale.zhongtuan.views.EditTextWithBorder;

public class SpecialSaleBuyAtOnce extends BaseActivity implements
		OnClickListener, NetAsyncListener, OnCheckedChangeListener {
	private ImageView iv_product_show;
	private TextView tv_prodct_introduce;
	private TextView tv_price_pro;
	private ImageButton ib_sub;
	private EditText tv_showbuynum;
	private Button bt_buy_at_once2;
	private Cursor productList;
	private ImageButton ib_add;
	private int buynum = 1;
	private TextView tv_there_be;
	private TextView tv_nownum;
	private TextView tv_xiaoji;
	private String price, primePrice;
	private TextView tv_all_price;
	private TextView tv_all_sum;
	private Float allprice;
	private LinearLayout ll_seleted_address;
	private LinearLayout ll_detail_address;
	private TextView tv_show_address;
	private TextView tv_show_name_and_tell;
	private String title;
	private CustomProgressDialog dialog;
	private String shopid;
	private String cppic;
	private String productId;
	private String addrid;
	private String chima;
	private TextView tv_size;
	private String tmcid;
	private String kucun;
	private EditTextWithBorder etwb_content;
	private TextView tv_custum_need;
	private TextView tv_youhuimoney;
	private TextView tv_tuanbi_pay;
	private CheckBox cb_selet_tuanbi;
	private float tuanbimoney=0;
	private String tmqno;
	private String quanmoney;
	private Tuanbi tuanbi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.special_sale_buy_at_once);
		initview();
		loaddata();

	}

	private void initview() {
		Button back = (Button) findViewById(R.id.back);
		back.setOnClickListener(this);
		back.setBackgroundResource(R.drawable.special_sale_lady_return_background);
		TextView tv_header_tittle = (TextView) findViewById(R.id.tv_header_tittle);
		tv_header_tittle.setText("立刻购买");
		iv_product_show = (ImageView) findViewById(R.id.iv_product_show);
		tv_prodct_introduce = (TextView) findViewById(R.id.tv_prodct_introduce);
		tv_price_pro = (TextView) findViewById(R.id.tv_price_pro);
		ib_sub = (ImageButton) findViewById(R.id.ib_sub);
		tv_size = (TextView) findViewById(R.id.tv_size);
		ib_sub.setOnClickListener(this);
		ib_sub.setClickable(false);
		tv_showbuynum = (EditText) findViewById(R.id.tv_showbuynum);
		tv_showbuynum.setText(buynum + "");
		ib_add = (ImageButton) findViewById(R.id.ib_add);
		ib_add.setOnClickListener(this);
		bt_buy_at_once2 = (Button) findViewById(R.id.bt_buy_at_once2);
		bt_buy_at_once2.setOnClickListener(this);
		tv_there_be = (TextView) findViewById(R.id.tv_there_be);
		tv_nownum = (TextView) findViewById(R.id.tv_nownum);
		tv_xiaoji = (TextView) findViewById(R.id.tv_xiaoji);
		tv_all_price = (TextView) findViewById(R.id.tv_all_price);
		tv_all_sum = (TextView) findViewById(R.id.tv_all_sum);
		ll_seleted_address = (LinearLayout) findViewById(R.id.ll_seleted_address);
		ll_seleted_address.setOnClickListener(this);
		ll_detail_address = (LinearLayout) findViewById(R.id.ll_detail_address);
		ll_detail_address.setOnClickListener(this);
		tv_show_address = (TextView) findViewById(R.id.tv_show_address);
		tv_show_name_and_tell = (TextView) findViewById(R.id.tv_show_name_and_tell);
		TextView tv_receive_msg = (TextView) findViewById(R.id.tv_reveive_msg);
		etwb_content = (EditTextWithBorder) findViewById(R.id.etwb_content);
		tv_custum_need = (TextView) findViewById(R.id.tv_custum_need);
		LinearLayout ll_youhuiquan = (LinearLayout) findViewById(R.id.ll_youhuiquan);
		tv_youhuimoney = (TextView) findViewById(R.id.tv_youhuimoney);
		tv_tuanbi_pay = (TextView) findViewById(R.id.tv_tuanbi_pay);
		cb_selet_tuanbi = (CheckBox) findViewById(R.id.cb_selet_tuanbi);
		tv_custum_need.getPaint().setFakeBoldText(true);
		tv_receive_msg.getPaint().setFakeBoldText(true);
		ll_youhuiquan.setOnClickListener(this);
		cb_selet_tuanbi.setOnCheckedChangeListener(this);

	}

	private void loaddata() {

		// 加载地址信息
		loadUserAddress();
		productId = (String) getIntent().getCharSequenceExtra("productId");
		chima = getIntent().getStringExtra("chima");
		tmcid = getIntent().getStringExtra("tmcid");
		if (TextUtils.isEmpty(chima)) {
			tv_size.setVisibility(View.GONE);

		} else {
			tv_size.setText("尺码：" + chima);
		}
		productList = DBUtilities.getProductList(productId);// cpmx/商品数据(商铺ID:商品id=>(cpmc=名称,cppic=图片,sl=数量,dj=价格,je=金额))
															// json
		productList.moveToFirst();
		cppic = productList.getString(productList.getColumnIndex("_picurl"));
		shopid = productList.getString(productList.getColumnIndex("_shopid"));
		String url = productList.getString(productList
				.getColumnIndex("_picurl"));
		ImageUtilities.loadBitMap(url, iv_product_show);
		title = productList.getString(productList.getColumnIndex("_title"));
		tv_prodct_introduce.setText(title);
		price = productList.getString(productList.getColumnIndex("_tmdj"));
		primePrice = productList.getString(productList.getColumnIndex("_dj0"));
		allprice = Float.parseFloat(price);
		tv_price_pro.setText(price);
		tv_all_price.setText("￥" + price);
		kucun = productList.getString(productList.getColumnIndex("_kcsl"));
		String buynums = productList.getString(productList
				.getColumnIndex("_buynums"));
		if (!TextUtils.equals(buynums, "0")) {
			tv_there_be.setText("限购：" + buynums + " 件");
			kucun = buynums;
		} else {

			tv_there_be.setText("库存：" + kucun + " 件");
		}
		tv_xiaoji.setText("￥" + price);
		Cursor getdefmsg = DBUtilities.getdefmsg();
		if (!getdefmsg.moveToFirst()) {
			ll_seleted_address.setVisibility(View.VISIBLE);
			ll_detail_address.setVisibility(View.GONE);
		} else {
			ll_seleted_address.setVisibility(View.GONE);
			ll_detail_address.setVisibility(View.VISIBLE);
			String address = getdefmsg.getString(getdefmsg
					.getColumnIndex("_address"));
			String name = getdefmsg.getString(getdefmsg
					.getColumnIndex("_truename"));
			String tell = getdefmsg.getString(getdefmsg.getColumnIndex("_tel"));
			addrid = getdefmsg.getString(getdefmsg.getColumnIndex("_id"));
			tv_show_address.setText(address);
			tv_show_name_and_tell.setText(name + " " + tell);
		}
		tv_showbuynum.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				if (tv_showbuynum.getText().toString().length() == 0) {
					return;
				}
				if (Integer.parseInt(tv_showbuynum.getText().toString()) > Integer
						.parseInt(kucun)) {
					tv_showbuynum.setText(kucun);
				}
				buynum = Integer.parseInt(tv_showbuynum.getText().toString());
				tv_nownum.setText("数量:" + buynum + "");
				// tv_showbuynum.setText(buynum+"");
				tv_all_sum.setText(buynum + "");
				allprice = (float) Math.round(Float.parseFloat(price) * 100
						* buynum) / 100;
				// cost = (float) Math.round(Float.parseFloat(cpdj) * sum * 100)
				// / 100;
				tv_xiaoji.setText(allprice + "");
				tv_all_price.setText("￥" + allprice);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {

			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_sub:
			buynum = Integer.parseInt(tv_showbuynum.getText().toString());
			if (buynum == 1) {
				ib_sub.setBackgroundResource(R.drawable.can_not_sub_anymore);
				ib_sub.setClickable(false);
			} else {
				ib_sub.setClickable(true);
				ib_sub.setBackgroundResource(R.drawable.sub);
				buynum--;
				tv_nownum.setText("数量:" + buynum + "");
			}
			tv_showbuynum.setText(buynum + "");
			tv_all_sum.setText(buynum + "");
			allprice = (float) Math.round(Float.parseFloat(price) * 100
					* buynum) / 100;
			// cost = (float) Math.round(Float.parseFloat(cpdj) * sum * 100) /
			// 100;
			tv_xiaoji.setText(allprice + "");
			tv_all_price.setText("￥" + allprice);
			break;
		case R.id.ib_add:
			buynum = Integer.parseInt(tv_showbuynum.getText().toString());
			buynum++;
			tv_showbuynum.setText(buynum + "");
			if (buynum > 1) {
				ib_sub.setClickable(true);
				ib_sub.setBackgroundResource(R.drawable.sub);
			}
			tv_nownum.setText("数量:" + buynum + "");
			allprice = (float) Math.round(Float.parseFloat(price) * 100
					* buynum) / 100;
			tv_xiaoji.setText("￥" + allprice);
			tv_all_sum.setText(buynum + "");
			tv_all_price.setText("￥" + allprice);
			break;
		case R.id.bt_buy_at_once2:
			orderRequest();
			/*
			 * Intent intent =new Intent(getBaseContext(), Paymoney.class);
			 * intent.putExtra("title", title); intent.putExtra("xiaoji",
			 * allprice+""); startActivity(intent);
			 */
			break;
		case R.id.ll_seleted_address:
			Intent intent2 = new Intent(getBaseContext(),
					SelectedAddressActivity.class);
			intent2.putExtra("istemai", true);
			intent2.putExtra("request", 1);
			startActivityForResult(intent2, 1);
			break;
		case R.id.ll_detail_address:
			Intent intent3 = new Intent(getBaseContext(),
					SelectedAddressActivity.class);
			intent3.putExtra("istemai", true);
			intent3.putExtra("request", 1);
			startActivityForResult(intent3, 1);
			// finish();
			break;
		case R.id.back:
			finish();
			break;
		case R.id.ll_youhuiquan:
			Intent intent = new Intent(this, DiscountCoupon.class);
			intent.putExtra("shopid", "0,"+shopid);
			intent.putExtra("ordje", allprice+","+allprice );
			startActivityForResult(intent, 1);
			break;
		default:
			break;
		}

	}

	private void loadUserAddress() {
		NetAsync task_getAddress = new NetAsync(D.API_CPORD_GETADDRESS, this) {

			@Override
			public Object processDataInBackground(JsonElement elData) {
				Type type = new TypeToken<UserAddress[]>() {
				}.getType();
				UserAddress[] addressList = JsonUtilities.parseModelByType(
						elData, type);
				Model.delete(UserAddress.class);
				Model.save(addressList);
				return addressList;
			}

			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
			}
		};
		task_getAddress.execute();
	}

	private void orderRequest() {
		if (tv_showbuynum.getText().toString().length() == 0) {
			tv_showbuynum.setText("1");
		}

		/*
		 * Cursor getdefmsg = DBUtilities.getdefmsg();
		 * if(getdefmsg.moveToFirst()){ addrid =
		 * getdefmsg.getString(getdefmsg.getColumnIndex("_id")); }
		 */if (TextUtils.isEmpty(addrid)) {
			Toast.makeText(this, "您尚未填写收货地址!", 1).show();
			return;
		}

		dialog = CustomProgressDialog.createDialog(this);
		dialog.show();

		SharedPreferences pre = getSharedPreferences("zhongtuan_preference",
				Context.MODE_PRIVATE);

		final String lato = pre.getString("lat", "");
		final String cpmx = getOrderParamCpmx();
		final String lngo = pre.getString("lgn", "");
		final String custumNeed = etwb_content.getText().toString();
		NetAsync orderTask = new NetAsync(D.API_SPECIAL_CREATEORDER, this) {
			@Override
			public Object processDataInBackground(JsonElement elData) {
				Type type = new TypeToken<Map<String, String>>() {
				}.getType();
				Map<String, String> item = JsonUtilities.parseModelByType(
						elData, type);
				String ordno = item.get("ordno");
				return ordno;
			}

			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				params.add(new BasicNameValuePair("addrid", addrid));// addrid/地址ID
				params.add(new BasicNameValuePair("paym", ""));// paym/支付方式
				params.add(new BasicNameValuePair("sendm", ""));// sendm/送货方式
				params.add(new BasicNameValuePair("fapiao", ""));// fapiao/发票抬头
				params.add(new BasicNameValuePair("lngo", lngo));// lngo/经度
				params.add(new BasicNameValuePair("lato", lato));// lato/纬度
				params.add(new BasicNameValuePair("shop", shopid));// shop/商铺ID
																	// json
				params.add(new BasicNameValuePair("cpmx", cpmx));// cpmx/商品数据(商铺ID:商品id=>(cpmc=名称,cppic=图片,sl=数量,dj=价格,je=金额))
																	// json
				params.add(new BasicNameValuePair("shopmess", custumNeed));// 订单备注
				if(tmqno!=null){
					
					params.add(new BasicNameValuePair("tmqno", tmqno));// 优惠券号
				}
				if(tuanbi!=null){
					
					params.add(new BasicNameValuePair("tbmoney", tuanbi.getOrdtbmoney()));// 团币money
				}

			}
		};
		orderTask.execute();
	}

	public String getOrderParamCpmx() {
		LinkedList<HashMap<String, Object>> shopList = new LinkedList<HashMap<String, Object>>();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("cpmc", title);
		map.put("cppic", cppic);
		map.put("sl", buynum + "");
		map.put("dj", price);
		map.put("je", allprice + "");
		if (TextUtils.isEmpty(chima)) {
			tv_size.setVisibility(View.GONE);
			map.put("cm", "");
		} else {
			tv_size.setText("尺码：" + chima + "【温馨提醒内容】");
			map.put("cm", tmcid);
		}
		System.out.println("chima" + chima);
		HashMap<String, Map> shopItem = new HashMap<String, Map>();
		shopItem.put(productId, map);
		HashMap<String, Object> shopmap = new HashMap<String, Object>();
		shopmap.put(shopid, shopItem);
		shopList.add(shopmap);
		Gson gson = new Gson();
		String targetJson = gson.toJson(shopList);
		String result = StringUtilities.removeTargetInString(targetJson,
				new char[] { '[', ']' });
		System.out.println("result:" + result);
		return result;
	}

	@Override
	public void onResultError(String reqUrl, String errMsg) {
		if (dialog != null) {
			dialog.dismiss();
		}
		switch (reqUrl) {
		case D.ORDER_TUANBI_MONEY:
			cb_selet_tuanbi.setChecked(false);
			break;

		default:
			break;
		}
		Toast.makeText(getApplicationContext(), errMsg, Toast.LENGTH_SHORT)
				.show();

	}

	@Override
	public void onResultSuccess(String reqUrl, Object data) {

		switch (reqUrl) {
		case D.API_CPORD_GETADDRESS:
			UserAddress[] addressList = (UserAddress[]) data;
			showaddressmsg(addressList);
			break;
		case D.API_SPECIAL_CREATEORDER:
			dialog.dismiss();
			String ordno = (String) data;
			Intent intent = new Intent(getBaseContext(), Paymoney.class);
			// intent.putExtra("productname", title);
			intent.putExtra("cpje",  calulatemoney()+ "");
			intent.putExtra("ordno", ordno);
			// intent.putExtra("sum", buynum);
			// intent.putExtra("id", productId);
			// intent.putExtra("primePrice", primePrice);
			startActivity(intent);
			finish();
			break;
		case D.ORDER_TUANBI_MONEY:
			tuanbi = (Tuanbi) data;
			if(tuanbi.getOrdtbmoney()!=null){
				
			tuanbimoney = Float.parseFloat(tuanbi.getOrdtbmoney()) / 100;
			tv_tuanbi_pay.setText("可用" + tuanbi.getOrdtbmoney() + "团币" + "，抵￥ "
					+ tuanbimoney);
			}
			calulatemoney();
		default:
			break;
		}
	}
    private String calulatemoney(){
    	float quanmoney1;
    	if(quanmoney==null){
    		quanmoney1=0f;
    	}else{
    		quanmoney1=Float.parseFloat(quanmoney);
    	}
    	tv_all_price.setText("￥"+(allprice-quanmoney1-tuanbimoney)+"");
    	return (allprice-quanmoney1-tuanbimoney)+"";
    }
	@Override
	public void onTokenTimeout() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
		ZhongTuanApp.getInstance().logout(this, true);
	}

	private void showaddressmsg(UserAddress[] addressList) {
		UserAddress defaddress = null;
		for (UserAddress userAddress : addressList) {
			String isdef = userAddress.getIsdef();
			if (TextUtils.equals(isdef, "1")) {
				defaddress = userAddress;
				break;
			} else {
				break;
			}
		}
		if (defaddress == null) {
			ll_seleted_address.setVisibility(View.VISIBLE);
			ll_detail_address.setVisibility(View.GONE);
		} else {
			ll_seleted_address.setVisibility(View.GONE);
			ll_detail_address.setVisibility(View.VISIBLE);
			tv_show_address.setText(defaddress.getAddress());
			tv_show_name_and_tell.setText(defaddress.getTruename() + " "
					+ defaddress.getTel());
			addrid = defaddress.getUaid();
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case 2:
			String name = (String) data.getCharSequenceExtra("name");
			String tel = (String) data.getCharSequenceExtra("tel");
			String address = (String) data.getCharSequenceExtra("address");
			ll_seleted_address.setVisibility(View.GONE);
			ll_detail_address.setVisibility(View.VISIBLE);
			tv_show_address.setText(address);
			tv_show_name_and_tell.setText(name + " " + tel);
			Cursor cursor = DBUtilities.getitemaddressid(name, address, tel);
			if (cursor.moveToFirst()) {
				addrid = cursor.getString(cursor.getColumnIndex("_id"));
			}
			break;
		case 3:
			//Toast.makeText(this, "onactivityresult()", 0).show();
			tmqno = data.getStringExtra("tmqno");
			quanmoney = data.getStringExtra("quanmoney");
			tv_youhuimoney.setText("￥" + quanmoney);
			calulatemoney();
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
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			tuanbipay();
		} else {
			tv_tuanbi_pay.setText("可用0团币" + "，抵￥ 0");
			tuanbimoney=0f;
			calulatemoney();
		}

	}

	private void tuanbipay() {
		NetAsync orderTask = new NetAsync(D.ORDER_TUANBI_MONEY, this) {
			@Override
			public Object processDataInBackground(JsonElement elData) {
				Type type = new TypeToken<Tuanbi>() {
				}.getType();
				Tuanbi tuanbi = JsonUtilities.parseModelByType(elData, type);
				return tuanbi;
			}

			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				params.add(new BasicNameValuePair("tmid", productId));// addrid/地址ID

			}
		};
		orderTask.execute();
	}
}
