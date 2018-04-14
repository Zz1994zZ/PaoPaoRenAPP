package com.example.pptang;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;

/**
 * 
 * @author zz
 * 
 */
public class Man {
	private int x, y, max = 2, speed = 6;
	int  size, power = 1,realSize,pyx,pyy;//size = 100
	private String name = "Blanker";
	int rx = 1000, ry = 1000;// �����������ϵ�����~
	//MusicPlayer mp;// ��Ч����
	List<Ball> ball = new ArrayList<Ball>();
	List<Ball> allBall;
	List<Weapon> weapon = new ArrayList<Weapon>();
	List<Weapon> allWeapon;
	List<int[]> stuckList = new ArrayList<int[]>();
	Bitmap[] face, back, left, right, state, dying, dead;
	Bitmap imgnow;
	Ball newset = null;
	boolean isDead = false, isDying = false, isOver = false;
	boolean setBall = false, beStuck = false;
	boolean ddd = false;
	Map p;
	MiGong mg;
	Panel myPanel;
	int direction = 2, step = 0, gx = 0, gy = 0, ix = 0, iy = 0, type;
	boolean stand = true;
	ControllerAdapter ka;
	int weaponType = 0;
	int weaponNum = 0;
	int deadNum = 0;
	// IManager im;
	int ID = -1;
	public void setMan(Panel p){
		realSize=p.blockSize;
		size=p.ManSize;
		pyx=p.pyx;
		t.start();
		pyy=p.pyy;
	}

	public void setID(int i) {
		ID = i;
	}

	public int getID() {
		return ID;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setSeat(int x, int y) {
		this.x = x;
		this.y = y;
		System.out.println("��������");
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int x) {
		if (x < 4)
			return;
		this.speed = x;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int x) {
		if (x > 10)
			return;
		this.max = x;
	}

	public int getSize() {
		return size;
	}

	public int getPower() {
		return power;
	}

	public void setPower(int x) {
		if (x > 10)
			return;
		this.power = x;
	}

	// ********************************������Ϊ*********************************************
	private void move(int  d) {// �˶�
		if (isDead || isDying)
			return;
		switch (d) {
		case 1:
			state = back;
			direction = 1;
			stand = false;
			// System.out.println("up");
			break;
		case 2:
			state = face;
			direction = 2;
			stand = false;
			// System.out.println("down");
			break;
		case 3:
			state = left;
			direction = 3;
			stand = false;
			// System.out.println("left");
			break;
		case 4:
			state = right;
			direction = 4;
			stand = false;
			// System.out.println("right");
			break;
		}
	}

	private void stop(int  d) {// ֹͣ�˶�
		Bitmap[] rs = null;
		switch (d) {
		case 10:
			rs = back;
			break;
		case 20:
			rs = face;
			break;
		case 30:
			rs = left;
			break;
		case 40:
			rs = right;
			break;
		}
		if (rs == state && !isDead && !isDying) {
			imgnow = state[5];
			stand = true;
		}
	}

	// ******************************************************************************************************
	Controller ctl = new Controller() {// ���ܿ���

		@Override
		public void recive(int m, DatagramPacket dp) {
			// TODO Auto-generated method stub
			switch (m) {
			case 1:
			case 2:
			case 3:
			case 4:
				move(m);
				break;
			case 11:
				skill();
				break;
			case 10:
			case 20:
			case 30:
			case 40:
				stop(m);
				break;
			}
		}

		@Override
		public void recive(String m, DatagramPacket dp) {
			// TODO Auto-generated method stub
			
		}

	};

	public void setSkill(int x) {// ���ü���
		if (weaponType == x)
			weaponNum += 2;
		else {
			weaponType = x;
			weaponNum = 2;
		}
	}

	private void skill() {// ʩ�ż���
		if (isDead || isDying)
			return;
		if (weaponNum <= 0) {
			weaponType = 0;
			weaponNum = 0;
		}
		switch (weaponType) {
		case 0:
			if (ball.size() < max) {
				newset = Ball.setBall(Man.this);
				if (newset != null) {
					ball.add(newset);
					allBall.add(newset);
				}
			}
			break;
		case 1:
			Weapon ww = Weapon.getWeapon(Man.this);
			weapon.add(ww);
			allWeapon.add(ww);
			break;
		case 2:
			Weapon www = Weapon.getWeapon(Man.this);
			weapon.add(www);
			allWeapon.add(www);
			System.out.println("���׶ε���");
			break;
		case 3:
			System.out.println("���׶ε���");
			break;
		}
		if (weaponType != 0)
			weaponNum--;
	}

	/**
	 * ����***********************************************************************
	 * ************************
	 */
	ControllerAdapter ka0 = new ControllerAdapter() {// ********************************************1P����
		@Override
		public void keyReleased(int  e) {
			Bitmap[] rs = null;
			switch (e) {
			case 1:
				rs = back;
				break;
			case 2:
				rs = face;
				break;
			case 3:
				rs = left;
				break;
			case 4:
				rs = right;
				break;
			/*case 0:

				if (myPanel.shwoInfo)
					myPanel.shwoInfo = false;
				else
					myPanel.shwoInfo = true;
				System.out.println("showInfo");
				break;*/
			}
			if (rs == state && !isDead && !isDying) {
				imgnow = state[5];
				stand = true;
			}
		}

		@Override
		public void keyPressed(int e) {
			if (isDead || isDying)
				return;
			switch (e) {
			case 1:
				state = back;
				direction = 1;
				stand = false;
				// System.out.println("up");
				break;
			case 2:
				state = face;
				direction = 2;
				stand = false;
				// System.out.println("down");
				break;
			case 3:
				state = left;
				direction = 3;
				stand = false;
				// System.out.println("left");
				break;
			case 4:
				state = right;
				direction = 4;
				stand = false;
				// System.out.println("right");
				break;
			/*case KeyEvent.VK_M:
				skill();
				mg.safeBox(gx, gy);
				System.out.println("attack");
				break;*/
			case 0:
				skill();
				break;
			/*case KeyEvent.VK_1:
				if (speed < 10)
					speed++;
				break;
			case KeyEvent.VK_2:
				if (speed > 4)
					speed--;
				break;*/

			}
		}
	};

	// *******************************************************AI���****************************************
	/**
	 * �ƶ���ָ������
	 * 
	 * @param x
	 * @param y
	 */
	public Boolean moveTo(final int t) {
		int x = 0, y = 0;
		switch (t) {
		case 4:
			x = gx;
			y = gy - 1;
			System.out.println("�����ߵ�" + x + ":" + y);
			break;
		case 2:
			x = gx;
			y = gy + 1;
			System.out.println("�����ߵ�" + x + ":" + y);
			break;
		case 1:
			y = gy;
			x = gx - 1;
			System.out.println("�����ߵ�" + x + ":" + y);
			break;
		case 3:
			y = gy;
			x = gx + 1;
			System.out.println("�����ߵ�" + x + ":" + y);
			break;
		}
		int time = 0;
		boolean finish = false;
		while (!finish) {
			if ((gx != rx && gy != ry && myPanel.map.map[y][x] != 0) || isDead
					|| isDying || time >= 100)
				return false;
			try {
				Thread.sleep(10);
				time++;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			switch (t) {
			case 4:
				move(1);
				finish = (this.y + 30*size/100) <= y * p.size;
				break;
			case 2:
				move(2);
				finish = (this.y + 30*size/100) >= y * p.size;
				break;
			case 1:
				move(3);
				finish = (this.x + 20*size/100) <= x * p.size;
				break;
			case 3:
				move(4);
				finish = (this.x + 20*size/100) >= x * p.size;
				break;
			}
		}
		switch (t) {
		case 4:
			stop(10);
			break;
		case 2:
			stop(20);
			break;
		case 1:
			stop(30);
			break;
		case 3:
			stop(40);
			break;
		}
		System.out.println("���һ��");
		return true;

	}

	Thread ai = new Thread(new Runnable() { // AI�ж��߳�*******************************************************

				@Override
				public void run() {
					// TODO Auto-generated method stub
					int order = 1;
					while (true) {

						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (isDead || isDying)
							continue;
						//

						Man em;
						if (!Man.this.myPanel.people.isEmpty())
							em = Man.this.myPanel.people.get(0);
						else
							em = Man.this;
						// �ҵ�����
						// System.out.println(em.gx+":"+em.gy);//�����������
						int a[] = mg.aa(gy, gx, em.gy, em.gx);// ��ȡ·��
						int l = (a == null ? 0 : a.length);// ·������

						int sp[] = mg.safeBox(gy, gx);// �����ȫ·��
						int sl = (sp == null ? 0 : sp.length);// ·������

						if (em.isDead || em.isDying || l == 0)// ����������
						{
							order = 2;
						}

						if (sl != 0 || l == 0)// ����ȫ�����޷��ҵ��ط�·��ʱ����
							order = 2;

						//System.out.println("����(" + order + ")");
						Boolean shot = true;
						for (int i = 0; i < l; i++) {
							if (i != 0 && a[i] != a[i - 1])
								shot = false;
						}
						if (mg.isSafe(em))
							order = 1;
						//
						switch (order) {// �ж�ָ��*******************************
						case 1:// ǰ���������λ��
							if (l == 0)
								break;
							if (shot)
								skill();
							for (int i = 0; i < l && i == 0; i++) {
								//System.out.println("����");

								if (!moveTo(a[i])) {
									//System.out.println("����ס");
								}

							}
							if (l != 0) {
								{
									// System.out.println("����ָ���ص�");
									//System.out.println("���ܱ���ס���Ѿ�����Ŀ��");
								}
							}
							order = 2;// ÿ��һ�����ж��Ƿ�ȫ
							break;
						case 2:// Ѱ�Ұ�ȫλ��
							for (int i = 0; i < sl; i++) {

								//System.out.println("����");
								if (!moveTo(sp[i])) {
									// System.out.println("����ס");
									// break;
									order = 2;

								}

							}
							if (sl == 0) {
								//System.out.println("û�а�ȫλ��");
								order = 1;
							}

							break;
						case 3:// ����
							skill();
							break;
						case 4:// ����Զ�����
							break;
						case 5:// ���ɿ��ɵ���
							break;
						}

					}
				}

			});
	private InetController ic;

	// /**********************************************************************************************************
	public Man(int x, int y, int type, Panel panel, InetController ic) {
		this.ic = ic;
		this.myPanel = panel;
		this.allBall = panel.allBall;
		this.allWeapon = panel.allWeapon;
		this.p = panel.map;
		this.type = type;
		this.x = ix = x;
		this.y = iy = y;
		this.mg = new MiGong(myPanel, Man.this);
		init();
		/*gx = (x + size/2) / realSize;
		gy = (y + realSize) / realSize;*/
		switch (type) {
		case 0:// ����1P
			ka = ka0;
			
			name="Tom";
			break;
		case 1:// ����2P
			//ka = ka1;
			name="Jerry";
			break;
		case 2:// ����1P
			//ka = ka2;
			name="LonelyYou";
			break;
		case 3:// ����������ҿ���
			//ka = null;
			name="Player"+ID;
			break;
		case 4:// AI
			//ka = null;
			ai.start();
			break;
		}

	}

	public void refresh() {
		// System.out.println("����");
		x = ix;
		y = iy;
		gx = (x + size/2) / realSize;
		gy = (y + realSize) / realSize;
		imgnow = face[5];
		direction = 2;
		stand = true;
		isDying = false;
		isDead = false;
	}

	public void beAttack() {
		if (isDead || isDying)
			return;
		isDying = true;
		System.out.println("������~");
		imgnow = dying[0];
		new Thread(new Runnable() {
			@Override
			public void run() {

				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// System.out.println("����~");
				isDying = false;
				isDead = true;
				deadNum++;//��������������� �ǵ�����Ļ���~!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				deadNum=0;
				if (deadNum >= 3) {// ����������Ϸ����
					isOver = true;
					switch (type) {
					case 0:// ����1P
						myPanel.gameOver(0);
						return;
					case 1:// ����2P
						myPanel.gameOver(1);
						return;
					case 2:// ����1P
						myPanel.gameOver(0);
						return;
					case 3:// ����������ҿ���
						myPanel.gameOver(2);
						return;
					case 4:// AI
						myPanel.gameOver(3);
						return;
					}
				}
				imgnow = dead[0];
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Man.this.refresh();// ����
			}
		}).start();

	}

	public void beTouch(Man m) {
		if (!isDying)
			return;
		// if(m�Ƕ���)
		// isDying=false;
		// else
		// {isDying=false;
		// isDead=true; }
	}

	private boolean isTouch2(int x, int y) {
		//x += 20;
		//y += 30;
		x += pyx;
		y += pyy;
		int[][] mg;
		if (ddd)// �����ɫ��������
		{
			mg = new int[p.map.length][p.map[0].length];// ����һ�ݵ�ͼ
			for (int i = 0; i < p.map.length; i++) {
				for (int j = 0; j < p.map[0].length; j++) {
					mg[i][j] = p.map[i][j];
				}
			}
			/*
			 * for(int [] a:stuckList){ mg[a[1]][a[0]]=0;//ȡ����ɫ�µ�������ײ }
			 */
			mg[ry][rx] = 0;// ȡ����ɫ�µ�������ײ

		} else {
			mg = p.map;
			rx = 1000;
			ry = 1000;
		}
		if (x < 0 || y < 0 || x > p.m2 * p.size - p.size
				|| y > p.m1 * p.size - p.size) {
			// System.out.println("�߳����⣡");
			return true;
		}

		if (x % p.size == 0 && y % p.size == 0) {
			if (mg[y / p.size][x / p.size] != 0) { // System.out.println("�������������");
				return true;
			}
		} else if (x % p.size == 0) {
			if (mg[y / p.size][x / p.size] != 0
					|| mg[y / p.size + 1][x / p.size] != 0) {// System.out.println("��ֱ���ϰ���");
				return true;
			}
		} else if (y % p.size == 0) {
			if (mg[y / p.size][x / p.size] != 0
					|| mg[y / p.size][x / p.size + 1] != 0) {// System.out.println("ˮƽ���ϰ���");
				return true;
			}
		} else if (mg[y / p.size][x / p.size] != 0
				|| mg[y / p.size + 1][x / p.size] != 0
				|| mg[y / p.size][x / p.size + 1] != 0
				|| mg[y / p.size + 1][x / p.size + 1] != 0) {
			switch (direction) {
			case 1:
				if (mg[y / p.size][x / p.size] == 0 && x % p.size <= p.size / 2) {
					this.x--;
				} else if (mg[y / p.size][x / p.size + 1] == 0
						&& x % p.size >= p.size / 2) {
					this.x++;
				}
				break;
			case 2:
				if (mg[y / p.size + 1][x / p.size] == 0
						&& x % p.size <= p.size / 2) {
					this.x--;
				} else if (mg[y / p.size + 1][x / p.size + 1] == 0
						&& x % p.size >= p.size / 2) {
					this.x++;
				}
				break;
			case 3:
				if (mg[y / p.size][x / p.size] == 0 && y % p.size <= p.size / 2) {
					this.y--;
				} else if (mg[y / p.size + 1][x / p.size] == 0
						&& y % p.size >= p.size / 2) {
					this.y++;
				}
				break;
			case 4:
				if (mg[y / p.size][x / p.size + 1] == 0
						&& y % p.size <= p.size / 2) {
					this.y--;
				} else if (mg[y / p.size + 1][x / p.size + 1] == 0
						&& y % p.size >= p.size / 2) {
					this.y++;
				}
				break;
			}
			beStuck = true;
			return true;
		}
		return false;
	}

	private void init() {
		face = myPanel.RR.face;
		back = myPanel.RR.back;
		left = myPanel.RR.left;
		right = myPanel.RR.right;
		dead = myPanel.RR.dead;
		dying = myPanel.RR.dying;
		//mp = myPanel.RR.mp;
		state = face;
		imgnow = state[5];
		stand = true;
		//t.start();
	}

	Thread t = new Thread(new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			int i = 6;

			while (true) {
				gx = (x + size/2) / realSize;
				gy = (y + realSize) / realSize;
				try {
					Thread.sleep(speed);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (p.map[gy][gx] == -1)// �ж��Ƿ���ˮ����
				{
					rx = gx;
					ry = gy;
				}
				ddd = (Math.abs(rx * p.size - (x + 20)) < p.size)
						&& (Math.abs(ry * p.size - (y + 30)) < p.size);// �Ƿ���ˮ����
				if (stand || isDying || isDead)
					continue;

				switch (direction) {
				case 1:
					if (!isTouch2(x, y - 1)
							|| (isTouch2(x, y) && gy >= 1 && p.map[gy - 1][gx] == 0))// ����������ϰ��п�ס��������յ��ƶ�
																						// ����ͬ��
						y -= 1;
					break;
				case 2:
					if (!isTouch2(x, y + 1)
							|| (isTouch2(x, y) && gy + 1 < p.m1 && p.map[gy + 1][gx] == 0))
						y += 1;
					break;
				case 3:
					if (!isTouch2(x - 1, y)
							|| (isTouch2(x, y) && gx >= 1 && p.map[gy][gx - 1] == 0))
						x -= 1;
					break;
				case 4:
					if (!isTouch2(x + 1, y)
							|| (isTouch2(x, y) && gx + 1 < p.m2 && p.map[gy][gx + 1] == 0))
						x += 1;
					break;
				}
				gx = (x + size/2) / realSize;
				gy = (y + realSize) / realSize;
				i++;
				if (!isDying && !isDead)
					imgnow = state[i / speed / 2 % 6];
			}
		}
	});
}
