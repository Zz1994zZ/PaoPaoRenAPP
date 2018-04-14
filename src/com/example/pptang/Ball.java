package com.example.pptang;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;

/**
 * 
 * @author zz
 * 
 */
public class Ball {
	final int x, y, power;
	Bitmap imgnow;
	int state = 0;
	int ul, dl, ll, rl, m1, m2;// �������ұ�ը��Χ��m1��ֱ��ͼ������m2ˮƽ��ͼ����
	List<Ball> b;
	Graphics g;
	Man owner;
	Bitmap[] ball;
	Map m;

	public Ball(Man m) {
		this.x = m.gx;
		this.y = m.gy;
		this.power = m.getPower();
		this.b = m.allBall;
		this.m = m.p;
		this.owner = m;
		m1 = this.m.m1;
		m2 = this.m.m2;
		ball = owner.myPanel.RR.ball;
		imgnow = ball[0];
		for (Man mm : m.myPanel.people) {// ����Ƿ��н�ɫ�ڸ�������
			if ((Math.abs(x * this.m.size - (mm.getX() + 13)) < this.m.size)
					&& (Math.abs(y * this.m.size - (mm.getY() + 20)) < this.m.size)) {
				mm.rx = x;
				mm.ry = y;
				// System.out.println("������m~");
				mm.stuckList.add(new int[] { x, y });
			}// ���øý�ɫ�����������
		}
		this.m.map[y][x] = -1;// ���õ�������Ϊ-1
		t.start();
	}

	public Ball(int x, int y, int p, List<Ball> b, Man m) {
		this.x = x;
		this.y = y;
		this.power = p;
		this.b = b;
		this.m = m.p;
		this.owner = m;
		m1 = this.m.m1;
		m2 = this.m.m2;
		;
		ball = owner.myPanel.RR.ball;
	}

	static Ball setBall(Man m) {
		if (m.myPanel.map.map[m.gy][m.gx] == -1)// ɨ�����λ�����Ƿ���������
			return null;
		return new Ball(m);
	}

	public List<Ball> bomb() {
		if (state == 1)// �������ڱ�ը״̬��ֱ�ӷ��ز��ظ���ը
			return null;
		// new MusicPlayer("voice/34312.mid");//ˮ�ݱ�ը��Ч
		// owner.mp.play();
		m.map[y][x] = 0;
		state = 1;// ����״̬Ϊ���ڱ�ը
		int destoryBox[][] = new int[4][2];
		int destoryNum = 0;
		for (int i = x + 1; i <= x + power; i++) {// ����ɨ�豬ը��Χ
			if (i >= 0 && i < m2 && y >= 0 && y < m1 && m.map[y][i] == 2) {// m.map[y][i]=0;
				destoryBox[destoryNum][0] = y;
				destoryBox[destoryNum][1] = i;
				destoryNum++;
				rl++;
				break;
			} else if (i >= 0 && i < m2 && y >= 0 && y < m1
					&& (m.map[y][i] == 0 || m.map[y][i] == -1)) {
				rl++;
			} else
				break;
		}
		for (int i = x - 1; i >= x - power; i--) {// ����ɨ�豬ը��Χ
			if (i >= 0 && i < m2 && y >= 0 && y < m1 && m.map[y][i] == 2) {// m.map[y][i]=0;
				destoryBox[destoryNum][0] = y;
				destoryBox[destoryNum][1] = i;
				destoryNum++;
				ll++;
				break;
			} else if (i >= 0 && i < m2 && y >= 0 && y < m1
					&& (m.map[y][i] == 0 || m.map[y][i] == -1)) {
				ll++;
			} else
				break;
		}
		for (int i = y + 1; i <= y + power; i++) {// ����ɨ�豬ը��Χ
			if (x >= 0 && i >= 0 && x < m2 && i < m1 && m.map[i][x] == 2) {// m.map[i][x]=0;
				destoryBox[destoryNum][0] = i;
				destoryBox[destoryNum][1] = x;
				destoryNum++;
				dl++;
				break;
			} else if (x >= 0 && i >= 0 && x < m2 && i < m1
					&& (m.map[i][x] == 0 || m.map[i][x] == -1)) {
				dl++;
			} else
				break;
		}
		for (int i = y - 1; i >= y - power; i--) {// ����ɨ�豬ը��Χ
			if (x >= 0 && i >= 0 && x < m2 && i < m1 && m.map[i][x] == 2) {// m.map[i][x]=0;
				destoryBox[destoryNum][0] = i;
				destoryBox[destoryNum][1] = x;
				destoryNum++;
				ul++;
				break;
			} else if (x >= 0 && i >= 0 && x < m2 && i < m1
					&& (m.map[i][x] == 0 || m.map[i][x] == -1)) {
				ul++;
			} else
				break;
		}
		final List<Ball> delList = new ArrayList<Ball>();// ���������������б�
		for (Man mmm : m.people) {// ɨ���ɫ�Ƿ�ը��
			if (!mmm.isDead
					&& ((mmm.gx >= x - ll && mmm.gx <= x + rl && mmm.gy == y) || (mmm.gy >= y
							- ul
							&& mmm.gy <= y + dl && mmm.gx == x)))
				mmm.beAttack();
		}
		List<Ball> cb = new ArrayList<Ball>();
		cb.addAll(b);
		for (Ball bbb : cb) {// ɨ�豻���������ݲ�����
			if (bbb != this
					&& ((bbb.x >= x - ll && bbb.x <= x + rl && bbb.y == y) || (bbb.y >= y
							- ul
							&& bbb.y <= y + dl && bbb.x == x))
					&& bbb.state != 1) {
				delList.addAll(bbb.bomb());
				delList.add(bbb);
			}
		}
		for (int i = 0; i < destoryNum; i++) {// ����ը������שǽ����Ϊ0״̬
			m.beBomb(destoryBox[i][0], destoryBox[i][1]);
		}
		delList.add(this);// �������ݼ���ɾ���б�
		owner.ball.remove(Ball.this);// ɾ����ɫ�����б��б�����
		return delList;
	}

	Thread t = new Thread(new Runnable() {// ����������ը�߳�
				@Override
				public void run() {
					// TODO Auto-generated method stub

					for (int j = 0; j < 3 && state == 0; j++) {
						for (int i = 0; i < 4 && state == 0; i++) {
							try {
								Thread.sleep(250);
								imgnow = ball[i];
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					List<Ball> a = bomb();// ��ը
					// System.out.println("��������"+a);
					if (state == 1)// ��ʾ��ըЧ��״̬
					{
						try {
							for (int i = 0; i < 5; i++) {
								for (Man mmm : m.people) {// ɨ���ɫ�Ƿ�ը��
									if (!mmm.isDead
											&& ((mmm.gx >= x - ll
													&& mmm.gx <= x + rl && mmm.gy == y) || (mmm.gy >= y
													- ul
													&& mmm.gy <= y + dl && mmm.gx == x)))
										mmm.beAttack();
								}
								Thread.sleep(100);
							}

						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (a != null && !a.isEmpty()) {
							m.map[y][x] = 0;
							owner.ball.removeAll(a);// �ڽ�ɫ�������б���ɾ����ը������
							b.removeAll(a); // �������ݱ���ɾ����ը������
						}
					}

				}
			});

}
