package com.example.pptang;

/**
 * 用于生成性格迥异的AI~
 * 
 * @author zz
 * 
 */
public class AIFactory {
	public static Man getAI(int type, int x, int y, Panel p) {
		Man m = new Man(x, y, 4, p, null);
		switch (type) {
		case 1:// hunter
			m.setSpeed(4);
			m.setMax(4);
			m.setName("hunter");
			break;
		case 2:// warrior
			m.setSpeed(9);
			m.setPower(3);
			m.setMax(1);
			m.setName("warrior");
			break;
		case 3:// oracle
			m.setSpeed(8);
			m.setPower(2);
			m.setMax(4);
			m.setName("oracle");
			break;
		case 4:// zz
			m.setSpeed(4);
			m.setPower(10);
			m.setMax(10);
			m.setName("zz");
			break;

		}
		return m;
	}
}
