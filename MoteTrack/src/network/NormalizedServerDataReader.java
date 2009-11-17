package network;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import data.Position;
import data.SensorData;
import exceptions.NoPrevDataException;

public class NormalizedServerDataReader extends Observable implements Observer {
	private Position calibrationData=null;
	
	public NormalizedServerDataReader(Position calibrationData, double addPercent) {
		this.calibrationData = calibrationData;
		this.calibrationData.addPercentage(addPercent);
	}
	private boolean isWithinRatio(Position last, Position newPos) {
		if (Math.abs(last.getX()-newPos.getX())>calibrationData.getX()) return false;
		if (Math.abs(last.getY()-newPos.getY())>calibrationData.getY()) return false;
		if (Math.abs(last.getZ()-newPos.getZ())>calibrationData.getZ()) return false;
		return true;
	}
	@Override
	public void update(Observable arg0, Object arg1) {
		if (!(arg1 instanceof SensorData)) return;
		SensorData data = (SensorData) arg1;
		Position lastPos;
		if (calibrationData!=null) {
			try {
				lastPos = data.getLastPosition();
				System.out.println("speed: "+data.getVelocity());
			} catch (NoPrevDataException e) {
				lastPos = null;
			}
			if (lastPos==null) {
				lastPos=data.getPos();
			}
			if (isWithinRatio(lastPos, data.getPos())) {
				data.setPos(lastPos);
			} 
		}
		this.setChanged();
		this.notifyObservers(data);
	}

}
