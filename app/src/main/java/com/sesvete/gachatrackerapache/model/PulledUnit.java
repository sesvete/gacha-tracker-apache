package com.sesvete.gachatrackerapache.model;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sesvete.gachatrackerapache.R;
import com.sesvete.gachatrackerapache.helper.ApiService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class PulledUnit {
    private int numOfPulls;
    private String unitName;
    private int fromBanner;
    private String date;

    public PulledUnit() {
        // Required empty constructor
    }

    public PulledUnit(int numOfPulls, String unitName, int fromBanner, String date) {
        this.numOfPulls = numOfPulls;
        this.unitName = unitName;
        this.fromBanner = fromBanner;
        this.date = date;
    }

    public int getNumOfPulls() {
        return numOfPulls;
    }

    public void setNumOfPulls(int numOfPulls) {
        this.numOfPulls = numOfPulls;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public int getFromBanner() {
        return fromBanner;
    }

    public void setFromBanner(int fromBanner) {
        this.fromBanner = fromBanner;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    // write to database
    // TODO: funckija za zapis enote v podtkovno bazo

    /*
    public void writePulledUnitToDatabase(String uid, String game, String banner){
        DatabaseHelper databaseHelper = new DatabaseHelper();
        databaseHelper.savePulledUnit(uid, game, banner, getUnitName(), getNumOfPulls(), isFromBanner(), getDate(), new DatabaseHelper.OnSavePulledUnitCallback() {
            @Override
            public void onSavedPulledUnit(boolean success) {
                if (success){
                    Log.d("Pulled unit Writing", "Successfully wrote to Database");
                } else {
                    Log.d("Pulled unit Writing", "Failed writing to Database");
                }
            }
        });
    }

     */

    public void writePulledUnitToDatabase(Context context, Resources resources, int uid, String game, String banner){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(resources.getString(R.string.server_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        Call<ResponseBody> call = apiService.insertPulledUnitToDatabase(uid, game, banner, getUnitName(), getNumOfPulls(), getFromBanner(), getDate());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    Log.d("Pulled unit Writing", "Successfully wrote to Database");
                    // TODO: also set counter to 0 in same call
                    // oz tle bo update visual counter
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
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
