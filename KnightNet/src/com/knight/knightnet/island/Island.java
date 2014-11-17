package com.knight.knightnet.island;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;

import com.knight.input.InputHandler;
import com.knight.knightnet.gamecore.genes.AgentGenome;
import com.knight.knightnet.gamecore.genes.GenePool;
import com.knight.knightnet.gamecore.genes.GenomeCode;
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
	int generations = 1;
	InputHandler input;
	boolean running = true;
	
	GenePool pool;
	ArrayList<Survivor> survivors = new ArrayList<Survivor>();
	ArrayList<Gas> gas = new ArrayList<Gas>();
	ArrayList<Food> food = new ArrayList<Food>();
	
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
		pool = new GenePool();
		for(int i = 0; i < 10; i++) {
			AgentGenome a = new AgentGenome(new GenomeCode(6,2), pool);
			a.setX(Math.random() * this.getWidth());
			a.setY(Math.random() * this.getHeight());
			survivors.add(new Survivor(a));
			pool.population.add(a);
		}
		
		for(int i = 0; i < 20; i++) {
			Food f = new Food();
			f.setX(Math.random() * this.getWidth());
			f.setY(Math.random() * this.getHeight());
			food.add(f);
		}
		
		for(int i = 0; i < 5; i++) {
			Gas g = new Gas();
			g.setX(Math.random() * this.getWidth());
			g.setY(Math.random() * this.getHeight());
			gas.add(g);
		}
		
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
		
		if((ticks > 20 && input.getKey(KeyEvent.VK_SPACE)) || ticks > 10000) {
			System.out.print("EVOLVING (CURRENTLY " + generations + " GENERATION), ");
			System.out.println("PEAK FITNESS: " + pool.getFittest());
			if(generations % 5 == 0) {
				System.out.println("CLEANSING WEAK");
				pool.cleanse();
			}
			//pool.setAgents();
			pool.evolve();
			survivors.clear();
			for(AgentGenome a : pool.population) {
				a.setX(Math.random() * this.getWidth());
				a.setY(Math.random() * this.getHeight());
				survivors.add(new Survivor(a));
			}
			ticks = 0;
			generations++;
		}
		
		if(ticks > 20 && !speedmode && input.getKey(KeyEvent.VK_ENTER)) {
			speedmode = true;
			ticks = 0;
		}
		if(speedmode && input.getKey(KeyEvent.VK_BACK_SLASH)) {
			speedmode = false;
			ticks = 0;
		}
		
		for(Survivor s : survivors) {
			double foodDist = 0;
			double foodVecX = 0;
			double foodVecY = 0;
			Food foodClose = null;
			for(Food f : food) {
				if(foodDist == 0 || calcDist(s.getX(), s.getY(), f.getX(), f.getY()) < foodDist) {
					foodDist = calcDist(s.getX(), s.getY(), f.getX(), f.getY());
					foodVecX = s.getX() - f.getX();
					foodVecY = s.getY() - f.getY();
					foodClose = f;
				}
			}

			double gasDist = 0;
			double gasVecX = 0;
			double gasVecY = 0;
			Gas gasClose = null;
			for(Gas g : gas) {
				if(gasDist == 0 || calcDist(s.getX(), s.getY(), g.getX(), g.getY()) < gasDist) {
					gasDist = calcDist(s.getX(), s.getY(), g.getX(), g.getY());
					gasVecX = s.getX() - g.getX();
					gasVecY = s.getY() - g.getY();
					gasClose = g;
				}
			}
			double[] output = s.getAgent().getGenome().getNetwork().process(new double[] {
					Math.cos(s.getDirection()), Math.sin(s.getDirection()), foodVecX, foodVecY, gasVecX, gasVecY//CHANGE TO PRESENCE, NOT VECTOR http://doublezoom.free.fr/programmation/AG_Exemple_Survival.php
			});
			output[0] -= .5;
			output[0] /= 4;
			s.changeDirection(output[0]);
			s.changeX(Math.cos(s.getDirection()) * output[1]);
			s.changeY(Math.sin(s.getDirection()) * output[1]);
			if(s.getX() < 0) s.setX(0);
			if(s.getX() > this.getWidth()) s.setX(this.getWidth());
			if(s.getY() < 30) s.setY(30);
			if(s.getY() > this.getHeight()) s.setY(this.getHeight());
			if(foodDist < 15) {
				s.changeFitness(5);
				foodClose.setX(Math.random() * this.getWidth());
				foodClose.setY(Math.random() * this.getHeight());
			}
			if(gasDist < 15) {
				s.changeFitness(-2.5);
			}
		}
	}
	
	private double calcDist(double d, double e, double f, double g) {
		return Math.sqrt(Math.pow(d-f, 2) + Math.pow(e-g, 2));
	}

	private void draw() {
		if(backbuffer == null) backbuffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		if(backbuffer.getWidth() != getWidth() || backbuffer.getHeight() != getHeight()) backbuffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics g = getGraphics();
		Graphics bbg = backbuffer.getGraphics();
		bbg.clearRect(0, 0, getWidth(), getHeight());
		
		for(Survivor s : survivors) {
			bbg.setColor(new Color(238, 218, 183));
			bbg.fillOval((int)s.getX()-10, (int)s.getY()-10, 20, 20);
			bbg.setColor(Color.black);
			bbg.drawLine((int)s.getX() + 0, (int)s.getY() + 0, (int)(Math.cos(s.getDirection()) * 10 + 0) + (int)s.getX(), (int)(Math.sin(s.getDirection()) * 10 + 0) + (int)s.getY());
		}
		
		bbg.setColor(new Color(50, 225, 50));
		for(Gas gg : gas) {
			bbg.fillOval((int)gg.getX() - gg.getWidth()/2, (int)gg.getY() - gg.getHeight()/2, gg.getWidth(), gg.getHeight());
		}
		
		bbg.setColor(new Color(139, 119, 108));
		for(Food f : food) {
			bbg.fillOval((int)f.getX() - f.getWidth()/2, (int)f.getY() - f.getHeight()/2, f.getWidth(), f.getHeight());
		}
		
		g.drawImage(backbuffer, 0, 0, this);
	}

}
