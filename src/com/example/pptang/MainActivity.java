package com.example.pptang;

import android.os.Bundle;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

public class MainActivity extends Activity {
 private ControllerAdapter ca=null;
 private gameButton button=null;
 int width,height,size,ax;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DisplayMetrics dm = new DisplayMetrics();getWindowManager().getDefaultDisplay().getMetrics(dm);
		width = dm.widthPixels;//宽度
		height = dm.heightPixels ;//高度
		size=height/72;
		ax=(width-height)/4;
		setContentView(new MyView(this,this));
	}
	public void setCa(Man m){
		ca=m.ka;
	}
	public void addButton(gameButton b){
		this.button=b;
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
        if(ca==null||button==null)
        	return true;
		//获得触摸的坐标
		float x = event.getX();
		float y = event.getY(); 
		System.out.println(x+":"+y);
		if(event.getPointerCount()>=2)
		{
			for(int i=0;i<event.getPointerCount();i++){
				if(event.getX(i)>width*2/3)
					ca.keyPressed(0);
			}
		}
		switch (event.getAction()) 
		{
		//触摸屏幕时刻
		case MotionEvent.ACTION_DOWN:
  if(x>width*2/3)
        	ca.keyPressed(0);
		break;
		//触摸并移动时刻
		case MotionEvent.ACTION_MOVE:
			ca.keyPressed(button.changeSeat((int)x,(int) y));
		break;
		//终止触摸时刻
		case MotionEvent.ACTION_UP:
	        	ca.keyReleased(3);
	        	ca.keyReleased(4);
	        	ca.keyReleased(1);
	        	ca.keyReleased(2);
			    button.recover();
		break;
		}
		return true;
		}


}
