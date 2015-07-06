package com.iteambuysale.zhongtuan.listener.login;

import android.text.TextWatcher;

import com.iteambuysale.zhongtuan.listener.global.NetAsyncListener;
import com.iteambuysale.zhongtuan.listener.global.ValidateListener;
import com.iteambuysale.zhongtuan.listener.global.YzmListener;


public interface RegisterListener extends NetAsyncListener, YzmListener ,ValidateListener ,TextWatcher{

}
