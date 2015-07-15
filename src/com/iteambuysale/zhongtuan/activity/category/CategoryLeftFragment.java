package com.iteambuysale.zhongtuan.activity.category;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.background.NetAsync;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.fragment.home.CategoryFragment;
import com.iteambuysale.zhongtuan.listener.global.NetAsyncListener;
import com.iteambuysale.zhongtuan.model.Model;
import com.iteambuysale.zhongtuan.model.SpecialSaleCatagory;
import com.iteambuysale.zhongtuan.utilities.DBUtilities;
import com.iteambuysale.zhongtuan.utilities.JsonUtilities;

import org.apache.http.NameValuePair;

import java.lang.reflect.Type;
import java.util.List;


@SuppressLint("ValidFragment")
public class CategoryLeftFragment extends Fragment implements NetAsyncListener, AdapterView.OnItemClickListener {

    private static CategoryLeftFragment fragment;
    String [] from =new String[]{"_lbname"};
    int [] to=new int[] { R.id.tv_left_adapter };
    private MyAdapter adapter;
    private View beforeview;


   public static CategoryLeftFragment getinstance(){
              if(fragment==null){
              fragment= new CategoryLeftFragment();
              } else{
                  return fragment;
              }
       return  fragment;
   }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("leftfragment onActivityCreated() method");

    }

        ListView lv_left_cat;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println("leftfragment onActivityCreated() method");
         View view=inflater.inflate(R.layout.fragment_category_left, container, false);
        lv_left_cat = (ListView)view.findViewById(R.id.lv_left_cat);
        return view;
    }

        Cursor cursor;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        System.out.println("leftfragment onActivityCreated() method");
        cursor = DBUtilities.gettmbiglb(false);
        adapter=new MyAdapter(getActivity(),R.layout.category_left_menu_textview,cursor,from,to);
        lv_left_cat.setAdapter(adapter);
        loadCatgoryproduct();
        lv_left_cat.setOnItemClickListener(this);


    }

    private void loadCatgoryproduct() {
        NetAsync catory = new NetAsync(D.API_SPECIAL_GETTMLB, this) {

            @Override
            public Object processDataInBackground(JsonElement elData) {
                Type type = new TypeToken<SpecialSaleCatagory[]>() {
                }.getType();
                SpecialSaleCatagory[] specialSaleCatagory = JsonUtilities
                        .parseModelByType(elData, type);
                Model.delete(SpecialSaleCatagory.class);
                Model.save(specialSaleCatagory);
                return specialSaleCatagory;
            }

            @Override
            public void beforeRequestInBackground(List<NameValuePair> params) {

            }
        };
        catory.execute();
    }


    @Override
    public void onResultError(String reqUrl, String errMsg) {

    }

    @Override
    public void onResultSuccess(String reqUrl, Object data) {
          switch (reqUrl){
              case D.API_SPECIAL_GETTMLB:
                  cursor=DBUtilities.gettmbiglb(false);
                  if (cursor.moveToFirst()){

                      CategoryFragment.getrightFragment().setAdapter(cursor.getString(cursor.getColumnIndex("_id")));
                  }
                  adapter.changeCursor(cursor);
                  break;

          }
    }


    @Override
    public void onTokenTimeout() {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        CategoryFragment.getrightFragment().setAdapter(l + "");
       // Toast.makeText(getActivity(),l+"",0).show();
        beforeview = view;
    }

    private class MyAdapter extends SimpleCursorAdapter {
        public MyAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
            super(context, layout, c, from, to);
        }

    }

}
