package com.knight.knightnet.pong;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;

import com.knight.input.InputHandler;
import com.knight.knightnet.Agent;
import com.knight.knightnet.Genome;
import com.knight.knightnet.Population;

public class Pong extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;
	public static Pong pong;
	public static Thread thread;
	BufferedImage backbuffer;
	static int FPS = 30;
	int inputDelay = 0;
	InputHandler input;
	boolean running = true;
	
	ArrayList<Game> games = new ArrayList<Game>();
	ArrayList<Agent> agents = new ArrayList<Agent>();
	Population pop;
	Game spectating;
	int spectatingIndex = 0;

	public void startgame() {
		this.setSize(800,600);
		this.setVisible(true);
		this.setTitle("AI Pong: 0");
		thread = new Thread(this);
		input = new InputHandler(this);
		thread.start();
		pong = this;
	}

	@Override
	public void run() {
		pop = new Population();
		for(int i = 0; i < 50; i++) {
			Agent a = new Agent(pop);
			a.setGenome(new Genome(4, 1, 4, 6));
			Agent b = new Agent(pop);
			b.setGenome(new Genome(4, 1, 4, 6));
			agents.add(a);
			agents.add(b);
			games.add(new Game(a,b, pop));
		}
		spectating = games.get(0);
		
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
	
	private void tick(long delta) {
		if(delta == 0) delta = 1;
		inputDelay++;
		for(Game g : games) {
			g.tick(delta);
		}
		
		if(input.getKey(KeyEvent.VK_RIGHT) && inputDelay > 100) {
			spectatingIndex++;
			if(spectatingIndex >= games.size()) spectatingIndex = 0;
			spectating = games.get(spectatingIndex);
			inputDelay = 0;
			this.setTitle("AI Pong: " + spectatingIndex);
		}
		if(input.getKey(KeyEvent.VK_LEFT) && inputDelay > 100) {
			spectatingIndex--;
			if(spectatingIndex < 0) spectatingIndex = games.size() - 1;
			spectating = games.get(spectatingIndex);
			inputDelay = 0;
			this.setTitle("AI Pong: " + spectatingIndex);
		}
	}
	
	private void draw() {
		if(backbuffer == null) backbuffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		if(backbuffer.getWidth() != getWidth() || backbuffer.getHeight() != getHeight()) backbuffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics g = getGraphics();
		Graphics bbg = backbuffer.getGraphics();
		bbg.clearRect(0, 0, getWidth(), getHeight());
		
		bbg.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight());
		bbg.fillRect((int)spectating.paddles[0].getX(), (int)spectating.paddles[0].getY(), Paddle.width, Paddle.height);
		bbg.fillRect((int)spectating.paddles[1].getX(), (int)spectating.paddles[1].getY(), Paddle.width, Paddle.height);
		
		bbg.fillOval((int)spectating.ball.getX()-5, (int)spectating.ball.getY()-5, 10, 10);
		
		g.drawImage(backbuffer, 0, 0, this);
	}

}
