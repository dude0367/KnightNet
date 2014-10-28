package com.knight.knightnet.network;
import java.util.ArrayList;

/*(C) Copyright 2014-2015 dude0367 (Knight) & lkarinja (Karinja)
 * This program comes with absolutely no warranty.
 * This program is under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International
 * and no redistribution, commercial use, or modification can be made without contacting the program's developers.
 */

public class Layer {

	private ArrayList<Neuron> neurons = new ArrayList<Neuron>();//The neurons in this layer
	private Layer previous;//The last layer
	private Layer next;//The next layer
	private Network network;
	
	public Layer(int size, Layer last, Network network) {
		this(size, last);
		this.setNetwork(network);
	}

	public Layer(int size, Layer last) {
		setPrevious(last);
		if(last != null) {
			last.setNext(this);
		}
		for(int i = 0; i < size; i++) {//Make the neurons in the layer
			Neuron n = new Neuron(this);
			neurons.add(n);
		}
	}

	public double[] feedforward(double[] input) {
		/*double[] output = new double[getNeurons().size()];
		for(int i = 0; i < getNeurons().size(); i++) {
			Neuron n = getNeurons().get(i);
			double sum = 0;
			for(int o = 0; o < ) {.
				Neuron nn;
				sum += n.weights.get(nn) * input;
			}
		}
		if(next == null) return output;
		return next.feedforward(output);*/
		if(next == null) {
			return input;
		}
		double[] output = new double[next.getNeurons().size()];
		for(int i = 0; i < next.getNeurons().size(); i++) {
			Neuron n = next.getNeurons().get(i);
			double sum = 0;
			for(int o = 0; o < getNeurons().size(); o++/*Neuron nn : getNeurons()*/) {
				Neuron nn = getNeurons().get(o);
				sum += n.weights.get(nn) * input[o];
			}
			output[i] = n.calcSigmoid(sum);
		}
		return next.feedforward(output);
	}
	
	public Layer getFirstLayer() {
		if(previous == null) return this;
		return previous.getFirstLayer();
	}

	public Layer getPrevious() {
		return previous;
	}

	public void setPrevious(Layer previous) {
		this.previous = previous;
	}

	public Layer getNext() {
		return next;
	}

	public void setNext(Layer next) {
		this.next = next;
	}

	public ArrayList<Neuron> getNeurons() {
		return neurons;
	}

	public void setNeurons(ArrayList<Neuron> neurons) {
		this.neurons = neurons;
	}

	public Network getNetwork() {
		return network;
	}

	public void setNetwork(Network network) {
		this.network = network;
	}

}
