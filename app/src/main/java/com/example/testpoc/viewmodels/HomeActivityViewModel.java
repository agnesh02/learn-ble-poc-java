package com.example.testpoc.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class HomeActivityViewModel extends AndroidViewModel {
    public HomeActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Integer> counter = new MutableLiveData(0);

    public void incrementCounter(){
        counter.postValue(counter.getValue()+1);
    }

    public void decrementCounter(){
        counter.postValue(counter.getValue()-1);
    }
}
