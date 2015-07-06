package com.iteambuysale.zhongtuan.model;

import com.iteambuysale.zhongtuan.annotation.Arg;
import com.iteambuysale.zhongtuan.annotation.Column;
import com.iteambuysale.zhongtuan.annotation.Table;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.utilities.StringUtilities;

@Table(name = D.DB_TABLE_USER)
public class User extends Model {
	@Column(name = "_id", primary = true)
	private String uid; 					// id
	private String username;			 	// 用户名
	@Column(name = "_userpwd", len = 40)
	private String userpwd; 				// 密码

	@Column(name = "_nickname", len = 80)
	private String nickname; 				// 昵称

	@Column(name = "_mobile", len = 20)
	private String mobile; 					// 手机

	@Column(name = "_email", len = 40)
	private String email; 					// 邮箱

	@Column(name = "_sex", len = 5)
	private String sex; 					// 性别

	@Column(name = "_birthday", len = 20)
	private String birthday; 				// 生日

	@Column(name = "_signate", len = 20)
	private String signate;					 // 签名
	@Column(name = "_tbmoney", len = 20)
	private String tbmoney;	
	private String mobyzm;	
	@Column(name = "_avatar")
	private String avatar;
	private String handpwd;					 // 手势密码
	private String regdate;					 // 注册日期
	private String regip; 					 // 注册ip
	private String lastdate;				 // 最后登录日期
	private String lastip; 					 // 最后登录ip
	private String userlb; 					 // 用户
	@Arg(name = "acctoken")
	private String acctoken;				 // 用户token

	public String getTbmoney() {
		return tbmoney;
	}

	public void setTbmoney(String tbmoney) {
		this.tbmoney = tbmoney;
	}

	public User() {
		super();
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUserpwd() {
		return userpwd;
	}

	public void setUserpwd(String userpwd) {
		this.userpwd = userpwd;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSex() {
		return sex;

	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = StringUtilities.formatDate(birthday);
	}

	public String getSignate() {
		return signate;
	}

	public void setSignate(String signate) {
		this.signate = signate;
	}

	public String getHandpwd() {
		return handpwd;
	}

	public void setHandpwd(String handpwd) {
		this.handpwd = handpwd;
	}

	public String getRegdata() {
		return regdate;
	}

	public void setRegdata(String regdata) {
		this.regdate = regdata;
	}

	public String getRegip() {
		return regip;
	}

	public void setRegip(String regip) {
		this.regip = regip;
	}

	public String getLastdate() {
		return lastdate;
	}

	public void setLastdate(String lastdate) {
		this.lastdate = lastdate;
	}

	public String getLastip() {
		return lastip;
	}

	public void setLastip(String lastip) {
		this.lastip = lastip;
	}

	public String getUserlb() {
		return userlb;
	}

	public void setUserlb(String userlb) {
		this.userlb = userlb;
	}

	public String getAcctoken() {
		return acctoken;
	}

	public void setAcctoken(String acctoken) {
		this.acctoken = acctoken;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMobyzm() {
		return mobyzm;
	}

	public void setMobyzm(String mobyzm) {
		this.mobyzm = mobyzm;
	}

	public String getRegdate() {
		return regdate;
	}

	public void setRegdate(String regdate) {
		this.regdate = regdate;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
}
