package com.subsnj.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import com.subsnj.time.Interval;
import com.subsnj.subtitles.Subtitle;
import com.subsnj.ui.UISyncer;
import com.subsnj.util.Utils;

public abstract class SubtitleParser {

    private File activeFile;
    private String fileContents;
    private String[] subtitleBlocks;
    private List<Subtitle> subtitles;

    public SubtitleParser() {
    }

    /**
     * Creates a new parser with the properties of the given parser.
     *
     * @param parser The parser from which to take the properties.
     */
    public SubtitleParser(SubtitleParser parser) {
        this.activeFile = parser.getActiveFile();
        this.fileContents = parser.getFileContents();
        this.subtitleBlocks = parser.getSubtitleBlocks();
        this.subtitles = parser.getSubtitles();
    }

    public void beginParsing(String filePath) {
        activeFile = new File(filePath);
        loadFile();
        loadSubs();
    }

    /**
     * Loads the {@link #activeFile} to memory. The file will automatically be
     * encoded in ISO-8859-1. This method will also take care of files encoded
     * in UTF-8-BOM by removing (if they appear), the very first 3 characters
     * that represent BOM. Files encoded in other formats won't be affected.
     */
    private void loadFile() {
        StringBuilder sb = new StringBuilder();
        boolean firstLine = true;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(activeFile),
                "ISO-8859-1"))) {
            String line;
            while ((line = in.readLine()) != null) {
                if (firstLine && line.startsWith("ï»¿")) { //remove bom
                    line = line.substring(3);
                }
                sb.append(line).append(System.getProperty("line.separator"));
                firstLine = false;
            }
        } catch (IOException ex) {
            Utils.showErrorMessage("Error", "Could not read file.", UISyncer.getINSTANCE());
            System.err.println(ex.getMessage());
        }
        fileContents = sb.toString();
    }

    /**
     * Loads the subtitles into the program and may also perform verification
     * tasks depending on the implementation of this method.
     */
    public abstract void loadSubs();

    public File getActiveFile() {
        return activeFile;
    }

    public String getFileContents() {
        return fileContents;
    }

    public String[] getSubtitleBlocks() {
        return subtitleBlocks;
    }

    public String getFileName() {
        return activeFile.getName();
    }

    public String getFilePath() {
        return activeFile.getPath();
    }

    public List<Subtitle> getSubtitles() {
        return subtitles;
    }

    public Subtitle getSubtitle(int index) {
        return subtitles.get(index);
    }

    /**
     * Returns the amount of subtitles in the current list of subtitles.
     *
     * @return the amount of subtitles loaded in this parser.
     */
    public int getSubtitleCount() {
        return subtitles.size();
    }

    public void setSubtitles(List<Subtitle> subtitles) {
        this.subtitles = subtitles;
    }

    public void setSubtitleBlocks(String[] subtitleBlocks) {
        this.subtitleBlocks = subtitleBlocks;
    }

    public void printSubs() {
        for (Subtitle subtitle : subtitles) {
            System.out.println(subtitle);
            System.out.println();
        }
    }

    public void logWarning(String str) {
        UISyncer.getINSTANCE().logWarning(str);
    }

    public void logMessage(String str) {
        UISyncer.getINSTANCE().log(str);
    }

    public void logError(String str) {
        UISyncer.getINSTANCE().logError(str);
    }

    public void logSuccess(String str) {
        UISyncer.getINSTANCE().logSuccess(str);
    }

    /**
     * Returns a specific parser adapted to the file located at the path. If the
     * file is of type .srt, this method will return a {@code SRTParser} and so
     * on.
     *
     * @param filepath The filepath of the subtitle file.
     * @return a Subtitle parser.
     */
    public static SubtitleParser getParserForType(String filepath) {
        String[] filePathSplit = filepath.split("\\.");
        String type = filePathSplit[filePathSplit.length - 1];
        return type.toLowerCase().equals("srt") ? new SRTParser() : new SUBParser();
    }

    /**
     * Writes a file that will contain the subtitles stored within the program.
     * The user may choose to overwrite the existing file, or create a new one.
     * <p>
     * <b>Note:</b> by default, the program will create a new file for the
     * synchronised subtitles.</p>
     *
     * @param overwrite Whether to overwrite the existing file.
     * @return {@code true} if the writing was successful.
     */
    public abstract boolean spitFile(boolean overwrite);

    /**
     * Each new parser that inherits from this class requires a means to
     * understand how time is depicted in a subtitle file. This method
     * transforms a timestamp into a usable set of intervals.
     *
     * @param timestamp a timestamp in subtitle file format.
     * @return an array of two intervals.
     */
    public abstract Interval[] timestampToIntervals(String timestamp);

    /**
     * Same as {@link timestampToIntervals(java.lang.String)} but for strings
     * that contain only one part of a timestamp. E.g: only the start interval.
     *
     * @param timestamp a timestamp in subtitle format.
     * @return a single {@code Interval} object.
     */
    public abstract Interval singleTimestampToInterval(String timestamp);

    /**
     * Returns the pattern that must be followed for timestamps of this subtitle
     * format.
     *
     * @return a {@code String} as a generic timestamp providing an example of
     * what a legal timestamp should look like for this subtitle format. E.g:
     * for SRT subtitles, this method will return "00:00:00,000".
     */
    public abstract String getLegalTimePattern();

}
