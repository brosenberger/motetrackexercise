package misc;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
	
	public PatternRecorder() {
		list = new LinkedList<AnglePattern>();
	}
	
	public void startRecording() {
		if (record) return;
		record = true;
	}
	
	public void stopRecording() {
		ObjectOutputStream writer;
		String fileName = JOptionPane.showInputDialog(null,"Enter filename where pattern should be stored","Enter filename", JOptionPane.QUESTION_MESSAGE);
		String patternName = JOptionPane.showInputDialog(null,"Enter pattern Name","patter name",JOptionPane.QUESTION_MESSAGE);
		try {
			writer = new ObjectOutputStream(new FileOutputStream("./patterns/"+fileName,true));
			writer.writeObject(patternName);
			Iterator<AnglePattern> it = list.iterator();
			while (it.hasNext()) {
				writer.writeObject(it.next());
			}
			writer.flush();
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		if (arg1 instanceof AnglePattern && record) {
			list.add((AnglePattern) arg1);
		}
	}

}
