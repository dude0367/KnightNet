package com.knight.knightnet.island;

import com.knight.knightnet.gamecore.genes.AgentGenome;

public class Survivor {
	
	private AgentGenome agent;
	private double direction;
	
	public Survivor(AgentGenome a) {
		this.setAgent(a);
	}
	
	public double getX() {
		return agent.getX();
	}
	
	public double getY() {
		return agent.getY();
	}
	
	public void changeX(double change) {
		agent.setX(getX() + change);
	}
	
	public void changeY(double change) {
		agent.setY(getY() + change);
	}

	public void setX(int x) {
		this.agent.setX(x);
	}

	public void setY(int y) {
		this.agent.setY(y);
	}

	public AgentGenome getAgent() {
		return agent;
	}

	public void setAgent(AgentGenome agent) {
		this.agent = agent;
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

	public void changeFitness(double d) {
		this.agent.changeFitness(d);
	}

}
