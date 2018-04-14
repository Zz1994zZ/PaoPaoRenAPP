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
	 * 接收消息
	 */
	Controller ctl = new Controller() {

		@Override
		public void recive(String m, DatagramPacket dp) {
			// TODO Auto-generated method stub
			System.out.println("收到" + m);
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
					// System.out.println(("发现"+dp.getAddress()+"的房间"));
					// mp.join.setEnabled(true);
					// System.out.println("连接到host");
				} else if (a[0].equals("gamestart")) {
					myID = Integer.valueOf(a[1]);
					setState(STATE.GAMING);
					System.out.println("收到游戏开始!获取ID" + myID);
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
	 * 开始工作
	 */
	public void startWork() {
		im.satrtRMulticast();// 开始接受组播消息
		im.startRecive();// 开始接受单薄消息
		this.setState(STATE.REDYING);// 设置状态为在准备中
	}

	/**
	 * 加入房间
	 */
	public void joinHouse() {

	}

	/**
	 * 停止工作
	 */
	public void stopWork() {
		setState(STATE.OVER);
		im.stopRecive();// 停止一切接收，关闭端口
	}

	/**
	 * new出所需的Man对象放到user列表
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
			if (id == myID)// 设置本人Man type为2即网络1p
				m = new Man(Integer.valueOf(b[i][1]), Integer.valueOf(b[i][2]),
						2, gamePanel, this);
			else
				// 其他网络断type为3
				m = new Man(Integer.valueOf(b[i][1]), Integer.valueOf(b[i][2]),
						3, gamePanel, this);
			m.setID(id);
			users.add(m);
		}
	}

	/**
	 * 获取玩家
	 * 
	 * @return
	 */
	public List<Man> getMans() {
		return users;
	}

	/**
	 * 向主机发送字符串消息M
	 * 
	 * @param m
	 */
	public void sendM(String m) {
		System.out.println("主机在：" + host.getIp() + ":" + host.getPort());
		im.send(m, new Partner(host.getIp(), hostPort));
		System.out.println("向主机发送" + m);
	}

	/**
	 * 处理主机传送的数据
	 * 
	 * @param m
	 */
	public void handle(String m) {// 游戏进行时的数据处理
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
	 * 开始进行游戏
	 */
	public void startGame() {//开始游戏 转换activity
		System.out.println("开始！");

	}

	public void setState(STATE s) {
		state = s;
	}

	/**
	 * 重写InetController的发送消息方法在键盘操作时调用发送消息到主机
	 */
	@Override
	public void sendMessage(String m) {
		// TODO Auto-generated method stub
		sendM(m);// 客户端发送数据到主机
	}
}
