package com.yfm.view;


import java.io.InputStream;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.yfm.mryt.R;

public class GifView extends SurfaceView {

	private boolean isFirst = true,onefirst=true;
	private float oldLineDistance;
	private float rate = 1;
	private float oldRate = 1;
	private Bitmap currbitmap=null;
	private Paint paint;
	private boolean isdraw=true;
	private GifDecoder gifDecoder=null;
	private Context context;
	private ProgressDialog pd;
	private float oldx,oldy,tempx,tempy,tx,ty;
	

	public GifView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
		// TODO Auto-generated constructor stub
		currbitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
		this.getHolder().addCallback(new Callback());
		
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == MotionEvent.ACTION_UP) {
			isFirst = true;
			onefirst=true;
			oldRate = rate;
			tx=tempx;
			ty=tempy;
		} else {
			if (event.getPointerCount() == 2) {
				if (isFirst) {
					oldLineDistance = (float) Math.sqrt(Math.pow(event.getX(1)
							- event.getX(0), 2)
							+ Math.pow(event.getY(1) - event.getY(0), 2));
					isFirst = false;
				} else {
					float newLineDistance = (float) Math.sqrt(Math.pow(event.getX(1) - event.getX(0), 2)
							+ Math.pow(event.getY(1) - event.getY(0), 2));
					rate = oldRate * newLineDistance / oldLineDistance;
				}
			}else if(event.getPointerCount()==1&&isFirst){
				if(onefirst){
					oldx=event.getX();
					oldy=event.getY();
					onefirst = false;
				}else{
					tempx=tx+event.getX()-oldx;
					tempy=ty+event.getY()-oldy;
				}
			}
		}
		return true;
	}
	public void gifdraw(){
		Canvas canvas=this.getHolder().lockCanvas();
		if(canvas!=null){
			if(currbitmap!=null){
				canvas.drawColor(Color.BLACK);
				canvas.save();
				canvas.scale(rate, rate, this.getWidth()/ 2, this.getHeight() / 2);
				canvas.translate(tempx, tempy);
				int width = this.getWidth() / 2 - currbitmap.getWidth() / 2;
				int height = this.getHeight() / 2 - currbitmap.getHeight() / 2;
				canvas.drawBitmap(currbitmap, width, height, paint);
				canvas.restore();
			}
			this.getHolder().unlockCanvasAndPost(canvas);
		}
	}
	class DrawThread extends Thread{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			while(isdraw){
				try {
					Thread.sleep(50);
					gifdraw();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	class Callback implements SurfaceHolder.Callback{

		@Override
		public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void surfaceCreated(SurfaceHolder arg0) {
			// TODO Auto-generated method stub
			
			DrawThread thread=new DrawThread();
			thread.start();
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder arg0) {
			// TODO Auto-generated method stub
			isdraw=false;
			if(gifDecoder!=null){
				gifDecoder.free();
			}
			
		}
		
	}
    /**
     * 设置图片，开始解码
     * @param is 要设置的图片
     */
    public void setGifDecoderImage(InputStream is){
    	if(gifDecoder != null){
    		gifDecoder.free();
    		gifDecoder= null;
    	}
    	handler.sendEmptyMessage(1);
    	gifDecoder = new GifDecoder(is,new GifActionImpl());
    	gifDecoder.start();
    }
    class GifActionImpl implements GifAction{

		@Override
		public void parseOk(boolean parseStatus, int frameIndex) {
			// TODO Auto-generated method stub
			if(parseStatus&&frameIndex==-1){
				handler.sendEmptyMessage(2);
				GifPlayThread gt=new GifPlayThread();
				gt.start();
			}else if(!parseStatus){
				handler.sendEmptyMessage(3);
			}
		}
    	
    }
    class GifPlayThread extends Thread{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			while(isdraw){
				try {
					GifFrame frame = gifDecoder.next();
					currbitmap=frame.image;
					long sp=frame.delay;
					if(sp==0){
						sp=100;
					}
					Thread.sleep(sp);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
    	
    }
    private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what==1){
				pd=new ProgressDialog(context);
				pd.setMessage("正在解析图片...");
				pd.show();
			}
			if(msg.what==2){
				pd.dismiss();
			}
			if(msg.what==3){
				pd.dismiss();
				Toast.makeText(context, "解析图片失败", Toast.LENGTH_LONG).show();
			}
		}
    	
    };

}
