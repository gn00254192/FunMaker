package com.example.funmaker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.example.funmaker.R.id;

import android.R.color;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.audiofx.BassBoost.Settings;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Alist extends Activity {
	Boolean searchable = true;
	ListView lv;
	TextView tv;
	int[] images;
	static String[][] ulrlist = new String[5][3];
	private Bitmap[][] image = new Bitmap[5][3];
	private ProgressDialog progressDialog = null;
	
	/**載入*/
	public class PostTask extends AsyncTask<Void, String, Bitmap> {
		public ImageView im;
		public int p;

		public PostTask(int position) {
			// TODO Auto-generated constructor stub
			p = position;

		}

		
		
		@Override
		protected Bitmap doInBackground(Void... params) {

			// All your code goes in here

			// If you want to do something on the UI use progress update
			Log.v("Search image", "載入類別" + p + "圖片。");
			image[p][0] = getBitmapFromURL(ulrlist[p][0]);
			image[p][1] = getBitmapFromURL(ulrlist[p][1]);
			image[p][2] = getBitmapFromURL(ulrlist[p][2]);
			return image[p][1];
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			lv.setAdapter(new ImageTextAdapter(Alist.this));
			if ((p + 1) < 5) {
				PostTask posttask;
				posttask = new PostTask(p + 1);

				posttask.execute();
			} else {
				searchable = true;
				progressDialog.dismiss();
			}
				
			super.onPostExecute(result);
		}

		protected void onProgressUpdate(String progress) {

		}
	}

	public class PostTask1 extends AsyncTask<Void, String, Bitmap> {
		public String surl;

		public PostTask1(String string) {
			surl = string;
			// TODO Auto-generated constructor stub
		}

		@Override
		protected Bitmap doInBackground(Void... params) {
			Log.v("search image", "開始搜尋 " + surl + "。");

			// All your code goes in here

			// If you want to do something on the UI use progress update
			String str;

			URL myUrl = null;
			//搜尋關鍵字
			try {
				myUrl = new URL(
						"http://summerimagenetapi.appspot.com/imagenetapi?act=1&name="
								+ URLEncoder.encode(surl, "utf-8"));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.v("Search image", "MalformedURLException");  
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.v("Search image", "UnsupportedEncodingException");  

			}
			
			// 取得 URLConnection
			HttpURLConnection conn;
			try {
				conn = (HttpURLConnection) myUrl.openConnection();

				conn.setDoInput(true); // 設定為可從伺服器讀取資料
				conn.setDoOutput(false); // 設定為可寫入資料至伺服器
				conn.setRequestMethod("GET"); // 設定請求方式為 GET
				// 以下是設定 MIME 標頭中的 Content-type
				conn.setRequestProperty("Content-type",
						"application/x-www-form-urlencoded");
				conn.connect(); // 開始連接
				// 透過 URLConnection 的 getOutputStream() 取的 OutputStream,
				// 並建立以UTF-8
				// 為編碼的 OutputStreamWriter

				// 利用 URLConnection 的 getInputStream() 取得 InputStream
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(conn.getInputStream(), "UTF-8"));
				for (int i = 0; i < 5; i++) {
					Log.v("Search image", "截取類別" + i + "ID。");

					str = reader.readLine();
					if (str != null) {
						str=str.substring(0,str.indexOf(","));
						setting.node[i] = str;
					} else
						break;
					str = reader.readLine();
					if (str != null) {
						ulrlist[i][0] = str;
						// image[i][0] = getBitmapFromURL(ulrlist[i][0]);
					} else
						break;
					str = reader.readLine();
					if (str != null) {
						ulrlist[i][1] = str;
						// image[i][1] = getBitmapFromURL(ulrlist[i][1]);

					} else
						break;
					str = reader.readLine();
					if (str != null) {
						ulrlist[i][2] = str;
						// image[i][2] = getBitmapFromURL(ulrlist[i][2]);

					} else
						break;

				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;

		}

		protected void onPostExecute(Bitmap reString) {
			lv.setAdapter(new ImageTextAdapter(Alist.this));

			PostTask posttask;
			posttask = new PostTask(0);

			posttask.execute();

			super.onPostExecute(reString);

		}

		protected void onProgressUpdate(String progress) {

		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("搜尋圖庫");
		//全螢幕顯示
		this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//不顯示標題列
	    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_alist);
		lv = (ListView) findViewById(R.id.listView1);
		Button btn = (Button) findViewById(id.button1);
		tv = (TextView) findViewById(R.id.textView_search);
		btn.setOnClickListener(push);
		tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tv.setBackgroundColor(color.holo_blue_bright);
				finish();
			}
		});
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// PostTask posttask;
				// posttask = new PostTask(0);
				//
				// posttask.execute();
				// TODO Auto-generated method stub
				setting.select = arg2;
				Log.v("Search image", "選擇了類別" + arg2);
				Intent intent = new Intent(Alist.this, Result.class);
				startActivityForResult(intent, 9527);
//				Toast.makeText(getApplicationContext(),
//						"this is " + (arg2 + 1), Toast.LENGTH_SHORT).show();
			}
		});

	}
	
	@Override  
    protected void onDestroy() {  
        super.onDestroy();  
        Log.v("Destory", "Alist onDestroy");
//        for(int i=0;i<5;i++)
//        	for(int j=0;j<3;j++) {
//        		if(image[i][j] != null) 
//            		image[i][j].recycle();
//        	}
//        image = null;
        System.gc();
    }  
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==9527&&resultCode==RESULT_OK) {
			
			Intent intent=new Intent();
			Bundle bundle = new Bundle();
			bundle.putString("path", setting.imagenet_selected_image_path);
			intent.putExtra("bundle", bundle);
			setResult(RESULT_OK, intent);
			finish();
		}
	}
	
	
	
	private Button.OnClickListener push = new Button.OnClickListener() {
		public void onClick(View arg0) {
			if (searchable) {
				progressDialog = ProgressDialog.show(Alist.this, "請稍等...", "搜尋中...", true);
//				progressDialog = new ProgressDialog(Alist.this);
//				progressDialog.setTitle("請稍等...");
//				progressDialog.setMessage("搜尋圖片中...");
//				progressDialog.setCancelable(true);
//				progressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "取消", new DialogInterface.OnClickListener() {
//				    @Override
//				    public void onClick(DialogInterface dialog, int which) {
//				        dialog.dismiss();
//				        
//				    }
//				});
//				progressDialog.show();
				searchable = false;
				EditText Et = (EditText) findViewById(id.editText1);
				PostTask1 posttask;
				posttask = new PostTask1(Et.getText().toString());

				posttask.execute();
			}
		}
	};

	public class ImageTextAdapter extends BaseAdapter {
		LayoutInflater layoutInflater;

		class ViewHolder {
			ImageView imageview, imageview1, imageview2;
			TextView text;
		}

		ImageTextAdapter(Context c) {
			layoutInflater = (LayoutInflater) c
					.getSystemService(LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 5;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			// TODO Auto-generated method stub
			ViewHolder viewholder;
			if (convertView == null) {
				convertView = layoutInflater.inflate(R.layout.listview, null);
				viewholder = new ViewHolder();
				viewholder.text = (TextView) convertView
						.findViewById(R.id.textView1);
				viewholder.imageview = (ImageView) convertView
						.findViewById(R.id.imageView1);
				viewholder.imageview1 = (ImageView) convertView
						.findViewById(R.id.ImageView01);
				viewholder.imageview2 = (ImageView) convertView
						.findViewById(R.id.ImageView02);
				convertView.setTag(viewholder);
			} else {
				viewholder = (ViewHolder) convertView.getTag();
			}

			if (image[position][0] != null) {
				viewholder.imageview.setImageBitmap(image[position][0]);

			} else {
				viewholder.imageview.setImageResource(R.drawable.ic_launcher);
			}
			if (image[position][1] != null) {
				viewholder.imageview1.setImageBitmap(image[position][1]);

			} else {
				viewholder.imageview1.setImageResource(R.drawable.ic_launcher);
			}
			if (image[position][2] != null) {
				viewholder.imageview2.setImageBitmap(image[position][2]);

			} else {
				viewholder.imageview2.setImageResource(R.drawable.ic_launcher);
			}

			viewholder.text.setText("TOP" + (position + 1));
			viewholder.text.setTextSize(18);
			viewholder.text.setTextColor(Color.WHITE);
			return convertView;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.alist, menu);
		return true;
	}

	public static Bitmap getBitmapFromURL(String src) {
		try {
			URL url = new URL(src);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
