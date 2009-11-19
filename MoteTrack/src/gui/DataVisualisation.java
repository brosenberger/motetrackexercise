package gui;

import data.Position;
import data.SensorData;
import data.Vector3d;
import java.awt.Color;
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
import java.util.Iterator;

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
public class DataVisualisation implements GLEventListener {

    private boolean drawHistory = true, 
                    draw2dBeacon = true,
                    drawPlane = false,
                    drawAxes = false,
                    detailedRoomPlan = true;
    private ArrayList<String> selectedTagIds;
    private ArrayList<String> selectedTag1Ids;
    private ArrayList<String> selectedTag2Ids;
    private double tagSize = 0.2;
    private HashMap<String, SensorData> actualSensorDatas;
    private int maxHistoryReadings = 20;

    public void setMaxHistoryReadings(int maxHistoryReadings) {
        this.maxHistoryReadings = maxHistoryReadings;
    }

    public void setDetailedRoomPlan(boolean detailedRoomPlan) {
        this.detailedRoomPlan = detailedRoomPlan;
    }

    public void setDrawAxes(boolean drawAxes) {
        this.drawAxes = drawAxes;
    }

    public void setTagSize(double size) {
        tagSize = size;
    }

    public void setSelectedTagIds(ArrayList<String> list) {
        selectedTagIds = list;
    }

    public void setSelectedFromTagIds(ArrayList<String> list) {
        selectedTag1Ids = list;
    }

    public void setSelectedToTagIds(ArrayList<String> list) {
        selectedTag2Ids = list;
    }

    public void setDrawPlane(boolean drawPlane) {
        this.drawPlane = drawPlane;
    }
    
    public void init(GLAutoDrawable drawable) {
        strafe = new Vector3d(-1,0,0);
        strafeCorr = new Vector3d(-1, 0, 0);
        absolutePosition = new Vector3d(0, 0, 0);
        absoluteDirection = new Vector3d(0, 0, 1);
        selectedTagIds = new ArrayList<String>();
        selectedTag1Ids = new ArrayList<String>();
        selectedTag2Ids = new ArrayList<String>();
        actualSensorDatas = SensorData.getData();
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

    private Vector3d absolutePosition;
    private Vector3d absoluteDirection;


    private void moveAbsolute(GL gl) {
        gl.glTranslated(absolutePosition.getX(), absolutePosition.getY(), absolutePosition.getZ());
//        System.out.println("pos: "+absolutPosition);
    }

    private void rotate(double pitch, double roll, double yaw, GL gl) {
        gl.glRotated(pitch, 1, 0, 0);
        gl.glRotated(roll, 0, 1, 0);
        gl.glRotated(yaw, 0, 0, 1);
    }

    private void resetAbsoluteValues() {
        absoluteDirection = new Vector3d(0, 0, 1);
        absolutePosition =  new Vector3d(0, 0, 0);
    }

    public void reset() {
        old_x = old_y = oldMoveForward = moveStrafe = move_y = moveForward = 0;
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
        gl.glTranslated(0, move_y, 0);
        gl.glTranslated(old_rotx, old_roty, old_rotz);
        moveAbsolute(gl);

        gl.glTranslated(0 , -1.8, -10);
        gl.glRotated(-90, 1, 0, 0);
        gl.glTranslated(-7, -3.5, 0);

        // END OF MOVING VIEWPOINT //


        // DRAW THE SCENE
        
        // Drawing tags
        actualSensorDatas = SensorData.getData();
        if (actualSensorDatas != null) {
            for (SensorData data : actualSensorDatas.values()) {
               // gl.glPushMatrix();
                gl.glColor3d(1, 1, 1);
                if (data != null) {
//                        System.out.println("data: "+data);
                    drawTag(data, gl);
                }
                //gl.glPopMatrix();
            }
        }

        setDrawingColor(gl, Color.GREEN);
        // Draw Cell1 Base
        gl.glBegin(GL.GL_LINE_LOOP);
            gl.glVertex3d( 8.028, 0.440, 0);
            gl.glVertex3d(14.298, 0.890, 0);
            gl.glVertex3d(12.558, 6.890, 0);
            gl.glVertex3d( 9.008, 6.910, 0);
        gl.glEnd();

        // Draw Cell1
//        gl.glBegin(GL.GL_LINE_LOOP);
//            gl.glVertex3d( 8.028, 0.440, 2.270);
//            gl.glVertex3d(14.298, 0.890, 2.250);
//            gl.glVertex3d(12.558, 6.890, 2.260);
//            gl.glVertex3d( 9.008, 6.910, 2.240);
//        gl.glEnd();

        // Draw Cell2 Base
        gl.glBegin(GL.GL_LINE_LOOP);
            gl.glVertex3d(0.436, 0.720, 0);
            gl.glVertex3d(6.295, 0.515, 0);
            gl.glVertex3d(6.744, 3.404, 0);
            gl.glVertex3d(0.512, 3.306, 0);
        gl.glEnd();

        // Draw Cell2
//        gl.glBegin(GL.GL_LINE_LOOP);
//            gl.glVertex3d(0.436, 0.720, 2.250);
//            gl.glVertex3d(6.295, 0.515, 2.286);
//            gl.glVertex3d(6.744, 3.404, 2.305)8+f
//            gl.glVertex3d(0.512, 3.306, 2.271);
//        gl.glEnd();

        if (detailedRoomPlan) {
            drawRooms(gl);
        } else {
            drawSimpleRooms(gl);
        }

        // Draw Coordinate Axes
        if (drawAxes) {
            drawCoordinateAxes(gl);
        }


        if (drawPlane) {
            drawWirePlane(50, 50, 0, 25, 25, Color.WHITE, gl);
        }

        // Remember that every push needs a pop; this one is paired with
        // rotating the entire gear assembly
        gl.glPopMatrix();

        // Flush all drawing operations to the graphics card
        gl.glFlush();
    }

    private void drawSimpleRooms(GL gl) {
        setDrawingColor(gl, Color.WHITE);

        // Draw Simple Room 1
        drawWireCube(new Position(7.06, 3.478, 3.5), Color.WHITE, gl);

        // Draw Simple Room 2
        gl.glPushMatrix();
        gl.glTranslated(7.56, 0, 0);
        drawWireCube(new Position(7.308, 7.32, 3.5), Color.WHITE, gl);
        gl.glPopMatrix();
    }

    private double depthDoor1 = 0.5, depthDoor2 = 0.1, depthDoor3 = 0.15, depthWallUp = 0.1;
    private double door1up = 6.5, door1down = 5.6, door2left = 5.7, door2right = 6.6, door3up = door1up, door3down = door1down;
    private void drawRooms(GL gl) {
        double z = 0;
        setDrawingColor(gl, Color.WHITE);
  
        drawFloor(gl);
        drawDoorways(gl);
        drawCeilings(gl);
        drawEdges(gl);
    }

    private void drawFloor(GL gl) {
        double z = 0;
      // draw floor
        gl.glBegin(GL.GL_LINE_LOOP);
            gl.glVertex3d(0, 0, z);
            gl.glVertex3d(7.06, 0, z);
            gl.glVertex3d(7.06, 3.478, z);
            // door 2 right
            gl.glVertex3d(door2right, 3.478, z);
            gl.glVertex3d(door2right, 3.478+depthDoor2, z);
            // gangway lower right
            gl.glVertex3d(7.06, 3.478+depthDoor2, z);
            // door 1 down
            gl.glVertex3d(7.06, door1down, z);
            gl.glVertex3d(7.56, door1down, z);
            gl.glVertex3d(7.56, 0, z);
            gl.glVertex3d(7.56+7.308, 0, z);
            gl.glVertex3d(7.56+7.308, 7.32, z);
            gl.glVertex3d(7.56, 7.32, z);
            // door 1 up
            gl.glVertex3d(7.56, door1up, z);
            gl.glVertex3d(7.06, door1up, z);
            // gangway upper right
            gl.glVertex3d(7.06, 7.32+depthWallUp, z);
            // outer upper right
            gl.glVertex3d(7.56+7.308+0.5, 7.32+depthWallUp, z);

            gl.glVertex3d(7.56+7.308+0.5, -0.5, z);
            gl.glVertex3d(-0.5, -0.5, z);
            gl.glVertex3d(-0.5, 3.478+depthDoor2+3.9+depthWallUp, z);
            // gangway upper left
            gl.glVertex3d(5.25+depthDoor3, 3.478+depthDoor2+3.9+depthWallUp, z);
            // door 3 up
            gl.glVertex3d(5.25+depthDoor3, door3up, z);
            gl.glVertex3d(5.25, door3up, z);
            gl.glVertex3d(5.25, 3.478+depthDoor2+3.9, z);
            gl.glVertex3d(0, 3.478+depthDoor2+3.9, z);
            gl.glVertex3d(0, 3.478+depthDoor2, z);
            gl.glVertex3d(5.25, 3.478+depthDoor2, z);
            // door 3 down
            gl.glVertex3d(5.25, door3down, z);
            gl.glVertex3d(5.25+depthDoor3, door3down, z);
            // gangway lower left
            gl.glVertex3d(5.25+depthDoor3, 3.478+depthDoor2, z);
            // door 2 left
            gl.glVertex3d(door2left, 3.478+depthDoor2, z);
            gl.glVertex3d(door2left, 3.478, z);

            gl.glVertex3d(0, 3.478, z);
        gl.glEnd();
    }

    private void drawDoorways(GL gl) {
        double z = 2.5;
        // draw doorway ceilings
        // door 1
        gl.glBegin(GL.GL_LINE_LOOP);
            gl.glVertex3d(7.06, door1down, z);
            gl.glVertex3d(7.56, door1down, z);
            gl.glVertex3d(7.56, door1up, z);
            gl.glVertex3d(7.06, door1up, z);
        gl.glEnd();

        // door2
        gl.glBegin(GL.GL_LINE_LOOP);
            gl.glVertex3d(door2right, 3.478, z);
            gl.glVertex3d(door2right, 3.478+depthDoor2, z);
            gl.glVertex3d(door2left, 3.478+depthDoor2, z);
            gl.glVertex3d(door2left, 3.478, z);
        gl.glEnd();

        // door 3
        gl.glBegin(GL.GL_LINE_LOOP);
            gl.glVertex3d(5.25+depthDoor3, door3up, z);
            gl.glVertex3d(5.25, door3up, z);
            gl.glVertex3d(5.25, door3down, z);
            gl.glVertex3d(5.25+depthDoor3, door3down, z);
        gl.glEnd();

        // draw doorway edges
        gl.glBegin(GL.GL_LINES);
            // door 1
            gl.glVertex3d(7.06, door1down, 0);
            gl.glVertex3d(7.06, door1down, z);

            gl.glVertex3d(7.56, door1down, 0);
            gl.glVertex3d(7.56, door1down, z);

            gl.glVertex3d(7.56, door1up, 0);
            gl.glVertex3d(7.56, door1up, z);

            gl.glVertex3d(7.06, door1up, 0);
            gl.glVertex3d(7.06, door1up, z);

            // door2
            gl.glVertex3d(door2right, 3.478, 0);
            gl.glVertex3d(door2right, 3.478, z);

            gl.glVertex3d(door2right, 3.478+depthDoor2, 0);
            gl.glVertex3d(door2right, 3.478+depthDoor2, z);

            gl.glVertex3d(door2left, 3.478+depthDoor2, 0);
            gl.glVertex3d(door2left, 3.478+depthDoor2, z);

            gl.glVertex3d(door2left, 3.478, 0);
            gl.glVertex3d(door2left, 3.478, z);

            // door 3
            gl.glVertex3d(5.25+depthDoor3, door3up, 0);
            gl.glVertex3d(5.25+depthDoor3, door3up, z);

            gl.glVertex3d(5.25, door3up, 0);
            gl.glVertex3d(5.25, door3up, z);

            gl.glVertex3d(5.25, door3down, 0);
            gl.glVertex3d(5.25, door3down, z);

            gl.glVertex3d(5.25+depthDoor3, door3down, 0);
            gl.glVertex3d(5.25+depthDoor3, door3down, z);
        gl.glEnd();
    }

    private void drawCeilings(GL gl) {
        // draw generall ceiling
        double z = 4;
        gl.glBegin(GL.GL_LINE_LOOP);
            gl.glVertex3d(-0.5, -0.5, z);
            gl.glVertex3d(7.56+7.308+0.5, -0.5, z);
            gl.glVertex3d(7.56+7.308+0.5, 7.32+depthWallUp, z);
            gl.glVertex3d(5.25+depthDoor3, 3.478+depthDoor2+3.9+depthWallUp, z);
            gl.glVertex3d(-0.5, 3.478+depthDoor2+3.9+depthWallUp, z);
        gl.glEnd();

        z = 3.5;
        // draw ceiling room 1
        gl.glBegin(GL.GL_LINE_LOOP);
            gl.glVertex3d(7.56, 0, z);
            gl.glVertex3d(7.06+0.5+7.308, 0, z);
            gl.glVertex3d(7.06+0.5+7.308, 7.32, z);
            gl.glVertex3d(7.56, 7.32, z);
        gl.glEnd();

        // draw ceiling room 2
        gl.glBegin(GL.GL_LINE_LOOP);
            gl.glVertex3d(0, 0, z);
            gl.glVertex3d(7.06, 0, z);
            gl.glVertex3d(7.06, 3.478, z);
            gl.glVertex3d(0, 3.478, z);
        gl.glEnd();

        // draw ceiling room 3
        gl.glBegin(GL.GL_LINE_LOOP);
            gl.glVertex3d(0, 3.478+depthDoor2, z);
            gl.glVertex3d(5.25, 3.478+depthDoor2, z);
            gl.glVertex3d(5.25, 3.478+depthDoor2+3.9, z);
            gl.glVertex3d(0, 3.478+depthDoor2+3.9, z);
        gl.glEnd();

        // draw ceiling gangway
        gl.glBegin(GL.GL_LINE_LOOP);
            gl.glVertex3d(5.25+depthDoor3, 3.478+depthDoor2, z);
            gl.glVertex3d(7.06, 3.478+depthDoor2, z);
            gl.glVertex3d(7.06, 7.32+depthWallUp, z);
            gl.glVertex3d(5.25+depthDoor3, 3.478+depthDoor2+3.9+depthWallUp, z);
        gl.glEnd();
    }

    private void drawEdges(GL gl) {
        double z = 3.5;
        // draw edges
        gl.glBegin(GL.GL_LINES);
            // building
            gl.glVertex3d(-0.5, -0.5, 0);
            gl.glVertex3d(-0.5, -0.5, 4);

            gl.glVertex3d(7.56+7.308+0.5, -0.5, 0);
            gl.glVertex3d(7.56+7.308+0.5, -0.5, 4);

            gl.glVertex3d(7.56+7.308+0.5, 7.32+depthWallUp, 0);
            gl.glVertex3d(7.56+7.308+0.5, 7.32+depthWallUp, 4);

//            gl.glVertex3d(5.25+depthDoor3, 3.478+depthDoor2+3.9+depthWallUp, 0);
//            gl.glVertex3d(5.25+depthDoor3, 3.478+depthDoor2+3.9+depthWallUp, 4);

            gl.glVertex3d(-0.5, 3.478+depthDoor2+3.9+depthWallUp, 0);
            gl.glVertex3d(-0.5, 3.478+depthDoor2+3.9+depthWallUp, 4);

            // rooms
            // room 1
            gl.glVertex3d(7.56, 0, 0);
            gl.glVertex3d(7.56, 0, z);

            gl.glVertex3d(7.06+0.5+7.308, 0, 0);
            gl.glVertex3d(7.06+0.5+7.308, 0, z);

            gl.glVertex3d(7.06+0.5+7.308, 7.32, 0);
            gl.glVertex3d(7.06+0.5+7.308, 7.32, z);

            gl.glVertex3d(7.56, 7.32, 0);
            gl.glVertex3d(7.56, 7.32, z);

            // room 2
            gl.glVertex3d(0, 0, 0);
            gl.glVertex3d(0, 0, 3.5);

            gl.glVertex3d(7.06, 0, 0);
            gl.glVertex3d(7.06, 0, z);

            gl.glVertex3d(7.06, 3.478, 0);
            gl.glVertex3d(7.06, 3.478, z);

            gl.glVertex3d(0, 3.478, 0);
            gl.glVertex3d(0, 3.478, z);

            // room 3
            gl.glVertex3d(0, 3.478+depthDoor2, 0);
            gl.glVertex3d(0, 3.478+depthDoor2, z);

            gl.glVertex3d(5.25, 3.478+depthDoor2, 0);
            gl.glVertex3d(5.25, 3.478+depthDoor2, z);

            gl.glVertex3d(5.25, 3.478+depthDoor2+3.9, 0);
            gl.glVertex3d(5.25, 3.478+depthDoor2+3.9, z);

            gl.glVertex3d(0, 3.478+depthDoor2+3.9, 0);
            gl.glVertex3d(0, 3.478+depthDoor2+3.9, z);

            // gangway
            gl.glVertex3d(5.25+depthDoor3, 3.478+depthDoor2, 0);
            gl.glVertex3d(5.25+depthDoor3, 3.478+depthDoor2, z);

            gl.glVertex3d(7.06, 3.478+depthDoor2, 0);
            gl.glVertex3d(7.06, 3.478+depthDoor2, z);

            gl.glVertex3d(7.06, 7.32+depthWallUp, 0);
            gl.glVertex3d(7.06, 7.32+depthWallUp, z);

            gl.glVertex3d(5.25+depthDoor3, 3.478+depthDoor2+3.9+depthWallUp, 0);
            gl.glVertex3d(5.25+depthDoor3, 3.478+depthDoor2+3.9+depthWallUp, z);

        gl.glEnd();
    }

    private void drawStrafingInfo(GL gl) {
        gl.glBegin(GL.GL_LINES);
            setDrawingColor(gl, Color.PINK);
            gl.glVertex3d(-absoluteDirection.getX(), absoluteDirection.getZ(), -absoluteDirection.getY());
            gl.glVertex3d(0, 0, 0);
            setDrawingColor(gl, Color.yellow);
            gl.glVertex3d(-strafe.getX(), strafe.getZ(), -strafe.getY());
            gl.glVertex3d(0, 0, 0);
            setDrawingColor(gl, Color.ORANGE);
            gl.glVertex3d(-strafeCorr.getX(), strafeCorr.getZ(), -strafeCorr.getY());
            gl.glVertex3d(0, 0, 0);
        gl.glEnd();
    }

    private void drawWirePlane(double width, double depth, double z, int splitX, int splitY, Color color, GL gl) {
        setDrawingColor(gl, color);
        double widthHalf = width/2;
        double depthHalf = depth/2;
        double x = -widthHalf;
        double y = depthHalf;
        double xAdd = width/splitX;
        double yAdd = depth/splitY;

        gl.glBegin(GL.GL_LINES);
        
        while (x <= widthHalf) {
            gl.glVertex3d(x, y, z);
            gl.glVertex3d(x, -y, z);
            x += xAdd;
        }

        x = widthHalf;

        while (y >= -depthHalf) {
            gl.glVertex3d(x, y, z);
            gl.glVertex3d(-x, y, z);
            y -= yAdd;
        }
        
        gl.glEnd();
    }

    private void drawWireCube(Position from, Position to, Color color, GL gl) {
        gl.glPushMatrix();

        double from_x = from.getX(), from_y = from.getY(), from_z = from.getZ();
        double to_x = to.getX() - from_x;
        double to_y = to.getY() - from_y;
        double to_z = to.getZ() - from_z;

        gl.glTranslated(from_x, from_y, from_z);
        drawWireCube(new Position(to_x, to_y, to_z), color, gl);

        gl.glPopMatrix();
    }

    // draws a wired cube from (0, 0, 0) to (Pos.x, Pos.y, Pos.z)
    private void drawWireCube(Position pos, Color color, GL gl) {
        double x = pos.getX(), y = pos.getY(), z = pos.getZ();

        setDrawingColor(gl, color);

        // draw floor (front)
        gl.glBegin(GL.GL_LINE_LOOP);
            gl.glVertex3d(0, 0, 0);
            gl.glVertex3d(x, 0, 0);
            gl.glVertex3d(x, y, 0);
            gl.glVertex3d(0, y, 0);
        gl.glEnd();

        // draw ceiling (back)
        gl.glBegin(GL.GL_LINE_LOOP);
            gl.glVertex3d(0, 0, z);
            gl.glVertex3d(x, 0, z);
            gl.glVertex3d(x, y, z);
            gl.glVertex3d(0, y, z);
        gl.glEnd();

        // draw edges
        gl.glBegin(GL.GL_LINES);
            gl.glVertex3d(0, 0, 0);
            gl.glVertex3d(0, 0, z);

            gl.glVertex3d(x, 0, 0);
            gl.glVertex3d(x, 0, z);

            gl.glVertex3d(x, y, 0);
            gl.glVertex3d(x, y, z);

            gl.glVertex3d(0, y, 0);
            gl.glVertex3d(0, y, z);
        gl.glEnd();
    }

    private void zoom(double zoom, GL gl) {
        gl.glScaled(zoom, zoom, zoom);
    }

    private void drawCoordinateAxes(GL gl) {
        gl.glBegin(GL.GL_LINES);
            setDrawingColor(gl, Color.RED);
            gl.glVertex3d(0, 0, 0);
            gl.glVertex3d(5, 0, 0);
            setDrawingColor(gl, Color.GREEN);
            gl.glVertex3d(0, 0, 0);
            gl.glVertex3d(0, 5, 0);
            setDrawingColor(gl, Color.BLUE);
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


        int historySize = history.size();
        for (int i = 0, j = historySize-1; j >= 0 && i < maxHistoryReadings; i++, j--) {
            Position actPos = history.get(j).getPos();
            if (actPos != null) {
                gl.glVertex3d(actPos.getX(), actPos.getY(), actPos.getZ());
            }
        }
//            for (SensorData actData : data.getHistory()) {
//                Position actPos = actData.getPos();
//                if (actPos != null) {
//                    gl.glVertex3d(actPos.getX(), actPos.getY(), actPos.getZ());
//                }
//            }
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

        String id = data.getId();
        if (selectedTag1Ids.contains(id)) {
            setDrawingColor(gl, Color.BLUE);
        } else if (selectedTag2Ids.contains(id)) {
            setDrawingColor(gl, Color.YELLOW);
        } else if (selectedTagIds.contains(id)) {
            setDrawingColor(gl, Color.RED);
        } else {
            setDrawingColor(gl, Color.WHITE);
        }

        // draw 2d beacon
        if (draw2dBeacon) {
            draw2dBeacon(pos, gl);
        }
        
        //draw Connections to other tags
        drawConnections(data,gl);

        // drawing sphere
        gl.glPushMatrix();
        moveTo(posArr, gl);
        gl.glScaled(tagSize, tagSize, tagSize);
        drawSphere(25, gl);
        gl.glPopMatrix();
    }
    
    private void drawConnections(SensorData sd, GL gl) {
        if (!SensorData.getConnectedTags().containsKey(sd.getId())) {
            return;
        }

    	Iterator<String> it = SensorData.getConnectedTags().get(sd.getId()).iterator();
    	SensorData t;
    	gl.glBegin(gl.GL_LINES);
    	while (it.hasNext()) {
    		t = actualSensorDatas.get(it.next());
                if (t != null) {
                    gl.glVertex3d(sd.getPos().getX(),sd.getPos().getY(),sd.getPos().getZ());
                    gl.glVertex3d(t.getPos().getX(),t.getPos().getY(),t.getPos().getZ());
                }
    	}
    	gl.glEnd();
    }

    private void setDrawingColor(GL gl, Color color) {
        gl.glColor4fv(color.getRGBComponents(null), 0);
    }

    private void draw2dBeacon(Position pos, GL gl) {
        double x = pos.getX(), y = pos.getY(), z = pos.getZ();
        gl.glBegin(gl.GL_LINES);
            gl.glVertex3d(x, y, z);
            gl.glVertex3d(x, y, 0);

            gl.glVertex3d(x - 0.1, y, 0);
            gl.glVertex3d(x + 0.1, y, 0);

            gl.glVertex3d(x, y - 0.1, 0);
            gl.glVertex3d(x, y + 0.1, 0);
        gl.glEnd();
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
            absoluteDirection = new Vector3d(0, 0, 1);
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
////             TODO replace number 18 by mask
//            if (e.getModifiers() == 18) {
//                view_rotz += thetaX;
//            } else {
                view_roty += thetaY;
                view_rotx -= thetaX;
//            }
            absoluteDirection.rotate(-view_rotx, Vector3d.xAxis);
            absoluteDirection.rotate(-view_roty, Vector3d.yAxis);
//            absolutDirection.rotate(view_rotz, Vector3d.zAxis);

            calcStrafeVectors();
        }
    };

    private MouseListener ml = new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
            prevMouseX = e.getX();
            prevMouseY = e.getY();
        }
    };

    private double moveStrafe = 0, move_y = 0, moveForward = 0,
                    old_x = 0, old_y = 0, oldMoveForward = 0;

    private KeyListener kl = new KeyAdapter() {

        @Override
        public void keyPressed(KeyEvent e) {
            moveForward = 0;
            moveStrafe = 0;
            
            switch (e.getKeyCode())  {
                case KeyEvent.VK_W:
                case KeyEvent.VK_UP:
                    moveForward += 0.1;
                    break;
                case KeyEvent.VK_S:
                case KeyEvent.VK_DOWN:
                    moveForward -= 0.1;
                    break;
                case KeyEvent.VK_A:
                case KeyEvent.VK_LEFT:
                    moveStrafe += 0.1;
                    break;
                case KeyEvent.VK_D:
                case KeyEvent.VK_RIGHT:
                    moveStrafe -= 0.1;
                    break;
                case KeyEvent.VK_SPACE:
                    move_y -= 0.1;
                    break;
                case KeyEvent.VK_C:
                    move_y += 0.1;
                    break;
            }
            
//            old_x += move_x;
//            old_y += move_y;
            oldMoveForward += moveForward;

//            Vector3d v = new Vector3d(0, 0, move_z);
//            System.out.println(move_x+" "+move_y+" "+move_z);
            Vector3d v = Vector3d.scalarTimesVector(moveForward, absoluteDirection);
            absolutePosition.add(v);

            absolutePosition.add(Vector3d.scalarTimesVector(moveStrafe, strafeCorr));

//            moveView(move_x, move_y, move_z);
//            moveView(old_x, old_y, old_z);
        }

    };

    private void calcStrafeVectors() {
//        System.out.println("absolute: "+absoluteDirection);
         strafe = Vector3d.normalizeVector(Vector3d.projectToXZ(absoluteDirection));
//         System.out.println("strafe: "+strafe);
        // rotate counterclockwise
        strafeCorr = new Vector3d(strafe.getZ(), 0, -strafe.getX());
//        System.out.println("corr: "+strafeCorr);
    }

    private Vector3d strafe, strafeCorr;

    private double zoom = 1, rezoom = 1;

    private MouseWheelListener mwl = new MouseWheelListener() {

        public void mouseWheelMoved(MouseWheelEvent e) {
            zoom += e.getWheelRotation();
            rezoom = 1/zoom;
        }
    };
}

