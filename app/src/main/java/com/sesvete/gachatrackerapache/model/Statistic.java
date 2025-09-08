package com.sesvete.gachatrackerapache.model;

public class Statistic {
    private String statisticDescription;
    private double statisticNumber;

    public Statistic(String statisticDescription, double statisticNumber) {
        this.statisticDescription = statisticDescription;
        this.statisticNumber = statisticNumber;
    }

    public String getStatisticDescription() {
        return statisticDescription;
    }

    public void setStatisticDescription(String statisticDescription) {
        this.statisticDescription = statisticDescription;
    }

    public double getStatisticNumber() {
        return statisticNumber;
    }

    public void setStatisticNumber(double statisticNumber) {
        this.statisticNumber = statisticNumber;
    }

    @Override
    public String toString() {
        return "Statistic{" +
                "statisticDescription='" + statisticDescription + '\'' +
                ", statisticNumber=" + statisticNumber +
                '}';
    }
}
