package com.example.pptang;

import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.List;
/**
 * 
 * @author zz
 * 
 */
public class UserTool implements InetController {
	List<Man> users = new ArrayList<Man>();
	List<Host> hostGroup = new ArrayList<Host>();
	IManager im;

	static enum STATE {
		REDYING, REDY, GAMING, OVER
	};

	STATE state = STATE.REDYING;
	Panel gamePanel;
	String msg;
	Host host;
	int myID;
	int userPort = 8101, hostPort = 8200;
	String MulticastIp = "224.0.2.4";
	/**
	 * ������Ϣ
	 */
	Controller ctl = new Controller() {

		@Override
		public void recive(String m, DatagramPacket dp) {
			// TODO Auto-generated method stub
			System.out.println("�յ�" + m);
			switch (state) {
			case REDYING:
				String a[] = m.split(":");
				if (a[0].equals("imhost")) {
					// host=new Partner(dp.getAddress(),dp.getPort());
					boolean add = true;
					for (Host p : hostGroup) {
						if (p.getP().getIp().equals(dp.getAddress())) {
							add = false;
							break;
						}
					}
					if (add) {
						host = new Host(new Partner(dp.getAddress(),
								dp.getPort()), 0);
						hostGroup.add(host);
					}
					// System.out.println(("����"+dp.getAddress()+"�ķ���"));
					// mp.join.setEnabled(true);
					// System.out.println("���ӵ�host");
				} else if (a[0].equals("gamestart")) {
					myID = Integer.valueOf(a[1]);
					setState(STATE.GAMING);
					System.out.println("�յ���Ϸ��ʼ!��ȡID" + myID);
					startGame();
				} else {
					msg = m;
				}
				break;
			case REDY:
				break;
			case GAMING:
				handle(m);
				break;
			case OVER:
				break;
			}
		}

		@Override
		public void recive(int m, DatagramPacket dp) {
			// TODO Auto-generated method stub
			
		}

	};

	public UserTool() {
		im = new IManager(ctl, userPort);
	}

	/**
	 * ��ʼ����
	 */
	public void startWork() {
		im.satrtRMulticast();// ��ʼ�����鲥��Ϣ
		im.startRecive();// ��ʼ���ܵ�����Ϣ
		this.setState(STATE.REDYING);// ����״̬Ϊ��׼����
	}

	/**
	 * ���뷿��
	 */
	public void joinHouse() {

	}

	/**
	 * ֹͣ����
	 */
	public void stopWork() {
		setState(STATE.OVER);
		im.stopRecive();// ֹͣһ�н��գ��رն˿�
	}

	/**
	 * new�������Man����ŵ�user�б�
	 */
	public void createMan() {
		String[] a = msg.split("/");
		String[][] b = new String[a.length][];
		for (int i = 0; i < a.length; i++) {
			b[i] = a[i].split(":");
		}
		for (int i = 0; i < b.length; i++) {
			int id = Integer.valueOf(b[i][0]);
			Man m;
			if (id == myID)// ���ñ���Man typeΪ2������1p
				m = new Man(Integer.valueOf(b[i][1]), Integer.valueOf(b[i][2]),
						2, gamePanel, this);
			else
				// ���������typeΪ3
				m = new Man(Integer.valueOf(b[i][1]), Integer.valueOf(b[i][2]),
						3, gamePanel, this);
			m.setID(id);
			users.add(m);
		}
	}

	/**
	 * ��ȡ���
	 * 
	 * @return
	 */
	public List<Man> getMans() {
		return users;
	}

	/**
	 * �����������ַ�����ϢM
	 * 
	 * @param m
	 */
	public void sendM(String m) {
		System.out.println("�����ڣ�" + host.getIp() + ":" + host.getPort());
		im.send(m, new Partner(host.getIp(), hostPort));
		System.out.println("����������" + m);
	}

	/**
	 * �����������͵�����
	 * 
	 * @param m
	 */
	public void handle(String m) {// ��Ϸ����ʱ�����ݴ���
		String[] mm = m.split(":");
		for (Man man : users) {
			if ((man.getID() + "").equals(mm[0])) {
				man.setSeat(Integer.valueOf(mm[2]), Integer.valueOf(mm[3]));
				man.ctl.recive(mm[1], null);
				break;
			}
		}
	}

	/**
	 * ��ʼ������Ϸ
	 */
	public void startGame() {//��ʼ��Ϸ ת��activity
		System.out.println("��ʼ��");

	}

	public void setState(STATE s) {
		state = s;
	}

	/**
	 * ��дInetController�ķ�����Ϣ�����ڼ��̲���ʱ���÷�����Ϣ������
	 */
	@Override
	public void sendMessage(String m) {
		// TODO Auto-generated method stub
		sendM(m);// �ͻ��˷������ݵ�����
	}
}
