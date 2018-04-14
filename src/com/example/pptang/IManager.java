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
 * ��������
 * 
 * @author Administrator
 * 
 */
/*
 * class CC { ServerSocket s; Socket socket; int port=8888; IManager im; public
 * CC(IManager im,int port){ this.im=im; this.port=port; init(); } void init(){
 * try{ s = new ServerSocket(port); } catch(IOException e){
 * System.out.println("�˿�����ʧ�ܣ�"); e.printStackTrace(); } } public void recive(){
 * Socket ss=null; try { ss=s.accept(); InputStream op=ss.getInputStream();
 * BufferedReader is=new BufferedReader(new InputStreamReader(op));
 * is.readLine(); } catch (IOException e) { // TODO Auto-generated catch block
 * e.printStackTrace(); } } }
 */
/**
 * ��������
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

	public void sendMulticast(final String word, InetAddress ip) {// �����鲥
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

	public void sendMessage(final String word, final Partner p) {// ����UDP����
		try {
			byte[] buf = word.getBytes();
			InetAddress address = p.getIp();
			DatagramPacket dp = new DatagramPacket(buf, buf.length, address,
					p.getPort());
			dss = new DatagramSocket();
			/*
			 * System.out.println("���ݰ�Ŀ���ַ��"+dp.getAddress()+"\n");
			 * System.out.println("���ݰ�Ŀ��˿ںţ�"+dp.getPort()+"\n");
			 * System.out.println("���ݰ����ȣ�"+dp.getLength()+"\n");
			 */
			dss.send(dp);
		} catch (Exception e) {
			System.out.println("����ʧ��");

		}

	}

	public void startRMulticast() {// ��ʼ�����鲥
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
					msr = new MulticastSocket(MulticastPort); // 1.����һ�����ڷ��ͺͽ��յ�MulticastSocket�鲥�׽��ֶ���
					msr.joinGroup(group); // 3.ʹ���鲥�׽���joinGroup(),������뵽һ���鲥
					dp1 = new DatagramPacket(buf1, buf1.length);
					System.out.println("�鲥����port=" + MulticastPort);
				} catch (Exception e) {
					System.out.println("����ʧ�ܣ�");
					isWoking = false;
				}
				while (isWoking) {

					try {
						Thread.sleep(30);
						msr.receive(dp1);
						String message1 = new String(dp1.getData(), 0,
								dp1.getLength());
						/*
						 * System.out.println("�յ����ݳ��ȣ�"+length+"\n"); System.
						 * out.println("�յ��������ԣ�"+address+"�˿ڣ�"+port+"\n");
						 */
						System.out.println("\n�յ��鲥�����ǣ�" + message1 + "\n");
						im.ctr.recive(message1, dp1);

					} catch (Exception e) {
						System.out.println("�鲥�����׽��ֽ���ʧ�ܣ�");
					}

				}
				while (isWoking && !msr.isClosed())
					msr.close();// �ر��׽����ͷŶ˿�
				System.out.println("�ɹ��ر��鲥�׽���");
			}

		}).start();
	}

	public void startRecive() {// ��ʼ���յ���
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
					System.out.println("��������port=" + myPort);
				} catch (Exception e) {
					System.out.println("���������׽��ִ���ʧ�ܣ�");
					isWoking = false;
				}
				while (isWoking) {
					try {
						// synchronized (isWoking) {
						ds.receive(dp);
						int length = dp.getLength();
						String message = new String(dp.getData(), 0, length);
						/*
						 * System.out.println("�յ����ݳ��ȣ�"+length+"\n"); System.
						 * out.println("�յ��������ԣ�"+address+"�˿ڣ�"+port+"\n");
						 */
						System.out.println("\n�յ����������ǣ�" + message + "\n");
						im.ctr.recive(message, dp);
					} catch (Exception e) {
						System.out.println("��������ʧ�ܣ�");
						isWoking = false;
					}
				}
				while (isWoking && !ds.isClosed())
					ds.close();// �ر��׽���
				System.out.println("�ɹ��رյ����׽���");
			}

		}).start();

	}
}
