package com.knight.knightnet.joust;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;

import com.knight.input.InputHandler;
import com.knight.knightnet.Agent;
import com.knight.knightnet.Genome;
import com.knight.knightnet.Population;

public class TestGameJoust extends JFrame implements Runnable {
	
	private static final long serialVersionUID = 1L;
	public static TestGameJoust game;
	public static Thread thread;
	public static boolean running = true;
	BufferedImage backbuffer;
	
	public int jousterCount = 10;
	public static int hiddenLayers = 1;
	public static int neuronsPerLayer = 4;
	int jousterLength = 5;
	int lanceLength = 5;
	public int ticks = 0;
	int generations = 1;
	int FPS = 30;
	boolean speedmode = false;
	InputHandler input;
	
	public ArrayList<Jouster> jousters = new ArrayList<Jouster>();
	public ArrayList<Agent> agents = new ArrayList<Agent>();
	Population pop;
	
	public void startgame() {
		game = this;
		game.setSize(800, 600);
		game.setTitle("KnightNet ANN Joust");
		game.setVisible(true);
		//game.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		input = new InputHandler(this);
		thread = new Thread(game);
		thread.start();
	}
	
	
	public void run() {
		pop = new Population(new ArrayList<Agent>());
		for(int i = 0; i < jousterCount; i++) {
			Agent a = new Agent(new Genome(3,3, hiddenLayers, neuronsPerLayer), pop);
			a.setX(game.getWidth() * Math.random());
			a.setY(game.getHeight() * Math.random());
			Jouster j = new Jouster(a, pop);
			jousters.add(j);
			agents.add(a);
		}
		pop.setAgents(agents);

		long lastTime = System.currentTimeMillis();
		long delta = System.currentTimeMillis() - lastTime;
		long time = System.currentTimeMillis();
		
		while(running) {
			delta = System.currentTimeMillis() - lastTime;
			tick(delta);
			if(!speedmode) draw();
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
	
	void tick(long delta) {
		if((ticks > 20 && input.getKey(KeyEvent.VK_SPACE)) || ticks > 6000) {
			System.out.print("EVOLVING (CURRENTLY " + generations + " GENERATION), ");
			System.out.println("PEAK FITNESS: " + pop.getFittest());
			pop.evolve();
			generations++;
			//System.out.println("POPULATION EVOLVED");
			jousters.clear();
			for(int i = 0; i < jousterCount; i++) {
				Jouster j = new Jouster(pop.population.get(i), pop);
				j.setX(game.getWidth() * Math.random());
				j.setY(game.getHeight() * Math.random());
				jousters.add(j);
			}
			ticks = 0;
		}
		if(ticks > 20 && !speedmode && input.getKey(KeyEvent.VK_ENTER)) {
			speedmode = true;
			ticks = 0;
		}
		if(speedmode && input.getKey(KeyEvent.VK_BACK_SLASH)) {
			speedmode = false;
			ticks = 0;
		}
		for(Jouster j : jousters) {
			double x = j.getX();
			double y = j.getY();
			Jouster closest = null;
			double dist = 0;
			for(Jouster jj : jousters) {
				if(j == jj) continue;
				if(dist == 0) {
					closest = jj;
					dist = Math.sqrt(Math.pow(x - jj.getX(), 2) + Math.pow(y - jj.getY(), 2));
				} else {
					if(Math.sqrt(Math.pow(x - jj.getX(), 2) + Math.pow(y - jj.getY(), 2)) < dist) {
						closest = jj;
						dist = Math.sqrt(Math.pow(x - jj.getX(), 2) + Math.pow(y - jj.getY(), 2));
					}
				}
			}
			double[] output = j.getGenome().getNetwork().process(new double[] {
					x - closest.getX(), y - closest.getY(), j.getLanceAngle()
			});
			output[0] -= .5;
			output[1] -= .5;
			dist = Math.sqrt(Math.pow(output[0], 2) + Math.pow(output[1], 2));
			if(dist < 0.0001) dist = 1;
			output[0] /= dist;
			output[1] /= dist;
			output[2] *= Math.PI * 2;
			j.setLanceAngle(output[2]);
			double mult = 0.1 * delta + 1;
			j.setX(x + output[0] * mult);
			j.setY(y + output[1] * mult);
			if(j.getX() > this.getWidth()) {
				j.setX(0);
			}
			if(j.getX() < 0) {
				j.setX(this.getWidth());
			}
			if(j.getY() > this.getHeight()) {
				j.setY(0);
			}
			if(j.getY() < 0) {
				j.setY(this.getHeight());
			}
			double lanceVecX = lanceLength * Math.cos(j.getLanceAngle()) + j.getX();
			double lanceVecY = lanceLength * Math.sin(j.getLanceAngle()) + j.getY();
			double lanceOriginX = j.getX() + jousterLength / 2;
			double lanceOriginY = j.getY() + jousterLength / 2;
			double lanceX = lanceVecX + lanceOriginX;
			double lanceY = lanceVecY + lanceOriginY;
			Point p = new Point();
			p.setLocation(lanceX, lanceY);
			if(new Rectangle((int)closest.getX(), (int)closest.getY(), jousterLength, jousterLength).contains(p)) {
				j.changeFitness(5);
				closest.changeFitness(-2.5);
			}
		}
		ticks++;
	}
	
	void draw() {
		if(backbuffer == null) backbuffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		if(backbuffer.getWidth() != getWidth() || backbuffer.getHeight() != getHeight()) backbuffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics g = getGraphics();
		Graphics bbg = backbuffer.getGraphics();
		bbg.clearRect(0, 0, getWidth(), getHeight());
		
		for(Jouster j : jousters) {
			bbg.setColor(Color.WHITE);
			if(j.getFitness() == pop.getFittest() && pop.getFittest() != 1) {
				bbg.setColor(Color.red);
			}
			bbg.drawRect((int)j.getX(), (int)j.getY(), jousterLength, jousterLength);
			int lanceVecX = (int)(lanceLength * Math.cos(j.getLanceAngle()) + j.getX());
			int lanceVecY = (int)(lanceLength * Math.sin(j.getLanceAngle()) + j.getY());
			int lanceOriginX = (int)(j.getX() + jousterLength / 2);
			int lanceOriginY = (int)(j.getY() + jousterLength / 2);
			bbg.drawLine(lanceOriginX, lanceOriginY, lanceVecX, lanceVecY);
			
			double x = j.getX();
			double y = j.getY();
			Jouster closest = null;
			double dist = 0;
			for(Jouster jj : jousters) {
				if(j == jj) continue;
				if(dist == 0) {
					closest = jj;
					dist = Math.sqrt(Math.pow(x - jj.getX(), 2) + Math.pow(y - jj.getY(), 2));
				} else {
					if(Math.sqrt(Math.pow(x - jj.getX(), 2) + Math.pow(y - jj.getY(), 2)) < dist) {
						closest = jj;
						dist = Math.sqrt(Math.pow(x - jj.getX(), 2) + Math.pow(y - jj.getY(), 2));
					}
				}
			}
			bbg.setColor(Color.gray);
			bbg.drawLine(lanceOriginX, lanceOriginY, (int)closest.getX(), (int)closest.getY());
		}
		
		g.drawImage(backbuffer, 0, 0, this);
	}

}
