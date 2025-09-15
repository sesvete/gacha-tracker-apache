package com.sesvete.gachatrackerapache.helper;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {
    @FormUrlEncoded
    @POST("signup.php")
    Call<ResponseBody> signupUser(
            @Field("username") String username,
            @Field("password") String password
    );
}
