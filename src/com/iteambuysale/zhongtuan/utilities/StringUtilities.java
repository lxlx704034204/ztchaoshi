package com.iteambuysale.zhongtuan.utilities;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;

import android.content.Context;

import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.model.DateTime;

public class StringUtilities {
	
	/**
	 * 组装post请求参数
	 * 
	 * @param list
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static byte[] getPostQuery(List<NameValuePair> list) throws UnsupportedEncodingException {
		StringBuilder strbuilder = new StringBuilder();
		boolean first = true;
		if (null != list) {
			for (NameValuePair nvp : list) {
				if (first) {
					first = false;
				} else {
					strbuilder.append("&");
				}
				strbuilder.append(URLEncoder.encode(nvp.getName(), "UTF-8"));
				strbuilder.append("=");
				String value = null == nvp.getValue()?"":nvp.getValue();
				strbuilder.append(URLEncoder.encode(value, "UTF-8"));
			}
		}

		return strbuilder.toString().getBytes("UTF-8");
	}

	/**
	 * 创建where条件语句
	 * 
	 * @param name
	 * @param type
	 * @return
	 */
	public static String createWhere(String name, String type) {
		return name + " " + type + " ?";
	}

	/**
	 * 组装where条件语句
	 * 
	 * @param names
	 * @param type
	 * @return
	 */
	public static String createWhere(String[] names, String type, String sparator) {
		StringBuilder strBuilder = new StringBuilder();
		for (int i = 0; i < names.length; i++) {
			strBuilder.append(names[i] + " " + type + " ?");
			if (i + 1 != names.length) {
				strBuilder.append(" " + sparator + " ");
			}
		}
		return strBuilder.toString();
	}

	/**
	 * 大写首字母
	 */
	public static String upCase(String str) {
		StringBuilder sb = new StringBuilder(str);
		char a = str.charAt(0);
		sb.setCharAt(0, (char) (a - 32));
		return sb.toString();
	}

	/**
	 * 通过url获取供图片文件存储使用的名字
	 * 
	 * @param url
	 *            图片url
	 * @return 图片文件名
	 */
	public static String getImageFileName(String url) {
		if(url.length()==0)return "";
		String regEx = "([^\\/]*\\.\\w*$)";
		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(url);
		if (mat.find()) {
			return mat.group(1);
		} else {
			return url;
		}
	}
	
	/**
	 * 判断url是否为htpp url
	 * @param url
	 * @return
	 * Anddward.Liao <Anddward@gmail.com>
	 * 20142014-12-20上午11:38:03
	 */
	public static boolean ishttpUrl(String url){
		if (url.length()<4)return false;
		String match = url.substring(0, 4);
		if("http".equals(match)){
			return true;
		}
		return false;
	}
	

	/**
	 * 从出生日期算年龄
	 * 
	 * @param birth
	 *            出生年月日
	 * @return 要显示出来的年龄字符串
	 */
	public static String getAgeFromBirthday(String birth) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy", Locale.getDefault());
		int now = Integer.valueOf(format.format(new Date()));
		if (null != birth) {
			String[] tmp = birth.split("-");
			int age = Integer.valueOf(tmp[D.YEAR]);
			return (now - age) + "岁";
		} else {
			return "18岁";
		}
	}

	/**
	 * 解释“yyyy-mm-dd”为int[]数组
	 * 
	 * @param date
	 * @return
	 */
	public static int[] parseDate2Array(String date) {
		String[] tmp = date.split("-");
		return new int[] { Integer.valueOf(tmp[D.YEAR]), Integer.valueOf(tmp[D.MONTH]), Integer.valueOf(tmp[D.DAY]) };
	}

	/**
	 * 分析字串，返回DateTime对象
	 * 
	 * @param str
	 * @return
	 */
	public static DateTime getDateTimefromString(String str) {
		String date = formatDate(str);
		if (null != date) {
			int[] d = parseDate2Array(date);
			return new DateTime(d[D.YEAR], d[D.MONTH] - 1, d[D.DAY]);
		}
		Calendar c = Calendar.getInstance();
		return new DateTime(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
	}

	/**
	 * 格式化时间，格式：年年年年-月月-日日
	 * 
	 * @param date
	 *            字符串date "yyyy-mm-dd hh-mm-ss"
	 * @return 格式化后的时间字串
	 */
	public static String formatDate(String date) {
		String regEx = "\\d{4}-\\d{2}-\\d{2}";
		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(date);
		if (mat.find()) {
			return mat.group();
		}
		return null;
	}

	/**
	 * 从string.xml中获取字串
	 * 
	 * @param context
	 * @param resId
	 * @return resId 对应的字符串
	 */
	public static String getStringFromR(Context context, int resId) {
		String targetString = "";
		targetString = context.getResources().getString(resId);
		return targetString;
	}

	/**
	 * 翻转字串
	 * 
	 * @param str
	 * @return 翻转后的字串
	 */
	public static String getReverseString(String str) {
		StringBuilder strBuilder = new StringBuilder(str);
		return strBuilder.reverse().toString();
	}

	/**
	 * 对字串进行md5加密处理
	 * 
	 * @param sourceStr
	 * @return 加密处理后的字串
	 */
	public static String getMD5(String sourceStr) {
		String result = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(sourceStr.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			result = buf.toString();
		} catch (NoSuchAlgorithmException e) {
		}
		return result;
	}

	/**
	 * 除掉字符串中匹配的字符
	 * 
	 * @param str
	 *            需要处理的字串
	 * @param target
	 *            字符数组，包含想要去除的字符
	 * @return 去除指定字符后的字串
	 */
	public static String removeTargetInString(String str, char[] target) {
		StringBuilder result = new StringBuilder();
		Arrays.sort(target);
		for (int i = 0; i < str.length(); i++) {
			char tmp = str.charAt(i);
			if (0 > Arrays.binarySearch(target, tmp)) {
				result.append(tmp);
			}
		}
		return result.toString();
	}

	/**
	 * 正则获取11位电话号码
	 * @param str
	 * @return
	 */
	public static String getPhoneNumFromString(String str) {
		String regEx = "\\d{11}";
		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(str);
		if (mat.find()){
			return mat.group();
		}
		return str;
	}
}