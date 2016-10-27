package com.mytest.dawnloadtest.db;

import java.util.List;

import com.mytest.dawnloadtest.data.ThreadIfo;

public interface ThreadDao {

	public void  insertThread(ThreadIfo threadIfo);
	public void  deleteThread(String url,int thread_id );
	public void  updateThread(String url,int thread_id,int finished );
	public List<ThreadIfo>  getThread(String url);
	public boolean isexists(String url,int thread_id);
}
