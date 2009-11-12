/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package data;

/**
 *
 * @author Scheinecker Thomas
 */
public class Position {

    private double x, y, z;

    public Position(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
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

    public double[] toArray() {
        return new double[] {x, y, z};
    }

    @Override
    public String toString() {
        return x+" "+y+" "+z;
    }

    public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setZ(double z) {
		this.z = z;
	}
    public void addPercentage(double perc) {
    	this.x *= (1+perc);
    	this.y *= (1+perc);
    	this.z *= (1+perc);
    }
}
