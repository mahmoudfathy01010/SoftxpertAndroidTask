package com.elbaitdesign.softxpertandroidtask.network;
import com.elbaitdesign.softxpertandroidtask.model.CarApiResponse;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("v1/cars")
    Observable<CarApiResponse> getCarsApiResponse(@Query("page")int page);
}
