package com.knight.knightnet.joust;

import com.knight.knightnet.Agent;
import com.knight.knightnet.Genome;

public class Jouster extends Agent {
	public TestGameJoust game;
	private double lanceAngle;

	public Jouster(TestGameJoust game) {
		this.game = game;
		setGenome(new Genome(3, 3));
	}

	public double getLanceAngle() {
		return lanceAngle;
	}

	public void setLanceAngle(double lanceAngle) {
		this.lanceAngle = lanceAngle;
	}

}
