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
    
    public static final int STARTED_RECORDING = 0;
    public static final int ALREADY_RECORDING = 1;
    public static final int SAVING_IN_PROGRESS = 2;


	private LinkedList<AnglePattern> list;
	private volatile boolean record=false;
	private volatile boolean saved=true;
	
	public PatternRecorder() {
		list = new LinkedList<AnglePattern>();
	}
	
	public int startRecording() {
		if (record) return ALREADY_RECORDING;
		if (!record && !saved) return SAVING_IN_PROGRESS;
		record = true;
		saved = false;
                return STARTED_RECORDING;
	}
	
	public void stopRecording() {
		record=false;
		ObjectOutputStream writer;
		//String fileName = JOptionPane.showInputDialog(null,"Enter filename where pattern should be stored","Enter filename", JOptionPane.QUESTION_MESSAGE);
		String patternName = JOptionPane.showInputDialog(null,"Enter pattern Name (file name will be '<pattern name>.ap'","patter name",JOptionPane.QUESTION_MESSAGE);
		String fileName = patternName+".ap";
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
