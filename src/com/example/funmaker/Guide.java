package com.example.funmaker;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

public class Guide extends Activity implements OnPageChangeListener {
	private ViewPager viewPager;
	private ViewGroup viewGroup;
	private ArrayList<View> list;
	private ImageView imageView;
	private ImageView[] imageViews;
	private LayoutInflater inflater;
	private MyAdapter adapter;
	
	private ImageView startbtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_guide);
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		viewGroup = (ViewGroup) findViewById(R.id.viewGroup);
         
		list = new ArrayList<View>();
		inflater = getLayoutInflater();
		list.add(inflater.inflate(R.layout.guide_00, null));
		list.add(inflater.inflate(R.layout.guide_01, null));
		list.add(inflater.inflate(R.layout.guide_02, null));
		
		imageViews = new ImageView[list.size()];

		for (int i = 0; i < list.size(); i++) {
			imageView = new ImageView(this);
			imageView.setLayoutParams(new LayoutParams(20, 20));
			imageView.setPadding(20, 0, 20, 0);

			imageViews[i] = imageView;
			if (i == 0) {
				// 暺恕餈蝔��洵銝���曄�鋡恍�銝�
				imageViews[i].setBackgroundResource(R.drawable.dot_white);
			} else {
				imageViews[i].setBackgroundResource(R.drawable.dot_white_30);
			}
			viewGroup.addView(imageView);
		}
		adapter = new MyAdapter(this, list);
		viewPager.setAdapter(adapter);
		viewPager.setOnPageChangeListener(this);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		for (int i = 0; i < imageViews.length; i++) {
			imageViews[arg0].setBackgroundResource(R.drawable.dot_white);
			if (arg0 != i) {
				imageViews[i].setBackgroundResource(R.drawable.dot_white_30);
			}
		}
		if (arg0==2) {
			startbtn = (ImageView) findViewById(R.id.startbtn);
			startbtn.setOnClickListener(new Button.OnClickListener(){ 
				@Override
				public void onClick(View v) {
					//TODO Auto-generated method stub
					AlertDialog.Builder dialog = new AlertDialog.Builder(Guide.this);
					dialog.setTitle("授權");	//設定dialog 的title顯示內容
					dialog.setIcon(android.R.drawable.ic_dialog_alert);	//設定dialog 的ICON
					dialog.setMessage("歡迎您使用四格漫畫，您是否同意我們蒐集您使用四格漫畫的作品，絕不涉及使用者的個人資料，一切資料必使用於學術之研究中。");
					dialog.setCancelable(false);	//關閉 Android 系統的主要功能鍵(menu,home等...)
					dialog.setPositiveButton("同意", new DialogInterface.OnClickListener() {
						@Override
					    public void onClick(DialogInterface dialog, int which) {  
					    // 按下"收到"以後要做的事情
					    	setting.agree = true;
					    	Intent intent = new Intent();
					    	intent.setClass(Guide.this,SAMMEditor.class);
							startActivity(intent);
							finish();
							imageViews = null;
							imageView = null;
							startbtn = null;
							System.gc();
					    }  
					});
					dialog.setNegativeButton("不同意", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							setting.agree = false;
					    	Intent intent = new Intent();
					    	intent.setClass(Guide.this,SAMMEditor.class);
							startActivity(intent);
							finish();
						}
					});
					dialog.show();

				}         
			});   
		}
	}
	
}