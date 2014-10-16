package com.knight.knightnet;
import java.util.HashMap;

/*(C) Copyright 2014-2015 dude0367 (Knight) & lkarinja (Karinja)
 * This program comes with absolutely no warranty.
 * This program is under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International
 * and no redistribution, commercial use, or modification can be made without contacting the program's developers.
 */

public class Neuron {
	
	HashMap<Neuron, Double> weights = new HashMap<Neuron, Double>();
	Layer layer;
	private double curveMod = 1;
	
	public Neuron(Layer l) {
		layer = l;
		if(layer.getPrevious() != null) {
			for(Neuron n : layer.getPrevious().getNeurons()) {
				weights.put(n, (Math.random() - .5) * 2);//Random weight between -1 and 1
			}
		}
	}
	
	public double calcSigmoid(double activation) {
		return 1.0 / (1.0 + Math.pow(Math.E, (activation * -1) / curveMod));
	}
}
