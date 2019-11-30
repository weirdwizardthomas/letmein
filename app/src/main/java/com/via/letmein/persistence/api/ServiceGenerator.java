package com.via.letmein.persistence.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A retrofit service generator to connect to the server's api.
 *
 * @author Tomas Koristka: 291129@via.dk
 */
public class ServiceGenerator {
    //Server's api base URL
    private static final String BASE_URL = "https://9d04a36a-6449-4095-a647-8b62690a2680.mock.pstmn.io/api/";

    private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = retrofitBuilder.build();

    /**
     * Server's API to which requests will be sent.
     */
    private static Api api = retrofit.create(Api.class);

    public static Api getApi() {
        return api;
    }
}
