package com.sesvete.gachatrackerapache.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sesvete.gachatrackerapache.MainActivity;
import com.sesvete.gachatrackerapache.R;
import com.sesvete.gachatrackerapache.SignInWithPasswordActivity;
import com.sesvete.gachatrackerapache.model.LoginResponse;
import com.sesvete.gachatrackerapache.model.RefreshTokenResponse;
import com.sesvete.gachatrackerapache.model.ResponseError;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthenticationHelperApache {

    public static void signupUser(String email, String password, Activity activity, Resources resources, Context context, long timerCreateAccountStart){

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
                    editor.putLong("expireTime", loginResponse.getExpireTime());
                    editor.putString("token", loginResponse.getToken());
                    editor.apply();

                    long timerCreateAccountEnd = System.nanoTime();
                    long timerCreateAccountResult = (timerCreateAccountEnd - timerCreateAccountStart)/1000000;
                    Log.i("Timer Create Email account", Long.toString(timerCreateAccountResult) + " " + "ms");

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

    public static void loginUser(String email, String password, Activity activity, Resources resources, Context context, long timerSingInEmailStart){
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
                    editor.putLong("expireTime", loginResponse.getExpireTime());
                    editor.putString("token", loginResponse.getToken());
                    editor.apply();

                    long timerSingInEmailEnd = System.nanoTime();
                    long timerSingInEmailResult = (timerSingInEmailEnd - timerSingInEmailStart)/1000000;
                    Log.i("Timer sing in Email", Long.toString(timerSingInEmailResult) + " " + "ms");

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
        long timerLogoutStart = System.nanoTime();
        // The server doesn't need to do anything to log a user out since tokens are stateless.
        // Simply clear the token from the app's local storage.
        clearLocalSession(activity);
        long timerLogoutEnd = System.nanoTime();
        long timerLogoutResult = (timerLogoutEnd - timerLogoutStart)/1000000;
        Log.i("Timer Logout", Long.toString(timerLogoutResult) + " " + "ms");
    }

    public static void refreshToken(Context context, Resources resources, Activity activity){
        Retrofit retrofit = ApiClient.getClient(context, resources);
        ApiService apiService = retrofit.create(ApiService.class);

        Call<RefreshTokenResponse> call = apiService.refreshToken();
        call.enqueue(new Callback<RefreshTokenResponse>() {
            @Override
            public void onResponse(Call<RefreshTokenResponse> call, Response<RefreshTokenResponse> response) {
                if (response.isSuccessful()){
                    RefreshTokenResponse refreshTokenResponse = response.body();

                    // Save session state to SharedPreferences
                    SharedPreferences sharedPref = activity.getSharedPreferences("GachaTrackerPrefs", context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putLong("expireTime", refreshTokenResponse.getExpireTime());
                    editor.putString("token", refreshTokenResponse.getToken());
                    editor.apply();

                    Intent intent = new Intent(activity, MainActivity.class);
                    activity.startActivity(intent);
                    activity.finish();

                } else {
                    Intent intent = new Intent(activity, SignInWithPasswordActivity.class);
                    activity.startActivity(intent);
                    activity.finish();
                }
            }

            @Override
            public void onFailure(Call<RefreshTokenResponse> call, Throwable t) {
                Toast.makeText(activity, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(activity, SignInWithPasswordActivity.class);
                activity.startActivity(intent);
                activity.finish();
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
