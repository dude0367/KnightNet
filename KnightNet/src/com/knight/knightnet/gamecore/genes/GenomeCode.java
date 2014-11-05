package com.knight.knightnet.gamecore.genes;

import java.util.ArrayList;
import java.util.Random;

import com.knight.knightnet.gamecore.Genome;
import com.knight.knightnet.network.Network;

public class GenomeCode extends Genome {
	
	private int hiddenLayers = 1;
	private int neuronsPerLayer = 3;
	private int outputNeurons;
	private int inputNeurons;
	protected Network network;
	protected double mutationRate = 0.1;
	protected double crossOverRate = .7;
	protected ArrayList<Double> weights = new ArrayList<Double>();
	protected int code[] = new int[8 + 16 * 16 * 16 * 16];
	//http://homepages.inf.ed.ac.uk/pkoehn/publications/gann94.pdf
	/* Code Plan (Binary):
	 * First 4: # of layers (+1 (can't be 0))
	 * Second 4: # of neurons per layer (+1 (can't be 0))
	 * (This means that there can be up to 16^2 neurons)
	 * Each block of 16: Weights
	 * 	1: + or -
	 * 	5: Ones
	 * 	10: Decimal
	 * 
	 * (This means that there must be 8 + 16^3 digits in the code) 
	 */
	
	public GenomeCode(int inputNeurons, int outputNeurons) {
		super(inputNeurons, outputNeurons);
		Random rand = new Random();
		int hiddenLayers = rand.nextInt(15) + 1;
		int neuronsPerLayer = rand.nextInt(15) + 1;
		this.setHiddenLayers(hiddenLayers);
		this.setNeuronsPerLayer(neuronsPerLayer);
		
		String layers = ConvertToBinary(hiddenLayers);
		String neurons = ConvertToBinary(neuronsPerLayer);
		for(int i = 0; i < 4; i++) {
			if(layers.length() > 3 - i) {
				code[i] = Integer.valueOf(layers.substring(i - (4 - layers.length()), i+1-(4 - layers.length())));
			}
			if(neurons.length() > 3 - i) {
				code[i+4] = Integer.valueOf(neurons.substring(i- (4 - neurons.length()), i+1-(4 - neurons.length())));
			}
		}
		for(int i = 8; i < 8 + 16 * 16 * 16 * 16; i++) {
			code[i] = Math.random() < .5 ? 1 : 0;
		}
		network = new Network(hiddenLayers,inputNeurons, outputNeurons, neuronsPerLayer);
		network.fillWeights(code);
		System.out.println("Created");
	}
	
	public GenomeCode(int inputNeurons, int outputNeurons, int code[]) {
		super(inputNeurons, outputNeurons);
		int hiddenLayers = 0;
		int neuronsPerLayer = 0;
		int mult = 1;
		for(int i = 3; i >=0; i--) {
			hiddenLayers += mult * code[i];
			neuronsPerLayer += mult * code[i + 4];
			mult *= 2;
		}
		this.setHiddenLayers(hiddenLayers);
		this.setNeuronsPerLayer(neuronsPerLayer);
		this.code = code;
		network = new Network(hiddenLayers,inputNeurons, outputNeurons, neuronsPerLayer);
		network.fillWeights(code);
	}
	
	public static GenomeCode crossover(GenomeCode g1, GenomeCode g2) {
		//GenomeCode[] out = new GenomeCode[2];
		GenomeCode out = null;
		int code[] = new int[g1.code.length];
		for(int i = 0; i < g1.code.length; i++) {
			if(Math.random() < .5) {
				if(Math.random() > g1.mutationRate) {
					code[i] = g1.code[i];
				} else {
					code[i] = (int) (g1.code[i] + (Math.random() - .5) * 4);
				}
			} else {
				if(Math.random() > g1.mutationRate) {
					code[i] = g2.code[i];
				} else {
					code[i] = (int) (g2.code[i] + (Math.random() - .5) * 4);
				}
			}
		}
		out = new GenomeCode(g1.inputNeurons, g1.outputNeurons, code);
		return out;
	}
	
	public String ConvertToBinary(int d){
		String out = "";
		while(d > 0) {
			out = ((int)(d % 2)) + out;
			d /= 2;
		}
		return out;
	}
	
	public Network getNetwork() {
		return network;
	}

	public int getInputNeurons() {
		return inputNeurons;
	}

	public void setInputNeurons(int inputNeurons) {
		this.inputNeurons = inputNeurons;
	}

	public int getOutputNeurons() {
		return outputNeurons;
	}

	public void setOutputNeurons(int outputNeurons) {
		this.outputNeurons = outputNeurons;
	}

	public int getHiddenLayers() {
		return hiddenLayers;
	}

	public void setHiddenLayers(int hiddenLayers) {
		this.hiddenLayers = hiddenLayers;
	}

	public int getNeuronsPerLayer() {
		return neuronsPerLayer;
	}

	public void setNeuronsPerLayer(int neuronsPerLayer) {
		this.neuronsPerLayer = neuronsPerLayer;
	}

}
