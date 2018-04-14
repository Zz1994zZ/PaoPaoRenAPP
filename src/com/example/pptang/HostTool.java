package com.example.pptang;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author zz
 * 
 */
public class HostTool implements InetController {
	List<Partner> users = new ArrayList<Partner>();
	IManager im;

	static enum STATE {
		REDYING, REDY, GAMING, OVER
	};

	STATE state = STATE.REDYING;
	Panel gamePanel;
	IDgroup ig = new IDgroup();
	Partner myself;
	int userPort = 8101, hostPort = 8200;
	String MulticastIp = "224.0.2.4";
	final int LEAVE=0,JOIN=1,GAMESTART=2;
	Controller ctl = new Controller() {
		@Override
		public void recive(String m, DatagramPacket dp) {
			// TODO Auto-generated method stub
			System.out.println("�����յ�" + m);
			switch (state) {
			case REDYING:
				switch (Integer.valueOf(m)) {
				case LEAVE:// ���������뿪����
					for (Partner p : users) {
						if (p.getIp().equals(dp.getAddress())) {
							users.remove(p);
							System.out.println(dp.getAddress() + "leave");
						}
					}
					break;
				case JOIN:// �������˽��뷿��
					boolean add = true;
					for (Partner p : users) {// ��ѯ�Ƿ��Ѿ��ڷ�����
						if (p.getIp().equals(dp.getAddress()))
							add = false;
					}
					if (add) {
						users.add(new Partner(dp.getAddress(), userPort));
						System.out.println(dp.getAddress() + "join in~");
					}
					break;
				}
				break;
			case REDY:
				break;
			case GAMING:// ��Ϸ��
				//String a[] = m.split(":");
				if (Integer.valueOf(m)==GAMESTART)
					break;

				shanbo(handle(m));// ������յ�����Ϣ���鲥��������
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

	public String handle(String m) {// ��Ϸ����ʱ�����ݴ���
		String[] mm = m.split(":");
		for (Man man : getMans()) {
			if ((man.getID() + "").equals(mm[0])) {
				m = m + ":" + man.getX() + ":" + man.getY();
				man.ctl.recive(mm[1], null);
				System.out.println("����" + m);
				return m;
			}
		}
		return null;
	}

	/**
	 * ���Ϲ㲥������������Ϣ����������
	 */
	Thread t = new Thread(new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (state.equals(STATE.REDYING)) {
				try {
					Thread.sleep(1000);
					String msg = "imhost:" + users.size();
					for (Partner p : users) {
						msg += ":" + p.getIp();
					}
					im.sendMulticast(msg, InetAddress.getByName(MulticastIp));// �鲥�����пͻ���
					im.send(msg, new Partner(InetAddress.getByName("255.255.255.255"), userPort));
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	});

	public void setState(STATE s) {
		state = s;
	}

	/**
	 * �����������ܵ����������Ϣ�б�
	 * 
	 * @return
	 */
	public List<Partner> getPartners() {
		System.out.println("����б�: ");
		for (Partner p : users) {
			System.out.println(p.getIp() + "~");
		}
		return users;
	}

	public HostTool() {
		im = new IManager(ctl, hostPort);
	}

	public void startWork() {
		try {
			myself = new Partner(InetAddress.getLocalHost(), hostPort);// ����������������Ϣ
			users.add(myself);// ���Լ������û��б�
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		state = STATE.REDYING;
		im.startRecive();// ��ʼ���ܾ������е���
		t.start();// ��ʼ�㲥���������Ľ���
	}

	public void stopWork() {
		state = STATE.OVER;
		im.stopRecive();// ֹͣ���ܵ���
	}

	/**
	 * ��֪����С�������б�
	 */

	public void fenpei() {
		String message = "";
		for (Partner p : users) {
			Man m = p.getMan();
			message += m.getID() + ":" + m.getX() + ":" + m.getY() + "/";
		}
		System.out.println("Ⱥ��:" + message);
		shanbo(message);
	}

	/**
	 * ������������������� ������ID
	 */
	public void createMans() {
		for (Partner p : users) {
			int id = ig.getID();
			Man man;
			if (p.equals(myself))
				man = new Man(gamePanel.map.bothPlace[id - 1][0] * 60 - 20,
						gamePanel.map.bothPlace[id - 1][1] * 60 - 30, 2,
						gamePanel, this);
			else
				man = new Man(gamePanel.map.bothPlace[id - 1][0] * 60 - 20,
						gamePanel.map.bothPlace[id - 1][1] * 60 - 30, 3,
						gamePanel, this);
			man.setID(id);
			p.setMan(man);
			System.out
					.println("����ID " + p.getMan().getID() + "��ɫ��" + p.getIp());
		}
	}

	/**
	 * ��ô����Ķ�Ӧ�����û��Ľ�ɫ�����б�
	 * 
	 * @return
	 */

	public List<Man> getMans() {
		List<Man> a = new ArrayList<Man>();
		for (Partner p : users) {
			a.add(p.getMan());
		}
		return a;
	}

	/**
	 * ����M�ַ���������С���
	 * 
	 * @param m
	 */
	public void shanbo(String m) {
		try {
			im.sendMulticast(m, InetAddress.getByName(MulticastIp));
			System.out.println("�㲥" + m);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * ��ʼ��Ϸ
	 */
	public void startGame(Panel pl) {
		gamePanel = pl;
		createMans();
		fenpei();
		setState(STATE.GAMING);
		for (Partner p : users) {
			if (!p.equals(myself))
				im.send("gamestart:" + p.getMan().ID, p);
		}
	}

	@Override
	public void sendMessage(String m) {
		// TODO Auto-generated method stub
		im.send(m, myself);// �������Լ���������
	}
}

/**
 * �������η��䷿���û���ΨһID
 * 
 * @author Administrator
 * 
 */
class IDgroup {
	boolean[] used = { false, false, false, false };

	public int getID() {
		for (int i = 0; i < 4; i++)
			if (!used[i]) {
				used[i] = true;
				return i + 1;
			}
		return -1;
	}

	public void logoutID(int id) {
		used[id - 1] = false;
	}

}
