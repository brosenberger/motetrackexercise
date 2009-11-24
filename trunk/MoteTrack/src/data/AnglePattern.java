package data;

import java.io.Serializable;

public class AnglePattern implements Serializable {
	private double[] pattern;
	private static double range=0.15;
	
	public AnglePattern(int size) {
		pattern = new double[size];
	}
	
	public void setPatternAt(int pos, double value) {
		if (pos>=pattern.length || pos<0) return;
		pattern[pos]=value;
	}
	
	public double[] getPattern() {
		return pattern;
	}
	
	private boolean inRange(double x, double y) {
		return (Math.abs(x-y)<range);
	}
	
	public boolean match(AnglePattern p) {
		double[] pattern2 = p.getPattern();
		if (pattern2.length!=pattern.length) return false;
		for (int i=0;i<pattern.length;i++) {
			if (!inRange(pattern[i], pattern2[i])) return false;
		}
		return true;
	}
	public String toString() {
		String ret ="";
		for (int i=0;i<pattern.length;i++) {
			ret += ","+pattern[i];
		}
		return "("+ret.substring(1)+")";
	}
}
