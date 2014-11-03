package com.knight.knightnet.reader;

import java.awt.image.BufferedImage;

import com.knight.knightnet.network.Network;

public class Reader implements Runnable {

	Thread thread;
	ReaderGui gui;
	BufferedImage backbuffer;
	boolean running = true;
	Network network = new Network(7, 1, 4, 4);

	public Reader() {
		gui = new ReaderGui(this);
		gui.setVisible(true);
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		while(running) {
			tick();
			draw();
		}
	}

	private void draw() {
		/*if(backbuffer == null) backbuffer = new BufferedImage(gui.getWidth(), gui.getHeight(), BufferedImage.TYPE_INT_RGB);
		if(backbuffer.getWidth() != gui.getWidth() || backbuffer.getHeight() != gui.getHeight()) backbuffer = new BufferedImage(gui.getWidth(), gui.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics g = gui.getGraphics();
		Graphics bbg = backbuffer.getGraphics();

		for(int i = 0; i < 7; i++) {
			int x = 200;
			int y = 80;
			int width = 50;
			int height = 20;
			switch(i) {
			case 0:
				break;
			case 1:
				width = 20;
				height = 50;
				break;
			case 2:
				x += width;
				width = 20;
				height = 50;
				break;
			case 3:
				y += height;
				break;
			case 4:
				y += height;
				width  = 20;
				height = 50;
				break;
			case 5:
				
				break;
			case 6:

				break;
			}
			if(gui.states[i]) {
				bbg.setColor(Color.black);
			} else {
				bbg.setColor(Color.gray);
			}
			bbg.fillRect(x, y, width, height);
		}

		g.drawImage(backbuffer, 0, 0, gui);*/
	}

	private void tick() {

	}


}
