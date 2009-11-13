package gui;

import data.Position;
import data.SensorData;
import data.Vector3d;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.HashMap;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;


/**
 * GLRenderer.java <BR>
 * author: Brian Paul (converted to Java by Ron Cemer and Sven Goethel) <P>
 *
 * This version is equal to Brian Paul's version 1.2 1999/10/21
 */
public class GLRenderer implements GLEventListener {

    private boolean drawHistory = true, draw2dBeacon = true;

    public void init(GLAutoDrawable drawable) {
        absolutPosition = new Vector3d(0, 0, 0);
        absolutDirection = new Vector3d(0, 0, 1);
        // Use debug pipeline
        // drawable.setGL(new DebugGL(drawable.getGL()));

        GL gl = drawable.getGL();
        System.err.println("INIT GL IS: " + gl.getClass().getName());

        // Enable VSync
        gl.setSwapInterval(1);

        // Setup the drawing area and shading mode
        gl.glClearColor(0.1f, 0.1f, 0.1f, 0.0f);
        gl.glShadeModel(GL.GL_SMOOTH); // try setting this to GL_FLAT and see what happens

        drawable.addMouseListener(ml);
        drawable.addMouseMotionListener(mml);
        drawable.addKeyListener(kl);
        drawable.addMouseWheelListener(mwl);
        
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL gl = drawable.getGL();
        GLU glu = new GLU();

        if (height <= 0) { // avoid a divide by zero error!
        
            height = 1;
        }
        final float h = (float) width / (float) height;
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();


        glu.gluPerspective(45.0f, h, 0.0, 40.0);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    private Vector3d absolutPosition;
    private Vector3d absolutDirection;


    private void moveAbsolute(GL gl) {
        gl.glTranslated(absolutPosition.getX(), absolutPosition.getY(), absolutPosition.getZ());
//        System.out.println("pos: "+absolutPosition);
    }

    private void rotate(double pitch, double roll, double yaw, GL gl) {
        gl.glRotated(pitch, 1, 0, 0);
        gl.glRotated(roll, 0, 1, 0);
        gl.glRotated(yaw, 0, 0, 1);
    }

    private void resetAbsoluteValues() {
        absolutDirection = new Vector3d(0, 0, 1);
        absolutPosition =  new Vector3d(0, 0, 0);
    }

    public void reset() {
        old_x = old_y = old_z = move_x = move_y = move_z = 0;
        view_rotx = view_roty = view_rotz = old_rotx = old_roty = old_rotz = 0;
        resetAbsoluteValues();
    }

    public void display(GLAutoDrawable drawable) {
//        resetAbsoluteValues();

        GL gl = drawable.getGL();

        // Clear the drawing area
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        // Reset the current matrix to the "identity"
        gl.glLoadIdentity();
        
        gl.glPushMatrix();

        // MOVING VIEWPOINT //

        rotate(view_rotx, view_roty, view_rotz, gl);
        gl.glTranslated(-old_rotx, -old_roty, -old_rotz);
        gl.glTranslated(move_x, move_y, 0);
        gl.glTranslated(old_rotx, old_roty, old_rotz);
        moveAbsolute(gl);


        gl.glRotated(20, 1, 0, 0);
        gl.glRotated(25, 0, 1,0);
        gl.glTranslated(0 , -5, -10);
        gl.glRotated(-90, 1, 0, 0);
        gl.glTranslated(-7, -3.5, 0);

        // END OF MOVING VIEWPOINT //

        // DRAW THE SCENE
        
        // Drawing tags
        HashMap<String, SensorData> sd = SensorData.getData();
        if (sd != null) {
            for (SensorData data : SensorData.getData().values()) {
               // gl.glPushMatrix();
                gl.glColor3d(1, 1, 1);
                if (data != null) {
//                        System.out.println("data: "+data);
                    drawTag(data, gl);
                }
                //gl.glPopMatrix();
            }
        }
        
        // Draw Cell1 Base
        gl.glBegin(GL.GL_LINE_LOOP);
            gl.glColor3d(1, 1, 1);
            gl.glVertex3d( 8.028, 0.440, 0);
            gl.glVertex3d(14.298, 0.890, 0);
            gl.glVertex3d(12.558, 6.890, 0);
            gl.glVertex3d( 9.008, 6.910, 0);
        gl.glEnd();

        // Draw Cell1
        gl.glBegin(GL.GL_LINE_LOOP);
            gl.glColor3d(1, 0, 0);
            gl.glVertex3d( 8.028, 0.440, 2.270);
            gl.glVertex3d(14.298, 0.890, 2.250);
            gl.glVertex3d(12.558, 6.890, 2.260);
            gl.glVertex3d( 9.008, 6.910, 2.240);
        gl.glEnd();

        // Draw Cell2 Base
        gl.glBegin(GL.GL_LINE_LOOP);
            gl.glColor3d(1, 1, 1);
            gl.glVertex3d(0.436, 0.720, 0);
            gl.glVertex3d(6.295, 0.515, 0);
            gl.glVertex3d(6.744, 3.404, 0);
            gl.glVertex3d(0.512, 3.306, 0);
        gl.glEnd();

        // Draw Cell2
        gl.glBegin(GL.GL_LINE_LOOP);
            gl.glColor3d(0, 1, 0);
            gl.glVertex3d(0.436, 0.720, 2.250);
            gl.glVertex3d(6.295, 0.515, 2.286);
            gl.glVertex3d(6.744, 3.404, 2.305);
            gl.glVertex3d(0.512, 3.306, 2.271);
        gl.glEnd();
        
        // Draw Coordinate Axes
        drawCoordinateAxes(gl);

        zoom(zoom, gl);

        // Remember that every push needs a pop; this one is paired with
        // rotating the entire gear assembly
        gl.glPopMatrix();

        // Flush all drawing operations to the graphics card
        gl.glFlush();
    }

    private void zoom(double zoom, GL gl) {
        gl.glScaled(zoom, zoom, zoom);
    }

    private void drawCoordinateAxes(GL gl) {
        gl.glBegin(GL.GL_LINES);
            gl.glColor3d(1, 0, 0);
            gl.glVertex3d(0, 0, 0);
            gl.glVertex3d(5, 0, 0);
            gl.glColor3d(0, 1, 0);
            gl.glVertex3d(0, 0, 0);
            gl.glVertex3d(0, 5, 0);
            gl.glColor3d(0, 0, 1);
            gl.glVertex3d(0, 0, 0);
            gl.glVertex3d(0, 0, 5);
        gl.glEnd();
    }


    private void drawTagHistory(SensorData data, GL gl) {
        switch ((int)(Long.parseLong(data.getId()) % 5)) {
            case 0:
                gl.glColor3d(1, 1, 0);
                break;
            case 1:
                gl.glColor3d(1, 1, 1);
                break;
            case 2:
                gl.glColor3d(0, 1, 1);
                break;
            case 3:
                gl.glColor3d(0.5, 0.2, 0.3);
                break;
            case 4:
                gl.glColor3d(1, 0, 1);
                break;
        }


        ArrayList<SensorData> history = data.getHistory();
        if (history == null) {
            return;
        }
        
        gl.glBegin(GL.GL_LINE_STRIP);
            for (SensorData actData : data.getHistory()) {
                Position actPos = actData.getPos();
                if (actPos != null) {
                    gl.glVertex3d(actPos.getX(), actPos.getY(), actPos.getZ());
                }
            }
        gl.glEnd();

        gl.glColor3d(1, 1, 1);
    }

    private void drawTag(SensorData data, GL gl) {
        Position pos = data.getPos();
        double x = pos.getX(), y = pos.getY(), z = pos.getZ();
        double[] posArr = pos.toArray();

        if (drawHistory) {
            drawTagHistory(data, gl);
        }

        // draw 2d beacon
        if (draw2dBeacon) {
            gl.glBegin(gl.GL_LINES);
                gl.glColor3d(1, 1, 1);
                gl.glVertex3dv(posArr, 0);
                gl.glVertex3d(x, y, 0);

                gl.glVertex3d(x - 0.1, y, 0);
                gl.glVertex3d(x + 0.1, y, 0);

                gl.glVertex3d(x, y - 0.1, 0);
                gl.glVertex3d(x, y + 0.1, 0);
            gl.glEnd();
        }

        moveTo(posArr, gl);
        gl.glScaled(0.2, 0.2, 0.2);
        drawSphere(25, gl);
        gl.glScaled(5, 5, 5);
        moveBack(posArr, gl);
    }

    private void moveTo(double[] pos, GL gl) {
        gl.glTranslated(pos[0],pos[1],pos[2]);
    }

    private void moveBack(double[] pos, GL gl) {
        gl.glTranslated(-pos[0], -pos[1], -pos[2]);
    }
    
    private void drawSphere(int segs, GL gl) {
        gl.glPushMatrix();
        drawHalfSphere(segs, gl);
        gl.glRotated(180, 1, 0, 0);
        drawHalfSphere(segs, gl);
        gl.glPopMatrix();
    }

    private void drawHalfSphere(int segs, GL gl) {
		int i, j;
		float angle=0.0f;
		float[] vert= new float[3];
		float r, h;

		gl.glPushMatrix();
		gl.glScalef(0.5f, 0.5f, 0.5f);
		h=1.0f-(2.0f/(float) segs);
		r=(float) Math.sqrt(1.0f-h*h);
		for (i=0; i<segs; i++)
		{
			gl.glBegin(gl.GL_TRIANGLES);
			vert[0]=r*(float) Math.cos(angle*Math.PI/180.0f);
			vert[1]=h;
			vert[2]=r*(float) Math.sin(angle*Math.PI/180.0f);
			gl.glNormal3fv(vert, 0);
			gl.glVertex3fv(vert, 0);
			gl.glNormal3f(0.0f, 1.0f, 0.0f);
			gl.glVertex3f(0.0f, 1.0f, 0.0f);
			vert[0]=r*(float) Math.cos((angle+(360.0f/(float) segs))*Math.PI/180.0f);
			vert[1]=h;
			vert[2]=r*(float) Math.sin((angle+(360.0f/(float) segs))*Math.PI/180.0f);
			gl.glNormal3fv(vert, 0);
			gl.glVertex3fv(vert, 0);
			gl.glEnd();
			angle+=(360.0f/(float) segs);
		}
		for (i=0; i<segs/2-1; i++)
		{
			float h1, h2, r1, r2;

			h1=1.0f-(2.0f/(float) segs)*(i+1);
			r1=(float) Math.sqrt(1.0f-h1*h1);
			h2=1.0f-(2.0f/(float) segs)*(i+2);
			r2=(float) Math.sqrt(1.0f-h2*h2);
			angle=0.0f;
			for (j=0; j<segs; j++)
			{
				gl.glBegin(gl.GL_QUADS);
				vert[0]=r1*(float) Math.cos(angle*Math.PI/180.0f);
				vert[1]=h1;
				vert[2]=r1*(float) Math.sin(angle*Math.PI/180.0f);
				gl.glNormal3fv(vert, 0);
				gl.glVertex3fv(vert, 0);
				vert[0]=r2*(float) Math.cos(angle*Math.PI/180.0f);
				vert[1]=h2;
				vert[2]=r2*(float) Math.sin(angle*Math.PI/180.0f);
				gl.glNormal3fv(vert, 0);
				gl.glVertex3fv(vert, 0);
				angle+=360.0f/(float) segs;
				vert[0]=r2*(float) Math.cos(angle*Math.PI/180.0f);
				vert[1]=h2;
				vert[2]=r2*(float) Math.sin(angle*Math.PI/180.0f);
				gl.glNormal3fv(vert, 0);
				gl.glVertex3fv(vert, 0);
				vert[0]=r1*(float) Math.cos(angle*Math.PI/180.0f);
				vert[1]=h1;
				vert[2]=r1*(float) Math.sin(angle*Math.PI/180.0f);
				gl.glNormal3fv(vert, 0);
				gl.glVertex3fv(vert, 0);
				gl.glEnd();
			}
		}
		gl.glPopMatrix();
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }

    public void setDrawHistory(boolean state) {
        drawHistory = state;
    }

    public void setDraw2dBeachon(boolean state) {
        draw2dBeacon = state;
    }

    private float view_rotx = 0.0f, view_roty = 0.0f, view_rotz = 0.0f;
    private float old_rotx, old_roty, old_rotz;
    private int prevMouseX, prevMouseY;

    private MouseMotionListener mml = new MouseMotionAdapter() {

        @Override
        public void mouseDragged(MouseEvent e) {
            absolutDirection = new Vector3d(0, 0, 1);
            old_rotx = view_rotx;
            old_roty = view_roty;
            old_rotz = view_rotz;
//            view_rotx = view_roty = view_rotz = 0;

            int x = e.getX();
            int y = e.getY();
            
            Dimension size = e.getComponent().getSize();

            float thetaY = 360.0f * ( (float)(x-prevMouseX)/(float)size.width);
            float thetaX = 360.0f * ( (float)(prevMouseY-y)/(float)size.height);

            prevMouseX = x;
            prevMouseY = y;
//            System.out.println(e.getModifiers());
            // TODO replace number 18 by mask
            if (e.getModifiers() == 18) {
                view_rotz += thetaX;
            } else {
                view_roty += thetaY;
                view_rotx -= thetaX;
            }
            absolutDirection.rotate(-view_rotx, Vector3d.xAxis);
            absolutDirection.rotate(-view_roty, Vector3d.yAxis);
            absolutDirection.rotate(view_rotz, Vector3d.zAxis);
        }
    };

    private MouseListener ml = new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
            prevMouseX = e.getX();
            prevMouseY = e.getY();
        }
    };

    private double move_x = 0, move_y = 0, move_z = 0,
                    old_x = 0, old_y = 0, old_z = 0;

    private KeyListener kl = new KeyAdapter() {

        @Override
        public void keyPressed(KeyEvent e) {
            move_z = 0;
            
            switch (e.getKeyCode())  {
                case KeyEvent.VK_W:
                case KeyEvent.VK_UP:
                    if (e.getModifiers() == KeyEvent.CTRL_MASK) {
                        move_y -= 0.1;
                        break;
                    }
                    move_z += 0.1;
                    break;
                case KeyEvent.VK_S:
                case KeyEvent.VK_DOWN:
                    if (e.getModifiers() == KeyEvent.CTRL_MASK) {
                        System.out.println("ctrl");
                        move_y += 0.1;
                        break;
                    }
                    move_z -= 0.1;
                    break;
                case KeyEvent.VK_A:
                case KeyEvent.VK_LEFT:
                    move_x += 0.1;
                    break;
                case KeyEvent.VK_D:
                case KeyEvent.VK_RIGHT:
                    move_x -= 0.1;
                    break;
            }
            
//            old_x += move_x;
//            old_y += move_y;
            old_z += move_z;

//            Vector3d v = new Vector3d(0, 0, move_z);
//            System.out.println(move_x+" "+move_y+" "+move_z);
            Vector3d v = Vector3d.scalarTimesVector(move_z, absolutDirection);
            absolutPosition.add(v);

//            moveView(move_x, move_y, move_z);
//            moveView(old_x, old_y, old_z);
        }

    };

    private double zoom = 1, rezoom = 1;

    private MouseWheelListener mwl = new MouseWheelListener() {

        public void mouseWheelMoved(MouseWheelEvent e) {
            zoom += e.getWheelRotation();
            rezoom = 1/zoom;
        }
    };
}

