package com.iteambuysale.zhongtuan.listener.near;

public interface CategoryListener {
	/**
	 * 根据传入的大类小类加载对应数据
	 * @author lforxeverc
	 * @param dl 大类id
	 * @param xl 小类id
	 */
	public void loadBusinessByTag(String dl,String xl);
}
