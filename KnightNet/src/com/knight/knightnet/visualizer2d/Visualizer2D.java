package com.knight.knightnet.visualizer2d;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import com.knight.knightnet.gamecore.Agent;
import com.knight.knightnet.network.Layer;
import com.knight.knightnet.network.Neuron;
import com.knight.knightnet.network.Population;

public class Visualizer2D extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private Population pop;
	BufferedImage backbuffer;
	
	public Visualizer2D() {
		this.setSize(800, 600);
		this.setTitle("ANN Visualizer");
		this.setVisible(true);
	}
	
	public Visualizer2D(Population p) {
		this();
		pop = p;
	}
	
	public void draw() {
		if(backbuffer == null) backbuffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		if(backbuffer.getWidth() != getWidth() || backbuffer.getHeight() != getHeight()) backbuffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics g = getGraphics();
		Graphics bbg = backbuffer.getGraphics();
		bbg.clearRect(0, 0, getWidth(), getHeight());
		Agent top = null;
		for(Agent a : pop.population) {
			if(a.getFitness() == pop.getFittest()) {
				top = a;
				break;
			}
		}
		if(top == null) top = pop.population.get(0);
		int layercount = top.getGenome().getNetwork().getLayers().size();
		int spread = (this.getWidth() - 50) / layercount;
		int x = 25;
		int lastySpread = 0;
		int width = 25;
		int height = 25;
		for(Layer l : top.getGenome().getNetwork().getLayers()) {
			int neuroncount = l.getNeurons().size();
			int yspread = (this.getHeight() - 50) / neuroncount;
			int y = yspread / 3;
			for(Neuron n : l.getNeurons()) {
				bbg.drawRect(x, y, width, height);
				if(l.getPrevious() != null) {
					int tempY = lastySpread / 3;
					for(Neuron nn : l.getPrevious().getNeurons()) {
						bbg.drawLine(x - spread + width, tempY + height/2, x, y + height/2);
						bbg.setColor(Color.red);
						bbg.drawString("" + n.getWeights().get(nn), x - spread/2, (y-tempY)/2);
						tempY += lastySpread;
					}
				}
				y+=yspread;
			}
			lastySpread = yspread;
			x+=spread;
		}
		
		g.drawImage(backbuffer, 0, 0, this);
	}

	public Population getPop() {
		return pop;
	}

	public void setPop(Population pop) {
		this.pop = pop;
	}

}
