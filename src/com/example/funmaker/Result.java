package com.example.funmaker;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.R.color;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;
import android.widget.TextView;
import android.widget.Toast;

public class Result extends Activity {
	private String[] imageIDs;
	private Bitmap[] imageids ;
	private TextView tv;
	private final String DEFAULT_SAVE_PATH = "SPenSDK";
	private String  mTempAMSFolderPath = null;
	private ProgressDialog progressDialog = null;
	
	class GoodTask extends AsyncTask<Void, Integer, String> {
		// <�ǤJ�Ѽ�, �B�z����s�����Ѽ�, �B�z��ǥX�Ѽ�>
		@Override
		protected String doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			if(!isCancelled()) {
				Log.v("cancel", "isCancelled() = " + isCancelled());
			}
			String str;
			Log.v("search image",
					"http://summerimagenetapi.appspot.com/imagenetapi?act=2&star=1&number=10&id="
							+ setting.node[setting.select]);
			URL myUrl = null;
			try {
				myUrl = new URL(
						"http://summerimagenetapi.appspot.com/imagenetapi?act=2&star=1&number=10&id="
								+ setting.node[setting.select]);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// ���o URLConnection
			HttpURLConnection conn;
		
			try {
				conn = (HttpURLConnection) myUrl.openConnection();

				conn.setDoInput(true); // �]�w���i�q���A��Ū�����
				conn.setDoOutput(false); // �]�w���i�g�J��Ʀܦ��A��
				conn.setRequestMethod("GET"); // �]�w�ШD�覡�� GET
				// �H�U�O�]�w MIME ���Y���� Content-type
				conn.setRequestProperty("Content-type",
						"application/x-www-form-urlencoded");
				conn.connect(); // �}�l�s��
				// �z�L URLConnection �� getOutputStream() ���� OutputStream,
				// �ëإߥHUTF-8
				// ���s�X�� OutputStreamWriter

				// �Q�� URLConnection �� getInputStream() ���o InputStream
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(conn.getInputStream(), "UTF-8"));
				imageIDs = new String[10];
				for (int i = 0; i < 10; i++) {
					Log.v("search img", "geting image" + i + " url.");

					str = reader.readLine();
					if (str != null)
						imageIDs[i] = str;
					else
						break;

				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		

			imageids = new Bitmap[imageIDs.length];
			// �A�I�����B�z���Ӯɤu�@
			int j=0;
			for (int i = 0; i < imageIDs.length; i++)
				if (imageIDs[i] != null)
				{
					Log.v("search img", "geting image" + i + " bitmap.");
					imageids[j] = getBitmapFromURL(imageIDs[i]);
					if(imageids[j]!=null)
						j++;
				}
			setting.select=j;
			return null; // �|�ǵ� onPostExecute(String result) �� String result
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = ProgressDialog.show(Result.this, "�еy��...", "���J�Ϥ���...", true);
//			progressDialog = new ProgressDialog(Result.this);
//			progressDialog.setTitle("�еy��...");
//			progressDialog.setMessage("���J�Ϥ���...");
//			progressDialog.setCancelable(true);
//			progressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "����", new DialogInterface.OnClickListener() {
//			    @Override
//			    public void onClick(DialogInterface dialog, int which) {
//			        dialog.dismiss();
//			        finish();
//			    }
//			});
//			progressDialog.show();

			// �I���u�@�B�z"�e"�ݧ@����
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);

			// �I���u�@�B�z"��"��s����
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			Gallery gallery = (Gallery) findViewById(R.id.gallery1);
			ImageAdapter imageAdapter = new ImageAdapter(Result.this);
			// �]�w�Ϥ��ӷ�

			// �]�w�Ϥ�����m
			imageAdapter.setmImageIds(imageids);
			// �Ϥ�����
			imageAdapter.setHeight(300);
			// �Ϥ��e��
			imageAdapter.setWidth(350);
			gallery.setAdapter(imageAdapter);
			Log.v("search img", "Put image to gallery.");
			gallery.setUnselectedAlpha(0.5f);// ����쪺�Ϥ�����t
			gallery.setSpacing(30);// �Ϥ��P�Ϥ��������j
			gallery.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView parent, View view,
						int position, long id) {
					// create basic save/road file path
					File sdcard_path = Environment.getExternalStorageDirectory();
					File default_path =  new File(sdcard_path, DEFAULT_SAVE_PATH);
					if(!default_path.exists()){
						if(!default_path.mkdirs()){
							Log.e("TAG", "Default Save Path Creation Error");
							return ;
						}
					}
					
					mTempAMSFolderPath = default_path.getAbsolutePath();
					
					String strPath = new String(mTempAMSFolderPath+"/RESOURCE/");
		            File fPath = new File(strPath); 
		            if (!fPath.exists()) { 
		                fPath.mkdir(); 
		            }
		            setting.imagenet_selected_image_path = strPath+"imagenet_selected_image.png";
					File myDrawFile = new File(strPath+"imagenet_selected_image.png");
					try {
						BufferedOutputStream bos = new BufferedOutputStream
						(new FileOutputStream(myDrawFile));
						imageids[position].compress(Bitmap.CompressFormat.PNG, 90, bos);
						bos.flush();
						bos.close();
					} catch (Exception e) {
						e.printStackTrace();
						Log.d(null, "Save file error!");
					}
					Log.d(null, "Save file ok! Path = "+setting.imagenet_selected_image_path);
					Log.v("search img", "Selected image" + position + ".");
//					Toast.makeText(Result.this, "�z�諸�O��" + position + "�i��",
//							Toast.LENGTH_LONG).show();
					setResult(RESULT_OK);
					finish();
				}
			});
			// �I���u�@�B�z��"��"�ݧ@����
			progressDialog.dismiss();

		}

		protected void onCancelled(Boolean result) {
			// TODO Auto-generated method stub
			super.onCancelled();
			Log.v("cancel", "isCancelled() = " + isCancelled());
			// �I���u�@�Q"����"�ɧ@���ơA���ɤ��@ onPostExecute(String result)
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("�j�M���G");
		//���ù����
		this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);  
		//����ܼ��D�C
	    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_result);
		Log.v("search img", "Created.");
		tv = (TextView) findViewById(R.id.textView_result);
		tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tv.setBackgroundColor(color.holo_blue_bright);
				finish();
			}
		});
		GoodTask goodTask = new GoodTask();
		goodTask.execute();

	}

	@Override  
    protected void onDestroy() {  
        super.onDestroy();
        Log.v("Destory", "Result onDestroy");
//        Log.v("Destory", "imageids length = " + imageids.length);  
//        for(int i=0;i<imageIDs.length;i++) {
//        	if(imageids[i] != null) 
//        		imageids[i].recycle();
//        }
        System.gc();
    }  
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.result, menu);
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
