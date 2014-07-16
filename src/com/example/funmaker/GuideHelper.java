package com.example.funmaker;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

public class GuideHelper {

	private Activity context;
	private ViewGroup rootLayout;
	//private ScrollLayout scrollLayout;
	//private int[] guideResIds = {R.drawable.guide_help1, R.drawable.guide_help2, R.drawable.guide_help3, R.drawable.guide_help4};
	private static final String GUIDE_VERSION_NAME = "GUIDEVERSION";
	private static final int  GUIDE_VERSION_CODE = 3;
	private int guide;
	
	public GuideHelper(Context context, int i){
		this.context = (Activity)context;
		
		guide = i;
		createGuideLayout();
		initGuideView();
	}
	
	/**
	 * @Description: 創建引導圖層
	 * @param @return
	 * @return ViewGroup
	 * @throws
	 */
	private void createGuideLayout(){
		ViewGroup rootView = (ViewGroup) context.getWindow().getDecorView();
		if(guide==0) {
			LayoutInflater lf = context.getLayoutInflater();
			rootLayout = (ViewGroup) lf.inflate(R.layout.tutorial, null);
		}
		else {
			LayoutInflater lf = context.getLayoutInflater();
			rootLayout = (ViewGroup) lf.inflate(R.layout.tutorial2, null);
		}
		//scrollLayout = (ScrollLayout) rootLayout.findViewById(R.id.scroll_layout);
		
		rootLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
			//不需要任何操作，只是为了消費事件
			}
		});
		rootView.addView(rootLayout);
	}
	
	/**
	 * @Description: 初始化引導視圖
	 * @param 
	 * @return void
	 * @throws
	 */
	public void initGuideView() {
		/*for(int resId : guideResIds){
			scrollLayout.addView(makeGuideView(resId));
		}*/
		TextView textexit = (TextView)rootLayout.findViewById(R.id.textexit);
//		textexit.setTypeface(Typeface.createFromAsset(context.getAssets(), null));
		
//		TextView Textviewtype=(TextView)rootLayout.findViewById(R.id.textset);
//		TextView Textviewtype2=(TextView)rootLayout.findViewById(R.id.textset2);
//		TextView top_riht_Textview=(TextView)rootLayout.findViewById(R.id.TopRightText);
//		Textviewtype.setTypeface(Typeface.createFromAsset(context.getAssets(), null));
//		Textviewtype2.setTypeface(Typeface.createFromAsset(context.getAssets(), null));
//		top_riht_Textview.setTypeface(Typeface.createFromAsset(context.getAssets(), null));
		textexit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				closeGuide();
			}
		});

		//scrollLayout.setPageEndListener(new PageEndListener(){

			/*@Override
			public void scrollEnd() {
				closeGuide();
			}
			
		});*/
	}
	
	/**
	 * @Description: 生成每個引導視圖
	 * @param @param resId
	 * @param @return
	 * @return View
	 * @throws
	 */
	/*public View makeGuideView(int resId){
		ImageView guideView = new ImageView(context);
		guideView.setImageResource(resId);
		guideView.setPadding(10, 10, 10, 10);
		return guideView;
	}*/
	
	/**
	 * @Description: 打開引導層
	 * @param 
	 * @return void
	 * @throws
	 */
	public void openGuide(){
		if(guideCheck()){
			rootLayout.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * @Description: 關閉引導層
	 * @param 
	 * @return void
	 * @throws
	 */
	public void closeGuide(){
		
		AlphaAnimation alphaAnim = new AlphaAnimation(1.0f, 0.2f);
		alphaAnim.setDuration(500);
		ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		scaleAnim.setDuration(500);
		AnimationSet AnimSet = new AnimationSet(false);
		AnimSet.setDuration(500);
		AnimSet.addAnimation(scaleAnim);
		AnimSet.addAnimation(alphaAnim);
		
		AnimSet.setAnimationListener(new AnimationListener(){

			@Override
			public void onAnimationStart(Animation animation) {
				
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				rootLayout.clearAnimation();
				rootLayout.setVisibility(View.GONE);
				saveGuideVersion();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
		});
		
		rootLayout.startAnimation(AnimSet);
	}
	
	/**
	 * @Description: 保存引導版本記錄
	 * @param 
	 * @return void
	 * @throws
	 */
	private void saveGuideVersion() {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		Editor edit = sp.edit();
		edit.putInt(GUIDE_VERSION_NAME, GUIDE_VERSION_CODE);
		edit.commit();
	}
	
	/**
	 * @Description: 檢測引導圖版本，判斷是否啟動引導
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	private boolean guideCheck(){
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		int guideVer = sp.getInt(GUIDE_VERSION_NAME, 0);
		if(GUIDE_VERSION_CODE > 0 && GUIDE_VERSION_CODE > guideVer) {
			return true;
		} else {
			return true;	//如果只顯示一次的話改成false
		}
	}
	
	public boolean dispatchTouchEvent(MotionEvent event) {
		closeGuide();
		return false;
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		closeGuide();
		return false;
	}
	
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		closeGuide();
		return false;
	}
}
