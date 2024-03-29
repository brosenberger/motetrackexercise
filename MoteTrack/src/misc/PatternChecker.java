package misc;

import java.util.LinkedList;
import java.util.Observable;

import data.AnglePattern;
import java.util.NoSuchElementException;

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
	//	System.out.println("add pattern to compare");
                for (int i=0;i<observePattern.length;i++) {
                        if (pat.match(observePattern[i])) {
                                this.setChanged();
                                this.notifyObservers(this.patternName);
//                                System.out.println("pattern matched "+this.patternName);
                                break;
                        }
                }
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
//		AnglePattern toCheck;
//		System.out.println("start checking "+this.patternName);
//		while (run) {
//			if (actPattern.size()>0) {
//				try {
//                                    toCheck = actPattern.removeFirst();
//                                    for (int i=0;i<observePattern.length;i++) {
//                                            if (toCheck.match(observePattern[i])) {
//                                                    this.setChanged();
//                                                    this.notifyObservers(this.patternName);
////                                                    System.out.println("pattern matched "+this.patternName);
//                                                    break;
//                                            }
//                                    }
//                                } catch (NoSuchElementException e) {}
//			}
//		}
	}
}
