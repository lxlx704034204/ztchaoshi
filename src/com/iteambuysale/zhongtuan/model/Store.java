package com.iteambuysale.zhongtuan.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.iteambuysale.zhongtuan.annotation.Column;
import com.iteambuysale.zhongtuan.annotation.Table;

@Table(name = "STORE_LIST")
public class Store extends Model implements Serializable {

	private static final long serialVersionUID = -6893049032706825603L;
	@Column(name = "_id", primary = true)
	private String sid;
	@Column(name = "_shopname")
	private String shopname;
	@Column(name = "_picurl")
	private String picurl;
	@Column(name = "_address")
	private String address;
	@Column(name = "_tel")
	private String tel;
	@Column(name = "_lngo")
	private String lngo;
	@Column(name = "_lato")
	private String lato;
	@Column(name = "_perfee")
	private String perfee;
	@Column(name = "_zfen")
	private String zfen;
	@Column(type = "INTEGER", name = "_models")
	private String models;
	@Column(type = "INTEGER", name = "_distance")
	private String distance;
	private Map<String, Product> cpmx;

	public Map<String, Product> getCpmx() {
		return cpmx;
	}

	public void setCpmx(Map<String, Product> cpmx) {
		this.cpmx = cpmx;
	}

	public String getModels() {
		return models;
	}

	public void setModels(String models) {
		this.models = models;
	}

	private List<Product> products;

	public String getShopname() {
		return shopname;
	}

	public void setShopname(String shopname) {
		this.shopname = shopname;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getPicurl() {
		return picurl;
	}

	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getLngo() {
		return lngo;
	}

	public void setLngo(String lngo) {
		this.lngo = lngo;
	}

	public String getLato() {
		return lato;
	}

	public void setLato(String lato) {
		this.lato = lato;
	}

	public String getPerfee() {
		return perfee;
	}

	public void setPerfee(String perfee) {
		this.perfee = perfee;
	}

	public String getZfen() {
		return zfen;
	}

	public void setZfen(String zfen) {
		this.zfen = zfen;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

}
