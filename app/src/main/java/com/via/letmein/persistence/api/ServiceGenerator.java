package com.via.letmein.persistence.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A retrofit service generator to connect to the server's api.
 *
 * @author Tomas Koristka: 291129@via.dk
 */
public class ServiceGenerator {

    private static final int PORT = 8080;

    private static Retrofit getRetrofitInstance(String ipAddress) {
        String baseUrl = "http:/" + ipAddress + ":" + PORT + "/api/";

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create());
        return retrofitBuilder.build();
    }

    /**
     * Server's API to which requests will be sent.
     */

    public static Api getApi(String baseUrl) {
        return getRetrofitInstance(baseUrl).create(Api.class);
    }
}
