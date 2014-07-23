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

import com.example.funmaker.Constants.Extra;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class Showmore extends Activity {
	ProgressDialog mDialog;

	TextView tv;
	Bitmap[] imageids;
	int sum = 1;
	static int i;
	int count = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_search);

		Button btn = (Button) findViewById(R.id.button1);

		btn.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				mDialog = new ProgressDialog(Showmore.this);
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



	public class PostTask2 extends AsyncTask<Void, String, String> {
		public String surl;

		public PostTask2(String string) {
			surl = string;
			// TODO Auto-generated constructor stub
		}

		@Override
		protected String doInBackground(Void... params) {

			// All your code goes in here

			// If you want to do something on the UI use progress update
			String str = "", str1 = "";

			URL myUrl = null;
			try {
				myUrl = new URL("http://mjimagenetapi.appspot.com/showmore?name="
						+ URLEncoder.encode(surl, "utf-8") + "&asc="
						+ (int) (Math.random() * (65525) + 1));
				setting.search = URLEncoder.encode(surl, "utf-8");
				Log.v("searchurl", myUrl.toString());
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

				imageids = new Bitmap[6];
				int i = 0;
				count = 0;
				if ((str = reader.readLine()) != null) {
					if (Integer.parseInt(str) == 1) {
						setting.stat = 1;
						while ((str = reader.readLine()) != null && count < 30) {
							setting.imagelist[count][0] = str;
							if ((str = reader.readLine()) != null)
								setting.imagelist[count][1] = str;
							Log.v("asdasd", setting.imagelist[count][0] + "");
							// imageids[count] =
							// getBitmapFromURL(setting.imagelist[count][1]);
							count++;

						}
					} else if (Integer.parseInt(str) == 2) {
						setting.stat = 2;
						while ((str = reader.readLine()) != null && count < 30) {
							str = str.substring(2);
							str1 = str.substring(str.indexOf(",") + 1);
							setting.imagelist[count][0] = str.substring(0,
									str.indexOf(",") + str1.indexOf(",") + 2);
							Log.v("url3", setting.imagelist[count][0]);
							setting.imagelist[count][1] = str.substring(
									str.indexOf(",") + str1.indexOf(",") + 2,
									str.length());
							// imageids[count] =
							// getBitmapFromURL(setting.imagelist[count][1]);
							count++;

						}
					}
					LOG.OUT(setting.stat);
				}
				conn.disconnect();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return str;

		}

		protected void onPostExecute(String reString) {

			if (count > 0) {
				Constants.IMAGES = new String[count];
				for (int i = 0; i < count; i++) {

					Constants.IMAGES[i] = setting.imagelist[i][1];
					Log.v("asdasd", Constants.IMAGES[i] + "");

				}
				mDialog.dismiss();
				Log.v("teststeststests1", Constants.IMAGES[i] + "");
				DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
						.cacheInMemory(false)
						// 1.8.6包使用時候，括號裏面傳入參數true
						.cacheOnDisc()
						// 1.8.6包使用時候，括號裏面傳入參數true
						.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
						.bitmapConfig(Bitmap.Config.RGB_565).build();
				Log.v("teststeststests2", Constants.IMAGES[i] + "");
				ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
						getApplicationContext())
						.defaultDisplayImageOptions(defaultOptions)
						.threadPriority(Thread.NORM_PRIORITY - 2)
						.denyCacheImageMultipleSizesInMemory()
						.discCacheFileNameGenerator(new Md5FileNameGenerator())
						.tasksProcessingOrder(QueueProcessingType.LIFO)
						
						.writeDebugLogs() // Remove for release app

						.build();
				Log.v("teststeststests3", Constants.IMAGES[i] + "");
				Log.v("teststeststests4", Constants.IMAGES[i] + "");
				ImageLoader.getInstance().init(config);
				Log.v("teststeststests5", Constants.IMAGES[i] + "");
				ImageLoader imageLoader = ImageLoader.getInstance();
				Log.v("teststeststests6", Constants.IMAGES[i] + "");
				imageLoader.clearDiscCache();
				Log.v("teststeststests7", Constants.IMAGES[i] + "");
				imageLoader.clearMemoryCache();
				Log.v("teststeststests8", Constants.IMAGES[i] + "");
				Intent intent = new Intent(Showmore.this, ImageGridActivity.class);
				Log.v("teststeststests9", Constants.IMAGES[i] + "");
				intent.putExtra(Extra.IMAGES, Constants.IMAGES);
				Log.v("teststeststests10", Constants.IMAGES[i] + "");
				startActivity(intent);
				Log.v("teststeststests11", Constants.IMAGES[i] + "");
				Showmore.this.finish();
			} else {
				AlertDialog.Builder dialog = new AlertDialog.Builder(Showmore.this);
				dialog.setMessage("找不到該物件，請重新搜尋");
				dialog.setIcon(android.R.drawable.ic_dialog_alert);
				dialog.setCancelable(false);
				dialog.setPositiveButton("確定",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// 按下PositiveButton要做的事

							}
						});

				dialog.show();
				mDialog.dismiss();

			}
			Log.v("asd", "done");
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
