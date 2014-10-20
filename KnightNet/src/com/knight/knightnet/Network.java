package com.knight.knightnet;
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
		out.addAll(getNeurons(getFirstLayer()));
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
