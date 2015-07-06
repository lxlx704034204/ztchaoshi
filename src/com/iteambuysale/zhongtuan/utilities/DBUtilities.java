package com.iteambuysale.zhongtuan.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.model.UserAddress;

public class DBUtilities {

	public static ZhongTuanApp mInstance = ZhongTuanApp.getInstance();

	/**
	 * TABLE CITY & PROVINCE
	 * 
	 * @param context
	 * @param id
	 * @return
	 */
	public static String getcityIdByName(String cityName) {
		SQLiteDatabase db = mInstance.getPcdDB();
		Cursor cr =db.query(D.DB_TABLE_CITY, null, D.DB_CITY_COL_NAME + " like ?",new String[] { "%"+cityName+"%" }, null, null, null);
		String cityId = null;
		if (cr.moveToFirst()) {
			cityId = cr.getString(cr.getColumnIndex(D.DB_CITY_COL_ID));
		}
		cr.close();
		return cityId;
	}

	public static String getcityCodeByName(Context context,String cityName) {
		SQLiteDatabase db = mInstance.getPcdDB();
		Cursor cr =db.query(D.DB_TABLE_CITY, null, D.DB_CITY_COL_NAME + " like ?",new String[] { "%"+cityName+"%" }, null, null, null);
		String cityCode = null;
		if (cr.moveToFirst()) {
			cityCode = cr.getString(cr.getColumnIndex(D.DB_CITY_COL_CITYCODE));
		}
		cr.close();
		return cityCode;
	}

	public static Cursor getCityByProvinceId(Context context,String id) {
		SQLiteDatabase db = mInstance.getPcdDB();
		return db.query(D.DB_TABLE_CITY, new String[] { D.DB_CITY_COL_ID, D.DB_CITY_COL_NAME },
				D.DB_CITY_COL_PROVINCE_ID + " = ?", new String[] { id }, null, null, null);
	}

	public static Cursor getProvince(Context context) {
		SQLiteDatabase db = mInstance.getPcdDB();
		return db.query(D.DB_TABLE_PROVINCE, null, null, null, null, null, null);
	}


	public static Cursor getAddressList() {
		SQLiteDatabase db = mInstance.getRDB();
		return db.query(D.DB_TABLE_ADDRESS_LIST, new String[] { D.DB_ADDRESS_LIST_COL_UAID,
				D.DB_ADDRESS_LIST_COL_TRUENAME, D.DB_ADDRESS_LIST_COL_TEL, D.DB_ADDRESS_LIST_COL_ADDRESS }, null, null,
				null, null, D.DB_ADDRESS_LIST_COL_UAID + " desc");

	}
   public static Cursor getdefmsg(){
	   SQLiteDatabase db = mInstance.getRDB();
	   return db.query(D.DB_TABLE_ADDRESS_LIST, new String[]{"*"}, "_isdef=?", new String[]{ "1" }, null, null, null);
   }
   public static Cursor getitemaddressid(String name,String address,String tel){
	   SQLiteDatabase db = mInstance.getRDB();
	   return db.query(D.DB_TABLE_ADDRESS_LIST, new String[]{"*"}, "_truename=? and _tel=? and _address=?", new String[]{name,tel,address }, null, null, null);
   }
	public static UserAddress getDefaultAddress() {
		UserAddress address = null;
		SQLiteDatabase db = mInstance.getRDB();
		Cursor cr =
				db.query(D.DB_TABLE_ADDRESS_LIST, new String[] { D.DB_ADDRESS_LIST_COL_UAID,
						D.DB_ADDRESS_LIST_COL_TRUENAME, D.DB_ADDRESS_LIST_COL_ADDRESS, D.DB_ADDRESS_LIST_COL_TEL },
						D.DB_ADDRESS_LIST_COL_ISDEF + "=?", new String[] { "1" }, null, null, null);
		if (cr.moveToFirst()) {
			address = new UserAddress();
		}
		for (cr.moveToFirst(); !cr.isAfterLast(); cr.moveToNext()) {
			address.setUaid(cr.getString(cr.getColumnIndex(D.DB_ADDRESS_LIST_COL_UAID)));
			address.setTruename(cr.getString(cr.getColumnIndex(D.DB_ADDRESS_LIST_COL_TRUENAME)));
			address.setTel(cr.getString(cr.getColumnIndex(D.DB_ADDRESS_LIST_COL_TEL)));
			address.setAddress(cr.getString(cr.getColumnIndex(D.DB_ADDRESS_LIST_COL_ADDRESS)));
		}
		cr.close();
		return address;
	}


	public static Cursor getProductList(int limitNum) {
		SQLiteDatabase db = mInstance.getRDB();
		return db.query(D.DB_TABLE_PRODUCT_LIST, null, null, null, null, null, D.DB_PRODUCT_SELLS
				+ " desc",limitNum+"");

	}
	
	public static Cursor getProductList() {
		SQLiteDatabase db = mInstance.getRDB();
		return db.query(D.DB_TABLE_PRODUCT_LIST, null, null, null, null, null, D.DB_PRODUCT_SELLS
				+ " desc");

	}
	/*public static Cursor getoneProductmsg() {
		SQLiteDatabase db = mInstance.getRDB();
		return db.query(D.DB_TABLE_PRODUCT_LIST, new String[]{"*" }, null, null, null, null,null);
	}*/

	public static Cursor getStoreList() {
		SQLiteDatabase db = mInstance.getRDB();
		return db.query(D.DB_TABLE_STORE_LIST, null, null, null, null, null,D.DB_STORE_LIST_DISTANCE+" asc");

	}
	public static void deleteStoreList() {
		SQLiteDatabase db = mInstance.getRDB();
		db.delete(D.DB_TABLE_STORE_LIST,null, null);

	}

	public static Cursor getTemaiList() {
		SQLiteDatabase db = mInstance.getRDB();
		return db.query(D.DB_TABLE_TEMAI_LIST,null, null, null, null, null,
				"_xh asc,_sdate desc,_id desc");
	}
    public static Cursor getTeamVersonList(){
    	SQLiteDatabase db = mInstance.getRDB();
		return db.rawQuery("select * from TEMAI_LIST_VERSON order by _xh asc", null);
    }
    /**
     * 按日期排列区前四个特卖
     * @param shopid
     * @return
     */
    public static Cursor getShopProductList(String  shopid){
    	SQLiteDatabase db = mInstance.getRDB();
    	  Cursor cr=db.rawQuery("select * from TEMAI_LIST_VERSON where _shopid=? order by _edate desc limit 0,4", new String[]{shopid});
    	return cr;
    }
    /**
     * 按照相应的要求排序
     * @param shopid
     * @param order
     * @return
     */
    static boolean is=true;
    public static Cursor getShopProductOrderBy(String  shopid,String order){
    	if (shopid==null) {
			return null;
		}
    	if (order==null) {
			order="_id desc";
			
			
			
		}
    	SQLiteDatabase db = mInstance.getRDB();
    	String str="select * from TEMAI_LIST_VERSON where _shopid="+shopid+" order by "+order;
    	//System.out.println("select * from TEMAI_LIST_VERSON where _shopid="+shopid+" order by "+order);
    	return db.rawQuery(str, new String []{});
    	
    	 
    }

    /**
     * 按销量排列取前六特卖
     * @param shopid
     * @return
     */
    public static Cursor getpopularproductlist(String shopid){
    	SQLiteDatabase db = mInstance.getRDB();
  	  Cursor cr=db.rawQuery("select * from TEMAI_LIST_VERSON where _shopid=? order by _sells desc limit 0,6", new String[]{shopid});
  	  return cr;
    }
    /**
     * 模糊查询特卖信息
     * @param title
     * @return
     */
    public static Cursor getsearchmsg(String title){
    	SQLiteDatabase db = mInstance.getRDB();
    	return db.rawQuery("select * from TEMAI_LIST_VERSON where _title like ? ", new String []{ "%"+title+"%" });
    }
  /*  public static Cursor getTeamVersonList(String cup,String orderby){
    	SQLiteDatabase db = mInstance.getRDB();
		return db.query(D.DB_TABLE_TEMAI_LIST_VERSONT,null, "_cpdl=? and _cpxl=?", new String[]{cup,lbid}, null, null,
				"_id desc");
    	if (cup==null) {
			return null;
		}
    	return db.query(D.DB_TABLE_TEMAI_LIST_VERSONT,null, "_cpdl=?", new String[]{cup}, null, null,
				"_id desc");
    }*/
    public static Cursor getProductList(String tmid) {
		SQLiteDatabase db = mInstance.getRDB();
		return db.query(D.DB_TABLE_TEMAI_LIST_VERSONT, new String[] {"*"}, "_id=?", new String[]{tmid}, null, null, 
				null,null);
		

	}
   public static Cursor getSpecialSaleCatagory(String cup){
	   SQLiteDatabase db = mInstance.getRDB();
	   return db.query(D.DB_TABLE_TEMAI_CATAGORY, null, "_cup=?", new String[]{cup}, null, null, null);
   }
	public static Cursor getEventList() {
		SQLiteDatabase db = mInstance.getRDB();
		return db.query(D.DB_TABLE_EVENT_LIST, null, null, null, null, null,"_zt desc,_xh desc,_edate asc");

	}
	
	public static Cursor getTBHList() {
		SQLiteDatabase db = mInstance.getRDB();
		return db.query(D.DB_TABLE_TBH_LIST, null, null, null, null, null,"_zt desc,_xh desc,_edate asc");

	}
	public static void deleteAddress(String uaid){
		SQLiteDatabase db = mInstance.getWDB();
		db.delete(D.DB_TABLE_ADDRESS_LIST, "_id=?", new String[]{uaid});
		
	}
	public static ArrayList<Map<String, String>> getList(){
		ArrayList<Map<String,String>> list=new ArrayList<Map<String,String>>();
		SQLiteDatabase db=mInstance.getPcdDB();
		Cursor cursor=db.query(D.DB_TABLE_CITY, null, null, null, null, null,null );
		while(cursor.moveToNext()){
			Map<String,String> item=new HashMap<String,String>();
			item.put("code",cursor.getString(cursor.getColumnIndex(D.DB_CITY_COL_CODE)));
			item.put("cityName",cursor.getString(cursor.getColumnIndex(D.DB_CITY_COL_NAME)) );
			list.add(item);
		}
		cursor.close();
		return list;
	}
	public static ArrayList<String> getAutoList(){
		ArrayList<String> list=new ArrayList<String>();
		SQLiteDatabase db=mInstance.getPcdDB();
		Cursor cursor=db.query(D.DB_TABLE_CITY, null, null, null, null, null,null);
		while(cursor.moveToNext()){
			String cityName=cursor.getString(cursor.getColumnIndex(D.DB_CITY_COL_NAME));
			list.add(cityName);
		}
		cursor.close();
		return list;
		
	}

	public static Cursor gettmbiglb(boolean limit) {
		SQLiteDatabase db = mInstance.getRDB();
		if (limit) {
			return  db.rawQuery("select * from TEMAI_CATAGORY where _cup='0' limit 0,7", null);
		}else{
			return  db.rawQuery("select * from TEMAI_CATAGORY where _cup='0' ", null);
		}
	}
	public static String gettmlbname(String id){
		SQLiteDatabase db = mInstance.getRDB();
		Cursor cursor=db.rawQuery("select _lbname from TEMAI_CATAGORY where _id=?", new String[]{id});
		if(cursor.moveToFirst()){
			return cursor.getString(0);
		}
		cursor.close();
		return null;
	}
	/**
	 * 获取特卖商品的信息
	 * @param shopId
	 */
	public static Cursor  getShopmsg(String shopId) {
		SQLiteDatabase db = mInstance.getRDB();
		return db.rawQuery("select * from Shop_Msg where _id=?",new String[]{shopId});
		
	}

	public static void deletecollection(String string) {
		SQLiteDatabase db = mInstance.getRDB();
		db.delete("COLLECTION_LIST", "_id=?", new String[]{string});
		
	}

	public static String getCollectionLbid(String string) {
		SQLiteDatabase db = mInstance.getRDB();
		Cursor cursor = db.rawQuery("select _lbid from COLLECTION_LIST where _id=? ", new String []{string});
		if(cursor.moveToFirst()){
			return cursor.getString(cursor.getColumnIndex("_lbid"));
		}
		cursor.close();
		return null;
	}

	public static Cursor getrecmByuid(String phoneNumber, boolean istemai) {
		SQLiteDatabase db = mInstance.getRDB();
		if (istemai) {
			
			return db.rawQuery("select * from TEMAIEVALUATION_LIST where _username=? order by _id desc", new String []{phoneNumber});
		}else{
			return db.rawQuery("select * from EVALUATION_LIST where _username=? order by _id desc", new String []{phoneNumber});
		}
		
	}

	public static Cursor getTeamVersonList(String cup, String order) {
		if (cup==null) {
			return null;
		}
		if(order==null){
			order="_id desc";
		}
		SQLiteDatabase db = mInstance.getRDB();
    	return db.query(D.DB_TABLE_TEMAI_LIST_VERSONT,null, "_cpdl=?", new String[]{cup}, null, null,
				order);
	}

	public static Cursor getSplAdvMsg(String type ,String id) {
		SQLiteDatabase db = mInstance.getRDB();
		if(id==null){
			return db.rawQuery("select * from ADVERT_MSG where _advtype=? ", new String []{type});
		}
    	return db.rawQuery("select * from ADVERT_MSG where _advtype=? and _id=?", new String []{type,id});
		
	}
	public static Cursor getallAdvMsg(){
		SQLiteDatabase db = mInstance.getRDB();
    	return db.rawQuery("select * from ADVERT_MSG ", new String []{});
		
	}

	public static Cursor getsomeTmproduct(String cpid,String orderby ) {
		String []str=cpid.split(",");
		StringBuilder sql=new StringBuilder("select * from TEMAI_LIST_VERSON where ");
		
		for(int i=0;i<str.length;i++){
			sql.append(" _id=? or");
		}
		/*if(TextUtils.isEmpty(orderby)){
			orderby="_id "
		}*/
		String sqls=(String) sql.subSequence(0, sql.lastIndexOf("or"));
		if(!TextUtils.isEmpty(orderby)){
			sqls=sqls+" order by"+orderby;
		}
		//System.out.println("sql:"+sqls+"\n str.length:"+str.length);
		SQLiteDatabase db = mInstance.getRDB();
		return db.rawQuery(sqls , str);
	}

	public static Cursor getallShop() {
		SQLiteDatabase db =mInstance.getRDB();
		return db.rawQuery("select * from Shop_Msg order by _xh desc,_id desc ", null);
	}
	/**
	 * 从数据库中读取未付款的订单
	 * @return
	 */
	public static Cursor getUnpayOrder(){
		SQLiteDatabase db =mInstance.getRDB();
		return  db.query("ORDER_TM_LIST", null, "_ordzt = ?",
				new String[] { "0" }, null, null, "_id desc");
	}
	/**
	 * 根据订单号查询产品
	 * @param orderno
	 * @return
	 */
	public static Cursor getOrderByoderNumber(String orderno){
		SQLiteDatabase db =mInstance.getRDB();
	    return db.rawQuery("select * from ORDER_TM_LIST where _ordno =? order by _id desc", new String[]{orderno});
	}
	/**从ORDER_DETAILS_TM表里面获取 订单号为orderno的子订单号
	 * @param orderno
	 * @return
	 */
	public static Cursor  getcpmx(String orderno){
		SQLiteDatabase db =mInstance.getRDB();
	    return db.rawQuery("select * from ORDER_DETAILS_TM where _ordnox like ? order by _id desc", new String[]{orderno+"%"});
	
	} 
	

}
