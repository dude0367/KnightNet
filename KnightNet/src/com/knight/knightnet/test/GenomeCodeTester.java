package com.knight.knightnet.test;

import com.knight.knightnet.gamecore.genes.GenomeCode;

public class GenomeCodeTester {
	
	public GenomeCodeTester() {
		GenomeCode g1 = new GenomeCode(3,3);
		GenomeCode g2 = new GenomeCode(3,3);
		GenomeCode baby = GenomeCode.crossover(g1, g2);
		baby.getNetwork().process(new double[]{1.0,1.0,1.0});
		//g.getHiddenLayers();
	}

}
