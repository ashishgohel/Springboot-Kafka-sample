package com.daimler.ingestion.service.ingestionengine.config;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class RetryInterceptor implements Interceptor {

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();

        //Invoking the request
        Response response =  chain.proceed(request);
        int count=0;
        while(!response.isSuccessful() && count<3){
            try{
                Thread.sleep(1000);
            }catch (Exception e){
                e.printStackTrace();
            }
            response = chain.proceed(request);
        }
        return response;
    }
}
