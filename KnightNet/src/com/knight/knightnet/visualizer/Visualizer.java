package com.knight.knightnet.visualizer;

/*(C) Copyright 2014-2015 dude0367 (Knight) & lkarinja (Karinja)
 * This program comes with absolutely no warranty.
 * This program is under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International
 * and no redistribution, commercial use, or modification can be made without contacting the program's developers.
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import com.knight.knightnet.network.Population;
//http://freespace.virgin.net/hugo.elias/routines/3d_to_2d.htm

//http://en.wikipedia.org/wiki/Triangulation <-Read me

//http://freespace.virgin.net/hugo.elias/routines/3d_to_2d.htm

//http://stackoverflow.com/questions/8633034/basic-render-3d-perspective-projection-onto-2d-screen-with-camera-without-openg <-Very helpful

//Read me: http://www.experts-exchange.com/Programming/Game/Q_21869528.html

//http://gamedev.stackexchange.com/questions/28599/3d-first-person-movement

//http://stackoverflow.com/questions/18158238/how-to-make-a-camera-move-in-the-direction-it-is-facing-in-java <-THANK YOU!!!

//http://stackoverflow.com/questions/701504/perspective-projection-help-a-noob

//http://www.dreamincode.net/forums/topic/239174-3d-perspective-projection/ <-Very helpful
public class Visualizer extends JFrame{
	private static final long serialVersionUID = 13374208008135L;
	private final int HEIGHT=600;
	private final int WIDTH=800;
	protected static Calc calc;
	protected static Vector<Double> camera;
	protected static Vector<Double> view;
	protected Population pop;
	protected BufferedImage backbuffer;

	public Visualizer(){
		this.setTitle("ANN Visualizer");
		calc=new Calc();
		camera=new Vector<Double>(200.0,200.0,200.0);
		view=new Vector<Double>(0.0,0.0,0.0);

		setSize(WIDTH,HEIGHT);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		setVisible(true);
		addKeyListener(new KeyListener(){
			@Override
			public void keyPressed(KeyEvent e){
				double y = view.x;
				double p = view.y;
				double k = 5.0;
				double xzLength = Math.cos(p) * k;
				double dx = xzLength * Math.cos(y);
				double dz = xzLength * Math.sin(y);
				double dy = k * Math.sin(p);
				switch (e.getKeyCode()){
				case KeyEvent.VK_W:
					view.x+=Math.toRadians(1);
					draw();
					break;
				case KeyEvent.VK_A:
					view.y+=Math.toRadians(1);
					draw();
					break;
				case KeyEvent.VK_S:
					view.x-=Math.toRadians(1);
					draw();
					break;
				case KeyEvent.VK_D:
					view.y-=Math.toRadians(1);
					draw();
					break;
				case KeyEvent.VK_Q:
					view.z+=Math.toRadians(1);
					draw();
					break;
				case KeyEvent.VK_E:
					view.z-=Math.toRadians(1);
					draw();
					break;
				case KeyEvent.VK_UP:
					camera.x += dx;
					camera.y += dy;
					camera.z += dz;
					draw();
					break;
					/*case KeyEvent.VK_LEFT:
					camera.x += dx;
					camera.y += dy;
					camera.z += dz;
					repaint();
					break;*/
				case KeyEvent.VK_DOWN:
					camera.x -= dx;
					camera.y -= dy;
					camera.z -= dz;
					draw();
					break;
					/*case KeyEvent.VK_RIGHT:
					camera.x += dx;
					camera.y += dy;
					camera.z += dz;
					repaint();
					break;*/
				case KeyEvent.VK_R:
					camera.x=0.0;
					camera.y=0.0;
					camera.z=0.0;
					view.x=0.0;
					view.y=0.0;
					view.z=0.0;
					draw();
					break;
				}
			}
			@Override
			public void keyReleased(KeyEvent e){}
			@Override
			public void keyTyped(KeyEvent e){
			}
		});
		addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent arg0) {
				System.out.println("Camera: "+camera.toString());
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
			}
			@Override
			public void mouseReleased(MouseEvent arg0) {
			}
		});
	}
	public void draw(Population pop) {
		this.setPop(pop);
		draw();
	}
	public void draw(){
		if(backbuffer == null) backbuffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		if(backbuffer.getWidth() != getWidth() || backbuffer.getHeight() != getHeight()) backbuffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);

		Graphics g = getGraphics();
		Graphics bbg = backbuffer.getGraphics();
		bbg.clearRect(0, 0, getWidth(), getHeight());

		bbg.setColor(new Color(255,0,0)); //RED
		Vector<Vector<Double>> xAxis=new Vector<Vector<Double>>(calc.to2D(new Vector<Double>(0.0,0.0,0.0), camera, view),calc.to2D(new Vector<Double>(100.0,0.0,0.0), camera, view), new Vector<Double>(0.0,0.0,0.0));
		bbg.drawLine((int)Math.round((Double)xAxis.x.x),(int)Math.round((Double)xAxis.x.y),(int)Math.round((Double)xAxis.y.x),(int)Math.round((Double)xAxis.y.y));

		bbg.setColor(new Color(0,255,0)); //GREEN
		Vector<Vector<Double>> yAxis=new Vector<Vector<Double>>(calc.to2D(new Vector<Double>(0.0,0.0,0.0), camera, view),calc.to2D(new Vector<Double>(0.0,100.0,0.0), camera, view), new Vector<Double>(0.0,0.0,0.0));
		bbg.drawLine((int)Math.round((Double)yAxis.x.x),(int)Math.round((Double)yAxis.x.y),(int)Math.round((Double)yAxis.y.x),(int)Math.round((Double)yAxis.y.y));

		bbg.setColor(new Color(0,0,255)); //BLUE
		Vector<Vector<Double>> zAxis=new Vector<Vector<Double>>(calc.to2D(new Vector<Double>(0.0,0.0,0.0), camera, view),calc.to2D(new Vector<Double>(0.0,0.0,100.0), camera, view), new Vector<Double>(0.0,0.0,0.0));
		bbg.drawLine((int)Math.round((Double)zAxis.x.x),(int)Math.round((Double)zAxis.x.y),(int)Math.round((Double)zAxis.y.x),(int)Math.round((Double)zAxis.y.y));

		g.drawImage(backbuffer, 0, 0, this);
	}
	public Population getPop() {
		return pop;
	}
	public void setPop(Population pop) {
		this.pop = pop;
	}
	public int getHeight(){
		return this.HEIGHT;
	}
	public int getWidth(){
		return this.WIDTH;
	}
}
class Vector<E> extends Object{
	public E x,y,z;
	Vector(){}
	Vector(E x, E y, E z){
		this.x=x;
		this.y=y;
		this.z=z;
	}
	protected void setX(E xVal){
		this.x=xVal;
	}
	protected void setY(E yVal){
		this.y=yVal;
	}
	protected void setZ(E zVal){
		this.z=zVal;
	}
	protected E getX(){
		return this.x;
	}
	protected E getY(){
		return this.y;
	}
	protected E getZ(){
		return this.z;
	}
	public String toString(){
		String s=("("+this.x.toString()+","+this.y.toString()+","+this.z.toString()+")");
		return s;
	}
}
class Calc implements Runnable{
	private boolean isRunning=false;
	private Thread thread;
	Calc(){
		thread = new Thread(this);
		isRunning=true;
		thread.start();
	}
	protected Vector<Double> to2D(Vector<Double> point, Vector<Double> camera, Vector<Double> viewAngle){
		double dX,dY,dZ;
		double aX,aY,aZ;
		double cX,cY,cZ; 
		double thetaX,thetaY,thetaZ;
		double eX,eY,eZ;
		double bX,bY;
		//3D point in space
		aX=point.x;
		aY=point.y;
		aZ=point.z;
		//Camera position in space
		cX=camera.x;
		cY=camera.y;
		cZ=camera.z;
		//Camera angles (Tait-Bryan)
		thetaX=viewAngle.x;
		thetaY=viewAngle.y;
		thetaZ=viewAngle.z;
		//Position of viewers eye
		eX=camera.x-400; //Change me later
		eY=camera.y-300; //Change me later
		//Keep this as 500 as it will affect the FOV (Can be changed later)
		eZ=500;
		//Calculations
		dX=Math.cos(thetaY)*(Math.sin(thetaZ)*(aY-cY)+Math.cos(thetaZ)*(aX-cY))-Math.sin(thetaY)*(aZ-cZ);
		dY=Math.sin(thetaX)*(Math.cos(thetaY)*(aZ-cZ)+Math.sin(thetaY)*(Math.sin(thetaZ)*(aY-cY)+Math.cos(thetaZ)*(aX-cX)))+Math.cos(thetaX)*(Math.cos(thetaZ)*(aY-cY)-Math.sin(thetaZ)*(aX-cX));
		dZ=Math.cos(thetaX)*(Math.cos(thetaY)*(aZ-cZ)+Math.sin(thetaY)*(Math.sin(thetaZ)*(aY-cY)+Math.cos(thetaZ)*(aX-cX)))-Math.sin(thetaX)*(Math.cos(thetaZ)*(aY-cY)-Math.sin(thetaZ)*(aX-cX));
		//These are the 2D points that were calculated
		bX=((eZ/dZ)*dX)-eX;
		bY=((eZ/dZ)*dY)-eY;
		//Return the 2D point that was calculated
		return new Vector<Double>(bX,bY,0.0);
	}
	@Override
	public void run() {
		while(this.isRunning){
			//Have thread running...
		}
	}
	public boolean isRunning() {
		return isRunning;
	}
	public boolean stop(){
		this.isRunning=false;
		return true;
	}
}