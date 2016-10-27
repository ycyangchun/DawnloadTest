package com.mytest.dawnloadtest.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBhelper extends SQLiteOpenHelper {

	
	
	public DBhelper(Context context) {
		super(context, "download.db", null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("create table thread_info(_id integer primary key autoincrement,thread_id integer," +
				"url text,start integer,end integer,finished integer)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("drop table if exists thread_info");
		db.execSQL("create table thread_info(_id integer primary key autoincrement,thread_id integer," +
				"url text,start integer,end integer,finished integer)");
	}

}
