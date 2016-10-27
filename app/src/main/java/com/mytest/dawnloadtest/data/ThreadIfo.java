package com.mytest.dawnloadtest.data;

/*
 * 下载线程的信息
 */

public class ThreadIfo {

	private int id;
	private String url;
	private int start;
	private int stop;
	private int finished;
	public ThreadIfo(int id, String url, int start, int stop, int finished) {
		super();
		this.id = id;
		this.url = url;
		this.start = start;
		this.stop = stop;
		this.finished = finished;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getStop() {
		return stop;
	}
	public void setStop(int stop) {
		this.stop = stop;
	}
	public int getFinished() {
		return finished;
	}
	public void setFinished(int finished) {
		this.finished = finished;
	}
	@Override
	public String toString() {
		return "ThreadIfo [id=" + id + ", url=" + url + ", start=" + start
				+ ", stop=" + stop + ", finished=" + finished + "]";
	}
	
	
}
