/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package listener;

import data.HistoryCollector;
import java.util.EventListener;

/**
 *
 * @author Scheinecker Thomas
 */
public interface HistoryCollectorListener extends EventListener {

    public void newTagId(HistoryCollector source, String ids);
}
