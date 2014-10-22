package com.knight.knightnet.sotf;

import javax.swing.JFrame;

public class sotf extends JFrame implements Runnable {
	private static final long serialVersionUID = 1L;
	public static sotf SOTF;
	public static Thread thread;
	static int FPS = 60;
	boolean running = true;
	

	public sotf() {
		System.out.println("Starting Survival of the Fittest!");
		this.setSize(800,600);
		this.setVisible(true);
		this.setTitle("Survival of the Fittest");
		thread = new Thread(this);
		thread.start();
		SOTF = this;
	}

	@Override
	public void run() {
		long lastTime = System.currentTimeMillis();
		long delta = System.currentTimeMillis() - lastTime;
		long time = System.currentTimeMillis();
		
		while(running) {
			delta = System.currentTimeMillis() - lastTime;
			tick(delta);
			draw();
			time = (long) ((1000 / FPS) - (System.currentTimeMillis() - time));
			lastTime = System.currentTimeMillis();
			if (time > 0) { 
				try {
					Thread.sleep(time); 
				} 
				catch(Exception e){} 
			}
		}
	}
	
	private static void tick(long delta) {
		
	}
	
	private static void draw() {
		
	}

}
