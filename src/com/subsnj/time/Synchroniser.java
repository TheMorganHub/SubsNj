package com.subsnj.time;

import com.subsnj.subtitles.SRTSubtitle;
import com.subsnj.subtitles.Subtitle;
import com.subsnj.parsers.SubtitleParser;
import java.awt.EventQueue;
import java.io.File;
import java.util.List;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import com.subsnj.ui.UISyncer;
import com.subsnj.util.Utils;

/**
 * The class that's used for subtitle synchronisation tasks. Tasks performed by
 * this class are all within a background thread, with chunks being sent to the
 * EDT in order to provide the user with real-time information on the sync task.
 * That information will appear in the main UI log.
 */
public class Synchroniser extends SwingWorker<Void, Integer> {

    private int mode;
    public static final int ADVANCE = 0;
    public static final int DELAY = 1;
    private Interval amount;
    private JProgressBar bar;
    private SubtitleParser parser;
    private boolean errors;
    private boolean overwriteFile;
    private List<Subtitle> subtitles;
    private File activeFile;
    private UISyncer uiSyncer;
    private int startRange;
    private int endRange;

    public Synchroniser(SubtitleParser parser, int mode, Interval amount,
            int startRange, int endRange, boolean overwriteFile) {
        uiSyncer = UISyncer.getINSTANCE();
        this.parser = parser;
        this.mode = mode;
        this.amount = amount;
        this.startRange = startRange;
        this.endRange = endRange;
        this.bar = uiSyncer.getProgressBar();
        this.overwriteFile = overwriteFile;
        this.activeFile = parser.getActiveFile();
        this.subtitles = parser.getSubtitles();
        uiSyncer.enableSyncRelatedComps(false);
    }

    @Override
    protected Void doInBackground() {
        try {
            uiSyncer.log("Starting synchronisation...");
            for (int i = startRange - 1; i < endRange; i++) {
                if (subtitles.get(i).getState() == Subtitle.HEALTHY
                        || subtitles.get(i).getState() == Subtitle.PARTIALLY_CORRUPT) {
                    switch (mode) {
                        case ADVANCE:
                            subtitles.get(i).advance(amount);
                            break;
                        case DELAY:
                            subtitles.get(i).delay(amount);
                            break;
                    }
                } else {
                    errors = true;
                    final int corruptIndex = i + 1;
                    EventQueue.invokeLater(() -> {
                        uiSyncer.logError("Subtitle at index " + corruptIndex + " is corrupted and cannot be synchronised.");
                    });
                }
                publish(Utils.percentage(i, endRange));
                Thread.sleep(1); //add some artificial delay
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    protected void process(List<Integer> chunks) {
        for (Integer chunk : chunks) {
            if (chunk == bar.getValue()) {
                continue;
            }
            bar.setValue(chunk);
        }
    }

    @Override
    protected void done() {
        if (bar.getValue() < 100) { //don't ask me why
            bar.setValue(100);
        }
        String logTxt = "The file '" + activeFile.getName()
                + "' has been " + (mode == ADVANCE ? "advanced" : "delayed") + " by " + amount + ".";
        if (errors) {
            uiSyncer.logWarning(logTxt);
        } else {
            uiSyncer.logSuccess(logTxt);
        }
        parser.spitFile(overwriteFile);
        uiSyncer.enableSyncRelatedComps(true);
    }

}
