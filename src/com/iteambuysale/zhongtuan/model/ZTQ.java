package com.iteambuysale.zhongtuan.model;

import com.iteambuysale.zhongtuan.annotation.Column;
import com.iteambuysale.zhongtuan.annotation.Table;
@Table(name = "ZTQ_LIST")
public class ZTQ extends Model {
	@Column(name = "_id", primary = true)
	private String qid;
	@Column(name = "_qno" )
	private String qno;
	@Column(name = "_shopid" )
	private String shopid;
	@Column(name = "_cpmid" )
	private String cpmid;
	@Column(name = "_cpmc" )
	private String cpmc;
	@Column(name = "_ordno" )
	private String ordno;
	@Column(name = "_mobile" )
	private String mobile;
	@Column(name = "_username" )
	private String username;
	@Column(name = "_qzt" )	//-1 已退款   0 未使用   1 已使用
	private String qzt;
	@Column(name = "_time" )
	private String time;
	@Column(name = "_edate" )
	private String edate;
	private OrderDetailsTG[] cpmx;
	public String getQid() {
		return qid;
	}
	public void setQid(String qid) {
		this.qid = qid;
	}
	public String getQno() {
		return qno;
	}
	public void setQno(String qno) {
		this.qno = qno;
	}
	public String getShopid() {
		return shopid;
	}
	public void setShopid(String shopid) {
		this.shopid = shopid;
	}
	public String getCpmid() {
		return cpmid;
	}
	public void setCpmid(String cpmid) {
		this.cpmid = cpmid;
	}
	public String getCpmc() {
		return cpmc;
	}
	public void setCpmc(String cpmc) {
		this.cpmc = cpmc;
	}
	public String getOrdno() {
		return ordno;
	}
	public void setOrdno(String ordno) {
		this.ordno = ordno;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getQzt() {
		return qzt;
	}
	public void setQzt(String qzt) {
		this.qzt = qzt;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getEdate() {
		return edate;
	}
	public void setEdate(String edate) {
		this.edate = edate;
	}
	public OrderDetailsTG[] getCpmx() {
		return cpmx;
	}
	public void setCpmx(OrderDetailsTG[] cpmx) {
		this.cpmx = cpmx;
	}
	
	
	
	
	

}
