package com.knight.knightnet;

import java.util.ArrayList;
import java.util.Random;

public class Population {

	public ArrayList<Agent> population;// = new ArrayList<Agent>();
	private double fittest = 1;

	public Population(ArrayList<Agent> agents) {
		population = agents;
	}
	
	public Population() {
		
	}

	public void evolve() {
		fittest = 1;
		ArrayList<Agent> nextGen = new ArrayList<Agent>();
		for(int i = 0; i < population.size() / 2; i++) {
			Agent agent1 = weightedRandom(population);
			//population.remove(agent1);
			Agent agent2 = agent1;//weightedRandom(population);
			while(agent2 == agent1) agent2 = weightedRandom(population);
			//population.remove(agent2);
			Genome[] babyGenomes = Genome.crossover(agent1.getGenome(), agent2.getGenome());
			Agent baby1 = new Agent(babyGenomes[0], this);
			Agent baby2 = new Agent(babyGenomes[1], this);
			if(agent1.getGenome() == babyGenomes[0]) {
				baby1.fitness = agent1.fitness;
				baby2.fitness = agent2.fitness;
			}
			nextGen.add(baby1);
			nextGen.add(baby2);
		}
		population.clear();
		population = nextGen;
	}
	
	public void cleanse() {
		ArrayList<Agent> kill = new ArrayList<Agent>();
		ArrayList<Agent> replacements = new ArrayList<Agent>();
		for(Agent a : population) {
			if(a.getFitness() > 1) continue;
			Genome g = a.getGenome();
			replacements.add(new Agent(new Genome(g.inputNeurons, g.outputNeurons, g.hiddenLayers, g.neuronsPerLayer), this));
			//population.remove(a);
			kill.add(a);
		}
		population.removeAll(kill);
		population.addAll(replacements);
	}

	public Agent weightedRandom(ArrayList<Agent> pop) {
		double sum = 0;
		for(Agent a : population) {
			sum += a.fitness;
		}
		double rand = new Random().nextDouble() * sum;
		for(Agent t : pop) {
			Agent a = (Agent) t;
			rand -= a.fitness;
			if(rand <= 0) {
				return a;
			}
		}
		return null;
	}
	
	public void setAgents(ArrayList<Agent> agents) {
		population = agents;
	}

	public double getFittest() {
		return fittest;
	}

	public void setFittest(double fittest) {
		this.fittest = fittest;
	}

}
