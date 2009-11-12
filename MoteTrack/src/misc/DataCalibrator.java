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
        private String fileName=null;
        
        public DataCalibrator(int maxVals) {
            this.vals = new HashMap<String, StatisticData>();
            this.maxVals=maxVals;
        }
        public DataCalibrator(int maxVals, String fileName) {
        	this(maxVals);
        	this.fileName = fileName;
        }
        
        @Override
        public void update(Observable arg0, Object arg1) {
        	if (arg1 instanceof data.SensorData) {
            SensorData sens = (SensorData) arg1;
            System.out.print("new sensor val ");
            StatisticData stat;
            if (!vals.containsKey(sens.getId())) {
                stat = new StatisticData();
                vals.put(sens.getId(), stat);
            }
            else stat = vals.get(sens.getId());
            stat.addPosition(sens.getPos());
            System.out.println("("+stat.getCount()+")");
            if (stat.getCount()==maxVals) {
            	this.setChanged();
                this.notifyObservers(stat);                           
            }
        }
    }
}
