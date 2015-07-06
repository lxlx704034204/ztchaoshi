package com.iteambuysale.zhongtuan.model;

import java.io.Serializable;

import com.iteambuysale.zhongtuan.annotation.Column;
import com.iteambuysale.zhongtuan.annotation.Table;

@Table(name = "ACTIVITIES_LIST")
public class Activities extends Model implements Serializable {

	private static final long serialVersionUID = -5506660682084386526L;

	@Column(name = "_id", primary = true)
	private String id;
	@Column(name = "_tgno")
	private String tgno; // no
	@Column(name = "_tgname")
	private String tgname; // name
	@Column(name = "_title")
	private String title; // 活动标题
	@Column(name = "_address")
	private String address; // 活动地址
	@Column(name = "_url")
	private String url; // url
	@Column(name = "_tdate")
	private String tdate; // date
	@Column(name = "_ttime")
	private String ttime; // time
	@Column(name = "_bmtel")
	private String bmtel; // phone
	@Column(name = "_bmqq")
	private String bmqq; // qq
	@Column(name = "_picurl")
	private String picurl; // 图片加载链接
	@Column(name = "_zt", type = "Integer")
	private String zt;
	@Column(name = "_xh", type = "Integer")
	private String xh;
	@Column(name = "_edate")
	private String edate;
	@Column(name = "_detail")
	private String detail; // 活动详情
	@Column(name = "_memo")
	private String memo; // 活动优惠
	@Column(name = "_picbrand")
	private String picbrand; // 参与品牌
	@Column(name = "_picpro")
	private String picpro; // 特价产品
	@Column(name = "_www")
	private String www; // 交通线路
	@Column(name = "_reapp")
	private String reapp; // 是否允许重复报名

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getPicbrand() {
		return picbrand;
	}

	public void setPicbrand(String picbrand) {
		this.picbrand = picbrand;
	}

	public String getPicpro() {
		return picpro;
	}

	public void setPicpro(String picpro) {
		this.picpro = picpro;
	}

	public String getWww() {
		return www;
	}

	public void setWww(String www) {
		this.www = www;
	}

	public String getReapp() {
		return reapp;
	}

	public void setReapp(String reapp) {
		this.reapp = reapp;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public String getXh() {
		return xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}

	public String getEdate() {
		return edate;
	}

	public void setEdate(String edate) {
		this.edate = edate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTgno() {
		return tgno;
	}

	public void setTgno(String tgno) {
		this.tgno = tgno;
	}

	public String getTgname() {
		return tgname;
	}

	public void setTgname(String tgname) {
		this.tgname = tgname;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTdate() {
		return tdate;
	}

	public void setTdate(String tdate) {
		this.tdate = tdate;
	}

	public String getTtime() {
		return ttime;
	}

	public void setTtime(String ttime) {
		this.ttime = ttime;
	}

	public String getBmtel() {
		return bmtel;
	}

	public void setBmtel(String bmtel) {
		this.bmtel = bmtel;
	}

	public String getBmqq() {
		return bmqq;
	}

	public void setBmqq(String bmqq) {
		this.bmqq = bmqq;
	}

	public String getPicurl() {
		return picurl;
	}

	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}

}
