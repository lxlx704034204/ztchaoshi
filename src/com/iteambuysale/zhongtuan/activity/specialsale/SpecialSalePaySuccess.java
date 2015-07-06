package com.iteambuysale.zhongtuan.activity.specialsale;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.activity.BaseActivity;
import com.iteambuysale.zhongtuan.activity.HomeActivity;

public class SpecialSalePaySuccess extends BaseActivity implements
		OnClickListener {
	private Button back;
	private TextView tittle;
	private Button btn_buy_continue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.spe_sale_pay_success);
		initview();
	}

	private void initview() {
		back = (Button) findViewById(R.id.back);
		tittle = (TextView) findViewById(R.id.tv_header_tittle);
		back.setOnClickListener(this);
		back.setBackgroundResource(R.drawable.header_back);
		btn_buy_continue = (Button) findViewById(R.id.btn_buy_continue);
		btn_buy_continue.setOnClickListener(this);
		tittle.setText("支付结果");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.btn_buy_continue:
			Intent intent = new Intent(getApplicationContext(),
					HomeActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
			break;

		default:
			break;
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
}
