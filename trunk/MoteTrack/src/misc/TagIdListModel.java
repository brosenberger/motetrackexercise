/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package misc;

import data.HistoryCollector;
import data.SensorData;
import exceptions.IllegalTagIdFormatException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import listener.HistoryCollectorListener;

/**
 *
 * @author Scheinecker Thomas
 */
public class TagIdListModel implements ListModel {

    private ArrayList<ListDataListener> listeners;
    private ArrayList<String> tagIds;
    private HistoryCollector hc;


    public TagIdListModel() {
        listeners = new ArrayList<ListDataListener>();
        tagIds = new ArrayList<String>();
    }
    
    public TagIdListModel(HistoryCollector hc) {
        listeners = new ArrayList<ListDataListener>();
        setHistorycollector(hc);
    }

    public void setHistorycollector(HistoryCollector hc) {
        if (this.hc != null) {
            this.hc.removeListener(hcl);
        }
        this.hc = hc;
        hc.addListener(hcl);
        try {
            tagIds = SensorData.format(hc.getTagids(), false);
        } catch (IllegalTagIdFormatException ex) {
            tagIds = new ArrayList<String>();
            String msg = "Tag Id Format Exception occured while setting HistoryCollector - ID: "+ex.getIllegalTagId();
            System.err.println(msg);
            Logger.getLogger(TagIdListModel.class.getName()).log(Level.SEVERE, msg, ex);
        }
    }

    private HistoryCollectorListener hcl = new HistoryCollectorListener() {

        public void newTagId(HistoryCollector source, String id) {
                if (source == hc) {
                    try {
                        id = SensorData.formatId(id, false);
                        tagIds.add(id);
                        fireListDataListeners(tagIds.indexOf(id));
                    } catch (IllegalTagIdFormatException ex) {
                        String msg = "Tag Id Format Exception occured while handling newTagId event - ID: "+ex.getIllegalTagId();
                        System.err.println(msg);
                        Logger.getLogger(TagIdListModel.class.getName()).log(Level.SEVERE, msg, ex);
                    }

                }
            }
    };

    public int getSize() {
        return tagIds.size();
    }

    public String getElementAt(int index) {
        return tagIds.get(index);
    }

    public ArrayList<String> getElementsAt(int[] indices) {
        ArrayList<String> elements = new ArrayList<String>();
        for (int i : indices) {
            elements.add(getElementAt(i));
        }
        return elements;
    }

    public void addListDataListener(ListDataListener l) {
        listeners.add(l);
    }

    public void removeListDataListener(ListDataListener l) {
        listeners.remove(l);
    }

    private void fireListDataListeners(int index) {
        for (ListDataListener l : listeners) {
            l.intervalAdded(new ListDataEvent(this, 0, index, index));
        }
    }
}
