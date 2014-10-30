package com.knight.knightnet.gamecore;

import com.knight.knightnet.network.Population;

public class Agent {
	private Genome genome;
	protected double x, y;
	private double fitness = 1;
	protected Population population;
	
	public Agent() {
		
	}
	
	public Agent(Population pop) {
		setPopulation(pop);
	}
	
	public Agent(Genome g, Population pop) {
		this(pop);
		genome = g;
	}
	
	public Agent(Agent a, Population pop) {
		this(pop);
		this.setGenome(a.getGenome());
		this.setX(a.getX());
		this.setY(a.getY());
		this.setFitness(a.getFitness());
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
	
	public void changeFitness(double delta) {
		if(getFitness() == getPopulation().getFittest()) {
			getPopulation().setFittest(getFitness() + delta);
		}
		setFitness(getFitness() + delta);
		if(getFitness() > getPopulation().getFittest()) {
			getPopulation().setFittest(getFitness());
		}
	}
	
	public double getFitness() {
		return fitness;
	}

	public Population getPopulation() {
		return population;
	}

	public void setPopulation(Population population) {
		this.population = population;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

}
