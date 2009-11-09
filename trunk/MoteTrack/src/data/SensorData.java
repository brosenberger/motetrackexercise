package data;

public class SensorData {
	private String id;
	private long timestamp;
	private double x,y,z;
	
	public SensorData(String id, long timestamp, double x, double y, double z) {
		this.id = id;
		this.timestamp = timestamp;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public String toString() {
		return id+" "+timestamp+" "+x+" "+y+" "+z;
	}

	public String getId() {
		return id;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}
}
