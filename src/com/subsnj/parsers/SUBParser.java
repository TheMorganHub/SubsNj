package com.subsnj.parsers;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.subsnj.time.Interval;
import com.subsnj.subtitles.SRTSubtitle;
import com.subsnj.subtitles.SUBSubtitle;
import com.subsnj.subtitles.Subtitle;
import com.subsnj.ui.UISyncer;

/**
 * A parser used for subtitles of type .SUB. Due to the way SUB subtitles work,
 * it would make it really hard for users to consistently synchronise them,
 * which is why all this parser does is convert the SUB file into a SRT file.
 *
 * @author Morgan
 */
public class SUBParser extends SubtitleParser {

    /**
     * The frame rate at which the subtitles work
     */
    private double frames;

    public SUBParser() {
    }

    @Override
    public void loadSubs() {
        try {
            boolean errors = false;
            String[] subtitleBlocks = getFileContents().split("\r\n");
            Pattern p = Pattern.compile("(\\{\\d+\\}\\{\\d+\\})(...*)");
            List<Subtitle> subtitles = new ArrayList<>();
            logMessage("SUB format found. Converting to SRT...");
            for (int i = 0; i < subtitleBlocks.length; i++) {
                Matcher m = p.matcher(subtitleBlocks[i]);
                if (m.find() && m.groupCount() == 2) {
                    if (i == 0) {
                        frames = Double.parseDouble(m.group(2));
                        continue;
                    }
                    SUBSubtitle subtitle = new SUBSubtitle();
                    subtitle.setIntervals(timestampToIntervals(m.group(1)));
                    subtitle.setText(m.group(2));
                    subtitles.add(convertToSRT(subtitle, i));
                } else {
                    errors = true;
                    logWarning("Subtitle at index " + i + " is malformed or empty and won't be converted.");
                }
            }
            setSubtitles(subtitles);
            printSubs();
            UISyncer.getINSTANCE().setParser(new SRTParser(this));
            if (errors) {
                logWarning("Ready for synchronisation.");
            } else {
                logSuccess("Ready for synchronisation.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Converts a given {@code SUBSubtitle} into a {@code SRTSubtitle}.
     *
     * @param subtitle The subtitle to convert.
     * @param index The index of the subtitle.
     * @return a {@code SRTSubtitle}.
     */
    public SRTSubtitle convertToSRT(SUBSubtitle subtitle, int index) {
        SRTSubtitle srtSubtitle = new SRTSubtitle();
        srtSubtitle.setIndex("" + index);
        srtSubtitle.setIntervals(subtitle.getIntervals());
        srtSubtitle.setText(subtitle.getText().replaceAll("\\|", "\r\n") + "\r\n");
        return srtSubtitle;
    }

    //implementing this to be able to inherit from the SubtitleParser class
    @Override
    public boolean spitFile(boolean overwrite) {
        return true;
    }

    @Override
    public Interval[] timestampToIntervals(String timestamp) {
        double startFrameNumber = Double.parseDouble(timestamp.substring(1, timestamp.indexOf("}")));
        double endFrameNumber = Double.parseDouble(timestamp.substring(timestamp.lastIndexOf("{") + 1, timestamp.length() - 1));
        return new Interval[]{new Interval(startFrameNumber, frames), new Interval(endFrameNumber, frames)};
    }

    //implementing this to be able to inherit from SubtitleParser class
    @Override
    public Interval singleTimestampToInterval(String timestamp) {
        return null;
    }

    @Override
    public String getLegalTimePattern() {
        return "{start_frame}{end_frame}";
    }

}
