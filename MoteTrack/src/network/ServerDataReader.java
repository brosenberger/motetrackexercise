package network;

import java.util.HashMap;

import data.SensorData;
import java.util.ArrayList;
import java.util.List;
import listener.ServerDataListener;

public class ServerDataReader extends ServerReader {
    String ids;
    HashMap<String, Object> map;

    private List<ServerDataListener> listeners;
    private boolean filterIds;
    public ServerDataReader(String srv, int port) {
        super(srv, port);
        filterIds = false;

        listeners = new ArrayList<ServerDataListener>();
    }

    public ServerDataReader(String srv, int port, String ids) {
            super(srv, port);
            listeners = new ArrayList<ServerDataListener>();

            if (ids == null || ids.length() == 0) {
                filterIds = false;
            } else {
                String[] splitIds = ids.split(";");
                map = new HashMap<String, Object>();
                for (int i=0;i<splitIds.length;i++) {
                        String id = splitIds[i];
                        map.put(id, "");
                }

                filterIds = true;
            }
    }

    @Override
    protected void updateObserver(String msg) {
            String[] sMsg = msg.split(" ");
            String id = sMsg[0];
            if (filterIds && !map.containsKey(id)) {
                return;
            }
//		System.out.println(msg);
            long timestamp = Long.parseLong(sMsg[1]);
//            if (timestamp < System.currentTimeMillis()-500) return; // Server liefert zu beginn 2 minuten alte daten
            SensorData data = new SensorData(id,timestamp,Double.parseDouble(sMsg[2]),Double.parseDouble(sMsg[3]),Double.parseDouble(sMsg[4]));
            this.setChanged();
            this.notifyObservers(data);
            fireEvent(data);
    }
    
    
    public void addListener(ServerDataListener l) {
        listeners.add(l);
    }

    public void removeListener(ServerDataListener l) {
        listeners.remove(l);
    }

    void fireEvent(SensorData data) {
        for (ServerDataListener l : listeners) {
            l.newSensorDataReceived(data);
        }
    }
}
