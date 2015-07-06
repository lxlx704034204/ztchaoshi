package com.base;

import android.content.Context;
import android.view.View;

public class Basepager {
	public Context context;
	private View rootview;
	private boolean istemai;

	public Basepager(Context context) {
		this.context = context;
		rootview = initview();
	}

	public View initview() {
		return null;
	};

	public View getRootview() {
		return rootview;
	}

	public void setistmai(boolean istemai) {
		this.istemai = istemai;
	}
	public boolean getismai(){
		return istemai;
	}
	public void notedata(){
		
	}
}
