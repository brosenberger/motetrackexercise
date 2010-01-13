package data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class AnglePattern implements Serializable {
	private ArrayList<PositionEnum> patternList;
	private double[] pattern;
	private static double range=0.15;
	
	public AnglePattern(ArrayList<PositionEnum> patternList) {
		this.patternList = patternList;
		pattern = new double[patternList.size()/3];
	}
	
	public void setPatternAt(int pos, double value) {
		if (pos>=pattern.length || pos<0) return;
		pattern[pos]=value;
	}
		
	public double[] getPattern() {
		return pattern;
	}
	
	public ArrayList<PositionEnum> getPatternList() {
		return patternList;
	}
	
	private boolean inRange(double x, double y) {
		return (Math.abs(x-y)<range);
	}
	
	private boolean matchPattern(ArrayList<PositionEnum> a1,ArrayList<PositionEnum> a2) {
		if (a1.size()!=a2.size()) return false;
		Iterator<PositionEnum> i1, i2;
		i1 = a1.iterator();
		i2 = a2.iterator();
		while (i1.hasNext()) {
			if (i1.next()!=i2.next()) return false;
		}
		return true;
	}
	
	public boolean match(AnglePattern p) {
		if (matchPattern(p.getPatternList(),this.patternList)) return false;
		double[] pattern2 = p.getPattern();
		//if (pattern2.length!=pattern.length) return false;
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
