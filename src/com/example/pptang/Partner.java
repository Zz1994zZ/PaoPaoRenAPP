package com.example.pptang;



import java.net.InetAddress;
/**
 * С��� �������������Ϣ���
 * @author Administrator
 *
 */
public class Partner {
private String name="blank";
private InetAddress ip;
private int port;
private Man m;
public Partner(InetAddress ip,int port){
	this.ip=ip;
	this.port=port;
}
/**
 * ������Ҷ�Ӧ��Man����
 * @param m
 */
public void setMan(Man m){
	this.m=m;
}
/**
 * �����Ҷ�Ӧ��Man����
 * @return
 */
public Man getMan(){
	return m;
}
public void setName(String name){
	this.name=name;
}
public String getName(){
	return name;
}
public void setIp(InetAddress ip){
	this.ip=ip;
}
public InetAddress getIp(){
	return ip;
}
public void setPotr(int port){
	this.port=port;
}
public int getPort(){
	return port;
}
}
