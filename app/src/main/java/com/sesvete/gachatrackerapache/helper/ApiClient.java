package com.sesvete.gachatrackerapache.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import com.sesvete.gachatrackerapache.R;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient(Context context, Resources resources){
        if (retrofit == null) {
            SharedPreferences sharedPref = context.getSharedPreferences("GachaTrackerPrefs", Context.MODE_PRIVATE);
            String jwtToken = sharedPref.getString("token", null);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request originalRequest = chain.request();

                    // If a JWT token exists, add it to the Authorization header
                    Request.Builder builder = originalRequest.newBuilder();
                    if (jwtToken != null) {
                        builder.header("Authorization", "Bearer " + jwtToken);
                    }

                    Request newRequest = builder.build();
                    return chain.proceed(newRequest);
                }
            });
            OkHttpClient client = httpClient.build();
            // Build the Retrofit client with the custom OkHttpClient
            retrofit = new Retrofit.Builder()
                    .baseUrl(resources.getString(R.string.server_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}
