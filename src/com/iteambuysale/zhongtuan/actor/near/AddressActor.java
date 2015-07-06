package com.iteambuysale.zhongtuan.actor.near;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout;

import com.google.gson.JsonElement;
import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.actor.SuperActor;
import com.iteambuysale.zhongtuan.background.NetAsync;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.near.ChooseInfoListener;
import com.iteambuysale.zhongtuan.model.Model;
import com.iteambuysale.zhongtuan.model.User;
import com.iteambuysale.zhongtuan.model.UserAddress;
import com.iteambuysale.zhongtuan.utilities.DBUtilities;
import com.iteambuysale.zhongtuan.utilities.JsonUtilities;
import com.iteambuysale.zhongtuan.utilities.StringUtilities;
import com.iteambuysale.zhongtuan.utilities.VerificationUtilities;

public class AddressActor extends SuperActor implements OnItemSelectedListener{
	
	private SimpleCursorAdapter province_adapter;
	private SimpleCursorAdapter city_adapter;
	private SimpleCursorAdapter addressList_adapter;

	private Context mContext;
	private ChooseInfoListener mListener;
	private LinearLayout mFooterView;
	
	public AddressActor(Context context){
		super(context);
		mContext = context;
		mListener = (ChooseInfoListener)context;
	}

	/**
	 * 初始化布局
	 */
	public void initView(){
		initTitleBar(D.BAR_SHOW_LEFT, "选择收货地址");
		initFooterView();
		initListView();
		initProvinceSpinner();
	}
	
	/**
	 * 刷新UserAddress
	 * @param address
	 */
	public void ReflashAddressList(UserAddress address) {
		setCurrentView(mFooterView);
		address.setTruename(getReciverName());
		address.setTel(getPhone());
		address.setAddress(getStreetAddress());
		address.save();
		updateList();
		clearNewAddressWidgetContent();
		toogle();
		setCurrentView($v(mContext));
	}

	/**
	 * 添加新的地址
	 */
	public void addNewAddress(){
		setCurrentView(mFooterView);
		if(!VerificationUtilities.validateStringNotNull(getPhone())){
			mListener.onResultError(D.API_CPORD_NEWADDRESS, D.ERROR_MSG_CHOOSE_NAME_NULL);
			return;
		}
		if(!VerificationUtilities.validatePassword(getReversePhone())){
			mListener.onResultError(D.API_CPORD_NEWADDRESS, D.ERROR_MSG_PHONE_NOTMATCH);
			return;
		}
		if(!VerificationUtilities.validateZipCode(getZipCode())){
			mListener.onResultError(D.API_CPORD_NEWADDRESS, D.ERROR_MSG_CHOOSE_ZIPCODE_NOT_MATCH);
			return;
		}
		if(!VerificationUtilities.validateStringNotNull(getStreetAddress())){
			mListener.onResultError(D.API_CPORD_NEWADDRESS, D.ERROR_MSG_CHOOSE_ADDRESS_NULL);
			return;
		}
		NetAsync task_addAddress = new NetAsync(D.API_CPORD_NEWADDRESS,mListener) {
			
			@Override
			public Object processDataInBackground(JsonElement elData) {
				UserAddress address = JsonUtilities.parseModelByType(elData, UserAddress.class);
				return address;
			}
			
			@Override
			public void beforeRequestInBackground(List<NameValuePair> params) {
				params.add(new BasicNameValuePair(D.ARG_CHOOSE_TRUENAME, getReciverName()));
				params.add(new BasicNameValuePair(D.ARG_CHOOSE_TEL, getPhone()));
				params.add(new BasicNameValuePair(D.ARG_CHOOSE_ZIPCODE, getZipCode()));
				params.add(new BasicNameValuePair(D.ARG_CHOOSE_PROVINCE, getProvinceId()));
				params.add(new BasicNameValuePair(D.ARG_CHOOSE_CITY, getCityId()));
				params.add(new BasicNameValuePair(D.ARG_CHOOSE_ADDRESS, getStreetAddress()));
				params.add(new BasicNameValuePair(D.ARG_CHOOSE_ISDEF, getIsDefault()));				
			}
		};
		task_addAddress.execute();
		setCurrentView($v(mContext));
	}
	
	/**
	 * mark 下checkbox的选择情况
	 */
	public void markDownDefCheck(){
		setCurrentView(mFooterView);
		if ($cb("def").isChecked()){
			$cb("def").setTag("1");
		}else{
			$cb("def").setTag("0");
		}
		setCurrentView($v(mContext));
	}
	
	/**
	 * 更迭按钮隐藏与显示
	 */
	public void toogle() {
		ToggleVisiable(new String[]{"newAddress","addressArea"});
	}
	
	/*================================== Spinner Events ===================================*/
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		setCurrentView(mFooterView);
		switch (parent.getId()) {
		case R.id.province:
			$sp("province").setTag(String.valueOf(id));
			initCitySpinner((int)id);
			break;
		case R.id.city:
			$sp("city").setTag(String.valueOf(id));
			break;
		default:
			break;
		}
		setCurrentView($v(mContext));
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		
	}
	
	/*================================================ Helpers ===================================================*/
	/**
	 * 初始化listView
	 */
	private void initListView(){
		$lv("addressList").addFooterView(mFooterView);
		String[] from = new String[]{D.DB_ADDRESS_LIST_COL_TRUENAME,D.DB_ADDRESS_LIST_COL_TEL,D.DB_ADDRESS_LIST_COL_ADDRESS};
		int[] to = new int[]{R.id.tv_choose_item_name,R.id.tv_choose_item_phone,R.id.tv_choose_item_address};
		Cursor cr = DBUtilities.getAddressList();
		addressList_adapter = new SimpleCursorAdapter(mContext, R.layout.x_list_address, cr, from, to,0);
		$lv("addressList").setAdapter(addressList_adapter);
		$lv("addressList").setOnItemClickListener(mListener);
	}
	
	/**
	 * 初始化footerview
	 */
	private void initFooterView(){
		mFooterView = new LinearLayout(mContext);
		mFooterView.setOrientation(LinearLayout.VERTICAL);
		LayoutInflater.from(mContext).inflate(R.layout.x_block_new_add, mFooterView);
		
		String uid = ZhongTuanApp.getInstance().getAppSettings().uid;
		User user = Model.load(new User(), uid);
		setCurrentView(mFooterView);
		$et("name").setText(user.getNickname());
		$et("phone").setText(user.getMobile());
		$ll("addressArea").setVisibility(View.GONE);
		setCurrentView($v(mContext));
	}
	
	/**
	 * 初始化省份列表
	 */
	private void initProvinceSpinner(){
		setCurrentView(mFooterView);
		String[] from = new String[]{D.DB_PROVINCE_COL_NAME};
		int[] to = new int[]{R.id.tv_address_spinner_item};
		Cursor cr = DBUtilities.getProvince(mContext);
		province_adapter = new SimpleCursorAdapter(mContext,R.layout.x_spinner_add_item, cr, from, to,0);
		$sp("province").setAdapter(province_adapter);
		$sp("province").setSelection(4);
		$sp("province").setOnItemSelectedListener(this);
		setCurrentView($v(mContext));
	}
	
	/**
	 * 城市列表初始化
	 * @param provinceId
	 */
	private void initCitySpinner(int provinceId){
		setCurrentView(mFooterView);
		String[] from = new String[]{D.DB_CITY_COL_NAME};
		int[] to = new int[]{R.id.tv_address_spinner_item};
		Cursor cr = DBUtilities.getCityByProvinceId(mContext, String.valueOf(provinceId));
		city_adapter = new SimpleCursorAdapter(mContext, R.layout.x_spinner_add_item, cr, from, to, 0);
		$sp("city").setAdapter(city_adapter);
		$sp("city").setOnItemSelectedListener(this);
		setCurrentView($v(mContext));
	}
	
	/**
	 * 获取反序手机号码
	 * @return
	 */
	private String getReversePhone(){
		return StringUtilities.getReverseString($et("phone").getText().toString());
	}
	
	/**
	 *  获取收货人名称
	 * @return
	 */
	private String getReciverName() {
		return $et("name").getText().toString();
	}

	/**
	 *  获取手机号码
	 * @return
	 */
	private String getPhone() {
		return $et("phone").getText().toString();
	}

	/**
	 *  获取街道地址
	 * @return
	 */
	private String getStreetAddress() {
		return $et("address").getText().toString();
	}

	/**
	 *  获取邮政编码
	 * @return
	 */
	private String getZipCode() {
		return $et("zipCode").getText().toString();
	}
	
	/**
	 * 获取省份id
	 * @return
	 */
	private String getProvinceId(){
		setCurrentView(mFooterView);
		String id = (String)$sp("province").getTag();
		setCurrentView($v(mContext));
		return id;
	}
	
	/**
	 * 获取城市id
	 * @return
	 */
	private String getCityId(){
		setCurrentView(mFooterView);
		String id = (String)$sp("city").getTag();
		setCurrentView($v(mContext));
		return id;
	}
	
	/**
	 * 返回是否选择了默认地址
	 * @return
	 */
	private String getIsDefault(){
		setCurrentView(mFooterView);
		String isDef = (String)$cb("def").getTag();
		setCurrentView($v(mContext));
		return isDef;
	}
	
	/**
	 * 更新列表
	 */
	private void updateList(){
		Cursor oldcr = addressList_adapter.getCursor();
		Cursor newcr = DBUtilities.getAddressList();
		addressList_adapter.changeCursor(newcr);
		oldcr.close();
		addressList_adapter.notifyDataSetChanged();
	}
	

	/**
	 * 恢复默认选择
	 */
	private void clearNewAddressWidgetContent() {
		$et("address").setText("");
		$cb("def").setChecked(true);
	}
}
