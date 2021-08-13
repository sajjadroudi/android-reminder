package com.mobiliha.eventsbadesaba.data.remote;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class MainInterceptor implements Interceptor {

    public static final String TAG = "MainInterceptor";

    private static final String VERSION_TYPE = "xb";
    private static final String VERSION_TYPE_DEF_VALUE = "1";

    private static final String VERSION_CODE = "xc";
    private static final String VERSION_CODE_DEF_VALUE = "71";

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();

        Headers headers = request.headers()
                .newBuilder()
                .add(VERSION_TYPE, VERSION_TYPE_DEF_VALUE)
                .add(VERSION_CODE, VERSION_CODE_DEF_VALUE)
                .build();

        Request newRequest = request.newBuilder()
                .headers(headers)
                .build();

        return chain.proceed(newRequest);
    }

}
