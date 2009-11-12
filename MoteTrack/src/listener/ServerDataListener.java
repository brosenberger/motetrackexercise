/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package listener;

import data.SensorData;
import java.util.EventListener;

/**
 *
 * @author Scheinecker Thomas
 */
public interface ServerDataListener extends EventListener {

    public void newSensorDataReceived(SensorData newData);
}
