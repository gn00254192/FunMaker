/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.example.funmaker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public class ImageGridActivity extends AbsListViewBaseActivity {

	String[] imageUrls;

	DisplayImageOptions options;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_image_grid);

		imageUrls = Constants.IMAGES;

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_stub)
				.showImageForEmptyUri(R.drawable.ic_empty)
				.showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
				.cacheOnDisc(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		listView = (GridView) findViewById(R.id.gridview);
		((GridView) listView).setAdapter(new ImageAdapter());
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.v("asd", "" + position);
				startImagePagerActivity(position);
			}
		});
	}

	private void startImagePagerActivity(int position) {
		
		if (setting.stat == 2) {
			Intent intent = new Intent(ImageGridActivity.this, DrawEditor.class);
			startActivity(intent);
			setting.imagenumber = position;
			ImageGridActivity.this.finish();
		} else if (setting.stat == 1) {
			PostTaskforchoice posttask;
			posttask = new PostTaskforchoice();
			posttask.execute();
			setting.imagenumber = position;
			ImageGridActivity.this.finish();
		}
		

	}

	public class ImageAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return imageUrls.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			View view = convertView;
			if (view == null) {
				view = getLayoutInflater().inflate(R.layout.item_grid_image,
						parent, false);
				holder = new ViewHolder();
				assert view != null;
				holder.imageView = (ImageView) view.findViewById(R.id.image);
				holder.imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

				holder.progressBar = (ProgressBar) view
						.findViewById(R.id.progress);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			imageLoader.displayImage(imageUrls[position], holder.imageView,
					options, new SimpleImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {
							holder.progressBar.setProgress(0);
							holder.progressBar.setVisibility(View.VISIBLE);
						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							holder.progressBar.setVisibility(View.GONE);
						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							holder.progressBar.setVisibility(View.GONE);
						}
					}, new ImageLoadingProgressListener() {
						@Override
						public void onProgressUpdate(String imageUri,
								View view, int current, int total) {
							holder.progressBar.setProgress(Math.round(100.0f
									* current / total));
						}
					});

			return view;
		}

		class ViewHolder {
			ImageView imageView;
			ProgressBar progressBar;
		}
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