package com.iteambuysale.zhongtuan.model;

import com.iteambuysale.zhongtuan.annotation.Column;
import com.iteambuysale.zhongtuan.annotation.Table;

@Table(name = "Shop_Msg")
public class ShopMsg extends Model {
	@Column(name = "_id" , primary = true)
	private String sid;						//唯一id
	@Column(name = "_address", len = 20)			
	private String address; 						//商店地址
	@Column(name = "_attens", len = 20)			
	private String attens; 
	@Column(name = "_bests", len = 20)			
	private String bests; 
	
	@Column(name = "_carea", len = 20)			
	private String carea; 
	
	@Column(name = "_city", len = 20)			
	private String city; 
	
	@Column(name = "_clb", len = 20)			
	private String clb; 
	
	@Column(name = "_collects", len = 20)			
	private String collects; 
	
	@Column(name = "_compid", len = 20)			
	private String compid; 
	
	@Column(name = "_cup", len = 20)			
	private String cup; 
	
	@Column(name = "_lato", len = 20)			
	private String lato; 
	
	@Column(name = "_lngo", len = 20)			
	private String lngo; 
	
	@Column(name = "_models", len = 20)			
	private String models; 
	
	@Column(name = "_perfee", len = 20)			
	private String perfee; 
	
	@Column(name = "_picurl", len = 20)			
	private String picurl; 
	
	@Column(name = "_province", len = 20)			
	private String province; 
	
	@Column(name = "_shopname", len = 20)			
	private String shopname; 
	
	@Column(name = "_tel", len = 20)			
	private String tel; 
	
	@Column(name = "_urlqrcode", len = 20)			
	private String urlqrcode; 
	
	@Column(name = "_useradmin", len = 20)			
	private String useradmin; 
	
	@Column(name = "_userlog", len = 20)			
	private String userlog; 
	
	@Column(name = "_zfen", len = 20)			
	private String zfen;
	@Column(name = "_tgbz", len = 20)	//推广标志		
	private String tgbz;
	@Column(name = "_tgword", len = 20)	//推广文字		
	private String tgword;
	@Column(name = "_adpic", len = 20)	//推广图片		
	private String adpic;
	@Column(name = "_xh", len = 20)	//序号		
	private String xh;

	



	public String getTgbz() {
		return tgbz;
	}

	public void setTgbz(String tgbz) {
		this.tgbz = tgbz;
	}

	public String getTgword() {
		return tgword;
	}

	public void setTgword(String tgword) {
		this.tgword = tgword;
	}

	public String getAdpic() {
		return adpic;
	}

	public void setAdpic(String adpic) {
		this.adpic = adpic;
	}

	public String getXh() {
		return xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAttens() {
		return attens;
	}

	public void setAttens(String attens) {
		this.attens = attens;
	}

	public String getBests() {
		return bests;
	}

	public void setBests(String bests) {
		this.bests = bests;
	}

	public String getCarea() {
		return carea;
	}

	public void setCarea(String carea) {
		this.carea = carea;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getClb() {
		return clb;
	}

	public void setClb(String clb) {
		this.clb = clb;
	}

	public String getCollects() {
		return collects;
	}

	public void setCollects(String collects) {
		this.collects = collects;
	}

	public String getCompid() {
		return compid;
	}

	public void setCompid(String compid) {
		this.compid = compid;
	}

	public String getCup() {
		return cup;
	}

	public void setCup(String cup) {
		this.cup = cup;
	}

	public String getLato() {
		return lato;
	}

	public void setLato(String lato) {
		this.lato = lato;
	}

	public String getLngo() {
		return lngo;
	}

	public void setLngo(String lngo) {
		this.lngo = lngo;
	}

	public String getModels() {
		return models;
	}

	public void setModels(String models) {
		this.models = models;
	}

	public String getPerfee() {
		return perfee;
	}

	public void setPerfee(String perfee) {
		this.perfee = perfee;
	}

	public String getPicurl() {
		return picurl;
	}

	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getShopname() {
		return shopname;
	}

	public void setShopname(String shopname) {
		this.shopname = shopname;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getUrlqrcode() {
		return urlqrcode;
	}

	public void setUrlqrcode(String urlqrcode) {
		this.urlqrcode = urlqrcode;
	}

	public String getUseradmin() {
		return useradmin;
	}

	public void setUseradmin(String useradmin) {
		this.useradmin = useradmin;
	}

	public String getUserlog() {
		return userlog;
	}

	public void setUserlog(String userlog) {
		this.userlog = userlog;
	}

	public String getZfen() {
		return zfen;
	}

	public void setZfen(String zfen) {
		this.zfen = zfen;
	} 
	
	
	
	
	
}
