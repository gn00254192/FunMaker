package com.example.funmaker;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private Integer width;
    private Integer height=200;
    private Bitmap[] mImageIds;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
    	if(mImageIds[position]!=null)
    	{
        ImageView imageView = new ImageView(mContext);
        //設定圖片來源
        imageView.setImageBitmap(mImageIds[position]);
        //設定圖片的寬、高
        imageView.setLayoutParams(new Gallery.LayoutParams((mImageIds[position].getHeight()/height)*mImageIds[position].getWidth(), height));
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    	}
		return null;
       
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Bitmap[] getmImageIds() {
        return mImageIds;
    }

    public void setmImageIds(Bitmap[] mImageIds) {
        this.mImageIds = mImageIds;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public int getCount() {
        return setting.select;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    public float getScale(boolean focused, int offset){
    	   return Math.max(0, 1.0f/(float)Math.pow(2, Math.abs(offset)));
    	   }
}