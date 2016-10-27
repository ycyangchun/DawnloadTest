package com.mytest.dawnloadtest.data;

import java.io.Serializable;


/**
 * 下载文件的信息
 */
public class FileIfo implements Serializable{

	private int id;
	private String filename;
	private String fileurl;
	private int filelength;
	private int finished;
	
	
	
	
	public FileIfo(int id, String filename, String fileurl, int filelength,
			int finished) {
		super();
		this.id = id;
		this.filename = filename;
		this.fileurl = fileurl;
		this.filelength = filelength;
		this.finished = finished;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getFileurl() {
		return fileurl;
	}
	public void setFileurl(String fileurl) {
		this.fileurl = fileurl;
	}
	public int getFilelength() {
		return filelength;
	}
	public void setFilelength(int filelength) {
		this.filelength = filelength;
	}
	public int getFinished() {
		return finished;
	}
	public void setFinished(int finished) {
		this.finished = finished;
	}

	@Override
	public String toString() {
		return "FileIfo [id=" + id + ", filename=" + filename + ", fileurl="
				+ fileurl + ", filelength=" + filelength + ", finished="
				+ finished + "]";
	}
	
	
	
}
