package com.example.funmaker;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FacebookShare extends Activity {
	//private static final List<String> PERMISSIONS2 = Arrays.asList( "read_stream","user_photos");
	private static final List<String> PERMISSIONS = Arrays.asList("publish_actions","publish_stream");
	String CheckinsNum;
	EditText message ;
	byte[] Imagedata;
	private ProgressDialog myDialog;
	private ProgressDialog loginProgress;
	boolean iflogin= false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_facebook_share);
		setTitle("�|�溩�e");
		
	    message = (EditText)findViewById(R.id.editText1);
	    message.getText().toString();
	    
	    //ImageCut();
	    /****************************FB�n�J*******************************/
	    // start Facebook Login
	    Session.openActiveSession(this, true, new Session.StatusCallback() {

	      // callback when session changes state
	      @Override
	      public void call(Session session, SessionState state, Exception exception) {
	        if (session.isOpened()) {

			    if (session != null){
			        // Check for publish permissions 
			    	
			        List<String> permissions = session.getPermissions();
			        if (!isSubsetOf(PERMISSIONS, permissions)) {
			         Session.NewPermissionsRequest newPermissionsRequest = 
			         new Session.NewPermissionsRequest(FacebookShare.this, PERMISSIONS);
			        session.requestNewPublishPermissions(newPermissionsRequest);
			        }
			   } 
			    
			    
			    loginProgress = new ProgressDialog(FacebookShare.this);
			    loginProgress.setMessage("facebook�n�J��......");
			    loginProgress.setButton("����", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						loginProgress.dismiss();
						finish();
					}
	 
				});

			    loginProgress.show();
	          // make request to the /me API
	          Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
              
	        	  
	            // callback after Graph API response with user object
	            @Override
	            public void onCompleted(GraphUser user, Response response) {
	              if (user != null) {
	            	 loginProgress.dismiss();
	            	iflogin = true;
	                TextView welcome = (TextView) findViewById(R.id.textView1);
	                welcome.setText(user.getName()+"�Q���Ǥ���?");
	                
	                ImageView iv = (ImageView)findViewById(R.id.imageViewObj);
	                iv.setImageBitmap(setting.upload);
                   
	                
	                
	                
	              }
	            }
	          });
	        }
	      }
	    });
	    /************************FB�n�J**********************************/
	    
	  }    //end of onCreate
		
		 public void oncheckins(View v)
	    {	
			if(iflogin)
	    	checkinUsePlaceId();
			else
			Toast.makeText(this, "facebook�|���n�J", Toast.LENGTH_SHORT).show();	
		    
	    }		
		

		 
		 @Override
		 public void onActivityResult(int requestCode, int resultCode, Intent data) {
		      super.onActivityResult(requestCode, resultCode, data);
		      Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
		    //���o�ۤ����^����ť��
		      if(requestCode==1)
		      {
			        // ������ɮ�
			        if ( resultCode == RESULT_OK )
			        {
			            // ���o�ɮת� Uri
			            Uri uri = data.getData();
			            if( uri != null )
			       
			            	
			            {  Log.v("DIV",uri.toString());
//			            ImageView iv = (ImageView)this.findViewById(R.id.imageViewObj);
//	                    iv.setImageURI( uri );
	                    TextView imageuri = (TextView) findViewById(R.id.textView2);
	                    imageuri.setSelected(true);

	                    //uri �൴����|
	                    getAbsoluteImagePath(uri);
	                    imageuri.setText(getAbsoluteImagePath(uri));
			            	ContentResolver resolver = getContentResolver(); 
			            	try {
								Imagedata=readStream(resolver.openInputStream(uri));
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			            }
			
			        }

		    	  
		      }
		 } 
		 private void checkinUsePlaceId() {
			    Session session = Session.getActiveSession();
			    if (session != null){
			        // Check for publish permissions    
			       /* List<String> permissions = session.getPermissions();
			        Log.v("DIV",permissions+"");
			        if (!isSubsetOf(PERMISSIONS2, permissions)) {
			            Session.NewPermissionsRequest newPermissionsRequest = new Session
			                    .NewPermissionsRequest(MainActivity.this, PERMISSIONS2);
			        session.requestNewPublishPermissions(newPermissionsRequest);
			        Log.v("DIV",permissions+"+");
			        return;
			        }*/
			        Bundle postParams = new Bundle();
			        postParams.putString("message", message.getText().toString());										//���d���X

			        try {
			        	 postParams.putByteArray("picture", Bitmap2Bytes(setting.upload));
			        	 } catch (Exception e) {
			        	  e.printStackTrace();
			        	 } 
			        
			        Request.Callback callback= new Request.Callback() {
			            public void onCompleted(Response response) {
			                JSONObject graphResponse = response
			                                           .getGraphObject()
			                                           .getInnerJSONObject();
			                String postId = null;
			                try {
			                    postId = graphResponse.getString("id");
			                } catch (JSONException e) {
			                }
			                FacebookRequestError error = response.getError();
			                if (error != null) {
			                    Toast.makeText(FacebookShare.this
			                         .getApplicationContext(),
			                         error.getErrorMessage(),
			                         Toast.LENGTH_SHORT).show();
			                    } else {
			                    	ShowMsgDialog();
			                }
			            }
			        };

			        Request request = new Request(session, "me/photos", postParams, 
			                              HttpMethod.POST, callback);
			        
			        myDialog = ProgressDialog.show(FacebookShare.this, "", "�W�ǹϤ���...", true);
			        RequestAsyncTask task = new RequestAsyncTask(request);
			        task.execute();
			        
			    }
			}
		  	private boolean isSubsetOf(Collection<String> subset, Collection<String> superset) {
		  	    for (String string : subset) {
		  	        if (!superset.contains(string)) {
		  	            return false;
		  	        }
		  	    }
		  	    return true;
		  	}
		  	
		  	private void ShowMsgDialog(){
		  		/******************���d���\��ק�ENT_NOR��******************/

		  	    /*****************************************************/
				Builder MyAlertDialog = new AlertDialog.Builder(this);
				MyAlertDialog.setMessage("���ɦ��\");
				DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener()
				{
				public void onClick(DialogInterface dialog, int which) {
				//�p�G��������Ʊ� �N�|�������� ��ܤ��
					myDialog.dismiss();
				}
				};;
				MyAlertDialog.setNeutralButton("�T�w",OkClick );
				MyAlertDialog.show();
			}

		  	
//			private void ImageCut()
//			{
//			     Button button = (Button)findViewById(R.id.button1);
//			     //�]�w���s����r
//			     button.setText("��ܹϤ�");
//			     //�]�w���s��ť��
//			     button.setOnClickListener(new Button.OnClickListener(){
//			     public void onClick(View v) {
//
//		                // �إ� "����ɮ� Action" �� Intent
//		                Intent intent = new Intent( Intent.ACTION_PICK );
//		                Log.d("1111111111", "Cancelled");
//		                // �L�o�ɮ׮榡
//		                intent.setType( "image/*" );
//		                
//		                // �إ� "�ɮ׿�ܾ�" �� Intent  (�ĤG�ӰѼ�: ��ܾ������D)
//		                Intent destIntent = Intent.createChooser( intent, "����ɮ�" );
//		                
//		                // �������ɮ׿�ܾ� (�����B�z���G, �|Ĳ�o onActivityResult �ƥ�)
//		                startActivityForResult( destIntent, 1 );
//		                Log.d("FACEBOOKCANCEL", "Cancelled");
//		                
//			}
//			       
//			     });
//				
//				
//			}
						  	
		    public static byte[] readStream(InputStream inStream) throws Exception { 
		        byte[] buffer = new byte[1024]; 
		        int len = -1; 
		        ByteArrayOutputStream outStream = new ByteArrayOutputStream(); 
		        while ((len = inStream.read(buffer)) != -1) { 
		            outStream.write(buffer, 0, len); 
		        } 
		        byte[] data = outStream.toByteArray(); 
		        outStream.close(); 
		        inStream.close(); 
		        return data; 
		    } 
		    
		    
		    
		    protected String getAbsoluteImagePath(Uri uri) {
		    	// can post image
		    	String[] proj = { MediaStore.Images.Media.DATA };
		    	Cursor cursor = managedQuery(uri, proj, // Which columns to return
		    	null, // WHERE clause; which rows to return (all rows)
		    	null, // WHERE clause selection arguments (none)
		    	null); // Order-by clause (ascending by name)
		    	if (cursor != null) {
		    	int column_index = cursor
		    	.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		    	cursor.moveToFirst();


		    	return cursor.getString(column_index);
		    	} else {

		    	//�p�G��Ь��Ż���������w�g�O������|�F
		    	return uri.getPath();
		    	}
		    	}
		    
		    
		    private byte[] Bitmap2Bytes(Bitmap bm){
		    	 ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    	 bm.compress(Bitmap.CompressFormat.PNG, 100, baos); 
		    	return baos.toByteArray();
		    	 }
		    
		    
		    
		    
		}
