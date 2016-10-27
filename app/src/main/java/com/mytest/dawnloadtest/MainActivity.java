package com.mytest.dawnloadtest;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mytest.dawnloadtest.data.FileIfo;
import com.mytest.dawnloadtest.service.DownloadService;

public class MainActivity extends Activity {

	private Button start;
	private Button stop;
	private TextView name;
	private ProgressBar pBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();

		IntentFilter intentFilter=new IntentFilter();
		intentFilter.addAction(DownloadService.ACTION_UPDATE);
		registerReceiver(broadcastReceiver, intentFilter);
		// 创建文件信息对象

		final FileIfo fileIfo = new FileIfo(
				0,
				"imooc.apk",
				"http://61.182.134.3:9090/201610090001a.apk",
				0, 0);
		name.setText(fileIfo.getFilename());
		start.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent intent=new Intent(MainActivity.this,DownloadService.class);
				intent.setAction("ACTION_START");
				intent.putExtra("fileifo", fileIfo);
				startService(intent);
			}
		});
		stop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent intent=new Intent(MainActivity.this,DownloadService.class);
				intent.setAction("ACTION_STOP");
				intent.putExtra("fileifo", fileIfo);
				startService(intent);
			}
		});
	}

	private void initView() {
		// TODO Auto-generated method stub
		start = (Button) findViewById(R.id.download_bt);
		stop = (Button) findViewById(R.id.stop_bt);
		name = (TextView) findViewById(R.id.name_tv);
		pBar = (ProgressBar) findViewById(R.id.myprogressBar);
		
		pBar.setMax(100);
	}
	
	BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (DownloadService.ACTION_UPDATE.equals(intent.getAction())) {
				pBar.setProgress(intent.getIntExtra("finished", 0));
			}
		}
	};
	
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(broadcastReceiver);
	};
}
