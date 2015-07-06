package com.teambuy.zhongtuan.model;

import java.io.Serializable;

import com.teambuy.zhongtuan.annotation.Column;
import com.teambuy.zhongtuan.annotation.Table;
           //TEMAI_LIST_VERSON
@Table(name="TEMAI_LIST_VERSON")
public class TemaiVerson2 extends Model implements Serializable {

	private static final long serialVersionUID = 1022184010289335718L;

	@Column(name="_id",primary=true)
	private String tmid;	//id
	@Column(name="_bests")
	private String bests;	
	@Column(name="_collects")
	private String collects;	
	@Column(name="_content")
	private String content;	
	@Column(name="_cpdl")
	private String cpdl;	
	@Column(name="_cpxl")
	private String cpxl;		
	@Column(name="_dfen")
	private String dfen;	
	@Column(name="_dj0")
	private String dj0;	
	@Column(name="_dj1")
	private String dj1;	
	@Column(name="_edate")
	private String edate;	
	@Column(name="_jldw")		
	private String jldw;	
	@Column(name="_kcsl")	
	private String kcsl;
	@Column(name="_mid")	
	private String mid;
	@Column(name="ok")	
	private String ok;
	@Column(name="_picurl")	
	private String picurl;
	@Column(name="_rebate")	
	private String rebate;
	@Column(name="_sdate")	
	private String sdate;
	@Column(name="_sells")	
	private String sells;
	@Column(name="_shopid")	
	private String shopid;
	@Column(name="_title")	
	private String title;
	@Column(type = "FLOAT",name="_tmdj")	
	private String tmdj;
	@Column(name="_tmlb")	
	private String tmlb;
	@Column(name="_tmurl")	
	private String tmurl;
	@Column(name="_tmword")	////特卖描述
	private String tmword;
	@Column(type="INTEGER" ,name="_xh")	
	private String xh;
	@Column(name="_zkl")
	private String zkl;
	@Column(name="_buytimes")//可以购买的次数
	private String buytimes;
	@Column(name="_buynums")//可以购买的数量
	private String buynums;
	public String getBuytimes() {
		return buytimes;
	}
	public void setBuytimes(String buytimes) {
		this.buytimes = buytimes;
	}
	public String getBuynums() {
		return buynums;
	}
	public void setBuynums(String buynums) {
		this.buynums = buynums;
	}
	public String getTmid() {
		return tmid;
	}
	public void setTmid(String tmid) {
		this.tmid = tmid;
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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
	public String getDfen() {
		return dfen;
	}
	public void setDfen(String dfen) {
		this.dfen = dfen;
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
	public String getEdate() {
		return edate;
	}
	public void setEdate(String edate) {
		this.edate = edate;
	}
	public String getJldw() {
		return jldw;
	}
	public void setJldw(String jldw) {
		this.jldw = jldw;
	}
	public String getKcsl() {
		return kcsl;
	}
	public void setKcsl(String kcsl) {
		this.kcsl = kcsl;
	}
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getOk() {
		return ok;
	}
	public void setOk(String ok) {
		this.ok = ok;
	}
	public String getPicurl() {
		return picurl;
	}
	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}
	public String getRebate() {
		return rebate;
	}
	public void setRebate(String rebate) {
		this.rebate = rebate;
	}
	public String getSdate() {
		return sdate;
	}
	public void setSdate(String sdate) {
		this.sdate = sdate;
	}
	public String getSells() {
		return sells;
	}
	public void setSells(String sells) {
		this.sells = sells;
	}
	public String getShopid() {
		return shopid;
	}
	public void setShopid(String shopid) {
		this.shopid = shopid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTmdj() {
		return tmdj;
	}
	public void setTmdj(String tmdj) {
		this.tmdj = tmdj;
	}
	public String getTmlb() {
		return tmlb;
	}
	public void setTmlb(String tmlb) {
		this.tmlb = tmlb;
	}
	public String getTmurl() {
		return tmurl;
	}
	public void setTmurl(String tmurl) {
		this.tmurl = tmurl;
	}
	public String getTmword() {
		return tmword;
	}
	public void setTmword(String tmword) {
		this.tmword = tmword;
	}
	public String getXh() {
		return xh;
	}
	public void setXh(String xh) {
		this.xh = xh;
	}
	public String getZkl() {
		return zkl;
	}
	public void setZkl(String zkl) {
		this.zkl = zkl;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
	
	

}
