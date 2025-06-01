package com.example.easyflight;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FlightApi {
    @GET("flights")
    Call<FlightResponse> getFlights(
            @Query("access_key") String accessKey,
            @Query("dep_iata") String from,
            @Query("arr_iata") String to
    );
}
