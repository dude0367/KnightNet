package com.knight.knightnet.shootout;

import com.knight.knightnet.gamecore.Agent;
import com.knight.knightnet.network.Population;

public class Tank extends Agent {
	
	public static int width = 40;
	public static int height = 40;
	public static int barrelLength = 25;
	public static int bulletSpeed = 5;
	
	private double direction = 0;
	private double colliderDist = 0;
	
	public Tank(Agent a, Population pop) {
		super(a, pop);
	}

	public double getDirection() {
		return direction;
	}

	public void setDirection(double direction) {
		this.direction = direction;
	}
	
	public void changeDirection(double direction) {
		this.direction += direction;
	}

	public Bullet fire() {
		this.changeFitness(-.1);
		Bullet b = new Bullet();
		b.setShooter(this);
		b.setAngle(direction);
		b.setSpeed(bulletSpeed);
		b.setX(getX());
		b.setY(getY());
		return b;
	}

	public double getColliderDist() {
		if(colliderDist == 0) colliderDist = Math.sqrt(Math.pow(width, 2) + Math.pow(height, 2));
		return colliderDist;
	}

	public void setColliderDist(double colliderDist) {
		this.colliderDist = colliderDist;
	}

}
