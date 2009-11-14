/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package listener;

import java.util.EventListener;
import network.ServerReader;

/**
 *
 * @author Scheinecker Thomas
 */
public interface ConnectionListener extends EventListener {

    public void connectionStateChanged(ServerReader source, boolean connected, boolean intended);

    public void unknownHost(ServerReader source, String host);
}
