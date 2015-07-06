package com.iteambuysale.zhongtuan.model;

import java.io.Serializable;

import com.iteambuysale.zhongtuan.annotation.Column;
import com.iteambuysale.zhongtuan.annotation.Table;
import com.iteambuysale.zhongtuan.define.D;

@Table(name="TEMAI_LIST")
public class Temai extends Model implements Serializable {

	private static final long serialVersionUID = 1022184010289335718L;

	@Column(name="_id",primary=true)
	private String tmid;	//id
	@Column(name="_title")
	private String title;	//特卖标题
	@Column(name="_tmword")
	private String tmword;	//特卖描述
	@Column(name="_tmlb")
	private String tmlb;	//操作类别，dp打开产品，zt打开webView
	@Column(name="_tmurl")
	private String tmurl;	//webview  url
	@Column(name="_mid")
	private String mid;		//产品 id
	@Column(name="_tmdj")
	private String tmdj;	//特卖价格
	@Column(name="_sdate")
	private String sdate;	//特卖开始时间
	@Column(name="_edate")
	private String edate;	//特卖结束时间
	@Column(name="_picurl")
	private String picurl;	//图片
	@Column(type="INTEGER",name="_xh")		//序号
	private String xh;	
	@Column(name="_yuandj")	//原价
	private String yuandj;
	@Column(name="_zkl")	//折扣
	private String zkl;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTmword() {
		return tmword;
	}
	public void setTmword(String tmword) {
		this.tmword = tmword;
	}
	public String getTmlb() {
		return tmlb;
	}
	public void setTmlb(String tmlb) {
		if("dp".equals(tmlb)){
			this.tmlb = String.valueOf(D.OPT_PRODUCT);
		}else{
			this.tmlb = String.valueOf(D.OPT_LINK);
		}
	}
	public String getTmurl() {
		return tmurl;
	}
	public void setTmurl(String tmurl) {
		this.tmurl = tmurl;
	}
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getTmdj() {
		return tmdj;
	}
	public void setTmdj(String tmdj) {
		this.tmdj = tmdj;
	}
	public String getSdate() {
		return sdate;
	}
	public void setSdate(String sdate) {
		this.sdate = sdate;
	}
	public String getEdate() {
		return edate;
	}
	public void setEdate(String edate) {
		this.edate = edate;
	}
	public String getPicurl() {
		return picurl;
	}
	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}
	public String getTmid() {
		return tmid;
	}
	public void setTmid(String tmid) {
		this.tmid = tmid;
	}
	public String getXh() {
		return xh;
	}
	public void setXh(String xh) {
		this.xh = xh;
	}
	public String getYuandj() {
		return yuandj;
	}
	public void setYuandj(String yuandj) {
		this.yuandj = yuandj;
	}
	public String getZkl() {
		return zkl;
	}
	public void setZkl(String zkl) {
		this.zkl = zkl;
	}
	
	
	

}
