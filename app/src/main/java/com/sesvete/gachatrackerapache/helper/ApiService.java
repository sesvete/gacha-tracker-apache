package com.sesvete.gachatrackerapache.helper;

import com.sesvete.gachatrackerapache.model.CounterInitialization;
import com.sesvete.gachatrackerapache.model.LoginResponse;
import com.sesvete.gachatrackerapache.model.PulledUnit;
import com.sesvete.gachatrackerapache.model.UserStats;

import java.util.ArrayList;

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
    Call<CounterInitialization> getDatabaseCounterData(
            @Field("uid") int uid,
            @Field("game") String game,
            @Field("banner") String banner
    );

    @FormUrlEncoded
    @POST("insert_pulled_unit.php")
    Call<ResponseBody> insertPulledUnitToDatabase(
            @Field("uid") int uid,
            @Field("game") String game,
            @Field("banner") String banner,
            @Field("unit_name") String unitName,
            @Field("num_of_pulls") int numOfPulls,
            @Field("from_banner") int fromBanner,
            @Field("date") String date

    );

    @FormUrlEncoded
    @POST("get_history.php")
    Call<ArrayList<PulledUnit>> getHistoryFromDatabase(
            @Field("uid") int uid,
            @Field("game") String game,
            @Field("banner") String banner
    );

    @FormUrlEncoded
    @POST("get_personal_stats.php")
    Call<UserStats> getPersonalStatsFromDatabase(
            @Field("uid") int uid,
            @Field("game") String game,
            @Field("banner") String banner
    );

    @FormUrlEncoded
    @POST("get_global_stats.php")
    Call<ArrayList<UserStats>> getGlobalStatsFromDatabase(
            @Field("game") String game,
            @Field("banner") String banner
    );

}
