package com.knight.knightnet.joust;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;

import com.knight.knightnet.Population;

public class TestGameJoust extends JFrame implements Runnable {

	
	private static final long serialVersionUID = 1L;
	public static TestGameJoust game;
	public static Thread thread;
	public static boolean running = true;
	BufferedImage backbuffer;
	
	public int jousterCount = 10;
	int jousterLength = 5;
	int lanceLength = 10;
	public int ticks = 0;
	
	public ArrayList<Jouster> jousters = new ArrayList<Jouster>();
	Population pop;
	
	public void startgame() {
		game = this;
		game.setSize(800, 600);
		game.setTitle("KnightNet ANN Joust");
		game.setVisible(true);
		//game.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		thread = new Thread(game);
		thread.start();
	}
	
	
	public void run() {
		pop = new Population(jousters);
		for(int i = 0; i < jousterCount; i++) {
			Jouster j = new Jouster(pop);
			j.setX(game.getWidth() * Math.random());
			j.setY(game.getHeight() * Math.random());
			jousters.add(j);
		}
		pop.setAgents(jousters);
		while(running) {
			tick();
			draw();
		}
	}
	
	void tick() {
		if(ticks % 10000 == 0) {
			System.out.println("EVOLVING");
			System.out.println("PEAK FITNESS: " + pop.getFittest());
			pop.evolve();
			jousters.clear();
			for(int i = 0; i < jousterCount; i++) {
				Jouster j = new Jouster(pop.population.get(i), pop);
				j.setX(game.getWidth() * Math.random());
				j.setY(game.getHeight() * Math.random());
				jousters.add(j);
			}
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
			dist = Math.sqrt(Math.pow(output[0], 2) + Math.pow(output[1], 2));
			output[0] /= dist;
			output[1] /= dist;
			output[0] -= .5;
			output[1] -= .5;
			output[2] *= Math.PI * 2;
			j.setLanceAngle(output[2]);
			j.setX(x + output[0]);
			j.setY(y + output[1]);
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
				j.changeFitness(.2);
				closest.changeFitness(-.1);
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
			if(j.getFitness() == pop.getFittest() && pop.getFittest() != 0) {
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
