package com.example.easyflight;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FlightApiService {
    private static final String BASE_URL = "http://api.aviationstack.com/v1/";
    private static Retrofit retrofit = null;

    public static FlightApi getApi() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(FlightApi.class);
    }
}
