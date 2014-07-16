package com.example.funmaker;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.StrictMode;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;


public class ImageSearch extends Activity {

	private String result;
	private String search_name;
	private String apiurl = "http://summerimagenetapi.appspot.com/imagenetapi?";
//	private String APIurl1 = "http://summerimagenetapi.appspot.com/imagenetapi?act=1&name=plant";
//	private String APIurl2 = "http://summerimagenetapi.appspot.com/imagenetapi?act=2&id=n03956922&star=2&number=1";
	private String url1;
	private String url2;
	private String id1, id2, id3, id4, id5;
	private static final String TAG = "Panoramio";
	private String draweditor_bg_url_1;
	private String draweditor_bg_url_2;
	private String draweditor_bg_url_3;
	private String draweditor_bg_url_4;
	private String draweditor_bg_url_5;
	private String draweditor_bg_url_6;
	private String draweditor_bg_url_7;
	private String draweditor_bg_url_8;
	private String draweditor_bg_url_9;
	
	private static int act;
	private int star;
	private int number;
	private static final int IO_BUFFER_SIZE = 4 * 1024;	
	
	EditText  editText;
	Button    btn_search; 
	ImageView iv1, iv2, iv3, iv4, iv5, iv6, iv7, iv8, iv9;
	TextView  tv1, tv2, tv3;
	final TextView tvs[] = {tv1, tv2, tv3};
	final ImageView ivs[] = {iv1, iv2, iv3, iv4, iv5, iv6, iv7, iv8, iv9};
	
	private Handler act_Handler = new Handler();
	private Handler mThreadHandler;
	private HandlerThread mThread;
	
	private ProgressDialog progressDialog = null;
//	private OnClickListener mImageViewClickListener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setTitle("載入背景描繪");
		setContentView(R.layout.activity_image_search);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		final EditText  editText=(EditText)findViewById(R.id.editText1);
		
		btn_search=(Button)findViewById(R.id.btn_getsearch); 
		
		iv1  = (ImageView)findViewById(R.id.imageView1);
		iv1.setOnClickListener(mImageViewClickListener);
		iv2  = (ImageView)findViewById(R.id.imageView2);
		iv2.setOnClickListener(mImageViewClickListener);
		iv3  = (ImageView)findViewById(R.id.imageView3);
		iv3.setOnClickListener(mImageViewClickListener);
		iv4  = (ImageView)findViewById(R.id.imageView4);
		iv4.setOnClickListener(mImageViewClickListener);
		iv5  = (ImageView)findViewById(R.id.imageView5);
		iv5.setOnClickListener(mImageViewClickListener);
		iv6  = (ImageView)findViewById(R.id.imageView6);
		iv6.setOnClickListener(mImageViewClickListener);
		iv7  = (ImageView)findViewById(R.id.imageView7);
		iv7.setOnClickListener(mImageViewClickListener);
		iv8  = (ImageView)findViewById(R.id.imageView8);
		iv8.setOnClickListener(mImageViewClickListener);
		iv9  = (ImageView)findViewById(R.id.imageView9);
		iv9.setOnClickListener(mImageViewClickListener);
//		iv10 = (ImageView)findViewById(R.id.imageView10);
//		iv11 = (ImageView)findViewById(R.id.imageView11);
//		iv12 = (ImageView)findViewById(R.id.imageView12);
//		iv13 = (ImageView)findViewById(R.id.imageView13);
//		iv14 = (ImageView)findViewById(R.id.imageView14);
//		iv15 = (ImageView)findViewById(R.id.imageView15);
		
		tv1 = (TextView)findViewById(R.id.tv1);
		tv1.setOnClickListener(mBtnClickListener);
		tv2 = (TextView)findViewById(R.id.tv2);
		tv2.setOnClickListener(mBtnClickListener);
		tv3 = (TextView)findViewById(R.id.tv3);
		tv3.setOnClickListener(mBtnClickListener);
//		tv4 = (TextView)findViewById(R.id.tv4);
//		tv4.setOnClickListener(mBtnClickListener);
//		tv5 = (TextView)findViewById(R.id.tv5);
//		tv5.setOnClickListener(mBtnClickListener);
		
		
//		mThread = new HandlerThread("name");
//		mThread.start();
//		mThreadHandler = new Handler(mThread.getLooper());
		
		//----------------------------------------------------
		//		解決無法在主執行緒無法使用http
		//----------------------------------------------------	
		StrictMode
	    .setThreadPolicy(new StrictMode.ThreadPolicy.Builder()  
	    .detectDiskReads()  
	    .detectDiskWrites()  
	    .detectNetwork()   // or .detectAll() for all detectable problems  
	    .penaltyLog()  
	    .build());  
		StrictMode
	    .setVmPolicy(new StrictMode.VmPolicy.Builder()  
	    .detectLeakedSqlLiteObjects()  
	    .detectLeakedClosableObjects()  
	    .penaltyLog()  
	    .penaltyDeath()  
	    .build());
		
        editText.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
//				Toast.makeText(ImageSearch.this, String.valueOf(actionId), Toast.LENGTH_LONG).show();  
				return false;
			}
		});
    	
        //搜尋按鈕觸發事件 
        btn_search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				btn_search.setClickable(false);
				
				search_name = editText.getText().toString();
				
				act = 1;
//				for(int i=0;i<tvs.length+1;i++) {
//					tvs[i].setText("");
//				}
//				for(int i=0;i<ivs.length+1;i++) {
//					ivs[i].setImageBitmap(null);
//				}
				
//				mThreadHandler.post(act1);
				tv1.setText("");
				tv2.setText("");
				tv3.setText("");
//				tv4.setText("");
//				tv5.setText("");
				
				iv1.setImageBitmap(null);
				iv2.setImageBitmap(null);
				iv3.setImageBitmap(null);
				iv4.setImageBitmap(null);
				iv5.setImageBitmap(null);
				iv6.setImageBitmap(null);
				iv7.setImageBitmap(null);
				iv8.setImageBitmap(null);
				iv9.setImageBitmap(null);
//				iv10.setImageBitmap(null);
//				iv11.setImageBitmap(null);
//				iv12.setImageBitmap(null);
//				iv13.setImageBitmap(null);
//				iv14.setImageBitmap(null);
//				iv15.setImageBitmap(null);
				
				new Thread(runnable).start();
			}
		});
        
	}

//	private Runnable act1 = new Runnable() {
//		
//		@Override
//		public void run() {
//			// TODO Auto-generated method stub
////			progressDialog = ProgressDialog.show(ImageSearch.this, "請稍等...", "獲取資料中...", true);
////			getContent_act1(search_name);
//			if(isNetworkAvailable()) {
//				url1 = apiurl + "act=" + act + "&name=" + search_name;
//	            try {
//	            	URL url = new URL(url1);
//	            	HttpURLConnection con = (HttpURLConnection) url.openConnection();
//	            	readStream_act1(con.getInputStream());
//	            } catch (Exception e) {
//	            	e.printStackTrace();
//	            }
//			}
//			else {
//				Toast.makeText(ImageSearch.this, "Network is not available!", Toast.LENGTH_SHORT).show();
//			}
//		}
//	};
	
	public boolean isNetworkAvailable() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }
	

	Runnable runnable = new Runnable() {
	    @Override
	    public void run() {
	    	// TODO: HTTP request.
	        Message msg = new Message();
	        Bundle data = new Bundle();
	        data.putString("value",search_name);
	        msg.setData(data);
	        handler.sendMessage(msg);
	    }
	};
	
	Handler handler = new Handler(){
	    @Override
	    public void handleMessage(Message msg) {
	        super.handleMessage(msg);
	        Bundle data = msg.getData();
	        String val = data.getString("value");
	        Log.i("mylog","搜尋 " + val);
//	        progressDialog = ProgressDialog.show(ImageSearch.this, "請稍等...", "獲取資料中...", true);
//	        Log.i("mylog","act1 ProgressDialog show");
	        getContent_act1(val);

//			btn_search.setClickable(true);
	    }
	};

	
	private void readStream_act1(InputStream in) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
//			StringBuffer sb = new StringBuffer("");
//			String NL = System.getProperty("line.separator");
//			URL url;
            for(int i=0;(line = reader.readLine()) != null;i++) {
//            	sb.append(line + NL);
            	switch(i) {
            	case 0:
            		tv1.setText("Popular 1:");
            		id1 = line.substring(0, 9);
//            		tv1.setText("ID = " + line.substring(0, 9) + ", " + line.substring(10) + "張圖片");
            		Log.i("mylog","act1 tv1 set");
            		break;
            	case 1:
//            		url=new URL(line);
            		
//    				//建立一個AsyncTask執行緒進行圖片讀取動作，並帶入圖片連結網址路徑
//    	            new AsyncTask<String, Void, Bitmap>() 
//    	            {
//    	            	@Override
//    	            	protected Bitmap doInBackground(String... params) 
//    	            	{
//    	            		String url = params[0];
//    	            		return getBitmapFromURL(url);
//    	            	}
//    	            	@Override
//    	            	protected void onPostExecute(Bitmap result) 
//    	            	{
//    	            		iv1.setImageBitmap(result);
//    	            		super.onPostExecute(result);
//    	            	}       
//    	            }.execute(line);
            		
//            		iv1.setImageBitmap(loadBitmap(line));
            		loadBitmap2(line, iv1);
//            		iv1.setImageBitmap(GetURLBitmap(url));
            		Log.i("mylog","act1 iv1 set");
            		break;
            	case 2:
//            		url=new URL(line);
//            		iv2.setImageBitmap(loadBitmap(line));
            		loadBitmap2(line, iv2);
//            		iv2.setImageBitmap(GetURLBitmap(url));
            		Log.i("mylog","act1 iv2 set");
            		break;
            	case 3:
//            		url=new URL(line);
//            		iv3.setImageBitmap(loadBitmap(line));
            		loadBitmap2(line, iv3);
//            		iv3.setImageBitmap(GetURLBitmap(url));
            		Log.i("mylog","act1 iv3 set");
            		break;
            	case 4:
            		tv2.setText("Popular 2:");
            		id2 = line.substring(0, 9);
//            		tv2.setText("ID = " + line.substring(0, 9) + ", " + line.substring(10) + "張圖片");
            		Log.i("mylog","act1 tv2 set");
            		break;
            	case 5:
//            		url=new URL(line);
            		loadBitmap2(line, iv4);
//            		iv4.setImageBitmap(loadBitmap(line));
            		Log.i("mylog","act1 iv4 set");
            		break;
            	case 6:
//            		url=new URL(line);
            		loadBitmap2(line, iv5);
//            		iv5.setImageBitmap(loadBitmap(line));
            		Log.i("mylog","act1 iv5 set");
            		break;
            	case 7:
//            		url=new URL(line);
            		loadBitmap2(line, iv6);
//            		iv6.setImageBitmap(loadBitmap(line));
            		Log.i("mylog","act1 iv6 set");
            		break;
            	case 8:
            		tv3.setText("Popular 3:");
            		id3 = line.substring(0, 9);
//            		tv3.setText("ID = " + line.substring(0, 9) + ", " + line.substring(10) + "張圖片");
            		Log.i("mylog","act1 tv3 set");
            		break;
            	case 9:
//            		url=new URL(line);
            		loadBitmap2(line, iv7);
//            		iv7.setImageBitmap(loadBitmap(line));
            		Log.i("mylog","act1 iv7 set");
            		break;
            	case 10:
//            		url=new URL(line);
            		loadBitmap2(line, iv8);
//            		iv8.setImageBitmap(loadBitmap(line));
            		Log.i("mylog","act1 iv8 set");
            		break;
            	case 11:
//            		url=new URL(line);
            		loadBitmap2(line, iv9);
//            		iv9.setImageBitmap(loadBitmap(line));
            		Log.i("mylog","act1 iv9 set");
            		break;
//            	case 12:
//            		tv4.setText("Popular 4:");
//            		id4 = line.substring(0, 9);
////            		tv4.setText("ID = " + line.substring(0, 9) + ", " + line.substring(10) + "張圖片");
//            		Log.i("mylog","act1 tv4 set");
//            		break;
//            	case 13:
////            		url=new URL(line);
////            		loadBitmap2(line, iv10);
//            		iv9.setImageBitmap(loadBitmap(line));
//            		Log.i("mylog","act1 iv10 set");
//            		break;
//            	case 14:
////            		url=new URL(line);
////            		loadBitmap2(line, iv11);
//            		iv11.setImageBitmap(loadBitmap(line));
//            		Log.i("mylog","act1 iv11 set");
//            		break;
//            	case 15:
////            		url=new URL(line);
////            		loadBitmap2(line, iv12);
//            		iv12.setImageBitmap(loadBitmap(line));
//            		Log.i("mylog","act1 iv12 set");
//            		break;
//            	case 16:
//            		tv5.setText("Popular 5:");
//            		id5 = line.substring(0, 9);
////            		tv5.setText("ID = " + line.substring(0, 9) + ", " + line.substring(10) + "張圖片");
//            		Log.i("mylog","act1 tv5 set");
//            		break;
//            	case 17:
////            		url=new URL(line);
////            		loadBitmap2(line, iv13);
//            		iv13.setImageBitmap(loadBitmap(line));
//            		Log.i("mylog","act1 iv13 set");
//            		break;
//            	case 18:
////            		url=new URL(line);
////            		loadBitmap2(line, iv14);
//            		iv14.setImageBitmap(loadBitmap(line));
//            		Log.i("mylog","act1 iv14 set");
//            		break;
//            	case 19:
////            		url=new URL(line);
////            		loadBitmap2(line, iv15);
//            		iv15.setImageBitmap(loadBitmap(line));
//            		Log.i("mylog","act1 iv15 set");
//            		break;
            	}
            	
            }
            if (reader != null) {
//				String page = sb.toString();
//				tv1.setText("HTTP response is: "+ '\n' +page);
            	Log.i("mylog","act1 done");
            	Toast.makeText(this, "載入完成", Toast.LENGTH_LONG).show();
//				progressDialog.dismiss();
//				Log.i("mylog","act1 progressDialog dismiss");
            }
        } catch (IOException e) {
        	e.printStackTrace();
        } finally {
            if (reader != null) {
            	try {
            		reader.close();
            	} catch (IOException e) {
            		e.printStackTrace();
            	}
            }
        }
	}
	
	private void readStream_act2(InputStream in) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
//			StringBuffer sb = new StringBuffer("");
//			String NL = System.getProperty("line.separator");
//			URL url;
            for(int i=0;(line = reader.readLine()) != null;i++) {
//            	sb.append(line + NL);
            	switch(i) {
            	case 0:
//            		url=new URL(line);
            		loadBitmap2(line, iv1);
            		draweditor_bg_url_1 = line;
            		Log.i("mylog","iv1str = "+line);
//            		iv1bm = loadBitmap(line);
//            		iv1.setImageBitmap(iv1bm);
            		Log.i("mylog","act2 iv1 set");
            		break;
            	case 1:
//            		url=new URL(line);
            		loadBitmap2(line, iv2);
            		draweditor_bg_url_2 = line;
//            		iv2.setImageBitmap(loadBitmap(line));
            		Log.i("mylog","act2 iv2 set");
            		break;
            	case 2:
//            		url=new URL(line);
            		loadBitmap2(line, iv3);
            		draweditor_bg_url_3 = line;
//            		iv3.setImageBitmap(loadBitmap(line));
            		Log.i("mylog","act2 iv3 set");
            		break;
            	case 3:
//            		url=new URL(line);
            		loadBitmap2(line, iv4);
            		draweditor_bg_url_4 = line;
//            		iv4.setImageBitmap(loadBitmap(line));
            		Log.i("mylog","avt2 iv4 set");
            		break;
            	case 4:
//            		url=new URL(line);
            		loadBitmap2(line, iv5);
            		draweditor_bg_url_5 = line;
//            		iv5.setImageBitmap(loadBitmap(line));
            		Log.i("mylog","act2 iv5 set");
            		break;
            	case 5:
//            		url=new URL(line);
            		loadBitmap2(line, iv6);
            		draweditor_bg_url_6 = line;
//            		iv6.setImageBitmap(loadBitmap(line));
            		Log.i("mylog","act2 iv6 set");
            		break;
            	case 6:
//            		url=new URL(line);
            		loadBitmap2(line, iv7);
            		draweditor_bg_url_7 = line;
//            		iv7.setImageBitmap(loadBitmap(line));
            		Log.i("mylog","act2 iv7 set");
            		break;
            	case 7:
//            		url=new URL(line);
            		loadBitmap2(line, iv8);
            		draweditor_bg_url_8 = line;
//            		iv8.setImageBitmap(loadBitmap(line));
            		Log.i("mylog","act2 iv8 set");
            		break;
            	case 8:
//            		url=new URL(line);
            		loadBitmap2(line, iv9);
            		draweditor_bg_url_9 = line;
//            		iv9.setImageBitmap(loadBitmap(line));
            		Log.i("mylog","act2 iv9 set");
            		break;
//            	case 9:
////            		url=new URL(line);
////            		loadBitmap2(line, iv10);
//            		iv10.setImageBitmap(loadBitmap(line));
//            		Log.i("mylog","act2 iv10 set");
//            		iv11.setImageBitmap(null);
//            		iv12.setImageBitmap(null);
//            		iv13.setImageBitmap(null);
//            		iv14.setImageBitmap(null);
//            		iv15.setImageBitmap(null);
//            		break;
            	}
            }
            if (reader != null) {
//				String page = sb.toString();
//				tv1.setText("http reaponse is: "+ '\n' +page);
            	Log.i("mylog","act2 done");
//            	if(progressDialog.isShowing()) {
//            		progressDialog.dismiss();
//            		Log.i("mylog","act2 progressDialog dismiss");
//            	}
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
              try {
                reader.close();
              } catch (IOException e) {
                e.printStackTrace();
              }
            }
        }
    }
	
	public static Bitmap GetURLBitmap(URL url)
	{
		try
		{
			URLConnection conn = url.openConnection();
			conn.connect();
			InputStream isCover = conn.getInputStream();
			Bitmap bmpCover = BitmapFactory.decodeStream(isCover);
			isCover.close();
			return bmpCover;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public String getContent_act1(String name){
		if(isNetworkAvailable()) {
			try {
				url1 = apiurl + "act=" + act + "&name=" + URLEncoder.encode(name, "utf-8");
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
//			method 1: HttpURLConnection
            try {
            	URL url = new URL(url1);
                  HttpURLConnection con = (HttpURLConnection) url.openConnection();
                  readStream_act1(con.getInputStream());
                } catch (Exception e) {
                	e.printStackTrace();
                }
            
//			method 2: HttpClient
//			GetServerMessage message = new GetServerMessage();
//			String msg = message.stringQuery(APIurl2);
//			TextView tv1 = (TextView)findViewById(R.id.tv1);
//			tv1.setText("Server message is "+msg);
//			Toast.makeText(ImageSearch.this, result, Toast.LENGTH_SHORT).show();
		}
		else {
			Toast.makeText(ImageSearch.this, "Network is not available!", Toast.LENGTH_SHORT).show();
		}
		return result;
	}
	
	public String getContent_act2(String id){
		if(isNetworkAvailable()) {
			url1 = apiurl + "act=" + act + "&id=" + id + "&star=2&number=10";
			// method 1: HttpURLConnection
            try {
                  URL url = new URL(url1);
                  HttpURLConnection con = (HttpURLConnection) url.openConnection();
                  readStream_act2(con.getInputStream());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            
			
			// method 2: HttpClient
//			GetServerMessage message = new GetServerMessage();
//            String msg = message.stringQuery(APIurl2);
//            TextView tv1 = (TextView)findViewById(R.id.tv1);
//            tv1.setText("Server message is "+msg);
//            Toast.makeText(ImageSearch.this, result, Toast.LENGTH_SHORT).show();
		}
		else {
			Toast.makeText(ImageSearch.this, "Network is not available!", Toast.LENGTH_SHORT).show();
		}
		return result;
	}

	private OnClickListener mBtnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int nBtnID = v.getId();
			if(nBtnID == tv1.getId()){
				Log.i("mylog","請求結果-->tv1 clicked");
				Log.i("mylog","act2 start");
//				progressDialog = ProgressDialog.show(ImageSearch.this, "請稍等...", "獲取資料中...", true);
//				Log.i("mylog","act2 ProgressDialog show");
//				String tmps = (String) tv1.getText();
//				Log.i("mylog","act2 gettext");
				
				tv1.setText("");
//				tv1.setText(tmps.substring(5, 14));
				Log.i("mylog","act2 tv1 settext");
				tv2.setText("");
				Log.i("mylog","act2 tv2 settext");
				tv3.setText("");
				Log.i("mylog","act2 tv3 settext");
//				tv4.setText("");
//				Log.i("mylog","act2 tv4 settext");
//				tv5.setText("");
//				Log.i("mylog","act2 tv5 settext");
				
				act = 2;
				Log.i("mylog","set act = 2");
				
				tv1.setText("");
				tv2.setText("");
				tv3.setText("");
//				tv4.setText("");
//				tv5.setText("");
				
				iv1.setImageBitmap(null);
				iv2.setImageBitmap(null);
				iv3.setImageBitmap(null);
				iv4.setImageBitmap(null);
				iv5.setImageBitmap(null);
				iv6.setImageBitmap(null);
				iv7.setImageBitmap(null);
				iv8.setImageBitmap(null);
				iv9.setImageBitmap(null);
//				iv10.setImageBitmap(null);
//				iv11.setImageBitmap(null);
//				iv12.setImageBitmap(null);
//				iv13.setImageBitmap(null);
//				iv14.setImageBitmap(null);
//				iv15.setImageBitmap(null);
				
				getContent_act2(id1);
//				getContent_act2((String) tv1.getText());
			}
			if(nBtnID == tv2.getId()){
				Log.i("mylog","請求結果-->tv2 clicked");
				Log.i("mylog","act2 start");
//				progressDialog = ProgressDialog.show(ImageSearch.this, "請稍等...", "獲取資料中...", true);
//				Log.i("mylog","act2 ProgressDialog show");
//				String tmps = (String) tv2.getText();
//				Log.i("mylog","act2 gettext");
				
				tv1.setText("");
//				tv1.setText(tmps.substring(5, 14));
				Log.i("mylog","act2 tv1 settext");
				tv2.setText("");
				Log.i("mylog","act2 tv2 settext");
				tv3.setText("");
				Log.i("mylog","act2 tv3 settext");
//				tv4.setText("");
//				Log.i("mylog","act2 tv4 settext");
//				tv5.setText("");
//				Log.i("mylog","act2 tv5 settext");
				
				act = 2;
				Log.i("mylog","set act = 2");
				
				tv1.setText("");
				tv2.setText("");
				tv3.setText("");
//				tv4.setText("");
//				tv5.setText("");
				
				iv1.setImageBitmap(null);
				iv2.setImageBitmap(null);
				iv3.setImageBitmap(null);
				iv4.setImageBitmap(null);
				iv5.setImageBitmap(null);
				iv6.setImageBitmap(null);
				iv7.setImageBitmap(null);
				iv8.setImageBitmap(null);
				iv9.setImageBitmap(null);
//				iv10.setImageBitmap(null);
//				iv11.setImageBitmap(null);
//				iv12.setImageBitmap(null);
//				iv13.setImageBitmap(null);
//				iv14.setImageBitmap(null);
//				iv15.setImageBitmap(null);
				
				getContent_act2(id2);
//				getContent_act2((String) tv1.getText());
			}
			if(nBtnID == tv3.getId()){
				Log.i("mylog","請求結果-->tv3 clicked");
				Log.i("mylog","act2 start");
//				progressDialog = ProgressDialog.show(ImageSearch.this, "請稍等...", "獲取資料中...", true);
//				Log.i("mylog","act2 ProgressDialog show");
//				String tmps = (String) tv3.getText();
//				Log.i("mylog","act2 gettext");
				
				tv1.setText("");
//				tv1.setText(tmps.substring(5, 14));
				Log.i("mylog","act2 tv1 settext");
				tv2.setText("");
				Log.i("mylog","act2 tv2 settext");
				tv3.setText("");
				Log.i("mylog","act2 tv3 settext");
//				tv4.setText("");
//				Log.i("mylog","act2 tv4 settext");
//				tv5.setText("");
//				Log.i("mylog","act2 tv5 settext");
				
				act = 2;
				Log.i("mylog","set act = 2");
				tv1.setText("");
				tv2.setText("");
				tv3.setText("");
//				tv4.setText("");
//				tv5.setText("");
				
				iv1.setImageBitmap(null);
				iv2.setImageBitmap(null);
				iv3.setImageBitmap(null);
				iv4.setImageBitmap(null);
				iv5.setImageBitmap(null);
				iv6.setImageBitmap(null);
				iv7.setImageBitmap(null);
				iv8.setImageBitmap(null);
				iv9.setImageBitmap(null);
//				iv10.setImageBitmap(null);
//				iv11.setImageBitmap(null);
//				iv12.setImageBitmap(null);
//				iv13.setImageBitmap(null);
//				iv14.setImageBitmap(null);
//				iv15.setImageBitmap(null);
				
				getContent_act2(id3);
//				getContent_act2((String) tv1.getText());
			}
//			if(nBtnID == tv4.getId()){
//				Log.i("mylog","請求結果-->tv4 clicked");
//				Log.i("mylog","act2 start");
////				progressDialog = ProgressDialog.show(ImageSearch.this, "請稍等...", "獲取資料中...", true);
////				Log.i("mylog","act2 ProgressDialog show");
////				String tmps = (String) tv4.getText();
////				Log.i("mylog","act2 gettext");
//				
//				tv1.setText("");
////				tv1.setText(tmps.substring(5, 14));
//				Log.i("mylog","act2 tv1 settext");
//				tv2.setText("");
//				Log.i("mylog","act2 tv2 settext");
//				tv3.setText("");
//				Log.i("mylog","act2 tv3 settext");
//				tv4.setText("");
//				Log.i("mylog","act2 tv4 settext");
//				tv5.setText("");
//				Log.i("mylog","act2 tv5 settext");
//				
//				act = 2;
//				Log.i("mylog","set act = 2");
//				getContent_act2(id4);
////				getContent_act2((String) tv1.getText());
//			}
//			if(nBtnID == tv5.getId()){
//				Log.i("mylog","請求結果-->tv5 clicked");
//				Log.i("mylog","act2 start");
////				progressDialog = ProgressDialog.show(ImageSearch.this, "請稍等...", "獲取資料中...", true);
////				Log.i("mylog","act2 ProgressDialog show");
////				String tmps = (String) tv5.getText();
////				Log.i("mylog","act2 gettext");
//				
//				tv1.setText("");
////				tv1.setText(tmps.substring(5, 14));
//				Log.i("mylog","act2 tv1 settext");
//				tv2.setText("");
//				Log.i("mylog","act2 tv2 settext");
//				tv3.setText("");
//				Log.i("mylog","act2 tv3 settext");
//				tv4.setText("");
//				Log.i("mylog","act2 tv4 settext");
//				tv5.setText("");
//				Log.i("mylog","act2 tv5 settext");
//				
//				act = 2;
//				Log.i("mylog","set act = 2");
//				getContent_act2(id5);
////				getContent_act2((String) tv1.getText());
//			}
		}
	};
	
	private OnClickListener mImageViewClickListener = new OnClickListener() {
    	
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int nIvID = v.getId();
			
			if(nIvID == iv1.getId() && act == 2) {
				
				Intent intent=new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("bgurl", draweditor_bg_url_1);
				intent.putExtra("bundle", bundle);
				setResult(RESULT_OK, intent);
				ImageSearch.this.finish();
				Log.i("mylog","iv1 clicked, draweditor bg setting");
			}
			
			if(nIvID == iv2.getId() && act == 2) {
				
				Intent intent=new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("bgurl", draweditor_bg_url_2);
				intent.putExtra("bundle", bundle);
				setResult(RESULT_OK, intent);
				ImageSearch.this.finish();
				Log.i("mylog","iv2 clicked, draweditor bg setting");
			}
			
			if(nIvID == iv3.getId() && act == 2) {
				
				Intent intent=new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("bgurl", draweditor_bg_url_3);
				intent.putExtra("bundle", bundle);
				setResult(RESULT_OK, intent);
				ImageSearch.this.finish();
				Log.i("mylog","iv3 clicked, draweditor bg setting");
			}
			
			if(nIvID == iv4.getId() && act == 2) {
				
				Intent intent=new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("bgurl", draweditor_bg_url_4);
				intent.putExtra("bundle", bundle);
				setResult(RESULT_OK, intent);
				ImageSearch.this.finish();
				Log.i("mylog","iv4 clicked, draweditor bg setting");
			}
			
			if(nIvID == iv5.getId() && act == 2) {
				
				Intent intent=new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("bgurl", draweditor_bg_url_5);
				intent.putExtra("bundle", bundle);
				setResult(RESULT_OK, intent);
				ImageSearch.this.finish();
				Log.i("mylog","iv5 clicked, draweditor bg setting");
			}
			
			if(nIvID == iv6.getId() && act == 2) {
				
				Intent intent=new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("bgurl", draweditor_bg_url_6);
				intent.putExtra("bundle", bundle);
				setResult(RESULT_OK, intent);
				ImageSearch.this.finish();
				Log.i("mylog","iv6 clicked, draweditor bg setting");
			}
			
			if(nIvID == iv7.getId() && act == 2) {
				
				Intent intent=new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("bgurl", draweditor_bg_url_7);
				intent.putExtra("bundle", bundle);
				setResult(RESULT_OK, intent);
				ImageSearch.this.finish();
				Log.i("mylog","iv7 clicked, draweditor bg setting");
			}			
			
			if(nIvID == iv8.getId() && act == 2) {
				
				Intent intent=new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("bgurl", draweditor_bg_url_8);
				intent.putExtra("bundle", bundle);
				setResult(RESULT_OK, intent);
				ImageSearch.this.finish();
				Log.i("mylog","iv8 clicked, draweditor bg setting");
			}			
			
			if(nIvID == iv9.getId() && act == 2) {
				
				Intent intent=new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("bgurl", draweditor_bg_url_9);
				intent.putExtra("bundle", bundle);
				setResult(RESULT_OK, intent);
				ImageSearch.this.finish();
				Log.i("mylog","iv9 clicked, draweditor bg setting");
			}
			
		}
		
	};
	
	//讀取網路圖片，型態為Bitmap
	private static Bitmap getBitmapFromURL(String imageUrl) 
	{
		try {
			URL url = new URL(imageUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap bitmap = BitmapFactory.decodeStream(input);
			return bitmap;
		} 
		catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
//	抓圖片2
	public static void loadBitmap2(String url, ImageView iv) {
		try {
			InputStream is = (InputStream)new URL(url).getContent();
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = false;
			if(act == 1) {
				options.inSampleSize = 1;	
			}
			else {
				options.inSampleSize = 2;	
			}
			
			Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
//			Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(url).getContent());
			iv.setImageBitmap(bitmap); 
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
//	抓圖片1
	public static Bitmap loadBitmap(String url) {
	    Bitmap bitmap = null;
	    InputStream in = null;
	    BufferedOutputStream out = null;

	    try {
	        in = new BufferedInputStream(new URL(url).openStream(), IO_BUFFER_SIZE);

	        final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
	        out = new BufferedOutputStream(dataStream, IO_BUFFER_SIZE);
	        copy(in, out);
	        out.flush();

	        final byte[] data = dataStream.toByteArray();
	        BitmapFactory.Options options = new BitmapFactory.Options();
	        //options.inSampleSize = 1;

	        bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,options);
	    } catch (IOException e) {
	        Log.e(TAG, "Could not load Bitmap from: " + url);
	    } finally {
	        closeStream(in);
	        closeStream(out);
	    }
	    return bitmap;
	}
	
	/**
     * Closes the specified stream.
     * 
     * @param stream The stream to close.
     */
    private static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                android.util.Log.e(TAG, "Could not close stream", e);
            }
        }
    }
	
    /**
     * Copy the content of the input stream into the output stream, using a
     * temporary byte array buffer whose size is defined by
     * {@link #IO_BUFFER_SIZE}.
     * 
     * @param in The input stream to copy from.
     * @param out The output stream to copy to.
     * @throws IOException If any error occurs during the copy.
     */
    private static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] b = new byte[IO_BUFFER_SIZE];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }
    
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		// Release View resources
//		iv1 = null;
//		iv2 = null;
//		iv3 = null;
//		iv4 = null;
//		iv5 = null;
//		iv6 = null;
//		iv7 = null;
//		iv8 = null;
//		iv9 = null;
//		tv1 = null;
//		tv2 = null;
//		tv3 = null;
//		iv1bm = null;
//		iv2bm = null;
//		iv3bm = null;
//		iv4bm = null;
//		iv5bm = null;
//		iv6bm = null;
//		iv7bm = null;
//		iv8bm = null;
//		iv9bm = null;
////		setContentView(null);
//	}
    
    
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.image_search, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item)	{
		super.onOptionsItemSelected(item);
		
		switch(item.getItemId()) {		
		//================================================
		// File Menu
		//================================================
		// Respond to the action bar's Up/Home button
		    case android.R.id.home:
		    {
		        finish();
		    }
		}
		return true;
	}
}
