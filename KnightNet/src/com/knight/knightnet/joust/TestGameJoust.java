package com.knight.knightnet.joust;

import java.util.ArrayList;

import javax.swing.JFrame;

public class TestGameJoust extends JFrame implements Runnable {

	
	private static final long serialVersionUID = 1L;
	public static TestGameJoust game;
	public static Thread thread;
	public static boolean running = true;
	
	public int jousterCount = 10;
	
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
		for(int i = 0; i < jousterCount; i++) {
			Jouster j = new Jouster(game);
			
			jousters.add(j);
		}
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
