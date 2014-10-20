package com.knight.knightnet;

import java.util.ArrayList;
import java.util.Random;

public class Genome {
	
	protected int hiddenLayers = 1;
	protected int neuronsPerLayer = 3;
	protected int outputNeurons;
	protected int inputNeurons;
	protected Network network;
	protected double mutationRate = 0.001;
	protected double crossOverRate = .7;
	protected ArrayList<Double> weights = new ArrayList<Double>();
	
	public Genome(int inputNeurons, int outputNeurons) {
		this.outputNeurons = outputNeurons;
		this.inputNeurons = inputNeurons;
		network = new Network(hiddenLayers,inputNeurons, outputNeurons, neuronsPerLayer);
	}
	
	public Genome(ArrayList<Double> weight, int inputNeurons, int outputNeurons) {
		this(inputNeurons, outputNeurons);
		network.populate(weight);
	}
	
	public static Genome[] crossover(Genome g1, Genome g2) {
		Genome[] out = new Genome[2];
		if(Math.random() < g1.crossOverRate) {
			int toSwitch = new Random().nextInt(g1.weights.size());
			ArrayList<Double> g1weights = new ArrayList<Double>();
			ArrayList<Double> g2weights = new ArrayList<Double>();
			for(int i = 0; i < toSwitch; i++) {
				g1weights.add(g2.weights.get(i));
				g2weights.add(g1.weights.get(i));
			}
			for(int i = toSwitch; i < g1.weights.size(); i++) {
				g1weights.add(g1.weights.get(i));
				g2weights.add(g2.weights.get(i));
			}
			out[0] = new Genome(g1weights, g1.inputNeurons, g1.outputNeurons);
			out[1] = new Genome(g2weights, g1.inputNeurons, g1.outputNeurons);
		} else {
			out[0] = g1;
			out[1] = g2;
		}
		return out;
	}
	
	public void findWeights() {
		ArrayList<Neuron> neurons = network.getNeurons();
		for(Neuron n : neurons) weights.addAll(n.weights.values());
	}
	
	public Network getNetwork() {
		return network;
	}

}
