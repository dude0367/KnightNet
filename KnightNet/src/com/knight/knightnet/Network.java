package com.knight.knightnet;
import java.util.ArrayList;

/*(C) Copyright 2014-2015 dude0367 (Knight) & lkarinja (Karinja)
 * This program comes with absolutely no warranty.
 * This program is under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International
 * and no redistribution, commercial use, or modification can be made without contacting the program's developers.
 */
 //TEST AGAIN AGAIN
public class Network {
	
	private ArrayList<Layer> layers = new ArrayList<Layer>();
	
	public Network(int layers, int input, int output, int hiddensize) {
		Layer last = new Layer(input, null);
		getLayers().add(last);//Input layer
		for(int i = 0; i < layers; i++) {//Hidden layers
			last = new Layer(hiddensize, last);
			getLayers().add(last);
		}
		getLayers().add(new Layer(output, last));//Output layer
	}
	
	public void process(double[] input) {
		for(Neuron n : layers.get(0).getNeurons()) {
			 System.out.println("Processing and stuff");
		}
	}

	public ArrayList<Layer> getLayers() {
		return layers;
	}

	public void setLayers(ArrayList<Layer> layers) {
		this.layers = layers;
	}

}
