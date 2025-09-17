package com.sesvete.gachatrackerapache.helper;

import com.sesvete.gachatrackerapache.model.CounterProgress;
import com.sesvete.gachatrackerapache.model.LoginResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {
    @FormUrlEncoded
    @POST("signup.php")
    Call<LoginResponse> signupUser(
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("login.php")
    Call<LoginResponse> loginUser(
            @Field("username") String username,
            @Field("password") String password
    );

    @POST("logout.php")
    Call<ResponseBody> logoutUser();

    @FormUrlEncoded
    @POST("update_counter.php")
    Call<ResponseBody> updateDatabaseCounter(
            @Field("uid") int uid,
            @Field("game") String game,
            @Field("banner") String banner,
            @Field("progress") int progress,
            @Field("guaranteed") int guaranteed
    );

    @FormUrlEncoded
    @POST("get_counter_data.php")
    Call<CounterProgress> getDatabaseCounterData(
            @Field("uid") int uid,
            @Field("game") String game,
            @Field("banner") String banner
    );

}
