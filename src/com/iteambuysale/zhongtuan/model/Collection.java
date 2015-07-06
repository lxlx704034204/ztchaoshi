package com.iteambuysale.zhongtuan.model;

import java.io.Serializable;

import com.iteambuysale.zhongtuan.annotation.Column;
import com.iteambuysale.zhongtuan.annotation.Table;

@Table(name = "COLLECTION_LIST")
public class Collection extends Model implements Serializable {

	private static final long serialVersionUID = -1197768138707444649L;

	@Column(name = "_id")
	private String ufid;
	@Column(name = "_username")
	private String username;
	@Column(name = "_uflb")
	private String uflb;
	@Column(name = "_lbid")
	private String lbid;
	@Column(name = "_title")
	private String title;
	@Column(name = "_picurl")
	private String picurl;
	@Column(name = "_memo")
	private String memo;
	@Column(name = "_dateandtime")
	private String dateandtime;
	@Column(name = "_lngo")
	private String lngo;
	@Column(name = "_lato")
	private String lato;
	@Column(name = "_dj")
	private String dj;
	@Column(name = "_dfen")
	private String dfen;
	@Column(name = "_collects")
	private String collects;
	public String getUfid() {
		return ufid;
	}
	public void setUfid(String ufid) {
		this.ufid = ufid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUflb() {
		return uflb;
	}
	public void setUflb(String uflb) {
		this.uflb = uflb;
	}
	public String getLbid() {
		return lbid;
	}
	public void setLbid(String lbid) {
		this.lbid = lbid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPicurl() {
		return picurl;
	}
	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getDateandtime() {
		return dateandtime;
	}
	public void setDateandtime(String dateandtime) {
		this.dateandtime = dateandtime;
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
	public String getDj() {
		return dj;
	}
	public void setDj(String dj) {
		this.dj = dj;
	}
	public String getDfen() {
		return dfen;
	}
	public void setDfen(String dfen) {
		this.dfen = dfen;
	}
	public String getCollects() {
		return collects;
	}
	public void setCollects(String collects) {
		this.collects = collects;
	}

}
