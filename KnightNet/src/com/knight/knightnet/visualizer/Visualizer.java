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
//http://en.wikipedia.org/wiki/3D_projection <-Basic concept

//http://freespace.virgin.net/hugo.elias/routines/3d_to_2d.htm

//http://en.wikipedia.org/wiki/Triangulation <-Read me

//http://freespace.virgin.net/hugo.elias/routines/3d_to_2d.htm

//http://stackoverflow.com/questions/8633034/basic-render-3d-perspective-projection-onto-2d-screen-with-camera-without-openg <-Very helpful

//Read me: http://www.experts-exchange.com/Programming/Game/Q_21869528.html

//http://gamedev.stackexchange.com/questions/28599/3d-first-person-movement

//http://stackoverflow.com/questions/18158238/how-to-make-a-camera-move-in-the-direction-it-is-facing-in-java <-Info for movement of camera

//http://stackoverflow.com/questions/701504/perspective-projection-help-a-noob

//http://www.dreamincode.net/forums/topic/239174-3d-perspective-projection/ <-Very helpful for actual calculations and stuff

//http://docs.opencv.org/modules/calib3d/doc/camera_calibration_and_3d_reconstruction.html <-README

//http://stackoverflow.com/questions/724219/how-to-convert-a-3d-point-into-2d-perspective-projection

//http://users.ece.gatech.edu/lanterma/mpg10/mpglecture04f10_3dto2dproj.pdf <-This is what I want

//http://www.codeincodeblock.com/2012/03/projecting-3d-world-co-ordinates-into.html <-Useful site

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
		this.setTitle("Visualizer");
		calc=new Calc(this);
		camera=new Vector<Double>(0.0,0.0,500.0);
		view=new Vector<Double>(0.0,0.0,0.0);

		setSize(WIDTH,HEIGHT);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		setVisible(true);
		addKeyListener(new KeyListener(){
			@Override
			public void keyPressed(KeyEvent e){
				double k = 5.0; //Distance to move
				
				double y = view.x; //X Angle View **YAW
				double p = view.y; //Y Angle View **PITCH
				double xzLength = Math.cos(p) * k; //Calculated Distance
				double dx = xzLength * Math.cos(y); //Distance to move about the X
				double dz = xzLength * Math.sin(y); //Distance to move about the Z
				double dy = k * Math.sin(p); //Distance to move about the Y **Note that this is usually not changed unless roll is changed
				
				double horY = view.x+(Math.PI); //X Angle View **YAW
				double horP = view.y; //Y Angle View **PITCH
				double horXZLength = Math.cos(horP) * k; //Calculated Distance
				double horDX = horXZLength * Math.cos(horY); //Distance to move about the X
				double horDZ = horXZLength * Math.sin(horY); //Distance to move about the Z
				double horDY = k * Math.sin(horP); //Distance to move about the Y **Note that this is usually not changed unless roll is changed
				
				switch (e.getKeyCode()){
				case KeyEvent.VK_W: //If the "w" key is down
					view.x+=Math.toRadians(1); //Rotate about the "X" axis
					draw();
					break;
				case KeyEvent.VK_A: //If the "a" key is down
					view.y+=Math.toRadians(1); //Rotate about the "Y" axis
					draw();
					break;
				case KeyEvent.VK_S: //If the "s" key is down
					view.x-=Math.toRadians(1); //Rotate about the "X" axis
					draw();
					break;
				case KeyEvent.VK_D: //If the "d" key is down
					view.y-=Math.toRadians(1); //Rotate about the "Y" axis
					draw();
					break;
				case KeyEvent.VK_Q: //If the "q" key is down
					view.z+=Math.toRadians(1); //Rotate about the "Z" axis
					draw();
					break;
				case KeyEvent.VK_E: //If the "e" key is down
					view.z-=Math.toRadians(1); //Rotate about the "Z" axis
					draw();
					break;
				case KeyEvent.VK_UP: //If the UP key is down
					//Move forwards
					camera.x += dx;
					camera.y += dy; //This usually will not change
					camera.z += dz;
					draw();
					break;
				case KeyEvent.VK_LEFT: //If the LEFT key is down
					//Move left
					camera.x += horDX;
					camera.y += horDY; //This usually will not change
					camera.z += horDZ;
					draw();
					break;
				case KeyEvent.VK_DOWN: //If the DOWN key is down
					//Move backwards
					camera.x -= dx;
					camera.y -= dy; //This usually will not change
					camera.z -= dz;
					draw();
					break;
				case KeyEvent.VK_RIGHT: //If the RIGHT key is down
					//Move right
					camera.x -= horDX;
					camera.y -= horDY; //This usually will not change
					camera.z -= horDZ;
					draw();
					break;
				case KeyEvent.VK_SPACE: //If the SPACEBAR key is down
					//Move up
					camera.y += k;
					draw();
					break;
				case KeyEvent.VK_SHIFT: //If the SHIFT key is down
					//Move down
					camera.y -= k;
					draw();
					break;
				case KeyEvent.VK_R: //If the "r" key is down
					//Reset view angles and camera
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
		bbg.drawString("X",(int)Math.round((Double)xAxis.y.x),(int)Math.round((Double)xAxis.y.y));
		
		bbg.setColor(new Color(0,255,0)); //GREEN
		Vector<Vector<Double>> yAxis=new Vector<Vector<Double>>(calc.to2D(new Vector<Double>(0.0,0.0,0.0), camera, view),calc.to2D(new Vector<Double>(0.0,100.0,0.0), camera, view), new Vector<Double>(0.0,0.0,0.0));
		bbg.drawLine((int)Math.round((Double)yAxis.x.x),(int)Math.round((Double)yAxis.x.y),(int)Math.round((Double)yAxis.y.x),(int)Math.round((Double)yAxis.y.y));
		bbg.drawString("Y",(int)Math.round((Double)yAxis.y.x),(int)Math.round((Double)yAxis.y.y));
		
		bbg.setColor(new Color(0,0,255)); //BLUE
		Vector<Vector<Double>> zAxis=new Vector<Vector<Double>>(calc.to2D(new Vector<Double>(0.0,0.0,0.0), camera, view),calc.to2D(new Vector<Double>(0.0,0.0,100.0), camera, view), new Vector<Double>(0.0,0.0,0.0));
		bbg.drawLine((int)Math.round((Double)zAxis.x.x),(int)Math.round((Double)zAxis.x.y),(int)Math.round((Double)zAxis.y.x),(int)Math.round((Double)zAxis.y.y));
		bbg.drawString("Z",(int)Math.round((Double)zAxis.y.x),(int)Math.round((Double)zAxis.y.y));
		
		bbg.setColor(new Color(255,0,255)); //PURPLE
		Vector<Double> toCam=calc.to2D(new Vector<Double>(0.0,0.0,0.0), camera, view);
		bbg.drawLine((int)Math.round((Double)toCam.x),(int)Math.round((Double)toCam.y),this.getWidth()/2, this.getHeight()/2);

		bbg.setColor(new Color(255,255,255));
		bbg.drawString(camera.toString(), 20, 40);
		bbg.drawString(view.toString(), 20, 60);
		
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
	private Thread thread=null;
	protected Visualizer superior=null;
	Calc(Visualizer superior){
		this.superior=superior;
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
		cX=camera.x;//+superior.getWidth()/2;
		cY=camera.y;//+superior.getHeight()/2;
		cZ=camera.z;
		//Camera angles (Tait-Bryan)
		thetaX=viewAngle.x;
		thetaY=viewAngle.y;
		thetaZ=viewAngle.z;
		//Position of viewers eye
		eX=camera.x-superior.getWidth()/2; //Change me later
		eY=camera.y-superior.getHeight()/2; //Change me later
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