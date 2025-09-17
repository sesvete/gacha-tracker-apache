package com.sesvete.gachatrackerapache.model;

public class CounterProgress {
    private int progress;
    private int guaranteed;

    public CounterProgress() {
        // required empty constructor
    }

    public CounterProgress(int progress, int guaranteed) {
        this.progress = progress;
        this.guaranteed = guaranteed;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getGuaranteed() {
        return guaranteed;
    }

    public void setGuaranteed(int guaranteed) {
        this.guaranteed = guaranteed;
    }
}
