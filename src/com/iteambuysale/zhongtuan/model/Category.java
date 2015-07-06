package com.iteambuysale.zhongtuan.model;

import java.io.Serializable;
import java.util.List;

public class Category implements Serializable {

	private static final long serialVersionUID = 6101917016646233273L;
	private String name;
	private String id;
	private List<Category> subCategorys;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Category> getSubCategorys() {
		return subCategorys;
	}

	public void setSubCategorys(List<Category> subCategorys) {
		this.subCategorys = subCategorys;
	}

}
