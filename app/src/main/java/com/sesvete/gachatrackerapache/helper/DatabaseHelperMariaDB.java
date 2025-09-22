package com.sesvete.gachatrackerapache.helper;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sesvete.gachatrackerapache.R;
import com.sesvete.gachatrackerapache.model.PulledUnit;
import com.sesvete.gachatrackerapache.model.ResponseError;
import com.sesvete.gachatrackerapache.model.Statistic;
import com.sesvete.gachatrackerapache.model.UserStats;

import java.util.ArrayList;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DatabaseHelperMariaDB {

    public static void updateDatabaseCounter(Context context, Resources resources, String game, String banner, int progress, boolean guaranteed, Button btnCounterConfirm, Button btnCounterPlusOne, Button btnCounterPlusX, Button btnCounterPlusTen, long timerBtnPlusOneStart){

        int dbGuaranteed = 0;
        if (guaranteed){
            dbGuaranteed = 1;
        } else {
            dbGuaranteed = 0;
        }

        Retrofit retrofit = ApiClient.getClient(context, resources);

        ApiService apiService = retrofit.create(ApiService.class);

        Call<ResponseBody> call = apiService.updateDatabaseCounter(game, banner, progress, dbGuaranteed);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    enableButtons(btnCounterConfirm, btnCounterPlusOne, btnCounterPlusX, btnCounterPlusTen);
                    long timerBtnPlusOneEnd = System.nanoTime();
                    long timerBtnPlusOneResult = (timerBtnPlusOneEnd - timerBtnPlusOneStart)/1000000;
                    Log.i("Timer btn +", Long.toString(timerBtnPlusOneResult) + " " + "ms");
                }
                else {
                    try {
                        Gson gson = new GsonBuilder().create();
                        ResponseError error = gson.fromJson(response.errorBody().string(), ResponseError.class);
                        Toast.makeText(context, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        Toast.makeText(context, "An unexpected error occurred.", Toast.LENGTH_SHORT).show();
                    }
                    enableButtons(btnCounterConfirm, btnCounterPlusOne, btnCounterPlusX, btnCounterPlusTen);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                enableButtons(btnCounterConfirm, btnCounterPlusOne, btnCounterPlusX, btnCounterPlusTen);
            }
        });
    }

    public static void retrievePullsHistory (Context context, Resources resources, String game, String banner, HistoryRecViewAdapter adapter, RecyclerView recyclerViewHistory, long timerHistoryStart){
        Retrofit retrofit = ApiClient.getClient(context, resources);

        ApiService apiService = retrofit.create(ApiService.class);
        Call<ArrayList<PulledUnit>> call = apiService.getHistoryFromDatabase(game, banner);
        call.enqueue(new Callback<ArrayList<PulledUnit>>() {
            @Override
            public void onResponse(Call<ArrayList<PulledUnit>> call, Response<ArrayList<PulledUnit>> response) {
                if (response.isSuccessful() && response.body() != null ){
                    ArrayList<PulledUnit> pulledUnitsList = response.body();
                    if (!pulledUnitsList.isEmpty()){
                        adapter.setPulledUnits(pulledUnitsList);

                        recyclerViewHistory.setAdapter(adapter);
                        recyclerViewHistory.setLayoutManager(new LinearLayoutManager(context));
                    }
                    long timerHistoryEnd = System.nanoTime();
                    long timerHistoryResult= (timerHistoryEnd - timerHistoryStart)/1000000;
                    Log.i("Timer history", Long.toString(timerHistoryResult) + " " + "ms");
                } else {
                    try {
                        Gson gson = new GsonBuilder().create();
                        ResponseError error = gson.fromJson(response.errorBody().string(), ResponseError.class);
                        Toast.makeText(context, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        Toast.makeText(context, "An unexpected error occurred.", Toast.LENGTH_SHORT).show();
                    }
                    recyclerViewHistory.setAdapter(adapter);
                    recyclerViewHistory.setLayoutManager(new LinearLayoutManager(context));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<PulledUnit>> call, Throwable t) {
                Toast.makeText(context, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                recyclerViewHistory.setAdapter(adapter);
                recyclerViewHistory.setLayoutManager(new LinearLayoutManager(context));
            }
        });
    }

    public static void getPersonalStats(Context context, Resources resources, ArrayList<Statistic> statisticList, String game, String bannerType, StatsRecViewAdapter adapter, long timerPersonalStatsStart, TextView txtStatsTitle, MaterialButton btnStatsPersonal, MaterialButton btnStatsGlobal){
        statisticList.clear();
        Retrofit retrofit = ApiClient.getClient(context, resources);
        ApiService apiService = retrofit.create(ApiService.class);
        Call<UserStats> call = apiService.getPersonalStatsFromDatabase(game, bannerType);
        call.enqueue(new Callback<UserStats>() {
            @Override
            public void onResponse(Call<UserStats> call, Response<UserStats> response) {
                if (response.isSuccessful() && response.body() != null){
                    UserStats userStats = response.body();
                    ArrayList<Integer> numOfPullsList = userStats.getNumOfPullsList();
                    ArrayList<Boolean> fiftyFiftyOutcomes = userStats.getFiftyFiftyOutcomes();
                    int numOfFiveStars = numOfPullsList.size();
                    if (!bannerType.equals("standard") && !bannerType.equals("bangboo")){
                        int intNumWonFiftyFifty = StatsHelper.numWonFiftyFifty(fiftyFiftyOutcomes);
                        int intNumLostFiftyFifty = StatsHelper.numLostFiftyFifty(fiftyFiftyOutcomes);
                        double doublePercentageFiftyFifty = StatsHelper.percentageFiftyFifty(intNumWonFiftyFifty, intNumLostFiftyFifty);

                        statisticList.add(new Statistic(resources.getString(R.string.percentage_fifty_fifty), doublePercentageFiftyFifty));
                        statisticList.add(new Statistic(resources.getString(R.string.total_won_fifty_fifty), intNumWonFiftyFifty));
                        statisticList.add(new Statistic(resources.getString(R.string.total_lost_fifty_fifty), intNumLostFiftyFifty));
                    }
                    double doubleAvgNumPulls = StatsHelper.avgNumPulls(numOfPullsList);
                    int intTotalNumPulls = StatsHelper.totalNumPulls(numOfPullsList);
                    int currencyValue;

                    if (game.equals("tribe_nine")){
                        currencyValue = 120;
                    }
                    else {
                        currencyValue = 160;
                    }
                    statisticList.add(new Statistic(resources.getString(R.string.avg_for_five_star), doubleAvgNumPulls));
                    statisticList.add(new Statistic(resources.getString(R.string.total_five_stars), numOfFiveStars));
                    statisticList.add(new Statistic(resources.getString(R.string.total_num_pulls), intTotalNumPulls));
                    statisticList.add(new Statistic(resources.getString(R.string.avg_currency_five_star), Math.round((doubleAvgNumPulls * currencyValue) * 100.0) / 100.0));
                    statisticList.add(new Statistic(resources.getString(R.string.total_currency_five_star), intTotalNumPulls * currencyValue));
                    adapter.setStatisticList(statisticList);

                    long timerPersonalStatsEnd = System.nanoTime();
                    long timerPersonalStatsResult= (timerPersonalStatsEnd - timerPersonalStatsStart)/1000000;
                    Log.i("Timer Personal Stats", Long.toString(timerPersonalStatsResult) + " " + "ms");
                    onPersonalStatsPress(txtStatsTitle, btnStatsPersonal, btnStatsGlobal);
                }
                else {
                    try {
                        Gson gson = new GsonBuilder().create();
                        ResponseError error = gson.fromJson(response.errorBody().string(), ResponseError.class);
                        Toast.makeText(context, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        Toast.makeText(context, "An unexpected error occurred.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<UserStats> call, Throwable t) {
                Toast.makeText(context, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void getGlobalStats(Context context, Resources resources, ArrayList<Statistic> statisticList, String game, String bannerType, StatsRecViewAdapter adapter, long timerGlobalStatsStart, TextView txtStatsTitle, MaterialButton btnStatsPersonal, MaterialButton btnStatsGlobal){
        statisticList.clear();
        Retrofit retrofit = ApiClient.getClient(context, resources);

        ApiService apiService = retrofit.create(ApiService.class);
        Call<ArrayList<UserStats>> call = apiService.getGlobalStatsFromDatabase(game, bannerType);
        call.enqueue(new Callback<ArrayList<UserStats>>() {
            @Override
            public void onResponse(Call<ArrayList<UserStats>> call, Response<ArrayList<UserStats>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ArrayList<Integer> listNumOfFiveStars = new ArrayList<>();
                    ArrayList<Integer> listIntNumWonFiftyFifty = new ArrayList<>();
                    ArrayList<Integer> listIntNumLostFiftyFifty = new ArrayList<>();
                    ArrayList<Double> listDoublePercentageFiftyFifty = new ArrayList<>();
                    ArrayList<Double> listDoubleAvgNumPulls = new ArrayList<>();
                    ArrayList<Integer> listIntTotalNumPulls = new ArrayList<>();

                    ArrayList<UserStats> allUserStats = response.body();
                    for (UserStats singularUserStat : allUserStats){
                        ArrayList<Boolean> wonAndLost5050 = singularUserStat.getFiftyFiftyOutcomes();
                        ArrayList<Integer> pullsForFiveStar = singularUserStat.getNumOfPullsList();
                        if (!pullsForFiveStar.isEmpty() && !wonAndLost5050.isEmpty()){
                            int numOfFiveStars = pullsForFiveStar.size();
                            listNumOfFiveStars.add(numOfFiveStars);
                            int intNumWonFiftyFifty = StatsHelper.numWonFiftyFifty(wonAndLost5050);
                            listIntNumWonFiftyFifty.add(intNumWonFiftyFifty);
                            int intNumLostFiftyFifty = StatsHelper.numLostFiftyFifty(wonAndLost5050);
                            listIntNumLostFiftyFifty.add(intNumLostFiftyFifty);
                            double doublePercentageFiftyFifty = StatsHelper.percentageFiftyFifty(intNumWonFiftyFifty, intNumLostFiftyFifty);
                            listDoublePercentageFiftyFifty.add(doublePercentageFiftyFifty);
                            double doubleAvgNumPulls = StatsHelper.avgNumPulls(pullsForFiveStar);
                            listDoubleAvgNumPulls.add(doubleAvgNumPulls);
                            int intTotalNumPulls = StatsHelper.totalNumPulls(pullsForFiveStar);
                            listIntTotalNumPulls.add(intTotalNumPulls);
                        }
                    }
                    if (!listNumOfFiveStars.isEmpty()){
                        if (!bannerType.equals("standard") && !bannerType.equals("bangboo")){

                            double roundGlobalPercentageFifty = StatsHelper.calculateListAvg(StatsHelper.sumArrayDoubleList(listDoublePercentageFiftyFifty), listDoublePercentageFiftyFifty.size());
                            double roundGlobalTotalWonFifty = StatsHelper.calculateListAvg(StatsHelper.sumArrayIntegerList(listIntNumWonFiftyFifty), listIntNumWonFiftyFifty.size());
                            double roundGlobalTotalLostFifty = StatsHelper.calculateListAvg(StatsHelper.sumArrayIntegerList(listIntNumLostFiftyFifty), listIntNumLostFiftyFifty.size());

                            statisticList.add(new Statistic(resources.getString(R.string.percentage_fifty_fifty), roundGlobalPercentageFifty));
                            statisticList.add(new Statistic(resources.getString(R.string.total_won_fifty_fifty), roundGlobalTotalWonFifty));
                            statisticList.add(new Statistic(resources.getString(R.string.total_lost_fifty_fifty), roundGlobalTotalLostFifty));
                        }
                        int currencyValue;
                        if (game.equals("tribe_nine")){
                            currencyValue = 120;
                        }
                        else {
                            currencyValue = 160;
                        }
                        double sumAvgNumPulls = StatsHelper.sumArrayDoubleList(listDoubleAvgNumPulls)/listDoubleAvgNumPulls.size();

                        double roundSumAvgNumPulls = StatsHelper.calculateListAvg(StatsHelper.sumArrayDoubleList(listDoubleAvgNumPulls), listDoubleAvgNumPulls.size());
                        double roundSumAvgTotalNumPulls = StatsHelper.calculateListAvg(StatsHelper.sumArrayIntegerList(listIntTotalNumPulls), listIntTotalNumPulls.size());
                        double roundAvgTotalFiveStars = StatsHelper.calculateListAvg(StatsHelper.sumArrayIntegerList(listNumOfFiveStars), listNumOfFiveStars.size());

                        statisticList.add(new Statistic(resources.getString(R.string.avg_for_five_star), roundSumAvgNumPulls));
                        statisticList.add(new Statistic(resources.getString(R.string.total_five_stars), roundAvgTotalFiveStars));
                        statisticList.add(new Statistic(resources.getString(R.string.total_num_pulls), roundSumAvgTotalNumPulls));
                        statisticList.add(new Statistic(resources.getString(R.string.avg_currency_five_star), Math.round((sumAvgNumPulls * currencyValue) * 100.0) / 100.0));
                        statisticList.add(new Statistic(resources.getString(R.string.total_currency_five_star), roundSumAvgTotalNumPulls * currencyValue));

                        adapter.setStatisticList(statisticList);

                        long timerGlobalStatsEnd = System.nanoTime();
                        long timerGlobalStatsResult= (timerGlobalStatsEnd - timerGlobalStatsStart)/1000000;
                        Log.i("Timer Global Stats", Long.toString(timerGlobalStatsResult) + " " + "ms");
                        onGlobalStatsPress(txtStatsTitle, btnStatsPersonal, btnStatsGlobal);
                    }

                } else {
                    try {
                        Gson gson = new GsonBuilder().create();
                        ResponseError error = gson.fromJson(response.errorBody().string(), ResponseError.class);
                        Toast.makeText(context, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        Toast.makeText(context, "An unexpected error occurred.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<ArrayList<UserStats>> call, Throwable t) {
                Toast.makeText(context, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void enableButtons(Button btnCounterConfirm, Button btnCounterPlusOne, Button btnCounterPlusX, Button btnCounterPlusTen){
        btnCounterConfirm.setEnabled(true);
        btnCounterPlusOne.setEnabled(true);
        btnCounterPlusX.setEnabled(true);
        btnCounterPlusTen.setEnabled(true);
    }

    public static void disableButtons(Button btnCounterConfirm, Button btnCounterPlusOne, Button btnCounterPlusX, Button btnCounterPlusTen){
        btnCounterConfirm.setEnabled(false);
        btnCounterPlusOne.setEnabled(false);
        btnCounterPlusX.setEnabled(false);
        btnCounterPlusTen.setEnabled(false);
    }

    public static void onPersonalStatsPress(TextView txtStatsTitle, MaterialButton btnStatsPersonal, MaterialButton btnStatsGlobal){
        txtStatsTitle.setText(R.string.personal_stats);
        btnStatsPersonal.setEnabled(false);
        btnStatsGlobal.setEnabled(true);
    }

    public static void onGlobalStatsPress(TextView txtStatsTitle, MaterialButton btnStatsPersonal, MaterialButton btnStatsGlobal){
        txtStatsTitle.setText(R.string.global_stats);
        btnStatsPersonal.setEnabled(true);
        btnStatsGlobal.setEnabled(false);
    }

}
