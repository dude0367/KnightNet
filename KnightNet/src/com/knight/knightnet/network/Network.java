package com.knight.knightnet.network;
import java.util.ArrayList;

/*(C) Copyright 2014-2015 dude0367 (Knight) & lkarinja (Karinja)
 * This program comes with absolutely no warranty.
 * This program is under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International
 * and no redistribution, commercial use, or modification can be made without contacting the program's developers.
 */

public class Network {
	
	private ArrayList<Layer> layers = new ArrayList<Layer>();
	
	public Network(int layers, int input, int output, int hiddensize) {
		Layer last = new Layer(input, null, this);
		getLayers().add(last);//Input layer
		for(int i = 0; i < layers; i++) {//Hidden layers
			last = new Layer(hiddensize, last, this);
			getLayers().add(last);
		}
		getLayers().add(new Layer(output, last, this));//Output layer
	}
	
	public void populate(ArrayList<Double> weights) {
		ArrayList<Neuron> neurons = getNeurons();
		//System.out.println("Weight size: " + weights.size());
		//System.out.println("Neurons: " + neurons.size());
		for(Neuron n : neurons) {
			for(Neuron nn : n.weights.keySet()) {
				n.weights.put(nn, weights.get(0));
				weights.remove(0);
			}
		}
	}
	
	public double[] process(double[] input) {
		double[] output = getFirstLayer().feedforward(input);
		return output;
	}
	
	public Layer getFirstLayer() {
		return layers.get(0);
	}

	public ArrayList<Layer> getLayers() {
		return layers;
	}

	public void setLayers(ArrayList<Layer> layers) {
		this.layers = layers;
	}
	
	public ArrayList<Neuron> getNeurons() {
		ArrayList<Neuron> out = new ArrayList<Neuron>();
		out.addAll(getNeurons(getFirstLayer()));//There is always 9 neurons (3 - 3 - 3 network)
		return out;
	}
	
	public ArrayList<Neuron> getNeurons(Layer l) {
		ArrayList<Neuron> out = new ArrayList<Neuron>();
		out.addAll(l.getNeurons());
		if(l.getNext() != null) {
			out.addAll(getNeurons(l.getNext()));
		}
		return out;
	}

	public void fillWeights(int[] code) {
		ArrayList<Double> weights = new ArrayList<Double>();
		for(int i = 8; i < 8 + 16 * 16 * 16 * 16; i += 16) {
			double d = 0;
			int mult = 1;
			for(int o = 1; o < 6; o++) {
				d += code[i+o] * mult;
				mult *= 2;
			}
			mult = 1;
			for(int o = 6; o < 16; o++) {
				d += ((double)code[i+o]) / (double)mult;
				mult *= 2;
			}
			d *= code[i] == 1 ? 1 : -1; //Make negative or nah
			weights.add(d);
		}
		populate(weights);
	}

}
