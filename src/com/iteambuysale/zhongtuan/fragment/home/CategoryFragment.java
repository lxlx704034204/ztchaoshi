package com.iteambuysale.zhongtuan.fragment.home;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.activity.category.CategoryLeftFragment;
import com.iteambuysale.zhongtuan.activity.category.CategoryRight_Fragment;
import com.iteambuysale.zhongtuan.listener.global.NetAsyncListener;

import org.apache.http.NameValuePair;

import java.lang.reflect.Type;
import java.util.List;
public class CategoryFragment extends Fragment  {
    private FrameLayout fl_left_frag;
    private FrameLayout fl_right_frag;
    private static CategoryLeftFragment leftFragment;
    private static  CategoryRight_Fragment rightFragment;
//    private boolean flag=false;

    public static CategoryFragment newInstance() {
        CategoryFragment fragment = new CategoryFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("fragment oncreate()");
//        leftFragment =new   CategoryLeftFragment();
//        rightFragment =new CategoryRight_Fragment();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println("salefragment oncreateview()");
        View view=inflater.inflate(R.layout.fragment_category, container, false);
        TextView tv_header_title= (TextView) view.findViewById(R.id.tv_header_tittle);
        tv_header_title.setText(R.string.category);
        fl_left_frag = (FrameLayout) view.findViewById(R.id.fl_left_frag);
        fl_right_frag = (FrameLayout) view.findViewById(R.id.fl_right_frag);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        System.out.println("salefragment onActivityCreate()");
        super.onActivityCreated(savedInstanceState);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        leftFragment =new   CategoryLeftFragment();
        rightFragment =new CategoryRight_Fragment();
           ft.replace(R.id.fl_left_frag, leftFragment);
      //  if (!flag){
           // ft.remove(rightFragment);
           // ft.add(R.id.fl_right_frag, rightFragment);
            ft.replace(R.id.fl_right_frag, rightFragment);
      //  }
       // flag=true;
        ft.commit();

    }
    public   static CategoryLeftFragment getleftFragment(){

//        return CategoryLeftFragment.getinstance();
        return leftFragment;

    }
    public  static CategoryRight_Fragment getrightFragment(){
        return  rightFragment;
    }

}
