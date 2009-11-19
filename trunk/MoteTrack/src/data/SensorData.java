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
        private static HashMap<String, ArrayList<String>> connectedTags = new HashMap<String, ArrayList<String>>();
        
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
        public Position getLastPosition() throws NoPrevDataException {
        	if (!prevData.containsKey(id)) {
                throw new NoPrevDataException();
            }
        	SensorData data = prevData.get(id);
        	if (data==null) return null;
        	return data.getPos();
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
        if (prevData.get(id)==null) throw new NoPrevDataException();

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
    
    public double getAngle(Position pos) {
    	Vector3d vec = new Vector3d(this.pos,pos);
    	return Vector3d.getAngleBetween(vec, Vector3d.projectToXY(vec));
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
        if (!isValidTagId(id)) {
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
                sb.deleteCharAt(11);
                sb.deleteCharAt(7);
                sb.deleteCharAt(3);
                break;
            default:
                // Should never be reached as isValidTagId checks that
                throw new RuntimeException("ILLEGAL TAG ID LENGTH (SHOULD NEVER BE REACHED) - ID: "+id);
        }


        return sb.toString();
    }

    public static boolean connectTags(String tagId1, String tagId2) {
        try {
            tagId1 = formatId(tagId1, true);
            tagId2 = formatId(tagId2, true);
        } catch (IllegalTagIdFormatException e) {
            System.err.println("ILLEGAL TAG IDS");
            return false;
        }
        
        if (areConnected(tagId1, tagId2)) {
            return false;
        }

        ArrayList<String> connected;
        if (connectedTags.containsKey(tagId1)) {
            connected = connectedTags.get(tagId1);
        } else {
            connected = new ArrayList<String>();
            connectedTags.put(tagId1, connected);
        }

        return connected.add(tagId2);
    }

    public static boolean disconnectTags(String tagId1, String tagId2) {
        try {
            tagId1 = formatId(tagId1, true);
            tagId2 = formatId(tagId2, true);
        } catch (IllegalTagIdFormatException e) {
            System.err.println("ILLEGAL TAG IDS");
            return false;
        }
        boolean removed = false;

        if (areConnected(tagId1, tagId2)) {
            if (connectedTags.containsKey(tagId1)) {
                removed = connectedTags.get(tagId1).remove(tagId2);
            } else {
                removed = connectedTags.get(tagId2).remove(tagId1);
            }
        }

        return removed;
    }

    private static boolean areConnected(String tagId1, String tagId2) {
        boolean connected = false;
        if (connectedTags.containsKey(tagId1)) {
            connected = connectedTags.get(tagId1).contains(tagId2);
        } else if (connectedTags.containsKey(tagId2)) {
            connected = connectedTags.get(tagId2).contains(tagId1);
        }

        return connected;
    }

    public static HashMap<String, ArrayList<String>> getConnectedTags() {
        return connectedTags;
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
