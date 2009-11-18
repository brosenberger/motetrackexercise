/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ConnectTagDialog.java
 *
 * Created on 18.11.2009, 08:23:16
 */

package gui;

import data.SensorData;
import exceptions.IllegalTagIdFormatException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import misc.TagIdListModel;

/**
 *
 * @author tscheinecker
 */
public class ConnectTagDialog extends javax.swing.JDialog {

    private MoteTrackApp parent;
    private TagIdListModel tagIdListModel;
    /** Creates new form ConnectTagDialog */
    public ConnectTagDialog(MoteTrackApp parent, boolean modal) {
        super(parent, modal);
        list1Selected = list2Selected = false;
        this.parent = parent;
        
        tagIdListModel = parent.getTagIdListModel();
        initComponents();
        setConnectAndDisconnectEnabled();

        // LISTENER
        tagIdListModel.addListDataListener(listDataListener);
        tagIdList1.addListSelectionListener(list1SelectionListener);
        tagIdList2.addListSelectionListener(list2SelectionListener);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tagIdList1 = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        tagIdList2 = new javax.swing.JList();
        connectButton = new javax.swing.JButton();
        disconnectButton = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        tagIdList1.setModel(tagIdListModel);
        jScrollPane1.setViewportView(tagIdList1);

        tagIdList2.setModel(tagIdListModel);
        jScrollPane2.setViewportView(tagIdList2);

        connectButton.setText("Connect");
        connectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectButtonActionPerformed(evt);
            }
        });

        disconnectButton.setText("Disconnect");
        disconnectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                disconnectButtonActionPerformed(evt);
            }
        });

        jButton1.setText("Clear Selection");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(disconnectButton, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                    .addComponent(connectButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jScrollPane1, jScrollPane2});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(142, 142, 142)
                        .addComponent(connectButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(disconnectButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 123, Short.MAX_VALUE)
                        .addComponent(jButton1)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void connectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectButtonActionPerformed
        int[] indices1 = tagIdList1.getSelectedIndices();
        int[] indices2 = tagIdList2.getSelectedIndices();
        if (indices1.length == 0 || indices2.length == 0) {
            return;
        }
        
        for (int i : indices1) {
           for (int j: indices2) {
               String tag1 = tagIdListModel.getElementAt(i);
               String tag2 = tagIdListModel.getElementAt(j);
               if (!tag1.equals(tag2)) {
                   SensorData.connectTags(tag1, tag2);
               }
           }
        }
    }//GEN-LAST:event_connectButtonActionPerformed

    private void disconnectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_disconnectButtonActionPerformed
        int[] indices1 = tagIdList1.getSelectedIndices();
        int[] indices2 = tagIdList2.getSelectedIndices();
        if (indices1.length == 0 || indices2.length == 0) {
            return;
        }

        for (int i : indices1) {
           for (int j: indices2) {
               String tag1 = tagIdListModel.getElementAt(i);
               String tag2 = tagIdListModel.getElementAt(j);
               if (!tag1.equals(tag2)) {
                   SensorData.disconnectTags(tag1, tag2);
               }
           }
        }
    }//GEN-LAST:event_disconnectButtonActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        tagIdList1.clearSelection();
        tagIdList2.clearSelection();
    }//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton connectButton;
    private javax.swing.JButton disconnectButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList tagIdList1;
    private javax.swing.JList tagIdList2;
    // End of variables declaration//GEN-END:variables

    private ListSelectionListener list1SelectionListener = new ListSelectionListener() {

        public void valueChanged(ListSelectionEvent e) {
            int[] indices = tagIdList1.getSelectedIndices();

            list1Selected = (indices.length != 0);
            setConnectAndDisconnectEnabled();

            ArrayList<String> selectedIds = tagIdListModel.getElementsAt(indices);
            try {
                parent.setSelectedTag1Ids(SensorData.format(selectedIds, true));
            } catch (IllegalTagIdFormatException ex) {
                System.err.println("ILLEGAL TAG ID FORMAT (list1SelectionListener - ConnectTagDialog)");
                Logger.getLogger(ConnectTagDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    };

    private boolean list1Selected, list2Selected;

    private ListSelectionListener list2SelectionListener = new ListSelectionListener() {

        public void valueChanged(ListSelectionEvent e) {
            int[] indices = tagIdList2.getSelectedIndices();

            list2Selected = (indices.length != 0);
            setConnectAndDisconnectEnabled();

            ArrayList<String> selectedIds = tagIdListModel.getElementsAt(indices);
            try {
                parent.setSelectedTag2Ids(SensorData.format(selectedIds, true));
            } catch (IllegalTagIdFormatException ex) {
                System.err.println("ILLEGAL TAG ID FORMAT (list2SelectionListener - ConnectTagDialog)");
                Logger.getLogger(ConnectTagDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    };

    private ListDataListener listDataListener = new ListDataListener() {

        public void intervalAdded(ListDataEvent e) {
            connectButton.setEnabled(tagIdListModel.getSize() != 0);
        }

        public void intervalRemoved(ListDataEvent e) {
            connectButton.setEnabled(tagIdListModel.getSize() != 0);
        }

        public void contentsChanged(ListDataEvent e) {
            connectButton.setEnabled(tagIdListModel.getSize() != 0);
        }
    };

    private void setConnectAndDisconnectEnabled() {
        boolean enabled = list1Selected && list2Selected;
        connectButton.setEnabled(enabled);
        disconnectButton.setEnabled(enabled);
    }

}
