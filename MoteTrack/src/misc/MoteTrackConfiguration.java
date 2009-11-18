package misc;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import data.Position;

public class MoteTrackConfiguration {
	private Configurator config;
	public MoteTrackConfiguration(String fileName) {
		config = new Configurator(fileName);
	}
	public void loadConfiguration() {
		try {
			config.loadConfiguration();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	public void saveConfiguration() {
		try {
			config.saveConfiguration();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public Position getCalibrationData() {
		String confVal=config.get("calibration");
		if (confVal==null) return new Position(0,0,0);
		String[] split = confVal.split(";");
		return new Position(Double.parseDouble(split[0]),Double.parseDouble(split[1]),Double.parseDouble(split[2]));
	}
	public void setCalibrationData(Position calib) {
		config.put("calibration",calib.getX()+";"+calib.getY()+";"+calib.getZ());
	}
	public void setConnectedTags(HashMap<String, ArrayList<String>> list) {
		String whole="", key;
		Iterator<String> itMap = list.keySet().iterator();
		Iterator<String> itArr;
		while (itMap.hasNext()) {
			key = itMap.next();
			itArr=list.get(key).iterator();
			whole+=";"+key;
			while(itArr.hasNext()) {
				whole+=","+itArr.next();
			}
		}
		config.put("connections", whole.substring(1));
	}
	public HashMap<String, ArrayList<String>> getConnectedTags() {
		HashMap<String, ArrayList<String>> list = new HashMap<String, ArrayList<String>>();
		ArrayList<String> arr;
		String[] split = config.get("connections").split(";");
		String[] line;
		for (int i=0;i<split.length;i++) {
			line = split[i].split(",");
			arr = new ArrayList<String>();
			for (int j=1;j<line.length;j++) {
				arr.add(line[j]);
			}
			list.put(line[0], arr);
		}
		return list;
	}
}
