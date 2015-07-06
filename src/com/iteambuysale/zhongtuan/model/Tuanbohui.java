package com.iteambuysale.zhongtuan.model;

import java.io.Serializable;

import com.iteambuysale.zhongtuan.annotation.Column;
import com.iteambuysale.zhongtuan.annotation.Table;
@Table(name="TUANBOHUI_LIST")
public class Tuanbohui extends Model implements Serializable {
	@Column(name="_id",primary=true)private String eid;	//展会id
	@Column(name="_exname")private String exname;		//展会名称
	@Column(name="_title")private String title;		//标题
	@Column(name="_address")private String address;			//地址
	@Column(name="_exdate")private String exdate;		//开展时间
	@Column(name="_www")private String www;			//交通路线
	@Column(name="_czy")private String czy;		//负责人
	@Column(name="_xh")private String xh;		//序号
	@Column(name="_detail")private String detail;		// 描述
	@Column(name="_picurl")private String picurl; 		//图片URL
	@Column(name="_zt")private String zt;   //状态
	@Column(name="_edate")private String edate;		//闭展时间（标准）
	@Column(name="_tdate")private String tdate;	//日期（中文）
	@Column(name="_ttime")private String ttime;//时间（中文）
	@Column(name="_joinlc")private String joinlc;   //参展流程
	@Column(name="_weiquan")private String weiquan;	//维权
	@Column(name="_piclarge")private String piclarge;  //展会大图
	@Column(name="_piclargeurl")private String piclargeurl;	//大图链接
	@Column(name="_tuiguang")private String tuiguang;	//展会推广
	@Column(name="_pichfbz")private String pichfbz;	//往届回顾
	@Column(name="_pichf")private String pichf;//往届回顾内容
	@Column(name="_bmtel")private String bmtel;//报名电话
	@Column(name="_bmqq")private String bmqq;//报名QQ
	@Column(name="_reapp")private String reapp;//是否允许重复报名
	@Column(name="_areacode")private String areacode;//展会地区号
	@Column(name="_mobmemo")private String mobmemo;//展会详情
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getEid() {
		return eid;
	}
	public void setEid(String eid) {
		this.eid = eid;
	}
	public String getExname() {
		return exname;
	}
	public void setExname(String exname) {
		this.exname = exname;
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
	public String getExdate() {
		return exdate;
	}
	public void setExdate(String exdate) {
		this.exdate = exdate;
	}
	public String getWww() {
		return www;
	}
	public void setWww(String www) {
		this.www = www;
	}
	public String getCzy() {
		return czy;
	}
	public void setCzy(String czy) {
		this.czy = czy;
	}
	public String getXh() {
		return xh;
	}
	public void setXh(String xh) {
		this.xh = xh;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getPicurl() {
		return picurl;
	}
	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}
	public String getZt() {
		return zt;
	}
	public void setZt(String zt) {
		this.zt = zt;
	}
	public String getEdate() {
		return edate;
	}
	public void setEdate(String edate) {
		this.edate = edate;
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
	public String getJoinlc() {
		return joinlc;
	}
	public void setJoinlc(String joinlc) {
		this.joinlc = joinlc;
	}
	public String getWeiquan() {
		return weiquan;
	}
	public void setWeiquan(String weiquan) {
		this.weiquan = weiquan;
	}
	public String getPiclarge() {
		return piclarge;
	}
	public void setPiclarge(String piclarge) {
		this.piclarge = piclarge;
	}
	public String getPiclargeurl() {
		return piclargeurl;
	}
	public void setPiclargeurl(String piclargeurl) {
		this.piclargeurl = piclargeurl;
	}
	public String getTuiguang() {
		return tuiguang;
	}
	public void setTuiguang(String tuiguang) {
		this.tuiguang = tuiguang;
	}
	public String getPichfbz() {
		return pichfbz;
	}
	public void setPichfbz(String pichfbz) {
		this.pichfbz = pichfbz;
	}
	public String getPichf() {
		return pichf;
	}
	public void setPichf(String pichf) {
		this.pichf = pichf;
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
	public String getReapp() {
		return reapp;
	}
	public void setReapp(String reapp) {
		this.reapp = reapp;
	}
	public String getAreacode() {
		return areacode;
	}
	public void setAreacode(String areacode) {
		this.areacode = areacode;
	}
	public String getMobmemo() {
		return mobmemo;
	}
	public void setMobmemo(String mobmemo) {
		this.mobmemo = mobmemo;
	}
	
	
	


}
