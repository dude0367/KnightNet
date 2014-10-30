package com.knight.knightnet.joust;

import com.knight.knightnet.gamecore.Agent;
import com.knight.knightnet.gamecore.Genome;
import com.knight.knightnet.network.Population;

public class Jouster extends Agent {
	
	private double lanceAngle;
	private boolean stabbing = false;
	
	public Jouster(Population pop) {
		super(pop);
		setGenome(new Genome(4, 3, TestGameJoust.hiddenLayers, TestGameJoust.neuronsPerLayer));
	}
	
	public Jouster(Agent a, Population pop) {
		super(pop);
		this.setGenome(a.getGenome());
		this.setX(a.getX());
		this.setY(a.getY());
		this.setFitness(a.getFitness());
	}
	
	public double getLanceAngle() {
		return lanceAngle;
	}

	public void setLanceAngle(double lanceAngle) {
		this.lanceAngle = lanceAngle;
	}

	public void setStabbing(boolean b) {
		stabbing = b;
	}

	public boolean isStabbing() {
		return stabbing;
	}

}
