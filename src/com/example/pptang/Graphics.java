package com.example.pptang;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Graphics {
	MyView v;
	Canvas c;
	Graphics(MyView v){
		this.v=v;
	}
	public void setC(Canvas c){
		this.c=c;
	}
	public void drawImage(Bitmap img,int x,int y,int width,int height,Panel p){
		if(c!=null)
		v.drawImg(c, img, x, y, width,height);
	}
	/*public void unlockCanvas(){
		v.unlockCanvas(c);
	}*/
}
