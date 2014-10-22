package com.knight.knightnet.pong;

import com.knight.knightnet.Agent;
import com.knight.knightnet.Population;

public class Game {
	
	Ball ball;
	Paddle[] paddles = new Paddle[2];
	
	public Game(Agent a, Agent b, Population p) {
		paddles[0] = new Paddle(a, p);
		paddles[0].setX(5);
		paddles[1] = new Paddle(b, p);
		paddles[1].setX(Pong.pong.getWidth() - 10);
		ball = new Ball();
		resetBall();
	}

	public void tick(long delta) {
		ball.setX(ball.getX() + Math.cos(ball.getAngle()) * ball.getSpeed() * delta);
		ball.setY(ball.getY() - Math.sin(ball.getAngle()) * ball.getSpeed() * delta);
		if(ball.getY() >= Pong.pong.getHeight() || ball.getY() <= 30) {
			ball.setAngle(2 * Math.PI - ball.getAngle());
		}
		if(ball.getX() <= 0) {
			resetBall();
		}
		if(ball.getX() >= Pong.pong.getWidth()) {
			resetBall();
		}
		
		for(int i = 0; i < 2; i++) {
			int o = 1;
			if(i == o) o--;
			double[] output = paddles[i].getGenome().getNetwork().process(new double[] {
					paddles[i].getY()-ball.getY(), Math.abs(paddles[i].getX() - ball.getX()), ball.getAngle(), ball.getSpeed()/*, paddles[o].getY()*/
			});
			output[0] -= .5;
			output[0] *= delta * 4;
			paddles[i].setY(paddles[i].getY() + output[0]);
			if(paddles[i].getY() < 30) paddles[i].setY(30);
			if(paddles[i].getY() > Pong.pong.getHeight() - Paddle.height) paddles[i].setY(Pong.pong.getHeight() - Paddle.height);
		}
	}
	
	void resetBall() {
		ball.setX(Pong.pong.getWidth()/2);
		ball.setY(Pong.pong.getHeight()/2);
		ball.setAngle(Math.random() * Math.PI * 2);
		ball.setSpeed(.1);
	}

}
