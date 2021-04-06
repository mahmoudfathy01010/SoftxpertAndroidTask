package com.elbaitdesign.softxpertandroidtask.network;

import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.elbaitdesign.softxpertandroidtask.util.Constants.API_ROOT;


public class RetrofitInstance {
    private static retrofit2.Retrofit retrofit = null;
    public static ApiService getService(){
        if(retrofit==null){
            retrofit=new retrofit2.Retrofit.Builder()
                    .baseUrl(API_ROOT)
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit.create(ApiService.class);

    }
}
