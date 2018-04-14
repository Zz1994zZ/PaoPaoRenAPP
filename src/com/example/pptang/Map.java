package com.example.pptang;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author zz
 *
 */
public class Map {
	ReadResource rr;
    int size;//60
	List<Man> people=new  ArrayList<Man>();
	int m1,m2;//行 列
	int backBlockNum;//作为背景的格子宽度
	int[][] map;
	int [][] bothPlace;
	Panel pl;
	public static int [][] map1={
			{2,2,2,2,2,2,0,0,0,0,0,2,2,2,2,2,2},
			{2,2,2,2,2,2,0,0,0,0,0,2,2,2,2,2,2},
			{2,2,2,2,2,2,0,1,0,1,0,2,2,2,2,2,2},
			{2,2,2,2,2,2,0,0,0,0,0,2,2,2,2,2,2},
			{2,2,2,2,2,2,0,1,0,1,0,2,2,2,2,2,2},
			{2,2,2,2,2,2,0,0,0,0,0,2,2,2,2,2,2},
			{2,2,2,2,2,2,0,1,0,1,0,2,2,2,2,2,2},
			{2,2,2,2,2,2,0,0,0,0,0,2,2,2,2,2,2},
			{2,2,2,2,2,2,0,0,0,0,0,2,2,2,2,2,2},
			{2,2,2,2,2,2,0,0,0,0,0,2,2,2,2,2,2},
			{2,2,2,2,2,2,0,0,0,0,0,2,2,2,2,2,2}
	};
	public static int [][] map2={
 	        {0,0,0,2,2,0,0,0,2,0,0,0,2,2,0,0,0},
			{0,0,1,2,0,2,2,2,0,2,2,2,0,2,1,0,0},
			{1,0,2,0,2,0,2,0,2,0,2,0,2,0,2,2,1},
			{1,0,0,2,0,2,1,1,1,1,1,2,0,2,0,2,1},
			{1,2,0,2,2,2,2,2,2,2,2,2,2,2,0,2,1},
			{1,2,0,2,0,2,2,2,2,2,2,2,0,2,0,2,1},
			{1,2,0,2,2,2,1,1,1,1,2,2,2,2,0,2,1},
			{1,2,2,0,2,0,2,2,2,2,2,0,2,0,2,2,1},
			{1,2,2,2,0,2,2,2,2,2,2,2,0,2,2,2,1},
			{1,2,2,2,2,0,2,0,0,0,2,0,2,2,2,2,1},
			{0,0,2,0,2,2,0,2,0,2,0,2,2,0,2,0,0},
			{0,0,0,0,2,2,2,0,2,0,2,2,2,0,0,0,0}};
	public static int[][] bothPlace2={{0,0,},
			           {14,11},
			           {14,0},
			           {0,11}};

	public static int[][] bothPlace1={{7,0,},
			           {7,9},
			           {9,7},
			           {9,9}};
	public Map(int [][] map,int [][] bothPlace,ReadResource rr){
		this.rr=rr;
		this.map=map;
		this.bothPlace=bothPlace;
		init();
	}
    private void init(){//初始化相关数据，读取地图图片
        m1=map.length;
        m2=map[0].length;
        backBlockNum=rr.ax/rr.size/6+1;
    }
    public void drawMap(Graphics g){//绘制地图
    	for(int i=0;i<m1;i++){
    		for(int j=0;j<m2;j++){
    			if(map[i][j]!=-1)
    			g.drawImage(rr.mapImg[map[i][j]],size*j,size*i,size,size,null);
    			else
    			{g.drawImage(rr.mapImg[0],size*j,size*i,size,size,null);
    			//g.setColor(Color.PINK);
    			//g.drawLine(size*j,size*i,size*j+60,size*i+60);
    			}
    		}
    	}
    	
    	//背景的花坛
    	for(int i=0;i<m1;i++){
    		for(int j=-1;j>=-backBlockNum;j--){
    			g.drawImage(rr.mapImg[1],size*j,size*i,size,size,null);
    		}
    		for(int j=m2;j<=backBlockNum+m2;j++){
    			g.drawImage(rr.mapImg[1],size*j,size*i,size,size,null);
    		}
    	}
    	setMapBy(this.toString());
    	
    }
    @Override
	public String toString(){//字符串化地图
    	String s="";
    	for(int i=0;i<m1;i++){
    		for(int j=0;j<m2;j++){
    			s=s+map[i][j]+":";
    		}
    		s+="/";
    	}
    	return s;
    }
    public void beBomb(int x,int y){//引爆指定位置
    	if(map[x][y]==0)
    		return;
    	 map[x][y]=0;
			if((int)(Math.random()*4)==0){//在被炸毁的砖块位置放置道具
				 new Item(pl,x,y);
			 }
    }
    public void setMapBy(String s){//获取地图
    	String[] m=s.split("/");
    	this.m1=m.length;
    	int [][] a=new int [m1][]; 
    	for(int i=0;i<m1;i++){
    		String mm[]=m[i].split(":");
        	this.m2=mm.length;
    		a[i]=new int [m2];
    		for(int j=0;j<m2;j++){
    			a[i][j]=Integer.valueOf(mm[j]);
    		}
    	}
    	this.map=a;
    	/*for(int i=0;i<m1;i++){
    		System.out.println("");
    		for(int j=0;j<m2;j++){
        		System.out.print(a[i][j]+" ");
    		}
    	}*/
    }
}
