package com.knight.knightnet.island;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import com.knight.input.InputHandler;
import com.knight.knightnet.shootout.Shootout;

public class Island extends JFrame implements Runnable {
	
	private static final long serialVersionUID = 1L;
	private boolean speedmode = false;
	public static Shootout shootout;
	public static Thread thread;
	BufferedImage backbuffer;
	static int FPS = 30;
	int framesThisSecond = 0;
	int lastFPS = 0;
	int inputDelay = 0;
	int ticks = 0;
	InputHandler input;
	boolean running = true;
	
	public Island() {
		this.setSize(800,600);
		this.setVisible(true);
		this.setTitle("AI Island");
		thread = new Thread(this);
		input = new InputHandler(this);
		thread.start();
	}

	@Override
	public void run() {
		long lastTime = System.currentTimeMillis();
		long delta = System.currentTimeMillis() - lastTime;
		long time = System.currentTimeMillis();
		long lastSecond = time / 1000;
		while(running) {
			delta = System.currentTimeMillis() - lastTime;
			tick(delta);
			if(!speedmode) draw();
			framesThisSecond++;
			time = (long) ((1000 / FPS) - (System.currentTimeMillis() - lastTime));
			lastTime = System.currentTimeMillis();
			if(lastSecond - lastTime / 1000 < 0) {
				lastSecond = lastTime / 1000;
				writeTitle();
				lastFPS = framesThisSecond;
				framesThisSecond = 0;
			}
			if (time > 0) { 
				try {
					if(!speedmode) Thread.sleep(time); 
				} 
				catch(Exception e){} 
			}
		}
	}
	
	private void writeTitle() {
		this.setTitle("AI Island " + lastFPS);// + " FPS, APEX: " + (int)pop.getFittest() + " (" + spectatingIndex + ")");
	}
	
	private void tick(long delta) { 
		if(delta == 0) delta = 1;
		ticks++;
	}
	
	private void draw() {
		if(backbuffer == null) backbuffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		if(backbuffer.getWidth() != getWidth() || backbuffer.getHeight() != getHeight()) backbuffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics g = getGraphics();
		Graphics bbg = backbuffer.getGraphics();
		bbg.clearRect(0, 0, getWidth(), getHeight());
		
		g.drawImage(backbuffer, 0, 0, this);
	}

}
