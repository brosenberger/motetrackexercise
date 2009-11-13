package data;

import exceptions.IllegalTagIdFormatException;
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
        
        public void setPos(Position pos) {
        	this.pos = pos;
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


    public static ArrayList<String> format(ArrayList<String> list, boolean toShort) throws IllegalTagIdFormatException {
        ArrayList<String> newList = new ArrayList<String>();
        for (String s : list) {
            newList.add(formatId(s, toShort));
        }
        return newList;
    }

    public static String formatId(String id, boolean toShort) throws IllegalTagIdFormatException {
        if (isValidTagId(id)) {
            throw new IllegalTagIdFormatException(id);
        }
        StringBuffer sb = new StringBuffer(id);
        switch (sb.length()) {
            case 12:
                if (toShort) return id;
                sb.insert(9, '-');
                sb.insert(6, '-');
                sb.insert(3, '-');
                break;
            case 15:
                if (!toShort) return id;
                sb.deleteCharAt(12);
                sb.deleteCharAt(8);
                sb.deleteCharAt(4);
                break;
            default:
                // Should never be reached as isValidTagId checks that
                throw new RuntimeException("ILLEGAL TAG ID LENGTH (SHOULD NEVER BE REACHED)");
        }


        return sb.toString();
    }

    public static boolean isValidTagId(String id) {
        switch (id.length()) {
            case 12:
                 try {
                    Long.parseLong(id);
                } catch (NumberFormatException e) {
                    return false;
                }
                break;
            case 15:
                String[] idParts = id.split("-");
                if (idParts.length != 4) {
                    return false;
                }

                for (String s : idParts) {
                    if (s.length() != 3) {
                        return false;
                    }
                    try {
                        Long.parseLong(s);
                    } catch (NumberFormatException e) {
                        return false;
                    }
                }
                break;
            default:
                return false;
        }

        return true;
    }
}
