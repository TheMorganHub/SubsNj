package com.subsnj.parsers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import com.subsnj.time.Interval;
import com.subsnj.subtitles.SRTSubtitle;
import com.subsnj.subtitles.Subtitle;
import com.subsnj.ui.UISyncer;
import com.subsnj.util.Utils;

/**
 * A {@code SRTParser} object is used to beginParsing a SRT file and create, for
 * each subtitle found within the file, a {@link SRTSubtitle} object, which can
 * then be altered individually by the program.
 *
 * @author Morgan
 */
public class SRTParser extends SubtitleParser {

    private int lastGoodIndex;
    /**
     * The pattern of an index in a legal SRT file.
     */
    public static Pattern indexPattern = Pattern.compile("[0-9]+");
    /**
     * The pattern of a timestamp in a legal SRT file.
     */
    public static Pattern timestampPattern = Pattern.compile("((\\d{2}):){2}(\\d{2}),(\\d{3}) "
            + "--> ((\\d{2}):){2}(\\d{2}),(\\d{3})");

    public SRTParser() {
    }

    /**
     * The constructor that is to be used when a convertion from one subtitle
     * format to another takes place.
     *
     * @param parser another {@code SubtitleParser}
     */
    public SRTParser(SubtitleParser parser) {
        super(parser);
    }

    /**
     * Used to cosmetically denote in the log that there have been corrupt
     * subtitles in the last verification.
     */
    private boolean corruptSubtitles;
    /**
     * Used to cosmetically denote in the log that there have been fully corrupt
     * subtitles in the last verification.
     */
    private boolean fullyCorruptSubtitles;

    /**
     * If true, the parser will ignore indices received from the file and will
     * count from the last good index + 1 for the next subtitle. A good index is
     * an index belonging to a partially corrupt or healthy subtitle. This flag
     * will be set to true whenever the parser encounters a subtitle that is
     * missing an index.
     */
    private boolean useLastGoodIndex;

    /**
     * Populates the {@link #subtitles} list by splitting the contents of the
     * active file in blocks, as can be seen in any legal SRT file. Each block
     * is then split further by line and validated. Once the latter is done, the
     * subtitle is finally added to the list.
     */
    @Override
    public void loadSubs() {
        try {
            corruptSubtitles = false;
            fullyCorruptSubtitles = false;
            useLastGoodIndex = false;
            lastGoodIndex = 0;
            List<Subtitle> subtitles = new ArrayList<>();
            setSubtitleBlocks(Pattern.compile("\\r\\n[\\r\\n]+").split(getFileContents()));
            String[] subtitleBlocks = getSubtitleBlocks();
            logMessage("Found ~" + subtitleBlocks.length + " subtitles in file.");
            logMessage("Checking file integrity...");

            for (int i = 0; i < subtitleBlocks.length; i++) {
                String[] subtitleBlock = subtitleBlocks[i].split("\r\n");
                SRTSubtitle subtitle = new SRTSubtitle();
                validateSubtitle(cleanSubtitleBlock(subtitleBlock), subtitle);
                subtitles.add(subtitle);
            }
            logMessage("End of verification...");
            if (fullyCorruptSubtitles) {
                logError("Some subtitles were found damaged beyond repair and could potentially lead "
                        + "to unpredicted behaviour during playback. It is recommended to manually inspect the "
                        + "subtitle file before synchronisation.");
            }
            if (corruptSubtitles || fullyCorruptSubtitles) {
                logWarning("Ready for synchronisation.\n");
            } else {
                logSuccess("Ready for synchronisation.\n");
            }
            setSubtitles(subtitles);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Validates a subtitle block according to the pattern of a subtitle block
     * in a SRT file. If any salvageable inconsistencies are found during
     * validation, the subtitle object will be marked as
     * {@link Subtitle#PARTIALLY_CORRUPT} and repairs will be attempted
     * depending on the problem. Examples that would fall under that category
     * are misplaced timestamps or invalid indices. However, if the subtitle is
     * found to be missing a timestamp, the subtitle object will be
     * automatically marked as {@link Subtitle#FULLY_CORRUPT}. If all the tests
     * are passed, the subtitle will be labelled {@link Subtitle#HEALTHY}.
     * <p>
     * <b>Note:</b> the validation process is not without its margin of error.
     * Arbitrary or obscure formatting errors may cause this method to
     * misdiagnose the subtitle, but most common cases are covered. Regardless,
     * it's always recommended to click the "Open original file" button whenever
     * serious issues arise.</p>
     *
     * @param subtitleBlock a block that represents a subtitle in a SRT file.
     * @param subtitle a {@link SRTSubtitle} object whose properties will be
     * populated as each section of the block is validated.
     */
    public void validateSubtitle(String[] subtitleBlock, SRTSubtitle subtitle) {

        boolean indexFound = false;
        boolean timestampFound = false;
        int indexIndex = -1;
        int timestampIndex = -1;
        String[] emergencySubBlock = null;
        for (int i = 0; i < subtitleBlock.length; i++) {
            if (!indexFound && indexPattern.matcher(subtitleBlock[i].trim()).matches()) {
                int subIndex = Integer.parseInt(subtitleBlock[i].trim());
                subtitle.setIndex(useLastGoodIndex ? "" + (lastGoodIndex + 1) : "" + subIndex);
                indexFound = true;
                indexIndex = i;
            }
            if (!timestampFound && timestampPattern.matcher(subtitleBlock[i]).matches()) {
                subtitle.setIntervals(timestampToIntervals(subtitleBlock[i]));
                timestampFound = true;
                timestampIndex = i;
            }
        }
        if (!indexFound && !timestampFound) {
            subtitle.setState(Subtitle.FULLY_CORRUPT);
            logError("Subtitle between index " + lastGoodIndex + " and " + (lastGoodIndex + 1)
                    + " is malformed and cannot be recovered.");
            return;
        }
        if (!indexFound) { //time to repair the corruptIndex :)
            subtitle.setIndex("" + (lastGoodIndex + 1));
            useLastGoodIndex = true;
            indexIndex = 0;
            timestampIndex++;
            emergencySubBlock = new String[subtitleBlock.length + 1];
            emergencySubBlock[0] = "" + subtitle.getIndex();
            System.arraycopy(subtitleBlock, 0, emergencySubBlock, 1, subtitleBlock.length);
            subtitle.setState(Subtitle.PARTIALLY_CORRUPT);
            corruptSubtitles = true;
            logWarning("Subtitle at index " + (lastGoodIndex + 1) + " had an invalid index and has been repaired.");
        }
        if (!timestampFound) {
            subtitle.setState(Subtitle.FULLY_CORRUPT);
            fullyCorruptSubtitles = true;
            logError("Subtitle at index " + (lastGoodIndex + 1) + " is missing a timestamp and cannot be recovered.");
            return;
        } else {
            if (timestampIndex != 1 && indexFound) {
                corruptSubtitles = true;
                logWarning("Subtitle at index " + (lastGoodIndex + 1) + " had a misplaced timestamp and has been repaired.");
            }
        }
        BodyOffsets bodyOffs = new BodyOffsets(indexIndex, timestampIndex,
                emergencySubBlock == null ? subtitleBlock : emergencySubBlock);
        if (bodyOffs.bodyIsEmpty()) { //subtitle has no body
            subtitle.setState(Subtitle.PARTIALLY_CORRUPT);
            corruptSubtitles = true;
            logWarning("Subtitle at index " + (lastGoodIndex + 1) + " has a missing body.");
        } else {
            bodyOffs.populate(subtitle);
        }
        lastGoodIndex = Integer.parseInt(subtitle.getIndex());
    }

    /**
     * Removes any empty lines from a given subtitle block.
     *
     * @param subtitleBlock a Subtitle block.
     * @return a {@code String} array with no empty lines.
     */
    public String[] cleanSubtitleBlock(String[] subtitleBlock) {
        List<String> cleanBlock = new ArrayList<>();
        for (String line : subtitleBlock) {
            if (!line.isEmpty()) {
                cleanBlock.add(line);
            }
        }
        String[] cleanArray = new String[cleanBlock.size()];
        for (int j = 0; j < cleanBlock.size(); j++) {
            cleanArray[j] = cleanBlock.get(j);
        }
        return cleanArray;
    }

    /**
     * Writes the newly synchronised file to disk. The file will be located
     * exactly where {@link #activeFile} is. UISyncer will append ' (Synced)' to
     * the name of the file to differentiate it from the original, unless the
     * user chooses to overwrite the original file.
     * <p>
     * Subtitles that are healthy or partially corrupt will be written normally
     * from the {@link #subtitles} list. The ones that aren't will be copied
     * exactly as they were found in the file, without any alterations made to
     * them.</p>
     * <p>
     * The encoding to be used in this operation is 'ISO-8859-1'.</p>
     *
     * @param overwrite whether to overwrite the original file or create a new
     * one.
     * @return {@code true} if the writing of the file was successful.
     */
    @Override
    public boolean spitFile(boolean overwrite) {
        File toWrite = overwrite ? new File(getActiveFile().getPath()) : Utils.appendToFileName(getActiveFile(), " (Synced)");
        List<Subtitle> subtitles = getSubtitles();
        String[] subtitleBlocks = getSubtitleBlocks();
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(toWrite),
                "ISO-8859-1"))) {
            for (int i = 0; i < subtitles.size(); i++) {
                bw.write(i == 0 ? "" : "\r\n");
                switch (subtitles.get(i).getState()) {
                    case Subtitle.HEALTHY:
                    case Subtitle.PARTIALLY_CORRUPT:
                        bw.write("" + subtitles.get(i));
                        break;
                    case Subtitle.FULLY_CORRUPT:
                        String[] corrupted = subtitleBlocks[i].split("\r\n");
                        int sizeDiff = corrupted.length < 3 ? 3 - corrupted.length : 1;
                        bw.write(subtitleBlocks[i]);
                        for (int j = 0; j < sizeDiff; j++) {
                            bw.write("\r\n");
                        }
                        break;
                }
            }
            UISyncer.getINSTANCE().log("The file is located at " + toWrite.getPath());
            return true;
        } catch (IOException ex) {
            Utils.showErrorMessage("Error", "Could not save file", UISyncer.getINSTANCE());
            System.err.println(ex.getMessage());
        }
        return false;
    }

    @Override
    public Interval[] timestampToIntervals(String timestamp) {
        String[] timeStampSplit = timestamp.split("-->");
        return new Interval[]{singleTimestampToInterval(timeStampSplit[0].trim()),
            singleTimestampToInterval(timeStampSplit[1].trim())};
    }

    @Override
    public Interval singleTimestampToInterval(String timestamp) {
        String[] timeStampSplit = timestamp.split(":|,");
        int hours = Integer.parseInt(timeStampSplit[0]);
        int minutes = Integer.parseInt(timeStampSplit[1]);
        int seconds = Integer.parseInt(timeStampSplit[2]);
        int milliseconds = Integer.parseInt(timeStampSplit[3]);
        return new Interval(hours, minutes, seconds, milliseconds);
    }

    @Override
    public String getLegalTimePattern() {
        return "00:00:00,000";
    }

    /**
     * Used to represent the range or indices in a subtitle block that contain
     * the body or text of the subtitle.
     */
    private class BodyOffsets {

        private int timestampIndex;
        private int indexIndex;
        private String[] subtitleBlock;
        public int start;
        public int end;

        public BodyOffsets(int indexIndex, int timestampIndex, String[] subtitleblock) {
            this.indexIndex = indexIndex;
            this.timestampIndex = timestampIndex;
            this.subtitleBlock = subtitleblock;
            calculate();
        }

        public boolean bodyIsEmpty() {
            return start > end;
        }

        /**
         * Calculates the start and end offsets of the body.
         */
        public void calculate() {
            if (timestampIndex > indexIndex) {
                int diff = timestampIndex - indexIndex;
                if (diff > 1) {
                    start = subtitleBlock.length - timestampIndex;
                    end = timestampIndex - 1;
                } else {
                    start = timestampIndex + 1;
                    end = subtitleBlock.length - 1;
                }
            } else {
                int diff = indexIndex - timestampIndex;
                if (diff > 1) {
                    start = subtitleBlock.length - indexIndex;
                    end = indexIndex - 1;
                } else {
                    start = indexIndex + 1;
                    end = subtitleBlock.length - 1;
                }
            }
        }

        public void populate(SRTSubtitle subtitle) {
            for (int i = start; i <= end; i++) {
                subtitle.appendLine(subtitleBlock[i]);
                subtitle.appendLine("\r\n");
            }
        }

        @Override
        public String toString() {
            return "START: " + start + " | END: " + end;
        }

    }

}
