package com.knight.input;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;



public class InputHandler implements KeyListener {
	
	private boolean[] keys = new boolean[256];
	
	public InputHandler(Component c) {
		c.addKeyListener(this);
	}

	@Override
	public void keyPressed(KeyEvent evt) {
		keys[evt.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent evt) {
		keys[evt.getKeyCode()] = false;
	}

	@Override
	public void keyTyped(KeyEvent evt) {

	}
	
	public boolean getKey(int key) {
		return keys[key];
	}

}
