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
		return this.pop;
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
/*
 * FIX ME PLEASE NATHAN
class Plane extends Object{
	Vector normal;
	float distance;
	Plane(){
		
	}
	protected void setX(Vector normal){
		this.normal=normal;
	}
	protected Vector getNormal(){
		return this.normal;
	}
}
*/
class Calc implements Runnable{
	private boolean isRunning=false;
	private Thread thread=null;
	protected Visualizer superior=null;
	protected double m[]=new double[16];
	Calc(Visualizer superior){
		this.superior=superior;
		this.thread = new Thread(this);
		this.isRunning=true;
		this.thread.start();
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
		eZ=90;
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
	//http://www.eng.utah.edu/~cs6360/Lectures/frustum.pdf <-Read Me
	/*
	 * #include <vector>
#include <cmath>
#include <stdexcept>
#include <algorithm>

struct Vector
{
    Vector() : x(0),y(0),z(0),w(1){}
    Vector(float a, float b, float c) : x(a),y(b),z(c),w(1){}

    Assume proper operator overloads here, with vectors and scalars 
    float Length() const
    {
        return std::sqrt(x*x + y*y + z*z);
    }

    Vector Unit() const
    {
        const float epsilon = 1e-6;
        float mag = Length();
        if(mag < epsilon){
            std::out_of_range e("");
            throw e;
        }
        return *this / mag;
    }
};

inline float Dot(const Vector& v1, const Vector& v2)
{
    return v1.x*v2.x + v1.y*v2.y + v1.z*v2.z;
}

class Matrix
{
    public:
    Matrix() : data(16)
    {
        Identity();
    }
    void Identity()
    {
        std::fill(data.begin(), data.end(), float(0));
        data[0] = data[5] = data[10] = data[15] = 1.0f;
    }
    float& operator[](size_t index)
    {
        if(index >= 16){
            std::out_of_range e("");
            throw e;
        }
        return data[index];
    }
    Matrix operator*(const Matrix& m) const
    {
        Matrix dst;
        int col;
        for(int y=0; y<4; ++y){
            col = y*4;
            for(int x=0; x<4; ++x){
                for(int i=0; i<4; ++i){
                    dst[x+col] += m[i+col]*data[x+i*4];
                }
            }
        }
        return dst;
    }
    Matrix& operator*=(const Matrix& m)
    {
        *this = (*this) * m;
        return *this;
    }

    The interesting stuff 
    void SetupClipMatrix(float fov, float aspectRatio, float near, float far)
    {
        Identity();
        float f = 1.0f / std::tan(fov * 0.5f);
        data[0] = f*aspectRatio;
        data[5] = f;
        data[10] = (far+near) / (far-near);
        data[11] = 1.0f; this 'plugs' the old z into w
        data[14] = (2.0f*near*far) / (near-far);
        data[15] = 0.0f;
    }

    std::vector<float> data;
};

inline Vector operator*(const Vector& v, const Matrix& m)
{
    Vector dst;
    dst.x = v.x*m[0] + v.y*m[4] + v.z*m[8 ] + v.w*m[12];
    dst.y = v.x*m[1] + v.y*m[5] + v.z*m[9 ] + v.w*m[13];
    dst.z = v.x*m[2] + v.y*m[6] + v.z*m[10] + v.w*m[14];
    dst.w = v.x*m[3] + v.y*m[7] + v.z*m[11] + v.w*m[15];
    return dst;
}

typedef std::vector<Vector> VecArr;
VecArr ProjectAndClip(int width, int height, float near, float far, const VecArr& vertex)
{
    float halfWidth = (float)width * 0.5f;
    float halfHeight = (float)height * 0.5f;
    float aspect = (float)width / (float)height;
    Vector v;
    Matrix clipMatrix;
    VecArr dst;
    clipMatrix.SetupClipMatrix(60.0f * (M_PI / 180.0f), aspect, near, far);
      Here, after the perspective divide, you perform Sutherland-Hodgeman clipping 
        by checking if the x, y and z components are inside the range of [-w, w].
        One checks each vector component seperately against each plane. Per-vertex
        data like colours, normals and texture coordinates need to be linearly
        interpolated for clipped edges to reflect the change. If the edge (v0,v1)
        is tested against the positive x plane, and v1 is outside, the interpolant
        becomes: (v1.x - w) / (v1.x - v0.x)
        I skip this stage all together to be brief.
    
    for(VecArr::iterator i=vertex.begin(); i!=vertex.end(); ++i){
        v = (*i) * clipMatrix;
        v /= v.w; Don't get confused here. I assume the divide leaves v.w alone.
        dst.push_back(v);
    }


    for(VecArr::iterator i=dst.begin(); i!=dst.end(); ++i){
        i->x = (i->x * (float)width) / (2.0f * i->w) + halfWidth;
        i->y = (i->y * (float)height) / (2.0f * i->w) + halfHeight;
    }
    return dst;
}
	 * */
	//http://stackoverflow.com/questions/8633034/basic-render-3d-perspective-projection-onto-2d-screen-with-camera-without-openg THIS IS THE LINK
	protected double[] BuildPerspProjMat(double fov, double aspect, double znear, double zfar){ //http://stackoverflow.com/questions/8633034/basic-render-3d-perspective-projection-onto-2d-screen-with-camera-without-openg
		
		double xymax = znear * Math.tan(fov * (Math.PI/360.0));
		double ymin = -xymax;
		double xmin = -xymax;
		
		double width = xymax - xmin;
		double height = xymax - ymin;
		
		double depth = zfar - znear;
		double q = -(zfar + znear) / depth;
		double qn = -2 * (zfar * znear) / depth;
		
		double w = 2 * znear / width;
		w = w / aspect;
		double h = 2 * znear / height;
		
		m[0]  = w;
		m[1]  = 0.0;
		m[2]  = 0.0;
		m[3]  = 0.0;
		
		m[4]  = 0.0;
		m[5]  = h;
		m[6]  = 0.0;
		m[7]  = 0.0;
		
		m[8]  = 0.0;
		m[9]  = 0.0;
		m[10] = q;
		m[11] = -1.0;
		
		m[12] = 0.0;
		m[13] = 0.0;
		m[14] = qn;
		m[15] = 0.0;
		return m;
	}
	/*Thus, for a point p, we would:

	Perform model transformation matrix * p, resulting in pm.
	Perform projection matrix * pm, resulting in pp.
	Clipping pp against the viewing volume.
	Perform viewport transformation matrix * pp, resulting is ps: point on screen.
	*/
	protected void makeFrustum(double fovY, double aspectRatio, double front, double back){
	    double DEG2RAD = 3.14159265 / 180;

	    double tangent = Math.tan(fovY/2 * DEG2RAD);   // tangent of half fovY
	    double height = front * tangent;          // half height of near plane
	    double width = height * aspectRatio;      // half width of near plane

	    // params: left, right, bottom, top, near, far
	    setFrustum(-width, width, -height, height, front, back);
	}
	//This is glfrustrum
	protected void setFrustum(double width, double width2, double height, double height2, double front, double back){
	    //m.identity();
	    m[0]  = 2 * front / (width2 - width);
	    m[2]  = (width2 + width) / (width2 - width);
	    m[5]  = 2 * front / (height2 - height);
	    m[6]  = (height2 + height) / (height2 - height);
	    m[10] = -(back + front) / (back - front);
	    m[11] = -(2 * back * front) / (back - front);
	    m[14] = -1;
	    m[15] = 0;
	}
	@Override
	public void run() {
		while(this.isRunning){
			//Have thread running...
		}
	}
	public boolean isRunning() {
		return this.isRunning;
	}
	public boolean stop(){
		this.isRunning=false;
		return true;
	}
}

/*
 * HELP (This is code from http://www.cubic.org/docs/3dclip.htm used as reference)
plane p;                      // plane to construct from a,b and c
tvector a,b,c;                // points to build a plane from
{
   // build normal vector
   tvector q,v;
   q.x = b.x - a.x;    v.x = b.x - c.x;
   q.y = b.y - a.y;    v.x = b.y - c.y;
   q.z = b.y - a.y;    v.x = b.z - c.z;
   p.normal = crossproduct (q,v);
   normalize_vector (q.normal);

   // calculate distance to origin
   p.distance = dotproduct (p.normal, a);  // you could also use b or c
}
struct tvector
{
  float x,y,z;             // standard vector
};

struct plane
{
  tvector  normal;         // normalized Normal-Vector of the plane
  float    distance;       // shortest distance from plane to Origin
};

struct frustum
{
  plane sides[4];          // represent the 4 sides of the frustum
  plane znear;             // the z-near plane
}
void setup_frustum (float project_scale, float SX, float SY)
// * project_scale is the projection scaling factor used in the perspective
//   projection. It's the value you multiply x and y with before you divide
//   them by z. (usually I use 256 for this value).
//
//  * SX and SY are the size of the viewport you want to draw at (320,200 anyone?)
{
  float angle_horizontal =  atan2(SX/2,project_scale)-0.0001;
  float angle_vertical   =  atan2(SY/2,project_scale)-0.0001;
  float sh               =  sin(angle_horizontal);
  float sv               =  sin(angle_vertical);
  float ch               =  cos(angle_horizontal);
  float cv               =  cos(angle_vertical);
  // left
  sides[0].normal.x=ch;
  sides[0].normal.y=0;
  sides[0].normal.z=sh;
  sides[0].distance = 0;
  // right
  sides[1].normal.x=-ch;
  sides[1].normal.y=0;
  sides[1].normal.z=sh;
  sides[1].distance = 0;
  // top
  sides[2].normal.x=0;
  sides[2].normal.y=cv;
  sides[2].normal.z=sv;
  sides[2].distance = 0;
  // bottom
  sides[3].normal.x=0;
  sides[3].normal.y=-cv;
  sides[3].normal.z=sv;
  sides[3].distance = 0;
  // z-near clipping plane
  znear.normal.x=0;
  znear.normal.y=0;
  znear.normal.z=1;
  znear.distance = -10;
}
distance = dotproduct (point, plane.normal) - plane.distance;

//The distance is < zero if the point is on the backside of the plane. It's zero if the point is on the plane, and positive otherwise. If both points have a negative distance we can remove them. The line will be entirely on the backside of the plane. If both are positive we don't have to do anything (the line is completely visible). But if the signs are different we have to calculate the intersection point of the plane and the line:

float da;   // distance plane -> point a
float db;   // distance plane -> point b

float s = da/(da-db);   // intersection factor (between 0 and 1)

intersectpoint.x = a.x + s*(b.x-a.x);
intersectpoint.y = a.y + s*(b.y-a.y);
intersectpoint.z = a.z + s*(b.z-a.z);

// need to clip more values (texture coordinates)? do it this way:
intersectpoint.value = a.value + s*(b.value-a.value);
*/