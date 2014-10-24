package com.knight.knightnet.shootout;

public class Bullet {
	
	private double x, y;
	private double angle, speed;
	private Tank shooter;

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public Tank getShooter() {
		return shooter;
	}

	public void setShooter(Tank shooter) {
		this.shooter = shooter;
	}

}
