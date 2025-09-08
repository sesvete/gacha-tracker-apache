package com.sesvete.gachatrackerapache.helper;

import java.util.ArrayList;
import java.util.List;

public class StatsHelper {

    public static int numWonFiftyFifty(List<Boolean> outcomes){
        int won = 0;
        for (boolean outcome : outcomes){
            if (outcome){
                won += 1;
            }
        }
        return won;
    }

    public static int numLostFiftyFifty(List<Boolean> outcomes){
        int lost = 0;
        for (boolean outcome : outcomes){
            if (!outcome){
                lost += 1;
            }
        }
        return lost;
    }

    public static double percentageFiftyFifty(int numWon, int numLost){
        if (numWon + numLost == 0){
            return 0.0; // v primeru, da delimo z 0
        }
        double result = (double) (numWon) /(numWon + numLost);
        return Math.round(result * 100.0) / 100.0;
    }

    public static double avgNumPulls(List<Integer> numbers){
        if (numbers == null || numbers.isEmpty()) {
            return 0;
        }
        int sum = 0;
        for (int number : numbers) {
            sum += number;
        }
        double result = ((double) sum /numbers.size());
        return Math.round(result * 100.0) / 100.0;
    }

    public static int totalNumPulls(List<Integer> numbers) {
        if (numbers == null || numbers.isEmpty()) {
            return 0;
        }
        int sum = 0;
        for (int number : numbers) {
            sum += number;
        }
        return sum;
    }

    public static int sumArrayIntegerList(ArrayList<Integer> numbers) {
        int sum = 0;
        for (int number : numbers) {
            sum += number;
        }
        return sum;
    }

    public static double sumArrayDoubleList(ArrayList<Double> numbers) {
        double sum = 0;
        for (double number : numbers) {
            sum += number;
        }
        return sum;
    }

    public static double calculateListAvg(double number, int listSize) {
        double result = number/listSize;
        return Math.round(result * 100.0) / 100.0;
    }
}
