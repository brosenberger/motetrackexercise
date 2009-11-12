/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package data;

import java.util.ArrayList;
import java.util.HashMap;
import listener.ServerDataListener;
import network.ServerDataReader;

/**
 *
 * @author Scheinecker Thomas
 */
public class HistoryCollector {

    private HashMap<String, ArrayList<SensorData>> lists;
    
    public HistoryCollector(ServerDataReader reader) {
        reader.addListener(sdl);
        lists = new HashMap<String, ArrayList<SensorData>>();
    }

    private ServerDataListener sdl = new ServerDataListener() {
        public void newSensorDataReceived(SensorData data) {
            String id = data.getId();
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
