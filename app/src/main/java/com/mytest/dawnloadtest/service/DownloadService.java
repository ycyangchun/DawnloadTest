package com.mytest.dawnloadtest.service;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;

import com.mytest.dawnloadtest.data.FileIfo;

public class DownloadService extends Service {

	
	private DownloadTask task=null;
	public static final int initmsg = 0;
	public static final String ACTION_START = "ACTION_START";
	public static final String ACTION_STOP = "ACTION_STOP";
	public static final String ACTION_UPDATE = "ACTION_UPDATE";
	public static final String DOWNLOAD_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/downloads/";

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		if (ACTION_START.equals(intent.getAction())) {
			FileIfo fileIfo = (FileIfo) intent.getSerializableExtra("fileifo");
			System.out.println("文件信息已经开始" + fileIfo.toString());
			
			//启动初始化线程
			new InitThread(fileIfo).start();
		} else if (ACTION_STOP.equals(intent.getAction())) {
			FileIfo fileIfo = (FileIfo) intent.getSerializableExtra("fileifo");
			System.out.println("文件信息已经停止" + fileIfo.toString());
			if (task!=null) {
				task.ispause=true;
			}
		}

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case initmsg:
				FileIfo fileIfo=(FileIfo) msg.obj;
				
				System.out.println("handler中的fileIfo"+fileIfo.toString());
				task=new DownloadTask(getApplicationContext(), fileIfo);
				task.download();
				break;

			default:
				break;
			}
		};
	};

	/*
	 * 初始化子线程
	 */
	class InitThread extends Thread {

		private FileIfo fileIfo = null;

		public InitThread(FileIfo fileIfo) {
			super();
			this.fileIfo = fileIfo;
		}

		@Override
		public void run() {
			HttpURLConnection conn = null;
			RandomAccessFile raf= null;
			try {

				// 连接网络文件
				URL url = new URL(fileIfo.getFileurl());
				conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(3000);
				conn.setRequestMethod("GET");
				int length = -1;
				if (conn.getResponseCode() == 200) {
					// 获得文件长度
					length = conn.getContentLength();
					System.out.println("这是文件的长度"+length);
				}
				if (length <= 0) {
					return;
				}
				File dir = new File(DOWNLOAD_PATH);
				if (!dir.exists()) {
					dir.mkdir();
				}
				// 在本地创建文件
				File file = new File(dir, fileIfo.getFilename());
				raf = new RandomAccessFile(file, "rwd");
				
				// 设置文件长度
				raf.setLength(length);
				fileIfo.setFilelength(length);
				handler.obtainMessage(initmsg,fileIfo).sendToTarget();
				
				raf.close();
				conn.disconnect();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
