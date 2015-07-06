package com.iteambuysale.zhongtuan.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;

public class ZTQSCAdapter extends SimpleCursorAdapter {

	public ZTQSCAdapter(Context context, int layout, Cursor c, String[] from,
			int[] to, int flags) {
		super(context, layout, c, from, to, flags);
	}

}
