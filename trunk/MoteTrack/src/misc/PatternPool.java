package misc;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import data.AnglePattern;
import data.SensorData;

public class PatternPool extends Observable implements Observer {
	LinkedList<PatternChecker> patternChecker;
	
	public PatternPool() {
		patternChecker = new LinkedList<PatternChecker>();
	}
	
	public void loadPattern(String fileName) {
		ObjectInputStream reader=null;
		LinkedList<AnglePattern> list = new LinkedList<AnglePattern>();
		String patternName="";
		try {
			reader = new ObjectInputStream(new FileInputStream(fileName));
			Object obj = null;
			while ((obj=reader.readObject())!=null) {
				if (obj instanceof String) {
					if (patternName.length()>0) {
						storeAndActivatePattern(patternName,list);
						patternName = (String) obj;
						list.clear();
					}
				}else if (obj instanceof AnglePattern) {
					list.add((AnglePattern) obj);
				}
			}
		} catch (EOFException e) {
			System.out.println("end of pattern read");
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
		check.setObservePattern(list.toArray(null));
		check.startObservation();
		this.patternChecker.add(check);
	}

	private void distributePattern(AnglePattern arg1) {
		Iterator<PatternChecker> it = patternChecker.iterator();
		while (it.hasNext()) {
			it.next().checkPattern(arg1);
		}
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		if (arg0 instanceof SensorData) {
			this.distributePattern((AnglePattern) arg1);
		} else if (arg0 instanceof PatternChecker) {
			this.setChanged();
			this.notifyObservers(arg1);
		}
	}

}
