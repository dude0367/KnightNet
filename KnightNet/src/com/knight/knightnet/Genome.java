package com.knight.knightnet;

public class Genome {
	
	int hiddenLayers = 1;
	int neuronsPerLayer = 3;
	int outputNeurons;
	int inputNeurons;
	Network network;
	double mutationRate = 0.001;
	double crossOverRate = .7;
	
	public Genome(int inputNeurons, int outputNeurons) {
		this.outputNeurons = outputNeurons;
		this.inputNeurons = inputNeurons;
	}

}
