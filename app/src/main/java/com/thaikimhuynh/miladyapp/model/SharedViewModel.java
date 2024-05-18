package com.thaikimhuynh.miladyapp.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<Boolean> sharedVariable = new MutableLiveData<>();

    public LiveData<Boolean> getSharedVariable() {
        return sharedVariable;
    }

    public void setSharedVariable(boolean value) {
        sharedVariable.setValue(value);
    }
}
