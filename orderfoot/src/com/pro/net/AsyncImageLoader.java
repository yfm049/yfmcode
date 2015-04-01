package com.pro.net;
import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.widget.ImageView;
/**
 * 实现图片的异步载入显示
 * @author kandanle
 *
 */
public class AsyncImageLoader {

    /**
     * 软引用对象，在响应内存需要时，由垃圾回收器决定是否清除此对象。软引用对象最常用于实现内存敏感的缓存。
     */
    private HashMap<String, SoftReference<Drawable>> imageCache;
    private Context context;
    public AsyncImageLoader(Context context){
        imageCache = new HashMap<String, SoftReference<Drawable>>();
        this.context=context;
    }
   
    public Drawable loadDrawable(final String msg,final ImageView imageView){
        if(imageCache.containsKey(msg)){
            //从缓存中读取人人
            SoftReference<Drawable> softReference = imageCache.get(msg);
            Drawable drawable = softReference.get();
            if(drawable != null){
            	imageView.setImageDrawable(drawable);
                return drawable;
            }
        }
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.obj!=null){
                	imageView.setImageDrawable((Drawable)msg.obj);
                }
                
            }
        };
        new Thread(){
            public void run() {
                Drawable drawable = loadImageFromUrl(context,msg);
                if(drawable!=null){
                	imageCache.put(msg, new SoftReference<Drawable>(drawable));
                    Message message = handler.obtainMessage(0, drawable);
                    handler.sendMessage(message);
                }
                
            }
        }.start();
       
        return null;
    }
   
    public Drawable loadImageFromUrl(Context context,String msg){
    	Drawable drawable=null;
        try{
        	SocketClient sc=new SocketClient(context);
        	byte[] b=sc.GetImageByte(msg);
        	drawable=new BitmapDrawable(context.getResources(), BitmapFactory.decodeByteArray(b, 0, b.length));
        	
        }catch(Exception e){
            e.printStackTrace();
        }
        return drawable;
    }
    public Bitmap stringtoBitmap(String string) {
		// 将字符串转换成Bitmap类型
		Bitmap bitmap = null;
		try {
			byte[] bitmapArray;
			bitmapArray = Base64.decode(string, Base64.DEFAULT);
			bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
					bitmapArray.length);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}
   
}