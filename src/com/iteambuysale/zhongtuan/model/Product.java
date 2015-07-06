package com.iteambuysale.zhongtuan.model;

import java.io.Serializable;

import com.iteambuysale.zhongtuan.annotation.Column;
import com.iteambuysale.zhongtuan.annotation.Table;
             //PRODUCT_LIST
@Table(name = "PRODUCT_LIST")//PRODUCT_LIST
public class Product extends Model implements Serializable {

	private static final long serialVersionUID = -7585744536449611045L;
	@Column(name = "_id", primary = true)
	private String mid; // 商品id
	@Column(name = "_cpmc")
	private String cpmc; // 商品名称
	@Column(name = "_cpgg")
	private String cpgg; // 规格
	@Column(name = "_picurl")
	private String picurl; // 商品图片
	@Column(name = "_cpdl")
	private String cpdl; // 大类id
	@Column(name = "_cpxl")
	private String cpxl; // 小类id
	@Column(name = "_urlqrcode")
	private String urlqrcode; // 二维码code
	@Column(name = "_dj0")
	private String dj0; // 原价
	@Column(name = "_dj1")
	private String dj1; // 折扣价格
	@Column(name = "_dj2")
	private String dj2; // 中团价格
	@Column(name = "_kcsl")
	private String kcsl; // 库存数量
	@Column(name = "_sells")
	private String sells; // 销量
	@Column(name = "_jldw")
	private String jldw; // 单位
	@Column(name = "_bests")
	private String bests; // 好评
	@Column(name = "_collects")
	private String collects; // 收藏
	@Column(name = "_shopid")
	private String shopid;
	@Column(name = "_content")//商品描述
	private String content;
	@Column(name = "_cpmemo")//套餐详情
	private String cpmemo;
	@Column(name = "_sysm")//消费提醒
	private String sysm;
	

	public String getCpmemo() {
		return cpmemo;
	}

	public void setCpmemo(String cpmemo) {
		this.cpmemo = cpmemo;
	}

	public String getSysm() {
		return sysm;
	}

	public void setSysm(String sysm) {
		this.sysm = sysm;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getShopid() {
		return shopid;
	}

	public void setShopid(String shopid) {
		this.shopid = shopid;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getCpmc() {
		return cpmc;
	}

	public void setCpmc(String cpmc) {
		this.cpmc = cpmc;
	}

	public String getCpgg() {
		return cpgg;
	}

	public void setCpgg(String cpgg) {
		this.cpgg = cpgg;
	}

	public String getPicurl() {
		return picurl;
	}

	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}

	public String getCpdl() {
		return cpdl;
	}

	public void setCpdl(String cpdl) {
		this.cpdl = cpdl;
	}

	public String getCpxl() {
		return cpxl;
	}

	public void setCpxl(String cpxl) {
		this.cpxl = cpxl;
	}

	public String getUrlqrcode() {
		return urlqrcode;
	}

	public void setUrlqrcode(String urlqrcode) {
		this.urlqrcode = urlqrcode;
	}

	public String getDj0() {
		return dj0;
	}

	public void setDj0(String dj0) {
		this.dj0 = dj0;
	}

	public String getDj1() {
		return dj1;
	}

	public void setDj1(String dj1) {
		this.dj1 = dj1;
	}

	public String getDj2() {
		return dj2;
	}

	public String getNumDj2() {
		return dj2;
	}

	public void setDj2(String dj2) {
		this.dj2 = dj2;
	}

	public String getKcsl() {
		return kcsl;
	}

	public void setKcsl(String kcsl) {
		this.kcsl = kcsl;
	}

	public String getSells() {
		return sells;
	}

	public void setSells(String sells) {
		this.sells = sells;
	}

	public String getJldw() {
		return jldw;
	}

	public void setJldw(String jldw) {
		this.jldw = jldw;
	}

	public String getBests() {
		return bests;
	}

	public void setBests(String bests) {
		this.bests = bests;
	}

	public String getCollects() {
		return collects;
	}

	public void setCollects(String collects) {
		this.collects = collects;
	}

}
