package com.dream4it.youquba.net;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by meiming on 17-2-2.
 */

public class NetManager {
    private static final int DEFAULT_TIMEOUT = 10;

    private static class SingletonHolder{
        private static final NetManager INSTANCE = new NetManager();
    }

    public static NetManager getInstance(){
        return SingletonHolder.INSTANCE;
    }

    private NetManager(){}


    public <S> S create(Class<S> service, OkHttpClient client,
                        Converter.Factory converterFactory,
                        CallAdapter.Factory callAdapterFactory,
                        String baseUrl){
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .addConverterFactory(converterFactory)
                .addCallAdapterFactory(callAdapterFactory)
                .baseUrl(baseUrl)
                .build();
        return retrofit.create(service);
    }

    public <S> S createGson(Class<S> service) {
        return create(service, getOkHttpClient(), GsonConverterFactory.create(), RxJavaCallAdapterFactory.create(), getBaseUrl(service));
    }

    public <S> S createScalars(Class<S> service){
        return create(service, getOkHttpClient(), ScalarsConverterFactory.create(), RxJavaCallAdapterFactory.create(), getBaseUrl(service));
    }

    private <S> String getBaseUrl(Class<S> service){
        try {
            Field field = service.getField("BASE_URL");
            return (String) field.get(service);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private OkHttpClient getOkHttpClient() {
        //配置超时拦截器
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        //配置log打印拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(loggingInterceptor);
        return builder.build();
    }
}
