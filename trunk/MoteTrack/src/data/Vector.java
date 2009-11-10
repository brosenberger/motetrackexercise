/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package data;

/**
 *
 * @author Scheinecker Thomas
 */
public class Vector {

    private double x, y, z;

    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector (double fromX, double fromY, double fromZ, double toX, double toY, double toZ) {
        this(toX - fromX, toY - fromY, toZ - fromZ);
    }

    public Vector(Position pos) {
        this(pos.getX(), pos.getY(), pos.getZ());
    }

    public Vector(Position from, Position to) {
        this(to.getX() - from.getX(), to.getY() - from.getY(), to.getZ() - from.getZ());
    }

    public double getLength() {
        return (Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2)));
    }
}
