package com.knight.knightnet;

public class Agent {
	private Genome genome;
	protected double x, y;
	protected double fitness;
	
	public Agent() {
		
	}
	
	public Agent(Genome g) {
		genome = g;
	}
	
	public Genome getGenome() {
		return genome;
	}
	public void setGenome(Genome genome) {
		this.genome = genome;
	}

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

}
