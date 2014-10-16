package com.knight.knightnet;

import java.util.ArrayList;
import java.util.Random;

public class Genome {
	
	int hiddenLayers = 1;
	int neuronsPerLayer = 3;
	int outputNeurons;
	int inputNeurons;
	Network network;
	double mutationRate = 0.001;
	double crossOverRate = .7;
	ArrayList<Double> weights = new ArrayList<Double>();
	
	public Genome(int inputNeurons, int outputNeurons) {
		this.outputNeurons = outputNeurons;
		this.inputNeurons = inputNeurons;
		network = new Network(hiddenLayers,inputNeurons, outputNeurons, neuronsPerLayer);
	}
	
	public Genome(Genome g1, Genome g2) {
		if(Math.random() < crossOverRate) {
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
			//Make from crossed over
		} else {
			//TODO: CHANGE THIS SO IT MAKES 2 BABBIES
		}
	}
	
	public void findWeights() {
		ArrayList<Neuron> neurons = getNeurons();
		for(Neuron n : neurons) weights.addAll(n.weights.values());
	}
	
	public ArrayList<Neuron> getNeurons() {
		ArrayList<Neuron> out = new ArrayList<Neuron>();
		out.addAll(getNeurons(network.getFirstLayer()));
		return out;
	}
	
	public ArrayList<Neuron> getNeurons(Layer l) {
		ArrayList<Neuron> out = new ArrayList<Neuron>();
		out.addAll(l.getNeurons());
		/*for(Neuron n : l.getNeurons()) {
			out.addAll(n.weights.keySet());
		}*/
		if(l.getNext() != null) {
			out.addAll(getNeurons(l.getNext()));
		}
		return out;
	}

}
