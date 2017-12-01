package hu.ait.apod.network;

import hu.ait.apod.data.APODResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApodAPI {

    @GET("apod")
    Call<APODResult> getAPOD(@Query("date") String date, @Query("hd") String hd,
                             @Query("api_key") String API_KEY);
}