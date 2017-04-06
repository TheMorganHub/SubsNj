package com.subsnj.subtitles;

import com.subsnj.time.Interval;

public class SRTSubtitle extends Subtitle {

    private String index;

    public SRTSubtitle() {
        super();
        setText("\r\n");
        index = "";
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    @Override
    public void appendLine(String text) {
        if (getText().equals("\r\n")) {
            setText(text);
        } else {
            appendText(text);
        }
    }

    public String getTimeStamp() {
        return getStartTime() + " --> " + getEndTime();
    }

    @Override
    public void advance(Interval amount) {
        getStartTime().advance(amount);
        getEndTime().advance(amount);
    }

    @Override
    public void delay(Interval amount) {
        getStartTime().delay(amount);
        getEndTime().delay(amount);
    }

    @Override
    public String toString() {
        return index + "\r\n" + getTimeStamp() + "\r\n" + getText();
    }
}
