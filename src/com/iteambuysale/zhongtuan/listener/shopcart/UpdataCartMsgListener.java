package com.iteambuysale.zhongtuan.listener.shopcart;

import java.util.ArrayList;

public interface UpdataCartMsgListener  {
   public void updataCartMsg(String ctid, String sl);
   public void changeprice(ArrayList<Integer> selectnum);
}
