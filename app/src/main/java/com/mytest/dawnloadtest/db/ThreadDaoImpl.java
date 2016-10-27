package com.mytest.dawnloadtest.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mytest.dawnloadtest.data.ThreadIfo;

public class ThreadDaoImpl implements ThreadDao {

	private DBhelper dBhelper;

	public ThreadDaoImpl(Context context) {
		super();
		dBhelper = new DBhelper(context);
	}

	@Override
	public void insertThread(ThreadIfo threadIfo) {
		// TODO Auto-generated method stub
		SQLiteDatabase db=dBhelper.getWritableDatabase();
		db.execSQL("insert into thread_info(thread_id,url,start,end,finished) values(?,?,?,?,?)",
				new Object[]{threadIfo.getId(),threadIfo.getUrl(),threadIfo.getStart(),threadIfo.getStop(),threadIfo.getFinished()});
		db.close();
	}

	@Override
	public void deleteThread(String url, int thread_id) {
		// TODO Auto-generated method stub
		SQLiteDatabase db=dBhelper.getWritableDatabase();
		db.execSQL("delete from thread_info where url=? and thread_id=?",new Object[]{url,thread_id});
		db.close();
	}

	@Override
	public void updateThread(String url, int thread_id, int finished) {
		// TODO Auto-generated method stub
		SQLiteDatabase db=dBhelper.getWritableDatabase();
		db.execSQL("update thread_info set finished=? where url=? and thread_id=?",new Object[]{finished,url,thread_id});
		db.close();
	}

	@Override
	public List<ThreadIfo> getThread(String url) {
		// TODO Auto-generated method stub
		SQLiteDatabase db=dBhelper.getWritableDatabase();
		Cursor cursor=db.rawQuery("select * from thread_info where url=? ",new String[]{url});
		List<ThreadIfo> list=new ArrayList<ThreadIfo>();
		while (cursor.moveToNext()) {
			ThreadIfo threadIfo=new ThreadIfo(cursor.getInt(cursor.getColumnIndex("thread_id")), 
					cursor.getString(cursor.getColumnIndex("url")), 
					cursor.getInt(cursor.getColumnIndex("start")), 
					cursor.getInt(cursor.getColumnIndex("end")), 
					cursor.getInt(cursor.getColumnIndex("finished")));
			list.add(threadIfo);
		}
		cursor.close();
		db.close();
		return list;
	}

	@Override
	public boolean isexists(String url, int thread_id) {
		SQLiteDatabase db=dBhelper.getWritableDatabase();
		Cursor cursor=db.rawQuery("select * from thread_info where url=? and thread_id=?",new String[]{url,thread_id+""});
		if(cursor.moveToNext()) {
			return true;
		}
		cursor.close();
		db.close();
		return false;
	}

}
