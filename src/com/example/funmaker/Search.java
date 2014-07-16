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

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class Search extends Activity {
	ProgressDialog mDialog;

	TextView tv;
	Bitmap[] imageids;
	int sum = 1;
	static int i;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//全螢幕顯示
		this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//不顯示標題列
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_search);

		Button btn = (Button) findViewById(R.id.button1);

		btn.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				mDialog = new ProgressDialog(Search.this);
				mDialog.setMessage("Loading...");
				mDialog.setCancelable(false);
				mDialog.show();
				EditText edt = (EditText) findViewById(R.id.editText1);
				PostTask2 posttask;
				posttask = new PostTask2(edt.getText().toString());

				posttask.execute();

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		menu.add(0, 0, 0, "more");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// 依據itemId來判斷使用者點選哪一個item
		switch (item.getItemId()) {
		case 0:
			if (setting.stat == 1) {
				Intent intent = new Intent(this, Showmore.class);
				startActivity(intent);
			}
			finish();
			break;

		default:
		}
		return super.onOptionsItemSelected(item);
	}

	public class PostTask2 extends AsyncTask<Void, String, String> {
		public String surl;

		public PostTask2(String string) {
			surl = string;
			// TODO Auto-generated constructor stub
		}

		@Override
		protected String doInBackground(Void... params) {
			Log.v("adasd", "ewfwefweffewfwewfwef");

			// All your code goes in here

			// If you want to do something on the UI use progress update
			String str = "";

			URL myUrl = null;
			try {
				myUrl = new URL("http://mjimagenetapi.appspot.com/search?name="
						+ URLEncoder.encode(surl, "utf-8"));
				setting.search = URLEncoder.encode(surl, "utf-8");

			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
				int count = 0;
				imageids = new Bitmap[6];
				int i = 0;
				if ((str = reader.readLine()) != null) {
					if (Integer.parseInt(str) == 1) {
						setting.stat = 1;
						while ((str = reader.readLine()) != null && count < 6) {
							setting.imagelist[count][0] = str;
							if ((str = reader.readLine()) != null)
								setting.imagelist[count][1] = str;
							Log.v("asdasd", setting.imagelist[count][0] + "");
							imageids[count] = getBitmapFromURL(setting.imagelist[count][1]);
							if (imageids[count] != null)
								count++;

						}
					} else if (Integer.parseInt(str) == 2) {
						setting.stat = 2;
						while ((str = reader.readLine()) != null && count < 6) {
							str = str.substring(2);
							setting.imagelist[count][0] = str.substring(0,
									str.indexOf(",") + 3);
							setting.imagelist[count][1] = str.substring(
									str.indexOf(",") + 3, str.length());
							imageids[count] = getBitmapFromURL(setting.imagelist[count][1]);
							if (imageids[count] != null)
								count++;

						}
					}
//					LOG.OUT(setting.stat);
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return str;

		}

		protected void onPostExecute(String reString) {
			String str = "";
			ImageButton img;
			img = (ImageButton) Search.this.findViewById(R.id.imageView1);
			int num = 0;
			for (i = 0; i < 2; i++) {
				if (imageids[i] != null) {
					img.setImageBitmap(imageids[i]);
					final int a = i;
					img.setOnClickListener(new ImageButton.OnClickListener() {
						@Override
						public void onClick(View v) {
							if (setting.stat == 2) {
								Intent intent = new Intent(Search.this,DrawEditor.class);
								startActivity(intent);
								setting.imagenumber = a;
								Search.this.finish();
							} else if (setting.stat == 1) {
								PostTaskforchoice posttask;
								posttask = new PostTaskforchoice();
								posttask.execute();
								setting.imagenumber = a;
								Search.this.finish();
							}

						}
					});

					num = img.getId() + 2;
					img = (ImageButton) Search.this.findViewById(num);
				}
			}
			for (i = 2; i < 6; i++) {
				if (imageids[i] != null) {
					img.setImageBitmap(imageids[i]);
					final int a = i;
					img.setOnClickListener(new ImageButton.OnClickListener() {
						@Override
						public void onClick(View v) {

							if (setting.stat == 2) {
								Intent intent = new Intent(Search.this,DrawEditor.class);
								startActivity(intent);
								setting.imagenumber = a;
								Search.this.finish();
							} else if (setting.stat == 1) {
								PostTaskforchoice posttask;
								posttask = new PostTaskforchoice();
								posttask.execute();
								setting.imagenumber = a;
								Search.this.finish();
							}

						}
					});
					num = img.getId() + 1;
					img = (ImageButton) Search.this.findViewById(num);
				}
			}
			mDialog.dismiss();

			Log.v("asd", "asd");
			super.onPostExecute(reString);

		}

		protected void onProgressUpdate(String progress) {

		}

	}

	public Bitmap getBitmapFromURL(String src) {
		try {
			Log.v("asd", src);
			URL url = new URL(src);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.setConnectTimeout(5000);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			if (myBitmap != null)
				return getResizedBitmap(myBitmap, 120, 120);
			else
				return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
		int width = bm.getWidth();
		int height = bm.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		float finalscale = (scaleWidth < scaleHeight) ? scaleWidth
				: scaleHeight;
		if (finalscale > 1) {
			return bm;
		}
		// CREATE A MATRIX FOR THE MANIPULATION
		Matrix matrix = new Matrix();
		// RESIZE THE BIT MAP
		matrix.postScale(finalscale, finalscale);
		// "RECREATE" THE NEW BITMAP
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
				matrix, false);
		return resizedBitmap;
	}

	public class PostTaskforchoice extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			Log.v("adasd", "ewfwefweffewfwewfwef");

			// All your code goes in here

			// If you want to do something on the UI use progress update
			String str = "";

			URL myUrl = null;
			try {
				myUrl = new URL("http://mjimagenetapi.appspot.com/choice?key="
						+ setting.imagelist[setting.imagenumber][0]);

			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

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
				int count = 0;
				imageids = new Bitmap[6];
				int i = 0;

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;

		}

		protected void onPostExecute() {
			super.onPostExecute(null);

		}

		protected void onProgressUpdate() {

		}

	}
}
