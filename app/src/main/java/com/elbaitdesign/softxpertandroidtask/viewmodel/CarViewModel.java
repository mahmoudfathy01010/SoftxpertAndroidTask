package com.elbaitdesign.softxpertandroidtask.viewmodel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.elbaitdesign.softxpertandroidtask.model.CarApiResponse;
import com.elbaitdesign.softxpertandroidtask.network.RetrofitInstance;
import com.elbaitdesign.softxpertandroidtask.util.Constants;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CarViewModel extends ViewModel {

    private MutableLiveData<CarApiResponse> _carApiResponse = new MutableLiveData<>();
    public LiveData<CarApiResponse> carApiResponse = _carApiResponse;

    private MutableLiveData<String> _error = new MutableLiveData<>();
    public LiveData<String> error = _error;

    private int pageCounter = Constants.PAGE_COUNTER_INITIAL_STATE;

    public void getCarApiResponse (Integer pageNumber){
        Observable<CarApiResponse> observable = RetrofitInstance.getService().getCarsApiResponse(pageNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        observable.subscribe( new Observer<CarApiResponse>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onNext(@NonNull CarApiResponse carApiResponse) {
                if(carApiResponse!=null){
                    _carApiResponse.setValue(carApiResponse);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                _error.setValue(e.getMessage());
            }

            @Override
            public void onComplete() {
            }
        });
    }

    public void resetPageCounter(){
        pageCounter=Constants.PAGE_COUNTER_INITIAL_STATE;
    }

    public void getData(){
        pageCounter++;
       getCarApiResponse(pageCounter);
    }


}
