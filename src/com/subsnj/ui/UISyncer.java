package com.subsnj.ui;

import com.subsnj.time.*;
import com.subsnj.parsers.SubtitleParser;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingWorker;
import javax.swing.text.DefaultCaret;
import com.subsnj.actions.Actions;
import com.subsnj.listeners.*;
import com.subsnj.parsers.SRTParser;
import com.subsnj.subtitles.*;
import com.subsnj.util.Utils;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.regex.Pattern;
import javax.swing.AbstractAction;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.decorator.HighlighterFactory;

public class UISyncer extends javax.swing.JFrame {

    private static final UISyncer INSTANCE = new UISyncer();
    private SubtitleParser parser;
    private TableCellListener tableCellListener;

    private UISyncer() {
        initComponents();
        getRootPane().setDefaultButton(btnSyncIt);
        initSecondary();
    }

    private void initSecondary() {
        getRootPane().setDropTarget(new DragNDropListener());
        txtLog.setDropTarget(new DragNDropListener());
        SpinnerFocusListener spinnerFocusListener = new SpinnerFocusListener();
        getJSpinnerEditor(spinnerHours).addFocusListener(spinnerFocusListener);
        getJSpinnerEditor(spinnerMinutes).addFocusListener(spinnerFocusListener);
        getJSpinnerEditor(spinnerSeconds).addFocusListener(spinnerFocusListener);
        getJSpinnerEditor(spinnerMilliseconds).addFocusListener(spinnerFocusListener);
    }

    public void receiveFile(String filepath) {
        parser = SubtitleParser.getParserForType(filepath);
        tableCellListener = new TableCellListener(uiTable, new ActionEditSubtitle());
        setCursor(Utils.WAIT_CURSOR);
        Utils.emptyDoc(txtLog);
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                parser.beginParsing(filepath);
                return null;
            }

            @Override
            protected void done() {
                lblFileName.setText(parser.getFileName());
                txtFilePath.setText(parser.getFilePath());
                spinnerTo.setValue(parser.getSubtitles().size());
                ((SpinnerNumberModel) spinnerTo.getModel()).setMaximum(parser.getSubtitles().size());
                enableSyncRelatedComps(true);
                setCursor(Utils.DEFAULT_CURSOR);
                loadEditTab();
                getJSpinnerEditor(spinnerMilliseconds).requestFocus();
            }
        };
        worker.execute();
    }

    public void loadEditTab() {
        List<Subtitle> subtitles = parser.getSubtitles();
        DefaultTableModel model = (DefaultTableModel) uiTable.getModel();
        for (Subtitle subtitle : subtitles) {
            SRTSubtitle srtSub = (SRTSubtitle) subtitle;
            String index = srtSub.getIndex();
            String startTime = srtSub.getStartTime().toString();
            String endTime = srtSub.getEndTime().toString();
            String body = srtSub.getText().replaceAll("\r\n", "|");
            Object[] row = {index, startTime, endTime, body};
            model.addRow(row);
        }
        uiTable.packAll();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGroupDelay = new javax.swing.ButtonGroup();
        btnGroupOverwrite = new javax.swing.ButtonGroup();
        btnGroupEditRangeOrAll = new javax.swing.ButtonGroup();
        tabPane = new javax.swing.JTabbedPane();
        pnlContainerSynchronise = new javax.swing.JPanel();
        pnlChooseFile = new javax.swing.JPanel();
        txtFilePath = new javax.swing.JTextField();
        btnBrowse = new javax.swing.JButton();
        iLblFilename = new javax.swing.JLabel();
        lblFileName = new javax.swing.JLabel();
        radioOverwrite = new javax.swing.JRadioButton();
        radioCreateNew = new javax.swing.JRadioButton();
        btnSyncIt = new javax.swing.JButton();
        scrLog = new javax.swing.JScrollPane();
        txtLog = new javax.swing.JTextPane();
        btnOpenFile = new javax.swing.JButton();
        progressBar = new javax.swing.JProgressBar();
        pnlChooseSettings = new javax.swing.JPanel();
        iLblHours = new javax.swing.JLabel();
        spinnerMilliseconds = new javax.swing.JSpinner();
        iLblSeconds = new javax.swing.JLabel();
        spinnerHours = new javax.swing.JSpinner();
        iLblMilliseconds = new javax.swing.JLabel();
        spinnerMinutes = new javax.swing.JSpinner();
        spinnerSeconds = new javax.swing.JSpinner();
        iLblMinutes = new javax.swing.JLabel();
        pnlChooseSettingsTop = new javax.swing.JPanel();
        radioDelay = new javax.swing.JRadioButton();
        iLblFrom = new javax.swing.JLabel();
        spinnerTo = new javax.swing.JSpinner();
        iLblTo = new javax.swing.JLabel();
        radioAdvance = new javax.swing.JRadioButton();
        spinnerFrom = new javax.swing.JSpinner();
        pnlContainerEdit = new javax.swing.JPanel();
        scrTable = new javax.swing.JScrollPane();
        uiTable = new org.jdesktop.swingx.JXTable();
        btnDelayAll = new javax.swing.JButton();
        btnAdvanceAll = new javax.swing.JButton();
        radioByRange = new javax.swing.JRadioButton();
        radioAll = new javax.swing.JRadioButton();
        radioSelection = new javax.swing.JRadioButton();
        btnSaveSubtitleFile = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        menuHelp = new javax.swing.JMenu();
        menuAbout = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SubsNj");
        setIconImage(new ImageIcon(getClass().getResource("/imgresources/SubsNj_icon.png")).getImage());
        setMinimumSize(new java.awt.Dimension(500, 415));

        tabPane.setFocusable(false);

        pnlChooseFile.setBorder(javax.swing.BorderFactory.createTitledBorder("1. Pick a file"));

        btnBrowse.setText("Browse...");
        btnBrowse.setFocusable(false);
        btnBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowseActionPerformed(evt);
            }
        });

        iLblFilename.setText("File name:");

        btnGroupOverwrite.add(radioOverwrite);
        radioOverwrite.setText("Overwrite");
        radioOverwrite.setToolTipText("The original file will be overwritten with the synchronised subtitles");
        radioOverwrite.setFocusable(false);

        btnGroupOverwrite.add(radioCreateNew);
        radioCreateNew.setSelected(true);
        radioCreateNew.setText("Create new");
        radioCreateNew.setToolTipText("A new file will be created with the synchronised subtitles");
        radioCreateNew.setFocusable(false);

        javax.swing.GroupLayout pnlChooseFileLayout = new javax.swing.GroupLayout(pnlChooseFile);
        pnlChooseFile.setLayout(pnlChooseFileLayout);
        pnlChooseFileLayout.setHorizontalGroup(
            pnlChooseFileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlChooseFileLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlChooseFileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlChooseFileLayout.createSequentialGroup()
                        .addComponent(txtFilePath, javax.swing.GroupLayout.DEFAULT_SIZE, 639, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBrowse))
                    .addGroup(pnlChooseFileLayout.createSequentialGroup()
                        .addComponent(iLblFilename)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblFileName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pnlChooseFileLayout.createSequentialGroup()
                        .addComponent(radioCreateNew)
                        .addGap(18, 18, 18)
                        .addComponent(radioOverwrite)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlChooseFileLayout.setVerticalGroup(
            pnlChooseFileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlChooseFileLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlChooseFileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFilePath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBrowse))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlChooseFileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(iLblFilename)
                    .addComponent(lblFileName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlChooseFileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(radioCreateNew)
                    .addComponent(radioOverwrite))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnSyncIt.setText("Sync it!");
        btnSyncIt.setEnabled(false);
        btnSyncIt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSyncItActionPerformed(evt);
            }
        });

        txtLog.setEditable(false);
        scrLog.setViewportView(txtLog);
        DefaultCaret caretLog = (DefaultCaret) txtLog.getCaret();
        caretLog.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        btnOpenFile.setText("Open original file");
        btnOpenFile.setEnabled(false);
        btnOpenFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenFileActionPerformed(evt);
            }
        });

        pnlChooseSettings.setBorder(javax.swing.BorderFactory.createTitledBorder("2. Choose your settings"));

        iLblHours.setText("Hours");

        spinnerMilliseconds.setModel(new javax.swing.SpinnerNumberModel(0, 0, 999, 100));

        iLblSeconds.setText("Seconds");

        spinnerHours.setModel(new javax.swing.SpinnerNumberModel(0, 0, 9, 1));

        iLblMilliseconds.setText("Milliseconds");

        spinnerMinutes.setModel(new javax.swing.SpinnerNumberModel(0, 0, 99, 1));

        spinnerSeconds.setModel(new javax.swing.SpinnerNumberModel(0, 0, 99, 1));

        iLblMinutes.setText("Minutes");

        btnGroupDelay.add(radioDelay);
        radioDelay.setSelected(true);
        radioDelay.setText("Delay");
        radioDelay.setFocusable(false);
        radioDelay.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        iLblFrom.setText("From subtitle");

        spinnerTo.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));
        spinnerTo.setEnabled(false);

        iLblTo.setText("To");

        btnGroupDelay.add(radioAdvance);
        radioAdvance.setText("Advance");
        radioAdvance.setFocusable(false);
        radioAdvance.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        spinnerFrom.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));
        spinnerFrom.setEnabled(false);

        javax.swing.GroupLayout pnlChooseSettingsTopLayout = new javax.swing.GroupLayout(pnlChooseSettingsTop);
        pnlChooseSettingsTop.setLayout(pnlChooseSettingsTopLayout);
        pnlChooseSettingsTopLayout.setHorizontalGroup(
            pnlChooseSettingsTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlChooseSettingsTopLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(radioDelay)
                .addGap(18, 18, 18)
                .addComponent(radioAdvance)
                .addGap(57, 57, 57)
                .addComponent(iLblFrom)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(spinnerFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(iLblTo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(spinnerTo, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(303, Short.MAX_VALUE))
        );
        pnlChooseSettingsTopLayout.setVerticalGroup(
            pnlChooseSettingsTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlChooseSettingsTopLayout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(pnlChooseSettingsTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(radioDelay)
                    .addComponent(radioAdvance)
                    .addComponent(iLblFrom)
                    .addComponent(spinnerFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(iLblTo)
                    .addComponent(spinnerTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1))
        );

        javax.swing.GroupLayout pnlChooseSettingsLayout = new javax.swing.GroupLayout(pnlChooseSettings);
        pnlChooseSettings.setLayout(pnlChooseSettingsLayout);
        pnlChooseSettingsLayout.setHorizontalGroup(
            pnlChooseSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlChooseSettingsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlChooseSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlChooseSettingsLayout.createSequentialGroup()
                        .addComponent(iLblHours)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spinnerHours, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 114, Short.MAX_VALUE)
                        .addComponent(iLblMinutes)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spinnerMinutes, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 114, Short.MAX_VALUE)
                        .addComponent(iLblSeconds)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spinnerSeconds, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 112, Short.MAX_VALUE)
                        .addComponent(iLblMilliseconds)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spinnerMilliseconds, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(pnlChooseSettingsTop, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlChooseSettingsLayout.setVerticalGroup(
            pnlChooseSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlChooseSettingsLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(pnlChooseSettingsTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnlChooseSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spinnerMinutes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(iLblHours)
                    .addComponent(iLblMinutes)
                    .addComponent(spinnerSeconds, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(iLblSeconds)
                    .addComponent(iLblMilliseconds)
                    .addComponent(spinnerMilliseconds, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spinnerHours, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlContainerSynchroniseLayout = new javax.swing.GroupLayout(pnlContainerSynchronise);
        pnlContainerSynchronise.setLayout(pnlContainerSynchroniseLayout);
        pnlContainerSynchroniseLayout.setHorizontalGroup(
            pnlContainerSynchroniseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlContainerSynchroniseLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlContainerSynchroniseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrLog, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pnlChooseSettings, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlContainerSynchroniseLayout.createSequentialGroup()
                        .addComponent(btnOpenFile)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSyncIt))
                    .addComponent(pnlChooseFile, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlContainerSynchroniseLayout.setVerticalGroup(
            pnlContainerSynchroniseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlContainerSynchroniseLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(pnlChooseFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlChooseSettings, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrLog, javax.swing.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlContainerSynchroniseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSyncIt)
                    .addComponent(btnOpenFile))
                .addContainerGap())
        );

        tabPane.addTab("Synchronise", pnlContainerSynchronise);

        uiTable.setBackground(new java.awt.Color(240, 240, 240));
        uiTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Index", "Start time", "End time", "Body"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        uiTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        uiTable.setCellSelectionEnabled(true);
        uiTable.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        uiTable.setGridColor(new java.awt.Color(204, 204, 204));
        uiTable.setRowHeight(20);
        uiTable.setSelectionBackground(new java.awt.Color(183, 219, 255));
        uiTable.setSelectionForeground(new java.awt.Color(0, 0, 0));
        uiTable.setSortable(false);
        uiTable.getTableHeader().setReorderingAllowed(false);
        scrTable.setViewportView(uiTable);
        uiTable.addHighlighter(HighlighterFactory.createAlternateStriping(new Color(255, 255, 255), new Color(0xEDEDED)));

        btnDelayAll.setText("Delay");
        btnDelayAll.setEnabled(false);
        btnDelayAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelayAllActionPerformed(evt);
            }
        });

        btnAdvanceAll.setText("Advance");
        btnAdvanceAll.setEnabled(false);
        btnAdvanceAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdvanceAllActionPerformed(evt);
            }
        });

        btnGroupEditRangeOrAll.add(radioByRange);
        radioByRange.setText("By range");
        radioByRange.setFocusable(false);

        btnGroupEditRangeOrAll.add(radioAll);
        radioAll.setSelected(true);
        radioAll.setText("All");
        radioAll.setFocusable(false);

        btnGroupEditRangeOrAll.add(radioSelection);
        radioSelection.setText("Selection");
        radioSelection.setFocusable(false);

        btnSaveSubtitleFile.setText("Save");
        btnSaveSubtitleFile.setEnabled(false);
        btnSaveSubtitleFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveSubtitleFileActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlContainerEditLayout = new javax.swing.GroupLayout(pnlContainerEdit);
        pnlContainerEdit.setLayout(pnlContainerEditLayout);
        pnlContainerEditLayout.setHorizontalGroup(
            pnlContainerEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlContainerEditLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlContainerEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrTable, javax.swing.GroupLayout.DEFAULT_SIZE, 756, Short.MAX_VALUE)
                    .addGroup(pnlContainerEditLayout.createSequentialGroup()
                        .addComponent(btnSaveSubtitleFile)
                        .addGap(18, 18, 18)
                        .addComponent(btnDelayAll)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAdvanceAll)
                        .addGap(18, 18, 18)
                        .addComponent(radioAll)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(radioByRange)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(radioSelection)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlContainerEditLayout.setVerticalGroup(
            pnlContainerEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlContainerEditLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrTable, javax.swing.GroupLayout.DEFAULT_SIZE, 501, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlContainerEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDelayAll)
                    .addComponent(btnAdvanceAll)
                    .addComponent(radioByRange)
                    .addComponent(radioAll)
                    .addComponent(radioSelection)
                    .addComponent(btnSaveSubtitleFile))
                .addContainerGap())
        );

        tabPane.addTab("Edit", pnlContainerEdit);

        menuHelp.setText("?");

        menuAbout.setText("About");
        menuAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAboutActionPerformed(evt);
            }
        });
        menuHelp.add(menuAbout);

        menuBar.add(menuHelp);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabPane, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabPane, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnBrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowseActionPerformed
        Actions.actionOpenFile();
    }//GEN-LAST:event_btnBrowseActionPerformed

    private void btnSyncItActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSyncItActionPerformed
        actionSyncIt();
    }//GEN-LAST:event_btnSyncItActionPerformed

    public void actionSyncIt() {
        int h = (Integer) spinnerHours.getValue();
        int m = (Integer) spinnerMinutes.getValue();
        int s = (Integer) spinnerSeconds.getValue();
        int ms = (Integer) spinnerMilliseconds.getValue();
        Interval amount = new Interval(h, m, s, ms);
        boolean overwriteFile = radioOverwrite.isSelected();
        int startRange = (Integer) spinnerFrom.getValue();
        int endRange = (Integer) spinnerTo.getValue();

        if (startRange > endRange) {
            Utils.showMessage("Invalid range", "Check that the starting range is lower than the end.", this);
            return;
        }

        if (amount.isZero()) {
            Utils.showMessage("Invalid interval", "The interval must be of at least 1 millisecond.", this);
            return;
        }
        Synchroniser synchroniser = radioAdvance.isSelected()
                ? new Synchroniser(parser, Synchroniser.ADVANCE, amount, startRange, endRange, overwriteFile)
                : new Synchroniser(parser, Synchroniser.DELAY, amount, startRange, endRange, overwriteFile);
        synchroniser.execute();
    }

    private void btnOpenFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenFileActionPerformed
        try {
            if (Files.exists(Paths.get(parser.getFilePath()))) {
                Runtime.getRuntime().exec("notepad " + parser.getFilePath());
            } else {
                Utils.showMessage("Open file", "The file you are trying to open no longer exists.", this);
            }
        } catch (IOException ex) {
            Utils.showErrorMessage("Could not open file.", "Syncer has encountered "
                    + "an error whilst trying to open the original file.", scrLog);
        }
    }//GEN-LAST:event_btnOpenFileActionPerformed

    private void menuAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuAboutActionPerformed
        UIAbout uiAbout = new UIAbout();
        uiAbout.setVisible(true);
    }//GEN-LAST:event_menuAboutActionPerformed

    private void btnDelayAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelayAllActionPerformed
        actionSyncFromEditTab(radioDelay);
    }//GEN-LAST:event_btnDelayAllActionPerformed

    private void btnAdvanceAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdvanceAllActionPerformed
        actionSyncFromEditTab(radioAdvance);
    }//GEN-LAST:event_btnAdvanceAllActionPerformed

    private void btnSaveSubtitleFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveSubtitleFileActionPerformed
        if (parser.spitFile(true)) {
            Utils.showMessage("Save file", "Your file " + parser.getFileName() + " has been saved.",
                    getParent());
        }
    }//GEN-LAST:event_btnSaveSubtitleFileActionPerformed

    public void actionSyncFromEditTab(JRadioButton radioAction) {
        int start;
        int end;
        if (radioByRange.isSelected()) {
            UIRangeSelection uIRangeSelection = new UIRangeSelection();
            int[] ranges = uIRangeSelection.showDialog(parser.getSubtitleCount());
            if (ranges[0] > -1 | ranges[1] > -1) {
                start = ranges[0];
                end = ranges[1];
            } else {
                return;
            }
        } else if (radioSelection.isSelected()) {
            int[] selectedRows = uiTable.getSelectedRows();
            start = selectedRows[0] + 1;
            end = selectedRows[selectedRows.length - 1] + 1;
        } else { //all subs
            start = 1;
            end = parser.getSubtitleCount();
        }
        radioAction.setSelected(true);
        tabPane.setSelectedIndex(0);
        spinnerFrom.setValue(start);
        spinnerTo.setValue(end);
        getJSpinnerEditor(spinnerMilliseconds).requestFocus();
    }

    public JTextField getJSpinnerEditor(JSpinner spinner) {
        return ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField();
    }

    public void enableSyncRelatedComps(boolean flag) {
        btnOpenFile.setEnabled(flag);
        btnSyncIt.setEnabled(flag);
        spinnerFrom.setEnabled(flag);
        spinnerTo.setEnabled(flag);
        btnDelayAll.setEnabled(flag);
        btnAdvanceAll.setEnabled(flag);
        btnSaveSubtitleFile.setEnabled(flag);
    }

    public JTextPane getTxtLog() {
        return txtLog;
    }

    public JScrollPane getScrLog() {
        return scrLog;
    }

    public void logError(String str) {
        Utils.appendAsError(txtLog, "Segoe_UI", 12, str + "\n");
    }

    public void log(String str) {
        Utils.appendAsMessage(txtLog, "Segoe_UI", 12, str + "\n");
    }

    public void logSuccess(String str) {
        Utils.appendAsGreen(txtLog, "Segoe_UI", 12, str + "\n");
    }

    public void logWarning(String str) {
        Utils.appendAsOrange(txtLog, "Segoe_UI", 12, str + "\n");
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }

    public void setParser(SubtitleParser parser) {
        this.parser = parser;
    }

    public static UISyncer getINSTANCE() {
        return INSTANCE;
    }

    /**
     * The class that represents the action of editing a subtitle within the
     * table in the "Edit" tab of the main interface.
     *
     * @author Morgan
     */
    private class ActionEditSubtitle extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            Pattern timestampPattern = SRTParser.timestampPattern;
            Subtitle changedSub = parser.getSubtitle(tableCellListener.getRow());
            int row = tableCellListener.getRow();
            int column = tableCellListener.getColumn();
            Object oldValue = (String) tableCellListener.getOldValue();
            String newValue = (String) tableCellListener.getNewValue();
            switch (column) {
                case 1: //start time
                    if (timestampPattern.matcher(newValue + " --> "
                            + parser.getLegalTimePattern()).matches()) {
                        changedSub.setStartTime(parser.singleTimestampToInterval(newValue));
                    } else {
                        Utils.showErrorMessage("Invalid time format", "The timestamps "
                                + "for this type of subtitle should look like: "
                                + parser.getLegalTimePattern(), UISyncer.getINSTANCE());
                        tableCellListener.getTable().setValueAt(oldValue, row, column);
                    }
                    break;
                case 2: //end time
                    if (timestampPattern.matcher(newValue + " --> "
                            + parser.getLegalTimePattern()).matches()) {
                        changedSub.setEndTime(parser.singleTimestampToInterval(newValue));
                    } else {
                        Utils.showErrorMessage("Invalid time format", "The timestamps "
                                + "for this type of subtitle should look like: "
                                + parser.getLegalTimePattern(), UISyncer.getINSTANCE());
                        tableCellListener.getTable().setValueAt(oldValue, row, column);
                    }
                    break;
                case 3: //body
                    if (!newValue.endsWith("|")) {
                        newValue += "|"; //add line break
                        tableCellListener.getTable().setValueAt(newValue, row, column);
                    }
                    changedSub.setText(newValue.replaceAll("\\|", "\r\n"));
                    break;
            }
        }

    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdvanceAll;
    private javax.swing.JButton btnBrowse;
    private javax.swing.JButton btnDelayAll;
    private javax.swing.ButtonGroup btnGroupDelay;
    private javax.swing.ButtonGroup btnGroupEditRangeOrAll;
    private javax.swing.ButtonGroup btnGroupOverwrite;
    private javax.swing.JButton btnOpenFile;
    private javax.swing.JButton btnSaveSubtitleFile;
    private javax.swing.JButton btnSyncIt;
    private javax.swing.JLabel iLblFilename;
    private javax.swing.JLabel iLblFrom;
    private javax.swing.JLabel iLblHours;
    private javax.swing.JLabel iLblMilliseconds;
    private javax.swing.JLabel iLblMinutes;
    private javax.swing.JLabel iLblSeconds;
    private javax.swing.JLabel iLblTo;
    private javax.swing.JLabel lblFileName;
    private javax.swing.JMenuItem menuAbout;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenu menuHelp;
    private javax.swing.JPanel pnlChooseFile;
    private javax.swing.JPanel pnlChooseSettings;
    private javax.swing.JPanel pnlChooseSettingsTop;
    private javax.swing.JPanel pnlContainerEdit;
    private javax.swing.JPanel pnlContainerSynchronise;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JRadioButton radioAdvance;
    private javax.swing.JRadioButton radioAll;
    private javax.swing.JRadioButton radioByRange;
    private javax.swing.JRadioButton radioCreateNew;
    private javax.swing.JRadioButton radioDelay;
    private javax.swing.JRadioButton radioOverwrite;
    private javax.swing.JRadioButton radioSelection;
    private javax.swing.JScrollPane scrLog;
    private javax.swing.JScrollPane scrTable;
    private javax.swing.JSpinner spinnerFrom;
    private javax.swing.JSpinner spinnerHours;
    private javax.swing.JSpinner spinnerMilliseconds;
    private javax.swing.JSpinner spinnerMinutes;
    private javax.swing.JSpinner spinnerSeconds;
    private javax.swing.JSpinner spinnerTo;
    private javax.swing.JTabbedPane tabPane;
    private javax.swing.JTextField txtFilePath;
    private javax.swing.JTextPane txtLog;
    private org.jdesktop.swingx.JXTable uiTable;
    // End of variables declaration//GEN-END:variables
}
