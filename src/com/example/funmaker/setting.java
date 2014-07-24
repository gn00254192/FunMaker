package com.example.funmaker;

import android.R.bool;
import android.graphics.Bitmap;

public class setting {
	static float th = (float) 0.95;
	static float k=1;
	static int seedx=169,seedy=300,time=100000;
	static int np=256,ni=256,no=256,ng=1024;
	static int t=256, tt=(int) (Math.sqrt(t)/2);
	static int s=256,select=0;
	static String[] node=new String[5];
	static boolean drawable=false;
	static String imagenet_selected_image_path = "";
	static Bitmap upload;
	static Boolean agree;
	static int stat=-1;
	static String search;
	static String[][] imagelist=new String[6][2];
	static int imagenumber=-1;
}
