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
		LinkedList<AnglePattern> list=new LinkedList<AnglePattern>();
		if (name.equalsIgnoreCase("linkerArmAusgestreckt")) {
			list=generatePattern(PositionEnum.leftWrist,PositionEnum.leftShoulder,PositionEnum.rightShoulder, 180);
		} else if (name.equalsIgnoreCase("rechterArmAusgestreckt")) {
			list=generatePattern(PositionEnum.leftShoulder,PositionEnum.rightShoulder, PositionEnum.rightWrist,180);
		} else if (name.equalsIgnoreCase("linkerArm90")) {
			list=generatePattern(PositionEnum.leftWrist,PositionEnum.leftShoulder,PositionEnum.rightShoulder, 90);
		} else if (name.equalsIgnoreCase("rechterArm90")) {
			list=generatePattern(PositionEnum.leftShoulder,PositionEnum.rightShoulder,PositionEnum.rightWrist, 90);
		} else if (name.equalsIgnoreCase("linkerArmHuefte90")) {
			list=generatePattern(PositionEnum.leftWrist,PositionEnum.leftShoulder,PositionEnum.leftHip, 90);
		} else if (name.equalsIgnoreCase("linkerArmHuefte180")) {
			list=generatePattern(PositionEnum.leftWrist,PositionEnum.leftShoulder,PositionEnum.leftHip, 180);
		} else if (name.equalsIgnoreCase("rechterArmHuefte180")) {
			list=generatePattern(PositionEnum.rightWrist,PositionEnum.rightShoulder,PositionEnum.rightHip, 180);
		} else if (name.equalsIgnoreCase("rechterArmHuefte90")) {
			list=generatePattern(PositionEnum.rightWrist,PositionEnum.rightShoulder,PositionEnum.rightHip, 90);
		} else if (name.equalsIgnoreCase("linkerArmHuefte00")) {
			list=generatePattern(PositionEnum.leftWrist,PositionEnum.leftShoulder,PositionEnum.leftHip, 0);
		} else if (name.equalsIgnoreCase("rechterArmHuefte00")) {
			list=generatePattern(PositionEnum.rightWrist,PositionEnum.rightShoulder,PositionEnum.rightHip, 0);
		} 
		return list;
	}

        public static final String[] standardPatternNames = {"linkerArmAusgestreckt",
                                                            "rechterArmAusgestreckt",
                                                            "linkerArm90",
                                                            "rechterArm90",
                                                            "linkerArmHuefte90",
                                                            "linkerArmHuefte180",
                                                            "rechterArmHuefte180",
                                                            "rechterArmHuefte90",
                                                            "linkerArmHuefte00",
                                                            "rechterArmHuefte00"};

	public static String[] getStandardPatternNames() {
		return standardPatternNames;
	}
}
