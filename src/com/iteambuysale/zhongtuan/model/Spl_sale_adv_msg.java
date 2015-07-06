package com.iteambuysale.zhongtuan.model;

import java.util.ArrayList;
import java.util.Map;

import com.iteambuysale.zhongtuan.annotation.Column;

public class Spl_sale_adv_msg {
	//private Adv  adv;
	//private ArrayList<ArrayList<String []>> advs;
	//private Map<String ,Arraylist<string[]>> adv;
	private Map<String , ArrayList<String []>> adv;

	public Map<String, ArrayList<String[]>> getAdv() {
		return adv;
	}

	public void setAdv(Map<String, ArrayList<String[]>> adv) {
		this.adv = adv;
	} 
	
	
	/*public  class Adv {
		private ArrayList<Cxzad> cxzad;
		private ArrayList<Zspad> zspad;
		public ArrayList<Cxzad> getCxzad() {
			return cxzad;
		}
		public void setCxzad(ArrayList<Cxzad> cxzad) {
			this.cxzad = cxzad;
		}
		public ArrayList<Zspad> getZspad() {
			return zspad;
		}
		public void setZspad(ArrayList<Zspad> zspad) {
			this.zspad = zspad;
		}
		
	}
	public class Cxzad {
		private  ArrayList<String[]>  str;

		public ArrayList<String[]> getStr() {
			return str;
		}

		public void setStr(ArrayList<String[]> str) {
			this.str = str;
		}
       
	
		
	}
	
	public class Zspad{
		private ArrayList<String []> str;

		public ArrayList<String[]> getStr() {
			return str;
		}

		public void setStr(ArrayList<String[]> str) {
			this.str = str;
		}

		
		
	}*/
}
