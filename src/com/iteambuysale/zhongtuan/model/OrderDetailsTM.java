package com.iteambuysale.zhongtuan.model;

import com.iteambuysale.zhongtuan.annotation.Column;
import com.iteambuysale.zhongtuan.annotation.Table;

@Table(name="ORDER_DETAILS_TM",dbouble_key=true,key1="_ordnox",key2="_cpmid")
public class OrderDetailsTM extends Model {
	@Column(name="_id",primary = true)
	private String id;				// 自增id
	@Column(name="_ordnox")
	private String ordnox;			// 订单id	\
	@Column(name = "_cpmid")		//			||==>(这两个要唯一)
	private String cpmid;			// 产品id	/
	@Column(name = "_osl")private String osl;				// 下单数量
	@Column(name = "_oje")private String oje;				// 下单金额
	@Column(name = "_refje")private String refje;			// 退款金额
	@Column(name = "_cpmc")private String cpmc;			// 产品名称
	@Column(name = "_cppic")private String cppic;		// 产品图片
	@Column(name = "_odj")private String odj;	//下单价格
	@Column(name = "_cpgg")private String cpgg;	//产品规格
	@Column(name = "_cpmemo")private String cpmemo;//套餐详情
	@Column(name = "_sysm")private String sysm;//消费提醒
	public String getOdj() {
		return odj;
	}
	public void setOdj(String odj) {
		this.odj = odj;
	}
	public String getCpgg() {
		return cpgg;
	}
	public void setCpgg(String cpgg) {
		this.cpgg = cpgg;
	}
	public String getCpmemo() {
		return cpmemo;
	}
	public void setCpmemo(String cpmemo) {
		this.cpmemo = cpmemo;
	}
	public String getSysm() {
		return sysm;
	}
	public void setSysm(String sysm) {
		this.sysm = sysm;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrdnox() {
		return ordnox;
	}
	public void setOrdnox(String ordnox) {
		this.ordnox = ordnox;
	}
	public String getCpmid() {
		return cpmid;
	}
	public void setCpmid(String cpmid) {
		this.cpmid = cpmid;
	}
	public String getOsl() {
		return osl;
	}
	public void setOsl(String osl) {
		this.osl = osl;
	}
	public String getOje() {
		return oje;
	}
	public void setOje(String oje) {
		this.oje = oje;
	}
	public String getRefje() {
		return refje;
	}
	public void setRefje(String refje) {
		this.refje = refje;
	}
	public String getCpmc() {
		return cpmc;
	}
	public void setCpmc(String cpmc) {
		this.cpmc = cpmc;
	}
	public String getCppic() {
		return cppic;
	}
	public void setCppic(String cppic) {
		this.cppic = cppic;
	}

}
