/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package misc;

import data.HistoryCollector;
import java.util.ArrayList;
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

    public TagIdListModel(HistoryCollector hc) {
        this.hc = hc;
        hc.addListener(hcl);
        listeners = new ArrayList<ListDataListener>();
        tagIds = format(hc.getTagids());
    }

    private ArrayList<String> format(ArrayList<String> list) {
        ArrayList<String> newList = new ArrayList<String>();
        for (String s : list) {
            newList.add(formatId(s));
        }
        return newList;
    }

    private String formatId(String id) {
        StringBuffer sb = new StringBuffer(id);
        sb.insert(9, '-');
        sb.insert(6, '-');
        sb.insert(3, '-');

        return sb.toString();
    }

    private HistoryCollectorListener hcl = new HistoryCollectorListener() {

    public void newTagId(HistoryCollector source, String id) {
            if (source == hc) {
                id = formatId(id);
                tagIds.add(id);
                System.out.println(id);
                fireListDataListeners(tagIds.indexOf(id));
            }
        }
    };

    public int getSize() {
        return tagIds.size();
    }

    public Object getElementAt(int index) {
        return tagIds.get(index);
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
