package com.thaikimhuynh.miladyapp.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.Closeable;

public class SharedViewModel extends ViewModel {


    private MutableLiveData<String> sharedVariable = new MutableLiveData<>();

    public LiveData<String> getSharedVariable() {
        return sharedVariable;
    }

    public void setSharedVariable(String value) {
        sharedVariable.setValue(value);
    }

}
