package com.example.pptang;

import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.view.SurfaceView;


public class ReadResource{
	SurfaceView view;
	MainActivity activity;
	int size,bSize,mSize,ax;
	boolean isReady=false;
	Bitmap [] ball=new Bitmap[9];
	Bitmap [] mapImg=new Bitmap[3];
	Bitmap [] face,back,left,right,dying,dead;
	Bitmap [] wface,wback,wleft,wright;
	Bitmap [] item;
	Bitmap [][][] man=new Bitmap[2][6][6];
	Bitmap victory,failed,cGame,button1,button2;
	ReadResource (SurfaceView view,MainActivity activity){
		this.view=view;
		this.activity=activity;
		size=activity.size;
		ax=activity.ax;
		bSize=size*6;
		mSize=size*10;
	}
	/*  
	   * 从Assets中读取图片  
	   */  
	  public  Bitmap getImageFromAssetsFile(String fileName)  
	  {  
	      Bitmap image = null;  
	      AssetManager am = activity.getResources().getAssets();  
	      try  
	      {  
	          InputStream is = am.open(fileName);  
	          image = BitmapFactory.decodeStream(is);  
	          is.close();  
	      }  
	      catch (IOException e)  
	      {  
	          e.printStackTrace();  
	      }  
	  
	      return image;  
	  
	  }  
	 private Bitmap imgChange(Bitmap img,int x,int y){
		   int width = img.getWidth();
	       int height = img.getHeight();
	       int newWidth = x;
	       int newHeight =  y;
	       float scaleWidth = ((float) newWidth) / width;
	       float scaleHeight = ((float) newHeight) / height;
	       Matrix matrix = new Matrix();
	       matrix.postScale(scaleWidth, scaleHeight);
	       //matrix.setTranslate(x, y);
	     return Bitmap.createBitmap(img, 0, 0, width, height, matrix, true);
	 }
	 public Bitmap imgChange(Bitmap img,int x){
		   int width = img.getWidth();
	       int height = img.getHeight();
	       float scaleWidth = ((float) x) / width;
	       float scaleHeight = ((float) x) / height;
	       Matrix matrix = new Matrix();
	       matrix.postScale(scaleWidth, scaleHeight);
	       //matrix.setTranslate(x, y);
	     return Bitmap.createBitmap(img, 0, 0, width, height, matrix, true);
	 }
	
	public   void init(){
	
				// TODO Auto-generated method stub
				  face=new Bitmap[6];
				  back=new Bitmap[6];
				  left=new Bitmap[6];
				  right=new Bitmap[6];
				  dead=new Bitmap[6];
				  dying=new Bitmap[6];
				    wface=new Bitmap[5];
					wback=new Bitmap[5];
					wleft=new Bitmap[5];
					wright=new Bitmap[5];
					item=new Bitmap [5];
					
					  for(int i=0;i<6;i++){//人物
						  face[i]=getImageFromAssetsFile("images/man1/face/f"+i+".png");
						  back[i]=getImageFromAssetsFile("images/man1/back/b"+i+".png");
						  left[i]=getImageFromAssetsFile ("images/man1/left/l"+i+".png");
						  right[i]=getImageFromAssetsFile("images/man1/right/r"+i+".png");
					  }
					  dead[0]=getImageFromAssetsFile("images/man1/dead.png");
					  dying[0]=getImageFromAssetsFile("images/man1/dying.png");
				      for(int i=0;i<3;i++){//地图
								mapImg[i]=getImageFromAssetsFile("images/wall/wall"+i+".jpg");
				    	}
				    	for(int i=0;i<9;i++){//泡泡
								ball[i]=getImageFromAssetsFile("images/ball/"+(i+1)+".png");
				    	}
				    	 for(int i=0;i<5;i++)//武器
						  {  
				    		 wface[i]=getImageFromAssetsFile("images/daodan/f/d"+(i+1)+".png");
						     wback[i]=getImageFromAssetsFile("images/daodan/b/d"+(i+1)+".png");
						     wleft[i]=getImageFromAssetsFile("images/daodan/l/d"+(i+1)+".png");
						     wright[i]=getImageFromAssetsFile("images/daodan/r/d"+(i+1)+".png");
						  }
				    	 for(int i=0;i<5;i++){
				    		 item[i]=getImageFromAssetsFile("images/itemIcon/"+(i+1)+".png");
				    	 }
				    	 //三条游戏结束显示
				    	 victory=getImageFromAssetsFile("images/menu/victory.png");
				    	 failed=getImageFromAssetsFile("images/menu/failed.png");
				    	 cGame=getImageFromAssetsFile("images/menu/cGame.jpg");
				    	   // mp=new MusicPlayer("34312.mid");
				    	 button1=getImageFromAssetsFile("images/button/button.png");
				    	 button2=getImageFromAssetsFile("images/button/button2.png");
				    	 //缩放图片
				    	 
						  for(int i=0;i<6;i++){//人物
							  face[i]=imgChange(face[i],mSize); 
							  back[i]=imgChange(back[i],mSize);
							  left[i]=imgChange(left[i],mSize);
							  right[i]=imgChange(right[i],mSize);
						  }
						  dead[0]=imgChange(dead[0],mSize);
						  dying[0]=imgChange(dying[0],mSize);
					      for(int i=0;i<3;i++){//地图
					    	  mapImg[i]=imgChange(mapImg[i],bSize);	
					    	}
					    	for(int i=0;i<4;i++){//泡泡
					    		ball[i]=imgChange(ball[i],bSize);		
					    	}
					    	
					    	ball[4]=imgChange(ball[4],bSize,bSize+bSize/3);	
					    	ball[5]=imgChange(ball[5],bSize+bSize/3,bSize);	
					    	ball[6]=imgChange(ball[6],bSize,bSize+bSize/3);	
					    	ball[7]=imgChange(ball[7],bSize+bSize/3,bSize);	
					    	ball[8]=imgChange(ball[8],bSize+bSize/6,bSize+bSize/6);	
					    	 for(int i=0;i<5;i++)//武器
							  {  
					    		 wface[i]= imgChange( wface[i],bSize);		
					    		 wback[i]=imgChange( wback[i],bSize);		
					    		 wleft[i]=imgChange( wleft[i],bSize);		
					    		 wright[i]=imgChange( wright[i],bSize);		
							  }
					    	 for(int i=0;i<5;i++){
					    		 item[i]=imgChange(  item[i],bSize);							    	
					    	 }
					    	 button1=imgChange(button1,bSize*5);		
					    	 button2=imgChange(button2,bSize*2);		
					    	 
					    	 //三条游戏结束显示
					    	 victory=getImageFromAssetsFile("images/menu/victory.png");
					    	 failed=getImageFromAssetsFile("images/menu/failed.png");
					    	 cGame=getImageFromAssetsFile("images/menu/cGame.jpg");
				    	 //缩放图片结束
		      isReady=true;
		      
		      

		
	}

}
