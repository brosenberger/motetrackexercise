/*
 * SimpleGLCanvas.java
 *
 * Created on 30. Juli 2008, 16:18
 */

package gui;

import exceptions.IllegalTagIdFormatException;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.JSpinner;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import com.sun.opengl.util.Animator;
import data.HistoryCollector;
import data.Position;
import data.PositionEnum;
import data.SensorData;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import misc.MoteTrackConfiguration;
import models.TagIdListModel;
import network.ServerDataReader;
import network.VelocityNormalizerDataReader;
import misc.PatternRecorder;
import misc.PatternPool;
import network.NormalizedServerDataReader;

/**
 *
 * @author cylab
 * @author mbien
 */
public class MoteTrackApp extends JFrame {

    static {
        // When using a GLCanvas, we have to set the Popup-Menues to be HeavyWeight,
        // so they display properly on top of the GLCanvas
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
    }
    
    private Animator animator;
    private DataVisualisation renderer;
    private ServerDataReader client;
    private HistoryCollector historyCollector;
    private StartReplayServerDialog startServerDialog;
    private ConnectToServerDialog connectToServerDialog;
    private ConnectTagDialog connectTagDialog;
    private TagIdListModel tagIdListModel;
    private VelocityNormalizerDataReader velocityNormalizerDataReader;
    private MoteTrackConfiguration conf;
    private LoggerFrame loggerFrame;
    private PatternRecorder patternRecorder;
    private PatternPool patternPool;
    private SelectEnumDialog selectEnumDialog;

    private SpinnerNumberModel historySpinnerModel, maxVelocitySpinnerModel, tagSizeSpinnerModel;

    private void autoStartReplayServer() {
        startServerDialog.startServer();
    }

    public void setVelocityNormalizerDataReader(VelocityNormalizerDataReader observer) {
        this.velocityNormalizerDataReader = observer;
    }

    /** Creates new form MainFrame */
    public MoteTrackApp() {
        historySpinnerModel = new SpinnerNumberModel(5, 0, 2000, 1);
        maxVelocitySpinnerModel = new SpinnerNumberModel(3.0, 0.0, 100.0, 0.1);
        tagSizeSpinnerModel = new SpinnerNumberModel(2, 0, 10, 1);

        patternPool = new PatternPool();
        tagIdListModel = new TagIdListModel();
        patternRecorder = new PatternRecorder();
        SensorData.getDummyObs().addObserver(patternRecorder);
        SensorData.getDummyObs().addObserver(patternPool);
        
        initComponents();

        startServerDialog = new StartReplayServerDialog(this, true);
        connectToServerDialog = new ConnectToServerDialog(this, true);
        connectTagDialog = new ConnectTagDialog(this, false);
        selectEnumDialog = new SelectEnumDialog(this, false);
        loggerFrame = new LoggerFrame();
        maxVelocitySpinner.setValue(connectToServerDialog.getMaxVelocity());

        // for debugging only
//        autoStartReplayServer();

        // LISTENER
        tagIdList.addListSelectionListener(listSelectionListener);
        maxVelocitySpinner.addChangeListener(maxVelocitySpinnerChangeListener);
        maxHistoryReadingsSpinner.addChangeListener(maxHistoryReadingsSpinnerChangeListener);
        calibrationSpinner.addChangeListener(calibrationSpinnerChangeListener);


        setTitle("Motion Tracking Application");

        renderer = new DataVisualisation();
        canvas.addGLEventListener(renderer);
        animator = new Animator(canvas);

        // This is a workaround for the GLCanvas not adjusting its size, when the frame is resized.
        canvas.setMinimumSize(new Dimension());         
        
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                // Run this on another thread than the AWT event queue to
                // make sure the call to Animator.stop() completes before
                // exiting
                new Thread(new Runnable() {

                    public void run() {
                        animator.stop();
                        System.exit(0);
                    }
                }).start();
            }
        });
    }

    @Override
    public void setVisible(boolean show){
        if(!show)
            animator.stop();
        super.setVisible(show);
        if(show)
            animator.start();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        JLabel tagIdListLabel = new JLabel();
        canvas = new GLCanvas(createGLCapabilites());
        historyCheckBox = new JCheckBox();
        beacon2dCheckBox = new JCheckBox();
        resetButton = new JButton();
        tagIdListScrollPane = new JScrollPane();
        tagIdList = new JList();
        planeCheckBox = new JCheckBox();
        tagSizeSpinner = new JSpinner();
        tagSizeLabel = new JLabel();
        drawAxesCheckBox = new JCheckBox();
        detailedRoomPlanCheckBox = new JCheckBox();
        maxVelocitySpinner = new JSpinner();
        maxVelocityLabel = new JLabel();
        jLabel1 = new JLabel();
        maxHistoryReadingsSpinner = new JSpinner();
        startRecordButton = new JButton();
        stopRecordButton = new JButton();
        calibrationSpinner = new JSpinner();
        jMenuBar1 = new JMenuBar();
        fileMenu = new JMenu();
        startReplayServerItem = new JMenuItem();
        connectMenuItem = new JMenuItem();
        connectTagsMenuItem = new JMenuItem();
        exitMenuItem = new JMenuItem();
        optionsMenu = new JMenu();
        saveConfigMenuItem = new JMenuItem();
        loadConfigMenuItem = new JMenuItem();
        loadPatternMenuItem = new JMenuItem();
        windowsMenu = new JMenu();
        showLoggerFrameMenuItem = new JMenuItem();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        tagIdListLabel.setText("List of Tag IDs");

        historyCheckBox.setSelected(true);
        historyCheckBox.setText("draw history");
        historyCheckBox.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                historyCheckBoxStateChanged(evt);
            }
        });

        beacon2dCheckBox.setSelected(true);
        beacon2dCheckBox.setText("draw 2D beacon");
        beacon2dCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                beacon2dCheckBoxActionPerformed(evt);
            }
        });

        resetButton.setText("Reset View");
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                resetButtonActionPerformed(evt);
            }
        });

        tagIdList.setModel(tagIdListModel);
        tagIdListScrollPane.setViewportView(tagIdList);

        planeCheckBox.setText("show Plane");
        planeCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                planeCheckBoxActionPerformed(evt);
            }
        });

        tagSizeSpinner.setModel(tagSizeSpinnerModel);
        tagSizeSpinner.setValue(new Integer(2));
        tagSizeSpinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                tagSizeSpinnerStateChanged(evt);
            }
        });

        tagSizeLabel.setText("Tag drawing size (dm)");

        drawAxesCheckBox.setText("draw Axes");
        drawAxesCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                drawAxesCheckBoxActionPerformed(evt);
            }
        });

        detailedRoomPlanCheckBox.setSelected(true);
        detailedRoomPlanCheckBox.setText("detailed room plan");
        detailedRoomPlanCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                detailedRoomPlanCheckBoxActionPerformed(evt);
            }
        });

        maxVelocitySpinner.setModel(maxVelocitySpinnerModel);
        maxVelocitySpinner.setEnabled(false);

        maxVelocityLabel.setText("max velocity im m/s");
        maxVelocityLabel.setEnabled(false);

        jLabel1.setText("draw history for last n readings");

        maxHistoryReadingsSpinner.setModel(historySpinnerModel);
        maxHistoryReadingsSpinner.setValue(20);

        startRecordButton.setText("start pattern recording");
        startRecordButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                startRecordButtonActionPerformed(evt);
            }
        });

        stopRecordButton.setText("stop pattern recording");
        stopRecordButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                stopRecordButtonActionPerformed(evt);
            }
        });

        calibrationSpinner.setValue(5);

        fileMenu.setText("File");

        startReplayServerItem.setText("Start Replay Server");
        startReplayServerItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                startReplayServerItemActionPerformed(evt);
            }
        });
        fileMenu.add(startReplayServerItem);

        connectMenuItem.setText("Connect to Server");
        connectMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                connectMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(connectMenuItem);

        connectTagsMenuItem.setText("Connect Tags");
        connectTagsMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                connectTagsMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(connectTagsMenuItem);

        exitMenuItem.setAction(exitAction);
        exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
        exitMenuItem.setText("Exit");
        fileMenu.add(exitMenuItem);

        jMenuBar1.add(fileMenu);

        optionsMenu.setText("Options");

        saveConfigMenuItem.setText("Save configuration");
        saveConfigMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                saveConfigMenuItemActionPerformed(evt);
            }
        });
        optionsMenu.add(saveConfigMenuItem);

        loadConfigMenuItem.setText("Load configuration");
        loadConfigMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                loadConfigMenuItemActionPerformed(evt);
            }
        });
        optionsMenu.add(loadConfigMenuItem);

        loadPatternMenuItem.setText("Load Pattern");
        loadPatternMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                loadPatternMenuItemActionPerformed(evt);
            }
        });
        optionsMenu.add(loadPatternMenuItem);

        jMenuBar1.add(optionsMenu);

        windowsMenu.setText("Windows");

        showLoggerFrameMenuItem.setText("Logger View");
        showLoggerFrameMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                showLoggerFrameMenuItemActionPerformed(evt);
            }
        });
        windowsMenu.add(showLoggerFrameMenuItem);

        jMenuBar1.add(windowsMenu);

        setJMenuBar(jMenuBar1);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                    .addComponent(tagIdListLabel)
                    .addComponent(tagIdListScrollPane, GroupLayout.PREFERRED_SIZE, 162, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(canvas, GroupLayout.PREFERRED_SIZE, 551, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                    .addComponent(planeCheckBox)
                    .addComponent(beacon2dCheckBox)
                    .addComponent(historyCheckBox)
                    .addComponent(resetButton)
                    .addComponent(drawAxesCheckBox)
                    .addComponent(detailedRoomPlanCheckBox)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(tagSizeSpinner, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(tagSizeLabel)
                        .addGap(19, 19, 19))
                    .addComponent(jLabel1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(maxHistoryReadingsSpinner, GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE)
                        .addGap(110, 110, 110))
                    .addComponent(startRecordButton)
                    .addComponent(stopRecordButton)
                    .addComponent(calibrationSpinner, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
                    .addComponent(maxVelocityLabel)
                    .addComponent(maxVelocitySpinner, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(Alignment.TRAILING)
                    .addComponent(canvas, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 446, Short.MAX_VALUE)
                    .addGroup(Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(tagIdListLabel)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(tagIdListScrollPane, GroupLayout.DEFAULT_SIZE, 426, Short.MAX_VALUE))
                    .addGroup(Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(resetButton)
                        .addGap(11, 11, 11)
                        .addComponent(historyCheckBox)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(beacon2dCheckBox)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(planeCheckBox)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(drawAxesCheckBox)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(detailedRoomPlanCheckBox)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(maxHistoryReadingsSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(startRecordButton)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(stopRecordButton)
                        .addPreferredGap(ComponentPlacement.UNRELATED)
                        .addComponent(calibrationSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(ComponentPlacement.RELATED, 72, Short.MAX_VALUE)
                        .addComponent(maxVelocityLabel)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(maxVelocitySpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                            .addComponent(tagSizeSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(tagSizeLabel))))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void beacon2dCheckBoxActionPerformed(ActionEvent evt) {//GEN-FIRST:event_beacon2dCheckBoxActionPerformed
        renderer.setDraw2dBeachon(beacon2dCheckBox.isSelected());
    }//GEN-LAST:event_beacon2dCheckBoxActionPerformed

    private void resetButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_resetButtonActionPerformed
        renderer.reset();
    }//GEN-LAST:event_resetButtonActionPerformed

    private void startReplayServerItemActionPerformed(ActionEvent evt) {//GEN-FIRST:event_startReplayServerItemActionPerformed
        startServerDialog.setLocationRelativeTo(this);
        startServerDialog.setVisible(true);
    }//GEN-LAST:event_startReplayServerItemActionPerformed

    private void connectMenuItemActionPerformed(ActionEvent evt) {//GEN-FIRST:event_connectMenuItemActionPerformed
        connectToServerDialog.setLocationRelativeTo(this);
        connectToServerDialog.setVisible(true);
    }//GEN-LAST:event_connectMenuItemActionPerformed

    private void planeCheckBoxActionPerformed(ActionEvent evt) {//GEN-FIRST:event_planeCheckBoxActionPerformed
        renderer.setDrawPlane(planeCheckBox.isSelected());
    }//GEN-LAST:event_planeCheckBoxActionPerformed

    private void tagSizeSpinnerStateChanged(ChangeEvent evt) {//GEN-FIRST:event_tagSizeSpinnerStateChanged
        renderer.setTagSize(Double.parseDouble(tagSizeSpinner.getValue().toString())/10);
    }//GEN-LAST:event_tagSizeSpinnerStateChanged

    private void drawAxesCheckBoxActionPerformed(ActionEvent evt) {//GEN-FIRST:event_drawAxesCheckBoxActionPerformed
        renderer.setDrawAxes(drawAxesCheckBox.isSelected());
    }//GEN-LAST:event_drawAxesCheckBoxActionPerformed

    private void detailedRoomPlanCheckBoxActionPerformed(ActionEvent evt) {//GEN-FIRST:event_detailedRoomPlanCheckBoxActionPerformed
        renderer.setDetailedRoomPlan(detailedRoomPlanCheckBox.isSelected());
    }//GEN-LAST:event_detailedRoomPlanCheckBoxActionPerformed

    private void connectTagsMenuItemActionPerformed(ActionEvent evt) {//GEN-FIRST:event_connectTagsMenuItemActionPerformed
        connectTagDialog.setVisible(true);
    }//GEN-LAST:event_connectTagsMenuItemActionPerformed

    private void loadConfigMenuItemActionPerformed(ActionEvent evt) {//GEN-FIRST:event_loadConfigMenuItemActionPerformed
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.addChoosableFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) return true;
                String fileName = f.getName();
                String fileExtension = fileName.substring(fileName.length()-4);
                return fileExtension.equalsIgnoreCase(".ini");//(parts[parts.length-1].equalsIgnoreCase("ini"));
            }

            @Override
            public String getDescription() {
                return "*.ini - Motion Tracking Configuration Files";
            }
        });
        
        String workdir = System.getProperty("user.dir");
        fc.setSelectedFile(new File(workdir));
        int value = fc.showOpenDialog(this);
        if (value != JFileChooser.APPROVE_OPTION) {
           return;
        }
        File file = fc.getSelectedFile();
        conf = new MoteTrackConfiguration(file.getAbsolutePath());
        conf.loadConfiguration();
        loadConfiguration();
    }//GEN-LAST:event_loadConfigMenuItemActionPerformed

    private void historyCheckBoxStateChanged(ChangeEvent evt) {//GEN-FIRST:event_historyCheckBoxStateChanged
        renderer.setDrawHistory(historyCheckBox.isSelected());
    }//GEN-LAST:event_historyCheckBoxStateChanged

    private void showLoggerFrameMenuItemActionPerformed(ActionEvent evt) {//GEN-FIRST:event_showLoggerFrameMenuItemActionPerformed
        loggerFrame.setVisible(true);
    }//GEN-LAST:event_showLoggerFrameMenuItemActionPerformed

    private void startRecordButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_startRecordButtonActionPerformed
        selectEnumDialog.setVisible(true);
    }//GEN-LAST:event_startRecordButtonActionPerformed

    public void startPatternRecorder() {
        switch (patternRecorder.startRecording()) {
            case PatternRecorder.STARTED_RECORDING:
                JOptionPane.showMessageDialog(this, "Recording started.", "RECORDING STARTED", JOptionPane.INFORMATION_MESSAGE);
                break;
            case PatternRecorder.ALREADY_RECORDING:
                JOptionPane.showMessageDialog(this, "startRecording() returned with error ALREADY RECORDING", "ALREADY RECORDING", JOptionPane.ERROR_MESSAGE);
                break;
            case PatternRecorder.SAVING_IN_PROGRESS:
                JOptionPane.showMessageDialog(this, "startRecording() returned with error SAVING IN PROGRESS", "SAVING IN PROGRESS", JOptionPane.ERROR_MESSAGE);
                break;
            default:
                JOptionPane.showMessageDialog(this, "Unknown return value of startRecording()", "UNKNOWN RETURN VALUE", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void stopRecordButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_stopRecordButtonActionPerformed
        patternRecorder.stopRecording();
        SensorData.setPatterList(null);
    }//GEN-LAST:event_stopRecordButtonActionPerformed

    private void loadPatternMenuItemActionPerformed(ActionEvent evt) {//GEN-FIRST:event_loadPatternMenuItemActionPerformed
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

        String workdir = System.getProperty("user.dir");
        fc.setSelectedFile(new File(workdir));

        int value = fc.showOpenDialog(this);
        if (value != JFileChooser.APPROVE_OPTION) {
           return;
        }
        File file = fc.getSelectedFile();

        patternPool.loadPattern(file.getAbsolutePath());
    }//GEN-LAST:event_loadPatternMenuItemActionPerformed

    private void saveConfigMenuItemActionPerformed(ActionEvent evt) {//GEN-FIRST:event_saveConfigMenuItemActionPerformed
//        conf.set
    }//GEN-LAST:event_saveConfigMenuItemActionPerformed

    /**
     * Called from within initComponents().
     * hint: to customize the generated code choose 'Customize Code' in the contextmenu
     * of the selected UI Component you wish to cutomize in design mode.
     * @return Returns customized GLCapabilities.
     */
    private GLCapabilities createGLCapabilites() {
        
        GLCapabilities capabilities = new GLCapabilities();
        capabilities.setHardwareAccelerated(true);

        // try to enable 2x anti aliasing - should be supported on most hardware
        capabilities.setNumSamples(2);
        capabilities.setSampleBuffers(true);
        
        return capabilities;
    }
    
    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        // Run this in the AWT event thread to prevent deadlocks and race conditions
        EventQueue.invokeLater(new Runnable() {
            public void run() {

                // switch to system l&f for native font rendering etc.
                try{
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                }catch(Exception ex) {
                    Logger.getLogger(getClass().getName()).log(Level.INFO, "can not enable system look and feel", ex);
                }

                MoteTrackApp frame = new MoteTrackApp();

                frame.setVisible(true);
            }
        });
    }

    public TagIdListModel getTagIdListModel() {
        return tagIdListModel;
    }

    private Action exitAction = new AbstractAction("EXIT") {

        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    };

    private ListSelectionListener listSelectionListener = new ListSelectionListener() {

        public void valueChanged(ListSelectionEvent e) {
            Object[] selectedIds = tagIdList.getSelectedValues();
            ArrayList<String> selectedIdsList = new ArrayList<String>();
            for (Object id : selectedIds) {
                selectedIdsList.add(((String)id).split("@")[0]);
            }
            try {
                selectedIdsList = SensorData.format(selectedIdsList, true);
            } catch (IllegalTagIdFormatException ex) {
                String msg = "IllegalTagIdFormatException occured while handling selection change event - ID: "+ex.getIllegalTagId();
                System.err.println(msg);
                Logger.getLogger(MoteTrackApp.class.getName()).log(Level.SEVERE, msg, ex);
            }

            renderer.setSelectedTagIds(selectedIdsList);
        }
    };

    public void connectToServer(ServerDataReader client, HistoryCollector historyCollector) {
        this.client = client;
        this.historyCollector = historyCollector;
        ((TagIdListModel) tagIdList.getModel()).setHistorycollector(historyCollector);
    }

    public void connectToServer(ServerDataReader client, HistoryCollector historyCollector, VelocityNormalizerDataReader observer) {
        this.client = client;
        this.historyCollector = historyCollector;
        ((TagIdListModel) tagIdList.getModel()).setHistorycollector(historyCollector);
        this.velocityNormalizerDataReader = observer;
    }

    public void setMaxVelocitySpinnerValue(double value) {
        maxVelocitySpinner.setValue(value);
    }

    public void setMaxVelocitySpinnerEnabled(boolean enabled) {
        maxVelocitySpinner.setEnabled(enabled);
        maxVelocityLabel.setEnabled(enabled);
    }

    private ChangeListener maxVelocitySpinnerChangeListener = new ChangeListener() {

        public void stateChanged(ChangeEvent e) {
            if (velocityNormalizerDataReader != null) {
                velocityNormalizerDataReader.setMaxSpeed(Double.parseDouble(maxVelocitySpinner.getValue().toString()));
            }
        }
    };

    private ChangeListener maxHistoryReadingsSpinnerChangeListener = new ChangeListener() {

        public void stateChanged(ChangeEvent e) {
            renderer.setMaxHistoryReadings(Integer.parseInt(maxHistoryReadingsSpinner.getValue().toString()));
        }
    };

    private ChangeListener calibrationSpinnerChangeListener = new ChangeListener() {

        public void stateChanged(ChangeEvent e) {
            double val = (Double.parseDouble(calibrationSpinner.getValue().toString()))/10;
            NormalizedServerDataReader.getInstance().setCalibrationData(new Position(val, val, val));
        }
    };


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JCheckBox beacon2dCheckBox;
    private JSpinner calibrationSpinner;
    private GLCanvas canvas;
    private JMenuItem connectMenuItem;
    private JMenuItem connectTagsMenuItem;
    private JCheckBox detailedRoomPlanCheckBox;
    private JCheckBox drawAxesCheckBox;
    private JMenuItem exitMenuItem;
    private JMenu fileMenu;
    private JCheckBox historyCheckBox;
    private JLabel jLabel1;
    private JMenuBar jMenuBar1;
    private JMenuItem loadConfigMenuItem;
    private JMenuItem loadPatternMenuItem;
    private JSpinner maxHistoryReadingsSpinner;
    private JLabel maxVelocityLabel;
    private JSpinner maxVelocitySpinner;
    private JMenu optionsMenu;
    private JCheckBox planeCheckBox;
    private JButton resetButton;
    private JMenuItem saveConfigMenuItem;
    private JMenuItem showLoggerFrameMenuItem;
    private JButton startRecordButton;
    private JMenuItem startReplayServerItem;
    private JButton stopRecordButton;
    private JList tagIdList;
    private JScrollPane tagIdListScrollPane;
    private JLabel tagSizeLabel;
    private JSpinner tagSizeSpinner;
    private JMenu windowsMenu;
    // End of variables declaration//GEN-END:variables

    public void setSelectedFromTagIds(ArrayList<String> list) {
        renderer.setSelectedFromTagIds(list);
    }

    public void setSelectedToTagIds(ArrayList<String> list) {
        renderer.setSelectedToTagIds(list);
    }

    private void loadConfiguration() {
        // CALIBRATION DATA
        Position pos = conf.getCalibrationData();
        if (pos != null) {
            connectToServerDialog.setNormalizationPos(pos);
        }


        // TAG ID FILTER
        String[] filterIds = conf.getTagFilter();
        if (filterIds != null) {
            connectToServerDialog.setTagIdFilter(filterIds);
        }

//        HashMap<String, ArrayList<String>> connections = conf.getConnectedTags();
//        SensorData.setConnectedTags(connections);


        // MAX HISTORY READINGS
        try {
            int maxHistoryReadings = conf.getHistoryReadings();
            maxHistoryReadingsSpinner.setValue(maxHistoryReadings);
        } catch (NumberFormatException e) {}


        // REPLAY FILE
        String file = conf.getReplayFile();
        if (file != null) {
            startServerDialog.setReplayFile(file);
        }

        // REPLAY PORT
        try {
            int port = conf.getReplayPort();
            startServerDialog.setReplayServerPort(port);
        } catch (NumberFormatException e) {}

        // REPLAY RATE
        try {
            int rate = conf.getReplayRate();
            startServerDialog.setReplayRate(rate);
        } catch (NumberFormatException e) {}

        // SERVER ADDRESS
        String server = conf.getServer();
        if (server != null) {
            connectToServerDialog.setServerAddress(server);
        }

        // HISTORY ENABLED
        boolean drawHistory = conf.drawHistory();
        historyCheckBox.setSelected(drawHistory);

        // LOG ENABLED
        boolean log = conf.log();
        connectToServerDialog.setLogSelected(log);

        // LOG PATH
        String logPath = conf.getlogPath();
        if (logPath != null) {
            connectToServerDialog.setLogPath(logPath);
        }

        // LOG NAME
        String logFile = conf.getLogFile();
        if (logFile != null) {
            connectToServerDialog.setLogFile(logFile);
        }

        // MAY VELOCITY
        try {
            int maxVelocity = conf.getMaxVelocity();
            connectToServerDialog.setMaxVelocity(maxVelocity);
        } catch (NumberFormatException e) {}

        // MAX VELOCITY ENABLED
        boolean maxVelocityObs = conf.velocityObs();
        connectToServerDialog.setMaxVelocitySelected(maxVelocityObs);

        // NORMALIZE DATA ENABLED
        boolean normalized = conf.isNormalized();
        connectToServerDialog.setNormalizationSelected(normalized);

        // AUTO START SERVER ENABLED
        boolean autoStartServer = conf.autoStartServer();
        if (autoStartServer) {
            startServerDialog.startServer();
        }

        // ID TO ENUM MAPPING
        HashMap<String, PositionEnum> idsToEnum = conf.getIdEnumMatch();
        SensorData.setPosEnum(idsToEnum);
    }

}
