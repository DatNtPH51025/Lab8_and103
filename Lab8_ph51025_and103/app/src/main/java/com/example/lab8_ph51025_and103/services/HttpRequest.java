package com.example.lab8_ph51025_and103.services;

import static com.example.lab8_ph51025_and103.services.ApiServices.BASE_URL;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpRequest {
    private ApiServices requestInterface;
    private static final String SHOP_ID = "195466";
    private static final String TOKEN_GHN = "e7b05f95-ad33-11ef-accc-c6f6f22065b5";

    public HttpRequest() {
        requestInterface = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ApiServices.class);
    }

    public HttpRequest(String token) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @NonNull
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                Request request = chain.request().newBuilder().addHeader("Authorization","Bearer " + token).build();
                return chain.proceed(request);
            }
        });
        requestInterface = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build().create(ApiServices.class);
    }




    public ApiServices callAPI() {
        return requestInterface;
    }
}
