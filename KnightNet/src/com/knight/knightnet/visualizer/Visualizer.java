package com.knight.knightnet.visualizer;

/*(C) Copyright 2014-2015 dude0367 (Knight) & lkarinja (Karinja)
 * This program comes with absolutely no warranty.
 * This program is under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International
 * and no redistribution, commercial use, or modification can be made without contacting the program's developers.
 */

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
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
	public static final int height=600;
	public static final int width=800;
	public static final double H=100;
	public static final double L=100;
	public static Visualizer engine;
	public static Calc calc;
	public static Vector<Double> camera;
	public static double rot=0;
	public static Vector<Double> view;
	public static Prism P;
	Visualizer(){
		super("Engine");
		setSize(width,height);
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
					repaint();
					break;
				case KeyEvent.VK_A:
					view.y+=Math.toRadians(1);
					repaint();
					break;
				case KeyEvent.VK_S:
					view.x-=Math.toRadians(1);
					repaint();
					break;
				case KeyEvent.VK_D:
					view.y-=Math.toRadians(1);
					repaint();
					break;
				case KeyEvent.VK_Q:
					view.z+=Math.toRadians(1);
					repaint();
					break;
				case KeyEvent.VK_E:
					view.z-=Math.toRadians(1);
					repaint();
					break;
				case KeyEvent.VK_UP:
					camera.x += dx;
					camera.y += dy;
					camera.z += dz;
					repaint();
					break;
				case KeyEvent.VK_LEFT:
					camera.x += dx;
					camera.y += dy;
					camera.z += dz;
					repaint();
					break;
				case KeyEvent.VK_DOWN:
					camera.x -= dx;
					camera.y -= dy;
					camera.z -= dz;
					repaint();
					break;
				case KeyEvent.VK_RIGHT:
					camera.x += dx;
					camera.y += dy;
					camera.z += dz;
					repaint();
					break;
				case KeyEvent.VK_R:
					camera.x=0.0;
					camera.y=0.0;
					camera.z=0.0;
					view.x=0.0;
					view.y=0.0;
					view.z=0.0;
					repaint();
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
	@SuppressWarnings("rawtypes")
	@Override
	public void paint(Graphics g){
		if(calc == null) {
			System.out.println("Calc is null, you dun goofed");
			return;
		}
		g.clearRect(0,0,width,height);
		P=create(new Vector<Double>(0.0,H,0.0),new Vector<Double>(L,H,0.0),new Vector<Double>(L,0.0,0.0),new Vector<Double>(0.0,0.0,0.0),new Vector<Double>(0.0,H,100.0),new Vector<Double>(L,H,100.0),new Vector<Double>(L,0.0,100.0),new Vector<Double>(0.0,0.0,100.0));
		for(int i=1;i<=6;i++){
			Side s=P.getSide(i);
			Polygon p=new Polygon();
			for(int k=1;k<=4;k++){
				Vector<Double> point=calc.to2D(s.getVector(k), camera, view);
				int x=(int) Math.round((Double)point.x);
				int y=(int) Math.round((Double)point.y);
				p.addPoint(x,y);
			}
			g.drawPolygon(p);
			p.invalidate();
		}
		g.setColor(new Color(255,0,0)); //RED
		Vector<Vector> xAxis=new Vector<Vector>(calc.to2D(new Vector<Double>(0.0,0.0,0.0), camera, view),calc.to2D(new Vector<Double>(100.0,0.0,0.0), camera, view), new Vector<Double>(0.0,0.0,0.0));
		g.drawLine((int)Math.round((Double)xAxis.x.x),(int)Math.round((Double)xAxis.x.y),(int)Math.round((Double)xAxis.y.x),(int)Math.round((Double)xAxis.y.y));

		g.setColor(new Color(0,255,0)); //GREEN
		Vector<Vector> yAxis=new Vector<Vector>(calc.to2D(new Vector<Double>(0.0,0.0,0.0), camera, view),calc.to2D(new Vector<Double>(0.0,100.0,0.0), camera, view), new Vector<Double>(0.0,0.0,0.0));
		g.drawLine((int)Math.round((Double)yAxis.x.x),(int)Math.round((Double)yAxis.x.y),(int)Math.round((Double)yAxis.y.x),(int)Math.round((Double)yAxis.y.y));

		g.setColor(new Color(0,0,255)); //BLUE
		Vector<Vector> zAxis=new Vector<Vector>(calc.to2D(new Vector<Double>(0.0,0.0,0.0), camera, view),calc.to2D(new Vector<Double>(0.0,0.0,100.0), camera, view), new Vector<Double>(0.0,0.0,0.0));
		g.drawLine((int)Math.round((Double)zAxis.x.x),(int)Math.round((Double)zAxis.x.y),(int)Math.round((Double)zAxis.y.x),(int)Math.round((Double)zAxis.y.y));

		System.out.println(xAxis.toString());
		System.out.println(yAxis.toString());
		System.out.println(zAxis.toString());
	}
	public static void createVisualizer(/*String[] args*/) {
		calc=new Calc();
		camera=new Vector<Double>(200.0,200.0,200.0);
		view=new Vector<Double>(0.0,0.0,0.0);
		engine=new Visualizer();
	}
	public Prism create(Vector<Double> vert1,Vector<Double> vert2,Vector<Double> vert3,Vector<Double> vert4,Vector<Double> vert5,Vector<Double> vert6,Vector<Double> vert7,Vector<Double> vert8){
		Side s1,s2,s3,s4,s5,s6;
		s1=new Side(new Vector<Double>(vert1.x,vert1.y,vert1.z), new Vector<Double>(vert2.x,vert2.y,vert2.z),new Vector<Double>(vert3.x,vert3.y,vert3.z), new Vector<Double>(vert4.x,vert4.y,vert4.z));
		s2=new Side(new Vector<Double>(vert1.x,vert1.y,vert1.z), new Vector<Double>(vert2.x,vert2.y,vert2.z),new Vector<Double>(vert6.x,vert6.y,vert6.z), new Vector<Double>(vert5.x,vert5.y,vert5.z));
		s3=new Side(new Vector<Double>(vert1.x,vert1.y,vert1.z), new Vector<Double>(vert4.x,vert4.y,vert4.z),new Vector<Double>(vert8.x,vert8.y,vert8.z), new Vector<Double>(vert5.x,vert5.y,vert5.z));
		s4=new Side(new Vector<Double>(vert2.x,vert2.y,vert2.z), new Vector<Double>(vert3.x,vert3.y,vert3.z),new Vector<Double>(vert7.x,vert7.y,vert7.z), new Vector<Double>(vert6.x,vert6.y,vert6.z));
		s5=new Side(new Vector<Double>(vert5.x,vert5.y,vert5.z), new Vector<Double>(vert6.x,vert6.y,vert6.z),new Vector<Double>(vert7.x,vert7.y,vert7.z), new Vector<Double>(vert8.x,vert8.y,vert8.z));
		s6=new Side(new Vector<Double>(vert3.x,vert3.y,vert3.z), new Vector<Double>(vert4.x,vert4.y,vert4.z),new Vector<Double>(vert8.x,vert8.y,vert8.z), new Vector<Double>(vert7.x,vert7.y,vert7.z));
		return new Prism(s1,s2,s3,s4,s5,s6);
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
	public String toString(){
		String s=("("+this.x.toString()+","+this.y.toString()+","+this.z.toString()+")");
		return s;
	}
}
class Side{
	public Vector<Double> v1,v2,v3,v4;
	Side(){}
	Side(Vector<Double> v1, Vector<Double> v2, Vector<Double> v3, Vector<Double> v4){
		this.v1=v1;
		this.v2=v2;
		this.v3=v3;
		this.v4=v4;
	}
	public Vector<Double> getVector(int k){
		Vector<Double> v=new Vector<Double>();
		switch(k){
		case 1:
			v=v1;
			break;
		case 2:
			v=v2;
			break;
		case 3:
			v=v3;
			break;
		case 4:
			v=v4;
			break;
		}
		return v;
	}
	public String toString(){
		String s1,s2,s3,s4;
		s1=String.format("%-20S",v1.toString());
		s2=String.format("%-20S",v2.toString());
		s3=String.format("%-20S",v3.toString());
		s4=String.format("%-20S",v4.toString());
		return (s1+s2+"\n"+s3+s4+"\n");
	}
}
class Prism{
	public Side s1,s2,s3,s4,s5,s6;
	Prism(Side s1,Side s2,Side s3,Side s4,Side s5,Side s6){
		this.s1=s1;
		this.s2=s2;
		this.s3=s3;
		this.s4=s4;
		this.s5=s5;
		this.s6=s6;
	}
	public Side getSide(int k){
		Side s=new Side();
		switch(k){
		case 1:
			s=s1;
			break;
		case 2:
			s=s2;
			break;
		case 3:
			s=s3;
			break;
		case 4:
			s=s4;
			break;
		case 5:
			s=s5;
			break;
		case 6:
			s=s6;
			break;
		}
		return s;
	}
	public String toString(){
		String s="";
		s+="1:"+s1.toString()+"\n\n";
		s+="2:"+s2.toString()+"\n\n";
		s+="3:"+s3.toString()+"\n\n";
		s+="4:"+s4.toString()+"\n\n";
		s+="5:"+s5.toString()+"\n\n";
		s+="6:"+s6.toString()+"\n\n";
		return s;
	}
}
class Calc /*extends Visualizer*/ implements Runnable{
	private static final long serialVersionUID = 1L;
	private boolean isRunning=false;
	private Thread thread;
	Calc(){
		thread = new Thread(this);
		//this.isRunning=true;
		thread.start();
		//run();
	}
	public Vector<Double> to2D(Vector<Double> point, Vector<Double> camera, Vector<Double> viewAngle){
		double dX,dY,dZ;
		double aX,aY,aZ;
		double cX,cY,cZ; 
		double thetaX,thetaY,thetaZ;
		double eX,eY,eZ; //Keep equal to 'c'
		double bX,bY;
		aX=point.x;
		aY=point.y;
		aZ=point.z;
		cX=camera.x;
		cY=camera.y;
		cZ=camera.z;
		thetaX=viewAngle.x;
		thetaY=viewAngle.y;
		thetaZ=viewAngle.z;
		eX=camera.x;
		eY=camera.y;
		eZ=500;
		dX=Math.cos(thetaY)*(Math.sin(thetaZ)*(aY-cY)+Math.cos(thetaZ)*(aX-cY))-Math.sin(thetaY)*(aZ-cZ);
		dY=Math.sin(thetaX)*(Math.cos(thetaY)*(aZ-cZ)+Math.sin(thetaY)*(Math.sin(thetaZ)*(aY-cY)+Math.cos(thetaZ)*(aX-cX)))+Math.cos(thetaX)*(Math.cos(thetaZ)*(aY-cY)-Math.sin(thetaZ)*(aX-cX));
		dZ=Math.cos(thetaX)*(Math.cos(thetaY)*(aZ-cZ)+Math.sin(thetaY)*(Math.sin(thetaZ)*(aY-cY)+Math.cos(thetaZ)*(aX-cX)))-Math.sin(thetaX)*(Math.cos(thetaZ)*(aY-cY)-Math.sin(thetaZ)*(aX-cX));
		bX=((eZ/dZ)*dX)-eX;
		bY=((eZ/dZ)*dY)-eY;
		return new Vector<Double>(bX,bY,0.0);
	}
	@Override
	public void run() {
		while(this.isRunning);
	}
	public boolean isRunning() {
		return isRunning;
	}
	public boolean stop(){
		try{
			this.isRunning=false;
			return true;
		}catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}
}