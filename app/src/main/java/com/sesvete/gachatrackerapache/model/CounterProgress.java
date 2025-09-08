package com.sesvete.gachatrackerapache.model;

public class CounterProgress {
    private int number;
    private boolean guaranteed;

    public CounterProgress() {
        // required empty constructor
    }

    public CounterProgress(int number, boolean guaranteed) {
        this.number = number;
        this.guaranteed = guaranteed;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isGuaranteed() {
        return guaranteed;
    }

    public void setGuaranteed(boolean guaranteed) {
        this.guaranteed = guaranteed;
    }

    @Override
    public String toString() {
        return "CounterProgress{" +
                "number=" + number +
                ", guaranteed=" + guaranteed +
                '}';
    }
}
