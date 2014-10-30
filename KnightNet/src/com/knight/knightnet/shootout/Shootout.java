package com.knight.knightnet.shootout;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;

import com.knight.input.InputHandler;
import com.knight.knightnet.gamecore.Agent;
import com.knight.knightnet.gamecore.Genome;
import com.knight.knightnet.network.Population;

public class Shootout extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;
	private boolean speedmode = false;
	public static Shootout shootout;
	public static Thread thread;
	BufferedImage backbuffer;
	static int FPS = 30;
	int framesThisSecond = 0;
	int lastFPS = 0;
	int inputDelay = 0;
	InputHandler input;
	boolean running = true;
	
	ArrayList<ShootGame> games = new ArrayList<ShootGame>();
	ArrayList<Agent> agents = new ArrayList<Agent>();
	Population pop;
	ShootGame spectating;
	int spectatingIndex = 0;
	double cornerAngle = 0;
	public int ticks = 0;
	int generations = 1;
	
	public void startgame() {
		this.setSize(800,600);
		this.setVisible(true);
		this.setTitle("AI Shootout: 0");
		thread = new Thread(this);
		input = new InputHandler(this);
		thread.start();
		shootout = this;
	}

	@Override
	public void run() {
		pop = new Population();
		for(int i = 0; i < 50; i++) {
			Agent a = new Agent(pop);
			a.setGenome(new Genome(/*6*/5, 3, 3, 5));
			Agent b = new Agent(pop);
			b.setGenome(new Genome(5, 3, 3, 5));
			agents.add(a);
			agents.add(b);
			games.add(new ShootGame(a,b, pop));
		}
		spectating = games.get(0);
		pop.setAgents(agents);

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
		this.setTitle("AI Shootout " + lastFPS + " FPS, APEX: " + pop.getFittest() + " (" + spectatingIndex + ")");
	}

	private void tick(long delta) {
		if(delta == 0) delta = 1;
		ticks++;
		if(ticks > 5000) {
			System.out.print("EVOLVING (CURRENTLY " + generations + " GENERATION), ");
			System.out.println("PEAK FITNESS: " + pop.getFittest());
			generations++;
			if(generations % 5 == 0) {
				System.out.println("CLEANSING WEAK");
				pop.cleanse();
			}
			pop.evolve();
			int size = games.size();
			games.clear();
			for(int i = 0; i < size * 2; i+=2) {
				games.add(new ShootGame(new Tank(pop.population.get(i), pop),
						new Tank(pop.population.get(i + 1), pop), pop));
			}
			/*for(ShootGame g : games) {
				g.setPlayers(new Tank[] {
						new Tank(pop.population.get(i), pop),
						new Tank(pop.population.get(i + 1), pop)
				});
				i += 2;
			}*/
			ticks = 0;
			spectating = games.get(spectatingIndex);
		}
		
		inputDelay++;
		for(ShootGame g : games) {
			g.tick(delta);
		}
		
		if(input.getKey(KeyEvent.VK_RIGHT) && inputDelay > 20) {
			spectatingIndex++;
			if(spectatingIndex >= games.size()) spectatingIndex = 0;
			spectating = games.get(spectatingIndex);
			inputDelay = 0;
			writeTitle();
		}
		if(input.getKey(KeyEvent.VK_LEFT) && inputDelay > 20) {
			spectatingIndex--;
			if(spectatingIndex < 0) spectatingIndex = games.size() - 1;
			spectating = games.get(spectatingIndex);
			inputDelay = 0;
			writeTitle();
		}
		if(ticks > 20 && !speedmode && input.getKey(KeyEvent.VK_ENTER)) {
			speedmode = true;
			ticks = 0;
		}
		if(speedmode && input.getKey(KeyEvent.VK_BACK_SLASH)) {
			speedmode = false;
			ticks = 0;
		}
	}
	
	private void draw() {
		if(backbuffer == null) backbuffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		if(backbuffer.getWidth() != getWidth() || backbuffer.getHeight() != getHeight()) backbuffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics g = getGraphics();
		Graphics bbg = backbuffer.getGraphics();
		bbg.clearRect(0, 0, getWidth(), getHeight());
		
		bbg.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight());
		drawTank(spectating.getPlayers()[0], bbg);
		drawTank(spectating.getPlayers()[1], bbg);
		
		for(Bullet b : spectating.getBullets()) {
			bbg.fillOval((int)(b.getX()-2.5), (int)(b.getY()-2.5), 5, 5);
		}

		Point pos = this.getMousePosition();
		int x = 0;
		int width = 30;
		int height = 10;
		for(int i = 0; i < games.size(); i++) {
			double apex = games.get(i).getLocalApex();
			int y = i * 11 + 30;
			boolean selected = false;
			if(pos != null) {
				selected = pos.x < x + width && pos.y < y + height && pos.x > x && pos.y > y;
			}
			if(selected) {
				bbg.setColor(Color.red);
				spectatingIndex = i;
				spectating = this.games.get(i);
			}
			bbg.drawString( i + ": " + apex, 5, y + 11);
			if(selected) bbg.setColor(Color.white);
		}
		
		g.drawImage(backbuffer, 0, 0, this);
	}
	
	private void drawTank(Tank t, Graphics g) {
		int x = (int) t.getX();
		int y = (int) t.getY();
		int xShift = Tank.width / 2;
		int yShift = Tank.height /2;
		double angle = t.getDirection();
		if(cornerAngle == 0) cornerAngle = Math.atan(Tank.height / Tank.width);
		Point a = new Point(), b = new Point(), c = new Point(), d = new Point(), e = new Point();
		Point f = new Point(), gg = new Point();
		a.setLocation(x + xShift * Math.cos(cornerAngle + angle), y + yShift * Math.sin(cornerAngle + angle));
		d.setLocation(x + xShift * Math.cos(cornerAngle + angle + Math.PI/2), y + yShift * Math.sin(cornerAngle + angle + Math.PI/2));
		c.setLocation(x + xShift * Math.cos(cornerAngle + angle + Math.PI), y + yShift * Math.sin(cornerAngle + angle + Math.PI));
		b.setLocation(x + xShift * Math.cos(cornerAngle + angle + 3*(Math.PI/2)), y + yShift * Math.sin(cornerAngle + angle + (Math.PI/2) * 3));
		e.setLocation(x + Tank.barrelLength * Math.cos(angle), y + Tank.barrelLength * Math.sin(angle));
		
		//FOV
		f.setLocation(x + 1000 * Math.cos(angle + t.getFOV() / 2), y + 1000 * Math.sin(angle + t.getFOV() / 2));
		gg.setLocation(x + 1000 * Math.cos(angle - t.getFOV() / 2), y + 1000 * Math.sin(angle - t.getFOV() / 2));
		
		g.setColor(Color.GRAY);
		g.drawLine(x, y, f.x, f.y);
		g.drawLine(x, y, gg.x, gg.y);
		g.setColor(Color.WHITE);
		
		g.drawLine(a.x, a.y, b.x, b.y);
		g.drawLine(b.x, b.y, c.x, c.y);
		g.drawLine(c.x, c.y, d.x, d.y);
		g.drawLine(d.x, d.y, a.x, a.y);
		g.drawLine(x, y, e.x, e.y);
		g.drawString(t.getFitness() + "", x - 5, y - 5);
	}

}
