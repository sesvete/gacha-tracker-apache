package com.sesvete.gachatrackerapache.model;

import java.util.ArrayList;

public class UserStats {
    private ArrayList<Integer> numOfPullsList;
    private ArrayList<Boolean> fiftyFiftyOutcomes;

    public UserStats() {
    }

    public UserStats(ArrayList<Integer> numOfPullsList, ArrayList<Boolean> fiftyFiftyOutcomes) {
        this.numOfPullsList = numOfPullsList;
        this.fiftyFiftyOutcomes = fiftyFiftyOutcomes;
    }

    public ArrayList<Integer> getNumOfPullsList() {
        return numOfPullsList;
    }

    public void setNumOfPullsList(ArrayList<Integer> numOfPullsList) {
        this.numOfPullsList = numOfPullsList;
    }

    public ArrayList<Boolean> getFiftyFiftyOutcomes() {
        return fiftyFiftyOutcomes;
    }

    public void setFiftyFiftyOutcomes(ArrayList<Boolean> fiftyFiftyOutcomes) {
        this.fiftyFiftyOutcomes = fiftyFiftyOutcomes;
    }

    @Override
    public String toString() {
        return "UserStats{" +
                "numOfPullsList=" + numOfPullsList +
                ", fiftyFiftyOutcomes=" + fiftyFiftyOutcomes +
                '}';
    }
}
