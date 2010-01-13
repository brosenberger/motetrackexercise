package misc;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import data.*;

public class PatternPool extends Observable implements Observer {
	LinkedList<PatternChecker> patternChecker;
	
	public PatternPool() {
		patternChecker = new LinkedList<PatternChecker>();
	}
	/*
	private void loadPattern(PositionEnum l, PositionEnum anglePoint, PositionEnum r, double angle, String name) {
		ArrayList<PositionEnum> list= new ArrayList<PositionEnum>();
		list.add(l); list.add(anglePoint); list.add(r);
		AnglePattern p = new AnglePattern(list);
		p.setPatternAt(0, angle);
		LinkedList<AnglePattern> listAnglePattern = new LinkedList<AnglePattern>();
		listAnglePattern.add(p);
		storeAndActivatePattern(name, listAnglePattern);
	}
	*/
	public void loadPattern() {
		for (String pName : Pattern.getStandardPatternNames()) {
			storeAndActivatePattern(pName, Pattern.getPattern(pName));
        }
		//storeAndActivatePattern("linkerArmAusgestreckt",Pattern.getPattern("linkerArmAusgestreckt"));
		//storeAndActivatePattern("rechterArmAusgestreckt",Pattern.getPattern("rechterArmAusgestreckt"));
		//loadPattern(PositionEnum.leftWrist,PositionEnum.leftShoulder,PositionEnum.rightShoulder, 180,"linkerArmAusgestreckt");
	}
	
	public void loadPattern(String fileName) {
		ObjectInputStream reader=null;
		ObjectOutputStream out=null;
		int i=0;
		LinkedList<AnglePattern> list = new LinkedList<AnglePattern>();
		String patternName="";
		try {
			reader = new ObjectInputStream(new FileInputStream(fileName));
			//out = new ObjectOutputStream(new FileOutputStream(fileName,true));
			Object obj = null;
			while ((obj=reader.readObject())!=null) {
				if (obj instanceof String) {
					if (patternName.length()>0) {
						storeAndActivatePattern(patternName,list);
						System.out.println("pattern "+patternName+" added");
						i++;
						patternName = (String) obj;
						list.clear();
					} else { //first iteration
						patternName = (String) obj;
					}
				}else if (obj instanceof AnglePattern) {
					list.add((AnglePattern) obj);
                                        System.out.println("Angle Pattern: "+((AnglePattern) obj).toString());
				}
			}
		} catch (EOFException e) {
			System.out.println("pattern "+patternName+" added");
			storeAndActivatePattern(patternName,list);
		}	catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
				//out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void removePattern(String patternName) {
		for (int i=0;i<this.patternChecker.size();i++) {
			if (this.patternChecker.get(i).getPatternName().equalsIgnoreCase(patternName)) {
				this.patternChecker.remove(i);
				return;
			}
		}
	}
	
	private void storeAndActivatePattern(String patternName,LinkedList<AnglePattern> list) {
		PatternChecker check = new PatternChecker(patternName);
		AnglePattern[] arr = new AnglePattern[1];
		check.setObservePattern(arr = list.toArray(arr));
		check.startObservation();
		this.patternChecker.add(check);
		list.clear();
	}

	private void distributePattern(AnglePattern arg1) {
		Iterator<PatternChecker> it = patternChecker.iterator();
		while (it.hasNext()) {
			it.next().checkPattern(arg1);
		}
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		if (arg0 instanceof DummyObservable) {
		//	System.out.print("pattern to check, now distributing");
			this.distributePattern((AnglePattern) arg1);
		} else if (arg0 instanceof PatternChecker) {
			this.setChanged();
			this.notifyObservers(arg1);
		}
	}

}
