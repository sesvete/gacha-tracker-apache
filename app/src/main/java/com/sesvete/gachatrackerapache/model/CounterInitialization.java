package com.sesvete.gachatrackerapache.model;

import com.google.gson.annotations.SerializedName;

public class CounterInitialization {
    private int progress;
    private int guaranteed;
    @SerializedName("num_of_pulls")
    private String numOfPulls;
    @SerializedName("unit_name")
    private String unitName;
    @SerializedName("from_banner")
    private String fromBanner;

    public CounterInitialization(int progress, int guaranteed, String numOfPulls, String unitName, String fromBanner) {
        this.progress = progress;
        this.guaranteed = guaranteed;
        this.numOfPulls = numOfPulls;
        this.unitName = unitName;
        this.fromBanner = fromBanner;
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

    public String getNumOfPulls() {
        return numOfPulls;
    }

    public void setNumOfPulls(String numOfPulls) {
        this.numOfPulls = numOfPulls;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getFromBanner() {
        return fromBanner;
    }

    public void setFromBanner(String fromBanner) {
        this.fromBanner = fromBanner;
    }
}
