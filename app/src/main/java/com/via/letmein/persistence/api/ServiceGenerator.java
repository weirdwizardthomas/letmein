package com.via.letmein.persistence.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A retrofit service generator to connect to the server's api.
 *
 * @author Tomas Koristka: 291129@via.dk
 */
public class ServiceGenerator {

    public static final int PORT = 8080;

    private static Retrofit getRetrofitInstance(String ipAddress) {
        String baseUrl = "http:/" + ipAddress + ":" + PORT + "/api/";

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create());
        return retrofitBuilder.build();
    }

    public static final String MOCKUP_ADDRESS = "https://9d04a36a-6449-4095-a647-8b62690a2680.mock.pstmn.io/api/";

    public static Api getMockupApi() {
        return new Retrofit.Builder()
                .baseUrl(MOCKUP_ADDRESS)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Api.class);
    }

    /**
     * Server's API to which requests will be sent.
     */

    public static Api getApi(String baseUrl) {
        return getRetrofitInstance(baseUrl).create(Api.class);
    }
}
