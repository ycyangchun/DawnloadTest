package com.mytest.dawnloadtest.service;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import com.mytest.dawnloadtest.data.FileIfo;
import com.mytest.dawnloadtest.data.ThreadIfo;
import com.mytest.dawnloadtest.db.ThreadDao;
import com.mytest.dawnloadtest.db.ThreadDaoImpl;

public class DownloadTask {

	private Context mContext = null;
	private FileIfo fileIfo = null;
	private ThreadDao threadDao = null;
	private int mfinished = 0;
	public boolean ispause = false;

	public DownloadTask(Context mContext, FileIfo fileIfo) {
		super();
		this.mContext = mContext;
		this.fileIfo = fileIfo;
		threadDao = new ThreadDaoImpl(mContext);
	}

	public void download() {
		// 读取数据库的线程信息
		List<ThreadIfo> mlist = threadDao.getThread(fileIfo.getFileurl());
		ThreadIfo threadIfo = null;
		if (mlist.size() == 0) {
			threadIfo = new ThreadIfo(0, fileIfo.getFileurl(), 0,
					fileIfo.getFilelength(), 0);
		} else {
			threadIfo = mlist.get(0);
		}
		// 创建子线程进行下载
		new Downloadthread(threadIfo).start();
	}

	class Downloadthread extends Thread {
		private ThreadIfo threadIfo = null;

		public Downloadthread(ThreadIfo threadIfo) {
			super();
			this.threadIfo = threadIfo;
		}

		@Override
		public void run() {
			// 向数据库插入线程信息
			if (!threadDao.isexists(threadIfo.getUrl(), threadIfo.getId())) {
				threadDao.insertThread(threadIfo);
			}
			// 设置下载位置
			URL url = null;
			HttpURLConnection conn = null;
			RandomAccessFile raf = null;
			InputStream is = null;
			try {
				url = new URL(threadIfo.getUrl());
				conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(3000);
				conn.setRequestMethod("GET");

				int start = threadIfo.getStart() + threadIfo.getFinished();
				conn.setRequestProperty("Range", "bytes=" + start + "-"
						+ threadIfo.getStop());

				// 设置文件的写入位置
				File file = new File(DownloadService.DOWNLOAD_PATH,
						fileIfo.getFilename());
				raf = new RandomAccessFile(file, "rwd");
				raf.seek(start);
				Intent intent = new Intent(DownloadService.ACTION_UPDATE);
				mfinished += threadIfo.getFinished();
				// 开始下载
				if (conn.getResponseCode() == 206) {
					// 读取数据
					is = conn.getInputStream();
					byte[] buffer = new byte[1024 * 4];
					int len = -1;
					long time = System.currentTimeMillis();
					while ((len = is.read(buffer)) != -1) {
						// 写入文件
						raf.write(buffer, 0, len);
						mfinished += len;
						// 把下载进度发送广播给activity
						if (System.currentTimeMillis() - time > 500) {
							intent.putExtra("finished", mfinished * 100
									/ fileIfo.getFilelength());
							mContext.sendBroadcast(intent);
							time = System.currentTimeMillis();
						}

						// 在下载暂停时保存下载进度
						if (ispause) {
							threadDao.updateThread(threadIfo.getUrl(),
									threadIfo.getId(), mfinished);
							return;
						}
					}

					Intent intent2 = new Intent();
					intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent2.setAction(android.content.Intent.ACTION_VIEW);
					Uri uri = Uri.fromFile(new File(Environment
							.getExternalStorageDirectory().getAbsolutePath()
							+ "/downloads/imooc.apk")); // 这里是APK路径
					intent2.setDataAndType(uri,
							"application/vnd.android.package-archive");
					mContext.startActivity(intent2);

					threadDao.deleteThread(threadIfo.getUrl(),
							threadIfo.getId());
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				conn.disconnect();
				try {
					raf.close();
					is.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}
	}
}
