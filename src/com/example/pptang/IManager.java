package com.example.pptang;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author zz
 * 
 */
public class IManager {
	List<Partner> partner = new ArrayList<Partner>();
	Controller ctr;
	// CC c;
	int dcPort = 8888;
	DC d;

	public IManager(Controller ctr, int dcPort) {
		// c=new CC(ccPort);
		this.ctr = ctr;
		d = new DC(this, dcPort);
	}

	public void send(String word, Partner p) {
		d.sendMessage(word, p);
	}

	public void sendMulticast(String word, InetAddress ip) {
		d.sendMulticast(word, ip);
	}

	public void startRecive() {
		d.startRecive();
	}

	public void satrtRMulticast() {
		d.startRMulticast();
	}

	public void stopRecive() {
		d.stopWoking();
	}

	public void setDCport(int p) {
		dcPort = p;
	}
}

/**
 * 控制连接
 * 
 * @author Administrator
 * 
 */
/*
 * class CC { ServerSocket s; Socket socket; int port=8888; IManager im; public
 * CC(IManager im,int port){ this.im=im; this.port=port; init(); } void init(){
 * try{ s = new ServerSocket(port); } catch(IOException e){
 * System.out.println("端口申请失败！"); e.printStackTrace(); } } public void recive(){
 * Socket ss=null; try { ss=s.accept(); InputStream op=ss.getInputStream();
 * BufferedReader is=new BufferedReader(new InputStreamReader(op));
 * is.readLine(); } catch (IOException e) { // TODO Auto-generated catch block
 * e.printStackTrace(); } } }
 */
/**
 * 数据连接
 * 
 * @author Administrator
 * 
 */
class DC {
	int myPort;
	IManager im;
	Boolean isWoking = true;
	private DatagramSocket dss;
	private MulticastSocket mss;
	private int MulticastPort = 4006;
	String MulticastIp = "224.0.2.4";
	public DC(IManager im, int port) {
		this.im = im;
		myPort = port;
	}

	public void stopWoking() {
		isWoking = false;
	}

	public void sendMulticast(final String word, InetAddress ip) {// 发送组播
		try {
			mss = new MulticastSocket();
			mss.joinGroup(ip);
			byte[] buf = word.getBytes();
			DatagramPacket dp = new DatagramPacket(buf, buf.length, ip,
					MulticastPort);
			mss.send(dp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendMessage(final String word, final Partner p) {// 发送UDP单播
		try {
			byte[] buf = word.getBytes();
			InetAddress address = p.getIp();
			DatagramPacket dp = new DatagramPacket(buf, buf.length, address,
					p.getPort());
			dss = new DatagramSocket();
			/*
			 * System.out.println("数据包目标地址："+dp.getAddress()+"\n");
			 * System.out.println("数据包目标端口号："+dp.getPort()+"\n");
			 * System.out.println("数据包长度："+dp.getLength()+"\n");
			 */
			dss.send(dp);
		} catch (Exception e) {
			System.out.println("发送失败");

		}

	}

	public void startRMulticast() {// 开始接收组播
		isWoking = true;
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				DatagramPacket dp1 = null;
				MulticastSocket msr = null;

				byte[] buf1 = new byte[1024];
				try {
					InetAddress group = InetAddress.getByName(MulticastIp);
					msr = new MulticastSocket(MulticastPort); // 1.创建一个用于发送和接收的MulticastSocket组播套接字对象
					msr.joinGroup(group); // 3.使用组播套接字joinGroup(),将其加入到一个组播
					dp1 = new DatagramPacket(buf1, buf1.length);
					System.out.println("组播接收port=" + MulticastPort);
				} catch (Exception e) {
					System.out.println("创建失败！");
					isWoking = false;
				}
				while (isWoking) {

					try {
						Thread.sleep(30);
						msr.receive(dp1);
						String message1 = new String(dp1.getData(), 0,
								dp1.getLength());
						/*
						 * System.out.println("收到数据长度："+length+"\n"); System.
						 * out.println("收到数据来自："+address+"端口："+port+"\n");
						 */
						System.out.println("\n收到组播数据是：" + message1 + "\n");
						im.ctr.recive(message1, dp1);

					} catch (Exception e) {
						System.out.println("组播接收套接字接收失败！");
					}

				}
				while (isWoking && !msr.isClosed())
					msr.close();// 关闭套接字释放端口
				System.out.println("成功关闭组播套接字");
			}

		}).start();
	}

	public void startRecive() {// 开始接收单播
		isWoking = true;
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				DatagramPacket dp = null;
				DatagramSocket ds = null;

				byte[] buf = new byte[1024];
				try {

					dp = new DatagramPacket(buf, buf.length);
					ds = new DatagramSocket((myPort));
					System.out.println("单播接受port=" + myPort);
				} catch (Exception e) {
					System.out.println("单播接收套接字创建失败！");
					isWoking = false;
				}
				while (isWoking) {
					try {
						// synchronized (isWoking) {
						ds.receive(dp);
						int length = dp.getLength();
						String message = new String(dp.getData(), 0, length);
						/*
						 * System.out.println("收到数据长度："+length+"\n"); System.
						 * out.println("收到数据来自："+address+"端口："+port+"\n");
						 */
						System.out.println("\n收到单播数据是：" + message + "\n");
						im.ctr.recive(message, dp);
					} catch (Exception e) {
						System.out.println("单播接收失败！");
						isWoking = false;
					}
				}
				while (isWoking && !ds.isClosed())
					ds.close();// 关闭套接字
				System.out.println("成功关闭单播套接字");
			}

		}).start();

	}
}
