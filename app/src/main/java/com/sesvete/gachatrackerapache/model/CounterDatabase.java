package com.sesvete.gachatrackerapache.model;

public class CounterDatabase {
    private int uid;
    private String game;
    private String banner;
    private int progress;
    private int guaranteed;

    public CounterDatabase(int uid, String game, String banner, int progress, int guaranteed) {
        this.uid = uid;
        this.game = game;
        this.banner = banner;
        this.progress = progress;
        this.guaranteed = guaranteed;
    }

    public int getUid() {
        return uid;
    }

    public String getGame() {
        return game;
    }

    public String getBanner() {
        return banner;
    }

    public int getProgress() {
        return progress;
    }

    public int getGuaranteed() {
        return guaranteed;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void setGuaranteed(int guaranteed) {
        this.guaranteed = guaranteed;
    }
}
