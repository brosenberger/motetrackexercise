package misc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JOptionPane;

import data.AnglePattern;

public class PatternRecorder implements Observer {
	private LinkedList<AnglePattern> list;
	private volatile boolean record=false;
	private volatile boolean saved=true;
	
	public PatternRecorder() {
		list = new LinkedList<AnglePattern>();
	}
	
	public void startRecording() {
		if (record) return;
		if (!record && !saved) return;
		record = true;
		saved = false;
	}
	
	public void stopRecording() {
		record=false;
		ObjectOutputStream writer;
		String fileName = JOptionPane.showInputDialog(null,"Enter filename where pattern should be stored","Enter filename", JOptionPane.QUESTION_MESSAGE);
		String patternName = JOptionPane.showInputDialog(null,"Enter pattern Name","patter name",JOptionPane.QUESTION_MESSAGE);
		try {
			writer = new ObjectOutputStream(new FileOutputStream("./patterns/"+fileName,true));
			writer.writeObject(patternName);
			System.out.println("wrote patternname "+patternName);
			Iterator<AnglePattern> it = list.iterator();
			while (it.hasNext()) {
				AnglePattern p = it.next();
				writer.writeObject(p);
				System.out.println("wrote pattern: "+p);
			}
			writer.flush();
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			saved = true;
		}
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		if (arg1 instanceof AnglePattern && record) {
			list.add((AnglePattern) arg1);
		}
	}

}
