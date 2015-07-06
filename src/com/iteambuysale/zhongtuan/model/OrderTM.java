package com.iteambuysale.zhongtuan.model;

import com.iteambuysale.zhongtuan.annotation.Column;
import com.iteambuysale.zhongtuan.annotation.Table;

@Table(name = "ORDER_TM_LIST")
public class OrderTM extends Model {
	@Column(name = "_id" , primary = true)
	private String coid;						//唯一id
	@Column(name = "_ordno", len = 20)			
	private String ordno; 						//订单号
	@Column(name = "_shopid",len = 20)
	private String shopid;						//订单所属店铺id
	@Column(name = "_username", len = 20)
	private String username;					//用户名
	@Column(name="_ordsl" ,len = 10)
	private String ordsl;						//订单产品数量
	@Column(name="_ordje" ,len = 10)
	private String ordje;						//订单金额
	@Column(name="_ordzt" ,type="INTEGER")
	private String ordzt;						//订单状态 0，1，2，3
	@Column(name="_dateandtime" ,len = 10)
	private String dateandtime;					//订单日期时间
	@Column(name="_sendid" ,len = 10)
	private String sendid;						//取货方式：送货上门0，到店取货1，面对面交易2，快递派送3
	@Column(name="_zfen" ,len = 10)
	private String zfen;						//评分
	@Column(name="_rememo" ,len = 10)
	private String rememo;						//评论
	@Column(name="_province",len = 5)
	private String province;					//省份id
	@Column(name="_city" ,len = 5)
	private String city;						//城市id
	@Column(name="_address",len = 20)
	private String address;						//地址
	@Column(name="_truename",len = 20)
	private String truename;					//收件人名字
	@Column(name="_tel",len=15)
	private String tel;							//联系电话
	@Column(name="_zipcode",len = 10)
	private String zipcode;						//寄件邮编
	@Column(name = "_lngo",len = 20)
	private String lngo;						//纬度
	@Column(name = "_lato",len = 20)
	private String lato;						//经度
	@Column(name = "_paym",len = 10)
	private String paym;						//支付方式
	@Column(name = "_payje",len = 10)
	private String payje;						//支付金额
	@Column(name = "_refje" ,len = 10)
	private String refje;						//退款金额
	@Column(name = "_sendm" ,len = 10)
	private String sendm;						//快递方式
	@Column(name = "_fapiao",len = 20)
	private String fapiao;						//发票抬头	
	@Column(name="_fcpmid")
	private String fcpmid;						//第一个产品id
	@Column(name="_fcpmc")
	private String fcpmc;						//第一个产品名称
	@Column(name="_fcppic")
	private String fcppic;						//第一个产品的图片
	@Column(name="_logco")
	private String logco;//物流公司
	@Column(name="_logid")//物流id
	private String logid;
	@Column(name="_logno")//物流单号
	private String logno;
	
	public String getLogco() {
		return logco;
	}
	public void setLogco(String logco) {
		this.logco = logco;
	}
	public String getLogid() {
		return logid;
	}
	public void setLogid(String logid) {
		this.logid = logid;
	}
	public String getLogno() {
		return logno;
	}
	public void setLogno(String logno) {
		this.logno = logno;
	}

	private OrderDetailsTM[] cpmx;		
	//订单详情
	
	public String getCoid() {
		return coid;
	}
	public void setCoid(String coid) {
		this.coid = coid;
	}
	public String getOrdno() {
		return ordno;
	}
	public void setOrdno(String ordno) {
		this.ordno = ordno;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getOrdsl() {
		return ordsl;
	}
	public void setOrdsl(String ordsl) {
		this.ordsl = ordsl;
	}
	public String getOrdje() {
		return ordje;
	}
	public void setOrdje(String ordje) {
		this.ordje = ordje;
	}
	public String getOrdzt() {
		return ordzt;
	}
	public void setOrdzt(String ordzt) {
		this.ordzt = ordzt;
	}
	public String getDateandtime() {
		return dateandtime;
	}
	public void setDateandtime(String dateandtime) {
		this.dateandtime = dateandtime;
	}
	public String getSendid() {
		return sendid;
	}
	public void setSendid(String sendid) {
		this.sendid = sendid;
	}
	public String getZfen() {
		return zfen;
	}
	public void setZfen(String zfen) {
		this.zfen = zfen;
	}
	public String getRememo() {
		return rememo;
	}
	public void setRememo(String rememo) {
		this.rememo = rememo;
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
	public String getLngo() {
		return lngo;
	}
	public void setLngo(String lngo) {
		this.lngo = lngo;
	}
	public String getLato() {
		return lato;
	}
	public void setLato(String lato) {
		this.lato = lato;
	}
	public String getPaym() {
		return paym;
	}
	public void setPaym(String paym) {
		this.paym = paym;
	}
	public String getPayje() {
		return payje;
	}
	public void setPayje(String payje) {
		this.payje = payje;
	}
	public String getRefje() {
		return refje;
	}
	public void setRefje(String refje) {
		this.refje = refje;
	}
	public String getSendm() {
		return sendm;
	}
	public void setSendm(String sendm) {
		this.sendm = sendm;
	}
	public String getFapiao() {
		return fapiao;
	}
	public void setFapiao(String fapiao) {
		this.fapiao = fapiao;
	}
	public OrderDetailsTM[] getCpmx() {
		return cpmx;
	}
	public void setCpmx(OrderDetailsTM[] cpmx) {
		this.cpmx = cpmx;
	}
	public String getFcpmid() {
		return fcpmid;
	}
	public void setFcpmid(String fcpmid) {
		this.fcpmid = fcpmid;
	}
	public String getFcpmc() {
		return fcpmc;
	}
	public void setFcpmc(String fcpmc) {
		this.fcpmc = fcpmc;
	}
	public String getFcppic() {
		return fcppic;
	}
	public void setFcppic(String fcppic) {
		this.fcppic = fcppic;
	}
	public String getShopid() {
		return shopid;
	}
	public void setShopid(String shopid) {
		this.shopid = shopid;
	}	
	
	
}
