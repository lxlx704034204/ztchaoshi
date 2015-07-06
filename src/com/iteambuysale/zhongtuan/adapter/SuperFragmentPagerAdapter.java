package com.iteambuysale.zhongtuan.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SuperFragmentPagerAdapter extends FragmentPagerAdapter {
	List<Fragment> mFlist;
	FragmentManager mFm;

	public SuperFragmentPagerAdapter(FragmentManager fm,List<Fragment> flist) {
		super(fm);
		mFlist = flist;
		mFm = fm;
	}

	@Override
	public Fragment getItem(int position) {
		return mFlist.get(position);
	}

	@Override
	public int getCount() {
		return mFlist.size();
	}
	
	@Override
    public int getItemPosition(Object object) {
//            return super.getItemPosition(object);
            return POSITION_NONE;
    }
}
