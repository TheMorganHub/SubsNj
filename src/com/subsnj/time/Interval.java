package com.subsnj.time;

import com.subsnj.util.Utils;

/**
 * Represents an interval of time. An interval can be used to delay or advance a
 * subtitle or to measure time.
 *
 * @author Morgan
 */
public class Interval {

    public int hours;
    public int minutes;
    public int seconds;
    public int milliseconds;

    public Interval(long ms) {
        this.milliseconds = (int) (ms % 1000);
        this.seconds = (int) (ms / 1000) % 60;
        this.minutes = (int) (ms / (1000 * 60)) % 60;
        this.hours = (int) (ms / (1000 * 60 * 60)) % 24;
    }

    /**
     * Constructor used for intervals in subtitle formats that use frames and
     * frame rate. It takes the exact frame number in which a subtitle appears
     * and divides it by the total framerate. It then multiplies this by 1000 to
     * obtain the exact time in ms in which the subtitle appears.
     *
     * @param frameNumber the specific frame in which the subtitle appears.
     * @param frameRate the total framerate of the video.
     */
    public Interval(double frameNumber, double frameRate) {
        double totalMs = (frameNumber / frameRate) * 1000;
        this.milliseconds = (int) (totalMs % 1000);
        this.seconds = (int) (totalMs / 1000) % 60;
        this.minutes = (int) (totalMs / (1000 * 60)) % 60;
        this.hours = (int) (totalMs / (1000 * 60 * 60)) % 24;
    }

    /**
     * Constructor that takes an exact amount of hours, minutes, seconds, and
     * milliseconds.
     *
     * @param hours
     * @param minutes
     * @param seconds
     * @param milliseconds
     */
    public Interval(int hours, int minutes, int seconds, int milliseconds) {
        this(Utils.convertToMs(hours, minutes, seconds, milliseconds));
    }

    public void delay(Interval amount) {
        setTimeInMs(toMs() + amount.toMs());
    }

    public void advance(Interval amount) {
        setTimeInMs(toMs() > amount.toMs() ? toMs() - amount.toMs() : 0);
    }

    public boolean isZero() {
        return toMs() == 0;
    }

    public long toMs() {
        return Utils.convertToMs(hours, minutes, seconds, milliseconds);
    }

    /**
     * Sets the time of this interval by an amount in ms.
     *
     * @param ms a new amount.
     */
    public void setTimeInMs(long ms) {
        this.seconds = (int) (ms / 1000) % 60;
        this.minutes = (int) (ms / (1000 * 60)) % 60;
        this.hours = (int) (ms / (1000 * 60 * 60)) % 24;
        this.milliseconds = (int) (ms % 1000);
    }

    @Override
    public String toString() {
        return (hours < 10 ? "0" + hours : hours)
                + ":"
                + (minutes < 10 ? "0" + minutes : minutes)
                + ":"
                + (seconds < 10 ? "0" + seconds : seconds)
                + ","
                + (milliseconds < 10 ? "00" + milliseconds
                        : milliseconds < 100 ? "0" + milliseconds : milliseconds);
    }

}
