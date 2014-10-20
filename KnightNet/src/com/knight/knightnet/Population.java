package com.knight.knightnet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Population {

	public ArrayList<Agent> population;// = new ArrayList<Agent>();

	public Population(ArrayList agents) {
		population = agents;
	}

	public void evolve() {
		ArrayList<Agent> nextGen = new ArrayList<Agent>();
		while(population.size() > 0) {
			Agent agent1 = weightedRandom(population);
			population.remove(agent1);
			Agent agent2 = weightedRandom(population);
			population.remove(agent2);
			Genome[] babyGenomes = Genome.crossover(agent1.getGenome(), agent2.getGenome());
			Agent baby1 = new Agent(babyGenomes[0]);
			Agent baby2 = new Agent(babyGenomes[1]);
			nextGen.add(baby1);
			nextGen.add(baby2);
		}
		population = nextGen;
	}

	public Agent weightedRandom(ArrayList<Agent> pop) {
		double sum = 0;
		for(Agent t : population) {
			Agent a = t;
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

}
