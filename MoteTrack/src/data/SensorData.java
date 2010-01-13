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

    // FLAGS
    public boolean toFast = false;
    public boolean corrected = false;


    private String id;
    private long timestamp;
    private Position pos;
    private PositionEnum posEnum;
    private double velocity;
    private SensorData previousData;
    private SensorData followingData;
    private Vector3d direction;
    private static ArrayList<PositionEnum> patternList = null;

    private static HashMap<String, PositionEnum> idToEnum = new HashMap<String, PositionEnum>();
    private static HashMap<PositionEnum, String> enumToId = new HashMap<PositionEnum, String>();
    private static HashMap<String, SensorData> actData = new HashMap<String, SensorData>(),
                                                prevData = new HashMap<String, SensorData>();

    private static DummyObservable dummyObs = new DummyObservable();

    private ArrayList<SensorData> history;
    private static HashMap<String, ArrayList<String>> connectedTags = new HashMap<String, ArrayList<String>>();

    public SensorData(String id, long timestamp, double x, double y, double z) {
            this.id = id;
            this.timestamp = timestamp;
            pos = new Position(x, y, z);
            posEnum = idToEnum.get(id);
            previousData = actData.get(id);
            if (hasPreviousData()) previousData.followingData = this;
            calcDirectionAndVelocity();
            dataActualice(id);
    }

    public static void setPatterList(ArrayList<PositionEnum> list) {
        patternList = list;
    }

    private void calcDirectionAndVelocity() {
        direction = calcDirection();
        velocity = calcVelocity();
    }

    public void getRidOfData() {
        if (hasFollowingData()) {
            if (hasPreviousData()) {
                followingData.previousData = previousData;
                previousData.followingData = followingData;
            } else {
                followingData.previousData = null;
            }
            prevData.put(id, actData.get(id).previousData);
        } else {
            previousData.followingData = null;
            actData.put(id, previousData);
            prevData.put(id, previousData.previousData);
        }
    }

    private void dataActualice(String id) {
        prevData.put(id, actData.put(id, this));
        //@Testausgabe
        //System.out.println(SensorData.getPattern());
      //  if (patternList != null) {
        for (String pName : Pattern.getStandardPatternNames()) {
        	dummyObs.setObjectChanged(SensorData.getPattern(Pattern.getPattern(pName).getFirst().getPatternList()));
        }
        //}
    }

    public static Set<String> getActualTagIds() {
        return actData.keySet();
    }

    public static SensorData getDataForPosEnum(PositionEnum posEnum) {
        return actData.get(enumToId.get(posEnum));
    }

    public SensorData getPreviousData() {
        return previousData;
    }

    public SensorData getFollowingData() {
        return followingData;
    }

    public boolean hasPreviousData() {
        return previousData != null;
    }

    public boolean hasFollowingData() {
        return followingData != null;
    }

    public SensorData getSensorData(PositionEnum posEnum) {
        try {
            return actData.get(enumToId.get(posEnum));
        } catch (NullPointerException e) {
            return null;
        }
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

    public ArrayList<SensorData> getHistory() {
        if (history == null) return null;
        return new ArrayList<SensorData>(history);
    }
    public Position getLastPosition() throws NoPrevDataException {
        if (!prevData.containsKey(id)) {
            throw new NoPrevDataException();
        }

        if (hasPreviousData()) {
            return previousData.getPos();
        }

        return null;
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
    public double getVelocity(){
        return velocity;
    }

    private double calcVelocity() {
        if (previousData==null) return 0;

        long deltaT = (timestamp - previousData.timestamp); // milliseconds
        double dist = getDirection().getLength();   // meter
        return dist / deltaT * 1000; // m/s
    }

    public Vector3d calcDirection() {
        if (hasPreviousData()) {
            return new Vector3d(previousData.pos, pos);
        }
        
        return new Vector3d(0, 0, 0);
    }

    public Vector3d getDirection() {
        return direction;
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
    
    public static AnglePattern getPattern(ArrayList<PositionEnum> patternList) {
    	AnglePattern pattern = new AnglePattern(patternList);
    	SensorData anglePoint,f,s;
    	Vector3d v1, v2;
    	for (int i=0;i<patternList.size();i++) {
    		f = SensorData.getDataForPosEnum(patternList.get(i++));
    		anglePoint = SensorData.getDataForPosEnum(patternList.get(i++));
    		s = SensorData.getDataForPosEnum(patternList.get(i));
                if (f == null || s == null || anglePoint == null) {
                    return pattern;
                }
    		v1 = new Vector3d(anglePoint.getPos(),f.getPos());
    		v2 = new Vector3d(anglePoint.getPos(),s.getPos());
    		pattern.setPatternAt((i-2)%3,Math.toDegrees(Vector3d.getAngleBetween(v1, v2)));
                //System.out.println(Math.toDegrees(Vector3d.getAngleBetween(v1, v2)));
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
