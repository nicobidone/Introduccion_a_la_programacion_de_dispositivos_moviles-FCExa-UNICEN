package com.unicen.tandilrecicla.data.remote;

import com.unicen.tandilrecicla.data.model.Recycling;

import io.reactivex.Flowable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RequestApi {

    @GET("users/{id}/total/")
    Flowable<ResponseBody> makeQuery(@Path("id") String id);

    @POST("users/{id}/recycling/")
    Flowable<Recycling> savePost(@Path("id") String id, @Body Recycling recycling);
}

