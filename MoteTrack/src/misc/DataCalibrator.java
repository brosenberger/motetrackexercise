package misc;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import data.SensorData;
import data.StatisticData;

public class DataCalibrator extends Observable implements Observer {
	private final HashMap<String, StatisticData> vals;
	private final int maxVals;
	
	public DataCalibrator(int maxVals) {
		this.vals = new HashMap<String, StatisticData>();
		this.maxVals=maxVals;
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		if (arg1.getClass().equals("data.SensorData")) {
			SensorData sens = (SensorData) arg1;
			StatisticData stat = vals.get(sens.getId());
			stat.addPosition(sens.getPos());
			if (stat.getCount()==maxVals) {
				this.hasChanged();
				this.notifyObservers(stat);
			}
		}
	}

}
