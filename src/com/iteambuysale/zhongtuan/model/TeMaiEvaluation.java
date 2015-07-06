package com.iteambuysale.zhongtuan.model;

import java.io.Serializable;

import com.iteambuysale.zhongtuan.annotation.Column;
import com.iteambuysale.zhongtuan.annotation.Table;
@Table(name="TEMAIEVALUATION_LIST")
public class TeMaiEvaluation extends Model implements Serializable {

	private static final long serialVersionUID = 6160633965327308649L;
	
	@Column(name="_id",primary=true)
	private String cocid;
	@Column(name="_ordno")
	private String ordno;		
	@Column(name="_shopid")
	private String shopid;		
	@Column(name="_cpid")
	private String cpid;			
	@Column(name="_cpmc")
	private String cpmc;		
	@Column(name="_username")
	private String username;
	@Column(name="_level")
	private String level;		
	@Column(name="_dfen")
	private String dfen;		
	@Column(name="_recmemo")
	private String recmemo;		
	@Column(name="_dateandtime")
	private String dateandtime; 
	@Column(name="_recpic")
	private String recpic; 


	public String getRecpic() {
		return recpic;
	}
	public void setRecpic(String recpic) {
		this.recpic = recpic;
	}
	public String getCocid() {
		return cocid;
	}
	public void setCocid(String cocid) {
		this.cocid = cocid;
	}
	public String getOrdno() {
		return ordno;
	}
	public void setOrdno(String ordno) {
		this.ordno = ordno;
	}
	public String getShopid() {
		return shopid;
	}
	public void setShopid(String shopid) {
		this.shopid = shopid;
	}
	public String getCpid() {
		return cpid;
	}
	public void setCpid(String cpid) {
		this.cpid = cpid;
	}
	public String getCpmc() {
		return cpmc;
	}
	public void setCpmc(String cpmc) {
		this.cpmc = cpmc;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getDfen() {
		return dfen;
	}
	public void setDfen(String dfen) {
		this.dfen = dfen;
	}
	public String getRecmemo() {
		return recmemo;
	}
	public void setRecmemo(String recmemo) {
		this.recmemo = recmemo;
	}
	public String getDateandtime() {
		return dateandtime;
	}
	public void setDateandtime(String dateandtime) {
		this.dateandtime = dateandtime;
	}
}
