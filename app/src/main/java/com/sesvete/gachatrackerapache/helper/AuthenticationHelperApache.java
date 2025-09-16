package com.sesvete.gachatrackerapache.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sesvete.gachatrackerapache.MainActivity;
import com.sesvete.gachatrackerapache.R;
import com.sesvete.gachatrackerapache.SignInWithPasswordActivity;
import com.sesvete.gachatrackerapache.model.LoginResponse;
import com.sesvete.gachatrackerapache.model.ResponseError;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthenticationHelperApache {

    public static void signupUser(String email, String password, Activity activity, Resources resources, Context context){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(resources.getString(R.string.server_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        Call<LoginResponse> call = apiService.signupUser(email, password);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()){
                    LoginResponse loginResponse = response.body();

                    // Save session state to SharedPreferences
                    SharedPreferences sharedPref = activity.getSharedPreferences("GachaTrackerPrefs", context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("uid", loginResponse.getUid());
                    editor.putString("username", loginResponse.getUsername());
                    editor.putLong("expiresAt", loginResponse.getExpires_at());
                    editor.apply();

                    Intent intent = new Intent(activity, MainActivity.class);
                    activity.startActivity(intent);
                    activity.finish();

                } else {
                    // handle errors
                    try {
                        Gson gson = new GsonBuilder().create();
                        ResponseError error = gson.fromJson(response.errorBody().string(), ResponseError.class);
                        Toast.makeText(activity, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        Toast.makeText(activity, "An unexpected error occurred.", Toast.LENGTH_SHORT).show();
                    }
                }

            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                // Handle network errors (e.g., no internet connection)
                Toast.makeText(activity, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void loginUser(String email, String password, Activity activity, Resources resources, Context context){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(resources.getString(R.string.server_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        Call<LoginResponse> call = apiService.loginUser(email, password);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()){
                    LoginResponse loginResponse = response.body();

                    // Save session state to SharedPreferences
                    SharedPreferences sharedPref = activity.getSharedPreferences("GachaTrackerPrefs", context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("uid", loginResponse.getUid());
                    editor.putString("username", loginResponse.getUsername());
                    editor.putLong("expiresAt", loginResponse.getExpires_at());
                    editor.apply();

                    Intent intent = new Intent(activity, MainActivity.class);
                    activity.startActivity(intent);
                    activity.finish();

                } else {
                    // handle errors
                    try {
                        Gson gson = new GsonBuilder().create();
                        ResponseError error = gson.fromJson(response.errorBody().string(), ResponseError.class);
                        Toast.makeText(activity, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        Toast.makeText(activity, "An unexpected error occurred.", Toast.LENGTH_SHORT).show();
                    }
                }

            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                // Handle network errors (e.g., no internet connection)
                Toast.makeText(activity, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void logoutUser(Resources resources, Activity activity){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(resources.getString(R.string.server_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        Call<ResponseBody> call = apiService.logoutUser();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // Clear the local session data regardless of server response
                clearLocalSession(activity);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Clear the local session data even on network failure
                clearLocalSession(activity);
            }
        });
    }

    private static void clearLocalSession(Activity activity){
        SharedPreferences sharedPref = activity.getSharedPreferences("GachaTrackerPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(activity, SignInWithPasswordActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

}
