/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package data;

import java.util.ArrayList;
import java.util.HashMap;
import listener.HistoryCollectorListener;
import listener.ServerDataListener;
import network.ServerDataReader;

/**
 *
 * @author Scheinecker Thomas
 */
public class HistoryCollector {

    private ArrayList<HistoryCollectorListener> listeners;
    private HashMap<String, ArrayList<SensorData>> lists;
    
    public HistoryCollector(ServerDataReader reader) {
        reader.addListener(sdl);
        lists = new HashMap<String, ArrayList<SensorData>>();
        tagIds = new ArrayList<String>();
        listeners = new ArrayList<HistoryCollectorListener>();
    }

    private  ArrayList<String> tagIds;

    public ArrayList<String> getTagids() {

        return new ArrayList<String>(tagIds);
    }

    public void addListener(HistoryCollectorListener l) {
        listeners.add(l);
    }

    public void removeListener(HistoryCollectorListener l) {
        listeners.remove(l);
    }

    private void fireNewTagId(String id) {
        for (HistoryCollectorListener l : listeners) {
            l.newTagId(this, id);
        }
    }

    private ServerDataListener sdl = new ServerDataListener() {
        public void newSensorDataReceived(SensorData data) {
            String id = data.getId();
            if (!tagIds.contains(id)) {
                tagIds.add(id);
                fireNewTagId(id);
            }
            ArrayList<SensorData> history;
            if (!lists.containsKey(id)) {
                history = new ArrayList<SensorData>();
                history.add(data);
                lists.put(id, history);
            } else {
                history = lists.get(id);
                history.add(data);
            }
            data.setHistory(history);
        }
    };



}
