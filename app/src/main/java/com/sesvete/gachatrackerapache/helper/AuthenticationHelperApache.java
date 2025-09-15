package com.sesvete.gachatrackerapache.helper;

import android.app.Activity;
import android.content.res.Resources;
import android.widget.Toast;

import com.sesvete.gachatrackerapache.R;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthenticationHelperApache {

    public static void signupUser(String email, String password, Activity activity, Resources resources){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(resources.getString(R.string.server_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        Call<ResponseBody> call = apiService.signupUser(email, password);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    Toast.makeText(activity, "Account created successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, "Account creation failed!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Handle network errors (e.g., no internet connection)
                Toast.makeText(activity, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }

}
