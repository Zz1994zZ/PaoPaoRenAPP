package com.example.pptang;

import android.graphics.Bitmap;

/**
 * 
 * @author zz
 *
 */
public class Item {
Panel p;
int ID=1;
Boolean working=true;
int gx,gy;
Bitmap img;
int size;
private void BePick(Man m){
	System.out.println(m.ID+"Åöµ½"+ID+"ºÅµÀ¾ß");
	working=false;
	switch(ID){
	case 1:
	case 2:
		m.setSkill(this.ID);
		break;
	case 3:
		m.setMax(m.getMax()+1);
		break;
	case 4:
		m.setSpeed(m.getSpeed()-1);
		break;
	case 5:
		m.setPower(m.getPower()+1);
		break;
	}
	p.allItem.remove(this);
}
public Item(Panel p,int x,int y){
	ID=(int) (Math.random()*5+1);
	this.img=p.RR.item[ID-1];
	this.p=p;
	this.gx=x;
	this.gy=y;
	this.size=p.map.size;
	p.allItem.add(this);
	t.start();
}
public void drawItem(Graphics g){
	if(working)
	g.drawImage(img, gy*size, gx*size, size, size,p);
}
Thread t=new Thread(new Runnable(){
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(working){
			try {
    			Thread.sleep(100);
    		} catch (InterruptedException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
			for(Man m:p.people){
				if(!m.isDead&&m.gx==gy&&m.gy==gx){
					BePick(m);
				}
			}
		}
	}
});	

}
