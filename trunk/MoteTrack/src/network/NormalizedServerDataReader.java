package network;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import data.Position;
import data.SensorData;

public class NormalizedServerDataReader extends Observable implements Observer {
	private Position calibrationData=null;
	//private Position lastPos=null;
	private HashMap<String,Position> lastPosMap;
	
	public NormalizedServerDataReader(Position calibrationData, double addPercent) {
		this.calibrationData = calibrationData;
		this.calibrationData.addPercentage(addPercent);
		lastPosMap = new HashMap<String,Position>();
	}
	private boolean isWithinRatio(Position last, Position newPos) {
		if (Math.abs(last.getX()-newPos.getX())>calibrationData.getX()) return false;
		if (Math.abs(last.getY()-newPos.getY())>calibrationData.getY()) return false;
		if (Math.abs(last.getZ()-newPos.getZ())>calibrationData.getZ()) return false;
		return true;
	}
	@Override
	public void update(Observable arg0, Object arg1) {
		SensorData data = (SensorData) arg1;
		Position lastPos;
		if (calibrationData!=null) {
			lastPos = lastPosMap.get(data.getId());
			if (lastPos==null) {
				lastPos=data.getPos();
				lastPosMap.put(data.getId(), lastPos);
			}
//			System.out.print("Positiondifference: "+Math.abs(lastPos.getX()-data.getX())+" "+Math.abs(lastPos.getY()-data.getY())+" "+Math.abs(lastPos.getZ()-data.getZ())+": ");
			if (isWithinRatio(lastPos, data.getPos())) {
				data.setPos(lastPos);
//				System.out.println("position ignored");
			} else {
				lastPos = data.getPos();
//				System.out.println("new position");
			}
		}
		this.setChanged();
		this.notifyObservers(data);
	}

}
