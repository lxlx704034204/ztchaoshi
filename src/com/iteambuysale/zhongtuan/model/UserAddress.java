package com.iteambuysale.zhongtuan.model;

import java.io.Serializable;

import com.iteambuysale.zhongtuan.annotation.Column;
import com.iteambuysale.zhongtuan.annotation.Table;

@Table(name="ADDRESS_LIST")
public class UserAddress extends Model implements Serializable {

	private static final long serialVersionUID = 3586338795179434555L;
	@Column(name="_id",primary=true)
	private String uaid;
	@Column(name="_username")
	private String username;
	@Column(name="_province")
	private String province;
	@Column(name="_city")
	private String city;
	@Column(name="_carea")  //区
	private String carea;
	@Column(name="_address")
	private String address;
	@Column(name="_truename")
	private String truename;
	@Column(name="_tel")
	private String tel;
	@Column(name="_zipcode")
	private String zipcode;
	@Column(name="_isdef")	//默认地址标记
	private String isdef;
	@Column(name="_sendid")	//收货时间
	private String sendid;


	public String getCarea() {
		return carea;
	}

	public void setCarea(String carea) {
		this.carea = carea;
	}

	public String getSendid() {
		return sendid;
	}

	public void setSendid(String sendid) {
		this.sendid = sendid;
	}

	public UserAddress() {

	}

	public UserAddress(String uaid, String address, String truename, String tel) {
		super();
		this.uaid = uaid;
		this.address = address;
		this.truename = truename;
		this.tel = tel;
	}

	public String getUaid() {
		return uaid;
	}

	public void setUaid(String uaid) {
		this.uaid = uaid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTruename() {
		return truename;
	}

	public void setTruename(String truename) {
		this.truename = truename;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getIsdef() {
		return isdef;
	}

	public void setIsdef(String isdef) {
		this.isdef = isdef;
	}

}
