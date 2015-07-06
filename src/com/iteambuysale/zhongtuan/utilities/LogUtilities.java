package com.iteambuysale.zhongtuan.utilities;

import android.util.Log;

import com.iteambuysale.zhongtuan.define.D;

public class LogUtilities {
	/**
	 *  设置debug开关 true为debug状态，false为关闭状态，默认打开
	 */
	private static Boolean debugState = true;
	
	/**
	 * 默认log为最高等级verbose
	 * @param tag log的tag
	 * @param msg log的信息
	 */
	public static void Log(String tag, String msg) {
		Log(tag, msg, "");
	}
	/**
	 * 包含等级限制的Log
	 * @param tag log的tag
	 * @param msg log的信息
	 * @param level log的等级 D.DEBUG_...开头（已在D.java命名）
	 */
	public static void Log(String tag, String msg, String level) {
		if (debugState) {
			if (level=="") {
				level = D.DEBUG_VERBOSE;
			}
			switch (level) {
			case D.DEBUG_VERBOSE:
				android.util.Log.v(tag, msg);
				break;
			case D.DEBUG_DEBUG:
				android.util.Log.d(tag, msg);
				break;
			case D.DEBUG_INFO:
				android.util.Log.i(tag, msg);
				break;
			case D.DEBUG_WARN:
				android.util.Log.w(tag, msg);
				break;
			case D.DEBUG_ERROR:
				android.util.Log.e(tag, msg);
				break;
			default:
				android.util.Log.v(tag,"你个逗比把level设置错了!---->"+msg);
				break;
			}
		}

	}
	/**
	 * log信息过长使用superLog()分段log
	 * @param sb log的超长信息嘿嘿
	 */
	public static void superLog(String sb){
		if(debugState){
		if (sb.length() > 4000) {
		    Log.d(D.NETWORK_DEBUG, "sb.length = " + sb.length());
		    int chunkCount = sb.length() / 4000;     // integer division
		    for (int i = 0; i <= chunkCount; i++) {
		        int max = 4000 * (i + 1);
		        if (max >= sb.length()) {
		            Log.d(D.NETWORK_DEBUG,sb.substring(4000 * i));
		        } else {
		            Log.d(D.NETWORK_DEBUG,sb.substring(4000 * i, max));
		        }
		    }
		} else {
		    Log.d(D.NETWORK_DEBUG,sb.toString());
		}
	}
	}
	/**
	 * 全局设置Log的开关，需要在程序入口处调用，调试开关状态 true为打开，false为关闭，默认为true
	 * @param state 
	 */
	public static void setDebugSwitch(Boolean state){
		debugState=state;
	}

}
