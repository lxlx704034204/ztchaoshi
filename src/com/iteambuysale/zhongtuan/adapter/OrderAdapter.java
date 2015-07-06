package com.iteambuysale.zhongtuan.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.listener.me.OrderListListener;
import com.iteambuysale.zhongtuan.utilities.ImageUtilities;

public class OrderAdapter extends SimpleCursorAdapter  {
	Context context;

	public OrderAdapter(Context context, int layout, Cursor c, String[] from,
			int[] to, OrderListListener listener) {
		super(context, layout, c, from, to, 0);
	}

	@Override
	public void setViewImage(ImageView iv, String url) {
		ImageUtilities.loadBitMap(url, iv);
	}

	@Override
	public void setViewText(TextView v, String text) {
		super.setViewText(v, text);
		int vid = v.getId();
		switch (vid) {
		case R.id.orderNo:
			v.setText("订单号：" + text);
			break;
		case R.id.price:
			v.setText("￥" + text);
			break;
		case R.id.count:
			v.setText("共有" + text + "件商品");
			break;
		case R.id.status:
			int status = Integer.valueOf(text);
			switch (status) {
			case 0:
				v.setText("未付款");
				break;
			case 1:
				v.setText("已付款");
				break;
			case 2:
				v.setText("待评价");
				break;
			case 3:
				v.setText("已评价");
				break;
			case 4:
				v.setText("服务中");
				break;
			case 5:
				v.setText("退款处理中");
				break;
			case 6:
				v.setText("同意退款");
				break;
			case 7:
				v.setText("服务完成");
				break;
			case 8:
				v.setText("退款完成");
				break;
			case 9:
				v.setText("退款中");
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}
	}

}
