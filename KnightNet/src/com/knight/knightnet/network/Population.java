package com.knight.knightnet.network;

import java.util.ArrayList;
import java.util.Random;

import com.knight.knightnet.gamecore.Agent;
import com.knight.knightnet.gamecore.Genome;

public class Population {

	public ArrayList<Agent> population;// = new ArrayList<Agent>();
	protected double fittest = 1;

	public Population(ArrayList<Agent> agents) {
		population = agents;
	}
	
	public Population() {
		
	}

	public void evolve() {
		fittest = 1;
		ArrayList<Agent> nextGen = new ArrayList<Agent>();
		for(int i = 0; i < population.size(); i++) {
			Agent agent1 = weightedRandom(population);
			//population.remove(agent1);
			Agent agent2 = agent1;//weightedRandom(population);
			while(agent2 == agent1) agent2 = weightedRandom(population);
			//population.remove(agent2);
			Genome/*[]*/ babyGenomes = Genome.crossover(agent1.getGenome(), agent2.getGenome());
			Agent baby1 = new Agent(babyGenomes/*[0]*/, this);
			//Agent baby2 = new Agent(babyGenomes[1], this);
			/*if(agent1.getGenome() == babyGenomes[0]) {
				baby1.setFitness(agent1.getFitness());
				baby2.setFitness(agent2.getFitness());
			}*/
			/*if(baby1 == agent1) baby1.setFitness(agent1.getFitness());
			if(baby1 == agent2) baby1.setFitness(agent2.getFitness());*/
			nextGen.add(baby1);
			//nextGen.add(baby2);
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
			replacements.add(new Agent(new Genome(g.getInputNeurons(), g.getOutputNeurons(), g.getHiddenLayers(), g.getNeuronsPerLayer()), this));
			//population.remove(a);
			kill.add(a);
		}
		population.removeAll(kill);
		population.addAll(replacements);
	}

	public Agent weightedRandom(ArrayList<Agent> pop) {
		double sum = 0;
		for(Agent a : population) {
			sum += a.getFitness();
		}
		double rand = new Random().nextDouble() * sum;
		for(Agent t : pop) {
			Agent a = (Agent) t;
			rand -= a.getFitness();
			if(rand <= 0) {
				return a;
			}
		}
		return null;
	}
	
	public double getAverage() {
		double average = 0;
		for(Agent a : this.population) {
			average += a.getFitness();
		}
		average /= this.population.size();
		return average;
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
