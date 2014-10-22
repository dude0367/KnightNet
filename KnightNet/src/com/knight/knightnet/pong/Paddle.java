package com.knight.knightnet.pong;

import com.knight.knightnet.Agent;
import com.knight.knightnet.Population;

public class Paddle extends Agent {
	
	public static int width = 10;
	public static int height = 40;
	
	public Paddle(Population pop) {
		super(pop);
	}
	
	public Paddle(Agent a, Population pop) {
		super(a, pop);
	}

}
