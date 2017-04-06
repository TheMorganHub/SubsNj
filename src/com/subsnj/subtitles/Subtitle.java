package com.subsnj.subtitles;

import com.subsnj.time.Interval;

/**
 * A class to represent the characteristics and behaviour shared amongst all
 * subtitle formats.
 *
 * @author Morgan
 */
public abstract class Subtitle {

    private Interval startTime;
    private Interval endTime;
    private int state;
    private String text;

    /**
     * Represents a subtitle that is healthy and legal within SRT format.
     * Healthy subtitles may be synchronised or altered.
     */
    public static final int HEALTHY = 0;

    /**
     * Represents a subtitle that is partially corrupt but has its timestamp
     * intact. Partially corrupt subtitles may still be synchronised.
     */
    public static final int PARTIALLY_CORRUPT = 1;

    /**
     * Represents a subtitle that is beyond repair. Fully corrupt subtitles
     * cannot be touched and won't be synchronised.
     */
    public static final int FULLY_CORRUPT = 2;

    public Subtitle() {
        this.state = Subtitle.HEALTHY;
    }

    public abstract void appendLine(String text);

    public void setIntervals(Interval[] intervals) {
        startTime = intervals[0];
        endTime = intervals[1];
    }

    public void setStartTime(Interval startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Interval endTime) {
        this.endTime = endTime;
    }

    public Interval getStartTime() {
        return startTime;
    }

    public Interval getEndTime() {
        return endTime;
    }

    public Interval[] getIntervals() {
        return new Interval[]{startTime, endTime};
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void appendText(String text) {
        this.text += text;
    }

    public abstract void delay(Interval interval);

    public abstract void advance(Interval interval);

}
