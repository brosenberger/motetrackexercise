/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package models;

import java.util.ArrayList;
import java.util.logging.Logger;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;

/**
 *
 * @author Scheinecker Thomas
 */
public class LoggerComboBoxModel implements ComboBoxModel {

    ArrayList<Logger> logger;
    ArrayList<ListDataListener> listener;

    Logger selectedLogger;

    public LoggerComboBoxModel(ArrayList<Class> classes) {
        logger = new ArrayList<Logger>();

        for (Class c : classes) {
            logger.add(Logger.getLogger(c.toString()));
        }

        selectedLogger = logger.get(0);

        listener = new ArrayList<ListDataListener>();
    }

    public void setSelectedItem(Logger anItem) {
        selectedLogger = anItem;
    }

    public void setSelectedItem(Object anItem) {
        
            Logger log = (Logger)anItem;
            setSelectedItem(log);
    }

    public Logger getSelectedItem() {
        return selectedLogger;
    }

    public int getSize() {
        return logger.size();
    }

    public Logger getElementAt(int index) {
        return logger.get(index);
    }

    public void addListDataListener(ListDataListener l) {
        listener.add(l);
    }

    public void removeListDataListener(ListDataListener l) {
        listener.remove(l);
    }


}
