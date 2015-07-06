package com.iteambuysale.zhongtuan.model;

import com.iteambuysale.zhongtuan.annotation.Column;
import com.iteambuysale.zhongtuan.annotation.Table;
@Table(name = "ADVERT_MSG",dbouble_key=true,key1="_id",key2="_advtype")
public class Spl_sale_v2_adv extends Model {
	
	@Column(name="_picturl")
	private String picturl;
	@Column(name="_reqtype")
	private String reqType;
	@Column(name="_reqdata")
	private String reqdata;
	@Column(name="_reqname")
	private String reqname;
	@Column(name="_id")
	private String id;
	@Column(name="_advtype")
	private String advtype;
	/*@Column(name="_id",primary=true)
	private String id;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}*/
	public String getAdvtype() {
		return advtype;
	}
	public void setAdvtype(String advtype) {
		this.advtype = advtype;
	}
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPicturl() {
		return picturl;
	}
	public void setPicturl(String picturl) {
		this.picturl = picturl;
	}
	public String getReqType() {
		return reqType;
	}
	public void setReqType(String reqType) {
		this.reqType = reqType;
	}
	public String getReqdata() {
		return reqdata;
	}
	public void setReqdata(String reqdata) {
		this.reqdata = reqdata;
	}
	public String getReqname() {
		return reqname;
	}
	public void setReqname(String reqname) {
		this.reqname = reqname;
	}
	
}
