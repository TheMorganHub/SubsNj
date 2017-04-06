package com.subsnj.subtitles;

import com.subsnj.time.Interval;

public class SUBSubtitle extends Subtitle {

    public SUBSubtitle() {
    }

    @Override
    public void appendLine(String text) {
    }

    @Override
    public void delay(Interval interval) {
    }

    @Override
    public void advance(Interval interval) {
    }

    @Override
    public String toString() {
        return getStartTime() + " - " + getEndTime() + "\n" + getText();
    }
}
