package data;

import exceptions.WrongIdException;

public class SensorData {
	private String id;
	private long timestamp;
	private Position pos;
	
	public SensorData(String id, long timestamp, double x, double y, double z) {
		this.id = id;
		this.timestamp = timestamp;
		pos = new Position(x, y, z);
	}

        public SensorData(String data) {
            String[] splitData = data.split(" ");
            id = splitData[0];
            timestamp = Long.parseLong(splitData[1]);
            pos = new Position(Double.parseDouble(splitData[2]), Double.parseDouble(splitData[3]), Double.parseDouble(splitData[4]));
        }

        @Override
	public String toString() {
		return id+" "+timestamp+" "+pos;
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
    public double getVelocity(SensorData previous) throws WrongIdException {
        if (!previous.id.equals(id)) {
            throw new WrongIdException();
        }

        long deltaT = (timestamp - previous.timestamp); // milliseconds
        double dist = getDirection(previous).getLength();   // meter
        return dist / deltaT / 1000; // m/s
    }

    public Vector getDirection(SensorData previous) throws WrongIdException {
                if (!previous.id.equals(id)) {
            throw new WrongIdException();
        }

        return new Vector(previous.pos, pos);
    }
}
