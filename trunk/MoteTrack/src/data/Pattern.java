package data;

import java.util.ArrayList;
import java.util.LinkedList;

public class Pattern {
	public static LinkedList<AnglePattern> generatePattern(PositionEnum l, PositionEnum anglePoint, PositionEnum r, double angle) {
		ArrayList<PositionEnum> list= new ArrayList<PositionEnum>();
		list.add(l); list.add(anglePoint); list.add(r);
		AnglePattern p = new AnglePattern(list);
		p.setPatternAt(0, angle);
		LinkedList<AnglePattern> listAnglePattern = new LinkedList<AnglePattern>();
		listAnglePattern.add(p);
		return listAnglePattern;
	}
	public static LinkedList<AnglePattern> getPattern(String name) {
		LinkedList<AnglePattern> list=null;
		if (name.equalsIgnoreCase("linkerArmAusgestreckt")) {
			list=generatePattern(PositionEnum.leftWrist,PositionEnum.leftShoulder,PositionEnum.rightShoulder, 180);
		} else if (name.equalsIgnoreCase("rechterArmAusgestreckt")) {
			list=generatePattern(PositionEnum.leftShoulder,PositionEnum.rightShoulder, PositionEnum.rightWrist,180);
		} else if (name.equalsIgnoreCase("linkerArm90")) {
			list=generatePattern(PositionEnum.leftWrist,PositionEnum.leftShoulder,PositionEnum.rightShoulder, 90);
		} else if (name.equalsIgnoreCase("rechterArm90")) {
			list=generatePattern(PositionEnum.leftShoulder,PositionEnum.rightShoulder,PositionEnum.rightWrist, 90);
		}
		return list;
	}
	public static String[] getStandardPatternNames() {
		String result[] = {"linkderArmAusgestreckt", "rechterArmAusgestreckt","linkerArm90","rechterArm90"};
		return result;
	}
}
