package data;

import exceptions.NoPrevDataException;
import exceptions.WrongIdException;
import java.util.ArrayList;
import java.util.HashMap;

public class SensorData {
	private String id;
	private long timestamp;
	private Position pos;

        private static HashMap<String, SensorData> actData = new HashMap<String, SensorData>(),
                                                    prevData = new HashMap<String, SensorData>();
	private ArrayList<SensorData> history;
        
	public SensorData(String id, long timestamp, double x, double y, double z) {
		this.id = id;
		this.timestamp = timestamp;
		pos = new Position(x, y, z);
                dataActualice(id);
	}

        public SensorData(String sensorData) {
            String[] splitData = sensorData.split(" ");
            id = splitData[0];
            timestamp = Long.parseLong(splitData[1]);
            pos = new Position(Double.parseDouble(splitData[2]), Double.parseDouble(splitData[3]), Double.parseDouble(splitData[4]));
            dataActualice(id);
        }

        private void dataActualice(String id) {
            prevData.put(id, actData.put(id, this));
        }

        public ArrayList<SensorData> getHistory() {
            if (history == null) return null;
            return new ArrayList<SensorData>(history);
        }

        @Override
	public String toString() {
		return id+" "+timestamp+" "+pos;
	}

        public static HashMap<String, SensorData> getData() {
            return new HashMap<String, SensorData>(actData);
        }

	public String getId() {
		return id;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public double getX() {
		return pos.getX();
	}

	public double getY() {
		return pos.getY();
	}

	public double getZ() {
		return pos.getZ();
	}

        public Position getPos() {
            return pos;
        }

    /**
     *
     * @throws WrongIdException is thrown if the ID of parameter previous differs from actual ID
     * @param previous
     * @return returns the actual velocity
     */
    public double getVelocity() throws NoPrevDataException {
        if (!prevData.containsKey(id)) {
            throw new NoPrevDataException();
        }

        long deltaT = (timestamp - prevData.get(id).timestamp); // milliseconds
        double dist = getDirection().getLength();   // meter
        return dist / deltaT / 1000; // m/s
    }

    public Vector3d getDirection() throws NoPrevDataException {
        if (!prevData.containsKey(id)) {
            throw new NoPrevDataException();
        }
        return new Vector3d(prevData.get(id).pos, pos);
    }

    public void setHistory(ArrayList<SensorData> history) {
        this.history = history;
    }
}
