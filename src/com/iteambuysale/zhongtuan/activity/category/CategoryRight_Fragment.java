package com.iteambuysale.zhongtuan.activity.category;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.iteambuysale.zhongtuan.R;
import com.iteambuysale.zhongtuan.activity.specialsale.LadyCatgory;
import com.iteambuysale.zhongtuan.utilities.DBUtilities;
import com.iteambuysale.zhongtuan.utilities.ImageUtilities;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CategoryRight_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CategoryRight_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@SuppressLint("ValidFragment")
public class CategoryRight_Fragment extends Fragment implements AdapterView.OnItemClickListener {
    private static CategoryRight_Fragment fragment;
    private GridView gv_colmn;
    private Myadapter adapter;
    private String [] from=new  String[]{"_icon","_lbname"} ;
    private int[] to=new int[]{R.id.iv_right_ad,R.id.tv_small_cat};
    private String cup;

    public static CategoryRight_Fragment getinstance(){
       if(fragment==null){
           fragment =new CategoryRight_Fragment();
       }else {
           return fragment;
       }
       return  fragment;
   }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("rigtfragment oncreate() method");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        System.out.println("rigtfragment onCreateView() method");
         View view=inflater.inflate(R.layout.fragment_right_category, container, false);
         gv_colmn = (GridView) view.findViewById(R.id.gv_colmn);
        //setAdapter();
         return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        gv_colmn.setOnItemClickListener(this);
        System.out.println("rigtfragment onActivityCreated() method");
    }

    public void setAdapter(String id) {
        cup=id;
        Cursor cursor = DBUtilities.getsmallcategory(id);
       // Toast.makeText(getActivity(), "cursor length"+cursor.getCount(), 0).show();
        if(adapter!=null){
             adapter.changeCursor(cursor);
             adapter.notifyDataSetChanged();
         }else {
            if(getActivity()!=null){
             adapter=new Myadapter(getActivity(),R.layout.category_right_imageview,cursor,from,to);
             gv_colmn.setAdapter(adapter);
            // Toast.makeText(getActivity(), "context not null", 0).show();
            }else {
                System.out.println("categoryrightfragment getactivity is null");
            }
         }
//        Toast.makeText(getActivity(),"no data",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(getActivity(), LadyCatgory.class);
        intent.putExtra("cup", cup);
        intent.putExtra("tmid",l+"");
        startActivity(intent);
    }

    private class Myadapter extends SimpleCursorAdapter{

         public Myadapter(Context context, int layout, Cursor c, String[] from, int[] to) {
             super(context, layout, c, from, to);
         }

         @Override
         public void setViewImage(ImageView v, String value) {

             ImageUtilities.loadBitMap(value,v);
         }
     }

}
