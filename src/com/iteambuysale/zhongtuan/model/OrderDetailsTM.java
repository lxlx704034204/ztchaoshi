package com.iteambuysale.zhongtuan.model;

import com.iteambuysale.zhongtuan.annotation.Column;
import com.iteambuysale.zhongtuan.annotation.Table;

@Table(name = "ORDER_DETAILS_TM", dbouble_key = true, key1 = "_ordnox", key2 = "_cpmid")
public class OrderDetailsTM extends Model {
    @Column(name = "_id", primary = true)
    private String id; // 自增id
    @Column(name = "_ordnox"  )
    private String ordnox; // 订单id \
    @Column(name = "_cpmid")
    // ||==>(这两个要唯一)
    private String cpmid; // 产品id /
    @Column(name = "_osl")
    private String osl; // 下单数量
    @Column(name = "_oje")
    private String oje; // 下单金额
    @Column(name = "_refje")
    private String refje; // 退款金额
    @Column(name = "_cpmc")
    private String cpmc; // 产品名称
    @Column(name = "_cppic")
    private String cppic; // 产品图片
    @Column(name = "_odj")
    private String odj; // 下单价格
    @Column(name = "_cpgg")
    private String cpgg; // 产品规格
    @Column(name = "_cpmemo")
    private String cpmemo;// 套餐详情
    @Column(name = "_sysm")
    private String sysm;// 消费提醒
    @Column(name = "_dateandtime")
    private String dateandtime;// 时间和日期
    @Column(name = "_ordmess")
    private String ordmess;// 时间和日期
    @Column(name = "_stbmoney")
    private String stbmoney;// 团币
    @Column(name = "_tmword")
    private String tmword;// 团币
    @Column(name = "_utbmoney")
    private String utbmoney;// 团币
    @Column(name = "_ordzt")
    private String ordzt;// 时间和日期
    @Column(name = "_logco")
    private String logco;// 物流名称
     @Column(name = "_logid")
    private String logid;// 物流名称
     @Column(name = "_logno")
    private String logno;// 物流名称

    public String getLogid() {
        return logid;
    }

    public void setLogid(String logid) {
        this.logid = logid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }

    public String getRefid() {
        return refid;
    }

    public void setRefid(String refid) {
        this.refid = refid;
    }

    public String getOrdno() {
        return ordno;
    }

    public void setOrdno(String ordno) {
        this.ordno = ordno;
    }

    public String getLogno() {
        return logno;
    }

    public void setLogno(String logno) {
        this.logno = logno;
    }

    @Column(name = "_ordno")
    private String ordno;// 物流名称
    @Column(name = "_refid")
    private String refid;// 物流名称
  @Column(name = "_shopid")
    private String shopid;// 物流名称
    @Column(name = "_username")
    private String username;// 物流名称


    public String getStbmoney() {
        return stbmoney;
    }

    public void setStbmoney(String stbmoney) {
        this.stbmoney = stbmoney;
    }

    public String getTmword() {
        return tmword;
    }

    public void setTmword(String tmword) {
        this.tmword = tmword;
    }

    public String getLogco() {
        return logco;
    }

    public void setLogco(String logco) {
        this.logco = logco;
    }

    public String getUtbmoney() {
        return utbmoney;
    }

    public void setUtbmoney(String utbmoney) {
        this.utbmoney = utbmoney;
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

    public String getOrdmess() {
        return ordmess;
    }

    public void setOrdmess(String ordmess) {
        this.ordmess = ordmess;
    }


    public OrderDetailsTM() {
    }

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
