package com.iteambuysale.zhongtuan.model;

import java.io.Serializable;

import com.iteambuysale.zhongtuan.annotation.Column;
import com.iteambuysale.zhongtuan.annotation.Table;
@Table(name="TEMAI_CATAGORY")
public class SpecialSaleCatagory extends Model implements Serializable {
	@Column(name="_id",primary=true)
	private String lbid;	//id
	@Column(name="_cpsl")
	private String cpsl;	//id
	@Column(name="_cup")
	private String cup;	//父类所在的大类
	@Column(name="_icon")
	private String icon;	//图片
	@Column(name="_lbname")
	private String lbname;	
	public SpecialSaleCatagory(String lbid, String cpsl, String cup,
			String icon, String lbname, String spsl) {
		super();
		this.lbid = lbid;
		this.cpsl = cpsl;
		this.cup = cup;
		this.icon = icon;
		this.lbname = lbname;
		this.spsl = spsl;
	}
	public SpecialSaleCatagory() {
		// TODO Auto-generated constructor stub
	}
	@Column(name="_spsl")
	private String spsl;
	public String getLbid() {
		return lbid;
	}
	public void setLbid(String lbid) {
		this.lbid = lbid;
	}
	public String getCpsl() {
		return cpsl;
	}
	public void setCpsl(String cpsl) {
		this.cpsl = cpsl;
	}
	public String getCup() {
		return cup;
	}
	public void setCup(String cup) {
		this.cup = cup;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getLbname() {
		return lbname;
	}
	public void setLbname(String lbname) {
		this.lbname = lbname;
	}
	public String getSpsl() {
		return spsl;
	}
	public void setSpsl(String spsl) {
		this.spsl = spsl;
	}	
	
}
