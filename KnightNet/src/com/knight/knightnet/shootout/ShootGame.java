package com.knight.knightnet.shootout;

import java.util.ArrayList;

import com.knight.knightnet.gamecore.Agent;
import com.knight.knightnet.network.Population;

public class ShootGame {
	
	private Tank[] players = new Tank[2];
	private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	private double localApex;
	
	public ShootGame(Agent a, Agent b, Population pop) {
		getPlayers()[0] = new Tank(a, pop);
		getPlayers()[0].setX(50);
		getPlayers()[0].setY(Shootout.shootout.getHeight()/2);
		getPlayers()[0].setShootgame(this);
		getPlayers()[1] = new Tank(b, pop);
		getPlayers()[1].setX(Shootout.shootout.getWidth() - 50);
		getPlayers()[1].setY(Shootout.shootout.getHeight()/2);
		getPlayers()[1].setShootgame(this);
		getPlayers()[1].setDirection(Math.PI);
	}
	
	public void tick(long delta) {
		//players[0].changeDirection(.01);
		double localApex = -999999;
		for(int i = 0; i < 2; i++) {
			int o = 0;
			if(o == i) o++;
			Tank t = players[i];
			Tank tt = players[o];
			localApex = (t.getFitness() > localApex) ? t.getFitness() : localApex;
			
			if(t.getDirection() > 2 * Math.PI) t.changeDirection(-2 * Math.PI);
			if(t.getDirection() < 0) t.changeDirection(2 * Math.PI);
			
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
				double tempDist = Math.sqrt(Math.pow(b.getX() - t.getX(), 2) + Math.pow(b.getY() - t.getY(), 2));
				if(dist == 0 || tempDist < dist) {
					bxVec = b.getX() - t.getX();
					byVec = b.getY() - t.getY();
					dist = tempDist;
				}
			}
			double angleToEnemy = ((t.getX() > tt.getX() ? 1 : 0) * Math.PI) + Math.atan((t.getY() - tt.getY()) / (t.getX() - tt.getX()));
			boolean pointingAtEnemy = (t.getDirection() + t.getFOV()/2) > angleToEnemy && (t.getDirection() - t.getFOV()/2) < angleToEnemy;
			if(pointingAtEnemy) {
				//t.changeFitness(.1);
			}
			double output[] = players[i].getGenome().getNetwork().process(new double[] {
					xVec, yVec, Math.cos(t.getDirection()), 
					Math.sin(t.getDirection()), bxVec, byVec,
					pointingAtEnemy ? 10 : 0
			});
			t.changeDirection(output[0] - .5);
			output[1] -= .5;
			output[1] *= .2;
			t.setX(t.getX() + Math.cos(t.getDirection()) * output[1] * delta);
			t.setY(t.getY() + Math.sin(t.getDirection()) * output[1] * delta);
			if(t.getX() < 0) t.setX(0);
			if(t.getX() > Shootout.shootout.getWidth()) t.setX(Shootout.shootout.getWidth());
			if(t.getY() < 30) t.setY(30);
			if(t.getY() > Shootout.shootout.getHeight()) t.setY(Shootout.shootout.getHeight());
			if(output[2] > .5 && t.getCannonCooldown() <= 0) {
				bullets.add(t.fire());
				if(pointingAtEnemy) t.changeFitness(2);
				else t.changeFitness(-.4);
				t.setCannonCooldown(20);
			}
			t.coolCannon(0.1 * delta);
		}
		
		Tank t = players[0];//Shooter
		Tank ot = players[1];//Not shooter
		ArrayList<Bullet> toRemove = new ArrayList<Bullet>();
		for(Bullet b : getBullets()) {
			if(b.getShooter() == players[0]) {
				t = players[0];
				ot = players[1];
			} else {
				t = players[1];
				ot = players[0];
			}
			/*if(b.getShooter() == t && t == players[0]) {
				t = players[0];
				t = (b.getShooter()==players[0]) ? players[0] : players[1];
				ot = players[1];
			} else if(b.getShooter() == ot && t == players[1]) {
				t = players[0];
				ot = players[1];
			}*/
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
				double dist = Math.sqrt(Math.pow(b.getX() - ot.getX(), 2) + Math.pow(b.getY() - ot.getY(), 2));
				if(/*t != b.getShooter() && */dist <= ot.getColliderDist()) {
					toRemove.add(b);
					t.changeFitness(2);
					ot.changeFitness(-1);
				}
			}
		}
		bullets.removeAll(toRemove);
		setLocalApex(localApex);
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

	public double getLocalApex() {
		return localApex;
	}

	public void setLocalApex(double localApex) {
		this.localApex = localApex;
	}

	public Tank getOtherTank(Tank t) {
		return players[0] == t ? players[1] : players[0];
	}
}
