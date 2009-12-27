package data;

import exceptions.IllegalTagIdFormatException;
import exceptions.NoPrevDataException;
import exceptions.WrongIdException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Observable;
import java.util.Set;

public class SensorData extends Observable {
	private String id;
	private long timestamp;
	private Position pos;
        private PositionEnum posEnum;
        public boolean toFast = false;

        private static HashMap<String, PositionEnum> idToEnum = new HashMap<String, PositionEnum>();
        private static HashMap<PositionEnum, String> enumToId = new HashMap<PositionEnum, String>();
        private static HashMap<String, SensorData> actData = new HashMap<String, SensorData>(),
                                                    prevData = new HashMap<String, SensorData>();

        private static DummyObservable dummyObs = new DummyObservable();

	private ArrayList<SensorData> history;
        private static HashMap<String, ArrayList<String>> connectedTags = new HashMap<String, ArrayList<String>>();

        public static Set<String> getActualTagIds() {
            return actData.keySet();
        }

        public static SensorData getDataForPosEnum(PositionEnum posEnum) {
            return actData.get(enumToId.get(posEnum));
        }

        public SensorData getSensorData(PositionEnum posEnum) {
            try {
                return actData.get(enumToId.get(posEnum));
            } catch (NullPointerException e) {
                return null;
            }
        }

	public SensorData(String id, long timestamp, double x, double y, double z) {
		this.id = id;
		this.timestamp = timestamp;
		pos = new Position(x, y, z);
                posEnum = idToEnum.get(id);
                dataActualice(id);
	}

        public static DummyObservable getDummyObs() {
            return dummyObs;
        }

        public static void setPosEnum(PositionEnum posEnum, String id) {
            idToEnum.put(id, posEnum);
            enumToId.put(posEnum, id);
        }

        public static void setPosEnum(HashMap<String, PositionEnum> idsToEnum) {
            Set<String> keys = idsToEnum.keySet();
            for (String key : keys) {
                setPosEnum(idsToEnum.get(key), key);
            }

            // Adding connections between tags based on Position Enums
            HashMap<String, ArrayList<String>> connections = new HashMap<String, ArrayList<String>>();
            ArrayList<String> list = new ArrayList<String>();
            list.add(enumToId.get(PositionEnum.leftWrist));
            list.add(enumToId.get(PositionEnum.leftHip));
            list.add(enumToId.get(PositionEnum.rightShoulder));
            connections.put(enumToId.get(PositionEnum.leftShoulder), list);
            list = new ArrayList<String>();
            list.add(enumToId.get(PositionEnum.leftAnkle));
            list.add(enumToId.get(PositionEnum.rightHip));
            connections.put(enumToId.get(PositionEnum.leftHip), list);
            list = new ArrayList<String>();
            list.add(enumToId.get(PositionEnum.rightAnkle));
            list.add(enumToId.get(PositionEnum.rightShoulder));
            connections.put(enumToId.get(PositionEnum.rightHip), list);
            list = new ArrayList<String>();
            list.add(enumToId.get(PositionEnum.rightWrist));
            connections.put(enumToId.get(PositionEnum.rightShoulder), list);

            setConnectedTags(connections);
        }

        public static void setConnectedTags(HashMap<String, ArrayList<String>> connectedTags2) {
            connectedTags = connectedTags2;
        }

        public static PositionEnum getPosEnum(String id) throws IllegalTagIdFormatException {
            id = SensorData.formatId(id, true);
            return idToEnum.get(id);
        }

        public PositionEnum getPosEnum() {
            return posEnum;
        }

        public SensorData(String sensorData) {
            String[] splitData = sensorData.split(" ");
            id = splitData[0];
            timestamp = Long.parseLong(splitData[1]);
            pos = new Position(Double.parseDouble(splitData[2]), Double.parseDouble(splitData[3]), Double.parseDouble(splitData[4]));
            posEnum = idToEnum.get(id);
            dataActualice(id);
        }

        private void dataActualice(String id) {
            prevData.put(id, actData.put(id, this));
            //@Testausgabe
            //System.out.println(SensorData.getPattern());
            dummyObs.setObjectChanged(SensorData.getPattern());
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
        return dist / deltaT * 1000; // m/s
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
                // Should never be reached as isValidTagId() checks that
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
    
    public static AnglePattern getPattern() {
    	AnglePattern pattern = new AnglePattern(7);
    	HashMap<String, ArrayList<String>> connections = SensorData.getConnectedTags();
    	HashMap<String, SensorData> data = SensorData.getData();
    	SensorData from, to;
    	int i=0;
    	String fromStr;
    	Iterator<String> itKeys =connections.keySet().iterator();
    	Iterator<String> itConn;
    	while (itKeys.hasNext()) {
    		fromStr = itKeys.next();
    		from = data.get(fromStr);
    		if (from == null) continue;
    		itConn = connections.get(fromStr).iterator();
    		while (itConn.hasNext()) {
    			to = data.get(itConn.next());
    			if (to==null) continue;
    			pattern.setPatternAt(i++, Math.toDegrees(from.getAngle(to.getPos())));
    		}
    	}
    	return pattern;
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
    
    public double distanceTo(SensorData data) {
        return (new Vector3d(pos, data.pos)).getLength();
    }

    public static double distanceBetween(SensorData data1, SensorData data2) {
        return (new Vector3d(data1.pos, data2.pos)).getLength();
    }
}
