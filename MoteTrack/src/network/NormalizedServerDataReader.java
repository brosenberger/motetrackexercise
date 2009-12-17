package network;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import data.Position;
import data.SensorData;
import exceptions.NoPrevDataException;

public class NormalizedServerDataReader extends Observable implements Observer {
	private volatile Position calibrationData=null;
	private static NormalizedServerDataReader instance;
	
	public NormalizedServerDataReader(Position calibrationData) {
		this.calibrationData = calibrationData;
		instance = this;
	}
	
	public static NormalizedServerDataReader getInstance() {
		return instance;
	}
	
	private Position update(Position lastPos, Position newPos) {
		return new Position(lastPos.getX()*(1-calibrationData.getX())+newPos.getX()*calibrationData.getX(),
				lastPos.getY()*(1-calibrationData.getY())+newPos.getY()*calibrationData.getY(),
				lastPos.getZ()*(1-calibrationData.getZ())+newPos.getZ()*calibrationData.getZ());
	}
	
	public void setCalibrationData(Position calibration) {
		calibrationData = calibration;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if (!(arg1 instanceof SensorData)) return;
		SensorData data = (SensorData) arg1;
		Position lastPos;
		if (calibrationData!=null) {
			try {
				lastPos = data.getLastPosition();
//				System.out.println("speed: "+data.getVelocity());
			} catch (NoPrevDataException e) {
				lastPos = data.getPos();
			}
			data.setPos(update(lastPos, data.getPos()));
		}
		this.setChanged();
		this.notifyObservers(data);
	}

}
