package misc;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import data.Pattern;

public class HigherPatternMatcher implements Observer{
	private static HashMap<String,Long> map = new HashMap<String, Long>();
	
	public HigherPatternMatcher() {
		for (String pattern : Pattern.getStandardPatternNames()) {
			map.put(pattern,(long)0);
		}
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		if (arg1 instanceof String) {
			map.put((String)arg1, System.currentTimeMillis());
			get();
		}
	}
	private long diff(String name) {
		return System.currentTimeMillis()-map.get(name).longValue();
	}
	
	public LinkedList<String> get() {
		int timeDif=500;
		LinkedList<String> list = new LinkedList<String>();
		if (diff(Pattern.standardPatternNames[0])<timeDif && diff(Pattern.standardPatternNames[1])<timeDif) list.add("beide Arme ausgestreckt");
		if (diff(Pattern.standardPatternNames[2])<timeDif && diff(Pattern.standardPatternNames[3])<timeDif && diff(Pattern.standardPatternNames[5])<timeDif && diff(Pattern.standardPatternNames[6])<timeDif) list.add("beide Arme hoch");
		System.out.println("higher pattern "+list.size());
		return list;
	}

}
