package com.via.letmein.persistence.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.via.letmein.persistence.api.Api.ADDRESS_PORT_DELIMITER;
import static com.via.letmein.persistence.api.Api.API_PATH;
import static com.via.letmein.persistence.api.Api.HTTP;
import static com.via.letmein.persistence.api.Api.PORT;

/**
 * A retrofit service generator to connect to the server's api.
 *
 * @author Tomas Koristka: 291129@via.dk
 */
public class ServiceGenerator {

    private static Retrofit instance;

    private static synchronized Retrofit getRetrofitInstance(String ipAddress) {

        if (instance == null) {
            String baseUrl = HTTP + ipAddress + ADDRESS_PORT_DELIMITER + PORT + API_PATH;

            Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create());
            instance = retrofitBuilder.build();
        }
        return instance;
    }

    /**
     * Server's API to which requests will be sent.
     */
    public static synchronized Api getApi(String baseUrl) {
        return getRetrofitInstance(baseUrl).create(Api.class);
    }
}
