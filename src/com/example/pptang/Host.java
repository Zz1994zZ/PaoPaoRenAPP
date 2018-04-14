package com.example.pptang;

import java.net.InetAddress;

/**
 * 局域网主机
 * 
 * @author Administrator
 * 
 */
public class Host {
	private Partner p;
	private int num = 0;

	/**
	 * 获得该主机的网络相关信息
	 * 
	 * @return
	 */
	public Partner getP() {
		return p;
	}

	public void setP(Partner p) {
		this.p = p;
	}

	/**
	 * 获得主机人数
	 * 
	 * @return
	 */
	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public Host(Partner p, int num) {
		this.p = p;
		this.num = num;
	}

	/**
	 * 获取主机IP
	 * 
	 * @return
	 */
	public InetAddress getIp() {
		return p.getIp();
	}

	/**
	 * 获取主机服务进程端口
	 * 
	 * @return
	 */
	public int getPort() {
		return p.getPort();
	}
}
