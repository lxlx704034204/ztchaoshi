package com.iteambuysale.zhongtuan.utilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.iteambuysale.zhongtuan.define.D;

public class VerificationUtilities {
	/**
	 * 手机号码校验
	 * @param str
	 * @return true:通过 false:不通过
	 */
	public static boolean validateReversePhone(String phoneNum) {
		String regEx = "^\\d{9}[3587][1]$";
		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(phoneNum);
		return mat.matches();
	}
	
	/**
	 * 密码校验:两次输入的密码是否一致
	 * @param Password 
	 * @param rep_password
	 * @return true:通过 false:不通过
	 */
	public static boolean validatePassword(String Password, String rep_password) {
		return Password.equals(rep_password);
	}

	/**
	 * 密码校验：长度是否大于6位
	 * @param password
	 * @return true:通过 false:不通过
	 */
	public static boolean validatePassword(String password) {
		return password.length() >= 6;
	}
	
	/**
	 * 邮政编码校验:6位纯数字
	 * @param zipCode
	 * @return true:通过 false:不通过
	 */
	public static boolean validateZipCode(String zipCode){
		String regEx = "^\\d{6}$";
		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(zipCode);
		return mat.matches();
	}

	/**
	 * 字符串校验：不为空
	 * @param str
	 * @return true:通过 false:不通过
	 */
	public static boolean validateStringNotNull(String str) {
		return !str.equals("");
	}

	/**
	 * 性别校验
	 * @param sex
	 * @return true:通过 false:不通过
	 */
	public static boolean validateSex(String sex) {
		return (sex.equals(D.MALE) || sex.equals(D.FEMALE) || sex.equals(D.UNKNOW));
	}

	/**
	 * 日期校验:"yyyy-mm-dd"
	 * @param date
	 * @return true:通过 false:不通过
	 */
	public static boolean validateDate(String date) {
		String regEx = "^\\d{4}-\\d{2}-\\d{2}$";
		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(date);
		return mat.matches();
	}

	/**
	 * 验证码校验：6位纯数字
	 * @param yzm
	 * @return true:通过 false:不通过
	 */
	public static boolean validateYZM(String yzm) {
		String regEx = "^\\d{6}$";
		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(yzm);
		return mat.matches();
	}
}
