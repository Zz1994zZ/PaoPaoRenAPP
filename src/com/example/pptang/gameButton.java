package com.example.pptang;

public class gameButton {
int x,y,width,height,x2,y2,R,centerX,centerY;
int direction;
Panel p;
ReadResource rr;
	gameButton(int size,Panel p){
		this.p=p;
		rr=p.RR;
		height=width=size*30;
		y=42*size;
	    x=0;
	    y2=centerY=y+9*size;
	    x2=centerX=x+9*size;
	    R=6*size;
	}
	public int  changeSeat(int sx,int sy){
		int dx,dy,dx2,dy2;
		dx=sx-centerX-R;
		dy=sy-centerY-R;
		dx2=dx*dx;
		dy2=dy*dy;
		if(dx2+dy2<=7*R*R&&dx2+dy2>=R*R){
			x2=sx-R;
			y2=sy-R;
			if(dy<=0&&dy2>=dx2)
				return 1;
			if(dy>=0&&dy2>=dx2)
				return 2;
			if(dx<=0&&dx2>=dy2)
				return 3;
			return 4;
		}
        return -1;
	}
	public void drawButton(Graphics g){
		g.drawImage(rr.button1, x-rr.ax, y, width, height, p);
		g.drawImage(rr.button2, x2-rr.ax, y2, width, height, p);
	}
	public void recover(){
		x2=centerX;
		y2=centerY;
	}
	
}
