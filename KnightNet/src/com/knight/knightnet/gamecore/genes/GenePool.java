package com.knight.knightnet.gamecore.genes;

import java.util.ArrayList;
import java.util.Random;

import com.knight.knightnet.gamecore.Agent;
import com.knight.knightnet.gamecore.Genome;
import com.knight.knightnet.network.Population;

public class GenePool extends Population {
	
	public ArrayList<AgentGenome> population = new ArrayList<AgentGenome>();
	
	@Override
	public void evolve() {
		fittest = 1;
		ArrayList<AgentGenome> nextGen = new ArrayList<AgentGenome>();
		for(int i = 0; i < population.size(); i++) {
			AgentGenome agent1 = weightRand(population);
			AgentGenome agent2 = agent1;//weightedRandom(population);
			while(agent2 == agent1) agent2 = weightRand(population);
			GenomeCode babyGenome = GenomeCode.crossover(agent1.getGenome(), agent2.getGenome());
			AgentGenome baby = new AgentGenome(babyGenome, this);
			nextGen.add(baby);
		}
		population.clear();
		population = nextGen;
	}
	
	private AgentGenome weightRand(ArrayList<AgentGenome> pop) {
		double sum = 0;
		for(AgentGenome a : population) {
			if(a.getFitness() < 0) continue;
			sum += a.getFitness();
		}
		double rand = new Random().nextDouble() * sum;
		for(AgentGenome a : pop) {
			if(a.getFitness() < 0) continue;
			rand -= a.getFitness();
			if(rand <= 0) {
				return a;
			}
		}
		return null;
	}
	
	public void cleanse() {
		ArrayList<AgentGenome> kill = new ArrayList<AgentGenome>();
		ArrayList<AgentGenome> replacements = new ArrayList<AgentGenome>();
		for(AgentGenome a : population) {
			if(a.getFitness() > 1) continue;
			GenomeCode g = a.getGenome();
			replacements.add(new AgentGenome(new GenomeCode(g.getInputNeurons(), g.getOutputNeurons()), this));
			//population.remove(a);
			kill.add(a);
		}
		population.removeAll(kill);
		population.addAll(replacements);
	}

}
