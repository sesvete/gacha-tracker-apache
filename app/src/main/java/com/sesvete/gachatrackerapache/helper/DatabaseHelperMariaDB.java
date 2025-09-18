package com.sesvete.gachatrackerapache.helper;

import android.content.Context;
import android.content.res.Resources;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sesvete.gachatrackerapache.R;
import com.sesvete.gachatrackerapache.model.PulledUnit;
import com.sesvete.gachatrackerapache.model.ResponseError;

import java.util.ArrayList;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DatabaseHelperMariaDB {

    public static void updateDatabaseCounter(Context context, Resources resources, int uid, String game, String banner, int progress, boolean guaranteed, Button btnCounterConfirm, Button btnCounterPlusOne, Button btnCounterPlusX, Button btnCounterPlusTen){

        int dbGuaranteed = 0;
        if (guaranteed){
            dbGuaranteed = 1;
        } else {
            dbGuaranteed = 0;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(resources.getString(R.string.server_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        Call<ResponseBody> call = apiService.updateDatabaseCounter(uid, game, banner, progress, dbGuaranteed);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    enableButtons(btnCounterConfirm, btnCounterPlusOne, btnCounterPlusX, btnCounterPlusTen);
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

    public static void retrievePullsHistory (Context context, Resources resources, int uid, String game, String banner, HistoryRecViewAdapter adapter, RecyclerView recyclerViewHistory){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(resources.getString(R.string.server_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        Call<ArrayList<PulledUnit>> call = apiService.getHistoryFromDatabase(uid, game, banner);
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

}
