package com.example.pptang;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MyView extends SurfaceView implements Runnable,SurfaceHolder.Callback {  
    Panel p;
    public MyView(Context context,MainActivity activity) {
		super(context);
		// TODO Auto-generated constructor stub
		this.activity=activity;
		getHolder().addCallback(this);
		r=new ReadResource(this,activity);
	 	r.init(); 	
	 	p=new Panel(r,Map.map2, Map.bothPlace2, new Graphics(this));
	 	List<Man> m = new ArrayList<Man>();
	 	m.add(new Man(p.map.bothPlace[0][0] * p.blockSize - p.pyx,
				p.map.bothPlace[0][1] * p.blockSize -p.pyy, 0, p, null));
	 	activity.setCa(m.get(0));
	 for (int i = 0; i < 3; i++) {
			m.add(AIFactory.getAI((int)(Math.random()*4)+1,
					p.map.bothPlace[i + 1][0]  * p.blockSize - p.pyx,
					p.map.bothPlace[i + 1][1]* p.blockSize -p.pyy, p));
		}
	 	p.setPeople(m);
	 	activity.addButton(p.button);//添加按钮
	 	viewThread =new Thread(this);
	}
    Thread viewThread;
    Activity activity;
    ReadResource r;  
    SurfaceHolder surfaceHolder;  
    @Override  
    public void run() {  
       Log.d("surfaceview", "run");  
        Canvas canvas = null;  
        surfaceHolder=MyView.this.getHolder();
        while (!Thread.currentThread().isInterrupted()) {  
            try {     
            	  canvas= null;
                  canvas = surfaceHolder.lockCanvas();//获取画布对象(获取整个屏幕的画布)  
                if (canvas != null) {  
                	p.g.setC(canvas);
                	p.paint(p.g);
                    //解锁画布，提交画好的图像  
                    surfaceHolder.unlockCanvasAndPost(canvas);  
                }  
                Thread.sleep(20);  
            } catch (InterruptedException e) {  
                Thread.currentThread().interrupt();  
                Log.d("surfaceview", "InterruptedException");  
            } finally {  
                Log.d("surfaceview", "finally");  
            }  
        }  
    }
   public  void drawImg(Canvas canvas,Bitmap img,int x,int y,int w,int h){
	   canvas.drawBitmap(img, x+r.ax, y, null);
   }	

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
	 
		viewThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		try {
			Thread.sleep(9999999);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}  
  
}  