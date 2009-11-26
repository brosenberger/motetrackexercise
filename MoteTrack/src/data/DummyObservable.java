package data;

import java.util.Observable;

public class DummyObservable extends Observable{
	public void setObjectChanged(Object obj) {
		this.setChanged();
		this.notifyObservers(obj);
	}

}
