package com.knight.knightnet.sotf;

import com.knight.knightnet.gamecore.Agent;
import com.knight.knightnet.gamecore.Genome;
import com.knight.knightnet.network.Population;

public class Player extends Agent {
	
	/*
	 * Inputs:
	 * Current warrior target (x, y) (Vector)
	 * Current builder target (x, y) (vector)
	 * Current builder build type
	 * Current builder build time remaining
	 * 
	 * Outputs:
	 * New Target (x, y) (Vector)
	 * New Builder target (x, y)
	 * New Builder build type
	 * */
	
	public Player(Population pop) {
		super(pop);
		setGenome(new Genome(6, 5, 4, 4));
	}

}
