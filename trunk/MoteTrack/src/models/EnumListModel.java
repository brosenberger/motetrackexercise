/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package models;

import data.PositionEnum;
import java.util.ArrayList;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 *
 * @author Scheinecker Thomas
 */
public class EnumListModel implements ListModel{
    private ArrayList<PositionEnum> displayList;

    private ArrayList<ListDataListener> listeners;

    public EnumListModel() {
        displayList = new ArrayList<PositionEnum>();
        listeners = new ArrayList<ListDataListener>();
    }

    public void addAllEnums() {
        addElement(PositionEnum.leftWrist);
        addElement(PositionEnum.leftShoulder);
        addElement(PositionEnum.leftHip);
        addElement(PositionEnum.leftAnkle);
        addElement(PositionEnum.rightWrist);
        addElement(PositionEnum.rightShoulder);
        addElement(PositionEnum.rightHip);
        addElement(PositionEnum.rightAnkle);
    }

    public ArrayList<PositionEnum> getList() {
        return new ArrayList<PositionEnum>(displayList);
    }

    public boolean addElement(PositionEnum posEnum) {
        if (!displayList.contains(posEnum)) {
            boolean val =  displayList.add(posEnum);
            fireListDataListeners();
            return val;
        }

        return false;
    }

    public int getSize() {
        return displayList.size();
    }

    public PositionEnum getElementAt(int index) {
        return displayList.get(index);
    }

    public boolean containsElement(PositionEnum s) {
        return displayList.contains(s);
    }

    public boolean removeElement(PositionEnum s) {
        boolean removed = displayList.remove(s);
        fireListDataListeners();
        return removed;
    }

    public PositionEnum removeElement(int i) {
        PositionEnum val = displayList.remove(i);
        fireListDataListeners();
        return val;
    }

    public PositionEnum[] removeElements(int[] indices) {
        ArrayList<PositionEnum> values = new ArrayList<PositionEnum>();
        for (int i : indices) {
            values.add(removeElement(i));
        }
        fireListDataListeners();
        return values.toArray(new PositionEnum[] {});
    }

    public void addListDataListener(ListDataListener l) {
        listeners.add(l);
    }

    public void removeListDataListener(ListDataListener l) {
        listeners.remove(l);
    }

    private void fireListDataListenersAdded(int index) {
        for (ListDataListener l : listeners) {
            l.intervalAdded(new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, index, index));
        }
    }

    private void fireListDataListenersRemoved(int index) {
        for (ListDataListener l : listeners) {
            l.intervalRemoved(new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, index, index));
        }
    }

    private void fireListDataListeners() {
        for (ListDataListener l : listeners) {
            l.contentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, getSize()-1));
        }
    }

    public String getListEntries() {
        int size = getSize();
        if (size == 0) {
            return "";
        }

        StringBuffer sb = new StringBuffer();
        sb.append(getElementAt(0));
        for (int i = 1; i < size; i++) {
            sb.append(';');
            sb.append(getElementAt(i));
        }
        return sb.toString();
    }
}
