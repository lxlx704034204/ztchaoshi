package com.iteambuysale.zhongtuan.model;

import java.util.Calendar;
import java.util.Locale;

import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.utilities.StringUtilities;

public class DateTime {
	private int day;
	private int year;
	private int month;

	public DateTime(int year,int month,int day) {
		super();
		this.day = day;
		this.year = year;
		this.month = month;
	}

	public DateTime() {
		Calendar c = Calendar.getInstance();
		this.day = c.get(Calendar.DAY_OF_MONTH);
		this.month = c.get(Calendar.MONTH);
		this.year = c.get(Calendar.YEAR);
	}
	
	public DateTime(String date){
		if (null!=date && !"".equals(date)){
			int[] tmp = StringUtilities.parseDate2Array(date);
			this.day = tmp[D.DAY];
			this.month = tmp[D.MONTH]-1;
			this.year = tmp[D.YEAR];
		}
	}

	@Override
	public String toString() {
		String tarString = "";
		String y = String.format(Locale.getDefault(), "%04d", getYear());
		String m = String.format(Locale.getDefault(), "%02d", getRealMonth());
		String d = String.format(Locale.getDefault(), "%02d", getDay());
		tarString = y + "-" + m + "-" + d;
		return tarString;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getRealMonth() {
		return this.month + 1;
	}

}
