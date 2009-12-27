/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package models;

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

    private boolean showEnums;


    public TagIdListModel() {
        listeners = new ArrayList<ListDataListener>();
        tagIds = new ArrayList<String>();
        showEnums = true;
    }
    
    public TagIdListModel(HistoryCollector hc) {
        listeners = new ArrayList<ListDataListener>();
        setHistorycollector(hc);
    }

    public void setShowEnums(boolean showEnums) {
        this.showEnums = showEnums;
    }

    public void setHistorycollector(HistoryCollector hc) {
        if (this.hc != null) {
            this.hc.removeListener(hcl);
        }
        this.hc = hc;
        hc.addListener(hcl);
        try {
            ArrayList<String> list = SensorData.format(hc.getTagids(), false);
            tagIds = new ArrayList<String>();
            for (String id : list) {
                String enumVal = SensorData.getPosEnum(id).toString();
                String entry = (enumVal.equals("null")?id:id+"@"+enumVal);
                tagIds.add(entry);
            }
            if (getSize()>0) fireListDataListeners(getSize()-1);
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
                        String entry = id+"@"+SensorData.getPosEnum(id);
                        tagIds.add(entry);
                        fireListDataListeners(tagIds.indexOf(entry));
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

    public String getIdAt(int index) {
        return tagIds.get(index).split("@")[0];
    }

    public ArrayList<String> getElementsAt(int[] indices) {
        ArrayList<String> elements = new ArrayList<String>();
        for (int i : indices) {
            elements.add(getElementAt(i));
        }
        return elements;
    }

    public ArrayList<String> getIdsAt(int[] indices) {
        ArrayList<String> elements = new ArrayList<String>();
        for (int i : indices) {
            elements.add(getIdAt(i));
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
