package com.knight.knightnet.gamecore.genes;

import com.knight.knightnet.gamecore.Agent;
import com.knight.knightnet.network.Population;

public class AgentGenome extends Agent {
	protected GenomeCode genome;
	
	public AgentGenome(GenomeCode g, Population pop) {
		super(pop);
		genome = g;
	}
	
	public AgentGenome(AgentGenome a, Population pop) {
		super(pop);
		this.setGenome(a.getGenome());
		this.setX(a.getX());
		this.setY(a.getY());
		this.setFitness(a.getFitness());
	}
	
	@Override
	public GenomeCode getGenome() {
		return genome;
	}
	public void setGenome(GenomeCode genome) {
		this.genome = genome;
	}

}
