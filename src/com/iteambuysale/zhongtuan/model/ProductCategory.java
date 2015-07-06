package com.iteambuysale.zhongtuan.model;

import java.io.Serializable;

import com.iteambuysale.zhongtuan.annotation.Column;
import com.iteambuysale.zhongtuan.annotation.Table;
@Table(name="PRODUCTCCATEGORY_LIST")
public class ProductCategory extends Model implements Serializable{
	private static final long serialVersionUID = -2965756351779743129L;
	
	@Column(name="_id",primary=true)
	private String lbid;				// 分类id
	@Column(name="_lbname")				
	private String lbname;				// 分类名称
	@Column(name="_cup")
	private String cup;					// 大类id
	@Column(name="_spsl")
	private String spsl;				// 商品数量
	@Column(name="_cpsl")
	private String cpsl;				//  
	@Column(name="_icon")
	private String icon;				// 
	
	public String getLbid() {
		return lbid;
	}
	public void setLbid(String lbid) {
		this.lbid = lbid;
	}
	public String getLbname() {
		return lbname;
	}
	public void setLbname(String lbname) {
		this.lbname = lbname;
	}
	public String getCup() {
		return cup;
	}
	public void setCup(String cup) {
		this.cup = cup;
	}
	public String getSpsl() {
		return spsl;
	}
	public void setSpsl(String spsl) {
		this.spsl = spsl;
	}
	public String getCpsl() {
		return cpsl;
	}
	public void setCpsl(String cpsl) {
		this.cpsl = cpsl;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	


}
