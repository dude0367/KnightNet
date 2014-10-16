package com.knight.knightnet.joust;

import java.util.ArrayList;

import javax.swing.JFrame;

public class TestGameJoust extends JFrame implements Runnable {

	
	private static final long serialVersionUID = 1L;
	public static TestGameJoust game;
	public static Thread thread;
	public static boolean running = true;
	
	public ArrayList<Jouster> jousters = new ArrayList<Jouster>();
	
	public void startgame() {
		game = this;
		game.setSize(800, 600);
		game.setTitle("KnightNet ANN Joust");
		game.setVisible(true);
		thread = new Thread(game);
		thread.start();
	}
	
	
	public void run() {
		while(running) {
			tick();
			draw();
		}
	}
	
	void tick() {
		
	}
	
	void draw() {
		
	}

}
