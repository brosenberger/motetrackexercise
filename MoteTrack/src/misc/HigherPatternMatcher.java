package misc;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import data.Pattern;
import gui.MoteTrackApp;

public class HigherPatternMatcher implements Observer{
	private static HashMap<String,Long> map = new HashMap<String, Long>(),
                                            move = new HashMap<String, Long>();

        private static final String[] highPatterns = {"beide Arme ausgestreckt", "beide Arme hoch"};
        private static MoteTrackApp moteApp;
        private static int count = 0;

	public HigherPatternMatcher(MoteTrackApp app) {
		for (String pattern : Pattern.getStandardPatternNames()) {
			map.put(pattern,(long)0);
		}

                for(String pattern : highPatterns) {
                    move.put(pattern,(long)0);
                }

                moteApp = app;

                movements.add("");
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		if (arg1 instanceof String) {
			map.put((String)arg1, System.currentTimeMillis());
//                        System.out.println("HPM: updated");
			get();
		}
	}
	private long diff(String name) {
		return System.currentTimeMillis()-map.get(name).longValue();
	}
	
	public LinkedList<String> get() {
		int timeDif=1000;
		LinkedList<String> list = new LinkedList<String>();
		if (diff(Pattern.standardPatternNames[0])<timeDif && diff(Pattern.standardPatternNames[1])<timeDif){
                    list.add("beide Arme ausgestreckt");
                    detectMovement("beide Arme ausgestreckt");
                    
                } else if (diff(Pattern.standardPatternNames[2])<timeDif && diff(Pattern.standardPatternNames[3])<timeDif && diff(Pattern.standardPatternNames[5])<timeDif && diff(Pattern.standardPatternNames[6])<timeDif) {
                    list.add("beide Arme hoch");
                    detectMovement("beide Arme hoch");
                } else if (diff(Pattern.standardPatternNames[2])<timeDif && diff(Pattern.standardPatternNames[3])<timeDif && diff(Pattern.standardPatternNames[4])<timeDif && diff(Pattern.standardPatternNames[7])<timeDif) {
                    list.add("beide Arme tief");
                    detectMovement("beide Arme tief");
                }
//                System.out.println(diff(Pattern.standardPatternNames[2])+" | "+diff(Pattern.standardPatternNames[3])+" | "+diff(Pattern.standardPatternNames[5])+" | "+diff(Pattern.standardPatternNames[6]));
//                System.out.println("higher pattern "+list.size());
		return list;
	}

        int moveDiff=500;
        private static LinkedList<String> movements = new LinkedList<String>();

        private void detectMovement(String patternName) {
            move.put(patternName, System.currentTimeMillis());
            System.out.println(patternName);

            if (movements.getLast().equalsIgnoreCase(patternName)) {
                return;
            } else {
                if (patternName.equalsIgnoreCase("beide Arme hoch")) {
                    System.out.println("aufi");
                    moteApp.setPictureUP();
                } else if (patternName.equalsIgnoreCase("beide Arme ausgestreckt")){
                    System.out.println("owi");
                    moteApp.setPictureDOWN();
                    if (movements.getLast().equalsIgnoreCase("beide Arme hoch")) {
                        count++;
                        moteApp.setCounter(count);
                    }
                } else if (patternName.equalsIgnoreCase("beide Arme tief")) {
                    System.out.println("ganz owi");
                    moteApp.setPictureNOTHING();
                }
                movements.add(patternName);
            }

//            if(patternName.equalsIgnoreCase("beide Arme hoch")) {
//                if(System.currentTimeMillis()-move.get("beide Arme ausgestreckt") < moveDiff) {
//                    System.out.println("arme hoch");
//                    move.put("beide Arme ausgestreckt", (long)0);
//                }
//            } else if(patternName.equalsIgnoreCase("beide Arme ausgestreckt")) {
//                if(System.currentTimeMillis()-move.get("beide Arme hoch") < moveDiff) {
//                    System.out.println("arme runter");
//                    move.put("beide Arme hoch", (long)0);
//                }
//            }
        }

}
