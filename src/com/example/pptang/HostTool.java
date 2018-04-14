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
			System.out.println("主机收到" + m);
			switch (state) {
			case REDYING:
				switch (Integer.valueOf(m)) {
				case LEAVE:// 处理有人离开房间
					for (Partner p : users) {
						if (p.getIp().equals(dp.getAddress())) {
							users.remove(p);
							System.out.println(dp.getAddress() + "leave");
						}
					}
					break;
				case JOIN:// 处理有人进入房间
					boolean add = true;
					for (Partner p : users) {// 查询是否已经在房间中
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
			case GAMING:// 游戏中
				//String a[] = m.split(":");
				if (Integer.valueOf(m)==GAMESTART)
					break;

				shanbo(handle(m));// 处理接收到的消息后组播到所有人
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

	public String handle(String m) {// 游戏进行时的数据处理
		String[] mm = m.split(":");
		for (Man man : getMans()) {
			if ((man.getID() + "").equals(mm[0])) {
				m = m + ":" + man.getX() + ":" + man.getY();
				man.ctl.recive(mm[1], null);
				System.out.println("处理" + m);
				return m;
			}
		}
		return null;
	}

	/**
	 * 不断广播我是主机的消息到局域网中
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
					im.sendMulticast(msg, InetAddress.getByName(MulticastIp));// 组播到所有客户端
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
	 * 返回主机接受的玩家网络信息列表
	 * 
	 * @return
	 */
	public List<Partner> getPartners() {
		System.out.println("伙伴列表: ");
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
			myself = new Partner(InetAddress.getLocalHost(), hostPort);// 设置主机的网络消息
			users.add(myself);// 把自己加入用户列表
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		state = STATE.REDYING;
		im.startRecive();// 开始接受局域网中单播
		t.start();// 开始广播我是主机的进程
	}

	public void stopWork() {
		state = STATE.OVER;
		im.stopRecive();// 停止接受单播
	}

	/**
	 * 告知所有小伙伴玩家列表
	 */

	public void fenpei() {
		String message = "";
		for (Partner p : users) {
			Man m = p.getMan();
			message += m.getID() + ":" + m.getX() + ":" + m.getY() + "/";
		}
		System.out.println("群发:" + message);
		shanbo(message);
	}

	/**
	 * 主机创建所有人物对象 并分配ID
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
					.println("分配ID " + p.getMan().getID() + "角色到" + p.getIp());
		}
	}

	/**
	 * 获得创建的对应室友用户的角色对象列表
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
	 * 发送M字符串到所有小伙伴
	 * 
	 * @param m
	 */
	public void shanbo(String m) {
		try {
			im.sendMulticast(m, InetAddress.getByName(MulticastIp));
			System.out.println("广播" + m);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 开始游戏
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
		im.send(m, myself);// 主机给自己发送命令
	}
}

/**
 * 用于依次分配房间用户的唯一ID
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
