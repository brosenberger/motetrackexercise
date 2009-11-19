package network;

import java.util.Observable;
import java.util.Observer;

import data.Position;
import data.SensorData;
import exceptions.NoPrevDataException;

public class VelocityNormalizerDataReader extends Observable implements
		Observer {
	private volatile double maxSpeed;
	
	public VelocityNormalizerDataReader(double maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
	
	public void setMaxSpeed(double maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		if (!(arg1 instanceof SensorData)) return;
		SensorData data = (SensorData) arg1;
		try {
			if (data.getVelocity()>this.maxSpeed) {
				data.setPos(data.getLastPosition());
			} else if (data.getVelocity()<Math.pow(10, -7));
		} catch (NoPrevDataException e) {}
		this.setChanged();
		this.notifyObservers(data);
	}

}
