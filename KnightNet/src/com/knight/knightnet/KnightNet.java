package com.knight.knightnet;

public class KnightNet {

	//Backpropagation guide: http://home.agh.edu.pl/~vlsi/AI/backp_t_en/backprop.html
	//Basic ANN guide: http://www.ai-junkie.com/ann/evolved/nnt1.html
	
	public static void main(String args[]) {//ENTRY POINT TO TEST NETWORK
		Network network = new Network(1, 2, 2, 3);/*Create network with 1 hidden layer, 
													2 input neurons and 2 output neurons,
													and 3 neurons per hidden layer*/
		network.getLayers();
	}

}
