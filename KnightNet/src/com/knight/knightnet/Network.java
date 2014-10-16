package com.knight.knightnet;
import java.util.ArrayList;

/*(C) Copyright 2014-2015 dude0367 (Knight) & lkarinja (Karinja)
 * This program comes with absolutely no warranty.
 * This program is under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International
 * and no redistribution, commercial use, or modification can be made without contacting the program's developers.
 */
 //TEST AGAIN AGAIN AGAIN
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
	
	public void process(double[] input) {
		
	}

	public ArrayList<Layer> getLayers() {
		return layers;
	}

	public void setLayers(ArrayList<Layer> layers) {
		this.layers = layers;
	}

}
