package com.iteambuysale.zhongtuan.model;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class ShopCart implements Parcelable{
  private String buynums;
  private String cmid;
  private String cpmc;
  private String cppic;
  private String ctid;
  private String dj;
  private String je;
  private String mid;
  private String shopid;
  private String sl;
  private String username;
  private String shopname;
  private String chima;
public String getChima() {
	return chima;
}
public void setChima(String chima) {
	this.chima = chima;
}
public String getShopname() {
	return shopname;
}
public void setShopname(String shopname) {
	this.shopname = shopname;
}
public String getBuynums() {
	return buynums;
}
public void setBuynums(String buynums) {
	this.buynums = buynums;
}
public String getCmid() {
	return cmid;
}
public void setCmid(String cmid) {
	this.cmid = cmid;
}
public String getCpmc() {
	return cpmc;
}
public void setCpmc(String cpmc) {
	this.cpmc = cpmc;
}
public String getCppic() {
	return cppic;
}
public void setCppic(String cppic) {
	this.cppic = cppic;
}
public String getCtid() {
	return ctid;
}
public void setCtid(String ctid) {
	this.ctid = ctid;
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
public String getMid() {
	return mid;
}
public void setMid(String mid) {
	this.mid = mid;
}
public String getShopid() {
	return shopid;
}
public void setShopid(String shopid) {
	this.shopid = shopid;
}
public String getSl() {
	return sl;
}
public void setSl(String sl) {
	this.sl = sl;
}
public String getUsername() {
	return username;
}
public void setUsername(String username) {
	this.username = username;
}
@Override
public int describeContents() {
	// TODO Auto-generated method stub
	return 0;
}
@Override
public void writeToParcel(Parcel dest, int flags) {
	dest.writeString(buynums);
	dest.writeString(cmid);
	dest.writeString(cpmc);
	dest.writeString(cppic);
	dest.writeString(ctid);
	dest.writeString(dj);
	dest.writeString(je);
	dest.writeString(mid);
	dest.writeString(shopid);
	dest.writeString(sl);
	dest.writeString(username);
	dest.writeString(shopname);
	dest.writeString(chima);
}
public static final Parcelable.Creator<ShopCart> CREATOR = new Parcelable.Creator<ShopCart>(){

	@Override
	public ShopCart createFromParcel(Parcel source) {
	   ShopCart  shopcart=new ShopCart();
	    shopcart.buynums=source.readString();
	    shopcart.cmid=source.readString();
	    shopcart.cpmc=source.readString();
	    shopcart.cppic=source.readString();
	    shopcart.ctid=source.readString();
	    shopcart.dj=source.readString();
	    shopcart.je=source.readString();
	    shopcart.mid=source.readString();
	    shopcart.shopid=source.readString();
	    shopcart.sl=source.readString();
	    shopcart.username=source.readString();
	    shopcart.shopname=source.readString();
	    shopcart.chima=source.readString();
		return shopcart;
	}

	@Override
	public ShopCart[] newArray(int size) {
		return new ShopCart[size];
	}
	
};

}
