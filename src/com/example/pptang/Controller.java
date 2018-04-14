package com.example.pptang;

import java.net.DatagramPacket;

public abstract class Controller {
	public abstract void recive(int m, DatagramPacket dp);
	public abstract void recive(String m, DatagramPacket dp);
}
