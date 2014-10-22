package com.knight.knightnet;

import java.util.ArrayList;
import java.util.Random;

public class Genome {
	
	protected int hiddenLayers = 1;
	protected int neuronsPerLayer = 3;
	protected int outputNeurons;
	protected int inputNeurons;
	protected Network network;
	protected static double mutationRate = 0.1;
	protected double crossOverRate = .7;
	protected ArrayList<Double> weights = new ArrayList<Double>();
	
	public Genome(int inputNeurons, int outputNeurons, int hiddenLayers, int neuronsPerLayer) {
		this.outputNeurons = outputNeurons;
		this.inputNeurons = inputNeurons;
		this.hiddenLayers = hiddenLayers;
		this.neuronsPerLayer = neuronsPerLayer;
		network = new Network(hiddenLayers,inputNeurons, outputNeurons, neuronsPerLayer);
	}
	
	public Genome(ArrayList<Double> weight, int inputNeurons, int outputNeurons, int hiddenLayers, int neuronsPerLayer) {
		this(inputNeurons, outputNeurons, hiddenLayers, neuronsPerLayer);
		network.populate(weight);
	}
	
	public static Genome[] crossover(Genome g1, Genome g2) {
		Genome[] out = new Genome[2];
		g1.findWeights();
		g2.findWeights();
		int g1Weights = g1.weights.size();
		int g2Weights = g2.weights.size();
		if(g1Weights != g2Weights) System.out.println("Weights have different amount (" + g1Weights + ":" + g2Weights + ")");
		if(Math.random() < g1.crossOverRate) {
			int toSwitch = new Random().nextInt(g1.weights.size() - 1);
			ArrayList<Double> g1weights = new ArrayList<Double>();
			ArrayList<Double> g2weights = new ArrayList<Double>();
			for(int i = 0; i < toSwitch; i++) {
				if(Math.random() > mutationRate) g1weights.add(g2.weights.get(i));
				else g1weights.add(g2.weights.get(i) + (Math.random() - .5) * 2);
				if(Math.random() > mutationRate) g2weights.add(g1.weights.get(i));
				else g2weights.add(g1.weights.get(i) + (Math.random() - .5) * 2);
			}
			for(int i = toSwitch; i < g1.weights.size(); i++) {
				if(Math.random() > mutationRate) g1weights.add(g1.weights.get(i));
				else g1weights.add(g1.weights.get(i) + (Math.random() - .5) * 2);
				if(Math.random() > mutationRate) g2weights.add(g2.weights.get(i));
				else g2weights.add(g2.weights.get(i) + (Math.random() - .5) * 2);
			}
			out[0] = new Genome(g1weights, g1.inputNeurons, g1.outputNeurons, g1.hiddenLayers, g1.neuronsPerLayer);
			out[1] = new Genome(g2weights, g1.inputNeurons, g1.outputNeurons, g1.hiddenLayers, g1.neuronsPerLayer);
		} else {
			out[0] = g1;
			out[1] = g2;
		}
		g1.weights.clear();
		g2.weights.clear();
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
