package com.iteambuysale.zhongtuan.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.iteambuysale.zhongtuan.utilities.StringUtilities;

public class OrderCreate {
	private HashMap<String, ArrayList<Product>> shopMap = new HashMap<String, ArrayList<Product>>();
	Gson gson = new Gson();
	
	// 初始化订单	
	public OrderCreate(List<Product> products) {
		for (int i = 0; i < products.size(); i++) {
			Product product = products.get(i);
			String key = products.get(i).getShopid();

			ArrayList<Product> productList = shopMap.get(key);
			if (null == productList) {
				// key value not exists
				productList = new ArrayList<Product>();
				shopMap.put(key, productList);
				productList = shopMap.get(key);
			}
			productList.add(product);
		}
	}

	// 返回订单cpmx的json参数
	public String getOrderParamCpmx() {
		LinkedList<HashMap<String, Object>> shopList = new LinkedList<HashMap<String, Object>>();
		for (String key : shopMap.keySet()) {
			List<Product> products = shopMap.get(key);
			
			
			LinkedList<HashMap<String, Item>> productList = new LinkedList<HashMap<String, Item>>();
			for (int i=0; i<products.size();i++){
				Product product = products.get(i);
				HashMap<String, Item> productItem = new HashMap<String, Item>();
				productItem.put(product.getMid(),new Item(product.getCpmc(),"1",product.getNumDj2(),product.getPicurl()));
				productList.add(productItem);
			}
			HashMap<String, Object> shopItem = new HashMap<String, Object>();
			shopItem.put(key, productList);
			shopList.add(shopItem);
		}
		String targetJson = gson.toJson(shopList);
		String result = StringUtilities.removeTargetInString(targetJson,new char[]{'[',']'});
		return result;
	}
	
	// 返回订单的shop json参数
	public String getOrderParamShop(){
		ArrayList<String> shopIdList = new ArrayList<String>();
		for (String key:shopMap.keySet()){
			shopIdList.add(key);
		}		
		String targetJson = gson.toJson(shopIdList);
		return targetJson;
	}

}

class Item {
	private String cpmc;
	private String sl;
	private String dj;
	private String je;
	private String cppic;

	public Item(String itemName, String itemNum, String itemPrice,String itemCppic) {
		super();
		this.cpmc = itemName;
		this.sl = itemNum;
		this.dj = itemPrice;
		this.je = String.valueOf(Integer.valueOf(sl)*Float.valueOf(dj));
		this.setCppic(itemCppic);
	}

	public String getCpmc() {
		return cpmc;
	}

	public void setCpmc(String cpmc) {
		this.cpmc = cpmc;
	}

	public String getSl() {
		return sl;
	}

	public void setSl(String sl) {
		this.sl = sl;
	}

	public String getDj() {
		return dj;
	}

	public void setDj(String dj) {
		this.dj = dj;
	}

	public String getJe() {
		return je;
	}

	public void setJe(String je) {
		this.je = je;
	}

	public String getCppic() {
		return cppic;
	}

	public void setCppic(String cppic) {
		this.cppic = cppic;
	}
}
