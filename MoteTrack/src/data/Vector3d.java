/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package data;

/**
 *
 * @author Scheinecker Thomas
 */
public class Vector3d {

    public static final Vector3d xAxis = new Vector3d(1, 0, 0),
                                    yAxis = new Vector3d(0, 1, 0),
                                    zAxis = new Vector3d(0, 0, 1);

    private double x, y, z;

    public Vector3d(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3d(Vector3d v) {
        x = v.x;
        y = v.y;
        z = v.z;
    }

    public Vector3d (double fromX, double fromY, double fromZ, double toX, double toY, double toZ) {
        this(toX - fromX, toY - fromY, toZ - fromZ);
    }

    public Vector3d(Position pos) {
        this(pos.getX(), pos.getY(), pos.getZ());
    }

    public Vector3d(Position from, Position to) {
        this(to.getX() - from.getX(), to.getY() - from.getY(), to.getZ() - from.getZ());
    }

    public double getLength() {
        return (Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2)));
    }

    public void rotate(double a, Vector3d v) {
        double x1 = x*(cos(a) + v.x*v.x*(1-cos(a))) + y*(v.x*v.y*(1-cos(a))-v.z*sin(a)) + z*(v.x*v.z*(1-cos(a))+v.y*sin(a));
        double y1 = x*(v.x*v.y*(1-cos(a))+v.z*sin(a)) + y*(cos(a) + v.y*v.y*(1-cos(a))) + z*(v.y*v.z*(1-cos(a))-v.x*sin(a));
        double z1 = x*(v.x*v.z*(1-cos(a))+v.y*sin(a)) + y*(v.y*v.z*(1-cos(a))+v.x*sin(a)) + z*(cos(a) + v.z*v.z*(1-cos(a)));

        x = x1;
        y = y1;
        z = z1;
    }

    public void add(Vector3d v) {
        x += v.x;
        y += v.y;
        z += v.z;
    }

    public void substract(Vector3d v) {
        x -= v.x;
        y -= v.y;
        z -= v.z;
    }

    // Rotating Angle around X-axis
    public double getPitch() {
        return getAngleBetween(projectToYZ(this), yAxis);
    }

    // Rotating Angle around Y-axis
    public double getRoll() {
        return getAngleBetween(projectToXZ(this), zAxis);
    }

    // Rotating Angle around Z-axis
    public double getYaw() {
        return getAngleBetween(projectToXY(this), xAxis);
    }


    public static Vector3d projectToXY(Vector3d v) {
        return new Vector3d(v.x, v.y, 0);
    }

    public static Vector3d projectToXZ(Vector3d v) {
        return new Vector3d(v.x, 0, v.z);
    }

    public static Vector3d projectToYZ(Vector3d v) {
        return new Vector3d(0, v.y, v.z);
    }

    public static double getAngleBetween(Vector3d v1, Vector3d v2) {
        return (scalarMultiply(v1, v2)/(v1.getLength()*v2.getLength()));
    }

    public static double scalarMultiply(Vector3d v1, Vector3d v2) {
        return (v1.x*v2.x + v1.y*v2.y + v1.z*v2.z);
    }

    private double cos(double angle) {
        if (angle == 90) return 0;
        return Math.cos(Math.toRadians(angle));
    }

    private double sin(double angle) {
        return Math.sin(Math.toRadians(angle));
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public static Vector3d scalarTimesVector(double d, Vector3d v) {
        return new Vector3d(d*v.x, d*v.y, d*v.z);
    }

    @Override
    public String toString() {
        return x+" | "+y+" | "+z;
    }

    
}

