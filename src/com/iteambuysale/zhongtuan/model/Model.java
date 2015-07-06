package com.iteambuysale.zhongtuan.model;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.iteambuysale.zhongtuan.ZhongTuanApp;
import com.iteambuysale.zhongtuan.annotation.Arg;
import com.iteambuysale.zhongtuan.annotation.Column;
import com.iteambuysale.zhongtuan.annotation.Table;
import com.iteambuysale.zhongtuan.define.D;
import com.iteambuysale.zhongtuan.utilities.StringUtilities;

public abstract class Model {
	

	/* ======================================== SAVE ============================================== */
	/**
	 * 保存model
	 */
	public void save() { 
		
		SQLiteDatabase _db = ZhongTuanApp.getInstance().getWDB();
		
		save(_db, true);
	}

	/**
	 * 保存model
	 * 
	 * @param allowNull
	 *            是否保存空内容
	 */
	public void save(boolean allowNull) {
		SQLiteDatabase _db = ZhongTuanApp.getInstance().getWDB();
		save(_db, allowNull);
	}

	/**
	 * 保存model
	 * 
	 * @param db
	 */
	public void save(SQLiteDatabase db) {
		save(db, true);
	}

	/**
	 * 存储整个list数据
	 * 
	 * @param tList
	 */
	public static <T extends Model> void save(T[] tList) {
		SQLiteDatabase _db = ZhongTuanApp.getInstance().getWDB();
		save(tList, _db);
	}
	/**
	 * 存之前先清空表
	 * @param tList
	 */
	public  <T extends Model> void save(T[] tList,boolean isdelete) {
		Class<? extends Model> clazz =this.getClass();
		String _tableName = getTableName(clazz);
		SQLiteDatabase _db = ZhongTuanApp.getInstance().getWDB();
		_db.delete(_tableName, null, null);
		save(tList, _db); 
		} 
	/**
	 * 存储整个list数据
	 * 
	 * @param tList
	 * @param db
	 */
	public static <T extends Model> void save(T[] tList, SQLiteDatabase db) {
		db.beginTransaction();
		for (T t : tList) {
			t.save(db);
		}
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	/**
	 * 保存model
	 * 
	 * @param db
	 */
	public void save(SQLiteDatabase db, boolean allowNull) {
		Class<? extends Model> clazz = this.getClass();
		String _tableName = getTableName(clazz);
		String _pk = getPKey(clazz);
		ContentValues _cv = getContentValues(clazz, allowNull);
		if (clazz.getAnnotation(Table.class).dbouble_key()) { // 双键处理
			String key1 = clazz.getAnnotation(Table.class).key1();
			String key2 = clazz.getAnnotation(Table.class).key2();
			String[] columns = new String[] { _pk, key1, key2 };
			String[] whereColumns = new String[] { key1, key2 };
			String[] values = new String[] { _cv.getAsString(key1), _cv.getAsString(key2) };
			String where = StringUtilities.createWhere(whereColumns, D.EQU, D.AND);
			if (isUnitRecoredExists(clazz, columns, where, values, db, _cv)) {
				db.update(_tableName, _cv, where, values);
				return;
			}
		}
		else if(_cv.get(_pk)==null){
			return;			
		}
		else if (isRecoredExists(clazz, _cv.getAsString(_pk), db)) {
			String where = StringUtilities.createWhere(_pk, D.EQU);
			String[] values = new String[] { _cv.getAsString(_pk) };
			db.update(_tableName, _cv, where, values);
			return;
		}
		db.insert(_tableName, null, _cv);
	}

	/* ========================================== LOAD ============================================ */

	/**
	 * 通过id获取实例
	 * 
	 * @param target
	 * @param id
	 * @return
	 */
	public static <T extends Model> T load(T target, String id) {
		Class<? extends Model> clazz = target.getClass();
		String[] keys = new String[] { getPKey(clazz) };
		String[] values = new String[] { id };
		Cursor _cr = load(clazz, keys, values, null, false);
		return load(target, _cr);
	}

	/**
	 * 根据类型，查询对象数组
	 * 
	 * @param clazz
	 * @param cr
	 * @return
	 */
	public static <T extends Model> ArrayList<T> load(Class<T> clazz, Cursor cr) {
		ArrayList<T> list = new ArrayList<T>();
		try {
			for (cr.moveToFirst(); !cr.isAfterLast(); cr.moveToNext()) {
				T target = clazz.newInstance();
				list.add(load(target, cr,cr.getPosition()));
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 查询数据
	 * 
	 * @param clazz
	 * @param keys
	 * @param values
	 * @param order
	 * @param reverse
	 * @return Raw Cursor
	 */
	public static Cursor load(Class<? extends Model> clazz, String[] keys, String[] values, String order,
			boolean reverse) {
		String _order = reverse ? order + " desc" : order;
		SQLiteDatabase _db = ZhongTuanApp.getInstance().getRDB();
		String _tableName = getTableName(clazz);
		String _where = StringUtilities.createWhere(keys, D.EQU, D.AND);
		Cursor _cr = _db.query(_tableName, null, _where, values, null, null, _order);
		return _cr;
	}
	
	public static Cursor load_more(Class<? extends Model> clazz, String[] keys, String[] values, String order,
			boolean reverse) {
		StringBuilder _whereBuilder=new StringBuilder();
		String _order = reverse ? order + " desc" : order;
		SQLiteDatabase _db = ZhongTuanApp.getInstance().getRDB();
		String _tableName = getTableName(clazz);
//		String _where = StringUtilities.createWhere(keys, D.EQU, D.AND);
//		Cursor _cr = _db.query(_tableName, null, _where, values, null, null, _order);
		for(int i=0;i<values.length;i++){
			_whereBuilder.append("_ordzt=? OR"+" ");
		}
		String _where=_whereBuilder.substring(0, _whereBuilder.length()-3);
		Cursor _cr = _db.query(_tableName, null, _where, values, null, null, _order);
		return _cr;
	}

	/**
	 * 通过cursor获取对象
	 * 
	 * @param target
	 * @param cr
	 * @return
	 */
	public static <T extends Model> T load(T target, Cursor cr) {
		if (cr.getCount() == 1){
			return load(target, cr, 0);
		}
		return target;
	}
	
	public static <T extends Model> T load(T target,Cursor cr,int position){
		cr.moveToPosition(position);
		Class<? extends Model> clazz = target.getClass();
		for (Field f : clazz.getDeclaredFields()) {
			if (!f.isAnnotationPresent(Column.class))
				continue;
			String value = cr.getString(cr.getColumnIndex(getColumnName(f)));
			setValue(target, f.getName(), value);
		}
		return target;
	}

	/* ========================================= GET NAME ============================================= */
	/**
	 * 从属性名得到对应的列名或参数名
	 * 
	 * @param clazz
	 * @param fieldName
	 * @param type
	 * @return
	 */
	public static <T extends Model> String getName(Class<T> clazz, String fieldName, int type) {
		Field _f = null;
		try {
			_f = clazz.getDeclaredField(fieldName);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		if (null != _f) {
			switch (type) {
			case D.COLUMN_NAME:
				return getColumnName(_f);
			case D.ARG_NAME:
				return getArgName(_f);
			default:
				return null;
			}
		}
		return null;
	}

	/**
	 * 获取主键名
	 * 
	 * @param clazz
	 * @return
	 */
	public static <T extends Model> String getPKey(Class<T> clazz) {
		for (Field f : clazz.getDeclaredFields()) {
			if (!f.isAnnotationPresent(Column.class))
				continue;
			if (!f.getAnnotation(Column.class).primary())
				continue;
			return getColumnName(f);
		}
		return "_id";
	}

	/**
	 * 获取列名
	 * 
	 * @param field
	 *            列
	 * @return
	 */
	public static String getColumnName(Field field) {
		if (field.isAnnotationPresent(Column.class)) {
			return field.getAnnotation(Column.class).name();
		}
		return null;
	}

	/**
	 * 获取列名
	 * 
	 * @param clazz
	 * @param fieldName
	 * @return
	 */
	public static <T extends Model> String getColumnName(Class<T> clazz, String fieldName) {
		return getName(clazz, fieldName, D.COLUMN_NAME);
	}

	/**
	 * 获取参数名
	 * 
	 * @param field
	 * @return
	 */
	public static <T extends Model> String getArgName(Field field) {
		if (field.isAnnotationPresent(Arg.class)) {
			return field.getAnnotation(Arg.class).name();
		}
		return null;
	}

	/**
	 * 获取参数名
	 * 
	 * @param clazz
	 * @param fieldName
	 * @return
	 */
	public static <T extends Model> String getArgName(Class<T> clazz, String fieldName) {
		return getName(clazz, fieldName, D.ARG_NAME);
	}

	/**
	 * 获取表名
	 * 
	 * @param clazz
	 * @return 当表名不能获取（没有设置@table）,返回null
	 */
	public static String getTableName(Class<? extends Model> clazz) {
		if (!clazz.isAnnotationPresent(Table.class)) {
			return null;
		}
		return clazz.getAnnotation(Table.class).name();
	}

	/* =============================================== DELETE ============================================ */
	/**
	 * 清空数据表所有数据
	 * 
	 * @param clazz
	 */
	public static <T extends Model> void delete(Class<T> clazz) {
		SQLiteDatabase db = ZhongTuanApp.getInstance().getWDB();
		String _tableName = getTableName(clazz);
		db.delete(_tableName, null, null);
	}

	/* ============================================= CHECK & TEST ======================================== */
	/**
	 * 检查记录是否已经存在
	 * 
	 * @param clazz
	 * @param id
	 * @return
	 */
	public static boolean isRecoredExists(Class<? extends Model> clazz, String id) {
		SQLiteDatabase db = ZhongTuanApp.getInstance().getRDB();
		return isRecoredExists(clazz, id, db);
	}

	/**
	 * 检查记录是否已经存在
	 * 
	 * @return true/false
	 */
	public static boolean isRecoredExists(Class<? extends Model> clazz, String id, SQLiteDatabase db) {
		String _where = StringUtilities.createWhere(getPKey(clazz), D.EQU);
		String _pk = getPKey(clazz);
		Cursor _cr = db.query(getTableName(clazz), new String[] { _pk }, _where, new String[] { id }, null, null, null);
		if (_cr.moveToFirst()) {
			return true;
		}
		_cr.close();
		return false;
	}

	/**
	 * 双键确认数据非重复
	 * 
	 * @param clazz
	 * @param columns
	 * @param where
	 * @param values
	 * @param db
	 * @return
	 */
	public static boolean isUnitRecoredExists(Class<? extends Model> clazz, String[] columns, String where,
			String[] values, SQLiteDatabase db, ContentValues cv) {
		boolean flag = false;
		String _pk = getPKey(clazz);
		Cursor _cr = db.query(getTableName(clazz), columns, where, values, null, null, null);
		if (_cr.moveToFirst()) {
			cv.put(_pk, _cr.getString(_cr.getColumnIndex(_pk)));
			flag = true;
		}
		_cr.close();
		return flag;
	}

	/* ============================================= GET TARGET =========================================== */
	/**
	 * 封装contentValues
	 * 
	 * @param clazz
	 * @return
	 */
	public ContentValues getContentValues(Class<? extends Model> clazz, boolean allwoNull) {
		ContentValues _cv = new ContentValues();
		for (Field f : clazz.getDeclaredFields()) {
			if (!f.isAnnotationPresent(Column.class))
				continue;
			String value = getStringValue(clazz, f.getName());
			if (allwoNull || null != value && !"".equals(value)) {
				_cv.put(getColumnName(f), value);
			}
		}
		return _cv;
	}

	/**
	 * 得到STRING型属性的值
	 * 
	 * @param clazz
	 * @param fieldName
	 * @return
	 */
	public String getStringValue(Class<? extends Model> clazz, String fieldName) {
		return (String) getValue(clazz, fieldName);
	}

	/**
	 * 获取指定属性的值
	 * 
	 * @param clazz
	 * @param fieldName
	 * @return 当不能获取指定属性的值,返回null
	 */
	public Object getValue(Class<? extends Model> clazz, String fieldName) {
		Object result = null;
		try {
			Method m = clazz.getDeclaredMethod("get" + StringUtilities.upCase(fieldName), new Class[] {});
			result = m.invoke(this, new Object[] {});
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return result;
	}

	/* ============================================= SET VALUES =========================================== */
	/**
	 * 设置指定属性的值
	 * 
	 * @param target
	 * @param fieldName
	 * @param value
	 */
	public static boolean setValue(Object target, String fieldName, String value) {
		try {
			Class<?> clazz = target.getClass();
			Method m = clazz.getDeclaredMethod("set" + StringUtilities.upCase(fieldName), new Class[] { String.class });
			m.invoke(target, value);
			return true;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return false;
	}

}
