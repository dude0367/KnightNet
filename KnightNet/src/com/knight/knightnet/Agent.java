package com.knight.knightnet;

public class Agent {
	private Genome genome;
	protected double x, y;
	protected double fitness;
	protected Population population;
	
	public Agent() {
		
	}
	
	public Agent(Population pop) {
		setPopulation(pop);
	}
	
	public Agent(Genome g, Population pop) {
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
	
	public void changeFitness(double delta) {
		fitness += delta;
		if(fitness > getPopulation().getFittest()) {
			getPopulation().setFittest(fitness);
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

}
