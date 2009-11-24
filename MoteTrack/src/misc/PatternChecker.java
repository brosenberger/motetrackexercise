package misc;

import java.util.LinkedList;
import java.util.Observable;

import data.AnglePattern;

public class PatternChecker extends Observable implements Runnable {
	private volatile boolean run=true;
	private volatile LinkedList<AnglePattern> actPattern=new LinkedList<AnglePattern>();
	private AnglePattern[] observePattern = null;
	private String patternName;
	
	public PatternChecker(String patternName) {
		this.patternName = patternName;
	}
	
	public String getPatternName() {
		return this.patternName;
	}
	
	public void checkPattern(AnglePattern pat) {
		this.actPattern.addLast(pat);
	}

	public void setObservePattern(Object[] array) {
		this.observePattern=(AnglePattern[]) array;
	}
	
	public void startObservation() {
		Thread t = new Thread(this);
		t.setDaemon(true);
		t.start();
	}
	
	public void stopObservation() {
		this.run = false;
	}

	@Override
	public void run() {
		AnglePattern toCheck;
		while (run) {
			if (actPattern.size()>0) {
				toCheck = actPattern.removeFirst();
				for (int i=0;i<observePattern.length;i++) {
					if (toCheck.match(observePattern[i])) {
						this.setChanged();
						this.notifyObservers(this.patternName);
						break;
					}
				}
			}
		}
	}
}
