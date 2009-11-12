/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package listener;

import data.SensorData;
import java.util.EventObject;

/**
 *
 * @author Scheinecker Thomas
 */
public class ServerDataEventObject extends EventObject{

    	public ServerDataEventObject(SensorData source) {
		super(source);
	}
}
