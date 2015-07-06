package com.iteambuysale.zhongtuan.actor.me;

import android.content.Context;
import android.database.Cursor;

import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.actor.SuperActor;
import com.iteambuysale.zhongtuan.adapter.OrderAdapter;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.me.OrderListListener;
import com.iteambuysale.zhongtuan.model.Model;
import com.iteambuysale.zhongtuan.model.OrderTG;

public class OrderListActor extends SuperActor {
	
	Context mContext;
	OrderListListener mListener;
	OrderAdapter mAdapter;
	public OrderListActor(Context context,OrderListListener listener) {
		super(context);
		mContext = context;
		mListener = listener;
		setCurrentModel(OrderTG.class);
		String[] from = new String[] { _("ordno"),_("fcpmc"),_("fcppic"),_("ordje"),_("ordsl"),_("ordzt") };
		int[] to = new int[] { $("orderNo"), $("title"),$("pic"),$("price"),$("count"),$("status")};
		mAdapter = new OrderAdapter(mContext, R.layout.x_list_order, null, from, to, mListener);
	}
	
	public void initViews(String tag){
		$lv("orderList").setAdapter(mAdapter);
		$lv("orderList").setOnItemClickListener(mListener);
//		$pv("orderpv").setPullDownListener(mListener);
		String[] keys = new String[]{_("ordzt")};
		String orderBy = _("ordno");
		String[] values;
		Cursor cr = null;
		switch (tag) {
		case D.OPT_PAYMENT:
			values = new String[]{"0"};
			cr = Model.load_more(OrderTG.class, keys, values, orderBy, true);
			break;
		case D.OPT_CONFORM:
			values = new String[]{"1","4","7"};
			cr = Model.load_more(OrderTG.class, keys, values, orderBy, true);
			break;
		case D.OPT_COMMENT:
			values = new String[]{"2","3"};
			cr = Model.load_more(OrderTG.class, keys, values, orderBy, true);
			break;
		case D.OPT_HISTORY:
			values = new String[]{"5","6","8","9"};
			cr = Model.load_more(OrderTG.class, keys, values, orderBy, true);
			break;
		default:
			break;
		}
		changeCursor(cr);
	}
	
	/**
	 * 刷新列表
	 */
	public void updateList() {
		mAdapter.notifyDataSetChanged();
	}
	
	/* ====================================== helpers ====================================== */

	/**
	 * 切换cursor
	 * @param cr
	 */
	private void changeCursor(Cursor cr) {
		Cursor oldCr = mAdapter.getCursor();
		mAdapter.changeCursor(cr);
		if (null != oldCr) {
			oldCr.close();
		}
		updateList();
	}
}
