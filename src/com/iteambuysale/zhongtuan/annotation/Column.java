package com.iteambuysale.zhongtuan.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
	boolean primary() default false;
	String name();
	String type() default "VARCHAR";
	int len() default 20;
}
