package com.knight.knightnet.shootout;

import java.util.ArrayList;

import com.knight.knightnet.gamecore.Agent;
import com.knight.knightnet.network.Population;

public class ShootGame {
	
	private Tank[] players = new Tank[2];
	private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	
	public ShootGame(Agent a, Agent b, Population pop) {
		getPlayers()[0] = new Tank(a, pop);
		getPlayers()[0].setX(50);
		getPlayers()[0].setY(Shootout.shootout.getHeight()/2);
		getPlayers()[1] = new Tank(b, pop);
		getPlayers()[1].setX(Shootout.shootout.getWidth() - 50);
		getPlayers()[1].setY(Shootout.shootout.getHeight()/2);
		getPlayers()[1].setDirection(Math.PI);
	}
	
	public void tick(long delta) {
		//players[0].changeDirection(.01);
		for(int i = 0; i < 2; i++) {
			int o = 0;
			if(o == i) o++;
			Tank t = players[i];
			Tank tt = players[o];
			double xVec = t.getX() - tt.getX();
			double yVec = t.getY() - tt.getY();
			double dist = Math.sqrt(Math.pow(xVec, 2) + Math.pow(yVec, 2));
			xVec /= dist;
			yVec /= dist;
			dist = 0;
			double bxVec = 0;
			double byVec = 0;
			for(Bullet b : bullets) {
				if(b.getShooter() == t) continue;
				if(dist == 0 || Math.sqrt(Math.pow(b.getX() - t.getX(), 2) + Math.pow(b.getY() - t.getY(), 2)) < dist) {
					bxVec = b.getX() - t.getX();
					byVec = b.getY() - t.getY();
					
				}
			}
			double output[] = players[i].getGenome().getNetwork().process(new double[] {
					xVec, yVec, Math.cos(t.getDirection()), Math.sin(t.getDirection()), bxVec, byVec
			});
			t.changeDirection(output[0] - .5);
			output[1] -= .5;
			output[1] *= 2;
			t.setX(t.getX() + Math.cos(t.getDirection()) * output[1]);
			t.setY(t.getY() + Math.sin(t.getDirection()) * output[1]);
			if(output[2] > .75) {
				bullets.add(t.fire());
			}
		}
		
		Tank t = players[0];//Shooter
		Tank ot = players[1];//Not shooter
		ArrayList<Bullet> toRemove = new ArrayList<Bullet>();
		for(Bullet b : getBullets()) {
			if(b.getShooter() == t && t == players[0]) {
				t = players[1];
				ot = players[0];
			} else/* if(b.getShooter() == t && t == players[1])*/ {
				t = players[0];
				ot = players[1];
			}
			b.setX(b.getX() + Math.cos(b.getAngle()) * b.getSpeed());
			b.setY(b.getY() + Math.sin(b.getAngle()) * b.getSpeed());
			if(b.getX() < 0) {
				toRemove.add(b);
			} else if(b.getX() > Shootout.shootout.getWidth()){
				toRemove.add(b);
			}else if(b.getY() < 30){
				toRemove.add(b);
			}else if(b.getY() > Shootout.shootout.getHeight()){
				toRemove.add(b);
			} else {
				double dist = Math.sqrt(Math.pow(b.getX() - t.getX(), 2) + Math.pow(b.getY() - t.getY(), 2));
				if(t != b.getShooter() && dist <= t.getColliderDist()) {
					toRemove.add(b);
					t.changeFitness(-1);
					ot.changeFitness(5);
				}
			}
		}
		bullets.removeAll(toRemove);
	}

	public Tank[] getPlayers() {
		return players;
	}

	public void setPlayers(Tank[] players) {
		this.players = players;
	}

	public ArrayList<Bullet> getBullets() {
		return bullets;
	}

	public void setBullets(ArrayList<Bullet> bullets) {
		this.bullets = bullets;
	}
}
