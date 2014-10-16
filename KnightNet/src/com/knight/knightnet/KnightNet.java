package com.knight.knightnet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/*(C) Copyright 2014-2015 dude0367 (Knight) & lkarinja (Karinja)
 * This program comes with absolutely no warranty.
 * This program is under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International
 * and no redistribution, commercial use, or modification can be made without contacting the program's developers.
 */

public class KnightNet {

	//Backpropagation guide: http://home.agh.edu.pl/~vlsi/AI/backp_t_en/backprop.html
	//Basic ANN guide: http://www.ai-junkie.com/ann/evolved/nnt1.html

	public static void main(String args[]) {//ENTRY POINT TO TEST NETWORK
		Network network = new Network(1, 2, 2, 3);/*Create network with 1 hidden layer, 
													2 input neurons and 2 output neurons,
													and 3 neurons per hidden layer*/
		boolean running = true;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String in = null;
		String out = "Welcome to the KnightNet";
		while(running) {
			System.out.println(out);
			try {
				in = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(in.contains(" ")) {
				String[] split = in.split(" ");
				if(split[0].equalsIgnoreCase("input")) {
					double[] input = new double[split.length - 1];
					for(int i = 1; i < split.length; i++) {
						input[i-1] = Double.valueOf(split[i]);
					}
					double[] output = network.process(input);
					//System.out.println(output);
					out = "";
					for(double d : output) out += d + "\n";
				}
				
				else {
					out = "Invalid input";
				}
			} else {
				if(in.equalsIgnoreCase("end") || in.equalsIgnoreCase("stop")) {
					System.out.println("Bye");
					running = false;
				} else
				if(in.equalsIgnoreCase("reset") || in.equalsIgnoreCase("restart")) {
					network = new Network(1, 2, 2, 3);
					out = "Reset";
				}
				
				else {
					out = "Invalid input";
				}
			}
		}
	}

}
