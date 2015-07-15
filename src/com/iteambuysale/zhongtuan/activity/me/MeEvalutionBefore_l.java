package com.iteambuysale.zhongtuan.activity.me;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.activity.BaseActivity;
import com.iteambuysale.zhongtuan.activity.me.unpay.ApplyRefundActivity;
import com.iteambuysale.zhongtuan.activity.me.unpay.EvaluateActivity;
import com.iteambuysale.zhongtuan.background.NetAsync;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.listener.global.NetAsyncListener;
import com.iteambuysale.zhongtuan.model.Model;
import com.iteambuysale.zhongtuan.model.OrderDetailsTM;
import com.iteambuysale.zhongtuan.model.OrderTM;
import com.iteambuysale.zhongtuan.utilities.DBUtilities;
import com.iteambuysale.zhongtuan.utilities.ImageUtilities;
import com.iteambuysale.zhongtuan.utilities.JsonUtilities;
import com.iteambuysale.zhongtuan.views.CustomProgressDialog;

public class MeEvalutionBefore_l extends BaseActivity implements OnClickListener, NetAsyncListener {

    private int[] to;
    private String[] from;
    private ListView listview;
    private final int GO_EVALUTE = 6;//去评价
    private final int ENSURE_PACKAGE = 9;//确认收货
    private final int APPLY_REFUND = 7;//		申请退款
    private final int CHECKED_LOGISTIC=10;//查看物流
    private int fromstatus;
    private final int UN_PAY = 0;
    private final int PAYED = 1;
    private final int WAIT_EVAL = 2;
    private final int SALE_AFTER = 3;
    private final int ALL_ORDER = 4;
    private CustomProgressDialog mProgressDialog;
    private int orderstatus;
    private SQLiteDatabase db;
    private boolean isfirst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.wait_pay_l);
        super.onCreate(savedInstanceState);
        initview();
        initdata();
    }

    private void initview() {
        ImageView iv_finish = (ImageView) findViewById(R.id.iv_finish);
        TextView tv_center_title = (TextView) findViewById(R.id.tv_center_title);
        tv_center_title.setText("评价晒单");
        listview = (ListView) findViewById(R.id.lv_order_wait_pay);
        iv_finish.setOnClickListener(this);
    }

    MyCursorAdapter adapter;
    String orderno;

    private void initdata() {
        mProgressDialog= CustomProgressDialog.createDialog(this);
        to = new int[]{R.id.iv_img_one, R.id.tv_product_name,
                R.id.tv_product_price, R.id.tv_buynums, R.id.tv_me_eval};
        from = new String[]{"_cppic", "_cpmc", "_oje", "_osl", "_ordnox"};
        orderno = getIntent().getStringExtra("orderno");
        orderstatus = getIntent().getIntExtra("orderstatus", -1);//intent.putExtra("orderstatus",orderstatus);
        fromstatus = getIntent().getIntExtra("Fromstatus", 0);
        Cursor cursor = DBUtilities.getcpmx(orderno + "%");
        adapter = new MyCursorAdapter(this, R.layout.order_detial_adapter_l, cursor, from, to);
        listview.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        db = ZhongTuanApp.getInstance().getRDB();
     //   System.out.println("外边orderstatus:"+orderstatus+!isfirst);
        if (!isfirst) {
      //      System.out.println("里边orderstatus:" + orderstatus + !isfirst);

            if (orderstatus == UN_PAY) {
                loadWaitPayOrder("0");
            } else if (orderstatus == PAYED) {
                if(fromstatus!=CHECKED_LOGISTIC){
                    System.out.println("fromstatus!=CHECKED_LOGISTIC");
                loadWaitPayOrder("1,4");
                }else {
                    System.out.println("CHECKED_LOGISTIC");
                    loadordermx(orderno);
                }
            } else if (orderstatus == WAIT_EVAL) {
                loadWaitPayOrder("2");
            } else if (orderstatus == SALE_AFTER) {
            }
        }else {
            if (orderstatus == PAYED) {
                if(fromstatus==CHECKED_LOGISTIC){
                    System.out.println("CHECKED_LOGISTIC");
                    loadordermx(orderno);
                }
            }
        }
        isfirst = false;

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_finish:
                finish();
                break;
            default:
                break;
        }

    }

    /**
     * 确认收货
     *
     * @param orderno
     */
    private void ensureGetGoods(final String orderno, final String onox) {
        if (mProgressDialog != null ) {
            mProgressDialog.show();
        }
        NetAsync ensureorder = new NetAsync(
                D.API_SPECIAL_ORDER_ENSURE_GOODS, this) {

            @Override
            public Object processDataInBackground(JsonElement elData) {
                return null;
            }

            @Override
            public void beforeRequestInBackground(List<NameValuePair> params) {
                params.add(new BasicNameValuePair("ordno", orderno));
                params.add(new BasicNameValuePair("onox", onox));
            }
        };
        ensureorder.execute();
    }

    protected void ensurePackage(final String orderno, final String onox) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(
                this);
        dialog.setTitle("提示");
        dialog.setMessage("是否确认收货？");
        dialog.setPositiveButton("确认",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0,
                                        int arg1) {
                        if (fromstatus == APPLY_REFUND) {

                        } else if (fromstatus == ENSURE_PACKAGE) {
                            ensureGetGoods(orderno, onox);
                        }
                    }
                });
        dialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0,
                                        int arg1) {
                        arg0.dismiss();
                    }
                });
        dialog.create().show();

    }
    private void loadordermx(final String orderno) {
        mProgressDialog.show();
        NetAsync deleteTask = new NetAsync(D.API_MY_TMORDERMX, this) {

            @Override
            public Object processDataInBackground(JsonElement elData) {
                Type type = new TypeToken<OrderDetailsTM[]>() {
                }.getType();
                OrderDetailsTM [] arrayOrder= JsonUtilities.parseModelByType(elData, type);
                Model.save(arrayOrder);
                System.out.println("save go on");
                return null;
            }

            @Override
            public void beforeRequestInBackground(List<NameValuePair> params) {

                params.add(new BasicNameValuePair("ordno", orderno));
            }
        };
        deleteTask.execute();
    }

    /*to = new int[] { R.id.iv_img_one, R.id.tv_product_name,
            R.id.tv_product_price, R.id.tv_buynums,R.id.tv_me_eval ,R.id.tv_me_eval2};
    from = new String[] { "_cppic", "_cpmc", "_oje", "_osl","_ordnox" ,"_ordzt"};*/
    private class MyCursorAdapter extends SimpleCursorAdapter {

        public MyCursorAdapter(Context context, int layout, Cursor c,
                               String[] from, int[] to) {
            super(context, layout, c, from, to);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            super.bindView(view, context, cursor);
            if (view != null) {
                TextView tv_me_eval = (TextView) view.findViewById(R.id.tv_me_eval);
                String ordzt = cursor.getString(cursor.getColumnIndex("_ordzt"));
                System.out.println("ordzt:" + ordzt);
                switch (ordzt) {
                    case "2"://已收货
                        if (fromstatus == ENSURE_PACKAGE) {
                            tv_me_eval.setBackgroundResource(R.color.default_gray);
                            tv_me_eval.setClickable(false);
                        }
                        break;
                    case "3"://已评价
                        if (fromstatus == GO_EVALUTE) {
                            tv_me_eval.setBackgroundResource(R.color.default_gray);
                            tv_me_eval.setClickable(false);
                        }
                        break;
                    default:
                        tv_me_eval.setBackgroundResource(R.color.default_red);
                        tv_me_eval.setClickable(true);
                        break;
                }
            }

        }

        @Override
        public void setViewText(TextView v, final String text) {
            switch (v.getId()) {
                case R.id.tv_product_price:
                    v.setText("￥" + text);
                    break;
                case R.id.tv_buynums:
                    v.setText("X" + text);
                    break;

                case R.id.tv_me_eval:
                    if (fromstatus == ENSURE_PACKAGE) {
                        v.setText("确认收货");
                    } else if (fromstatus == APPLY_REFUND) {
                        v.setText("申请退款");
                    }else if (fromstatus==CHECKED_LOGISTIC){
                        v.setText("查看物流");
                    }
                    v.setVisibility(View.VISIBLE);
                    v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (fromstatus == ENSURE_PACKAGE) {
                                ensurePackage(orderno, text);
                            } else {
                                if (fromstatus == APPLY_REFUND) {
                                    Intent apply_intent = new Intent(getApplicationContext(),
                                            ApplyRefundActivity.class);
                                    Cursor cursor = DBUtilities.getcpmx(text);
                                    if (cursor.moveToFirst()) {

                                        apply_intent.putExtra("ordno", orderno);
                                        apply_intent.putExtra("cost",cursor.getString(cursor.getColumnIndex("_oje")) );
                                        apply_intent.putExtra("onox", text);// "ORDER_LIST"的"_id"
                                        apply_intent.putExtra("sum", cursor.getString(cursor.getColumnIndex("_osl")));
                                        apply_intent.putExtra("productName", cursor.getString(cursor.getColumnIndex("_cpmc")));
                                        apply_intent.putExtra("currentstate", 1+"");//0团购，1特卖
                                        startActivity(apply_intent);
                                        finish();
                                    }
                                    //  Toast.makeText(getApplicationContext(), "此功能暂时未完成", 0).show();
                                } else if (fromstatus == GO_EVALUTE) {
                                    // Toast.makeText(getApplicationContext(), "去评价", 0).show();
                                    Intent intent = new Intent(getApplicationContext(), EvaluateActivity.class);
                                    Cursor cursor = DBUtilities.getcpmx(text);
                                    if (cursor.moveToFirst()) {
                                        intent.putExtra("id", getIntent().getStringExtra("orderid"));
                                        intent.putExtra("name", cursor.getString(cursor.getColumnIndex("_cpmc")));
                                        intent.putExtra("picurl", cursor.getString(cursor.getColumnIndex("_cppic")));
                                        intent.putExtra("cost", cursor.getString(cursor.getColumnIndex("_oje")));
                                        intent.putExtra("sum", cursor.getString(cursor.getColumnIndex("_oje")));
                                        intent.putExtra("time", getIntent().getStringExtra("time"));
                                        intent.putExtra("onox", text);
                                    }
                                    startActivity(intent);
                                } else if (fromstatus == CHECKED_LOGISTIC) {
                                    String logistic = DBUtilities.getLogistic(text);
                                    if (logistic != null) {
                                        String url = "http://app.teambuy.com.cn/webc/m/tmlog/id/"
                                                + logistic;
                                        System.out.println(url);
                                        Uri uri = Uri.parse(url);
                                        Intent intent = new Intent();
                                        intent.setAction("android.intent.action.VIEW");
                                        intent.setData(uri);
                                        startActivity(intent);
                                    }
                                }
                            }

                        }
                    });
                    break;
                default:
                    break;
            }
        }

        @Override
        public void setViewImage(ImageView v, String value) {
            ImageUtilities.loadBitMap(value, v);
        }

    }

    @Override
    public void onResultError(String reqUrl, String errMsg) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        switch (reqUrl){
            case D.API_MY_GETTMORDER:
                listview.setVisibility(View.INVISIBLE);
                break;
        }
    }

    @Override
    public void onResultSuccess(String reqUrl, Object data) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        switch (reqUrl) {
            case D.API_MY_GETTMORDER:
            case D.API_MY_TMORDERMX:
                Cursor cursor = DBUtilities.getcpmx(orderno + "%");
                if(cursor.getCount()<=0){
                    finish();
                   // listview.setVisibility(View.INVISIBLE);

                    return;
                }
                adapter.changeCursor(cursor);
                adapter.notifyDataSetChanged();
                break;
            case D.API_SPECIAL_ORDER_ENSURE_GOODS:
                loadWaitPayOrder("1,4");
                break;



        }
    }

    @Override
    public void onTokenTimeout() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        ZhongTuanApp.getInstance().logout(this,true);
    }

    private void loadWaitPayOrder(final String orderStatus) {
        mProgressDialog.show();
        NetAsync waitpayAsync = new NetAsync(D.API_MY_GETTMORDER, this) {
            @Override
            public Object processDataInBackground(JsonElement elData) {
                Type type = new TypeToken<Map<String, OrderTM>>() {
                }.getType();
                Model.delete(OrderTM.class);
                Model.delete(OrderDetailsTM.class);
                Map<String, OrderTM> orderMap = JsonUtilities
                        .parseModelByType(elData, type);
                db.beginTransaction();
                for (OrderTM o : orderMap.values()) {
                    o.save(db);
                    OrderDetailsTM[] odList = o.getCpmx();
                    Model.save(odList, db);
                }
                db.setTransactionSuccessful();
                db.endTransaction();
                return orderMap;
            }

            @Override
            public void beforeRequestInBackground(List<NameValuePair> params) {
                params.add(new BasicNameValuePair("ordzt", orderStatus));
            }
        };
        waitpayAsync.execute();
    }
}
